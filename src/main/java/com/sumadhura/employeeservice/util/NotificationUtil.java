package com.sumadhura.employeeservice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.sumadhura.employeeservice.dto.PushNotification;


public class NotificationUtil extends Sender{
	
	private String gcmKey;

	public NotificationUtil(String gcmKey) {
		super(gcmKey);
		this.gcmKey = gcmKey;
	}
	
	@SuppressWarnings("unused")
	public PushNotification sendNotification(List<String> devicelist, String message ){
		HashMap<String, Integer> deviceStatus = new HashMap<String, Integer>();
		int successcount = 0;
		int failcount = 0;
		Message msg = null;
		//String GCM_API_KEY = "AIzaSyAqnxZ8xNQEuecF0aA-yB_0rgEusk0MXD4"; 
		PushNotification pushNotification = new PushNotification();
		try {

			NotificationUtil sender = new NotificationUtil(gcmKey);
			msg = new Message.Builder()
			.collapseKey("1")
			.timeToLive(3)
			.delayWhileIdle(false) //when screen is off then the receiver doesn't get notification.
			.addData("message",message)
			.build();
			/* Sends a message to many devices, retrying in case of unavailability.*/
			MulticastResult result = sender.send(msg, devicelist, 5);
			for (int i = 0; i < result.getTotal(); i++) {
				Result r = result.getResults().get(i);

				if (r.getMessageId() != null) {
					String canonicalRegId = r.getCanonicalRegistrationId();
					if (canonicalRegId != null) {
						String successToken = devicelist.get(i) ;
					}
					else{
						String successToken = devicelist.get(i) ;
					}
				} else {
					String error = r.getErrorCodeName();
					System.out.println(error);
					String successToken = devicelist.get(i);
				}
			}
			successcount = result.getSuccess();
			failcount = result.getFailure();
			deviceStatus.put("successdeviceSize :",successcount);
			deviceStatus.put("faileddeviceSize:", failcount);
			System.out.println("Android Success :"+successcount);
			System.out.println("Android Failed :"+failcount);
			pushNotification.setSuccessCount( successcount);
			pushNotification.setFailCount( failcount);
	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pushNotification;
	}
	
	public static void main(String[] args) {
		
		List<String> iphoneDeviceList = new ArrayList<String>();
		String message = "<!DOCTYPE html><html><head><title>Sumadhura Notitifications</title></head>"
				+"<body>"
			    +"<p>venkat</p>"
				+"</body>"
				+"</html>";
		String device_token ="c20RH23x9Z4:APA91bFZ4so1CSErHsH8n-w_BhoIZKbgaHdSr7ix1HtWKTGX8QET8iQwSrgcrG5h9L3cpCJoqbgKYm6d3J1mP43Mnlwc6w0UNqLdhXgjBwfKujvw_DgdMsNuDONKx4Zg3zL5HDgqGRs4";
		iphoneDeviceList.add(device_token);
		new NotificationUtil("AAAAKX3WKBc:APA91bGKUiuO3h-I13xWgMdFc-08A_VxgnqqsIq8IdwO2sJ4nMbsSaKAW7HmQeWP8gmJWNiGa8bmjNu1HZADKQyOfKOyoU_K-eqeRuvi-NGXajb3pXc_i9wOqqWfLT0D9JWyFNEPiwBd").sendNotification(iphoneDeviceList,message);
	}
}
