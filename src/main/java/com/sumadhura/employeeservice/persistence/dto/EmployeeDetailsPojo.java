/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeDetailsPojo class provides EMPLOYEE_DETAILS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 30.04.2019
 * @time 05:18PM
 */

@Entity
@Data
public class EmployeeDetailsPojo {

	@Column(name="EMP_DETAILS_ID")
	private Long empDetailsId;
	
	@Column(name="EMP_NAME")
	private String employeeName;
	
	@Column(name="EMP_DESIGNATION")
	private String employeeDesignation;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="DEPT_ID")
	private Long departmentId;
	
	@Column(name="MOBILE_NO")
	private String mobileNo;
	
	@Column(name="USER_PROFILE")
	private String userProfile;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="EMPLOYEE_ID")
	private Long employeeId;
	@Column(name="EMP_NAME_DESG")
	private String employeeNameDesg;
		
}


