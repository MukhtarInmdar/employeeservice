package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApplyLoanLeadDetailsPojo {

	@Column(name = "CUSTOMER_LOAN_EOI_DETAILS_ID")	private Long customerLoanEOIDetailsId;
	@Column(name = "BANKERS_LIST_ID")	private Long bankerListId;
	@Column(name = "BANKER_NAME")	private String bankerName;
	@Column(name = "BANKER_COMMENTS")	private String bankerComments;
	@Column(name = "CUST_ID")	private Long customerId;
	@Column(name = "CUST_NAME")	private String customerName;
	@Column(name = "FLAT_BOOK_ID")	private Long flatBookingId;
	@Column(name = "FLAT_ID")	private Long flatId;
	@Column(name = "FLAT_NO")	private String flatNo;
	@Column(name = "SITE_ID")	private Long siteId;
	@Column(name = "SITE_NAME")	private String siteName;
	@Column(name = "BOOKING_DATE")	private Timestamp bookingDate;
	@Column(name = "CREATTION_DATE")	private Timestamp createdDate;
	@Column(name = "BANKER_LEAD_VIEW_STATUS_ID")	private Long bankerLeadViewStatusId;
	@Column(name = "BANKER_LEAD_VIEW_STATUS")	private String bankerLeadViewStatus;
	@Column(name = "LEAD_STATUS_ID")	private Long leadStatusId;
	@Column(name = "LEAD_STATUS")	private String leadStatus;
	@Column(name ="FLAT_COST")	private Double flatCost;
	@Column(name ="FLAT_COST_IN_STR")	private String strFlatCost;
	private List<ApplayLoanDocumentsPojo> loanDocuments;

}
