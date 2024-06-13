drop table if exists `paper_entry`;
create table paper_entry
(
    `paper_id` bigint not null comment '题单 id',
    `question_id` bigint not null comment '题目 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index idx_pq_id(`paper_id` asc, `question_id`) using btree
) comment '题单-题目 关联表';
