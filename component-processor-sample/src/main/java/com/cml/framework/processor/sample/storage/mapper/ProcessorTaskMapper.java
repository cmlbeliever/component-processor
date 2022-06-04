package com.cml.framework.processor.sample.storage.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProcessorTaskMapper extends JpaRepository<ProcessorTaskModelDO, Long> {

    ProcessorTaskModelDO findByTaskTypeAndRequestId(String taskType, String requestId);
}
