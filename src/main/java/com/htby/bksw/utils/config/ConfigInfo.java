package com.htby.bksw.utils.config;

import com.htby.bksw.entity.IotsIoserverInfoEntity;
import com.htby.bksw.entity.regist.RegistInfo;
import com.htby.bksw.utils.sys.SysUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网接口配置信息实体类(用于持久化数据和服务间的通讯)
 * @author 周西栋
 * @date
 * @param
 * @return
 */
@Data
@Slf4j
public class ConfigInfo implements Serializable {

    private final long serialVersionUID = 1L;
    
    /**
     * 服务id 
     */
    public String server_id = null;

    /**
     * 服务名称
     */
    public String server_name = null;

    /**
     * 服务说明
     */
    public String server_remark = null;

    /**
     * 服务类型 
     */
    public String server_type = null;

    /**
     * mac地址 
     */
    public String mac_address = null;

    /**
     * 心跳间隔（单位是秒）
     * 默认心跳间隔是60秒
     */
    private int send_palpitate_interval = 60;

    /**
     * 采集的数据存放的topic
     */
    private String kafka_topic = null;

    /**
     * mqtt 注册 主题 broker1
     */
    public String mqtt_regist_topic_1 = null;

    /**
     * mqtt 配置 主题 broker1
     */
    public String mqtt_config_topic_1 = null;

    /**
     * mqtt 控制 主题 broker1
     */
    public String mqtt_ctrl_topic_1 = null;

    /**
     * mqtt 应答 主题 broker1
     */
    public String mqtt_answer_topic_1 = null;

    /**
     * mqtt 状态 主题 broker1
     */
    public String mqtt_status_topic_1 = null;

    /**
     * mqtt 日志 主题 broker1
     */
    public String mqtt_log_topic_1 = null;

    /**
     * mqtt 注册 主题 broker2
     */
    public String mqtt_regist_topic_2 = null;

    /**
     * mqtt 配置 主题 broker2
     */
    public String mqtt_config_topic_2 = null;

    /**
     * mqtt 控制 主题 broker2
     */
    public String mqtt_ctrl_topic_2 = null;

    /**
     * mqtt 应答web 主题 broker2
     */
    public String mqtt_answer_web_topic_2 = null;

    /**
     * mqtt 应答control 主题 broker2
     */
    public String mqtt_answer_control_topic_2 = null;

    /**
     * mqtt 状态 主题 broker2
     */
    public String mqtt_status_topic_2 = null;
    
    /**
     * 采集代理注册的消息
     * String 是mac地址
     */
    private Map<String,RegistInfo> regist_map = new HashMap<>();

    /**
     * 本服务有权管理的采集代理
     * String 是mac地址
     */
    private List<String> macaddress_list = new ArrayList<>();

    /**
     * 存放日志上传后的访问路径
     */
    private Map<String,Map<String,String>> map_logs = new HashMap<>();

}
