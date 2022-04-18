package com.cml.framework.processor.core.ex;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 22:16:00
 */
public class ProcessorStatusIllegalException extends IllegalStateException {
    public ProcessorStatusIllegalException() {
    }

    public ProcessorStatusIllegalException(String s) {
        super(s);
    }

    public ProcessorStatusIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessorStatusIllegalException(Throwable cause) {
        super(cause);
    }
}
