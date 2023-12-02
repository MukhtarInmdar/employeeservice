/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeRolePojo class provides EMPLOYEE_ROLES Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 20.06.2019
 * @time 12:42PM
 */

@Data
@Entity
public class EmployeeRolePojo {
	
	@Column(name="ROLE_ID")
	private Long roleId;
	@Column(name="NAME")
	private String name;
	@Column(name="DESCRIPTION")
	private String description;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_BY")
	private Long createdBy;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

}


