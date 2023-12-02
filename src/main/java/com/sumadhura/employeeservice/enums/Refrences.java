package com.sumadhura.employeeservice.enums;

public enum Refrences {

	PAPER_INSERT_PRINT_AD(1L,"Paper Insert/ Print Ad"),
	WEBSITE(2L,"Website"),
	SMS(3L,"SMS"),
	ONLINE_REALTY_PORTAL(4L,"Online/ Realty Portal"),
	HOARDING(5L,"Hoarding"),
	APARTMENT_ACTIVITY(6L,"Apartment Activity"),
	TV_RADIO(8L,"TV/ Radio"),
	EMAILER(9L,"Emailer"),
	EXHIBITION(10L,"Exhibition"),
	CORPORATE_ACTIVITY(11L,"Corporate Activity"),
	EXISTING_OWNER(12L,"Reference, Existing SUMADHURA Home Owner"),
	FRIEND_FAMILY(13L,"Reference, Friends/Family"),
	CHANEL_PARTNER(14L,"Channel Partner (CP)")	
	;
	
	private Refrences(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	private Long id;
	private String name;
	/**
	 * @return the id
	 */
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
