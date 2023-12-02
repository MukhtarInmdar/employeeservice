package com.sumadhura.employeeservice.persistence.dto;



import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class BankerList {
	
	@Column(name = "BANKERS_LIST_ID")
	private Long bankerListId;
	
	@Column(name = "BANKER_NAME")
	private String bankerName;

	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "REGION_STATE")
	private Long regionStateId;
	
	@Column(name = "CITY_NAME")
	private String regionStateName;
	
	@Column(name = "URL_LOCATION")
	private String urlLocation;

	@Column(name = "CONTACT_PERSON")
	private String contactPerson;
	
	@Column(name = "CONTACT_PERSON_PHONE")
	private String contactPersonPhone;
	
}
