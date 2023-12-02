package com.sumadhura.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReferredFrndComments extends Result{
	
	private static final long serialVersionUID = 5493157417624519206L;

	private String refrenceId;
	
	private String comments;
	
	private String referralStatusName;
	
	private Long referralStatusValue;
	

}
