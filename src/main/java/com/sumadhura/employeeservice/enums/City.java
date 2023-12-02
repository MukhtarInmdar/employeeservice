package com.sumadhura.employeeservice.enums;

public enum City {

	
	Hyderabad(1L,"Hyderabad"),
	Bengaluru(2L,"Bengaluru"),
	Delhi(3L,"New Delhi"),
	Bihar(4L,"Bihar"),
	Ludhiana(5L,"Ludhiana")
	;

	private City(Long id, String name) {
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
