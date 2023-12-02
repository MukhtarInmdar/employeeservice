package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.ModicationInvoiceAppRej;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;

import lombok.Data;

@Data
public class FinBookingFormModiCostResponse {
	private Long finBookingFormModiCostId;
	private Long finsetOffAppLevelId;
	private Long modifiacationStatusId;
	private Long bookingFormId;
	private Double taxAmount;
	private Double basicAmount;
	private Double totalAmount;
	private Long siteAccountId;
	private String siteBankAccountNumber;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;

	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
	private List<FinBookingFormModiCostDtlsResponse> finBookingFormModiCostDtlsList;
	private List<ModicationInvoiceAppRej> approveRejectDetailsList;
	
	
}
