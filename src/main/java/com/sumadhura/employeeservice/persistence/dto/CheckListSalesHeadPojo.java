package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;


@Entity
@Data
public class CheckListSalesHeadPojo {

	@Column(name = "CHECKLIST_SALESHEAD_ID")
	private Long salesHeadId;
	@Column(name = "CUSTOMER_ID")
	private Long customerId;
	@Column(name = "SOURCE_OF_BOOKING")
	private String sourceofBooking;
	@Column(name = "REFERAL_BONUS")
	private String referralBonusStatus;
	@Column(name = "OFFER_DETAILS")
	private String offersAny;
	@Column(name = "ERP_DETAILS")
	private String erpDetails;
	@Column(name = "SALES_TEAM_COMMITMENTS")
	private String salesTeamCommitments;
	@Column(name = "REMARKS")
	private String remarks;
	@Column(name = "SALES_HEAD_APPROVED_DATE")
	private Timestamp projectSalesHeadDate;
	@Column(name = "APPROVER_EMPLOYEE_ID")
	private Long authorizedSignatoryId;
	@Column(name = "APROVER_APROVED_DATE")
	private Timestamp authorizedSignatoryDate;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookingId;
	@Column(name="AVAILABILITY")
	private String availability;
	@Column(name="AVAILABILITY_OTHER")
	private String availabilityIfOther;
	@Column(name="PROJECT_SALES_HEAD_ID")
	private Long projectSalesheadId;

}
