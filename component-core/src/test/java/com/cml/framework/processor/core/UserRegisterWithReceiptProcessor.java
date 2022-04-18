package com.cml.framework.processor.core;

import com.cml.framework.processor.core.enums.ProcessorTypeEnums;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.core.flowtask.enums.FlowTaskEnums;
import com.cml.framework.processor.core.impl.TaskDrivenFlowProcessor;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:22:00
 */
public class UserRegisterWithReceiptProcessor extends TaskDrivenFlowProcessor {

    @Override
    public ProcessorType processorType() {
        return ProcessorTypeEnums.USER_REGISTER2;
    }

    @Override
    protected ProcessorContext buildContext(ProcessorRequest request) {
        return new ProcessorContext();
    }

    @Override
    protected FlowTaskType[] flowTasks() {
        return new FlowTaskType[]{
                FlowTaskEnums.TASK_STEP1,
                FlowTaskEnums.TASK_WAIT_RECEIPT_STEP2,
                FlowTaskEnums.TASK_STEP3,
                FlowTaskEnums.TASK_STEP4,
        };
    }
}
