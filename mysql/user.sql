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

 Date: 29/10/2019 11:45:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `friends` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '好友列表',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `nameindex`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000009 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'all', '', NULL);
INSERT INTO `user` VALUES (1000006, 'lry', 'admin', 'all,xxx');
INSERT INTO `user` VALUES (1000007, 'xxx', 'admin', 'all,lry,ll');
INSERT INTO `user` VALUES (1000008, 'll', 'admin', 'all,xxx');

SET FOREIGN_KEY_CHECKS = 1;
