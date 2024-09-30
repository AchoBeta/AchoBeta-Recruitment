drop table if exists `member`;
create table `member`
(
    `id` bigint primary key auto_increment comment '正式成员 id',
    `resume_id` bigint not null comment '简历 id',
    `manager_id` bigint unique not null comment '新开的管理员账号 id',
    `parent_id` bigint not null comment '父管理员 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_resume_id`(`resume_id` asc) using btree,
    unique index `uni_manager_id`(`manager_id` asc) using btree,
    index `idx_parent_id`(`parent_id` asc) using btree
) comment '正式成员表';