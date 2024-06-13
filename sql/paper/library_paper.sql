drop table if exists `library_paper`;
create table `library_paper`
(
    `lib_id` bigint not null comment '试卷库 id',
    `paper_id` bigint not null comment '试卷 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_lp_id`(`lib_id` asc, `paper_id`) using btree
) comment '试卷库-试卷 关联表';
