/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50527
 Source Host           : localhost:3306
 Source Schema         : lry

 Target Server Type    : MySQL
 Target Server Version : 50527
 File Encoding         : 65001

 Date: 29/10/2019 11:45:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发送内容',
  `send_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发送者',
  `receive_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接收者',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `is_delete` int(1) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of msg
-- ----------------------------
INSERT INTO `msg` VALUES (84, '请求添加好友', 'll', 'xxx', '2019-10-29 10:14:57', 1);
INSERT INTO `msg` VALUES (85, 'sdaffd', 'll', 'xxx', '2019-10-29 10:15:33', 0);
INSERT INTO `msg` VALUES (86, 'sdfsgdgf', 'll', 'xxx', '2019-10-29 10:15:44', 0);
INSERT INTO `msg` VALUES (87, 'asdfaf', 'xxx', 'll', '2019-10-29 10:15:57', 0);

SET FOREIGN_KEY_CHECKS = 1;
