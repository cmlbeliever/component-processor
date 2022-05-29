package com.cml.framework.processor.sample.processor;

import com.cml.framework.processor.core.flow.FlowTaskHolder;
import com.cml.framework.processor.core.impl.TaskDrivenFlowProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DefaultFlowProcessor  extends TaskDrivenFlowProcessor {

    @Autowired
    private FlowTaskHolder flowTaskHolder;

    @Override
    public FlowTaskHolder getFlowTaskHolder() {
        return this.flowTaskHolder;
    }
}
