package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.FinPenaltyStatisticsPojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author @NIKET CH@V@N
 * @since 11.01.2020
 * @time 06:50 PM
 * @description this class is for holding request data for financial module
 */
@ToString
@Getter
@Setter
public class FinancialCustomerDetails implements Cloneable { 
	
	private Long sno;
	private String siteName;
	private String customerName;
	private String flatNo;
	private Long bookingFormId;
	private String bookingDate;
	private String agreementDate;
	private String saleDeedDate;
	private String milestoneName;
	private Long mileStoneNo;
	private String totalAgreementCost;
	private String totalPayAmount;
	private String totalPaidAmount;
	private String totalInterestAmount;
	private String totalDelayedDays;
	private String sumOfInitiatedWaiverAmount;//totalInterestWaiverPendingAmount;
	private String totalPenalityPaidAmount;
	private String totalInterestWaiverAdjAmount;
	private String lastInterestWaiverAdjAmount;
	private String penaltybalanceAmount;
	private List<FinPenaltyStatisticsPojo> interestCalculationBreakUp;
	//private List<Map<String, Object>> paymentPaidList;
	/* Malladi Changes */
	private String bhk;
	private String handingOverDate;
	private String registrationDate;
	private String basicFlatCost;
	private String amenitiesFlatCost;
	private String totalFlatCostExclGst;
	//ACP
	
	private String corpusPayAmount;
	private String corpusPaidAmount;
	//private String corpusBalanceAmount;
	
	private String modificationPayAmount;
	private String modificationPaidAmount;
	//private String modificationBalanceAmount;
	
	private String legalPayAmount;
	private String legalPaidAmount;
	//private String legalBalanceAmount;
	
	private String flatKhataPayAmount;
	private String flatKhataPaidAmount;
	//private String flatKhataBalanceAmount;
	
	private String maintenanceChargePayAmount;
	private String maintenanceChargePaidAmount;
	//private String maintenanceChargeBalanceAmount;
	
}
