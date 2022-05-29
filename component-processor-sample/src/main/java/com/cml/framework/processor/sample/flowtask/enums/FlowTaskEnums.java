package com.cml.framework.processor.sample.flowtask.enums;

import com.cml.framework.processor.core.flow.FlowTaskType;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:25:00
 */
public enum FlowTaskEnums implements FlowTaskType {
    TASK_STEP1("taskStep1"),
    TASK_STEP2("taskStep2"),
    TASK_WAIT_RECEIPT_STEP2("TASK_WAIT_RECEIPT_STEP2"),
    TASK_STEP3("taskStep3"),
    TASK_STEP4("taskStep4"),
    ;
    private String type;

    FlowTaskEnums(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return this.type;
    }
}
