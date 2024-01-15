
drop table if exists `short_link`;
create table `short_link`(
    `id` bigint(20) PRIMARY KEY AUTO_INCREMENT comment '短链接编号',
    `origin_url` varchar(256) DEFAULT '' comment '原链接',
    `short_code` char(6) UNIQUE DEFAULT '' comment '短链code',
    `is_used` bit(1) DEFAULT b'0' comment '是否使用过',
    `is_deleted` bit(1) DEFAULT b'0' comment '逻辑删除',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
);
create UNIQUE index SHORT_CODE_INDEX on `short_link`(`short_code`);

