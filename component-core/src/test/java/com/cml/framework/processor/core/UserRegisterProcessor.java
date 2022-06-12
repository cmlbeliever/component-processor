package com.cml.framework.processor.core;

import com.cml.framework.processor.core.enums.ProcessorTypeEnums;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.core.flowtask.enums.FlowTaskEnums;
import com.cml.framework.processor.core.impl.TaskDrivenFlowProcessor;
import com.cml.framework.processor.core.request.UserRegisterRequest;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:22:00
 */
public class UserRegisterProcessor extends TaskDrivenFlowProcessor {

    @Override
    public String processorType() {
        return ProcessorTypeEnums.USER_REGISTER.type();
    }

    @Override
    protected ProcessorContext buildContext(ProcessorRequest request) {
        UserRegisterRequest userRegisterRequest = (UserRegisterRequest) request;
        ProcessorContext processorContext = new ProcessorContext();
        processorContext.putExtra("error", userRegisterRequest.isError() ? "error" : null);
        return processorContext;
    }

    @Override
    protected FlowTaskType[] flowTasks() {
        return new FlowTaskType[]{
                FlowTaskEnums.TASK_STEP1,
                FlowTaskEnums.TASK_STEP2,
                FlowTaskEnums.TASK_STEP3,
                FlowTaskEnums.TASK_STEP4,
        };
    }
}
