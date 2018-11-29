package com.htby.bksw.utils.mqtt2;

import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.MesBody;
import com.htby.bksw.entity.Message;
import com.htby.bksw.entity.common.CommonConfig;
import com.htby.bksw.entity.ctrl.CtrlInfo;
import com.htby.bksw.logger.utils.RedisUtils;
import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.config.ConfigUtils;
import com.htby.bksw.utils.mqtt.MqttUtils;
import com.htby.bksw.utils.templates.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
public class SubscribHandler2 {

    private static Jedis jedis = new RedisUtils().jedisPool.getResource();

    /**
     * 定义一个阻塞长度为128长度的队列
     * 配置 主题 broker2
     * <p>
     * add        增加一个元索                如果队列已满，则抛出一个IIIegaISlabEepeplian异常
     * remove     移除并返回队列头部的元素      如果队列为空，则抛出一个NoSuchElementException异常
     * element    返回队列头部的元素           如果队列为空，则抛出一个NoSuchElementException异常
     * offer      添加一个元素并返回true       如果队列已满，则返回false
     * poll       移除并返问队列头部的元素      如果队列为空，则返回null
     * peek       返回队列头部的元素           如果队列为空，则返回null
     * put        添加一个元素                如果队列满，则阻塞
     * take       移除并返回队列头部的元素      如果队列为空，则阻塞
     */
    private static BlockingQueue<MqttMessage> QUEUE_CONFIG = new LinkedBlockingQueue<MqttMessage>(128);

    /**
     * 定义一个阻塞长度为512长度的队列
     * 控制 主题 broker2
     */
    private static BlockingQueue<MqttMessage> QUEUE_CTRL = new LinkedBlockingQueue<MqttMessage>(512);

    /**
     * 定义一个阻塞长度为1024长度的队列
     * 应答ctrl 主题 broker2
     */
    private static BlockingQueue<MqttMessage> QUEUE_ANSWER_CTRL = new LinkedBlockingQueue<MqttMessage>(1024);

    /**
     * 定义一个阻塞长度为1024长度的队列
     * 应答web 主题 broker2
     */
    private static BlockingQueue<MqttMessage> QUEUE_ANSWER_WEB = new LinkedBlockingQueue<MqttMessage>(1024);

    /**
     * 消息处理
     * topic 主题
     * qos 消息质量
     * msg 接收到的消息
     * topic 订阅主题
     * 详细说明参考 《14_mqtt通讯协议》
     */
    public static void handler(String topic, MqttMessage mqttMessage) throws InterruptedException {
        String array_top[] = topic.split("/");
        int len = array_top.length;
        String top = array_top[len > 0 ? (len - 1) : 0];
        switch (top) {
            case "CONFIG":
                QUEUE_CONFIG.put(mqttMessage);
                break;
            default:
                if (CollectConfig.SERVERID != null && !CollectConfig.MACADDRESS_LIST.isEmpty()) {
                    switch (top) {
                        case "CTRL":
                            QUEUE_CTRL.put(mqttMessage);
                            break;
                        case "ANSWER": {
                            top = topic.split("/")[0];
                            switch (top) {
                                case "WEB":
                                    QUEUE_ANSWER_WEB.put(mqttMessage);
                                    break;
                                case "CONTROL":
                                    QUEUE_ANSWER_CTRL.put(mqttMessage);
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                        default:
                            break;
                    }
                }
                break;
        }
    }

    /**
     * 处理配置信息
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void handlerConfig() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MqttMessage mqttMessage = QUEUE_CONFIG.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()), Message.class);
                        if (message != null && message.getBody() != null) {
                            // 回应消息
                            publish2(message);
                            if (message.getBody().getSub_type() == 1) {// collect的配置
                                // 设置自身配置
                                if (message.getBody().getContext() != null) {
                                    String str = message.getBody().getContext().get(0);
                                    CommonConfig commonConfig = JSONObject.parseObject(str, CommonConfig.class);
                                    if (commonConfig != null) {
                                        // 更新管辖的采集代理集合
                                        ConfigUtils configUtils = new ConfigUtils();
                                        configUtils.setCommonConfig(commonConfig);
                                    }
                                }
                            } else {// 关于采集代理的配置
                                // 将消息从broker1 转到 broker2
                                inToOut(message);
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理配置信息出现异常！异常信息是：{}", e.getMessage());
                    }
                }
            }
        }, "配置线程2").start();
    }

    /**
     * 处理控制信息
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void handlerCtrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MqttMessage mqttMessage = QUEUE_CTRL.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()), Message.class);
                        if (message != null) {
                            // 回应消息
                            publish2(message);
                            // 将消息从broker2 转到 broker1
                            inToOut(message);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理控制信息出现异常！异常信息是：{}", e.getMessage());
                    }
                }
            }
        }, "控制线程2").start();
    }

    /**
     * 处理ctrl应答信息
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void handlerAnswerCtrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MqttMessage mqttMessage = QUEUE_ANSWER_CTRL.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()), Message.class);
                        if (message != null && message.getBody() != null && message.getBody().getContext() != null) {
                            // 移除相应的发布记录
                            jedis.del(message.getCallback_id());
                        }
                    } catch (InterruptedException e) {
                        log.error("处理ctrl应答信息出现异常！异常信息是：{}", e.getMessage());
                    }
                }
            }
        }, "ctrl应答线程2").start();
    }

    /**
     * 处理web应答信息
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void handlerAnswerWeb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MqttMessage mqttMessage = QUEUE_ANSWER_WEB.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()), Message.class);
                        if (message != null && message.getBody() != null && message.getBody().getContext() != null) {
                            // 移除相应的发布记录
                            jedis.del(message.getCallback_id());
                        }
                    } catch (InterruptedException e) {
                        log.error("处理web应答信息出现异常！异常信息是：{}", e.getMessage());
                    }
                }
            }
        }, "web应答线程2").start();
    }

    /**
     * 向broker2回应
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void publish2(Message message) {
        // 回应topic
        String answer_topic = "COLLECT/" + message.getSource_type().toUpperCase() + "/" + message.getSource_mac() + "/ANSWER";
        // mqtt工具类 broker2
        MqttUtils2 mqttUtils = new MqttUtils2();
        mqttUtils.publish(answer_topic, getAnswer(message.getMsg_id(),0,"SUCCESS"));
    }

    /**
     * 向broker2回应
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void publish2(Message message, int subtype, String context) {
        // 回应topic
        String answer_topic = "COLLECT/" + message.getSource_type().toUpperCase() + "/" + message.getSource_mac() + "/ANSWER";
        // mqtt工具类 broker2
        MqttUtils2 mqttUtils = new MqttUtils2();
        mqttUtils.publish(answer_topic, getAnswer(message.getMsg_id(), subtype, context));
    }

    /**
     * 构造应答消息
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static MqttMessage getAnswer(String msg_id, int subtype,String context) {

        // 构造消息内容
        MesBody body = new MesBody();
        List<String> list = new ArrayList<>();
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
     * broker2 转发到 broker1
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static void inToOut(Message message) {
        // 获得消息的类型
        String configType = getType(message.getMsg_type());
        if (message == null || configType == null || message.getBody() == null || message.getBody().getContext() == null || message.getBody().getContext().get(0) == null)
            return;
        String txt = message.getBody().getContext().get(0);
        // 利用目标的mac地址获得发布的topic
        String mac = null;
        String topic = null;
        if (configType.equals("CTRL")) { // 控制
            CtrlInfo ctrlInfo = JSONObject.parseObject(txt, CtrlInfo.class);
            mac = ctrlInfo.getProxy_mac();
            // 判断该mac地址是不是归自己所管辖
            boolean contains = CollectConfig.MACADDRESS_LIST.contains(mac);
            if (contains) { // 不为空说明有该mac地址
                // 发布给目标的topic
                topic = CollectConfig.SERVERTYPE + "/PROXY/" + mac + "/CTRL";
                // 转发
                sendMessage(topic,message);
            }
        } else if (configType.equals("CONFIG")) { // 配置
            CommonConfig commonConfig = JSONObject.parseObject(txt, CommonConfig.class);
            mac = commonConfig.getServer_mac(); // 获得mac地址
            // 判断该mac地址是不是归自己所管辖
            boolean contains = CollectConfig.MACADDRESS_LIST != null && CollectConfig.MACADDRESS_LIST.contains(mac);
            if (contains) { // 不为空说明有该mac地址
                if (message.getBody().getSub_type() == 2) { // 同步设备和变量
                    // 变更消息子类型
                    message.getBody().setSub_type(1);
                } else if (message.getBody().getSub_type() == 3) { // 下发采集代理的配置信息
                    // 变更消息子类型
                    message.getBody().setSub_type(2);
                }
                // 发布给目标的topic
                topic = CollectConfig.SERVERTYPE + "/PROXY/" + mac + "/CONFIG";
                // 转发
                sendMessage(topic,message);
            }
        }
    }

    /**
     * 转发消息
     */
    private static void sendMessage(String topic, Message message){
        if (message != null){
            // 管理订阅消息
            jedis.set("COLLECT_SUBSCRIB_2_" + message.getMsg_id(),JSONObject.toJSONString(message),"NX","EX",60);

            // 修改一些参数
            message.setCallback_id(message.getMsg_id());
            message.setMsg_id(CollectConfig.SERVERTYPE + System.currentTimeMillis());
            message.setSource_type(CollectConfig.SERVERTYPE);
            message.setSource_mac(CollectConfig.MACADDRESS);
            message.setCreate_time(System.currentTimeMillis());
            message.setLicense("");// TODO 授权码待定
            // 构造mqtt消息
            MessageTemplates templates = new MessageTemplates();
            MqttMessage mqttMessage = templates.getMqttMessage(message);
            // 构造topic
            MqttUtils mqttUtils = new MqttUtils();
            mqttUtils.publish(topic, mqttMessage);
            // 管理发布信息
            jedis.set("COLLECT_PUBLISH_1_" + message.getMsg_id(),JSONObject.toJSONString(message),"NX","EX",60);
            // 移除已经回应的消息
            jedis.del("COLLECT_PUBLISH_1_" + message.getCallback_id());
        }
    }

    /**
     * 转类型
     *
     * @param
     * @return
     * @author 周西栋
     * @date
     */
    private static String getType(int typs) {
        String str_type = null;
        switch (typs) {
            case 1:
                str_type = "REGIST";
                break;
            case 2:
                str_type = "PING";
                break;
            case 3:
                str_type = "DATA";
                break;
            case 4:
                str_type = "CONFIG";
                break;
            case 5:
                str_type = "STATUS";
                break;
            case 6:
                str_type = "ANSWER";
                break;
            case 7:
                str_type = "CTRL";
                break;
            default:
                break;
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
        handlerConfig();
        handlerCtrl();
        handlerAnswerWeb();
        handlerAnswerCtrl();
    }
}
