package com.sumadhura.employeeservice.service.helpers;


import static com.sumadhura.employeeservice.util.Util.isEmptyObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.EmpFinPushLegalAndModifiNotificationInfo;
import com.sumadhura.employeeservice.dto.EmployeeFinTranPaymentSetOffRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.EmployeeFinancialRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialResponse;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinBookingFormLegalCostPdfResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormLegalCostResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormLglCostDtlsResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormModiCostDtlsResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormModiCostResponse;
import com.sumadhura.employeeservice.dto.FinProjectAccountResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryDetailsResponse;
import com.sumadhura.employeeservice.dto.Financial;
import com.sumadhura.employeeservice.dto.FinancialDemandNoteMS_TRN_Request;
import com.sumadhura.employeeservice.dto.FinancialProjectMileStoneResponse;
import com.sumadhura.employeeservice.dto.MileStone;
import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.FinEnum;
import com.sumadhura.employeeservice.enums.FinTransactionMode;
import com.sumadhura.employeeservice.enums.FinTransactionType;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsResponse;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormDemandNotePojo;
import com.sumadhura.employeeservice.persistence.dto.FinClosingBalanceReportPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprovalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialProjectMileStonePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.ModicationInvoiceAppRej;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;
import com.sumadhura.employeeservice.service.EmployeeFinancialService;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.AgreementDraftCalculations;
import com.sumadhura.employeeservice.service.dto.AminitiesInfraCostInfo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.Co_ApplicantInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.DynamicKeyValueInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinTranPaymentSetOffInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialMultipleTRNInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.FinConsolidatedReceiptInfo;
import com.sumadhura.employeeservice.service.dto.FinTransactionEntryDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinTransactionEntryDetailsPdfInfo;
import com.sumadhura.employeeservice.service.dto.FinancialConsolidatedReceiptInfo;
import com.sumadhura.employeeservice.service.dto.FinancialConsolidatedReceiptPdfInfo;
import com.sumadhura.employeeservice.service.dto.FinancialCustomerDetails;
import com.sumadhura.employeeservice.service.dto.FinancialDemandNoteMS_TRN_Info;
import com.sumadhura.employeeservice.service.dto.FinancialProjectMileStoneInfo;
import com.sumadhura.employeeservice.service.dto.FinancialTransactionDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinancialTransactionEmailInfo;
import com.sumadhura.employeeservice.service.dto.FinancialUploadDataInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.dto.FlatCostInfo;
import com.sumadhura.employeeservice.service.dto.MileStoneInfo;
import com.sumadhura.employeeservice.service.dto.ProfessionalInfo;
import com.sumadhura.employeeservice.service.dto.WelcomeMailGeneratorInfo;
import com.sumadhura.employeeservice.util.AESEncryptDecrypt;
import com.sumadhura.employeeservice.util.CsvGeneratorHelper;
import com.sumadhura.employeeservice.util.CurrencyUtil;
import com.sumadhura.employeeservice.util.IOSPushNotificationUtil;
import com.sumadhura.employeeservice.util.NumberToWord;
import com.sumadhura.employeeservice.util.PdfHelper;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;
import com.turo.pushy.apns.ApnsClientBuilder;

@Component("employeeFinancialHelper")
public class EmployeeFinancialHelper {

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
	@Autowired(required = true)
	@Qualifier("PdfHelper")
	private PdfHelper pdfHelper;

	@Autowired(required = true)
	@Qualifier("CsvHelper")
	private CsvGeneratorHelper csvGeneratorHelper;
	
	@Autowired(required = true)
	@Qualifier("EmployeeFinancialPushNotificationHelper")
	private EmployeeFinancialPushNotificationHelper pushNotificationHelper;
	
	@Autowired(required = true)
	private ResponceCodesUtil responceCodesUtil;
	
	@Autowired
	private CurrencyUtil currencyUtil;
	
	@Autowired
	@Qualifier("EmployeeFinancialService")
	private EmployeeFinancialService employeeFinancialService;

	
	private static final RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static final int roundingModeSize = 2;
	//private int roundingModeFinalAmountSize = 2;
	
	private static final List<String> builders = Arrays.asList("1","3","4","5");//*add here only builder id's
	private static final List<String> landOwner = Arrays.asList("2","6","7","8","9","10");//*add here only land owner id's
	
	private static  String NOTIFICATION_TITLE = "Sumadhura Demand Note";
	private static  String NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
	private static  String NOTIFICATION_TYPE = "Sumadhura Demand Note";
	private static  String NOTIFICATION_TYPE1 = "Sumadhura Demand Note";
	
	public List<FileInfo> demandNoteGenaratorHelper(List<EmployeeFinancialResponse> employeeFinancialResponse, String requestFrom, 
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo, final Map<Long, List<FinancialProjectMileStonePojo>> flatPreviousMilestoneNonPaidData)
			throws Exception {
		log.info(" ***** control is inside the demandNoteGenaratorHelper in EmployeeFinancialHelper ***** "+employeeFinancialResponse.size()+" employeeFinancialServiceInfo.isThisUplaodedData()"+employeeFinancialServiceInfo.isThisUplaodedData());
		//boolean isThisUploadedData = employeeFinancialServiceInfo.isThisUplaodedData();
		Properties prop = responceCodesUtil.getApplicationProperties();
		List<FileInfo> fileInfoList = new ArrayList<>();
		final List<EmployeeFinancialPushNotificationInfo> pushNotification = new ArrayList<EmployeeFinancialPushNotificationInfo>();
		final boolean isReGenerateDemandNote =  Util.isEmptyObject(employeeFinancialServiceInfo.isReGenerateDemandNote())?false:employeeFinancialServiceInfo.isReGenerateDemandNote();
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String rightSidelogoFilePath = "";
		String leftSidelogoFilePath = "";
		//Map<String,String> isFilePathFound = new HashMap<>();
		String thanksAndRegardsFrom = "";
		String greetingsFrom = "";
		
		//getTheCommonInformation(employeeFinancialServiceInfo);
		
		/*if(employeeFinancialServiceInfo.getSiteId()!=null && employeeFinancialServiceInfo.getSiteId().equals(131l)) {
			rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_LOGO1");
			thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_GREETING_MSG_FROM");
			NOTIFICATION_TITLE = "Aspire Aurum demand Note";
			NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
			NOTIFICATION_TYPE = "Sumadhura Demand Note";
			NOTIFICATION_TYPE1 = "Sumadhura Demand Note";
		} else {
			rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1");
			thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_GREETING_MSG_FROM");
			NOTIFICATION_TITLE = "Sumadhura Demand Note";
			NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
			NOTIFICATION_TYPE = "Sumadhura Demand Note";
			NOTIFICATION_TYPE1 = "Sumadhura Demand Note";
		}*/
		
		/* For Interest Letter We need to Change Notification Title and Body */
		if("GenerateDemandNote".equalsIgnoreCase(requestFrom) && "Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			NOTIFICATION_TITLE = "Interest Note";
			NOTIFICATION_BODY = "Dear Customer your Interest Note has been issued on your flat.";
			NOTIFICATION_TYPE = "Sumadhura Interest Letter";
			NOTIFICATION_TYPE1 = "Sumadhura Interest Letter";
		}
		
		//boolean isShowGstInPDF = employeeFinancialServiceInfo.isShowGstInPDF();
		/* generating demand note for different flats */
		if(Util.isEmptyObject(employeeFinancialResponse)) {
			log.info("Failed to generate demand note, No pending milestone data found. demandNoteGenaratorHelper");
			throw new EmployeeFinancialServiceException("Failed to generate demand note, No pending milestone data found.");
		}
		if(Util.isEmptyObject(employeeFinancialResponse.get(0).getFlatsResponse())) {
			//if customer details not found
			log.info("Failed to generate demand note, Customer data not found.");
			throw new EmployeeFinancialServiceException("Failed to generate demand note, Customer data not found.");
		}
		/*if(employeeFinancialServiceInfo.getSiteName()==null) {
			log.info("Failed to generate demand note, Site data missing.");
			throw new EmployeeFinancialServiceException("Failed to generate demand note, Site data missing.");
		}*/
		//CustomerPropertyDetailsInfo  customerPropertyDetailsInfo = employeeFinancialResponse.get(0).getFlatsResponse().get(0).getSiteName();
		Site site = new Site();
		site.setSiteId(employeeFinancialServiceInfo.getSiteId());
		site.setName(employeeFinancialServiceInfo.getSiteName());		

		for(EmployeeFinancialResponse resp : employeeFinancialResponse ) {
			List<MileStoneInfo> mileStoneInfoList = new ArrayList<>();
			Email email = new Email();
			FileInfo fileInfo = new FileInfo();
			CustomerPropertyDetailsInfo  customerPropertyDetailsInfo = null;
			if(Util.isEmptyObject(resp.getFlatsResponse())) {
				//if customer details not found
				log.info("Failed to generate demand note, Customer data not found.");
				throw new EmployeeFinancialServiceException("Failed to generate demand note, Customer data not found.");
			}
			customerPropertyDetailsInfo = resp.getFlatsResponse().get(0);
			
			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			rightSidelogoFilePath = logoAndOtherDetails.get("rightSidelogoFilePath");
			leftSidelogoFilePath = logoAndOtherDetails.get("leftSidelogoFilePath");

			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
			
			if(employeeFinancialServiceInfo.getSiteId()!=null && employeeFinancialServiceInfo.getSiteId().equals(131l) &&!"Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				NOTIFICATION_TITLE = "Aspire Aurum demand Note";
				NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
				NOTIFICATION_TYPE = "Sumadhura Demand Note";
				NOTIFICATION_TYPE1 = "Sumadhura Demand Note";
			} else {
				if ("Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
					NOTIFICATION_TITLE = customerPropertyDetailsInfo.getSiteName() + " Interest Letter";
					NOTIFICATION_BODY = "Dear Customer intrest letter  has been generated. please check in finance page.";
					NOTIFICATION_TYPE = "Interest Letter";
					NOTIFICATION_TYPE1 = "Interest Letter";
				} else {

					NOTIFICATION_TITLE = customerPropertyDetailsInfo.getSiteName() + " Demand Note";
					NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
					NOTIFICATION_TYPE = "Sumadhura Demand Note";
					NOTIFICATION_TYPE1 = "Sumadhura Demand Note";
				}
			}

			DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
			
			if(Util.isNotEmptyObject(rightSidelogoFilePath)) {//checking file is present in local folder, if exists loading file from local folder
				if(isFilePathFoundPdf.get(rightSidelogoFilePath)!=null && isFilePathFoundPdf.get(rightSidelogoFilePath).equals("true")) {
					info.setRightSidelogoFilePath(rightSidelogoFilePath);
				} else {
					File file = new File(rightSidelogoFilePath);
					if(file.exists()) {//checking file is exists or not, if exist the loading this file in pdf
						isFilePathFoundPdf.put(rightSidelogoFilePath, "true");
						info.setRightSidelogoFilePath(rightSidelogoFilePath);
					}
				}
			}
			
			if(Util.isNotEmptyObject(leftSidelogoFilePath)) {
				if(isFilePathFoundPdf.get(leftSidelogoFilePath)!=null && isFilePathFoundPdf.get(leftSidelogoFilePath).equals("true")) {
					info.setLeftSidelogoFilePath(leftSidelogoFilePath);
				} else {
					File file = new File(leftSidelogoFilePath);
					if(file.exists()) {
						isFilePathFoundPdf.put(leftSidelogoFilePath, "true");
						info.setLeftSidelogoFilePath(leftSidelogoFilePath);
					}
				}
			}

			

			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			info.setCurrentMileStoneName(resp.getCurrentMilestoneName());

			//info.setSumOfDemandNotePercentage(employeeFinancialServiceInfo.getSumOfDemandNotePercentage());
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSumOfDemandNotePercentage())) {
				info.setSumOfDemandNotePercentage(BigDecimal.valueOf(employeeFinancialServiceInfo.getSumOfDemandNotePercentage()).setScale(roundingModeSize, roundingMode).toString());
			}else {
				info.setSumOfDemandNotePercentage("0");
			}
			//10,000.536000001454 = 10,000.53 
			/* current milestone due date */
			if(Util.isNotEmptyObject(resp.getCurrentMilestoneDueDate())) {
				info.setCurrentMilestoneDueDate(TimeUtil.timestampToDD_MM_YYYY(resp.getCurrentMilestoneDueDate()));
			}else {
				info.setCurrentMilestoneDueDate("N/A");
			}
			
			if(Util.isEmptyObject(resp.getFinancialProjectMileStoneResponse())) {
				String flatNo = "";
				if(resp.getFlatsResponse()!=null && resp.getFlatsResponse().size()!=0) {
					flatNo = resp.getFlatsResponse().get(0).getFlatNo();
				}
				log.info("Failed to generate demand note, No pending milestone data found. Flat No : "+flatNo);
				throw new EmployeeFinancialServiceException("Failed to generate demand note, No pending milestone data found. Flat No : "+flatNo);
			}
			/* setting demandNote Generated Date */
			if(Util.isNotEmptyObject(resp.getDemandNoteDate())) {
				info.setDemandNoteGeneratedDate(TimeUtil.timestampToDD_MM_YYYY(resp.getDemandNoteDate()));
			} else {
				info.setDemandNoteGeneratedDate("N/A");
			}
			
			if(Util.isNotEmptyObject(resp.isShowGstInPDF())) {
				info.setIsShowGstInPDF(resp.isShowGstInPDF());
			} else {
				info.setIsShowGstInPDF(Boolean.TRUE);
			}
			
			/* setting applicant and coApplicant names and mobile numbers */
			if(Util.isNotEmptyObject(resp.getFlatsResponse())) {
				List<CustomerPropertyDetailsInfo> detailsList  = resp.getFlatsResponse();
				StringBuilder name = new StringBuilder(); 
				StringBuilder mobileNos = new StringBuilder();
				StringBuilder flatNo = new StringBuilder();
				StringBuilder floorName = new StringBuilder();
				StringBuilder blockName = new StringBuilder();
				StringBuilder siteName = new StringBuilder();
				StringBuilder pancard = new StringBuilder();
				StringBuilder finSchemeId = new StringBuilder();
				StringBuilder finSchemeName = new StringBuilder();
				StringBuilder finSchemeType = new StringBuilder();
				if(resp.getFlatsResponse().size()>1) {
					//this code only for UAT and CUG
					throw new EmployeeFinancialServiceException("Failed to generate demand note, found more than one record for customer");
				}
				
				Long noOfCustomersInFlat=0l;
//				for(CustomerPropertyDetailsInfo  customerPropertyDetailsInfo : resp.getFlatsResponse()){
				for (int index = 0; index <  detailsList.size(); index++) {
						customerPropertyDetailsInfo = detailsList.get(index);				
					
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName()) || Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())) {
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName())){	
						name.append(customerPropertyDetailsInfo.getCustomerName());
						noOfCustomersInFlat++;
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())){
							name.append(" & &nbsp;");
							name.append(customerPropertyDetailsInfo.getCoAppFullName());
							noOfCustomersInFlat++;
						}
					}else {
						name.append("N/A");
					}
					}else {
						name.append("N/A");
					}
					
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber()) ||  Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())) {
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
							mobileNos.append(customerPropertyDetailsInfo.getContactNumber());
						}
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())){
							if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
								mobileNos.append(",");
							}
							mobileNos.append(customerPropertyDetailsInfo.getAlternatePhoneNo());
						}
					}else {
						mobileNos.append("N/A");
					}
					
					/* setting flat Number */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
						flatNo.append(customerPropertyDetailsInfo.getFlatNo());
					}else {
						flatNo.append("N/A");
					}
					
					/* setting Floor Name   */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFloorName())) {
						floorName.append(customerPropertyDetailsInfo.getFloorName());
					}else {
						floorName.append("N/A");
					}
					
					/* setting Block name */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBlockName())) {
						blockName.append(customerPropertyDetailsInfo.getBlockName());
					}else {
						blockName.append("N/A");
					}
					
					/* setting Site Name */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteName())) {
						siteName.append(customerPropertyDetailsInfo.getSiteName());
					}else {
						siteName.append("N/A");
					}
					
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeName())) {
						finSchemeName.append(customerPropertyDetailsInfo.getFinSchemeName());
					} else {
						finSchemeName.append("N/A");
						log.info("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
						//throw new EmployeeFinancialServiceException("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
					}
					
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeType())) {
						finSchemeType.append(customerPropertyDetailsInfo.getFinSchemeType());
					} else {
						finSchemeType.append("N/A");
					}
					
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeId())) {
						finSchemeId.append(customerPropertyDetailsInfo.getFinSchemeId());
					} else {
						finSchemeId.append("N/A");
						log.info("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
						//throw new EmployeeFinancialServiceException("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
					}
					
					/* setting customer pancard  */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getPancard())) {
						pancard.append(customerPropertyDetailsInfo.getPancard());
					}else {
						pancard.append("N/A");
					}
					
					/* setting siteId */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteId())) {
						info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
					}else {
						info.setSiteId("Anonymous_Site");
					}
					
					/* setting flatBookingId */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatBookingId())) {
						info.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId().toString());
						info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
					}else {
						info.setFlatBookingId("Anonymous_Flat_Booking");
					}
				}
				info.setNoOfCustomersIncludedInBooking(noOfCustomersInFlat);
				info.setCustomerNames(name.toString());
				info.setMobileNumbers(mobileNos.toString());
				info.setFlatName(flatNo.toString());
				info.setFloorName(floorName.toString());
				info.setBlockName(blockName.toString());
				info.setFinSchemeName(finSchemeName.toString());
				info.setFinSchemeId(finSchemeId.toString());
				info.setFinSchemeType(finSchemeType.toString());
				if(info.getSiteId()!=null && info.getSiteId().equals("131")) {
					info.setSiteName("Aspire Aurum");
				}else {
					info.setSiteName(siteName.toString());
				}
				info.setPancard(pancard.toString());
			}
			
			/* setting applicant permenent address  */
			if(Util.isNotEmptyObject(resp.getCustomerAddressInfoList())) {
				for(AddressInfo addressInfo : resp.getCustomerAddressInfoList()){
				/* Taking correspondence Address  */	
                 if(Util.isNotEmptyObject(addressInfo)) {
                	 if(addressInfo.getAddressMappingType().getAddressType().equalsIgnoreCase("Correspondence")){
                		 if(Util.isNotEmptyObject(addressInfo.getAddress1())) {
                		    info.setAddress(addressInfo.getAddress1());
                			break;
                		    /*if(Util.isNotEmptyObject(addressInfo.getPincode())) {
                		    	 info.setAddress(info.getAddress()+"-"+addressInfo.getPincode());
                		    	 break;
                		    }*/
                		 }else {
                			  info.setAddress("N/A"); 
                		 }
                	 }else {
                		 info.setAddress("N/A");  
                	 }
                 }
			  }
			}else {
			   info.setAddress("N/A");
			}
			
			getSiteAdderss(resp.getSiteAddressInfoList(),info,customerPropertyDetailsInfo);
			
			/* setting milestone amount */
			/*  settimg milestone amount in words */
			
			if(Util.isNotEmptyObject(resp.getTotalDemandNoteAmount())) {
				
				String amountInWords = getTheAmountInWords(resp.getTotalDemandNoteAmount());
				info.setMileStoneAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalDemandNoteAmount()).setScale(roundingModeSize, roundingMode).toString()));
				info.setMileStoneAmountInWords(amountInWords);
			}else {
				info.setMileStoneAmount("0.00");
				info.setMileStoneAmountInWords("Zero Rupees Only ");
			}
			
			/* setting Total Flat Amount */
			if(Util.isNotEmptyObject(resp.getTotalMileStoneAmount())) {
				info.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalMileStoneAmount()).setScale(roundingModeSize, roundingMode).toString()));
			}else {
				info.setTotalAmount("0.00");	
			}
			/* Total Received Amount or Customer Paid amount */
			if(Util.isNotEmptyObject(resp.getTotalPaidAmount())) {
				info.setTotalPaidAmount(getTheAmountWithCommas(resp.getTotalPaidAmount()));
				//info.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));
			}else {
				info.setTotalPaidAmount("0.00");	
			}
			/* Total Due Amount Excluding GST */
			if(Util.isNotEmptyObject(resp.getTotalmileStoneBasicAmount())) {
					info.setAmount(getTheAmountWithCommas(resp.getTotalmileStoneBasicAmount()));
				/*if(!isShowGstInPDF) {
					info.setAmount(getTheAmountWithCommas(resp.getTotalMileStoneAmount() - (resp.getTotalPaidAmount())));
				}*/
			}else {
				info.setAmount("0.00");	
			}
			/* Total CGST Amount */
			if(Util.isNotEmptyObject(resp.getTotalCgstAmount())) {//!employeeFinancialServiceInfo.isShowGstInPDF()
				info.setTotalCgstAmount(getTheAmountWithCommas(resp.getTotalCgstAmount()));
				/*if(!isShowGstInPDF) {
					info.setTotalCgstAmount("0.00");
				}*/
			}else {
				info.setTotalCgstAmount("0.00");	
			}
			/* Total SGST Amount */
			if(Util.isNotEmptyObject(resp.getTotalSgstAmount())) {//!employeeFinancialServiceInfo.isShowGstInPDF()
				info.setTotalSgstAmount(getTheAmountWithCommas(resp.getTotalSgstAmount()));
				/*if(!isShowGstInPDF) {
					info.setTotalSgstAmount("0.00");
				}*/
			}else {
				info.setTotalSgstAmount("0.00");	
			}
			/* Total Due Amount */
			if(Util.isNotEmptyObject(resp.getTotalDueAmount())) {
				info.setTotalDueAmount(getTheAmountWithCommas(resp.getTotalDueAmount()));
			}else {
				info.setTotalDueAmount("0.00");	
			}
			
			/* Total Pending Penalty Amount */
			if(Util.isNotEmptyObject(resp.getTotalPendingPenaltyAmount())) {
				info.setTotalPendingPenaltyAmount(getTheAmountWithCommas(resp.getTotalPendingPenaltyAmount()));
			}else {
				info.setTotalPendingPenaltyAmount("0.00");	
			}
			
			if(Util.isNotEmptyObject(resp.getTotalMilestoneDuePercent())) {
				info.setTotalMilestoneDuePercent(getTheAmountWithCommas(resp.getTotalMilestoneDuePercent()));
			}else {
				info.setTotalMilestoneDuePercent("0.0");
			}
			
			/* setting Demand Note PDF File Name */
			if(Util.isNotEmptyObject(resp.getDemandNotePdfFileName())) {
				info.setDemandNotePdfFileName(resp.getDemandNotePdfFileName());
			}else {
				info.setDemandNotePdfFileName("demandNoteForCurrentMileStone.pdf");
			}
		 
			/* setting site Account  */
         	if(Util.isNotEmptyObject(resp.getFinProjectAccountResponseList())) {
         		StringBuilder accountInfo = new StringBuilder();
         		for(FinProjectAccountResponse accountResponse : resp.getFinProjectAccountResponseList()) {
         			/* setting bank details  */
         			if(Util.isNotEmptyObject(accountResponse.getBankName())){
         				accountInfo.append(accountResponse.getBankName());
         				accountInfo.append(",");
         			}
         			
         			if(Util.isNotEmptyObject(accountResponse.getAccountHolderName())){
         				info.setAccountHolderName(accountResponse.getAccountHolderName());
         			} else {
         				throw new EmployeeFinancialServiceException("Account holder name missing..!");
         			}
         			/* setting bank account details  */
         			if(Util.isNotEmptyObject(accountResponse.getSiteBankAccountNumber())){
         				accountInfo.append("<strong> A/C. "+accountResponse.getSiteBankAccountNumber()+"</strong>");
         				accountInfo.append(",");
         			}
         			
         			/* setting Account Address */
         			if(Util.isNotEmptyObject(accountResponse.getAccountAddress())){
         				accountInfo.append(accountResponse.getAccountAddress());
         				accountInfo.append(",");
         			}
         			
         			/* setting Account IFSC Code */
         			if(Util.isNotEmptyObject(accountResponse.getIfscCode())){
         				accountInfo.append(" IFSC CODE - ");
         				accountInfo.append(accountResponse.getIfscCode());
         			}
         	    }
         		info.setSiteAccount(accountInfo.toString());
         	}
         	int serialNumber = 1;
         	Double currentMilestoneDueAmount = 0.0;
         	Double previousMilestoneDueAmount = 0.0;
         	for(FinancialProjectMileStoneResponse response : resp.getFinancialProjectMileStoneResponse()) {
         		MileStoneInfo mileStoneInfo = new MileStoneInfo();
         		
         		if(Util.isNotEmptyObject(response.getMilestoneName()) && response.getMilestoneName().equalsIgnoreCase("Dummy MileStone for Regenerate Demand Note")) {
         			continue;
         		}
         		if(isReGenerateDemandNote) {
         			if(Util.isNotEmptyObject(response.getDemandNoteDate())) {
         				//info.setDemandNoteGeneratedDate(TimeUtil.timestampToDD_MM_YYYY(response.getDemandNoteDate()));	
         			}else {
         				info.setDemandNoteGeneratedDate("N/A");
         			}
         		}
         		info.setFinBokFomDmdNoteId(response.getFinBokFomDmdNoteId()==null?0l:response.getFinBokFomDmdNoteId());
         		info.setDemandNoteNo(response.getDemandNoteNo()==null?"N/A":response.getDemandNoteNo());
         		//log.info("EmployeeFinancialHelper.demandNoteGenaratorHelper() BookingFormDemandNoteId == "+response.getFinBokFomDmdNoteId());
         		/* setting Milestone Number  */
				/*if (Util.isNotEmptyObject(response.getMileStoneNo())) {
					mileStoneInfo.setProjectMilestoneId(response.getMileStoneNo().toString());
				} else {
					mileStoneInfo.setProjectMilestoneId("N/A");
				}*/
				
				mileStoneInfo.setProjectMilestoneId(String.valueOf(serialNumber));
				serialNumber++;
        		/* setting Milestone Name */
				if (Util.isNotEmptyObject(response.getMilestoneName())) {
					mileStoneInfo.setMilestoneName(response.getMilestoneName());
				} else {
					mileStoneInfo.setMilestoneName("N/A");
				}
         		/* setting Due percentage  */
				if (Util.isNotEmptyObject(response.getMileStonePercentage())) {
					mileStoneInfo.setDue(response.getMileStonePercentage().toString());
				} else {
					mileStoneInfo.setDue("N/A");
				}
         		/* setting DemandNote Date */
				if (Util.isNotEmptyObject(response.getDemandNoteDate())) {
					//mileStoneInfo.setDemandNoteDate(TimeUtil.timestampToDD_MM_YYYY(response.getMilestoneDate()));
					mileStoneInfo.setDemandNoteDate(TimeUtil.timestampToDD_MM_YYYY(response.getDemandNoteDate()));
					if(isReGenerateDemandNote || requestFrom.equalsIgnoreCase("GenerateDemandNote")) {
						if (Util.isNotEmptyObject(response.getMilestoneDemandNoteDate())) {
							mileStoneInfo.setDemandNoteDate(TimeUtil.timestampToDD_MM_YYYY(response.getMilestoneDemandNoteDate()));		
						}else {
							mileStoneInfo.setDemandNoteDate("N/A");
							log.info("Failed to generate demand note, demand note date not found on Flat "+info.getFlatName());
							throw new EmployeeFinancialServiceException("Failed to generate demand note, demand note date not found on Flat "+info.getFlatName());
						}
					}
				} else {
					mileStoneInfo.setDemandNoteDate("");
					log.info("Failed to generate demand note, demand note date not found on Flat "+info.getFlatName());
					throw new EmployeeFinancialServiceException("Failed to generate demand note, demand note date not found on Flat "+info.getFlatName());
				}
         		/* setting DemandNote Due Date */
				if (Util.isNotEmptyObject(response.getMileStoneDueDate())) {
					mileStoneInfo.setDemandNoteDueDate(TimeUtil.timestampToDD_MM_YYYY(response.getMileStoneDueDate()));
					info.setLastMilestoneDueDate(mileStoneInfo.getDemandNoteDueDate());
				} else {
					info.setLastMilestoneDueDate("");
					mileStoneInfo.setDemandNoteDueDate("");
					log.info("Failed to generate demand note,Due date not found on Flat "+info.getFlatName());
					throw new EmployeeFinancialServiceException("Failed to generate demand note,Due date not found on Flat "+info.getFlatName());
				}
         		/* setting Total flat amount including GST  */
         		if(Util.isNotEmptyObject(response.getMileStoneTotalAmount())) {
         			mileStoneInfo.setTotalFlatCostIncludeGST(getTheAmountWithCommas(response.getMileStoneTotalAmount()));
         		}else {
         			mileStoneInfo.setTotalFlatCostIncludeGST("0.00");
         		}
         		/* setting Total flat amount Recived include GST   */
         		if(Util.isNotEmptyObject(response.getPaidAmount())) {
         			mileStoneInfo.setAmountRecivedIncludeGST(getTheAmountWithCommas(response.getPaidAmount()));
         		}else {
         			mileStoneInfo.setAmountRecivedIncludeGST("0.00");
         		}
         		/* setting Due amount excluding GST */
         		if(Util.isNotEmptyObject(response.getMileStoneTotalAmount())) {
         			if(Util.isNotEmptyObject(response.getPaidAmount())) {
         				mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(response.getMileStoneTotalAmount()-response.getMileStoneTaxAmount()-(response.getPaidAmount()==null?0d:response.getPaidAmount()))).setScale(roundingModeSize, roundingMode).toString()));
         			}else {
         				mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(response.getMileStoneTotalAmount()-response.getMileStoneTaxAmount()-(response.getPaidAmount()==null?0d:response.getPaidAmount()))).setScale(roundingModeSize, roundingMode).toString()));
         			}
         			/*if(!isShowGstInPDF) {
         				mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(response.getMileStoneTotalAmount()-(response.getPaidAmount()==null?0d:response.getPaidAmount()))).setScale(roundingModeSize, roundingMode).toString()));
         			}*/
         		}else {
         			mileStoneInfo.setDueAmountExcludeGST("0.00");
         		}
         		
        		/*double cgstAmount = 0;
         		double sgstAmount = 0;
         		if(Util.isNotEmptyObject(response.getBokFrmDemNteSchTaxMapInfos())) {
         			List<FinBookingFormMstSchTaxMapInfo> listOfSchemeDetails = response.getBokFrmDemNteSchTaxMapInfos();
         			//@SuppressWarnings("unused")
					double otherThanGstAmount = 0d;
         			for (FinBookingFormMstSchTaxMapInfo bokFrmDemNteSchTaxMapInfo : listOfSchemeDetails) {
         				if(Util.isNotEmptyObject(bokFrmDemNteSchTaxMapInfo.getTaxTypeId()) && bokFrmDemNteSchTaxMapInfo.getTaxTypeId().equals(MetadataId.GST.getId())) {
         					//cgstAmount = bokFrmDemNteSchTaxMapInfo.getBasicAmount()/2;
         					//sgstAmount = bokFrmDemNteSchTaxMapInfo.getBasicAmount()/2;
         					//gstPercentage = bokFrmDemNteSchTaxMapInfo.getPercentageValue();
         				} else {
         					otherThanGstAmount = bokFrmDemNteSchTaxMapInfo.getBasicAmount();
         				}
        			}
         			for (FinBookingFormMstSchTaxMapInfo bokFrmDemNteSchTaxMapInfo : listOfSchemeDetails) {
         				if(Util.isNotEmptyObject(bokFrmDemNteSchTaxMapInfo.getTaxTypeId()) && bokFrmDemNteSchTaxMapInfo.getTaxTypeId().equals(MetadataId.GST.getId())) {
         					double GSTAMount = ((bokFrmDemNteSchTaxMapInfo.getPercentageValue()/(bokFrmDemNteSchTaxMapInfo.getPercentageValue()+100))*(response.getPaidAmount()-otherThanGstAmount));//(mileStonePojo.getSetOffAmount()-otherThanGstAmount)
         					cgstAmount = bokFrmDemNteSchTaxMapInfo.getBasicAmount()/2;
         					sgstAmount = bokFrmDemNteSchTaxMapInfo.getBasicAmount()/2;
         				}
    				}
         		}*/
         		
         		/* setting CGST */
         		if(Util.isNotEmptyObject(response.getCgstAmount())) {
         			mileStoneInfo.setCGST(getTheAmountWithCommas(response.getCgstAmount()));
         			/*if(!isShowGstInPDF) {
         				mileStoneInfo.setCGST("0.00");
         			}*/
         		}else {
         			mileStoneInfo.setCGST("0.00");
         		}
         		/* setting SGST */
         		if(Util.isNotEmptyObject(response.getSgstAmount())) {
         			mileStoneInfo.setSGST(getTheAmountWithCommas(response.getSgstAmount()));
         			/*if(!isShowGstInPDF) {
         				mileStoneInfo.setSGST("0.00");
         			}*/
         		}else {
         			mileStoneInfo.setSGST("0.00");
         		}
         		/* setting Total Due Amount if(Util.isNotEmptyObject(response.getTotalDueAmount())) {*/
         		if(Util.isNotEmptyObject(response.getMileStoneTotalAmount())) {
         			mileStoneInfo.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getMileStoneTotalAmount()-(isEmptyObject(response.getPaidAmount())?0d:response.getPaidAmount())).setScale(roundingModeSize, roundingMode).toString()));
				} else {
					mileStoneInfo.setTotalDueAmount("0.00");
				}
         		
         		/* calculating total current milestone due amount based on current milestone ids */
         		if(employeeFinancialServiceInfo.getCurrentprojectMileStoneIds().contains(response.getProjectMilestoneId())) {
         			currentMilestoneDueAmount += (BigDecimal.valueOf(response.getMileStoneTotalAmount()-(isEmptyObject(response.getPaidAmount())?0d:response.getPaidAmount())).setScale(roundingModeSize, roundingMode)).doubleValue();
         		}
         		
         	  if(Util.isNotEmptyObject(resp.getIsInterestOrWithOutInterest())) {
         		  info.setIsInterestOrWithOutInterest(Boolean.valueOf(resp.getIsInterestOrWithOutInterest()));
         		 if(resp.getIsInterestOrWithOutInterest()) {
         		    /* setting Interest include GST */
         		    if(Util.isNotEmptyObject(response.getTotalPendingPenaltyAmount())) {
         		    	mileStoneInfo.setIntrestIncludeGST(getTheAmountWithCommas(response.getTotalPendingPenaltyAmount()));
         		    }else {
         		    	mileStoneInfo.setIntrestIncludeGST("0.00");
         		    }
         		    /* Total No of Days Delayed to Pay Amount is used in Interest Letter */ 
         		    if(Util.isNotEmptyObject(response.getDaysDelayed())) {
         		    	mileStoneInfo.setDaysDelayed(response.getDaysDelayed().toString());
         		    }else {
         		    	mileStoneInfo.setDaysDelayed("0");
         		    }
         	     }
         	  }		
         	  
         	  mileStoneInfoList.add(mileStoneInfo);
         	System.out.println(mileStoneInfoList);
         	}
			info.setMileStones(mileStoneInfoList);
			
			/* Total No of Days Delayed to Pay Amount is used in Interest Letter */
			if(Util.isNotEmptyObject(resp.getTotalDaysDelayed())) {
				info.setTotalDaysDelayed(resp.getTotalDaysDelayed().toString());
			}else {
				info.setTotalDaysDelayed("0");
			}
			
			/* calculating previous milestone Due amount */ 
			previousMilestoneDueAmount  = BigDecimal.valueOf(Util.isNotEmptyObject(resp.getTotalDueAmount())?resp.getTotalDueAmount():0.00).setScale(roundingModeSize, roundingMode).doubleValue()- BigDecimal.valueOf(currentMilestoneDueAmount).setScale(roundingModeSize, roundingMode).doubleValue() ;
			
			/* setting previous milestone due amount */
			info.setPreviousMilestoneDueAmount(getTheAmountWithCommas(previousMilestoneDueAmount));
			
			/* setting current milestone due amount */
			info.setCurrentMilestoneDueAmount(getTheAmountWithCommas(currentMilestoneDueAmount));
			
			if(mileStoneInfoList.isEmpty()) {
				log.info("Demand note not generated, No pending milestone details found on Flat "+info.getFlatName());
				throw new EmployeeFinancialServiceException("Demand note not generated, No pending milestone details found on Flat "+info.getFlatName());
			}
			
			if(Util.isNotEmptyObject(mileStoneInfoList)) {
				email.setFlag(true);
			}else {
				email.setFlag(false);
			}
			
			info.setFlatSaleOwnerIdBasedOnAccountId(Long.valueOf(resp.getFlatsResponse().get(0).getFlatSaleOwnerId()));
			info.setFlatSaleOwnerNameBasedOnAccountId(resp.getFlatsResponse().get(0).getFlatSaleOwner());
			
			EmployeeFinancialTransactionResponse trnResp = new EmployeeFinancialTransactionResponse();
			trnResp.setOfficeDetailsList(resp.getOfficeDetailsList());
			trnResp.setFlatsResponse(resp.getFlatsResponse());
			setOfficeDetails(trnResp,info);
			
			if(Util.isNotEmptyObject(resp.getFlatsResponse()) && resp.getFlatsResponse().get(0).getSiteId().equals(107l)
					&& !"1".equals(resp.getFlatsResponse().get(0).getFlatSaleOwnerId())
					) {
				//please refer FLATS_SALE_OWNERS table for "1", "1" is sale owner of the current project company and other are landlord flats
				//for "1" sale owner id company address fetching from DB
				info.setSiteName("Eden Garden");
				info.setThanksAndRegardsFrom("Thank You From EG");
				info.setReceivedAmountBy("I ");
			} else {
				info.setReceivedAmountBy("We ");//this code is for receipt pdf
			}
			
			/* generating pdf */
			List<DemandNoteGeneratorInfo> demandNoteGeneratorInfo = new ArrayList<>();
			demandNoteGeneratorInfo.add(info);
//			boolean execute = Util.isEmptyObject(prop.getProperty("SAVE_DEMAND_NOTE_DATA"))?true:prop.getProperty("SAVE_DEMAND_NOTE_DATA").equals("yes");
//			if(execute) {
//			//bvr
//			System.out.println("================================================================================");
//			System.out.println("Flat Booking Id            :"+info.getFlatBookingId());
//			System.out.println("Site Name                  :"+info.getSiteName());
//			System.out.println("Flat No                    :"+info.getFlatName());
//			System.out.println("total balance due amount   :"+info.getAmount());
//			System.out.println("total total due amount     :"+info.getTotalDueAmount());
//			System.out.println("total pending penalty amount :"+info.getTotalPendingPenaltyAmount());
//			System.out.println("================================================================================");
//			System.out.println(info);
//				//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//			String query ="INSERT INTO DEMAND_NOTE_DETAILS(DEMAND_NOTE_DETAILS_ID,SITE_NAME,FLAT_NO,BAL_DUE_AMOUNT,TOTAL_DUE_AMOUNT,TOTAL_INETEREST_AMOUNT,FLAT_BOOK_ID) VALUES(DEMAND_NOTE_DETAILS_SEQ.NEXTVAL,:siteName,:flatName,:Amount,:TotalDueAmount,:TotalPendingPenaltyAmount,:flatBookingId)";
//		    commonMethodToInsertData(query, info,"DEMAND_NOTE_DETAILS_ID" ); 
//		    
//		    //bvr
//		   }
			email.setDemandNoteGeneratorInfoList(demandNoteGeneratorInfo);
			fileInfo = pdfHelper.XMLWorkerHelper(email,requestFrom,employeeFinancialServiceInfo);
			resp.setFileInfoList(Arrays.asList(fileInfo));
		
			/** Setting Total Pending Penalty Amount to Update Interest Amount in Fin_Booking_Form_Demand_Note Table based 
			  * on Fin_Booking_Form_Demand_Note_Id for Interest Letter */
			if("GenerateDemandNote".equalsIgnoreCase(requestFrom) && "Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				fileInfo.setTotalPendingPenaltyAmount(resp.getTotalPendingPenaltyAmount());
			}
			EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo();
			if ("Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				financialPushNotification.setBookingFormId(info.getBookingFormId());
				financialPushNotification.setNotificationTitle(NOTIFICATION_TITLE);
				financialPushNotification.setNotificationBody(NOTIFICATION_BODY);
				financialPushNotification.setNotificationDescription("");
				financialPushNotification.setTypeMsg(NOTIFICATION_TYPE);
				financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1);
				financialPushNotification.setDemandNoteUrl(fileInfo.getUrl());
				financialPushNotification.setDemandNoteName(info.getDemandNoteNo());
				financialPushNotification.setDemandNoteCreatedDate(resp.getDemandNoteDate());
				financialPushNotification.setSiteId(employeeFinancialServiceInfo.getSiteId());
				financialPushNotification.setFlatSaleOwnerId(customerPropertyDetailsInfo.getFlatSaleOwnerId());
				financialPushNotification.setFlatSaleOwner(customerPropertyDetailsInfo.getFlatSaleOwner());
				pushNotification.add(financialPushNotification);
			} else {
				financialPushNotification.setBookingFormId(info.getBookingFormId());
				financialPushNotification.setNotificationTitle(NOTIFICATION_TITLE);
				financialPushNotification.setNotificationBody(NOTIFICATION_BODY);
				financialPushNotification.setNotificationDescription("");
				financialPushNotification.setTypeMsg(NOTIFICATION_TYPE);
				financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1);
				financialPushNotification.setDemandNoteUrl(fileInfo.getUrl());
				financialPushNotification.setDemandNoteName(info.getDemandNoteNo());
				financialPushNotification.setDemandNoteCreatedDate(resp.getDemandNoteDate());
				financialPushNotification.setSiteId(employeeFinancialServiceInfo.getSiteId());
				financialPushNotification.setFlatSaleOwnerId(customerPropertyDetailsInfo.getFlatSaleOwnerId());
				financialPushNotification.setFlatSaleOwner(customerPropertyDetailsInfo.getFlatSaleOwner());
				pushNotification.add(financialPushNotification);
			}
			//resp.setFileInfoList(Arrays.asList(fileInfo));
			fileInfoList.add(fileInfo);
		}
		boolean executePushNoti = true;
		boolean sendEmailAndNotification = true;
		if("Final_Demand_Note".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl()) && "saveBookingDetails".equalsIgnoreCase(employeeFinancialServiceInfo.getRequestUrl())) {
			executePushNoti = false;
		}
		
		sendEmailAndNotification = Util.isEmptyObject(prop.getProperty("SEND_NOTIFICATION_AND_EMAIL"))?true:prop.getProperty("SEND_NOTIFICATION_AND_EMAIL").equals("yes");
		System.out.println("sendEmailAndNotification "+sendEmailAndNotification+" "+prop.getProperty("SEND_NOTIFICATION_AND_EMAIL")+" "+prop.getProperty("SEND_NOTIFICATION_AND_EMAIL").equals("yes"));
		if (sendEmailAndNotification == false) {
			// if the value is false, then don't send the email and push notification
			executePushNoti = sendEmailAndNotification;
			employeeFinancialServiceInfo.setSendNotification(sendEmailAndNotification);
			System.out.println("stopped sendEmailAndNotification "+sendEmailAndNotification+" "+prop.getProperty("SEND_NOTIFICATION_AND_EMAIL")+" "+prop.getProperty("SEND_NOTIFICATION_AND_EMAIL").equals("yes"));
		}
		//new EmployeeFinancialPushNotification();
		if(requestFrom.equalsIgnoreCase("GenerateDemandNote") && !employeeFinancialServiceInfo.isThisUplaodedData() && executePushNoti
			&& "Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())
			&& employeeFinancialServiceInfo.isSendNotification()
				) {
			ExecutorService executorService = Executors.newFixedThreadPool(10);
				try {
					executorService.execute(new Runnable() {
						public void run() {
							for (EmployeeFinancialPushNotificationInfo pushNotificationInfo : pushNotification) {
								try {
									System.out.println("EmployeeFinancialHelper.demandNoteGenaratorHelper(...).new Runnable() {...}.run() "+pushNotificationInfo.getBookingFormId());
									if(Util.isNotEmptyObject(flatPreviousMilestoneNonPaidData)) {//if flatBooking Id is not exist in flatPreviousMilestoneNonPaidData that measn , for that booking demand note not generated, either it's failed to generate or already generated 
										if(!flatPreviousMilestoneNonPaidData.containsKey(pushNotificationInfo.getBookingFormId())) {
											continue;
										}
										if(Util.isEmptyObject(flatPreviousMilestoneNonPaidData.get(pushNotificationInfo.getBookingFormId()))) {
											continue;
										}
									}
									
									pushNotificationHelper.sendFinancialStatusNotification(pushNotificationInfo,null);
								} catch (InformationNotFoundException e) {
								 	e.printStackTrace();
								}
							}//pushNotification
						}
					});
					executorService.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
					executorService.shutdown();
				}
		}
		
		return fileInfoList;
	}
	@SuppressWarnings("unused")
	private Long commonMethodToInsertData(String query, Object pojo, String pK_ID) {
 		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
 		nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(pojo), keyHolder,new String[] {pK_ID});
		//log.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	public void checkPushNotification(EmployeeFinancialTransactionRequest financialTransactionRequest) {
		final List<EmployeeFinancialPushNotificationInfo> pushNotification = new ArrayList<EmployeeFinancialPushNotificationInfo>();
		EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo();
		financialPushNotification.setBookingFormId(financialTransactionRequest.getBookingFormId());
		financialPushNotification.setNotificationTitle(NOTIFICATION_TITLE);
		financialPushNotification.setNotificationBody(NOTIFICATION_BODY);
		financialPushNotification.setNotificationDescription("");
		financialPushNotification.setTypeMsg(NOTIFICATION_TYPE);
		financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1);
		financialPushNotification.setDemandNoteUrl("");
		financialPushNotification.setDemandNoteName("DN/001");
		financialPushNotification.setDemandNoteCreatedDate(new Timestamp(new Date().getTime()));
		financialPushNotification.setSiteId(financialTransactionRequest.getSiteId());
/*		financialPushNotification.setFlatSaleOwnerId(customerPropertyDetailsInfo.getFlatSaleOwnerId());
		financialPushNotification.setFlatSaleOwner(customerPropertyDetailsInfo.getFlatSaleOwner());*/
		pushNotification.add(financialPushNotification);
		
		for (EmployeeFinancialPushNotificationInfo pushNotificationInfo : pushNotification) {
			try {
				pushNotificationHelper.sendFinancialStatusNotification(pushNotificationInfo,null);
			} catch (InformationNotFoundException e) {
			 	e.printStackTrace();
			}
		}//pushNotification
		
	}
	
	public void getSiteAdderss(List<AddressInfo> list,	DemandNoteGeneratorInfo info, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) 
			throws Exception {
		Properties prop = responceCodesUtil.getApplicationProperties();
		/*if(customerPropertyDetailsInfo.getSiteId()!=null && customerPropertyDetailsInfo.getSiteId().equals(107l)
				&& !"1".equals(customerPropertyDetailsInfo.getFlatSaleOwnerId())
				) {
			String flatSaleOwner =customerPropertyDetailsInfo.getFlatSaleOwner().trim().replaceAll(" ", "");
			String siteAddress = prop.getProperty(flatSaleOwner+"_SITE_ADDRESS");
			String getSurveyNo = prop.getProperty(flatSaleOwner+"_SITE_SURVEY_NO");
			info.setSurveyNo(getSurveyNo==null?"N/A":getSurveyNo);
			info.setSiteAddress(siteAddress);
		} else*/ if(customerPropertyDetailsInfo.getFlatSaleOwner()!=null && customerPropertyDetailsInfo.getFlatSaleOwner().equalsIgnoreCase("Indimax")) {
			String flatSaleOwner =customerPropertyDetailsInfo.getFlatSaleOwner().trim().replaceAll(" ", "");
			String siteAddress = prop.getProperty(flatSaleOwner+"_SITE_ADDRESS");
			String getSurveyNo = prop.getProperty(flatSaleOwner+"_SITE_SURVEY_NO");
			info.setSurveyNo(getSurveyNo==null?"N/A":getSurveyNo);
			info.setSiteAddress(siteAddress);
		} else if(Util.isNotEmptyObject(list)) {
			/* setting site address and survey number */
			StringBuilder siteAddress = new StringBuilder();
			
			for(AddressInfo addressInfo : list){
				
				/*  setting survey number */
				if(Util.isNotEmptyObject(addressInfo.getSurveyNo())) {
					info.setSurveyNo(addressInfo.getSurveyNo());
				}else {
					info.setSurveyNo("-");
				}
				if(Util.isNotEmptyObject(addressInfo.getStreet())) {
					siteAddress.append(addressInfo.getStreet());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getArea())) {
					siteAddress.append(addressInfo.getArea());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getDistrict())) {
					siteAddress.append(addressInfo.getDistrict());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getCity())) {
					siteAddress.append(addressInfo.getCity());
					siteAddress.append("");
				}
				if(Util.isNotEmptyObject(addressInfo.getPincode())) {
					siteAddress.append("-");
					siteAddress.append(addressInfo.getPincode());
				}
			}
			info.setSiteAddress(siteAddress.toString());
		} else {
			info.setSiteAddress("-");
			log.info("Site Details not found."+customerPropertyDetailsInfo.getFlatNo());
			throw new EmployeeFinancialServiceException("Site Details not found.");
		}
	}

	public Map<String, String> getTheCommonInformation(Site site, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		Map<String,String> logoAndOtherDetails = new HashMap<>();
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String rightSidelogoFilePath="";
		String leftSidelogoFilePath="";

		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		/*bvr 1*/
		 String flatSaleOwner =null;
		 String flatSaleOwnerId ="";

		Properties prop = responceCodesUtil.getApplicationProperties();
		
		if(site.getSiteId()!=null && site.getSiteId().equals(131l)) {
			rightSidelogoForPdf = prop.getProperty("ASPIRE_LOGO1");
			rightSidelogoFilePath =  prop.getProperty("ASPIRE_LOGO1_PATH");
			thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("ASPIRE_GREETING_MSG_FROM");
			/*NOTIFICATION_TITLE = "Aspire Aurum demand Note";
			NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
			NOTIFICATION_TYPE = "Sumadhura Demand Note";
			NOTIFICATION_TYPE1 = "Sumadhura Demand Note";*/
		}else if(site.getSiteId()!=null && site.getSiteId().equals(139L)) {
			rightSidelogoForPdf = prop.getProperty("AMBER_LOGO1");
			rightSidelogoFilePath =  prop.getProperty("AMBER_LOGO1_PATH");
			thanksAndRegardsFrom = prop.getProperty("AMBER_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("AMBER_GREETING_MSG_FROM");
			/*NOTIFICATION_TITLE = "Aspire Aurum demand Note";
			NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
			NOTIFICATION_TYPE = "Sumadhura Demand Note";
			NOTIFICATION_TYPE1 = "Sumadhura Demand Note";*/
		} else {
			rightSidelogoForPdf = prop.getProperty("SUMADHURA_LOGO1");
			rightSidelogoFilePath =  prop.getProperty("SUMADHURA_LOGO1_PATH");
			thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");
			/*NOTIFICATION_TITLE = site.getName()+" Demand Note";
			NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
			NOTIFICATION_TYPE = "Sumadhura Demand Note";
			NOTIFICATION_TYPE1 = "Sumadhura Demand Note";*/
		}
		site.setSiteId(site.getSiteId()==null?0l:site.getSiteId());

		if (Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatSaleOwnerNameBasedOnAccountId())
				&& "approveFinancialMultipleTransaction".equals(customerPropertyDetailsInfo.getRequestFrom())) {

			flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwnerNameBasedOnAccountId().trim().replaceAll(" ",
					"");
		} else {
			flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner();
		}
		if (Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatSaleOwnerIdBasedOnAccountId())
				&& "approveFinancialMultipleTransaction".equals(customerPropertyDetailsInfo.getRequestFrom())) {

			flatSaleOwnerId = customerPropertyDetailsInfo.getFlatSaleOwnerIdBasedOnAccountId().toString();
		} else {
			flatSaleOwnerId = customerPropertyDetailsInfo.getFlatSaleOwnerId();
		}
		if(customerPropertyDetailsInfo!=null && flatSaleOwner!= null) {
			//String flatSaleOwner1 =customerPropertyDetailsInfo.getFlatSaleOwner().trim().replaceAll(" ", "");
			
			if(site.getSiteId()!=null && site.getSiteId().equals(107l)
					&& !"1".equals(flatSaleOwnerId)
					) {
				flatSaleOwner =flatSaleOwner.trim().replaceAll(" ", "");
				rightSidelogoForPdf = prop.getProperty(site.getSiteId()+"_"+flatSaleOwner+"_LOGO1");
				rightSidelogoFilePath =  prop.getProperty(site.getSiteId()+"_"+flatSaleOwner+"_LOGO1_PATH");
				thanksAndRegardsFrom = prop.getProperty(site.getSiteId()+"_"+flatSaleOwner+"_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty(site.getSiteId()+"_"+flatSaleOwner+"_GREETING_MSG_FROM");
				
				/*leftSidelogoFilePath =  prop.getProperty(site.getSiteId()+flatSaleOwner+"_LOGO1");
				rightSidelogoFilePath =  prop.getProperty(site.getSiteId()+flatSaleOwner+"_LOGO1_PATH");*/
				
			} else if(site.getSiteId().equals(134l) && flatSaleOwner.equalsIgnoreCase("Indimax")) {
				flatSaleOwner =flatSaleOwner.trim().replaceAll(" ", "");
				rightSidelogoForPdf = prop.getProperty(flatSaleOwner+"_LOGO1");
				rightSidelogoFilePath =  prop.getProperty(flatSaleOwner+"_LOGO1_PATH");
				thanksAndRegardsFrom = prop.getProperty(flatSaleOwner+"_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty(flatSaleOwner+"_GREETING_MSG_FROM");
				/*NOTIFICATION_TITLE = site.getName()+" Demand Note";
				NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
				NOTIFICATION_TYPE = "Sumadhura Demand Note";
				NOTIFICATION_TYPE1 = "Sumadhura Demand Note";*/
			} else if (site.getSiteId().equals(134l) && flatSaleOwner.equalsIgnoreCase("Sumadhura Vasavi LLP")) {
				flatSaleOwner =flatSaleOwner.trim().replaceAll(" ", "");
				leftSidelogoForPdf  = prop.getProperty("SUMADHURA_LOGO1");
				rightSidelogoForPdf = prop.getProperty(flatSaleOwner+"_LOGO1");
				
				leftSidelogoFilePath =  prop.getProperty("SUMADHURA_LOGO1_PATH");
				rightSidelogoFilePath =  prop.getProperty(flatSaleOwner+"_LOGO1_PATH");
				
				thanksAndRegardsFrom = prop.getProperty(flatSaleOwner+"_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty(flatSaleOwner+"_GREETING_MSG_FROM");
				/*NOTIFICATION_TITLE = site.getName()+" Demand Note";
				NOTIFICATION_BODY = "Dear Customer your demand note has been generated!.";
				NOTIFICATION_TYPE = "Sumadhura Demand Note";
				NOTIFICATION_TYPE1 = "Sumadhura Demand Note";*/
			}
		}
		
		logoAndOtherDetails.put("rightSidelogoForPdf", rightSidelogoForPdf);
		logoAndOtherDetails.put("leftSidelogoForPdf", leftSidelogoForPdf);
		
		logoAndOtherDetails.put("rightSidelogoFilePath", rightSidelogoFilePath);
		logoAndOtherDetails.put("leftSidelogoFilePath", leftSidelogoFilePath);
		
		logoAndOtherDetails.put("thanksAndRegardsFrom", thanksAndRegardsFrom);
		logoAndOtherDetails.put("greetingsFrom", greetingsFrom);
		
		/*logoAndOtherDetails.put("NOTIFICATION_TITLE", NOTIFICATION_TITLE);
		logoAndOtherDetails.put("NOTIFICATION_BODY", NOTIFICATION_BODY);
		logoAndOtherDetails.put("NOTIFICATION_TYPE", NOTIFICATION_TYPE);
		logoAndOtherDetails.put("NOTIFICATION_TYPE1", NOTIFICATION_TYPE1);*/
		return logoAndOtherDetails;
	}

	public List<FileInfo> demandNoteInterestExcelGenaratorHelper(List<EmployeeFinancialResponse> employeeFinancialResponseList,
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo, EmployeeFinancialServiceDao employeeFinancialServiceDao, BookingFormServiceDao bookingFormServiceDaoImpl) throws Exception {
		log.info("EmployeeFinancialHelper.demandNoteInterestExcelGenaratorHelper()");
		List<FileInfo> fileInfoList = new ArrayList<>();
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		
		double grandTotalAggrementCost = 0d;
		double grandTotalPaidAmount = 0d;
		double grandTotalPenaltyAmount = 0d;
		
		//Map<Long,List<FinancialCustomerDetails>> map = new HashMap<>();
		List<FinancialCustomerDetails> customerDetailsList = new ArrayList<FinancialCustomerDetails>();
		Long sno=1l;
		String siteName = "";
		String requestUrl = employeeFinancialServiceInfo.getRequestUrl();
		List<Map<String, Object>> paymentPaidList = null;
		//Customer wise details
		if(requestUrl.equals("CalculateCustomerInterestData") || requestUrl.equals("customerWiseInterestDetails") || requestUrl.equals("GiveInterestWaiverReport")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String saleDeedDate = null;
			Double sumOfInitiatedWaiverAmount = 0.0;
			Double sumOfLastApprovedWaiverAmount = 0.0;
			for (EmployeeFinancialResponse resp : employeeFinancialResponseList) {
				if (Util.isNotEmptyObject(resp.getFinancialProjectMileStoneResponse()) && Util.isNotEmptyObject(resp.getFlatsResponse())) {
					saleDeedDate = null;
					CustomerPropertyDetailsInfo flatInfo = resp.getFlatsResponse().get(0);
					if (siteName.isEmpty()) {
						siteName = resp.getFlatsResponse().get(0).getSiteName();
					}
					FinancialCustomerDetails customerDetails = new FinancialCustomerDetails();
					if (requestUrl.equals("GiveInterestWaiverReport")) {
						BookingFormRequest bookingFormRequestCopy = new BookingFormRequest();
						bookingFormRequestCopy.setCustomerId(flatInfo.getCustomerId());
						bookingFormRequestCopy.setFlatBookingId(flatInfo.getFlatBookingId());
						bookingFormRequestCopy.setStatusId(null);
						sumOfInitiatedWaiverAmount = employeeFinancialServiceDao.getInitiatedInterestWaiverDetails(flatInfo,requestUrl);
						
						//sumOfLastApprovedWaiverAmount = employeeFinancialServiceDao.getLastApprovedInterestWaiverDetails(flatInfo,requestUrl);
						
						List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequestCopy);
						if (Util.isNotEmptyObject(flatBookPojo)) {
							if (flatBookPojo.get(0).getSaleDeedDate() != null) {
								saleDeedDate = dateFormat.format(flatBookPojo.get(0).getSaleDeedDate());
							} else {
								saleDeedDate = "-";
							}
							customerDetails.setSaleDeedDate(saleDeedDate);
						}
					}
						
					double totalInterestAmount = resp.getTotalPenaltyAmount()==null?0d:resp.getTotalPenaltyAmount();
					double totalPenalityPaidAmount  =  resp.getTotalPenalityPaidAmount()==null?0d:resp.getTotalPenalityPaidAmount();
					//double totalPendingInterestAmount = resp.getTotalPendingPenaltyAmount()==null?0d:resp.getTotalPendingPenaltyAmount();
					double totalInterestWaiverAdjAmount =  resp.getTotalInterestWaiverAdjAmount()==null?0d:resp.getTotalInterestWaiverAdjAmount();
					
					double totalDelayedDays = resp.getTotalDaysDelayed() == null ? 0d : resp.getTotalDaysDelayed();
					double totalPaidAmount = resp.getTotalPaidAmount() == null ? 0d : resp.getTotalPaidAmount();
					double totalAgreementCost = flatInfo.getTotalAgreementCost() == null ? 0d : flatInfo.getTotalAgreementCost();
						
						/* Malladi Changes */
						double basicFlatCost = flatInfo.getBasicFlatCost()==null?0d:flatInfo.getBasicFlatCost();
						double amenitiesFlatCost = flatInfo.getAmenitiesFlatCost()==null?0d:flatInfo.getAmenitiesFlatCost();
						double totalFlatCostExclGst = flatInfo.getTotalFlatCostExclGst()==null?0d:flatInfo.getTotalFlatCostExclGst();
		
					if (totalDelayedDays == 0) {
						// continue;uncomment this
					}

					grandTotalAggrementCost += totalAgreementCost;
					grandTotalPaidAmount += totalPaidAmount;
					grandTotalPenaltyAmount += totalInterestAmount;
						
						customerDetails.setSno(sno);
						customerDetails.setSiteName(siteName);
						customerDetails.setCustomerName(flatInfo.getCustomerName());
						customerDetails.setFlatNo(flatInfo.getFlatNo());
						customerDetails.setBookingFormId(flatInfo.getFlatBookingId());
						customerDetails.setBookingDate(TimeUtil.timestampToDD_MM_YYYY(flatInfo.getBookingDate()));
						customerDetails.setTotalAgreementCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalAgreementCost).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalPaidAmount).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setTotalInterestAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalInterestAmount).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setTotalDelayedDays(String.valueOf(totalDelayedDays));
						
						customerDetails.setTotalInterestWaiverAdjAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalInterestWaiverAdjAmount).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setTotalPenalityPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalPenalityPaidAmount).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setSumOfInitiatedWaiverAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfInitiatedWaiverAmount).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						double balAmt = totalInterestAmount - totalInterestWaiverAdjAmount-totalPenalityPaidAmount-sumOfInitiatedWaiverAmount;
						if(balAmt<0) {
							customerDetails.setPenaltybalanceAmount("- "+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(balAmt).setScale(roundingModeSize, roundingMode).toString()).replaceAll("[^0-9.]", "").replaceAll("\u00A0", "").trim());
						} else {
							customerDetails.setPenaltybalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(balAmt).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						}
						//this is temporary code, for loading last approved waiver amount
						customerDetails.setLastInterestWaiverAdjAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfLastApprovedWaiverAmount).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						/* Malladi Changes */
						customerDetails.setBhk(flatInfo.getBhk()==null?"-":flatInfo.getBhk());
						customerDetails.setHandingOverDate(flatInfo.getHandingOverDate()==null?"-":TimeUtil.timestampToDD_MM_YYYY(flatInfo.getHandingOverDate()));
						customerDetails.setAgreementDate(flatInfo.getAgreementDate()==null?"-":TimeUtil.timestampToDD_MM_YYYY(flatInfo.getAgreementDate()));
						customerDetails.setRegistrationDate(flatInfo.getRegistrationDate()==null?"-":TimeUtil.timestampToDD_MM_YYYY(flatInfo.getRegistrationDate()));
						customerDetails.setBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(basicFlatCost).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setAmenitiesFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesFlatCost).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						customerDetails.setTotalFlatCostExclGst(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExclGst).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
						
						if(Util.isNotEmptyObject(resp.getPaymentPaidList())) {//other than principal amount and penalty amount
							Map<String, Object> paymentPaid = resp.getPaymentPaidList().get(0);
							if(paymentPaid.get("corpusPayAmount")!=null){
								customerDetails.setCorpusPayAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("corpusPayAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							}else {
								customerDetails.setCorpusPayAmount("0.00");
							}
							if(paymentPaid.get("corpusPaidAmount")!=null){
								customerDetails.setCorpusPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("corpusPaidAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setCorpusPaidAmount("0.00");
							}
							if(paymentPaid.get("modificationPayAmount")!=null){
								customerDetails.setModificationPayAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("modificationPayAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setModificationPayAmount("0.00");
							}
							if(paymentPaid.get("modificationPaidAmount")!=null){
								customerDetails.setModificationPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("modificationPaidAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setModificationPaidAmount("0.00");
							}
							if(paymentPaid.get("legalPayAmount")!=null){
								customerDetails.setLegalPayAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("legalPayAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setLegalPayAmount("0.00");
							}
							if(paymentPaid.get("legalPaidAmount")!=null){
								customerDetails.setLegalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("legalPaidAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setLegalPaidAmount("0.00");
							}
							if(paymentPaid.get("flatKhataPayAmount")!=null){
								customerDetails.setFlatKhataPayAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("flatKhataPayAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setFlatKhataPayAmount("0.00");
							}
							if(paymentPaid.get("flatKhataPaidAmount")!=null){
								customerDetails.setFlatKhataPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("flatKhataPaidAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setFlatKhataPaidAmount("0.00");
							}
							if(paymentPaid.get("maintenanceChargePayAmount")!=null){
								customerDetails.setMaintenanceChargePayAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("maintenanceChargePayAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setMaintenanceChargePayAmount("0.00");
							}
							if(paymentPaid.get("maintenanceChargePaidAmount")!=null){
								customerDetails.setMaintenanceChargePaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(paymentPaid.get("maintenanceChargePaidAmount").toString())).setScale(roundingModeSize, roundingMode).toString()).replaceAll("\u00A0", "").trim());
							} else {
								customerDetails.setMaintenanceChargePaidAmount("0.00");
							}
						}
						//customerDetails.setPaymentPaidList(resp.getPaymentPaidList());
						resp.setPaymentPaidList(null);
						
						log.info(customerDetails);
						customerDetailsList.add(customerDetails);
						sno++;
				}
			}
			
			System.out.println();
			log.info(customerDetailsList);
			if(Util.isEmptyObject(customerDetailsList)) {
				return fileInfoList;
			}
		}else {//milestoneWiseInterestDetails
			//milestone wise details
			for(EmployeeFinancialResponse resp : employeeFinancialResponseList ) {
				if(Util.isNotEmptyObject(resp.getFinancialProjectMileStoneResponse()) && Util.isNotEmptyObject(resp.getFlatsResponse())) {
					for(FinancialProjectMileStoneResponse response : resp.getFinancialProjectMileStoneResponse()) {
						if(siteName.isEmpty()) {
							siteName  = resp.getFlatsResponse().get(0).getSiteName();	
						}
						FinancialCustomerDetails customerDetails = new FinancialCustomerDetails();	
						CustomerPropertyDetailsInfo flatInfo = resp.getFlatsResponse().get(0);
						double totalInterestAmount = response.getTotalPendingPenaltyAmount()==null?0d:response.getTotalPendingPenaltyAmount();
						double totalDelayedDays = response.getDaysDelayed()==null?0d:response.getDaysDelayed();
						double totalPayAmount = response.getPayAmount()==null?0d:response.getPayAmount();
						double totalPaidAmount = response.getPaidAmount()==null?0d:response.getPaidAmount();
						//double totalAgreementCost = flatInfo.getTotalAgreementCost()==null?0d:flatInfo.getTotalAgreementCost();
						
						customerDetails.setSno(sno);
						customerDetails.setCustomerName(flatInfo.getCustomerName());
						customerDetails.setFlatNo(flatInfo.getFlatNo());
						customerDetails.setMilestoneName(response.getMilestoneName());
						customerDetails.setSiteName(siteName);
						customerDetails.setMileStoneNo(response.getMileStoneNo());
						customerDetails.setBookingDate(TimeUtil.timestampToDD_MM_YYYY(flatInfo.getBookingDate()));
						if(flatInfo.getAgreementDate()!=null) 
							customerDetails.setAgreementDate(TimeUtil.timestampToDD_MM_YYYY(flatInfo.getAgreementDate()));
						else
							customerDetails.setAgreementDate("-");
						
						//customerDetails.setTotalAgreementCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalAgreementCost).setScale(roundingModeSize, roundingMode).toString()));
						customerDetails.setTotalPayAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalPayAmount).setScale(roundingModeSize, roundingMode).toString()));
						customerDetails.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalPaidAmount).setScale(roundingModeSize, roundingMode).toString()));
						customerDetails.setTotalInterestAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalInterestAmount).setScale(roundingModeSize, roundingMode).toString()));
						customerDetails.setTotalDelayedDays(String.valueOf(totalDelayedDays));
						
						log.info(customerDetails);
						customerDetailsList.add(customerDetails);
						sno++;
					}
				}
			}
	}

		DemandNoteGeneratorInfo demandNoteGeneratorInfo = new DemandNoteGeneratorInfo();
		demandNoteGeneratorInfo.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalAggrementCost).setScale(roundingModeSize, roundingMode).toString()));
		demandNoteGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalPaidAmount).setScale(roundingModeSize, roundingMode).toString()));
		demandNoteGeneratorInfo.setTotalPendingPenaltyAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalPenaltyAmount).setScale(roundingModeSize, roundingMode).toString()));
		
		demandNoteGeneratorInfo.setBookingFormId(107l);
		demandNoteGeneratorInfo.setSiteId(employeeFinancialServiceInfo.getSiteId().toString());
		demandNoteGeneratorInfo.setCustomerDetailsList(customerDetailsList);
		demandNoteGeneratorInfo.setDemandNotePdfFileName(siteName+" - Customer Financial Details.pdf");
		/* generating pdf */
		List<DemandNoteGeneratorInfo> demandNoteGeneratorInfoList = new ArrayList<>();
		demandNoteGeneratorInfoList.add(demandNoteGeneratorInfo);
		
		email.setDemandNoteGeneratorInfoList(demandNoteGeneratorInfoList);
		email.setDemandNoteGeneratorInfo(demandNoteGeneratorInfo);
		
		try {
			fileInfo = pdfHelper.XMLExcelWorkerHelperForInterestAmt(email, "", employeeFinancialServiceInfo);
			fileInfoList.add(fileInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(employeeFinancialServiceInfo.getRequestUrl().equals("CalculateCustomerInterestData")) {
			try {
				demandNoteGeneratorInfo.setDemandNotePdfFileName(siteName+" - Customer Financial Details.xls");
				//fileInfo = csvGeneratorHelper.CSVWorkerHelper(email, "", employeeFinancialServiceInfo);
				//fileInfoList.add(fileInfo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {//CalculateCustomerInterestBreakUpData
			//same method for interest breakup
			try {
				demandNoteGeneratorInfo.setDemandNotePdfFileName(siteName+" - Interest data.xls");
				//fileInfo = csvGeneratorHelper.InterestBreakUpWorkerHelper(email, "", employeeFinancialServiceInfo,employeeFinancialResponseList);
				//fileInfoList.add(fileInfo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		/* Malladi Changes */
		/* This is for Get Interest Waiver report Details service to pass all the values to front end */
		if(requestUrl.equals("GiveInterestWaiverReport")) {
			employeeFinancialServiceInfo.setDemandNoteGeneratorInfoList(demandNoteGeneratorInfoList);
		}
		
		return fileInfoList;
	}
	
	public String getTheAmountInWords(Double amountToConvertInWords) {
	 	String amount = BigDecimal.valueOf(amountToConvertInWords).setScale(roundingModeSize, roundingMode).toString();
		//System.out.println(amount+"\t first " +amountToConvertInWords);
		String[] splitedAmount = amount.split("\\.");
		StringBuffer amountInWords = new StringBuffer(new NumberToWord().convertNumberToWords(new BigDecimal(splitedAmount[0]).setScale(roundingModeSize, roundingMode).longValue()));
		if(splitedAmount[0].equals("0")) {
			amountInWords = new StringBuffer("Zero");
		}
		StringBuffer amountInWordsPaisa = new StringBuffer("");
		if(splitedAmount.length>1 && Double.valueOf(splitedAmount[1])!=0) {
			amountInWordsPaisa = new StringBuffer(" Rupees And ").append(new NumberToWord().convertNumberToWords(new BigDecimal(splitedAmount[1]).setScale(roundingModeSize, roundingMode).longValue()) +" Paisa Only");
		}else {
			amountInWords.append(" Rupees Only");
		}
		if(amountToConvertInWords!=null && amountToConvertInWords == 0) {
			amountInWords = new StringBuffer("Zero Rupees Only");
		}
		return amountInWords.append(amountInWordsPaisa).toString();
	}
	
	private String getTheAmountWithCommas(Double amountToConvertInWords) {
		return currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amountToConvertInWords).setScale(roundingModeSize, roundingMode).toString());
	}
	
	private Map<String,String> isFilePathFoundPdf = new HashMap<>();

	public void receiptGenaratorHelper(EmployeeFinancialTransactionResponse resp, FileInfo fileInfo,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
		log.info(" ***** control is inside the receiptGenaratorHelper in EmployeeFinancialHelper ***** transactionServiceInfo.isThisUplaodedData() "+transactionServiceInfo.isThisUplaodedData());
		boolean isUploadedData = transactionServiceInfo.isThisUplaodedData();
		List<MileStoneInfo> mileStoneInfoList = new ArrayList<>();
		CustomerPropertyDetailsInfo	customerPropertyDetailsInfo = null;
		Email email = new Email();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String thanksAndRegardsFrom="";
		String rightSidelogoFilePath = "";
		String leftSidelogoFilePath = "";

		/*if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
			rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_LOGO1");	
			thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
		}else {
			rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1");
			thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
		}*/
		
		Site site = new Site();
		site.setSiteId(transactionServiceInfo.getSiteId());
		site.setName(transactionServiceInfo.getSiteName());		

		/*bvr 1*/
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 =transactionServiceInfo.getCustomerPropertyDetailsInfo();
		customerPropertyDetailsInfo2.setFlatSaleOwnerIdBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerIdBasedOnAccountId());
		customerPropertyDetailsInfo2.setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerNameBasedOnAccountId());
		customerPropertyDetailsInfo2.setRequestFrom("approveFinancialMultipleTransaction");
		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo2);			
		
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		rightSidelogoFilePath = logoAndOtherDetails.get("rightSidelogoFilePath");
		leftSidelogoFilePath = logoAndOtherDetails.get("leftSidelogoFilePath");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		//greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
		
		if(Util.isNotEmptyObject(rightSidelogoFilePath)) {//checking file is present in local folder, if exists loading file from local folder
			if(isFilePathFoundPdf.get(rightSidelogoFilePath)!=null && isFilePathFoundPdf.get(rightSidelogoFilePath).equals("true")) {
				info.setRightSidelogoFilePath(rightSidelogoFilePath);
			} else {
				File file = new File(rightSidelogoFilePath);
				if(file.exists()) {//checking file is exists or not, if exist the loading this file in pdf
					isFilePathFoundPdf.put(rightSidelogoFilePath, "true");
					info.setRightSidelogoFilePath(rightSidelogoFilePath);
				}
			}
		}
		
		if(Util.isNotEmptyObject(leftSidelogoFilePath)) {
			if(isFilePathFoundPdf.get(leftSidelogoFilePath)!=null && isFilePathFoundPdf.get(leftSidelogoFilePath).equals("true")) {
				info.setLeftSidelogoFilePath(leftSidelogoFilePath);
			} else {
				File file = new File(leftSidelogoFilePath);
				if(file.exists()) {
					isFilePathFoundPdf.put(leftSidelogoFilePath, "true");
					info.setLeftSidelogoFilePath(leftSidelogoFilePath);
				}
			}
		}
		
		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		
		if(isUploadedData) {
			/* setting demandNote Generated Date */
			if(Util.isNotEmptyObject(resp.getTransactionReceiveDate())) {
				info.setTransactionReceiptDate(TimeUtil.timestampToDD_MM_YYYY(resp.getTransactionReceiveDate()));
			}else {
				info.setTransactionReceiptDate("N/A");
				log.info("Transaction Receive date not found"+transactionServiceInfo.getBookingFormId());
				throw new EmployeeFinancialServiceException("Transaction Receive date not found");
			}
		} else {
			info.setTransactionReceiptDate(TimeUtil.timestampToDD_MM_YYYY(new Timestamp(new Date().getTime())));
		}
		
		/* setting demandNote Generated Date */
		if(Util.isNotEmptyObject(resp.getTransactionReceiveDate())) {
			info.setTransactionReceiveDate(TimeUtil.timestampToDD_MM_YYYY(resp.getTransactionReceiveDate()));
		}else {
			info.setTransactionReceiveDate("N/A");
			log.info("Transaction Receive date not found"+transactionServiceInfo.getBookingFormId());
			throw new EmployeeFinancialServiceException("Transaction Receive date not found");
		}
		
		if (Util.isNotEmptyObject(resp.getTransactionReceiptNo())) {
			info.setTransactionReceiptNo(resp.getTransactionReceiptNo());
		} else {
			info.setTransactionReceiptNo("N/A");
			log.info("Transaction receipt date not found"+transactionServiceInfo.getBookingFormId());
			throw new EmployeeFinancialServiceException("Transaction receipt number not found");
		}
		
		if (Util.isNotEmptyObject(resp.getBankName())) {
			info.setBankName(resp.getBankName());
			if(resp.getBankName().equals("N/A")) {
				info.setBankName("-");	
			}
		} else {
			info.setBankName("-");
		}

		if (Util.isNotEmptyObject(resp.getTransactionDate())) {
			info.setTransactionDate(TimeUtil.timestampToDD_MM_YYYY(resp.getTransactionDate()));
		} else {
			info.setTransactionDate("N/A");
		}
		
		if (Util.isNotEmptyObject(resp.getBookingFormId())) {
			info.setBookingFormId(resp.getBookingFormId());
		}
		
		if (Util.isNotEmptyObject(resp.getIsShowGstInPDF())) {
			if (resp.getIsShowGstInPDF() == true) {
				info.setIsShowGstInPDF(Boolean.TRUE);
			} else {
				info.setIsShowGstInPDF(Boolean.FALSE);
			}
		} else {
			info.setIsShowGstInPDF(Boolean.FALSE);
		}
		//isShowGstInPDF = info.getIsShowGstInPDF();
		if (Util.isNotEmptyObject(resp.getTransactionEntryId())) {
			info.setTransactionEntryId(resp.getTransactionEntryId());
		}
		
		if(Util.isNotEmptyObject(resp.getTotalCgstAmount())) { 
			info.setTotalCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
			/*if(!isShowGstInPDF) {
				 info.setTotalCgstAmount("0.00"); 
			}*/
		}else { info.setTotalCgstAmount("0.00"); }

		if(Util.isNotEmptyObject(resp.getTotalSgstAmount())) { 
			info.setTotalSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
			/*if(!isShowGstInPDF) {
				 info.setTotalSgstAmount("0.00"); 
			}*/
		}else { info.setTotalSgstAmount("0.00"); }
		
		if(Util.isNotEmptyObject(resp.getTransactionModeName())) {
			info.setTransactionModeName(resp.getTransactionModeName());
			if(resp.getTransactionModeName().equals(MetadataId.CHEQUE.getName())) {
				if(Util.isNotEmptyObject(resp.getChequeNumber())) {
					info.setChequeNumber(resp.getChequeNumber());
				} else {
					log.info("Transaction Cheque number found empty"+transactionServiceInfo.getBookingFormId());
					throw new EmployeeFinancialServiceException("Transaction Cheque number found empty");
				}
			}else if(resp.getTransactionModeName().equals(MetadataId.ONLINE.getName())) {
				if(Util.isNotEmptyObject(resp.getReferenceNo())) {
					info.setReferenceNo(resp.getReferenceNo());
				} else {
					log.info("Transaction online reference number found empty"+transactionServiceInfo.getBookingFormId());
					throw new EmployeeFinancialServiceException("Transaction online reference number found empty");

				}
				if(Util.isNotEmptyObject(resp.getTransferModeName())) {
					info.setTransferModeName(resp.getTransferModeName());
				} else {
					log.info("Transaction amount transfer mode found empty"+transactionServiceInfo.getBookingFormId());
					throw new EmployeeFinancialServiceException("Transaction amount transfer mode found empty");
				}
			}
		}		
		
//		if(Util.isNotEmptyObject(resp.getTotalReceiptAmount())) { info.setTotalCgstAmount(resp.getTotalReceiptAmount());
//		}else { info.setTotalReceiptAmount(0.0); }
		/* setting Company Billing Address and other details */
		

		/*bvr 1*/
		info.setFlatSaleOwnerIdBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerIdBasedOnAccountId());
		info.setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerNameBasedOnAccountId());
		info.setRequestFrom("approveFinancialMultipleTransaction");
		setOfficeDetails(resp,info);
				
		/* setting applicant and coApplicant names and mobile numbers */
		if(Util.isNotEmptyObject(resp.getFlatsResponse())) {
			StringBuilder name = new StringBuilder(); 
			StringBuilder mobileNos = new StringBuilder();
			StringBuilder flatNo = new StringBuilder();
			StringBuilder floorName = new StringBuilder();
			StringBuilder blockName = new StringBuilder();
			StringBuilder siteName = new StringBuilder();
			StringBuilder pancard = new StringBuilder();
			StringBuilder finSchemeId = new StringBuilder();
			StringBuilder finSchemeName = new StringBuilder();
			StringBuilder finSchemeType = new StringBuilder();

			List<CustomerPropertyDetailsInfo> detailsList  = resp.getFlatsResponse();

			for (int index = 0; index <  detailsList.size(); index++) {
				customerPropertyDetailsInfo = detailsList.get(index);
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName()) || Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())) {
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName())){	
					name.append(customerPropertyDetailsInfo.getCustomerName());
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())){
						name.append("& &nbsp;");
						name.append(customerPropertyDetailsInfo.getCoAppFullName());
					}
				}else {
					name.append("N/A");
				}
				}else {
					name.append("N/A");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber()) ||  Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())) {
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
						mobileNos.append(customerPropertyDetailsInfo.getContactNumber());
					}
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())){
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
							mobileNos.append(",");
						}
						mobileNos.append(customerPropertyDetailsInfo.getAlternatePhoneNo());
					}
				}else {
					mobileNos.append("N/A");
				}
				
				/* setting flat Number */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
					flatNo.append(customerPropertyDetailsInfo.getFlatNo());
				}else {
					flatNo.append("N/A");
				}
				
				/* setting Floor Name   */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFloorName())) {
					floorName.append(customerPropertyDetailsInfo.getFloorName());
				}else {
					floorName.append("N/A");
				}
				
				/* setting Block name */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBlockName())) {
					blockName.append(customerPropertyDetailsInfo.getBlockName());
				}else {
					blockName.append("N/A");
				}
				
				/* setting Site Name */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteName())) {
					siteName.append(customerPropertyDetailsInfo.getSiteName());
				}else {
					siteName.append("N/A");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeName())) {
					finSchemeName.append(customerPropertyDetailsInfo.getFinSchemeName());
				}else {
					finSchemeName.append("N/A");
					log.info("Failed to generate demand note, Scheme not found for Flat No "+info.getFlatName());
					//throw new EmployeeFinancialServiceException("Failed to generate demand note, Scheme not found for Flat No "+info.getFlatName());
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeType())) {
					finSchemeType.append(customerPropertyDetailsInfo.getFinSchemeType());
				}else {
					finSchemeType.append("N/A");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeId())) {
					finSchemeId.append(customerPropertyDetailsInfo.getFinSchemeId());
				}else {
					finSchemeId.append("N/A");
					log.info("Failed to generate demand note, Scheme not found for Flat No "+info.getFlatName());
					//throw new EmployeeFinancialServiceException("Failed to generate demand note, Scheme not found for Flat No "+info.getFlatName());
				}
				
				/* setting customer pancard  */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getPancard())) {
					pancard.append(customerPropertyDetailsInfo.getPancard());
				}else {
					pancard.append("N/A");
				}
				
				/* setting siteId */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteId())) {
					info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
				}else {
					info.setSiteId("Anonymous_Site");
				}
				
				/* setting flatBookingId */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatBookingId())) {
					info.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId().toString());
					info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
				}else {
					info.setFlatBookingId("Anonymous_Flat_Booking");
				}
				
			}
			info.setCustomerNames(name.toString());
			info.setMobileNumbers(mobileNos.toString());
			info.setFlatName(flatNo.toString());
			info.setFloorName(floorName.toString());
			info.setBlockName(blockName.toString());
			//info.setSiteName(siteName.toString());
			info.setFinSchemeName(finSchemeName.toString());
			info.setFinSchemeId(finSchemeId.toString());
			info.setFinSchemeType(finSchemeType.toString());
			if(info.getSiteId()!=null && info.getSiteId().equals("131")) {
				info.setSiteName("Aspire Aurum");
			}else {
				info.setSiteName(siteName.toString());
			}
			info.setPancard(pancard.toString());
		}
		
		if(Util.isNotEmptyObject(resp.getFlatsResponse()) && resp.getFlatsResponse().get(0).getSiteId().equals(107l)
				//&& !"1".equals(resp.getFlatsResponse().get(0).getFlatSaleOwnerId())
				&& Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatSaleOwnerIdBasedOnAccountId().toString())
				&& !"1".equals(customerPropertyDetailsInfo.getFlatSaleOwnerIdBasedOnAccountId().toString())
				) {
			//please refer FLATS_SALE_OWNERS table for "1", "1" is sale owner of the current project company and other are landlord flats
			info.setSiteName("Eden Garden");
			info.setThanksAndRegardsFrom("Thank You From EG");
			info.setReceivedAmountBy("I ");
		} else {
			info.setReceivedAmountBy("We ");//this code is for receipt pdf
		}
		
		/* setting applicant permenent address  */
		if(Util.isNotEmptyObject(resp.getCustomerAddressInfoList())) {
			for(AddressInfo addressInfo : resp.getCustomerAddressInfoList()){
			/* Taking correspondence Address  */	
             if(Util.isNotEmptyObject(addressInfo)) {
            	 if(addressInfo.getAddressMappingType().getAddressType().equalsIgnoreCase("Correspondence")){
            		 if(Util.isNotEmptyObject(addressInfo.getAddress1())) {
            		    info.setAddress(addressInfo.getAddress1());
            		    break;
            		    /*if(Util.isNotEmptyObject(addressInfo.getPincode())) {
            		    	 info.setAddress(info.getAddress()+"-"+addressInfo.getPincode());
            		    	 break;
            		    }*/
            		 }else {
            			  info.setAddress("N/A"); 
            		 }
            	 }else {
            		 info.setAddress("N/A");  
            	 }
             }
		  }
		}else {
		   info.setAddress("N/A");
		}
		getSiteAdderss(resp.getSiteAddressInfoList(),info,customerPropertyDetailsInfo);
		/* setting site address and survey number */
		/*if(Util.isNotEmptyObject(resp.getSiteAddressInfoList())) {
			StringBuilder siteAddress = new StringBuilder();
			for(AddressInfo addressInfo : resp.getSiteAddressInfoList()){
				
				  setting survey number 
				if(Util.isNotEmptyObject(addressInfo.getSurveyNo())) {
					info.setSurveyNo(addressInfo.getSurveyNo());
				}else {
					info.setSurveyNo("N/A");
				}
				if(Util.isNotEmptyObject(addressInfo.getStreet())) {
					siteAddress.append(addressInfo.getStreet());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getArea())) {
					siteAddress.append(addressInfo.getArea());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getDistrict())) {
					siteAddress.append(addressInfo.getDistrict());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getCity())) {
					siteAddress.append(addressInfo.getCity());
					siteAddress.append("");
				}
				if(Util.isNotEmptyObject(addressInfo.getPincode())) {
					siteAddress.append("-");
					siteAddress.append(addressInfo.getPincode());
				}
			}
			info.setSiteAddress(siteAddress.toString());
		}*/
		
		/* setting milestone amount.*/
		/*  setting milestone amount in words */
		if(Util.isNotEmptyObject(resp.getTotalReceiptAmount())) {
			String amountInWords = getTheAmountInWords(resp.getTotalReceiptAmount());
			info.setTotalReceiptAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalReceiptAmount()).setScale(roundingModeSize, roundingMode).toString()));
			info.setMileStoneAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalReceiptAmount()).setScale(roundingModeSize, roundingMode).toString()));
			info.setMileStoneAmountInWords(amountInWords);
		} else {
			info.setMileStoneAmount("0.00");
			info.setMileStoneAmountInWords("Zero Rupees Only ");
		}
		
		if (Util.isNotEmptyObject(resp.getTotalReceiptPaidAmount())) {
			info.setTotalReceiptPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalReceiptPaidAmount()).setScale(roundingModeSize, roundingMode).toString().trim()));
			String amountInWords = getTheAmountInWords(resp.getTotalReceiptPaidAmount());
			info.setTotalReceiptPaidAmountInWords(amountInWords);
		} else {
			info.setTotalReceiptPaidAmount("0.00");
			info.setTotalReceiptPaidAmountInWords("Zero Rupees Only ");
		}

		/* setting Demand Note PDF File Name */
		if (Util.isNotEmptyObject(resp.getTransactionPdfFileName())) {
			info.setDemandNotePdfFileName(resp.getTransactionPdfFileName());
		} else {
			info.setDemandNotePdfFileName("receiptForPaymentDone.pdf");
		}
	 
		/* setting site Account  */
     	if(Util.isNotEmptyObject(resp.getFinProjectAccountResponseList())) {/*
     		StringBuilder accountInfo = new StringBuilder();
     		for(FinProjectAccountResponse accountResponse : resp.getFinProjectAccountResponseList()) {
     			 setting bank details  
     			if(Util.isNotEmptyObject(accountResponse.getBankName())){
     				accountInfo.append(accountResponse.getBankName());
     				//accountInfo.append(",");
     			}
     			if(Util.isNotEmptyObject(accountResponse.getAccountHolderName())){
         				info.setAccountHolderName(accountResponse.getAccountHolderName());
         			}
     			 setting bank account details  
//     			if(Util.isNotEmptyObject(accountResponse.getAccountNo())){
//     				accountInfo.append("<strong>"+accountResponse.getAccountNo()+"</strong>");
//     				accountInfo.append(",");
//     			}
     			
     			 setting Account Address 
//     			if(Util.isNotEmptyObject(accountResponse.getAccountAddress())){
//     				accountInfo.append(accountResponse.getAccountAddress());
//     				accountInfo.append(",");
//     			}
     			
     			 setting Account IFSC Code 
//     			if(Util.isNotEmptyObject(accountResponse.getIfscCode())){
//     				accountInfo.append(" IFSC CODE - ");
//     				accountInfo.append(accountResponse.getIfscCode());
//     			}
     	    }
     		info.setSiteAccount(accountInfo.toString());
     	*/}
     	Long serialNumber = 1l;
     	for(FinancialProjectMileStoneResponse response : resp.getFinancialProjectMileStoneResponse()) {
     		MileStoneInfo mileStoneInfo = new MileStoneInfo();
     		
     		if(Util.isEmptyObject(response.getMileStoneNo())) {
     			//continue;
     		}
     		/* setting Milestone Number  */
     		if(Util.isNotEmptyObject(serialNumber)) {
     			mileStoneInfo.setProjectMilestoneId(serialNumber.toString());
     		}else {
     			mileStoneInfo.setProjectMilestoneId("N/A");
     		}
     		serialNumber++;
     		/* setting Milestone Name */
     		if(Util.isNotEmptyObject(response.getMilestoneName())) {
     			mileStoneInfo.setMilestoneName(response.getMilestoneName());
     		}else {
     			mileStoneInfo.setMilestoneName("N/A");
     		}
     		/* setting Due percentage  */
     		if(Util.isNotEmptyObject(response.getMileStonePercentage())) {
     			mileStoneInfo.setDue(response.getMileStonePercentage().toString());
     		}else {
     			mileStoneInfo.setDue("N/A");
     		}
     		/* setting DemandNote Date */
     		if(Util.isNotEmptyObject(response.getMilestoneDate())) {
     			mileStoneInfo.setDemandNoteDate(TimeUtil.timestampToDD_MM_YYYY(response.getMilestoneDate()));
     		}else {
     			mileStoneInfo.setDemandNoteDate("");
     		}
     		/* setting DemandNote Due Date */
     		if(Util.isNotEmptyObject(response.getMileStoneDueDate())) {
     			mileStoneInfo.setDemandNoteDueDate(TimeUtil.timestampToDD_MM_YYYY(response.getMileStoneDueDate()));
     		}else {
     			mileStoneInfo.setDemandNoteDueDate("");
     		}
     		/* setting Total flat amount including GST  */
     		if(Util.isNotEmptyObject(response.getMileStoneTotalAmount())) {
     			//mileStoneInfo.setTotalFlatCostIncludeGST(response.getMileStoneTotalAmount().toString());
     			mileStoneInfo.setTotalFlatCostIncludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getMileStoneTotalAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setTotalFlatCostIncludeGST("0.00");
     		}
     		/* setting Total flat amount Recived include GST   */
     		if(Util.isNotEmptyObject(response.getPaidAmount())) {
     			mileStoneInfo.setAmountRecivedIncludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setAmountRecivedIncludeGST("0.00");
     		}
     		/* setting Due amount excluding GST */
     		if(Util.isNotEmptyObject(response.getPaidAmount())) {
     			//if(Util.isNotEmptyObject(response.getPaidAmount()) && Util.isNotEmptyObject(response.getGstAmount())) {	
     			mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getPaidAmount()-(response.getGstAmount()==null?0d:response.getGstAmount())).setScale(roundingModeSize, roundingMode).toString()));
     			if(info.getFinSchemeType()!=null && info.getFinSchemeType().equals("Special Offer")) {
     				mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));	
     			}
     			/*if(!isShowGstInPDF) {
     				mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));	
     			}*/
     		}else {
     			mileStoneInfo.setDueAmountExcludeGST("0.00");
     		}
     		
   			/* setting CGST */
     		if(Util.isNotEmptyObject(response.getCgstAmount())) {
     			//mileStoneInfo.setCGST(response.getCgstAmount().toString());
     			mileStoneInfo.setCGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setCGST("0.00");
     		}
     		/* setting SGST */
     		if(Util.isNotEmptyObject(response.getSgstAmount())) {
     			//mileStoneInfo.setSGST(response.getSgstAmount().toString());
     			mileStoneInfo.setSGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setSGST("0.00");
     		}
     		
     		if(Util.isNotEmptyObject(response.getGstAmount())) {
     			mileStoneInfo.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getGstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setGstAmount("0.00");
     		}
     		
     		/* setting Total Due Amount */
     		/*if(Util.isNotEmptyObject(response.getTotalDueAmount())) {
     			
     			mileStoneInfo.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getTotalDueAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setTotalDueAmount("0.00");
     		}*/
     		mileStoneInfo.setSAC("9954");
     		mileStoneInfoList.add(mileStoneInfo);
     	}
		info.setMileStones(mileStoneInfoList);
		
		if(mileStoneInfoList.isEmpty()) {
			log.info("Failed to generate receipt for Flat No "+info.getFlatName());
			throw new EmployeeFinancialServiceException("Failed to generate receipt for Flat No "+info.getFlatName());
		}
		
		if (Util.isNotEmptyObject(mileStoneInfoList)) {
			email.setFlag(true);
		} else {
			email.setFlag(false);
		}
		
		/* generating pdf */
		List<DemandNoteGeneratorInfo> demandNoteGeneratorInfo = new ArrayList<>();
		demandNoteGeneratorInfo.add(info);
		email.setDemandNoteGeneratorInfoList(demandNoteGeneratorInfo);
		
		pdfHelper.XMLWorkerHelperForReceipt(email,fileInfo,transactionServiceInfo, MetadataId.FIN_BOOKING_FORM_MILESTONES.getName());	
	}
	
	public FileInfo modificationChargesInvoiceGeneratorHelper(EmployeeFinancialTransactionResponse resp,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
		log.info(" ***** control is inside the modificationChargesInvoiceGeneratorHelper in EmployeeFinancialHelper ***** ");
		final List<EmployeeFinancialPushNotificationInfo> pushNotification = new ArrayList<EmployeeFinancialPushNotificationInfo>();
		FileInfo fileInfo = new FileInfo();
		FinBookingFormModiCostResponse modiCostResponse = resp.getFinBookingFormModiCostResponse();
		FinBookingFormAccountsResponse finBookingFormAccountsResponse = resp.getFinBookingFormAccountsResponse();

		if(Util.isNotEmptyObject(modiCostResponse) && Util.isNotEmptyObject(modiCostResponse.getFinBookingFormModiCostDtlsList()) && Util.isNotEmptyObject(finBookingFormAccountsResponse)) {
			CustomerPropertyDetailsInfo	customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
			DemandNoteGeneratorInfo info = new DemandNoteGeneratorInfo();
			Email email = new Email();
			email.setPortNumber(transactionServiceInfo.getPortNumber());
			String rightSidelogoForPdf = "";
			String leftSidelogoForPdf = "";
			String thanksAndRegardsFrom="";
			
			String NOTIFICATION_TITLE = transactionServiceInfo.getCustomerPropertyDetailsInfo().getSiteName()+" Modification Invoice";
			String NOTIFICATION_BODY = "Dear Customer your modification invoice has been generated!.";
			String NOTIFICATION_TYPE = "Sumadhura Modification Invoice";
			String NOTIFICATION_TYPE1 = "Sumadhura Modification Invoice";

			if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
				//rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_LOGO1");	
				//thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
				NOTIFICATION_TITLE = "Aspire Aurum Modification Invoice";
				NOTIFICATION_BODY = "Dear Customer your modification invoice has been generated!.";
				NOTIFICATION_TYPE = "Sumadhura Modification Invoice";
				NOTIFICATION_TYPE1 = "Sumadhura Modification Invoice";
			}else {
				//rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1");
				//thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			}
			
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());		

			//ACP Added
/*			transactionServiceInfo.getCustomerPropertyDetailsInfo().setFlatSaleOwnerIdBasedOnAccountId(Long.valueOf(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwnerId()));
			transactionServiceInfo.getCustomerPropertyDetailsInfo().setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwner());
*/
			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,transactionServiceInfo.getCustomerPropertyDetailsInfo());			
			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			//greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
			
			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			/* setting Invoice Details */
			info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
			if(Util.isNotEmptyObject(finBookingFormAccountsResponse.getFinBokAccInvoiceNo())) {
				info.setInvoiceNo(finBookingFormAccountsResponse.getFinBokAccInvoiceNo());
			}else {
				info.setInvoiceNo("N/A");
			}
			
			if(Util.isNotEmptyObject(modiCostResponse.getFinBookingFormModiCostId())) {
				info.setFolderId(modiCostResponse.getFinBookingFormModiCostId().toString());
			}else {
				info.setFolderId("Anonymous");
			}
			
			if(Util.isNotEmptyObject(modiCostResponse.getBasicAmount())) {
				info.setAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(modiCostResponse.getBasicAmount()).setScale(roundingModeSize, roundingMode).toString()));
			}else {
				info.setAmount("0.00");
			}
			
			if(Util.isNotEmptyObject(resp.getPercentageValue())) {
				info.setPercentageValue(BigDecimal.valueOf(resp.getPercentageValue()).setScale(roundingModeSize, roundingMode).toString());
				info.setCgstPercentageValue(BigDecimal.valueOf(resp.getPercentageValue()/2).setScale(roundingModeSize, roundingMode).toString());
				info.setSgstPercentageValue(BigDecimal.valueOf(resp.getPercentageValue()/2).setScale(roundingModeSize, roundingMode).toString());
			}else {
				info.setPercentageValue("0");
			}
			
			if(Util.isNotEmptyObject(modiCostResponse.getTaxAmount())) { 
				info.setTotalCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(modiCostResponse.getTaxAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
				info.setTotalSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(modiCostResponse.getTaxAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
				info.setTotalTaxAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(modiCostResponse.getTaxAmount()).setScale(roundingModeSize, roundingMode).toString()));
			}else { 
				info.setTotalCgstAmount("0.00"); 
				info.setTotalSgstAmount("0.00");
				info.setTotalTaxAmount("0.00");
			}
			
			if(Util.isNotEmptyObject(modiCostResponse.getTotalAmount())) {
				info.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(modiCostResponse.getTotalAmount()).setScale(roundingModeSize,roundingMode).toString()));
			}else {
				info.setTotalAmount("0.00");
			}
			
			if(Util.isNotEmptyObject(finBookingFormAccountsResponse.getPaidAmount())) {
				//info.setTotalExcessAmount(finBookingFormAccountsResponse.getPaidAmount().toString());
				info.setTotalExcessAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finBookingFormAccountsResponse.getPaidAmount()).setScale(roundingModeSize,roundingMode).toString()));
			}else {
				info.setTotalExcessAmount("0.00");
			}
			
			if(Util.isNotEmptyObject(modiCostResponse.getTotalAmount()) && Util.isNotEmptyObject(finBookingFormAccountsResponse.getPaidAmount())) {
				String setTotalPayableAmount = BigDecimal.valueOf(modiCostResponse.getTotalAmount()-finBookingFormAccountsResponse.getPaidAmount()).setScale(roundingModeSize,roundingMode).toString();
				info.setTotalPayableAmount(currencyUtil.convertUstoInFormat(setTotalPayableAmount));
				String amountInWords = getTheAmountInWords(Double.valueOf(setTotalPayableAmount));
				info.setTotalAmountInWords(amountInWords);
				//info.setTotalAmountInWords(new NumberToWord().convertNumberToWords(Double.valueOf(info.getTotalPayableAmount()).longValue()) +" Rupees Only.");
			}else {
				info.setTotalPayableAmount("0.00");
				info.setTotalAmountInWords("Zero Rupees Only.");
			}
			
			/* setting applicant and coApplicant names and mobile numbers */
			if(Util.isNotEmptyObject(resp.getFlatsResponse())) {
				StringBuilder name = new StringBuilder(); 
				StringBuilder mobileNos = new StringBuilder();
				StringBuilder flatNo = new StringBuilder();
				StringBuilder floorName = new StringBuilder();
				StringBuilder blockName = new StringBuilder();
				StringBuilder siteName = new StringBuilder();
				StringBuilder pancard = new StringBuilder();
				
				List<CustomerPropertyDetailsInfo> detailsList  = resp.getFlatsResponse();

				for (int index = 0; index <  detailsList.size(); index++) {
					customerPropertyDetailsInfo = detailsList.get(index);

					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName())) {
						name.append(customerPropertyDetailsInfo.getCustomerName());
					}else {
						name.append("N/A");
					}
					
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber()) ||  Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())) {
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
							mobileNos.append(customerPropertyDetailsInfo.getContactNumber());
						}
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())){
							if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
								mobileNos.append(",");
							}
							mobileNos.append(customerPropertyDetailsInfo.getAlternatePhoneNo());
						}
					}else {
						mobileNos.append("N/A");
					}
					
					/* setting flat Number */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
						flatNo.append(customerPropertyDetailsInfo.getFlatNo());
					}else {
						flatNo.append("N/A");
					}
					
					/* setting Floor Name   */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFloorName())) {
						floorName.append(customerPropertyDetailsInfo.getFloorName());
					}else {
						floorName.append("N/A");
					}
					
					/* setting Block name */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBlockName())) {
						blockName.append(customerPropertyDetailsInfo.getBlockName());
					}else {
						blockName.append("N/A");
					}
					
					/* setting Site Name */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteName())) {
						siteName.append(customerPropertyDetailsInfo.getSiteName());
					}else {
						siteName.append("N/A");
					}
					
					/* setting customer pancard  */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getPancard())) {
						pancard.append(customerPropertyDetailsInfo.getPancard());
					}else {
						pancard.append("N/A");
					}
					
					/* setting siteId */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteId())) {
						info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
					}else {
						info.setSiteId("Anonymous_Site");
					}
					
					/* setting flatBookingId */
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatBookingId())) {
						info.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId().toString());
						info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
					}else {
						info.setFlatBookingId("Anonymous_Flat_Booking");
					}
					
				}
				info.setCustomerNames(name.toString());
				info.setMobileNumbers(mobileNos.toString());
				info.setFlatName(flatNo.toString());
				info.setFloorName(floorName.toString());
				info.setBlockName(blockName.toString());
				//info.setSiteName(siteName.toString());
				if(info.getSiteId()!=null && info.getSiteId().equals("131")) {
					info.setSiteName("Aspire Aurum");
				}else {
					info.setSiteName(siteName.toString());
				}
				info.setPancard(pancard.toString());
			}
			
			/* setting site Account  */
         	if(Util.isNotEmptyObject(resp.getFinProjectAccountResponseList())) {
         		StringBuilder accountInfo = new StringBuilder();
         		for(FinProjectAccountResponse accountResponse : resp.getFinProjectAccountResponseList()) {
         			/* setting bank details  */
         			if(Util.isNotEmptyObject(accountResponse.getBankName())){
         				accountInfo.append(accountResponse.getBankName());
         				accountInfo.append(",");
         			}
         			if(Util.isNotEmptyObject(accountResponse.getAccountHolderName())){
         				info.setAccountHolderName(accountResponse.getAccountHolderName());
         			} else {
         				throw new EmployeeFinancialServiceException("Account holder name missing..!");
         			}
         			/* setting bank account details  */
         			if(Util.isNotEmptyObject(accountResponse.getSiteBankAccountNumber())){
         				accountInfo.append("<strong> A/C. "+accountResponse.getSiteBankAccountNumber()+"</strong>");
         				accountInfo.append(",<br/>");
         			}
         			
         			/* setting Account Address */
         			if(Util.isNotEmptyObject(accountResponse.getAccountAddress())){
         				accountInfo.append(accountResponse.getAccountAddress());
         				accountInfo.append(",<br/>");
         			}
         			
         			/* setting Account IFSC Code */
         			if(Util.isNotEmptyObject(accountResponse.getIfscCode())){
         				accountInfo.append(" IFSC CODE - ");
         				accountInfo.append(accountResponse.getIfscCode());
         			}
         	    }
         		info.setSiteAccount(accountInfo.toString());
         	}
			
			/* setting Modifications Cost Invoice PDF File Name */
			if(Util.isNotEmptyObject(resp.getDemandNotePdfFileName())) {
				info.setDemandNotePdfFileName(resp.getDemandNotePdfFileName());
			}else {
				info.setDemandNotePdfFileName("modification_charges_invoice.pdf");
			}
			
			Long count = 0L;
	     	for(FinBookingFormModiCostDtlsResponse modiCostDtlsResp : modiCostResponse.getFinBookingFormModiCostDtlsList()) {
	     		
	     		if(Util.isEmptyObject(modiCostDtlsResp.getModificationChargeDesc())) {
	     			modiCostDtlsResp.setModificationChargeDesc("N/A");
	     		}
	     		if(Util.isEmptyObject(modiCostDtlsResp.getUnits())) {
	     			modiCostDtlsResp.setUnits("N/A");
	     		}
	     		if(Util.isNotEmptyObject(modiCostDtlsResp.getQuantity())){
	     			modiCostDtlsResp.setQuantity(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(modiCostDtlsResp.getQuantity())).setScale(roundingModeSize, roundingMode).toString()));
	     		}else {
	     			modiCostDtlsResp.setQuantity("0.00");
	     		}
	     		if(Util.isNotEmptyObject(modiCostDtlsResp.getRate())){
	     			modiCostDtlsResp.setRate(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(modiCostDtlsResp.getRate())).setScale(roundingModeSize, roundingMode).toString()));
	     		}else {
	     			modiCostDtlsResp.setRate("0.00");
	     		}
	     		if(Util.isNotEmptyObject(modiCostDtlsResp.getBasicAmount())){
	     			modiCostDtlsResp.setBasAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(modiCostDtlsResp.getBasicAmount())).setScale(roundingModeSize, roundingMode).toString()));
	     		}else {
	     			modiCostDtlsResp.setBasAmount("0.00");
	     		}
	     		modiCostDtlsResp.setModificationChargesId(++count);
	     	}
			info.setFinBookingFormModiCostDtlsList(modiCostResponse.getFinBookingFormModiCostDtlsList());
			
			//ACP Added
			info.setFlatSaleOwnerIdBasedOnAccountId(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwnerIdBasedOnAccountId());
			info.setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwnerNameBasedOnAccountId());

			/* setting Company Billing Address and other details */
			setOfficeDetails(resp,info);

			/* generating pdf */
			email.setDemandNoteGeneratorInfo(info);
			fileInfo = pdfHelper.XMLWorkerHelperForModificationInvoice(email);
			
			EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo();
			financialPushNotification.setBookingFormId(info.getBookingFormId());
			financialPushNotification.setNotificationTitle(NOTIFICATION_TITLE);
			financialPushNotification.setNotificationBody(NOTIFICATION_BODY);
			financialPushNotification.setNotificationDescription("");
			financialPushNotification.setTypeMsg(NOTIFICATION_TYPE);
			financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1);
			financialPushNotification.setSiteId(transactionServiceInfo.getSiteId());
			
			EmpFinPushLegalAndModifiNotificationInfo legalAndModifiNotificationInfo = new EmpFinPushLegalAndModifiNotificationInfo();  
			legalAndModifiNotificationInfo.setType(MetadataId.MODIFICATION_COST.getId());
			legalAndModifiNotificationInfo.setFinBokAccInvoiceNo(info.getInvoiceNo());
			legalAndModifiNotificationInfo.setMetadataName(MetadataId.MODIFICATION_COST.getName());
			legalAndModifiNotificationInfo.setDocumentLocation(fileInfo.getUrl());
			legalAndModifiNotificationInfo.setDocumentLocation(fileInfo.getName());
			legalAndModifiNotificationInfo.setBookingFormId(info.getBookingFormId());
			financialPushNotification.setLegalAndModifiNotificationInfo(legalAndModifiNotificationInfo);
			pushNotification.add(financialPushNotification);
		}
		
		if(!transactionServiceInfo.isThisUplaodedData()) {
			ExecutorService executorService = Executors.newFixedThreadPool(10);
				try {
					executorService.execute(new Runnable() {
						public void run() {
							for (EmployeeFinancialPushNotificationInfo pushNotificationInfo : pushNotification) {
								try {
									pushNotificationHelper.sendFinancialStatusNotification(pushNotificationInfo,null);
								} catch (InformationNotFoundException e) {
								 	e.printStackTrace();
								}
							}//pushNotification
						}
					});
					executorService.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
					executorService.shutdown();
				}
		}
		return fileInfo;
	}
	
	public void setOfficeDetailsPojo(List<OfficeDtlsPojo> officeDetailsPojoList, CustomerPropertyDetailsInfo customerPropertyDetailsInfo, 
			DemandNoteGeneratorInfo info) {
		Properties prop = null;
		if(Util.isNotEmptyObject(responceCodesUtil)) {
			 prop = responceCodesUtil.getApplicationProperties();
		} else {
			prop = new ResponceCodesUtil().getApplicationProperties();
		}
		String flatSaleOwner = "";
		//String tempFlatSaleOwner = "";
		if(Util.isNotEmptyObject(customerPropertyDetailsInfo)) {
			flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner();
			if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				flatSaleOwner = customerPropertyDetailsInfo.getSiteId()+"_"+flatSaleOwner;	
			}
		}

		if(flatSaleOwner.equalsIgnoreCase("Indimax") || flatSaleOwner.equalsIgnoreCase("131_Landlord")) {
			flatSaleOwner = flatSaleOwner.trim().replaceAll(" ", "");
			String companyName = prop.getProperty(flatSaleOwner+"_COMPANY_NAME");
			String companySalutation = prop.getProperty(flatSaleOwner+"_COMPANY_SALUTATION");
			String companyBilling = prop.getProperty(flatSaleOwner+"_COMPANY_BILLING_ADDRESS");
			String companyTelephoneNo = prop.getProperty(flatSaleOwner+"_COMPANY_TELEPHONE_NO");
			String companyEmail = prop.getProperty(flatSaleOwner+"_COMPANY_EMAIL");

			String companyCin = prop.getProperty(flatSaleOwner+"_COMPANY_CIN");
			String companyGstin = prop.getProperty(flatSaleOwner+"_COMPANY_GSTIN");
			String companyWebsite = prop.getProperty(flatSaleOwner+"_COMPANY_WEBSITE");
			String companyLlpin = prop.getProperty(flatSaleOwner+"_COMPANY_LLPIN");
			String companyPanNumber = prop.getProperty(flatSaleOwner+"_COMPANY_PAN");
			String companyCity = prop.getProperty(flatSaleOwner+"_COMPANY_CITY");
			
			info.setCompanyName(companyName);
			info.setCompanySalutation(Util.isEmptyObject(companySalutation)?"Mr.":companySalutation);
			info.setCompanyBillingAddress(companyBilling);
			info.setCompanyTelephoneNo(companyTelephoneNo==""?"-":companyTelephoneNo);
			info.setCompanyEmail(companyEmail==""?"-":companyEmail);
			
			info.setCompanyCin(companyCin==""?"-":companyCin);
			info.setCompanyGstin(companyGstin==null?"-":companyGstin);
			info.setCompanyWebsite(companyWebsite);
			info.setCompanyLlpin(companyLlpin);
			info.setCompanyPanNumber(companyPanNumber);
			info.setCompanyCity(companyCity);
		} else if(Util.isNotEmptyObject(customerPropertyDetailsInfo) && customerPropertyDetailsInfo.getSiteId().equals(107l)
				//&& !"1".equals(resp.getFlatsResponse().get(0).getFlatSaleOwnerId())
				&& !"1".equals(customerPropertyDetailsInfo.getFlatSaleOwnerId())
				) {
			//please refer FLATS_SALE_OWNERS table for "1", "1" is sale owner of the current project company and other are landlord flats
			//for "1" sale owner id company address fetching from DB
			flatSaleOwner = customerPropertyDetailsInfo.getSiteId()+"_"+flatSaleOwner;

			flatSaleOwner = flatSaleOwner.trim().replaceAll(" ", "");
			String companyName = prop.getProperty(flatSaleOwner+"_COMPANY_NAME");
			String companySalutation = prop.getProperty(flatSaleOwner+"_COMPANY_SALUTATION");
			//String companyNameFooter = prop.getProperty(flatSaleOwner+"_COMPANY_NAME_FOOTER");
			String companyBilling = prop.getProperty(flatSaleOwner+"_COMPANY_BILLING_ADDRESS");
			String companyTelephoneNo = prop.getProperty(flatSaleOwner+"_COMPANY_TELEPHONE_NO");
			String companyEmail = prop.getProperty(flatSaleOwner+"_COMPANY_EMAIL");

			String companyCin = prop.getProperty(flatSaleOwner+"_COMPANY_CIN");
			String companyGstin = prop.getProperty(flatSaleOwner+"_COMPANY_GSTIN");
			String companyWebsite = prop.getProperty(flatSaleOwner+"_COMPANY_WEBSITE");
			String companyLlpin = prop.getProperty(flatSaleOwner+"_COMPANY_LLPIN");
			String companyPanNumber = prop.getProperty(flatSaleOwner+"_COMPANY_PAN");
			String companyCity = prop.getProperty(flatSaleOwner+"_COMPANY_CITY");
			
			info.setCompanyName(companyName);
			//info.setCompanyNameFooter(companyNameFooter);
			info.setCompanyBillingAddress(companyBilling);
			info.setCompanyTelephoneNo(Util.isEmptyObject(companyTelephoneNo)?"-":companyTelephoneNo);
			info.setCompanyEmail(Util.isEmptyObject(companyEmail)?"-":companyEmail);
			
			info.setCompanyCin(companyCin==""?"-":companyCin);
			info.setCompanyGstin(companyGstin==null?"-":companyGstin);
			info.setCompanyWebsite(companyWebsite==null?"-":companyWebsite);
			info.setCompanyLlpin(companyLlpin);
			info.setCompanyPanNumber(companyPanNumber);
			info.setCompanyCity(companyCity);
			info.setCompanySalutation(Util.isEmptyObject(companySalutation)?"Mr.":companySalutation);
		} else if(Util.isNotEmptyObject(officeDetailsPojoList) && Util.isNotEmptyObject(officeDetailsPojoList.get(0))) {
			OfficeDtlsPojo officeDetailsResponse = officeDetailsPojoList.get(0);
			if(Util.isNotEmptyObject(officeDetailsResponse.getName())) {
				info.setCompanyName(officeDetailsResponse.getName());
			}else {
				info.setCompanyName("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getBillingAddress())){
				info.setCompanyBillingAddress(officeDetailsResponse.getBillingAddress());
			}else {
				info.setCompanyBillingAddress("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getTelephoneNo())) {
				info.setCompanyTelephoneNo(officeDetailsResponse.getTelephoneNo());
			}else {
				info.setCompanyTelephoneNo("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getEmail())) {
				info.setCompanyEmail(officeDetailsResponse.getEmail());
			}else {
				info.setCompanyEmail("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCin())) {
				info.setCompanyCin(officeDetailsResponse.getCin());
			}else {
				info.setCompanyCin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getGstin())) {
				info.setCompanyGstin(officeDetailsResponse.getGstin());
			}else {
				info.setCompanyGstin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getWebsite())) {
				info.setCompanyWebsite(officeDetailsResponse.getWebsite());
			}else {
				info.setCompanyWebsite("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getLlpin())) {
				info.setCompanyLlpin(officeDetailsResponse.getLlpin());
			}else {
				info.setCompanyLlpin("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getPan())) {
				info.setCompanyPanNumber(officeDetailsResponse.getPan());
			}else {
				info.setCompanyPanNumber("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCity())) {
				info.setCompanyCity(officeDetailsResponse.getCity());
			} else {
				info.setCompanyCity("-");
			}
			info.setCompanySalutation("M/s.");
			
		} else {
			info.setCompanyName("-");
			info.setCompanyBillingAddress("-");
			info.setCompanyTelephoneNo("-");
			info.setCompanyEmail("-");
			info.setCompanyCin("-");
			info.setCompanyGstin("-");
			info.setCompanyWebsite("-");
			info.setCompanyPanNumber("-");
			info.setCompanyCity("-");
			info.setCompanySalutation("-");
		}
	}

	//these method in used in multiple places like sending email, generating pdf time, so if changing the code pls check other code
	private void setOfficeDetails(EmployeeFinancialTransactionResponse resp, DemandNoteGeneratorInfo info) {
		Properties prop = responceCodesUtil.getApplicationProperties();
		String flatSaleOwner = "";
		String flatSaleOwnerId = "";
		//String tempFlatSaleOwner = "";
		if(Util.isNotEmptyObject(resp.getFlatsResponse())) {
			//flatSaleOwner = resp.getFlatsResponse().get(0).getFlatSaleOwner();
			if(Util.isNotEmptyObject(info.getFlatSaleOwnerNameBasedOnAccountId())){
				   flatSaleOwner =info.getFlatSaleOwnerNameBasedOnAccountId().trim().replaceAll(" ", "");
				}
			if(resp.getFlatsResponse().get(0).getSiteId().equals(131l)) {
				flatSaleOwner = resp.getFlatsResponse().get(0).getSiteId()+"_"+flatSaleOwner;
			}
		}

		if (Util.isNotEmptyObject(info.getFlatSaleOwnerIdBasedOnAccountId()) && "approveFinancialMultipleTransaction".equals(info.getRequestFrom())) {
			flatSaleOwnerId=info.getFlatSaleOwnerIdBasedOnAccountId().toString();
		}else {
			if(Util.isNotEmptyObject(resp.getFlatsResponse()) && resp.getFlatsResponse().get(0).getSiteId().equals(107l) &&Util.isNotEmptyObject(resp.getFlatsResponse().get(0).getFlatSaleOwnerId()))
			{
			flatSaleOwnerId=resp.getFlatsResponse().get(0).getFlatSaleOwnerId();
			}
		}
		if(flatSaleOwner.equalsIgnoreCase("Indimax") || flatSaleOwner.equalsIgnoreCase("131_Landlord")) {
			flatSaleOwner = flatSaleOwner.trim().replaceAll(" ", "");
			String companyName = prop.getProperty(flatSaleOwner+"_COMPANY_NAME");
			//String companyNameFooter = prop.getProperty(flatSaleOwner+"_COMPANY_NAME_FOOTER");
			String companyBilling = prop.getProperty(flatSaleOwner+"_COMPANY_BILLING_ADDRESS");
			String companyTelephoneNo = prop.getProperty(flatSaleOwner+"_COMPANY_TELEPHONE_NO");
			String companyEmail = prop.getProperty(flatSaleOwner+"_COMPANY_EMAIL");

			String companyCin = prop.getProperty(flatSaleOwner+"_COMPANY_CIN");
			String companyGstin = prop.getProperty(flatSaleOwner+"_COMPANY_GSTIN");
			String companyWebsite = prop.getProperty(flatSaleOwner+"_COMPANY_WEBSITE");
			String companyLlpin = prop.getProperty(flatSaleOwner+"_COMPANY_LLPIN");
			String companyPanNumber = prop.getProperty(flatSaleOwner+"_COMPANY_PAN");
			String companyCity = prop.getProperty(flatSaleOwner+"_COMPANY_CITY");
			
			info.setCompanyName(companyName);
			//info.setCompanyNameFooter(companyNameFooter);
			info.setCompanyBillingAddress(companyBilling);
			info.setCompanyTelephoneNo(Util.isEmptyObject(companyTelephoneNo)?"-":companyTelephoneNo);
			info.setCompanyEmail(Util.isEmptyObject(companyEmail)?"-":companyEmail);
			
			info.setCompanyCin(companyCin==""?"-":companyCin);
			info.setCompanyGstin(companyGstin==null?"-":companyGstin);
			info.setCompanyWebsite(companyWebsite==null?"-":companyWebsite);
			info.setCompanyLlpin(companyLlpin);
			info.setCompanyPanNumber(companyPanNumber);
			info.setCompanyCity(companyCity);
		} else if(Util.isNotEmptyObject(resp.getFlatsResponse()) && resp.getFlatsResponse().get(0).getSiteId().equals(107l)
				//&& !"1".equals(resp.getFlatsResponse().get(0).getFlatSaleOwnerId())
				&& !"1".equals(flatSaleOwnerId)
				) {
			//please refer FLATS_SALE_OWNERS table for "1", "1" is sale owner of the current project company and other are landlord flats
			//for "1" sale owner id company address fetching from DB
			flatSaleOwner = resp.getFlatsResponse().get(0).getSiteId()+"_"+flatSaleOwner;

			flatSaleOwner = flatSaleOwner.trim().replaceAll(" ", "");
			String companyName = prop.getProperty(flatSaleOwner+"_COMPANY_NAME");
			//String companyNameFooter = prop.getProperty(flatSaleOwner+"_COMPANY_NAME_FOOTER");
			String companyBilling = prop.getProperty(flatSaleOwner+"_COMPANY_BILLING_ADDRESS");
			String companyTelephoneNo = prop.getProperty(flatSaleOwner+"_COMPANY_TELEPHONE_NO");
			String companyEmail = prop.getProperty(flatSaleOwner+"_COMPANY_EMAIL");

			String companyCin = prop.getProperty(flatSaleOwner+"_COMPANY_CIN");
			String companyGstin = prop.getProperty(flatSaleOwner+"_COMPANY_GSTIN");
			String companyWebsite = prop.getProperty(flatSaleOwner+"_COMPANY_WEBSITE");
			String companyLlpin = prop.getProperty(flatSaleOwner+"_COMPANY_LLPIN");
			String companyPanNumber = prop.getProperty(flatSaleOwner+"_COMPANY_PAN");
			String companyCity = prop.getProperty(flatSaleOwner+"_COMPANY_CITY");
			
			info.setCompanyName(companyName);
			//info.setCompanyNameFooter(companyNameFooter);
			info.setCompanyBillingAddress(companyBilling);
			info.setCompanyTelephoneNo(Util.isEmptyObject(companyTelephoneNo)?"-":companyTelephoneNo);
			info.setCompanyEmail(Util.isEmptyObject(companyEmail)?"-":companyEmail);
			
			info.setCompanyCin(companyCin==""?"-":companyCin);
			info.setCompanyGstin(companyGstin==null?"-":companyGstin);
			info.setCompanyWebsite(companyWebsite==null?"-":companyWebsite);
			info.setCompanyLlpin(companyLlpin);
			info.setCompanyPanNumber(companyPanNumber);
			info.setCompanyCity(companyCity);
		
			
		} else if(Util.isNotEmptyObject(resp.getOfficeDetailsList()) && Util.isNotEmptyObject(resp.getOfficeDetailsList().get(0))) {
			OfficeDtlsResponse officeDetailsResponse = resp.getOfficeDetailsList().get(0);
			if(Util.isNotEmptyObject(officeDetailsResponse.getName())) {
				info.setCompanyName(officeDetailsResponse.getName());
			}else {
				info.setCompanyName("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getBillingAddress())){
				info.setCompanyBillingAddress(officeDetailsResponse.getBillingAddress());
			}else {
				info.setCompanyBillingAddress("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getTelephoneNo())) {
				info.setCompanyTelephoneNo(officeDetailsResponse.getTelephoneNo());
			}else {
				info.setCompanyTelephoneNo("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getEmail())) {
				info.setCompanyEmail(officeDetailsResponse.getEmail());
			}else {
				info.setCompanyEmail("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCin())) {
				info.setCompanyCin(officeDetailsResponse.getCin());
			}else {
				info.setCompanyCin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getGstin())) {
				info.setCompanyGstin(officeDetailsResponse.getGstin());
			}else {
				info.setCompanyGstin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getWebsite())) {
				info.setCompanyWebsite(officeDetailsResponse.getWebsite());
			}else {
				info.setCompanyWebsite("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getLlpin())) {
				info.setCompanyLlpin(officeDetailsResponse.getLlpin());
			}else {
				info.setCompanyLlpin("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getPan())) {
				info.setCompanyPanNumber(officeDetailsResponse.getPan());
			}else {
				info.setCompanyPanNumber("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCity())) {
				info.setCompanyCity(officeDetailsResponse.getCity());
			} else {
				info.setCompanyCity("-");
			}
		} else {
			info.setCompanyName("-");
			info.setCompanyBillingAddress("-");
			info.setCompanyTelephoneNo("-");
			info.setCompanyEmail("-");
			info.setCompanyCin("-");
			info.setCompanyGstin("-");
			info.setCompanyWebsite("-");
			info.setCompanyPanNumber("-");
			info.setCompanyCity("-");
		}
		
	}

	public List<FileInfo> legalChargesInvoiceGeneratorHelper(List<EmployeeFinancialTransactionResponse> employeeFinancialTransactionResponseList,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
		log.info(" ***** control is inside the legalChargesInvoiceGeneratorHelper in EmployeeFinancialHelper ***** ");
		final List<EmployeeFinancialPushNotificationInfo> pushNotification = new ArrayList<EmployeeFinancialPushNotificationInfo>();
		Properties prop= responceCodesUtil.getApplicationProperties();

		List<FileInfo> fileInfoList = new ArrayList<>();

		for(EmployeeFinancialTransactionResponse resp : employeeFinancialTransactionResponseList ) {
			FileInfo fileInfo = new FileInfo();
			CustomerPropertyDetailsInfo	customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
			FinBookingFormLegalCostPdfResponse legalCostPdfResponse = resp.getFinBookingFormLegalCostPdfResponse();
			if(Util.isNotEmptyObject(legalCostPdfResponse) && Util.isNotEmptyObject(legalCostPdfResponse.getFinBookingFormAccountsResponse())
					&& Util.isNotEmptyObject(legalCostPdfResponse.getFinBookingFormLegalCostResponse()) && Util.isNotEmptyObject(legalCostPdfResponse.getFinBookingFormLegalCostResponse().getFinBookingFormLglCostDtlsList())) {
				DemandNoteGeneratorInfo info = new DemandNoteGeneratorInfo();
				Email email = new Email();
				email.setPortNumber(transactionServiceInfo.getPortNumber());
				String rightSidelogoForPdf = "";
				String leftSidelogoForPdf = "";
				String thanksAndRegardsFrom="";
				
				/* generating Legal Charge Invoice for different flats */
				String NOTIFICATION_TITLE = transactionServiceInfo.getCustomerPropertyDetailsInfo().getSiteName()+" Legal Invoice";
				String NOTIFICATION_BODY = "Dear Customer your legal invoice has been generated!.";
				String NOTIFICATION_TYPE = "Sumadhura Legal Invoice";
				String NOTIFICATION_TYPE1 = "Sumadhura Legal Invoice";

				if(Util.isNotEmptyObject(transactionServiceInfo.getSiteIds()) && transactionServiceInfo.getSiteIds().get(0).equals(131l)) {
					//rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_LOGO1");
					//thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
					NOTIFICATION_TITLE = "Aspire Aurum Legal Invoice";
					NOTIFICATION_BODY = "Dear Customer your legal invoice has been generated!.";
					NOTIFICATION_TYPE = "Sumadhura Legal Invoice";
					NOTIFICATION_TYPE1 = "Sumadhura Legal Invoice";
				} else {
					//rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1");
					//thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
				}
				
				Site site = new Site();
				site.setSiteId(customerPropertyDetailsInfo.getSiteId());
				site.setName(customerPropertyDetailsInfo.getSiteName());		
				//ACP Added
				/*transactionServiceInfo.getCustomerPropertyDetailsInfo().setFlatSaleOwnerIdBasedOnAccountId(Long.valueOf(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwnerId()));
				transactionServiceInfo.getCustomerPropertyDetailsInfo().setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwner());*/
				
				Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,transactionServiceInfo.getCustomerPropertyDetailsInfo());			
				
				rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
				leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
				thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
				//greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
				
				info.setRightSidelogoForPdf(rightSidelogoForPdf);
				info.setLeftSidelogoForPdf(leftSidelogoForPdf);
				info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
				
				FinBookingFormAccountsResponse finBokAccountsResp = legalCostPdfResponse.getFinBookingFormAccountsResponse();
				FinBookingFormLegalCostResponse finBokLegalcostResp = legalCostPdfResponse.getFinBookingFormLegalCostResponse();
				/* setting Invoice Details */
				info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
				if(Util.isNotEmptyObject(finBokAccountsResp.getFinBokAccInvoiceNo())) {
					info.setInvoiceNo(finBokAccountsResp.getFinBokAccInvoiceNo());
				}else {
					info.setInvoiceNo("N/A");
				}
				
				if(Util.isNotEmptyObject(finBokLegalcostResp.getFinBokFrmLegalCostId())) {
					info.setFolderId(finBokLegalcostResp.getFinBokFrmLegalCostId().toString());
				}else {
					info.setFolderId("Anonymous");
				}
				
				if(Util.isNotEmptyObject(finBokLegalcostResp.getLegalAmount())) {
					info.setAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finBokLegalcostResp.getLegalAmount()).setScale(roundingModeSize, roundingMode).toString()));
				}else {
					info.setAmount("0.00");
				}
				
				if(Util.isNotEmptyObject(resp.getPercentageValue())) {
					info.setPercentageValue(BigDecimal.valueOf(resp.getPercentageValue()).setScale(roundingModeSize, roundingMode).toString());
					
					info.setCgstPercentageValue(BigDecimal.valueOf(resp.getPercentageValue()/2).setScale(roundingModeSize, roundingMode).toString());
					info.setSgstPercentageValue(BigDecimal.valueOf(resp.getPercentageValue()/2).setScale(roundingModeSize, roundingMode).toString());
					
				}else {
					info.setPercentageValue("0.00");
				}
				
				if(Util.isNotEmptyObject(finBokLegalcostResp.getTaxAmount())) { 
					info.setTotalCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finBokLegalcostResp.getTaxAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
					info.setTotalSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finBokLegalcostResp.getTaxAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
					info.setTotalTaxAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finBokLegalcostResp.getTaxAmount()).setScale(roundingModeSize, roundingMode).toString()));
				}else { 
					info.setTotalCgstAmount("0.00"); 
					info.setTotalSgstAmount("0.00");
					info.setTotalTaxAmount("0.00");
				}
				
				if(Util.isNotEmptyObject(finBokLegalcostResp.getTotalAmount())) {
					double totalAmount = finBokLegalcostResp.getTotalAmount();
					String setTotalAmount =  BigDecimal.valueOf(totalAmount).setScale(roundingModeSize, roundingMode).toString();
					String amountInWords = getTheAmountInWords(Double.valueOf(totalAmount));
					info.setTotalAmount(currencyUtil.convertUstoInFormat(setTotalAmount));
					//info.setTotalAmountInWords(new NumberToWord().convertNumberToWords(Double.valueOf(info.getTotalAmount()).longValue()) +" Rupees Only.");
					info.setTotalAmountInWords(amountInWords);
					//log.info("Total Amount ----------------------------------------- \n"+finBokLegalcostResp.getTotalAmount()+"\n"+setTotalAmount+"\n"+amountInWords);
				} else {
					info.setTotalAmount("0.00");
					info.setTotalAmountInWords("Zero Rupees Only.");
				}
				
				/* setting applicant and coApplicant names and mobile numbers */
				if(Util.isNotEmptyObject(resp.getFlatsResponse())) {
					StringBuilder name = new StringBuilder(); 
					StringBuilder mobileNos = new StringBuilder();
					StringBuilder flatNo = new StringBuilder();
					StringBuilder floorName = new StringBuilder();
					StringBuilder blockName = new StringBuilder();
					StringBuilder siteName = new StringBuilder();
					StringBuilder pancard = new StringBuilder();
					
					List<CustomerPropertyDetailsInfo> detailsList  = resp.getFlatsResponse();

					for (int index = 0; index <  detailsList.size(); index++) {
						customerPropertyDetailsInfo = detailsList.get(index);

						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName()) || Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())) {
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName())){	
							name.append(customerPropertyDetailsInfo.getCustomerName());
							if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())){
								name.append(" AND ");
								name.append(customerPropertyDetailsInfo.getCoAppFullName());
							}
						}else {
							name.append("N/A");
						}
						}else {
							name.append("N/A");
						}
						
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber()) ||  Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())) {
							if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
								mobileNos.append(customerPropertyDetailsInfo.getContactNumber());
							}
							if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())){
								if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
									mobileNos.append(",");
								}
								mobileNos.append(customerPropertyDetailsInfo.getAlternatePhoneNo());
							}
						}else {
							mobileNos.append("N/A");
						}
						
						/* setting flat Number */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
							flatNo.append(customerPropertyDetailsInfo.getFlatNo());
						}else {
							flatNo.append("N/A");
						}
						
						/* setting Floor Name   */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFloorName())) {
							floorName.append(customerPropertyDetailsInfo.getFloorName());
						}else {
							floorName.append("N/A");
						}
						
						/* setting Block name */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBlockName())) {
							blockName.append(customerPropertyDetailsInfo.getBlockName());
						}else {
							blockName.append("N/A");
						}
						
						/* setting Site Name */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteName())) {
							siteName.append(customerPropertyDetailsInfo.getSiteName());
						}else {
							siteName.append("N/A");
						}
						
						/* setting customer pancard  */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getPancard())) {
							pancard.append(customerPropertyDetailsInfo.getPancard());
						}else {
							pancard.append("N/A");
						}
						
						/* setting siteId */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteId())) {
							info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
						}else {
							info.setSiteId("Anonymous_Site");
						}
						
						/* setting flatBookingId */
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatBookingId())) {
							info.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId().toString());
							info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
						}else {
							info.setFlatBookingId("Anonymous_Flat_Booking");
						}
						
					}
					info.setCustomerNames(name.toString());
					info.setMobileNumbers(mobileNos.toString());
					info.setFlatName(flatNo.toString());
					info.setFloorName(floorName.toString());
					info.setBlockName(blockName.toString());
					//info.setSiteName(siteName.toString());
					if(info.getSiteId()!=null && info.getSiteId().equals("131")) {
						info.setSiteName("Aspire Aurum");
					}else {
						info.setSiteName(siteName.toString());
					}
					info.setPancard(pancard.toString());
				}
				
				/* setting applicant permenent address  */
				info.setCustomerStateCode("N/A");
				info.setCustomerGstin("N/A");
				//StringBuilder customerStateName = new StringBuilder();
				if(Util.isNotEmptyObject(resp.getCustomerAddressInfoList())) {
					for(AddressInfo addressInfo : resp.getCustomerAddressInfoList()){
					/* Taking Correspondence Address  */
		             if(Util.isNotEmptyObject(addressInfo)) {
		            	 if(addressInfo.getAddressMappingType().getAddressType().equalsIgnoreCase("Correspondence")){
		            		 if(Util.isNotEmptyObject(addressInfo.getAddress1())) {
		            		    info.setAddress(addressInfo.getAddress1());
		            		    if(Util.isNotEmptyObject(addressInfo.getState())) {
		            		    	info.setCustomerState(addressInfo.getState());
		            		    }else {
		            		    	info.setCustomerState("N/A");
		            		    }
		            		    if(Util.isNotEmptyObject(addressInfo.getPincode())) {
		            		    	 //info.setAddress(info.getAddress()+"-"+addressInfo.getPincode());
		            		    	 break;
		            		    }
		            		 }else {
		            			 info.setAddress("N/A");
			            		 info.setCustomerState("N/A");
		            		 }
		            	 }else {
		            		 info.setAddress("N/A");
		            		 info.setCustomerState("N/A");
		            	 }
		             }
				  }
				}else {
					info.setAddress("N/A");
           		 	info.setCustomerState("N/A");
				}
				
				/* setting site address and survey number */
				info.setSiteStateCode("N/A");
				
				if(customerPropertyDetailsInfo.getFlatSaleOwnerNameBasedOnAccountId().equalsIgnoreCase("Indimax")) {
					String flatSaleOwner =customerPropertyDetailsInfo.getFlatSaleOwnerNameBasedOnAccountId().trim().replaceAll(" ", "");
					String siteAddress = prop.getProperty(flatSaleOwner+"_SITE_ADDRESS");
					String getSurveyNo = prop.getProperty(flatSaleOwner+"_SITE_SURVEY_NO");
					info.setSurveyNo(getSurveyNo==null?"N/A":getSurveyNo);
					info.setSiteAddress(siteAddress);
				} else if(Util.isNotEmptyObject(resp.getSiteAddressInfoList())) {
					
					StringBuilder siteAddress = new StringBuilder();
					StringBuilder placeOfSupply = new StringBuilder();
					for(AddressInfo addressInfo : resp.getSiteAddressInfoList()){
						
						/*  setting survey number */
						if(Util.isNotEmptyObject(addressInfo.getSurveyNo())) {
							info.setSurveyNo(addressInfo.getSurveyNo());
						}else {
							info.setSurveyNo("N/A");
						}
						if(Util.isNotEmptyObject(addressInfo.getStreet())) {
							siteAddress.append(addressInfo.getStreet());
							siteAddress.append(",");
						}
						if(Util.isNotEmptyObject(addressInfo.getArea())) {
							siteAddress.append(addressInfo.getArea());
							siteAddress.append(",");
						}
						if(Util.isNotEmptyObject(addressInfo.getDistrict())) {
							siteAddress.append(addressInfo.getDistrict());
							siteAddress.append(",");
						}
						if(Util.isNotEmptyObject(addressInfo.getCity())) {
							siteAddress.append(addressInfo.getCity());
							siteAddress.append("");
							placeOfSupply.append(addressInfo.getCity());
							if(Util.isNotEmptyObject(addressInfo.getState())) {
								placeOfSupply.append(",").append(addressInfo.getState());
							}
						}else {
							placeOfSupply.append("N/A");
							if(Util.isNotEmptyObject(addressInfo.getState())) {
								placeOfSupply.append(",").append(addressInfo.getState());
							}
						}
						if(Util.isNotEmptyObject(addressInfo.getState())) {
							info.setSiteState(addressInfo.getState());
						}else {
							info.setSiteState("N/A");
						}
						if(Util.isNotEmptyObject(addressInfo.getPincode())) {
							siteAddress.append("-");
							siteAddress.append(addressInfo.getPincode());
						}
					}
					info.setPlaceOfSupply(placeOfSupply.toString());
					info.setSiteAddress(siteAddress.toString());
				}
				
				/* setting site Account  */
	         	if(Util.isNotEmptyObject(resp.getFinProjectAccountResponseList())) {
	         		StringBuilder accountInfo = new StringBuilder();
	         		for(FinProjectAccountResponse accountResponse : resp.getFinProjectAccountResponseList()) {
	         			/* setting bank details  */
	         			if(Util.isNotEmptyObject(accountResponse.getBankName())){
	         				accountInfo.append(accountResponse.getBankName());
	         				accountInfo.append(",");
	         			}
	         			if(Util.isNotEmptyObject(accountResponse.getAccountHolderName())){
	         				info.setAccountHolderName(accountResponse.getAccountHolderName());
	         			} else {
	         				throw new EmployeeFinancialServiceException("Account holder name missing..!");
	         			}
	         			/* setting bank account details  */
	         			if(Util.isNotEmptyObject(accountResponse.getSiteBankAccountNumber())){
	         				accountInfo.append("<strong> A/C. "+accountResponse.getSiteBankAccountNumber()+"</strong>");
	         				accountInfo.append(",<br/>");
	         			}
	         			
	         			/* setting Account Address */
	         			if(Util.isNotEmptyObject(accountResponse.getAccountAddress())){
	         				accountInfo.append(accountResponse.getAccountAddress());
	         				accountInfo.append(",<br/>");
	         			}
	         			
	         			/* setting Account IFSC Code */
	         			if(Util.isNotEmptyObject(accountResponse.getIfscCode())){
	         				accountInfo.append(" IFSC CODE - ");
	         				accountInfo.append(accountResponse.getIfscCode());
	         			}
	         	    }
	         		info.setSiteAccount(accountInfo.toString());
	         	}
				
				
				/* setting Legal Charge Invoice PDF File Name */
				if(Util.isNotEmptyObject(resp.getDemandNotePdfFileName())) {
					info.setDemandNotePdfFileName(resp.getDemandNotePdfFileName());
				}else {
					info.setDemandNotePdfFileName("legal_charge_invoice.pdf");
				}
				/* Setting Legal Cost Details */
				Long count = 0L;
		     	for(FinBookingFormLglCostDtlsResponse legalCostDtlsResp : finBokLegalcostResp.getFinBookingFormLglCostDtlsList()) {
		     		
		     		if(Util.isEmptyObject(legalCostDtlsResp.getDescription())) {
		     			legalCostDtlsResp.setDescription("N/A");
		     		}
		     		if(Util.isNotEmptyObject(legalCostDtlsResp.getLegalAmount())){
		     			legalCostDtlsResp.setLegalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(legalCostDtlsResp.getLegalAmount())).setScale(roundingModeSize, roundingMode).toString()));
		     		}else {
		     			legalCostDtlsResp.setLegalAmount("0.00");
		     		}
		     		legalCostDtlsResp.setFinBokingFormLglCostDtlsId(++count);
		     	}
				info.setFinBookingFormLglCostDtlsList(finBokLegalcostResp.getFinBookingFormLglCostDtlsList());
				
				//ACP Added
				info.setFlatSaleOwnerIdBasedOnAccountId(Long.valueOf(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwnerIdBasedOnAccountId()));
				info.setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getCustomerPropertyDetailsInfo().getFlatSaleOwnerNameBasedOnAccountId());

				setOfficeDetails(resp,info);
				/* setting Company Billing Address and other details */
				/*if(Util.isNotEmptyObject(resp.getOfficeDetailsList()) && Util.isNotEmptyObject(resp.getOfficeDetailsList().get(0))) {
					OfficeDtlsResponse officeDetailsResponse = resp.getOfficeDetailsList().get(0);
					if(Util.isNotEmptyObject(officeDetailsResponse.getName())) {
						info.setCompanyName(officeDetailsResponse.getName());
					}else {
						info.setCompanyName("N/A");
					}
					if(Util.isNotEmptyObject(officeDetailsResponse.getBillingAddress())){
						info.setCompanyBillingAddress(officeDetailsResponse.getBillingAddress());
					}else {
						info.setCompanyBillingAddress("N/A");
					}
					if(Util.isNotEmptyObject(officeDetailsResponse.getTelephoneNo())) {
						info.setCompanyTelephoneNo(officeDetailsResponse.getTelephoneNo());
					}else {
						info.setCompanyTelephoneNo("N/A");
					}
					if(Util.isNotEmptyObject(officeDetailsResponse.getEmail())) {
						info.setCompanyEmail(officeDetailsResponse.getEmail());
					}else {
						info.setCompanyEmail("N/A");
					}
					if(Util.isNotEmptyObject(officeDetailsResponse.getCin())) {
						info.setCompanyCin(officeDetailsResponse.getCin());
					}else {
						info.setCompanyCin("N/A");
					}
					if(Util.isNotEmptyObject(officeDetailsResponse.getGstin())) {
						info.setCompanyGstin(officeDetailsResponse.getGstin());
					}else {
						info.setCompanyGstin("N/A");
					}
					
					if(Util.isNotEmptyObject(officeDetailsResponse.getWebsite())) {
						info.setCompanyWebsite(officeDetailsResponse.getWebsite());
					}else {
						info.setCompanyWebsite("-");
					}
					
					if(Util.isNotEmptyObject(officeDetailsResponse.getLlpin())) {
						info.setCompanyLlpin(officeDetailsResponse.getLlpin());
					}else {
						info.setCompanyLlpin("-");
					}
				}else {
					info.setCompanyName("N/A");
					info.setCompanyBillingAddress("N/A");
					info.setCompanyTelephoneNo("N/A");
					info.setCompanyEmail("N/A");
					info.setCompanyCin("N/A");
					info.setCompanyGstin("N/A");
					info.setCompanyWebsite("-");
					info.setCompanyLlpin("-");
				}*/
				/* generating pdf */
				email.setDemandNoteGeneratorInfo(info);
				
				fileInfo = pdfHelper.XMLWorkerHelperForLegalInvoice(email);
				
				EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo();
				financialPushNotification.setBookingFormId(info.getBookingFormId());
				financialPushNotification.setNotificationTitle(NOTIFICATION_TITLE);
				financialPushNotification.setNotificationBody(NOTIFICATION_BODY);
				financialPushNotification.setNotificationDescription("");
				financialPushNotification.setTypeMsg(NOTIFICATION_TYPE);
				financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1);
				financialPushNotification.setSiteId(transactionServiceInfo.getSiteId());
				
				EmpFinPushLegalAndModifiNotificationInfo legalAndModifiNotificationInfo = new EmpFinPushLegalAndModifiNotificationInfo();  
				legalAndModifiNotificationInfo.setType(MetadataId.LEGAL_COST.getId());
				legalAndModifiNotificationInfo.setFinBokAccInvoiceNo(info.getInvoiceNo());
				legalAndModifiNotificationInfo.setMetadataName(MetadataId.LEGAL_COST.getName());
				legalAndModifiNotificationInfo.setDocumentLocation(fileInfo.getUrl());
				legalAndModifiNotificationInfo.setDocumentName(fileInfo.getName());
				legalAndModifiNotificationInfo.setBookingFormId(info.getBookingFormId());
				financialPushNotification.setLegalAndModifiNotificationInfo(legalAndModifiNotificationInfo);
				pushNotification.add(financialPushNotification);
			}
			fileInfoList.add(fileInfo);
		}
		
		if( !transactionServiceInfo.isThisUplaodedData()) {
				ExecutorService executorService = Executors.newFixedThreadPool(10);
					try {
						executorService.execute(new Runnable() {
							public void run() {
								for (EmployeeFinancialPushNotificationInfo pushNotificationInfo : pushNotification) {
									try {
										pushNotificationHelper.sendFinancialStatusNotification(pushNotificationInfo,null);
									} catch (InformationNotFoundException e) {
									 	e.printStackTrace();
									}
								}//pushNotification
							}
						});
						executorService.shutdown();
					} catch (Exception e) {
						e.printStackTrace();
						executorService.shutdown();
					}
			}
			
		
		return fileInfoList;
	}
	
	public void validateFileAttachementd(List<EmployeeFinancialTransactionResponse> transactionResponseList, List<FileInfo> fileInfoList, Map<Long, List<FinancialProjectMileStonePojo>> flatPreviousMilestoneNonPaidData) throws Exception{
		log.info("***** Control inside the EmployeeFinancialHelper.validateFileAttachementd() *****");
		int count = 0;

		for (int index = 0; index <  transactionResponseList.size();index++) {
				EmployeeFinancialTransactionResponse resp = transactionResponseList.get(index);
				if(Util.isNotEmptyObject(resp) && Util.isNotEmptyObject(resp.getFlatsResponse()) 
					&& Util.isNotEmptyObject(resp.getFlatsResponse().get(0)) && Util.isNotEmptyObject(resp.getFlatsResponse().get(0).getCustomerEmail())) {

				CustomerPropertyDetailsInfo customerPropertyDetailsInfo = resp.getFlatsResponse().get(0);

				FileInfo demandNoteFileInfo = resp.getFileInfoList().get(0);
				FileInfo fileInfo = fileInfoList.get(count);
				if(fileInfo.getFilePath()!=null && demandNoteFileInfo.getFilePath()!=null) {//don't move this if condition to live, before cheking
					if(!fileInfo.getFilePath().equals(demandNoteFileInfo.getFilePath()) && !fileInfo.getUrl().equals(demandNoteFileInfo.getUrl())) {
						log.info("Error occurred while sending demand note. File has found mismatch");		
						throw new EmployeeFinancialServiceException("Error occurred while sending demand note. File has found mismatch");
					}
					if(!fileInfo.getName().contains(customerPropertyDetailsInfo.getFlatNo())) {
							log.info("Error occurred while sending demand note.");		
							throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
					}
					if(!demandNoteFileInfo.getName().contains(customerPropertyDetailsInfo.getFlatNo())) {
						log.info("Error occurred while sending demand note.");		
						throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
					}
				}
				if(Util.isNotEmptyObject(fileInfo) && Util.isNotEmptyObject(fileInfo.getFilePath())) {
					String fileName = fileInfo.getFilePath();
					File file = new File(fileName);
					long fileLength = file.length();
					//checking weather file is successfully generated or not, if not generated then waiting for file to be generate
					if (fileLength < 10 && file.exists()) {
						index = index - 1;
						//here count variable not increasing, bcoz we need to check the same fileInfoList object with resp list object, and here index value also decreasing 
						System.out.println(fileInfo.getFilePath()+" \nfile.length() "+fileLength+" index repeat "+index);
						Thread.sleep(3000);
						continue;
					}

					
					if(Util.isEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
						log.info("Error occurred while sending demand note.");		
						throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
					} else if(!fileInfo.getName().contains(customerPropertyDetailsInfo.getFlatNo())) {
						log.info("Error occurred while sending demand note.");		
						throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
					}
					
					if(Util.isNotEmptyObject(flatPreviousMilestoneNonPaidData)) {//if flatBooking Id is not exist in flatPreviousMilestoneNonPaidData that measn , for that booking demand note not generated, either it's failed to generate or already generated 
						if(!flatPreviousMilestoneNonPaidData.containsKey(customerPropertyDetailsInfo.getFlatBookingId())) {
							log.info("Error occurred while sending demand note.");		
							throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
						}
					}
				} else {
					//Util.isNotEmptyObject(fileInfo)
					log.info("Error occurred while sending demand note. Generated pdf file not found");
					throw new EmployeeFinancialServiceException("Error occurred while sending demand note. Generated pdf file not found");
				}
			}
			++count;
		}
	}
	
    // TO append string into a file
	public static void appendStrToFile(String fileName, String str) {
		// Try block to check for exceptions
		try {
			// Open given file in append mode by creating an object of BufferedWriter class
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));

			// Writing on output stream
			out.write(str);
			out.write("\n");//new line
			// Closing the connection
			out.close();
		} catch (IOException e) {
			System.out.println("exception occoured" + e);
		}
	}
	
	@Async//These for new
	public void sendFinancialMailToCustomers(List<EmployeeFinancialTransactionResponse> transactionResponseList, List<FileInfo> fileInfoList, String name,
			Map<Long, List<FinancialProjectMileStonePojo>> flatPreviousMilestoneNonPaidData) throws Exception, IOException {
		log.info(" ***** control is inside the sendFinancialMailToCustomers in EmployeeFinancialHelper ***** flatPreviousMilestoneNonPaidData Size \n");
		
		String demandNotePdfFilePath = "";
		String fileName = "";
		SimpleDateFormat dateFormat = null;
		String previousTransactionReceiveDate = null;
		StringBuilder filePath = null;
		boolean sendEmailAndNotification = true;
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			previousTransactionReceiveDate = dateFormat.format(new Date()).replace(":", "-");
			Properties prop= responceCodesUtil.getApplicationProperties();
			demandNotePdfFilePath = prop.getProperty("CUG_DEMAND_NOTE_PDF_PATH");
			sendEmailAndNotification = Util.isEmptyObject(prop.getProperty("SEND_NOTIFICATION_AND_EMAIL"))?true:prop.getProperty("SEND_NOTIFICATION_AND_EMAIL").equals("yes");
			System.out.println("sendEmailAndNotification "+sendEmailAndNotification);
			if(sendEmailAndNotification==false) {
				System.out.println("sendEmailAndNotification not sent "+sendEmailAndNotification);
				//if the value is false, then don't send the email and push notification
				System.out.println("stopped sendEmailAndNotification "+sendEmailAndNotification+" EMAIL "+prop.getProperty("SEND_NOTIFICATION_AND_EMAIL")+" "+prop.getProperty("SEND_NOTIFICATION_AND_EMAIL").equals("yes"));
				return;
			}
			log.info(" ***** control is inside the sendFinancialMailToCustomers in EmployeeFinancialHelper ***** ");
			filePath = new StringBuilder(demandNotePdfFilePath) .append("/").append("134")
						.append("/DemandNotePreviewFiles");
				//storing success msg in txt file
			fileName = new String(filePath+"/"+previousTransactionReceiveDate+".txt");
			if(Util.isNotEmptyObject(flatPreviousMilestoneNonPaidData)) {
				appendStrToFile(fileName,"Total list size demand note : "+flatPreviousMilestoneNonPaidData.size()+" "+name);
			}
			//LocalDateTime now = LocalDateTime.now();
			//log.info("sendFinancialMailToCustomers"+ dtf.format(now));
			Thread.sleep(5000l);//check and move to live
			//now = LocalDateTime.now();
			//log.info("sendFinancialMailToCustomers"+ dtf.format(now));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Long bookingId = 0l;
		int count = 0;
		boolean isRequestFromDemandNote = name.equalsIgnoreCase(MetadataId.BOOKING_FORM_DEMAND_NOTE.getName());
		for(EmployeeFinancialTransactionResponse resp : transactionResponseList) {
			  try {
				if(Util.isNotEmptyObject(resp) && Util.isNotEmptyObject(resp.getFlatsResponse()) 
						&& Util.isNotEmptyObject(resp.getFlatsResponse().get(0)) && Util.isNotEmptyObject(resp.getFlatsResponse().get(0).getCustomerEmail())) {
					bookingId = 0l;
					FileInfo fileInfo = fileInfoList.get(count);
					CustomerPropertyDetailsInfo customerPropertyDetailsInfo = resp.getFlatsResponse().get(0);
					String flatNo = customerPropertyDetailsInfo.getFlatNo();

					if(isRequestFromDemandNote) {
						//if the request from demand note, then taking file details from resp.getFileInfoList() object, not from fileInfoList list object
						FileInfo demandNoteFileInfo = resp.getFileInfoList().get(0);
						if(fileInfo.getFilePath()!=null && demandNoteFileInfo.getFilePath()!=null) {//don't move this if condition to live, before checking
							if(!fileInfo.getFilePath().equals(demandNoteFileInfo.getFilePath()) && !fileInfo.getUrl().equals(demandNoteFileInfo.getUrl())) {
								log.info("list file path and resp file path is not same ");
								appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" not Email sent failed... list file path and resp file path is not same ");
								throw new EmployeeFinancialServiceException("Error occurred while sending demand note. File has found mismatch");
							}
							if(!fileInfo.getName().contains(customerPropertyDetailsInfo.getFlatNo())) {
								log.info("pdf fileName does't match the flatNo");
								appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" not Email sent failed... pdf fileName does't match the flatNo");
								throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
							}
							if(!demandNoteFileInfo.getName().contains(customerPropertyDetailsInfo.getFlatNo())) {
								log.info("resp file name does't match with flat no");		
								appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" not Email sent failed... resp file name does't match with flat no");
								throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
							}
						}
						//if the request from demand note, then taking file details from resp.getFileInfoList() object, not from fileInfoList list object					
						fileInfo = demandNoteFileInfo;
					}
					if(Util.isNotEmptyObject(fileInfo)) {
						bookingId = customerPropertyDetailsInfo.getFlatBookingId();
						//String flatNo = resp.getFlatsResponse().get(0).getFlatNo();
						Email email = new Email();
						String siteName = "";
						email.setFilePath(fileInfo.getFilePath());
						email.setFileName(fileInfo.getName());
						email.setToMail(resp.getFlatsResponse().get(0).getCustomerEmail());
						//CustomerPropertyDetailsInfo customerPropertyDetailsInfo = resp.getFlatsResponse().get(0);
						if(Util.isNotEmptyObject(flatPreviousMilestoneNonPaidData)) {//if flatBooking Id is not exist in flatPreviousMilestoneNonPaidData that measn , for that booking demand note not generated, either it's failed to generate or already generated 
							if(!flatPreviousMilestoneNonPaidData.containsKey(customerPropertyDetailsInfo.getFlatBookingId())) {
								++count;
								log.info(fileInfo.getName()+" "+customerPropertyDetailsInfo.getFlatNo()+" Flat does't contains in flatPreviousMilestoneNonPaidData");
								appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" not Email sent failed... flatPreviousMilestoneNonPaidData.containsKey(customerPropertyDetailsInfo.getFlatBookingId())");
								continue;
							}
							
							if(Util.isEmptyObject(flatPreviousMilestoneNonPaidData.get(customerPropertyDetailsInfo.getFlatBookingId()))) {
								++count;
								log.info(fileInfo.getName()+" "+customerPropertyDetailsInfo.getFlatNo()+" Flat does't contains in flatPreviousMilestoneNonPaidData");
								appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" not Email sent failed... flatPreviousMilestoneNonPaidData.containsKey(customerPropertyDetailsInfo.getFlatBookingId())");
								continue;
							}
						}
						
						 if(fileInfo.getName()!=null && !fileInfo.getName().contains(customerPropertyDetailsInfo.getFlatNo())) {
								log.info("fileInfo.getName() is not the same as customerPropertyDetailsInfo.getFlatNo()");
								log.info(fileInfo.getName()+" "+customerPropertyDetailsInfo.getFlatNo());
								//throw new EmployeeFinancialServiceException("Error occurred while sending demand note.");
						 }
						
						if (customerPropertyDetailsInfo.getSiteId().equals(131l)) {
							email.setThanksAndRegardsFrom("Greetings from  Aspire Aurum");
							siteName = "Aspire Aurum";
						} else {
							email.setThanksAndRegardsFrom("Greetings from Sumadhura");
							siteName = resp.getFlatsResponse().get(0).getSiteName();
						}

						if (customerPropertyDetailsInfo.getFlatSaleOwner() != null) {
							if (customerPropertyDetailsInfo.getFlatSaleOwner().equalsIgnoreCase("Indimax")) {
								email.setThanksAndRegardsFrom("Greeting for the day");	
							} else if (customerPropertyDetailsInfo.getFlatSaleOwner().equalsIgnoreCase("Sumadhura Vasavi LLP")) {
								email.setThanksAndRegardsFrom("Greeting for the day");
							}
						}
						
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo) && customerPropertyDetailsInfo.getSiteId().equals(107l)
								&& !"1".equals(customerPropertyDetailsInfo.getFlatSaleOwnerId())
								) {
							email.setThanksAndRegardsFrom("Greeting for the day");
						}
						
						log.info(" ***** control is inside the sendFinancialMailToCustomers in EmployeeFinancialHelper ***** NAMED "+name+" Email :"+resp.getFlatsResponse().get(0).getCustomerEmail());
						if(isRequestFromDemandNote) {
							email.setTemplateName("/demandnotes/Demand_Note_Email_To_Customer.vm");
							//email.setSubject("Reg: Demand Note Payment. ");
							email.setSubject(siteName+"-"+flatNo+" Demand Note.");//this is for CUG
							//email.setSubject(siteName+" Demand Note.");//this is for live
						}else if(name.equalsIgnoreCase(MetadataId.INTEREST_LETTER.getName())) {
							email.setTemplateName("/demandnotes/Interest_Letter_Email_To_Customer.vm");
							//email.setSubject("Interest Letter");
							email.setRequestUrl("Interest Letter");
							email.setSubject(siteName+"-"+flatNo+" Interest Letter.");//this is for CUG
						}else if(name.equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())){
							email.setTemplateName("/demandnotes/Modification_INV_Email_To_Customer.vm");
							email.setSubject("Reg: Flat Modifications Payment. ");
						}else if(name.equalsIgnoreCase(MetadataId.LEGAL_COST.getName())){
							email.setTemplateName("/demandnotes/Legal_INV_Email_To_Customer.vm");
							email.setSubject("Reg: Legal Cost Payment. ");
						}else if(name.equalsIgnoreCase(Status.PAID.getDescription())){
							email.setTemplateName("/demandnotes/Receipt_Email_To_Customers.vm");
							//email.setSubject("Reg: Your Payment is done successfully. ");
							email.setSubject("Thank you! We Received your Payment.");
						}
						mailServiceImpl.sendFinancialMailToCustomers(email);
						appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" Email sent successfully..."+" "+name);
					}
				}
				++count;
			} catch (Exception ex) {
				++count;
				String flatNo = "";
				bookingId = 0l;
				if(Util.isNotEmptyObject(resp.getFlatsResponse().get(0))) {
					bookingId = resp.getFlatsResponse().get(0).getFlatBookingId();
					flatNo = resp.getFlatsResponse().get(0).getFlatNo();
				}
				appendStrToFile(fileName,""+flatNo+" bookingId "+bookingId+" Email sent failed... "+name+" exception occoured" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
	private static List<String> testingFlats = Arrays.asList("NA-0001A","HO-0001A","EG-0001A","SU-0001A","AR-0001A");
	//@Async Async not working properly for emails, sending same content and for all emails
	public void sendFinancialApprovalEmail(List<EmployeeFinancialTransactionServiceInfo> list,
			String name, List<String> toEmails) throws Exception, Exception {
		log.info("***** Control inside the EmployeeFinancialHelper.sendFinancialApprovalEmail() *****"+name+" "+list.size());
		List<String> toMails = new ArrayList<>();
		//List<String> BccMails = new ArrayList<>();
		//List<String> toCC = new ArrayList<>();
		Email email = new Email();
		StringBuffer portalUrl = null;
		String requestUrl = "";
		long portNumber = 0l;
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		FinancialTransactionEmailInfo financialTransactionEmailInfo = new FinancialTransactionEmailInfo(); 
		List<FinancialTransactionDetailsInfo>  financialTransactionDetailsList =  new ArrayList<>();
		String transactionId = "";
		Properties prop= responceCodesUtil.getApplicationProperties();
		for (EmployeeFinancialTransactionServiceInfo transactionServiceInfo : list) {
			requestUrl = transactionServiceInfo.getRequestUrl()==null?"":transactionServiceInfo.getRequestUrl();
			if(transactionServiceInfo.getTransactionNo()!=null && transactionServiceInfo.getTransactionNo().equals(transactionId)) {
				continue;
			}
		
			String logoForPdf = "";
			String thanksAndRegardsFrom="";
			String greetingsFrom = "";
			if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
				logoForPdf = prop.getProperty("ASPIRE_LOGO1");
				thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty("ASPIRE_GREETING_MSG_FROM");
			} else {
				logoForPdf = prop.getProperty("SUMADHURA_LOGO1");
				thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");
			}
			
			transactionId = transactionServiceInfo.getTransactionNo();
			portNumber = transactionServiceInfo.getPortNumber();
			financialTransactionEmailInfo.setCurrentApprovalLevelName(transactionServiceInfo.getCurrentApprovalLevelName());
			financialTransactionEmailInfo.setNextApprovalLevelName(transactionServiceInfo.getNextApprovalLevelName());
			financialTransactionEmailInfo.setPaymentTowards(transactionServiceInfo.getTransactionTowards().toString().replace("[", "").replace("]", ""));
			//log.info(transactionServiceInfo.getCurrentApprovalLevelName()+" \t"+transactionServiceInfo.getNextApprovalLevelName()+" \t"+portNumber);	
			
			FinancialTransactionDetailsInfo transactionDetailsInfo = new FinancialTransactionDetailsInfo();
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
			transactionDetailsInfo.setTransactionTypeName(transactionServiceInfo.getTransactionTypeName());
			transactionDetailsInfo.setTransactionModeName(transactionServiceInfo.getTransactionModeName());
			transactionDetailsInfo.setTransferModeName(transactionServiceInfo.getTransferModeName());
			transactionDetailsInfo.setFinTransactionNo(transactionServiceInfo.getTransactionNo()==null?"N/A":transactionServiceInfo.getTransactionNo());
			transactionDetailsInfo.setComment(transactionServiceInfo.getComment()==null?"-":transactionServiceInfo.getComment());
			transactionDetailsInfo.setTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionServiceInfo.getTransactionAmount()).setScale(roundingModeSize, roundingMode).toString()));
			transactionDetailsInfo.setChequeNumber(transactionServiceInfo.getChequeNumber());
			
			if(Util.isNotEmptyObject(transactionServiceInfo.getComments())) {
				transactionDetailsInfo.setWaiverReason(transactionServiceInfo.getComments().get(0).get("INTEREST_WAIVER"));	
			}
			
			if(transactionServiceInfo.getChequeHandoverDate()!=null) {
				transactionDetailsInfo.setChequeHandoverDate(TimeUtil.timestampToDD_MM_YYYY(transactionServiceInfo.getChequeHandoverDate()));
			} else {
				transactionDetailsInfo.setChequeHandoverDate("-");
			}
			
			if(transactionServiceInfo.getChequeClearanceDate()!=null) {
				transactionDetailsInfo.setChequeClearanceDate(TimeUtil.timestampToDD_MM_YYYY(transactionServiceInfo.getChequeClearanceDate()));
			} else {
				transactionDetailsInfo.setChequeClearanceDate("-");
			}
			
			if(transactionServiceInfo.getTransactionDate()!=null) {
				transactionDetailsInfo.setTransactionDate(TimeUtil.timestampToDD_MM_YYYY(transactionServiceInfo.getTransactionDate()));
			} else {
				transactionDetailsInfo.setTransactionDate("-");
			}
			
			transactionDetailsInfo.setFlatId(customerPropertyDetailsInfo.getFlatId ());
			transactionDetailsInfo.setFlatNo(customerPropertyDetailsInfo.getFlatNo());
			transactionDetailsInfo.setCustomerName(customerPropertyDetailsInfo.getCustomerName());
			transactionDetailsInfo.setSiteName(customerPropertyDetailsInfo.getSiteName());
			transactionDetailsInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
			
			EmployeeFinancialTransactionResponse trnResp = new EmployeeFinancialTransactionResponse();
			trnResp.setOfficeDetailsList(transactionServiceInfo.getOfficeDetailsList());
			setOfficeDetails(trnResp,info);
			
			info.setRightSidelogoForPdf(logoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			
			financialTransactionDetailsList.add(transactionDetailsInfo);
			long nextEmpId = 0;
			if(testingFlats.contains(customerPropertyDetailsInfo.getFlatNo())) {
				//continue;
			}
			if(!requestUrl.equals(FinEnum.APPROVE_MULTIPLE_TRANSACTION.getName()) ) {
				//if this is approval of transaction
				List<FinTransactionApprovalDetailsPojo> listOfNextApprovalEmaEmails = transactionServiceInfo.getListOfNextApprovalEmaEmails();
				if(Util.isNotEmptyObject(listOfNextApprovalEmaEmails)) {
					for (FinTransactionApprovalDetailsPojo approvalDetailsPojo : listOfNextApprovalEmaEmails) {
						if(Util.isNotEmptyObject(approvalDetailsPojo.getEmpEmail()) && !toMails.contains(approvalDetailsPojo.getEmpEmail())) {
							toMails.add(approvalDetailsPojo.getEmpEmail());
							nextEmpId = approvalDetailsPojo.getEmpId();
						}
					}
				}
				//if this is rejection of transaction
				List<FinTransactionApprStatPojo> finTransactionApprStatPojoList = transactionServiceInfo.getFinTransactionApprStatPojoList();
				if(Util.isNotEmptyObject(finTransactionApprStatPojoList)) {
					for (FinTransactionApprStatPojo apprStatPojo : finTransactionApprStatPojoList) {
						if(Util.isNotEmptyObject(apprStatPojo.getEmpEmail()) && !toMails.contains(apprStatPojo.getEmpEmail())) {
							toMails.add(apprStatPojo.getEmpEmail());
						}
					}
				}
			} else if(Util.isNotEmptyObject(toEmails)) {
				toMails.addAll(toEmails);
			}
			
			if(Util.isEmptyObject(portalUrl)) {
				if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer(responceCodesUtil.getApplicationNamePropeties("UAT_PORTAL_URL"));
				}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("UAT_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("CUG_PORTAL_URL"));
				}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("CUG_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("LIVE_PORTAL_URL"));	
				}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("LIVE_PORTAL_URL"));	
				}
			}
			if(transactionServiceInfo.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
				String key = "SUAMSCUSTOMERAPP";
				String encryptedRequestUrl = AESEncryptDecrypt.encrypt("approveFromMail",AESEncryptDecrypt.convertKeyToHex(key));
				String transactionModeNameKey = AESEncryptDecrypt.encrypt("Interest Waiver",AESEncryptDecrypt.convertKeyToHex(key));
				//log.info(AESEncryptDecrypt.decrypt(encryptedRequestUrl,AESEncryptDecrypt.convertKeyToHex(key)));
				//log.info(AESEncryptDecrypt.decrypt(transactionModeNameKey,AESEncryptDecrypt.convertKeyToHex(key)));
				if(nextEmpId!=0) {
					portalUrl.append("#/login?Authentication_id=1&requestUrl="+encryptedRequestUrl+"&redirectUrl=approve-interest-waiver&transactionEntryId="+transactionServiceInfo.getTransactionEntryId()+"&bookingFormId="+transactionServiceInfo.getBookingFormId()+"&transactionModeName="+transactionModeNameKey+"&empId="+nextEmpId);
				}
			}
		}
		log.info(portalUrl);
		
		financialTransactionEmailInfo.setPortalUrl(portalUrl.toString());
		financialTransactionEmailInfo.setPortNumber(portNumber);
		financialTransactionEmailInfo.setFinancialTransactionDetailsList(financialTransactionDetailsList);
		email.setFinancialTransactionEmailInfo(financialTransactionEmailInfo);
		email.setDemandNoteGeneratorInfo(info);
		
		financialTransactionEmailInfo.setButtonType(name);
		
		if (name.equalsIgnoreCase(Status.CREATED.getDescription())) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Regarding Approval of Financial Transaction.");
		} else if (name.equalsIgnoreCase(Status.APPROVED.getDescription())) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Regarding Approval of Financial Transaction.");
		} else if (name.equalsIgnoreCase(Status.MODIFY.getDescription())) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Regarding Modification of Financial Transaction.");
		} else if (name.equalsIgnoreCase(Status.CHEQUE_BOUNCED.getDescription())) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Regarding Cheque Bounced of Financial Transaction.");
		} else if (name.equalsIgnoreCase(Status.REJECTED.getDescription()) || name.equalsIgnoreCase(Status.CHEQUE_BOUNCED.getDescription())) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Regarding Rejection of Financial Transaction.");
		} else if (name.equalsIgnoreCase(Status.TRANSACTION_COMPLETED.getDescription())) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Financial Transaction approved successfully.");
			
		} else if (name.equalsIgnoreCase("Transaction Payment Request")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Financial Payment refund Transaction request.");
			
		} else if (name.equalsIgnoreCase("Transaction Payment Request Approved")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Financial Payment refund Transaction Request Approved.");
			
		} else if (name.equalsIgnoreCase("Transaction Payment Refund")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Financial Payment refund Transaction Approval.");
			
		} else if (name.equalsIgnoreCase("Transaction Payment Refund Approved")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Financial Payment refund Transaction Appproved.");
			
		} else if (name.equalsIgnoreCase("Interest waiver initiated")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Interest waiver approval request.");
		
		} else if (name.equalsIgnoreCase("Interest waiver approved")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Interest waiver request approved.");
			
		}  else if (name.equalsIgnoreCase("Interest waiver rejected")) {
			email.setTemplateName("/demandnotes/approveFinancialTRN.vm");
			email.setSubject("Interest waiver request rejected.");
		}
		
		//BccMails.add("chaniket@amaravadhis.com");
		if(Util.isNotEmptyObject(toMails)) {
			email.setToMails(toMails.toArray(new String[] {}));
			//email.setBccs(BccMails.toArray(new String[] {}));
			mailServiceImpl.sendFinancialDemandNoteAndTransactionMailToEmployee(email);	
			log.info(" ToMails "+Arrays.toString(toMails.toArray(new String[] {}))+" "+portNumber);
		}
	}
	
	public void sendFinancialChequeBounceEmail(List<EmployeeFinancialTransactionServiceInfo> list,
			String name, List<String> toEmails) throws Exception {
		log.info("EmployeeFinancialHelper.sendFinancialChequeBounceEmail() ***** ");
		List<String> toMails = new ArrayList<>();
		//List<String> BccMails = new ArrayList<>();
		//List<String> toCC = new ArrayList<>();
		Email email = new Email();
		StringBuffer portalUrl = null;
		String requestUrl = "";
		long portNumber = 0l;
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		FinancialTransactionEmailInfo financialTransactionEmailInfo = new FinancialTransactionEmailInfo(); 
		List<FinancialTransactionDetailsInfo>  financialTransactionDetailsList =  new ArrayList<>();
		String transactionId = "";
		Properties prop= responceCodesUtil.getApplicationProperties();
		for (EmployeeFinancialTransactionServiceInfo transactionServiceInfo : list) {
			requestUrl = transactionServiceInfo.getRequestUrl()==null?"":transactionServiceInfo.getRequestUrl();
			if(transactionServiceInfo.getTransactionNo()!=null && transactionServiceInfo.getTransactionNo().equals(transactionId)) {
				continue;
			}
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
			String logoForPdf = "";
			String thanksAndRegardsFrom="";
			String greetingsFrom = "";
			if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
				logoForPdf = prop.getProperty("ASPIRE_LOGO1");
				thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty("ASPIRE_GREETING_MSG_FROM");
			} else {
				logoForPdf = prop.getProperty("SUMADHURA_LOGO1");
				thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");
			}
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());		

			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site, customerPropertyDetailsInfo);

			/*rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");*/
			logoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			/*rightSidelogoFilePath = logoAndOtherDetails.get("rightSidelogoFilePath");
			leftSidelogoFilePath = logoAndOtherDetails.get("leftSidelogoFilePath");*/

			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
			
			transactionId = transactionServiceInfo.getTransactionNo();
			portNumber = transactionServiceInfo.getPortNumber();
			financialTransactionEmailInfo.setCurrentApprovalLevelName(transactionServiceInfo.getCurrentApprovalLevelName());
			financialTransactionEmailInfo.setNextApprovalLevelName(transactionServiceInfo.getNextApprovalLevelName());
			financialTransactionEmailInfo.setPaymentTowards(transactionServiceInfo.getTransactionTowards().toString().replace("[", "").replace("]", ""));
			//log.info(transactionServiceInfo.getCurrentApprovalLevelName()+" \t"+transactionServiceInfo.getNextApprovalLevelName()+" \t"+portNumber);	
			
			FinancialTransactionDetailsInfo transactionDetailsInfo = new FinancialTransactionDetailsInfo();
			
			transactionDetailsInfo.setTransactionTypeName(transactionServiceInfo.getTransactionTypeName());
			transactionDetailsInfo.setTransactionModeName(transactionServiceInfo.getTransactionModeName());
			transactionDetailsInfo.setTransferModeName(transactionServiceInfo.getTransferModeName());
			transactionDetailsInfo.setFinTransactionNo(transactionServiceInfo.getTransactionNo()==null?"N/A":transactionServiceInfo.getTransactionNo());
			transactionDetailsInfo.setComment(transactionServiceInfo.getComment()==null?"-":transactionServiceInfo.getComment());
			transactionDetailsInfo.setTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionServiceInfo.getTransactionAmount()).setScale(roundingModeSize, roundingMode).toString()));
			transactionDetailsInfo.setChequeNumber(transactionServiceInfo.getChequeNumber());
						
			if(transactionServiceInfo.getChequeClearanceDate()!=null) {
				transactionDetailsInfo.setChequeClearanceDate(TimeUtil.timestampToDD_MM_YYYY(transactionServiceInfo.getChequeClearanceDate()));
			} else {
				transactionDetailsInfo.setChequeClearanceDate("-");
			}
			
			if(transactionServiceInfo.getBankName()!=null) {
				transactionDetailsInfo.setBankName(transactionServiceInfo.getBankName());
			} else {
				transactionDetailsInfo.setBankName("-");
			}
			
			if(transactionServiceInfo.getTransactionDate()!=null) {
				transactionDetailsInfo.setTransactionDate(TimeUtil.timestampToDD_MM_YYYY(transactionServiceInfo.getTransactionDate()));
			} else {
				transactionDetailsInfo.setTransactionDate("-");
			}
			
			transactionDetailsInfo.setFlatId(customerPropertyDetailsInfo.getFlatId ());
			transactionDetailsInfo.setFlatNo(customerPropertyDetailsInfo.getFlatNo());
			transactionDetailsInfo.setCustomerName(customerPropertyDetailsInfo.getCustomerName());
			transactionDetailsInfo.setSiteName(customerPropertyDetailsInfo.getSiteName());
			transactionDetailsInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
			
			EmployeeFinancialTransactionResponse trnResp = new EmployeeFinancialTransactionResponse();
			trnResp.setOfficeDetailsList(transactionServiceInfo.getOfficeDetailsList());
			//adding all the ofc detials to info object from trnResp object 
			setOfficeDetails(trnResp,info);
			
			info.setRightSidelogoForPdf(logoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			
			financialTransactionDetailsList.add(transactionDetailsInfo);
			
			//customer email
			toMails.add(customerPropertyDetailsInfo.getCustomerEmail());
			
			if(Util.isEmptyObject(portalUrl)) {
				if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer(responceCodesUtil.getApplicationNamePropeties("UAT_PORTAL_URL"));
				}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("UAT_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("CUG_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("CUG_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("LIVE_PORTAL_URL"));	
				}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("LIVE_PORTAL_URL"));	
				}
			}

			log.info(portalUrl);
			
			financialTransactionEmailInfo.setPortalUrl(portalUrl.toString());
			financialTransactionEmailInfo.setPortNumber(portNumber);
			financialTransactionEmailInfo.setFinancialTransactionDetailsList(financialTransactionDetailsList);
			email.setFinancialTransactionEmailInfo(financialTransactionEmailInfo);
			email.setDemandNoteGeneratorInfo(info);
			
			financialTransactionEmailInfo.setButtonType(name);
			
			if (name.equalsIgnoreCase(Status.CHEQUE_BOUNCED.getDescription())) {
				email.setTemplateName("/demandnotes/ChequeBounce.vm");
				email.setSubject(customerPropertyDetailsInfo.getSiteName()+"-"+customerPropertyDetailsInfo.getFlatNo()+"-Alert For Cheque Bounce! ");
			}
			
			//BccMails.add("chaniket@amaravadhis.com");
			if(Util.isNotEmptyObject(toMails)) {
				email.setToMails(toMails.toArray(new String[] {}));
				//email.setBccs(BccMails.toArray(new String[] {}));
				mailServiceImpl.sendFinancialMailToCustomers(email);	
				log.info(" ToMails "+Arrays.toString(toMails.toArray(new String[] {}))+" "+portNumber);
			}
			
		}	
	}
	
	@Async
	public void approvalModificationInoviceEmailHelper(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			List<ModicationInvoiceAppRej> nextApprovalDetailsList ,String condition) throws Exception, Exception {
		for (ModicationInvoiceAppRej modicationInvoiceAppRej2 : nextApprovalDetailsList) {
			transactionServiceInfo.setModificationNextApprovalDetailsList(Arrays.asList(modicationInvoiceAppRej2));
			transactionServiceInfo.setNextApprovalLevelName(modicationInvoiceAppRej2.getEmpName());
			transactionServiceInfo.setModificationApproveRejectDetailsList(null);
			sendModificationInoviceApprovalEmail(Arrays.asList(transactionServiceInfo), condition , null);
		}
	}
	
	@Async
	public void rejectModificationInoviceEmailHelper(List<EmployeeFinancialTransactionServiceInfo> list,
			String condition, List<String> toEmails) throws Exception {
		//used async so no need to wait for response
		sendModificationInoviceApprovalEmail(list, condition , toEmails);
	}
	
	public void sendModificationInoviceApprovalEmail(List<EmployeeFinancialTransactionServiceInfo> list,
			String name, List<String> toEmails) throws Exception, Exception {
		log.info("***** Control inside the EmployeeFinancialHelper.sendFinancialApprovalEmail() *****"+name+" "+list.size());
		List<String> toMails = new ArrayList<>();
		//List<String> BccMails = new ArrayList<>();
		//List<String> toCC = new ArrayList<>();
		Email email = new Email();
		StringBuffer portalUrl = null;
		String requestUrl = "";
		long portNumber = 0l;
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		FinancialTransactionEmailInfo financialTransactionEmailInfo = new FinancialTransactionEmailInfo(); 
		List<FinancialTransactionDetailsInfo>  financialTransactionDetailsList =  new ArrayList<>();
		Thread.sleep(2000);
		Properties prop= responceCodesUtil.getApplicationProperties();
		for (EmployeeFinancialTransactionServiceInfo transactionServiceInfo : list) {
			requestUrl = transactionServiceInfo.getRequestUrl()==null?"":transactionServiceInfo.getRequestUrl();
			
			String logoForPdf = "";
			String thanksAndRegardsFrom="";
			String greetingsFrom = "";
			if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
				logoForPdf = prop.getProperty("ASPIRE_LOGO1");
				thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty("ASPIRE_GREETING_MSG_FROM");
			} else {
				logoForPdf = prop.getProperty("SUMADHURA_LOGO1");
				thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");
			}
			
			portNumber = transactionServiceInfo.getPortNumber();
			financialTransactionEmailInfo.setCurrentApprovalLevelName(transactionServiceInfo.getCurrentApprovalLevelName());
			financialTransactionEmailInfo.setNextApprovalLevelName(transactionServiceInfo.getNextApprovalLevelName());
			
			FinancialTransactionDetailsInfo transactionDetailsInfo = new FinancialTransactionDetailsInfo();
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
			transactionDetailsInfo.setComment(transactionServiceInfo.getComment()==null?"-":transactionServiceInfo.getComment());
			transactionDetailsInfo.setTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionServiceInfo.getTransactionAmount()).setScale(roundingModeSize, roundingMode).toString()));
			
		 	transactionDetailsInfo.setFlatId(customerPropertyDetailsInfo.getFlatId ());
			transactionDetailsInfo.setFlatNo(customerPropertyDetailsInfo.getFlatNo());
			transactionDetailsInfo.setCustomerName(customerPropertyDetailsInfo.getCustomerName());
			transactionDetailsInfo.setSiteName(customerPropertyDetailsInfo.getSiteName());
			transactionDetailsInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
			
			EmployeeFinancialTransactionResponse trnResp = new EmployeeFinancialTransactionResponse();
			trnResp.setOfficeDetailsList(transactionServiceInfo.getOfficeDetailsList());
			info.setFlatSaleOwnerIdBasedOnAccountId(customerPropertyDetailsInfo.getFlatSaleOwnerIdBasedOnAccountId());
			info.setFlatSaleOwnerNameBasedOnAccountId(customerPropertyDetailsInfo.getFlatSaleOwnerNameBasedOnAccountId());
			setOfficeDetails(trnResp,info);
			
			info.setRightSidelogoForPdf(logoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			
			financialTransactionDetailsList.add(transactionDetailsInfo);
			long nextEmpId = 0;
			if(!requestUrl.equals(FinEnum.APPROVE_MULTIPLE_TRANSACTION.getName()) ) {
				//if this is approval of transaction
				List<ModicationInvoiceAppRej> listOfNextApprovalEmaEmails = transactionServiceInfo.getModificationNextApprovalDetailsList();
				if(Util.isNotEmptyObject(listOfNextApprovalEmaEmails)) {
					for (ModicationInvoiceAppRej approvalDetailsPojo : listOfNextApprovalEmaEmails) {
						if(Util.isNotEmptyObject(approvalDetailsPojo.getEmpEmail()) && !toMails.contains(approvalDetailsPojo.getEmpEmail())) {
							toMails.add(approvalDetailsPojo.getEmpEmail());
						}
					}
				}
				//if this is rejection of transaction
				List<ModicationInvoiceAppRej> finTransactionApprStatPojoList = transactionServiceInfo.getModificationApproveRejectDetailsList();
				if(Util.isNotEmptyObject(finTransactionApprStatPojoList)) {
					for (ModicationInvoiceAppRej apprStatPojo : finTransactionApprStatPojoList) {
						if(Util.isNotEmptyObject(apprStatPojo.getEmpEmail()) && !toMails.contains(apprStatPojo.getEmpEmail())) {
							toMails.add(apprStatPojo.getEmpEmail());
						}
					}
				}
			} else if(Util.isNotEmptyObject(toEmails)) {
				toMails.addAll(toEmails);
			}
			
			if(Util.isEmptyObject(portalUrl)) {
				if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer(responceCodesUtil.getApplicationNamePropeties("UAT_PORTAL_URL"));
				}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("UAT_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("CUG_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("CUG_PORTAL_URL"));
				} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("LIVE_PORTAL_URL"));	
				}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
					portalUrl = new StringBuffer( responceCodesUtil.getApplicationNamePropeties("LIVE_PORTAL_URL"));	
				}
			}
			if(transactionServiceInfo.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
				String key = "SUAMSCUSTOMERAPP";
				String encryptedRequestUrl = AESEncryptDecrypt.encrypt("approveFromMail",AESEncryptDecrypt.convertKeyToHex(key));
				String transactionModeNameKey = AESEncryptDecrypt.encrypt("Interest Waiver",AESEncryptDecrypt.convertKeyToHex(key));
				//log.info(AESEncryptDecrypt.decrypt(encryptedRequestUrl,AESEncryptDecrypt.convertKeyToHex(key)));
				//log.info(AESEncryptDecrypt.decrypt(transactionModeNameKey,AESEncryptDecrypt.convertKeyToHex(key)));
				portalUrl.append("#/login?Authentication_id=1&requestUrl="+encryptedRequestUrl+"&redirectUrl=approve-interest-waiver&transactionEntryId="+transactionServiceInfo.getTransactionEntryId()+"&bookingFormId="+transactionServiceInfo.getBookingFormId()+"&transactionModeName="+transactionModeNameKey+"&empId="+nextEmpId);
			}
		}
		log.info(portalUrl);
		
		financialTransactionEmailInfo.setPortalUrl(portalUrl.toString());
		financialTransactionEmailInfo.setPortNumber(portNumber);
		financialTransactionEmailInfo.setFinancialTransactionDetailsList(financialTransactionDetailsList);
		email.setFinancialTransactionEmailInfo(financialTransactionEmailInfo);
		email.setDemandNoteGeneratorInfo(info);
		
		financialTransactionEmailInfo.setButtonType(name);
		
		if (name.equalsIgnoreCase("Modification invoice created")) {
			email.setTemplateName("/demandnotes/approveModificationInvoice.vm");
			email.setSubject("Regarding Approval of Modification Invoice.");
		} else if (name.equalsIgnoreCase("Modification invoice approved")) {
			email.setTemplateName("/demandnotes/approveModificationInvoice.vm");
			email.setSubject("Regarding Approval of Modification Invoice.");
		} else if (name.equalsIgnoreCase(Status.MODIFY.getDescription())) {
			email.setTemplateName("/demandnotes/approveModificationInvoice.vm");
			email.setSubject("Regarding Approval of Modify Modification Invoice.");
		}  else if (name.equalsIgnoreCase("Modification invoice rejected") ) {
			email.setTemplateName("/demandnotes/approveModificationInvoice.vm");
			email.setSubject("Regarding Rejection of Modification Transaction.");
		} 
		
		//BccMails.add("chaniket@amaravadhis.com");
		if(Util.isNotEmptyObject(toMails)) {
			email.setToMails(toMails.toArray(new String[] {}));
			//email.setBccs(BccMails.toArray(new String[] {}));
			mailServiceImpl.sendFinancialDemandNoteAndTransactionMailToEmployee(email);	
			log.info(" ToMails "+Arrays.toString(toMails.toArray(new String[] {}))+" "+portNumber);
		}
	}


	public FileInfo generateZipFile(List<FileInfo> fileInfoList,EmployeeFinancialServiceInfo serviceInfo) throws FileNotFoundException, IOException, Exception {
		long portNumber = serviceInfo.getPortNumber();
		String demandNotePdfFilePath = "";
		String demandNotePdfFileUrl = "";
		boolean isGenerateTaxPdf = false;
		//int countFilesUploadedInZip = 0;
		log.info(" ***** EmployeeFinancialServiceImpl.generateZipFile() ***** "+portNumber);
		FileInfo zipFileInfo = new FileInfo();
		String zipFileName = "";
		/* Saving Interest Letter Files in Separate Folder */
		if("Interest_Letter".equalsIgnoreCase(serviceInfo.getActionUrl())) {
			zipFileName =  " InterestLetterPdfFiles.zip";
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_URL");
			} else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			}
		} else if ("FinancialReceiptUploadData".equalsIgnoreCase(serviceInfo.getActionUrl())) {
			zipFileName =  " TaxInvoiceUploadedFiles.zip";
			isGenerateTaxPdf = true;
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_URL");
			}  else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_URL");
			} else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			}
		} else {
			zipFileName =  " DemandNotePdfFiles.zip";
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
			}else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			}
		}
		
		/* Generating Zip File Name with Current Timestamp */
		zipFileName = pdfHelper.generateFileName(zipFileName);
		StringBuilder filePath = null;
		StringBuilder fileUrl = null;
		if("FinancialReceiptUploadData".equalsIgnoreCase(serviceInfo.getActionUrl())) {
			filePath = new StringBuilder(demandNotePdfFilePath)
					.append("").append("TaxInvoiceUploadedFiles");
			
			fileUrl = new StringBuilder(demandNotePdfFileUrl)
					.append("").append("TaxInvoiceUploadedFiles");
		} else {
			filePath = new StringBuilder(demandNotePdfFilePath)
					.append("/").append(serviceInfo.getSiteIds().get(0)+"/DemandNotePreviewFiles");
					/*.append(!requestFrom.equalsIgnoreCase("GenerateDemandNote")?"":"\\"+demandNoteGeneratorInfo.getFlatBookingId())
					.append(!requestFrom.equalsIgnoreCase("GenerateDemandNote")?"":"\\"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());*/
			
			fileUrl = new StringBuilder(demandNotePdfFileUrl)
					.append("/").append(serviceInfo.getSiteIds().get(0)+"/DemandNotePreviewFiles");
					/*.append(!requestFrom.equalsIgnoreCase("GenerateDemandNote")?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
					.append(!requestFrom.equalsIgnoreCase("GenerateDemandNote")?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());*/
		}
		String fileName = pdfHelper.getFileName(filePath.toString(), zipFileName);
		try {
			//File file = new File(filePath.toString(), fileName);if (!file.exists()) {file.createNewFile();} 
		}catch(Exception e) {
			//System.out.println(e.getMessage());
		}
		StringBuilder ZIPFileNameAndPath = new StringBuilder(filePath).append("/").append(fileName);
		
		zipFileInfo.setUrl(fileUrl.append("/").append(fileName).toString());
		zipFileInfo.setFilePath(ZIPFileNameAndPath.toString());
		//System.out.println("EmployeeFinancialHelper.generateZipFile() "+ zipFileInfo.getUrl()+" \t"+zipFileInfo.getFilePath());
		
		FileOutputStream out = new FileOutputStream(ZIPFileNameAndPath.toString());
		ZipOutputStream zos = new ZipOutputStream(out);
		//CountDownLatch latch = new CountDownLatch(1);
		//for (FileInfo fileInfo : fileInfoList) {
		for (int index = 0; index <  fileInfoList.size();index++) {
			FileInfo fileInfo = fileInfoList.get(index);
			String fileName1 = fileInfo.getFilePath();
			File file = new File(fileName1);
			//System.out.println(fileInfo.getFilePath()+" \nfile.length() "+file.length()+" index "+index);
			long fileLength = file.length();
			if(isGenerateTaxPdf) {
				if(fileLength<10 && file.exists()) {
					index = index-1;
					//System.out.println(fileInfo.getFilePath()+" \nfile.length() "+file.length()+" index "+index);
					//Thread thisThread = Thread.currentThread();
					Thread.sleep(3000);
					continue;
				}
			}
			
			InputStream in = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(in);
			zos.putNextEntry(new ZipEntry(file.getName()));
			// Get the file
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException fnfe) {
				// If the file does not exists, write an error entry instead of file contents
				zos.write(("ERRORld not find file " + file.getName()).getBytes());
				zos.closeEntry();
				// //System.out.println("Couldfind file " + file.getAbsolutePath());
				continue;
			}
			BufferedInputStream fif = new BufferedInputStream(fis);
			// Write the contents of the file
			int data = 0;
			while ((data = fif.read()) != -1) {
				zos.write(data);
			}

			fif.close();
			fis.close();
			in.close();
			bis.close();

			file.delete();
			//log.info("File Deleted : " +flag+" \t: "+file.getAbsolutePath());
			zos.closeEntry(); 
			try {
				if (file.exists())
					file.delete();
			} catch (Exception e) {

			}
			// //System.out.println("Finishedng file " + file.getName() + " and deleted " +
			// flag);
		} // for loop

		zos.close();
		out.close();
		return zipFileInfo;
	}

	/**
	 * Used common method for legal, modification,corpus fund, flat khata,maintenance charges
	 * @param resp
	 * @param fileInfo
	 * @param transactionServiceInfo
	 * @param metaDataName
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws EmployeeFinancialServiceException
	 * @throws Exception
	 */
	public void receiptGenaratorHelperForModificationLegalCost(EmployeeFinancialTransactionResponse resp, FileInfo fileInfo, 
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, String metaDataName) throws FileNotFoundException, IOException,
			DocumentException, EmployeeFinancialServiceException, Exception {
		log.info(" ***** control is inside the receiptGenaratorHelperForModificationLegalCost in EmployeeFinancialHelper ***** ");
		
		
		if(FinTransactionMode.WAIVED_OFF.getId().equals(transactionServiceInfo.getTransactionModeId())) {
	       return;
		}
		String NOTIFICATION_TITLE = "Sumadhura Modification Invoice";
		String NOTIFICATION_BODY = "Dear Customer your modification invoice has been generated!.";
		String NOTIFICATION_TYPE = "Sumadhura Modification Invoice";
		String NOTIFICATION_TYPE1 = "Sumadhura Modification Invoice";
		CustomerPropertyDetailsInfo	customerPropertyDetailsInfo = null;
		List<MileStoneInfo> mileStoneInfoList = new ArrayList<>();
		Email email = new Email();
		boolean isUploadedData = transactionServiceInfo.isThisUplaodedData();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String rightSidelogoFilePath = "";
		String leftSidelogoFilePath = "";

		String thanksAndRegardsFrom="";
		if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
			//rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_LOGO1");	
			//thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
			
	     	if(metaDataName.equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Modification Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Modification Receipt";
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.LEGAL_COST.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Legal Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Legal Receipt";
	    	/* Malladi Changes */
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.FIN_PENALTY.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Interest Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Interest Receipt";
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.CORPUS_FUND.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Corpus Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Corpus Receipt";
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Flat Khata Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Flat Khata Receipt";
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.MAINTENANCE_CHARGE.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Maintenance Charge Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Maintenance Charge Receipt";
	     	}
		} else {
			//rightSidelogoForPdf = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1");
			//thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			
	     	if(metaDataName.equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		//NOTIFICATION_BODY = "Dear Customer your modification invoice has been generated!.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Modification Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Modification Receipt";
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.LEGAL_COST.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Legal Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Legal Receipt";
	    	/* Malladi Changes */	
	     	}else if(metaDataName.equalsIgnoreCase(MetadataId.FIN_PENALTY.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Interest Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Interest Receipt";
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.CORPUS_FUND.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Corpus Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Corpus Receipt";
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Flat Khata Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Flat Khata Receipt";
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.MAINTENANCE_CHARGE.getName())) {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Maintenance Charge Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Maintenance Charge Receipt";
	     	} else {
	     		NOTIFICATION_TITLE = "Thank you! We Received your Payment.";
	    		NOTIFICATION_BODY = "We Thank you for the Payment. The payment receipt is uploaded in the receipts tab in your App for your kind perusal.";
	    		NOTIFICATION_TYPE = "Sumadhura Maintenance Charge Receipt";
	    		NOTIFICATION_TYPE1 = "Sumadhura Maintenance Charge Receipt";
	     	}
		}
		
		Site site = new Site();
		site.setSiteId(transactionServiceInfo.getSiteId());
		site.setName(transactionServiceInfo.getSiteName());		


		/*bvr 1*/
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 =transactionServiceInfo.getCustomerPropertyDetailsInfo();
		customerPropertyDetailsInfo2.setFlatSaleOwnerIdBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerIdBasedOnAccountId());
		customerPropertyDetailsInfo2.setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerNameBasedOnAccountId());
		customerPropertyDetailsInfo2.setRequestFrom("approveFinancialMultipleTransaction");
		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo2);			
		
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		rightSidelogoFilePath = logoAndOtherDetails.get("rightSidelogoFilePath");
		leftSidelogoFilePath = logoAndOtherDetails.get("leftSidelogoFilePath");

		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		//greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);

		if(Util.isNotEmptyObject(rightSidelogoFilePath)) {//checking file is present in local folder, if exists loading file from local folder
			if(isFilePathFoundPdf.get(rightSidelogoFilePath)!=null && isFilePathFoundPdf.get(rightSidelogoFilePath).equals("true")) {
				info.setRightSidelogoFilePath(rightSidelogoFilePath);
			} else {
				File file = new File(rightSidelogoFilePath);
				if(file.exists()) {//checking file is exists or not, if exist the loading this file in pdf
					isFilePathFoundPdf.put(rightSidelogoFilePath, "true");
					info.setRightSidelogoFilePath(rightSidelogoFilePath);
				}
			}
		}
		
		if(Util.isNotEmptyObject(leftSidelogoFilePath)) {
			if(isFilePathFoundPdf.get(leftSidelogoFilePath)!=null && isFilePathFoundPdf.get(leftSidelogoFilePath).equals("true")) {
				info.setLeftSidelogoFilePath(leftSidelogoFilePath);
			} else {
				File file = new File(leftSidelogoFilePath);
				if(file.exists()) {
					isFilePathFoundPdf.put(leftSidelogoFilePath, "true");
					info.setLeftSidelogoFilePath(leftSidelogoFilePath);
				}
			}
		}

		
		if(isUploadedData) {
			/* setting demandNote Generated Date */
			if(Util.isNotEmptyObject(resp.getTransactionReceiveDate())) {
				info.setTransactionReceiptDate(TimeUtil.timestampToDD_MM_YYYY(resp.getTransactionReceiveDate()));
			}else {
				info.setTransactionReceiptDate("N/A");
			}
		} else {
			info.setTransactionReceiptDate(TimeUtil.timestampToDD_MM_YYYY(new Timestamp(new Date().getTime())));
		}
		
		/* setting demandNote Generated Date */
		if(Util.isNotEmptyObject(resp.getTransactionReceiveDate())) {
			info.setTransactionReceiveDate(TimeUtil.timestampToDD_MM_YYYY(resp.getTransactionReceiveDate()));
		}else {
			info.setTransactionReceiveDate("N/A");
		}
		
		if (Util.isNotEmptyObject(resp.getTransactionReceiptNo())) {
			info.setTransactionReceiptNo(resp.getTransactionReceiptNo());
		} else {
			info.setTransactionReceiptNo("N/A");
		}
		
		if (Util.isNotEmptyObject(resp.getBankName())) {
			info.setBankName(resp.getBankName());
			if(resp.getBankName().equals("N/A")) {
				info.setBankName("-");	
			}
		} else {
			info.setBankName("-");
		}

		if (Util.isNotEmptyObject(resp.getTransactionDate())) {
			info.setTransactionDate(TimeUtil.timestampToDD_MM_YYYY(resp.getTransactionDate()));
		} else {
			info.setTransactionDate("N/A");
		}
		
		if (Util.isNotEmptyObject(resp.getBookingFormId())) {
			info.setBookingFormId(resp.getBookingFormId());
		}
		
		if (Util.isNotEmptyObject(resp.getIsShowGstInPDF())) {
			if (resp.getIsShowGstInPDF() == true) {
				info.setIsShowGstInPDF(Boolean.TRUE);
			} else {
				info.setIsShowGstInPDF(Boolean.FALSE);
			}
		} else {
			info.setIsShowGstInPDF(Boolean.FALSE);
		}
		
		if (Util.isNotEmptyObject(resp.getTransactionEntryId())) {
			info.setTransactionEntryId(resp.getTransactionEntryId());
		}
		
		if(Util.isNotEmptyObject(resp.getTransactionModeName())) {
			info.setTransactionModeName(resp.getTransactionModeName());
			if(resp.getTransactionModeName().equals(MetadataId.CHEQUE.getName())) {
				if(Util.isNotEmptyObject(resp.getChequeNumber())) {
					info.setChequeNumber(resp.getChequeNumber());
				}
			}else if(resp.getTransactionModeName().equals(MetadataId.ONLINE.getName())) {
				if(Util.isNotEmptyObject(resp.getReferenceNo())) {
					info.setReferenceNo(resp.getReferenceNo());
				}
				if(Util.isNotEmptyObject(resp.getTransferModeName())) {
					info.setTransferModeName(resp.getTransferModeName());
				}
			}
		}		
		
//		if(Util.isNotEmptyObject(resp.getTotalReceiptAmount())) { info.setTotalCgstAmount(resp.getTotalReceiptAmount());
//		}else { info.setTotalReceiptAmount(0.0); }
		
		/* setting applicant and coApplicant names and mobile numbers */
		if(Util.isNotEmptyObject(resp.getFlatsResponse())) {
			StringBuilder name = new StringBuilder(); 
			StringBuilder mobileNos = new StringBuilder();
			StringBuilder flatNo = new StringBuilder();
			StringBuilder floorName = new StringBuilder();
			StringBuilder blockName = new StringBuilder();
			StringBuilder siteName = new StringBuilder();
			StringBuilder pancard = new StringBuilder();
			
			List<CustomerPropertyDetailsInfo> detailsList  = resp.getFlatsResponse();

			for (int index = 0; index <  detailsList.size(); index++) {
				customerPropertyDetailsInfo = detailsList.get(index);
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName()) || Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())) {
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName())){	
					name.append(customerPropertyDetailsInfo.getCustomerName());
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())){
						name.append(" AND ");
						name.append(customerPropertyDetailsInfo.getCoAppFullName());
					}
				}else {
					name.append("N/A");
				}
				}else {
					name.append("N/A");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber()) ||  Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())) {
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
						mobileNos.append(customerPropertyDetailsInfo.getContactNumber());
					}
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())){
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
							mobileNos.append(",");
						}
						mobileNos.append(customerPropertyDetailsInfo.getAlternatePhoneNo());
					}
				}else {
					mobileNos.append("N/A");
				}
				
				/* setting flat Number */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
					flatNo.append(customerPropertyDetailsInfo.getFlatNo());
				}else {
					flatNo.append("N/A");
				}
				
				/* setting Floor Name   */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFloorName())) {
					floorName.append(customerPropertyDetailsInfo.getFloorName());
				}else {
					floorName.append("N/A");
				}
				
				/* setting Block name */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBlockName())) {
					blockName.append(customerPropertyDetailsInfo.getBlockName());
				}else {
					blockName.append("N/A");
				}
				
				/* setting Site Name */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteName())) {
					siteName.append(customerPropertyDetailsInfo.getSiteName());
				}else {
					siteName.append("N/A");
				}
				
				/* setting customer pancard  */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getPancard())) {
					pancard.append(customerPropertyDetailsInfo.getPancard());
				}else {
					pancard.append("N/A");
				}
				
				/* setting siteId */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteId())) {
					info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
				}else {
					info.setSiteId("Anonymous_Site");
				}
				
				/* setting flatBookingId */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatBookingId())) {
					info.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId().toString());
					info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
				}else {
					info.setFlatBookingId("Anonymous_Flat_Booking");
				}
				
			}
			info.setCustomerNames(name.toString());
			info.setMobileNumbers(mobileNos.toString());
			info.setFlatName(flatNo.toString());
			info.setFloorName(floorName.toString());
			info.setBlockName(blockName.toString());
			//info.setSiteName(siteName.toString());
			if(info.getSiteId()!=null && info.getSiteId().equals("131")) {
				info.setSiteName("Aspire Aurum");
			}else {
				info.setSiteName(siteName.toString());
			}
			info.setPancard(pancard.toString());
		}
		
		/* setting applicant permenent address  */
		if(Util.isNotEmptyObject(resp.getCustomerAddressInfoList())) {
			for(AddressInfo addressInfo : resp.getCustomerAddressInfoList()){
			/* Taking correspondence Address  */	
             if(Util.isNotEmptyObject(addressInfo)) {
            	 if(addressInfo.getAddressMappingType().getAddressType().equalsIgnoreCase("Correspondence")){
            		 if(Util.isNotEmptyObject(addressInfo.getAddress1())) {
            		    info.setAddress(addressInfo.getAddress1());
            		    if(Util.isNotEmptyObject(addressInfo.getPincode())) {
            		    	 //info.setAddress(info.getAddress()+"-"+addressInfo.getPincode());
            		    	 break;
            		    }
            		 }else {
            			  info.setAddress("N/A"); 
            		 }
            	 }else {
            		 info.setAddress("N/A");  
            	 }
             }
		  }
		}else {
		   info.setAddress("N/A");
		}
		
		getSiteAdderss(resp.getSiteAddressInfoList(),info,customerPropertyDetailsInfo);
		/* setting site address and survey number */
		/*if(Util.isNotEmptyObject(resp.getSiteAddressInfoList())) {
			StringBuilder siteAddress = new StringBuilder();
			for(AddressInfo addressInfo : resp.getSiteAddressInfoList()){
				
				  setting survey number 
				if(Util.isNotEmptyObject(addressInfo.getSurveyNo())) {
					info.setSurveyNo(addressInfo.getSurveyNo());
				}else {
					info.setSurveyNo("N/A");
				}
				if(Util.isNotEmptyObject(addressInfo.getStreet())) {
					siteAddress.append(addressInfo.getStreet());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getArea())) {
					siteAddress.append(addressInfo.getArea());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getDistrict())) {
					siteAddress.append(addressInfo.getDistrict());
					siteAddress.append(",");
				}
				if(Util.isNotEmptyObject(addressInfo.getCity())) {
					siteAddress.append(addressInfo.getCity());
					siteAddress.append("");
				}
				if(Util.isNotEmptyObject(addressInfo.getPincode())) {
					siteAddress.append("-");
					siteAddress.append(addressInfo.getPincode());
				}
			}
			info.setSiteAddress(siteAddress.toString());
		}*/
		
		/* setting milestone amount.*/
		/*  settimg milestone amount in words */
		if(Util.isNotEmptyObject(resp.getTotalReceiptAmount())) {
			String amountInWords = getTheAmountInWords(resp.getTotalReceiptAmount());
			info.setTotalReceiptAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalReceiptAmount()).setScale(roundingModeSize, roundingMode).toString()));
			info.setMileStoneAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalReceiptAmount()).setScale(roundingModeSize, roundingMode).toString()));
			//info.setMileStoneAmountInWords(new NumberToWord().convertNumberToWords(BigDecimal.valueOf(resp.getTotalReceiptAmount()).setScale(roundingModeSize, roundingMode).longValue()) +" Rupees Only.");
			info.setMileStoneAmountInWords(amountInWords);
		}else {
			info.setMileStoneAmount("0.00");
			info.setMileStoneAmountInWords("Zero Rupees Only ");
		}
		
		if (Util.isNotEmptyObject(resp.getTotalReceiptPaidAmount())) {
			info.setTotalReceiptPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalReceiptPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));
			String amountInWords = getTheAmountInWords(resp.getTotalReceiptPaidAmount());
			info.setTotalReceiptPaidAmountInWords(amountInWords);
		} else {
			info.setTotalReceiptPaidAmount("0.00");
			info.setTotalReceiptPaidAmountInWords("Zero Rupees Only ");
		}
		
		if(Util.isNotEmptyObject(resp.getTotalGSTAmount())) { 
			info.setTotalCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalGSTAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else { info.setTotalCgstAmount("0.00"); }

		
		if(Util.isNotEmptyObject(resp.getTotalGSTAmount())) { 
			info.setTotalCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalGSTAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
		}else { info.setTotalCgstAmount("0.00"); }

		if(Util.isNotEmptyObject(resp.getTotalGSTAmount())) { 
			info.setTotalSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(resp.getTotalGSTAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
		}else { info.setTotalSgstAmount("0.00"); }

		/* setting Demand Note PDF File Name */
		if (Util.isNotEmptyObject(resp.getTransactionPdfFileName())) {
			info.setDemandNotePdfFileName(resp.getTransactionPdfFileName());
		} else {
			info.setDemandNotePdfFileName("receiptForPaymentDone.pdf");
		}
	 
		/* setting site Account  */
     	if(Util.isNotEmptyObject(resp.getFinProjectAccountResponseList())) {
     		StringBuilder accountInfo = new StringBuilder();
     		for(FinProjectAccountResponse accountResponse : resp.getFinProjectAccountResponseList()) {
     			/* setting bank details  */
     			if(Util.isNotEmptyObject(accountResponse.getBankName())){
     				accountInfo.append(accountResponse.getBankName());
     				//accountInfo.append(",");
     			}
     			/* setting bank account details  */
//     			if(Util.isNotEmptyObject(accountResponse.getAccountNo())){
//     				accountInfo.append("<strong>"+accountResponse.getAccountNo()+"</strong>");
//     				accountInfo.append(",");
//     			}
     			
     			/* setting Account Address */
//     			if(Util.isNotEmptyObject(accountResponse.getAccountAddress())){
//     				accountInfo.append(accountResponse.getAccountAddress());
//     				accountInfo.append(",");
//     			}
     			
     			/* setting Account IFSC Code */
//     			if(Util.isNotEmptyObject(accountResponse.getIfscCode())){
//     				accountInfo.append(" IFSC CODE - ");
//     				accountInfo.append(accountResponse.getIfscCode());
//     			}
     	    }
     		info.setSiteAccount(accountInfo.toString());
     	}
     	/* for Modification Cost, Legal Cost and also Milestone Receipt */
     	List<FinancialProjectMileStoneResponse> responseTypeList = new ArrayList<>();
     	if(metaDataName.equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())) {
     		responseTypeList = resp.getModificationCostResponseList();
     	} else if(metaDataName.equalsIgnoreCase(MetadataId.LEGAL_COST.getName())) {
     		responseTypeList = resp.getLegalCostResponseList();
     	} else if(metaDataName.equalsIgnoreCase(MetadataId.FIN_PENALTY.getName())) {
         	/* Malladi Changes */
     		responseTypeList = resp.getInterestCostResponseList();
     		email.setTitle("INTEREST CHARGES RECEIPT");
     	} else if(metaDataName.equalsIgnoreCase(MetadataId.MAINTENANCE_CHARGE.getName())) {
     		responseTypeList = resp.getMaintenanceCostResponseList();
     		email.setTitle("MAINTENANCE CHARGES RECEIPT");
     	} else if(metaDataName.equalsIgnoreCase(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getName())) {
     		responseTypeList = resp.getFlatKhataResponseList();
     		email.setTitle("FLAT KHATA RECEIPT");
     	} else if(metaDataName.equalsIgnoreCase(MetadataId.CORPUS_FUND.getName())) {
     		responseTypeList = resp.getCorpusFundsResponseList();
     		email.setTitle("CORPUS FUND RECEIPT");
     	}

     	//int count = 0;
     	int serialNumber = 1;
     	for(FinancialProjectMileStoneResponse response : responseTypeList) {
     		MileStoneInfo mileStoneInfo = new MileStoneInfo();
     		
     		/* setting Milestone Number  */
      			mileStoneInfo.setProjectMilestoneId(String.valueOf(serialNumber));
     			serialNumber++;
     			
     		/* setting Milestone Name */
     		if(Util.isNotEmptyObject(response.getMilestoneName())) {
     			mileStoneInfo.setMilestoneName(response.getMilestoneName());
     		}else {
     			mileStoneInfo.setMilestoneName("N/A");
     		}
     		
     		/* setting Due amount excluding GST */
     		if(Util.isNotEmptyObject(response.getPaidAmount()) && Util.isNotEmptyObject(response.getGstAmount())) {
     			//if(Util.isNotEmptyObject(response.getPaidAmount()) && Util.isNotEmptyObject(response.getGstAmount())) {	
     			mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getPaidAmount()-response.getGstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else if(Util.isNotEmptyObject(response.getPaidAmount()) && Util.isEmptyObject(response.getGstAmount())) {
     			//if(Util.isNotEmptyObject(response.getPaidAmount()) && Util.isNotEmptyObject(response.getGstAmount())) {	
     			mileStoneInfo.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		} else {
     			mileStoneInfo.setDueAmountExcludeGST("0.00");
     		}
     		
   			/* setting CGST */
     		if(Util.isNotEmptyObject(response.getCgstAmount())) {
     			mileStoneInfo.setCGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setCGST("0.00");
     		}
     		/* setting SGST */
     		if(Util.isNotEmptyObject(response.getSgstAmount())) {
     			mileStoneInfo.setSGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setSGST("0.00");
     		}
     		
     		if(Util.isNotEmptyObject(response.getGstAmount())) {
     			mileStoneInfo.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(response.getGstAmount()).setScale(roundingModeSize, roundingMode).toString()));
     		}else {
     			mileStoneInfo.setGstAmount("0.00");
     		}
     		
     		/* setting Total Due Amount */
     		/*if(Util.isNotEmptyObject(response.getTotalDueAmount())) {
     			mileStoneInfo.setTotalDueAmount(BigDecimal.valueOf(response.getTotalDueAmount()).setScale(roundingModeSize, roundingMode).toString());
     		}else {
     			mileStoneInfo.setTotalDueAmount("0.00");
     		}*/
     		
     		mileStoneInfo.setSAC("9982");
     		mileStoneInfoList.add(mileStoneInfo);
     	}
		info.setMileStones(mileStoneInfoList);
		if(Util.isNotEmptyObject(mileStoneInfoList)) {
			email.setFlag(true);
		}else {
			email.setFlag(false);
		}
		if(mileStoneInfoList.isEmpty()) {
			log.info("Failed to generate receipt for Flat No "+info.getFlatName());
			throw new EmployeeFinancialServiceException("Failed to generate receipt for Flat No "+info.getFlatName());
		}

		/*bvr 1*/
		info.setFlatSaleOwnerIdBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerIdBasedOnAccountId());
		info.setFlatSaleOwnerNameBasedOnAccountId(transactionServiceInfo.getFlatSaleOwnerNameBasedOnAccountId());
		info.setRequestFrom("approveFinancialMultipleTransaction");
		/* setting Company Billing Address and other details */
		setOfficeDetails(resp,info);
		
		if(Util.isNotEmptyObject(resp.getFlatsResponse()) && resp.getFlatsResponse().get(0).getSiteId().equals(107l)
			//	&& !"1".equals(resp.getFlatsResponse().get(0).getFlatSaleOwnerId())
				&& !"1".equals(transactionServiceInfo.getFlatSaleOwnerIdBasedOnAccountId().toString())
				) {
			//please refer FLATS_SALE_OWNERS table for "1", "1" is sale owner of the current project company and other are landlord flats
			//for "1" sale owner id company address fetching from DB
			info.setSiteName("Eden Garden");
			info.setThanksAndRegardsFrom("Thank You From EG");
			info.setReceivedAmountBy("I ");
		} else {
			info.setReceivedAmountBy("We ");//this code is for receipt pdf
		}

		/* generating pdf */
		List<DemandNoteGeneratorInfo> demandNoteGeneratorInfo = new ArrayList<>();
		demandNoteGeneratorInfo.add(info);
		email.setDemandNoteGeneratorInfoList(demandNoteGeneratorInfo);
		pdfHelper.XMLWorkerHelperForReceipt(email,fileInfo,transactionServiceInfo, metaDataName);	
		
		try {
			//Legal and Modification receipt Push notification
			if(transactionServiceInfo.isSendNotification()  && !transactionServiceInfo.isThisUplaodedData()) {
				
				String invoiceDocument = responseTypeList.get(0).getDocumentLocation();
				
				EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo() ;
				financialPushNotification.setBookingFormId(transactionServiceInfo.getBookingFormId());
				
				financialPushNotification.setNotificationTitle(NOTIFICATION_TITLE) ;
				financialPushNotification.setNotificationBody(NOTIFICATION_BODY) ;
				if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l) ) {
					financialPushNotification.setNotificationDescription("") ;
					financialPushNotification.setTypeMsg(NOTIFICATION_TYPE) ;
					financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1) ;//not in use
				} else  { 
					financialPushNotification.setNotificationDescription("") ;
					financialPushNotification.setTypeMsg(NOTIFICATION_TYPE) ;	
					financialPushNotification.setTypeOfPushNotificationMsg(NOTIFICATION_TYPE1) ;//not in use
				} 
				
				financialPushNotification.setStatus("Transaction Completed");
				financialPushNotification.setPaymentMode(resp.getTransactionModeName());
				financialPushNotification.setBankName(transactionServiceInfo.getBankName());
				financialPushNotification.setAmount(Util.isEmptyObject(transactionServiceInfo.getTransactionAmount())?"0.00":currencyUtil.getTheAmountWithCommas(transactionServiceInfo.getTransactionAmount(), roundingModeSize, roundingMode));
				financialPushNotification.setReceiptNumber(resp.getTransactionReceiptNo());
				financialPushNotification.setReferenceNumber(Util.isEmptyObject(transactionServiceInfo.getReferenceNo())?transactionServiceInfo.getChequeNumber():transactionServiceInfo.getReferenceNo().toString());
				financialPushNotification.setClearenceDate(transactionServiceInfo.getChequeClearanceDate()==null?transactionServiceInfo.getTransactionReceiveDate():transactionServiceInfo.getChequeClearanceDate());
				financialPushNotification.setCreatedDate(new Timestamp(new Date().getTime()));
				financialPushNotification.setSiteId(transactionServiceInfo.getSiteId());
				if(Util.isNotEmptyObject(fileInfo)) {
					financialPushNotification.setUploadedDocs(fileInfo.getUrl());
					financialPushNotification.setMileStoneName(fileInfo.getName());
					financialPushNotification.setInvoiceDocument(invoiceDocument);
				}
				pushNotificationHelper.sendFinancialStatusNotification(financialPushNotification,resp);	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sortUploadedData(EmployeeFinancialServiceInfo financialServiceInfo) {
		//log.info("***** Control inside the EmployeeFinancialHelper.sortUploadedData() *****");
		Collections.sort(financialServiceInfo.getDemandNoteMSRequests(), new Comparator<FinancialDemandNoteMS_TRN_Info>() {
			@Override
			public int compare(FinancialDemandNoteMS_TRN_Info m1, FinancialDemandNoteMS_TRN_Info m2) {
				if (m1.getDN_or_TRN_date().equals(m2.getDN_or_TRN_date())) {
					return 0;
				}else if (m1.getDN_or_TRN_date().after(m2.getDN_or_TRN_date())) {
					return 1;
				}else {
					return -1;	
				}
			}
		});		
		/*financialServiceInfo.getDemandNoteMSRequests().sort(new Comparator<FinancialDemandNoteMS_TRN_Info>() {
			@Override
			public int compare(FinancialDemandNoteMS_TRN_Info m1, FinancialDemandNoteMS_TRN_Info m2) {
				if (m1.getDN_or_TRN_date().equals(m2.getDN_or_TRN_date())) {
					return 0;
				}else if (m1.getDN_or_TRN_date().after(m2.getDN_or_TRN_date())) {
					return 1;
				}else {
					return -1;	
				}
			}
		});*/
	}

	public void sortApprovalData(List<EmployeeFinancialMultipleTRNInfo> multipleTrnRequestForApproval) {//pending transaction for approval,uncleared cheque
		//log.info("***** Control inside the EmployeeFinancialHelper.sortUploadedData() *****");
		Collections.sort(multipleTrnRequestForApproval, new Comparator<EmployeeFinancialMultipleTRNInfo>() {
			@Override
			public int compare(EmployeeFinancialMultipleTRNInfo m1, EmployeeFinancialMultipleTRNInfo m2) {
				if(m1.getTransactionReceiveDate()==null && m1.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
					return 1;
				}
				if (m1.getTransactionReceiveDate().equals(m2.getTransactionReceiveDate())) {
					return 0;
				}else if (m1.getTransactionReceiveDate().after(m2.getTransactionReceiveDate())) {
					return 1;
				}else {
					return -1;	
				}
			}
		});		
	}

	public void sortTransactionDataOnEdit(EmployeeFinancialTransactionServiceInfo serviceInfo) {
		log.info("***** Control inside the EmployeeFinancialHelper.sortTransactionDataOnEdit() *****");
		for (EmployeeFinancialTransactionServiceInfo info : serviceInfo.getReConstructedtransactionServiceRequest()) {
			if (info.getTransactionTypeName().equals(FinTransactionType.RECEIPT.getName())) {
				info.setDate(info.getTransactionReceiveDate());
			} else {
				info.setDate(info.getTransactionDate());
			}
		}		
		
		Collections.sort(serviceInfo.getReConstructedtransactionServiceRequest(), new Comparator<EmployeeFinancialTransactionServiceInfo>() {
			@Override
			public int compare(EmployeeFinancialTransactionServiceInfo m1, EmployeeFinancialTransactionServiceInfo m2) {
				if (m1.getDate().equals(m2.getDate())) {
					return 0;
				}else if (m1.getDate().after(m2.getDate())) {
					return 1;
				}else {
					return -1;	
				}
			}
		});
	}
	
	public void validateSaveTransactionServiceData(EmployeeFinancialTransactionRequest financialTransactionRequest) throws InSufficeientInputException, Exception {
		boolean isDataValid = true;
		boolean isEmpExistsInList = false;
		long empId = financialTransactionRequest.getEmpId();
		Properties prop= responceCodesUtil.getApplicationProperties();

		List<String> listOfEmployee = Arrays.asList(prop.getProperty("BACK_DATED_TRANSACTION_ENTRY_OPTION").split("\\,"));
		String enableEntry = prop.getProperty("ENABLE_BACK_DATED_TRANSACTION_ENTRY_OPTION");
		enableEntry = (Util.isEmptyObject(enableEntry) ? "" : enableEntry);
		//List<Long> listOfEmployee = Arrays.asList(88L,40l,74l,79l);

		if(FinTransactionType.RECEIPT.getId().equals(financialTransactionRequest.getTransactionTypeId())
				//&& FinTransactionMode.ONLINE.getId().equals(financialTransactionRequest.getTransactionModeId())
				){
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionReceiveDate())) {
				isDataValid = false;
				throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
			}
			isEmpExistsInList = listOfEmployee.contains(String.valueOf(empId));
			//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Timestamp currentDate = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(new Date().getTime()));
			Timestamp endDate = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(new Date().getTime()));
			Timestamp endDateFifth = TimeUtil.removeTimePartFromTimeStamp1(TimeUtil.addDays(new Timestamp(TimeUtil.getFirstDateOfMonth(endDate).getTime()), 4));

			Timestamp currentMonthFirst = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(TimeUtil.getFirstDateOfMonth(endDate).getTime()));
			Timestamp transactionReceiveDate = TimeUtil.removeTimePartFromTimeStamp1(financialTransactionRequest.getTransactionReceiveDate());
			
			Date previousMothStartDate = TimeUtil.getPreviousMonth(new Date());
			Timestamp previousMonthFirstDate = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(TimeUtil.getFirstDateOfMonth(previousMothStartDate).getTime()));
			
			if (transactionReceiveDate.after(endDateFifth)) {
				log.info("EmployeeFinancialHelper.validateSaveTransactionServiceData() after " + transactionReceiveDate + " - " + endDateFifth);
			} else if (transactionReceiveDate.before(endDateFifth) && isEmpExistsInList == false && enableEntry.equals("true")) {
				log.info("EmployeeFinancialHelper.validateSaveTransactionServiceData() before " + transactionReceiveDate + " - " + endDateFifth);
				//if transaction date is before the end Date
				if(previousMonthFirstDate.after(transactionReceiveDate)) {
					log.info("FinancialTest.main() prev before " + previousMonthFirstDate + " - " + transactionReceiveDate);	
					throw new EmployeeFinancialServiceException("Receipt is back dated entry unable to approve pls contact MIS team or Admin");
				} else if ((transactionReceiveDate.after(previousMonthFirstDate) || transactionReceiveDate.equals(previousMonthFirstDate)) 
						&& (transactionReceiveDate.before(endDateFifth) || transactionReceiveDate.equals(endDateFifth)) 
						&& (currentDate.before(endDateFifth) || currentDate.equals(endDateFifth))) {
					//if transaction date is after previous month 1st date and transaction revice date is before the 5th of current month
					//and current date is before the 5th of current month
					log.info("FinancialTest.main() prev before " + previousMonthFirstDate + " - " + transactionReceiveDate);
					//throw new EmployeeFinancialServiceException("Receipt is back dated entry unable to approve pls contact MIS team or Admin");
				} else if (currentDate.after(currentMonthFirst) && transactionReceiveDate.before(currentMonthFirst)) {
					log.info("FinancialTest.main() prev before " + previousMonthFirstDate + " - " + transactionReceiveDate);	
					throw new EmployeeFinancialServiceException("Receipt is back dated entry unable to approve pls contact MIS team or Admin");
				}
			}
			if (!FinTransactionMode.WAIVED_OFF.getId().equals(financialTransactionRequest.getTransactionModeId())) {
				if (!FinTransactionMode.ONLINE.getId().equals(financialTransactionRequest.getTransactionModeId())) {
					int response = employeeFinancialService.checkDuplicateTransactionOrNot(financialTransactionRequest);
					if (response >= 1) {
						throw new EmployeeFinancialServiceException("Sorry, duplicate transactions are not allowed. This transaction already exists.");
					}
				}
			}
			
			//bvr receipt waived offf
			if(FinTransactionMode.WAIVED_OFF.getId().equals(financialTransactionRequest.getTransactionModeId())) {
				for(EmployeeFinTranPaymentSetOffRequest setOff :financialTransactionRequest.getPaymentSetOffDetails())
				{
					if("Principal_Amount".equalsIgnoreCase(setOff.getSetOffTypeName()) && 0<setOff.getAmount())
					{
						throw new EmployeeFinancialServiceException("Principal Amount can't be Waived off.");
					}
				}
			}
		} else if(FinTransactionType.RECEIPT.getId().equals(financialTransactionRequest.getTransactionTypeId()) && FinTransactionMode.CHEQUE.getId().equals(financialTransactionRequest.getTransactionModeId())){
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionReceiveDate())) {
				isDataValid = false;
			}
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionDate())) {
				isDataValid = false;
			}
		} else if(FinTransactionType.PAYMENT.getId().equals(financialTransactionRequest.getTransactionTypeId()) && FinTransactionMode.CHEQUE.getId().equals(financialTransactionRequest.getTransactionModeId())){
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionForId())) {
				isDataValid = false;
			}
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionAmount())) {
				isDataValid = false;
			}
		} else if(FinTransactionType.PAYMENT.getId().equals(financialTransactionRequest.getTransactionTypeId()) && FinTransactionMode.ONLINE.getId().equals(financialTransactionRequest.getTransactionModeId())){
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionForId())) {
				isDataValid = false;
			}
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionAmount())) {
				isDataValid = false;
			}
		}		
	 
		if (Util.isEmptyObject(financialTransactionRequest.getTransactionTypeId())) {
			isDataValid = false;
		}
		if (Util.isEmptyObject(financialTransactionRequest.getTransactionTypeName())) {
			isDataValid = false;//"Receipt"
		}
		
		if (Util.isEmptyObject(financialTransactionRequest.getTransactionModeId())) {
			isDataValid = false;
		}
		
		if (Util.isEmptyObject(financialTransactionRequest.getTransactionModeName())) {
			isDataValid = false;
		}
		
		/*if (Util.isEmptyObject(financialTransactionRequest.getTransactionDate())) {
			isDataValid = false;
		}*/
		
		if (Util.isEmptyObject(financialTransactionRequest.getFlatIds())) {
			isDataValid = false;
		}
		if (Util.isEmptyObject(financialTransactionRequest.getPaymentSetOffDetails())) {
			isDataValid = false;
		}
		
		if(!isDataValid) {
			//List<String> errorMsgs = new ArrayList<String>();
			//errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		
	}

	public void validateUploadExcelData(EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		boolean isDataValid = true;
		List<String> errorMsgsList = new ArrayList<String>();
		valiateMilestoneSheet(employeeFinancialRequest);
		int indexOfObject = 1;
		if(Util.isNotEmptyObject(employeeFinancialRequest.getDemandNoteTransactionRequests()))
		for (FinancialDemandNoteMS_TRN_Request trn_MilestoneRequest : employeeFinancialRequest.getDemandNoteTransactionRequests()) {
			if (Util.isEmptyObject(trn_MilestoneRequest.getRecordType())) {
				isDataValid = false;
				
				errorMsgsList.add("Type is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			if (Util.isEmptyObject(trn_MilestoneRequest.getSiteName())) {
				isDataValid = false;
				errorMsgsList.add("Site name is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			if (Util.isEmptyObject(trn_MilestoneRequest.getBlockName())) {
				isDataValid = false;
				errorMsgsList.add("Block name is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			if (Util.isEmptyObject(trn_MilestoneRequest.getFlatNo())) {
				isDataValid = false;
				errorMsgsList.add("Flat no is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			
			if (Util.isEmptyObject(trn_MilestoneRequest.getTransactionTypeName())) {
				isDataValid = false;
				errorMsgsList.add("Transaction Type is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			
			if (Util.isEmptyObject(trn_MilestoneRequest.getTransactionModeName())) {
				isDataValid = false;
				errorMsgsList.add("Transaction Mode Mode is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			
			if (Util.isEmptyObject(trn_MilestoneRequest.getTransactionAmount())) {
				isDataValid = false;
				errorMsgsList.add("Transaction Amount Amount is missing in Transaction Sheet, Row Number "+indexOfObject);
			}

			if (Util.isEmptyObject(trn_MilestoneRequest.getReceiptStage())) {
				isDataValid = false;
				errorMsgsList.add("Receipt Stage Stage is missing in Transaction Sheet, Row Number "+indexOfObject);
			}
			
			if(trn_MilestoneRequest.getTransactionTypeName().equals(MetadataId.PAYMENT.getName())) {
				if (Util.isEmptyObject(trn_MilestoneRequest.getTransactionFor())) {
					isDataValid = false;
					errorMsgsList.add("Transaction For is missing in Transaction Sheet, Row Number "+indexOfObject);
				}	
			}else {
				if(trn_MilestoneRequest.getTransactionModeName().equals(MetadataId.ONLINE.getName())) {
					if (Util.isEmptyObject(trn_MilestoneRequest.getTransferModeName())) {
						isDataValid = false;
						errorMsgsList.add("Transfer Mode is missing in Transaction Sheet, Row Number "+indexOfObject);
					}
					
					if (Util.isEmptyObject(trn_MilestoneRequest.getReferenceNo())) {
						isDataValid = false;
						errorMsgsList.add("Reference No is missing in Transaction Sheet, Row Number "+indexOfObject);
					}
					
					if (Util.isEmptyObject(trn_MilestoneRequest.getOnlineReceiveDate())) {
						isDataValid = false;
						errorMsgsList.add("Online Receive Date is missing in Transaction Sheet, Row Number "+indexOfObject);
					}
				}else {
					if (Util.isEmptyObject(trn_MilestoneRequest.getChequeDate())) {
						isDataValid = false;
						errorMsgsList.add("Cheque Date is missing in Transaction Sheet, Row Number "+indexOfObject);
					}
					if (Util.isEmptyObject(trn_MilestoneRequest.getChequeNumber())) {
						isDataValid = false;
						errorMsgsList.add("Cheque Number is missing in Transaction Sheet, Row Number "+indexOfObject);
					}
					if (Util.isEmptyObject(trn_MilestoneRequest.getChequeReceiveDate())) {
						isDataValid = false;
						errorMsgsList.add("Cheque Receive Date is missing in Transaction Sheet, Row Number "+indexOfObject);
					}
				}
			}//else
			
			/*if(Util.isEmptyObject(trn_MilestoneRequest.getPaymentSetOffDetails())) {
				isDataValid = false;
				errorMsgsList.add("Payment set off details is missing in Transaction Sheet, Row Number "+indexOfObject);
			}else {
				boolean isAnyPaymentSetOffExists = false;
				for (EmployeeFinTranPaymentSetOffRequest paymentSetOffRequest : trn_MilestoneRequest.getPaymentSetOffDetails()) {
					if(!isAnyPaymentSetOffExists) {
						if(Util.isNotEmptyObject(paymentSetOffRequest.getAmount())) {
							isAnyPaymentSetOffExists = true;
						}
					}
				}
				
				if(!isAnyPaymentSetOffExists) {
					isDataValid = false;
					errorMsgsList.add("Payment set off details is missing in Transaction Sheet, Row Number "+indexOfObject);
				}
			}*/
			
			if(!isDataValid) {
				throw new InSufficeientInputException(errorMsgsList);
			}
			indexOfObject++;
		}
		
		if(!isDataValid) {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgsList);
		}
		
	}

	private void valiateMilestoneSheet(EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		boolean isDataValid = true;
		if(Util.isNotEmptyObject(employeeFinancialRequest.getDemandNoteMSRequests())) 
		for (FinancialDemandNoteMS_TRN_Request trn_MilestoneRequest : employeeFinancialRequest.getDemandNoteMSRequests()) {
			if (Util.isEmptyObject(trn_MilestoneRequest.getRecordType())) {
				isDataValid = false;
				//throw new InSufficeientInputException(Arrays.asList("Type is missing in Milestone Sheet"));
			}
			if (Util.isEmptyObject(trn_MilestoneRequest.getSiteName())) {
				isDataValid = false;
			}
			if (Util.isEmptyObject(trn_MilestoneRequest.getBlockName())) {
				isDataValid = false;
				//throw new InSufficeientInputException(Arrays.asList("Block name is missing in Milestone Sheet"));
			}
			if (Util.isEmptyObject(trn_MilestoneRequest.getFlatNo())) {
				isDataValid = false;
				//throw new InSufficeientInputException(Arrays.asList("Flat no is missing in Milestone Sheet"));
			}
			
			if (Util.isEmptyObject(trn_MilestoneRequest.getMilestoneName())) {
				isDataValid = false;
				//throw new InSufficeientInputException(Arrays.asList("Milestone name is missing in Milestone Sheet"));
			}
			
			if (Util.isEmptyObject(trn_MilestoneRequest.getMileStoneNo())) {
				isDataValid = false;
				//throw new InSufficeientInputException(Arrays.asList("Milestone No is missing in Milestone Sheet"));
			}
		}
		
		if(!isDataValid) {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	public List<FileInfo> generateConsolidatedReceipt(FinancialConsolidatedReceiptInfo financialConsolidatedReceiptInfo,
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo)
			throws Exception{
		log.info("***** Control is inside the generateConsolidatedReceipt in EmployeeFinancialHelper *****");
		List<FileInfo> fileInfoList = new ArrayList<>();
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		FinancialConsolidatedReceiptPdfInfo financialConsolidatedReceiptPdfInfo = new FinancialConsolidatedReceiptPdfInfo();
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo = employeeFinancialServiceInfo.getCustomerPropertyDetailsInfo();
		//Properties prop = responceCodesUtil.getApplicationProperties();
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String thanksAndRegardsFrom="";
		if(employeeFinancialServiceInfo.getSiteId()!=null && employeeFinancialServiceInfo.getSiteId().equals(131l)) {
			//rightSidelogoForPdf = prop.getProperty("ASPIRE_LOGO1");	
			//thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
		} else {
			//rightSidelogoForPdf = prop.getProperty("SUMADHURA_LOGO1");
			//thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
		}
		
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());		

		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		
		String rightSidelogoFilePath = logoAndOtherDetails.get("rightSidelogoFilePath");
		String leftSidelogoFilePath = logoAndOtherDetails.get("leftSidelogoFilePath");
		
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		//greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
	
		if(Util.isNotEmptyObject(rightSidelogoFilePath)) {//checking file is present in local folder, if exists loading file from local folder
			if(isFilePathFoundPdf.get(rightSidelogoFilePath)!=null && isFilePathFoundPdf.get(rightSidelogoFilePath).equals("true")) {
				financialConsolidatedReceiptPdfInfo.setRightSidelogoFilePath(rightSidelogoFilePath);
			} else {
				File file = new File(rightSidelogoFilePath);
				if(file.exists()) {//checking file is exists or not, if exist the loading this file in pdf
					isFilePathFoundPdf.put(rightSidelogoFilePath, "true");
					financialConsolidatedReceiptPdfInfo.setRightSidelogoFilePath(rightSidelogoFilePath);
				}
			}
		}
		
		if(Util.isNotEmptyObject(leftSidelogoFilePath)) {
			if(isFilePathFoundPdf.get(leftSidelogoFilePath)!=null && isFilePathFoundPdf.get(leftSidelogoFilePath).equals("true")) {
				financialConsolidatedReceiptPdfInfo.setLeftSidelogoFilePath(leftSidelogoFilePath);
			} else {
				File file = new File(leftSidelogoFilePath);
				if(file.exists()) {
					isFilePathFoundPdf.put(leftSidelogoFilePath, "true");
					financialConsolidatedReceiptPdfInfo.setLeftSidelogoFilePath(leftSidelogoFilePath);
				}
			}
		}
		
		financialConsolidatedReceiptPdfInfo.setRightSidelogoForPdf(rightSidelogoForPdf);
		financialConsolidatedReceiptPdfInfo.setLeftSidelogoForPdf(leftSidelogoForPdf);
		financialConsolidatedReceiptPdfInfo.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		financialConsolidatedReceiptPdfInfo.setConsolidatedReceiptDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		financialConsolidatedReceiptPdfInfo.setBookingFormId(employeeFinancialServiceInfo.getBookingFormId());
		/* Customer Details and Project Details */
		String addSign = "";
		String customerNames = "N/A";
		String siteName = "N/A";
		String flatNo = "N/A";
		String sbua = "N/A";
		String bookingDate = "N/A";
/*		String totalPrincipalAmount = "0.0";
		String totalCgstAmount = "0.0";
		String totalSgstAmount = "0.0";
		String totalAmount = "0.0";
		String totalBouncePrincipalAmount = "0.0";
		String totalBounceCgstAmount = "0.0";
		String totalBounceSgstAmount = "0.0";
		String totalBounceAmount = "0.0";*/
		/* Customer Details */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getCustomerPropertyDetailsPojoList()) 
			&& Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getCustomerPropertyDetailsPojoList().get(0))) {
			CustomerPropertyDetailsPojo customerPropertyDetailsPojo = financialConsolidatedReceiptInfo.getCustomerPropertyDetailsPojoList().get(0);
			/* Customer Names */
			if(Util.isNotEmptyObject(customerPropertyDetailsPojo.getCustomerName())) {
				StringBuilder customerName = new StringBuilder();
				customerName.append(customerPropertyDetailsPojo.getCustomerName());
				if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getCoApplicantPojoList())) {
					for(CoApplicantPojo coApplicantPojo : financialConsolidatedReceiptInfo.getCoApplicantPojoList()) {
						if(Util.isNotEmptyObject(coApplicantPojo) && Util.isNotEmptyObject(coApplicantPojo.getCoAppFullName())) {
							customerName.append(" & &nbsp;"+coApplicantPojo.getCoAppFullName());
						}
					}
				}
				customerNames = customerName.toString();
			}
			/* Site Name */
			if(Util.isNotEmptyObject(customerPropertyDetailsPojo.getSiteName())) {
				siteName = customerPropertyDetailsPojo.getSiteName();
			}
			/* Flat No */
			if(Util.isNotEmptyObject(customerPropertyDetailsPojo.getFlatNo())) {
				flatNo = customerPropertyDetailsPojo.getFlatNo();
			}
			/* SBUA */
			if(Util.isNotEmptyObject(customerPropertyDetailsPojo.getSbua())) {
				sbua = customerPropertyDetailsPojo.getSbua().toString();
			}
			/* Booking Date */
			if(Util.isNotEmptyObject(customerPropertyDetailsPojo.getBookingDate())) {
				bookingDate = TimeUtil.timestampToDD_MM_YYYY(customerPropertyDetailsPojo.getBookingDate());
			}
		}
		financialConsolidatedReceiptPdfInfo.setCustomerNames(customerNames);
		financialConsolidatedReceiptPdfInfo.setSiteName(siteName);
		financialConsolidatedReceiptPdfInfo.setFlatNo(flatNo);
		financialConsolidatedReceiptPdfInfo.setSbua(sbua);
		financialConsolidatedReceiptPdfInfo.setBookingDate(bookingDate);
		financialConsolidatedReceiptPdfInfo.setCurrentDate(TimeUtil.timestampToDD_MM_YYYY(new Timestamp(new Date().getTime())));
		
		Properties prop = responceCodesUtil.getApplicationProperties();
		//String flatSaleOwner = resp.getFlatsResponse().get(0).getFlatSaleOwner();
		if(Util.isNotEmptyObject(customerPropertyDetailsInfo) &&  customerPropertyDetailsInfo.getFlatSaleOwner().equalsIgnoreCase("Indimax")) {
			String flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner().trim().replaceAll(" ", "");
			String companyName = prop.getProperty(flatSaleOwner+"_COMPANY_NAME");
			String companyBilling = prop.getProperty(flatSaleOwner+"_COMPANY_BILLING_ADDRESS");
			String companyTelephoneNo = prop.getProperty(flatSaleOwner+"_COMPANY_TELEPHONE_NO");
			String companyEmail = prop.getProperty(flatSaleOwner+"_COMPANY_EMAIL");

			String companyCin = prop.getProperty(flatSaleOwner+"_COMPANY_CIN");
			String companyGstin = prop.getProperty(flatSaleOwner+"_COMPANY_GSTIN");
			String companyWebsite = prop.getProperty(flatSaleOwner+"_COMPANY_WEBSITE");
			String companyLlpin = prop.getProperty(flatSaleOwner+"_COMPANY_LLPIN");
			String companyPanNumber = prop.getProperty(flatSaleOwner+"_COMPANY_PAN");
			String companyCity = prop.getProperty(flatSaleOwner+"_COMPANY_CITY");
			
			financialConsolidatedReceiptPdfInfo.setCompanyName(companyName);
			financialConsolidatedReceiptPdfInfo.setCompanyBillingAddress(companyBilling);
			financialConsolidatedReceiptPdfInfo.setCompanyTelephoneNo(companyTelephoneNo==""?"-":companyTelephoneNo);
			financialConsolidatedReceiptPdfInfo.setCompanyEmail(companyEmail==""?"-":companyEmail);
			
			financialConsolidatedReceiptPdfInfo.setCompanyCin(companyCin==""?"-":companyCin);
			financialConsolidatedReceiptPdfInfo.setCompanyGstin(companyGstin==null?"-":companyGstin);
			financialConsolidatedReceiptPdfInfo.setCompanyWebsite(companyWebsite);
			financialConsolidatedReceiptPdfInfo.setCompanyLlpin(companyLlpin);
			financialConsolidatedReceiptPdfInfo.setCompanyPanNumber(companyPanNumber);
			financialConsolidatedReceiptPdfInfo.setCompanyCity(companyCity);
		} else 

		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getOfficeDetailsList()) && Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getOfficeDetailsList().get(0))) {
			OfficeDtlsResponse officeDetailsResponse = financialConsolidatedReceiptInfo.getOfficeDetailsList().get(0);
			if(Util.isNotEmptyObject(officeDetailsResponse.getName())) {
				financialConsolidatedReceiptPdfInfo.setCompanyName(officeDetailsResponse.getName());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyName("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getBillingAddress())){
				financialConsolidatedReceiptPdfInfo.setCompanyBillingAddress(officeDetailsResponse.getBillingAddress());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyBillingAddress("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getTelephoneNo())) {
				financialConsolidatedReceiptPdfInfo.setCompanyTelephoneNo(officeDetailsResponse.getTelephoneNo());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyTelephoneNo("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getEmail())) {
				financialConsolidatedReceiptPdfInfo.setCompanyEmail(officeDetailsResponse.getEmail());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyEmail("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCin())) {
				financialConsolidatedReceiptPdfInfo.setCompanyCin(officeDetailsResponse.getCin());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyCin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getGstin())) {
				financialConsolidatedReceiptPdfInfo.setCompanyGstin(officeDetailsResponse.getGstin());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyGstin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getWebsite())) {
				financialConsolidatedReceiptPdfInfo.setCompanyWebsite(officeDetailsResponse.getWebsite());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyWebsite("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getLlpin())) {
				financialConsolidatedReceiptPdfInfo.setCompanyLlpin(officeDetailsResponse.getLlpin());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyLlpin("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getPan())) {
				financialConsolidatedReceiptPdfInfo.setCompanyPanNumber(officeDetailsResponse.getPan());
			}else {
				financialConsolidatedReceiptPdfInfo.setCompanyPanNumber("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCity())) {
				financialConsolidatedReceiptPdfInfo.setCompanyCity(officeDetailsResponse.getCity());
			} else {
				financialConsolidatedReceiptPdfInfo.setCompanyCity("N/A");
			}
		}else {
			financialConsolidatedReceiptPdfInfo.setCompanyName("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyBillingAddress("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyTelephoneNo("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyEmail("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyCin("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyGstin("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyWebsite("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyPanNumber("N/A");
			financialConsolidatedReceiptPdfInfo.setCompanyCity("N/A");
		}
		
		/* Total Flat Cost */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalFlatCost())) {
			financialConsolidatedReceiptPdfInfo.setTotalFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalFlatCost("0.0");
		}
		
		/* Total Paid Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalPaidAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalPaidAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalPaidAmount("0.0");
		}
		
		/* Balance on Total Cost */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getBalanceOnTotalCost())) {
			if(financialConsolidatedReceiptInfo.getTotalPaidAmount()!=null) {
				if(financialConsolidatedReceiptInfo.getTotalPaidAmount() >financialConsolidatedReceiptInfo.getTotalFlatCost()) {
					addSign = "-";
				}
			}
			financialConsolidatedReceiptPdfInfo.setBalanceOnTotalCost(addSign+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getBalanceOnTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setBalanceOnTotalCost("0.0");
		}
		
		/* Received and Payment Grand Totals */
		/* Total Principal Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalPrincipalAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalPrincipalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalPrincipalAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalPrincipalAmount("0.0");
		}
		
		/* Total CGST Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalCgstAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalCgstAmount("0.0");
		}
		
		/* Total SGST Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalSgstAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalSgstAmount("0.0");
		}
		
		/* Total Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalAmount("0.0");
		}
		
		/* Bounce Totals */
		/* Total Bounce Principal Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalBouncePrincipalAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalBouncePrincipalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalBouncePrincipalAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalBouncePrincipalAmount("0.0");
		}
		
		/* Total Bounce CGST Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalBounceCgstAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalBounceCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalBounceCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalBounceCgstAmount("0.0");
		}
		
		/* Total Bounce SGST Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalBounceSgstAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalBounceSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalBounceSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalBounceSgstAmount("0.0");
		}
		
		/* Total Bounce Amount */
		if(Util.isNotEmptyObject(financialConsolidatedReceiptInfo.getTotalBounceAmount())) {
			financialConsolidatedReceiptPdfInfo.setTotalBounceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(financialConsolidatedReceiptInfo.getTotalBounceAmount()).setScale(roundingModeSize, roundingMode).toString()));
		}else {
			financialConsolidatedReceiptPdfInfo.setTotalBounceAmount("0.0");
		}
		
		/*List<FinConsolidatedReceiptInfo> finCompletedTransactionsList = financialConsolidatedReceiptInfo.getFinCompletedTransactionsList();
		List<FinConsolidatedReceiptInfo> finBouncedTransactionsList = financialConsolidatedReceiptInfo.getFinBouncedTransactionsList();*/
		
		List<FinTransactionEntryDetailsPdfInfo> completedTransactionsList = new ArrayList<>(); 
		List<FinTransactionEntryDetailsPdfInfo> bouncedTransactionsList = new ArrayList<>();	
		long srNo =1l; 
		addSign = "";
		for (FinConsolidatedReceiptInfo completedTransaction : financialConsolidatedReceiptInfo.getFinCompletedTransactionsList()) {
			FinTransactionEntryDetailsPdfInfo entryDetailsPdfInfo = new FinTransactionEntryDetailsPdfInfo();
			entryDetailsPdfInfo.setSrNo(srNo);
			entryDetailsPdfInfo.setChequeOrOnlineReceivedDate(TimeUtil.timestampToDD_MM_YYYY(completedTransaction.getTransactionReceiveDate()));
			entryDetailsPdfInfo.setChequeOrOnlineReferenceNo(completedTransaction.getChequeOrReferenceNo());
			entryDetailsPdfInfo.setChequeOrOnlineTransactionDate(TimeUtil.timestampToDD_MM_YYYY(completedTransaction.getTransactionDate()));
			if(completedTransaction.getTransactionTypeId() == 2) {//FinTransactionType.PAYMENT.getId();
				addSign = "-";//is this is refund transaction then add (minus) sign on refund amount
			} else {
				addSign = "";
			}
			entryDetailsPdfInfo.setPrincipalAmount(addSign+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(completedTransaction.getPrincipalAmount()).setScale(roundingModeSize, roundingMode).toString()));
			entryDetailsPdfInfo.setCgstAmount(addSign+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(completedTransaction.getCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
			entryDetailsPdfInfo.setSgstAmount(addSign+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(completedTransaction.getSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
			entryDetailsPdfInfo.setTotalAmount(addSign+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(completedTransaction.getSetOffAmount()).setScale(roundingModeSize, roundingMode).toString()));

			entryDetailsPdfInfo.setStatus(completedTransaction.getTransactionStatusName());
			completedTransactionsList.add(entryDetailsPdfInfo);
			srNo++;
		}
		
		srNo =1l;
		for (FinConsolidatedReceiptInfo bounceTransaction : financialConsolidatedReceiptInfo.getFinBouncedTransactionsList()) {
			FinTransactionEntryDetailsPdfInfo entryDetailsPdfInfo = new FinTransactionEntryDetailsPdfInfo();
			entryDetailsPdfInfo.setSrNo(srNo);
			entryDetailsPdfInfo.setChequeOrOnlineReceivedDate(TimeUtil.timestampToDD_MM_YYYY(bounceTransaction.getTransactionReceiveDate()));
			entryDetailsPdfInfo.setChequeOrOnlineReferenceNo(bounceTransaction.getChequeOrReferenceNo());
			entryDetailsPdfInfo.setChequeOrOnlineTransactionDate(TimeUtil.timestampToDD_MM_YYYY(bounceTransaction.getTransactionDate()));
 
			entryDetailsPdfInfo.setPrincipalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(bounceTransaction.getPrincipalAmount()).setScale(roundingModeSize, roundingMode).toString()));
			entryDetailsPdfInfo.setCgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(bounceTransaction.getCgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
			entryDetailsPdfInfo.setSgstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(bounceTransaction.getSgstAmount()).setScale(roundingModeSize, roundingMode).toString()));
			entryDetailsPdfInfo.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(bounceTransaction.getSetOffAmount()).setScale(roundingModeSize, roundingMode).toString()));

			entryDetailsPdfInfo.setStatus(bounceTransaction.getTransactionStatusName());
			bouncedTransactionsList.add(entryDetailsPdfInfo);
			srNo++;
		}
		
		financialConsolidatedReceiptPdfInfo.setFinCompletedTransactionsList(completedTransactionsList);
		financialConsolidatedReceiptPdfInfo.setFinBouncedTransactionsList(bouncedTransactionsList);
		financialConsolidatedReceiptPdfInfo.setIsBounceTransactionExists(!(bouncedTransactionsList.isEmpty()));
		
		if(bouncedTransactionsList.isEmpty() && completedTransactionsList.isEmpty()) {
			throw new EmployeeFinancialServiceException("No transaction found, could not generate statement receipt!");
		}
		financialConsolidatedReceiptPdfInfo.setConsolidatedPdfFileName(financialConsolidatedReceiptPdfInfo.getSiteName()+"-"+financialConsolidatedReceiptPdfInfo.getFlatNo()+" - Statement of Account.pdf");
		/*fileInfo.setName("All Projects.pdf");
		fileInfo.setUrl("http://106.51.38.64:9999/images/sumadhura_projects_images/Brochures/All Projects.pdf");*/
		email.setConsolidatedReceiptPdfInfos(Arrays.asList(financialConsolidatedReceiptPdfInfo));
		pdfHelper.XMLWorkerHelperForConsolidatedReceipt(email, fileInfo, employeeFinancialServiceInfo, "");
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	public Map<String, List<Map<String, Object>>> generateClosingBalanceReport(List<FinClosingBalanceReportPojo> balanceReportPojos, 
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		log.info("***** Control inside the EmployeeFinancialHelper.generateClosingBalanceReport() *****");
		//List<FileInfo> fileInfoList = new ArrayList<>();
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		List<Map<String,Object>> dataForPdf = new ArrayList<>();
		List<Map<String,Object>> dataForPdfFooter = new ArrayList<>();
		//Email email = new Email();
		//FileInfo fileInfo = new FileInfo();
		//FinancialConsolidatedReceiptPdfInfo financialConsolidatedReceiptPdfInfo = new FinancialConsolidatedReceiptPdfInfo();
		
		/*String logoForPdf = "";
		String thanksAndRegardsFrom="";
		if(employeeFinancialServiceInfo.getSiteId()!=null && employeeFinancialServiceInfo.getSiteId().equals(131l)) {
			logoForPdf = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_LOGO1");	
			thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
		} else {
			logoForPdf = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1");
			thanksAndRegardsFrom = responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
		}*/
		
		
			String siteName = "";
			String bookingDate = "";
			String agreementDate = "";
			
			/* Malladi Changes */
			String saleDeedDate = null;
			String handingOverDate = null;
			String registrationDate = null;
			
			long serialNo = 1;
			double basicFlatCost = 0d;
			double amenitiesFlatCost = 0d;
			double totalCostExclGST = 0d;
			double totalAgreementCost =0d;
			double totalAmountPaid=0d;
			double TotalPendingAmountAsPerWorkCompletion=0d;	
			double totalPendingAmount=0d;
			
			Map<String,Object> closingBalanceReportHeading = new HashMap<String,Object>();
			closingBalanceReportHeading.put("Sr.No", "Sr.No");
			closingBalanceReportHeading.put("Site Name", "Site Name");
			closingBalanceReportHeading.put("Block Name", "Block Name");
			closingBalanceReportHeading.put("Flat No", "Flat No");
			closingBalanceReportHeading.put("Customer Name", "Customer Name");
			closingBalanceReportHeading.put("Pan card", "Pan card");
			closingBalanceReportHeading.put("Booking Date", "Booking Date");
			closingBalanceReportHeading.put("Agreement Date", "Agreement Date");
			closingBalanceReportHeading.put("SBUA", "SBUA");
			closingBalanceReportHeading.put("Basic Flat Cost", "Basic Flat Cost");
			closingBalanceReportHeading.put("Amenities Flat Cost", "Amenities Flat Cost");
			closingBalanceReportHeading.put("Total Cost", "Total Cost");
			closingBalanceReportHeading.put("Total Amount Paid", "Total Amount Paid");
			closingBalanceReportHeading.put("Total Pending Amount As Per Work Completion", "Total Pending Amount As Per Work Completion");
			
		for (FinClosingBalanceReportPojo finClosingBalanceReportPojo : balanceReportPojos) {
			agreementDate = "";
			bookingDate = "";
			
			/* Malladi Changes */
			saleDeedDate = "";
			handingOverDate = "";
			registrationDate = "";
			
			Map<String,Object> closingBalanceReport = new HashMap<String,Object>();
			
			if(finClosingBalanceReportPojo.getBookingDate()!=null) {
				bookingDate = TimeUtil.timestampToDD_MM_YYYY(finClosingBalanceReportPojo.getBookingDate());
			}
			if(finClosingBalanceReportPojo.getAgreementDate()!=null) {
				agreementDate = TimeUtil.timestampToDD_MM_YYYY(finClosingBalanceReportPojo.getAgreementDate());
			}
			
			/* Malladi Changes */
			if(finClosingBalanceReportPojo.getSaleDeedDate()!=null) {
				saleDeedDate = TimeUtil.timestampToDD_MM_YYYY(finClosingBalanceReportPojo.getSaleDeedDate());
			}
			if(finClosingBalanceReportPojo.getHandingOverDate()!=null) {
				handingOverDate = TimeUtil.timestampToDD_MM_YYYY(finClosingBalanceReportPojo.getHandingOverDate());
			}
			if(finClosingBalanceReportPojo.getRegistrationDate()!=null) {
				registrationDate = TimeUtil.timestampToDD_MM_YYYY(finClosingBalanceReportPojo.getRegistrationDate());
			}
			
			siteName = finClosingBalanceReportPojo.getSiteName();
			closingBalanceReport.put("serialNo", serialNo);
			closingBalanceReport.put("siteName", siteName);
			closingBalanceReport.put("blockName", finClosingBalanceReportPojo.getBlockName());
			closingBalanceReport.put("flatNo", finClosingBalanceReportPojo.getFlatNo());
			closingBalanceReport.put("custName", finClosingBalanceReportPojo.getCustomerName());
			closingBalanceReport.put("pancard", finClosingBalanceReportPojo.getPancard());
			closingBalanceReport.put("bookingDate",bookingDate);
			closingBalanceReport.put("agreementDate",agreementDate);
			closingBalanceReport.put("sbua",finClosingBalanceReportPojo.getSbua());
			
			/* Malladi Changes */
			closingBalanceReport.put("saleDeedDate", saleDeedDate);
			closingBalanceReport.put("handingOverDate", handingOverDate);
			closingBalanceReport.put("registrationDate", registrationDate);
			
			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getBasicFlatCost())) {
				basicFlatCost+=finClosingBalanceReportPojo.getBasicFlatCost();
				closingBalanceReport.put("basicFlatCost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finClosingBalanceReportPojo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			}

			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getAmenitiesFlatCost())) {
				amenitiesFlatCost+=finClosingBalanceReportPojo.getAmenitiesFlatCost();
				closingBalanceReport.put("amenitiesFlatCost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finClosingBalanceReportPojo.getAmenitiesFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			}
			
			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getAmenitiesFlatCost()) && Util.isNotEmptyObject(finClosingBalanceReportPojo.getBasicFlatCost())) {
				double amt = finClosingBalanceReportPojo.getBasicFlatCost()+finClosingBalanceReportPojo.getAmenitiesFlatCost();
				totalCostExclGST += amt;
				closingBalanceReport.put("totalCostExclGST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amt).setScale(roundingModeSize, roundingMode).toString()));
			}
			
			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getTotalAgreementCost())) {
				totalAgreementCost+=finClosingBalanceReportPojo.getTotalAgreementCost();
				closingBalanceReport.put("totalAgreementCost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finClosingBalanceReportPojo.getTotalAgreementCost()).setScale(roundingModeSize, roundingMode).toString()));
			}
			
			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getTotalAmountPaid())) {
				totalAmountPaid+=finClosingBalanceReportPojo.getTotalAmountPaid();
				closingBalanceReport.put("totalAmountPaid",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finClosingBalanceReportPojo.getTotalAmountPaid()).setScale(roundingModeSize, roundingMode).toString()));
			}
			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getTotalAmountPaid()) &&Util.isNotEmptyObject(finClosingBalanceReportPojo.getTotalAgreementCost())) {
				Double pendingAmount=finClosingBalanceReportPojo.getTotalAgreementCost()-finClosingBalanceReportPojo.getTotalAmountPaid();
				totalPendingAmount+=pendingAmount;
				String pendingAmt=currencyUtil.convertUstoInFormat(BigDecimal.valueOf(pendingAmount).setScale(roundingModeSize, roundingMode).toString());
				closingBalanceReport.put("pendingAmount",pendingAmt.replaceAll("\u00A0", ""));
			}
			
			//closingBalanceReport.put("excessAmount",finClosingBalanceReportPojo.getExcessAmount());
			if(Util.isNotEmptyObject(finClosingBalanceReportPojo.getTotalPendingAmountAsPerWorkCompletion())) {
				TotalPendingAmountAsPerWorkCompletion+=finClosingBalanceReportPojo.getTotalPendingAmountAsPerWorkCompletion();
				/*if(finClosingBalanceReportPojo.getTotalPendingAmountAsPerWorkCompletion().equals(-5174376.48d)) {
					log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = ");
				}*/
				if(finClosingBalanceReportPojo.getTotalPendingAmountAsPerWorkCompletion()<0) {
					closingBalanceReport.put("TotalPendingAmountAsPerWorkCompletion","-"+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Math.abs(finClosingBalanceReportPojo.getTotalPendingAmountAsPerWorkCompletion())).setScale(roundingModeSize, roundingMode).toString().trim()));
				} else {
					closingBalanceReport.put("TotalPendingAmountAsPerWorkCompletion",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(finClosingBalanceReportPojo.getTotalPendingAmountAsPerWorkCompletion()).setScale(roundingModeSize, roundingMode).toString()));
				}
			}
			
			dataForPdf.add(closingBalanceReport);
			serialNo++;
		}
			dataForPdf1.put("closingBalanceData", dataForPdf);
			
		
			Map<String,Object> closingBalanceReportFooter = new HashMap<String,Object>();
			closingBalanceReportFooter.put("basicFlatCost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(basicFlatCost).setScale(roundingModeSize, roundingMode).toString()));
			closingBalanceReportFooter.put("amenitiesFlatCost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesFlatCost).setScale(roundingModeSize, roundingMode).toString()));
			closingBalanceReportFooter.put("totalCostExclGST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalCostExclGST).setScale(roundingModeSize, roundingMode).toString()));
			closingBalanceReportFooter.put("totalAgreementCost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalAgreementCost).setScale(roundingModeSize, roundingMode).toString()));
			closingBalanceReportFooter.put("totalAmountPaid",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalAmountPaid).setScale(roundingModeSize, roundingMode).toString()));
			closingBalanceReportFooter.put("totalPendingAmount",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalPendingAmount).setScale(roundingModeSize, roundingMode).toString()));
			if(TotalPendingAmountAsPerWorkCompletion<0) {
				closingBalanceReportFooter.put("TotalPendingAmountAsPerWorkCompletion","-"+currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Math.abs(TotalPendingAmountAsPerWorkCompletion)).setScale(roundingModeSize, roundingMode).toString().trim()));
			} else {
				closingBalanceReportFooter.put("TotalPendingAmountAsPerWorkCompletion",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(TotalPendingAmountAsPerWorkCompletion).setScale(roundingModeSize, roundingMode).toString()));
			}

			dataForPdfFooter.add(closingBalanceReportFooter);
			dataForPdf1.put("closingBalanceReportFooter",dataForPdfFooter);
		
		/*try {
			email.setDataForPdf(dataForPdf1);
			email.setName(siteName + " - Closing balance.xls");
			fileInfo = csvGeneratorHelper.ClosingBalanceReportWorkerHelper(email, "", employeeFinancialServiceInfo);
			fileInfoList.add(fileInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/
			
		return dataForPdf1;
	}

	public void setDemandNoteFormat(FinancialProjectMileStoneInfo mileStoneInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo,
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		log.info("setDemandNoteFormat "+mileStoneInfo.getMasterDemandNotedate()+", getMasterDemandNoteDueDate "+mileStoneInfo.getMasterDemandDueDate()+", getMileStoneNo "+mileStoneInfo.getMileStoneNo()+", getProjectMilestoneId"+mileStoneInfo.getProjectMilestoneId());
		Properties prop = responceCodesUtil.getApplicationProperties();
		String FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL = prop.getProperty("FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL");
		String MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL = prop.getProperty("MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL");
		if(employeeFinancialServiceInfo.getActionUrl()!=null && "Final_Demand_Note".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL = "Final_Demand_Note";
			MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL = "Final_Demand_Note";
		}
		if(employeeFinancialServiceInfo.isThisUplaodedData()) {
			int days = TimeUtil.differenceBetweenDays(customerPropertyDetailsInfo.getBookingDate(), mileStoneInfo.getDemandNoteDate());
			if (days > 0) {
				//if(employeeFinancialServiceInfo.isThisUplaodedData()) {
					employeeFinancialServiceInfo.setActionUrl(MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL);
				//}
			} else {
				//if(employeeFinancialServiceInfo.isThisUplaodedData()) {
					employeeFinancialServiceInfo.setActionUrl(FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL);
				//}	
			}		
		}
	}
	
	public void assignDemandNoteDateForRegenerate(FinancialProjectMileStoneInfo mileStoneInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo,
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws Exception {
		log.info("getMasterDemandNoteDate "+mileStoneInfo.getMasterDemandNotedate()+", getMasterDemandNoteDueDate "+mileStoneInfo.getMasterDemandDueDate()+", getMileStoneNo "+mileStoneInfo.getMileStoneNo()+", getProjectMilestoneId"+mileStoneInfo.getProjectMilestoneId());
		if(mileStoneInfo.getMileStoneNo()!=null && (mileStoneInfo.getMileStoneNo() == 1)) {
			mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),15));
			
			if(customerPropertyDetailsInfo.getSiteId().equals(126l)) {
				//IF BOOKING IS BEFORE 31-MARCH-2022 THEN ADD DEFAULT DUE DATE, by purnima and pavan sir
				if(customerPropertyDetailsInfo.getBookingDate().before(new Timestamp(format.parse("31-03-2022").getTime())) ||
						customerPropertyDetailsInfo.getBookingDate().equals(new Timestamp(format.parse("31-03-2022").getTime()))
						) {
					mileStoneInfo.setMileStoneDueDate(new Timestamp(format.parse("01-05-2022").getTime()));
				}
			}
			
		} else if(mileStoneInfo.getMileStoneNo()!=null && (mileStoneInfo.getMileStoneNo() == 2)) {
			mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),30));
				
			if(customerPropertyDetailsInfo.getSiteId().equals(126l)) {
				//IF BOOKING IS BEFORE 31-MARCH-2022 THEN ADD DEFAULT DUE DATE, by purnima and pavan sir
				if(customerPropertyDetailsInfo.getBookingDate().before(new Timestamp(format.parse("31-03-2022").getTime())) ||
						customerPropertyDetailsInfo.getBookingDate().equals(new Timestamp(format.parse("31-03-2022").getTime()))
						) {
					mileStoneInfo.setMileStoneDueDate(new Timestamp(format.parse("01-05-2022").getTime()));
				}
			}
			
		}
	}
	
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	public void assignDemandNoteDate(FinancialProjectMileStoneInfo mileStoneInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo,
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws Exception {
		log.info("getMasterDemandNoteDate "+mileStoneInfo.getMasterDemandNotedate()+", getMasterDemandNoteDueDate "+mileStoneInfo.getMasterDemandDueDate()+", getMileStoneNo "+mileStoneInfo.getMileStoneNo()+", getProjectMilestoneId"+mileStoneInfo.getProjectMilestoneId());
		Properties prop = responceCodesUtil.getApplicationProperties();
		//DUE_DATE_ADD_DAYS
		String FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL = prop.getProperty("FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL");
		String MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL = prop.getProperty("MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL");
		if(employeeFinancialServiceInfo.getActionUrl()!=null && "Final_Demand_Note".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL = "Final_Demand_Note";
			MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL = "Final_Demand_Note";
		}
		if(mileStoneInfo.getMileStoneNo()!=null && (mileStoneInfo.getMileStoneNo() == 1)) {
			mileStoneInfo.setDemandNoteDate(customerPropertyDetailsInfo.getBookingDate());
			mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),15));
			if(customerPropertyDetailsInfo.getSiteId().equals(126l)) {
				//IF BOOKING IS BEFORE 31-MARCH-2022 THEN ADD DEFAULT DUE DATE, by purnima and pavan sir
				if(customerPropertyDetailsInfo.getBookingDate().before(new Timestamp(format.parse("31-03-2022").getTime())) ||
						customerPropertyDetailsInfo.getBookingDate().equals(new Timestamp(format.parse("31-03-2022").getTime()))
						) {
					mileStoneInfo.setMileStoneDueDate(new Timestamp(format.parse("01-05-2022").getTime()));
				}
			}
			if(employeeFinancialServiceInfo.isThisUplaodedData()) {
				employeeFinancialServiceInfo.setActionUrl(FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL);
			}
		} else if(mileStoneInfo.getMileStoneNo()!=null && (mileStoneInfo.getMileStoneNo() == 2)) {
			/*if(mileStoneInfo.getMasterDemandNotedate() != null && employeeFinancialServiceInfo.getRequestUrl()!=null && !employeeFinancialServiceInfo.getRequestUrl().equalsIgnoreCase("ViewCustomerData")) {
				
			}*/
			//if the milestone is selected in front end, and milestone master date is empty then take demand note date and due whaterver selected in front end
			if(employeeFinancialServiceInfo.getCurrentprojectMileStoneIds()!=null && employeeFinancialServiceInfo.getCurrentprojectMileStoneIds().contains(mileStoneInfo.getProjectMilestoneId())
					&& mileStoneInfo.getMasterDemandNotedate() == null && (employeeFinancialServiceInfo.getRequestUrl()==null || !employeeFinancialServiceInfo.getRequestUrl().equalsIgnoreCase("ViewCustomerData"))
					) {
				
				if(employeeFinancialServiceInfo.isThisUplaodedData()) {
					employeeFinancialServiceInfo.setActionUrl(FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL);
				}
			} else {
				mileStoneInfo.setDemandNoteDate(customerPropertyDetailsInfo.getBookingDate());
				mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),30));
				
				if(customerPropertyDetailsInfo.getSiteId().equals(126l)) {
					//IF BOOKING IS BEFORE 31-MARCH-2022 THEN ADD DEFAULT DUE DATE, by purnima and pavan sir
					if(customerPropertyDetailsInfo.getBookingDate().before(new Timestamp(format.parse("31-03-2022").getTime())) ||
							customerPropertyDetailsInfo.getBookingDate().equals(new Timestamp(format.parse("31-03-2022").getTime()))
							) {
						mileStoneInfo.setMileStoneDueDate(new Timestamp(format.parse("01-05-2022").getTime()));
					}
				}
				
				if(employeeFinancialServiceInfo.isThisUplaodedData()) {
					employeeFinancialServiceInfo.setActionUrl(FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL);
				}
			}		
		} else {
			mileStoneInfo.setDemandNoteDate(mileStoneInfo.getDemandNoteDate());
			if(mileStoneInfo.getDemandNoteDate()!=null) {
				mileStoneInfo.setMileStoneDueDate(mileStoneInfo.getMileStoneDueDate());
			}
			if(employeeFinancialServiceInfo.isThisUplaodedData()) {
				int days = TimeUtil.differenceBetweenDays(customerPropertyDetailsInfo.getBookingDate(), mileStoneInfo.getDemandNoteDate());
				if (days > 0) {
					//if(employeeFinancialServiceInfo.isThisUplaodedData()) {
						employeeFinancialServiceInfo.setActionUrl(MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL);
					//}
				} else {
					//if(employeeFinancialServiceInfo.isThisUplaodedData()) {
						employeeFinancialServiceInfo.setActionUrl(FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL);
					//}	
				}		
			}
		}
		
		
		//01-01-2020 booking date 15-01-2020 show demand note date
		//15-01-2020 demand date  01-01-2020 show booking date
		//if master demand note after the booking date, show the master date as demand note date for this customer, for current processing milestone
		//this is related to milestone wise getMilestoneDemandNoteDate
		//this is related to Master data
		//this same logic used in mobile app
		if (mileStoneInfo.getMasterDemandNotedate() != null) {// && mileStoneInfo.getMilestoneDemandNoteDate() == null
			int days = TimeUtil.differenceBetweenDays(customerPropertyDetailsInfo.getBookingDate(), mileStoneInfo.getMasterDemandNotedate());
			if (days > 0) {
				if(employeeFinancialServiceInfo.isThisUplaodedData()) {
					employeeFinancialServiceInfo.setActionUrl(MILESTONE_COMPLETION_DN_FORMAT_FOR_EXCEL);
				}
				mileStoneInfo.setDemandNoteDate(mileStoneInfo.getMasterDemandNotedate());
				mileStoneInfo.setMileStoneDueDate(mileStoneInfo.getMasterDemandDueDate());
			} else {
				if(employeeFinancialServiceInfo.isThisUplaodedData()) {
					employeeFinancialServiceInfo.setActionUrl(FIRST_DISBURSEMENT_DN_FORMAT_FOR_EXCEL);
				}
				if(mileStoneInfo.getMileStoneNo() == 1) {
					mileStoneInfo.setDemandNoteDate(customerPropertyDetailsInfo.getBookingDate());
					mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),15));
					
					if(customerPropertyDetailsInfo.getSiteId().equals(126l)) {
						//IF BOOKING IS BEFORE 31-MARCH-2022 THEN ADD DEFAULT DUE DATE, by purnima and pavan sir
						if(customerPropertyDetailsInfo.getBookingDate().before(new Timestamp(format.parse("31-03-2022").getTime())) ||
								customerPropertyDetailsInfo.getBookingDate().equals(new Timestamp(format.parse("31-03-2022").getTime()))
								) {
							mileStoneInfo.setMileStoneDueDate(new Timestamp(format.parse("01-05-2022").getTime()));
						}
					}
					
				} else if(mileStoneInfo.getMileStoneNo() == 2) {
					/*if(employeeFinancialServiceInfo.getCurrentprojectMileStoneIds()!=null && employeeFinancialServiceInfo.getCurrentprojectMileStoneIds().contains(mileStoneInfo.getProjectMilestoneId())
							&& mileStoneInfo.getMasterDemandNotedate() == null && (employeeFinancialServiceInfo.getRequestUrl()==null || !employeeFinancialServiceInfo.getRequestUrl().equalsIgnoreCase("ViewCustomerData"))
							) {
						
					} else {*/
						mileStoneInfo.setDemandNoteDate(customerPropertyDetailsInfo.getBookingDate());
						mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),30));
						
						if(customerPropertyDetailsInfo.getSiteId().equals(126l)) {
							//IF BOOKING IS BEFORE 31-MARCH-2022 THEN ADD DEFAULT DUE DATE, by purnima and pavan sir
							if(customerPropertyDetailsInfo.getBookingDate().before(new Timestamp(format.parse("31-03-2022").getTime())) ||
									customerPropertyDetailsInfo.getBookingDate().equals(new Timestamp(format.parse("31-03-2022").getTime()))
									) {
								mileStoneInfo.setMileStoneDueDate(new Timestamp(format.parse("01-05-2022").getTime()));
							}
						}
						
				   //}	
				} else {
					mileStoneInfo.setDemandNoteDate(customerPropertyDetailsInfo.getBookingDate());
					mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(customerPropertyDetailsInfo.getBookingDate()),30));
				}
			}
		}		
	}

	public List<FileInfo> generateAllotmentLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper
			, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);

		@SuppressWarnings("unchecked")//here in this object customer details
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")//here in this object customer flat details, like flat no
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")//office details of the project
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";

		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());
		
		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		String rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		String leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);

		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Allotment Letter.pdf");
		info.setDemandNotePdfFileName("Allotment Letter - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");

		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		if(info.getCompanyCity().equals("Bengaluru")) {
          	if(!info.getSiteId().equals("124")) {
          		customerUnitDetails.put("chargesMsg","*The above mentioned Flat Cost is excluding Registration,Stamp duty,BWSSB charges and any other statutory charges.");
			} else {
				customerUnitDetails.put("chargesMsg","");
			}
		} else {
			customerUnitDetails.put("chargesMsg","*The above mentioned Flat Cost is excluding Registration,Stamp duty,HMWSSB charges and any other statutory charges.");
		}
		
		if(info.getSiteId().equals("134")) {
			customerUnitDetails.put("chargesMsg","The above mentioned Flat Cost is excluding Registration,Stamp duty charges and any other statutory charges.");
			info.setGreetingsFrom("With Greetings for the day!");
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom==null?"-":thanksAndRegardsFrom.toUpperCase());
			info.setCompanyName(info.getCompanyName().toUpperCase());
		}
		if(info.getSiteId().equals("114")) {
			customerUnitDetails.put("chargesMsg","*The above-mentioned Flat Cost is excluding Registration,Stamp duty,HMWSSB charges and any other statutory charges.");
			info.setGreetingsFrom("With Greetings for the day!");
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom==null?"-":thanksAndRegardsFrom.toUpperCase());
			info.setCompanyName(info.getCompanyName().toUpperCase());
		}
		if(info.getSiteId().equals("133")) {
			customerUnitDetails.put("chargesMsg","*The above-mentioned Flat Cost is excluding Registration,Stamp duty,HMWSSB charges and any other statutory charges.");
			info.setGreetingsFrom("With Greetings for the day!");
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom==null?"-":thanksAndRegardsFrom.toUpperCase());
			info.setCompanyName(info.getCompanyName().toUpperCase());
		}
		email.setDemandNoteGeneratorInfo(info);
		pdfHelper.XMLWorkerHelperForAllotmentLetter(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	public List<FileInfo> generateOlympusCostBreakUpLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		
		@SuppressWarnings("unchecked")
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStones =  new ArrayList<>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		@SuppressWarnings("unchecked")
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		@SuppressWarnings("unchecked")
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
		@SuppressWarnings("unchecked")
		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
		List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
		/*Venkatesh B*/
		for (AminitiesInfraCostInfo costInfo : aminitiesInfraCostInfoList) {
			if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - CORNER FLAT")) {
				email.setPlcCornerflatTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				customerUnitDetails.put("PLC - CORNER FLAT", costInfo.getTotalCost());//Added in customerUnitDetails 
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - EAST FACING")) {
				email.setPlcEastFacingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("INFRA AND AMENITIES")) {
				email.setInfraAndAmenitesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("FLOOR RISE")) {
				email.setFloorRiseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - PREMIUM FACING CHARGES")) {
				email.setPlcPremiumFacingChanrgesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CAR PARKING")) {
				email.setCarPrakingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CLUB HOUSE")) {
				email.setClubHouseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			}
		}
		
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestoneAmount = 0.0;
		if(mileStones!=null) {
			for (MileStoneInfo ms : mileStones) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String,Object> mileStone = new HashMap<>();
				double percentage = Double.valueOf(ms.getDue());
				double amount =  Double.valueOf(ms.getTotalDueAmount());
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",BigDecimal.valueOf(percentage).setScale(0, roundingMode).toString());
				mileStone.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amount).setScale(roundingModeSize, roundingMode).toString()));
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				sumofMilestonePercentgae += percentage;
				sumofMilestoneAmount += amount;
				mileStonesList.add(mileStone);
			}
		}
		Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);
		
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		if(siteOtherChargesDetails!=null) {
			Map<String,Object> otherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo other : siteOtherChargesDetails) {
				if(other.getAmount()!=null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
				}
			}
			siteOtherChargesDetailsList.add(otherDetails);
		}
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		email.setDataForPdf(dataForPdf1);

		if("Indimax".equals(customerPropertyDetailsInfo.getFlatSaleOwner())) {
			customerDetails.put("HeadingName","INDMAX INFRASTRUCTURE INDIA PVT LTD");
		} else {
			customerDetails.put("HeadingName","SUMADHURA VASAVI INFRASTRUCTURE LLP");
		}
		
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());
		
		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		
		//#ACP
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
		info.setDemandNotePdfFileName("Cost BreakUp - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setDemandNoteGeneratorInfo(info);
		info.setCompanyName(info.getCompanyName().toUpperCase());
		
		email.setTemplateName("/vmtemplates/COST_BREAK_UP_OLYMPUS.vm");
		pdfHelper.XMLWorkerHelperForCostBreakUpLetter(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	
	public List<FileInfo> generateCostBreakUpLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		
		@SuppressWarnings("unchecked")
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStones =  new ArrayList<>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		@SuppressWarnings("unchecked")
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		@SuppressWarnings("unchecked")
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
				
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestoneAmount = 0.0;
		if(mileStones!=null) {
			for (MileStoneInfo ms : mileStones) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String,Object> mileStone = new HashMap<>();
				double percentage = Double.valueOf(ms.getDue());
				double amount =  Double.valueOf(ms.getTotalDueAmount());
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",BigDecimal.valueOf(percentage).setScale(0, roundingMode).toString());
				mileStone.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amount).setScale(roundingModeSize, roundingMode).toString()));
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				sumofMilestonePercentgae += percentage;
				sumofMilestoneAmount += amount;
				mileStonesList.add(mileStone);
			}
		}
		Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);
		
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		
		if(siteOtherChargesDetails!=null) {
			Map<String,Object> otherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo other : siteOtherChargesDetails) {
				if(other.getAmount()!=null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					if (customerPropertyDetailsInfo.getSiteId().equals(124l)) {
						if ("103".equals(other.getMeteDataTypeId().toString())) {
							otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount() / 2).setScale(roundingModeSize, roundingMode).toString()));
						} else {
							otherDetails.put(other.getMeteDataTypeId().toString(),
									currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
						}
					} else {
						otherDetails.put(other.getMeteDataTypeId().toString(),
								currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount())
										.setScale(roundingModeSize, roundingMode).toString()));
					}
				}
			}
			siteOtherChargesDetailsList.add(otherDetails);
		}
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());
		
		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		
		//#ACP
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
		info.setDemandNotePdfFileName("Cost BreakUp - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setDemandNoteGeneratorInfo(info);
		email.setTemplateName("/vmtemplates/COST_BREAK_UP_AND_PAYMENT_SCHEDULE.vm");
		
		pdfHelper.XMLWorkerHelperForCostBreakUpLetter(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	public List<FileInfo> generateGardenByTheBrookBreakUpLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		
		@SuppressWarnings("unchecked")
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStones = new ArrayList<>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		@SuppressWarnings("unchecked")
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		@SuppressWarnings("unchecked")
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
		
		@SuppressWarnings("unchecked")
		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
		List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
		/*Venkatesh B*/
		for (AminitiesInfraCostInfo costInfo : aminitiesInfraCostInfoList) {
			if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - CORNER FLAT")) {
				email.setPlcCornerflatTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				customerUnitDetails.put("PLC - CORNER FLAT", costInfo.getTotalCost());//Added in customerUnitDetails 
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - EAST FACING")) {
				email.setPlcEastFacingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("INFRA AND AMENITIES")) {
				email.setInfraAndAmenitesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("FLOOR RISE")) {
				email.setFloorRiseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - PREMIUM FACING CHARGES")) {
				email.setPlcPremiumFacingChanrgesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CAR PARKING")) {
				email.setCarPrakingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CLUB HOUSE")) {
				email.setClubHouseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
			}
		}

		
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestoneAmount = 0.0;
		if(mileStones!=null) {
			for (MileStoneInfo ms : mileStones) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String,Object> mileStone = new HashMap<>();
				double percentage = Double.valueOf(ms.getDue());
				double amount =  Double.valueOf(ms.getTotalDueAmount());
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",BigDecimal.valueOf(percentage).setScale(0, roundingMode).toString());
				mileStone.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amount).setScale(roundingModeSize, roundingMode).toString()));
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				sumofMilestonePercentgae += percentage;
				sumofMilestoneAmount += amount;
				mileStonesList.add(mileStone);
			}
		}
		
		Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);
		
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		if(siteOtherChargesDetails!=null) {
			Map<String,Object> otherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo other : siteOtherChargesDetails) {
				if(other.getAmount()!=null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					//otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
					if("103".equals(other.getMeteDataTypeId().toString()) && Long.valueOf(133L).equals(customerPropertyDetailsInfo.getSiteId())) {///now
				     	otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
					} else {
						otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
					}
				}
			}
			siteOtherChargesDetailsList.add(otherDetails);
		}
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());
		
		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		
		//#ACP
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
		info.setDemandNotePdfFileName("Cost BreakUp - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		//------------------------Set Office details of the project
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);

		email.setDemandNoteGeneratorInfo(info);
		//email.setTemplateName("/vmtemplates/COST_BREAK_UP_AND_PAYMENT_SCHEDULE.vm");
		
		email.setTemplateName("/vmtemplates/COST_BREAK_UP_HORIZON.vm");
		pdfHelper.XMLWorkerHelperForCostBreakUpLetter(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<FileInfo> generateAgreementDraftHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception{
		log.info("***** Control inside the EmployeeFinancialHelper.generateAgreementDraftHelper() *****");
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStonesDetailsList =  new ArrayList<MileStoneInfo>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
		
		List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos =  (List<FinTransactionEntryDetailsInfo>) dataForGenerateAllotmentLetterHelper.get("transactionEntryDetailsInfos");
		List<FinPenaltyTaxPojo> taxDetailsList = (List<FinPenaltyTaxPojo>) dataForGenerateAllotmentLetterHelper.get("taxDetailsList");
		List<CustomerInfo> customerDetailsList = (List<CustomerInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantDetails");
		List<Co_ApplicantInfo> coApplicantDetailsList = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
		
		List<ProfessionalInfo> firstApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantProfessionalDetails");
		List<ProfessionalInfo> coApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantProfessionalDetails");	
		
		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
		Double schemeGSTPercentage = (Double) customerDetails.get("schemeGSTPercentage");
		Double totalMilestonePaidAmount = (Double) customerDetails.get("totalMilestonePaidAmount");
		FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
		//FinPenaltyTaxPojo finTaxPojo = taxDetailsList.get(0);
		FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
		List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
		
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double totalAgreementCost = 0.0;
		@SuppressWarnings("unused")
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestoneAmount = 0.0;
		double maintenancePercentage = 0.0;
		double maintenanceAmount = 0.0;
		double sumOfBasicCost = 0.0;
		double sumOfAllComponentGstAmount = 0.0;
		double grandTotalIncludingMaintenance = 0.0;
		//double sumOfTotalAmount = 0.0;
		long serialNo = 1l;
		double totalFlatCostExcludingGST = 0.0;
		double sbuaIntoCarpusFundSum = 0.0;
		double flatLegalAndDocumentationChargesAmt = 0.0;
		double legalCostPercentage = 0.0;
		//double sbuaValue=0.0;
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		if (taxDetailsList != null) {
			for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
				if (MetadataId.MAINTENANCE_CHARGE.getId().equals(finTaxPojo.getTaxType())) {
					maintenancePercentage = finTaxPojo.getPercentageValue();
				} else if (MetadataId.LEGAL_COST.getId().equals(finTaxPojo.getTaxType())) {
					legalCostPercentage = finTaxPojo.getPercentageValue();
				}
			}
		}
		
		if (siteOtherChargesDetails != null) {
			String strAmount = "";
			Map<String, Object> siteOtherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo otherDetailsDraft : siteOtherChargesDetails) {
				if (otherDetailsDraft.getAmount() != null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					if (MetadataId.MAINTENANCE_CHARGE.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//maintenanceAmount = other.getAmount();
						//maintenanceAmount = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						maintenanceAmount = (flatBookingInfo.getSbua()) * otherDetailsDraft.getAmount();
					} else if(MetadataId.CORPUS_FUND.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//sbuaValue = flatBookingInfo.getSbua();
						//sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() ) * otherDetailsDraft.getAmount();
					}else if(MetadataId.LEGAL_COST.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						flatLegalAndDocumentationChargesAmt =  otherDetailsDraft.getAmount();
					}
					strAmount = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(otherDetailsDraft.getAmount()).setScale(roundingModeSize, roundingMode).toString());
					otherDetailsDraft.setStrAmount(strAmount);
					otherDetailsDraft.setStrAmountInWords(getTheAmountInWords(otherDetailsDraft.getAmount()));
					
					siteOtherDetails.put(otherDetailsDraft.getMeteDataTypeId().toString(),otherDetailsDraft);
					
				}
			}
			siteOtherChargesDetailsList.add(siteOtherDetails);
		}
		System.out.println("maintenanceAmount " + (maintenanceAmount)+ (maintenanceAmount)*18);
		customerUnitDetails.put("sbuaIntoCarpusFundSum", sbuaIntoCarpusFundSum);
		customerUnitDetails.put("maintenanceAmount", maintenanceAmount);
		
		siteOtherChargesDetailsList.get(0).get("104");
		//maintenanceAmount = (flatBookingInfo.getSbua()*1d)*maintenanceAmount;
		List<AgreementDraftCalculations> agreementDraftCalculations = new ArrayList<>();
		if (flatCostInfo != null) {
			double gstAmount = 0.0;
			Double basicAmount = flatCostInfo.getBasicFlatCost();
			Double amenitiesFlatCost = flatCostInfo.getAmenitiesFlatCost();
			totalFlatCostExcludingGST = basicAmount+amenitiesFlatCost;
			totalAgreementCost = flatCostInfo.getTotalCost();
			
			double mileStonePercentage = schemeGSTPercentage;
			gstAmount = (basicAmount / 100) * mileStonePercentage;//for Sushantham site gstAmount on basicAmount 

			AgreementDraftCalculations data = new AgreementDraftCalculations();
			data.setSerialNo(serialNo);
			data.setParticulars("Cost of the Apartment");
			data.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			data.setGstPercentage(schemeGSTPercentage.toString());
			data.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(gstAmount).setScale(roundingModeSize, roundingMode).toString()));
			data.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()+gstAmount).setScale(roundingModeSize, roundingMode).toString()));

			agreementDraftCalculations.add(data);
			
			sumOfBasicCost += flatCostInfo.getBasicFlatCost();
			sumOfAllComponentGstAmount += gstAmount;
			serialNo++;
		}

		if (aminitiesInfraCostInfoList != null) {
			for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfoList) {
				if (aminitiesInfraCostInfo.getTotalCost() == null || aminitiesInfraCostInfo.getTotalCost() == 0) {
					continue;
				}
				System.out.println(aminitiesInfraCostInfo.getTotalCost()+" "+aminitiesInfraCostInfo.getAminititesInfraName()+" "+aminitiesInfraCostInfo.getAminititesInfraCost()+" "+aminitiesInfraCostInfo.getFlatCostId());
				//double amenitiesGstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(aminitiesInfraCostInfo.getTotalCost()));
				double amenitiesGstAmount = (aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100;
				System.out.println((aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100);
				//double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost()-amenitiesGstAmount;
				double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost();
				aminitiesInfraCostInfo.setGstAmount(amenitiesGstAmount);
		
				AgreementDraftCalculations data = new AgreementDraftCalculations();
				data.setSerialNo(serialNo);
				data.setParticulars(aminitiesInfraCostInfo.getAminititesInfraName());
				data.setBasicCost(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setGstPercentage(schemeGSTPercentage.toString());
				data.setGstAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setTotalAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount+amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				
				sumOfBasicCost += amenitiesBasicAmount;
				sumOfAllComponentGstAmount += amenitiesGstAmount;

				agreementDraftCalculations.add(data);
				serialNo++;
			}
		}
		double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
		double sumOfCustomerPaidAmount = 0.0d;
		boolean breakTheLoop = false;
		if(transactionEntryDetailsInfos!=null) {
			for (FinTransactionEntryDetailsInfo entryDetailsInfo : transactionEntryDetailsInfos) {
				if(Util.isEmptyObject(entryDetailsInfo.getBankName())) {
					entryDetailsInfo.setBankName("-");
				}
				double transactionAmount = entryDetailsInfo.getTransactionAmount();
				sumOfCustomerPaidAmount += transactionAmount;
				if(sumOfCustomerPaidAmount>tenPercentAmountOnFlat) {
					breakTheLoop = true;//need to show only 10% amount data
					transactionAmount = (sumOfCustomerPaidAmount-transactionAmount)-tenPercentAmountOnFlat;
				}
				//schemeGSTPercentage
				double transactionAmountExcludeGST = (transactionAmount*100/schemeGSTPercentage);//transactionAmount*(100/schemeGSTPercentage);
				double transactionAmountGST = (transactionAmount*5/105);
				entryDetailsInfo.setStrTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmount).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountInWords(getTheAmountInWords(transactionAmount));
				
				entryDetailsInfo.setStrTransactionAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountExcludeGSTInWords(getTheAmountInWords(transactionAmountExcludeGST));
				
				entryDetailsInfo.setStrTransactionAmountGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountGSTInWords(getTheAmountInWords(transactionAmountGST));
				if(breakTheLoop) {break;}
			}
		}
		//*************************************
		double msPercentage = 0.0;
		double msAmount =  0.0;
		double dueAmountExcludeGST = 0.0;
		double milestoneGstAmount = 0.0;
		String strMSAmt = "";
		String strMSPercentage = "";

		long milestoneNo = 0;
		if(mileStonesDetailsList!=null) {
			for (MileStoneInfo ms : mileStonesDetailsList) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String, Object> mileStone = new HashMap<>();
				msPercentage = Double.valueOf(ms.getDue());
				msAmount = Double.valueOf(ms.getTotalDueAmount());
				dueAmountExcludeGST = Double.valueOf(ms.getDueAmountExcludeGST());
				milestoneGstAmount = Double.valueOf(ms.getGstAmount());
				 
			    strMSAmt = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(msAmount).setScale(roundingModeSize, roundingMode).toString());
				strMSPercentage = BigDecimal.valueOf(msPercentage).setScale(0, roundingMode).toString();
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",strMSPercentage);
				mileStone.put("totalDueAmount", strMSAmt);
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				ms.setTotalDueAmount(strMSAmt);
				ms.setDue(strMSPercentage+"%");
				ms.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(dueAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				ms.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(milestoneGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				sumofMilestonePercentgae += msPercentage;
				sumofMilestoneAmount += msAmount;
				milestoneNo = ms.getMilStoneNo();
				mileStonesList.add(mileStone);
			}
		}
		
		/*Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);*/
		
		double flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * legalCostPercentage) / 100;
		double maintenanceGstAmount = (maintenanceAmount * maintenancePercentage) / 100;
		double maintenanceAmtWithGstAmount =  maintenanceAmount+maintenanceGstAmount;
		if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
			//this object for flat basic cost and amenities amount sum
			AgreementDraftCalculations MaintenanceData = new AgreementDraftCalculations();
			MaintenanceData.setSerialNo(serialNo);
			MaintenanceData.setParticulars("Maintenance for 12 Months");
			MaintenanceData.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setGstPercentage(String.valueOf(maintenancePercentage));
			MaintenanceData.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			agreementDraftCalculations.add(MaintenanceData);
		} else {
			maintenanceGstAmount = 0.0;
			maintenanceAmtWithGstAmount = 0.0;
		}
		sumOfBasicCost += maintenanceAmount;
		sumOfAllComponentGstAmount += maintenanceGstAmount;
		

		//totalAmtData object for flat basic cost and amenities amount sum
		AgreementDraftCalculations totalAmtDataObject = new AgreementDraftCalculations();
		totalAmtDataObject.setSerialNo(serialNo);
		totalAmtDataObject.setParticulars("Total");
		totalAmtDataObject.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setGstPercentage("");
		totalAmtDataObject.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost+sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		
		agreementDraftCalculations.add(totalAmtDataObject);
		/* Malladi Changes */
		if(customerPropertyDetailsInfo.getSiteId().equals(124l)) {
		//if(!customerPropertyDetailsInfo.getSiteId().equals(134l) && !customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			//Maintenance Charges object for milestone list
			MileStoneInfo maintenanceDetailsForMS = new MileStoneInfo();
			maintenanceDetailsForMS.setMilStoneNo(++milestoneNo);
			maintenanceDetailsForMS.setMilestoneName("Maintenance Charges payable on final payment request");
			maintenanceDetailsForMS.setDue("-");
			maintenanceDetailsForMS.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(maintenanceDetailsForMS);
			sumofMilestoneAmount+= maintenanceAmtWithGstAmount;
		}
		
		//total amount object for milestone list
		/* Malladi Changes */
		if(!customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			MileStoneInfo ms = new MileStoneInfo();
			ms.setMilStoneNo(++milestoneNo);
			ms.setMilestoneName("Total Amount");
			ms.setDue("");
			ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(ms);
		}
		
		double totalBalanceAmountOnFlat = 0.0;
		double tenPercentGSTAmount = 0.0;
		if (totalMilestonePaidAmount > tenPercentAmountOnFlat) {
			
		} else {
			tenPercentAmountOnFlat = totalMilestonePaidAmount;
		}
		tenPercentGSTAmount = (tenPercentAmountOnFlat*10)/100;//gst Amount on 10 % paid amount
		
		grandTotalIncludingMaintenance = sumOfBasicCost+sumOfAllComponentGstAmount;				
		totalBalanceAmountOnFlat = grandTotalIncludingMaintenance-tenPercentAmountOnFlat;
		
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenance(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalIncludingMaintenance).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceInWords(getTheAmountInWords(grandTotalIncludingMaintenance));
		welcomeMailGeneratorInfo.setTotalBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExcludingGST).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
				
		welcomeMailGeneratorInfo.setTotalBalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalBalanceAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalBalanceAmountInWords(getTheAmountInWords(totalBalanceAmountOnFlat));
		welcomeMailGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidAmountInWords(getTheAmountInWords(tenPercentAmountOnFlat));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmountInWords(getTheAmountInWords(tenPercentGSTAmount));
		
		
		customerUnitDetails.put("flat_legal_and_doc_charges_Sum_With_GST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("flat_legal_cost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt).setScale(roundingModeSize, roundingMode).toString()));
		
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		//dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String leftSidelogoForPdf = "";
		String rightSidelogoForPdf = "";
		String thanksAndRegardsFrom = "";
		String greetingsFrom = "";
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());

		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Agreement Draft.pdf");
		info.setDemandNotePdfFileName("Agreement Draft - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		welcomeMailGeneratorInfo.setCustomerDetailsList(customerDetailsList);
		welcomeMailGeneratorInfo.setCoApplicantDetailsList(coApplicantDetailsList);
		welcomeMailGeneratorInfo.setFirstApplicantProfessionalDetails(firstApplicantProfessionalDetails);
		welcomeMailGeneratorInfo.setCoApplicantProfessionalDetailsList(coApplicantProfessionalDetails);
		
		welcomeMailGeneratorInfo.setOfficeDetailsPojoList(officeDetailsPojoList);
		welcomeMailGeneratorInfo.setMileStones(mileStonesDetailsList);
		welcomeMailGeneratorInfo.setSiteOtherChargesDetails(siteOtherChargesDetails);
		welcomeMailGeneratorInfo.setTermsAndConditions(termsAndConditions);
		welcomeMailGeneratorInfo.setAgreementDraftCalculations(agreementDraftCalculations);
		welcomeMailGeneratorInfo.setTransactionEntryDetailsInfos(transactionEntryDetailsInfos);
		
		//welcomeMailGeneratorInfo.setDataForPdf(dataForPdf1);
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setWelcomeMailGeneratorInfo(welcomeMailGeneratorInfo);
		email.setDemandNoteGeneratorInfo(info);
		//template name to construct pdf
		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/* Malladi Changes */
		String flatSaleOwner = "";
		if(Util.isNotEmptyObject(customerPropertyDetailsInfo)) {
			flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner();
			if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				flatSaleOwner = customerPropertyDetailsInfo.getSiteId()+"_"+flatSaleOwner;	
			}
		}
		
		if(customerPropertyDetailsInfo.getSiteId().equals(131l) && flatSaleOwner.equalsIgnoreCase("131_Landlord")) {
			email.setTemplateName("/vmtemplates/Agreement_131_Landlord.vm");
		}else if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			email.setTemplateName("/vmtemplates/Agreement_131_Builder.vm");
		}else {
			email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		}
		pdfHelper.XMLWorkerHelperForAgreementDraftLetter(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}
	//====================================================
	@SuppressWarnings("unchecked")
	public List<FileInfo> generateOlympusAgreementDraftHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception{
		log.info("***** Control inside the EmployeeFinancialHelper.generateOlympusAgreementDraftHelper() *****");
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStonesDetailsList =  new ArrayList<MileStoneInfo>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
		
		List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos =  (List<FinTransactionEntryDetailsInfo>) dataForGenerateAllotmentLetterHelper.get("transactionEntryDetailsInfos");
		List<FinPenaltyTaxPojo> taxDetailsList = (List<FinPenaltyTaxPojo>) dataForGenerateAllotmentLetterHelper.get("taxDetailsList");
		List<CustomerInfo> customerDetailsList = (List<CustomerInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantDetails");
		List<Co_ApplicantInfo> coApplicantDetailsList = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
		
		List<ProfessionalInfo> firstApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantProfessionalDetails");
		List<ProfessionalInfo> coApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantProfessionalDetails");	
		
		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
		Double schemeGSTPercentage = (Double) customerDetails.get("schemeGSTPercentage");
		Double totalMilestonePaidAmount = (Double) customerDetails.get("totalMilestonePaidAmount");
		FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
		//FinPenaltyTaxPojo finTaxPojo = taxDetailsList.get(0);
		FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
		List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
		
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double totalAgreementCost = 0.0;
		
		@SuppressWarnings("unused")
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestonePercentgaeExclFirst = 0.0;//sum of all milestone percentage but excluding first
		@SuppressWarnings("unused")
		double sumofMilestoneAmount = 0.0;
		double sumofMilestoneAmountExclFirst = 0.0;//sum of all milestone amount but excluding first
		double maintenancePercentage = 0.0;
		double legalCostPercentage = 0.0;
		double maintenanceAmount = 0.0;
		double sumOfBasicCost = 0.0;
		double sumOfAllComponentGstAmount = 0.0;
		double grandTotalIncludingMaintenance = 0.0;
		//double sumOfTotalAmount = 0.0;
		long serialNo = 1l;
		double totalFlatCostExcludingGST = 0.0;
		double sbuaIntoCarpusFundSum = 0.0;
		double flatLegalAndDocumentationChargesAmt = 0.0;
		//double sbuaValue=0.0;
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		
		if (taxDetailsList != null) {
			for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
				if (MetadataId.MAINTENANCE_CHARGE.getId().equals(finTaxPojo.getTaxType())) {
					maintenancePercentage = finTaxPojo.getPercentageValue();
				} else if (MetadataId.LEGAL_COST.getId().equals(finTaxPojo.getTaxType())) {
					legalCostPercentage = finTaxPojo.getPercentageValue();
				}
			}
		}
		
		if (siteOtherChargesDetails != null) {
			String strAmount = "";
			Map<String, Object> siteOtherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo otherDetailsDraft : siteOtherChargesDetails) {
				if (otherDetailsDraft.getAmount() != null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					if (MetadataId.MAINTENANCE_CHARGE.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//if this code is changed same code to be changed in NOC validation code
						//maintenanceAmount = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						maintenanceAmount = flatBookingInfo.getSbua() * otherDetailsDraft.getAmount();
					} else if(MetadataId.CORPUS_FUND.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//sbuaValue = flatBookingInfo.getSbua();
						//sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() ) * otherDetailsDraft.getAmount();
					}  else if(MetadataId.LEGAL_COST.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						flatLegalAndDocumentationChargesAmt =  otherDetailsDraft.getAmount();
					}
					strAmount = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(otherDetailsDraft.getAmount()).setScale(roundingModeSize, roundingMode).toString());
					otherDetailsDraft.setStrAmount(strAmount);
					otherDetailsDraft.setStrAmountInWords(getTheAmountInWords(otherDetailsDraft.getAmount()));
					
					siteOtherDetails.put(otherDetailsDraft.getMeteDataTypeId().toString(),otherDetailsDraft);
					
				}
			}
			siteOtherChargesDetailsList.add(siteOtherDetails);
		}
		siteOtherChargesDetailsList.get(0).get("104");
		//maintenanceAmount = (flatBookingInfo.getSbua()*1d)*maintenanceAmount;
		List<AgreementDraftCalculations> agreementDraftCalculations = new ArrayList<>();
		if (flatCostInfo != null) {
			double gstAmount = 0.0;
			Double basicAmount = flatCostInfo.getBasicFlatCost();
			Double amenitiesFlatCost = flatCostInfo.getAmenitiesFlatCost();
			totalFlatCostExcludingGST = basicAmount+amenitiesFlatCost;
			totalAgreementCost = flatCostInfo.getTotalCost();
			
			double mileStonePercentage = schemeGSTPercentage;
			gstAmount = (basicAmount / 100) * mileStonePercentage;//for Sushantham site gstAmount on basicAmount 

			AgreementDraftCalculations data = new AgreementDraftCalculations();
			data.setSerialNo(serialNo);
			data.setParticulars("Cost of the Apartment");
			data.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			data.setGstPercentage(schemeGSTPercentage.toString());
			data.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(gstAmount).setScale(roundingModeSize, roundingMode).toString()));
			data.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()+gstAmount).setScale(roundingModeSize, roundingMode).toString()));

			agreementDraftCalculations.add(data);
			
			sumOfBasicCost += flatCostInfo.getBasicFlatCost();
			sumOfAllComponentGstAmount += gstAmount;
			serialNo++;
		}

		if (aminitiesInfraCostInfoList != null) {//taking all the amenities details
			for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfoList) {
				if (aminitiesInfraCostInfo.getTotalCost() == null || aminitiesInfraCostInfo.getTotalCost() == 0) {
					continue;
				}
				System.out.println(aminitiesInfraCostInfo.getTotalCost()+" "+aminitiesInfraCostInfo.getAminititesInfraName()+" "+aminitiesInfraCostInfo.getAminititesInfraCost()+" "+aminitiesInfraCostInfo.getFlatCostId());
				//double amenitiesGstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(aminitiesInfraCostInfo.getTotalCost()));
				double amenitiesGstAmount = (aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100;
				System.out.println((aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100);
				//double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost()-amenitiesGstAmount;
				double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost();
				aminitiesInfraCostInfo.setGstAmount(amenitiesGstAmount);
		
				AgreementDraftCalculations data = new AgreementDraftCalculations();
				data.setSerialNo(serialNo);
				data.setParticulars(aminitiesInfraCostInfo.getAminititesInfraName());
				data.setBasicCost(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setGstPercentage(schemeGSTPercentage.toString());
				data.setGstAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setTotalAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount+amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				
				sumOfBasicCost += amenitiesBasicAmount;
				sumOfAllComponentGstAmount += amenitiesGstAmount;

				agreementDraftCalculations.add(data);
				serialNo++;
			}
		}
		double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
		double sumOfCustomerPaidAmount = 0.0d;
		boolean breakTheLoop = false;
		double transactionAmount = 0.0;
		double transactionAmountGST = 0.0;
		double transactionAmountExcludeGST = 0.0;
		List<FinTransactionEntryDetailsInfo> copytransactionEntryDetailsInfos = new ArrayList<>();
		if(transactionEntryDetailsInfos!=null) {//taking transaction amount upto 10% paid
			for (FinTransactionEntryDetailsInfo entryDetailsInfo : transactionEntryDetailsInfos) {
				if(Util.isEmptyObject(entryDetailsInfo.getBankName())) {
					entryDetailsInfo.setBankName("-");
				}
				transactionAmount = entryDetailsInfo.getTransactionAmount();
				sumOfCustomerPaidAmount += transactionAmount;
				if(sumOfCustomerPaidAmount>=tenPercentAmountOnFlat) {
					breakTheLoop = true;//need to show only 10% amount data
					transactionAmount = (sumOfCustomerPaidAmount-transactionAmount)-tenPercentAmountOnFlat;
				}
				//schemeGSTPercentage double gstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(transactionAmount);
				transactionAmountGST = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(transactionAmount));
				transactionAmountExcludeGST = transactionAmount-transactionAmountGST;//transactionAmount*(100/schemeGSTPercentage);
				
				entryDetailsInfo.setStrTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmount).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountInWords(getTheAmountInWords(transactionAmount));
				
				entryDetailsInfo.setStrTransactionAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountExcludeGSTInWords(getTheAmountInWords(transactionAmountExcludeGST));
				
				entryDetailsInfo.setStrTransactionAmountGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountGSTInWords(getTheAmountInWords(transactionAmountGST));
				copytransactionEntryDetailsInfos.add(entryDetailsInfo);
				if(breakTheLoop) {break;}
			}
		}
		transactionEntryDetailsInfos = copytransactionEntryDetailsInfos;
		//*************************************
		double msPercentage = 0.0;
		double msAmount =  0.0;
		double dueAmountExcludeGST = 0.0;
		double milestoneGstAmount = 0.0;
		String strMSAmt = "";
		String strMSPercentage = "";

		@SuppressWarnings("unused")
		long milestoneNo = 0;
		if(mileStonesDetailsList!=null) {
			for (MileStoneInfo ms : mileStonesDetailsList) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String, Object> mileStone = new HashMap<>();
				msPercentage = Double.valueOf(ms.getDue());
				msAmount = Double.valueOf(ms.getTotalDueAmount());
				dueAmountExcludeGST = Double.valueOf(ms.getDueAmountExcludeGST());
				milestoneGstAmount = Double.valueOf(ms.getGstAmount());
				 
			    strMSAmt = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(msAmount).setScale(roundingModeSize, roundingMode).toString());
				strMSPercentage = BigDecimal.valueOf(msPercentage).setScale(0, roundingMode).toString();
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",strMSPercentage);
				mileStone.put("totalDueAmount", strMSAmt);
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				ms.setTotalDueAmount(strMSAmt);
				ms.setDue(strMSPercentage+"%");
				ms.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(dueAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				ms.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(milestoneGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				sumofMilestonePercentgae += msPercentage;
				sumofMilestoneAmount += msAmount;
				//milestoneNo = ms.getMilStoneNo();
				if (ms.getMilStoneNo() != 1) {
					sumofMilestonePercentgaeExclFirst += msPercentage;
					sumofMilestoneAmountExclFirst += msAmount;
				}
				
				mileStonesList.add(mileStone);
			}
		}
		
		/*Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);*/
		
		double maintenanceGstAmount = (maintenanceAmount * maintenancePercentage) / 100;
		double flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * legalCostPercentage) / 100;
		//double maintenanceAmtWithGstAmount =  maintenanceAmount+maintenanceGstAmount;
		if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
			//this object for flat basic cost and amenities amount sum
			/*AgreementDraftCalculations MaintenanceData = new AgreementDraftCalculations();
			MaintenanceData.setSerialNo(serialNo);
			MaintenanceData.setParticulars("Maintenance for 12 Months");
			MaintenanceData.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setGstPercentage(String.valueOf(maintenancePercentage));
			MaintenanceData.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			agreementDraftCalculations.add(MaintenanceData);*/
		} else {//Maintenance amount not adding for olympus flat basic flat and gst
			//maintenanceGstAmount = 0.0;
			//maintenanceAmount = 0.0;
			//maintenanceAmtWithGstAmount = 0.0;
		}
		System.out.println((96*2880)+ ((96*2880)*18/100));
		System.out.println(maintenanceGstAmount+maintenanceAmount);		

		//totalAmtData object for flat basic cost and amenities amount sum
		AgreementDraftCalculations totalAmtDataObject = new AgreementDraftCalculations();
		totalAmtDataObject.setSerialNo(serialNo);
		totalAmtDataObject.setParticulars("Total");
		totalAmtDataObject.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setGstPercentage("");
		totalAmtDataObject.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost+sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		
		agreementDraftCalculations.add(totalAmtDataObject);
		/*if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
			//Maintenance Charges object for milestone list
			MileStoneInfo maintenanceDetailsForMS = new MileStoneInfo();
			maintenanceDetailsForMS.setMilStoneNo(++milestoneNo);
			maintenanceDetailsForMS.setMilestoneName("Maintenance Charges payable on final payment request");
			maintenanceDetailsForMS.setDue("-");
			maintenanceDetailsForMS.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(maintenanceDetailsForMS);
			sumofMilestoneAmount+= maintenanceAmtWithGstAmount;
		}*/
		
		//total amount object for milestone list
		MileStoneInfo ms = new MileStoneInfo();
		ms.setMilStoneNo(null);
		ms.setMilestoneName("Total Amount");
		ms.setDue(BigDecimal.valueOf(sumofMilestonePercentgaeExclFirst).setScale(0, roundingMode).toString()+"%");
		//ms.setDue(BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString()+"%");
		ms.setDueAmountExcludeGST("");
		ms.setGstAmount("");
		ms.setThisLastMsRecord(true);
		ms.isThisLastMsRecord();
		//ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmountExclFirst).setScale(roundingModeSize, roundingMode).toString()));
		mileStonesDetailsList.add(ms);
		
		
		double totalBalanceAmountOnFlat = 0.0;
		double tenPercentGSTAmount = 0.0;
		if (totalMilestonePaidAmount > tenPercentAmountOnFlat) {
			
		} else {//if 10% percent of total flat amount is not paid, then take only paid amount of milestone
			tenPercentAmountOnFlat = totalMilestonePaidAmount;
		}
		//tenPercentGSTAmount = (tenPercentAmountOnFlat*10)/100;//gst Amount on 10 % paid amount
		tenPercentGSTAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(tenPercentAmountOnFlat));//gst Amount on 10 % paid amount
		grandTotalIncludingMaintenance = sumOfBasicCost+sumOfAllComponentGstAmount;
		totalBalanceAmountOnFlat = grandTotalIncludingMaintenance-tenPercentAmountOnFlat;
		
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenance(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalIncludingMaintenance).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceInWords(getTheAmountInWords(grandTotalIncludingMaintenance));
		welcomeMailGeneratorInfo.setTotalBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExcludingGST).setScale(roundingModeSize, roundingMode).toString()));
		//welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
		//welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
		
		//total booking balance amount
		welcomeMailGeneratorInfo.setTotalBalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalBalanceAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalBalanceAmountInWords(getTheAmountInWords(totalBalanceAmountOnFlat));
		
		//10 percent showing as paid amount, if amount paid more than 20% also
		welcomeMailGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidAmountInWords(getTheAmountInWords(tenPercentAmountOnFlat));
		
		//paid amount gst
		welcomeMailGeneratorInfo.setTotalPaidGSTAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmountInWords(getTheAmountInWords(tenPercentGSTAmount));
		
		//paid amount exclusing gst
		welcomeMailGeneratorInfo.setTotalPaidAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat-tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidAmountExcludeGSTInWords(getTheAmountInWords(tenPercentAmountOnFlat-tenPercentGSTAmount));		
		
		customerUnitDetails.put("sbuaIntoCarpusFundSum",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("sbuaIntoMaintenanceSumWithGST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount+maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("flat_legal_and_doc_charges_Sum_With_GST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("flat_legal_cost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("schedule_c_payment_plan_sum",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum+(maintenanceGstAmount+maintenanceAmount)+(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount)).setScale(roundingModeSize, roundingMode).toString()));
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		//dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String leftSidelogoForPdf = "";
		String rightSidelogoForPdf = "";
		String thanksAndRegardsFrom = "";
		String greetingsFrom = "";
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());

		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Agreement Draft.pdf");
		info.setDemandNotePdfFileName("Agreement Draft - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		welcomeMailGeneratorInfo.setCustomerDetailsList(customerDetailsList);
		welcomeMailGeneratorInfo.setCoApplicantDetailsList(coApplicantDetailsList);
		welcomeMailGeneratorInfo.setFirstApplicantProfessionalDetails(firstApplicantProfessionalDetails);
		welcomeMailGeneratorInfo.setCoApplicantProfessionalDetailsList(coApplicantProfessionalDetails);
		welcomeMailGeneratorInfo.setOfficeDetailsPojoList(officeDetailsPojoList);
		welcomeMailGeneratorInfo.setMileStones(mileStonesDetailsList);
		welcomeMailGeneratorInfo.setSiteOtherChargesDetails(siteOtherChargesDetails);
		welcomeMailGeneratorInfo.setTermsAndConditions(termsAndConditions);
		welcomeMailGeneratorInfo.setAgreementDraftCalculations(agreementDraftCalculations);
		welcomeMailGeneratorInfo.setTransactionEntryDetailsInfos(transactionEntryDetailsInfos);
		
		//welcomeMailGeneratorInfo.setDataForPdf(dataForPdf1);
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setWelcomeMailGeneratorInfo(welcomeMailGeneratorInfo);
		email.setDemandNoteGeneratorInfo(info);
		//template name to construct pdf
		
		if(customerPropertyDetailsInfo.getFlatSaleOwner()!=null && customerPropertyDetailsInfo.getFlatSaleOwner().equalsIgnoreCase("Indimax")) {
			email.setTemplateName("/vmtemplates/Agreement_134_INDMAX_Draft.vm");	
			pdfHelper.XMLWorkerHelperForAgreementDraftLetter(email, fileInfo);
		} else {
			email.setTemplateName("/vmtemplates/Agreement_134_VASAVI_Draft.vm");
			pdfHelper.XMLWorkerHelperForAgreementDraftLetterOlympus(email, fileInfo);
		}
		
		//can uncomment this line if again we are making vasavi draft dynamic
		//pdfHelper.XMLWorkerHelperForAgreementDraftLetter(email, fileInfo);
		
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	
	public List<FileInfo> welcomeMailLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<EmployeePojo> employeePojos) throws Exception {
		//Properties prop = responceCodesUtil.getApplicationProperties();
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		String empMobileNumber="";
		/*Long siteId = customerPropertyDetailsInfo.getSiteId()==null?0l:customerPropertyDetailsInfo.getSiteId();
		if(siteId!=null && siteId.equals(131l)) {
			logoForPdf = prop.getProperty("ASPIRE_LOGO1");
			thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("ASPIRE_GREETING_MSG_FROM");
		} else {
			logoForPdf = prop.getProperty("SUMADHURA_LOGO1");
			thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");
		}*/
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());		

		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);

		
		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		@SuppressWarnings("unchecked")
		List<AddressInfo> siteAddressInfoList = (List<AddressInfo>) dataForGenerateAllotmentLetterHelper.get("siteAddressInfoList");
		@SuppressWarnings("unchecked")
		List<FinProjectAccountResponse> finProjectAccountResponseList = (List<FinProjectAccountResponse>) dataForGenerateAllotmentLetterHelper.get("finProjectAccountResponseList");
		@SuppressWarnings("unchecked")
		List<EmployeeDetailsMailPojo> empployeeDetails = (List<EmployeeDetailsMailPojo>) dataForGenerateAllotmentLetterHelper.get("empployeeDetails");
		
		for(EmployeeDetailsMailPojo emp: empployeeDetails)
		{
			if (Util.isEmptyObject(empMobileNumber)) {
				empMobileNumber = emp.getMobileNumber()==null?"N/A":emp.getMobileNumber();
			} else {
				if (Util.isEmptyObject(emp.getMobileNumber())) {
				empMobileNumber = empMobileNumber + ","+emp.getMobileNumber();
				}
			}
			
		}
		double balanceBookingAmount=0.0;
		double totalAgreementCost=customerPropertyDetailsInfo.getTotalAgreementCost();
		double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
		double totalMilestonePaidAmount=(double) customerDetails.get("totalMilestonePaidAmount");
		balanceBookingAmount=tenPercentAmountOnFlat-totalMilestonePaidAmount;
		if(0>balanceBookingAmount) {
			balanceBookingAmount=0;
		}
		
		String strBalanceBookingAmount=currencyUtil.convertUstoInFormat(BigDecimal.valueOf(balanceBookingAmount).setScale(roundingModeSize, roundingMode).toString());
		String totalMilestonePaidAmountWithComma=currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalMilestonePaidAmount).setScale(roundingModeSize, roundingMode).toString());
		customerDetails.put("totalMilestonePaidAmountWithComma",totalMilestonePaidAmountWithComma);
		customerDetails.put("empMobileNumber", empMobileNumber);
		customerDetails.put("balanceBookingAmount", strBalanceBookingAmount);
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		getSiteAdderss(siteAddressInfoList,info,customerPropertyDetailsInfo);
		
		/* setting site Account  */
     	if(Util.isNotEmptyObject(finProjectAccountResponseList)) {
     		StringBuilder accountInfo = new StringBuilder();
     		for(FinProjectAccountResponse accountResponse :finProjectAccountResponseList) {
     			/* setting bank details  */
     			if(Util.isNotEmptyObject(accountResponse.getBankName())){
     				accountInfo.append(accountResponse.getBankName());
     				accountInfo.append(",");
     				customerDetails.put("accountBankName",accountResponse.getBankName());
     			}
     			if(Util.isNotEmptyObject(accountResponse.getAccountHolderName())){
     				info.setAccountHolderName(accountResponse.getAccountHolderName());
     				customerDetails.put("accountHolderName",accountResponse.getBankName());
     			} else {
     				throw new EmployeeFinancialServiceException("Account holder name missing..!");
     			}
     			/* setting bank account details  */
     			if(Util.isNotEmptyObject(accountResponse.getSiteBankAccountNumber())){
     				accountInfo.append("<strong> A/C. "+accountResponse.getSiteBankAccountNumber()+"</strong>");
     				accountInfo.append(",");
     				customerDetails.put("accountBankNumber",accountResponse.getSiteBankAccountNumber());
     			}
     			
     			/* setting Account Address */
     			if(Util.isNotEmptyObject(accountResponse.getAccountAddress())){
     				accountInfo.append(accountResponse.getAccountAddress());
     				accountInfo.append(",");
     				customerDetails.put("accountBankAddress",accountResponse.getAccountAddress());
     			}
     			
     			/* setting Account IFSC Code */
     			if(Util.isNotEmptyObject(accountResponse.getIfscCode())){
     				accountInfo.append(" IFSC CODE - ");
     				accountInfo.append(accountResponse.getIfscCode());
     				customerDetails.put("accountIfscCode",accountResponse.getIfscCode());
     			}
     			
     			if(Util.isNotEmptyObject(accountResponse.getSWIFT())){
     				customerDetails.put("accountSwift",accountResponse.getSWIFT());
     			} else {
     				customerDetails.put("accountSwift","-");
     			}

     			if(Util.isNotEmptyObject(accountResponse.getMICR())){
     				customerDetails.put("accountMicr",accountResponse.getMICR());
     			} else {
     				customerDetails.put("accountMicr","-");
     			}
     	    }
     		info.setSiteAccount(accountInfo.toString());
     	}
		
		customerDetails.put("rightSidelogoForPdf",rightSidelogoForPdf);
		customerDetails.put("leftSidelogoForPdf",leftSidelogoForPdf);
		customerDetails.put("thanksAndRegardsFrom",thanksAndRegardsFrom);
		customerDetails.put("greetingsFrom",greetingsFrom);
		if(employeePojos!=null && !employeePojos.isEmpty()) {
			EmployeePojo employee = employeePojos.get(0);
			customerDetails.put("empName",employee.getEmployeeName());
			customerDetails.put("empId",employee.getEmployeeId());
			customerDetails.put("empMobileNo",employee.getMobileNumber());
			customerDetails.put("emp_designation",employee.getEmp_designation()==null?"":employee.getEmp_designation());
		}
		
		
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
	
		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getFlatNo()+" -"+customerPropertyDetailsInfo.getSiteName()+" - Welcome letter.pdf");
		email.setSiteIds(Arrays.asList(customerPropertyDetailsInfo.getSiteId()));
		email.setDemandNoteGeneratorInfo(info);
		//only html constructed data we are taking, we are not generating any PDF file
		Map<String,Object> welcomeLetterData = pdfHelper.XMLWorkerHelperForWelcomeLetter(email, fileInfo);
		dataForGenerateAllotmentLetterHelper.put("welcomeLetterEmailContent", welcomeLetterData.get("welcomeLetterEmailContent"));
		//fileInfoList.add(fileInfo);
		return fileInfoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<FileInfo> generateAmberAgreementDraftHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception{
		log.info("***** Control inside the EmployeeFinancialHelper.generateAgreementDraftHelper() *****");
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");	
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStonesDetailsList =  new ArrayList<MileStoneInfo>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
		
		List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos =  (List<FinTransactionEntryDetailsInfo>) dataForGenerateAllotmentLetterHelper.get("transactionEntryDetailsInfos");
		List<FinPenaltyTaxPojo> taxDetailsList = (List<FinPenaltyTaxPojo>) dataForGenerateAllotmentLetterHelper.get("taxDetailsList");
		List<CustomerInfo> customerDetailsList = (List<CustomerInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantDetails");
		List<Co_ApplicantInfo> coApplicantDetailsList = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
		
		List<ProfessionalInfo> firstApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantProfessionalDetails");
		List<ProfessionalInfo> coApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantProfessionalDetails");	
		
		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
		Double schemeGSTPercentage = (Double) customerDetails.get("schemeGSTPercentage");
		Double totalMilestonePaidAmount = (Double) customerDetails.get("totalMilestonePaidAmount");
		FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
		//FinPenaltyTaxPojo finTaxPojo = taxDetailsList.get(0);
		FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
		List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
		
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double totalAgreementCost = 0.0;
		@SuppressWarnings("unused")
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestoneAmount = 0.0;
		double maintenancePercentage = 0.0;
		double maintenanceAmount = 0.0;
		double sumOfBasicCost = 0.0;
		double sumOfAllComponentGstAmount = 0.0;
		double grandTotalIncludingMaintenance = 0.0;
		//double sumOfTotalAmount = 0.0;
		long serialNo = 1l;
		double totalFlatCostExcludingGST = 0.0;
		double sbuaIntoCarpusFundSum = 0.0;
		double flatLegalAndDocumentationChargesAmt = 0.0;
		double legalCostPercentage = 0.0;
		//double sbuaValue=0.0;
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		if (taxDetailsList != null) {
			for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
				if (MetadataId.MAINTENANCE_CHARGE.getId().equals(finTaxPojo.getTaxType())) {
					maintenancePercentage = finTaxPojo.getPercentageValue();
				} else if (MetadataId.LEGAL_COST.getId().equals(finTaxPojo.getTaxType())) {
					legalCostPercentage = finTaxPojo.getPercentageValue();
				}
			}
		}
		
		if (siteOtherChargesDetails != null) {
			String strAmount = "";
			Map<String, Object> siteOtherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo otherDetailsDraft : siteOtherChargesDetails) {
				if (otherDetailsDraft.getAmount() != null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					if (MetadataId.MAINTENANCE_CHARGE.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//maintenanceAmount = other.getAmount();
						//maintenanceAmount = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						maintenanceAmount = (flatBookingInfo.getSbua()) * otherDetailsDraft.getAmount();
					} else if(MetadataId.CORPUS_FUND.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//sbuaValue = flatBookingInfo.getSbua();
						//sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() ) * otherDetailsDraft.getAmount();
					}else if(MetadataId.LEGAL_COST.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						flatLegalAndDocumentationChargesAmt =  otherDetailsDraft.getAmount();
					}
					strAmount = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(otherDetailsDraft.getAmount()).setScale(roundingModeSize, roundingMode).toString());
					otherDetailsDraft.setStrAmount(strAmount);
					otherDetailsDraft.setStrAmountInWords(getTheAmountInWords(otherDetailsDraft.getAmount()));
					
					siteOtherDetails.put(otherDetailsDraft.getMeteDataTypeId().toString(),otherDetailsDraft);
					
				}
			}
			siteOtherChargesDetailsList.add(siteOtherDetails);
		}
		System.out.println("maintenanceAmount " + (maintenanceAmount)+ (maintenanceAmount)*18);
		customerUnitDetails.put("sbuaIntoCarpusFundSum", sbuaIntoCarpusFundSum);
		customerUnitDetails.put("maintenanceAmount", maintenanceAmount);
		
		siteOtherChargesDetailsList.get(0).get("104");
		//maintenanceAmount = (flatBookingInfo.getSbua()*1d)*maintenanceAmount;
		List<AgreementDraftCalculations> agreementDraftCalculations = new ArrayList<>();
		if (flatCostInfo != null) {
			double gstAmount = 0.0;
			Double basicAmount = flatCostInfo.getBasicFlatCost();
			Double amenitiesFlatCost = flatCostInfo.getAmenitiesFlatCost();
			totalFlatCostExcludingGST = basicAmount+amenitiesFlatCost;
			totalAgreementCost = flatCostInfo.getTotalCost();
			
			double mileStonePercentage = schemeGSTPercentage;
			gstAmount = (basicAmount / 100) * mileStonePercentage;//for Sushantham site gstAmount on basicAmount 

			AgreementDraftCalculations data = new AgreementDraftCalculations();
			data.setSerialNo(serialNo);
			data.setParticulars("Cost of the Apartment");
			data.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			data.setGstPercentage(schemeGSTPercentage.toString());
			data.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(gstAmount).setScale(roundingModeSize, roundingMode).toString()));
			data.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()+gstAmount).setScale(roundingModeSize, roundingMode).toString()));

			agreementDraftCalculations.add(data);
			
			sumOfBasicCost += flatCostInfo.getBasicFlatCost();
			sumOfAllComponentGstAmount += gstAmount;
			serialNo++;
		}

		if (aminitiesInfraCostInfoList != null) {
			for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfoList) {
				if (aminitiesInfraCostInfo.getTotalCost() == null || aminitiesInfraCostInfo.getTotalCost() == 0) {
					continue;
				}
				System.out.println(aminitiesInfraCostInfo.getTotalCost()+" "+aminitiesInfraCostInfo.getAminititesInfraName()+" "+aminitiesInfraCostInfo.getAminititesInfraCost()+" "+aminitiesInfraCostInfo.getFlatCostId());
				//double amenitiesGstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(aminitiesInfraCostInfo.getTotalCost()));
				double amenitiesGstAmount = (aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100;
				System.out.println((aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100);
				//double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost()-amenitiesGstAmount;
				double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost();
				aminitiesInfraCostInfo.setGstAmount(amenitiesGstAmount);
		
				AgreementDraftCalculations data = new AgreementDraftCalculations();
				data.setSerialNo(serialNo);
				data.setParticulars(aminitiesInfraCostInfo.getAminititesInfraName());
				data.setBasicCost(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setGstPercentage(schemeGSTPercentage.toString());
				data.setGstAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setTotalAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount+amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				
				sumOfBasicCost += amenitiesBasicAmount;
				sumOfAllComponentGstAmount += amenitiesGstAmount;

				agreementDraftCalculations.add(data);
				serialNo++;
			}
		}
		double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
		double sumOfCustomerPaidAmount = 0.0d;
		boolean breakTheLoop = false;
		if(transactionEntryDetailsInfos!=null) {
			for (FinTransactionEntryDetailsInfo entryDetailsInfo : transactionEntryDetailsInfos) {
				if(Util.isEmptyObject(entryDetailsInfo.getBankName())) {
					entryDetailsInfo.setBankName("-");
				}
				double transactionAmount = entryDetailsInfo.getTransactionAmount();
				sumOfCustomerPaidAmount += transactionAmount;
				if(sumOfCustomerPaidAmount>tenPercentAmountOnFlat) {
					breakTheLoop = true;//need to show only 10% amount data
					transactionAmount = (sumOfCustomerPaidAmount-transactionAmount)-tenPercentAmountOnFlat;
				}
				//schemeGSTPercentage
				double transactionAmountExcludeGST = (transactionAmount*100/schemeGSTPercentage);//transactionAmount*(100/schemeGSTPercentage);
				double transactionAmountGST = (transactionAmount*5/105);
				entryDetailsInfo.setStrTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmount).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountInWords(getTheAmountInWords(transactionAmount));
				
				entryDetailsInfo.setStrTransactionAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountExcludeGSTInWords(getTheAmountInWords(transactionAmountExcludeGST));
				
				entryDetailsInfo.setStrTransactionAmountGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountGSTInWords(getTheAmountInWords(transactionAmountGST));
				if(breakTheLoop) {break;}
			}
		}
		//*************************************
		double msPercentage = 0.0;
		double msAmount =  0.0;
		double dueAmountExcludeGST = 0.0;
		double milestoneGstAmount = 0.0;
		String strMSAmt = "";
		String strMSPercentage = "";

		long milestoneNo = 0;
		if(mileStonesDetailsList!=null) {
			for (MileStoneInfo ms : mileStonesDetailsList) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String, Object> mileStone = new HashMap<>();
				msPercentage = Double.valueOf(ms.getDue());
				msAmount = Double.valueOf(ms.getTotalDueAmount());
				dueAmountExcludeGST = Double.valueOf(ms.getDueAmountExcludeGST());
				milestoneGstAmount = Double.valueOf(ms.getGstAmount());
				 
			    strMSAmt = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(msAmount).setScale(roundingModeSize, roundingMode).toString());
				strMSPercentage = BigDecimal.valueOf(msPercentage).setScale(0, roundingMode).toString();
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",strMSPercentage);
				mileStone.put("totalDueAmount", strMSAmt);
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				ms.setTotalDueAmount(strMSAmt);
				ms.setDue(strMSPercentage+"%");
				ms.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(dueAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				ms.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(milestoneGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				sumofMilestonePercentgae += msPercentage;
				sumofMilestoneAmount += msAmount;
				milestoneNo = ms.getMilStoneNo();
				mileStonesList.add(mileStone);
			}
		}
		
		/*Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);*/
		
		double flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * legalCostPercentage) / 100;
		double maintenanceGstAmount = (maintenanceAmount * maintenancePercentage) / 100;
		double maintenanceAmtWithGstAmount =  maintenanceAmount+maintenanceGstAmount;
		if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
			//this object for flat basic cost and amenities amount sum
			AgreementDraftCalculations MaintenanceData = new AgreementDraftCalculations();
			MaintenanceData.setSerialNo(serialNo);
			MaintenanceData.setParticulars("Maintenance for 12 Months");
			MaintenanceData.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setGstPercentage(String.valueOf(maintenancePercentage));
			MaintenanceData.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			agreementDraftCalculations.add(MaintenanceData);
		} else {
			maintenanceGstAmount = 0.0;
			maintenanceAmtWithGstAmount = 0.0;
		}
		sumOfBasicCost += maintenanceAmount;
		sumOfAllComponentGstAmount += maintenanceGstAmount;
		

		//totalAmtData object for flat basic cost and amenities amount sum
		AgreementDraftCalculations totalAmtDataObject = new AgreementDraftCalculations();
		totalAmtDataObject.setSerialNo(serialNo);
		totalAmtDataObject.setParticulars("Total");
		totalAmtDataObject.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setGstPercentage("");
		totalAmtDataObject.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost+sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		
		agreementDraftCalculations.add(totalAmtDataObject);
		/* Malladi Changes */
		if(customerPropertyDetailsInfo.getSiteId().equals(124l)) {
		//if(!customerPropertyDetailsInfo.getSiteId().equals(134l) && !customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			//Maintenance Charges object for milestone list
			MileStoneInfo maintenanceDetailsForMS = new MileStoneInfo();
			maintenanceDetailsForMS.setMilStoneNo(++milestoneNo);
			maintenanceDetailsForMS.setMilestoneName("Maintenance Charges payable on final payment request");
			maintenanceDetailsForMS.setDue("-");
			maintenanceDetailsForMS.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(maintenanceDetailsForMS);
			sumofMilestoneAmount+= maintenanceAmtWithGstAmount;
		}
		
		//total amount object for milestone list
		/* Malladi Changes */
		if(!customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			MileStoneInfo ms = new MileStoneInfo();
			ms.setMilStoneNo(++milestoneNo);
			ms.setMilestoneName("Total Amount");
			ms.setDue("");
			ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(ms);
		}
		
		double totalBalanceAmountOnFlat = 0.0;
		double tenPercentGSTAmount = 0.0;
		if (totalMilestonePaidAmount > tenPercentAmountOnFlat) {
			
		} else {
			tenPercentAmountOnFlat = totalMilestonePaidAmount;
		}
		tenPercentGSTAmount = (tenPercentAmountOnFlat*10)/100;//gst Amount on 10 % paid amount
		
		grandTotalIncludingMaintenance = sumOfBasicCost+sumOfAllComponentGstAmount;				
		totalBalanceAmountOnFlat = grandTotalIncludingMaintenance-tenPercentAmountOnFlat;
		
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenance(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalIncludingMaintenance).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceInWords(getTheAmountInWords(grandTotalIncludingMaintenance));
		welcomeMailGeneratorInfo.setTotalBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExcludingGST).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
				
		welcomeMailGeneratorInfo.setTotalBalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalBalanceAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalBalanceAmountInWords(getTheAmountInWords(totalBalanceAmountOnFlat));
		welcomeMailGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidAmountInWords(getTheAmountInWords(tenPercentAmountOnFlat));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmountInWords(getTheAmountInWords(tenPercentGSTAmount));
		
		
		customerUnitDetails.put("flat_legal_and_doc_charges_Sum_With_GST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("flat_legal_cost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt).setScale(roundingModeSize, roundingMode).toString()));
		
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		//dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String leftSidelogoForPdf = "";
		String rightSidelogoForPdf = "";
		String thanksAndRegardsFrom = "";
		String greetingsFrom = "";
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());

		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Agreement Draft.pdf");
		info.setDemandNotePdfFileName("Agreement Draft - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		welcomeMailGeneratorInfo.setCustomerDetailsList(customerDetailsList);
		welcomeMailGeneratorInfo.setCoApplicantDetailsList(coApplicantDetailsList);
		welcomeMailGeneratorInfo.setFirstApplicantProfessionalDetails(firstApplicantProfessionalDetails);
		welcomeMailGeneratorInfo.setCoApplicantProfessionalDetailsList(coApplicantProfessionalDetails);
		
		welcomeMailGeneratorInfo.setOfficeDetailsPojoList(officeDetailsPojoList);
		welcomeMailGeneratorInfo.setMileStones(mileStonesDetailsList);
		welcomeMailGeneratorInfo.setSiteOtherChargesDetails(siteOtherChargesDetails);
		welcomeMailGeneratorInfo.setTermsAndConditions(termsAndConditions);
		welcomeMailGeneratorInfo.setAgreementDraftCalculations(agreementDraftCalculations);
		welcomeMailGeneratorInfo.setTransactionEntryDetailsInfos(transactionEntryDetailsInfos);
		
		//welcomeMailGeneratorInfo.setDataForPdf(dataForPdf1);
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setWelcomeMailGeneratorInfo(welcomeMailGeneratorInfo);
		email.setDemandNoteGeneratorInfo(info);
		//template name to construct pdf
		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/* Malladi Changes */
		String flatSaleOwner = "";
		if(Util.isNotEmptyObject(customerPropertyDetailsInfo)) {
			flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner();
			if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				flatSaleOwner = customerPropertyDetailsInfo.getSiteId()+"_"+flatSaleOwner;	
			}
		}
		
		/*if(customerPropertyDetailsInfo.getSiteId().equals(131l) && flatSaleOwner.equalsIgnoreCase("131_Landlord")) {
			email.setTemplateName("/vmtemplates/Agreement_131_Landlord.vm");
		}else if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			email.setTemplateName("/vmtemplates/Agreement_131_Builder.vm");
		}else {
			email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		}*/
		
		
		pdfHelper.XMLWorkerHelperForAgreementDraftLetterAmber(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	@SuppressWarnings("unchecked")
	public List<FileInfo> generateHorizonAgreementDraftHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception{
		log.info("***** Control inside the EmployeeFinancialHelper.generateAgreementDraftHelper() *****");
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		//copying milestone objects, instead of using original object
		List<MileStoneInfo> mileStonesDetailsList =  new ArrayList<MileStoneInfo>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
		
		List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
		
		List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos =  (List<FinTransactionEntryDetailsInfo>) dataForGenerateAllotmentLetterHelper.get("transactionEntryDetailsInfos");
		List<FinPenaltyTaxPojo> taxDetailsList = (List<FinPenaltyTaxPojo>) dataForGenerateAllotmentLetterHelper.get("taxDetailsList");
		List<CustomerInfo> customerDetailsList = (List<CustomerInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantDetails");
		List<Co_ApplicantInfo> coApplicantDetailsList = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
		
		List<ProfessionalInfo> firstApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantProfessionalDetails");
		List<ProfessionalInfo> coApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantProfessionalDetails");	
		
		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
		Double schemeGSTPercentage = (Double) customerDetails.get("schemeGSTPercentage");
		Double totalMilestonePaidAmount = (Double) customerDetails.get("totalMilestonePaidAmount");
		FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
		//FinPenaltyTaxPojo finTaxPojo = taxDetailsList.get(0);
		FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
		List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
		
		List<Map<String,Object>> mileStonesList = new ArrayList<>();
		List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
		List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
		double totalAgreementCost = 0.0;
		@SuppressWarnings("unused")
		double sumofMilestonePercentgae = 0.0;
		double sumofMilestoneAmount = 0.0;
		double maintenancePercentage = 0.0;
		double maintenanceAmount = 0.0;
		double sumOfBasicCost = 0.0;
		double sumOfAllComponentGstAmount = 0.0;
		double grandTotalIncludingMaintenance = 0.0;
		//double sumOfTotalAmount = 0.0;
		long serialNo = 1l;
		double totalFlatCostExcludingGST = 0.0;
		double sbuaIntoCarpusFundSum = 0.0;
		double flatLegalAndDocumentationChargesAmt = 0.0;
		double legalCostPercentage = 0.0;
		//double sbuaValue=0.0;
		if(termsAndConditions!=null) {
			for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
				if(dynamicKeyValueInfo.getValue()!=null) {
						Map<String,Object> termsAndCondition = new HashMap<>();
						termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
						termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
						termsAndConditionsList.add(termsAndCondition);
				}
			}
		}
		if (taxDetailsList != null) {
			for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
				if (MetadataId.MAINTENANCE_CHARGE.getId().equals(finTaxPojo.getTaxType())) {
					maintenancePercentage = finTaxPojo.getPercentageValue();
				} else if (MetadataId.LEGAL_COST.getId().equals(finTaxPojo.getTaxType())) {
					legalCostPercentage = finTaxPojo.getPercentageValue();
				}
			}
		}
		
		if (siteOtherChargesDetails != null) {
			String strAmount = "";
			Map<String, Object> siteOtherDetails = new HashMap<>();
			for (SiteOtherChargesDetailsPojo otherDetailsDraft : siteOtherChargesDetails) {
				if (otherDetailsDraft.getAmount() != null) {
					//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
					if (MetadataId.MAINTENANCE_CHARGE.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//maintenanceAmount = other.getAmount();
						//maintenanceAmount = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						maintenanceAmount = (flatBookingInfo.getSbua()) * otherDetailsDraft.getAmount();
					} else if(MetadataId.CORPUS_FUND.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						//sbuaValue = flatBookingInfo.getSbua();
						//sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
						sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() ) * otherDetailsDraft.getAmount();
					}else if(MetadataId.LEGAL_COST.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
						flatLegalAndDocumentationChargesAmt =  otherDetailsDraft.getAmount();
					}
					strAmount = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(otherDetailsDraft.getAmount()).setScale(roundingModeSize, roundingMode).toString());
					otherDetailsDraft.setStrAmount(strAmount);
					otherDetailsDraft.setStrAmountInWords(getTheAmountInWords(otherDetailsDraft.getAmount()));
					
					siteOtherDetails.put(otherDetailsDraft.getMeteDataTypeId().toString(),otherDetailsDraft);
					
				}
			}
			siteOtherChargesDetailsList.add(siteOtherDetails);
		}
		System.out.println("maintenanceAmount " + (maintenanceAmount)+ (maintenanceAmount)*18);
		customerUnitDetails.put("sbuaIntoCarpusFundSum", sbuaIntoCarpusFundSum);
		customerUnitDetails.put("maintenanceAmount", maintenanceAmount);
		
		siteOtherChargesDetailsList.get(0).get("104");
		//maintenanceAmount = (flatBookingInfo.getSbua()*1d)*maintenanceAmount;
		List<AgreementDraftCalculations> agreementDraftCalculations = new ArrayList<>();
		if (flatCostInfo != null) {
			double gstAmount = 0.0;
			Double basicAmount = flatCostInfo.getBasicFlatCost();
			Double amenitiesFlatCost = flatCostInfo.getAmenitiesFlatCost();
			totalFlatCostExcludingGST = basicAmount+amenitiesFlatCost;
			totalAgreementCost = flatCostInfo.getTotalCost();
			
			double mileStonePercentage = schemeGSTPercentage;
			gstAmount = (basicAmount / 100) * mileStonePercentage;//for Sushantham site gstAmount on basicAmount 

			AgreementDraftCalculations data = new AgreementDraftCalculations();
			data.setSerialNo(serialNo);
			data.setParticulars("Cost of the Apartment");
			data.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			data.setGstPercentage(schemeGSTPercentage.toString());
			data.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(gstAmount).setScale(roundingModeSize, roundingMode).toString()));
			data.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()+gstAmount).setScale(roundingModeSize, roundingMode).toString()));

			agreementDraftCalculations.add(data);
			
			sumOfBasicCost += flatCostInfo.getBasicFlatCost();
			sumOfAllComponentGstAmount += gstAmount;
			serialNo++;
		}

		if (aminitiesInfraCostInfoList != null) {
			for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfoList) {
				if (aminitiesInfraCostInfo.getTotalCost() == null || aminitiesInfraCostInfo.getTotalCost() == 0) {
					continue;
				}
				System.out.println(aminitiesInfraCostInfo.getTotalCost()+" "+aminitiesInfraCostInfo.getAminititesInfraName()+" "+aminitiesInfraCostInfo.getAminititesInfraCost()+" "+aminitiesInfraCostInfo.getFlatCostId());
				//double amenitiesGstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(aminitiesInfraCostInfo.getTotalCost()));
				double amenitiesGstAmount = (aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100;
				System.out.println((aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100);
				//double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost()-amenitiesGstAmount;
				double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost();
				aminitiesInfraCostInfo.setGstAmount(amenitiesGstAmount);
		
				AgreementDraftCalculations data = new AgreementDraftCalculations();
				data.setSerialNo(serialNo);
				data.setParticulars(aminitiesInfraCostInfo.getAminititesInfraName());
				data.setBasicCost(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setGstPercentage(schemeGSTPercentage.toString());
				data.setGstAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				data.setTotalAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount+amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
				
				sumOfBasicCost += amenitiesBasicAmount;
				sumOfAllComponentGstAmount += amenitiesGstAmount;

				agreementDraftCalculations.add(data);
				serialNo++;
			}
		}
		double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
		double sumOfCustomerPaidAmount = 0.0d;
		boolean breakTheLoop = false;
		if(transactionEntryDetailsInfos!=null) {
			for (FinTransactionEntryDetailsInfo entryDetailsInfo : transactionEntryDetailsInfos) {
				if(Util.isEmptyObject(entryDetailsInfo.getBankName())) {
					entryDetailsInfo.setBankName("-");
				}
				double transactionAmount = entryDetailsInfo.getTransactionAmount();
				sumOfCustomerPaidAmount += transactionAmount;
				if(sumOfCustomerPaidAmount>tenPercentAmountOnFlat) {
					breakTheLoop = true;//need to show only 10% amount data
					transactionAmount = (sumOfCustomerPaidAmount-transactionAmount)-tenPercentAmountOnFlat;
				}
				//schemeGSTPercentage
				double transactionAmountExcludeGST = (transactionAmount*100/schemeGSTPercentage);//transactionAmount*(100/schemeGSTPercentage);
				double transactionAmountGST = (transactionAmount*5/105);
				entryDetailsInfo.setStrTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmount).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountInWords(getTheAmountInWords(transactionAmount));
				
				entryDetailsInfo.setStrTransactionAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountExcludeGSTInWords(getTheAmountInWords(transactionAmountExcludeGST));
				
				entryDetailsInfo.setStrTransactionAmountGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountGST).setScale(roundingModeSize, roundingMode).toString()));
				entryDetailsInfo.setTransactionAmountGSTInWords(getTheAmountInWords(transactionAmountGST));
				if(breakTheLoop) {break;}
			}
		}
		//*************************************
		double msPercentage = 0.0;
		double msAmount =  0.0;
		double dueAmountExcludeGST = 0.0;
		double milestoneGstAmount = 0.0;
		String strMSAmt = "";
		String strMSPercentage = "";

		long milestoneNo = 0;
		if(mileStonesDetailsList!=null) {
			for (MileStoneInfo ms : mileStonesDetailsList) {
				if(ms.getTotalDueAmount() == null) {
					continue;
				}
				Map<String, Object> mileStone = new HashMap<>();
				msPercentage = Double.valueOf(ms.getDue());
				msAmount = Double.valueOf(ms.getTotalDueAmount());
				dueAmountExcludeGST = Double.valueOf(ms.getDueAmountExcludeGST());
				milestoneGstAmount = Double.valueOf(ms.getGstAmount());
				 
			    strMSAmt = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(msAmount).setScale(roundingModeSize, roundingMode).toString());
				strMSPercentage = BigDecimal.valueOf(msPercentage).setScale(0, roundingMode).toString();
				mileStone.put("milestoneName", ms.getMilestoneName());
				mileStone.put("percentage",strMSPercentage);
				mileStone.put("totalDueAmount", strMSAmt);
				mileStone.put("milStoneNo", ms.getMilStoneNo());
				ms.setTotalDueAmount(strMSAmt);
				ms.setDue(strMSPercentage+"%");
				ms.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(dueAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
				ms.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(milestoneGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				sumofMilestonePercentgae += msPercentage;
				sumofMilestoneAmount += msAmount;
				milestoneNo = ms.getMilStoneNo();
				mileStonesList.add(mileStone);
			}
		}
		
		/*Map<String,Object> mileStoneTotal = new HashMap<>();
		mileStoneTotal.put("milestoneName", "Total Amount");
		mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
		mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
		mileStoneTotal.put("milStoneNo", "");
		mileStonesList.add(mileStoneTotal);*/
		
		double flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * legalCostPercentage) / 100;
		double maintenanceGstAmount = (maintenanceAmount * maintenancePercentage) / 100;
		double maintenanceAmtWithGstAmount =  maintenanceAmount+maintenanceGstAmount;
		if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
			//this object for flat basic cost and amenities amount sum
			AgreementDraftCalculations MaintenanceData = new AgreementDraftCalculations();
			MaintenanceData.setSerialNo(serialNo);
			MaintenanceData.setParticulars("Maintenance for 12 Months");
			MaintenanceData.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setGstPercentage(String.valueOf(maintenancePercentage));
			MaintenanceData.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			MaintenanceData.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			agreementDraftCalculations.add(MaintenanceData);
		} else {
			maintenanceGstAmount = 0.0;
			maintenanceAmtWithGstAmount = 0.0;
		}
		sumOfBasicCost += maintenanceAmount;
		sumOfAllComponentGstAmount += maintenanceGstAmount;
		

		//totalAmtData object for flat basic cost and amenities amount sum
		AgreementDraftCalculations totalAmtDataObject = new AgreementDraftCalculations();
		totalAmtDataObject.setSerialNo(serialNo);
		totalAmtDataObject.setParticulars("Total");
		totalAmtDataObject.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setGstPercentage("");
		totalAmtDataObject.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		totalAmtDataObject.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost+sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		
		agreementDraftCalculations.add(totalAmtDataObject);
		/* Malladi Changes */
		if(customerPropertyDetailsInfo.getSiteId().equals(124l)) {
		//if(!customerPropertyDetailsInfo.getSiteId().equals(134l) && !customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			//Maintenance Charges object for milestone list
			MileStoneInfo maintenanceDetailsForMS = new MileStoneInfo();
			maintenanceDetailsForMS.setMilStoneNo(++milestoneNo);
			maintenanceDetailsForMS.setMilestoneName("Maintenance Charges payable on final payment request");
			maintenanceDetailsForMS.setDue("-");
			maintenanceDetailsForMS.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(maintenanceDetailsForMS);
			sumofMilestoneAmount+= maintenanceAmtWithGstAmount;
		}
		
		//total amount object for milestone list
		/* Malladi Changes */
		if(!customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			MileStoneInfo ms = new MileStoneInfo();
			ms.setMilStoneNo(++milestoneNo);
			ms.setMilestoneName("Total Amount");
			ms.setDue("");
			ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(ms);
		}
		
		double totalBalanceAmountOnFlat = 0.0;
		double tenPercentGSTAmount = 0.0;
		if (totalMilestonePaidAmount > tenPercentAmountOnFlat) {
			
		} else {
			tenPercentAmountOnFlat = totalMilestonePaidAmount;
		}
		tenPercentGSTAmount = (tenPercentAmountOnFlat*10)/100;//gst Amount on 10 % paid amount
		
		grandTotalIncludingMaintenance = sumOfBasicCost+sumOfAllComponentGstAmount;				
		totalBalanceAmountOnFlat = grandTotalIncludingMaintenance-tenPercentAmountOnFlat;
		
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenance(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalIncludingMaintenance).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceInWords(getTheAmountInWords(grandTotalIncludingMaintenance));
		welcomeMailGeneratorInfo.setTotalBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExcludingGST).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
				
		welcomeMailGeneratorInfo.setTotalBalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalBalanceAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalBalanceAmountInWords(getTheAmountInWords(totalBalanceAmountOnFlat));
		welcomeMailGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidAmountInWords(getTheAmountInWords(tenPercentAmountOnFlat));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
		welcomeMailGeneratorInfo.setTotalPaidGSTAmountInWords(getTheAmountInWords(tenPercentGSTAmount));
		
		
		customerUnitDetails.put("flat_legal_and_doc_charges_Sum_With_GST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount).setScale(roundingModeSize, roundingMode).toString()));
		customerUnitDetails.put("flat_legal_cost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt).setScale(roundingModeSize, roundingMode).toString()));
		
		
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
		//dataForPdf1.put("mileStones",  mileStonesList);
		dataForPdf1.put("termsAndConditions", termsAndConditionsList);
		dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		
		email.setDataForPdf(dataForPdf1);
		String leftSidelogoForPdf = "";
		String rightSidelogoForPdf = "";
		String thanksAndRegardsFrom = "";
		String greetingsFrom = "";
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());

		Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Agreement Draft.pdf");
		info.setDemandNotePdfFileName("Agreement Draft - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		welcomeMailGeneratorInfo.setCustomerDetailsList(customerDetailsList);
		welcomeMailGeneratorInfo.setCoApplicantDetailsList(coApplicantDetailsList);
		welcomeMailGeneratorInfo.setFirstApplicantProfessionalDetails(firstApplicantProfessionalDetails);
		welcomeMailGeneratorInfo.setCoApplicantProfessionalDetailsList(coApplicantProfessionalDetails);
		
		welcomeMailGeneratorInfo.setOfficeDetailsPojoList(officeDetailsPojoList);
		welcomeMailGeneratorInfo.setMileStones(mileStonesDetailsList);
		welcomeMailGeneratorInfo.setSiteOtherChargesDetails(siteOtherChargesDetails);
		welcomeMailGeneratorInfo.setTermsAndConditions(termsAndConditions);
		welcomeMailGeneratorInfo.setAgreementDraftCalculations(agreementDraftCalculations);
		welcomeMailGeneratorInfo.setTransactionEntryDetailsInfos(transactionEntryDetailsInfos);
		
		//welcomeMailGeneratorInfo.setDataForPdf(dataForPdf1);
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setWelcomeMailGeneratorInfo(welcomeMailGeneratorInfo);
		email.setDemandNoteGeneratorInfo(info);
		//template name to construct pdf
		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/* Malladi Changes */
		String flatSaleOwner = "";
		if(Util.isNotEmptyObject(customerPropertyDetailsInfo)) {
			flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner();
			if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				flatSaleOwner = customerPropertyDetailsInfo.getSiteId()+"_"+flatSaleOwner;	
			}
		}
		
		/*if(customerPropertyDetailsInfo.getSiteId().equals(131l) && flatSaleOwner.equalsIgnoreCase("131_Landlord")) {
			email.setTemplateName("/vmtemplates/Agreement_131_Landlord.vm");
		}else if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
			email.setTemplateName("/vmtemplates/Agreement_131_Builder.vm");
		}else {
			email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		}*/
		if(customerPropertyDetailsInfo.getSiteId().equals(133l)) {
			return null;
		}
		pdfHelper.XMLWorkerHelperForAgreementDraftLetter_HORIZON(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
	}

	public void sendInterestWaiverPushNotification(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
		if (!FinTransactionMode.INTEREST_WAIVER.getId().equals(transactionServiceInfo.getTransactionModeId())) {
			return;
		}
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
		EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo();
		FinTransactionEntryDetailsResponse prevTransactionEntryDetails = transactionServiceInfo.getPrevTransactionEntryDetailsResponse();
		if(Util.isEmptyObject(prevTransactionEntryDetails)){
			return;
		}
		List<EmployeeFinTranPaymentSetOffInfo> paymentSetOffList = transactionServiceInfo.getPaymentSetOffDetails();
		List<FinancialProjectMileStoneResponse> mileStones = prevTransactionEntryDetails.getFinancialProjectMileStoneResponseList();
		
		List<MileStone> mileStonesList = new ArrayList<>();
		
		double totalInterestAmount = 0.0;
		double totalInterestPaidAmount = 0.0;
		double totalInterestWaiverAmount = 0.0;
		double totalInterestpendingAmount = totalInterestAmount-totalInterestPaidAmount-totalInterestWaiverAmount;
		//List<Map<String,Object>> milestonePushNoti = new ArrayList<>();
		
		if(Util.isEmptyObject(paymentSetOffList) || Util.isEmptyObject(mileStones)) {
			log.info("list details found empty");
			throw new EmployeeFinancialServiceException("Failed to generate Interest waiver,  on Flat "+customerPropertyDetailsInfo.getFlatNo());
		} else if (paymentSetOffList.size()!=mileStones.size()) {
			log.info("paymentSetOffList and mileStones is not equal");
			throw new EmployeeFinancialServiceException("Failed to generate Interest waiver,  on Flat "+customerPropertyDetailsInfo.getFlatNo());
		}
		for (int index = 0; index < mileStones.size(); index++) {
			FinancialProjectMileStoneResponse financialProjectMileStoneResponse = mileStones.get(index);
			EmployeeFinTranPaymentSetOffInfo setOffInfo = paymentSetOffList.get(index);
			if(Util.isEmptyObject(setOffInfo) || Util.isEmptyObject(setOffInfo.getAmount())) {
				log.info("Amount is empty");
				throw new EmployeeFinancialServiceException("Failed to generate Interest waiver,  on Flat "+customerPropertyDetailsInfo.getFlatNo());
			}
			//Map<String,Object> map = new HashMap<>();
			MileStone ms = new MileStone();
			double payAmount =  financialProjectMileStoneResponse.getPayAmount()==null?0d: financialProjectMileStoneResponse.getPayAmount();
			double paidAmount = financialProjectMileStoneResponse.getPaidAmount()==null?0d: financialProjectMileStoneResponse.getPaidAmount();
			double dueAmount = payAmount-paidAmount;
			
			double penaltyAmount = financialProjectMileStoneResponse.getTotalPenaltyAmount()==null?0d:financialProjectMileStoneResponse.getTotalPenaltyAmount();
			double paidPenaltyAmount = financialProjectMileStoneResponse.getTotalPenalityPaidAmount()==null?0d:financialProjectMileStoneResponse.getTotalPenalityPaidAmount();
			double interestWaiverAmount = financialProjectMileStoneResponse.getInterestWaiverAdjAmount()==null?0d:financialProjectMileStoneResponse.getInterestWaiverAdjAmount();
			interestWaiverAmount = interestWaiverAmount+setOffInfo.getAmount();
			
			double pendingPenalyAmount = penaltyAmount-paidPenaltyAmount-interestWaiverAmount;
			
			totalInterestAmount += penaltyAmount;
			totalInterestPaidAmount += paidPenaltyAmount;
			totalInterestWaiverAmount += interestWaiverAmount;
			//totalInterestpendingAmount += pendingPenalyAmount;
			ms.setProjectMilestoneId(financialProjectMileStoneResponse.getProjectMilestoneId());
			ms.setMilStoneNo(financialProjectMileStoneResponse.getMileStoneNo());
			ms.setMileStoneName(financialProjectMileStoneResponse.getMilestoneName());
			/*map.put("projectMilestoneId", financialProjectMileStoneResponse.getProjectMilestoneId());
			map.put("milStoneNo", financialProjectMileStoneResponse.getMileStoneNo());
			map.put("mileStoneName", financialProjectMileStoneResponse.getMilestoneName());*/
			
			ms.setMileStoneAmount(Double.valueOf(payAmount).toString());
			ms.setMileStoneDate(financialProjectMileStoneResponse.getMilestoneDate());
			/*map.put("mileStoneAmount", financialProjectMileStoneResponse.getPayAmount());
			map.put("mileStoneDate", financialProjectMileStoneResponse.getMilestoneDate());*/
			
			ms.setDemandNoteDate(financialProjectMileStoneResponse.getDemandNoteDate());
			ms.setMileStonePaidAmount(Double.valueOf(paidAmount).toString());
			ms.setMilestoneAmountDue(Double.valueOf(dueAmount).toString());
			ms.setMilestoneLastReceiptDate(financialProjectMileStoneResponse.getMilestoneLastReceiptDate());
			
			/*map.put("demandNoteDate", financialProjectMileStoneResponse.getDemandNoteDate());
			map.put("mileStonePaidAmount", financialProjectMileStoneResponse.getPaidAmount());
			map.put("milestoneAmountDue", dueAmount);
			map.put("milestoneLastReceiptDate", financialProjectMileStoneResponse.getMilestoneLastReceiptDate());*/
			
			ms.setTotalPenalityAmount(Double.valueOf(penaltyAmount).toString());
			ms.setTotalPendingPenaltyAmount(Double.valueOf(pendingPenalyAmount).toString());
			ms.setTotalPenalityPaidAmount(Double.valueOf(paidPenaltyAmount).toString());
			ms.setInterestWaiverAdjAmount(Double.valueOf(interestWaiverAmount).toString());
			ms.setDueDate( financialProjectMileStoneResponse.getMileStoneDueDate());
			
			/*map.put("totalPenalityAmount", penaltyAmount);
			map.put("totalPendingPenaltyAmount", pendingPenalyAmount);
			map.put("totalPenalityPaidAmount",paidPenaltyAmount);
			map.put("interestWaiverAdjAmount", interestWaiverAmount);
			map.put("dueDate", financialProjectMileStoneResponse.getMileStoneDueDate());*/
				
			//milestonePushNoti.add(map);
			mileStonesList.add(ms);
		}
		
		
		totalInterestpendingAmount = totalInterestAmount-totalInterestPaidAmount-totalInterestWaiverAmount;
		/*Map<String,Object> map = new HashMap<>();
		map.put("totalPenaltyAmount", totalInterestAmount);
		map.put("sumOfTotalPendingPenaltyAmount",totalInterestpendingAmount);
		map.put("sumOfTotalPenalityPaidAmount",totalInterestPaidAmount);
		map.put("sumOfInterestWaiverAdjAmount",totalInterestWaiverAmount);
		milestonePushNoti.add(map);*/
		
		Financial financial = new Financial();
		financial.setMileStones(mileStonesList);
		financial.setTotalPenaltyAmount(Double.valueOf(totalInterestAmount).toString());
		financial.setSumOfTotalPendingPenaltyAmount(Double.valueOf(totalInterestpendingAmount).toString());
		financial.setSumOfTotalPenalityPaidAmount(Double.valueOf(totalInterestPaidAmount).toString());
		financial.setSumOfInterestWaiverAdjAmount(Double.valueOf(totalInterestWaiverAmount).toString());
		
		financialPushNotification.setFinancial(financial);
		//financialPushNotification.setMilestonePushNotificationObj(milestonePushNoti);
		financialPushNotification.setBookingFormId(transactionServiceInfo.getBookingFormId());
		//FinTransactionEntryDetailsResponse prevTransactionEntryDetails = transactionServiceInfo.getPrevTransactionEntryDetailsResponse();
		financialPushNotification.setNotificationTitle("Interest amount waived details");
		financialPushNotification.setNotificationBody("As per your request, interest amount is waived off, Please have a look.");
		if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
			//financialPushNotification.setNotificationTitle("Aspire Aurum Receipt");
			//financialPushNotification.setNotificationBody("Dear Customer your receipt has been generated!.");
			financialPushNotification.setNotificationDescription("");
			financialPushNotification.setTypeMsg("Interest Waiver");
			financialPushNotification.setTypeOfPushNotificationMsg("Interest Waiver");
		} else {
			//financialPushNotification.setNotificationTitle("Sumadhura Receipt");
			//financialPushNotification.setNotificationBody("Dear Customer your receipt has been generated!.");
			financialPushNotification.setNotificationDescription("");
			financialPushNotification.setTypeMsg("Interest Waiver");	
			financialPushNotification.setTypeOfPushNotificationMsg("Interest Waiver");
		}
		
		financialPushNotification.setStatus("Transaction Completed");
		financialPushNotification.setCreatedDate(new Timestamp(new Date().getTime()));
		financialPushNotification.setSiteId(transactionServiceInfo.getSiteId());
		 
		//milestone push notification
		pushNotificationHelper.sendFinancialStatusNotification(financialPushNotification,null);	
			
	}

	public void checkTemplates(Email email, FileInfo fileInfo) throws Exception {
		pdfHelper.checkTemplates(email, new FileInfo());
	}

	public List<FileInfo> financialReceiptGeneratHelper(final EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws Exception {
		log.info("***** Control inside the EmployeeFinancialHelper.financialReceiptGeneratHelper() ***** port "+employeeFinancialServiceInfo.getPortNumber());
		String deleteOldFiles = "";
		String storingFilePath = "";
		String storingfileUrl = "";
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		Properties prop= responceCodesUtil.getApplicationProperties();
		long portNumber = employeeFinancialServiceInfo.getPortNumber();
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			storingFilePath =prop.getProperty("UAT_TRANSACTION_RECEIPT_PDF_PATH");
			storingfileUrl = prop.getProperty("UAT_TRANSACTION_RECEIPT_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			storingFilePath =prop.getProperty("UAT_TRANSACTION_RECEIPT_PDF_PATH");
			storingfileUrl = prop.getProperty("UAT_TRANSACTION_RECEIPT_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			storingFilePath =prop.getProperty("CUG_TRANSACTION_RECEIPT_PDF_PATH");
			storingfileUrl = prop.getProperty("CUG_TRANSACTION_RECEIPT_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			storingFilePath =prop.getProperty("CUG_TRANSACTION_RECEIPT_PDF_PATH");
			storingfileUrl = prop.getProperty("CUG_TRANSACTION_RECEIPT_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			storingFilePath =prop.getProperty("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
			storingfileUrl = prop.getProperty("LIVE_TRANSACTION_RECEIPT_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			storingFilePath =prop.getProperty("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
			storingfileUrl = prop.getProperty("LIVE_TRANSACTION_RECEIPT_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		deleteOldFiles =prop.getProperty("DELETE_GENERATED_TAX_INVOICE");
		employeeFinancialServiceInfo.setFilePath(storingFilePath+"TaxInvoice\\");
		employeeFinancialServiceInfo.setFileUrl(storingfileUrl+"TaxInvoice\\");
		StringBuilder deleteFilePath1 = new StringBuilder(storingFilePath).append("TaxInvoiceUploadedFiles");
		//StringBuilder urlPath1 = new StringBuilder(fileUrl).append("TaxInvoiceUploadedFiles");

		if(deleteOldFiles!=null && deleteOldFiles.equals("false")) {
			//deleting previous zip files
			pdfHelper.DeleteDemandNotePreviewFiles(new File(deleteFilePath1.toString()),0,"TaxInvoiceUploadedFiles");
		}
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String rightSidelogoFilePath = "";
		String leftSidelogoFilePath = "";

		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";

		//Map<String,String> isFilePathFound = new HashMap<>();
		List<FileInfo> fileInfoList = new ArrayList<>();
		List<MileStoneInfo> mileStones = null;
		//final Email email = null;
		//final FileInfo taxInvfileInfo =  null;
		DemandNoteGeneratorInfo  info = null;
		List<FinancialUploadDataInfo> financialUploadDataRequests = employeeFinancialServiceInfo.getFinancialUploadDataRequests();
		ExecutorService es=Executors.newFixedThreadPool(100);
		 for (FinancialUploadDataInfo financialUploadDataInfo : financialUploadDataRequests) {
			//System.out.println(financialUploaDataInfo);
			final Email email = new Email();
			final FileInfo taxInvfileInfo =  new FileInfo();
			info = new DemandNoteGeneratorInfo();

			Site site = new Site();
			site.setSiteId(financialUploadDataInfo.getSiteId());
			site.setName(financialUploadDataInfo.getSiteName());		

			BeanUtils.copyProperties(financialUploadDataInfo, info);
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo = new CustomerPropertyDetailsInfo();
			customerPropertyDetailsInfo.setSiteId(financialUploadDataInfo.getSiteId());
			customerPropertyDetailsInfo.setSiteName(financialUploadDataInfo.getSiteName());
			customerPropertyDetailsInfo.setFlatSaleOwner(financialUploadDataInfo.getFlatSaleOwner());

			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");

			rightSidelogoFilePath = logoAndOtherDetails.get("rightSidelogoFilePath");
			leftSidelogoFilePath = logoAndOtherDetails.get("leftSidelogoFilePath");

			//thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			//greetingsFrom = logoAndOtherDetails.get("greetingsFrom");
			thanksAndRegardsFrom = financialUploadDataInfo.getThanksAndRegardsFrom();
			
			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			
			if(Util.isNotEmptyObject(rightSidelogoFilePath)) {//checking file is present in local folder, if exists loading file from local folder
				if(isFilePathFoundPdf.get(rightSidelogoFilePath)!=null && isFilePathFoundPdf.get(rightSidelogoFilePath).equals("true")) {
					info.setRightSidelogoFilePath(rightSidelogoFilePath);
				} else {
					File file = new File(rightSidelogoFilePath);
					if(file.exists()) {//checking file is exists or not, if exist the loading this file in pdf
						isFilePathFoundPdf.put(rightSidelogoFilePath, "true");
						info.setRightSidelogoFilePath(rightSidelogoFilePath);
					}
				}
			}
			
			if(Util.isNotEmptyObject(leftSidelogoFilePath)) {
				if(isFilePathFoundPdf.get(leftSidelogoFilePath)!=null && isFilePathFoundPdf.get(leftSidelogoFilePath).equals("true")) {
					info.setLeftSidelogoFilePath(leftSidelogoFilePath);
				} else {
					File file = new File(leftSidelogoFilePath);
					if(file.exists()) {
						isFilePathFoundPdf.put(leftSidelogoFilePath, "true");
						info.setLeftSidelogoFilePath(leftSidelogoFilePath);
					}
				}
			}
			
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			
			/*double receiptPaidAmount = 0.0;
			String strReceiptPaidAmount = financialUploadDataInfo.getTotalReceiptPaidAmount();
			if(strReceiptPaidAmount.contains(",")) {
				receiptPaidAmount = Double.valueOf(strReceiptPaidAmount.replace(",",""));
			} else {
				receiptPaidAmount = Double.valueOf(receiptPaidAmount);
			}
			String amountInWords = getTheAmountInWords(receiptPaidAmount);
			info.setMileStoneAmountInWords(amountInWords);*/
			
			//info.setMileStoneAmountInWords("Hello");
			info.setFlatName(financialUploadDataInfo.getFlatNo());
			
			 mileStones = new ArrayList<>();
			MileStoneInfo mileStone = new MileStoneInfo();
			mileStone.setProjectMilestoneId("1");
			mileStone.setMilestoneName(financialUploadDataInfo.getMilestoneName());
			mileStone.setSAC(financialUploadDataInfo.getSac());
			mileStone.setDueAmountExcludeGST(financialUploadDataInfo.getDueAmountExcludeGST());
			
			mileStones.add(mileStone);
			info.setMileStones(mileStones);
			
			/*EmployeeFinancialTransactionResponse trnResp = new EmployeeFinancialTransactionResponse();
			trnResp.setOfficeDetailsList(financialUploadDataInfo.getOfficeDetailsList());
			trnResp.setFlatsResponse(null);
			setOfficeDetails(trnResp,info);*/
			String recieptNo=financialUploadDataInfo.getTransactionReceiptNo();
			String recieptShortName=recieptNo.replace("/", "_");
			//String recieptShortName = StringUtils.substringAfterLast(recieptNo, "/");
			financialUploadDataInfo.setPdfFileName(financialUploadDataInfo.getFlatNo()+"-"+financialUploadDataInfo.getSiteName()+"- tax invoice-"+recieptShortName+".pdf");
			
			email.setFinancialUploadDataRequests(Arrays.asList(financialUploadDataInfo));
			List<DemandNoteGeneratorInfo> demandNoteGeneratorInfo = new ArrayList<>();
			demandNoteGeneratorInfo.add(info);
			email.setDemandNoteGeneratorInfoList(demandNoteGeneratorInfo);
			
			transactionReceiptPdfFilePath = employeeFinancialServiceInfo.getFilePath(); 
			transactionGetReceiptFileURL = employeeFinancialServiceInfo.getFileUrl(); 
			String fileName = pdfHelper.getFileName(transactionReceiptPdfFilePath+"TaxInvoice-"+timeStamp+"/",financialUploadDataInfo.getPdfFileName());
			
			StringBuilder filePath = new StringBuilder(transactionReceiptPdfFilePath).append("TaxInvoice-"+timeStamp);
			StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append("TaxInvoice-"+timeStamp);
			
			filePath.append("/").append(fileName);//appending file name
			urlPath.append("/").append(fileName);//appending file name

			taxInvfileInfo.setName(fileName);
			taxInvfileInfo.setUrl(urlPath.toString());
			taxInvfileInfo.setFilePath(filePath.toString());
			new Thread(new Runnable() {
				   public void run(){
					try {
						pdfHelper.financialReceiptGeneratHelper(email,taxInvfileInfo,employeeFinancialServiceInfo);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				   }
			   }).start();
			
			fileInfoList.add(taxInvfileInfo);
		 }
		// es.shutdown();
		return fileInfoList;
	}

	public void validateDateForApprovalNotInUse(EmployeeFinancialTransactionServiceInfo financialTransactionRequest) throws Exception {
		
		if(FinTransactionType.RECEIPT.getId().equals(financialTransactionRequest.getTransactionTypeId())
				//&& FinTransactionMode.ONLINE.getId().equals(financialTransactionRequest.getTransactionModeId())
				){
			if (Util.isEmptyObject(financialTransactionRequest.getTransactionReceiveDate())) {
		
				throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
			}
			
			//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Timestamp currentDate = new Timestamp(new Date().getTime());
			Timestamp endDate = new Timestamp(new Date().getTime());
			Timestamp endDateFifth = TimeUtil.addDays(new Timestamp(TimeUtil.getFirstDateOfMonth(endDate).getTime()), 4);
			Timestamp currentMonthFirst = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(TimeUtil.getFirstDateOfMonth(endDate).getTime()));
			System.out.println(endDateFifth);
			
			Timestamp transactionReceiveDate = financialTransactionRequest.getTransactionReceiveDate();
			
			Date previousMothStartDate = TimeUtil.getPreviousMonth(new Date());
			Timestamp previousMonthFirstDate = new Timestamp(TimeUtil.getFirstDateOfMonth(previousMothStartDate).getTime());
			
			if (transactionReceiveDate.after(endDateFifth)) {
				System.out.println("FinancialTest.main() after " + transactionReceiveDate + " - " + endDateFifth);
			} else if (transactionReceiveDate.before(endDateFifth)) {
				System.out.println("FinancialTest.main() before " + transactionReceiveDate + " - " + endDateFifth);
				//if transaction date is before the end Date
				if(previousMonthFirstDate.after(transactionReceiveDate)) {
					log.info("FinancialTest.main() prev before " + previousMonthFirstDate + " - " + transactionReceiveDate);	
					throw new EmployeeFinancialServiceException("Receipt is back dated entry unable to approve pls contact MIS team or Admin");
				} else if (transactionReceiveDate.after(previousMonthFirstDate) && transactionReceiveDate.before(endDateFifth) && (currentDate.before(endDateFifth) || currentDate.equals(endDateFifth))) {
					log.info("FinancialTest.main() prev before " + previousMonthFirstDate + " - " + transactionReceiveDate);
					//throw new EmployeeFinancialServiceException("Receipt is back dated entry unable to approve pls contact MIS team or Admin");
				} else if (currentDate.after(currentMonthFirst) && transactionReceiveDate.before(currentMonthFirst)) {
					log.info("FinancialTest.main() prev before " + previousMonthFirstDate + " - " + transactionReceiveDate);	
					throw new EmployeeFinancialServiceException("Receipt is back dated entry unable to approve pls contact MIS team or Admin");
				}
			}
			
			System.out.println(previousMothStartDate);
			System.out.println(previousMonthFirstDate);
			System.out.println();
			
		}
		
	}
	
		public List<FileInfo> generateNOCLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		Email email = new Email();
		FileInfo fileInfo_Handing_Over_documents = new FileInfo();
		FileInfo fileInfo_Taken_Over_Documents = new FileInfo();
		FileInfo fileInfo_Bescom = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
		
		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
		List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
		
		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
		
		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
//		
//		@SuppressWarnings("unchecked")
//		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
//		
		@SuppressWarnings("unchecked")
		List<FlatBookingPojo> flatBookPojo =  (List<FlatBookingPojo>) dataForGenerateAllotmentLetterHelper.get("flatBookPojo");
		
		@SuppressWarnings("unchecked")
		List<Co_ApplicantInfo> coApplicantDetailsList  = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
		
		
		Long siteId = (Long) customerDetails.get("siteId");
		String siteName = (String) customerDetails.get("siteName");
		customerDetails.put("siteName", siteName.toUpperCase());
		
		
		int i = 1;
		String residenceType = "";
		String customerNameIncaps = (String) customerDetails.get("customerNameIncaps");
		StringBuffer customerNameWithCoApplicants = new StringBuffer();
		StringBuffer customerNames = new StringBuffer();
		customerNameWithCoApplicants.append(i + ". " + customerNameIncaps);
		customerNames.append( customerNameIncaps);
		for (Co_ApplicantInfo coApp : coApplicantDetailsList) {
			i++;
			String prefix=coApp.getNamePrefix() == null ? "" :coApp.getNamePrefix();
			customerNameWithCoApplicants.append(" and <br/> " + i + ". " +prefix+" "+ coApp.getFirstName().toUpperCase());
			customerNames.append(" and  "+prefix+" "+ coApp.getFirstName().toUpperCase());
		}
		//these condtion for if flat has 1 customer 
		if (i == 1) {
			residenceType = "Residing at";
		} else if (i == 2) {
			//if flat has 1 customer and 1 co-applicant
			residenceType = "Both are Residing at";
		} else if (i >= 3) {
			//if flat has 1 customer and more than one co-applicant
			residenceType = "All are Residing at";
		}
		customerDetails.put("customerNameWithCoApplicants", customerNameWithCoApplicants);
		customerDetails.put("residenceType", residenceType);
		customerDetails.put("customerNames", customerNames);

		for (FlatBookingPojo flatBook : flatBookPojo) {
			String saledeedDate = null;
			String aggrementDate = null;
			//String rrNo=null;
			if (Util.isNotEmptyObject(flatBook.getSaleDeedDate())) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				saledeedDate = formatter.format(flatBook.getSaleDeedDate());
			}
			if (Util.isNotEmptyObject(flatBook.getAgreementDate())) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				aggrementDate = formatter.format(flatBook.getAgreementDate());
			}
			customerUnitDetails.put("saleDeedDate",saledeedDate == null ? "___________" : saledeedDate);
			customerUnitDetails.put("agreementDate",aggrementDate == null ? "___________" : aggrementDate);
			customerUnitDetails.put("saleDeedNo",flatBook.getSaleDeedNo() == null ? "____________" : flatBook.getSaleDeedNo());
			customerUnitDetails.put("saleDeedCDno",flatBook.getSaleDeedCDno() == null ? "___________" : flatBook.getSaleDeedCDno());
			customerUnitDetails.put("propertySlNo",flatBook.getPropertySlNo() == null ? "_____________" : flatBook.getPropertySlNo());
			customerUnitDetails.put("RRNO",flatBook.getRrNo() == null ? "___________" : flatBook.getRrNo());
		}
		//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
	
		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		email.setDataForPdf(dataForPdf1);

		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
	//	info.setDemandNotePdfFileName("NOC_"+customerPropertyDetailsInfo.getFlatNo()+".pdf");
		info.setDemandNotePdfFileName("NOC_"+customerPropertyDetailsInfo.getFlatNo());
		
		setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		
		email.setDemandNoteGeneratorInfo(info);
		info.setCompanyName(info.getCompanyName().toUpperCase());
		//can check builders

		if(builders.contains(customerPropertyDetailsInfo.getFlatSaleOwnerId())) {
			info.setBuilderOrLandOwner("Builder/Promoter");
			info.setCondition("Builder/Promoter");
			if(customerPropertyDetailsInfo.getFlatSaleOwnerId().equals("3")) {//if sale owner is aspire series
				info.setCondition(customerPropertyDetailsInfo.getFlatSaleOwner());	
			}
		} else if(landOwner.contains(customerPropertyDetailsInfo.getFlatSaleOwnerId())) {
			info.setBuilderOrLandOwner("Land Owner");
			info.setCondition("Land Owner");
		} else {
			//if this condition execute means the sale owner not present in builders and landOwner list
			//so u can add in list
			log.info("Please add newly added sale owner id in list.");
			info.setBuilderOrLandOwner("Builder/Promoter");
			info.setCondition("Builder/Promoter");
			//throw new EmployeeFinancialServiceException("Please add newly added sale owner in list.");
		}
		
		if (siteId.equals(111l)) {
			email.setTemplateName("/vmtemplates/NOCLetter_Handing_Over_documents_nan.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Handing_Over_documents(email, fileInfo_Handing_Over_documents);
			fileInfoList.add(fileInfo_Handing_Over_documents);
			email.setTemplateName("/vmtemplates/NOCLetter_Taken_Over_Documents_nan.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Taken_Over_Documents(email, fileInfo_Taken_Over_Documents);
			fileInfoList.add(fileInfo_Taken_Over_Documents);
			email.setTemplateName("/vmtemplates/NOCLetter_Bescom_nan.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Bescom(email, fileInfo_Bescom);
			fileInfoList.add(fileInfo_Bescom);
		} else if(siteId.equals(124l)) {
			email.setTemplateName("/vmtemplates/NOCLetter_Handing_Over_documents_sus.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Handing_Over_documents(email, fileInfo_Handing_Over_documents);
			fileInfoList.add(fileInfo_Handing_Over_documents);
			email.setTemplateName("/vmtemplates/NOCLetter_Taken_Over_Documents_sus.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Taken_Over_Documents(email, fileInfo_Taken_Over_Documents);
			fileInfoList.add(fileInfo_Taken_Over_Documents);
			email.setTemplateName("/vmtemplates/NOCLetter_Bescom_sus.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Bescom(email, fileInfo_Bescom);
			fileInfoList.add(fileInfo_Bescom);
		} else {
			email.setTemplateName("/vmtemplates/NOCLetter_Handing_Over_documents.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Handing_Over_documents(email, fileInfo_Handing_Over_documents);
			fileInfoList.add(fileInfo_Handing_Over_documents);
			email.setTemplateName("/vmtemplates/NOCLetter_Taken_Over_Documents.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Taken_Over_Documents(email, fileInfo_Taken_Over_Documents);
			fileInfoList.add(fileInfo_Taken_Over_Documents);
			email.setTemplateName("/vmtemplates/NOCLetter_Bescom.vm");
			pdfHelper.XMLWorkerHelperForNOCLetter_Bescom(email, fileInfo_Bescom);
			fileInfoList.add(fileInfo_Bescom);
		}
		return fileInfoList;
	}

		public void sendFinancialChequeBouncePushNoti(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
			log.info("***** control inside the EmployeeFinancialHelper.sendFinancialChequeBouncePushNoti() ***** ");
			//CustomerPropertyDetailsInfo customerPropertyDetailsInfo = transactionServiceInfo.getCustomerPropertyDetailsInfo();
			//String invoiceDocument = response1.getFinancialProjectMileStoneResponse().get(0).getDocumentLocation();
			
			EmployeeFinancialPushNotificationInfo  financialPushNotification = new EmployeeFinancialPushNotificationInfo();
			financialPushNotification.setBookingFormId(transactionServiceInfo.getBookingFormId());
			StringBuffer buffer = new StringBuffer("Your Cheque of Rs.").append(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionServiceInfo.getTransactionAmount()).setScale(roundingModeSize, roundingMode).toString()))
					.append(" dated ").append(TimeUtil.getTimeInDD_MM_YYYY(transactionServiceInfo.getTransactionDate()))
					.append(" bearing Cheque number ").append(transactionServiceInfo.getChequeNumber())
					.append(" drawn on bank ").append(transactionServiceInfo.getBankName()).append(" is returned due to Signature mismatch");
					;
			
			financialPushNotification.setNotificationTitle("Alert For Cheque Bounce!");
			financialPushNotification.setNotificationBody(buffer.toString());
			if(transactionServiceInfo.getSiteId()!=null && transactionServiceInfo.getSiteId().equals(131l)) {
				//financialPushNotification.setNotificationTitle("Aspire Aurum Receipt");
				//financialPushNotification.setNotificationBody("Dear Customer your receipt has been generated!.");
				financialPushNotification.setNotificationDescription("");
				financialPushNotification.setTypeMsg("Bounce Transaction");
				financialPushNotification.setTypeOfPushNotificationMsg("Bounce Transaction");
			} else {
				//financialPushNotification.setNotificationTitle("Sumadhura Receipt");
				//financialPushNotification.setNotificationBody("Dear Customer your receipt has been generated!.");
				financialPushNotification.setNotificationDescription("");
				financialPushNotification.setTypeMsg("Bounce Transaction");	
				financialPushNotification.setTypeOfPushNotificationMsg("Bounce Transaction");
			}
			
			financialPushNotification.setStatus("Cheque Bounced");
			financialPushNotification.setPaymentMode(transactionServiceInfo.getTransactionModeName());
			financialPushNotification.setBankName(transactionServiceInfo.getBankName());
			financialPushNotification.setAmount(Util.isEmptyObject(transactionServiceInfo.getTransactionAmount())?"0.00":currencyUtil.getTheAmountWithCommas(transactionServiceInfo.getTransactionAmount(), roundingModeSize, roundingMode));
			financialPushNotification.setReceiptNumber(transactionServiceInfo.getTransactionReceiptNo());
			financialPushNotification.setReferenceNumber(Util.isEmptyObject(transactionServiceInfo.getReferenceNo())?transactionServiceInfo.getChequeNumber():transactionServiceInfo.getReferenceNo().toString());
			financialPushNotification.setClearenceDate(transactionServiceInfo.getChequeClearanceDate()==null?transactionServiceInfo.getTransactionReceiveDate():transactionServiceInfo.getChequeClearanceDate());
			financialPushNotification.setCreatedDate(transactionServiceInfo.getTransactionDate());
			financialPushNotification.setTransactionDate(transactionServiceInfo.getTransactionDate());
			financialPushNotification.setChequeDepositedDate(transactionServiceInfo.getChequeDepositedDate());
			
			financialPushNotification.setChequeBounceDate(transactionServiceInfo.getChequeBounceDate());
			financialPushNotification.setChequeBounceComment(transactionServiceInfo.getChequeBounceComment());
			
			financialPushNotification.setSiteId(transactionServiceInfo.getSiteId());
			/*if(Util.isNotEmptyObject(response1.getFileResponse())) {
				financialPushNotification.setUploadedDocs(response1.getFileResponse().getUrl());
				financialPushNotification.setMileStoneName(response1.getFileResponse().getName());
				financialPushNotification.setInvoiceDocument(invoiceDocument);
			}*/
			//milestone push notification
			pushNotificationHelper.sendFinancialStatusNotification(financialPushNotification,null);	
		
		}
		
		
		public List<FileInfo> generateFoliumCostBreakUpLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
			Email email = new Email();
			FileInfo fileInfo = new FileInfo();
			List<FileInfo> fileInfoList = new ArrayList<>();
			DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
			
			Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
			/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
			List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
			
			String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
			String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
			
			info.setFolderFilePath(folderFilePath);
			info.setFolderFileUrl(folderFileUrl);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
			@SuppressWarnings("unchecked")
			Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
			@SuppressWarnings("unchecked")
			List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
			
			@SuppressWarnings("unchecked")
			//copying milestone objects, instead of using original object
			List<MileStoneInfo> mileStones =  new ArrayList<>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
			@SuppressWarnings("unchecked")
			List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
			@SuppressWarnings("unchecked")
			List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
			@SuppressWarnings("unchecked")
			List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
			List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
			/*Venkatesh B*/
			for (AminitiesInfraCostInfo costInfo : aminitiesInfraCostInfoList) {
				if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - CORNER FLAT")) {
					email.setPlcCornerflatTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
					customerUnitDetails.put("PLC - CORNER FLAT", costInfo.getTotalCost());//Added in customerUnitDetails 
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - EAST FACING")) {
					email.setPlcEastFacingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("INFRA AND AMENITIES")) {
					email.setInfraAndAmenitesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("FLOOR RISE")) {
					email.setFloorRiseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - PREMIUM FACING CHARGES")) {
					email.setPlcPremiumFacingChanrgesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CAR PARKING")) {
					email.setCarPrakingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CLUB HOUSE")) {
					email.setClubHouseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				}
			}
			
			List<Map<String,Object>> mileStonesList = new ArrayList<>();
			List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
			List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
			double sumofMilestonePercentgae = 0.0;
			double sumofMilestoneAmount = 0.0;
			if(mileStones!=null) {
				for (MileStoneInfo ms : mileStones) {
					if(ms.getTotalDueAmount() == null) {
						continue;
					}
					Map<String,Object> mileStone = new HashMap<>();
					double percentage = Double.valueOf(ms.getDue());
					double amount =  Double.valueOf(ms.getTotalDueAmount());
					mileStone.put("milestoneName", ms.getMilestoneName());
					mileStone.put("percentage",BigDecimal.valueOf(percentage).setScale(0, roundingMode).toString());
					mileStone.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amount).setScale(roundingModeSize, roundingMode).toString()));
					mileStone.put("milStoneNo", ms.getMilStoneNo());
					sumofMilestonePercentgae += percentage;
					sumofMilestoneAmount += amount;
					mileStonesList.add(mileStone);
				}
			}
			Map<String,Object> mileStoneTotal = new HashMap<>();
			mileStoneTotal.put("milestoneName", "Total Amount");
			mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
			mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStoneTotal.put("milStoneNo", "");
			mileStonesList.add(mileStoneTotal);
			
			if(termsAndConditions!=null) {
				for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
					if(dynamicKeyValueInfo.getValue()!=null) {
							Map<String,Object> termsAndCondition = new HashMap<>();
							termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
							termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
							termsAndConditionsList.add(termsAndCondition);
					}
				}
			}
			if(siteOtherChargesDetails!=null) {
				Map<String,Object> otherDetails = new HashMap<>();
				for (SiteOtherChargesDetailsPojo other : siteOtherChargesDetails) {
					if(other.getAmount()!=null) {
						//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
						//otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
//				     	if("103".equals(other.getMeteDataTypeId().toString()) && Long.valueOf(133L).equals(customerPropertyDetailsInfo.getSiteId()) || Long.valueOf(126L).equals(customerPropertyDetailsInfo.getSiteId())) {///now
//				     	otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()/2).setScale(roundingModeSize, roundingMode).toString()));
//				    	} else {
//						otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
//					    }
						if (Long.valueOf(133L).equals(customerPropertyDetailsInfo.getSiteId())) {
							if ("103".equals(other.getMeteDataTypeId().toString())&& Long.valueOf(133L).equals(customerPropertyDetailsInfo.getSiteId())) {/// now
								otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount() / 2).setScale(roundingModeSize, roundingMode).toString()));
							} else {
								otherDetails.put(other.getMeteDataTypeId().toString(),
										currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount())
												.setScale(roundingModeSize, roundingMode).toString()));
							}
						}
						if (Long.valueOf(126L).equals(customerPropertyDetailsInfo.getSiteId())) {
							if ("103".equals(other.getMeteDataTypeId().toString())
									&& Long.valueOf(126L).equals(customerPropertyDetailsInfo.getSiteId())) {/// now
								otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount() / 2).setScale(roundingModeSize, roundingMode).toString()));
							} else {
								otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount())
												.setScale(roundingModeSize, roundingMode).toString()));
							}
						}
					}
				}
				siteOtherChargesDetailsList.add(otherDetails);
			}
			//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
			dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
			dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
			dataForPdf1.put("mileStones",  mileStonesList);
			dataForPdf1.put("termsAndConditions", termsAndConditionsList);
			dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
			
			email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
			email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
			email.setEmployeeName(customerDetails.get("empName").toString());
			email.setDataForPdf(dataForPdf1);

			if("Indimax".equals(customerPropertyDetailsInfo.getFlatSaleOwner())) {
				customerDetails.put("HeadingName","INDMAX INFRASTRUCTURE INDIA PVT LTD");
			} else {
				customerDetails.put("HeadingName","SUMADHURA VASAVI INFRASTRUCTURE LLP");
			}
			
			String thanksAndRegardsFrom="";
			String greetingsFrom = "";
			String rightSidelogoForPdf = "";
			String leftSidelogoForPdf = "";
			
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());
			
			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			
			//#ACP
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
			info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
			info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
			info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
			//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
			info.setDemandNotePdfFileName("Cost BreakUp - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
			
			setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
			
			email.setDemandNoteGeneratorInfo(info);
			info.setCompanyName(info.getCompanyName().toUpperCase());
			
			email.setTemplateName("/vmtemplates/COST_BREAK_UP_FOLIUM.vm");
			pdfHelper.XMLWorkerHelperForCostBreakUpLetterFolium(email, fileInfo);
			fileInfoList.add(fileInfo);
			return fileInfoList;
		}
		
		
		@SuppressWarnings("unchecked")
		public List<FileInfo> generateFoliumAgreementDraftHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception{
			log.info("***** Control inside the EmployeeFinancialHelper.generateOlympusAgreementDraftHelper() *****");
			Email email = new Email();
			FileInfo fileInfo = new FileInfo();
			List<FileInfo> fileInfoList = new ArrayList<>();
			DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
			WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
			Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
			/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
			List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
			
			String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
			String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
			
			Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
			Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
			
			List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
			//copying milestone objects, instead of using original object
			List<MileStoneInfo> mileStonesDetailsList =  new ArrayList<MileStoneInfo>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
			
			List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
			List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
			
			List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos =  (List<FinTransactionEntryDetailsInfo>) dataForGenerateAllotmentLetterHelper.get("transactionEntryDetailsInfos");
			List<FinPenaltyTaxPojo> taxDetailsList = (List<FinPenaltyTaxPojo>) dataForGenerateAllotmentLetterHelper.get("taxDetailsList");
			List<CustomerInfo> customerDetailsList = (List<CustomerInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantDetails");
			List<Co_ApplicantInfo> coApplicantDetailsList = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
			
			List<ProfessionalInfo> firstApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantProfessionalDetails");
			List<ProfessionalInfo> coApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantProfessionalDetails");	
			
			List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
			Double schemeGSTPercentage = (Double) customerDetails.get("schemeGSTPercentage");
			Double totalMilestonePaidAmount = (Double) customerDetails.get("totalMilestonePaidAmount");
			FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
			//FinPenaltyTaxPojo finTaxPojo = taxDetailsList.get(0);
			FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
			List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
			
			List<Map<String,Object>> mileStonesList = new ArrayList<>();
			List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
			List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
			double totalAgreementCost = 0.0;
			
			@SuppressWarnings("unused")
			double sumofMilestonePercentgae = 0.0;
			double sumofMilestonePercentgaeExclFirst = 0.0;//sum of all milestone percentage but excluding first
			@SuppressWarnings("unused")
			double sumofMilestoneAmount = 0.0;
			double sumofMilestoneAmountExclFirst = 0.0;//sum of all milestone amount but excluding first
			double maintenancePercentage = 0.0;
			double legalCostPercentage = 0.0;
			double maintenanceAmount = 0.0;
			double sumOfBasicCost = 0.0;
			double sumOfAllComponentGstAmount = 0.0;
			double grandTotalIncludingMaintenance = 0.0;
			//double sumOfTotalAmount = 0.0;
			long serialNo = 1l;
			double totalFlatCostExcludingGST = 0.0;
			double sbuaIntoCarpusFundSum = 0.0;
			double flatLegalAndDocumentationChargesAmt = 0.0;
			//double sbuaValue=0.0;
			if(termsAndConditions!=null) {
				for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
					if(dynamicKeyValueInfo.getValue()!=null) {
							Map<String,Object> termsAndCondition = new HashMap<>();
							termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
							termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
							termsAndConditionsList.add(termsAndCondition);
					}
				}
			}
			
			if (taxDetailsList != null) {
				for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
					if (MetadataId.MAINTENANCE_CHARGE.getId().equals(finTaxPojo.getTaxType())) {
						maintenancePercentage = finTaxPojo.getPercentageValue();
					} else if (MetadataId.LEGAL_COST.getId().equals(finTaxPojo.getTaxType())) {
						legalCostPercentage = finTaxPojo.getPercentageValue();
					}
				}
			}
			
			if (siteOtherChargesDetails != null) {
				String strAmount = "";
				Map<String, Object> siteOtherDetails = new HashMap<>();
				for (SiteOtherChargesDetailsPojo otherDetailsDraft : siteOtherChargesDetails) {
					if (otherDetailsDraft.getAmount() != null) {
						//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
						if (MetadataId.MAINTENANCE_CHARGE.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
							//if this code is changed same code to be changed in NOC validation code
							//maintenanceAmount = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
							maintenanceAmount = flatBookingInfo.getSbua() * otherDetailsDraft.getAmount();
						} else if(MetadataId.CORPUS_FUND.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
							//sbuaValue = flatBookingInfo.getSbua();
							//sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
							sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() ) * otherDetailsDraft.getAmount();
						}  else if(MetadataId.LEGAL_COST.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
							flatLegalAndDocumentationChargesAmt =  otherDetailsDraft.getAmount();
						}
						strAmount = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(otherDetailsDraft.getAmount()).setScale(roundingModeSize, roundingMode).toString());
						otherDetailsDraft.setStrAmount(strAmount);
						otherDetailsDraft.setStrAmountInWords(getTheAmountInWords(otherDetailsDraft.getAmount()));
						
						siteOtherDetails.put(otherDetailsDraft.getMeteDataTypeId().toString(),otherDetailsDraft);
						
					}
				}
				siteOtherChargesDetailsList.add(siteOtherDetails);
			}
			siteOtherChargesDetailsList.get(0).get("104");
			//maintenanceAmount = (flatBookingInfo.getSbua()*1d)*maintenanceAmount;
			List<AgreementDraftCalculations> agreementDraftCalculations = new ArrayList<>();
			if (flatCostInfo != null) {
				double gstAmount = 0.0;
				Double basicAmount = flatCostInfo.getBasicFlatCost();
				Double amenitiesFlatCost = flatCostInfo.getAmenitiesFlatCost();
				totalFlatCostExcludingGST = basicAmount+amenitiesFlatCost;
				totalAgreementCost = flatCostInfo.getTotalCost();
				
				double mileStonePercentage = schemeGSTPercentage;
				gstAmount = (basicAmount / 100) * mileStonePercentage;//for Sushantham site gstAmount on basicAmount 

				AgreementDraftCalculations data = new AgreementDraftCalculations();
				data.setSerialNo(serialNo);
				data.setParticulars("Cost of the Apartment");//Cost of the Apartment is nothing but basic flat cost 
				data.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
				data.setGstPercentage(schemeGSTPercentage.toString());
				data.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(gstAmount).setScale(roundingModeSize, roundingMode).toString()));
				data.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()+gstAmount).setScale(roundingModeSize, roundingMode).toString()));

				agreementDraftCalculations.add(data);
				
				sumOfBasicCost += flatCostInfo.getBasicFlatCost();
				sumOfAllComponentGstAmount += gstAmount;
				serialNo++;
			}

			if (aminitiesInfraCostInfoList != null) {//taking all the amenities details
				for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfoList) {
					if (aminitiesInfraCostInfo.getTotalCost() == null || aminitiesInfraCostInfo.getTotalCost() == 0) {
						continue;
					}
					System.out.println(aminitiesInfraCostInfo.getTotalCost()+" "+aminitiesInfraCostInfo.getAminititesInfraName()+" "+aminitiesInfraCostInfo.getAminititesInfraCost()+" "+aminitiesInfraCostInfo.getFlatCostId());
					//double amenitiesGstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(aminitiesInfraCostInfo.getTotalCost()));
					double amenitiesGstAmount = (aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100;
					System.out.println((aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100);
					//double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost()-amenitiesGstAmount;
					double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost();
					aminitiesInfraCostInfo.setGstAmount(amenitiesGstAmount);
			
					AgreementDraftCalculations data = new AgreementDraftCalculations();
					data.setSerialNo(serialNo);
					data.setParticulars(aminitiesInfraCostInfo.getAminititesInfraName());
					data.setBasicCost(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount).setScale(roundingModeSize, roundingMode).toString())));
					data.setGstPercentage(schemeGSTPercentage.toString());
					data.setGstAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
					data.setTotalAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount+amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
					
					sumOfBasicCost += amenitiesBasicAmount;
					sumOfAllComponentGstAmount += amenitiesGstAmount;

					agreementDraftCalculations.add(data);
					serialNo++;
				}
			}
			double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
			double sumOfCustomerPaidAmount = 0.0d;
			boolean breakTheLoop = false;
			double transactionAmount = 0.0;
			double transactionAmountGST = 0.0;
			double transactionAmountExcludeGST = 0.0;
			List<FinTransactionEntryDetailsInfo> copytransactionEntryDetailsInfos = new ArrayList<>();
			if(transactionEntryDetailsInfos!=null) {//taking transaction amount upto 10% paid
				for (FinTransactionEntryDetailsInfo entryDetailsInfo : transactionEntryDetailsInfos) {
					if(Util.isEmptyObject(entryDetailsInfo.getBankName())) {
						entryDetailsInfo.setBankName("-");
					}
					transactionAmount = entryDetailsInfo.getTransactionAmount();
					sumOfCustomerPaidAmount += transactionAmount;
					if(sumOfCustomerPaidAmount>=tenPercentAmountOnFlat) {
						breakTheLoop = true;//need to show only 10% amount data
						transactionAmount = (sumOfCustomerPaidAmount-transactionAmount)-tenPercentAmountOnFlat;
					}
					//schemeGSTPercentage double gstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(transactionAmount);
					transactionAmountGST = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(transactionAmount));
					transactionAmountExcludeGST = transactionAmount-transactionAmountGST;//transactionAmount*(100/schemeGSTPercentage);
					
					entryDetailsInfo.setStrTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmount).setScale(roundingModeSize, roundingMode).toString()));
					entryDetailsInfo.setTransactionAmountInWords(getTheAmountInWords(transactionAmount));
					
					entryDetailsInfo.setStrTransactionAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
					entryDetailsInfo.setTransactionAmountExcludeGSTInWords(getTheAmountInWords(transactionAmountExcludeGST));
					
					entryDetailsInfo.setStrTransactionAmountGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountGST).setScale(roundingModeSize, roundingMode).toString()));
					entryDetailsInfo.setTransactionAmountGSTInWords(getTheAmountInWords(transactionAmountGST));
					copytransactionEntryDetailsInfos.add(entryDetailsInfo);
					if(breakTheLoop) {break;}
				}
			}
			transactionEntryDetailsInfos = copytransactionEntryDetailsInfos;
			//*************************************
			double msPercentage = 0.0;
			double msAmount =  0.0;
			double dueAmountExcludeGST = 0.0;
			double milestoneGstAmount = 0.0;
			String strMSAmt = "";
			String strMSPercentage = "";

			@SuppressWarnings("unused")
			long milestoneNo = 0;
			if(mileStonesDetailsList!=null) {
				for (MileStoneInfo ms : mileStonesDetailsList) {
					if(ms.getTotalDueAmount() == null) {
						continue;
					}
					Map<String, Object> mileStone = new HashMap<>();
					msPercentage = Double.valueOf(ms.getDue());
					msAmount = Double.valueOf(ms.getTotalDueAmount());
					dueAmountExcludeGST = Double.valueOf(ms.getDueAmountExcludeGST());
					milestoneGstAmount = Double.valueOf(ms.getGstAmount());
					 
				    strMSAmt = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(msAmount).setScale(roundingModeSize, roundingMode).toString());
					strMSPercentage = BigDecimal.valueOf(msPercentage).setScale(0, roundingMode).toString();
					mileStone.put("milestoneName", ms.getMilestoneName());
					mileStone.put("percentage",strMSPercentage);
					mileStone.put("totalDueAmount", strMSAmt);
					mileStone.put("milStoneNo", ms.getMilStoneNo());
					ms.setTotalDueAmount(strMSAmt);
					ms.setDue(strMSPercentage+"%");
					ms.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(dueAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
					ms.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(milestoneGstAmount).setScale(roundingModeSize, roundingMode).toString()));
					sumofMilestonePercentgae += msPercentage;
					sumofMilestoneAmount += msAmount;
					//milestoneNo = ms.getMilStoneNo();
					if (ms.getMilStoneNo() != 1) {
						sumofMilestonePercentgaeExclFirst += msPercentage;
						sumofMilestoneAmountExclFirst += msAmount;
					}
					
					mileStonesList.add(mileStone);
				}
			}
			
			/*Map<String,Object> mileStoneTotal = new HashMap<>();
			mileStoneTotal.put("milestoneName", "Total Amount");
			mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
			mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStoneTotal.put("milStoneNo", "");
			mileStonesList.add(mileStoneTotal);*/
			
			double maintenanceGstAmount = (maintenanceAmount * maintenancePercentage) / 100;
			double flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * legalCostPercentage) / 100;
			//double maintenanceAmtWithGstAmount =  maintenanceAmount+maintenanceGstAmount;
			if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
				//this object for flat basic cost and amenities amount sum
				/*AgreementDraftCalculations MaintenanceData = new AgreementDraftCalculations();
				MaintenanceData.setSerialNo(serialNo);
				MaintenanceData.setParticulars("Maintenance for 12 Months");
				MaintenanceData.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
				MaintenanceData.setGstPercentage(String.valueOf(maintenancePercentage));
				MaintenanceData.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				MaintenanceData.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				agreementDraftCalculations.add(MaintenanceData);*/
			} else {//Maintenance amount not adding for olympus flat basic flat and gst
				//maintenanceGstAmount = 0.0;
				//maintenanceAmount = 0.0;
				//maintenanceAmtWithGstAmount = 0.0;
			}
			System.out.println((96*2880)+ ((96*2880)*18/100));
			System.out.println(maintenanceGstAmount+maintenanceAmount);		

			//totalAmtData object for flat basic cost and amenities amount sum
			AgreementDraftCalculations totalAmtDataObject = new AgreementDraftCalculations();
			totalAmtDataObject.setSerialNo(serialNo);
			totalAmtDataObject.setParticulars("Total");
			totalAmtDataObject.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
			totalAmtDataObject.setGstPercentage("");
			totalAmtDataObject.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			totalAmtDataObject.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost+sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			
			agreementDraftCalculations.add(totalAmtDataObject);
			/*if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
				//Maintenance Charges object for milestone list
				MileStoneInfo maintenanceDetailsForMS = new MileStoneInfo();
				maintenanceDetailsForMS.setMilStoneNo(++milestoneNo);
				maintenanceDetailsForMS.setMilestoneName("Maintenance Charges payable on final payment request");
				maintenanceDetailsForMS.setDue("-");
				maintenanceDetailsForMS.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				mileStonesDetailsList.add(maintenanceDetailsForMS);
				sumofMilestoneAmount+= maintenanceAmtWithGstAmount;
			}*/
			
			//total amount object for milestone list
			MileStoneInfo ms = new MileStoneInfo();
			ms.setMilStoneNo(null);
			ms.setMilestoneName("Total Amount");
			ms.setDue(BigDecimal.valueOf(sumofMilestonePercentgaeExclFirst).setScale(0, roundingMode).toString()+"%");
			//ms.setDue(BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString()+"%");
			ms.setDueAmountExcludeGST("");
			ms.setGstAmount("");
			ms.setThisLastMsRecord(true);
			ms.isThisLastMsRecord();
			//ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmountExclFirst).setScale(roundingModeSize, roundingMode).toString()));
			mileStonesDetailsList.add(ms);
			
			
			double totalBalanceAmountOnFlat = 0.0;
			double tenPercentGSTAmount = 0.0;
			if (totalMilestonePaidAmount > tenPercentAmountOnFlat) {
				
			} else {//if 10% percent of total flat amount is not paid, then take only paid amount of milestone
				tenPercentAmountOnFlat = totalMilestonePaidAmount;
			}
			//tenPercentGSTAmount = (tenPercentAmountOnFlat*10)/100;//gst Amount on 10 % paid amount
			tenPercentGSTAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(tenPercentAmountOnFlat));//gst Amount on 10 % paid amount
			grandTotalIncludingMaintenance = sumOfBasicCost+sumOfAllComponentGstAmount;
			totalBalanceAmountOnFlat = grandTotalIncludingMaintenance-tenPercentAmountOnFlat;
			//total amount of the flat is with maintenance amount = total cost of the flat + maintenance cost = 
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenance(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalIncludingMaintenance).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceInWords(getTheAmountInWords(grandTotalIncludingMaintenance));
			welcomeMailGeneratorInfo.setTotalBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExcludingGST).setScale(roundingModeSize, roundingMode).toString()));
			//welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
			//welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
			//Total flat cost - total paid amount = balance amount
			welcomeMailGeneratorInfo.setTotalBalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalBalanceAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalBalanceAmountInWords(getTheAmountInWords(totalBalanceAmountOnFlat));
			//total paid amount to the booking
			welcomeMailGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalPaidAmountInWords(getTheAmountInWords(tenPercentAmountOnFlat));
			//total paid GST
			welcomeMailGeneratorInfo.setTotalPaidGSTAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalPaidGSTAmountInWords(getTheAmountInWords(tenPercentGSTAmount));
			//total paid with out GST means basic amount
			welcomeMailGeneratorInfo.setTotalPaidAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat-tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalPaidAmountExcludeGSTInWords(getTheAmountInWords(tenPercentAmountOnFlat-tenPercentGSTAmount));		
			//
			customerUnitDetails.put("sbuaIntoCarpusFundSum",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
			customerUnitDetails.put("sbuaIntoMaintenanceSumWithGST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount+maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
			customerUnitDetails.put("flat_legal_and_doc_charges_Sum_With_GST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			customerUnitDetails.put("flat_legal_cost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt).setScale(roundingModeSize, roundingMode).toString()));
			customerUnitDetails.put("schedule_c_payment_plan_sum",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum+(maintenanceGstAmount+maintenanceAmount)+(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount)).setScale(roundingModeSize, roundingMode).toString()));
			
			//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
			dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
			dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
			//dataForPdf1.put("mileStones",  mileStonesList);
			dataForPdf1.put("termsAndConditions", termsAndConditionsList);
			dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
			
			email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
			email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
			email.setEmployeeName(customerDetails.get("empName").toString());
			
			email.setDataForPdf(dataForPdf1);
			String leftSidelogoForPdf = "";
			String rightSidelogoForPdf = "";
			String thanksAndRegardsFrom = "";
			String greetingsFrom = "";
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());

			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
			info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
			info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
			info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
			//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Agreement Draft.pdf");
			info.setDemandNotePdfFileName("Agreement Draft - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
			
			info.setFolderFilePath(folderFilePath);
			info.setFolderFileUrl(folderFileUrl);
			welcomeMailGeneratorInfo.setCustomerDetailsList(customerDetailsList);
			welcomeMailGeneratorInfo.setCoApplicantDetailsList(coApplicantDetailsList);
			welcomeMailGeneratorInfo.setFirstApplicantProfessionalDetails(firstApplicantProfessionalDetails);
			welcomeMailGeneratorInfo.setCoApplicantProfessionalDetailsList(coApplicantProfessionalDetails);
			welcomeMailGeneratorInfo.setOfficeDetailsPojoList(officeDetailsPojoList);
			welcomeMailGeneratorInfo.setMileStones(mileStonesDetailsList);
			welcomeMailGeneratorInfo.setSiteOtherChargesDetails(siteOtherChargesDetails);
			welcomeMailGeneratorInfo.setTermsAndConditions(termsAndConditions);
			welcomeMailGeneratorInfo.setAgreementDraftCalculations(agreementDraftCalculations);
			welcomeMailGeneratorInfo.setTransactionEntryDetailsInfos(transactionEntryDetailsInfos);
			
			//welcomeMailGeneratorInfo.setDataForPdf(dataForPdf1);
			
			setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
			
			email.setWelcomeMailGeneratorInfo(welcomeMailGeneratorInfo);
			email.setDemandNoteGeneratorInfo(info);
			//template name to construct pdf
			
//			if(customerPropertyDetailsInfo.getFlatSaleOwner()!=null && customerPropertyDetailsInfo.getFlatSaleOwner().equalsIgnoreCase("Indimax")) {
//				email.setTemplateName("/vmtemplates/Agreement_134_INDMAX_Draft.vm");	
//			}else {
//				email.setTemplateName("/vmtemplates/Agreement_134_VASAVI_Draft.vm");
//			}
			
			pdfHelper.XMLWorkerHelperForAgreementDraftLetterFolium(email, fileInfo);
			fileInfoList.add(fileInfo);
			return fileInfoList;
		}
		
		public List<FileInfo> generateHorizonCostBreakUpLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
			Email email = new Email();
			FileInfo fileInfo = new FileInfo();
			List<FileInfo> fileInfoList = new ArrayList<>();
			DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
			
			Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
			/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
			List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
			
			String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
			String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
			
			info.setFolderFilePath(folderFilePath);
			info.setFolderFileUrl(folderFileUrl);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
			@SuppressWarnings("unchecked")
			Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
			@SuppressWarnings("unchecked")
			List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
			
			@SuppressWarnings("unchecked")
			//copying milestone objects, instead of using original object
			List<MileStoneInfo> mileStones =  new ArrayList<>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
			@SuppressWarnings("unchecked")
			List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
			@SuppressWarnings("unchecked")
			List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
			@SuppressWarnings("unchecked")
			List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
			List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
			/*Venkatesh B*/
			for (AminitiesInfraCostInfo costInfo : aminitiesInfraCostInfoList) {
				if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - CORNER FLAT")) {
					email.setPlcCornerflatTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
					customerUnitDetails.put("PLC - CORNER FLAT", costInfo.getTotalCost());//Added in customerUnitDetails 
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - EAST FACING")) {
					email.setPlcEastFacingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("INFRA AND AMENITIES")) {
					email.setInfraAndAmenitesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("FLOOR RISE")) {
					email.setFloorRiseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - PREMIUM FACING CHARGES")) {
					email.setPlcPremiumFacingChanrgesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CAR PARKING")) {
					email.setCarPrakingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CLUB HOUSE")) {
					email.setClubHouseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				}
			}
			
			List<Map<String,Object>> mileStonesList = new ArrayList<>();
			List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
			List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
			double sumofMilestonePercentgae = 0.0;
			double sumofMilestoneAmount = 0.0;
			if(mileStones!=null) {
				for (MileStoneInfo ms : mileStones) {
					if(ms.getTotalDueAmount() == null) {
						continue;
					}
					Map<String,Object> mileStone = new HashMap<>();
					double percentage = Double.valueOf(ms.getDue());
					double amount =  Double.valueOf(ms.getTotalDueAmount());
					mileStone.put("milestoneName", ms.getMilestoneName());
					mileStone.put("percentage",BigDecimal.valueOf(percentage).setScale(0, roundingMode).toString());
					mileStone.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amount).setScale(roundingModeSize, roundingMode).toString()));
					mileStone.put("milStoneNo", ms.getMilStoneNo());
					sumofMilestonePercentgae += percentage;
					sumofMilestoneAmount += amount;
					mileStonesList.add(mileStone);
				}
			}
			Map<String,Object> mileStoneTotal = new HashMap<>();
			//not require for horizon
//			mileStoneTotal.put("milestoneName", "Total Amount");
//			mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
//			mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
//			mileStoneTotal.put("milStoneNo", "");
//			mileStonesList.add(mileStoneTotal);
			
			if(termsAndConditions!=null) {
				for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
					if(dynamicKeyValueInfo.getValue()!=null) {
							Map<String,Object> termsAndCondition = new HashMap<>();
							termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
							termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
							termsAndConditionsList.add(termsAndCondition);
					}
				}
			}
			if(siteOtherChargesDetails!=null) {
				Map<String,Object> otherDetails = new HashMap<>();
				for (SiteOtherChargesDetailsPojo other : siteOtherChargesDetails) {
					if(other.getAmount()!=null) {
						//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
						otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
						
					}
				}
				siteOtherChargesDetailsList.add(otherDetails);
			}
			//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
			dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
			dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
			dataForPdf1.put("mileStones",  mileStonesList);
			dataForPdf1.put("termsAndConditions", termsAndConditionsList);
			dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
			
			email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
			email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
			email.setEmployeeName(customerDetails.get("empName").toString());
			email.setDataForPdf(dataForPdf1);

			if("Indimax".equals(customerPropertyDetailsInfo.getFlatSaleOwner())) {
				customerDetails.put("HeadingName","INDMAX INFRASTRUCTURE INDIA PVT LTD");
			} else {
				customerDetails.put("HeadingName","SUMADHURA VASAVI INFRASTRUCTURE LLP");
			}
			
			String thanksAndRegardsFrom="";
			String greetingsFrom = "";
			String rightSidelogoForPdf = "";
			String leftSidelogoForPdf = "";
			
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());
			
			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			
			//#ACP
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
			info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
			info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
			info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
			//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
			info.setDemandNotePdfFileName("Cost BreakUp - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
			
			setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
			
			email.setDemandNoteGeneratorInfo(info);
			info.setCompanyName(info.getCompanyName().toUpperCase());
			
			email.setTemplateName("/vmtemplates/COST_BREAK_UP_HORIZON.vm");
			pdfHelper.XMLWorkerHelperForCostBreakUpLetterFolium(email, fileInfo);
			fileInfoList.add(fileInfo);
			return fileInfoList;
		}	

	
		public List<FileInfo> generateSST2BreakUpLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
			Email email = new Email();
			FileInfo fileInfo = new FileInfo();
			List<FileInfo> fileInfoList = new ArrayList<>();
			DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
			
			Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
			/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
			List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
			
			String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
			String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
			
			info.setFolderFilePath(folderFilePath);
			info.setFolderFileUrl(folderFileUrl);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
			@SuppressWarnings("unchecked")
			Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
			@SuppressWarnings("unchecked")
			List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
			
			@SuppressWarnings("unchecked")
			//copying milestone objects, instead of using original object
			List<MileStoneInfo> mileStones =  new ArrayList<>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
			@SuppressWarnings("unchecked")
			List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
			@SuppressWarnings("unchecked")
			List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
			@SuppressWarnings("unchecked")
			List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
			List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
			/*Venkatesh B*/
			for (AminitiesInfraCostInfo costInfo : aminitiesInfraCostInfoList) {
				if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - CORNER FLAT")) {
					email.setPlcCornerflatTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
					customerUnitDetails.put("PLC - CORNER FLAT", costInfo.getTotalCost());//Added in customerUnitDetails 
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - EAST FACING")) {
					email.setPlcEastFacingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("INFRA AND AMENITIES")) {
					email.setInfraAndAmenitesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("FLOOR RISE")) {
					email.setFloorRiseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("PLC - PREMIUM FACING CHARGES")) {
					email.setPlcPremiumFacingChanrgesTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CAR PARKING")) {
					email.setCarPrakingTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				} else if (costInfo.getAminititesInfraName().equalsIgnoreCase("CLUB HOUSE")) {
					email.setClubHouseTotalCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(costInfo.getTotalCost()).setScale(roundingModeSize, roundingMode).toString()));
				}
			}
			
			List<Map<String,Object>> mileStonesList = new ArrayList<>();
			List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
			List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
			double sumofMilestonePercentgae = 0.0;
			double sumofMilestoneAmount = 0.0;
			if(mileStones!=null) {
				for (MileStoneInfo ms : mileStones) {
					if(ms.getTotalDueAmount() == null) {
						continue;
					}
					Map<String,Object> mileStone = new HashMap<>();
					double percentage = Double.valueOf(ms.getDue());
					double amount =  Double.valueOf(ms.getTotalDueAmount());
					mileStone.put("milestoneName", ms.getMilestoneName());
					mileStone.put("percentage",BigDecimal.valueOf(percentage).setScale(0, roundingMode).toString());
					mileStone.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amount).setScale(roundingModeSize, roundingMode).toString()));
					mileStone.put("milStoneNo", ms.getMilStoneNo());
					sumofMilestonePercentgae += percentage;
					sumofMilestoneAmount += amount;
					mileStonesList.add(mileStone);
				}
			}
			Map<String,Object> mileStoneTotal = new HashMap<>();
			//not require for horizon
			mileStoneTotal.put("milestoneName", "Total Amount");
			mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
			mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStoneTotal.put("milStoneNo", "");
			mileStonesList.add(mileStoneTotal);
			
			if(termsAndConditions!=null) {
				for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
					if(dynamicKeyValueInfo.getValue()!=null) {
							Map<String,Object> termsAndCondition = new HashMap<>();
							termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
							termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
							termsAndConditionsList.add(termsAndCondition);
					}
				}
			}
			if(siteOtherChargesDetails!=null) {
				Map<String,Object> otherDetails = new HashMap<>();
				for (SiteOtherChargesDetailsPojo other : siteOtherChargesDetails) {
					if(other.getAmount()!=null) {
						//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
						if (customerPropertyDetailsInfo.getSiteId().equals(130l)) {
							if ("103".equals(other.getMeteDataTypeId().toString())) {
								otherDetails.put(other.getMeteDataTypeId().toString(),currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount() / 2).setScale(roundingModeSize, roundingMode).toString()));
							} else {
								otherDetails.put(other.getMeteDataTypeId().toString(),
										currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount()).setScale(roundingModeSize, roundingMode).toString()));
							}
						} else {
							otherDetails.put(other.getMeteDataTypeId().toString(),
									currencyUtil.convertUstoInFormat(BigDecimal.valueOf(other.getAmount())
											.setScale(roundingModeSize, roundingMode).toString()));
						}
					}
				}
				siteOtherChargesDetailsList.add(otherDetails);
			}
			//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
			dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
			dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
			dataForPdf1.put("mileStones",  mileStonesList);
			dataForPdf1.put("termsAndConditions", termsAndConditionsList);
			dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
			
			email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
			email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
			email.setEmployeeName(customerDetails.get("empName").toString());
			email.setDataForPdf(dataForPdf1);

			if("Indimax".equals(customerPropertyDetailsInfo.getFlatSaleOwner())) {
				customerDetails.put("HeadingName","INDMAX INFRASTRUCTURE INDIA PVT LTD");
			} else {
				customerDetails.put("HeadingName","SUMADHURA VASAVI INFRASTRUCTURE LLP");
			}
			
			String thanksAndRegardsFrom="";
			String greetingsFrom = "";
			String rightSidelogoForPdf = "";
			String leftSidelogoForPdf = "";
			
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());
			
			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			
			//#ACP
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
			info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
			info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
			info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
			//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Cost BreakUp.pdf");
			info.setDemandNotePdfFileName("Cost BreakUp - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
			
			setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
			
			email.setDemandNoteGeneratorInfo(info);
			info.setCompanyName(info.getCompanyName().toUpperCase());
			
			email.setTemplateName("/vmtemplates/COST_BREAK_UP_SUSH_PHASE2.vm");
			pdfHelper.XMLWorkerHelperForCostBreakUpLetterSST2(email, fileInfo);
			fileInfoList.add(fileInfo);
			return fileInfoList;
		}
		@SuppressWarnings("unchecked")
		public List<FileInfo> generateSST2AgreementDraftHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception{
			log.info("***** Control inside the EmployeeFinancialHelper.generateAgreementDraftHelper() *****");
			Email email = new Email();
			FileInfo fileInfo = new FileInfo();
			List<FileInfo> fileInfoList = new ArrayList<>();
			DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
			WelcomeMailGeneratorInfo welcomeMailGeneratorInfo = new WelcomeMailGeneratorInfo();
			Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
			/*List<Map<String,Object>> customerDetailsList = new ArrayList<>();
			List<Map<String,Object>> customerUnitDetailsList = new ArrayList<>();*/
			
			String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
			String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();
			
			Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
			Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
			
			List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");	
			//copying milestone objects, instead of using original object
			List<MileStoneInfo> mileStonesDetailsList =  new ArrayList<MileStoneInfo>((List<MileStoneInfo>) dataForGenerateAllotmentLetterHelper.get("mileStones"));
			
			List<DynamicKeyValueInfo> termsAndConditions =  (List<DynamicKeyValueInfo>) dataForGenerateAllotmentLetterHelper.get("termsAndConditions");
			List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails = (List<SiteOtherChargesDetailsPojo>) dataForGenerateAllotmentLetterHelper.get("siteOtherChargesDetailsList");
			
			List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos =  (List<FinTransactionEntryDetailsInfo>) dataForGenerateAllotmentLetterHelper.get("transactionEntryDetailsInfos");
			List<FinPenaltyTaxPojo> taxDetailsList = (List<FinPenaltyTaxPojo>) dataForGenerateAllotmentLetterHelper.get("taxDetailsList");
			List<CustomerInfo> customerDetailsList = (List<CustomerInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantDetails");
			List<Co_ApplicantInfo> coApplicantDetailsList = (List<Co_ApplicantInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantDetails");
			
			List<ProfessionalInfo> firstApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("firstApplicantProfessionalDetails");
			List<ProfessionalInfo> coApplicantProfessionalDetails = (List<ProfessionalInfo>) dataForGenerateAllotmentLetterHelper.get("coApplicantProfessionalDetails");	
			
			List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
			Double schemeGSTPercentage = (Double) customerDetails.get("schemeGSTPercentage");
			Double totalMilestonePaidAmount = (Double) customerDetails.get("totalMilestonePaidAmount");
			FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
			//FinPenaltyTaxPojo finTaxPojo = taxDetailsList.get(0);
			FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
			List<AminitiesInfraCostInfo> aminitiesInfraCostInfoList = flatBookingInfos.get(0).getAminitiesInfraCostInfo();
			
			List<Map<String,Object>> mileStonesList = new ArrayList<>();
			List<Map<String,Object>> termsAndConditionsList = new ArrayList<>();
			List<Map<String,Object>> siteOtherChargesDetailsList = new ArrayList<>();
			double totalAgreementCost = 0.0;
			@SuppressWarnings("unused")
			double sumofMilestonePercentgae = 0.0;
			double sumofMilestoneAmount = 0.0;
			double maintenancePercentage = 0.0;
			double maintenanceAmount = 0.0;
			double sumOfBasicCost = 0.0;
			double sumOfAllComponentGstAmount = 0.0;
			double grandTotalIncludingMaintenance = 0.0;
			//double sumOfTotalAmount = 0.0;
			long serialNo = 1l;
			double totalFlatCostExcludingGST = 0.0;
			double sbuaIntoCarpusFundSum = 0.0;
			double flatLegalAndDocumentationChargesAmt = 0.0;
			double legalCostPercentage = 0.0;
			//double sbuaValue=0.0;
			if(termsAndConditions!=null) {
				for (DynamicKeyValueInfo dynamicKeyValueInfo : termsAndConditions) {
					if(dynamicKeyValueInfo.getValue()!=null) {
							Map<String,Object> termsAndCondition = new HashMap<>();
							termsAndCondition.put("key", dynamicKeyValueInfo.getKey());
							termsAndCondition.put("value",dynamicKeyValueInfo.getValue());
							termsAndConditionsList.add(termsAndCondition);
					}
				}
			}
			if (taxDetailsList != null) {
				for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
					if (MetadataId.MAINTENANCE_CHARGE.getId().equals(finTaxPojo.getTaxType())) {
						maintenancePercentage = finTaxPojo.getPercentageValue();
					} else if (MetadataId.LEGAL_COST.getId().equals(finTaxPojo.getTaxType())) {
						legalCostPercentage = finTaxPojo.getPercentageValue();
					}
				}
			}
			
			if (siteOtherChargesDetails != null) {
				String strAmount = "";
				Map<String, Object> siteOtherDetails = new HashMap<>();
				for (SiteOtherChargesDetailsPojo otherDetailsDraft : siteOtherChargesDetails) {
					if (otherDetailsDraft.getAmount() != null) {
						//otherDetails.put("meteDataTypeId", other.getMeteDataTypeId());
						if (MetadataId.MAINTENANCE_CHARGE.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
							//maintenanceAmount = other.getAmount();
							//maintenanceAmount = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
							maintenanceAmount = (flatBookingInfo.getSbua()) * otherDetailsDraft.getAmount();
						} else if(MetadataId.CORPUS_FUND.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
							//sbuaValue = flatBookingInfo.getSbua();
							//sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() * otherDetailsDraft.getAmtForYears()) * otherDetailsDraft.getAmount();
							sbuaIntoCarpusFundSum = (flatBookingInfo.getSbua() ) * otherDetailsDraft.getAmount();
						}else if(MetadataId.LEGAL_COST.getId().equals(otherDetailsDraft.getMeteDataTypeId())) {
							flatLegalAndDocumentationChargesAmt =  otherDetailsDraft.getAmount();
						}
						strAmount = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(otherDetailsDraft.getAmount()).setScale(roundingModeSize, roundingMode).toString());
						otherDetailsDraft.setStrAmount(strAmount);
						otherDetailsDraft.setStrAmountInWords(getTheAmountInWords(otherDetailsDraft.getAmount()));
						
						siteOtherDetails.put(otherDetailsDraft.getMeteDataTypeId().toString(),otherDetailsDraft);
						
					}
				}
				siteOtherChargesDetailsList.add(siteOtherDetails);
			}
			System.out.println("maintenanceAmount " + (maintenanceAmount)+ (maintenanceAmount)*18);
			customerUnitDetails.put("sbuaIntoCarpusFundSum", sbuaIntoCarpusFundSum);
			customerUnitDetails.put("maintenanceAmount", maintenanceAmount);
			
			siteOtherChargesDetailsList.get(0).get("104");
			//maintenanceAmount = (flatBookingInfo.getSbua()*1d)*maintenanceAmount;
			List<AgreementDraftCalculations> agreementDraftCalculations = new ArrayList<>();
			if (flatCostInfo != null) {
				double gstAmount = 0.0;
				Double basicAmount = flatCostInfo.getBasicFlatCost();
				Double amenitiesFlatCost = flatCostInfo.getAmenitiesFlatCost();
				totalFlatCostExcludingGST = basicAmount+amenitiesFlatCost;
				totalAgreementCost = flatCostInfo.getTotalCost();
				
				double mileStonePercentage = schemeGSTPercentage;
				gstAmount = (basicAmount / 100) * mileStonePercentage;//for Sushantham site gstAmount on basicAmount 

				AgreementDraftCalculations data = new AgreementDraftCalculations();
				data.setSerialNo(serialNo);
				data.setParticulars("Cost of the Apartment");
				data.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
				data.setGstPercentage(schemeGSTPercentage.toString());
				data.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(gstAmount).setScale(roundingModeSize, roundingMode).toString()));
				data.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatCostInfo.getBasicFlatCost()+gstAmount).setScale(roundingModeSize, roundingMode).toString()));

				agreementDraftCalculations.add(data);
				
				sumOfBasicCost += flatCostInfo.getBasicFlatCost();
				sumOfAllComponentGstAmount += gstAmount;
				serialNo++;
			}

			if (aminitiesInfraCostInfoList != null) {
				for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfoList) {
					if (aminitiesInfraCostInfo.getTotalCost() == null || aminitiesInfraCostInfo.getTotalCost() == 0) {
						continue;
					}
					System.out.println(aminitiesInfraCostInfo.getTotalCost()+" "+aminitiesInfraCostInfo.getAminititesInfraName()+" "+aminitiesInfraCostInfo.getAminititesInfraCost()+" "+aminitiesInfraCostInfo.getFlatCostId());
					//double amenitiesGstAmount = ((schemeGSTPercentage/(schemeGSTPercentage+100))*(aminitiesInfraCostInfo.getTotalCost()));
					double amenitiesGstAmount = (aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100;
					System.out.println((aminitiesInfraCostInfo.getTotalCost()*schemeGSTPercentage)/100);
					//double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost()-amenitiesGstAmount;
					double amenitiesBasicAmount = aminitiesInfraCostInfo.getTotalCost();
					aminitiesInfraCostInfo.setGstAmount(amenitiesGstAmount);
			
					AgreementDraftCalculations data = new AgreementDraftCalculations();
					data.setSerialNo(serialNo);
					data.setParticulars(aminitiesInfraCostInfo.getAminititesInfraName());
					data.setBasicCost(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount).setScale(roundingModeSize, roundingMode).toString())));
					data.setGstPercentage(schemeGSTPercentage.toString());
					data.setGstAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
					data.setTotalAmount(String.valueOf(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(amenitiesBasicAmount+amenitiesGstAmount).setScale(roundingModeSize, roundingMode).toString())));
					
					sumOfBasicCost += amenitiesBasicAmount;
					sumOfAllComponentGstAmount += amenitiesGstAmount;

					agreementDraftCalculations.add(data);
					serialNo++;
				}
			}
			double tenPercentAmountOnFlat = (totalAgreementCost*10)/100;
			double sumOfCustomerPaidAmount = 0.0d;
			boolean breakTheLoop = false;
			if(transactionEntryDetailsInfos!=null) {
				for (FinTransactionEntryDetailsInfo entryDetailsInfo : transactionEntryDetailsInfos) {
					if(Util.isEmptyObject(entryDetailsInfo.getBankName())) {
						entryDetailsInfo.setBankName("-");
					}
					double transactionAmount = entryDetailsInfo.getTransactionAmount();
					sumOfCustomerPaidAmount += transactionAmount;
					if(sumOfCustomerPaidAmount>tenPercentAmountOnFlat) {
						breakTheLoop = true;//need to show only 10% amount data
						transactionAmount = (sumOfCustomerPaidAmount-transactionAmount)-tenPercentAmountOnFlat;
					}
					//schemeGSTPercentage
					double transactionAmountExcludeGST = (transactionAmount*100/schemeGSTPercentage);//transactionAmount*(100/schemeGSTPercentage);
					double transactionAmountGST = (transactionAmount*5/105);
					entryDetailsInfo.setStrTransactionAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmount).setScale(roundingModeSize, roundingMode).toString()));
					entryDetailsInfo.setTransactionAmountInWords(getTheAmountInWords(transactionAmount));
					
					entryDetailsInfo.setStrTransactionAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
					entryDetailsInfo.setTransactionAmountExcludeGSTInWords(getTheAmountInWords(transactionAmountExcludeGST));
					
					entryDetailsInfo.setStrTransactionAmountGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(transactionAmountGST).setScale(roundingModeSize, roundingMode).toString()));
					entryDetailsInfo.setTransactionAmountGSTInWords(getTheAmountInWords(transactionAmountGST));
					if(breakTheLoop) {break;}
				}
			}
			//*************************************
			double msPercentage = 0.0;
			double msAmount =  0.0;
			double dueAmountExcludeGST = 0.0;
			double milestoneGstAmount = 0.0;
			String strMSAmt = "";
			String strMSPercentage = "";

			long milestoneNo = 0;
			if(mileStonesDetailsList!=null) {
				for (MileStoneInfo ms : mileStonesDetailsList) {
					if(ms.getTotalDueAmount() == null) {
						continue;
					}
					Map<String, Object> mileStone = new HashMap<>();
					msPercentage = Double.valueOf(ms.getDue());
					msAmount = Double.valueOf(ms.getTotalDueAmount());
					dueAmountExcludeGST = Double.valueOf(ms.getDueAmountExcludeGST());
					milestoneGstAmount = Double.valueOf(ms.getGstAmount());
					 
				    strMSAmt = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(msAmount).setScale(roundingModeSize, roundingMode).toString());
					strMSPercentage = BigDecimal.valueOf(msPercentage).setScale(0, roundingMode).toString();
					mileStone.put("milestoneName", ms.getMilestoneName());
					mileStone.put("percentage",strMSPercentage);
					mileStone.put("totalDueAmount", strMSAmt);
					mileStone.put("milStoneNo", ms.getMilStoneNo());
					ms.setTotalDueAmount(strMSAmt);
					ms.setDue(strMSPercentage+"%");
					ms.setDueAmountExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(dueAmountExcludeGST).setScale(roundingModeSize, roundingMode).toString()));
					ms.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(milestoneGstAmount).setScale(roundingModeSize, roundingMode).toString()));
					sumofMilestonePercentgae += msPercentage;
					sumofMilestoneAmount += msAmount;
					milestoneNo = ms.getMilStoneNo();
					mileStonesList.add(mileStone);
				}
			}
			
			/*Map<String,Object> mileStoneTotal = new HashMap<>();
			mileStoneTotal.put("milestoneName", "Total Amount");
			mileStoneTotal.put("percentage",BigDecimal.valueOf(sumofMilestonePercentgae).setScale(0, roundingMode).toString());
			mileStoneTotal.put("totalDueAmount", currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
			mileStoneTotal.put("milStoneNo", "");
			mileStonesList.add(mileStoneTotal);*/
			
			double flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * legalCostPercentage) / 100;
			double maintenanceGstAmount = (maintenanceAmount * maintenancePercentage) / 100;
			double maintenanceAmtWithGstAmount =  maintenanceAmount+maintenanceGstAmount;
			if(!customerPropertyDetailsInfo.getSiteId().equals(134l)) {
				//this object for flat basic cost and amenities amount sum
				AgreementDraftCalculations MaintenanceData = new AgreementDraftCalculations();
				MaintenanceData.setSerialNo(serialNo);
				MaintenanceData.setParticulars("Maintenance for 12 Months");
				MaintenanceData.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmount).setScale(roundingModeSize, roundingMode).toString()));
				MaintenanceData.setGstPercentage(String.valueOf(maintenancePercentage));
				MaintenanceData.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				MaintenanceData.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				agreementDraftCalculations.add(MaintenanceData);
			} else {
				maintenanceGstAmount = 0.0;
				maintenanceAmtWithGstAmount = 0.0;
			}
			sumOfBasicCost += maintenanceAmount;
			sumOfAllComponentGstAmount += maintenanceGstAmount;
			

			//totalAmtData object for flat basic cost and amenities amount sum
			AgreementDraftCalculations totalAmtDataObject = new AgreementDraftCalculations();
			totalAmtDataObject.setSerialNo(serialNo);
			totalAmtDataObject.setParticulars("Total");
			totalAmtDataObject.setBasicCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
			totalAmtDataObject.setGstPercentage("");
			totalAmtDataObject.setGstAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			totalAmtDataObject.setTotalAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost+sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			
			agreementDraftCalculations.add(totalAmtDataObject);
			/* Malladi Changes */
			if(customerPropertyDetailsInfo.getSiteId().equals(124l)) {
			//if(!customerPropertyDetailsInfo.getSiteId().equals(134l) && !customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				//Maintenance Charges object for milestone list
				MileStoneInfo maintenanceDetailsForMS = new MileStoneInfo();
				maintenanceDetailsForMS.setMilStoneNo(++milestoneNo);
				maintenanceDetailsForMS.setMilestoneName("Maintenance Charges payable on final payment request");
				maintenanceDetailsForMS.setDue("-");
				maintenanceDetailsForMS.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(maintenanceAmtWithGstAmount).setScale(roundingModeSize, roundingMode).toString()));
				mileStonesDetailsList.add(maintenanceDetailsForMS);
				sumofMilestoneAmount+= maintenanceAmtWithGstAmount;
			}
			
			//total amount object for milestone list
			/* Malladi Changes */
			if(!customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				MileStoneInfo ms = new MileStoneInfo();
				ms.setMilStoneNo(++milestoneNo);
				ms.setMilestoneName("Total Amount");
				ms.setDue("");
				ms.setTotalDueAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumofMilestoneAmount).setScale(roundingModeSize, roundingMode).toString()));
				mileStonesDetailsList.add(ms);
			}
			
			double totalBalanceAmountOnFlat = 0.0;
			double tenPercentGSTAmount = 0.0;
			if (totalMilestonePaidAmount > tenPercentAmountOnFlat) {
				
			} else {
				tenPercentAmountOnFlat = totalMilestonePaidAmount;
			}
			tenPercentGSTAmount = (tenPercentAmountOnFlat*10)/100;//gst Amount on 10 % paid amount
			
			grandTotalIncludingMaintenance = sumOfBasicCost+sumOfAllComponentGstAmount;				
			totalBalanceAmountOnFlat = grandTotalIncludingMaintenance-tenPercentAmountOnFlat;
			
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfAllComponentGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceExcludeGST(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sumOfBasicCost).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenance(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(grandTotalIncludingMaintenance).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setGrandTotalIncludingMaintenanceInWords(getTheAmountInWords(grandTotalIncludingMaintenance));
			welcomeMailGeneratorInfo.setTotalBasicFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalFlatCostExcludingGST).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setSbuaIntoCarpusFundSum(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(sbuaIntoCarpusFundSum).setScale(roundingModeSize, roundingMode).toString()));
					
			welcomeMailGeneratorInfo.setTotalBalanceAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(totalBalanceAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalBalanceAmountInWords(getTheAmountInWords(totalBalanceAmountOnFlat));
			welcomeMailGeneratorInfo.setTotalPaidAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentAmountOnFlat).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalPaidAmountInWords(getTheAmountInWords(tenPercentAmountOnFlat));
			welcomeMailGeneratorInfo.setTotalPaidGSTAmount(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(tenPercentGSTAmount).setScale(roundingModeSize, roundingMode).toString()));
			welcomeMailGeneratorInfo.setTotalPaidGSTAmountInWords(getTheAmountInWords(tenPercentGSTAmount));
			
			
			customerUnitDetails.put("flat_legal_and_doc_charges_Sum_With_GST",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount).setScale(roundingModeSize, roundingMode).toString()));
			customerUnitDetails.put("flat_legal_cost",currencyUtil.convertUstoInFormat(BigDecimal.valueOf(flatLegalAndDocumentationChargesAmt).setScale(roundingModeSize, roundingMode).toString()));
			
			
			//dataForPdf1.put("officeDetailsPojoList", officeDetailsPojoList);
			dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
			dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));
			//dataForPdf1.put("mileStones",  mileStonesList);
			dataForPdf1.put("termsAndConditions", termsAndConditionsList);
			dataForPdf1.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
			
			email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
			email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
			email.setEmployeeName(customerDetails.get("empName").toString());
			
			email.setDataForPdf(dataForPdf1);
			String leftSidelogoForPdf = "";
			String rightSidelogoForPdf = "";
			String thanksAndRegardsFrom = "";
			String greetingsFrom = "";
			Site site = new Site();
			site.setSiteId(customerPropertyDetailsInfo.getSiteId());
			site.setName(customerPropertyDetailsInfo.getSiteName());

			Map<String,String> logoAndOtherDetails = getTheCommonInformation(site,customerPropertyDetailsInfo);			
			rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
			leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
			thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
			greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

			info.setRightSidelogoForPdf(rightSidelogoForPdf);
			info.setLeftSidelogoForPdf(leftSidelogoForPdf);
			info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
			info.setGreetingsFrom(greetingsFrom);
			info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
			info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
			info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
			info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
			//info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getSiteName()+" -"+customerPropertyDetailsInfo.getFlatNo()+" - Agreement Draft.pdf");
			info.setDemandNotePdfFileName("Agreement Draft - "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo()+".pdf");
			
			info.setFolderFilePath(folderFilePath);
			info.setFolderFileUrl(folderFileUrl);
			welcomeMailGeneratorInfo.setCustomerDetailsList(customerDetailsList);
			welcomeMailGeneratorInfo.setCoApplicantDetailsList(coApplicantDetailsList);
			welcomeMailGeneratorInfo.setFirstApplicantProfessionalDetails(firstApplicantProfessionalDetails);
			welcomeMailGeneratorInfo.setCoApplicantProfessionalDetailsList(coApplicantProfessionalDetails);
			
			welcomeMailGeneratorInfo.setOfficeDetailsPojoList(officeDetailsPojoList);
			welcomeMailGeneratorInfo.setMileStones(mileStonesDetailsList);
			welcomeMailGeneratorInfo.setSiteOtherChargesDetails(siteOtherChargesDetails);
			welcomeMailGeneratorInfo.setTermsAndConditions(termsAndConditions);
			welcomeMailGeneratorInfo.setAgreementDraftCalculations(agreementDraftCalculations);
			welcomeMailGeneratorInfo.setTransactionEntryDetailsInfos(transactionEntryDetailsInfos);
			
			//welcomeMailGeneratorInfo.setDataForPdf(dataForPdf1);
			
			setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
			
			email.setWelcomeMailGeneratorInfo(welcomeMailGeneratorInfo);
			email.setDemandNoteGeneratorInfo(info);
			//template name to construct pdf
			//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
			/* Malladi Changes */
			String flatSaleOwner = "";
			if(Util.isNotEmptyObject(customerPropertyDetailsInfo)) {
				flatSaleOwner = customerPropertyDetailsInfo.getFlatSaleOwner();
				if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
					flatSaleOwner = customerPropertyDetailsInfo.getSiteId()+"_"+flatSaleOwner;	
				}
			}
			
			/*if(customerPropertyDetailsInfo.getSiteId().equals(131l) && flatSaleOwner.equalsIgnoreCase("131_Landlord")) {
				email.setTemplateName("/vmtemplates/Agreement_131_Landlord.vm");
			}else if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				email.setTemplateName("/vmtemplates/Agreement_131_Builder.vm");
			}else {
				email.setTemplateName("/vmtemplates/AgreementDraft.vm");
			}*/
			
			
			pdfHelper.XMLWorkerHelperForAgreementDraftLetterSST2(email, fileInfo);
			fileInfoList.add(fileInfo);
			return fileInfoList;
		}
		
		
		
		
		public List<FileInfo> generateLoanNOCLetterHelper(Map<String, Object> dataForGenerateAllotmentLetterHelper,
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		Email email = new Email();
		FileInfo fileInfo = new FileInfo();
		List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo info = new DemandNoteGeneratorInfo();
		String bookingDateSlashFormat = "";
		String thanksAndRegardsFrom = "";
		String greetingsFrom = "";
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		Site site = new Site();

		Map<String, List<Map<String, Object>>> dataForPdf1 = new HashMap<>();

		String folderFilePath = dataForGenerateAllotmentLetterHelper.get("folderFilePath").toString();
		String folderFileUrl = dataForGenerateAllotmentLetterHelper.get("folderFileUrl").toString();

		info.setFolderFilePath(folderFilePath);
		info.setFolderFileUrl(folderFileUrl);

		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
//
//		@SuppressWarnings("unchecked")
//		List<FlatBookingInfo> flatBookingInfos = (List<FlatBookingInfo>) dataForGenerateAllotmentLetterHelper.get("flatBookingInfoList");
//			
//			@SuppressWarnings("unchecked")
//			List<FlatBookingPojo> flatBookPojo =  (List<FlatBookingPojo>) dataForGenerateAllotmentLetterHelper.get("flatBookPojo");
			
		Long siteId = (Long) customerDetails.get("siteId");
		String siteName = (String) customerDetails.get("siteName");
		String totalSaleConsideration = (String) customerDetails.get("totalAgreementCost");
		customerDetails.put("totalSaleConsideration", totalSaleConsideration.replaceAll("\u00A0", ""));
		String bookingDate = (String) customerDetails.get("bookingDate");
		String soldBasePrice = (String) customerUnitDetails.get("Sold Base Price");

		
		if (Util.isNotEmptyObject(soldBasePrice) && !"-".equals(soldBasePrice)) {
			soldBasePrice = "Rs." + soldBasePrice + "/-";
		} else {
			soldBasePrice = "";
		}

		if (Util.isNotEmptyObject(bookingDate) && !"N/A".equalsIgnoreCase(bookingDate)
				&& !"-".equalsIgnoreCase(bookingDate)) {
			bookingDateSlashFormat = bookingDate.replace("-", "/");
		} else {
			bookingDateSlashFormat = bookingDate;
		}
		
		customerDetails.put("siteName", siteName);
		customerDetails.put("bookingDateSlashFormat", bookingDateSlashFormat);
		customerUnitDetails.replace("Sold Base Price", soldBasePrice);
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());

		Map<String, String> logoAndOtherDetails = getTheCommonInformation(site, customerPropertyDetailsInfo);
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		dataForPdf1.put("customerDetails", Arrays.asList(customerDetails));
		dataForPdf1.put("customerUnitDetails", Arrays.asList(customerUnitDetails));

		email.setPortNumber(Long.valueOf(customerDetails.get("portNumber").toString()));
		email.setEmployeeId(Long.valueOf(customerDetails.get("empId").toString()));
		email.setEmployeeName(customerDetails.get("empName").toString());
		email.setDataForPdf(dataForPdf1);

		info.setRightSidelogoForPdf(rightSidelogoForPdf);
		info.setLeftSidelogoForPdf(leftSidelogoForPdf);

		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
		info.setSiteName(customerPropertyDetailsInfo.getSiteName().toString());
		info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		info.setInvoiceDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		info.setDemandNotePdfFileName("NOC_Banker_" + customerPropertyDetailsInfo.getFlatNo());

		setOfficeDetailsPojo(officeDetailsPojoList, customerPropertyDetailsInfo, info);

		email.setDemandNoteGeneratorInfo(info);
		info.setCompanyName(info.getCompanyName().toUpperCase());

		if ("126".equals(siteId.toString())) {
			email.setTemplateName("/vmtemplates/LOAN_NOC_LETTER_Folium.vm");
		} else if ("130".equals(siteId.toString())) {
			email.setTemplateName("/vmtemplates/LOAN_NOC_LETTER_sush_phase2.vm");
		} else if ("124".equals(siteId.toString())) {
			email.setTemplateName("/vmtemplates/LOAN_NOC_LETTER_sush_phase1.vm");
		} else if ("111".equals(siteId.toString())) {
			email.setTemplateName("/vmtemplates/LOAN_NOC_LETTER_nan.vm");
		}

		pdfHelper.XMLWorkerHelperForLoanNocLetter(email, fileInfo);
		fileInfoList.add(fileInfo);
		return fileInfoList;
		}
}
