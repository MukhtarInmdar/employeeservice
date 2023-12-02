package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanRequest extends Result implements Serializable {
	private static final long serialVersionUID = -8390603213995381974L;

	private Long departmentId;
	private Long roleId;
	private Long siteId;
	private List<Long> siteIds;
	private String siteName;
	private String flatNo;
	private Long employeeId;
	private Timestamp leadFromDate;
	private Timestamp leadToDate;
	private Long flatId;
	private Long custId;
	private String custName;
	private Long bookingFormId;
	private Long customerLoanEOIDetailsId;
	private String requestUrl;
	private Long bankerLeadViewStatusId;
	private String bankerLeadViewStatus;
	private Long leadStatusId;
	private String leadStatus;
	private String previousBankerComments;
	private String bankerComment;
	private Long bankerListId;
	private String bankerName;
}
