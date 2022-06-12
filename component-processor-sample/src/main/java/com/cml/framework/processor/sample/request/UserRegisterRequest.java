package com.cml.framework.processor.sample.request;

import com.cml.framework.processor.core.ProcessorRequest;
import lombok.Data;

@Data
public class UserRegisterRequest extends ProcessorRequest {
    /**
     * 主动抛出异常
     */
    private String error;
}
