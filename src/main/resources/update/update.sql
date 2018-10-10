alter table cms_log  modify column type varchar(100);

alter table wx_fans add sort bigint(20);

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for wx_tpl_msg_sub
-- ----------------------------
DROP TABLE IF EXISTS `wx_tpl_msg_sub`;
CREATE TABLE `wx_tpl_msg_sub` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `tpl_id` varchar(50) DEFAULT NULL COMMENT '模板ID',
  `name` varchar(100) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL COMMENT '消息标题',
  `create_date` datetime DEFAULT NULL COMMENT 'create_date',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板消息';

-- ----------------------------
-- Table structure for wx_tpl_msg_text
-- ----------------------------
DROP TABLE IF EXISTS `wx_tpl_msg_text`;
CREATE TABLE `wx_tpl_msg_text` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `tpl_id` varchar(50) DEFAULT NULL COMMENT '模板ID',
  `title` varchar(30) DEFAULT NULL COMMENT '消息标题',
  `content` longtext COMMENT '消息内容',
  `wx_tpl` varchar(200) DEFAULT NULL COMMENT '微信模板',
  `base_id` varchar(64) DEFAULT NULL COMMENT '消息主表id',
  `account` varchar(100) DEFAULT NULL COMMENT 'account',
  `create_date` datetime DEFAULT NULL COMMENT 'create_date',
  `url` varchar(200) DEFAULT NULL COMMENT '访问地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板消息';
