package com.sumadhura.employeeservice.enums;

public enum Function {

	Software(1,"Software"),
	SalesandMarketing(2,"SalesandMarketing"),
	HRadministration(3,"HR/Administration"),
	Finance(4,"Finance"),
	Production(5,"Production"),
	Legal(6,"Legal"),
	Operations(7,"Operations")

	;
	private Function(int id, String name) {
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
