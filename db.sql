CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `name` varchar(64) NOT NULL COMMENT '产品名称',
  `partner` varchar(32) DEFAULT '' COMMENT '合作伙伴',
  `sub` varchar(32) DEFAULT '' COMMENT '渠道',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品信息表';

insert into product(name,partner,sub) values('iphone 3g','umecps','test');
insert into product(name,partner,sub) values('iphone 3gs','umecps','test');
insert into product(name,partner,sub) values('iphone 4','umecps','test');
insert into product(name,partner,sub) values('iphone 4s','umecps','test');
insert into product(name,partner,sub) values('iphone 5','umecps','test');
insert into product(name,partner,sub) values('iphone 5s','umecps','test');
insert into product(name,partner,sub) values('iphone 8','umecps','test');
insert into product(name,partner,sub) values('iphone 8 plus','umecps','test');
insert into product(name,partner,sub) values('iphone x','umecps','test');
insert into product(name,partner,sub) values('iphone xs','umecps','test');
insert into product(name,partner,sub) values('iphone xs max','umecps','test');
insert into product(name,partner,sub) values('iphone 11','umecps','test');
insert into product(name,partner,sub) values('iphone 11 pro','umecps','test');
insert into product(name,partner,sub) values('iphone 11 pro max','umecps','test');

insert into product(name,partner,sub) values('iphone 8','other','test');
insert into product(name,partner,sub) values('iphone 8 plus','other','test');
insert into product(name,partner,sub) values('iphone x','other','test');
insert into product(name,partner,sub) values('iphone xs','other','test');
insert into product(name,partner,sub) values('iphone xs max','other','test');
insert into product(name,partner,sub) values('iphone 11','other','test');
insert into product(name,partner,sub) values('iphone 11 pro','other','test');
insert into product(name,partner,sub) values('iphone 11 pro max','other','test');

insert into product(name,partner,sub) values('shoe','umecps','test');
insert into product(name,partner,sub) values('shoe maker','umecps','test');
insert into product(name,partner,sub) values('shoe one','umecps','test');
insert into product(name,partner,sub) values('shoe store','umecps','test');

insert into product(name,partner,sub) values('book','umecps','test');
insert into product(name,partner,sub) values('book shelf','umecps','test');
insert into product(name,partner,sub) values('book online','umecps','test');