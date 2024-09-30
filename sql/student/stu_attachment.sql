/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : achobeta_recruitment

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 17/01/2024 13:44:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stu_attachment
-- ----------------------------
DROP TABLE IF EXISTS `stu_attachment`;
CREATE TABLE `stu_attachment`
(
    `id`          bigint UNSIGNED                                  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `resume_id`   bigint UNSIGNED                                  NOT NULL COMMENT '学生表主键id',
    `filename`    varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '附件名',
    `attachment`  bigint                                           NOT NULL COMMENT '附件资源码',
    `create_time` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     int                                              NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `is_deleted`  tinyint                                          NOT NULL DEFAULT 0 COMMENT '伪删除标记',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_resume_id` (`resume_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '学生附件表'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
