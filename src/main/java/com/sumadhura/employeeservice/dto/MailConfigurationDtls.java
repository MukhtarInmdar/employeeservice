package com.sumadhura.employeeservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class MailConfigurationDtls extends Result implements Serializable{

	private static final long serialVersionUID = -2059378041591276822L;
	private String module;
	private String userName;
	private String password;
	private Long siteId;
	private Long deviceId;
	private Long empId;
	
	public MailConfigurationDtls() {
		super();
	}

	/**
	 * @param module
	 * @param siteId
	 */
	public MailConfigurationDtls(String module, Long siteId) {
		super();
		this.module = module;
		this.siteId = siteId;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the siteId
	 */
	public Long getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the deviceId
	 */
	public Long getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * @return the key
	 */
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MailConfigurationDtls [module=" + module + ", userName=" + userName + ", password=" + password
				+ ", siteId=" + siteId + ", deviceId=" + deviceId +  "]";
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	
}
