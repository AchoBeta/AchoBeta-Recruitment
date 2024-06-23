drop table if exists `participation_period_link`;
create table `participation_period_link`
(
    `participation_id` bigint not null comment '“活动参与” id',
    `period_id` bigint not null comment '时间段 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `uni_pp_id`(`participation_id` asc, `period_id` asc) using btree
) comment '“活动参与”-时间段关联表';