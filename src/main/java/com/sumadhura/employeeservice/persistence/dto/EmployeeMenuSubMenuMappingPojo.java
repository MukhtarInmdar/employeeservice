/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeMenuSubMenuMappingPojo class provides EMPLOYEE_MENU_SUBMENU_MAPPING_MODULE Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 18.06.2019
 * @time 06:23PM
 */

@Data
@Entity
public class EmployeeMenuSubMenuMappingPojo {

@Column(name="MENU_SUB_MENU_MAPPING_ID")	
private Long menuSubMenuMappingId;
@Column(name="MODULE_ID")
private Long moduleId;
@Column(name="SUBMODULE_ID")
private Long subModuleId;
@Column(name="CREATED_BY")
private Long createdBy;
@Column(name="MODIFIED_BY")
private Long modifiedBy;
@Column(name="CREATED_DATE")
private Timestamp createdDate;
@Column(name="MODIFIED_DATE")
private Timestamp ModifiedDate;
@Column(name="STATUS_ID")
private Long statusId;

}

