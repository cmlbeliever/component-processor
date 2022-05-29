package com.cml.framework.processor.sample.configutaion;

import com.cml.framework.processor.core.FlowProcessor;
import com.cml.framework.processor.core.ProcessorContainer;
import com.cml.framework.processor.core.flow.FlowTask;
import com.cml.framework.processor.core.flow.FlowTaskHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务流自动配置
 */
@Component
@Configuration
public class ProcessorConfiguration {

    @Bean
    public FlowTaskHolder flowTaskHolder(List<FlowTask> flowTasks) {
        FlowTaskHolder flowTaskHolder = new FlowTaskHolder();
        flowTasks.forEach(flowTaskHolder::register);
        return flowTaskHolder;
    }

    @Bean
    public ProcessorContainer processorContainer(List<FlowProcessor> flowProcessors) {
        ProcessorContainer processorContainer = new ProcessorContainer();
        flowProcessors.forEach(processorContainer::register);
        return processorContainer;
    }

}
