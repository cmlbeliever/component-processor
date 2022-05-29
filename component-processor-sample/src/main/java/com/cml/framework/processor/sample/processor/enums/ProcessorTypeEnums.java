package com.cml.framework.processor.sample.processor.enums;

import com.cml.framework.processor.core.ProcessorType;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:23:00
 */
public enum ProcessorTypeEnums implements ProcessorType {
    USER_REGISTER("userRegister"),
    USER_REGISTER2("userRegister2")
    ;

    private String type;

    ProcessorTypeEnums(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return this.type;
    }
}
