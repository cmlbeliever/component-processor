package com.cml.framework.processor.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理器上下文
 */
public class ProcessorContainer {
    private ConcurrentHashMap<String, FlowProcessor> flowProcessorHolder = new ConcurrentHashMap<>();

    public void register(FlowProcessor processor) {
        flowProcessorHolder.putIfAbsent(processor.processorType(), processor);
    }

    public FlowProcessor take(String processorType) {
        return this.flowProcessorHolder.get(processorType);
    }
}
