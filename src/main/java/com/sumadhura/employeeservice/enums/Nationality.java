/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/*
* 
* Nationality enum provides different Nationality codes.
* 
* @author Venkat_Koniki
* @since 30.04.2019
* @time 10:25AM
*/

public enum Nationality {

	INDIAN(1L,"indian");
	
	private Long id;
	private String name;
	
	private Nationality(Long id, String name) {
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
