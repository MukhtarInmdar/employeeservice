/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * DevicePojo class provides Department specific fields.
 * 
 * @author Venkat_Koniki
 * @since 03.05.2019
 * @time 06:34PM
 */

@Entity
public class DevicePojo {

	@Column(name = "DEVICE_ID")
	private Long deviceId;

	@Column(name = "DEVICE_TOKEN")
	private String devicetoken;

	@Column(name = "DEVICE_MODEL")
	private String devicemodel;

	@Column(name = "APP_VERSION")
	private String appversion;

	@Column(name = "OS_TYPE")
	private String ostype;

	@Column(name = "OS_VERSION")
	private String osversion;

	@Column(name = "IMEI_NO")
	private String IMEIno;

	@Column(name = "DEVICE_STATUS")
	private String devicestatus;

	@Column(name = "USER_AGENT")
	private String useragent;

	@Column(name = "USER_XID")
	private String userxid;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "SERIAL_NO")
	private String serialno;

	@Column(name = "CREATED_DATE")
	private Timestamp createddate;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifieddate;

	@Column(name = "STATUS_ID")
	private Long statusid;
	
	@Column(name ="NON_CUSTOMER_ID")
	private Long nonCustomerId;
	
	@Column(name = "FLAT_ID")
	private Long flatId;
	
	@Column(name ="FLAT_BOOK_ID")
	private Long flatBookingId;
	
	@Column(name ="SITE_ID")
	private Long siteId;


	/**
	 * @return the deviceId
	 */
	public Long getDeviceId() {
		return deviceId;
	}

	public Long getNonCustomerId() {
		return nonCustomerId;
	}

	public void setNonCustomerId(Long nonCustomerId) {
		this.nonCustomerId = nonCustomerId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the devicetoken
	 */
	public String getDevicetoken() {
		return devicetoken;
	}

	/**
	 * @param devicetoken the devicetoken to set
	 */
	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}

	/**
	 * @return the devicemodel
	 */
	public String getDevicemodel() {
		return devicemodel;
	}

	/**
	 * @param devicemodel the devicemodel to set
	 */
	public void setDevicemodel(String devicemodel) {
		this.devicemodel = devicemodel;
	}

	/**
	 * @return the appversion
	 */
	public String getAppversion() {
		return appversion;
	}

	/**
	 * @param appversion the appversion to set
	 */
	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	/**
	 * @return the ostype
	 */
	public String getOstype() {
		return ostype;
	}

	/**
	 * @param ostype the ostype to set
	 */
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	/**
	 * @return the osversion
	 */
	public String getOsversion() {
		return osversion;
	}

	/**
	 * @param osversion the osversion to set
	 */
	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public Long getFlatId() {
		return flatId;
	}

	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}

	public Long getFlatBookingId() {
		return flatBookingId;
	}

	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}

	/**
	 * @return the iMEIno
	 */
	public String getIMEIno() {
		return IMEIno;
	}

	/**
	 * @param iMEIno the iMEIno to set
	 */
	public void setIMEIno(String iMEIno) {
		IMEIno = iMEIno;
	}

	/**
	 * @return the devicestatus
	 */
	public String getDevicestatus() {
		return devicestatus;
	}

	/**
	 * @param devicestatus the devicestatus to set
	 */
	public void setDevicestatus(String devicestatus) {
		this.devicestatus = devicestatus;
	}

	/**
	 * @return the useragent
	 */
	public String getUseragent() {
		return useragent;
	}

	/**
	 * @param useragent the useragent to set
	 */
	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	/**
	 * @return the userxid
	 */
	public String getUserxid() {
		return userxid;
	}

	/**
	 * @param userxid the userxid to set
	 */
	public void setUserxid(String userxid) {
		this.userxid = userxid;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the serialno
	 */
	public String getSerialno() {
		return serialno;
	}

	/**
	 * @param serialno the serialno to set
	 */
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	/**
	 * @return the createddate
	 */
	public Timestamp getCreateddate() {
		return createddate;
	}

	/**
	 * @param createddate the createddate to set
	 */
	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
	}

	/**
	 * @return the modifieddate
	 */
	public Timestamp getModifieddate() {
		return modifieddate;
	}

	/**
	 * @param modifieddate the modifieddate to set
	 */
	public void setModifieddate(Timestamp modifieddate) {
		this.modifieddate = modifieddate;
	}

	/**
	 * @return the statusid
	 */
	public Long getStatusid() {
		return statusid;
	}

	/**
	 * @param statusid the statusid to set
	 */
	public void setStatusid(Long statusid) {
		this.statusid = statusid;
	}
	/**
	 *@return the siteId
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

	
	 public boolean equals(Object obj) {
	        if (obj == this) {
	            return true;
	        }
	        else if (obj == null || obj.getClass() != this.getClass()) {
	            return false;
	        }else if(obj instanceof DevicePojo) {
	        	DevicePojo pojo = (DevicePojo) obj;
	        	return (this.getDeviceId().equals(pojo.getDeviceId())
	        			
	        			//&& this.getDevicetoken().equalsIgnoreCase(pojo.getDevicetoken())
	        			//&& this.getDevicemodel().equalsIgnoreCase(pojo.getDevicemodel())
	        			//&& this.getAppversion().equalsIgnoreCase(pojo.getAppversion())
	        			//&& this.getOstype().equalsIgnoreCase(pojo.getOstype())
	        			//&& this.getOsversion().equalsIgnoreCase(pojo.getOsversion())
	        			//&& this.getIMEIno().equalsIgnoreCase(pojo.getIMEIno())
	        			//&& this.getDevicestatus().equalsIgnoreCase(pojo.getDevicestatus())
	        			//&& this.getUseragent().equalsIgnoreCase(pojo.getUseragent())
	        			//&& this.getUserxid().equalsIgnoreCase(pojo.getUserxid())
	        			//&& this.getUuid().equalsIgnoreCase(pojo.getUuid())
	        			//&& this.getSerialno().equalsIgnoreCase(pojo.getSerialno())
	        			//&& this.getCreateddate().equals(pojo.getCreateddate())
	        			//&& this.getModifieddate().equals(pojo.getModifieddate())
	        			//&& this.getStatusid().equals(pojo.getStatusid())
	        			);
	        }else {
	        	return false;
	        }
	    }
	 
	 @Override
	    public int hashCode() {
	        return Objects.hash(deviceId, devicetoken, devicemodel,appversion,ostype,osversion,IMEIno,devicestatus,useragent,userxid,uuid,serialno,createddate,modifieddate,statusid,nonCustomerId,siteId);
	    }
	
	@Override
	public String toString() {
		return "DevicePojo [deviceId=" + deviceId + ", devicetoken=" + devicetoken + ", devicemodel=" + devicemodel
				+ ", appversion=" + appversion + ", ostype=" + ostype + ", osversion=" + osversion + ", IMEIno="
				+ IMEIno + ", devicestatus=" + devicestatus + ", useragent=" + useragent + ", userxid=" + userxid
				+ ", uuid=" + uuid + ", serialno=" + serialno + ", createddate=" + createddate + ", modifieddate="
				+ modifieddate + ", statusid=" + statusid + ", nonCustomerId=" +nonCustomerId +",siteId="+siteId+"]";
	}

}
