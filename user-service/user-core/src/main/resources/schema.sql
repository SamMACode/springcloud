# 创建用户表user_info
create table `user_info` (
  `id` varchar(32) not null,
  `username` varchar(32) default '',
  `password` varchar(32) default '',
  `openid` varchar(64) default '' comment '微信登录的openId',
  `role` tinyint(1) not null comment '1买家2卖家',
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key(`id`)
) engine=InnoDB default charset=utf8;

insert into user_info(id, username, password, openid, role)
    values('1234555', 'ioio', 'opopo', 'buyerabc', '1'),
          ('1234557', 'ioioho', 'onmpopo', 'sellerxyz', '2');