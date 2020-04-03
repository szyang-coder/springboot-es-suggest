CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `term` varchar(64) NOT NULL COMMENT '产品名称',
  `keyword` varchar(32) DEFAULT '' COMMENT '关键词',
  `suggest` varchar(256) DEFAULT '' COMMENT '搜索建议，$分割',
  `clickUrl` varchar(512) DEFAULT '' COMMENT '点击url',
  `imageUrl` varchar(512) DEFAULT '' COMMENT '图片url',
  `impressionUrl` varchar(512) DEFAULT '' COMMENT '',
  `labelRequired` varchar(512) DEFAULT '' COMMENT '',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品信息表';