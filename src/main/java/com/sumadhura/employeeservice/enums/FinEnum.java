package com.sumadhura.employeeservice.enums;

/**
 * 
 * @author @NIKET CH@V@N
 *
 */
public enum FinEnum {
	
	LOAD_ALL_DATA(1l,"LoadAllData"),
	LOAD_BY_ID(2l,"LoadById"),
	LOAD_BY_NAME(3l,"LoadByName"),
	LOAD_BY_MILESTONE_NAME(4l,"LoadByMileStoneName"),
	ALL_BLOCKS(5l,"All Blocks"),
	BLOCK_WISE(6l,"Block Wise"),
	SEND_SINGLE_MULTIPLE(7l,"Send Single/Multiple"),
	LOAD_INTEREST_DATA(8l,"Load Interest Data"),
	UPDATE_INTEREST_DATA(9l,"Update Interest Data"),
	COUNT_DATA(10l,"Count Data"),
	DELETE_TRANSACTION(11l,"Delete Transaction"),
	SHIFT_TRANSACTION(12l,"Shift Transaction"),
	EDIT_TRANSACTION(13l,"Edit Transaction"),
	UPLOADED_TRANSACTION(14l,"Uploaded Transaction"),
	LOAD_BY_MILESTONE(15l,"LoadByMileStone"),
	ASC(16l,"ASC"),
	DESC(17l,"DESC"),
	APPROVE_MULTIPLE_TRANSACTION(18l,"Approve Multiple Transaction"),
	GET_INACTIVE_BOOKINGS(19l,"getInactiveBookings"),
	GET_ACTIVE_INACTIVE_FLATS(20l,"GetActiveInactiveFlats"),
	SINGLE_TRANSACTION_APPROVAL(21l,"Single Approval"),
	MULTIPLE_TRANSACTION_APPROVAL(22l,"Multiple Approval"),
	RECEIPT(23L,"Receipt"),
	PAYMENT(24L,"Payment"),
	RE_ADJUST_TRANSACTION(25l,"Re Adjust Transaction"),
	CHECK_CHANGED_TRANSACTION_DETAILS(26l,"changed trn details"),
	lOAD_DATA(27l,"Load Data"),
	DO_NOT_lOAD_DATA(28l,"Do not Load Data"),
	PENALTY_STATISTICS(29l,"PENALTY_STATISTICS"),
	PENALTY_TAX_MAPPING(30l,"PENALTY_TAX_MAPPING"),
	
	//maintain id's in order
	;
	private Long id;
	private String name;

	private FinEnum(Long Id, String name) {
		this.id = Id;
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