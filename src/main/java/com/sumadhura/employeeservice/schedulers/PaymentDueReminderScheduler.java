package com.sumadhura.employeeservice.schedulers;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.SiteLevelNotifyRequestDTO;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialDetailsPojo;

import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialPushNotificationHelper;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;



@Component("paymentDueReminderScheduler")
public class PaymentDueReminderScheduler {
	
	@Autowired
	@Qualifier("EmployeeFinancialServiceDao")
	private EmployeeFinancialServiceDao employeeFinancialServiceDao;
	
	
	
	@Autowired(required = true)
	@Qualifier("EmployeeFinancialPushNotificationHelper")
	private EmployeeFinancialPushNotificationHelper pushNotificationHelper;
	
	private Logger logger = Logger.getLogger(PaymentDueReminderScheduler.class);
	
	@Value("${PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_DESC}")
	private String PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_DESC;
	
	@Value("${PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_TITLE}")
	private String PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_TITLE;
	
	@Value("${PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_TEXT}")
	private String PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_TEXT;
	
	@Scheduled(cron = "${cron.expressionForDueRemaider}")
	/* cron pattern for every second */
 //  @Scheduled(cron="*/10 * * * * *")
	public void paymentDueReminder() {
		logger.info("*** The control is inside the paymentDueReminder in PaymentDueReminderScheduler ****");
		synchronized ("thead blocked") {
			CustomerInfo customerInfo = new CustomerInfo();
			Map<String, Object> financialFailedData = new HashMap<String, Object>();
		//	customerInfo.setSiteId(126L); //TESTING PURPUSE
			//customerInfo.setFlatBookingId(364l);
			/*loading all active customer details*/
			List<CustomerPropertyDetailsPojo> customersList = employeeFinancialServiceDao.getCustomerAllActivePropertyDetails(customerInfo, Status.ACTIVE);
			logger.info("all active customers size " + customersList.size());
			for (CustomerPropertyDetailsPojo customerPojo : customersList) {
				logger.info("*** The control is inside the paymentDueReminder in PaymentDueReminderScheduler customer details ****"+ customerPojo);
				CustomerInfo customerInfoObj = new CustomerInfo();
				customerInfoObj.setFlatBookingId(customerPojo.getFlatBookingId());
				customerInfoObj.setSiteId(customerPojo.getSiteId());
				/*loading all active Financial details to take due date*/
				List<FinancialDetailsPojo> FinancialDetailsList = employeeFinancialServiceDao.getFinancialDetails(customerInfoObj, Status.ACTIVE);
				if (Util.isEmptyObject(FinancialDetailsList)) {
					financialFailedData.put(customerPojo.getFlatBookingId().toString(), customerPojo);
				}
				boolean status =false;
				/*looping Financial details to any due date is equal to */
				for (FinancialDetailsPojo FinancialDetailsPojoObj : FinancialDetailsList) {
				    int days =0;
				    /*based on due date getting days*/
					if(Util.isNotEmptyObject(FinancialDetailsPojoObj.getDueDate())){
					  days = TimeUtil.getDaysFromTwoDatesForPaymentDue(new Timestamp(System.currentTimeMillis()),FinancialDetailsPojoObj.getDueDate());
					}
					/*if contition true making status as true*/
					if (days == 3) {
						status=true;
						break;
					}
				}
				/*if status is true sending push Notification*/
				if (status) {
					/*for Project Notification code not in use*/
					/* creating SiteLevelNotifyRequestDTO and setting whatever values required. */
//					final SiteLevelNotifyRequestDTO notificationRequest = new SiteLevelNotifyRequestDTO();
//					notificationRequest.setFlatIds(Arrays.asList(customerPojo.getFlatId()));
//					notificationRequest.setSiteIds(Arrays.asList(customerPojo.getSiteId()));
//					notificationRequest.setEmployeeId(MetadataId.SYSTEM.getId());
//					notificationRequest.setRequestAction("BookingForm");
//					notificationRequest.setRequestPurpose("paymentDueReminder");
//					Map<String, Object> data = new HashMap<String, Object>();
//					data.put("#siteName", customerPojo.getSiteName());
//					notificationRequest.setData(data);
//					logger.info("*** The control is inside the paymentDueReminder in PaymentDueReminderScheduler and Exception raised for Customer ****"+ customerPojo + " /n notification Object is ");
//					/*sending Project Notification*/
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							try {
//								notificationRestService.sendProjectNotificationsForApprovals(notificationRequest);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								logger.info("*** The control is inside the paymentDueReminder in PaymentDueReminderScheduler and Exception raised for Customer ****  /n Exception is " + e);
//							}
//						}
//					}).start();
					/*for Project Notification code not in use*/
					
					
					/*sending push notification */
					final EmployeeFinancialPushNotificationInfo financialPushNotification = new EmployeeFinancialPushNotificationInfo();
					financialPushNotification.setBookingFormId(customerPojo.getFlatBookingId());
					financialPushNotification.setNotificationTitle(PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_TITLE);
					financialPushNotification.setNotificationBody(PAYMENT_DUE_REMINDER_NOTIFY_BOOKING_FORM_NOTI_DESC);
					financialPushNotification.setNotificationDescription("");
					financialPushNotification.setTypeMsg("Payment Remainder");
					financialPushNotification.setSiteId(customerPojo.getSiteId());
						logger.info("*** The control is inside the paymentDueReminder in PaymentDueReminderScheduler and Exception raised for Customer ****"+ customerPojo + " /n notification Object is ");
						/*sending push Notification*/
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									pushNotificationHelper.sendFinancialStatusNotification(financialPushNotification,null);
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("*** The control is inside the paymentDueReminder in PaymentDueReminderScheduler and Exception raised for Customer ****  /n Exception is " + e);
								}
							}
						}).start();
				}

			}
		}
	}
	   
	   
	   
	   
	   
}
