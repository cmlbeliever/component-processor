package com.cml.framework.processor.core.flow;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ziyu
 * @Description
 * @createTime 2021年10月17日 22:18:00
 */
public class FlowTaskHolder {
    private ConcurrentHashMap<FlowTaskType, FlowTask> flowTaskHolder = new ConcurrentHashMap<>();

    public void register(FlowTaskType type, FlowTask flowTask) {
        flowTaskHolder.put(type, flowTask);
    }

    public FlowTask take(FlowTaskType type){
        return flowTaskHolder.get(type);
    }

}
