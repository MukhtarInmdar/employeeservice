package com.sumadhura.employeeservice.enums;

/**
 * 
 * @author @NIKET CH@V@N
 *
 */
public enum FinTransactionFor {
	REFUND(1l, "Refund", ""), 
	FLAT_CANCELLATION(2l, "Flat Cancellation", ""),
	SYSTEM_REFUND(3l, "System Refund", "");

	private Long id;
	private String name;
	private String desc;

	private FinTransactionFor(Long id, String name, String desc) {
		this.id = id;
		this.name = name;
		this.desc = desc;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}