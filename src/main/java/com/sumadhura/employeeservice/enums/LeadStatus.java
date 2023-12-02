package com.sumadhura.employeeservice.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeadStatus {
	
	LEADNEW(0l,"LEAD","NEW"),
	LEADWORKING(1l,"LEAD","WORKING"),
	LEADNOTINTERESTED(2l,"LEAD","NOT INTERESTED"),
	OPPURTUNITYSCHEDULE(3l,"OPPURTUNITY","SCHEDULE"),
	OPPURTUNITYVISITED(4l,"OPPURTUNITY","VISITED"),
	OPPURTUNITYCANCELLED(5l,"OPPURTUNITY","CANCELLED"),
	BOOKINGSUBMITTED(6l,"BOOKING","SUBMITTED"),
	BOOKINGAPPROVED(7l,"BOOKING","APPROVED"),
	BOOKINGREJECTED(8l,"BOOKING","REJECTED"),
	
	;
	
	public Long statusCode;
	public String status;
	public String subStatus;
	
	private static Map<Long, String> statusMap = new LinkedHashMap<>();
	private static Map<Long, String> subStatuseMap = new LinkedHashMap<>();
	
	static {
		for(LeadStatus leadStatus : LeadStatus.values()) {
			statusMap.put(leadStatus.statusCode, leadStatus.status);
		}
	}
	
	static {
		for(LeadStatus leadStatus : LeadStatus.values()) {
			subStatuseMap.put(leadStatus.statusCode, leadStatus.subStatus);
		}
	}
	
	
	public static String getStatusById(Long siteId) {
		return statusMap.get(siteId);
	}
	
	public static String getSubStatusNameById(Long siteId) {
		return subStatuseMap.get(siteId);
	}
	
	public static void main(String[] args) {
		System.out.println("Result :"+getStatusById(8l) +", Short Name:"+getSubStatusNameById(8l) );
	}
	
	
	
	

}
