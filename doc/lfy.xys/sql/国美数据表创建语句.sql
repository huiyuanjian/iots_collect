drop table if exists Member_info;
create table Member_info
(
   member_id            int(10) not null  AUTO_INCREMENT comment '会员ID',
   member_name          varchar(40) comment '会员名称',
   account              varchar(40) comment '账号',
   pwd					varchar(40) comment '密码',
   wechat_id            varchar(40) comment '绑定微信号id',
   last_signday         datetime comment '最后签到日期',
   continuous           int(3) comment '连续签到次数',
   gold_amount          bigint(20) comment 'G豆',
   free_amount          int(5) comment '剩余的免费次数',
   memberImg			varchar(225) comment '会员头像',
   primary key (member_id)
) COMMENT='会员信息表';
 

drop table if exists Sign_history;
create table Sign_history
(
   id                   int(10) not null AUTO_INCREMENT ,
   member_id            int(10) comment '会员ID',
   sign_day             datetime comment '签到时间',
   primary key (id)
) comment '签到历史表';
 

drop table if exists Prize_record;
create table Prize_record
(
   record_id            int(10) not null AUTO_INCREMENT comment '记录ID',
   member_id            int(10) comment '会员ID',
   Prize_Name             varchar(100) comment '奖品名称',
   Prize_decript        varchar(200) comment '奖品描述',
   Prize_Gold        	int(10) default 0 comment '如果是g豆、话费、流量，则填写中奖金额',
   end_time				datetime comment '有效期截止时间',
   img_path      		varchar(200) comment '图片地址',
   contacts_name	varchar(40) COMMENT '联系人名字',
   tel	varchar(20) COMMENT '手机号码',
   postcode	varchar(10) COMMENT '邮政编码',
   country	varchar(10) COMMENT '国家',
   province	varchar(10) COMMENT '省份',
   city	varchar(10) COMMENT '城市',
   detailed_addr	varchar(100) COMMENT '详细地址信息',
   express varchar(50) COMMENT '快递单号',
   prize_type	int(1) COMMENT '奖品类型。0：谢谢惠顾，1：话费，2：流量，3：G豆，4：实物，5:代金券',
   receive     			int(1) comment '是否领取 0 未领取  1 已领取',
   create_time          datetime comment '中奖时间，数据创建时间',
   record_status        int(1) comment '是否删除，0 未删除 1 已删除',
   primary key (record_id)
) comment '中奖记录表';
 
drop table if exists Prize_pool_info;
create table Prize_pool_info
(
   Pool_id              int(10) not null AUTO_INCREMENT  comment '奖池ID',
   indexs               int(10) comment '排序',
   Prize_id             int(10) comment '奖品ID',
   pool_switch          int(2) comment '开关。0：否，关闭，1：是，启动。 每次最多启动8个奖品。',
   probability          decimal(6,6) comment '中奖概率',
   creator              int(10) comment '创建者',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
      primary key (Pool_id)

) comment '设置的奖池表，admin可以配置。';

 
drop table if exists Prize_info;
create table Prize_info
(
   Prize_id             int(10) not null AUTO_INCREMENT comment '奖品ID',
   Prize_name           varchar(40) comment '奖品名称',
   Prize_decript        varchar(200) comment '奖品描述',
   Prize_Gold        	int(10) comment '如果是g豆、话费、流量，则填写中奖金额',
   Prize_type           int(10) comment '奖品类型。',
   prize_link           varchar(225) comment '如果类型是1，则有链接',
   prize_amount         int(10) comment '奖品数量',
   end_time				datetime comment '有效期截止时间',
   img_path      		varchar(500) comment '图片地址',
   indexs               int(10) comment '排序',
   creator              int(10) comment '创建者',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (Prize_id)
) comment '奖品表';

drop table if exists Receipt_info;
create table Receipt_info
(
   Receipt_id             int(10) not null AUTO_INCREMENT comment 'ID',
   member_id         int(10)comment '会员id',
   contacts_name        varchar(40) comment '联系人名字',
   tel           varchar(15) comment '手机号码',
   postcode            varchar(10) comment '邮政编码',
   country         varchar(50) comment '国家',
   province           varchar(50)  comment '省份',
   city               varchar(50)  comment '城市',
   detailed_addr   varchar(100) comment '详细地址信息',
   default_addr  int(2) comment '是否默认地址，0：否，1：是',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (Receipt_id)
) comment '收货地址表';