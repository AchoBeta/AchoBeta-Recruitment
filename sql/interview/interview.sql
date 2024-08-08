drop table if exists `interview`;
create table `interview`
(
    `id` bigint primary key auto_increment comment '面试 id',
    `schedule_id` bigint unsigned not null comment '面试预约 id',
    `paper_id` bigint default null comment '面试试卷 id',
    `title` varchar(100) not null default '' comment '面试主题',
    `description` text not null comment '面试说明',
    `status` tinyint not null default 0 comment '是否开始（0未开始、1进行中、2已结束）',
    `address` varchar(500) not null default '' comment '线下面试地址/显示面试会议链接',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_schedule_id`(`schedule_id` asc) using btree,
    index `idx_paper_id`(`paper_id` asc) using btree
) comment '面试表';