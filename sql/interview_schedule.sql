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

 Date: 17/01/2024 13:43:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for interview_schedule
-- ----------------------------
DROP TABLE IF EXISTS `interview_schedule`;
CREATE TABLE `interview_schedule`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `stu_id` bigint UNSIGNED NOT NULL COMMENT '用户表id',
  `date` datetime NOT NULL COMMENT '预约日期',
  `start_time` datetime NOT NULL COMMENT '预约开始时间',
  `end_time` datetime NOT NULL COMMENT '预约结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '伪删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_stu_id`(`stu_id` ASC) USING BTREE,
  INDEX `idx_date`(`date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '面试时间预约表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
