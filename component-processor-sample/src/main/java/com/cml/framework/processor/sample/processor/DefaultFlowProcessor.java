package com.cml.framework.processor.sample.processor;

import com.cml.framework.processor.core.flow.FlowTaskHolder;
import com.cml.framework.processor.core.impl.TaskDrivenFlowProcessor;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class DefaultFlowProcessor extends TaskDrivenFlowProcessor {

    @Autowired
    private FlowTaskHolder flowTaskHolder;

    @Autowired
    private ProcessorTaskModelRepository processorTaskModelRepository;

    @PostConstruct
    public void init() {
        this.setFlowTaskHolder(flowTaskHolder);
        this.setProcessorTaskModelRepository(processorTaskModelRepository);
    }

}
