drop table if exists `recruitment`;
create table `recruitment`
(
    `id` bigint primary key auto_increment comment '招新 ID',
    `batch` int not null comment 'ab版本',
    `is_run` bit(1) not null default b'0' comment '是否启动',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    unique index `uni_id`(`id` asc) using btree
) comment '招新表';
