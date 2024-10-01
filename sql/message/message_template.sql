drop table if exists `message_template`;
create table `message_template`
(
    `id` bigint primary key auto_increment comment '模板消息 id',
    `template_title` varchar(100) not null default '' comment '模板消息标题',
    `template_content` text not null comment '模板消息内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment = '模板消息表';