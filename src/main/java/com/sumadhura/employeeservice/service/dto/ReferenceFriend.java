/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;


import lombok.Data;




/**
 * ReferenceFriend class provides REFERENCES_FRIEND Table specific fields.
 * 
 * @author Srivenu
 * @since 30.05.2019
 * @time 11:52AM
 */


@Data
public class ReferenceFriend {
	
	private Long referencesFriendId;
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
