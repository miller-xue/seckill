 -- 数据库初始化脚本

-- 创建数据库

CREATE DATABASE seckill;
-- 使用数据库
use seckill

-- 创建秒杀库存表
CREATE Table seckill(
  seckill_id bigint not null AUTO_INCREMENT comment '商品库存id',
  name VARCHAR(120)  NOT NULL COMMENT '商品名称',
  number int  not null comment '库存熟练',
  start_time TIMESTAMP  not null comment '秒杀开启时间',
  end_time TIMESTAMP  not null comment '秒杀结束时间',
  create_time TIMESTAMP not null  DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  primary key(seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8 comment='秒杀库存表'

insert into seckill(name,number,start_time,end_time)
values
('1000元秒杀iphone6','100','2018-03-28 00:00:00','2018-03-29 00:00:00'),
('2元秒杀ipad','200','2018-03-28 00:00:00','2018-03-29 00:00:00'),
('999元秒杀越南新娘','2','2018-03-28 00:00:00','2018-03-29 00:00:00'),
('300元秒杀小米4','400','2018-03-28 00:00:00','2018-03-29 00:00:00'),
('200秒杀大疆','300','2018-03-28 00:00:00','2018-03-29 00:00:00');


-- 秒杀成果明细表
-- 用户登陆认证的相关信息
create table success_killed(
  seckill_id bigint not null comment'秒杀商品ID',
  user_phone bigint not null comment '用户手机号',
  state tinyint not null default -1 comment '状态标示  -1:无效 0:成功 1:已付款 2:已发货',
  create_time TIMESTAMP not null  comment '创建时间',
  PRIMARY key (seckill_id,user_phone)/*联合主键*/,
  key idx_create_time(create_time)
)ENGINE = InnoDB  DEFAULT CHARSET = utf8 comment='秒杀成功明细表'


mysql -u root -p root