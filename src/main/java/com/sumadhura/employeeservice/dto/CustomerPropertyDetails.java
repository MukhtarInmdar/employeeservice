/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;

import lombok.ToString;

/**
 * CustomerPropertyDetails class provides CustomerPropertyDetails specific fields.
 * 
 * @author Venkat_Koniki
 * @since 03.05.2019
 * @time 11:51AM
 */

@ToString
public class CustomerPropertyDetails implements Serializable{

	private static final long serialVersionUID = 7926059360018806437L;
	private Long customerId;
	private String customerName;
	private String customerEmail;
	private String customerProfilePic;
	private Long flatBookingId;
	private Long flatId;
	private String flatNo;
	private Long flooId;
	private String floorName;
	private Long blockId;
	private String blockName;
	private Long siteId;
	private String siteName;
	
	
	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the customerProfilePic
	 */
	public String getCustomerProfilePic() {
		return customerProfilePic;
	}
	/**
	 * @param customerProfilePic the customerProfilePic to set
	 */
	public void setCustomerProfilePic(String customerProfilePic) {
		this.customerProfilePic = customerProfilePic;
	}
	/**
	 * @return the flatBookingId
	 */
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	/**
	 * @param flatBookingId the flatBookingId to set
	 */
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	/**
	 * @return the flatId
	 */
	public Long getFlatId() {
		return flatId;
	}
	/**
	 * @param flatId the flatId to set
	 */
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	/**
	 * @return the flatNo
	 */
	public String getFlatNo() {
		return flatNo;
	}
	/**
	 * @param flatNo the flatNo to set
	 */
	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}
	/**
	 * @return the flooId
	 */
	public Long getFlooId() {
		return flooId;
	}
	/**
	 * @param flooId the flooId to set
	 */
	public void setFlooId(Long flooId) {
		this.flooId = flooId;
	}
	/**
	 * @return the floorName
	 */
	public String getFloorName() {
		return floorName;
	}
	/**
	 * @param floorName the floorName to set
	 */
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	/**
	 * @return the blockId
	 */
	public Long getBlockId() {
		return blockId;
	}
	/**
	 * @param blockId the blockId to set
	 */
	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}
	/**
	 * @return the blockName
	 */
	public String getBlockName() {
		return blockName;
	}
	/**
	 * @param blockName the blockName to set
	 */
	public void setBlockName(String blockName) {
		this.blockName = blockName;
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
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail() {
		return customerEmail;
	}
	/**
	 * @param customerEmail the customerEmail to set
	 */
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}


}
