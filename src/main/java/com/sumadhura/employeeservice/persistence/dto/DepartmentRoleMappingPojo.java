/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeDepartmentMappingPojo class provides EMPLOYEE_DEPARTMENT_MAPING Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 18.06.2019
 * @time 05:18PM
 */

@Data
@Entity
public class DepartmentRoleMappingPojo {

	@Column(name="DEPARTMENT_ROLE_MAPPING_ID")	
	private Long departmentRoleMappingId;
	@Column(name="DEPT_ID")
	private Long departmentId;
	@Column(name="ROLE_ID")
	private Long roleId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="STATUS")
	private Long status;
	
}

