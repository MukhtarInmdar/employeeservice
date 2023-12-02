/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class TicketDetailsPojo {
	@Column(name = "TICKET_ID")
	private Long ticketId;	
	@Column(name = "TITLE")
	private String title;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "TICKET_TYPE_ID")
	private Long ticketTypeId;
	@Column(name = "TICKET_TYPE")
	private String ticketType;
	@Column(name = "TICKET_TYPE_DETAILS_ID")
	private Long ticketTypeDetailsId;
	@Column(name = "EMP_ID")
	private Long employeeId;
	@Column(name = "EMAIL")
	private String employeeMail;
	@Column(name = "DEPT_ID")
	private Long departmentId;
	@Column(name = "DEPT_NAME")
	private String departmentName;
	@Column(name = "EMP_NAME")
	private String employeeName;
	@Column(name = "CUST_ID")
	private Long customerId;
	@Column(name = "NAME_PREFIX")
	private String namePrefix;
	@Column(name = "CUST_NAME")
	private String customerName;
	@Column(name = "SITE_ID")
	private Long siteId;
	@Column(name = "SITE_NAME")
	private String siteName;
	@Column(name = "BLOCK_ID")
	private Long blockId;
	@Column(name = "BLOCK_NAME")
	private String blockName;
	@Column(name = "FLOOR_ID")
	private Long floorId;
	@Column(name = "FLOOR_NAME")
	private String floorName;
	@Column(name = "FLAT_ID")
	private Long flatId;
	@Column(name = "FLAT_NO")
	private String flatNo;
	@Column(name = "FLAT_BOOK_ID")
	private Long flatbookId;
	@Column(name = "LEVEL_ID")
	private String levelId;
}
