/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/*
* Module enum provides Module  codes.
* 
* @author Venkat_Koniki
* @since 05.07.2019
* @time 03:05PM
*/
public enum SubModule {

	VIEW_ALL_TICKETS(1L,"View All Tickets"),
	VIEW_MY_TICKETS(2L,"View My Tickets"),
	VIEW_INFO_REQUEST(3L,"View Info Request"),
	CREATE_NOTIFICATIONS(4L,"Create Notifications"),
	VIEW_APPROVE_NOTIFICATIONS(5L,"View & Approve Notifications"),
	CREATE_CUSTOMER(6L,"Create Customer"),
	APPROVED_CUSTOMER(7L,"Approve Customer"),
	VIEW_ALL_CUSTOMERS(8L,"View All Customers"),
	LEAVE_UPDATE(13L,"Leave Update"),
	APPROVE_ESCALATION_TIME(14L,"Approve Escalation Time"),
	ESCALATION_TICKETS(15L,"Escalation Tickets"),
	UNCLEARED_CHEQUE(44L,"Uncleared Cheque")
	;
	
	
	/*VIEW_FORMS_LIST(6L,"Create Customer"),
	VIEW_BOOKING_FORM(7L,"VIEW BOOKING FORM"),
	SAVE_BOOKING_FORM(8L,"SAVE BOOKING FORM"),
	ACTION_BOOKING_FORM(9L,"ACTION BOOKING FORM");*/
	
	
	private Long id;
	private String name;
	
	private SubModule(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
