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
public class EmployeeSubMenuSiteMappingPojo {

	@Column(name="SUBMENU_SITE_MAPPING_ID")
	private Long subMenuSiteMappingId;
	@Column(name="MENU_SUB_MENU_MAPPING_ID")
	private Long menuSubMenuMappingId;
	@Column(name="SITE_ID")
	private Long siteId;
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

