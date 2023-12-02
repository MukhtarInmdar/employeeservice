package com.sumadhura.employeeservice.service.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FinProjectAccountInfo{
	private Long finBankId;
	private String bankName;
	private Long finProjectAccountId;
	private String accountNo;
	private Long finSiteProjAccMapId;
	private Long siteId;
}

