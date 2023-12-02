/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/*
* 
* Type enum provides different Type codes.
* 
* @author Venkat_Koniki
* @since 18.05.2019
* @time 05:25PM
*/
public enum Type {

	HOST(1L,"HOST"),GUEST(2L,"GUEST"),SYSTEM(3L,"SYSTEM"),SYSTEM_ESCALATION(26L,"System Escalation"),EXTENDED_ESCALATION_TIME(27L,"Extended Escalation Time"),ANDRIOD(28L,"android"),IOS(29L,"ios");
	
	private Type(Long id, String name) {
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
