package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class SiteOtherChargesDetailsPojo {
	@Column(name ="SITE_ID")
	private Long siteId;
	
	@Column(name ="SITE_NAME")
	private String siteName;
	
	@Column(name ="METE_DATA_TYPE_ID")
	private Long meteDataTypeId;
	
	@Column(name ="AMOUNT")
	private Double amount;

	@Column(name ="AMT_FOR_YEARS")
	private Long amtForYears;
	
	@Column(name ="STATUS_ID")
	private String statusId;
	
	private String strAmount;
	private String strAmountInWords;
}
