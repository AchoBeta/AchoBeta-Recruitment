/*
 Navicat Premium Data Transfer

 Source Server         : 袁哥
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : 120.78.157.4:3306
 Source Schema         : achobeta_recruitment

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 25/09/2024 17:52:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `manager_id` bigint NOT NULL COMMENT '发送消息的管理员id',
  `tittle` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `attachment` bigint NULL DEFAULT NULL COMMENT '附件url',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '伪删除标记',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_manager_id`(`manager_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4222 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '“活动参与”表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
