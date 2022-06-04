package com.cml.framework.processor.sample.storage;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.enums.ProcessorStatus;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;
import com.cml.framework.processor.sample.storage.mapper.ProcessorTaskMapper;
import com.cml.framework.processor.sample.storage.mapper.ProcessorTaskModelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Optional;

/**
 * 数据库方式存储方案
 */
@Repository
public class RdsProcessorTaskModelRepository implements ProcessorTaskModelRepository {

    @Autowired
    private ProcessorTaskMapper processorTaskMapper;

    @Override
    public ProcessorTaskDomainModel find(String processorType, String requestId) {
        ProcessorTaskModelDO processorTaskModelDO = processorTaskMapper.findByTaskTypeAndRequestId(processorType, requestId);
        return convertFrom(processorTaskModelDO);
    }

    private ProcessorTaskDomainModel convertFrom(ProcessorTaskModelDO processorTaskModelDO) {
        if (null == processorTaskModelDO) {
            return null;
        }
        ProcessorTaskDomainModel model = new ProcessorTaskDomainModel();
        model.setCurrentFlow(processorTaskModelDO.getCurrentFlow());
        model.setRetryAt(processorTaskModelDO.getRetryAt());
        model.setRetryTimes(processorTaskModelDO.getRetryTimes());
        model.setStatus(ProcessorStatus.valueOf(processorTaskModelDO.getStatus()));
        model.setProcessorTaskType(processorTaskModelDO.getTaskType());
        model.setRequestId(processorTaskModelDO.getRequestId());
        model.setCreatedAt(processorTaskModelDO.getCreatedAt());
        model.setUpdatedAt(processorTaskModelDO.getUpdatedAt());
        Optional.ofNullable(processorTaskModelDO.getExtra())
                .ifPresent(extra -> {
                    model.setExtra(JSON.parseObject(extra, HashMap.class));
                });
        model.setOutBizId(processorTaskModelDO.getOutBizId());
        model.setId(processorTaskModelDO.getId());

        return model;
    }

    @Override
    public void save(ProcessorTaskDomainModel taskDomainModel) {
        ProcessorTaskModelDO taskModelDO = convertTo(taskDomainModel);
        ProcessorTaskModelDO saveModel = processorTaskMapper.save(taskModelDO);
        taskDomainModel.setId(saveModel.getId());
    }

    private ProcessorTaskModelDO convertTo(ProcessorTaskDomainModel taskDomainModel) {
        ProcessorTaskModelDO taskModelDO = new ProcessorTaskModelDO();
        taskModelDO.setTaskType(taskDomainModel.getProcessorTaskType());
        taskModelDO.setExtra(JSON.toJSONString(taskDomainModel.getExtra()));
        taskModelDO.setId(taskModelDO.getId());
        taskModelDO.setCreatedAt(taskDomainModel.getCreatedAt());
        taskModelDO.setUpdatedAt(taskDomainModel.getUpdatedAt());
        taskModelDO.setRetryAt(taskDomainModel.getRetryAt());
        taskModelDO.setStatus(taskDomainModel.getStatus().name());
        taskModelDO.setOutBizId(taskDomainModel.getOutBizId());
        taskModelDO.setCurrentFlow(taskDomainModel.getCurrentFlow());
        taskModelDO.setRequestId(taskDomainModel.getRequestId());
        taskModelDO.setRetryTimes(taskDomainModel.getRetryTimes());
        taskModelDO.setId(taskDomainModel.getId());
        return taskModelDO;
    }

    @Override
    public void modify(ProcessorTaskDomainModel taskDomainModel) {
        this.save(taskDomainModel);
    }
}
