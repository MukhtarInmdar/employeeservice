package com.sumadhura.employeeservice.enums;

/**
 * 
 * @author @NIKET CH@V@N
 *
 */
public enum FinTransactionType {
	RECEIPT(1l, "Receipt", ""),
	PAYMENT(2l, "Payment", ""),//refund entry
	INTEREST_WAIVER(3l,"Interest Waiver",""),
	CASH(4l, "Cash", ""),
	;

	private Long id;
	private String name;
	private String desc;

	private FinTransactionType(Long id, String name, String desc) {
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