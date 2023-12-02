package com.sumadhura.employeeservice.schedulers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryResponse;
import com.sumadhura.employeeservice.enums.FinTransactionType;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.service.EmployeeFinancialService;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.ExcelHelperInfo;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialHelper;
import com.sumadhura.employeeservice.util.ExcelHelper;
import com.sumadhura.employeeservice.util.PdfHelper;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.Getter;
import lombok.Setter;

@EnableAsync
@Setter
@Getter
@Component("generateTrnExcel")
public class GenerateExcelTransactionScheduler {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;

	@Autowired
	private ResponceCodesUtil responceCodesUtil;

	@Autowired
	@Qualifier("EmployeeFinancialService")
	private EmployeeFinancialService employeeFinancialService;
	
	@Autowired
	@Qualifier("EmployeeFinancialServiceDao")
	private EmployeeFinancialServiceDao employeeFinancialServiceDao;
	
	/*@Autowired(required = true)
	@Qualifier("PdfHelper")
	private PdfHelper pdfHelper;*/
	
	@Autowired(required = true)
	@Qualifier("excelHelper")
	private ExcelHelper excelHelper;
	
	@Scheduled(cron = "${cron.expressionExcelTrn}")
	public void generateTransactionExcelSendEmail() throws Exception {
		try {
			
		
		ExcelHelperInfo excelHelperInfo = new ExcelHelperInfo(); 
		
		List<String> headerNames = new ArrayList<>();
		List<List<Map<String,Object>>> listOfRowData = new ArrayList<>();
		//Headers names should be in order
		headerNames.add("S.No");
		headerNames.add("SalesForce Booking ID");
		headerNames.add("SalesForce TRN ID");
		headerNames.add("Project Name");
		headerNames.add("Project Account Number");
		headerNames.add("Customer Name");
		headerNames.add("Flat No");
		headerNames.add("Transaction Type");
		headerNames.add("Receipt Type");
		headerNames.add("Transaction Mode");
		headerNames.add("Cheque OR Reference No");
		headerNames.add("Received Date");
		headerNames.add("Online / Cheque Date");
		headerNames.add("Deposite Date");
		headerNames.add("Clearance Date");
		headerNames.add("Transaction Amount");
		headerNames.add("Bank Name");
		headerNames.add("Receipt Stage");
		headerNames.add("Source of Funds");
		headerNames.add("Last approved date");
		headerNames.add("Bounce Date");
		headerNames.add("Bounce Reason");
		headerNames.add("Transaction Id");

		excelHelperInfo.setHeaderNames(headerNames);
		
		//mention column types mandatory, columnType Map keys and headerNames Map keys should be match
		Map<String,String> columnType = new HashMap<>();
		columnType.put("S.No","Number");
		columnType.put("SalesForce Booking ID","String");
		columnType.put("SalesForce TRN ID","String");
		columnType.put("Project Name","String");
		columnType.put("Project Account Number","String");
		columnType.put("Customer Name","String");
		columnType.put("Flat No","String");
		columnType.put("Transaction Type","String");
		columnType.put("Receipt Type","String");
		columnType.put("Transaction Mode","String");
		columnType.put("Cheque OR Reference No","String");
		columnType.put("Received Date","Date");
		columnType.put("Online / Cheque Date","Date");
		columnType.put("Deposite Date","Date");
		columnType.put("Clearance Date","Date");
		columnType.put("Transaction Amount","String");
		columnType.put("Bank Name","String");
		columnType.put("Receipt Stage","String");
		columnType.put("Source of Funds","String");
		columnType.put("Last approved date","Date");
		columnType.put("Bounce Date","Date");
		columnType.put("Bounce Reason","String");
		columnType.put("Transaction Id","Number");
		
		excelHelperInfo.setColumnType(columnType);
		//String s = null; s.trim();
		log.info("GenerateExcelTransactionScheduler.generateTransactionExcelSendEmail()");
		
		Timestamp yesterDay = new Timestamp(new Date().getTime());
		yesterDay = TimeUtil.removeOneDay(yesterDay);
		yesterDay = TimeUtil.removeTimePartFromTimeStamp1(yesterDay);
		
		EmployeeFinancialTransactionServiceInfo tansactionServiceInfo = new EmployeeFinancialTransactionServiceInfo(); 
		tansactionServiceInfo.setCondition("loadCompletedTrnActiveBookingData");
		/*tansactionServiceInfo.setFromDate(yesterDay);
		tansactionServiceInfo.setToDate(yesterDay);*/
		
		//tansactionServiceInfo.setSiteId(114l);
		//tansactionServiceInfo.setSiteIds(Arrays.asList(114l));
		//tansactionServiceInfo.setBookingFormIds(Arrays.asList(22l));
		EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getMisPendingTransactions(tansactionServiceInfo);
		int serialNumber = 1;
		if(Util.isNotEmptyObject(employeeFinancialTransactionResponse)) {
			List<FinTransactionEntryResponse> list = employeeFinancialTransactionResponse.getFinTransactionEntryResponseList();
			
			List<Map<String,Object>> trnDataList = null;
			Map<String,Object> trnMapList = null;
			
			for (FinTransactionEntryResponse trnEntry : list) {
				if(trnEntry.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
					continue;
				}
				/*if(trnEntry.getTransactionModeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
					continue;
				}*/
				trnDataList = new ArrayList<>();
				trnMapList = new HashMap<>();
				//add data in List in Map object, this key should match with headerNames MAP object
				trnMapList.put("S.No", serialNumber++);
				trnMapList.put("SalesForce Booking ID",Util.isEmptyObject(trnEntry.getSalesforceBookingId())?null:trnEntry.getSalesforceBookingId());
				trnMapList.put("SalesForce TRN ID",Util.isEmptyObject(trnEntry.getSalesforceTransactionId())?null:trnEntry.getSalesforceTransactionId());
				trnMapList.put("Project Name",trnEntry.getSiteName());
				trnMapList.put("Transaction Id",trnEntry.getTransactionEntryId());
				trnMapList.put("Project Account Number",trnEntry.getSiteBankAccountNumber());
				trnMapList.put("Customer Name",trnEntry.getCustomerName());
				trnMapList.put("Flat No",trnEntry.getFlatNo());
				trnMapList.put("Transaction Type",trnEntry.getTransactionTypeName());
				trnMapList.put("Receipt Type",trnEntry.getTransactionPaymentSetOff());
				trnMapList.put("Transaction Mode",trnEntry.getTransactionModeName());
				trnMapList.put("Cheque OR Reference No",trnEntry.getChequeOrReferenceNo());
				trnMapList.put("Received Date",Util.isEmptyObject(trnEntry.getTransactionReceiveDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getTransactionReceiveDate()));
				trnMapList.put("Online / Cheque Date",Util.isEmptyObject(trnEntry.getTransactionDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getTransactionDate()));
				
				if(trnEntry.getTransactionTypeName().equalsIgnoreCase("Receipt") && trnEntry.getTransactionModeName().equalsIgnoreCase("Online")) {
					trnEntry.setChequeClearanceDate(trnEntry.getTransactionReceiveDate());
					trnEntry.setChequeDepositedDate(trnEntry.getTransactionReceiveDate());
				}
				
				trnMapList.put("Deposite Date",Util.isEmptyObject(trnEntry.getChequeDepositedDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getChequeDepositedDate()));
				trnMapList.put("Clearance Date",Util.isEmptyObject(trnEntry.getChequeClearanceDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getChequeClearanceDate()));
				trnMapList.put("Transaction Amount",trnEntry.getTransactionAmount());
				trnMapList.put("Bank Name",trnEntry.getBankName());
				if("Cheque Bounced".equalsIgnoreCase(trnEntry.getReceiptStage())) {
					trnEntry.setReceiptStage("Bounce");
				}
				trnMapList.put("Receipt Stage",trnEntry.getReceiptStage());
				trnMapList.put("Source of Funds",trnEntry.getSourceOfFunds());
				trnMapList.put("Last approved date",Util.isEmptyObject(trnEntry.getTransactionClosedDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getTransactionClosedDate()));
				//trnMapList.put("Bounce Date", trnEntry.getChequeBounceDate());
				trnMapList.put("Bounce Date",Util.isEmptyObject(trnEntry.getChequeBounceDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getChequeBounceDate()));
				
				trnMapList.put("Bounce Reason",trnEntry.getChequeBounceComment());				
				
				trnDataList.add(trnMapList);
				listOfRowData.add(trnDataList);
			}
			
			excelHelperInfo.setListOfRowData(listOfRowData);
		}
		
		String folderFilePath = "";
		String folderFileUrl = "";

		Properties prop = responceCodesUtil.getApplicationProperties();
		
		folderFilePath = prop.getProperty("TRN_EXCEL_FILE_DIRECTORY_PATH");
		folderFileUrl = prop.getProperty("TRN_EXCEL_FILE_URL_PATH");
		
		if (folderFilePath == null || folderFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}
		
		String fileName = "Transactions_"+TimeUtil.getTimeInSpecificFormat(TimeUtil.removeOneDay(new Timestamp(new Date().getTime())), "dd-MM-yyyy");//"dd-MM-yyyy hh-mm"
		
		StringBuilder filePath = new StringBuilder(folderFilePath);
				//.append("/")//.append(101).append("/")
				//.append(fileName).append(".xls");

		StringBuilder urlPath = new StringBuilder(folderFileUrl);
				//.append("/")//.append(101).append("/")
				//.append(fileName).append(".xls");
		
		fileName = new PdfHelper().getFileName(filePath.toString(), fileName+".xls");
		
		filePath.append(fileName);
		urlPath.append(fileName);
		
		excelHelperInfo.setExcelFilePath(filePath.toString());
		excelHelperInfo.setExcelFileUrl(urlPath.toString());
		excelHelperInfo.setExcelFileName(fileName);

		excelHelper.ExcelWorkerHelper(excelHelperInfo);
		List<String> emails = Arrays.asList("aniketchavan75077@gmail.com","venkateshwar444@gmail.com");

		String emailsList = responceCodesUtil.getApplicationProperties().getProperty("DEFAULT_TRANSACTION_SEND_EXCEL_EMAIL");
		emails = Arrays.asList(emailsList.split(","));
		
		emails = Arrays.asList("aniketchavan75077@gmail.com","venkateshwar444@gmail.com");//only FOR CUG
		List<String> ccEmails = Arrays.asList("venkateshwar444@gmail.com");//only FOR CUG
		/*emails = Arrays.asList("prithvi@sumadhuragroup.com","purnima@sumadhuragroup.com","sridhar@sumadhuragroup.com","vinod@sumadhuragroup.com");//only FOR LIVE
		List<String> ccEmails = Arrays.asList("mahalakshmi@sumadhuragroup.com","vamshik@sumadhuragroup.com");//Only for LIVE
*/
		log.info("GenerateExcelTransactionScheduler.generateTransactionExcelSendEmail() \n emails : "+emails+" \n ccEmails : "+ccEmails);	
		//if (serialNumber != 1) {
			sendUpdateRateOfInterestEmail(excelHelperInfo, emails,ccEmails);
		//}
		} catch(Exception exx) {
			exx.printStackTrace();
			try { 
				employeeFinancialService.sendBookingErrorMail(exx,null,"TransactionScheduler");
			} catch(Exception exx1) {
				
			}
		}
	}
	
	
	//second schedular for excuting schedular 2 different times and to send different emails
	@Scheduled(cron = "${cron.expressionExcelTrnTwo}")
	public void generateTransactionExcelSendEmailTwo() throws Exception {
		try {
			
		
		ExcelHelperInfo excelHelperInfo = new ExcelHelperInfo(); 
		
		List<String> headerNames = new ArrayList<>();
		List<List<Map<String,Object>>> listOfRowData = new ArrayList<>();
		//Headers names should be in order
		headerNames.add("S.No");
		headerNames.add("SalesForce Booking ID");
		headerNames.add("SalesForce TRN ID");
		headerNames.add("Project Name");
		headerNames.add("Project Account Number");
		headerNames.add("Customer Name");
		headerNames.add("Flat No");
		headerNames.add("Transaction Type");
		headerNames.add("Receipt Type");
		headerNames.add("Transaction Mode");
		headerNames.add("Cheque OR Reference No");
		headerNames.add("Received Date");
		headerNames.add("Online / Cheque Date");
		headerNames.add("Deposite Date");
		headerNames.add("Clearance Date");
		headerNames.add("Transaction Amount");
		headerNames.add("Bank Name");
		headerNames.add("Receipt Stage");
		headerNames.add("Source of Funds");
		headerNames.add("Last approved date");
		headerNames.add("Bounce Date");
		headerNames.add("Bounce Reason");
		headerNames.add("Transaction Id");

		excelHelperInfo.setHeaderNames(headerNames);
		
		//mention column types mandatory, columnType Map keys and headerNames Map keys should be match
		Map<String,String> columnType = new HashMap<>();
		columnType.put("S.No","Number");
		columnType.put("SalesForce Booking ID","String");
		columnType.put("SalesForce TRN ID","String");
		columnType.put("Project Name","String");
		columnType.put("Project Account Number","String");
		columnType.put("Customer Name","String");
		columnType.put("Flat No","String");
		columnType.put("Transaction Type","String");
		columnType.put("Receipt Type","String");
		columnType.put("Transaction Mode","String");
		columnType.put("Cheque OR Reference No","String");
		columnType.put("Received Date","Date");
		columnType.put("Online / Cheque Date","Date");
		columnType.put("Deposite Date","Date");
		columnType.put("Clearance Date","Date");
		columnType.put("Transaction Amount","String");
		columnType.put("Bank Name","String");
		columnType.put("Receipt Stage","String");
		columnType.put("Source of Funds","String");
		columnType.put("Last approved date","Date");
		columnType.put("Bounce Date","Date");
		columnType.put("Bounce Reason","String");
		columnType.put("Transaction Id","Number");
		
		excelHelperInfo.setColumnType(columnType);
		//String s = null; s.trim();
		log.info("GenerateExcelTransactionScheduler.generateTransactionExcelSendEmail()");
		
		Timestamp yesterDay = new Timestamp(new Date().getTime());
		yesterDay = TimeUtil.removeOneDay(yesterDay);
		yesterDay = TimeUtil.removeTimePartFromTimeStamp1(yesterDay);
		
		EmployeeFinancialTransactionServiceInfo tansactionServiceInfo = new EmployeeFinancialTransactionServiceInfo(); 
		tansactionServiceInfo.setCondition("loadCompletedTrnActiveBookingData");
		/*tansactionServiceInfo.setFromDate(yesterDay);
		tansactionServiceInfo.setToDate(yesterDay);*/
		
		//tansactionServiceInfo.setSiteId(114l);
		//tansactionServiceInfo.setSiteIds(Arrays.asList(114l));
		//tansactionServiceInfo.setBookingFormIds(Arrays.asList(22l));
		EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getMisPendingTransactions(tansactionServiceInfo);
		int serialNumber = 1;
		if(Util.isNotEmptyObject(employeeFinancialTransactionResponse)) {
			List<FinTransactionEntryResponse> list = employeeFinancialTransactionResponse.getFinTransactionEntryResponseList();
			
			List<Map<String,Object>> trnDataList = null;
			Map<String,Object> trnMapList = null;
			
			for (FinTransactionEntryResponse trnEntry : list) {
				if(trnEntry.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
					continue;
				}
				/*if(trnEntry.getTransactionModeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
					continue;
				}*/
				trnDataList = new ArrayList<>();
				trnMapList = new HashMap<>();
				//add data in List in Map object, this key should match with headerNames MAP object
				trnMapList.put("S.No", serialNumber++);
				trnMapList.put("SalesForce Booking ID",Util.isEmptyObject(trnEntry.getSalesforceBookingId())?null:trnEntry.getSalesforceBookingId());
				trnMapList.put("SalesForce TRN ID",Util.isEmptyObject(trnEntry.getSalesforceTransactionId())?null:trnEntry.getSalesforceTransactionId());
				trnMapList.put("Project Name",trnEntry.getSiteName());
				trnMapList.put("Transaction Id",trnEntry.getTransactionEntryId());
				trnMapList.put("Project Account Number",trnEntry.getSiteBankAccountNumber());
				trnMapList.put("Customer Name",trnEntry.getCustomerName());
				trnMapList.put("Flat No",trnEntry.getFlatNo());
				trnMapList.put("Transaction Type",trnEntry.getTransactionTypeName());
				trnMapList.put("Receipt Type",trnEntry.getTransactionPaymentSetOff());
				trnMapList.put("Transaction Mode",trnEntry.getTransactionModeName());
				trnMapList.put("Cheque OR Reference No",trnEntry.getChequeOrReferenceNo());
				trnMapList.put("Received Date",Util.isEmptyObject(trnEntry.getTransactionReceiveDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getTransactionReceiveDate()));
				trnMapList.put("Online / Cheque Date",Util.isEmptyObject(trnEntry.getTransactionDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getTransactionDate()));
				
				if(trnEntry.getTransactionTypeName().equalsIgnoreCase("Receipt") && trnEntry.getTransactionModeName().equalsIgnoreCase("Online")) {
					trnEntry.setChequeClearanceDate(trnEntry.getTransactionReceiveDate());
					trnEntry.setChequeDepositedDate(trnEntry.getTransactionReceiveDate());
				}
				
				trnMapList.put("Deposite Date",Util.isEmptyObject(trnEntry.getChequeDepositedDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getChequeDepositedDate()));
				trnMapList.put("Clearance Date",Util.isEmptyObject(trnEntry.getChequeClearanceDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getChequeClearanceDate()));
				trnMapList.put("Transaction Amount",trnEntry.getTransactionAmount());
				trnMapList.put("Bank Name",trnEntry.getBankName());
				if("Cheque Bounced".equalsIgnoreCase(trnEntry.getReceiptStage())) {
					trnEntry.setReceiptStage("Bounce");
				}
				trnMapList.put("Receipt Stage",trnEntry.getReceiptStage());
				trnMapList.put("Source of Funds",trnEntry.getSourceOfFunds());
				trnMapList.put("Last approved date",Util.isEmptyObject(trnEntry.getTransactionClosedDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getTransactionClosedDate()));
				//trnMapList.put("Bounce Date", trnEntry.getChequeBounceDate());
				trnMapList.put("Bounce Date",Util.isEmptyObject(trnEntry.getChequeBounceDate())?null:TimeUtil.timestampToDD_MM_YYYY(trnEntry.getChequeBounceDate()));
				
				trnMapList.put("Bounce Reason",trnEntry.getChequeBounceComment());				
				
				trnDataList.add(trnMapList);
				listOfRowData.add(trnDataList);
			}
			
			excelHelperInfo.setListOfRowData(listOfRowData);
		}
		
		String folderFilePath = "";
		String folderFileUrl = "";

		Properties prop = responceCodesUtil.getApplicationProperties();
		
		folderFilePath = prop.getProperty("TRN_EXCEL_FILE_DIRECTORY_PATH");
		folderFileUrl = prop.getProperty("TRN_EXCEL_FILE_URL_PATH");
		
		if (folderFilePath == null || folderFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}
		
		String fileName = "Transactions_"+TimeUtil.getTimeInSpecificFormat(TimeUtil.removeOneDay(new Timestamp(new Date().getTime())), "dd-MM-yyyy");//"dd-MM-yyyy hh-mm"
		
		StringBuilder filePath = new StringBuilder(folderFilePath);
				//.append("/")//.append(101).append("/")
				//.append(fileName).append(".xls");

		StringBuilder urlPath = new StringBuilder(folderFileUrl);
				//.append("/")//.append(101).append("/")
				//.append(fileName).append(".xls");
		
		fileName = new PdfHelper().getFileName(filePath.toString(), fileName+".xls");
		
		filePath.append(fileName);
		urlPath.append(fileName);
		
		excelHelperInfo.setExcelFilePath(filePath.toString());
		excelHelperInfo.setExcelFileUrl(urlPath.toString());
		excelHelperInfo.setExcelFileName(fileName);

		excelHelper.ExcelWorkerHelper(excelHelperInfo);
		List<String> emails = Arrays.asList("aniketchavan75077@gmail.com","venkateshwar442@gmail.com");

		String emailsList = responceCodesUtil.getApplicationProperties().getProperty("DEFAULT_TRANSACTION_SEND_EXCEL_EMAIL");
		emails = Arrays.asList(emailsList.split(","));
		
		emails = Arrays.asList("aniketchavan75077@gmail.com","venkateshwar442@gmail.com");//only FOR CUG
		List<String> ccEmails = Arrays.asList("venkateshwar442@gmail.com");//only FOR CUG
		/*emails = Arrays.asList("prithvi@sumadhuragroup.com","purnima@sumadhuragroup.com","sridhar@sumadhuragroup.com","vinod@sumadhuragroup.com");//only FOR LIVE
		List<String> ccEmails = Arrays.asList("mahalakshmi@sumadhuragroup.com","vamshik@sumadhuragroup.com");//Only for LIVE
*/
		log.info("GenerateExcelTransactionScheduler.generateTransactionExcelSendEmail() \n emails : "+emails+" \n ccEmails : "+ccEmails);	
		//if (serialNumber != 1) {
			sendUpdateRateOfInterestEmail(excelHelperInfo, emails,ccEmails);
		//}
		} catch(Exception exx) {
			exx.printStackTrace();
			try { 
				employeeFinancialService.sendBookingErrorMail(exx,null,"TransactionScheduler");
			} catch(Exception exx1) {
				
			}
		}
	}
	
	private void sendUpdateRateOfInterestEmail(ExcelHelperInfo excelHelperInfo, List<String> emails, List<String> ccEmails) {
		String logoForPdf = "";
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		DemandNoteGeneratorInfo info = new DemandNoteGeneratorInfo();
		Email email = new Email();
		EmployeeFinancialServiceInfo serviceInfo=new EmployeeFinancialServiceInfo();
		serviceInfo.setSiteId(111l);
		Properties prop= responceCodesUtil.getApplicationProperties();

		logoForPdf = prop.getProperty("SUMADHURA_LOGO1");
		thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
		greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");

		List<OfficeDtlsPojo> officeDetailsPojoList = employeeFinancialServiceDao.getOfficeDetailsBySite(serviceInfo);

		//toMails.add("aniketchavan75077@gmail.com");
		new EmployeeFinancialHelper().setOfficeDetailsPojo(officeDetailsPojoList,null,info);
		info.setSiteName("");
		//setting rate of interest last date//used existing field
		info.setCondition("Excel");
		info.setRightSidelogoForPdf(logoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		
		List<String> toMails = emails;
		email.setToMails(toMails.toArray(new String[] {}));
		if(Util.isNotEmptyObject(ccEmails)) {
			email.setCcs(ccEmails.toArray(new String[] {}));
		}
		email.setSubject(TimeUtil.getTimeInSpecificFormat(TimeUtil.removeOneDay(new Timestamp(new Date().getTime())), "dd-MM-yyyy")+" Financial Transaction excel!");
		//email.setMessage(emailMsg.toString());
		email.setTemplateName("/demandnotes/financialTransactionExcel.vm");
		email.setDemandNoteGeneratorInfo(info);
		email.setFilePath(excelHelperInfo.getExcelFilePath());
		email.setFileName(excelHelperInfo.getExcelFileName());
		mailServiceImpl.sendFinancialTransactionExcelToEmployee(email);
	}

}
