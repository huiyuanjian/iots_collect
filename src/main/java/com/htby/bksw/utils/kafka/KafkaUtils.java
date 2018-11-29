package com.htby.bksw.utils.kafka;

import com.alibaba.fastjson.JSON;
import com.htby.bksw.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class KafkaUtils {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaUtils.class);
    
    private static Producer<String, String> producer;
    private static Consumer<String, String> consumer;

    private KafkaUtils() {

    }
    
    public static void main(String[] args) {
    	getMsgFromKafka();
	}

    /**
     * 生产者，注意kafka生产者不能够从代码上生成主题，只有在服务器上用命令生成
     */
    static {
        Resource resource = new ClassPathResource("/application.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer<>(props);
        } catch (IOException e) {
            log.error("kafka 获取参数异常，异常信息为: {}",e.getMessage());
        }
    }

    /**
     * 消费者
     */
    static {
        Resource resource = new ClassPathResource("/application.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList("test"));
        } catch (IOException e) {
            log.error("kafka 获取参数异常，异常信息为: {}",e.getMessage());
        }
    }

    /**
     * 发送对象消息 至kafka上,调用json转化为json字符串，应为kafka存储的是String。
     * @param msg
     */
    public static void sendMsgToKafka(String topic,String key,Message msg) {
        producer.send(new ProducerRecord<String, String>(topic, key,JSON.toJSONString(msg)));
    }

    /**
     * 发送对象消息 至kafka上,调用json转化为json字符串，应为kafka存储的是String。
     * @param msg
     */
    public static void sendMsgToKafka(String topic,Message msg) {
        producer.send(new ProducerRecord<String, String>(topic, "key",JSON.toJSONString(msg)));
    }

    /**
     * 从kafka上接收对象消息，将json字符串转化为对象，便于获取消息的时候可以使用get方法获取。
     */
    public static void getMsgFromKafka(){
        while(true){
            ConsumerRecords<String, String> records = KafkaUtils.getKafkaConsumer().poll(100);
            if (records.count() > 0) {
                for (ConsumerRecord<String, String> record : records) {
//                    JSONObject jsonAlarmMsg = JSON.parseObject(record.value());
//                    IpranAlarm alarmMsg = JSONObject.toJavaObject(jsonAlarmMsg, IpranAlarm.class);
                    LOGGER.info("从kafka接收到的消息是：" + record.value().toString());
                }
            }
        }
    }
    
    public static Consumer<String, String> getKafkaConsumer() {
        return consumer;
    }

    public static void closeKafkaProducer() {
        producer.close();
    }

    public static void closeKafkaConsumer() {
        consumer.close();
    }
}


