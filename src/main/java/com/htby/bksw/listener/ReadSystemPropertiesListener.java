package com.htby.bksw.listener;

import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.sys.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(value = 1)
public class ReadSystemPropertiesListener  implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {
        log.info("第一步：读取系统信息");

        // 获取mac地址
        CollectConfig.MACADDRESS = SysUtils.getMACAddress();

        // 初始化mqtt broker1 需要订阅的主题
        CollectConfig.MQTT_REGIST_TOPIC_1 = "REGIST/COLLECT/" + CollectConfig.MACADDRESS; // 注册
        CollectConfig.MQTT_CONFIG_TOPIC_1 = "PROXY/COLLECT/" + CollectConfig.MACADDRESS + "/CONFIG"; // 配置
        CollectConfig.MQTT_ANSWER_TOPIC_1 = "PROXY/COLLECT/" + CollectConfig.MACADDRESS + "/ANSWER"; // 应答
        CollectConfig.MQTT_CTRL_TOPIC_1 = "PROXY/COLLECT/" + CollectConfig.MACADDRESS + "/CTRL"; // 控制
        CollectConfig.MQTT_STATUS_TOPIC_1 = "STATUS/COLLECT/" + CollectConfig.MACADDRESS; // 防止订阅不到主题
        CollectConfig.MQTT_LOG_TOPIC_1 = "PROXY/COLLECT/" + CollectConfig.MACADDRESS + "/LOG";// 采集代理的日志

        // 初始化mqtt broker2 需要订阅的主题
        CollectConfig.MQTT_REGIST_TOPIC_2 = "REGIST/COLLECT/" + CollectConfig.MACADDRESS; // 注册
        CollectConfig.MQTT_CONFIG_TOPIC_2 = "WEB/COLLECT/" + CollectConfig.MACADDRESS + "/CONFIG"; // 配置
        CollectConfig.MQTT_ANSWER_WEB_TOPIC_2 = "WEB/COLLECT/" + CollectConfig.MACADDRESS + "/ANSWER"; // 应答
        CollectConfig.MQTT_ANSWER_CONTROL_TOPIC_2 = "CONTROL/COLLECT/" + CollectConfig.MACADDRESS + "/ANSWER"; // 应答
        CollectConfig.MQTT_CTRL_TOPIC_2 = "CONTROL/COLLECT/" + CollectConfig.MACADDRESS + "/CTRL"; // 控制
        CollectConfig.MQTT_STATUS_TOPIC_2 = "STATUS/COLLECT/" + CollectConfig.MACADDRESS; // 状态

        log.info("我所用的mac地址是：{}",CollectConfig.MACADDRESS);
        log.info("第一步：完成");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
