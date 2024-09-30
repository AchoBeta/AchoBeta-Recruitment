drop table if exists `feishu_resource`;
create table `feishu_resource`
(
    `id` bigint primary key auto_increment comment '飞书资源 id',
    `ticket` varchar(50) unique not null comment '任务 ID',
    `original_name` varchar(100) not null comment '上传时的文件名',
    `token` varchar(50) not null default '' comment '导入云文档的 token',
    `type` varchar(20) not null default '' comment '导入的在线云文档类型',
    `url` varchar(200) not null default '' comment '导入云文档的 URL',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    unique index `uni_ticket`(`ticket`) using btree
) comment '飞书资源表';