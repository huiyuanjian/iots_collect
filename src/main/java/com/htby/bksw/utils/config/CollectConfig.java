/**   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.htby.bksw.iots_collect.utils.config 
 * @author: zhouxidong   
 * @date: 2018年5月27日 下午2:33:31 
 */
package com.htby.bksw.utils.config;

import com.htby.bksw.entity.IotsIoserverInfoEntity;
import com.htby.bksw.entity.Message;
import com.htby.bksw.entity.regist.RegistInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/** 
 * @ClassName: CollectConfig
 * @Description: 关于物联网接口服务的配置
 * @author: zhouxidong
 * @date: 2018年5月27日 下午2:33:31  
 */
@Slf4j
public class CollectConfig {

	private CollectConfig(){};

	/**
	 * 物联网接口服务的服务状态
	 */
	public static CollectServerStatusEnum SERVERSTATUS = CollectServerStatusEnum.START_UP;

	/**
	 * 服务id
	 */
	public static String SERVERID;

	/**
	 * 服务名称
	 */
	public static String SERVERNAME;

	/**
	 * 服务说明
	 */
	public static String SERVERREMARK;

	/**
	 * 心跳间隔（单位是秒）
	 * 默认心跳间隔是60秒
	 */
	public static int SEND_PALPITATE_INTERVAL = 60;
	
	/**
	 * kafka 上的主题
	 * 存放采集到的数据的topic
	 */
	public static String KAFKA_TOPIC = null;

	/**
	 * 服务类型
	 */
	public static String SERVERTYPE = "COLLECT";

	/**
	 * mac地址
	 */
	public static String MACADDRESS = null;

	/**
	 * mqtt 注册 主题 broker1
	 */
	public static String MQTT_REGIST_TOPIC_1 = null;

	/**
	 * mqtt 配置 主题 broker1
	 */
	public static String MQTT_CONFIG_TOPIC_1 = null;

	/**
	 * mqtt 控制 主题 broker1
	 */
	public static String MQTT_CTRL_TOPIC_1 = null;

	/**
	 * mqtt 应答 主题 broker1
	 */
	public static String MQTT_ANSWER_TOPIC_1 = null;

	/**
	 * mqtt 状态 主题 broker1
	 */
	public static String MQTT_STATUS_TOPIC_1 = null;

	/**
	 * mqtt 日志 主题 broker1
	 */
	public static String MQTT_LOG_TOPIC_1 = null;

	/**
	 * mqtt 注册 主题 broker2
	 */
	public static String MQTT_REGIST_TOPIC_2 = null;

	/**
	 * mqtt 配置 主题 broker2
	 */
	public static String MQTT_CONFIG_TOPIC_2 = null;

	/**
	 * mqtt 控制 主题 broker2
	 */
	public static String MQTT_CTRL_TOPIC_2 = null;

	/**
	 * mqtt 应答web 主题 broker2
	 */
	public static String MQTT_ANSWER_WEB_TOPIC_2 = null;

	/**
	 * mqtt 应答control 主题 broker2
	 */
	public static String MQTT_ANSWER_CONTROL_TOPIC_2 = null;

	/**
	 * mqtt 状态 主题 broker2
	 */
	public static String MQTT_STATUS_TOPIC_2 = null;

	/**
	 * 采集代理注册的消息
	 * String 是mac地址
	 */
	public static Map<String, RegistInfo> REGIST_MAP = new HashMap<>();

	/**
	 * 本服务有权管理的采集代理
	 * String 是mac地址
	 */
	public static List<String> MACADDRESS_LIST = new ArrayList<>();

	/**
	 * 存放上传后的日志访问路径
	 */
	public static Map<String,Map<String,String>> MAP_LOGS = new HashMap<>();

}
