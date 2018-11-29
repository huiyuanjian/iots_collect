package com.htby.bksw.entity;

/**
 * @ClassName: IOServerInfo
 * @Description: IOServer信息实体类（不用了）
 * @author 周西栋
 * @date 2018年5月7日
 *
 */
public class IOServerInfo {

	/**
	 * IOServer id
	 */
	private Integer id;
	
	/**
	 * IOServer名称
	 */
	private String name;
	
	/**
	 * IOServer地址
	 */
	private String addr;
	
	/**
	 * proxy id
	 */
	private Integer proxy_id;
	
	/**
	 * proxy名称
	 */
	private String proxy_name;
	
	/**
	 * proxy地址
	 */
	private String proxy_addr;
	
	/**
	 * IOServer与proxy通讯超时时间（秒）
	 */
	private Integer time_out;
	
	/**
	 * 存储队列服务状态标识
	 */
	private String storage_servicestatus_topic;
	
	/**
	 * 存储队列设备状态标识
	 */
	private String storage_devicestatus_topic;
	
	/**
	 * 存储队列采集/控制数据标识
	 */
	private String storage_msg_topic;
	
	/**
	 * 存储队列控制执行状态标识
	 */
	private String storage_msgstatus_topic;

	public String getStorage_msgstatus_topic() {
		return storage_msgstatus_topic;
	}

	public void setStorage_msgstatus_topic(String storage_msgstatus_topic) {
		this.storage_msgstatus_topic = storage_msgstatus_topic;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Integer getProxy_id() {
		return proxy_id;
	}

	public void setProxy_id(Integer proxy_id) {
		this.proxy_id = proxy_id;
	}

	public String getProxy_name() {
		return proxy_name;
	}

	public void setProxy_name(String proxy_name) {
		this.proxy_name = proxy_name;
	}

	public String getProxy_addr() {
		return proxy_addr;
	}

	public void setProxy_addr(String proxy_addr) {
		this.proxy_addr = proxy_addr;
	}

	public Integer getTime_out() {
		return time_out;
	}

	public void setTime_out(Integer time_out) {
		this.time_out = time_out;
	}

	public String getStorage_servicestatus_topic() {
		return storage_servicestatus_topic;
	}

	public void setStorage_servicestatus_topic(String storage_servicestatus_topic) {
		this.storage_servicestatus_topic = storage_servicestatus_topic;
	}

	public String getStorage_devicestatus_topic() {
		return storage_devicestatus_topic;
	}

	public void setStorage_devicestatus_topic(String storage_devicestatus_topic) {
		this.storage_devicestatus_topic = storage_devicestatus_topic;
	}

	public String getStorage_msg_topic() {
		return storage_msg_topic;
	}

	public void setStorage_msg_topic(String storage_msg_topic) {
		this.storage_msg_topic = storage_msg_topic;
	}

	@Override
	public String toString() {
		return "IOServerInfo [id=" + id + ", name=" + name + ", addr=" + addr + ", proxy_id=" + proxy_id
				+ ", proxy_name=" + proxy_name + ", proxy_addr=" + proxy_addr + ", time_out=" + time_out
				+ ", storage_servicestatus_topic=" + storage_servicestatus_topic + ", storage_devicestatus_topic="
				+ storage_devicestatus_topic + ", storage_msg_topic=" + storage_msg_topic + ", storage_msgstatus_topic="
				+ storage_msgstatus_topic + "]";
	}

}
