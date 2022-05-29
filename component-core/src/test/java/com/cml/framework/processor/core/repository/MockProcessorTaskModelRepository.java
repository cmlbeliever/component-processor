package com.cml.framework.processor.core.repository;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.ProcessorType;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;

import java.util.concurrent.ConcurrentHashMap;

public class MockProcessorTaskModelRepository implements ProcessorTaskModelRepository {

    private ConcurrentHashMap<String, ProcessorTaskDomainModel> storage = new ConcurrentHashMap<>();

    public ProcessorTaskDomainModel find(String processorType, String requestId) {
        return storage.get(key(processorType, requestId));
    }

    private String key(String processorType, String requestId) {
        return processorType + "_" + requestId;
    }

    public void save(ProcessorTaskDomainModel taskDomainModel) {
        storage.put(key(taskDomainModel), taskDomainModel);
    }

    private String key(ProcessorTaskDomainModel taskDomainModel) {
        return key(taskDomainModel.getProcessorTaskType(), taskDomainModel.getRequestId());
    }

    public void modify(ProcessorTaskDomainModel taskDomainModel) {
        this.storage.put(key(taskDomainModel), taskDomainModel);
    }

}
