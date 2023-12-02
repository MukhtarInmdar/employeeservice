package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class SiteDetailsPojo {
	@Column(name ="FLAT_ID")
	private Long flatId;
	
	@Column(name ="FLAT_NO")
	private String flatNo;
	
	@Column(name ="FLOOR_ID")
	private Long floorId;
	
	@Column(name ="FLOOR_NAME")
	private String floorName;
	
	@Column(name ="BLOCK_ID")
	private Long blockId;
	
	@Column(name ="BLOCK_NAME")
	private String blockName;
	
	@Column(name ="SITE_ID")
	private Long siteId;
	
	@Column(name ="SITE_NAME")
	private String siteName;
	
	@Column(name ="STATE_ID")
	private Long stateId;
	
	@Column(name ="FLAT_BOOK_ID")
	private Long flatBookingId;
	
	@Column(name ="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name ="CUST_ID")
	private String customerId;
	
	

}
