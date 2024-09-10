drop table if exists `time_period`;
create table `time_period`
(
    `id` bigint primary key auto_increment comment '时间段 id',
    `act_id` bigint not null comment '招新活动 id',
    `start_time` datetime not null comment '开始时间',
    `end_time` datetime not null comment '结束时间',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_act_id`(`act_id` asc) using btree
) comment '时间段表';