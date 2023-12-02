package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ReferencesFriendPojo {

	@Column(name="REFERENCES_FRIEND_ID")
	private Long referencesFriendId;
	@Column(name="FRIEND_NAME")
	private String referenceFreindsorFamilyName;
	
	public Long getReferencesFriendId() {
		return referencesFriendId;
	}
	public void setReferencesFriendId(Long referencesFriendId) {
		this.referencesFriendId = referencesFriendId;
	}
	public String getReferenceFreindsorFamilyName() {
		return referenceFreindsorFamilyName;
	}
	public void setReferenceFreindsorFamilyName(String referenceFreindsorFamilyName) {
		this.referenceFreindsorFamilyName = referenceFreindsorFamilyName;
	}

}
