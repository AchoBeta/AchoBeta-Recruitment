
drop table if exists `short_link`;
create table `short_link`(
    `id` bigint(20) PRIMARY KEY AUTO_INCREMENT comment '短链接编号',
    `origin_url` varchar(256) DEFAULT '' comment '原链接',
    `short_code` char(6) UNIQUE DEFAULT '' comment '短链code',
    `is_used` bit(1) DEFAULT b'0' comment '是否使用过',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime DEFAULT NULL comment '更新时间',
    `version` int DEFAULT 0 comment '乐观锁',
    `is_deleted` tinyint DEFAULT 0 comment '逻辑删除'
) comment '长短链关系表';
create UNIQUE index SHORT_CODE_INDEX on `short_link`(`short_code`);

