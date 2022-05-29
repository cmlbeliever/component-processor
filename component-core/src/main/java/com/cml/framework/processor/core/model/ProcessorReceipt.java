package com.cml.framework.processor.core.model;

import lombok.Data;

/**
 * 回执数据请求
 *
 * @author
 * @Description processorType+requestId = 需要同创建任务时的一样，根据这两个参数获取到对应的任务
 * @createTime 2021年10月17日 20:44:00
 */
@Data
public class ProcessorReceipt<T> {

    /**
     * 任务类型
     */
    private String processorType;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 业务id
     */
    private String outBizId;

    /**
     * 回执数据
     */
    private T data;

}
