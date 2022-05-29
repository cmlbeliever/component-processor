package com.cml.framework.processor.core.impl;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.ProcessResult;
import com.cml.framework.processor.core.ProcessorContext;
import com.cml.framework.processor.core.ProcessorRequest;
import com.cml.framework.processor.core.enums.ProcessorStatus;
import com.cml.framework.processor.core.ex.ProcessorStatusIllegalException;
import com.cml.framework.processor.core.flow.FlowTask;
import com.cml.framework.processor.core.flow.FlowTaskHolder;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.core.model.ProcessorReceipt;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 任务驱动处理器
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 21:33:00
 */
@Slf4j
public abstract class TaskDrivenFlowProcessor extends AbstractFlowProcessor {

    @Setter
    private FlowTaskHolder flowTaskHolder = new FlowTaskHolder();

    @Override
    protected ProcessResult doReceipt(ProcessorRequest request, ProcessorTaskDomainModel processorModel, ProcessorContext processorContext, ProcessorReceipt receipt) {
        // 获取所有节点
        FlowTaskType[] flowTasks = flowTasks();

        if (flowTasks == null || flowTasks.length == 0) {
            throw new IllegalArgumentException("task不能为空");
        }

        String currentTask = Optional.ofNullable(processorModel.getCurrentFlow()).orElse(flowTasks[0].type());

        int currentFlowIndex = indexOfCurrentFlow(flowTasks, currentTask);
        FlowTaskType flowTaskType = flowTasks[currentFlowIndex];

        FlowTask flowTask = Optional.ofNullable(flowTaskHolder.take(flowTaskType)).orElseThrow(() -> new IllegalArgumentException("flowTask 不存在"));

        ProcessResult result;
        try {
            // 执行回调任务，只会在一个任务中第一次执行
            result = flowTask.receipt(receipt.getData(), processorContext);
        } catch (Exception e) {
            result = handleFlowException(e);
        }

        log.info("<<<task={} receipt 执行结果：{}", flowTaskType.type(), JSON.toJSONString(result));

        if (result.isSuccess()) {
            boolean hasNextFlow = currentFlowIndex < flowTasks.length - 1;
            //设置下一个流程
            if (hasNextFlow) {
                processorModel.setCurrentFlow(flowTasks[currentFlowIndex + 1].type());
                processorModel.startProcess();
                modifyProcessorTask(processorModel);
                //继续执行下个任务
                return doProcess(request, processorModel, processorContext);
            }

        } else {
            result.setWaitReceipt(true);
        }

        return result;

    }

    @Override
    protected ProcessResult doProcess(ProcessorRequest request, ProcessorTaskDomainModel processorModel, ProcessorContext processorContext) {

        // 获取所有节点
        FlowTaskType[] flowTasks = flowTasks();

        if (flowTasks == null || flowTasks.length == 0) {
            throw new IllegalArgumentException("task不能为空");
        }

        String currentTask = Optional.ofNullable(processorModel.getCurrentFlow()).orElse(flowTasks[0].type());

        int currentFlowIndex = indexOfCurrentFlow(flowTasks, currentTask);

        for (int i = currentFlowIndex; i < flowTasks.length; i++) {

            FlowTaskType flowTaskType = flowTasks[i];

            FlowTask flowTask = Optional.ofNullable(flowTaskHolder.take(flowTaskType)).orElseThrow(() -> new IllegalArgumentException("flowTask 不存在"));

            //更新当前节点到DB 还可以优化
            processorModel.setCurrentFlow(flowTaskType.type());
            processorModel.startProcess();
            modifyProcessorTask(processorModel);

            ProcessResult flowTaskResult;

            try {
                // 执行流程任务
                flowTaskResult = flowTask.execute(request, processorContext);
            } catch (Exception e) {
                flowTaskResult = handleFlowException(e);
            }

            log.info("task={} 执行结果：{}", flowTaskType.type(), JSON.toJSONString(flowTaskResult));

            //失败处理
            if (!flowTaskResult.isSuccess() || flowTaskResult.isWaitReceipt()) {
                return flowTaskResult;
            }
        }

        return ProcessResult.success();
    }


    /**
     * 节点异常处理
     *
     * @param e
     * @return
     */
    protected ProcessResult handleFlowException(Exception e) {
        ProcessResult processResult = ProcessResult.fail();
        processResult.setErrorMsg(e.getMessage());
        processResult.setRetry(true);
        return processResult;
    }


    /**
     * 获取当前任务的节点，第一次进入和回执时候用于跳过已处理的任务
     *
     * @param flowTasks
     * @param currentTask
     * @return
     */
    private int indexOfCurrentFlow(FlowTaskType[] flowTasks, String currentTask) {

        for (int i = 0; i < flowTasks.length; i++) {
            if (flowTasks[i].type().equals(currentTask)) {
                return i;
            }
        }
        throw new IllegalArgumentException("task不能为空");
    }

    public FlowTaskHolder getFlowTaskHolder() {
        return flowTaskHolder;
    }
}
