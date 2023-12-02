/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Department class provides Department specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 12:45PM
 */
@ToString
@Getter
@Setter
public class Department implements Serializable{

	private static final long serialVersionUID = 6671591144079589517L;
	private Long departmentId;
	private String departmentName;
	private String description;
	private String departmentMail;
	private Long statusId;
	private String typeOf;
	private Long roleId;
	private String name;
	private Long departmentRoleMappingId;
	private Long employeeRoleMenuGroupingId;
	private Long employeeDepartmentMappingId;
	private Long employeeId;
	private List<LoginModule> loginModule;
	
}
