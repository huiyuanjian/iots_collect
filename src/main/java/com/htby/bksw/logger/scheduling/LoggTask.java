package com.htby.bksw.logger.scheduling;

import com.alibaba.fastjson.JSONObject;
import com.htby.bksw.entity.log.ServerLog;
import com.htby.bksw.logger.LoggerUtil;
import com.htby.bksw.logger.UploadLoggerService;
import com.htby.bksw.logger.utils.DateUtil;
import com.htby.bksw.logger.utils.RedisUtils;
import com.htby.bksw.utils.config.CollectConfig;
import com.htby.bksw.utils.log.UploadService;
import com.htby.bksw.utils.mqtt2.MqttUtils2;
import com.htby.bksw.utils.templates.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zcy
 * @date 2018/10/2510:30
 */
@Slf4j
@Component
@EnableScheduling
public class LoggTask {

    @Value("${logging.path}")
    private String logPath;

    @Value("${module.name}")
    private String serverName;

    @Value("${redis-two.host}")
    private String redisHost;

    @Value("${redis-two.port}")
    private int redisPort;

    @Value("${redis-two.password}")
    private String redisPwd;

    @Resource
    private LoggerUtil LogUtil;

    @Resource
    private UploadLoggerService UploadService;

    /**
     * 日志文件定时上传到fastdfs，上传成功后通过mqtt发布给broker2，让web订阅入库
     */
    @Scheduled(cron = "${schedules.upload}")
    public void runJob_log() {
        File path = new File(logPath);
        List<String> listPaths= new ArrayList<String>();
        // 根据目录 查找log文件
        listPaths = LogUtil.getChild(path,listPaths,"--Already",true);
        // 存放对应关系
        String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Map<String,String> map = CollectConfig.MAP_LOGS.get(key);
        if (map != null && !map.isEmpty()) {
            map = new HashMap<>();
        }
        if (listPaths != null) {
            for (String file:listPaths) {
                log.info("正在上传的文件是：{}",file);
                try {
                    //上传log到 fastdfs 服务器
                    String url = UploadService.upload(file);
                    //将添加成功的 进行名称修改 标志 已经上传过  "--Already" 标识
                    LogUtil.reanameFile(file,"--Already");
                    log.info("上传log到 fastdfs 服务器,访问路径为：{}",url);
                    String file_name =
                    map.put(file,url);
                } catch (IOException e) {
                    log.error("日志文件上传失败，失败的日志文件为：{}",file);
                    log.error("异常信息是：{}",e.getMessage());
                }
            }
        }
        // 将信息放回系统缓存中
        CollectConfig.MAP_LOGS.put(key,map);
        // 发布
        if (CollectConfig.MAP_LOGS != null && !CollectConfig.MAP_LOGS.isEmpty()) {
            send(getServerLog(CollectConfig.MAP_LOGS));
        }
    }

    /**
     * 生成用于上传的日志信息
     */
    private ServerLog getServerLog(Map<String,Map<String,String>> map){
        ServerLog serverLog = new ServerLog();
        serverLog.setServer_id(CollectConfig.SERVERID);
        serverLog.setServer_mac(CollectConfig.MACADDRESS);
        serverLog.setServer_type(CollectConfig.SERVERTYPE);
        serverLog.setServer_name(CollectConfig.SERVERNAME);
        serverLog.setUpload_time(new Date());
        serverLog.setMap(map);
        return serverLog;
    }

    /**
     * 发布日志信息
     */
    private void send(ServerLog serverLog){
        // 发布主题
        String topic = "LOG/" + CollectConfig.SERVERTYPE + "/" + CollectConfig.MACADDRESS;
        // 消息模版
        MessageTemplates templates = new MessageTemplates();
        // 发布工具类
        MqttUtils2 mqttUtils2 = new MqttUtils2();
        boolean upload = mqttUtils2.publish(topic,templates.getMqttMessage(templates.getMessage(7, templates.getMesBody(0,serverLog))));
        // 如果上传成功了，则清空容器
        if (upload) {
            CollectConfig.MAP_LOGS.clear();
        }
    }



    /**
     * log日志  fastdfs 成功后 删除
     * zcy
     * 20181025
     */
    @Scheduled(cron = "${schedules.delete}") // 每秒调用一次
    public void runJob_deletelog() {
        List<String> listPaths= new ArrayList<String>();
        File file= new File(logPath);
        //根据目录 查找log文件
        listPaths = LogUtil.getChild(file,listPaths,"--Already",false);
        if (!CollectionUtils.isEmpty(listPaths)){
            for (String path:listPaths) {
                //将删除名称为标志 已经上传过  "--Already" 标识
                LogUtil.deleteFile(path);
            }
        }

    }

}
