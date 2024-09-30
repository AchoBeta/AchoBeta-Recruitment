drop table if exists `user_feedback`;
create table `user_feedback`
(
    `id` bigint primary key auto_increment comment '反馈 id' ,
    `user_id` bigint not null comment '用户id',
    `batch_id` bigint not null comment '招新批次 id',
    `message_id` bigint default null comment '处理结果的消息 id',
    `title` varchar(256) default '' not null comment '反馈标题',
    `content` text not null comment '反馈内容',
    `attachment` bigint default null comment '附件链接',
    `feedback_time` datetime   default CURRENT_TIMESTAMP not null comment '反馈时间',
    `is_handle` bit default b'0' not null comment '是否处理标记',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_user_id`(`user_id` asc) using btree,
    index `idx_batch_id`(`batch_id` asc) using btree,
    index `idx_message_id`(`message_id` asc) using btree
) comment = '用户反馈表';