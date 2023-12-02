package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ContactInfoPojo {

	@Column(name="CONT_INFO_ID")
	private Long contInfoId;
	@Column(name="EMAIL_ID")
	private String email;
	/*
	@Column(name="CONTACT_NO")
	private Long phoneNo;
	@Column(name="ALTERNATIVE_CONTACT_NO")
	private Long alternatePhoneNo;
	*/
	@Column(name="CONTACT_NO")
	private String phoneNo;
	@Column(name="ALTERNATIVE_CONTACT_NO")
	private String alternatePhoneNo;
	@Column(name="CRETAED_DATE")
	private Timestamp createdDate;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="FLATBOOKING_ID")
	private Long flatBookId;
	
}
