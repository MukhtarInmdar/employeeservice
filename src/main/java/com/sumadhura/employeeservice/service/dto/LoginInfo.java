/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Data;

/**
 * LoginInfo  class provides Employee login specific fields.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:20PM
 */
@Data
public class LoginInfo {

	private Long employeeId;
	private String username;
	private String password;
	private String requestUrl;
	private Long departmentRoleMappingId;
	private Long employeeRoleMenuGroupingId;
	private Long employeeDepartmentMappingId;
	private Long employeeMenuSubMenuMappingId;
	private Long moduleId;
	private Long subModuleId;
	private Long siteId;
	private Long departmentId;
	private Long roleId;
	private String identifier;
	private String newPassword;
	private String email;
	private String mobileNo;
	private Integer otp;
	private String employeeName;
	private String action;
	private List<Long> moduleIds;
	private List<Long> subModuleIds;
	private List<Long> employeeMenuSubMenuMappingIds;
}
