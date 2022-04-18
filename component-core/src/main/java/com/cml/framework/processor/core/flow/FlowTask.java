package com.cml.framework.processor.core.flow;

import com.cml.framework.processor.core.ProcessResult;
import com.cml.framework.processor.core.ProcessorContext;
import com.cml.framework.processor.core.ProcessorRequest;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 21:00:00
 */
public interface FlowTask<T> {

    FlowTaskType taskType();

    ProcessResult execute(ProcessorRequest request, ProcessorContext processorContext);

    default ProcessResult receipt(T receiptData, ProcessorContext processorContext) {
        return ProcessResult.success();
    }

    ;
}
