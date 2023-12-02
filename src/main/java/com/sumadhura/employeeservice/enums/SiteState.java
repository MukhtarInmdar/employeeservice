package com.sumadhura.employeeservice.enums;

public enum SiteState {


	Acropolis("Telangana","Acropolis"),
	EdenGarden("Karnataka","EdenGarden"),
	Soham("Karnataka","Soham"),
	Nandanam("Karnataka","Nandanam");



	private SiteState(String id, String name) {
		this.id = id;
		this.name = name;
	}
	private String id;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
