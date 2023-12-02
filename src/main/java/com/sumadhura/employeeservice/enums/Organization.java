package com.sumadhura.employeeservice.enums;

public enum Organization {


	pvltd(1L,"Pvt. Ltd."),
	publicltd(2L,"Public Ltd."),
	Govtservices(3L,"Govt. Services"),
	Selfemployeed(4L,"Self Employed/Business"),
	Selfemployeedprofessional(5L,"Self Employed Professional");
	
	;



	private Organization(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	private Long id;
	private String name;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
