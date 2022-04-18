package com.cml.framework.processor.core;

import com.cml.framework.processor.core.flow.FlowTaskType;

/**
 * 流程上下文
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 20:43:00
 */
public class FlowContext {
    /**
     * 流程类型
     */
    private ProcessorType processorType;

    /**
     * 当前节点
     */
    private String currentFlow;

    private FlowTaskType currentTask;

    public ProcessorType getProcessorType() {
        return processorType;
    }

    public void setProcessorType(ProcessorType processorType) {
        this.processorType = processorType;
    }

    public String getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(String currentFlow) {
        this.currentFlow = currentFlow;
    }

    public FlowTaskType getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(FlowTaskType currentTask) {
        this.currentTask = currentTask;
    }
}
