package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.FinancialProjectMileStonePojo;

import lombok.Data;
/**
 * 
 * @author @NIKET CH@V@N
 * @since 11.01.2020
 * @time 06:50 PM
 * @description this class is for holding request data for financial module
 */
@Data
public class FinancialProjectMileStoneInfo implements Cloneable{
	
	private Long projectMilestoneId;
	private String milestoneName;
	private Long siteId;
	//private String dataUploadCondition;
	private Long finBookingFormMileStoneTaxId;
	private Long finBookingFormAccountsId;
	private Long finMilestoneClassifidesId;
	private Long finProjectDemandNoteId;
	private Long finBookingFormDemandNoteId;
	private Long finBookingFormMilestonesId; 
	private Long bookingFormId;
	private Long percentagesId;
	private Double mileStonePercentage;
	private Timestamp milestoneDate;
	private Timestamp mileStoneDueDate;
	private Timestamp mileStonePaidDate;
	private Double mileStoneDueAmount;
	private Double milestonePrincipalAmount;
	private Timestamp demandNoteDate;
	private Timestamp milestoneDemandNoteDate;//not in use
	
	//new
	private Timestamp masterDemandNotedate;//milestone master data wise demand note date
	private Timestamp masterDemandDueDate;
	
	//private Timestamp masterDemandNoteDate;//not in use
	//private Timestamp masterDemandNoteDueDate;//not in use
	
	private Timestamp interestCalculationUptoDate;
	private Long interestSelectionType;
	private boolean isReGenerateDemandNote;
	private boolean isShowGstInPDF;
	private boolean isThisUplaodedData;
	private boolean isThisExplicitGeneratedRecord;
	private Double mileStoneBasicAmount;
	private Double mileStoneTotalAmount; 
	private Double mileStoneTaxAmount;
	private Double sgstAmount;
	private Double cgstAmount;
	
	private Double mileStonePayAmount;
	private Double mileStonePaidAmount;
	private Double mileStonePendingAmount;
	private Double mileStonePaidBasicAmount;//used in interest calculations
	//private String requestFrom;
	private Long statusId;
	private Long mileStoneNo;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
 	private Timestamp modifiedDate;
 	private String condition;
 	private String interestCalculationFrom;
 	private List<Long> type;
 	private List<Long> paymentStatus;
 	private List<Long> currentprojectMileStoneIds;//current selected milestone ids
 	private String dataSelectionOrder;
 	private Double paidAmount;//2
	private Double totalPenaltyAmount;
	private Double totalPendingPenaltyAmount;
	private Double totalDueAmount;
	private Double payAmount;
	private Double setOffAmount;
	private Long submitedById;
	private String submitedByName;
	private String statusName;
	private Long finBookingFormTdsDtlsId ;
	//private String finTransactionNo;
	private String finBokAccInvoiceNo;
	private Long tdsStatusId;
	private String tdsStatusName;
	private String demandNoteSelectionType;
	private String excelRecordNo;
	List<FinBookingFormMstSchTaxMapInfo> bokFrmDemNteSchTaxMapInfos;
	private FinancialProjectMileStonePojo explicitGeneratedRecordMileStonePojo;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}