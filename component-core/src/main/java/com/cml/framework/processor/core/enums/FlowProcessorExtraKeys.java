package com.cml.framework.processor.core.enums;

import lombok.Getter;

/**
 * @author
 * @Description
 * @createTime 2021年10月20日 21:52:00
 */
@Getter
public enum FlowProcessorExtraKeys {
    /**
     * 请求参数
     */
    REQUEST("request"),

    /**
     * 回执请求
     */
    RECEIPT_REQUEST("receiptRequest"),

    /**
     * 业务拓展参数
     */
    BIZ_EXTRA("bizExtra"),

    /**
     * 异常
     */
    EX("ex"),

    /**
     * 结果
     */
    RESULT("result");

    private String key;

    FlowProcessorExtraKeys(String key) {
        this.key = key;
    }
}
