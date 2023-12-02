package com.sumadhura.employeeservice.dto;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
@Getter
@Setter
public class TicketPushNotificationRequest extends Result implements Serializable{
	

	private static final long serialVersionUID = -7544225839361332443L;
	private Long siteId;
	private Long flatBookingId;
	private String notificationText;
	private String notificationTitle;
	private Long ticketId;
}
