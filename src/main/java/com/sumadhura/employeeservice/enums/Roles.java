/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/**
 * @author VENKAT KONIKI.
 *
 */
public enum Roles {

	SR_CRM_EXECUTIVE(1L,"SR.CRM EXECUTIVE"),
	ACCOUNTS_HEAD(4L,"ACCOUNTS_HEAD"),
	TECH_CRM(6l,"TECH CRM"),
	CRM_SALES_HEAD(12L,"crmsaleshead"),
	CRM_CENTRAL_SALES_HEAD(16L,"crmcentralsaleshead"),
	TECH_CRM_HEAD(15L,"techcrmhead"),
	CENTRAL_TECH_CRM_HEAD(14L,"centraltechcrmhead"),
	ASSISTENT_GENERAL_MANAGER(15l, "ASSISTENT GENERAL MANAGER"),
	DIRECTORS(16l, "DIRECTORS"),
	//DIRECTORS(18l, "DIRECTORS");
	BANKER(21l, "BANKER");
	;

	private Roles(Long id, String name) {
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
