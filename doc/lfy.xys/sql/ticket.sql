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

 Date: 04/26/2018 11:23:47 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ticket`
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `jssdk_ticket` varchar(225) NOT NULL COMMENT '票证',
  `expires_in` int(4) DEFAULT '7200' COMMENT '有效期 （单位是秒）',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `appid` varchar(50) NOT NULL COMMENT 'appid',
  PRIMARY KEY (`appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ticket票证表（只允许存在一条信息）';

SET FOREIGN_KEY_CHECKS = 1;
