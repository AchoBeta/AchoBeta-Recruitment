drop table if exists `user`;
create table `user`(
    `id` bigint primary key auto_increment comment '用户唯一 id',
    `username` varchar(50) not null default '' comment '用户名',
    `nickname` varchar(50) not null default '' comment '用户昵称',
    `email` varchar(50) not null default '' comment '邮箱',
    `phone_number` varchar(11) not null default '' comment '手机号码',
    `password` varchar(100) not null default '' comment '密码',
    `user_type` int not null default 1 comment '用户类型：1.普通用户 2. 管理员',
    `avatar` bigint comment '头像地址',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_email`(`email` asc) using btree,
    index `idx_phone`(`phone_number` asc) using btree,
    unique index `uni_name`(`username` asc) using btree
) auto_increment = 10000 comment = '用户基本信息表';

-- root 管理员，username: root; password: AchoBeta666
insert into user(`username`, `nickname`, `password`, `user_type`)
    values('root', 'root', '$2a$10$YPKp0kzLjnNrW5CgKuDdiuF4tZO0KXacmhy2KT7N9Zey49Cmi/rfu', 2);
