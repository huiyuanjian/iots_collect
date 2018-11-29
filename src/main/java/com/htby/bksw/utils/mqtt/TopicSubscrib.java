package com.htby.bksw.utils.mqtt;

import com.htby.bksw.utils.config.CollectConfig;

/**
 * 主题订阅（broker1 专用）
 * @author 周西栋
 * @date
 * @param
 * @return
 */
public class TopicSubscrib {

    public void run(){
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.subscribTopic(CollectConfig.MQTT_ANSWER_TOPIC_1);
        mqttUtils.subscribTopic(CollectConfig.MQTT_CONFIG_TOPIC_1);
        mqttUtils.subscribTopic(CollectConfig.MQTT_CTRL_TOPIC_1);
        mqttUtils.subscribTopic(CollectConfig.MQTT_STATUS_TOPIC_1);
        mqttUtils.subscribTopic(CollectConfig.MQTT_LOG_TOPIC_1);

        mqttUtils.subscribTopic("REGIST/PROXY/#" );
        mqttUtils.subscribTopic("PROXY/COLLECT/+/" + CollectConfig.MACADDRESS + "/DATA");
    }
}
