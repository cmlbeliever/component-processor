package com.cml.framework.processor.core.repository;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.ProcessorType;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;

import java.util.concurrent.ConcurrentHashMap;

public class MockProcessorTaskModelRepository implements ProcessorTaskModelRepository {

    private ConcurrentHashMap<String, ProcessorTaskDomainModel> storage = new ConcurrentHashMap<>();

    public ProcessorTaskDomainModel find(ProcessorType processorType, String outBizId, String requestId) {
        return storage.get(key(processorType.type(), outBizId, requestId));
    }

    private String key(String processorType, String outBizId, String requestId) {
        return processorType + "_" + outBizId + "_" + requestId;
    }

    public void save(ProcessorTaskDomainModel taskDomainModel) {
        storage.put(key(taskDomainModel), taskDomainModel);
    }

    private String key(ProcessorTaskDomainModel taskDomainModel) {
        return key(taskDomainModel.getProcessorTaskType(), taskDomainModel.getOutBizId(), taskDomainModel.getRequestId());
    }

    public void modify(ProcessorTaskDomainModel taskDomainModel) {
        this.storage.put(key(taskDomainModel), taskDomainModel);
    }

    public ProcessorTaskDomainModel findById(Long processorId) {
        return storage.values()
                .stream()
                .filter(t -> t.getId().equals(processorId.toString()))
                .findFirst()
                .orElse(null);
    }
}
