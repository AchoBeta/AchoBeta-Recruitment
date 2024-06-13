drop table if exists `recruitment_activity`;
create table `recruitment_activity`(
    `id` bigint primary key auto_increment comment '招新 id',
    `paper_id` bigint default null comment '试卷 id',
    `batch` int not null comment 'ab版本',
    `deadline` datetime not null comment '招新截止时间',
    `is_run` bit not null default b'0' comment '是否启动',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_batch`(`batch` asc) using btree,
    index `idx_paper_id`(`paper_id` asc) using btree
) comment '招新活动表';