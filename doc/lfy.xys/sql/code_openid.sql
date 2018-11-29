/*
 Navicat Premium Data Transfer

 Source Server         : 120服务器
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 123.56.138.120
 Source Database       : renren-boot-new

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 04/26/2018 11:23:31 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `code_openid`
-- ----------------------------
DROP TABLE IF EXISTS `code_openid`;
CREATE TABLE `code_openid` (
  `openid` varchar(50) NOT NULL COMMENT '用户openid',
  `code` varchar(50) DEFAULT NULL COMMENT '上次用来获取openid的code',
  PRIMARY KEY (`openid`),
  KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='openid与code的关系表（每个openid只有一条数据）';

SET FOREIGN_KEY_CHECKS = 1;
