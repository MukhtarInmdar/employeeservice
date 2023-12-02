/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DepartmentWrapper class provides DepartmentWrapper specific fields.
 * 
 * @author Venkat_Koniki
 * @since 26.06.2019
 * @time 02:35PM
 */

@ToString
@Getter
@Setter
public class DepartmentWrapper {

	private Department department;
	private EmployeeRole employeeRole;
	
}
