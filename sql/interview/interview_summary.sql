drop table if exists `interview_summary`;
create table `interview_summary`
(
    `id` bigint primary key auto_increment comment '面试总结 id',
    `interview_id` bigint not null comment '面试 id',
    `basis` tinyint not null default 0 comment '基础理论知识掌握（0-5）',
    `coding` tinyint not null default 0 comment '代码能力（0-5）',
    `thinking` tinyint not null default 0 comment '思维能力（0-5）',
    `express` tinyint not null default 0 comment '表达能力（0-5）',
    `evaluate` varchar(500) not null default '' comment '面试总评',
    `suggest` varchar(500) not null default '' comment '复习建议',
    `playback` varchar(256) not null default '' comment '面试回放链接',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_interview_id`(`interview_id` asc) using btree
) comment '面试总结表';