/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookingFormRequestInfo {
	
	private Long customerId;
	private String customerName;
	private String customerEmail;
	private String customerProfilePic;
	private Long salesTeamLeadId;
	private Long flatBookingId;
	private Long flatId;
	private String flatNo;
	private Long flooId;
	private String floorName;
	private Long blockId;
	private String blockName;
	private Long siteId;
	private String siteName;
	private Long stateId;
	private List<String> mails;
}
