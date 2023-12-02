package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsResponse;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormFlatKhataDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMaintenanceDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyStatisticsPojo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormMstSchTaxMapInfo;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialProjectMileStoneResponse {

	private Long projectMilestoneId;
	private String milestoneName;
	private Long finMilestoneClassifidesId;
	private Long finBookingFormAccountsId;
	private Long finBookingFormMilestonesId;
	private Long finBokFomDmdNoteId;
	private Long percentagesId;
	private Double mileStonePercentage;
	private Timestamp milestoneDate;
	private Timestamp mileStoneDueDate;
	private Timestamp milestoneDemandNoteDate;
	private Timestamp milestoneLastReceiptDate;
	private Long projectMsStatusId;
	private Long mileStoneNo;
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private Double mileStoneBasicAmount;
	private Double mileStoneTotalAmount;
	private Double mileStoneTaxAmount;
	private Double mileStoneTdsAmount;
	private Timestamp masterDemandNotedate;
	private Timestamp masterDemandDueDate;
	private Double payAmount;
	private Double sgstAmount;
	private Double cgstAmount;
	private Double gstAmount;
	private Double paidAmount;
	private Double totalPenaltyAmount;
	private Double totalPendingPenaltyAmount;
	private Double totalPenalityPaidAmount;
	private Double interestWaiverAdjAmount;
	private String interestWaiverPendingAmount;//interest waiver initiated amount to approve
	private Double totalDueAmount;
	private Double paidTaxAmount;
	private String customerName;
	private Long bookingFormId;
	private Double setOffAmount;//this key is used for waiver request
	private Long submitedById;
	private String submitedByName;
	private Long msStatusId;
	private Long statusId;
	private String statusName;
	private Long tdsStatusId;
	private String tdsStatusName;
	private Long finBookingFormTdsDtlsId;
	private String demandNoteNo;
	private Timestamp demandNoteDate;
	private String documentLocation;
	private String documentName;
	private Long daysDelayed;
	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
	// private Long tdsDetailsStatusId;
	private List<FinPenaltyStatisticsPojo> penaltyStatisticsPojos;
	private List<FinBookingFormAccountsResponse> finBookingFormAccountsResponseList;
	private List<FinBookingFormMstSchTaxMapInfo> bokFrmDemNteSchTaxMapInfos;
	private List<CustomerPropertyDetailsInfo> flatsResponse;
	private List<FinBookingFormModiCostDtlsResponse> bookingFormModiCostDtlsResponses;
	private List<FinBookingFormLglCostDtlsResponse> bookingFormLglCostDtlsResponses;
	
	private List<FinBookingFormMaintenanceDtlsPojo> maintenanceChargesDtlsResponse;
	private List<FinBookingFormFlatKhataDtlsPojo> flatKhataChargesDtlsResponse;
	
	private List<FinPenaltyPojo> penaltyPojoList;
	private List<Map<String,Object>> mapData;
}
