package com.sumadhura.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialUploadDataRequest {
	private String transactionReceiptDate;	
	private String pdfTitle;	
	private String transactionReceiptNo; 
	private String flatNo;	
	//private String projectName;
	private String siteName;
	private String customerNames;	
	private String address;	
	private String reraNo;	
	private String placeOfSupply;	
	private String b2C_B2B;	
	private String projectMilestoneId;
	private String milestoneName;// this is same like particulars;	
	private String sac;	
	private String dueAmountExcludeGST;//principalAmount;	
	private String cgstHeading;	
	private String sgstHeading;	
	private String totalCgstAmount;	
	private String totalSgstAmount;	
	private String totalReceiptPaidAmount;
	private String mileStoneAmountInWords;
	
	private String flatSaleOwner;
	private String companyName;
	private String companyBillingAddress;
	private String companyTelephoneNo;
	private String companyEmail;
	private String companyCin;
	private String companyLlpin;
	private String companyGstin;
	private String companyWebsite;
	private String companyCity;
	private String thanksAndRegardsFrom;
	private String companyPanNumber;

}
