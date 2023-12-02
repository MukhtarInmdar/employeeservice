package com.sumadhura.employeeservice.rest.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sumadhura.employeeservice.service.mappers.LoginMapper;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

import com.sumadhura.employeeservice.dto.Employee;
import com.sumadhura.employeeservice.dto.Login;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.exception.AuthenticationException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.OtpException;
import com.sumadhura.employeeservice.service.LoginService;

/**
 * LoginRestService class provides Employee login specific services.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:20PM
 */

@Path("/login")
public class LoginRestService {

	@Autowired(required = true)
	@Qualifier("LoginServiceImpl")
	private LoginService loginServiceImpl;

	private static final Logger LOGGER = Logger.getLogger(LoginRestService.class);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/authenticate.spring")
	public Result authenticate(@NonNull Login login) throws IllegalAccessException, InvocationTargetException, AuthenticationException, InSufficeientInputException, InformationNotFoundException {
	LOGGER.info("******* The control is inside the authenticate in LoginRestService *******");
	Result result = null;
	if (Util.isNotEmptyObject(login.getUsername()) && Util.isNotEmptyObject(login.getPassword()) && Util.isNotEmptyObject(login.getRequestUrl())) {
		LoginMapper loginMapper = new LoginMapper();
		result = loginServiceImpl.authenticate(loginMapper.LoginToLoginInfo(login));
	} else {
		List<String> errorMsgs = new ArrayList<String>();
		errorMsgs.add("The Insufficient Input is given for requested service.");
		throw new InSufficeientInputException(errorMsgs);
	}
	return result;
   }
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/departmentSpecificModulesSubmodules.spring")
	public Result departmentSpecificModulesSubmodules(@NonNull Login login) throws IllegalAccessException, InvocationTargetException, AuthenticationException, InSufficeientInputException, InformationNotFoundException {
	LOGGER.info("******* The control is inside the authenticate in LoginRestService *******");
	Result result = null;
	if (Util.isNotEmptyObject(login.getDepartmentRoleMappingId()) && Util.isNotEmptyObject(login.getEmployeeDepartmentMappingId()) && Util.isNotEmptyObject(login.getRequestUrl()) && Util.isNotEmptyObject(login.getDepartmentId()) && Util.isNotEmptyObject(login.getRoleId()) && Util.isNotEmptyObject(login.getEmployeeId())){
		LoginMapper loginMapper = new LoginMapper();
		result = loginServiceImpl.departmentSpecificModulesSubmodules(loginMapper.LoginToLoginInfo(login));
	} else {
		List<String> errorMsgs = new ArrayList<String>();
		errorMsgs.add("The Insufficient Input is given for requested service.");
		throw new InSufficeientInputException(errorMsgs);
	}
	return result;
   }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/changePassword.spring")
	public Result changePassword(@NonNull Login login) throws InSufficeientInputException, IllegalAccessException, InvocationTargetException, AuthenticationException, IOException, OtpException {
		LOGGER.info("******* The control is inside the changePassword in LoginRestService *******");
		if(Util.isNotEmptyObject(login.getPassword()) && Util.isNotEmptyObject(login.getNewPassword())
			&& Util.isNotEmptyObject(login.getEmployeeId()) && Util.isNotEmptyObject(login.getRequestUrl())) {
			return loginServiceImpl.changePassword(new LoginMapper().LoginToLoginInfo(login));
			
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given.");
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sendOtpForgotPassword.spring")
	public Result sendOtpForgotPassword(@NonNull Login login) throws InSufficeientInputException, IllegalAccessException, InvocationTargetException, IOException, OtpException {
		LOGGER.info("******* The control is inside the sendOtpForgotPassword in LoginRestService *******");
		Login result = new Login();
		if(Util.isNotEmptyObject(login.getMobileNo()) && Util.isNotEmptyObject(login.getRequestUrl()) && Util.isEmptyObject(login.getAction())) {
			List<Employee> employeeList = loginServiceImpl.getEmployeeByMobileNo(new LoginMapper().LoginToLoginInfo(login));
			if(Util.isNotEmptyObject(employeeList) && employeeList.size()>1) {
				result.setDescription(HttpStatus.ONE_MOBILE_NUM_MULTI_EMP.getDescription());
				result.setResponseCode(HttpStatus.ONE_MOBILE_NUM_MULTI_EMP.getResponceCode());
				result.setResponseObjList(employeeList);
			}else if(Util.isEmptyObject(employeeList) || Util.isEmptyObject(employeeList.get(0))){
				result.setDescription(HttpStatus.MOBILE_NUMBER_NOT_REGISTERED.getDescription());
				result.setResponseCode(HttpStatus.MOBILE_NUMBER_NOT_REGISTERED.getResponceCode());
			}else {
				if(login.getRequestUrl().equalsIgnoreCase("resendOtp")) {
					result.setOtp(login.getOtp());
					result.setRequestUrl(login.getRequestUrl());
				}
				loginServiceImpl.sendOtpToEmployee(employeeList.get(0), result);
				result.setDescription(HttpStatus.OTP_SEND_SUCCESS.getDescription());
				result.setResponseCode(HttpStatus.OTP_SEND_SUCCESS.getResponceCode());
			}
		}else if(Util.isNotEmptyObject(login) && Util.isNotEmptyObject(login.getRequestUrl())&& Util.isNotEmptyObject(login.getAction()) 
			&& login.getAction().equalsIgnoreCase(MetadataId.MULTIPLE_ACCOUNTS.getName()) && Util.isNotEmptyObject(login.getEmployeeId())) {
			List<Employee> employeeList = loginServiceImpl.getEmployeeByMobileNo(new LoginMapper().LoginToLoginInfo(login));
			if(login.getRequestUrl().equalsIgnoreCase("resendOtp")) {
				result.setOtp(login.getOtp());
				result.setRequestUrl(login.getRequestUrl());
			}
			loginServiceImpl.sendOtpToEmployee(employeeList.get(0), result);
			result.setDescription(HttpStatus.OTP_SEND_SUCCESS.getDescription());
			result.setResponseCode(HttpStatus.OTP_SEND_SUCCESS.getResponceCode());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
}
