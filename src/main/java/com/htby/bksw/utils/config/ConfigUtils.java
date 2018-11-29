package com.htby.bksw.utils.config;

import com.htby.bksw.entity.common.CommonConfig;
import com.htby.bksw.utils.fileIO.FileUtils;

public class ConfigUtils {

    /**
     * 物联网接口服务的服务状态变化关系
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    public CollectServerStatusEnum change(boolean b){
        switch (CollectConfig.SERVERSTATUS){
            case START_UP:
                CollectConfig.SERVERSTATUS = CollectServerStatusEnum.READ_LOCAL_CONFIGURATION;
                break;
            case READ_LOCAL_CONFIGURATION:
                if(b){
                    CollectConfig.SERVERSTATUS = CollectServerStatusEnum.SERVICE_START;
                }else {
                    CollectConfig.SERVERSTATUS = CollectServerStatusEnum.FAILED_READ_LOCAL_CONFIGURATION;
                }
                break;
            case FAILED_READ_LOCAL_CONFIGURATION:
                CollectConfig.SERVERSTATUS = CollectServerStatusEnum.WAITING_FOR_WEB_CONNECTION;
                break;
            case WAITING_FOR_WEB_CONNECTION:
                if(b){
                    CollectConfig.SERVERSTATUS = CollectServerStatusEnum.CONNECT_TO_WEB;
                }
                break;
            case CONNECT_TO_WEB:
                CollectConfig.SERVERSTATUS = CollectServerStatusEnum.WAITING_FOR_DISPATCH_CONFIGURATION;
                break;
            case WAITING_FOR_DISPATCH_CONFIGURATION:
                if(b){
                    CollectConfig.SERVERSTATUS = CollectServerStatusEnum.CONFIGURATION_SUCCESS;
                }
                break;
            case CONFIGURATION_SUCCESS:
                if (b){
                    CollectConfig.SERVERSTATUS = CollectServerStatusEnum.SERVICE_START;
                }else {
                    CollectConfig.SERVERSTATUS = CollectServerStatusEnum.SERVICE_FAILED;
                }
                break;
            default:
                break;
        }
        return CollectConfig.SERVERSTATUS;
    }

    /**
     * 更新config
     */
    public void setConfig(ConfigInfo configInfo){

        CollectConfig.SERVERID = configInfo.getServer_id();// 服务id
        CollectConfig.SEND_PALPITATE_INTERVAL = configInfo.getSend_palpitate_interval();// 心跳topic
        CollectConfig.SERVERTYPE = configInfo.getServer_type();// 服务类型
        CollectConfig.KAFKA_TOPIC = configInfo.getKafka_topic();// 在kafka上的topic
        CollectConfig.MACADDRESS_LIST = configInfo.getMacaddress_list();// 管辖的采集代理

        CollectConfig.SERVERNAME = configInfo.getServer_name();// 服务名称
        CollectConfig.SERVERREMARK = configInfo.getServer_remark();// 服务说明
        CollectConfig.MACADDRESS = configInfo.getMac_address();// mac地址


        // 持久化配置信息到文件中
        FileUtils fileUtils = new FileUtils();
        fileUtils.saveLoginInfo(getConfigFromControl());
    }

    /**
     * 更新config
     */
    public void setCommonConfig( CommonConfig commonConfig){

        FileUtils fileUtils = new FileUtils();
        ConfigInfo configInfo = fileUtils.readLoginInfo();
        if(configInfo != null){
            configInfo.setMacaddress_list(commonConfig.getMac_address_list());// 管理的采集代理mac地址集合
            configInfo.setSend_palpitate_interval(commonConfig.getSend_palpitate_interval());// 心跳间隔
            configInfo.setServer_id(commonConfig.getServer_id());// 服务id
            configInfo.setServer_type(commonConfig.getServer_type());// 服务类型
            configInfo.setServer_name(commonConfig.getServer_name());// 服务名称

            configInfo.setServer_remark(commonConfig.getServer_remark());// 服务说明
            configInfo.setMac_address(commonConfig.getServer_mac());// mac地址
            configInfo.setKafka_topic(commonConfig.getKafka_topic());// kafka主题
            // 更新
            setConfig(configInfo);
        } else {
            CollectConfig.SEND_PALPITATE_INTERVAL = commonConfig.getSend_palpitate_interval();
            CollectConfig.MACADDRESS_LIST = commonConfig.getMac_address_list();
            CollectConfig.SERVERID = commonConfig.getServer_id();
            CollectConfig.SERVERTYPE = commonConfig.getServer_type();
            CollectConfig.SERVERNAME = commonConfig.getServer_name();// 服务名称

            CollectConfig.SERVERREMARK = commonConfig.getServer_remark();// 服务说明
            CollectConfig.MACADDRESS = commonConfig.getServer_mac();// mac地址
            CollectConfig.KAFKA_TOPIC = commonConfig.getKafka_topic();// kafka主题

            // 持久化
            fileUtils.saveLoginInfo(getConfigFromControl());
        }
    }

    /**
     * 从内存中读取配置信息
     */
    public ConfigInfo getConfigFromControl(){
        ConfigInfo configInfo = new ConfigInfo();

        configInfo.setSend_palpitate_interval(CollectConfig.SEND_PALPITATE_INTERVAL);
        configInfo.setMqtt_status_topic_2(CollectConfig.MQTT_STATUS_TOPIC_2);
        configInfo.setMqtt_status_topic_1(CollectConfig.MQTT_STATUS_TOPIC_1);
        configInfo.setMqtt_regist_topic_2(CollectConfig.MQTT_REGIST_TOPIC_2);
        configInfo.setMqtt_regist_topic_1(CollectConfig.MQTT_REGIST_TOPIC_1);

        configInfo.setMqtt_ctrl_topic_2(CollectConfig.MQTT_CTRL_TOPIC_2);
        configInfo.setMqtt_ctrl_topic_1(CollectConfig.MQTT_CTRL_TOPIC_1);
        configInfo.setMqtt_config_topic_2(CollectConfig.MQTT_CONFIG_TOPIC_2);
        configInfo.setMqtt_config_topic_1(CollectConfig.MQTT_CONFIG_TOPIC_1);
        configInfo.setMqtt_answer_web_topic_2(CollectConfig.MQTT_ANSWER_WEB_TOPIC_2);

        configInfo.setMqtt_answer_topic_1(CollectConfig.MQTT_ANSWER_TOPIC_1);
        configInfo.setMqtt_answer_control_topic_2(CollectConfig.MQTT_ANSWER_CONTROL_TOPIC_2);
        configInfo.setRegist_map(CollectConfig.REGIST_MAP);
        configInfo.setServer_remark(CollectConfig.SERVERREMARK);
        configInfo.setServer_name(CollectConfig.SERVERNAME);

        configInfo.setServer_type(CollectConfig.SERVERTYPE);
        configInfo.setKafka_topic(CollectConfig.KAFKA_TOPIC);
        configInfo.setMac_address(CollectConfig.MACADDRESS);
        configInfo.setServer_id(CollectConfig.SERVERID);
        configInfo.setMacaddress_list(CollectConfig.MACADDRESS_LIST);

        configInfo.setRegist_map(CollectConfig.REGIST_MAP);
        configInfo.setMap_logs(CollectConfig.MAP_LOGS);
        return configInfo;
    }
}
