/**
 * 
 */
package com.sumadhura.employeeservice.service.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.Type;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;

import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DevicePojo;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormRequestInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.NotificationRequestInfo;
import com.sumadhura.employeeservice.service.dto.PushNotificationInfo;
import com.sumadhura.employeeservice.util.IOSPushNotificationUtil;
import com.sumadhura.employeeservice.util.PushNotificationUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * BookingFormServiceHelper bean class provides helper methods for BookingFormServiceImpl.
 * 
 * @author Venkat_Koniki
 * @since 22.10.2019
 * @time 12:40PM
 */

@Component("BookingFormServiceHelper")
public class BookingFormServiceHelper{
	
	@Autowired(required = true)
	@Qualifier("BookingFormServiceDaoImpl")
	private BookingFormServiceDao bookingFormServiceDaoImpl;
	
	@Autowired(required=true)
	@Qualifier("employeeFinancialHelper")
	private EmployeeFinancialHelper employeeFinancialHelper;
	
	
	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
	private static final Logger LOGGER = Logger.getLogger(BookingFormServiceHelper.class);
	
	@Async("myexecutor")
	public Future<Boolean> sendBookingFormCustomerMailAndNotification(@NonNull BookingFormRequest bookingFormRequest) throws InformationNotFoundException{
		System.out.println("****Thread name****"+Thread.currentThread().getName());
		LOGGER.info("**** The control is inside the sendCustomerMail in  BookingFormServiceHelper ****");
		String ActionBookingUrl=bookingFormRequest.getRequestUrl();
		bookingFormRequest.setRequestUrl("venkat");
		bookingFormRequest.setFlatBookingId(Util.isNotEmptyObject(bookingFormRequest.getFlatBookingId())?bookingFormRequest.getFlatBookingId():0l);
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getCustomerPropertyDetails(bookingFormRequest);
		
		if(Util.isNotEmptyObject(customerPropertyDetailsPojos) && Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0))) {
			
			BookingFormRequestInfo bookingFormRequestInfo = new BookingFormRequestInfo();
			bookingFormRequestInfo.setCustomerName(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerName())?customerPropertyDetailsPojos.get(0).getCustomerName():"N/A");
			bookingFormRequestInfo.setFlatNo(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getFlatNo())?customerPropertyDetailsPojos.get(0).getFlatNo():"N/A");
			bookingFormRequestInfo.setSiteName(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getSiteName())?customerPropertyDetailsPojos.get(0).getSiteName():"N/A");
			bookingFormRequestInfo.setMails(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerEmail())?Arrays.asList(customerPropertyDetailsPojos.get(0).getCustomerEmail()):Arrays.asList("N/A"));
			
			if(mailServiceImpl.sendBookingApprovalMail(bookingFormRequestInfo)) {
				LOGGER.info("**** The booking form approval mail is sent successfully to the customer. ****");
			}
				
			 NotificationRequestInfo notificationRequestInfo = new NotificationRequestInfo();
	    	 notificationRequestInfo.setRequestUrl("bookingform");
	    	 notificationRequestInfo.setMobile(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getContactNumber())?customerPropertyDetailsPojos.get(0).getContactNumber():"N/A");
	    	 notificationRequestInfo.setEmail(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerEmail())?customerPropertyDetailsPojos.get(0).getCustomerEmail():"N/A");
	    	 List<DevicePojo> devicePojos = null;
	    	 try {
	    	  devicePojos = null;// notificationServiceDaoImpl.getDeviceList(notificationRequestInfo,Status.ACTIVE);
				
	    	     /*
				  devicePojos = new ArrayList<DevicePojo>(); DevicePojo devicePojo= new
				  DevicePojo(); devicePojo.setOstype("android"); devicePojo.setDevicetoken(
				  "dbGGUb4Duwo:APA91bEm2rmDkkj2gTtlSXldXAUM1wd5pgP9AR4jTcKLAP1BLMOCAD5BWHrhmRXjyBr_wLn-Qw1ni4Vrw3tlrm5WWwjKoHvZy9awXvM5RQtPlFV10wXqroK1EQfQvqJx7uZEogu2iDgN"
				  ); devicePojos.add(devicePojo);
				*/ 
	    	  
	    	 }catch(Exception ex) {
	    		 ex.printStackTrace();
	    	 }
			if (Util.isNotEmptyObject(devicePojos)) {
				for (DevicePojo devicePojo : devicePojos) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getOstype())) {
						PushNotificationInfo info = ctreateBookigFormApprovalNotification(devicePojo);
						try {
							if (Util.isNotEmptyObject(info.getOsType()) && info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
								/* send push notification to Android customer while booking form got Approved. */
								final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
								try {
								pushNotificationUtil.pushFCMNotification(info);
								}catch(Exception ex) {
									 LOGGER.error("**** The Error Message ****"+ex);
							    }
							} else if (Util.isNotEmptyObject(info.getOsType()) && info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
								/* send push notification to IOS customer while booking form got Approved. */
								final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
								try {
								ioSPushNotificationUtil.sendIosPushNotification(Arrays.asList(info.getDeviceToken()),ioSPushNotificationUtil.getBookingformPushNotificationPayLoad(info).toString(), false);
								}catch(Exception ex) {
									 LOGGER.error("**** The Error Message ****"+ex);
							    }
							}
						} catch (Exception e) {
							LOGGER.error("**** The Exception is raised while sending booking form approval notifications ****");
						}
					}
				}
			}
		}
		bookingFormRequest.setRequestUrl(ActionBookingUrl);
        return new AsyncResult<Boolean>(Boolean.TRUE);
	}
	private PushNotificationInfo ctreateBookigFormApprovalNotification(@NonNull DevicePojo devicePojo) throws InformationNotFoundException{
		LOGGER.info("**** The control is inside the ctreateBookigFormApprovalNotification in  BookingFormServiceHelper ****");
		
		PushNotificationInfo pushNotificationInfo = new PushNotificationInfo();
		if (Util.isNotEmptyObject(devicePojo.getOstype()) && devicePojo.getOstype().equalsIgnoreCase(Type.IOS.getName())) {
			  pushNotificationInfo.setOsType(Type.IOS.getName());
			if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
				pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
				pushNotificationInfo.setNotificationTitle("Notification : Sumadhura Booking Form");
				pushNotificationInfo.setNotificationMessage("Message: Dear Customer your Booking Form got Approved Please register into Sumadhura Customerapp using your Pan Number!.");
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while sending Notification to Customer. ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		} else if (Util.isNotEmptyObject(devicePojo.getOstype()) && devicePojo.getOstype().equalsIgnoreCase(Type.ANDRIOD.getName())) {
			  pushNotificationInfo.setOsType(Type.ANDRIOD.getName());
			if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
				pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
				pushNotificationInfo.setNotificationTitle("Notification : Sumadhura Booking Form ");
				pushNotificationInfo.setNotificationMessage("<b>Message</b> :"+ "Dear Customer your Booking Form got Approved Please register into Sumadhura Customerapp using your Pan Number!.");
				pushNotificationInfo.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
				pushNotificationInfo.setNotificationPriority("high");
				pushNotificationInfo.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
				pushNotificationInfo.setNotificationStyle("picture");
				pushNotificationInfo.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
				pushNotificationInfo.setNotificatioTitleColor("red");
				pushNotificationInfo.setNotificationType("message");
				pushNotificationInfo.setNotificationSound("default");	
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while sending Notification to Customer. ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		}
		return pushNotificationInfo;
	}
	
	public void sendBookingNOCMail(Map<String, Object> dataForGenerateAllotmentLetterHelper, CustomerPropertyDetailsInfo customerPropertyDetailsInfo
			, List<String> toMails, List<String> nOC_CcMails) throws Exception {
		LOGGER.info(" ***** BookingFormServiceHelper.sendBookingNOCMail() ***** ");
		Email email = new Email();
		//FileInfo fileInfo = new FileInfo();
		//List<FileInfo> fileInfoList = new ArrayList<>();
		DemandNoteGeneratorInfo  info = new DemandNoteGeneratorInfo();
	
		email.setTemplateName("/vmtemplates/NocGenerateEmail.vm");
		email.setSubject(customerPropertyDetailsInfo.getSiteName()+"-"+customerPropertyDetailsInfo.getFlatNo()+" NOC letter generated.");//this is for CUG
		email.setToMails(toMails.toArray(new String[] {}));
		if(Util.isNotEmptyObject(nOC_CcMails)) {
			email.setCcs(nOC_CcMails.toArray(new String[] {}));
		}
	
		String rightSidelogoForPdf = "";
		String leftSidelogoForPdf = "";
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";

		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());		

		Map<String,String> logoAndOtherDetails = employeeFinancialHelper.getTheCommonInformation(site,customerPropertyDetailsInfo);			
		
		rightSidelogoForPdf = logoAndOtherDetails.get("rightSidelogoForPdf");
		leftSidelogoForPdf = logoAndOtherDetails.get("leftSidelogoForPdf");
		thanksAndRegardsFrom = logoAndOtherDetails.get("thanksAndRegardsFrom");
		greetingsFrom = logoAndOtherDetails.get("greetingsFrom");

		Map<String,List<Map<String,Object>>> dataForPdf1 = new HashMap<>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> customerDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerDetails");
		@SuppressWarnings("unchecked")
		Map<String, Object> customerUnitDetails = (Map<String, Object>) dataForGenerateAllotmentLetterHelper.get("customerUnitDetails");
		@SuppressWarnings("unchecked")
		List<OfficeDtlsPojo> officeDetailsPojoList = (List<OfficeDtlsPojo>) dataForGenerateAllotmentLetterHelper.get("officeDetailsPojoList");
		@SuppressWarnings("unchecked")
		List<AddressInfo> siteAddressInfoList = (List<AddressInfo>) dataForGenerateAllotmentLetterHelper.get("siteAddressInfoList");
		//@SuppressWarnings("unchecked")
		//List<FinProjectAccountResponse> finProjectAccountResponseList = (List<FinProjectAccountResponse>) dataForGenerateAllotmentLetterHelper.get("finProjectAccountResponseList");
		
		employeeFinancialHelper.setOfficeDetailsPojo(officeDetailsPojoList,customerPropertyDetailsInfo,info);
		employeeFinancialHelper.getSiteAdderss(siteAddressInfoList,info,customerPropertyDetailsInfo);

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
		info.setDemandNotePdfFileName(customerPropertyDetailsInfo.getFlatNo()+" -"+customerPropertyDetailsInfo.getSiteName()+" - NOC Letter.pdf");
		
		email.setDemandNoteGeneratorInfo(info);
		//Map<String,Object> welcomeLetterData = pdfHelper.XMLWorkerHelperForWelcomeLetter(email, fileInfo);
		//dataForGenerateAllotmentLetterHelper.put("welcomeLetterEmailContent", welcomeLetterData.get("welcomeLetterEmailContent"));
		mailServiceImpl.sendNOCLetterMailToEmployee(email);
	}
	
}