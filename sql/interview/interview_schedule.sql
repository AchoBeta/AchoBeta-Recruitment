drop table if exists `interview_schedule`;
create table `interview_schedule`
(
    `id` bigint primary key auto_increment comment '面试预约 id',
    `participation_id` bigint not null comment '用户的“活动参与” id',
    `start_time` datetime not null comment '预约开始时间',
    `end_time` datetime not null comment '预约结束时间',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_participation_id`(`participation_id` asc) using btree
) comment '面试预约表';