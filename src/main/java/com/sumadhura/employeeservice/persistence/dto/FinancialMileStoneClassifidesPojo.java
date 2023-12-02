package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * 
 * @author A@IKET CH@V@N
 * @description this class is for employee financial service holding all the DB records annotated with Entity
 * @since 13-01-2020
 * @time 10:30 AM
 */
@Entity
@Data
public class FinancialMileStoneClassifidesPojo {

	@Column(name = "FIN_MILESTONE_CLASSIFIDES_ID")
	private Long finMilestoneClassifidesId;
	@Column(name = "SITE_ID")
	private Long siteId;
	@Column(name = "SITE_NAME")
	private String siteName;
	@Column(name = "MILESTONE_ALIAS_NAME")
	private String mileStoneAliasName;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name = "CREATED_BY")
	private Long createdBy;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	//@Embedded
	//private FinancialProjectMileStonePojo financialProjectMileStonePojo;
}
