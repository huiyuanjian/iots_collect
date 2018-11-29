package com.htby.bksw.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 日志文件下载
 */
@RestController
@RequestMapping("/logDownLoad")
@Slf4j
public class LogDownLoad {

    /**
     * 下载远程服务器文件
     */
    @RequestMapping("/download_file")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        OutputStream os = null;
        ReadableByteChannel rbc = null;
        try{
            // TODO 下面的 fileExtName 和 fileurl 从前端传过来
            //获取文件名
            String fileExtName = "log2018.controller";
            //根据条件得到文件路径
            String fileurl = "http://192.144.188.227/log2018.8.8.controller";
            if(!fileExtName.equals("")){
                os = response.getOutputStream();
                log.info("===========文件路径==========="+fileurl);
                //获取远程文件
                URL website = new URL(fileurl);//ConfigInfo.getString("file_server_path")+fileurl
                //获取数据通道
                rbc = Channels.newChannel(website.openStream());
                //获得浏览器代理信息
                final String userAgent = request.getHeader("USER-AGENT");
                //判断浏览器代理并分别设置响应给浏览器的编码格式
                String finalFileName = null;
                if(StringUtils.contains(userAgent, "MSIE")||StringUtils.contains(userAgent,"Trident")){//IE浏览器
                    finalFileName = URLEncoder.encode(fileExtName,"UTF8");
                    System.out.println("IE浏览器");
                }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                    finalFileName = new String(fileExtName.getBytes(), "ISO8859-1");
                }else{
                    finalFileName = URLEncoder.encode(fileExtName,"UTF8");//其他浏览器
                }
                //设置HTTP响应头
                response.reset();//重置 响应头
                response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
                response.addHeader("Content-Disposition" ,"attachment;filename=\"" +finalFileName+ "\"");//下载文件的名称
                //读取数据
                ByteBuffer bb = ByteBuffer.allocate(1024);
                int index = -1;
                // 循环取出流中的数据
                while ((index = rbc.read(bb)) > 0){
                    if (index <= 0) {
                        break;
                    }
                    bb.position(0);
                    byte[] b = new byte[index];
                    bb.get(b);
                    //将数据输出
                    os.write(b, 0, index);
                    bb.clear();//缓冲区不会被自动覆盖，需要主动调用该方法
                }
            }else{
                log.info("找不到该文件！");
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
            log.debug("获取文件错误！", e);
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("下载异常！", e);
        }finally{
            try {
                if(rbc != null){
                    rbc.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.debug("流关闭异常！", e);
            }
        }
    }

}
