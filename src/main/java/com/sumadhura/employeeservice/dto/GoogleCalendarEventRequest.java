package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class GoogleCalendarEventRequest {
	private String eventId;
	private String attendeeOneMail;
	private String attendeeTwoMail;
	private String summary;
	private String description;
	private String location;
	private Timestamp startTime;
	private Timestamp endTime;
}
