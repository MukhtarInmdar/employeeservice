package com.sumadhura.employeeservice.dto;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentPdfDetailInfo;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.FinancialConsolidatedReceiptPdfInfo;
import com.sumadhura.employeeservice.service.dto.FinancialTransactionEmailInfo;
import com.sumadhura.employeeservice.service.dto.FinancialUploadDataInfo;
import com.sumadhura.employeeservice.service.dto.WelcomeMailGeneratorInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@ToString
public class Email extends Result{
	private static final long serialVersionUID = 7739092085577934577L;
	private String fromMail;
	private String toMail;
	private String[] toMails;
	private String fromMailPassword;
	private String subject;
	private String emailBodyText;
	private Exception exception;
	private String TemplateName;
	private String name;
	private Long mobileNo;
	private String custData;
	private String siteName;
	private boolean status;
	private String departmentName;
	private String employeeName;
	private String customerName;
	private String flatNumber;
	private Long ticketId;
	private String message;
	private String ticketOwner;
	private String ticketClouserDate;
	private String flatNo;
	private String pendingEmployeeAndDepartmentName;
	private Timestamp escalationTime;
	private String ticketType;
	private String ticketSubject;
	private Long employeeId;
	private Long departmentId;
	private String requestUrl;
	private Long ticketEscalationId;
	private Long toId;
	private Long toDeptId;
	private Long typeOf;
	private String mailOtpApproval;
	private String type;
	private String merchantId;
	private Timestamp ticketCreatedDate;
	private Timestamp ticketExpectedCloserDate;
	private String cc;
	private boolean flag;
	private List<DemandNoteGeneratorInfo> demandNoteGeneratorInfoList;
	private List<FinancialConsolidatedReceiptPdfInfo> consolidatedReceiptPdfInfos;
	private List<FinancialUploadDataInfo> financialUploadDataRequests;
	private Map<String,List<Map<String,Object>>> dataForPdf;
	private List<Map<String,String>> dataForTemplate;
	private String demandNoteGeneratedDate;
	private DemandNoteGeneratorInfo demandNoteGeneratorInfo;
	private WelcomeMailGeneratorInfo welcomeMailGeneratorInfo;
	private String thanksAndRegardsFrom;
	//private List<FinancialTransactionEmailInfo> financialTransactionEmailInfoList;
	private FinancialTransactionEmailInfo financialTransactionEmailInfo;
	private String filePath;
	private String fileName;
    private Long portNumber;
    private List<Long> siteIds;
	private Timestamp startDate;
	private Timestamp endDate;
	private Long startDateParam;
	private Long endDateParam;
	private String siteIdsParam;
	private String[] ccs;
	private String[] bccs;
	private String categoryToBeRaised;
	private Long id;
	private String title;
	private CarParkingAllotmentPdfDetailInfo carParkingAllotmentPdfDetailInfo;
	
	private String referrerName;
	private String cityName;
	private String stateName;
	private String referrerEmailId;
	private String empComments;
	private List<String> flatPreferences;
	private String customerSiteShortName;
	private String referralStatusName;
	private Long referralStatusValue;
	private String empEmailId;
	private Long pincode;
	private List<String> interestedSiteNames;
	private String refrenceId;
	private String refferalPhoneNo;
	private String siteShortNameWithFlatNo;
	
	private String basementName;
	private String slotName;
	private String approverEmpName;
	
	/*Venkatesh B*/
	private String plcCornerflatTotalCost;
	private String plcEastFacingTotalCost;
	private String infraAndAmenitesTotalCost;
	private String floorRiseTotalCost;
	private String plcPremiumFacingChanrgesTotalCost;
	private String carPrakingTotalCost;
	private String clubHouseTotalCost;
	
	
	private Double totalAgreementCost;
	private List<String> filePaths;
	private String totalCost;
	private String interestedBankName;
	private String bankContactPersonName;
	private String contactNumber;
	private String customerEmail;
	@Column(name = "BOOKING_DATE")
	private String bookingDate;
	
}
