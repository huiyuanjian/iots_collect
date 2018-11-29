package com.htby.bksw.listener;

import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.config.ReadLocalConfigInfoThread;
import com.htby.bksw.utils.mqtt.MqttUtils;
import com.htby.bksw.utils.mqtt.TopicSubscrib;
import com.htby.bksw.utils.mqtt2.TopicSubscrib2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value = 4)
public class ReadLocalConfigThreadListener implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {
        log.info("第四步：唤醒读取本地配置文件的线程，等待启动步骤完成后执行");
        new Thread(new ReadLocalConfigInfoThread(),"读取本地配置的线程").start();
        try {
            // 当计数器减到0时，才会向下执行
            ListenManager.COUNT.await();
        } catch (InterruptedException e) {
            log.error("读取本地配置线程睡眠出现异常，异常信息为：{}",e.getMessage());
        }
        log.info("第四步：完成");
        log.info("物联网接口服务启动完成！");


        TopicSubscrib topicSubscrib = new TopicSubscrib();
        topicSubscrib.run();
        TopicSubscrib2 topicSubscrib2 = new TopicSubscrib2();
        topicSubscrib2.run();

    }
}
