package com.sumadhura.employeeservice.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.sumadhura.employeeservice.dto.GoogleCalendarEventRequest;

@Component("GoogleCalendarEventUtil")
public class GoogleCalendarEventUtil {
	
private final static Logger LOGGER = Logger.getLogger(GoogleCalendarEventUtil.class);
	
	//@Autowired(required = true)
	//private ResponceCodesUtil responceCodesUtil;
	
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	//private static final String TOKENS_DIRECTORY_PATH = "D:/CUSTOMERAPP_CUG/images/sumadhura_projects_images/tokens";
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	/*
	 * Global instance of the scopes required by this quickstart.
	 * If modifying these scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
	private static final String CREDENTIALS_FILE_PATH = "tokens/credentials.json";
	private static final String CALENDAR_ID = "primary";

	/*
	 * Creates an authorized Credential object.
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, final File file) throws IOException {
		LOGGER.info("*** The control is inside of the getCredentials in GoogleCalendarEventUtil ***");
		// Load client secrets.
		//InputStream in = GoogleCalendarEventUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
	    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
	    if (in == null) {
	    	LOGGER.error("Resource not found: " + CREDENTIALS_FILE_PATH);
	    	throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
	    }
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

	    // Build flow and trigger user authorization request.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	    								   HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	            						  .setDataStoreFactory(new FileDataStoreFactory(file))
	                                      .setAccessType("offline")
	                                      .build();
	    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8686).build();
	    //LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8181).build();
	    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
	
	/* For Deleting an Existing event in Google Calendar by Event Id */
	public void deleteGoogleCalendarEvent(GoogleCalendarEventRequest eventRequest) throws FileNotFoundException, IOException, GeneralSecurityException {
		LOGGER.info("*** The control is inside of the deleteGoogleCalendarEvent in GoogleCalendarEventUtil ***");
		File file = new ClassPathResource(TOKENS_DIRECTORY_PATH).getFile();
		//final String TOKENS_DIRECTORY_PATH = responceCodesUtil.getApplicationNamePropeties("TOKENS_DIRECTORY_PATH");
		//File file = new File(TOKENS_DIRECTORY_PATH);
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT,file))
			               .setApplicationName(APPLICATION_NAME)
			               .build();
		try {
			HttpResponse executeUnparsed = service.events().delete(CALENDAR_ID, eventRequest.getEventId()).executeUnparsed();
			if(Util.isNotEmptyObject(executeUnparsed) && Util.isNotEmptyObject(executeUnparsed.getStatusCode()) && executeUnparsed.getStatusCode()==204) {
				LOGGER.info("***The Google Calendar Event is deleted successfully and the event Id is:"+eventRequest.getEventId()+"***");
			}else {
				LOGGER.info("***The Google Calendar Event is not deleted and the event Id is:"+eventRequest.getEventId()+"***");
			}
		}catch(Exception ex){
			LOGGER.error("***The Exeption is occured while deleting Google Calendar Event "+ex.getMessage()+ex.getStackTrace()+"***"+"and the event Id is:"+eventRequest.getEventId());
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		GoogleCalendarEventUtil googleCalendarEventUtil = new GoogleCalendarEventUtil();
		GoogleCalendarEventRequest eventRequest = new GoogleCalendarEventRequest();
		eventRequest.setEventId("5jeer0ph8e3t1jkqgc5u5q3v5k");
		try {
			googleCalendarEventUtil.deleteGoogleCalendarEvent(eventRequest);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

}
