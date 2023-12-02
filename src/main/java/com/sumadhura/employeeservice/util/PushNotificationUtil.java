package com.sumadhura.employeeservice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumadhura.employeeservice.dto.ApptmtBookingsDetailResponse;
import com.sumadhura.employeeservice.dto.EmpFinPushLegalAndModifiNotificationInfo;
import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.NotificationRequest;
import com.sumadhura.employeeservice.dto.NotificationResponse;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.service.dto.PushNotificationInfo;

/**
 * 
 * @author Venkat_Koniki
 * @since 22.04.2019
 * @time 10:20AM
 */
public class PushNotificationUtil {
	private Logger LOGGER = Logger.getLogger(PushNotificationUtil.class);
	public final static String AUTH_KEY_FCM = "AAAAKX3WKBc:APA91bGKUiuO3h-I13xWgMdFc-08A_VxgnqqsIq8IdwO2sJ4nMbsSaKAW7HmQeWP8gmJWNiGa8bmjNu1HZADKQyOfKOyoU_K-eqeRuvi-NGXajb3pXc_i9wOqqWfLT0D9JWyFNEPiwBd";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	public final static String sumadhuraLogo = "http://106.51.38.64:9999/images/sumadhura_projects_images/sumadhuralogo.jpg";


	public void pushFCMNotification(String userDeviceIdKey, String title, String message) throws Exception {
		String authKey = AUTH_KEY_FCM; // You FCM AUTH key
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		json.put("to", userDeviceIdKey.trim());


		JSONObject info = new JSONObject();
		info.put("title", "Delighted on bringing in MS Dhoni on board as their brand ambassador, Madhusudhan G., Chairman and Managing Director, Sumadhura Group said, �We are honoured to have Dhoni, an eminent cricketer of international stature, join the Sumadhura Group family.");
		info.put("image","https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		info.put("priority","high");
		info.put("icon", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		info.put("style", "picture");
		info.put("picture", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		info.put("color", "green");
		info.put("type", "message");
		info.put( "sound", "Tri-tone");

		json.put("data", info);
		System.out.println(json.toString());

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		System.out.println(conn.getResponseCode());

		BufferedReader reader = new BufferedReader(new 
				InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + 
				reader.readLine());		    
	}
//
	public void pushFCMNotification(PushNotificationInfo pushNotificationInfo) throws Exception {

		String authKey = AUTH_KEY_FCM; 
		String FMCurl = API_URL_FCM;
		Long siteId = 0l;
		Random random = new Random();
		int randomNumber = random.nextInt(10000);
		String notId = "10231"+randomNumber;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		json.put("to", pushNotificationInfo.getDeviceToken().trim());


		JSONObject info = new JSONObject();
		//info.put("id",++i);
		info.put("title",pushNotificationInfo.getNotificationTitle());
		info.put("message",pushNotificationInfo.getNotificationMessage());
		//info.put("description", pushNotificationInfo.getNotificationTitle());
		info.put("priority",1);
		
		/*  for Ticket redirection purpouse */
        if(Util.isNotEmptyObject(pushNotificationInfo.getTicketId())) {
        	info.put("typeMsg", "Sumadhura Ticket Notification");
        	info.put("ticketId",Util.isNotEmptyObject(pushNotificationInfo.getTicketId())?pushNotificationInfo.getTicketId():0l);
        }
        if(Util.isNotEmptyObject(pushNotificationInfo.getStatusId())) {
        	info.put("statusId",pushNotificationInfo.getStatusId());
        }
        
        /* for refer friend notification */
        if(Util.isNotEmptyObject(pushNotificationInfo.getReferedCustomer())) {
        	info.put("typeMsg", "Sumadhura References Notification");
        	ReferedCustomer referedCustomerObj = pushNotificationInfo.getReferedCustomer();
        	info.put("referenceId",referedCustomerObj.getRefrenceId());
        	info.put("referrerName",referedCustomerObj.getReferrerName());
        	info.put("referrerEmailId",referedCustomerObj.getReferrerEmailId());
        	info.put("mobileNo",referedCustomerObj.getMobileNo());
        	info.put("cityName",referedCustomerObj.getCityName());
        	info.put("stateName",referedCustomerObj.getStateName());
        	info.put("pincode",referedCustomerObj.getPincode());
			/*  refered site */
			info.put("refrenceSite",referedCustomerObj.getRefrenceSite());
			/* Interested Flat */
			info.put("interestFlat",referedCustomerObj.getInterestFlat());
        	info.put("comments",referedCustomerObj.getComments());
        	info.put("referralStatusName",referedCustomerObj.getReferralStatusName());
        	info.put("referrerImg", referedCustomerObj.getReferrerImg());
        }
        
        if (Util.isNotEmptyObject(pushNotificationInfo.getFinancialPushNotification())) {
        	siteId = pushNotificationInfo.getFinancialPushNotification().getSiteId();
        	constructFinancialPushNotificationObject(pushNotificationInfo,info);
        }
        
        /* Setting Appointment Booking Details in Push Notification Object */
        if(Util.isNotEmptyObject(pushNotificationInfo.getApptmtBookingsDetailResponse())) {
        	siteId = pushNotificationInfo.getApptmtBookingsDetailResponse().getSiteId();
        	getAppointmentBookingDetailsPayLoad(pushNotificationInfo, info);
        }
        
		//info.put("icon", pushNotificationInfo.getNotificationIcon());
        if(siteId!=null && siteId.equals(131l) && Util.isNotEmptyObject(pushNotificationInfo.getFinancialPushNotification())) {
        	info.put("image",new ResponceCodesUtil().getApplicationProperties().get("ASPIRE_LOGO1").toString());
        }else {
        	info.put("image",new ResponceCodesUtil().getApplicationProperties().get("SUMADHURA_LOGO_HTTP").toString());
		}
	   // info.put("style", pushNotificationInfo.getNotificationStyle());
	   // info.put("picture", pushNotificationInfo.getNotificationImage());
		info.put("color", "red");
		info.put("type", "message");
		info.put( "sound", "default");
		info.put("notId", notId);
		//info.put("foreground", true);
		//info.put("coldstart", true);
		//info.put("forceShow", true);
		//info.put("forceStart", true);
		//info.put("delayWhileIdle", true);
		//info.put("android_channel_id","rn-push-notification-channel");
		//info.put("high_priority", "high");
		//info.put("show_in_foreground",true);
		//info.put("priority", "high");
		
		//json.put("time_to_live", 86400);
		//json.put("collapse_key", "new_message");
		//json.put("delay_while_idle", false);
		//json.put("priority", "high");
		//json.put("content_available",true);

		json.put("data", info);
		//System.out.println(json.toString());
		LOGGER.info("*** Push Notification PayLoad ***"+json.toString());
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		//System.out.println(conn.getResponseCode());
		LOGGER.info("*** Push Notification Response Code ***"+conn.getResponseCode());
		BufferedReader reader = new BufferedReader(new 
				InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + reader.readLine());
		LOGGER.info("*** Android Notification Response : ***"+ reader.readLine());

	}
	
	
	public void pushFCMNotificationMulti(PushNotificationInfo pushNotificationInfo) throws Exception {

		String authKey = AUTH_KEY_FCM; 
		String FMCurl = API_URL_FCM;
		Long siteId = 0l;
		Random random = new Random();
		int randomNumber = random.nextInt(10000);
		String notId = "10231"+randomNumber;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");
		
		/*bvr*/
		JSONArray regId = new JSONArray();
		List<String> deviceTokens=pushNotificationInfo.getDeviceTokens();
		 for (int i = 0; i < deviceTokens.size(); i++) {
             regId.put(deviceTokens.get(i).trim());
         }
		 JSONObject json = new JSONObject();
		 json.put("registration_ids", regId);
		 /*bvr*/
		 
		 
//		 JSONObject json = new JSONObject();
//		json.put("to", pushNotificationInfo.getDeviceToken().trim());


		JSONObject info = new JSONObject();
		//info.put("id",++i);
		info.put("title",pushNotificationInfo.getNotificationTitle());
		info.put("message",pushNotificationInfo.getNotificationMessage());
		//info.put("description", pushNotificationInfo.getNotificationTitle());
		info.put("priority",1);
		
		/*  for Ticket redirection purpouse */
        if(Util.isNotEmptyObject(pushNotificationInfo.getTicketId())) {
        	info.put("typeMsg", "Sumadhura Ticket Notification");
        	info.put("ticketId",Util.isNotEmptyObject(pushNotificationInfo.getTicketId())?pushNotificationInfo.getTicketId():0l);
        }
        if(Util.isNotEmptyObject(pushNotificationInfo.getStatusId())) {
        	info.put("statusId",pushNotificationInfo.getStatusId());
        }
        
        /* for refer friend notification */
        if(Util.isNotEmptyObject(pushNotificationInfo.getReferedCustomer())) {
        	info.put("typeMsg", "Sumadhura References Notification");
        	ReferedCustomer referedCustomerObj = pushNotificationInfo.getReferedCustomer();
        	info.put("referenceId",referedCustomerObj.getRefrenceId());
        	info.put("referrerName",referedCustomerObj.getReferrerName());
        	info.put("referrerEmailId",referedCustomerObj.getReferrerEmailId());
        	info.put("mobileNo",referedCustomerObj.getMobileNo());
        	info.put("cityName",referedCustomerObj.getCityName());
        	info.put("stateName",referedCustomerObj.getStateName());
        	info.put("pincode",referedCustomerObj.getPincode());
			/*  refered site */
			info.put("refrenceSite",referedCustomerObj.getRefrenceSite());
			/* Interested Flat */
			info.put("interestFlat",referedCustomerObj.getInterestFlat());
        	info.put("comments",referedCustomerObj.getComments());
        	info.put("referralStatusName",referedCustomerObj.getReferralStatusName());
        	info.put("referrerImg", referedCustomerObj.getReferrerImg());
        }
        
        if (Util.isNotEmptyObject(pushNotificationInfo.getFinancialPushNotification())) {
        	siteId = pushNotificationInfo.getFinancialPushNotification().getSiteId();
        	constructFinancialPushNotificationObject(pushNotificationInfo,info);
        }
        
        /* Setting Appointment Booking Details in Push Notification Object */
        if(Util.isNotEmptyObject(pushNotificationInfo.getApptmtBookingsDetailResponse())) {
        	siteId = pushNotificationInfo.getApptmtBookingsDetailResponse().getSiteId();
        	getAppointmentBookingDetailsPayLoad(pushNotificationInfo, info);
        }
        
		//info.put("icon", pushNotificationInfo.getNotificationIcon());
        if(siteId!=null && siteId.equals(131l) && Util.isNotEmptyObject(pushNotificationInfo.getFinancialPushNotification())) {
        	info.put("image",new ResponceCodesUtil().getApplicationProperties().get("ASPIRE_LOGO1").toString());
        }else {
        	info.put("image",new ResponceCodesUtil().getApplicationProperties().get("SUMADHURA_LOGO_HTTP").toString());
		}
	   // info.put("style", pushNotificationInfo.getNotificationStyle());
	   // info.put("picture", pushNotificationInfo.getNotificationImage());
		info.put("color", "red");
		info.put("type", "message");
		info.put( "sound", "default");
		info.put("notId", notId);
		//info.put("foreground", true);
		//info.put("coldstart", true);
		//info.put("forceShow", true);
		//info.put("forceStart", true);
		//info.put("delayWhileIdle", true);
		//info.put("android_channel_id","rn-push-notification-channel");
		//info.put("high_priority", "high");
		//info.put("show_in_foreground",true);
		//info.put("priority", "high");
		
		//json.put("time_to_live", 86400);
		//json.put("collapse_key", "new_message");
		//json.put("delay_while_idle", false);
		//json.put("priority", "high");
		//json.put("content_available",true);

		json.put("data", info);
		//System.out.println(json.toString());
		LOGGER.info("*** Push Notification PayLoad ***"+json.toString());
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		//System.out.println(conn.getResponseCode());
		LOGGER.info("*** Push Notification Response Code ***"+conn.getResponseCode());
		BufferedReader reader = new BufferedReader(new 
				InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + reader.readLine());
		LOGGER.info("*** Android Notification Response : ***"+ reader.readLine());

	}
	private void getAppointmentBookingDetailsPayLoad(PushNotificationInfo pushNotificationInfo, JSONObject info) {
		LOGGER.info("*** The control is inside the getAppointmentBookingDetailsPayLoad in  PushNotificationUtil ***");
		ApptmtBookingsDetailResponse apptmtBookingsDetailResponse = pushNotificationInfo.getApptmtBookingsDetailResponse();
		info.put("typeMsg", pushNotificationInfo.getTypeMsg());
		JSONObject appointmentBookingObject = new JSONObject();
		appointmentBookingObject.put("apptmtBookingsId", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtBookingsId())?apptmtBookingsDetailResponse.getApptmtBookingsId():"N/A");
		appointmentBookingObject.put("apptmtDate", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtDate())?apptmtBookingsDetailResponse.getApptmtDate().getTime():"N/A");
		appointmentBookingObject.put("slotTime", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getSlotTime())?apptmtBookingsDetailResponse.getSlotTime():"N/A");
		appointmentBookingObject.put("apptmtStatusName", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtStatusName())?apptmtBookingsDetailResponse.getApptmtStatusName():"N/A");
		appointmentBookingObject.put("assignedTypeName", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getAssignedTypeName())?apptmtBookingsDetailResponse.getAssignedTypeName():"N/A");
		appointmentBookingObject.put("apptmtSubTypeName", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtSubTypeName())?apptmtBookingsDetailResponse.getApptmtSubTypeName():"N/A");
		appointmentBookingObject.put("apptmtReqComments", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getApptmtReqComments())?apptmtBookingsDetailResponse.getApptmtReqComments():"N/A");
		appointmentBookingObject.put("appointmentTime", Util.isNotEmptyObject(apptmtBookingsDetailResponse.getAppointmentTime())?apptmtBookingsDetailResponse.getAppointmentTime():"N/A");
		info.put("appointmentBookingData", appointmentBookingObject);
	}

	private void constructFinancialPushNotificationObject(PushNotificationInfo pushNotificationInfo, JSONObject json) {
		LOGGER.info("***** Control inside the PushNotificationUtil.constructFinancialPushNotificationObject() *****");
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
			json.put("typeMsg", financialPushNotification.getTypeMsg());
			 if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Interest Receipt")) {
				 //json.put("typeMsg", "Sumadhura Modification Receipt");
				 
				 //doing this is temporary, as push notification was not working for interest receipt
			 }
			
			if(financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Demand Note") || "Interest Letter".equalsIgnoreCase(financialPushNotification.getTypeMsg()) ) {
				//info.remove("");
				financialObject.put("demandNoteName",Util.isEmptyObject(financialPushNotification.getDemandNoteName())?"N/A":financialPushNotification.getDemandNoteName());
				financialObject.put("demandNoteUrl",Util.isEmptyObject(financialPushNotification.getDemandNoteUrl())?"N/A":financialPushNotification.getDemandNoteUrl());
				financialObject.put("demandNoteCreatedDate",Util.isEmptyObject(financialPushNotification.getDemandNoteCreatedDate())?"N/A":financialPushNotification.getDemandNoteCreatedDate());
				//additionalData.put("demandNote", financialObject);
				json.put("demandNote", financialObject);
			} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Interest Waiver")) {
				//EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo = financialPushNotification.getLegalAndModifiNotificationInfo();
				
				//JSONObject mergedJSON = new JSONObject();
				//mergedJSON.j(financialPushNotification.getFinancial());
				
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
				//financialObject.put("mileStones", json1);
				financialObject.put("metadataName","Interest_Waiver");
				
				/*map.put("projectMilestoneId", financialProjectMileStoneResponse.getProjectMilestoneId());
				map.put("milStoneNo", financialProjectMileStoneResponse.getMileStoneNo());
				map.put("mileStoneName", financialProjectMileStoneResponse.getMilestoneName());*/
				
				/*map.put("mileStoneAmount", financialProjectMileStoneResponse.getPayAmount());
				map.put("mileStoneDate", financialProjectMileStoneResponse.getMilestoneDate());*/
				
				
				/*map.put("demandNoteDate", financialProjectMileStoneResponse.getDemandNoteDate());
				map.put("mileStonePaidAmount", financialProjectMileStoneResponse.getPaidAmount());
				map.put("milestoneAmountDue", dueAmount);
				map.put("milestoneLastReceiptDate", financialProjectMileStoneResponse.getMilestoneLastReceiptDate());*/
				
				
				/*map.put("totalPenalityAmount", penaltyAmount);
				map.put("totalPendingPenaltyAmount", pendingPenalyAmount);
				map.put("totalPenalityPaidAmount",paidPenaltyAmount);
				map.put("interestWaiverAdjAmount", interestWaiverAmount);
				map.put("dueDate", financialProjectMileStoneResponse.getMileStoneDueDate());*/

				
				//additionalData.put("demandNote", financialObject);
				json.put("interest_Waiver", financialObject);
			} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Legal Invoice")) {
				EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo = financialPushNotification.getLegalAndModifiNotificationInfo();
				financialObject.put("type",Util.isEmptyObject(legalAndModifiNotificationInfo.getType())?"N/A":legalAndModifiNotificationInfo.getType());
				financialObject.put("finBokAccInvoiceNo",Util.isEmptyObject(legalAndModifiNotificationInfo.getFinBokAccInvoiceNo())?"N/A":legalAndModifiNotificationInfo.getFinBokAccInvoiceNo());
				financialObject.put("metadataName","LEGAL_COST");
				financialObject.put("documentName",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentName())?"N/A":legalAndModifiNotificationInfo.getDocumentName());
				financialObject.put("documentLocation",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentLocation())?"N/A":legalAndModifiNotificationInfo.getDocumentLocation());
				//additionalData.put("demandNote", financialObject);
				json.put("legalInvoice", financialObject);
			} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Modification Invoice")) {
				EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo = financialPushNotification.getLegalAndModifiNotificationInfo();
				financialObject.put("type",Util.isEmptyObject(legalAndModifiNotificationInfo.getType())?"N/A":legalAndModifiNotificationInfo.getType());
				financialObject.put("finBokAccInvoiceNo",Util.isEmptyObject(legalAndModifiNotificationInfo.getFinBokAccInvoiceNo())?"N/A":legalAndModifiNotificationInfo.getFinBokAccInvoiceNo());
				financialObject.put("metadataName","MODIFICATION_COST");
				financialObject.put("documentName",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentName())?"N/A":legalAndModifiNotificationInfo.getDocumentName());
				financialObject.put("documentLocation",Util.isEmptyObject(legalAndModifiNotificationInfo.getDocumentLocation())?"N/A":legalAndModifiNotificationInfo.getDocumentLocation());
				//additionalData.put("demandNote", financialObject);
				json.put("modificationInvoice", financialObject);
			} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Receipt")) {
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
				json.put("paymentDetailsList", financialObject);
			} else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Bounce Transaction")) {
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
				json.put("paymentDetailsList", financialObject);
			}else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Modification Receipt")) {
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
				json.put("paymentDetailsList", financialObject);
			}else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Legal Receipt")) {
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
				json.put("paymentDetailsList", financialObject);
			}else if (financialPushNotification.getTypeMsg().equalsIgnoreCase("Sumadhura Interest Receipt")) {
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
				json.put("paymentDetailsList", financialObject);
			}
			
			//json.put("additionalData", additionalData);
		}
	}
	
	public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
		JSONObject mergedJSON = new JSONObject();
		System.out.println(json2);
		try {
			if(Util.isNotEmptyObject(json1) && !json1.toString().equals("{}")) {
				mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));	
			}
			for (String crunchifyKey : JSONObject.getNames(json2)) {
				mergedJSON.put(crunchifyKey, json2.get(crunchifyKey));
			}
 
		} catch (JSONException e) {
			throw new RuntimeException("JSON Exception" + e);
		}
		return mergedJSON;
	}


	public static void main1(String ... args ) throws Exception {
		System.out.println("************************************************");
		PushNotificationUtil test = new PushNotificationUtil();
		
		for(int i = 0;i<=0;i++) {
		//String device_token = "c0LwJOHO29c:APA91bEQepomV8V8xlpJQiw8UenKGc11fonj4tEszU0I9y0BqIA-9ZTCQz6tKxkZgvYmJHj151kkMOtUijHWcu-6brTU0AyM_PRQYkCdhO2CVySFyrXnmUxgDi9B9nUYYjC-MFgSwReg";
		PushNotificationInfo pushNotificationInfo = new PushNotificationInfo();
		//pushNotificationInfo.setDeviceToken("cRPW8N-C6JA:APA91bF1qDeMeV5OGBhqBJAs86TsxzuQa6c7ZrF8iXbMvIZUL817HV-YsIAJeFV-QnVAWKstoz-C6L0-UaqvMM0yic1CBDWlj6kJDqLTpB88bHrDiSjQXgm35zedx9ba2omfwPmJb32M");		
		pushNotificationInfo.setDeviceToken("fg2NX95B1wg:APA91bG0SixfEi1oPWv05JoPvQXXMWl3OvgVrz4p1LgEI_wh-zUCBOXdrmW_hyj9Vpv3IhTGnLrPQPMwYW2ber0ofv83XaM8JaInnSXeP4ufVXawRlohFjwVsvY78WxaB_kMB2qveefO");
		pushNotificationInfo.setNotificationTitle("Sumadhura Ticket  Notification");
		pushNotificationInfo.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificationPriority("high");
		pushNotificationInfo.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificationStyle("picture");
		pushNotificationInfo.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificatioTitleColor("black");
		pushNotificationInfo.setNotificationType("message");
		pushNotificationInfo.setNotificationSound("default");
		pushNotificationInfo.setTicketId(10231l);
		test.pushFCMNotification(pushNotificationInfo);
	}
	}
	
	
	public static void main(String ... args ) throws Exception {
		System.out.println("************************************************");
		PushNotificationUtil test = new PushNotificationUtil();
		
		for(int i = 0;i<=0;i++) {
		//String device_token = "c0LwJOHO29c:APA91bEQepomV8V8xlpJQiw8UenKGc11fonj4tEszU0I9y0BqIA-9ZTCQz6tKxkZgvYmJHj151kkMOtUijHWcu-6brTU0AyM_PRQYkCdhO2CVySFyrXnmUxgDi9B9nUYYjC-MFgSwReg";
		PushNotificationInfo pushNotificationInfo = new PushNotificationInfo();
		List<String> deviceTokens= new ArrayList<>();
		//deviceTokens.add("dD1hfIiUaAg:APA91bG_efvvmtbENBPUo4mXdirZs_8h0r88KpJIwR6zEYB_hK6Ebg4B7uYkkYLclD2GCWbUVBqfedFvbq_1UYGIAYk9-wzKsSMfVVsZbSZsLA19yYXXrHX_QWeTmr-0zpNAAMeP0AV4");
		deviceTokens.add("fg2NX95B1wg:APA91bG0SixfEi1oPWv05JoPvQXXMWl3OvgVrz4p1LgEI_wh-zUCBOXdrmW_hyj9Vpv3IhTGnLrPQPMwYW2ber0ofv83XaM8JaInnSXeP4ufVXawRlohFjwVsvY78WxaB_kMB2qveefO");
		//deviceTokens.add("cE0H6Q2Ldmg:APA91bHG3XgKXAjm4ErfiCSAbEY02pHwK_fYUtNvPiKIv4ylwbkO-YoRtGYpdb5HIejE8s6vRZypxp03K0-e9YNnV5LCbMAoypcOfZJw2K7EdfZ-Ti1w_-eNyZidqMy-40F55HBrZDHH");
		//deviceTokens.add("dv8GpB99nPY:APA91bFFqbvcFKVa9qf9u4NwpmOu_0-E8Iv3dbfa18thEXOZUf2LiBvcLsqgDq9yKUCKEb2ex3O6uBmoHa3jdtzYMX94bRBtmO-P8CR5hkre6xTzYgkfqA7PoQka1z8iziAqATo2Rkog");
		//deviceTokens.add("fZmd6GOGjBQ:APA91bHJwzeZKKXzCFzKkM3R3IeJDksyY5-jfVmB4PV5p552vdl1GHMmCapnrIrG0EAY1_xIOZHjvacCncgVEIAVlTbVSz-yp2t62RR5ILgptoG16SpMpM8TvihRd-myDz8J9RdaiaV9");
		//deviceTokens.add("cE0H6Q2Ldmg:APA91bHG3XgKXAjm4ErfiCSAbEY02pHwK_fYUtNvPiKIv4ylwbkO-YoRtGYpdb5HIejE8s6vRZypxp03K0-e9YNnV5LCbMAoypcOfZJw2K7EdfZ-Ti1w_-eNyZidqMy-40F55HBrZDHH");
		//pushNotificationInfo.setDeviceToken("dD1hfIiUaAg:APA91bG_efvvmtbENBPUo4mXdirZs_8h0r88KpJIwR6zEYB_hK6Ebg4B7uYkkYLclD2GCWbUVBqfedFvbq_1UYGIAYk9-wzKsSMfVVsZbSZsLA19yYXXrHX_QWeTmr-0zpNAAMeP0AV4");		
		//pushNotificationInfo.setDeviceToken("cE0H6Q2Ldmg:APA91bHG3XgKXAjm4ErfiCSAbEY02pHwK_fYUtNvPiKIv4ylwbkO-YoRtGYpdb5HIejE8s6vRZypxp03K0-e9YNnV5LCbMAoypcOfZJw2K7EdfZ-Ti1w_-eNyZidqMy-40F55HBrZDHH");
		pushNotificationInfo.setNotificationTitle("Sumadhura Ticket  Notification");
		pushNotificationInfo.setDeviceTokens(deviceTokens);
		pushNotificationInfo.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificationPriority("high");
		pushNotificationInfo.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificationStyle("picture");
		pushNotificationInfo.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		pushNotificationInfo.setNotificatioTitleColor("black");
		pushNotificationInfo.setNotificationType("message");
		pushNotificationInfo.setNotificationSound("default");
		pushNotificationInfo.setTicketId(10231l);
		test.pushFCMNotificationMulti(pushNotificationInfo);
	}
	}

	/**
	 * TESTING 
	 * @param userDeviceIdKey
	 * @param title
	 * @param message
	 * @param image
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public NotificationResponse pushFCMNotification(List<String> userDeviceIdKey, String title, String message,String image) throws Exception {
		String authKey = AUTH_KEY_FCM; // You FCM AUTH key
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");


		JSONObject json = new JSONObject();
		json.put("registration_ids", userDeviceIdKey);


		JSONObject info = new JSONObject();
		info.put("title", title);
		info.put("body", message);
		info.put("image","https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		info.put("priority","high");
		info.put("icon", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		info.put("style", "picture");
		info.put("picture", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		info.put("color", "green");
		info.put("type", "message");
		info.put( "sound", "Tri-tone");

		json.put("data", info);
		System.out.println(json.toString());

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		System.out.println(conn.getResponseCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + "rams");

		String readLine = reader.readLine();

		ObjectMapper objMapper=new ObjectMapper();
		NotificationResponse readValue = objMapper.readValue(readLine, NotificationResponse.class);
		System.out.println(readValue);

		return readValue;		    

	}

	 public NotificationResponse pushFCMNotification(List<String> userDeviceIdKey, String title, String message,String description,String type,String imageUrl,String filePath,Long id) throws Exception {
		String authKey = AUTH_KEY_FCM;
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");


		JSONObject json = new JSONObject();
		if(userDeviceIdKey!=null && userDeviceIdKey.size()==1){
			json.put("to", userDeviceIdKey.get(0).trim());
		} else {
			json.put("registration_ids", userDeviceIdKey);
		}

		json.put("data", dataObject(title, message,description,imageUrl,filePath,type,id));
		System.out.println(json.toString());

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		NotificationResponse readValue=null;
		BufferedReader reader=null;
		try{
		reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " +conn.getResponseCode());
		String readLine = reader.readLine();

		ObjectMapper objMapper=new ObjectMapper();
		readValue = objMapper.readValue(readLine, NotificationResponse.class);
		System.out.println(readValue);
		
		
		}catch (Exception e) {
			LOGGER.error("Notification Fail :", e);
			e.printStackTrace();
		} finally {
			reader.close();
			conn.disconnect();
		}
		return readValue;		    

	}
	

	public NotificationResponse pushNotifications(List<String> userDeviceIdKey,String type,NotificationRequest notifyReq) throws Exception {
		String authKey = AUTH_KEY_FCM;
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");


		JSONObject json = new JSONObject();
		if(userDeviceIdKey!=null && userDeviceIdKey.size()==1){
			json.put("to", userDeviceIdKey.get(0).trim());
		} else {
			json.put("registration_ids", userDeviceIdKey);
		}

		json.put("data", dataObject(type,notifyReq));
		System.out.println(json.toString());

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		NotificationResponse readValue=null;
		BufferedReader reader=null;
		try{
		reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " +conn.getResponseCode());
		String readLine = reader.readLine();

		ObjectMapper objMapper=new ObjectMapper();
		readValue = objMapper.readValue(readLine, NotificationResponse.class);
		System.out.println(readValue);
		
		
		}catch (Exception e) {
			LOGGER.error("Notification Fail :", e);
			e.printStackTrace();
		} finally {
			reader.close();
			conn.disconnect();
		}
		return readValue;		    

	}	
	public NotificationResponse pushFCMNotification(String userDeviceIdKey, String title,String description,String message,String type,String imageUrl,String fileLoc,Long id) throws Exception {
		String authKey = AUTH_KEY_FCM;
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");


		JSONObject json = new JSONObject();
		json.put("to", userDeviceIdKey.trim());
		json.put("data", dataObject(title, description,message,imageUrl,fileLoc,type,id));
		System.out.println(json.toString());

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		wr.close();

		NotificationResponse readValue=null;
		BufferedReader reader=null;
		try{
		reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " +conn.getResponseCode());
		String readLine = reader.readLine();

		ObjectMapper objMapper=new ObjectMapper();
		readValue = objMapper.readValue(readLine, NotificationResponse.class);
		System.out.println(readValue);
		
		}catch (Exception e) {
			LOGGER.error("Notification Fail :", e);
			e.printStackTrace();
		} finally {
			reader.close();
			conn.disconnect();
		}
		return readValue;		    

	}
	private JSONObject dataObject( String title, String description,String message,String imageUrl,String linkFileLoc,String type,Long id){
		Random random = new Random();
		Integer randomNumber = random.nextInt(10000);
		JSONObject data = new JSONObject();
		data.put("title", title);
		data.put("body", description);
		data.put("message", message);
		data.put("description", description);
		if(imageUrl!=null && !imageUrl.equalsIgnoreCase("")){
			data.put("image",new ResponceCodesUtil().getApplicationProperties().get("SUMADHURA_LOGO_HTTP").toString());
			//data.put("icon", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
			data.put("style", "picture");
			data.put("picture",imageUrl);
			/* inorder to find that images are from external drive location or server location */
			data.put("isDriveImage", !imageUrl.contains("images/sumadhura_projects_images")?true:false);
		}else {
			data.put("isDriveImage", false);
		}
		/* inorder to find that files are from external drive location or server location */
		if(Util.isNotEmptyObject(linkFileLoc)) {
			data.put("isDriveFile", !linkFileLoc.contains("images/sumadhura_projects_images")?true:false);
		}else {
			data.put("isDriveFile", false);
		}
		data.put("typeMsg", type);
		data.put("linkFileLoc", linkFileLoc);
		/* notificationId for redirection purpouse */
		data.put("id", id);
		data.put("priority",1);
		data.put("color", "green");
		data.put("type", "message");
		data.put( "sound", "default");
		data.put("notId", randomNumber.toString());
		return data;
	}
private JSONObject dataObject(String type,NotificationRequest req){
		String title = req.getMessage();
		@SuppressWarnings("unused")
		String description = req.getDescription();
		String message = req.getNotificationText();
		String imageUrl = req.getImgLoc();
		String linkFileLoc = req.getLinkFileLoc();
		
		JSONObject data = new JSONObject();
		data.put("title", title);
		data.put("body", message);
		data.put("message", message);
		
		if(imageUrl!=null && !imageUrl.equalsIgnoreCase("")){
			data.put("image",imageUrl);
			data.put("icon", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
			data.put("style", "picture");
			data.put("picture",imageUrl);
		}
		data.put("linkFileLoc", linkFileLoc);
		data.put("typeMsg", type);
		data.put("priority",1);
		data.put("color", "green");
		data.put("type", "message");
		data.put( "sound", "Tri-tone");
		
		return data;
	}}