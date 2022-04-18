package com.cml.framework.processor.core;

import com.alibaba.fastjson.JSON;
import com.cml.framework.processor.core.enums.ProcessorTypeEnums;
import com.cml.framework.processor.core.flow.FlowTaskHolder;
import com.cml.framework.processor.core.flowtask.*;
import com.cml.framework.processor.core.flowtask.enums.FlowTaskEnums;
import com.cml.framework.processor.core.model.ProcessorReceipt;
import com.cml.framework.processor.core.repository.MockProcessorTaskModelRepository;
import com.cml.framework.processor.core.repository.ProcessorTaskModelRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:29:00
 */
public class UserRegisterProcessorTest2 {

    private ProcessorContainer processorContainer = new ProcessorContainer();

    @Before
    public void before() {

        FlowTaskHolder flowTaskHolder = new FlowTaskHolder();
        flowTaskHolder.register(FlowTaskEnums.TASK_STEP1, new Step1FlowTask());
        flowTaskHolder.register(FlowTaskEnums.TASK_STEP2, new Step2FlowTask());
        flowTaskHolder.register(FlowTaskEnums.TASK_WAIT_RECEIPT_STEP2, new Step2WaitReceiptFlowTask());
        flowTaskHolder.register(FlowTaskEnums.TASK_STEP3, new Step3FlowTask());
        flowTaskHolder.register(FlowTaskEnums.TASK_STEP4, new Step4FlowTask());

        UserRegisterProcessor userRegisterProcessor = new UserRegisterProcessor();
        UserRegisterWithReceiptProcessor userRegisterProcessor2 = new UserRegisterWithReceiptProcessor();

        ProcessorTaskModelRepository repository = new MockProcessorTaskModelRepository();
        userRegisterProcessor.setProcessorTaskModelRepository(repository);
        userRegisterProcessor2.setProcessorTaskModelRepository(repository);

        userRegisterProcessor.setFlowTaskHolder(flowTaskHolder);
        userRegisterProcessor2.setFlowTaskHolder(flowTaskHolder);

        processorContainer.register(userRegisterProcessor);
        processorContainer.register(userRegisterProcessor2);
    }

    @Test
    public void testCommon() {
        ProcessorRequest processorRequest = new ProcessorRequest();
        processorRequest.setRequestId(UUID.randomUUID().toString());
        processorRequest.setProcessorType(ProcessorTypeEnums.USER_REGISTER.type());
        processorRequest.setOutBizId("userId1");

        ProcessResult process = processorContainer.take(processorRequest.getProcessorType()).process(processorRequest);
        System.out.println(JSON.toJSONString(process));
        assert process.isSuccess();
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

    }
}
