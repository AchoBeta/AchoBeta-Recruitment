SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
    `username`     varchar(50)  NOT NULL DEFAULT '' COMMENT '用户名',
    `nickname`     varchar(50)  NOT NULL DEFAULT '' COMMENT '用户昵称',
    `email`        varchar(50)  NOT NULL DEFAULT '' COMMENT '邮箱',
    `phone_number` varchar(11)  NOT NULL DEFAULT '' COMMENT '手机号码',
    `password`     varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
    `user_type`    int          NOT NULL DEFAULT 1 COMMENT '用户类型：1.普通用户 2. 管理员',
    `avatar`       bigint(20) COMMENT '头像地址',
    `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`      int          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `is_deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '伪删除标记',
    PRIMARY KEY (`id`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone_number`),
    UNIQUE INDEX `uni_name` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10000
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '用户基本信息表'
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

-- 管理员，username: root; password: AchoBeta666

insert into user
(`username`, `nickname`, `password`, `user_type`)
values
    ('root', 'root', '$2a$10$YPKp0kzLjnNrW5CgKuDdiuF4tZO0KXacmhy2KT7N9Zey49Cmi/rfu', 2)
;