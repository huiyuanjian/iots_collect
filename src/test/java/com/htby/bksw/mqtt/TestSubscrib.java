package com.htby.bksw.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.Message;
import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.mqtt.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class TestSubscrib {

    public static void main(String args[]){
        // 注册信息
        Message message = new Message();
        message.setMsg_id("BROKER1_COLLECT_" + String.valueOf(System.currentTimeMillis())); // 消息id
        message.setSource_mac(CollectConfig.MACADDRESS); // mac地址
        message.setMsg_type(1); // 注册信息
        message.setSource_type("COLLECT"); // 服务类型
        message.setCreate_time(System.currentTimeMillis()); // 时间戳

        // mqtt消息
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setRetained(true);
        mqttMessage.setQos(1);
        mqttMessage.setPayload((JSONObject.toJSONString(message)).getBytes());

        // 发布
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.publish(CollectConfig.MQTT_REGIST_TOPIC_1,mqttMessage); // 注册消息
        mqttUtils.publish(CollectConfig.MQTT_CONFIG_TOPIC_1,mqttMessage); // 配置
        mqttUtils.publish(CollectConfig.MQTT_ANSWER_TOPIC_1,mqttMessage); // 应答
        mqttUtils.publish(CollectConfig.MQTT_CTRL_TOPIC_1,mqttMessage); // 控制
        mqttUtils.publish(CollectConfig.MQTT_STATUS_TOPIC_1,mqttMessage); // 服务状态


        // 测试代码
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("打印我说明我已经执行了");
                    message.setCreate_time(System.currentTimeMillis()); // 时间戳
                    mqttMessage.setPayload((JSONObject.toJSONString(message)).getBytes());
                    MqttUtils utils = new MqttUtils();
                    utils.publish(CollectConfig.MQTT_REGIST_TOPIC_1,mqttMessage);
                    utils.publish(CollectConfig.MQTT_CONFIG_TOPIC_1,mqttMessage);
                    utils.publish(CollectConfig.MQTT_ANSWER_TOPIC_1,mqttMessage);
                    utils.publish(CollectConfig.MQTT_CTRL_TOPIC_1,mqttMessage);
                }
            }
        }).start();
    }
}
