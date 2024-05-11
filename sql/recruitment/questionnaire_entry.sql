-- 创建问卷-自定义项关联表
drop table if exists `questionnaire_entry`;
create table `questionnaire_entry` (
    `questionnaire_id` bigint not null comment '问卷 ID',
    `entry_id` bigint not null comment '自定义项 ID',
    `content` text not null comment '自定义项内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 主键
    primary key (`questionnaire_id`, `entry_id`),
    -- 外键约束
    foreign key (`questionnaire_id`) references `questionnaire`(`id`),
    foreign key (`entry_id`) references `custom_entry`(`id`),
    -- 索引（注意是先 问卷 ID 再 自定义项 ID）
    unique index `uni_id`(`questionnaire_id` asc, `entry_id` asc) using btree
) comment '问卷-自定义项关联表';