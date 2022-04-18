package com.cml.framework.processor.core;

import com.cml.framework.processor.core.model.ProcessorReceipt;

/**
 * 流程处理器
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 20:42:00
 */
public interface FlowProcessor {

    /**
     * 流程类型
     *
     * @return
     */
    String processorType();

    /**
     * 执行业务流程,任务不存在会创新一个新的，任务类型+业务id+请求id唯一
     *
     * @param request
     * @return
     */
    ProcessResult process(ProcessorRequest request);

    /**
     * 回执 ， 需要业务先获取到对应的任务
     *
     * @param request
     * @return
     */
    ProcessResult receipt(ProcessorReceipt request);
}
