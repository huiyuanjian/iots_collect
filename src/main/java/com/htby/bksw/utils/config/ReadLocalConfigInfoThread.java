package com.htby.bksw.utils.config;

import com.htby.bksw.listener.ListenManager;
import com.htby.bksw.utils.fileIO.FileUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 读取本地配置信息的线程
 * @author 周西栋
 * @date
 * @param
 * @return
 */
@Slf4j
public class ReadLocalConfigInfoThread implements Runnable{


    @Override
    public void run() {
        log.info("读取本地配置信息的线程已经启动！");
        while (CollectConfig.SERVERSTATUS == CollectServerStatusEnum.START_UP){
            FileUtils fileUtils = new FileUtils();
            ConfigInfo configInfo = fileUtils.readLoginInfo();
            ConfigUtils configUtils = new ConfigUtils();
            if(configInfo != null && CollectConfig.SERVERSTATUS == CollectServerStatusEnum.START_UP){
                // 加载配置到系统中
                configUtils.setConfig(configInfo);
                // 改变系统的状态
                configUtils.change(true);
                log.info("本地配置信息读取成功！信息内容是：{}",configInfo.toString());
            } else {
                // 改变系统的状态
                configUtils.change(false);
                log.info("没有本地配置信息，读取信息失败，信息为空！");
            }
            break;
        }
        log.info("读取本地配置的线程执行完毕！");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
