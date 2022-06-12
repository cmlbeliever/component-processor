create table t_processor_task
(
    id           bigint auto_increment,
    request_id   varchar(128)           not null,
    out_biz_id   varchar(128)           not null,
    task_type    varchar(64)            not null,
    status       varchar(16)            not null,
    current_flow varchar(64)            null,
    extra        varchar(2048)          null,
    retry_at     datetime               null,
    retry_times  int      default 0     not null,
    created_at   datetime default now() not null,
    updated_at   datetime default now() not null,
    constraint t_processor_task_pk
        primary key (id),
    unique index uidx_task_type_request_id
);