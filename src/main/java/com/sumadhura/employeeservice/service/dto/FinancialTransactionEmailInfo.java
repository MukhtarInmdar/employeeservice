package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Aniket Chavan
 * @since 07-10-2020
 * @time 05-30 PM
 */
@Setter
@Getter
@ToString
public class FinancialTransactionEmailInfo {
	private Long portNumber;
	private String portalUrl;
	private String currentApprovalLevelName;
	private String nextApprovalLevelName;
	private String buttonType;
	private String paymentTowards;
	private List<FinancialTransactionDetailsInfo> financialTransactionDetailsList;

}
