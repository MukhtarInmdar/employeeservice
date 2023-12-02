/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/**
 * @author Venkat
 * @Date 04-09-2020
 */
public enum Event {

	DOB(1L,"DATE_OF_BIRTH"),
	DOA(2L,"DATE_OF_ANIVERSARY")
	;
	
	private Event(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	private Long id;
	private String name;
	
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
