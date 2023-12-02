/**
 * 
 */
package com.sumadhura.employeeservice.service.mappers;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.sumadhura.employeeservice.dto.Department;
import com.sumadhura.employeeservice.dto.Employee;
import com.sumadhura.employeeservice.dto.EmployeeLogIn;
import com.sumadhura.employeeservice.dto.Login;
import com.sumadhura.employeeservice.dto.LoginModule;
import com.sumadhura.employeeservice.dto.LoginResponse;
import com.sumadhura.employeeservice.dto.LoginSubModule;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDepartmentMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLogInPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeRolePojo;
import com.sumadhura.employeeservice.persistence.dto.LoginMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.LoginSubMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.service.dto.LoginInfo;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * LoginMapper class provides different login conversions functionalities.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */
public class LoginMapper {

	private final Logger LOGGER = Logger.getLogger(LoginMapper.class);

	public LoginInfo LoginToLoginInfo(@NonNull Login login) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the LoginToLoginInfo in LoginMapper *****");
		LoginInfo loginInfo = new LoginInfo();
		BeanUtils.copyProperties(loginInfo, login);
		return loginInfo;
	}

	public Login LoginInfoToLogin(@NonNull LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the LoginInfoToLogin in LoginMapper *****");
		Login login = new Login();
		BeanUtils.copyProperties(login, loginInfo);
		return login;
	}
	public List<EmployeeLogIn> EmployeeLogInPojoToEmployeeLogIn(@NonNull List<EmployeeLogInPojo> employeeLogInPojo ) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the EmployeeLogInPojoToEmployeeLogIn in LoginMapper *****");
		List<EmployeeLogIn> employeeLogIns = new ArrayList<EmployeeLogIn>();
		for(EmployeeLogInPojo logInPojo : employeeLogInPojo) {
		EmployeeLogIn employeeLogIn = new EmployeeLogIn();
		BeanUtils.copyProperties(employeeLogIn, logInPojo);
		employeeLogIns.add(employeeLogIn);
		}
		LOGGER.debug("**** The employeeLogIns obj is ****"+employeeLogIns);
		return employeeLogIns;
	}
	
	public LoginSubModule loginSubMenuPojos$sitePojos(List<LoginSubMenuPojo> loginSubMenuPojos, List<SitePojo> sitePojos) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the loginSubMenuPojos$sitePojos in LoginMapper *****");
		LoginSubModule loginSubModule = new LoginSubModule();
		List<Site> sites = new ArrayList<Site>();
		
		if(Util.isNotEmptyObject(loginSubMenuPojos)) {
			BeanUtils.copyProperties(loginSubModule,loginSubMenuPojos.get(0));
		}
		if(Util.isNotEmptyObject(sitePojos)) {
			  for( SitePojo sitePojo : sitePojos) {
				  Site site = new Site();
				  BeanUtils.copyProperties(site,sitePojo);
				  sites.add(site);
			  }
		}
		loginSubModule.setSites(sites);
		LOGGER.debug("*** The loginSubModule obj is ****"+loginSubModule);
		return loginSubModule;
	}
	public LoginModule loginMenuPojos$loginSubMenuPojos(List<LoginMenuPojo> loginMenuPojos,
			List<LoginSubModule> loginSubModules) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the loginMenuPojos$loginSubMenuPojos in LoginMapper *****");
		LoginModule loginModule = new LoginModule();
		if(Util.isNotEmptyObject(loginMenuPojos))
			   BeanUtils.copyProperties(loginModule,loginMenuPojos.get(0));
		
		loginModule.setLoginSubModules(loginSubModules);
		LOGGER.debug("*** The LoginModule object is ***"+loginModule);
		return loginModule;
	}

	public Department loginMenuPojos$departmentPojo(List<LoginModule> loginModules,
			List<DepartmentPojo> departmentPojos, List<EmployeeRolePojo> employeeRolePojos,EmployeeDepartmentMappingPojo employeeDepartmentMappingPojo) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the loginMenuPojos$departmentPojo in LoginMapper *****");
		Department  department = new Department();
	
		if(Util.isNotEmptyObject(departmentPojos))
			   BeanUtils.copyProperties(department,departmentPojos.get(0));
		
		if(Util.isNotEmptyObject(loginModules))
			department.setLoginModule(loginModules);
		
		if(Util.isNotEmptyObject(employeeRolePojos)) {
			department.setRoleId(Util.isNotEmptyObject(employeeRolePojos.get(0).getRoleId())?employeeRolePojos.get(0).getRoleId():0l);
			department.setName(Util.isNotEmptyObject(employeeRolePojos.get(0).getName())?employeeRolePojos.get(0).getName():"N/A");
			department.setDepartmentRoleMappingId(Util.isNotEmptyObject(employeeDepartmentMappingPojo.getDepartmentRoleMappingId())?employeeDepartmentMappingPojo.getDepartmentRoleMappingId():0l);
		}
		LOGGER.debug("*** The Department obj is ***"+department);	 
		return department;
	}

	public LoginResponse departments$employeeLogIns(List<Department> departments,List<EmployeePojo> employeePojos) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the departments$employeeLogIns in LoginMapper *****");
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setDepartments(departments);
		if(Util.isNotEmptyObject(employeePojos)) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employee, employeePojos.get(0));
		loginResponse.setEmployee(employee);
		loginResponse.setEmpName(Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A");
		loginResponse.setEmpId(Util.isNotEmptyObject(employeePojos.get(0).getEmployeeId())?employeePojos.get(0).getEmployeeId():0l);
		}
		LOGGER.debug("*** The LoginResponse obj is ****"+loginResponse);
		return loginResponse;
	}

	public EmployeeLogInPojo updateEmployeeInPojo(@NonNull EmployeeLogIn employeeLogIn) {
		LOGGER.info("**** The control is inside the updateEmployeeInPojo in LoginMapper *****");
		EmployeeLogInPojo pojo = new EmployeeLogInPojo();
		pojo.setLastLogInTime(new Timestamp(new Date().getTime()));
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setEmployeeId(employeeLogIn.getEmployeeId());
		pojo.setId(employeeLogIn.getId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		LOGGER.info("**** The EmployeeLogInPojo object is *****"+pojo);
		return pojo;
	}
	
	public EmployeeLogInPojo loginInfoToemployeeLogInPojo(LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the loginInfoToemployeeLogInPojo in LoginMapper *****");
		EmployeeLogInPojo employeeLogInPojo = new EmployeeLogInPojo();
		BeanUtils.copyProperties(employeeLogInPojo, loginInfo);
		return employeeLogInPojo;
	}

	public List<Employee> employeePojoListToEmployeeList(List<EmployeePojo> employeePojoList) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the employeePojoListToEmployeeList in LoginMapper *****");
		List<Employee> employeeList  = new ArrayList<>();
		for(EmployeePojo employeePojo : employeePojoList) {
			Employee employee = new Employee();
			BeanUtils.copyProperties(employee, employeePojo);
			employeeList.add(employee);
		}
		return employeeList;
	}  

}
