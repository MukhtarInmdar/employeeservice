package com.sumadhura.employeeservice.enums;

public enum Sector {
	IT(1,"IT"),
	ITESBPOKPO(2,"ITES/BPO/KPO"),
	Manufacturing(3,"Manufacturing"),
	FinancialService(4,"Financial Services"),
	HspitalityService(5,"Hospitality Services"),
	MedicalPharmaceutical(6,"Medical/ Pharmaceutical"),
	MediaEntertainment(7,"Media/ Entertainment"),
	TravelTransport(8,"Travel/ Transport"),
	RetailsServie(9,"Retail Services"),
	Telecom(10,"Telecom"),
	;

	private Sector(int id, String name) {
	this.id = id;
	this.name = name;
	}
	private int id;
	private String name;
	/**
	* @return the id
	*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	* @return the name
	*/
	
	
}
