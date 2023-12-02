package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class FinancialConsolidatedReceiptPdfInfo {
	private String consolidatedReceiptDate;
	private String customerNames;
	private String siteName;
	private String flatNo;
	private String sbua;
	private String bookingDate;
	private Long bookingFormId;
	
	private String totalFlatCost;
	private String totalPaidAmount;
	private String balanceOnTotalCost;
	
	//Receipt table sum
	private String totalPrincipalAmount;
	private String totalCgstAmount;
	private String totalSgstAmount;
	private String totalAmount;
	
	//Bounce Transactions
	private String totalBouncePrincipalAmount;
	private String totalBounceCgstAmount;
	private String totalBounceSgstAmount;
	private String totalBounceAmount;
	
	private String currentDate;
	private String companyLogoPath;
	private String siteAddress;
	
	private String consolidatedPdfFileName;
	
	private String companyName;
	private String companyNameFooter;
	private String companyBillingAddress;
	private String companyTelephoneNo;
	private String companyEmail;
	private String companyCin;
	private String companyLlpin;
	private String companyGstin;
	private String companyWebsite;
	private String companyPanNumber;
	private String companyCity;
	
	//private String logoForPdf;
	private String rightSidelogoForPdf;
	private String leftSidelogoForPdf;
	
	private String rightSidelogoFilePath;
	private String leftSidelogoFilePath;
	
	private String thanksAndRegardsFrom;
	private String greetingsFrom;	
	private Boolean isBounceTransactionExists;
	
	private List<FinTransactionEntryDetailsPdfInfo> finCompletedTransactionsList;
	private List<FinTransactionEntryDetailsPdfInfo> finBouncedTransactionsList;	
}


