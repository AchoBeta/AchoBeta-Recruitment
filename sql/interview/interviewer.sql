drop table if exists `interviewer`;
create table `interviewer`
(
    `id` bigint primary key auto_increment comment '面试官 id',
    `manager_id` bigint not null comment '管理员 id',
    `schedule_id` bigint not null comment '面试预约 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_manager_id`(`manager_id` asc) using btree,
    index `idx_schedule_id`(`schedule_id` asc) using btree
) comment '面试官表';