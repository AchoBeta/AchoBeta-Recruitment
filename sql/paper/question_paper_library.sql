drop table if exists `question_paper_library`;
create table `question_paper_library`
(
    `id` bigint primary key auto_increment comment '试卷库 id',
    `lib_type` varchar(100) not null default '' comment '试卷库的类别',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_lib_type`(`lib_type` asc) using btree
) comment '试卷库表';