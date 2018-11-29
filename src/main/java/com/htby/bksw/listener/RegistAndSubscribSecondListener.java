package com.htby.bksw.listener;

import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.Message;
import com.htby.bksw.entity.common.CommonConfig;
import com.htby.bksw.entity.ctrl.CtrlInfo;
import com.htby.bksw.entity.status.StatusInfo;
import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.mqtt2.MqttUtils2;
import com.htby.bksw.utils.templates.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value = 3)
public class RegistAndSubscribSecondListener implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {
        log.info("第三步：向 broker2 注册自身信息，并创建需要订阅的主题");


        MessageTemplates messageTemplates = new MessageTemplates();
        MqttMessage mqttMessage = new MqttMessage();
        MqttUtils2 mqttUtils2 = new MqttUtils2();

        // 注册
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(1,messageTemplates.getMesBody(0,messageTemplates.getRegistInfo())));
        mqttUtils2.publish(CollectConfig.MQTT_REGIST_TOPIC_2 , mqttMessage);

        // 配置
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(4,messageTemplates.getMesBody(0, new CommonConfig())));
        mqttUtils2.publish(CollectConfig.MQTT_CONFIG_TOPIC_2 , mqttMessage);

        // 控制
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(8,messageTemplates.getMesBody(0,new CtrlInfo())));
        mqttUtils2.publish(CollectConfig.MQTT_CTRL_TOPIC_2 , mqttMessage);

        // web应答
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(6,null));
        mqttUtils2.publish(CollectConfig.MQTT_ANSWER_WEB_TOPIC_2 , mqttMessage);

        // 控制应答
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(6,null));
        mqttUtils2.publish(CollectConfig.MQTT_ANSWER_CONTROL_TOPIC_2 , mqttMessage);

        // 向下减一计数
        ListenManager.ENABLE_SUBSCRIB_2.countDown();

        log.info("第三步：完成");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
