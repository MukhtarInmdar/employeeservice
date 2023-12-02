/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * CustomerAddress class provides customer address specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 06:39PM
 */
public class CustomerAddress implements Serializable {

	private static final long serialVersionUID = 7995487374674024937L;
	private Long custAddressId;
	private String Hno;
	private String floorNo;
	private String tower;
	private String street;
	private String area;
	private String landmark;
	private String pincode;
	private Long cityId;
	private String city;
	private String cityIcon;
	private Long stateId;
	private String state;
	private String country;
	private Double langitude;
	private Double latitude;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private String addressType;
	private String address1;
	private String address2;
	private String address3;
	private Long statusId;
	/**
	 * @return the custAddressId
	 */
	public Long getCustAddressId() {
		return custAddressId;
	}
	/**
	 * @param custAddressId the custAddressId to set
	 */
	public void setCustAddressId(Long custAddressId) {
		this.custAddressId = custAddressId;
	}
	/**
	 * @return the hno
	 */
	public String getHno() {
		return Hno;
	}
	/**
	 * @param hno the hno to set
	 */
	public void setHno(String hno) {
		Hno = hno;
	}
	/**
	 * @return the floorNo
	 */
	public String getFloorNo() {
		return floorNo;
	}
	/**
	 * @param floorNo the floorNo to set
	 */
	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}
	/**
	 * @return the tower
	 */
	public String getTower() {
		return tower;
	}
	/**
	 * @param tower the tower to set
	 */
	public void setTower(String tower) {
		this.tower = tower;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the landmark
	 */
	public String getLandmark() {
		return landmark;
	}
	/**
	 * @param landmark the landmark to set
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}
	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	/**
	 * @return the cityId
	 */
	public Long getCityId() {
		return cityId;
	}
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the cityIcon
	 */
	public String getCityIcon() {
		return cityIcon;
	}
	/**
	 * @param cityIcon the cityIcon to set
	 */
	public void setCityIcon(String cityIcon) {
		this.cityIcon = cityIcon;
	}
	/**
	 * @return the stateId
	 */
	public Long getStateId() {
		return stateId;
	}
	/**
	 * @param stateId the stateId to set
	 */
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the langitude
	 */
	public Double getLangitude() {
		return langitude;
	}
	/**
	 * @param langitude the langitude to set
	 */
	public void setLangitude(Double langitude) {
		this.langitude = langitude;
	}
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}
	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}
	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerAddress [custAddressId=" + custAddressId + ", Hno=" + Hno + ", floorNo=" + floorNo + ", tower="
				+ tower + ", street=" + street + ", area=" + area + ", landmark=" + landmark + ", pincode=" + pincode
				+ ", cityId=" + cityId + ", city=" + city + ", cityIcon=" + cityIcon + ", stateId=" + stateId
				+ ", state=" + state + ", country=" + country + ", langitude=" + langitude + ", latitude=" + latitude
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", addressType=" + addressType
				+ ", address1=" + address1 + ", address2=" + address2 + ", address3=" + address3 + ", statusId="
				+ statusId + "]";
	}
		
}
