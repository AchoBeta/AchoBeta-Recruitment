
-- 创建面试表
drop table if exists `interview`;
create table `interview` (
    `id` bigint primary key auto_increment comment '面试 ID',
    `schedule_id` bigint unsigned not null comment '面试预约 ID',
    `manager_id` bigint not null comment '管理员 ID',
    `summary_id` bigint default null comment '面试总结（面试结束才能添加）',
    `title` varchar(100) not null default '' comment '面试主题',
    `state` tinyint not null default 0 comment '是否开始（0未开始、1开始、2结束）',
    `interview_link` varchar(256) not null default '' comment '会议链接',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 外键约束
    foreign key (`schedule_id`) references `interview_schedule`(`id`),
    foreign key (`manager_id`) references `user`(`id`),
    foreign key (`summary_id`) references `interview_summary`(`id`),
    -- 索引
    unique index `uni_id`(`id` asc) using btree,
    index `idx_schedule_id`(`schedule_id` asc) using btree,
    index `idx_manager_id`(`manager_id` asc) using btree,
    index `idx_summary_id`(`summary_id` asc) using btree
) comment '面试表';
