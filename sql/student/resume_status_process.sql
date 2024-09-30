drop table if exists `resume_status_process`;
create table `resume_status_process`
(
    `id` bigint primary key auto_increment comment 'id',
    `resume_id` bigint not null comment '简历 id',
    `resume_status` int not null comment '简历状态',
    `resume_event` int not null comment '简历事件',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_resume_id`(`resume_id` asc) using btree
) comment '招新简历状态过程表';
