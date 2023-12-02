package com.sumadhura.employeeservice.service.helpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.Type;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;

import com.sumadhura.employeeservice.persistence.dto.DevicePojo;
import com.sumadhura.employeeservice.service.dto.PushNotificationInfo;
import com.sumadhura.employeeservice.util.IOSPushNotificationUtil;
import com.sumadhura.employeeservice.util.PushNotificationUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;
@Component("EmployeeFinancialPushNotificationHelper")
public class EmployeeFinancialPushNotificationHelper {
	
	
	
    private static final Logger LOGGER = Logger.getLogger(EmployeeFinancialPushNotificationHelper.class);
	
	@Async("myexecutor")
	public Future<Boolean> sendFinancialStatusNotification(@NonNull EmployeeFinancialPushNotificationInfo financialPushNotification,Object objNotinuse) 
			throws InformationNotFoundException{
		System.out.println("****Thread name****"+Thread.currentThread().getName());
		LOGGER.info("***** Control inside the EmployeeFinancialPushNotificationHelper.sendFinancialStatusNotification() *****");
	    List<DevicePojo> devicePojos = null;
	    Map<String, DevicePojo> devicePojsMap = new HashMap<>();
		try {
			ReferedCustomer referedCustomer = new ReferedCustomer();
			referedCustomer.setFlatBookingId(financialPushNotification.getBookingFormId());
			devicePojos = null;//referredCustomerDaoImpl.getDeviceDetails(referedCustomer);
			/* inorder to eliminate duplicate Device Tokens we are using HashMap */
			for(DevicePojo devicePojo : devicePojos) {
				if(Util.isNotEmptyObject(devicePojo)  && Util.isNotEmptyObject(devicePojo.getDevicetoken()) 
						&& Util.isNotEmptyObject(devicePojo.getOstype())) {
					devicePojsMap.put(devicePojo.getDevicetoken(), devicePojo);
				}
			}
			
	    	}catch(Exception e) {
				LOGGER.error("**** The Exception is raised while sending Financial notifications ****");
			}
		
		for(DevicePojo devicePojo : devicePojsMap.values()) {
			if(Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getOstype()) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
				PushNotificationInfo info = createReferedCustomerReferralStatusNotification(devicePojo,financialPushNotification);
				try {
					if (Util.isNotEmptyObject(devicePojo.getOstype()) && devicePojo.getOstype().equalsIgnoreCase(Type.ANDRIOD.getName())) {
						/* send push notification to Android customer while booking form got Approved. */
						final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
						pushNotificationUtil.pushFCMNotification(info);
					} else if (Util.isNotEmptyObject(devicePojo.getOstype()) && devicePojo.getOstype().equalsIgnoreCase(Type.IOS.getName())) {
						/* send push notification to IOS customer while booking form got Approved. */
						final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
						ioSPushNotificationUtil.sendIosPushNotification(Arrays.asList(info.getDeviceToken()),
								ioSPushNotificationUtil.getFinancialPushNotificationPayLoad(info).toString(), true);
					}
				} catch(Exception e) {
					e.printStackTrace();
					LOGGER.error("**** The Exception is raised while sending financial notifications ****");
				}
			}
		}	
		
		return new AsyncResult<Boolean>(Boolean.TRUE);
	}
	private PushNotificationInfo createReferedCustomerReferralStatusNotification(@NonNull DevicePojo devicePojo,EmployeeFinancialPushNotificationInfo financialPushNotification2) throws InformationNotFoundException{
		LOGGER.info("**** The control is inside the createReferedCustomerReferralStatusNotification in  ReferedCustomerHelper ****");
		PushNotificationInfo pushNotificationInfo = new PushNotificationInfo();
		if (devicePojo.getOstype().equalsIgnoreCase(Type.ANDRIOD.getName())) {
			pushNotificationInfo.setNotificationTitle("<b>"+financialPushNotification2.getNotificationTitle()+"<b>");
		} else if (devicePojo.getOstype().equalsIgnoreCase(Type.IOS.getName())) {
			pushNotificationInfo.setNotificationTitle(financialPushNotification2.getNotificationTitle());
		}
		if("Payment Remainder".equalsIgnoreCase(financialPushNotification2.getTypeMsg()) && devicePojo.getOstype().equalsIgnoreCase(Type.IOS.getName()))
		{
			financialPushNotification2.setNotificationBody(financialPushNotification2.getNotificationBody().replaceAll("<br/>", ""));
		}
		if("Sumadhura Ticket Notification".equalsIgnoreCase(financialPushNotification2.getTypeMsg()) )
		{
			financialPushNotification2.setNotificationBody(financialPushNotification2.getNotificationBody().replaceAll("<br/>", ""));
			pushNotificationInfo.setTicketId(financialPushNotification2.getTicketId());
			pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
		}
		
		pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
		pushNotificationInfo.setNotificationMessage(financialPushNotification2.getNotificationBody());
		pushNotificationInfo.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificationPriority("high");
		pushNotificationInfo.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificationStyle("picture");
		pushNotificationInfo.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificatioTitleColor("red");
		pushNotificationInfo.setNotificationType("message");
		pushNotificationInfo.setNotificationSound("default");
		
		//EmployeeFinancialPushNotificationInfo financialPushNotification = new EmployeeFinancialPushNotificationInfo();
		
		/* setting refer friend Notifications */
		if(financialPushNotification2.getTypeMsg().equalsIgnoreCase("Sumadhura Demand Note")) {
			
		} else if(financialPushNotification2.getTypeMsg().equalsIgnoreCase("Sumadhura Receipt")) {
			
		} else {}

		/* to set ReferedCustomer Object into PushNotificationInfo Object */
		pushNotificationInfo.setFinancialPushNotification(financialPushNotification2);
		return pushNotificationInfo;
	}
	
}
