package com.htby.bksw.utils.templates;

import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.MesBody;
import com.htby.bksw.entity.Message;
import com.htby.bksw.entity.common.CommonConfig;
import com.htby.bksw.entity.ctrl.CtrlInfo;
import com.htby.bksw.entity.ping.PingInfo;
import com.htby.bksw.entity.regist.RegistInfo;
import com.htby.bksw.entity.status.StatusInfo;
import com.htby.bksw.utils.config.CollectConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息模版
 */
@Slf4j
public class MessageTemplates {

    /**
     * 获得MqttMessage
     */
    public MqttMessage getMqttMessage(Message message){
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setRetained(true);
        mqttMessage.setQos(1);
        mqttMessage.setPayload((JSONObject.toJSONString(message)).getBytes());
        return mqttMessage;
    }

    /**
     * 获得Message
     */
    public Message getMessage(int type,MesBody body){
        Message message = new Message();
        message.setMsg_id(CollectConfig.SERVERTYPE + String.valueOf(System.currentTimeMillis())); // 消息id
        message.setSource_mac(CollectConfig.MACADDRESS); // mac地址
        message.setMsg_type(type);
        message.setSource_type(CollectConfig.SERVERTYPE); // 服务类型
        message.setCreate_time(System.currentTimeMillis()); // 时间戳
        message.setBody(body);
        return message;
    }

    /**
     * 获得MesBody
     */
    public MesBody getMesBody(int subType,Object... object){
        List<String> list = new ArrayList<>();
        //索引遍历
        for(int i=0;i<object.length;i++) {
            list.add(JSONObject.toJSONString(object[i]));
        }

        MesBody body = new MesBody();
        body.setSub_type(subType);
        body.setContext(list);
        return body;
    }

    /**
     * 注册信息
     */
    public RegistInfo getRegistInfo(){
        RegistInfo registInfo = new RegistInfo();
        registInfo.setHost("");
        registInfo.setMacAddress(CollectConfig.MACADDRESS);
        registInfo.setRemark("我是物联网接口服务的注册信息");
        registInfo.setServerName("物联网接口服务");
        registInfo.setServerType(CollectConfig.SERVERTYPE);
        return registInfo;
    }

    /**
     * 服务的状态信息
     */
    public StatusInfo getStatusInfo(){
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setHost("");
        statusInfo.setMacAddress(CollectConfig.MACADDRESS);
        statusInfo.setServerType(CollectConfig.SERVERTYPE);
        statusInfo.setServerName(CollectConfig.SERVERNAME);
        statusInfo.setRemark(CollectConfig.SERVERREMARK);
        statusInfo.setCreatetime(System.currentTimeMillis());
        // TODO 写一个工具类，专门提供相应的数据
        statusInfo.setNet_usage(1.0f);
        statusInfo.setMemory_used(2);
        statusInfo.setMemory_free(3);
        statusInfo.setMem_usage(4.0f);
        statusInfo.setIo_usage(5);
        statusInfo.setCpu_usage(6);
        statusInfo.setCpu_temperature(7);
        return statusInfo;
    }

    /**
     * 服务的心跳信息
     */
    public PingInfo getPingInfo(){
        PingInfo pingInfo = new PingInfo();
        pingInfo.setCreatetime(System.currentTimeMillis());
        pingInfo.setHost("");
        pingInfo.setMacAddress(CollectConfig.MACADDRESS);
        pingInfo.setServerName(CollectConfig.SERVERNAME);
        pingInfo.setServerType(CollectConfig.SERVERTYPE);
        pingInfo.setRemark(CollectConfig.SERVERREMARK);
        return pingInfo;
    }

}
