/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeePojo class provides EMPLOYEE Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 12.06.2019
 * @time 10:14PM
 */

@Data
@Entity
public class EmployeePojo {
	
	@Column(name="CS_EMP_ID")
	private Long csEmpId;
	@Column(name="EMP_ID")
	private Long employeeId;
	@Column(name="FIRST_NAME")
	private String firstName;
	@Column(name="LAST_NAME")
	private String lastName;
	@Column(name="EMP_NAME")
	private String employeeName;
	@Column(name="EMAIL")
	private String email;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="MOBILE_NO")
	private String mobileNumber;
	@Column(name="USER_PROFILE")
	private String userProfile;	
	@Column(name="EMP_DET_ID")
	private Long empDetailsId;
	@Column(name="DEPT_ID")
	private Long departmentId;
	@Column(name ="USERNAME")
	private String username;
	@Column(name ="SALESFORCE_EMP_NAME")
	private String salesForceEmpName;
	
	//ACP Added
	@Column(name ="EMP_DESIGNATION") private String emp_designation;
}


