/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeLeaveDetailsPojo class provides EMPLOYEE_LEAVE_DETAILS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 11.06.2019
 * @time 01:30PM
 */
@Entity
@Data
public class EmployeeLeaveDetailsPojo {

	@Column(name="EMPLOYEE_LEAVE_DETAILS_ID")
	private Long employeeLeaveDetailsId;
	
	@Column(name="EMPLOYEE_ID")
	private Long employeeId;
	
	@Column(name="START_DATE")
	private Timestamp startDate;
	
	@Column(name="END_DATE")
	private Timestamp endDate;
	
	@Column(name="REJOIN_DATE")
	private Timestamp rejoinDate;
	
	@Column(name="APPROVED_BY")
	private Long approvedBy;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
		
}



