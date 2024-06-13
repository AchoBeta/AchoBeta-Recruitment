drop table if exists `questionnaire_entry`;
create table `questionnaire_entry`
(
    `questionnaire_id` bigint not null comment '问卷 id',
    `question_id` bigint not null comment '题目 id',
    `answer` text not null comment '回答',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引（注意是先 问卷 ID 再 自定义项 ID）
    index `idx_qe_id`(`questionnaire_id` asc, `question_id` asc) using btree
) comment '问卷-自定义项关联表';