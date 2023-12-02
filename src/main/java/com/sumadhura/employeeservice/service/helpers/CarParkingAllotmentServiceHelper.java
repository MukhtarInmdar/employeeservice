package com.sumadhura.employeeservice.service.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.enums.CarParkingAllotmentEnum;
import com.sumadhura.employeeservice.enums.Site;
import com.sumadhura.employeeservice.enums.Status;

import com.sumadhura.employeeservice.persistence.dto.CarParkingAllotmentPdfPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingApprovalLevelsPojo;
import com.sumadhura.employeeservice.persistence.dto.DevicePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentDetailInfo;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentInfo;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentPdfDetailInfo;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentPdfInfo;
import com.sumadhura.employeeservice.util.IOSPushNotificationUtil;
import com.sumadhura.employeeservice.util.PdfHelper;
import com.sumadhura.employeeservice.util.PushNotificationUtil;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

/**
 * CarParkingAllotmentServiceHelper is responsible for providing customer Car Parking Allotment related services helper
 * @author Malladi Venkatesh
 * @since 2021-04-14
 * @time 16:35
 *
 */

@Component("CarParkingAllotmentServiceHelper")
public class CarParkingAllotmentServiceHelper {
	
	@Autowired(required = true)
	@Qualifier("PdfHelper")
	private PdfHelper pdfHelper;
	
	@Autowired
	private ResponceCodesUtil responceCodesUtil;
	
	
	
	@Autowired
	private MailService mailServiceImpl;
	
	private final Logger LOGGER = Logger.getLogger(this.getClass());

	public String generatecarParkingAllotmentPdfFile(CarParkingAllotmentDetailInfo carParkingAllotmentDetailInfo, CarParkingAllotmentEnum cpAllotmentEnum) throws FileNotFoundException, IOException, DocumentException {
		LOGGER.info("*** The control is inside of the generatecarParkingAllotmentPdfFile in CarParkingAllotmentServiceHelper ***");
		CarParkingAllotmentPdfInfo carParkingAllotmentPdfInfo = carParkingAllotmentDetailInfo.getCarParkingAllotmentPdfInfo();
		Properties properties = responceCodesUtil.getApplicationProperties();
		CarParkingAllotmentPdfDetailInfo carParkingAllotmentPdfDetailInfo = new CarParkingAllotmentPdfDetailInfo();
		Email email = new Email();
		
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		String signatureEmp = null;
		
		/* For Aspire Aurum Flats */
		if(carParkingAllotmentPdfInfo.getSiteId()!=null && carParkingAllotmentPdfInfo.getSiteId().equals(Site.ASPIRE_AURUM.getId())) {
			rightSidelogoForPdf = properties.getProperty("ASPIRE_LOGO1");
			thanksAndRegardsFrom = properties.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = properties.getProperty("ASPIRE_GREETING_MSG");
	    /* For Sumadhura Infracon Flats */
		}else {
			rightSidelogoForPdf = properties.getProperty("SUMADHURA_LOGO1");
			thanksAndRegardsFrom = properties.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = properties.getProperty("SUMADHURA_GREETING_MSG");
		}
		
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getFlatSaleOwner())) {
			String flatSaleOwner = carParkingAllotmentPdfInfo.getFlatSaleOwner().trim().replaceAll(" ", "");
			/* For Indimax Flats */
			if(("Indimax").equalsIgnoreCase(carParkingAllotmentPdfInfo.getFlatSaleOwner())) {
				rightSidelogoForPdf = properties.getProperty(flatSaleOwner+"_LOGO1");
				thanksAndRegardsFrom = properties.getProperty(flatSaleOwner+"_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = properties.getProperty(flatSaleOwner+"_GREETING_MSG");
			/* For Sumadhura Vasavi LLP flats */	
			}else if (("Sumadhura Vasavi LLP").equalsIgnoreCase(carParkingAllotmentPdfInfo.getFlatSaleOwner())) {
				leftSidelogoForPdf  = properties.getProperty("SUMADHURA_LOGO1");
				rightSidelogoForPdf = properties.getProperty(flatSaleOwner+"_LOGO1");
				thanksAndRegardsFrom = properties.getProperty(flatSaleOwner+"_THANKS_AND_REGARDS_MSG_FROM");
				greetingsFrom = properties.getProperty(flatSaleOwner+"_GREETING_MSG");
			}
		}
		
		carParkingAllotmentPdfDetailInfo.setRightSidelogoForPdf(rightSidelogoForPdf);
		carParkingAllotmentPdfDetailInfo.setLeftSidelogoForPdf(leftSidelogoForPdf);
		carParkingAllotmentPdfDetailInfo.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		carParkingAllotmentPdfDetailInfo.setGreetingsFrom(greetingsFrom);
		carParkingAllotmentPdfDetailInfo.setAllotmentDate(TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		/* Customer Details and Project Details */
		String basementName = "N/A";
		String slotName = "N/A";
		String flatNo = "N/A";
		String custName = "N/A";
		String siteName = "N/A";
		String siteAddress = "N/A";

		/* Customer Details */
		/* Basement Name */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getBasementName())) {
			basementName = carParkingAllotmentPdfInfo.getBasementName();
		}
		/* Slot Name */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getSlotName())) {
			slotName = carParkingAllotmentPdfInfo.getSlotName();
		}
		/* Flat No */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getFlatNo())) {
			flatNo = carParkingAllotmentPdfInfo.getFlatNo();
		}
		/* Customer Name */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getCustName())) {
			custName = carParkingAllotmentPdfInfo.getCustName();
		}
		/* Site Name */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getSiteName())) {
			siteName = carParkingAllotmentPdfInfo.getSiteName();
		}
		/* Site Address */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getSiteAddress())) {
			siteAddress = carParkingAllotmentPdfInfo.getSiteAddress();
		}
		
		carParkingAllotmentPdfDetailInfo.setBasementName(basementName);
		carParkingAllotmentPdfDetailInfo.setSlotName(slotName);
		carParkingAllotmentPdfDetailInfo.setFlatNo(flatNo);
		carParkingAllotmentPdfDetailInfo.setCustName(custName);
		carParkingAllotmentPdfDetailInfo.setSiteName(siteName);
		carParkingAllotmentPdfDetailInfo.setSiteAddress(siteAddress);

		/* Office Address Details */
		if(("Indimax").equalsIgnoreCase(carParkingAllotmentPdfInfo.getFlatSaleOwner())) {
			String flatSaleOwner = carParkingAllotmentPdfInfo.getFlatSaleOwner().trim().replaceAll(" ", "");
			String companyName = properties.getProperty(flatSaleOwner+"_COMPANY_NAME");
			String companyBilling = properties.getProperty(flatSaleOwner+"_COMPANY_BILLING_ADDRESS");
			String companyTelephoneNo = properties.getProperty(flatSaleOwner+"_COMPANY_TELEPHONE_NO");
			String companyEmail = properties.getProperty(flatSaleOwner+"_COMPANY_EMAIL");

			String companyCin = properties.getProperty(flatSaleOwner+"_COMPANY_CIN");
			String companyGstin = properties.getProperty(flatSaleOwner+"_COMPANY_GSTIN");
			String companyWebsite = properties.getProperty(flatSaleOwner+"_COMPANY_WEBSITE");
			String companyLlpin = properties.getProperty(flatSaleOwner+"_COMPANY_LLPIN");
			String companyPanNumber = properties.getProperty(flatSaleOwner+"_COMPANY_PAN");
			String companyCity = properties.getProperty(flatSaleOwner+"_COMPANY_CITY");
			
			carParkingAllotmentPdfDetailInfo.setCompanyName(companyName);
			carParkingAllotmentPdfDetailInfo.setCompanyBillingAddress(companyBilling);
			carParkingAllotmentPdfDetailInfo.setCompanyTelephoneNo(companyTelephoneNo==""?"-":companyTelephoneNo);
			carParkingAllotmentPdfDetailInfo.setCompanyEmail(companyEmail==""?"-":companyEmail);
			
			carParkingAllotmentPdfDetailInfo.setCompanyCin(companyCin==""?"-":companyCin);
			carParkingAllotmentPdfDetailInfo.setCompanyGstin(companyGstin==null?"-":companyGstin);
			carParkingAllotmentPdfDetailInfo.setCompanyWebsite(companyWebsite);
			carParkingAllotmentPdfDetailInfo.setCompanyLlpin(companyLlpin);
			carParkingAllotmentPdfDetailInfo.setCompanyPanNumber(companyPanNumber);
			carParkingAllotmentPdfDetailInfo.setCity(companyCity);
			
		}else if(Util.isNotEmptyObject(carParkingAllotmentDetailInfo.getOfficeDetailsResponseList()) && Util.isNotEmptyObject(carParkingAllotmentDetailInfo.getOfficeDetailsResponseList().get(0))) {
			OfficeDtlsResponse officeDetailsResponse = carParkingAllotmentDetailInfo.getOfficeDetailsResponseList().get(0);
			if(Util.isNotEmptyObject(officeDetailsResponse.getName())) {
				carParkingAllotmentPdfDetailInfo.setCompanyName(officeDetailsResponse.getName());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyName("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getBillingAddress())){
				carParkingAllotmentPdfDetailInfo.setCompanyBillingAddress(officeDetailsResponse.getBillingAddress());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyBillingAddress("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getTelephoneNo())) {
				carParkingAllotmentPdfDetailInfo.setCompanyTelephoneNo(officeDetailsResponse.getTelephoneNo());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyTelephoneNo("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getEmail())) {
				carParkingAllotmentPdfDetailInfo.setCompanyEmail(officeDetailsResponse.getEmail());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyEmail("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCin())) {
				carParkingAllotmentPdfDetailInfo.setCompanyCin(officeDetailsResponse.getCin());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyCin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getGstin())) {
				carParkingAllotmentPdfDetailInfo.setCompanyGstin(officeDetailsResponse.getGstin());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyGstin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getWebsite())) {
				carParkingAllotmentPdfDetailInfo.setCompanyWebsite(officeDetailsResponse.getWebsite());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyWebsite("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getLlpin())) {
				carParkingAllotmentPdfDetailInfo.setCompanyLlpin(officeDetailsResponse.getLlpin());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyLlpin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getPan())) {
				carParkingAllotmentPdfDetailInfo.setCompanyPanNumber(officeDetailsResponse.getPan());
			}else {
				carParkingAllotmentPdfDetailInfo.setCompanyPanNumber("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCity())) {
				carParkingAllotmentPdfDetailInfo.setCity(officeDetailsResponse.getCity());
			} else {
				carParkingAllotmentPdfDetailInfo.setCity("N/A");
			}
			
		}else {
			carParkingAllotmentPdfDetailInfo.setCompanyName("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyBillingAddress("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyTelephoneNo("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyEmail("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyCin("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyGstin("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyWebsite("N/A");
			carParkingAllotmentPdfDetailInfo.setCompanyPanNumber("N/A");
			carParkingAllotmentPdfDetailInfo.setCity("N/A");
		}
		
		/* File Path and Url */
		String fileName = carParkingAllotmentPdfDetailInfo.getFlatNo()+"_"+carParkingAllotmentPdfDetailInfo.getSlotName()+"_CarParking_Allotment_Letter.pdf";
		String filePath = "";
		String fileUrl = "";
		/* For direct saving path in db */
		if(CarParkingAllotmentEnum.SAVE.equals(cpAllotmentEnum)) {
			filePath = responceCodesUtil.getApplicationNamePropeties("CAR_PARKING_ALLOTMENT_FILE_DIRECTORY_PATH")+carParkingAllotmentPdfInfo.getSiteId()
			  		   +"/"+carParkingAllotmentPdfInfo.getFlatBookId();
			
			/* Getting Unique File Name */
		    fileName = pdfHelper.getFileName(filePath, fileName);
		    
		/* for pre-view purpose not saving anywhere */
		}else if(CarParkingAllotmentEnum.PRE_VIEW.equals(cpAllotmentEnum)) {
			filePath = responceCodesUtil.getApplicationNamePropeties("CAR_PARKING_ALLOTMENT_TEMP_FILE_DIRECTORY_PATH")+carParkingAllotmentPdfInfo.getSiteId()
			           +"/"+carParkingAllotmentPdfInfo.getFlatBookId()+"/"+carParkingAllotmentPdfInfo.getSlotId();
			
			/* Deleting Old File */
		    Boolean oldFileStatus = pdfHelper.deleteFileIfExists(filePath, fileName);
		    if(!oldFileStatus) {
		    	throw new IOException("Unable to delete old file in specified location");
		    }
		}
		
		/* Employee Signature File by empId */
		if(Util.isNotEmptyObject(carParkingAllotmentPdfInfo.getApproverEmpId())) {
			signatureEmp = responceCodesUtil.getApplicationNamePropeties("SIGNATURE_EMP")+carParkingAllotmentPdfInfo.getApproverEmpId()
						   +"/"+carParkingAllotmentPdfInfo.getApproverEmpId()+"_Sign.png";
			carParkingAllotmentPdfDetailInfo.setSignatureEmp(signatureEmp);
		}
		
	    carParkingAllotmentPdfDetailInfo.setAllotmentLetterFilePath(filePath+"/"+fileName);
	    email.setCarParkingAllotmentPdfDetailInfo(carParkingAllotmentPdfDetailInfo);
	    pdfHelper.xMLWorkerHelperForCarParkingAllotmentLetter(email);
	    
	    /* For direct saving path in db */
		if(CarParkingAllotmentEnum.SAVE.equals(cpAllotmentEnum)) {
			fileUrl =  responceCodesUtil.getApplicationNamePropeties("CAR_PARKING_ALLOTMENT_FILE_URL_PATH")+carParkingAllotmentPdfInfo.getSiteId()
            +"/"+carParkingAllotmentPdfInfo.getFlatBookId()+"/"+fileName;
		    
		/* for pre-view purpose not saving anywhere */
		}else if(CarParkingAllotmentEnum.PRE_VIEW.equals(cpAllotmentEnum)) {
			fileUrl =  responceCodesUtil.getApplicationNamePropeties("CAR_PARKING_ALLOTMENT_TEMP_FILE_URL_PATH")+carParkingAllotmentPdfInfo.getSiteId()
            +"/"+carParkingAllotmentPdfInfo.getFlatBookId()+"/"+carParkingAllotmentPdfInfo.getSlotId()+"/"+fileName;
		}
		return fileUrl;
	}

	public void sendPushNotificationAllert(final CarParkingAllotmentInfo carParkingAllotmentInfo, final Status status) {
		LOGGER.info("*** The control is inside of the sendPushNotificationAllert in CarParkingAllotmentServiceHelper ***");
		String notificationTitle = null;
		String notificationBody = null;
		String fileLocation = null;
		String typeMsg = null;
		if(Status.ALLOTTED.description.equalsIgnoreCase(status.description)) {
			notificationTitle = "Carpaking allotment Notification.";
			notificationBody = "We are pleased to inform you that the Car Parking Allotment for your flat is done as per your selection. Attached is the Allotment letter for your reference.";
			fileLocation = carParkingAllotmentInfo.getAllotmentLetterPath();
			typeMsg = "Car_Parking_Allotment";
		}else if(Status.CANCELLED.description.equalsIgnoreCase(status.description)) {
			notificationTitle = "";
			notificationBody = "";
		}else if(Status.HOLD.description.equalsIgnoreCase(status.description)) {
			notificationTitle = "";
			notificationBody = "";
		}
		ReferedCustomer referedCustomer = new ReferedCustomer();
		Map<String, String> devicePojsMap = new ConcurrentHashMap<>();
		referedCustomer.setFlatBookingId(carParkingAllotmentInfo.getFlatBookId());
		List<DevicePojo> devicePojos =null;// referredCustomerDaoImpl.getDeviceDetails(referedCustomer);
		/* inorder to eliminate duplicate Device Tokens we are using HashMap */
		for(DevicePojo devicePojo : devicePojos) {
			if(Util.isNotEmptyObject(devicePojo)  && Util.isNotEmptyObject(devicePojo.getDevicetoken()) 
					&& Util.isNotEmptyObject(devicePojo.getOstype())) {
				devicePojsMap.put(devicePojo.getDevicetoken(), devicePojo.getOstype());
			}
		}
		/* sending push notification to Andriod and Ios devices */
		for (String deviceToken : devicePojsMap.keySet()) {
			if (devicePojsMap.get(deviceToken).equalsIgnoreCase("IOS")) {
				IOSPushNotificationUtil iOSPushNotificationUtil = new IOSPushNotificationUtil();
				JSONObject payload = iOSPushNotificationUtil.dataObject(notificationTitle,null,notificationBody,typeMsg,null,fileLocation,null);
				try {
					iOSPushNotificationUtil.sendIosPushNotification(Arrays.asList(deviceToken), payload.toString(),false);
				} catch (Exception ex) {
					LOGGER.error("*** The Exception information is ***" + ex);
				}
			}else if (devicePojsMap.get(deviceToken).equalsIgnoreCase("Android")) {
				/* sending push notifications to the andriod devices */
				PushNotificationUtil notifyUtilObj = new PushNotificationUtil();
				try {
					 notifyUtilObj.pushFCMNotification(deviceToken,notificationTitle,null,notificationBody,typeMsg,null,fileLocation,null);
				} catch (Exception ex) {
					LOGGER.error("*** The Exception information is ***" + ex);
				}
			}
		}
	}

	public void sendApprovalDetailsEmployeeMail(CarParkingAllotmentPdfPojo allotmentPdfPojo,CarParkingApprovalLevelsPojo approvalLevelsPojo, 
		EmployeePojo employeePojo, Status status) {
		LOGGER.info("*** The control is inside of the sendApprovalDetailsEmployeeMail in CarParkingAllotmentServiceHelper ***");
		Email email = new Email();
		String siteName = Util.isNotEmptyObject(allotmentPdfPojo.getSiteName())?allotmentPdfPojo.getSiteName():"N/A";
		String flatNo = Util.isNotEmptyObject(allotmentPdfPojo.getFlatNo())?allotmentPdfPojo.getFlatNo():"N/A";
		String basementName = Util.isNotEmptyObject(allotmentPdfPojo.getBasementName())?allotmentPdfPojo.getBasementName():"N/A";
		String slotName = Util.isNotEmptyObject(allotmentPdfPojo.getSlotName())?allotmentPdfPojo.getSlotName():"N/A";
		email.setSiteName(siteName);
		email.setFlatNo(flatNo);
		email.setBasementName(basementName);
		email.setSlotName(slotName);
		if(Status.PENDING_FOR_APPROVAL.equals(status) && Util.isNotEmptyObject(approvalLevelsPojo)
			&& Util.isNotEmptyObject(approvalLevelsPojo.getEmpMail())) {
			String subject = "Approval Request for Car parking allotment - "+siteName+" "+flatNo;
			String empName = Util.isNotEmptyObject(approvalLevelsPojo.getEmpName())?approvalLevelsPojo.getEmpName():"N/A";
			email.setSubject(subject);
			email.setEmployeeName(empName);
			email.setTemplateName("/vmtemplates/Car_Parking_Allotment_Pending_For_Approval_Emp_Mail.vm");
			for(String mail : approvalLevelsPojo.getEmpMail().split(",")) {
				email.setToMail(mail.trim());
				mailServiceImpl.sendEmployeeEmailAlert(email);
			}
		}else if(Status.APPROVED.equals(status) && Util.isNotEmptyObject(allotmentPdfPojo.getEmpMail())) {
			String subject = "Approved - "+siteName+" "+flatNo+" Car parking allotment request";
			String empName = Util.isNotEmptyObject(allotmentPdfPojo.getEmpName())?allotmentPdfPojo.getEmpName():"N/A";
			String approverEmpName = "N/A";
			if(Util.isNotEmptyObject(employeePojo) && Util.isNotEmptyObject(employeePojo.getFirstName())) {
				approverEmpName = employeePojo.getFirstName();
			}
			email.setSubject(subject);
			email.setEmployeeName(empName);
			email.setApproverEmpName(approverEmpName);
			email.setTemplateName("/vmtemplates/Car_Parking_Allotment_Approved_Emp_Mail.vm");
			for(String mail : allotmentPdfPojo.getEmpMail().split(",")) {
				email.setToMail(mail.trim());
				mailServiceImpl.sendEmployeeEmailAlert(email);
			}
		}else if(Status.REJECTED.equals(status) && Util.isNotEmptyObject(allotmentPdfPojo.getEmpMail())) {
			String subject = "Rejected - "+siteName+" "+flatNo+" Car parking allotment request";
			String empName = Util.isNotEmptyObject(allotmentPdfPojo.getEmpName())?allotmentPdfPojo.getEmpName():"N/A";
			String approverEmpName = "N/A";
			if(Util.isNotEmptyObject(employeePojo) && Util.isNotEmptyObject(employeePojo.getFirstName())) {
				approverEmpName = employeePojo.getFirstName();
			}
			email.setSubject(subject);
			email.setEmployeeName(empName);
			email.setApproverEmpName(approverEmpName);
			email.setTemplateName("/vmtemplates/Car_Parking_Allotment_Rejected_Emp_Mail.vm");
			for(String mail : allotmentPdfPojo.getEmpMail().split(",")) {
				email.setToMail(mail.trim());
				mailServiceImpl.sendEmployeeEmailAlert(email);
			}
		}
	}
}
