-- 创建消息表
drop table if exists `message`;
create table `message` (
    `id` bigint primary key auto_increment comment '面评 ID',
    `stu_id` bigint not null comment '学生 ID',
    `manager_id` bigint not null comment '管理员 ID',
    `title` varchar(100) not null default '' comment '消息标题',
    `content` text not null comment '消息内容',
    `deadline` datetime default null comment '截止时间（可选）',
    `attachment` varchar(256) default null comment '附件（可选）',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 外键约束
    foreign key (`stu_id`) references `user`(`id`),
    foreign key (`manager_id`) references `user`(`id`),
    -- 索引
    unique index `uni_id`(`id` asc) using btree,
    index `idx_stu_id`(`stu_id` asc) using btree,
    index `idx_manager_id`(`manager_id` asc) using btree
) comment '消息表';