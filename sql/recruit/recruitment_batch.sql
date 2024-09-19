drop table if exists `recruitment_batch`;
create table `recruitment_batch`
(
    `id` bigint primary key auto_increment comment '招新批次 id',
    `batch` int not null comment 'AchoBeta 届数',
    `title` varchar(100) not null default '' comment '招新标题',
    `deadline` datetime not null comment '截止时间',
    `is_run` bit not null default b'0' comment '是否启动',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_batch`(`batch` asc) using btree
) comment '招新批次表';