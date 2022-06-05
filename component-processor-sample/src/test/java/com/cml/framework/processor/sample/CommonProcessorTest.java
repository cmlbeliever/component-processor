package com.cml.framework.processor.sample;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.FlowProcessor;
import com.cml.framework.processor.core.ProcessResult;
import com.cml.framework.processor.core.ProcessorContainer;
import com.cml.framework.processor.core.ProcessorRequest;
import com.cml.framework.processor.core.enums.ProcessorStatus;
import com.cml.framework.processor.core.model.ProcessorReceipt;
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


    @Test
    public void testReceipt() {
        System.out.println("======================= \n\n\n");
        ProcessorRequest processorRequest = new ProcessorRequest();
        processorRequest.setRequestId(UUID.randomUUID().toString());
        processorRequest.setOutBizId("userId1");
        processorRequest.setProcessorType(ProcessorTypeEnums.USER_REGISTER2.type());

        FlowProcessor flowProcessor = processorContainer.take(processorRequest.getProcessorType());

        ProcessResult process = flowProcessor.process(processorRequest);
        System.out.println("---阶段1执行完成--->" + JSON.toJSONString(process));

        System.out.println("\n\n\n------------开始回执处理-----------\n\n\n");
        ProcessorReceipt processorReceipt = new ProcessorReceipt();
        processorReceipt.setData("结果xx");
        processorReceipt.setOutBizId(processorRequest.getOutBizId());
        processorReceipt.setProcessorType(processorRequest.getProcessorType());
        processorReceipt.setRequestId(processorRequest.getRequestId());
        process = flowProcessor.receipt(processorReceipt);
        System.out.println("receipt:" + JSON.toJSONString(process));

        ProcessorTaskDomainModel model = processorTaskModelRepository.find(processorRequest.getProcessorType(), processorRequest.getRequestId());
        System.out.println("receipt:" + JSON.toJSONString(model));

        assert !model.canExecute();
        assert model.isFinish();
        assert model.getStatus() == ProcessorStatus.SUCCESS;
    }

}
