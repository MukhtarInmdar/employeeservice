package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ReferencesMappingPojo {

	@Column(name="REFERENCES_MAPPING_ID")
	private Long referencesMappingId;
	@Column(name="TYPE_ID")
	private Long typeId;
	@Column(name="TYPE")
	private Long type;
	@Column(name="CUST_OTHER_ID")
	private Long custOtherId;
	
	
	
	
	public Long getReferencesMappingId() {
		return referencesMappingId;
	}
	public void setReferencesMappingId(Long referencesMappingId) {
		this.referencesMappingId = referencesMappingId;
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
	public Long getCustOtherId() {
		return custOtherId;
	}
	public void setCustOtherId(Long custOtherId) {
		this.custOtherId = custOtherId;
	}
	
	
	
	
}
