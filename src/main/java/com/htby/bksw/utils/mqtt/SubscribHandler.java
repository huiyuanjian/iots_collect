package com.htby.bksw.utils.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.MesBody;
import com.htby.bksw.entity.Message;
import com.htby.bksw.entity.log.ServerLog;
import com.htby.bksw.entity.regist.RegistInfo;
import com.htby.bksw.entity.regist.RegistManager;
import com.htby.bksw.logger.utils.RedisUtils;
import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.kafka.KafkaUtils;
import com.htby.bksw.utils.mqtt2.MqttUtils2;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * topic 业务处理类（broker1 逻辑专用）
 * 也就是说，这里的订阅消息全部来自采集代理
 */
@Slf4j
@Component
public class SubscribHandler {

    private static Jedis jedis = new RedisUtils("10.10.20.171",6379,"123456").jedisPool.getResource();


    /**
     * 定义一个阻塞长度为128长度的队列
     * 注册 主题 broker1
     *
     * add        增加一个元索                如果队列已满，则抛出一个IIIegaISlabEepeplian异常
     * remove     移除并返回队列头部的元素      如果队列为空，则抛出一个NoSuchElementException异常
     * element    返回队列头部的元素           如果队列为空，则抛出一个NoSuchElementException异常
     * offer      添加一个元素并返回true       如果队列已满，则返回false
     * poll       移除并返问队列头部的元素      如果队列为空，则返回null
     * peek       返回队列头部的元素           如果队列为空，则返回null
     * put        添加一个元素                如果队列满，则阻塞
     * take       移除并返回队列头部的元素      如果队列为空，则阻塞
     */
    private static BlockingQueue<MqttMessage> QUEUE_REGIST = new LinkedBlockingQueue<MqttMessage>(128);

    /**
     * 定义一个阻塞长度为128长度的队列
     * 配置 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_CONFIG = new LinkedBlockingQueue<MqttMessage>(128);

    /**
     * 定义一个阻塞长度为512长度的队列
     * 控制 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_CTRL = new LinkedBlockingQueue<MqttMessage>(512);

    /**
     * 定义一个阻塞长度为1024长度的队列
     * 应答 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_ANSWER = new LinkedBlockingQueue<MqttMessage>(1024);

    /**
     * 定义一个阻塞长度为512长度的队列
     * 状态 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_STATUS = new LinkedBlockingQueue<MqttMessage>(512);

    /**
     * 定义一个阻塞长度为512长度的队列
     * 心跳 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_PING = new LinkedBlockingQueue<MqttMessage>(512);

    /**
     * 定义一个阻塞长度为1024长度的队列
     * 数据 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_DATA = new LinkedBlockingQueue<MqttMessage>(1024);

    /**
     * 定义一个阻塞长度为128长度的队列
     * 数据 主题 broker1
     */
    private static BlockingQueue<MqttMessage> QUEUE_LOG = new LinkedBlockingQueue<MqttMessage>(128);

    /**
     * 消息处理
     * topic 主题
     * qos 消息质量
     * msg 接收到的消息
     * topic 订阅主题
     * 详细说明参考 《14_mqtt通讯协议》
     */
    public static void handler(String topic,MqttMessage mqttMessage) throws InterruptedException {
        String top = topic.split("/")[0];
        switch (top) {
            case "REGIST" :  QUEUE_REGIST.put(mqttMessage); break;
            default: if(!CollectConfig.MACADDRESS_LIST.isEmpty()){
                switch (top) {
                    case "STATUS" :  QUEUE_STATUS.put(mqttMessage); break;
                    case "PING" :  QUEUE_PING.put(mqttMessage); break;
                    default: {
                        String array_top[] = topic.split("/");
                        int len = array_top.length;
                        top = array_top[len > 0 ? (len - 1) : 0];
                        switch (top){
                            case "CONFIG" :  QUEUE_CONFIG.put(mqttMessage); break;
                            case "CTRL" :  QUEUE_CTRL.put(mqttMessage); break;
                            case "ANSWER" :  QUEUE_ANSWER.put(mqttMessage); break;
                            case "DATA" :  QUEUE_DATA.put(mqttMessage); break;
                            case "LOG" : QUEUE_LOG.put(mqttMessage); break;
                            default: break;
                        }
                    } break;
                }
            } break;
        }
    }

    /**
     * 处理注册信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerRegist(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_REGIST.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null){
                            RegistInfo registInfo  = null;
                            try {
                                registInfo = JSONObject.parseObject(message.getBody().getContext().get(0),RegistInfo.class);
                            } catch (Exception e){
                                log.error("注册信息解析异常，异常信息是：{}",e.getMessage());
                            }
                            if(registInfo != null && registInfo.getServerType().equals("PROXY")){
                                RegistManager.put(registInfo,mqttMessage);
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理注册信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"注册线程1").start();
    }

    /**
     * 处理配置信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerConfig(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_CONFIG.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null){
                            // 回应消息
                            publish1(message,1,"CONFIG");
                            // 将消息从broker1 转到 broker2
                            outToIn(message);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理配置信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"配置线程1").start();
    }

    /**
     * 处理控制信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerCtrl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_CTRL.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null){
                            // 回应消息
                            publish1(message,2,"CTRL");
                            // 将消息从broker1 转到 broker2
                            outToIn(message);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理控制信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"控制线程1").start();
    }

    /**
     * 处理心跳信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerPing(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_PING.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null){
                            // 回应消息
                            publish1(message,3,"PING");
                            // 将消息从broker1 转到 broker2
                            outToIn(message);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理心跳信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"心跳线程1").start();
    }

    /**
     * 处理状态信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerStatus(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_STATUS.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null){
                            // 回应消息
                            publish1(message,4,"STATUS");
                            // 将消息从broker1 转到 broker2
                            outToIn(message);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理状态信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"状态线程1").start();
    }

    /**
     * 处理应答信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerAnswer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_ANSWER.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getContext() != null){
                            // 移除相应的发布记录
                            jedis.del(message.getCallback_id());
                        }
                    } catch (InterruptedException e) {
                        log.error("处理应答信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"应答线程1").start();
    }

    /**
     * 处理数据信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerData(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_DATA.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null){
                            // 向kafka放数据
                            try {
                                KafkaUtils.sendMsgToKafka(CollectConfig.KAFKA_TOPIC,message);
                            } catch (Exception e) {
                                // 再试一次，向kafka放数据
                                try {
                                    KafkaUtils.sendMsgToKafka(CollectConfig.KAFKA_TOPIC,message);
                                } catch (Exception e1) {
                                    log.error("向kafka中存放数据失败！失败的信息是:{}",message.toString());
                                    log.error("异常信息是：{}",e1.getMessage());
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理数据信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"数据线程1").start();
    }

    /**
     * 日志转发
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerLog(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = QUEUE_LOG.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if (message != null && message.getBody() != null && message.getBody().getContext() != null){
                            publish1(message,5,"LOG");
                            ServerLog serverLog = JSONObject.parseObject(message.getBody().getContext().get(0),ServerLog.class);
                            // 判断该mac地址是不是归自己所管辖
                            boolean contains = CollectConfig.MACADDRESS_LIST.contains(serverLog.getServer_mac());
                            if (contains) { // 不为空说明有该mac地址
                                // 转发到broker2上
                                MqttUtils2 mqttUtils2 = new MqttUtils2();
                                mqttUtils2.publish("LOG/" + serverLog.getServer_type() + "/" + serverLog.getServer_mac(),mqttMessage);
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理数据信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"数据线程1").start();
    }

    /**
     * 向broker1回应
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void publish1(Message message){
        // 回应topic
        String answer_topic = "COLLECT/PROXY/" + message.getSource_mac() + "/ANSWER";
        // mqtt工具类 broker1
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.publish(answer_topic,getAnswer(message.getMsg_id(),0,"SUCCESS"));
    }

    /**
     * 向broker1回应
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void publish1(Message message,int subtype, String context){
        // 回应topic
        String answer_topic = "COLLECT/PROXY/" + message.getSource_mac() + "/ANSWER";
        // mqtt工具类 broker1
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.publish(answer_topic,getAnswer(message.getMsg_id(),subtype,context));
    }

    /**
     * 构造应答消息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static MqttMessage getAnswer(String msg_id,int subtype, String context){

        // 构造消息内容
        MesBody body = new MesBody();
        List<String> list = new ArrayList <>();
        list.add(context);
        body.setContext(list);
        body.setSub_type(subtype);

        // 构造消息
        Message answer_msg = new Message();
        answer_msg.setMsg_id(CollectConfig.MACADDRESS + System.currentTimeMillis());// 此消息的id
        answer_msg.setCallback_id(msg_id);// 应答目标的id
        answer_msg.setLicense("");// TODO 如果有授权码，则再写相应的逻辑
        answer_msg.setSource_mac(CollectConfig.MACADDRESS);// 发布者mac地址
        answer_msg.setSource_type(CollectConfig.SERVERTYPE);// 发布者类型
        answer_msg.setMsg_type(6);// 服务类型
        answer_msg.setCreate_time(System.currentTimeMillis()); // 创建时间
        answer_msg.setBody(body); // 消息体

        // 构造mqtt消息
        MqttMessage answer = new MqttMessage();
        answer.setQos(1);
        answer.setRetained(true);
        answer.setPayload(JSONObject.toJSONString(answer_msg).getBytes());

        return answer;
    }

    /**
     * broker1 转发到 broker2
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void outToIn(Message message){
        if (message == null || message.getBody() == null || message.getBody().getContext() == null || message.getBody().getContext().get(0) == null) return;
        // 根据接收到的消息找等待回复的消息
        String str1 = jedis.get("COLLECT_PUBLISH_1_" + message.getCallback_id());
        Message pub_msg = JSONObject.parseObject(str1,Message.class);
        Message back_msg = null;
        try {
            String str2 = jedis.get("COLLECT_SUBSCRIB_2_" + pub_msg.getCallback_id());
            back_msg = JSONObject.parseObject(str2,Message.class);
        } catch (Exception e){
            log.error("我没有拿到回复消息，异常信息是：{}",e.getMessage());
        }
        if(back_msg != null){
            // 修改一些参数
            message.setSource_type(CollectConfig.SERVERTYPE);
            message.setSource_mac(CollectConfig.MACADDRESS);
            message.setCallback_id(back_msg.getMsg_id());
            message.setCreate_time(System.currentTimeMillis());
            message.setLicense("");// TODO 授权码待定
            // 构造mqtt消息
            MqttMessage answer = new MqttMessage();
            answer.setQos(1);
            answer.setRetained(true);
            answer.setPayload(JSONObject.toJSONString(message).getBytes());
            // 构造topic
            String topic = CollectConfig.SERVERTYPE + "/" + back_msg.getSource_type().toUpperCase() + "/" + back_msg.getSource_mac() + "/" + getType(back_msg.getMsg_type());
            MqttUtils2 mqttUtils2 = new MqttUtils2();
            mqttUtils2.publish(topic,answer);
            // 移除已经回应的消息
            jedis.del("COLLECT_PUBLISH_1_" + message.getCallback_id());
            // 移除已经回应了的订阅
            jedis.del("COLLECT_SUBSCRIB_2_" + message.getCallback_id());
            // 记录到发布管理容器中
            jedis.set("COLLECT_PUBLISH_2_" + back_msg.getMsg_id(),JSONObject.toJSONString(message),"NX","EX",60);
        }
    }

    /**
     * 转类型
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static String getType(int typs){
        String str_type = null;
        switch (typs){
            case 1 : str_type = "REGIST"; break;
            case 2 : str_type = "PING"; break;
            case 3 : str_type = "DATA"; break;
            case 4 : str_type = "CONFIG"; break;
            case 5 : str_type = "STATUS"; break;
            case 6 : str_type = "ANSWER"; break;
            case 7 : str_type = "CTRL"; break;
            default: break;
        }
        return str_type;
    }


    /**
     * 启动队列任务
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    static {
        handlerRegist();
        handlerData();
        handlerConfig();
        handlerCtrl();
        handlerStatus();

        handlerPing();
        handlerAnswer();
        handlerLog();
    }
}
