package com.htby.bksw.listener;

import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.MesBody;
import com.htby.bksw.entity.Message;
import com.htby.bksw.entity.common.CommonConfig;
import com.htby.bksw.entity.ctrl.CtrlInfo;
import com.htby.bksw.entity.regist.RegistInfo;
import com.htby.bksw.entity.status.StatusInfo;
import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.mqtt.MqttUtils;
import com.htby.bksw.utils.templates.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Order(value = 2)
public class RegistAndSubscribFirstListener implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {
        log.info("第二步：向 broker1 注册自身信息，并创建需要订阅的主题");

        MessageTemplates messageTemplates = new MessageTemplates();
        MqttMessage mqttMessage = new MqttMessage();
        // 发布
        MqttUtils mqttUtils = new MqttUtils();
        // 注册消息
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(1,messageTemplates.getMesBody(0,messageTemplates.getRegistInfo())));
        mqttUtils.publish(CollectConfig.MQTT_REGIST_TOPIC_1,mqttMessage);

        // 配置
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(4,messageTemplates.getMesBody(0, new CommonConfig())));
        mqttUtils.publish(CollectConfig.MQTT_CONFIG_TOPIC_1,mqttMessage);

        // 应答
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(6,new MesBody()));
        mqttUtils.publish(CollectConfig.MQTT_ANSWER_TOPIC_1,mqttMessage);

        // 控制
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(8,messageTemplates.getMesBody(0,new CtrlInfo())));
        mqttUtils.publish(CollectConfig.MQTT_CTRL_TOPIC_1,mqttMessage);

        // 服务状态
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(5,messageTemplates.getMesBody(0,new StatusInfo())));
        mqttUtils.publish(CollectConfig.MQTT_STATUS_TOPIC_1,mqttMessage);

        // 日志
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(7,messageTemplates.getMesBody(0,"")));
        mqttUtils.publish(CollectConfig.MQTT_LOG_TOPIC_1,mqttMessage); // 日志


        // 向下减一计数
        ListenManager.ENABLE_SUBSCRIB_1.countDown();

        log.info("第二步：完成");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
