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

 Date: 17/01/2024 13:44:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `questionnaire_id` int NOT NULL DEFAULT 1 COMMENT '问卷id',
  `student_id` varchar(13) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '学号',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '姓名',
  `gender` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别',
  `major` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '专业',
  `class` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '班级',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '邮箱',
  `phone_number` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '手机号码',
  `reason` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '加入 AchoBeta 的理由',
  `introduce` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '个人介绍（自我认知）',
  `experience` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '个人经历 （项目经历、 职业规划等）',
  `awards` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '获奖经历',
  `image` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '照片',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '备注',
  `status_id` int NOT NULL DEFAULT 1 COMMENT '简历状态',
  `submit_count` int NOT NULL DEFAULT 0 COMMENT '提交次数',
  `batch` int NOT NULL DEFAULT 1 COMMENT 'ab版本',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '伪删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_questionnaire_id`(`questionnaire_id` ASC) USING BTREE,
  UNIQUE INDEX `uni_student_id`(`student_id` ASC) USING BTREE,
  UNIQUE INDEX `uni_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `uni_ab_version`(`batch` ASC) USING BTREE,
  UNIQUE INDEX `uni_class`(`class` ASC) USING BTREE,
  UNIQUE INDEX `uni_major`(`major` ASC) USING BTREE,
  UNIQUE INDEX `uni_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '学生用户简历表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
