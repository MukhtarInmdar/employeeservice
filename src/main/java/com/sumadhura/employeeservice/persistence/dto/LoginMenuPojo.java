/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeMenuPojo class provides EMPLOYEE_MENU_MODULE Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 18.06.2019
 * @time 06:23PM
 */

@Data
@Entity
public class LoginMenuPojo {

	@Column(name = "MODULE_ID")
	private Long moduleId;
	@Column(name = "MODULE_NAME")
	private String moduleName;
	@Column(name = "CREATED_BY")
	private Long createdBy;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")
	private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name = "MODULE_ICON")
	private String moduleIcon;
	
}
