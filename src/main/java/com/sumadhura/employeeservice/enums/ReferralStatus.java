package com.sumadhura.employeeservice.enums;

import com.sumadhura.employeeservice.util.Util;

public enum ReferralStatus {

	AGREEMENT_DONE(1l,"Agreement Done"),
	BOOKED(2l,"Booked"),
	ELIGIBLE(3l,"Eligible"),
	NON_ELIGIBLE(4l,"Non Eligible"),
	PAYMENT_INITIATED(6l,"Payment Initiated"),
	PAYMENT_DONE(5l,"Payment Done");

	private Long id;
	private String name;
	
	private ReferralStatus(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/* to get ReferralStatus Name by its value */
	public static String getReferralStatusName(Long referralStatusValue) {
		if(Util.isNotEmptyObject(referralStatusValue)) {
			ReferralStatus array[] = ReferralStatus.values();
			for(ReferralStatus referralStaus : array) {
				if(referralStatusValue.equals(referralStaus.getId())) {
					return referralStaus.getName();
				}
			}
		}
		return "";
	}
	
	/* to get ReferralStatus Value by its Name */
	public static Long getReferralStatusValue(String referralStausName) {
		if(Util.isNotEmptyObject(referralStausName)) {
			ReferralStatus array[] = ReferralStatus.values();
			for(ReferralStatus referralStaus : array) {
				if(referralStausName.equalsIgnoreCase(referralStaus.getName())) {
					return referralStaus.getId();
				}
			}
		}
		return 0l;
	}

}
