/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : address_book

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2018-07-23 11:36:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bureau
-- ----------------------------
DROP TABLE IF EXISTS `bureau`;
CREATE TABLE `bureau` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `duty` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bureau_county` (`cid`),
  CONSTRAINT `bureau_county` FOREIGN KEY (`cid`) REFERENCES `county` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `did` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `office` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `latitude` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `longitude` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `duty` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `contact_department` (`did`),
  CONSTRAINT `contact_department` FOREIGN KEY (`did`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for county
-- ----------------------------
DROP TABLE IF EXISTS `county`;
CREATE TABLE `county` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `duty` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_bureau` (`bid`),
  CONSTRAINT `department_bureau` FOREIGN KEY (`bid`) REFERENCES `bureau` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=509 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wrong
-- ----------------------------
DROP TABLE IF EXISTS `wrong`;
CREATE TABLE `wrong` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `did` int(11) DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '错误类型',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '内容',
  `time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`id`),
  KEY `wrong_department` (`did`),
  CONSTRAINT `wrong_department` FOREIGN KEY (`did`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
