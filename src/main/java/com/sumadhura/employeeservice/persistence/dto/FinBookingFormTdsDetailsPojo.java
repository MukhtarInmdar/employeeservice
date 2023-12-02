package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * 
 * @author A@IKET CH@V@N
 * @description this class is for employee financial service holding all the DB
 *              records annotated with Entity
 * @since 13-01-2020
 * @time 10:30 AM
 */
@Entity
@Data
public class FinBookingFormTdsDetailsPojo {
	
	@Column(name = "FIN_BOK_FRM_TDS_DTLS_ID") private Long finBookingFormTdsDtlsId ;
	@Column(name = "SUBMITED_BY") private Long submitedBy;
	@Column(name = "SUBMITED_BY_NAME") private String submitedByName;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "STATUS_NAME")  private String statusName;
	
	@Column(name = "TDS_STATUS_ID") private Long tdsStatusId;
	@Column(name = "TDS_STATUS_NAME")  private String tdsStatusName;
	
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;

}
