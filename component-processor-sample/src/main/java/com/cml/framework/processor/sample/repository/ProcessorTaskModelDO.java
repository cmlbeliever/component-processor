package com.cml.framework.processor.sample.repository;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity()
@Table(name = "t_processor_task")
public class ProcessorTaskModelDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 请求id，processorTaskType+outBizId+requestId=唯一
     */
    @Column(name = "request_id")
    private String requestId;

    /**
     * 业务id
     */
    @Column(name = "out_biz_id")
    private String outBizId;

    /**
     * 任务类型
     */
    @Column(name = "task_type")
    private String taskType;

    /**
     * 任务状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 当前流程
     */
    @Column(name = "current_flow")
    private String currentFlow;

    @Column(name = "extra")
    private String extra;

    @Column(name = "retry_at")
    private Date retryAt;

    @Column(name = "retry_times")
    private int retryTimes;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
