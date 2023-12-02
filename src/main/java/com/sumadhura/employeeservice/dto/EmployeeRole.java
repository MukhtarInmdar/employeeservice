/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * EmployeeRole class provides EMPLOYEE_ROLES  specific fields.
 * 
 * @author Venkat_Koniki
 * @since 26.06.2019
 * @time 02:42PM
 */

@ToString
@Getter
@Setter
public class EmployeeRole {

	private Long roleId;
	private String name;
	private String description;
	private Long statusId;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	
}
