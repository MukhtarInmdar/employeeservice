package com.sumadhura.employeeservice.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.sumadhura.employeeservice.dto.Employee;
import com.sumadhura.employeeservice.dto.EmployeeLogIn;
import com.sumadhura.employeeservice.dto.Login;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.exception.AuthenticationException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.OtpException;
import com.sumadhura.employeeservice.service.dto.LoginInfo;


/**
 * LoginService Interface provides Employee login specific services.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */
public interface LoginService {

	public Result authenticate(LoginInfo loginInfo) throws AuthenticationException, IllegalAccessException, InvocationTargetException, InformationNotFoundException;
	public List<EmployeeLogIn> verifyCredentials(LoginInfo loginInfo)throws AuthenticationException, IllegalAccessException, InvocationTargetException, InformationNotFoundException;
	public Result departmentSpecificModulesSubmodules(LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException, InformationNotFoundException;
	public Result changePassword(LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException, AuthenticationException, IOException, OtpException;
	public List<Employee> getEmployeeByMobileNo(LoginInfo loginToLoginInfo) throws IllegalAccessException, InvocationTargetException;
	public void sendOtpToEmployee(Employee employee, Login result) throws IOException, OtpException;
	
}
