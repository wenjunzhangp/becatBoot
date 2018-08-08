/*
Navicat MySQL Data Transfer

Source Server         : 本地环境
Source Server Version : 50610
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2018-08-08 19:35:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for quartz_schedulejob
-- ----------------------------
DROP TABLE IF EXISTS `quartz_schedulejob`;
CREATE TABLE `quartz_schedulejob` (
  `schedule_job_id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_job_name` varchar(250) NOT NULL DEFAULT '0',
  `schedule_job_group_id` int(11) DEFAULT '0',
  `status` bit(1) NOT NULL DEFAULT b'0',
  `schedule_job_description` varchar(250) DEFAULT '0',
  `create_time` datetime NOT NULL,
  `schedule_job_cron_expression` varchar(250) NOT NULL DEFAULT '0',
  `schedule_job_method` varchar(250) NOT NULL,
  `schedule_job_class` varchar(250) NOT NULL,
  PRIMARY KEY (`schedule_job_id`),
  UNIQUE KEY `schedule_job_name` (`schedule_job_name`),
  KEY `schedule_job_group_id` (`schedule_job_group_id`),
  CONSTRAINT `schedule_job_group_id` FOREIGN KEY (`schedule_job_group_id`) REFERENCES `quartz_schedulejob_group` (`schedule_job_groupId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quartz_schedulejob
-- ----------------------------
INSERT INTO `quartz_schedulejob` VALUES ('20', '微信积分推送任务', '17', '\0', '微信积分推送任务', '2017-04-17 15:32:24', '0 0/1 * * * ? *', 'wechatPush', 'com.baozi.task.WeChatScheduleJob');
INSERT INTO `quartz_schedulejob` VALUES ('24', 'QQ新闻推送任务', '37', '\0', 'QQ新闻推送任务', '2018-08-08 09:55:29', '* 1-59 * * * ?', 'qqPush', 'com.baozi.task.QqScheduleJob');

-- ----------------------------
-- Table structure for quartz_schedulejob_group
-- ----------------------------
DROP TABLE IF EXISTS `quartz_schedulejob_group`;
CREATE TABLE `quartz_schedulejob_group` (
  `schedule_job_groupId` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_job_group_name` varchar(250) NOT NULL DEFAULT '0',
  `schedule_job_group_description` varchar(250) DEFAULT '0',
  `status` bit(1) NOT NULL DEFAULT b'1',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`schedule_job_groupId`),
  UNIQUE KEY `schedule_job_group_name` (`schedule_job_group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quartz_schedulejob_group
-- ----------------------------
INSERT INTO `quartz_schedulejob_group` VALUES ('17', '微信任务组', '微信任务组（推送优惠券、推送消息、每日早报）', '\0', '2018-08-03 14:18:14');
INSERT INTO `quartz_schedulejob_group` VALUES ('37', 'QQ任务组', 'QQ任务组（推送新闻资讯）', '\0', '2018-08-08 09:53:41');
