package com.cml.framework.processor.core.model;

import com.cml.framework.processor.core.enums.ProcessorStatus;
import lombok.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @Description
 * @createTime 2021年10月17日 21:39:00
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessorTaskDomainModel {

    /**
     * 任务id
     */
    private String id;

    /**
     * 请求id，processorTaskType+outBizId+requestId=唯一
     */
    private String requestId;

    /**
     * 业务id
     */
    private String outBizId;

    /**
     * 任务类型
     */
    private String processorTaskType;

    /**
     * 任务状态
     */
    private ProcessorStatus status;

    /**
     * 当前流程
     */
    private String currentFlow;

    private Map<String, String> extra = new HashMap<>();

    private Date retryAt;

    private int retryTimes;

    private Date createdAt;

    private Date updatedAt;

    public void putExtra(String key, String value) {
        extra.put(key, value);
    }

    public boolean canExecute() {
        return status == ProcessorStatus.INIT || status == ProcessorStatus.PROCESSING || status == ProcessorStatus.RETRY;
    }

    public boolean isFinish() {
        return status == ProcessorStatus.FAIL || status == ProcessorStatus.SUCCESS;
    }

    public String takeExtra(String key) {
        return this.extra.get(key);
    }

    public boolean waitReceipt() {
        return status == ProcessorStatus.WAIT_RECEIPT;
    }

    public void startProcess() {
        this.status = ProcessorStatus.PROCESSING;
    }
}
