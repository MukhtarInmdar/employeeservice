/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeRoleMenuGroupingPojo class provides EMPLOYEE_ROLE_MENU_GROUPING Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 18.06.2019
 * @time 06:18PM
 */

@Data
@Entity
public class EmployeeRoleMenuGroupingPojo {

	@Column(name="MENU_ROLE_GROUP_ID")
	private Long menuRoleGroupId;
	@Column(name="EMPLOYEE_DEPARTMENT_MAPING_ID")
	private Long employeeDepartmentMappingId;
	@Column(name="MENU_MAPPING_ID")
	private Long menuMappingId;
	@Column(name="CREATED_BY")
	private Long createdBy;
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="STATUS_ID")
	private Long statusId;
  		
}
