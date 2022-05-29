package com.cml.framework.processor.sample.storage;

import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;

/**
 * 数据库方式存储方案
 */
public class RdsProcessorTaskModelRepository implements ProcessorTaskModelRepository {

    @Override
    public ProcessorTaskDomainModel find(String processorType, String requestId) {
        return null;
    }

    @Override
    public void save(ProcessorTaskDomainModel taskDomainModel) {

    }

    @Override
    public void modify(ProcessorTaskDomainModel taskDomainModel) {

    }
}
