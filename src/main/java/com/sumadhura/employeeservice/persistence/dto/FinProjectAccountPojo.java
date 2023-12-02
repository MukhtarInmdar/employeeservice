package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FinProjectAccountPojo {
	
	@Column(name  ="FIN_BANK_ID")
	private Long finBankId;
	
	@Column(name ="BANK_NAME")
	private String bankName;
	
	@Column(name ="FIN_PROJ_ACC_ID")
	//private Long finProjectAccountId;
	private Long siteAccountId;
	
	@Column(name ="ACCOUNT_NO")
	//private String accountNo;
	private String siteBankAccountNumber;
	
	@Column(name ="HOLDER_NAME")
	private String accountHolderName;
	
	@Column(name ="FIN_SITE_PROJ_ACC_MAP_ID")
	private Long finSiteProjAccMapId;
	
	@Column(name ="SITE_ID")
	private Long siteId;
	
	@Column(name ="IFSC_CODE")
	private String ifscCode;
	
	@Column(name ="ADDRESS_ID")
	private Long addressId;
	
	@Column(name ="ADDRESS1")
	private String accountAddress;
	
	@Column(name ="FLATS_SALE_OWNERS_ID")
	private Long flatSaleOwnerId;
	
	@Column(name ="FLAT_SALE_OWNER")
	private String flatSaleOwner;
	
	@Column(name ="ACCOUNT_NO_WITH_SALEOWNER")
	private String accountNoWithSaleOwner ;
	
	
}
