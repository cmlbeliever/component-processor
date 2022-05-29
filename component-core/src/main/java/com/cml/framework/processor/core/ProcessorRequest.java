package com.cml.framework.processor.core;

import lombok.Data;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 20:44:00
 */
@Data
public class ProcessorRequest {

    /**
     * 请求id processorType+requestId=唯一一条请求
     */
    private String requestId;

    /**
     * 业务id
     */
    private String outBizId;

    /**
     * 任务类型 建议业务上自定义枚举处理
     */
    private String processorType;

}
