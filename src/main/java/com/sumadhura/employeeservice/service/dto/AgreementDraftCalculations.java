package com.sumadhura.employeeservice.service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AgreementDraftCalculations {
	
	private Long serialNo;
	private String particulars;
	private String basicCost;
	private String gstPercentage;
	private String gstAmount;
	private String totalAmount;

	
}
