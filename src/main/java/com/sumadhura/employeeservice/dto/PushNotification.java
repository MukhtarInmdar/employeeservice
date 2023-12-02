package com.sumadhura.employeeservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class PushNotification extends Result{

	private static final long serialVersionUID = -8703405637753075234L;
	private String notificationType;
	private Long siteId;
	private Long blockId;
	private Long floorId;
	private Long flatId;
	private String customerType;
	private String location;
	private List<String> imageList;
	private String message;
	private Integer successCount;
	private Integer failCount;
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getBlockId() {
		return blockId;
	}
	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}
	public Long getFloorId() {
		return floorId;
	}
	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}
	public Long getFlatId() {
		return flatId;
	}
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PushNotification [notificationType=" + notificationType + ", siteId=" + siteId + ", blockId=" + blockId
				+ ", floorId=" + floorId + ", flatId=" + flatId + ", customerType=" + customerType + ", location="
				+ location + ", imageList=" + imageList + ", message=" + message + ", successCount=" + successCount
				+ ", failCount=" + failCount + "]";
	}

	
}
