package com.sumadhura.employeeservice.enums;
/*
* 
* Department enum provides different department codes.
* 
* @author Venkat_Koniki
* @since 30.04.2019
* @time 10:25AM
*/

public enum Department {
	
	 /* cug */
	 CRM(995L,"CRM"),
     TECH_CRM(994L,"TECH CRM"),
	 SALES(991L,"SALES"),
	 LEGAL(990L,"LEGAL"),
	 BANKER(984L,"BANKER"),
	 
	 /* uat */ 
	/* CRM(101L,"CRM"),
	TECH_CRM(102L,"TECH CRM"),
	SALES(104L,"SALES"),
	LEGAL(105L,"LEGAL"), */
	
	CRM_SALES(995L,"CRM SALES"),
	CRM_TECH(994L,"CRM TECH"),
	CRM_MIS(993L,"CRM MIS"),
	PRE_SALES(992L,"PRE SALES"),
	ACCOUNTS(997L,"ACCOUNTS"),
	ADMIN(989L,"ADMIN"),
	MANAGEMENT(988L,"MANAGEMENT"),
	
	/*EMPLOYEE(987L,"EMPLOYEE"),
	DEPARTMENT(986L,"DEPARTMENT"),
	CUSTOMER(985L,"CUSTOMER"),*/
	EMPLOYEE(8L,"EMPLOYEE"),
	DEPARTMENT(9L,"DEPARTMENT"),
	CUSTOMER(7L,"CUSTOMER"),
	SYSTEM(6L,"SYSTEM"),
	PM(4L,"PM")
	;
	
	private Department(Long id, String name) {
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
