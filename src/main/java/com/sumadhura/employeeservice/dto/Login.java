/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Login  class provides Employee login specific fields.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:20PM
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
@Setter
@ToString
public class Login extends Result implements Serializable {
	
	private static final long serialVersionUID = 4587857093810452548L;
	private Long employeeId;
	private String username;
	private String password;
	private String requestUrl;
	private Long departmentRoleMappingId;
	private Long employeeRoleMenuGroupingId;
	private Long employeeDepartmentMappingId;
	private Long departmentId;
	private Long roleId;
	private String newPassword;
	private String email;
	private String mobileNo;
	private Integer otp;
	private String employeeName;
	private String action;
}
