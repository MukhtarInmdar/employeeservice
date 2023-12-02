package com.sumadhura.employeeservice.enums;

/**
 * 
 * @author @NIKET CH@V@N
 *
 */
public enum FinTransactionMode {
	CHEQUE(1l, "Cheque", ""),
	ONLINE(2l, "Online", ""),
	INTEREST_WAIVER(3l,"Interest Waiver",""),
	CASH(4l, "Cash", ""),
	WAIVED_OFF(5l, "waived off", ""),
	;

	private Long id;
	private String name;
	private String desc;

	private FinTransactionMode(Long id, String name, String desc) {
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