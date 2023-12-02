package com.sumadhura.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinProjectAccountResponse {
	private Long finBankId;
	private String bankName;
		
	private Long siteAccountId;
	private String siteBankAccountNumber;
	private String accountHolderName;
	private Long finSiteProjAccMapId;
	private Long siteId;
    private String accountAddress;
	private String ifscCode;
	private String MICR;
	private String SWIFT;
	private Long flatSaleOwnerId;
	private String flatSaleOwner;
	private String accountNoWithSaleOwner ;
	
}
