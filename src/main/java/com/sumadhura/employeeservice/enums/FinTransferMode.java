package com.sumadhura.employeeservice.enums;

/**
 * 
 * @author @NIKET CH@V@N
 *
 */
public enum FinTransferMode {

	IMPS(1l, "IMPS", "Immediate Payment Service"), 
	NEFT(2l, "NEFT", "National Electronic Fund Transfer"),
	RTGS(3l, "RTGS", "Real Time Gross Settlement"),
	N_A(4l, "N/A", "Not Available"),
	ONLINE(5l, "ONLINE", "Online"),
	CHEQUE(6l, "CHEQUE", "Cheque")
	;

	private Long id;
	private String name;
	private String desc;

	private FinTransferMode(Long id, String name, String desc) {
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