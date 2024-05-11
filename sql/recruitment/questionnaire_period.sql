-- 创建问卷-时间段关联表
drop table if exists `questionnaire_period`;
create table `questionnaire_period` (
    `questionnaire_id` bigint not null comment '问卷 ID',
    `period_id` bigint not null comment '时间段 ID',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 主键
    primary key (`questionnaire_id`, `period_id`),
    -- 外键约束
    foreign key (`questionnaire_id`) references `questionnaire`(`id`),
    foreign key (`period_id`) references `time_period`(`id`),
    -- 索引（注意是先 问卷 ID 再 时间段 ID）
    unique index `uni_id`(`questionnaire_id` asc, `period_id` asc) using btree
) comment '问卷-时间段关联表';