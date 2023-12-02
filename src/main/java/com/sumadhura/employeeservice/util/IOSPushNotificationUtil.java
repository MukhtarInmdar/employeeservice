/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumadhura.employeeservice.dto.ApptmtBookingsDetailResponse;
import com.sumadhura.employeeservice.dto.EmpFinPushLegalAndModifiNotificationInfo;
import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionRequest;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.service.dto.PushNotificationInfo;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.turo.pushy.apns.util.concurrent.PushNotificationResponseListener;

import lombok.NonNull;

/**
 * This class is useful to send Pushnotifications to IOS mobiles.
 * 
 * @author Venkat_Koniki
 * @since 07.09.2019
 * @time 01:05PM
 */
@Component("IOSPushNotification")
public class IOSPushNotificationUtil {

	private static Logger LOGGER = Logger.getLogger(IOSPushNotificationUtil.class);
    //private static final Semaphore semaphore = new Semaphore(10000);//Semaphore, also known as semaphore, is a concept in the operating system. In Java concurrent programming, semaphore controls the number of concurrent threads.
    
	/*
	 code for sending ios push notification using .p12 file
	 public ApnsService getApnsService() {
		LOGGER.info("*** The control is inside the getApnsService in  IOSPushNotification ***");
		ApnsService pushService = APNS.newService()
				.withCert(Thread.currentThread().getContextClassLoader()
						//.getResourceAsStream("Certificates push notify production.p12"), "Amaravadhis@123")
						.getResourceAsStream("Certificates.p12"), "$CMD148")
				  //.withSandboxDestination()
				.withProductionDestination()
				.build();
		return pushService;
	}*/

	//https://victorleungtw.medium.com/apple-push-notification-with-java-spring-framework-b6ddada216cf
	//https://softwareengineering.stackexchange.com/questions/334706/sending-push-notifications-to-ios-devices-from-a-java-server
	//https://github.com/jchambers/pushy
	public Object sendIosPushNotification(Collection<String> deviceTokens, String payload, Boolean isRequiredResult)throws Exception { 
		LOGGER.info("*** The control is inside the sendIosPushNotification in  IOSPushNotification ***");
		
/*		List<PayloadPerDevice> payloadDevicePairs = new Vector<PayloadPerDevice>();
		payloadDevicePairs.add(new PayloadPerDevice(PushNotificationPayload.alert(payload), deviceTokens.toString().replace("[", "").replace("]", "")));
		//payloadDevicePairs.add(new PayloadPerDevice(PushNotificationBigPayload.alert(payload), deviceTokens.toString().replace("[", "").replace("]", "")));
		sentDetails = Push.payloads(Thread.currentThread().getContextClassLoader().getResourceAsStream("Certificates.p12"), "$CMD148", true, payloadDevicePairs);
		LOGGER.info("sentDetails.getSuccessfulNotifications() "+sentDetails.getSuccessfulNotifications());
		LOGGER.info("sentDetails.getFailedNotifications() "+sentDetails.getFailedNotifications());
		InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("Certificates.p12");
		LOGGER.info(io);
*/				
		//4th way
       //final CountDownLatch latch = new CountDownLatch(deviceTokens.size());//Every time a task is completed (the thread does not necessarily need to be completed), the latch is reduced by 1, until all tasks are completed, the next stage of tasks can be executed Improve performance
		ApnsClient apnPush = APNSConnect.getAPNSConnect(null);
		final AtomicLong successCnt = new AtomicLong(0);//Thread safe counter
        final String token = TokenUtil.sanitizeTokenString(deviceTokens.toString().replace("[", "").replace("]", ""));//com.app.sumadhuraApp.SumNotService.voip
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.app.sumadhuraApp", payload);
        //com.sumadhura.sampleNotification
        final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
            sendNotificationFuture = apnPush.sendNotification(pushNotification);

        try {
			@SuppressWarnings("unused")
			final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
			
			sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {

                @Override
                public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
                   //When using a listener, callers should check for a failure to send a
                   //notification by checking whether the future itself was successful
                   //since an exception will not be thrown.
                    if (future.isSuccess()) {
                        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                                sendNotificationFuture.getNow();
                        
                        if (pushNotificationResponse.isAccepted()) {
                            successCnt.incrementAndGet();
                            LOGGER.info("Notification send by the APNs gateway: token -> "+pushNotificationResponse.getPushNotification().getToken()+", success -> "+ pushNotificationResponse.isAccepted());
                        } else {
                            Date invalidTime = pushNotificationResponse.getTokenInvalidationTimestamp();
                            LOGGER.error("Notification rejected by the APNs gateway: "+ pushNotificationResponse.getRejectionReason());
                            if (invalidTime != null) {
                            	LOGGER.error("\t…and the token is invalid as of "+ pushNotificationResponse.getTokenInvalidationTimestamp());
                            }
                        }
                       //Handle the push notification response as before from here.
                    } else {
                       //Something went wrong when trying to send the notification to the
                       //APNs gateway. We can find the exception that caused the failure
                       //by getting future.cause().
                        future.cause().printStackTrace();
                    }
                   // latch.countDown();
                   // semaphore.release();//Release allowed, return the occupied semaphore
                }
            });

			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(successCnt.equals(new AtomicLong(1)) && isRequiredResult) {
        	return true;
        }
		//this is working code with .p12 file old code
/*		ApnsService pushService = getApnsService();
		pushService.start();
		 sending bulk notifications 
		pushService.push(deviceTokens, payload);
*/		
		if (isRequiredResult) {
			/*Map<String, Boolean> result = getNotificationResult(pushService, deviceTokens);
			pushService.stop();*/
			return true;
		} else {
			return Boolean.TRUE;
		}
	}

/*	public Map<String, Boolean> getNotificationResult(ApnsService pushService, Collection<String> deviceTokens) {
		LOGGER.info("*** The control is inside the getNotificationResult in  IOSPushNotification ***");

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		 finding the inactive devices 
		Map<String, Date> inactiveDevices = getIosInactiveDevices(pushService);

		 finding InactiveDeviceTokens 
		Set<String> inactiveDeviceTokens = inactiveDevices.keySet();

		LOGGER.info("*** The inactive deviceTokens ***" + inactiveDeviceTokens);

		 Adding sended pushnotification devices 
		for (String deviceToken : deviceTokens) {
			if (!inactiveDeviceTokens.isEmpty()) {
				for (String inActiveDeviceToken : inactiveDeviceTokens) {
					if (deviceToken.equalsIgnoreCase(inActiveDeviceToken)) {
						LOGGER.info("***iOS Push Notification Response Code for device : ***"+deviceToken+" sent status :"+Boolean.FALSE);
						result.put(deviceToken, Boolean.FALSE);
					} else {
						LOGGER.info("***iOS Push Notification Response Code for device : ***"+deviceToken+" sent status :"+Boolean.TRUE);
						result.put(deviceToken, Boolean.TRUE);
					}
				}
			} else {
				LOGGER.info("***iOS Push Notification Response Code for device : ***"+deviceToken+" sent status :"+Boolean.TRUE);
				result.put(deviceToken, Boolean.TRUE);
			}
		}
		return result;
	}

	public Map<String, Date> getIosInactiveDevices(ApnsService pushService) {
		LOGGER.info("*** The control is inside the getIosInactiveDevices in  IOSPushNotification ***");
		return pushService.getInactiveDevices();
	}
*/
	public JSONObject dataObject(String title, String description, String message, String type, String imageUrl,
			String fileLocation, Long notificationId) {
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		alert.put("title", title);
		// alert.put("subtitle",message);
		alert.put("body", message);
		aps.put("alert", alert);
		// aps.put("badge",1);
		aps.put("sound", "default");
		aps.put("category", type);
		aps.put("typeMsg", type);
		aps.put("mutable-content", 1);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		aps.put("id", notificationId);
		payload.put("description", description);
		payload.put("urlImageString", imageUrl);
		payload.put("linkFileLoc", fileLocation);
		/* inorder to find that images are from external drive location or server location */
		if(Util.isNotEmptyObject(imageUrl)) {
			payload.put("isDriveImage", !imageUrl.contains("images/sumadhura_projects_images")?true:false);
		}else {
			payload.put("isDriveImage", false);
		}
		/* inorder to find that files are from external drive location or server location */
		if(Util.isNotEmptyObject(fileLocation)) {
			payload.put("isDriveFile", !fileLocation.contains("images/sumadhura_projects_images")?true:false);
		}else {
			payload.put("isDriveFile", false);
		}
		// payload.put("launch-image","https://i.pinimg.com/originals/78/eb/dd/78ebdd935d77099e78e2655b311866df.jpg");
		payload.put("aps", aps);
		return payload;
	}

	public JSONObject getTicketingPushNotificationPayLoad(@NonNull PushNotificationInfo pushNotificationInfo) {
		LOGGER.info("**** The control is inside the ticketingPushNotificationPayLoad ****");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		alert.put("title", pushNotificationInfo.getNotificationTitle());
		alert.put("body", pushNotificationInfo.getNotificationMessage());
		aps.put("alert", alert);
		aps.put("sound", "default");
		aps.put("ticketId", pushNotificationInfo.getTicketId());
		aps.put("statusId", pushNotificationInfo.getStatusId());
		aps.put("typeMsg", "Sumadhura Ticket Notification");
		aps.put("mutable-content", 1);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("aps", aps);
		return payload;
	}

	public JSONObject getBookingformPushNotificationPayLoad(@NonNull PushNotificationInfo pushNotificationInfo) {
		LOGGER.info("**** The control is inside the ticketingPushNotificationPayLoad ****");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		alert.put("title", pushNotificationInfo.getNotificationTitle());
		alert.put("body", pushNotificationInfo.getNotificationMessage());
		aps.put("alert", alert);
		aps.put("sound", "default");
		aps.put("typeMsg", "Sumadhura BookingForm Notification");
		aps.put("mutable-content", 1);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("aps", aps);
		return payload;
	}
	
	public JSONObject getReferralStatusPushNotificationPayLoad(@NonNull PushNotificationInfo pushNotificationInfo) {
		LOGGER.info("**** The control is inside the getReferralStatusPushNotificationPayLoad in IOSPushNotificationUtil****");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		if(Util.isNotEmptyObject(pushNotificationInfo.getReferedCustomer())) {
			alert.put("typeMsg", "Sumadhura References Notification");
        	ReferedCustomer referedCustomerObj = pushNotificationInfo.getReferedCustomer();
        	alert.put("referenceId",referedCustomerObj.getRefrenceId());
        	alert.put("referrerName",referedCustomerObj.getReferrerName());
        	alert.put("referrerEmailId",referedCustomerObj.getReferrerEmailId());
        	alert.put("mobileNo",referedCustomerObj.getMobileNo());
        	alert.put("cityName",referedCustomerObj.getCityName());
        	alert.put("stateName",referedCustomerObj.getStateName());
        	alert.put("pincode",referedCustomerObj.getPincode());
			/*  refered site */
        	alert.put("refrenceSite",referedCustomerObj.getRefrenceSite());
			/* Interested Flat */
        	alert.put("interestFlat",referedCustomerObj.getInterestFlat());
        	alert.put("comments",referedCustomerObj.getComments());
        	alert.put("referralStatusName",referedCustomerObj.getReferralStatusName());
        	alert.put("referrerImg", referedCustomerObj.getReferrerImg());
        }
		alert.put("title", pushNotificationInfo.getNotificationTitle());
		alert.put("body", pushNotificationInfo.getNotificationMessage());
		aps.put("alert", alert);
		aps.put("sound", "default");
		aps.put("mutable-content", 1);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("aps", aps);
		return payload;
	}
	
	public JSONObject getAppointmentBookingDetailsPayLoad(@NonNull PushNotificationInfo pushNotificationInfo) {
		LOGGER.info("**** The control is inside the getAppointmentBookingDetailsPayLoad in IOSPushNotificationUtil****");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		Long siteId = 0l;
		if(Util.isNotEmptyObject(pushNotificationInfo.getApptmtBookingsDetailResponse())) {
			ApptmtBookingsDetailResponse apptmtBookingsDetailResponse = pushNotificationInfo.getApptmtBookingsDetailResponse();
			siteId = apptmtBookingsDetailResponse.getSiteId();
        	JSONObject appointmentBookingObject = new JSONObject();
    		appointmentBookingObject.put("apptmtBookingsId", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtBookingsId())?apptmtBookingsDetailResponse.getApptmtBookingsId():"N/A");
    		appointmentBookingObject.put("apptmtDate", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtDate())?apptmtBookingsDetailResponse.getApptmtDate().getTime():"N/A");
    		appointmentBookingObject.put("slotTime", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getSlotTime())?apptmtBookingsDetailResponse.getSlotTime():"N/A");
    		appointmentBookingObject.put("apptmtStatusName", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtStatusName())?apptmtBookingsDetailResponse.getApptmtStatusName():"N/A");
    		appointmentBookingObject.put("assignedTypeName", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getAssignedTypeName())?apptmtBookingsDetailResponse.getAssignedTypeName():"N/A");
    		appointmentBookingObject.put("apptmtSubTypeName", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtSubTypeName())?apptmtBookingsDetailResponse.getApptmtSubTypeName():"N/A");
    		appointmentBookingObject.put("apptmtReqComments", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtReqComments())?apptmtBookingsDetailResponse.getApptmtReqComments():"N/A");
    		appointmentBookingObject.put("appointmentTime", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getAppointmentTime())?apptmtBookingsDetailResponse.getAppointmentTime():"N/A");
    		alert.put("appointmentBookingData", appointmentBookingObject);
    		alert.put("typeMsg", pushNotificationInfo.getTypeMsg());
        }
        /* Setting Push Notification Icon */
        if(siteId!=null && siteId.equals(131l) && Util.isNotEmptyObject(pushNotificationInfo.getFinancialPushNotification())) {
        	alert.put("icon",new ResponceCodesUtil().getApplicationProperties().get("ASPIRE_LOGO1").toString());
        }else {
        	alert.put("icon",new ResponceCodesUtil().getApplicationProperties().get("SUMADHURA_LOGO").toString());
		}
		alert.put("title", pushNotificationInfo.getNotificationTitle());
		alert.put("body", pushNotificationInfo.getNotificationMessage());
		aps.put("alert", alert);
		aps.put("sound", "default");
		aps.put("mutable-content", 1);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("aps", aps);
		LOGGER.info("*** Push Notification PayLoad ***"+payload.toString());
		return payload;
	}
	
	public Object getFinancialPushNotificationPayLoad(PushNotificationInfo pushNotificationInfo) {
		LOGGER.info("**** The control is inside the getReferralStatusPushNotificationPayLoad in IOSPushNotificationUtil****");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();
		if (Util.isNotEmptyObject(pushNotificationInfo.getFinancialPushNotification())) {
			EmployeeFinancialPushNotificationInfo financialPushNotification = pushNotificationInfo.getFinancialPushNotification();
			//info.put("typeMsg", financialPushNotification.getTypeMsg());
			if (Util.isNotEmptyObject(financialPushNotification.getTypeMsg())) {
				JSONObject financialObject = new JSONObject();
				//JSONObject additionalData = new JSONObject();
				/*ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(financialPushNotification);
				System.out.println(jsonString);
				JsonNode jsonNode = mapper.readTree(jsonString);
				jsonNode.get(jsonString);
				financialObject = mergeJSONObjects(financialObject,new JSONObject(jsonNode.toString()));*/
				//additionalData.put("typeMsg", financialPushNotification.getTypeMsg());
				alert.put("typeMsg", financialPushNotification.getTypeMsg());
				if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Interest Receipt")) {
					//alert.put("typeMsg", "Sumadhura Modification Receipt");
					 //doing this is temporary 
				 }
				if(financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Demand Note") || "Interest Letter".equalsIgnoreCase(financialPushNotification.getTypeMsg())) {
					//info.remove("");
					financialObject.put("demandNoteName",Util.isEmptyObject(financialPushNotification.getDemandNoteName())?"N/A":financialPushNotification.getDemandNoteName());
					financialObject.put("demandNoteUrl",Util.isEmptyObject(financialPushNotification.getDemandNoteUrl())?"N/A":financialPushNotification.getDemandNoteUrl());
					financialObject.put("demandNoteCreatedDate",Util.isEmptyObject(financialPushNotification.getDemandNoteCreatedDate())?"N/A":financialPushNotification.getDemandNoteCreatedDate());
					//additionalData.put("demandNote", financialObject);
					alert.put("demandNote", financialObject);
				} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Interest Waiver")) {
					//EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo = financialPushNotification.getLegalAndModifiNotificationInfo();
					
					ObjectMapper mapper = new ObjectMapper();
					String jsonString = "";
					try {
						jsonString = mapper.writeValueAsString(financialPushNotification.getFinancial());
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					JSONObject jsonObject = new JSONObject(jsonString);
					financialObject.append("mileStones", jsonObject);
					
					financialObject.put("metadataName","Interest_Waiver");

					/* "projectMilestoneId": 1,
			            "milStoneNo": 1,
			            "mileStoneName": "On Booking",
			            "totalPenalityAmount": " 2,371.63",
			            "totalPendingPenaltyAmount": null,
			            "totalPenalityPaidAmount": " 1,371.63",
			            "interestWaiverAdjAmount": " 1,000.00",
			            "dueDate": 1614643200000,*/
					
					alert.put("interest_Waiver", financialObject);
				} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Legal Invoice")) {
					EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo = financialPushNotification.getLegalAndModifiNotificationInfo();
					financialObject.put("type",Util.isEmptyObject(legalAndModifiNotificationInfo.getType())?"N/A":legalAndModifiNotificationInfo.getType());
					financialObject.put("finBokAccInvoiceNo",Util.isEmptyObject(legalAndModifiNotificationInfo.getFinBokAccInvoiceNo())?"N/A":legalAndModifiNotificationInfo.getFinBokAccInvoiceNo());
					financialObject.put("metadataName","LEGAL_COST");
					financialObject.put("documentName",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentName())?"N/A":legalAndModifiNotificationInfo.getDocumentName());
					financialObject.put("documentLocation",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentLocation())?"N/A":legalAndModifiNotificationInfo.getDocumentLocation());
					//additionalData.put("demandNote", financialObject);
					alert.put("legalInvoice", financialObject);
				} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Modification Invoice")) {
					EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo = financialPushNotification.getLegalAndModifiNotificationInfo();
					financialObject.put("type",Util.isEmptyObject(legalAndModifiNotificationInfo.getType())?"N/A":legalAndModifiNotificationInfo.getType());
					financialObject.put("finBokAccInvoiceNo",Util.isEmptyObject(legalAndModifiNotificationInfo.getFinBokAccInvoiceNo())?"N/A":legalAndModifiNotificationInfo.getFinBokAccInvoiceNo());
					financialObject.put("metadataName","MODIFICATION_COST");
					financialObject.put("documentName",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentName())?"N/A":legalAndModifiNotificationInfo.getDocumentName());
					financialObject.put("documentLocation",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentLocation())?"N/A":legalAndModifiNotificationInfo.getDocumentLocation());
					//additionalData.put("demandNote", financialObject);
					alert.put("modificationInvoice", financialObject);
				} else if(financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Receipt")) {
					financialObject.put("amount",Util.isEmptyObject(financialPushNotification.getAmount())?"0.00":financialPushNotification.getAmount());
					financialObject.put("status",Util.isEmptyObject(financialPushNotification.getStatus())?"N/A":financialPushNotification.getStatus());
					financialObject.put("paymentMode",Util.isEmptyObject(financialPushNotification.getPaymentMode())?"N/A":financialPushNotification.getPaymentMode());
					financialObject.put("noOfTerms",Util.isEmptyObject(financialPushNotification.getNoOfTerms())?"N/A":financialPushNotification.getNoOfTerms());
					financialObject.put("bankName",Util.isEmptyObject(financialPushNotification.getBankName())?"N/A":financialPushNotification.getBankName());
					financialObject.put("paySchedId","N/A");
					financialObject.put("receiptNumber",Util.isEmptyObject(financialPushNotification.getReceiptNumber())?"N/A":financialPushNotification.getReceiptNumber());
					financialObject.put("referenceNumber",Util.isEmptyObject(financialPushNotification.getReferenceNumber())?"N/A":financialPushNotification.getReferenceNumber());
					financialObject.put("createdDate",Util.isEmptyObject(financialPushNotification.getCreatedDate())?"N/A":financialPushNotification.getCreatedDate().getTime());
					financialObject.put("clearenceDate",Util.isEmptyObject(financialPushNotification.getClearenceDate())?"N/A":financialPushNotification.getClearenceDate().getTime());
					financialObject.put("uploadedDocs",Util.isEmptyObject(financialPushNotification.getUploadedDocs())?"N/A":financialPushNotification.getUploadedDocs());
					financialObject.put("mileStoneName",Util.isEmptyObject(financialPushNotification.getMileStoneName())?"N/A":financialPushNotification.getMileStoneName());
					financialObject.put("invoiceDocument",Util.isEmptyObject(financialPushNotification.getInvoiceDocument())?"N/A":financialPushNotification.getInvoiceDocument());
					financialObject.put("paymentDetailsId", Util.isNotEmptyObject(financialPushNotification.getPaymentDetailsId())?"N/A":financialPushNotification.getPaymentDetailsId());
					financialObject.put("transactionDate", Util.isNotEmptyObject(financialPushNotification.getTransactionDate())?"N/A":financialPushNotification.getTransactionDate());
					financialObject.put("chequeDepositedDate", Util.isNotEmptyObject(financialPushNotification.getChequeDepositedDate())?"N/A":financialPushNotification.getChequeDepositedDate());
					financialObject.put("statusId", "N/A");
					financialObject.put("demandNote", "N/A");
					//additionalData.put("paymentDetailsList", financialObject);
					alert.put("paymentDetailsList", financialObject);
				} else if(financialPushNotification.getTypeMsg().equalsIgnoreCase("Bounce Transaction")) {
					financialObject.put("amount",Util.isEmptyObject(financialPushNotification.getAmount())?"0.00":financialPushNotification.getAmount());
					financialObject.put("status",Util.isEmptyObject(financialPushNotification.getStatus())?"N/A":financialPushNotification.getStatus());
					financialObject.put("paymentMode",Util.isEmptyObject(financialPushNotification.getPaymentMode())?"N/A":financialPushNotification.getPaymentMode());
					financialObject.put("noOfTerms",Util.isEmptyObject(financialPushNotification.getNoOfTerms())?"N/A":financialPushNotification.getNoOfTerms());
					financialObject.put("bankName",Util.isEmptyObject(financialPushNotification.getBankName())?"N/A":financialPushNotification.getBankName());
					financialObject.put("paySchedId","N/A");
					financialObject.put("receiptNumber",Util.isEmptyObject(financialPushNotification.getReceiptNumber())?"N/A":financialPushNotification.getReceiptNumber());
					financialObject.put("referenceNumber",Util.isEmptyObject(financialPushNotification.getReferenceNumber())?"N/A":financialPushNotification.getReferenceNumber());
					financialObject.put("createdDate",Util.isEmptyObject(financialPushNotification.getCreatedDate())?"N/A":financialPushNotification.getCreatedDate().getTime());
					financialObject.put("clearenceDate",Util.isEmptyObject(financialPushNotification.getClearenceDate())?"N/A":financialPushNotification.getClearenceDate().getTime());
					financialObject.put("uploadedDocs",Util.isEmptyObject(financialPushNotification.getUploadedDocs())?"N/A":financialPushNotification.getUploadedDocs());
					financialObject.put("mileStoneName",Util.isEmptyObject(financialPushNotification.getMileStoneName())?"N/A":financialPushNotification.getMileStoneName());
					financialObject.put("invoiceDocument",Util.isEmptyObject(financialPushNotification.getInvoiceDocument())?"N/A":financialPushNotification.getInvoiceDocument());
					financialObject.put("paymentDetailsId", Util.isEmptyObject(financialPushNotification.getPaymentDetailsId())?"N/A":financialPushNotification.getPaymentDetailsId());
					financialObject.put("transactionDate", Util.isEmptyObject(financialPushNotification.getTransactionDate())?"N/A":financialPushNotification.getTransactionDate());
					financialObject.put("chequeDepositedDate", Util.isEmptyObject(financialPushNotification.getChequeDepositedDate())?"N/A":financialPushNotification.getChequeDepositedDate());
					
					financialObject.put("chequeBounceDate", Util.isEmptyObject(financialPushNotification.getChequeBounceDate())?"N/A":financialPushNotification.getChequeBounceDate());
					financialObject.put("chequeBounceComment", Util.isEmptyObject(financialPushNotification.getChequeBounceComment())?"N/A":financialPushNotification.getChequeBounceComment());
					
					financialObject.put("statusId", "N/A");
					financialObject.put("demandNote", "N/A");
					//additionalData.put("paymentDetailsList", financialObject);
					alert.put("paymentDetailsList", financialObject);
				} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Modification Receipt")) {
					financialObject.put("amount",Util.isEmptyObject(financialPushNotification.getAmount())?"0.00":financialPushNotification.getAmount());
					financialObject.put("status",Util.isEmptyObject(financialPushNotification.getStatus())?"N/A":financialPushNotification.getStatus());
					financialObject.put("paymentMode",Util.isEmptyObject(financialPushNotification.getPaymentMode())?"N/A":financialPushNotification.getPaymentMode());
					financialObject.put("noOfTerms",Util.isEmptyObject(financialPushNotification.getNoOfTerms())?"N/A":financialPushNotification.getNoOfTerms());
					financialObject.put("bankName",Util.isEmptyObject(financialPushNotification.getBankName())?"N/A":financialPushNotification.getBankName());
					financialObject.put("paySchedId","N/A");
					financialObject.put("receiptNumber",Util.isEmptyObject(financialPushNotification.getReceiptNumber())?"N/A":financialPushNotification.getReceiptNumber());
					financialObject.put("referenceNumber",Util.isEmptyObject(financialPushNotification.getReferenceNumber())?"N/A":financialPushNotification.getReferenceNumber());
					financialObject.put("createdDate",Util.isEmptyObject(financialPushNotification.getCreatedDate())?"N/A":financialPushNotification.getCreatedDate().getTime());
					financialObject.put("clearenceDate",Util.isEmptyObject(financialPushNotification.getClearenceDate())?"N/A":financialPushNotification.getClearenceDate().getTime());
					financialObject.put("uploadedDocs",Util.isEmptyObject(financialPushNotification.getUploadedDocs())?"N/A":financialPushNotification.getUploadedDocs());
					financialObject.put("mileStoneName",Util.isEmptyObject(financialPushNotification.getMileStoneName())?"N/A":financialPushNotification.getMileStoneName());
					financialObject.put("invoiceDocument",Util.isEmptyObject(financialPushNotification.getInvoiceDocument())?"N/A":financialPushNotification.getInvoiceDocument());
					financialObject.put("paymentDetailsId", Util.isNotEmptyObject(financialPushNotification.getPaymentDetailsId())?"N/A":financialPushNotification.getPaymentDetailsId());
					financialObject.put("statusId", "N/A");
					financialObject.put("demandNote", "N/A");
					//additionalData.put("paymentDetailsList", financialObject);
					alert.put("paymentDetailsList", financialObject);
				} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Legal Receipt")) {
					financialObject.put("amount",Util.isEmptyObject(financialPushNotification.getAmount())?"0.00":financialPushNotification.getAmount());
					financialObject.put("status",Util.isEmptyObject(financialPushNotification.getStatus())?"N/A":financialPushNotification.getStatus());
					financialObject.put("paymentMode",Util.isEmptyObject(financialPushNotification.getPaymentMode())?"N/A":financialPushNotification.getPaymentMode());
					financialObject.put("noOfTerms",Util.isEmptyObject(financialPushNotification.getNoOfTerms())?"N/A":financialPushNotification.getNoOfTerms());
					financialObject.put("bankName",Util.isEmptyObject(financialPushNotification.getBankName())?"N/A":financialPushNotification.getBankName());
					financialObject.put("paySchedId","N/A");
					financialObject.put("receiptNumber",Util.isEmptyObject(financialPushNotification.getReceiptNumber())?"N/A":financialPushNotification.getReceiptNumber());
					financialObject.put("referenceNumber",Util.isEmptyObject(financialPushNotification.getReferenceNumber())?"N/A":financialPushNotification.getReferenceNumber());
					financialObject.put("createdDate",Util.isEmptyObject(financialPushNotification.getCreatedDate())?"N/A":financialPushNotification.getCreatedDate().getTime());
					financialObject.put("clearenceDate",Util.isEmptyObject(financialPushNotification.getClearenceDate())?"N/A":financialPushNotification.getClearenceDate().getTime());
					financialObject.put("uploadedDocs",Util.isEmptyObject(financialPushNotification.getUploadedDocs())?"N/A":financialPushNotification.getUploadedDocs());
					financialObject.put("mileStoneName",Util.isEmptyObject(financialPushNotification.getMileStoneName())?"N/A":financialPushNotification.getMileStoneName());
					financialObject.put("invoiceDocument",Util.isEmptyObject(financialPushNotification.getInvoiceDocument())?"N/A":financialPushNotification.getInvoiceDocument());
					financialObject.put("paymentDetailsId", Util.isNotEmptyObject(financialPushNotification.getPaymentDetailsId())?"N/A":financialPushNotification.getPaymentDetailsId());
					financialObject.put("statusId", "N/A");
					financialObject.put("demandNote", "N/A");
					//additionalData.put("paymentDetailsList", financialObject);
					alert.put("paymentDetailsList", financialObject);
				} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Interest Receipt")) {
					financialObject.put("amount",Util.isEmptyObject(financialPushNotification.getAmount())?"0.00":financialPushNotification.getAmount());
					financialObject.put("status",Util.isEmptyObject(financialPushNotification.getStatus())?"N/A":financialPushNotification.getStatus());
					financialObject.put("paymentMode",Util.isEmptyObject(financialPushNotification.getPaymentMode())?"N/A":financialPushNotification.getPaymentMode());
					financialObject.put("noOfTerms",Util.isEmptyObject(financialPushNotification.getNoOfTerms())?"N/A":financialPushNotification.getNoOfTerms());
					financialObject.put("bankName",Util.isEmptyObject(financialPushNotification.getBankName())?"N/A":financialPushNotification.getBankName());
					financialObject.put("paySchedId","N/A");
					financialObject.put("receiptNumber",Util.isEmptyObject(financialPushNotification.getReceiptNumber())?"N/A":financialPushNotification.getReceiptNumber());
					financialObject.put("referenceNumber",Util.isEmptyObject(financialPushNotification.getReferenceNumber())?"N/A":financialPushNotification.getReferenceNumber());
					financialObject.put("createdDate",Util.isEmptyObject(financialPushNotification.getCreatedDate())?"N/A":financialPushNotification.getCreatedDate().getTime());
					financialObject.put("clearenceDate",Util.isEmptyObject(financialPushNotification.getClearenceDate())?"N/A":financialPushNotification.getClearenceDate().getTime());
					financialObject.put("uploadedDocs",Util.isEmptyObject(financialPushNotification.getUploadedDocs())?"N/A":financialPushNotification.getUploadedDocs());
					financialObject.put("mileStoneName",Util.isEmptyObject(financialPushNotification.getMileStoneName())?"N/A":financialPushNotification.getMileStoneName());
					financialObject.put("invoiceDocument",Util.isEmptyObject(financialPushNotification.getInvoiceDocument())?"N/A":financialPushNotification.getInvoiceDocument());
					financialObject.put("paymentDetailsId", Util.isNotEmptyObject(financialPushNotification.getPaymentDetailsId())?"N/A":financialPushNotification.getPaymentDetailsId());
					financialObject.put("statusId", "N/A");
					financialObject.put("demandNote", "N/A");
					//additionalData.put("paymentDetailsList", financialObject);
					alert.put("paymentDetailsList", financialObject);
				}
				
				//alert.put("additionalData", additionalData);
			}
		}
		alert.put("title", pushNotificationInfo.getNotificationTitle());
		alert.put("body", pushNotificationInfo.getNotificationMessage());
		aps.put("alert", alert);
		aps.put("sound", "default");
		aps.put("mutable-content", 1);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("aps", aps);
		return payload;
	}

	public static void checkPushNotification(EmployeeFinancialTransactionRequest financialTransactionRequest, String host) {

		IOSPushNotificationUtil object = new IOSPushNotificationUtil();
		
		List<String> deviceTokens = new ArrayList<String>();
		deviceTokens.add(financialTransactionRequest.getDeviceToken());
		//deviceTokens.add("56d622f81ad024d276fbb9158635a9180bbe790ab340366a34b9d14b7ab697ef");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();

		alert.put("title", "Sumadhura Ticket SANDBOX Notification Testing");
		alert.put("subtitle", "Subtitle Sumadhura Ticket  Notification");
		alert.put("body", "Body Sumadhura Ticket has send successfully!");
		aps.put("alert", alert);
		aps.put("badge", 0);
		aps.put("sound", "default");
		aps.put("category", "Sumadhura");
		aps.put("mutable-content", 1);
		aps.put("foreground", false);
		aps.put("coldstart", true);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("urlImageString", "http://129.154.74.18:8090/images/sumadhura_projects_images/notification/ee.png");
		payload.put("linkFileLoc", "https://i.pinimg.com/originals/78/eb/dd/78ebdd935d77099e78e2655b311866df.jpg");
		payload.put("launch-image", "https://i.pinimg.com/originals/78/eb/dd/78ebdd935d77099e78e2655b311866df.jpg");
		payload.put("aps", aps);

		System.out.println(payload.toString());

		try {
			for (int i = 0; i < 2; i++)
				LOGGER.info(object.sendIosPushNotification(deviceTokens, payload.toString(), true));
		} catch (Exception ex) {
			LOGGER.error("*** The Exception information is ***" + ex);
		}

	
	}

	public static void main(String... args) {

		IOSPushNotificationUtil object = new IOSPushNotificationUtil();
		
		List<String> deviceTokens = new ArrayList<String>();
		deviceTokens.add("ca7fe90b32d61e39b76db5a36b156b43b5a6fb2251c419ae2b49d4647ccc3b85");
		//deviceTokens.add("9C245049DF3202C3CE2707F8B3506538DD174CBEFC70B072271DB7888EF410FB");
		JSONObject payload = new JSONObject();
		JSONObject aps = new JSONObject();
		JSONObject alert = new JSONObject();

		alert.put("title", "Sumadhura Ticket SANDBOX Notification Testing");
		alert.put("subtitle", "Subtitle Sumadhura Ticket  Notification");
		alert.put("body", "Body Sumadhura Ticket has send successfully!");
		aps.put("alert", alert);
		aps.put("badge", 0);
		aps.put("sound", "default");
		aps.put("category", "Sumadhura");
		aps.put("mutable-content", 1);
		aps.put("foreground", false);
		aps.put("coldstart", true);
		aps.put("critical", 1);
		aps.put("volume", 0.0);
		payload.put("urlImageString", "http://129.154.74.18:8090/images/sumadhura_projects_images/notification/ee.png");
		payload.put("linkFileLoc", "https://i.pinimg.com/originals/78/eb/dd/78ebdd935d77099e78e2655b311866df.jpg");
		payload.put("launch-image", "https://i.pinimg.com/originals/78/eb/dd/78ebdd935d77099e78e2655b311866df.jpg");
		payload.put("aps", aps);

		System.out.println(payload.toString());

		try {
			for (int i = 0; i < 3; i++)
				LOGGER.info(object.sendIosPushNotification(deviceTokens, payload.toString(), true));
		} catch (Exception ex) {
			LOGGER.error("*** The Exception information is ***" + ex);
		}

	}

}
