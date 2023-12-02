package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CarParkingAllotmentPdfPojo {
	
	@Column(name="BASEMENT_NAME")
	private String basementName;
	
	@Column(name="SLOT_NAME")
	private String slotName;
	
	@Column(name="FLAT_NO")
	private String flatNo;
	
	@Column(name="CUST_NAME")
	private String custName;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="SITE_ADDRESS")
	private String siteAddress;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	@Column(name="SLOT_ID")
	private Long slotId;
	
	@Column(name="ALLOTMENT_ID")
	private Long allotmentId;
	
	@Column(name="EMP_ID")
	private Long empId;
	
	@Column(name="EMP_NAME")
	private String empName;
	
	@Column(name="EMP_MAIL")
	private String empMail;
	
	@Column(name="FLATS_SALE_OWNERS_ID")
	private Long flatsSaleOwnersId;
	
	@Column(name="FLAT_SALE_OWNER")
	private String flatSaleOwner;
}
