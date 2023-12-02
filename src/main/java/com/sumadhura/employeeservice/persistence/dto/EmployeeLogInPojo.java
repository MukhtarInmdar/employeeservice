/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * EmployeeLogInPojo class provides EMPLOYEE_LOGIN Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 06:35PM
 */

@Entity
@Data
public class EmployeeLogInPojo {
    
	@Column(name="ID")
    private Long id;
	
	@Column(name="EMPLOYEE_ID")
    private Long employeeId;
	
	@Column(name="STATUS")
    private Long statusId;
	
	@Column(name="CREATED_BY")
    private Long createdBy;
	
	@Column(name="MODIFIED_BY")
    private Long modifiedBy;
	
	@Column(name="USERNAME")
    private String userName;
    
	@Column(name="PASSWORD")
    private String password;
    
	@Column(name="LAST_LOGIN_TIME")
	private Timestamp lastLogInTime;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

}
