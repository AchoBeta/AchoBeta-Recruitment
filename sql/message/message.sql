drop table if exists `message`;
create table `message`
(
    `id` bigint primary key auto_increment comment '消息 id',
    `manager_id` bigint not null comment '发送消息的管理员 id',
    `user_id` bigint not null comment '用户 id',
    `tittle` varchar(256) not null default '' comment '消息标题',
    `content` text not null comment '消息内容',
    `send_time` datetime not null default current_timestamp comment '发送时间',
    `attachment` bigint null default null comment '附件 url',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_manager_id`(`manager_id` asc) using btree,
    index `idx_user_id`(`user_id` asc) using btree
) comment = '消息表';