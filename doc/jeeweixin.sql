/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : jeeweixin

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2016-02-19 22:13:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_cms_sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_cms_sys_user`;
CREATE TABLE `t_cms_sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `realname` varchar(50) NOT NULL,
  `gender` int(1) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `birthday` varchar(50) DEFAULT NULL,
  `headImage` varchar(250) DEFAULT NULL,
  `enable` int(5) DEFAULT NULL,
  `ruleid` int(5) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `openid` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `loginname` (`loginname`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_cms_sys_user
-- ----------------------------
INSERT INTO `t_cms_sys_user` VALUES ('1', 'jeeweixin', '5RpuDkfdbTs1ctwfT6MurA==', '管理员', '1', null, null, null, null, '1', null, '2014-12-20 22:58:30', null);

-- ----------------------------
-- Table structure for `t_wxcms_account`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_account`;
CREATE TABLE `t_wxcms_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `account` varchar(100) NOT NULL,
  `appid` varchar(100) DEFAULT NULL,
  `appsecret` varchar(100) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `msgCount` int(11) DEFAULT '1',
  `createTime` datetime DEFAULT NULL,
  `referMoneyMin` double NOT NULL DEFAULT '0',
  `referMoneyMax` double NOT NULL DEFAULT '0',
  `initSendMoneyMax` double NOT NULL DEFAULT '0',
  `tixianMinMoney` double NOT NULL DEFAULT '0' COMMENT '最少提现金额',
  `initSendMoneyMin` double NOT NULL DEFAULT '0',
  `serverAppid` varchar(255) DEFAULT NULL,
  `serverAppsecret` varchar(255) DEFAULT NULL,
  `yaoyiyaoSendGoldCoinMin` double(20,4) NOT NULL,
  `yaoyiyaoSendGoldCoinMax` double(20,4) NOT NULL,
  `yaoyiyaoPercent` double(5,0) NOT NULL DEFAULT '0' COMMENT '摇一摇中奖概率',
  `goldCoinToMoney` double(20,2) NOT NULL DEFAULT '1.00' COMMENT '多少个金币兑换1元RMB',
  `giveYaoyiyaoTimesPerRefer` int(20) NOT NULL DEFAULT '0' COMMENT '推荐一个下线赠送摇一摇的次数',
  `taskProfit` double(20,4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_account
-- ----------------------------
INSERT INTO `t_wxcms_account` VALUES ('3', null, 'gh_4626d549a199', 'wxef58e82100eae819', '44f317467451623026e458e8070737e2', 'http://localhost/wxapi/gh_4626d549a199/message.html', '72597b9628704ab09e8b9e8cbe9b540a', '5', '2015-01-27 21:38:31', '5', '23', '33', '60', '32', 'wxef58e82100eae819', '44f317467451623026e458e8070737e2', '10.0000', '200.0000', '50', '100.00', '10', '20.0000');

-- ----------------------------
-- Table structure for `t_wxcms_account_fans`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_account_fans`;
CREATE TABLE `t_wxcms_account_fans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) DEFAULT NULL,
  `subscribeStatus` int(1) DEFAULT '1',
  `subscribeTime` varchar(50) DEFAULT NULL,
  `nickname` varbinary(50) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT '1',
  `language` varchar(50) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `province` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `headimgurl` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `remark` varchar(50) DEFAULT NULL,
  `wxid` varchar(50) DEFAULT NULL,
  `userMoney` double(20,2) NOT NULL DEFAULT '0.00',
  `userMoneyFreezed` double(20,2) NOT NULL DEFAULT '0.00',
  `lastUpdateTime` datetime DEFAULT NULL,
  `userMoneyPassword` varchar(40) DEFAULT NULL COMMENT '用户提现密码。默认为空',
  `userReferId` int(11) NOT NULL DEFAULT '0' COMMENT '推荐人编号',
  `mediaId` varchar(250) DEFAULT NULL,
  `userMoneyTixian` double(20,2) NOT NULL DEFAULT '0.00' COMMENT '用户提现金额累计',
  `userLevel1` int(11) NOT NULL DEFAULT '0',
  `userLevel2` int(11) NOT NULL DEFAULT '0',
  `userLevel3` int(11) NOT NULL DEFAULT '0',
  `headImgBlob` mediumblob,
  `recommendImgBlob` mediumblob,
  `userGoldCoin` double(20,2) NOT NULL DEFAULT '0.00' COMMENT '用户积分',
  `userYaoyiyaoTimesUesd` int(11) NOT NULL DEFAULT '0' COMMENT '用户使用过摇一摇的次数',
  `userGiveYaoyiyaoTimes` int(11) NOT NULL DEFAULT '0',
  `recommendImgCreateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `open_id_union` (`openid`) USING HASH,
  KEY `open_id_idx` (`openid`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_account_fans
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_account_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_account_menu`;
CREATE TABLE `t_wxcms_account_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mtype` varchar(50) DEFAULT NULL,
  `eventType` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `inputCode` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `msgId` varchar(100) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `gid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_account_menu
-- ----------------------------
INSERT INTO `t_wxcms_account_menu` VALUES ('4', 'click', 'key', '我的海报', 'my_refer', '', '1', '0', '', '2016-01-17 08:53:17', '2');
INSERT INTO `t_wxcms_account_menu` VALUES ('17', 'view', 'key', '提现排行', 'http://sunny88.ngrok.cc/wxapi/tixian_top.html', 'http://sunny88.ngrok.cc/wxapi/tixian_top.html', '3', '0', '', '2016-01-19 00:02:45', '2');
INSERT INTO `t_wxcms_account_menu` VALUES ('19', 'view', 'key', '个人中心', '', 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxef58e82100eae819&redirect_uri=http%3A%2F%2Fsunny88.ngrok.cc%2Fwxapi%2Fmy_center.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect', '2', '0', '', '2016-01-26 22:39:29', '2');
INSERT INTO `t_wxcms_account_menu` VALUES ('20', 'click', 'key', '我的海报', 'my_img_refer', '', '1', '0', '', '2016-01-31 01:17:45', '3');
INSERT INTO `t_wxcms_account_menu` VALUES ('23', 'view', 'key', '排行榜', '', 'http://sunny88.ngrok.cc/wxapi/tixian_top.html', '1', '0', '', '2016-01-31 01:24:02', '3');
INSERT INTO `t_wxcms_account_menu` VALUES ('25', 'click', 'key', '会员中心', 'my_center_sub', '', '1', '0', '', '2016-02-01 12:33:19', '3');
INSERT INTO `t_wxcms_account_menu` VALUES ('29', 'click', 'key', '个人中心', 'my_center_sub', '', '1', '25', '', '2016-02-05 23:38:14', '3');
INSERT INTO `t_wxcms_account_menu` VALUES ('30', 'click', 'key', '摇一摇', 'my_yaoyiyao_sub', '', '1', '25', '', '2016-02-05 23:58:30', '3');

-- ----------------------------
-- Table structure for `t_wxcms_account_menu_group`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_account_menu_group`;
CREATE TABLE `t_wxcms_account_menu_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_account_menu_group
-- ----------------------------
INSERT INTO `t_wxcms_account_menu_group` VALUES ('2', '潮北京', '1', '2016-01-17 08:52:53');
INSERT INTO `t_wxcms_account_menu_group` VALUES ('3', '订阅号', '0', '2016-01-31 01:17:21');

-- ----------------------------
-- Table structure for `t_wxcms_fans_tixian`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_fans_tixian`;
CREATE TABLE `t_wxcms_fans_tixian` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fansId` int(11) NOT NULL,
  `alipayName` varchar(20) NOT NULL,
  `alipayEmail` varchar(50) NOT NULL,
  `tixianMoney` double(11,2) NOT NULL,
  `tixianStatus` int(4) NOT NULL DEFAULT '0' COMMENT '0为等待提现，1为已经提现，-1为提现失败返回。',
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_fans_tixian
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_flow`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_flow`;
CREATE TABLE `t_wxcms_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userFlowMoney` double(11,2) NOT NULL,
  `createtime` datetime NOT NULL,
  `userFlowLog` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fansId` int(11) NOT NULL,
  `fromFansId` int(11) NOT NULL DEFAULT '0',
  `flowType` int(11) NOT NULL DEFAULT '0' COMMENT '1为推广红包，2为关注红包，3为取消关注扣除红包,4为提现记录',
  `userFlowLogBinary` varbinary(1000) DEFAULT NULL,
  `userFlowGoldCoin` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '金币数量',
  PRIMARY KEY (`id`),
  KEY `fans_id_idx` (`fansId`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=423 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_flow
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_msg_base`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_msg_base`;
CREATE TABLE `t_wxcms_msg_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgType` varchar(20) DEFAULT NULL,
  `inputCode` varchar(20) DEFAULT NULL,
  `rule` varchar(20) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `readCount` int(11) DEFAULT '0',
  `favourCount` int(11) unsigned zerofill DEFAULT '00000000000',
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_msg_base
-- ----------------------------
INSERT INTO `t_wxcms_msg_base` VALUES ('2', 'news', '1', null, null, null, null, '2015-03-21 11:19:48');
INSERT INTO `t_wxcms_msg_base` VALUES ('5', 'text', '8', null, null, null, null, '2016-01-17 09:29:59');
INSERT INTO `t_wxcms_msg_base` VALUES ('6', 'news', '8', null, null, null, null, '2016-01-17 09:31:26');
INSERT INTO `t_wxcms_msg_base` VALUES ('7', 'news', '8', null, null, null, null, '2016-01-17 09:39:48');
INSERT INTO `t_wxcms_msg_base` VALUES ('8', 'text', '8', null, null, null, null, '2016-01-17 10:07:57');
INSERT INTO `t_wxcms_msg_base` VALUES ('9', 'text', 'subscribe_reward_log', null, null, null, null, '2016-01-28 22:03:58');

-- ----------------------------
-- Table structure for `t_wxcms_msg_news`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_msg_news`;
CREATE TABLE `t_wxcms_msg_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `brief` varchar(255) DEFAULT NULL,
  `description` longtext,
  `picPath` varchar(255) DEFAULT NULL,
  `showPic` int(11) DEFAULT '0',
  `url` varchar(255) DEFAULT NULL,
  `fromurl` varchar(255) DEFAULT NULL,
  `base_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_msg_news
-- ----------------------------
INSERT INTO `t_wxcms_msg_news` VALUES ('1', '为什么是jeeweixin', 'jeeweixin', '', '', 'http://www.jeeweixin.com/res/upload/1426908565922.jpg', '1', 'http://www.weixinpy.com/', '', '2');
INSERT INTO `t_wxcms_msg_news` VALUES ('2', '微信开发教程', 'jeeweixin', '', '', 'http://www.jeeweixin.com/res/upload/1426908381642.jpg', null, '', 'http://www.weixinpy.com/', '3');

-- ----------------------------
-- Table structure for `t_wxcms_msg_text`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_msg_text`;
CREATE TABLE `t_wxcms_msg_text` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `base_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_msg_text
-- ----------------------------
INSERT INTO `t_wxcms_msg_text` VALUES ('2', 'fhtgyutyubhgyu', '4');
INSERT INTO `t_wxcms_msg_text` VALUES ('5', '你已经获得了#{money}元，满#{tixianMinMoney}元就可以提现了，点击我的海报生成海报发送给您的好友赚更多。', '9');

-- ----------------------------
-- Table structure for `t_wxcms_options`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_options`;
CREATE TABLE `t_wxcms_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createtime` datetime DEFAULT NULL,
  `referMoneyMin` double NOT NULL DEFAULT '0',
  `referMoneyMax` double NOT NULL DEFAULT '0',
  `initSendMoneyMax` double NOT NULL DEFAULT '0',
  `tixianMinMoney` double NOT NULL DEFAULT '0' COMMENT '最少提现金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_options
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_task_code`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_task_code`;
CREATE TABLE `t_wxcms_task_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wxCodeImgHref` varchar(255) NOT NULL,
  `wxRemark` varbinary(255) DEFAULT NULL,
  `userId` bigint(20) NOT NULL COMMENT '所属用户编号',
  `moneyPer` double(11,2) NOT NULL DEFAULT '0.00',
  `validateMenuUrl` varchar(255) NOT NULL COMMENT '验证收款的URL',
  `createtime` datetime DEFAULT NULL,
  `base64WxCode` text COMMENT '二维码的base64图片信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_task_code
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_task_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_task_log`;
CREATE TABLE `t_wxcms_task_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log` varbinary(255) NOT NULL,
  `money` double(11,2) NOT NULL,
  `openId` varchar(50) NOT NULL,
  `createtime` datetime NOT NULL,
  `taskId` bigint(20) NOT NULL COMMENT '二维码编号',
  `taskCodeNum` varchar(20) NOT NULL,
  `taskStatus` int(8) NOT NULL DEFAULT '0' COMMENT '0表示已经申请，1表示已完成，-1表示已经过期。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_user`;
CREATE TABLE `t_wxcms_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createtime` datetime NOT NULL,
  `userEmail` varchar(20) NOT NULL,
  `userPassword` varchar(32) NOT NULL,
  `userMoney` double(11,2) NOT NULL DEFAULT '0.00',
  `userForzenedMoney` double(11,2) NOT NULL,
  `userMoneyPassword` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_user
-- ----------------------------

-- ----------------------------
-- Table structure for `t_wxcms_user_flow`
-- ----------------------------
DROP TABLE IF EXISTS `t_wxcms_user_flow`;
CREATE TABLE `t_wxcms_user_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userFlowMoney` double(11,2) NOT NULL,
  `createtime` datetime NOT NULL,
  `userId` int(11) NOT NULL,
  `flowType` int(11) NOT NULL DEFAULT '0' COMMENT '1为充值，2为提现，3为有人关注做了任务扣除金额。4为提现成功冻结金额',
  `userFlowLogBinary` varbinary(1000) DEFAULT NULL,
  `userFlowGoldCoin` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '金币数量',
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`userId`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wxcms_user_flow
-- ----------------------------
