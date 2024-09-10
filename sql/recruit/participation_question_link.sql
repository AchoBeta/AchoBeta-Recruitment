drop table if exists `participation_question_link`;
create table `participation_question_link`
(
    `id` bigint primary key auto_increment comment 'id',
    `participation_id` bigint not null comment '“活动参与” id',
    `question_id` bigint not null comment '问题 id',
    `answer` text not null comment '回答',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_pq_id`(`participation_id` asc, `question_id` asc) using btree
) comment '“活动参与”-问题关联表';