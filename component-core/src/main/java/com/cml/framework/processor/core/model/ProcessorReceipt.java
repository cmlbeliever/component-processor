package com.cml.framework.processor.core.model;

import lombok.Data;

/**
 * 回执数据请求
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 20:44:00
 */
@Data
public class ProcessorReceipt<T> {

    /**
     * 任务id
     */
    private String processorId;

    /**
     * 任务类型
     */
    private String processorType;

    /**
     * 业务id
     */
    private String outBizId;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 回执数据
     */
    private T data;

}
