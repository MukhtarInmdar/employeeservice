package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class FinBookingFormLegalCostInfo {
	private Long finBokFrmLegalCostId;
	private Long bookingFormId;
	private Double legalAmount;
	private Double taxAmount;
	private Double totalAmount;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private Long statusId;
	private List<FinBookingFormLglCostDtlsInfo> finBookingFormLglCostDtlsList;
}
