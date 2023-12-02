/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * CustomerPropertyDetailsPojo class provides CUSTOMER Property specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 12:18AM
 */

@Entity
@Data
public class CustomerData {

	@Column(name = "CUST_ID")
	private Long customerId;

	@Column(name = "CUST_NAME")
	private String customerName;

	@Column(name = "SALES_TEAM_LEAD_ID")
	private Long salesTeamLeadId;
	
	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookingId;

	@Column(name = "FLAT_ID")
	private Long flatId;

	@Column(name = "FLAT_NO")
	private String flatNo;

	@Column(name = "FLOOR_ID")
	private Long flooId;

	@Column(name = "FLOOR_NAME")
	private String floorName;

	@Column(name = "BLOCK_ID")
	private Long blockId;

	@Column(name = "BLOCK_NAME")
	private String blockName;

	@Column(name = "SITE_ID")
	private Long siteId;

	@Column(name = "SITE_NAME")
	private String siteName;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "SALESFORCE_BOOKING_ID")
	private String salesForceBookingId;
	
	
	@Column(name = "FLOOR_DET_ID")
	private Long floorDetId;
	
	@Column(name = "BLOCK_DET_ID")
	private Long blockDetId;
	
	@Column(name = "STATUS")
	private String status;
	
	
	
}
