drop table if exists `short_link`;
create table `short_link`
(
    `id` bigint primary key auto_increment comment '短链接编号',
    `origin_url` varchar(512) default '' comment '原链接',
    `short_code` char(6) unique default '' comment '短链code',
    `is_used` bit default b'0' comment '是否使用过',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_short_code`(`short_code` asc) using btree
) comment '长短链关系表';
