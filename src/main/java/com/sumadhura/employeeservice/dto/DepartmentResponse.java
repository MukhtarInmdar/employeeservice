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
 * DepartmentResponse class provides Department specific fields.
 * 
 * @author Venkat_Koniki
 * @since 11.05.2019
 * @time 06:30PM
 */

@ToString
@Getter
@Setter
public class DepartmentResponse extends Result implements Serializable {
	
	private static final long serialVersionUID = -5772806537186895567L;

	private List<Department> departmentList;
	
}
