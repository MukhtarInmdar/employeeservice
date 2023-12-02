package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class CustChecklistVerificationPojo {

	@Column(name = "CUST_CHECK_VERI_ID")
	private Long custCheckVeriId;
	@Column(name = "CUST_ID")
	private Long custId;
	@Column(name = "CHECKLIST_DEPT_MAP_ID")
	private Long checklistDeptMapId;
	@Column(name = "STATUS_ID")
	private Long is_verified;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookId;
	
	public Long getFlatBookId() {
		return flatBookId;
	}
	public void setFlatBookId(Long flatBookId) {
		this.flatBookId = flatBookId;
	}
	public Long getCustCheckVeriId() {
		return custCheckVeriId;
	}
	public void setCustCheckVeriId(Long custCheckVeriId) {
		this.custCheckVeriId = custCheckVeriId;
	}
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Long getIs_verified() {
		return is_verified;
	}
	public void setIs_verified(Long is_verified) {
		this.is_verified = is_verified;
	}
	public Long getChecklistDeptMapId() {
		return checklistDeptMapId;
	}
	public void setChecklistDeptMapId(Long checklistDeptMapId) {
		this.checklistDeptMapId = checklistDeptMapId;
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
	
	
	
	
}
