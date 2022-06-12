package com.cml.framework.processor.sample.flowtask;

import com.cml.framework.processor.core.ProcessResult;
import com.cml.framework.processor.core.ProcessorContext;
import com.cml.framework.processor.core.ProcessorRequest;
import com.cml.framework.processor.core.flow.FlowTask;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.sample.flowtask.enums.FlowTaskEnums;
import org.springframework.stereotype.Component;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 22:25:00
 */
@Component
public class Step2FlowTask implements FlowTask {
    @Override
    public FlowTaskType taskType() {
        return FlowTaskEnums.TASK_STEP2;
    }

    @Override
    public ProcessResult execute(ProcessorRequest request, ProcessorContext processorContext) {
        System.out.println("\tflowTask2 execute");
        String error = processorContext.take("error");
        if ("error".equals(error)) {
            return ProcessResult.fail();
        }
        if ("retry".equals(error)) {
            throw new IllegalArgumentException("可以重试的异常");
        }
        return ProcessResult.success();
    }

}
