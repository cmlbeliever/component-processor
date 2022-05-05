package com.cml.framework.processor.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程上下文
 *
 * @author
 * @Description
 * @createTime 2021年10月17日 20:53:00
 */
public class ProcessorContext {
    private ConcurrentHashMap<String, Object> extras = new ConcurrentHashMap<>();

    public void putExtra(String k, Object v) {
        extras.put(k, v);
    }

    public <T> T take(String key) {
        return (T) extras.get(key);
    }
}
