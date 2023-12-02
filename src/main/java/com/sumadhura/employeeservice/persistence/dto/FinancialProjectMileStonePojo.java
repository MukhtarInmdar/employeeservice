package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumadhura.employeeservice.service.dto.FinBookingFormMstSchTaxMapInfo;

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
//@Embeddable
public class FinancialProjectMileStonePojo {
	@Column(name = "PROJECT_MS_STATUS_ID") 
	private Long projectMsStatusId;
	
	@Column(name = "PROJECT_MILESTONE_ID")
	private Long projectMilestoneId;
	
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID")
	private Long finBookingFormAccountsId;
	
	@Column(name = "MILESTONE_NAME")
	private String milestoneName;
	@Column(name = "FIN_MILESTONE_CLASSIFIDES_ID")
	private Long finMilestoneClassifidesId;
	
	@Column(name = "PERCENTAGE_ID")
	private Long percentagesId;
	
	@Column(name = "DN_CREATION_DATE") private Timestamp dnCreationDate;
	
	@Column(name = "MASTER_DMD_NOTE_DATE") private Timestamp masterDemandNotedate;//milestone master data wise demand note date
	@Column(name = "MASTER_DMD_DUE_DATE") private Timestamp masterDemandDueDate;
	
	@Column(name = "MS_DMD_NOTE_DATE") private Timestamp milestoneDemandNoteDate;//milestone wise demand note date
	@Column(name = "MILE_STONE_PERCENTAGE")
	private Double mileStonePercentage;
	@Column(name = "MILESTONE_DATE")
	private Timestamp milestoneDate;
	@Column(name = "MILE_STONE_NO")
	private Long mileStoneNo;
	@Column(name = "CREATED_BY")
	private Long createdBy;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	//@Column(name ="BASIC_AMOUNT")private Double basicAmount;
	//@Column(name ="TOTAL_AMOUNT") private Double totalAmount;
	@Column(name = "BASIC_AMOUNT")
	private Double mileStoneBasicAmount;
	@Column(name = "TOTAL_AMOUNT")
	private Double mileStoneTotalAmount;
	@Column(name ="TAX_AMOUNT")
	private Double mileStoneTaxAmount;
	@Column(name = "TDS_AMOUNT") 
	private Double mileStoneTdsAmount;

	@Column(name = "PAYABLE_AMOUNT") 
	private Double totalPayableAmount;
	
	@Column(name = "BALANCE_AMOUNT") 
	private Double mileStoneBalanceAmount;

	@Column(name = "DUE_DATE")
	private Timestamp mileStoneDueDate;
	@Column(name = "PAID_AMOUNT") 
	private Double paidAmount;
	@Column(name = "PAID_DATE") 
	private Timestamp mileStonePaidDate;
	@Column(name = "PAYMENT_STATUS") 
	private Long paymentStatus;
	
	@Column(name = "PENALITY_STATUS") 
	private Long penalityStatus;

	@Column(name = "PRINCIPAL_AMOUNT") 
	private Double milestonePrincipalAmount;
	
	@Column(name = "PAY_AMOUNT") 
	private Double payAmount;
	@Column(name = "SGST_AMOUNT") 
	private Double sgstAmount;
	@Column(name = "CGST_AMOUNT")
	private Double cgstAmount;
	
	@Column(name = "GST_AMOUNT")
	private Double gstAmount;

	@Column(name = "INTEREST_WAIVER_AMT") private Double interestWaiverAdjAmount;
	@Column(name = "INTEREST_WAIVER_PAID_AMT") private Double interestWaiverPaidAmount;
	
	@Column(name = "FIN_PENALTY_ID")
	private Long finPenaltyId;
	@Column(name = "FIN_BOK_FOM_DMD_NOTE_ID") 
	private Long finBokFomDmdNoteId;
	@Column(name ="TOTAL_PENALITY_AMOUNT")
	private Double totalPenaltyAmount;

	@Column(name ="TOTAL_PENDING_PENALITY_AMOUNT")
	private Double totalPendingPenaltyAmount;
	
	@Column(name ="TOTAL_PENALITY_PAID_AMOUNT")
	private Double totalPenalityPaidAmount;
	
	@Column(name ="TOTAL_PENDING_PENALITY_AMOUNT_DB")
	private Double totalPendingPenaltyAmount_Db;

	@Column(name = "IS_INTEREST_CALC_COMPLETED") private Long isInterestCalcCompleted;

	@Column(name ="TOTAL_DELAYED_DAYS_DB")
	private Long totalDelayedDays_Db;
	
	@Column(name ="TOTAL_DUE_AMOUNT")
	private Double totalDueAmount;
	
	@Column(name ="PAID_TAX_AMOUNT")
	private Double paidTaxAmount;
	
	@Column(name= "BOOKING_FORM_ID")
	private Long bookingFormId;
	@Column(name ="FIN_BOOKING_FORM_MILESTONES_ID")
	private Long finBookingFormMilestonesId;
	
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "INTEREST_SELECTION_TYPE") private Long interestSelectionType;
	
	@Column(name = "SET_OFF_AMOUNT") private Double setOffAmount;	
	@Column(name = "SUBMITED_BY_ID") private Long submitedById;
	@Column(name = "SUBMITED_BY_NAME") private String submitedByName;
	@Column(name = "MS_STATUS_ID")  private Long msStatusId;
	@Column(name = "STATUS_ID")  private Long statusId;
	@Column(name = "STATUS_NAME")  private String statusName;
	@Column(name = "TDS_STATUS_ID") private Long tdsStatusId;
	@Column(name = "TDS_STATUS_NAME")  private String tdsStatusName;
	@Column(name = "FIN_BOK_FRM_TDS_DTLS_ID") private Long finBookingFormTdsDtlsId ;
	
	@Column(name = "DEMAND_NOTE_NO") private String demandNoteNo;
	@Column(name = "DEMAND_NOTE_DATE") private Timestamp demandNoteDate;
	
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;
	@Column(name = "TOTAL_DEMAND_NOTE_TOTAL_AMT") private String demandNoteTotalAmount;
	//@Column(name = "TDS_DETAILS_STATUS_ID") private Long tdsDetailsStatusId;
	@Column(name = "DAYS_DIFF") private Long daysDelayed;
	private List<FinPenaltyStatisticsPojo> penaltyStatisticsPojos;
	private List<FinBookingFormAccountsStatementPojo> bookingFormAccountStatementPojo ;
	private List<FinBookingFormMstSchTaxMapInfo> bokFrmDemNteSchTaxMapInfos;
	private String isThisRecordExistsInDB;
	private List<FinBookingFormModiCostDtlsPojo> bookingFormModiCostDtlsPojos;
	private List<FinBookingFormLglCostDtlsPojo> bookingFormLglCostDtlsPojos;
	private List<FinBookingFormMaintenanceDtlsPojo> maintenanceChargesDtlsPojos;
	private List<FinBookingFormFlatKhataDtlsPojo> flatKhataChargesDtlsPojos;
	private FinBookingFormAccountsPojo milestonePenaltyRecordPojo;
	private List<FinPenaltyPojo> penaltyPojoList;
	private Boolean isThisLastMilestone;
	@Column(name = "BLOCK_NAME")
	private String blockName;
}
