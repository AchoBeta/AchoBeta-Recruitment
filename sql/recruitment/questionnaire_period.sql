drop table if exists `questionnaire_period`;
create table `questionnaire_period`(
    `questionnaire_id` bigint not null comment '问卷 id',
    `period_id` bigint not null comment '时间段 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引（注意是先 问卷 ID 再 时间段 ID）
    index `uni_qp_id`(`questionnaire_id` asc, `period_id` asc) using btree
) comment '问卷-时间段关联表';