/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : 120.78.157.4:3306
 Source Schema         : achobeta_recruitment

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板消息 ID',
  `template_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '模板消息标题',
  `template_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板消息内容',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '伪删除标记',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_manager_id`(`manager_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模板消息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
