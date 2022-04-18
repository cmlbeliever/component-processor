package com.cml.framework.processor.core;

import lombok.Data;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 20:44:00
 */
@Data
public class ProcessorRequest {

    private String requestId;

    private String outBizId;

    private String processorType;

}
