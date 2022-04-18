package com.cml.framework.processor.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程结果
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 20:42:00
 */
@Data
public class ProcessResult {

    private boolean success;

    private boolean retry;

    private boolean waitReceipt;

    private String errorCode;

    private String errorMsg;

    /**
     * 业务拓展参数，会存到db中
     */
    private Map<String, String> bizExtra = new HashMap<>();

    public static ProcessResult success(boolean waitReceipt) {
        ProcessResult processResult = new ProcessResult();
        processResult.setSuccess(true);
        processResult.setWaitReceipt(waitReceipt);
        return processResult;
    }

    public static ProcessResult success() {
        ProcessResult processResult = new ProcessResult();
        processResult.setSuccess(true);
        return processResult;
    }

    public static ProcessResult fail() {
        ProcessResult processResult = new ProcessResult();
        processResult.setSuccess(false);
        return processResult;
    }
}
