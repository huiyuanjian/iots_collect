package com.htby.bksw.utils.comment;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: CodeConfig
 * @Description: 统一返回码的配置类
 * @author 周西栋
 * @date 2018年5月13日
 *
 */
public class CodeConfig {
	
	/**
	 * 编码管理类
	 */
	public static Map<String,String> CODE_MAP = new HashMap<>();
	
/************************************************      单例模式     ************************************************/
	
	/**
	 * 创建一个新的实例 CodeConfig.
	 */
	private CodeConfig(){
		init();
	}
	
    /**
     * 私有对象变量
     */
    private static final CodeConfig single = new CodeConfig();
    
    /**
     * @Title: getInstance
     * @Description: 饿汉模式--单例（静态工厂方法） 
     * @param @return    参数
     * @return SingleSocketClientThread    返回类型
     * @throws
     */
    public static CodeConfig getInstance() {
    	
        return single;
    }
	
	/************************************************      单例模式     ************************************************/
	
    /**
     * @Title: init
     * @Description: 初始化CODE_MAP
     * @return void    返回类型
     * @throws
     */
    private void init(){
    	CODE_MAP.put("0", "操作成功");
    	
    	CODE_MAP.put("0001", "发送的不是IOTP协议信息--发送的内容过短");
    	CODE_MAP.put("0002", "发送的不是IOTP协议信息--发送的内容过长");
    	
    	CODE_MAP.put("0101", "IOTP协议-协议标签错误");
    	CODE_MAP.put("0102", "IOTP协议-结束符错误");
    	CODE_MAP.put("0103", "IOTP协议-协议体格式错误");
    	CODE_MAP.put("0104", "IOTP协议-协议的命令类型错误");
    	CODE_MAP.put("0105", "IOTP协议-协议的源ID号错误");
    	CODE_MAP.put("0106", "IOTP协议-协议的源类型号错误");
    	CODE_MAP.put("0107", "IOTP协议-协议的协议序列号错误（源端标识唯一ID，循环使用）");
    	CODE_MAP.put("0108", "IOTP协议-协议的协议子序列号错误（默认0，当有多包连续数据时，从1开始增加，-1表示最后一个数据包）");
    	CODE_MAP.put("0109", "IOTP协议-协议的时间戳格式错误");
    	CODE_MAP.put("0110", "IOTP协议-协议的节点授权码的不能为空");
    	CODE_MAP.put("0111", "IOTP协议-协议的协议体长度必须是数字");
    	CODE_MAP.put("0112", "IOTP协议-协议的协议体长度超过8192字节长度");
    	
    	CODE_MAP.put("0201", "采集数据协议-不是以“http://”开头");
		CODE_MAP.put("0202", "采集数据协议-层级状态不正确，请检查“/”的分级个数");
		CODE_MAP.put("0203", "采集数据协议-结尾不是“变量值_时间戳”的形式");
		CODE_MAP.put("0204", "采集数据协议-时间戳错误");
		
		CODE_MAP.put("0301", "控制协议-不是以“http://”开头");
		CODE_MAP.put("0302", "控制协议-层级状态不正确，请检查“/”的分级个数");
		CODE_MAP.put("0303", "控制协议-结尾不是“写变量值_时间戳_超时时间_尝试次数”的形式");
		CODE_MAP.put("0304", "控制协议-时间戳错误");
		CODE_MAP.put("0305", "控制协议-超时时间错误（只能是自然数）");
		CODE_MAP.put("0306", "控制协议-尝试次数错误（只能是正自然数）");
		
		CODE_MAP.put("0401", "状态协议-不是以“http://”开头");
		CODE_MAP.put("0402", "状态协议-状态的分级不正确，请检查“/”的分级个数");
		CODE_MAP.put("0403", "状态协议-服务状态-状态格式不正确，请参照“http://domain/service_status/service_id/stauts_cpu_storageused-storagetotal_memused-memtotal”的红色加粗部分");
		CODE_MAP.put("0404", "状态协议-服务状态-id格式不正确，请参照“http://domain/service_status/service_id/stauts_cpu_storageused-storagetotal_memused-memtotal”的红色加粗部分");
		CODE_MAP.put("0405", "状态协议-服务状态-运行状态信息格式不正确，请参照“http://domain/service_status/service_id/stauts_cpu_storageused-storagetotal_memused-memtotal”的红色加粗部分");
		CODE_MAP.put("0406", "状态协议-设备状态-状态格式不正确，请参照“http://domain/device_status/IOServer标识/分组标识/设备标识/status_lastonlinetime”的红色加粗部分");
		CODE_MAP.put("0407", "状态协议-设备状态-最后在线时间部分 格式不正确，请参照“http://domain/device_status/IOServer标识/分组标识/设备标识/status_lastonlinetime”的红色加粗部分");
		
		CODE_MAP.put("0501", "配置协议-采集代理配置-id不能为空值");
		CODE_MAP.put("0502", "配置协议-采集代理配置-id的数据类型不正确");
		CODE_MAP.put("0503", "配置协议-采集代理配置-id不能为负数");
		CODE_MAP.put("0504", "配置协议-采集代理配置-name不能为空");
		CODE_MAP.put("0505", "配置协议-采集代理配置-name不能为空格");
		CODE_MAP.put("0506", "配置协议-采集代理配置-address不能为空");
		CODE_MAP.put("0507", "配置协议-采集代理配置-address格式不正确（正确的格式形如：192.168.1.101:50125）");
		CODE_MAP.put("0508", "配置协议-采集代理配置-IOServer_id不能为空");
		CODE_MAP.put("0509", "配置协议-采集代理配置-IOServer_id数据类型错误");
		CODE_MAP.put("0510", "配置协议-采集代理配置-IOServer_id不能为负数");
		CODE_MAP.put("0511", "配置协议-采集代理配置-IOServer_address不能为空");
		CODE_MAP.put("0512", "配置协议-采集代理配置-IOServer_address格式不正确（正确的格式形如：192.168.1.101:50125）");
		CODE_MAP.put("0513", "配置协议-采集代理配置-IOServer_name不能为空");
		CODE_MAP.put("0514", "配置协议-采集代理配置-IOServer_name不能为空格");
		CODE_MAP.put("0515", "配置协议-采集代理配置-设备信息-id不能为空值");
		CODE_MAP.put("0516", "配置协议-采集代理配置-设备信息-id的数据类型不正确");
		CODE_MAP.put("0517", "配置协议-采集代理配置-设备信息-id不能为负数");
		CODE_MAP.put("0518", "配置协议-采集代理配置-设备信息-name不能为空");
		CODE_MAP.put("0519", "配置协议-采集代理配置-设备信息-name不能为空格");
		CODE_MAP.put("0520", "配置协议-采集代理配置-设备信息-addr不能为空");
		CODE_MAP.put("0521", "配置协议-采集代理配置-设备信息-addr格式不正确（正确的格式形如：192.168.1.101:50125）");
		CODE_MAP.put("0522", "配置协议-采集代理配置-设备信息-time_out超时时间不能为空");
		CODE_MAP.put("0523", "配置协议-采集代理配置-设备信息-time_out超时时间的数据类型不正确");
		CODE_MAP.put("0524", "配置协议-采集代理配置-设备信息-time_out超时时间不能是负数");
		CODE_MAP.put("0525", "配置协议-采集代理配置-设备信息-time_span默认打包时间不能为空");
		CODE_MAP.put("0526", "配置协议-采集代理配置-设备信息-time_span默认打包时间的数据类型不正确");
		CODE_MAP.put("0527", "配置协议-采集代理配置-设备信息-time_span默认打包时间不能是负数");
		CODE_MAP.put("0528", "配置协议-采集代理配置-变量信息-id不能为空");
		CODE_MAP.put("0529", "配置协议-采集代理配置-变量信息-id数据类型错误");
		CODE_MAP.put("0530", "配置协议-采集代理配置-变量信息-id不能为负数");
		CODE_MAP.put("0531", "配置协议-采集代理配置-变量信息-name不能为空");
		CODE_MAP.put("0532", "配置协议-采集代理配置-变量信息-name不能为空格");
		CODE_MAP.put("0533", "配置协议-采集代理配置-变量信息-device_id（关联设备id）不能为空");
		CODE_MAP.put("0534", "配置协议-采集代理配置-变量信息-device_id（关联设备id）的数据类型不正确");
		CODE_MAP.put("0535", "配置协议-采集代理配置-变量信息-device_id（关联设备id）不能为负数");
		CODE_MAP.put("0536", "配置协议-采集代理配置-变量信息-error_as_null（变量错误或者超时是否传输NULL）不能为空");
		CODE_MAP.put("0537", "配置协议-采集代理配置-变量信息-error_as_null（变量错误或者超时是否传输NULL）的数据类型不正确");
		CODE_MAP.put("0538", "配置协议-采集代理配置-变量信息-error_as_null（变量错误或者超时是否传输NULL）不能为负数");
		CODE_MAP.put("0539", "配置协议-采集代理配置-变量信息-time_out（变量超时时间）不能为空");
		CODE_MAP.put("0540", "配置协议-采集代理配置-变量信息-time_out（变量超时时间）的数据类型不正确");
    	
    }
    

}
