/**
 * 
 */
package com.sumadhura.employeeservice.enums;


/*
* Module enum provides Module  codes.
* 
* @author Venkat_Koniki
* @since 05.07.2019
* @time 02:59PM
*/

public enum Module {
	
	TICKETING(1L,"TICKETING"),LOGIN(2L,"LOGIN"),CUSTOMER_REGISTARTION(3L,"Customer Registration"),VIEW_CUSTOMERS(4l,"View Customers");
	
	private Long id;
	private String name;
	
	private Module(Long id, String name) {
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
