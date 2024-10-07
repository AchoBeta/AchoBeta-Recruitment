drop table if exists `digital_resource`;
create table `digital_resource`
(
    `id` bigint primary key auto_increment comment '资源 id',
    `code` bigint unique not null comment '资源码',
    `user_id` bigint not null comment '上传文件的用户 id',
    -- todo: 设置默认的权限等级
    `access_level` int not null default 2 comment '访问权限',
    `original_name` varchar(100) not null comment '上传时的文件名',
    `file_name` varchar(256) not null comment '在对象存储服务中存储的对象名',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    unique index `uni_code`(`code` asc) using btree,
    index `idx_user_id`(`user_id` asc) using btree
) comment '资源表';