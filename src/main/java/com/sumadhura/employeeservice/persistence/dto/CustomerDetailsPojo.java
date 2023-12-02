package com.sumadhura.employeeservice.persistence.dto;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CustomerDetailsPojo {
	
	@Column (name = "FLAT_BOOK_ID")
	private Long flatBookingId;
	
	@Column (name = "STATUS_ID")
	private Long fbStatusId;
	
	@Column (name = "CUST_ID")
	private Long custId;
	
	@Column (name = "CUST_NAME")
	private String custName;
	
	@Column (name = "FLAT_ID")
	private Long flatId;
	
	@Column (name = "FLAT_NO")
	private String flatNo;

	@Column (name = "FLOOR_ID")
	private Long floorId;
	
	@Column (name = "FLOOR_NAME")
	private String floorName;
	
	@Column (name = "BLOCK_ID")
	private Long bloockId;
	
	@Column (name = "BLOCK_NAME")
	private String bloockName;
	
	@Column (name = "SITE_ID")
	private Long siteId;
	
	@Column (name = "SITE_NAME")
	private String siteName;

	@Column (name = "APP_REG_DATE")
	private Timestamp appRegDate;
}
