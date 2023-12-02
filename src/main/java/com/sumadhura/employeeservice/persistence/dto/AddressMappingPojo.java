package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AddressMappingPojo {

	
	
	@Column(name="ADDRESS_MAPPING_ID")
	private Long addressMappingTypeId;
	@Column(name="TYPE_ID")
	private Long typeId;
	@Column(name="TYPE")
	private Long type;
	@Column(name="ADDRESS_TYPE")
	private String addressType;
	@Column(name="ADDRESS_ID")
	private Long addressId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Long getAddressMappingTypeId() {
		return addressMappingTypeId;
	}
	public void setAddressMappingTypeId(Long addressMappingTypeId) {
		this.addressMappingTypeId = addressMappingTypeId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	
	
	
	
}
	
	
