package com.htby.bksw.entity;

import com.htby.bksw.entity.proxy.DeviceInfo;
import com.htby.bksw.entity.proxy.PackageInfo;
import com.htby.bksw.entity.proxy.VarInfo;

import java.util.List;

/**
 * @ClassName: CollectionAgentConfig
 * @Description: 采集代理服务配置信息实体bean
 * @author 周西栋
 * @date 2018年5月7日
 *
 */
public class CollectionAgentConfig {

	/**
	 * 服务id
	 */
	private Integer id;
	
	/**
	 * 服务名称
	 */
	private String name;
	
	/**
	 * 服务地址
	 */
	private String address;
	
	/**
	 * IOServer ID(指mac地址)
	 */
	private String IOServer_id;
	
	/**
	 * IOServer地址
	 */
	private String IOServer_address;
	
	/**
	 * IOServer名称
	 */
	private String IOServer_name;
	
	/**
	 * 设备容器
	 */
	private List<DeviceInfo> device_list;

	/**
	 * 变量容器
	 */
	private List<VarInfo> var_list;

	/**
	 * 数据包容器
	 */
	private List<PackageInfo> pack_list;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the iOServer_id
	 */
	public String getIOServer_id() {
		return IOServer_id;
	}

	/**
	 * @param iOServer_id the iOServer_id to set
	 */
	public void setIOServer_id(String iOServer_id) {
		IOServer_id = iOServer_id;
	}

	/**
	 * @return the iOServer_address
	 */
	public String getIOServer_address() {
		return IOServer_address;
	}

	/**
	 * @param iOServer_address the iOServer_address to set
	 */
	public void setIOServer_address(String iOServer_address) {
		IOServer_address = iOServer_address;
	}

	/**
	 * @return the iOServer_name
	 */
	public String getIOServer_name() {
		return IOServer_name;
	}

	/**
	 * @param iOServer_name the iOServer_name to set
	 */
	public void setIOServer_name(String iOServer_name) {
		IOServer_name = iOServer_name;
	}

	/**
	 * @return the device_list
	 */
	public List<DeviceInfo> getDevice_list() {
		return device_list;
	}

	/**
	 * @param device_list the device_list to set
	 */
	public void setDevice_list(List<DeviceInfo> device_list) {
		this.device_list = device_list;
	}

	/**
	 * @return the var_list
	 */
	public List<VarInfo> getVar_list() {
		return var_list;
	}

	/**
	 * @param var_list the var_list to set
	 */
	public void setVar_list(List<VarInfo> var_list) {
		this.var_list = var_list;
	}

	/**
	 * @return the pack_list
	 */
	public List<PackageInfo> getPack_list() {
		return pack_list;
	}

	/**
	 * @param pack_list the pack_list to set
	 */
	public void setPack_list(List<PackageInfo> pack_list) {
		this.pack_list = pack_list;
	}

	/* (非 Javadoc)
	 * <p>Title: toString</p>
	 * <p>Description: </p>
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollectionAgentConfig [id=" + id + ", name=" + name + ", address=" + address + ", IOServer_id="
				+ IOServer_id + ", IOServer_address=" + IOServer_address + ", IOServer_name=" + IOServer_name
				+ ", device_list=" + device_list + ", var_list=" + var_list + ", pack_list=" + pack_list + "]";
	}
	
}
