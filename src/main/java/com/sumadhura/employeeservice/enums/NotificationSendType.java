package com.sumadhura.employeeservice.enums;

public enum NotificationSendType {
	SITE (1L,"SITE"),
	BLOCK (2L,"BLOCK"),
	FLOOR (3L,"FLOOR"),
	FLAT_BOOKING (4L,"FLAT_BOOKING"),
	STATE (16l,"STATE"),
	SBUA (17l, "SBUA"),
	FLAT_SERIES (18l, "FLAT_SERIES"),
	FACING (19l, "FACING"),
	BHK_TYPE (20L, "BHK_TYPE"),
	MESSAGE(64l, "MESSAGE"),
	DESCRIPTION(65l, "DESCRIPTION"),
	IMAGE_LOCATION (66l, "IMAGE_LOCATION"),
	FILE_LOCATION (67l, "FILE_LOCATION"),
	NOTIFICATION_TEXT (68l, "NOTIFICATION_TEXT"),
	OS_TYPE (69l, "OS_TYPE"),
	BOOKING_START_DATE(108l,"Booking Start Date"),
	BOOKING_END_DATE(109l,"Booking End Date"),
	BOOKING_SELECTED_FLATS_BY_DATES(110l,"Booking Selected Flats By Dates");
	
	private String name ;
	private Long id;
	
	private NotificationSendType(Long id, String name) {
		this.name = name;
		this.id = id;
	}
	
	/* Setters */
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/* Getters */
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
}
