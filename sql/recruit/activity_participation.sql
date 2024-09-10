drop table if exists `activity_participation`;
create table `activity_participation`
(
    `id` bigint primary key auto_increment comment '“活动参与” id',
    `stu_id` bigint not null comment '学生 id',
    `act_id` bigint not null comment '活动 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_stu_id`(`stu_id` asc) using btree,
    index `idx_act_id`(`act_id` asc) using btree
) comment '“活动参与”表';
-- todo: 分表优化