/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * EmployeeLogInPojo class provides EMPLOYEE_LOGIN Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 18.06.2019
 * @time 05:35PM
 */

@Getter
@Setter
@ToString
public class EmployeeLogIn extends Result implements Serializable{

	private static final long serialVersionUID = -3537034410132807847L;
	private Long id;
    private Long employeeId;
    private Integer statusId;
    private Long createdBy;
    private Long modifiedBy;
    private String userName;
    private String password;
	private Timestamp lastLogInTime;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	
}
