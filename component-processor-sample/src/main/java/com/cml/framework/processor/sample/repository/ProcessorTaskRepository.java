package com.cml.framework.processor.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessorTaskRepository extends JpaRepository<ProcessorTaskModelDO, Long> {
}
