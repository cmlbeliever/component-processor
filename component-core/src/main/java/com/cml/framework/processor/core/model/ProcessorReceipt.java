package com.cml.framework.processor.core.model;

import lombok.Data;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 20:44:00
 */
@Data
public class ProcessorReceipt<T> {

    /**
     * 任务id
     */
    private Long processorId;

    private T data;

}
