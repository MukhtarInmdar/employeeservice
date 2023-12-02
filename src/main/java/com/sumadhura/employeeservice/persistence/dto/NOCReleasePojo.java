package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class NOCReleasePojo {
	@Column(name = "NOC_RELEASE_ID") private Long nocReleaseId;
	@Column(name = "FLAT_BOOKING_ID") private Long flatBookingId;
	@Column(name = "NOC_RELEASE_DATE") private Timestamp nocReleaseDate;
	@Column(name = "DOC_LOCATION") private String docLocation;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "URL_LOCATION") private String nocURLLocation;
	private String nocShowStatus;
	private Long deptId;
	private Long roleId;
	private String docName;
}
