/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;


/**
 * EmployeePojo class provides EMPLOYEE Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 12.06.2019
 * @time 10:14PM
 */
@Getter
@Setter
public class Employee {

	private Long csEmpId;
	private Long employeeId;
	private String firstName;
	private String lastName;
	private String employeeName;
	private String email;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private String mobileNumber;
	private String userProfile;
	private String username;
	private String salesForceEmpName;
	
}
