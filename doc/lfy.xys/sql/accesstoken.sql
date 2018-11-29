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

 Date: 04/26/2018 11:23:24 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `accesstoken`
-- ----------------------------
DROP TABLE IF EXISTS `accesstoken`;
CREATE TABLE `accesstoken` (
  `access_token` varchar(50) NOT NULL COMMENT '基础信息access_token',
  `expires_in` int(4) DEFAULT '7200' COMMENT '有效期 （单位是秒）',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `appid` varchar(50) NOT NULL COMMENT 'appid',
  PRIMARY KEY (`appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='access_token信息表（该表中只允许一条数据）';

SET FOREIGN_KEY_CHECKS = 1;
