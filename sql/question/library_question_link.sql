drop table if exists `library_question_link`;
create table `library_question_link`
(
    `lib_id` bigint not null comment '题库 id',
    `question_id` bigint not null comment '问题 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_lq_id`(`lib_id` asc, `question_id`) using btree
) comment '题库-问题关联表';