package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;

import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.DepartmentRoleMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDepartmentMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLogInPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeMenuSubMenuMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeRoleMenuGroupingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeRolePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeSubMenuSiteMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.LoginMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.LoginSubMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
/*import com.sumadhura.employeeservice.persistence.dto.LoginDeptPojo;
import com.sumadhura.employeeservice.persistence.dto.LoginModulePojo;
import com.sumadhura.employeeservice.persistence.dto.LoginSubModulePojo;
import com.sumadhura.employeeservice.persistence.dto.LoginSubModuleSitePojo;*/
import com.sumadhura.employeeservice.service.dto.LoginInfo;

/**
 * LoginDao Interface provides Employee login specific functionalities.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */

public interface LoginDao {

	public List<EmployeeLogInPojo> getEmployeeLogInDetails(LoginInfo loginInfo,Status status);
	public List<EmployeeDepartmentMappingPojo> getEmployeeDepartmentMappingDetails(LoginInfo loginInfo,Status status);
	public List<DepartmentRoleMappingPojo> getDepartmentRoleMappingDetails(LoginInfo loginInfo,Status status);
	public List<EmployeeRoleMenuGroupingPojo> getEmployeeRoleMenuGroupingDetails(LoginInfo loginInfo, Status status);
    public List<EmployeeMenuSubMenuMappingPojo> getEmployeeMenuSubMenuMappingDetails(LoginInfo loginInfo, Status status);
	public List<LoginMenuPojo> getEmployeeMenuDetails(LoginInfo loginInfo, Status status);
	public List<LoginSubMenuPojo> getEmployeeSubMenuDetails(LoginInfo loginInfo, Status status);
	public List<EmployeeSubMenuSiteMappingPojo> getEmployeeSubMenuSiteMappingDetails(LoginInfo loginInfo, Status status);
	public List<SitePojo> getSiteDetails(LoginInfo loginInfo, Status status);
	public List<EmployeeRolePojo> getEmployeeRoleDetails(LoginInfo loginInfo, Status status);
	public Long updateEmployeeLogin(EmployeeLogInPojo EmployeeLogInPojo);
	public Boolean checkOldPassword(EmployeeLogInPojo employeeLogInPojo);
	public Long changePassword(EmployeeLogInPojo employeeLogInPojo);
	public List<EmployeePojo> getEmployeeByMobileNo(LoginInfo loginInfo);
}
