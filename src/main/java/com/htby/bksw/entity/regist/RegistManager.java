package com.htby.bksw.entity.regist;

import com.htby.bksw.utils.mqtt2.MqttUtils2;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册信息管理类
 */
@Slf4j
public class RegistManager {

    /**
     * 采集代理注册的消息
     * String 是mac地址
     */
    private static Map<String,RegistInfo> REGIST_MAP = new HashMap<>();

    /**
     * 添加到本地配置
     */
    public static void put(RegistInfo registInfo, MqttMessage mqttMessage){
        // 放进管理容器中
        REGIST_MAP.put(registInfo.getMacAddress(),registInfo);
        // 转发到broker2上
        MqttUtils2 mqttUtils2 = new MqttUtils2();
        mqttUtils2.publish("REGIST/PROXY/" + registInfo.getMacAddress(),mqttMessage);
    }

    /**
     * 获得注册信息集合
     */
    public static Map<String,RegistInfo> getRegistMap(){
        return REGIST_MAP;
    }

}
