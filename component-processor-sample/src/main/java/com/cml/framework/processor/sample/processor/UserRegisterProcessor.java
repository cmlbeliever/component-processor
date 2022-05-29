package com.cml.framework.processor.sample.processor;

import com.cml.framework.processor.core.ProcessorContext;
import com.cml.framework.processor.core.ProcessorRequest;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.sample.flowtask.enums.FlowTaskEnums;
import com.cml.framework.processor.sample.processor.enums.ProcessorTypeEnums;
import org.springframework.stereotype.Component;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:22:00
 */
@Component
public class UserRegisterProcessor extends DefaultFlowProcessor {

    @Override
    public String processorType() {
        return ProcessorTypeEnums.USER_REGISTER.type();
    }

    @Override
    protected ProcessorContext buildContext(ProcessorRequest request) {
        return new ProcessorContext();
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
