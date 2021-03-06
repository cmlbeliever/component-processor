package com.cml.framework.processor.core.repository;

import com.cml.framework.processor.core.ProcessorType;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;

/**
 * @Description
 * @createTime 2021年12月08日 11:14:00
 */
public interface ProcessorTaskModelRepository {

    /**
     * @param processorType
     * @param requestId
     * @return
     */
    ProcessorTaskDomainModel find(String processorType, String requestId);

    /**
     * 保存任务
     *
     * @param taskDomainModel
     */
    void save(ProcessorTaskDomainModel taskDomainModel);

    /**
     * 修改任务
     *
     * @param taskDomainModel
     */
    void modify(ProcessorTaskDomainModel taskDomainModel);
}
