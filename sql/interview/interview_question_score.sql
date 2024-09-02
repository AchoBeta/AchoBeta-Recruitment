drop table if exists `interview_question_score`;
create table `interview_question_score`
(
    `id` bigint primary key auto_increment comment '面试题评分 id',
    `interview_id` bigint not null comment '面试 id',
    `question_id` bigint not null comment '问题 id',
    `score` int not null comment '评分（0-10，-1为超纲）',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_interview_id`(`interview_id` asc) using btree,
    index `idx_question_id`(`question_id` asc) using btree
) comment '面试题评分关联表';
