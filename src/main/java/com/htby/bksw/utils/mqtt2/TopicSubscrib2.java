package com.htby.bksw.utils.mqtt2;

import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.mqtt.MqttUtils;

/**
 * 主题订阅（broker2 专用）
 * @author 周西栋
 * @date
 * @param
 * @return
 */
public class TopicSubscrib2 {

    public void run(){
        MqttUtils2 mqttUtils2 = new MqttUtils2();
        mqttUtils2.subscribTopic(CollectConfig.MQTT_CONFIG_TOPIC_2);
        mqttUtils2.subscribTopic(CollectConfig.MQTT_CTRL_TOPIC_2);
        mqttUtils2.subscribTopic(CollectConfig.MQTT_REGIST_TOPIC_2);
        mqttUtils2.subscribTopic(CollectConfig.MQTT_ANSWER_CONTROL_TOPIC_2);
        mqttUtils2.subscribTopic(CollectConfig.MQTT_ANSWER_WEB_TOPIC_2);
        mqttUtils2.subscribTopic(CollectConfig.MQTT_STATUS_TOPIC_2);
    }
}
