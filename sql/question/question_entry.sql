drop table if exists `question_entry`;
create table `question_entry`
(
    `id` bigint primary key auto_increment comment '题目 id',
    `title` varchar(100) not null default '' comment '标题',
    `standard` text not null comment '标答',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '题目 表';
