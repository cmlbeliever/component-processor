package com.cml.framework.processor.core.enums;

import lombok.Getter;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 21:46:00
 */
@Getter
public enum ProcessorStatus {
    /**
     * 初始化
     */
    INIT,

    /**
     * 处理中
     */
    PROCESSING,

    /**
     * 已完成
     */
    SUCCESS,

    /**
     * 失败
     */
    FAIL,

    /**
     * 重试
     */
    RETRY,

    /**
     * 待回执
     */
    WAIT_RECEIPT;
}
