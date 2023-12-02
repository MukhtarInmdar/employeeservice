package com.sumadhura.employeeservice.service.dto;

import lombok.Data;

@Data
public class CarParkingAllotmentPdfDetailInfo {
	
	private String rightSidelogoForPdf;
	private String leftSidelogoForPdf;
	private String thanksAndRegardsFrom;
	private String greetingsFrom;
	private String allotmentDate;
	
	private String basementName;
	private String slotName;
	private String flatNo;
	private String custName;
	private String siteName;
	private String siteAddress;
	
	private String companyName;
	private String companyBillingAddress;
	private String companyTelephoneNo;
	private String companyEmail;
	private String companyCin;
	private String companyLlpin;
	private String companyGstin;
	private String companyWebsite;
	private String companyPanNumber;
	private String city;
	
	private String allotmentLetterFilePath;
	private String signatureEmp;
}
