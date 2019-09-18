# 1.创建订单主表order_master,包括订单买家的基本信息.
create table order_master (
  `order_id` varchar(32) not null,
  `buyer_name` varchar(32) not null comment '买家姓名',
  `buyer_phone` varchar(32) not null comment '卖家联系方式',
  `buyer_address` varchar(128) not null comment '买家地址',
  `buyer_openid` varchar(64) not null comment '买家微信的openid',
  `order_amount` decimal(8,2) not null comment '订单总金额',
  `order_status` tinyint(3) not null comment '订单的状态',
  `pay_status` tinyint(3) not null comment '订单支付的状态',
  `create_time` timestamp not null default current_timestamp comment '订单的创建时间',
  `update_time` timestamp not null default current_timestamp on update current_timestamp comment '订单修改时间',
  primary key (`order_id`),
  key `idx_buyer_openid` (`buyer_openid`)
) engine=InnoDB default charset=utf8;

# 2.创建订单明细信息表order_detail.
create table order_detail (
  `detail_id` varchar(32) not null,
  `order_id` varchar(32) not null,
  `product_id` varchar(32) not null,
  `product_name` varchar(64) not null comment '商品名称',
  `product_price` decimal(8,2) not null comment '商品的价格,单位到分',
  `product_quantity` int not null comment '商品的数量',
  `product_icon` varchar(512) comment '商品的小图',
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time` timestamp not null default current_timestamp on update current_timestamp comment '订单修改时间',
  primary key (`detail_id`),
  key `idx_order_id` (`order_id`)
) engine=InnoDB default charset=utf8;
