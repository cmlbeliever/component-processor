package com.cml.framework.processor.sample.flowtask;

import com.cml.framework.processor.core.ProcessResult;
import com.cml.framework.processor.core.ProcessorContext;
import com.cml.framework.processor.core.ProcessorRequest;
import com.cml.framework.processor.core.flow.FlowTask;
import com.cml.framework.processor.core.flow.FlowTaskType;
import com.cml.framework.processor.sample.flowtask.enums.FlowTaskEnums;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 22:25:00
 */
public class Step2WaitReceiptFlowTask implements FlowTask<String> {
    @Override
    public FlowTaskType taskType() {
        return FlowTaskEnums.TASK_WAIT_RECEIPT_STEP2;
    }

    @Override
    public ProcessResult execute(ProcessorRequest request, ProcessorContext processorContext) {
        System.out.println("\tStep2WaitReceiptFlowTask execute");
        return ProcessResult.success(true);
    }

    @Override
    public ProcessResult receipt(String receiptData, ProcessorContext processorContext) {
        System.out.println("\tStep2WaitReceiptFlowTask receipt" + receiptData);
        return ProcessResult.success();
    }

}
