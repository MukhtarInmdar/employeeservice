package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerProfessionalDetailsPojo {

	
	@Column(name="CUST_PROFFISIONAL_ID")
	private Long custProffisionalId;
	@Column(name="DESIGNATION")
	private String designation;
	@Column(name="ADDRESS_OF_ORGANIZATION")
	private String addressOfOrganization;
	@Column(name="OFFICE_NUMBER")
	private String officeNumber;
	@Column(name="OFFICE_EMAIL_ID")
	private String officeEmailId;
	@Column(name="ORGANIZATION_ID")
	private Long organizationId;
	@Column(name="ORGANIZATION_TYPE_OTHERS")
	private Long organizationTypeOthers;
	@Column(name="SECTOR_ID")
	private Long sectorId;
	@Column(name="SECTOR_OTHERS")
	private String sectorOthers;
	@Column(name="WORKFUNCTION_ID")
	private Long workFunctionId;
	@Column(name="WORKFUNCTION_OTHERS")
	private String workFunctionOthers;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
}
	

	
	

