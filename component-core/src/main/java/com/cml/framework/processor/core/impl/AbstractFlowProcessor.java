package com.cml.framework.processor.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cml.framework.processor.core.*;
import com.cml.framework.processor.core.enums.FlowProcessorExtraKeys;
import com.cml.framework.processor.core.enums.ProcessorStatus;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.core.model.ProcessorReceipt;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 20:49:00
 */
public abstract class AbstractFlowProcessor<T> implements FlowProcessor {

    /**
     * spring 环境替换
     */
    private ProcessorTaskModelRepository processorTaskModelRepository;

    @Override
    public ProcessResult process(ProcessorRequest request) {

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

    @Override
    public ProcessResult receipt(ProcessorReceipt processorReceipt) {

        ProcessorTaskDomainModel taskDomainModel = processorTaskModelRepository.findById(processorReceipt.getProcessorId());

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
        //TODO 任务类型+流水号 唯一
        System.out.println("releaseLock: " + taskDomainModel.getProcessorTaskType() + taskDomainModel.getRequestId());
    }

    protected void tryLock(ProcessorTaskDomainModel taskDomainModel) {
        //TODO 任务类型+流水号 唯一
        System.out.println("lock:" + taskDomainModel.getProcessorTaskType() + taskDomainModel.getRequestId());
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
                taskDomainModel.setRetryAt(new Date());//TODO 添加时间
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

        ProcessorTaskDomainModel taskDomainModel = processorTaskModelRepository.find(this.processorType(), request.getOutBizId(), request.getRequestId());

        if (null == taskDomainModel) {
            taskDomainModel = ProcessorTaskDomainModel.builder()
                    .requestId(request.getRequestId())
                    .createdAt(new Date())
                    .processorTaskType(this.processorType().type())
                    .status(ProcessorStatus.PROCESSING)
                    .retryAt(new Date())
                    .retryTimes(0)
                    .outBizId(request.getOutBizId())
                    .updatedAt(new Date())
                    .extra(new HashMap<>())
                    .build();
            taskDomainModel.putExtra(FlowProcessorExtraKeys.REQUEST.getKey(), JSON.toJSONString(request));
            processorTaskModelRepository.save(taskDomainModel);
        }

        System.out.println("-----buildTaskDomainModel taskmodel 获取成功------" + taskDomainModel);

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
