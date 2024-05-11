-- 创建面评表
drop table if exists `interview_comment`;
create table `interview_comment` (
    `id` bigint primary key auto_increment comment '面评 ID',
    `interview_id` bigint not null comment '面试 ID',
    `manager_id` bigint not null comment '管理员 ID',
    `content` varchar(500) not null default '' comment '面评内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 外键约束
    foreign key (`interview_id`) references `interview`(`id`),
    foreign key (`manager_id`) references `user`(`id`),
    -- 索引
    unique index `uni_id`(`id` asc) using btree,
    index `idx_interview_id`(`interview_id` asc) using btree,
    index `idx_manager_id`(`manager_id` asc) using btree
) comment '面评表';