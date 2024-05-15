drop table if exists `question_entry`;
create table `question_entry`(
    `id` bigint primary key auto_increment comment '问题 id',
    `lib_id` bigint not null comment '题库 id',
    `title` varchar(100) not null default '' comment '标题',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_lib_id`(`lib_id` asc) using btree
) comment '自定义问题表';