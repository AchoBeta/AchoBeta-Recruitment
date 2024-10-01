drop table if exists `stu_attachment`;
create table `stu_attachment`
(
    `id` bigint primary key auto_increment comment 'id',
    `resume_id` bigint not null comment '学生表主键 id',
    `filename` varchar(256) not null default '' comment '附件名',
    `attachment` bigint not null comment '附件资源码',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_resume_id`(`resume_id` asc) using btree
) comment = '学生附件表';
