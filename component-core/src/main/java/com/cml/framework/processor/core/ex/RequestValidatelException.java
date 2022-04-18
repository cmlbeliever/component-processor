package com.cml.framework.processor.core.ex;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 22:16:00
 */
public class RequestValidatelException extends RuntimeException {
    public RequestValidatelException() {
    }

    public RequestValidatelException(String s) {
        super(s);
    }

    public RequestValidatelException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestValidatelException(Throwable cause) {
        super(cause);
    }
}
