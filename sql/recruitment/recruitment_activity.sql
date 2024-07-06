drop table if exists `recruitment_activity`;
create table `recruitment_activity`
(
    `id` bigint primary key auto_increment comment '招新活动 id',
    `batch_id` bigint not null comment '招新批次 id',
    `paper_id` bigint default null comment '试卷 id',
    `title` varchar(100) not null default '' comment '活动标题',
    `target` json not null comment '面向的人群 {"grade": [23, 24], "uid": [1, 2, 3]}',
    `description` text not null comment '活动说明',
    `deadline` datetime not null comment '截止时间',
    `is_run` bit not null default b'0' comment '是否启动',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_batch_id`(`batch_id` asc) using btree,
    index `idx_paper_id`(`paper_id` asc) using btree
) comment '招新活动表';