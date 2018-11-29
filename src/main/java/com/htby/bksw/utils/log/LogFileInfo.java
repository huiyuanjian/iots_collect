package com.htby.bksw.utils.log;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 存储日志上传后的一些路径信息等
 * @author 周西栋
 * @date
 * @param
 * @return
 */
@Component
@Slf4j
@Data
public class LogFileInfo {

    // 文件名
    private String fileName;

    // 上传到的路径
    private String uploadUrl;

    // 创建时间
    private Date createtime = new Date();

    // 日志的状态(初始状态是未上传)
    private LogFileStatusEnum logFileStatusEnum = LogFileStatusEnum.NOT_UPLOAD;

}
