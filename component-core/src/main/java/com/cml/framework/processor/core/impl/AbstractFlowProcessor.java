package com.cml.framework.processor.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cml.framework.processor.core.*;
import com.cml.framework.processor.core.enums.FlowProcessorExtraKeys;
import com.cml.framework.processor.core.enums.ProcessorStatus;
import com.cml.framework.processor.core.ex.RequestValidatelException;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.core.model.ProcessorReceipt;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 20:49:00
 */
@Slf4j
public abstract class AbstractFlowProcessor<T> implements FlowProcessor {

    /**
     * 默认重试延迟时间,单位 s
     */
    private static final long DEFAULT_RETRY_DELAY_TIME_IN_SECONDS = 10;
    /**
     * spring 环境替换
     */
    private ProcessorTaskModelRepository processorTaskModelRepository;

    @Override
    public ProcessResult process(ProcessorRequest request) {

        validateRequest(request);

        // 组装上下文
        ProcessorTaskDomainModel taskDomainModel = buildTaskDomainModel(request);

        if (taskDomainModel.isFinish()) {
            // 幂等返回
            return JSON.parseObject(taskDomainModel.takeExtra(FlowProcessorExtraKeys.RESULT.getKey()), ProcessResult.class);
        }

        if (!taskDomainModel.canExecute()) {
            throw new IllegalStateException("当前状态不能执行");
        }

        return execute(request, taskDomainModel, processorContext -> doProcess(request, taskDomainModel, processorContext));
    }

    private void validateRequest(ProcessorRequest request) {
        if (request.getOutBizId() == null) {
            throw new RequestValidatelException("outBizId非空");
        }
        if (request.getProcessorType() == null) {
            throw new RequestValidatelException("processorType非空");
        }
        if (request.getRequestId() == null) {
            throw new RequestValidatelException("requestId非空");
        }
    }

    @Override
    public ProcessResult receipt(ProcessorReceipt processorReceipt) {

        validateReceiptRequest(processorReceipt);

        ProcessorTaskDomainModel taskDomainModel = processorTaskModelRepository.find(processorReceipt.getProcessorType(), processorReceipt.getRequestId());

        if (null == taskDomainModel) {
            throw new IllegalArgumentException("任务不存在");
        }

        if (!taskDomainModel.waitReceipt()) {
            throw new IllegalStateException("非待回执状态");
        }

        String requestStr = taskDomainModel.takeExtra(FlowProcessorExtraKeys.REQUEST.getKey());
        ProcessorRequest request = JSON.parseObject(requestStr, ProcessorRequest.class);

        return execute(request, taskDomainModel, context -> doReceipt(request, taskDomainModel, context, processorReceipt));
    }

    private void validateReceiptRequest(ProcessorReceipt processorReceipt) {
        if (processorReceipt.getProcessorType() == null) {
            throw new RequestValidatelException("processorType非空");
        }
        if (processorReceipt.getRequestId() == null) {
            throw new RequestValidatelException("requestId非空");
        }
    }

    protected abstract ProcessResult doReceipt(ProcessorRequest request, ProcessorTaskDomainModel taskDomainModel, ProcessorContext processorContext, ProcessorReceipt<T> receipt);


    private ProcessResult execute(ProcessorRequest request, ProcessorTaskDomainModel taskDomainModel, Function<ProcessorContext, ProcessResult> function) {
        tryLock(taskDomainModel);

        // 类型支持
        ProcessorContext processorContext = buildContext(request);

        ProcessResult result = null;
        Exception ex = null;
        try {
            //上下文处理
            result = function.apply(processorContext);
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            afterProcess(taskDomainModel, result, ex);
            releaseLock(taskDomainModel);
        }

        return result;
    }

    protected void releaseLock(ProcessorTaskDomainModel taskDomainModel) {
        log.info("releaseLock processorType={} requestId={} 默认不处理", taskDomainModel.getProcessorTaskType(), taskDomainModel.getRequestId());
    }

    protected void tryLock(ProcessorTaskDomainModel taskDomainModel) {
        log.info("tryLock processorType={} requestId={} 默认不处理", taskDomainModel.getProcessorTaskType(), taskDomainModel.getRequestId());
    }

    protected abstract FlowTaskType[] flowTasks();


    protected abstract ProcessResult doProcess(ProcessorRequest request, ProcessorTaskDomainModel taskDomainModel, ProcessorContext processorContext);

    /**
     * 构造业务上下文
     *
     * @param request
     * @return
     */
    protected abstract ProcessorContext buildContext(ProcessorRequest request);


    protected void afterProcess(ProcessorTaskDomainModel taskDomainModel, ProcessResult result, Exception ex) {

        ProcessorStatus status = calculateStatus(result, ex);
        switch (status) {
            case RETRY:
            case PROCESSING:
            case INIT:
                taskDomainModel.setRetryAt(takeNextRetryTime(taskDomainModel, result));//TODO 添加时间
                taskDomainModel.setRetryTimes(taskDomainModel.getRetryTimes() + 1);
                break;
        }
        taskDomainModel.setStatus(status);
        taskDomainModel.putExtra(FlowProcessorExtraKeys.BIZ_EXTRA.getKey(), JSON.toJSONString(result.getBizExtra()));
        taskDomainModel.putExtra(FlowProcessorExtraKeys.RESULT.getKey(), JSON.toJSONString(result));
        if (null != ex) {
            taskDomainModel.putExtra(FlowProcessorExtraKeys.EX.getKey(), ex.getMessage());
        }
        processorTaskModelRepository.modify(taskDomainModel);
    }

    /**
     * 获取下次执行时间，默认10s
     *
     * @param taskDomainModel
     * @param result
     * @return
     */
    private Date takeNextRetryTime(ProcessorTaskDomainModel taskDomainModel, ProcessResult result) {
        LocalDateTime nextDateTime = LocalDateTime.now().plusSeconds(DEFAULT_RETRY_DELAY_TIME_IN_SECONDS);
        return Date.from(nextDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    private ProcessorStatus calculateStatus(ProcessResult result, Exception ex) {
        if (result.isSuccess()) {
            if (result.isWaitReceipt()) {
                return ProcessorStatus.WAIT_RECEIPT;
            }
            return ProcessorStatus.SUCCESS;
        }

        if (result.isRetry()) {
            return ProcessorStatus.RETRY;
        }

        return ProcessorStatus.FAIL;
    }


    /**
     * 前置业务逻辑
     *
     * @param request
     */
    protected ProcessorTaskDomainModel buildTaskDomainModel(ProcessorRequest request) {

        ProcessorTaskDomainModel taskDomainModel = processorTaskModelRepository.find(this.processorType(), request.getRequestId());

        if (null == taskDomainModel) {
            taskDomainModel = ProcessorTaskDomainModel.builder()
                    .requestId(request.getRequestId())
                    .createdAt(new Date())
                    .processorTaskType(this.processorType())
                    .status(ProcessorStatus.INIT)
                    .retryAt(new Date())
                    .retryTimes(0)
                    .outBizId(request.getOutBizId())
                    .updatedAt(new Date())
                    .extra(new HashMap<>())
                    .build();
            taskDomainModel.putExtra(FlowProcessorExtraKeys.REQUEST.getKey(), JSON.toJSONString(request, SerializerFeature.WriteClassName, SerializerFeature.WriteEnumUsingName));
            processorTaskModelRepository.save(taskDomainModel);
        }

        return taskDomainModel;
    }

    protected void modifyProcessorTask(ProcessorTaskDomainModel processorModel) {
        processorTaskModelRepository.modify(processorModel);
    }

    /**
     * 自定义仓储层
     *
     * @param processorTaskModelRepository
     */
    public void setProcessorTaskModelRepository(ProcessorTaskModelRepository processorTaskModelRepository) {
        this.processorTaskModelRepository = processorTaskModelRepository;
    }
}
