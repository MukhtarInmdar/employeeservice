/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * DepartmentPojo class provides Department specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 12:45PM
 */

@Entity
@Data
public class DepartmentPojo {

	@Column(name = "DEPT_ID")
	private Long departmentId;
	
	@Column(name = "NAME")
	private String departmentName;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "DEPT_EMAIL")
	private String departmentMail;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name="EMP_ID")
	private Long empId;
	
	@Column(name="EMP_NAME")
	private String empName;
	
	@Column(name="EMP_MAIL")
	private String empMail;

}
