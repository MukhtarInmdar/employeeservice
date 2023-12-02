package com.sumadhura.employeeservice.service.dto;



import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeRolePojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DepartmentRoleInfo class provides Employee Department and Roles specific properties.
 * 
 * @author Venkat_Koniki
 * @since 27.06.2019
 * @time 11:50AM
 */
@ToString
@Getter
@Setter
public class DepartmentRoleInfo {
	private List<DepartmentPojo> departmentPojos;
	private List<EmployeeRolePojo> employeeRolePojos;
}
