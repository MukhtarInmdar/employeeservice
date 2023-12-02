package com.sumadhura.employeeservice.persistence.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class LoginDetailsPojo {

	@Column(name="ID")
	private Long id;
	
	@Column(name="EMPLOYEE_ID")
	private Long employeeId;
	
	@Column(name="ROLE_ID")
	private Long roleId;
	
	@Column(name="USERNAME")
	private Long userName;
	
	@Column(name="PASSWORD")
	private Long password;
	
	@Column(name="LAST_LOGIN_TIME")
	private Date lastLoginTime;
	
	@Column(name="DEPT_ID")
	private Long deptId;

	
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserName() {
		return userName;
	}

	public void setUserName(Long userName) {
		this.userName = userName;
	}

	public Long getPassword() {
		return password;
	}

	public void setPassword(Long password) {
		this.password = password;
	}

	

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	
	
}


