package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;




@Entity
public class FlatDtlsAmenitsFlatMappingPojo {

	@Column(name = "FLAT_DTLS_AMENITS_FLAT_MAPP_ID")
	private Long flatDtlsAmenitsFlatMappId;
	@Column(name = "FLAT_DETAILS_ID")
	private Long flatDetailsId;
	@Column(name = "AMENITIES_FLAT_ID")
	private Long amenitiesFlatId;
	@Column(name = "STATUS_ID")
	private Long statusID;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "UPDATED_DATE")
	private Timestamp updatedDate;
	
	
	
}
