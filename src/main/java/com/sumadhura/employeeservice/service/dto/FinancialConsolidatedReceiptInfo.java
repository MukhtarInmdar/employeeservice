package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;

import lombok.Data;

@Data
public class FinancialConsolidatedReceiptInfo {
	private List<CustomerPropertyDetailsPojo> CustomerPropertyDetailsPojoList;
	private List<CoApplicantPojo> coApplicantPojoList;
	private List<AddressPojo> siteAddressPojoList;
	private List<OfficeDtlsResponse> officeDetailsList;
	
	private List<FinConsolidatedReceiptInfo> finCompletedTransactionsList;
	private List<FinConsolidatedReceiptInfo> finBouncedTransactionsList;
	
	private Double totalFlatCost;
	private Double totalPaidAmount;
	private Double balanceOnTotalCost;
	
	//Receipt table
	private Double totalPrincipalAmount;
	private Double totalCgstAmount;
	private Double totalSgstAmount;
	private Double totalAmount;
	
	//Bounced table
	private Double totalBouncePrincipalAmount;
	private Double totalBounceCgstAmount;
	private Double totalBounceSgstAmount;
	private Double totalBounceAmount;
	
	//private FinancialConsolidatedReceiptPdfInfo financialConsolidatedReceiptPdfInfo;
	

}


