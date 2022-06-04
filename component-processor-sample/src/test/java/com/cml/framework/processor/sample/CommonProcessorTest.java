package com.cml.framework.processor.sample;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.ProcessResult;
import com.cml.framework.processor.core.ProcessorContainer;
import com.cml.framework.processor.core.ProcessorRequest;
import com.cml.framework.processor.core.enums.ProcessorStatus;
import com.cml.framework.processor.core.model.ProcessorTaskDomainModel;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;
import com.cml.framework.processor.sample.processor.enums.ProcessorTypeEnums;
import com.cml.framework.processor.sample.storage.mapper.ProcessorTaskMapper;
import com.cml.framework.processor.sample.storage.mapper.ProcessorTaskModelDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest()
public class CommonProcessorTest {

    @Autowired
    private ProcessorContainer processorContainer;

    @Autowired
    private ProcessorTaskModelRepository processorTaskModelRepository;

    @Test
    public void testUserRegister() throws InterruptedException {
        ProcessorRequest processorRequest = new ProcessorRequest();
        processorRequest.setRequestId(UUID.randomUUID().toString());
        processorRequest.setProcessorType(ProcessorTypeEnums.USER_REGISTER.type());
        processorRequest.setOutBizId("userId1");

        ProcessResult processResult = processorContainer.take(ProcessorTypeEnums.USER_REGISTER.type()).process(processorRequest);
        assert processResult.isSuccess();

        ProcessorTaskDomainModel model = processorTaskModelRepository.find(processorRequest.getProcessorType(), processorRequest.getRequestId());
        assert !model.canExecute();
        assert model.isFinish();
        assert model.getStatus() == ProcessorStatus.SUCCESS;
    }

}
