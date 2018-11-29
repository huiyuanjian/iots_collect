package com.htby.bksw.utils.mqtt2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * mqtt 的连接信息 (broker2 专用)
 */
@Data
@Slf4j
public class AutoMqttProperties2 {

    // broker连接地址
    private String host;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 身份id
    private String clientid;

    // 超时（单位是秒）
    private int timeout;

    // 会话心跳时间 （单位为秒）
    private int keepalive;

    public AutoMqttProperties2 () {
        log.info("AutoMqttProperties2_before: {}",toString());
        Resource resource = new ClassPathResource("/application.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            this.host = props.getProperty("com.mqtt2.host");
            this.username = props.getProperty("com.mqtt2.username");
            this.password = props.getProperty("com.mqtt2.password");
            this.clientid = props.getProperty("com.mqtt2.clientid");
            this.timeout = Integer.valueOf(props.getProperty("com.mqtt2.timeout","20"));
            this.keepalive = Integer.valueOf(props.getProperty("com.mqtt2.keepalive","20"));
        } catch (IOException e) {
            log.error("mqtt broker2 获取参数异常，异常信息为: {}",e.getMessage());
        }
        log.info("AutoMqttProperties2_after: {}",toString());
    }

}
