package com.cml.framework.processor.core.model;

import com.cml.framework.processor.core.ProcessorType;
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
    private String processorId;

    private String processorType;

    private String outBizId;

    private String requestId;

    private T data;

}
