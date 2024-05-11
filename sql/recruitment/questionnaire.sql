drop table if exists `questionnaire`;
create table `questionnaire`
(
    `id` bigint primary key auto_increment comment '问卷 ID',
    `stu_id` bigint not null comment '学生 ID',
    `rec_id` bigint not null comment '招新 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 外键约束
    foreign key (`stu_id`) references `user`(`id`),
    foreign key (`rec_id`) references `recruitment`(`id`),
    -- 索引
    unique index `uni_id`(`id` asc) using btree,
    index `idx_stu_id`(`stu_id` asc) using btree,
    index `idx_rec_id`(`rec_id` asc) using btree
) comment '问卷表';