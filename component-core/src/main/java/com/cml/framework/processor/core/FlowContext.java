package com.cml.framework.processor.core;

import com.cml.framework.processor.core.flow.FlowTaskType;
import lombok.Data;

/**
 * 流程上下文
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 20:43:00
 */
@Data
public class FlowContext {
    /**
     * 流程类型
     */
    private ProcessorType processorType;

    /**
     * 当前节点
     */
    private String currentFlow;

    /**
     * 当前任务
     */
    private FlowTaskType currentTask;
}
