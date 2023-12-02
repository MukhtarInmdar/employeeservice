package com.sumadhura.employeeservice.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.Department;
import com.sumadhura.employeeservice.dto.Employee;
import com.sumadhura.employeeservice.dto.EmployeeLogIn;
import com.sumadhura.employeeservice.dto.Login;
import com.sumadhura.employeeservice.dto.LoginModule;
import com.sumadhura.employeeservice.dto.LoginResponse;
import com.sumadhura.employeeservice.dto.LoginSubModule;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.AuthenticationException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.OtpException;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;
import com.sumadhura.employeeservice.persistence.dao.LoginDao;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentRoleMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDepartmentMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLogInPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeMenuSubMenuMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeRoleMenuGroupingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeRolePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeSubMenuSiteMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.LoginMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.LoginSubMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.service.dto.DepartmentRoleInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.LoginInfo;
import com.sumadhura.employeeservice.service.dto.LoginModuleInfo;
import com.sumadhura.employeeservice.service.mappers.LoginMapper;

import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * LoginServiceImpl class provides the Implementation Employee login specific services.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:20PM
 */

@Service("LoginServiceImpl")
public class LoginServiceImpl implements LoginService {

	@Autowired(required=true)
	@Qualifier("LoginDaoImpl")
	private LoginDao loginDaoImpl;
	
	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;
	
	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	

	
	private static final Logger LOGGER = Logger.getLogger(LoginServiceImpl.class);

	@Override
	public List<EmployeeLogIn> verifyCredentials(@NonNull LoginInfo loginInfo) throws AuthenticationException, IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the verifyCredentials in LoginServiceImpl *****");
		LoginMapper loginMapper = new LoginMapper();
		List<EmployeeLogInPojo> employeeLogInPojos = loginDaoImpl.getEmployeeLogInDetails(loginInfo,Status.ACTIVE);
		if(Util.isNotEmptyObject(employeeLogInPojos)) {
			List<EmployeeLogIn> employeeLogIns = loginMapper.EmployeeLogInPojoToEmployeeLogIn(employeeLogInPojos);
			LOGGER.info("***"+loginDaoImpl.updateEmployeeLogin(loginMapper.updateEmployeeInPojo(employeeLogIns.get(0))));
			LOGGER.info("*** The employeeLogIns obj is ****"+employeeLogIns);
			return employeeLogIns;
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** Invalid credentials is given! *****");
			throw new AuthenticationException(errorMsgs);
		}
	}
	
	@Override
	public Result authenticate(@NonNull LoginInfo loginInfo)throws AuthenticationException, IllegalAccessException, InvocationTargetException, InformationNotFoundException {
		LOGGER.info("**** The control is inside the authenticate in LoginServiceImpl *****");
		LoginResponse response = new LoginResponse();
		LoginMapper loginMapper = new LoginMapper();
		List<Department> departments = new ArrayList<Department>();
		List<EmployeeLogIn> employeeLogIns = verifyCredentials(loginInfo);
		/* setting EmployeeId to the login info  */
		loginInfo.setEmployeeId(Util.isNotEmptyObject(employeeLogIns)?Util.isNotEmptyObject(employeeLogIns.get(0).getEmployeeId())?employeeLogIns.get(0).getEmployeeId():0l:0l);
		List<EmployeeDepartmentMappingPojo> employeeDepartmentMappingPojos = loginDaoImpl.getEmployeeDepartmentMappingDetails(loginInfo, Status.ACTIVE);
		LOGGER.debug("*** The employeeDepartmentMappingPojos obj is ***"+employeeDepartmentMappingPojos);
		if(Util.isNotEmptyObject(employeeDepartmentMappingPojos)) {
			/*  employee is registered with two or more departments */
			if(employeeDepartmentMappingPojos.size()>1) {
				 for(EmployeeDepartmentMappingPojo employeeDepartmentMappingPojo : employeeDepartmentMappingPojos) {
					 Department department = new Department();
					 DepartmentRoleInfo departmentRoleInfo =  getDepartmentEmployeeRole(loginInfo,employeeDepartmentMappingPojo);
					 department.setDepartmentName(departmentRoleInfo.getDepartmentPojos().get(0).getDepartmentName());
					 department.setDepartmentId(departmentRoleInfo.getDepartmentPojos().get(0).getDepartmentId());
					 department.setRoleId(departmentRoleInfo.getEmployeeRolePojos().get(0).getRoleId());
					 department.setName(departmentRoleInfo.getEmployeeRolePojos().get(0).getName());
					 department.setEmployeeDepartmentMappingId(employeeDepartmentMappingPojo.getEmployeeDepartmentMappingId());
					 department.setDepartmentRoleMappingId(employeeDepartmentMappingPojo.getDepartmentRoleMappingId());
					 department.setEmployeeId(loginInfo.getEmployeeId());
					 departments.add(department);
				 }
				response.setResponseCode(HttpStatus.twoOrMoreDepartments.getResponceCode());
				response.setDescription(HttpStatus.twoOrMoreDepartments.getDescription());
				response.setDepartments(departments);
				return response;
			
			/*  employee is registered with two or more departments */
			}else {
			    for(EmployeeDepartmentMappingPojo employeeDepartmentMappingPojo : employeeDepartmentMappingPojos) {
				departments.addAll(getDepartmentSpecificMenuAndSubMenu(loginInfo,employeeDepartmentMappingPojo));
			    }
			}
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** Information is not found for employeeDepartmentMappingPojos for given input employeeId *****");
			throw new InformationNotFoundException(errorMsgs);
		}
		/* creating Employee login Response  */
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(employeeLogIns.get(0).getEmployeeId())?employeeLogIns.get(0).getEmployeeId():0l);
		List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,Status.ACTIVE);
		response = loginMapper.departments$employeeLogIns(departments,employeePojos);
		LOGGER.debug("*** The Login Response object is ***"+response);
		response.setResponseCode(HttpStatus.success.getResponceCode());
		response.setDescription(HttpStatus.success.getDescription());
		return response;
	}
	private List<Department> getDepartmentSpecificMenuAndSubMenu(LoginInfo loginInfo,EmployeeDepartmentMappingPojo employeeDepartmentMappingPojo) throws IllegalAccessException, InvocationTargetException, InformationNotFoundException {
		LOGGER.info("**** The control is inside the getDepartmentSpecificMenuAndSubMenu in LoginServiceImpl *****");
		List<Department> departments = new ArrayList<Department>();
		LoginMapper loginMapper = new LoginMapper();
		
		if(Util.isNotEmptyObject(employeeDepartmentMappingPojo)) {
				List<LoginModule> loginModules = new ArrayList<LoginModule>();
				List<EmployeeRoleMenuGroupingPojo> employeeRoleMenuGroupingPojos = null;
				List<DepartmentPojo> departmentPojos = null;
				List<EmployeeRolePojo> employeeRolePojos = null;
				List<LoginMenuPojo> allLoginMenuPojosList = new ArrayList<>();
				Map<Long,List<LoginMenuPojo>> allLoginMenuPojosMap = new LinkedHashMap<>();
				List<LoginSubMenuPojo> allLoginSubMenuPojosList = new ArrayList<>();
				Map<Long,List<LoginSubMenuPojo>> allLoginSubMenuPojosMap = new LinkedHashMap<>();
				List<EmployeeMenuSubMenuMappingPojo> allEmployeeMenuSubMenuMappingPojosList = new ArrayList<>();
				Map<Long,List<EmployeeMenuSubMenuMappingPojo>> allEmployeeMenuSubMenuMappingPojosMap = new LinkedHashMap<>();
				List<EmployeeSubMenuSiteMappingPojo> allEmployeeSubMenuSiteMappingPojosList = new ArrayList<>();
				Map<Long,List<EmployeeSubMenuSiteMappingPojo>> allEmployeeSubMenuSiteMappingPojosMap = new LinkedHashMap<>();
				List<SitePojo> allSitePojosList = new ArrayList<SitePojo>();
				Map<Long,List<SitePojo>> allSitePojosMap = new LinkedHashMap<>();
				List<Long> allMenuSubMenuMappingIds = new ArrayList<>();
				List<EmployeeMenuSubMenuMappingPojo> allEmployeeMenuSubMenuMappingPojosMainList = new ArrayList<>();
				Map<Long,List<EmployeeMenuSubMenuMappingPojo>> allEmployeeMenuSubMenuMappingPojosMainMap = new LinkedHashMap<>();
				/* getting departmentRoleMapping Details  */
				DepartmentRoleInfo departmentRoleInfo = getDepartmentEmployeeRole(loginInfo,employeeDepartmentMappingPojo);
				departmentPojos = departmentRoleInfo.getDepartmentPojos();
				employeeRolePojos = departmentRoleInfo.getEmployeeRolePojos();
				
				/* setting departmentRoleMappingId to get menu and submenu details. */
			    loginInfo.setEmployeeDepartmentMappingId(Util.isNotEmptyObject(employeeDepartmentMappingPojo)?Util.isNotEmptyObject(employeeDepartmentMappingPojo.getEmployeeDepartmentMappingId())?employeeDepartmentMappingPojo.getEmployeeDepartmentMappingId():0l:0l);
			    loginInfo.setDepartmentRoleMappingId(Util.isNotEmptyObject(employeeDepartmentMappingPojo)?Util.isNotEmptyObject(employeeDepartmentMappingPojo.getDepartmentRoleMappingId())?employeeDepartmentMappingPojo.getDepartmentRoleMappingId():0l:0l);
			    /*  getting EmployeeRoleMenu Details */
			    employeeRoleMenuGroupingPojos = loginDaoImpl.getEmployeeRoleMenuGroupingDetails(loginInfo, Status.ACTIVE);
			    LOGGER.debug("*** The employeeRoleMenuGroupingPojos obj is ***"+employeeRoleMenuGroupingPojos);
			    if(Util.isNotEmptyObject(employeeRoleMenuGroupingPojos)){
			    	Set<Long> loginModuleIds = new LinkedHashSet<Long>();
			    	List<LoginModuleInfo> infos= new ArrayList<LoginModuleInfo>();
			    	/* getting Main MenuSubMenuMappingMap */
			    	List<Long> mainEmployeeMenuSubMenuMappingId = new ArrayList<>();
			    	for(EmployeeRoleMenuGroupingPojo employeeRoleMenuGroupingPojo : employeeRoleMenuGroupingPojos) {
			    		mainEmployeeMenuSubMenuMappingId.add(Util.isNotEmptyObject(employeeRoleMenuGroupingPojo)?Util.isNotEmptyObject(employeeRoleMenuGroupingPojo.getMenuMappingId())?employeeRoleMenuGroupingPojo.getMenuMappingId():0l:0l);
			    	}
			    	loginInfo.setEmployeeMenuSubMenuMappingIds(mainEmployeeMenuSubMenuMappingId);
			    	allEmployeeMenuSubMenuMappingPojosMainList = loginDaoImpl.getEmployeeMenuSubMenuMappingDetails(loginInfo, Status.ACTIVE);
			    	for(EmployeeMenuSubMenuMappingPojo employeeMenuSubMenuMappingPojo : allEmployeeMenuSubMenuMappingPojosMainList) {
			    		if(Util.isNotEmptyObject(employeeMenuSubMenuMappingPojo) && Util.isNotEmptyObject(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId())) {
			    			List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingPojoList = new ArrayList<>(); 
			    			if(allEmployeeMenuSubMenuMappingPojosMainMap.containsKey(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId())) {
			    				employeeMenuSubMenuMappingPojoList = allEmployeeMenuSubMenuMappingPojosMainMap.get(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId());
			    			}
			    			employeeMenuSubMenuMappingPojoList.add(employeeMenuSubMenuMappingPojo);
			    			allEmployeeMenuSubMenuMappingPojosMainMap.put(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId(), employeeMenuSubMenuMappingPojoList);
			    		}
			    	}
			    	//
					for(EmployeeRoleMenuGroupingPojo employeeRoleMenuGroupingPojo : employeeRoleMenuGroupingPojos) {
						/* getting employeeMenuSubMenuMapping Details  */
						 loginInfo.setEmployeeMenuSubMenuMappingId(Util.isNotEmptyObject(employeeRoleMenuGroupingPojo)?Util.isNotEmptyObject(employeeRoleMenuGroupingPojo.getMenuMappingId())?employeeRoleMenuGroupingPojo.getMenuMappingId():0l:0l);
						 //List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingPojos = loginDaoImpl.getEmployeeMenuSubMenuMappingDetails(loginInfo, Status.ACTIVE);
						 List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingPojos = allEmployeeMenuSubMenuMappingPojosMainMap.get(loginInfo.getEmployeeMenuSubMenuMappingId());
						 if(Util.isEmptyObject(employeeMenuSubMenuMappingPojos)) {
							 employeeMenuSubMenuMappingPojos = new ArrayList<>();
						 }
						 LOGGER.debug("*** The employeeMenuSubMenuMappingPojos obj is ***"+employeeMenuSubMenuMappingPojos);
						 if(Util.isNotEmptyObject(employeeMenuSubMenuMappingPojos)) {
						 for(EmployeeMenuSubMenuMappingPojo menuSubMenuMappingPojo : employeeMenuSubMenuMappingPojos) {
							 LoginModuleInfo info = new LoginModuleInfo();
							 info.setMenuSubMenuMappingId(Util.isNotEmptyObject(menuSubMenuMappingPojo)?Util.isNotEmptyObject(menuSubMenuMappingPojo.getMenuSubMenuMappingId())?menuSubMenuMappingPojo.getMenuSubMenuMappingId():0l:0l);
							 info.setModuleId(Util.isNotEmptyObject(menuSubMenuMappingPojo)?Util.isNotEmptyObject(menuSubMenuMappingPojo.getModuleId())?menuSubMenuMappingPojo.getModuleId():0l:0l);
							 info.setSubModuleId(Util.isNotEmptyObject(menuSubMenuMappingPojo)?Util.isNotEmptyObject(menuSubMenuMappingPojo.getSubModuleId())?menuSubMenuMappingPojo.getSubModuleId():0l:0l);
							 loginModuleIds.add(Util.isNotEmptyObject(menuSubMenuMappingPojo)?Util.isNotEmptyObject(menuSubMenuMappingPojo.getModuleId())?menuSubMenuMappingPojo.getModuleId():0l:0l);
							 infos.add(info);
							 allMenuSubMenuMappingIds.add(Util.isNotEmptyObject(menuSubMenuMappingPojo)?Util.isNotEmptyObject(menuSubMenuMappingPojo.getMenuSubMenuMappingId())?menuSubMenuMappingPojo.getMenuSubMenuMappingId():0l:0l);
					   }
					}
				 }
				Map<Long,List<Long>> moduleSubModuleMap =	menuAndSubMenuMapping(infos);
				/* getting all Menu Details Map */
				List<Long> allSubModuleIdsList = new ArrayList<>();
				loginInfo.setModuleIds(new ArrayList<>(loginModuleIds));
				allLoginMenuPojosList = loginDaoImpl.getEmployeeMenuDetails(loginInfo, Status.ACTIVE);
				for(LoginMenuPojo loginMenuPojo : allLoginMenuPojosList) {
					if(Util.isNotEmptyObject(loginMenuPojo) && Util.isNotEmptyObject(loginMenuPojo.getModuleId())) {
						List<LoginMenuPojo> loginMenuPojosList = new ArrayList<>();
						if(allLoginMenuPojosMap.containsKey(loginMenuPojo.getModuleId())) {
							loginMenuPojosList = allLoginMenuPojosMap.get(loginMenuPojo.getModuleId());
						}
						loginMenuPojosList.add(loginMenuPojo);
						allLoginMenuPojosMap.put(loginMenuPojo.getModuleId(),loginMenuPojosList);
						allSubModuleIdsList.addAll((List<Long>)moduleSubModuleMap.get(loginMenuPojo.getModuleId()));
					}
				}
				/* getting all Sub Module Details Map */
				loginInfo.setSubModuleIds(allSubModuleIdsList);
				allLoginSubMenuPojosList = loginDaoImpl.getEmployeeSubMenuDetails(loginInfo, Status.ACTIVE);
				for(LoginSubMenuPojo loginSubMenuPojo : allLoginSubMenuPojosList) {
					if(Util.isNotEmptyObject(loginSubMenuPojo) && Util.isNotEmptyObject(loginSubMenuPojo.getSubModuleId())) {
						List<LoginSubMenuPojo> loginSubMenuPojoList = new ArrayList<>();
						if(allLoginSubMenuPojosMap.containsKey(loginSubMenuPojo.getSubModuleId())) {
							loginSubMenuPojoList = allLoginSubMenuPojosMap.get(loginSubMenuPojo.getSubModuleId());
						}
						loginSubMenuPojoList.add(loginSubMenuPojo);
						allLoginSubMenuPojosMap.put(loginSubMenuPojo.getSubModuleId(),loginSubMenuPojoList);
					}
				}
				/* getting all Menu SubMenu Mapping Details Map */
				loginInfo.setIdentifier("menu specific submenu");
				loginInfo.setEmployeeMenuSubMenuMappingIds(allMenuSubMenuMappingIds);
				allEmployeeMenuSubMenuMappingPojosList = loginDaoImpl.getEmployeeMenuSubMenuMappingDetails(loginInfo, Status.ACTIVE);
				for(EmployeeMenuSubMenuMappingPojo employeeMenuSubMenuMappingPojo : allEmployeeMenuSubMenuMappingPojosList) {
					if(Util.isNotEmptyObject(employeeMenuSubMenuMappingPojo) && Util.isNotEmptyObject(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId())) {
						List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingPojoList = new ArrayList<>();
						if(allEmployeeMenuSubMenuMappingPojosMap.containsKey(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId())){
							employeeMenuSubMenuMappingPojoList = allEmployeeMenuSubMenuMappingPojosMap.get(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId());
						}
						employeeMenuSubMenuMappingPojoList.add(employeeMenuSubMenuMappingPojo);
						allEmployeeMenuSubMenuMappingPojosMap.put(employeeMenuSubMenuMappingPojo.getMenuSubMenuMappingId(),employeeMenuSubMenuMappingPojoList);
						
					}
				}
				/* getting all Menu SubMenu Site Mapping Details Map */
				allEmployeeSubMenuSiteMappingPojosList = loginDaoImpl.getEmployeeSubMenuSiteMappingDetails(loginInfo, Status.ACTIVE);
				for(EmployeeSubMenuSiteMappingPojo employeeSubMenuSiteMappingPojo : allEmployeeSubMenuSiteMappingPojosList) {
					if(Util.isNotEmptyObject(employeeSubMenuSiteMappingPojo) && Util.isNotEmptyObject(employeeSubMenuSiteMappingPojo.getMenuSubMenuMappingId())) {
						List<EmployeeSubMenuSiteMappingPojo> employeeSubMenuSiteMappingPojoList = new ArrayList<>();
						if(allEmployeeSubMenuSiteMappingPojosMap.containsKey(employeeSubMenuSiteMappingPojo.getMenuSubMenuMappingId())) {
							employeeSubMenuSiteMappingPojoList = allEmployeeSubMenuSiteMappingPojosMap.get(employeeSubMenuSiteMappingPojo.getMenuSubMenuMappingId());
						}
						employeeSubMenuSiteMappingPojoList.add(employeeSubMenuSiteMappingPojo);
						allEmployeeSubMenuSiteMappingPojosMap.put(employeeSubMenuSiteMappingPojo.getMenuSubMenuMappingId(),employeeSubMenuSiteMappingPojoList);
					}
				}
				/* getting all Site Details Map */
				allSitePojosList = loginDaoImpl.getSiteDetails(loginInfo, Status.COMPLETED);
				for(SitePojo sitePojo : allSitePojosList) {
					if(Util.isNotEmptyObject(sitePojo) && Util.isNotEmptyObject(sitePojo.getSiteId())) {
						List<SitePojo> sitePojoList = new ArrayList<>();
						if(allSitePojosMap.containsKey(sitePojo.getSiteId())) {
							sitePojoList = allSitePojosMap.get(sitePojo.getSiteId());
						}
						sitePojoList.add(sitePojo);
						allSitePojosMap.put(sitePojo.getSiteId(),sitePojoList);
					}
				}
				//
				if(Util.isNotEmptyObject(loginModuleIds)) {
					  /* removing nulls and zero's from set */
					  loginModuleIds.remove(null);
					  loginModuleIds.remove(0l);
					 
				     for( Long moduleId : loginModuleIds) {
				     List<LoginMenuPojo> loginMenuPojos = new ArrayList<LoginMenuPojo>();	 
				     List<LoginSubModule> loginSubModules = new ArrayList<LoginSubModule>(); 
				      /* getting Employee Menu Details   */	 
				     loginInfo.setModuleId(Util.isNotEmptyObject(moduleId)?moduleId:0l);
					 //loginMenuPojos.addAll(loginDaoImpl.getEmployeeMenuDetails(loginInfo, Status.ACTIVE));
				     loginMenuPojos = allLoginMenuPojosMap.get(moduleId);
				     if(Util.isEmptyObject(loginMenuPojos)) {
				    	 loginMenuPojos = new ArrayList<>();
				     }
					 
					 LOGGER.debug("*** The loginMenuPojos obj is ***"+loginMenuPojos);
			
					  /* getting submodules based on module Id */
					  if(Util.isNotEmptyObject(moduleSubModuleMap)) {
					       for(Long subModuleId : ((List<Long>)moduleSubModuleMap.get(moduleId))) {
					       List<LoginSubMenuPojo> loginSubMenuPojos = new ArrayList<LoginSubMenuPojo>();
					       List<SitePojo> sitePojos = new ArrayList<SitePojo>();
					       /* getting Employee SubMenu Details by subModuleId */ 
					       loginInfo.setSubModuleId(Util.isNotEmptyObject(subModuleId)?subModuleId:0l);
					       //loginSubMenuPojos =  loginDaoImpl.getEmployeeSubMenuDetails(loginInfo, Status.ACTIVE);
					       loginSubMenuPojos = allLoginSubMenuPojosMap.get(subModuleId);
					       if(Util.isEmptyObject(loginSubMenuPojos)) {
					    	   loginSubMenuPojos = new ArrayList<>();
					       }
					       LOGGER.debug("*** The loginSubMenuPojos obj is ***"+loginSubMenuPojos);
					      
					       LoginInfo dest = new LoginInfo();
					       BeanUtils.copyProperties(dest, loginInfo);
					       dest.setIdentifier("menu specific submenu");
					       
					       /* here add menu mapping id also */
					             for(LoginModuleInfo info : infos) {
					        	 if(info.getModuleId().equals(dest.getModuleId()) && info.getSubModuleId().equals(dest.getSubModuleId())) {
					        		 dest.setEmployeeMenuSubMenuMappingId(Util.isNotEmptyObject(info)?Util.isNotEmptyObject(info.getMenuSubMenuMappingId())?info.getMenuSubMenuMappingId():0l:0l);
					        	 }
					         }
					       /* Error prone code */
					       //List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingPojos = loginDaoImpl.getEmployeeMenuSubMenuMappingDetails(dest, Status.ACTIVE);
					       List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingPojos = allEmployeeMenuSubMenuMappingPojosMap.get(dest.getEmployeeMenuSubMenuMappingId());
						   if(Util.isEmptyObject(employeeMenuSubMenuMappingPojos)) {
							   employeeMenuSubMenuMappingPojos = new ArrayList<>();
						   }
					       LOGGER.debug("*** The employeeMenuSubMenuMappingPojos obj is ***"+employeeMenuSubMenuMappingPojos);
					       
					       for(EmployeeMenuSubMenuMappingPojo  menuSubMenuMappingPojo : employeeMenuSubMenuMappingPojos) {
					       /* getting Employee SubMenu Site Details */ 
					       loginInfo.setEmployeeMenuSubMenuMappingId(Util.isNotEmptyObject(menuSubMenuMappingPojo)?Util.isNotEmptyObject(menuSubMenuMappingPojo.getMenuSubMenuMappingId())?menuSubMenuMappingPojo.getMenuSubMenuMappingId():0l:0l);
					       //List<EmployeeSubMenuSiteMappingPojo> employeeSubMenuSiteMappingPojos =  loginDaoImpl.getEmployeeSubMenuSiteMappingDetails(loginInfo, Status.ACTIVE);
					       List<EmployeeSubMenuSiteMappingPojo> employeeSubMenuSiteMappingPojos = allEmployeeSubMenuSiteMappingPojosMap.get(loginInfo.getEmployeeMenuSubMenuMappingId());
							 LOGGER.debug("*** The employeeSubMenuSiteMappingPojos obj is ***"+employeeSubMenuSiteMappingPojos);
						     if(Util.isNotEmptyObject(employeeSubMenuSiteMappingPojos)) {
						    	 for(EmployeeSubMenuSiteMappingPojo employeeSubMenuSiteMappingPojo : employeeSubMenuSiteMappingPojos) {
						    		 loginInfo.setSiteId(Util.isNotEmptyObject(employeeSubMenuSiteMappingPojo)?Util.isNotEmptyObject(employeeSubMenuSiteMappingPojo.getSiteId())?employeeSubMenuSiteMappingPojo.getSiteId():0l:0l);
						    		 //sitePojos.addAll(loginDaoImpl.getSiteDetails(loginInfo, Status.COMPLETED));
						    		 List<SitePojo> sitePojoList = allSitePojosMap.get(loginInfo.getSiteId());
						    		 if(Util.isEmptyObject(sitePojoList)) {
						    			 sitePojoList = new ArrayList<>();
						    		 }
						    		 sitePojos.addAll(sitePojoList);
						    	 }
						      }
					       } 
						     /* creating submodule site array */
						     loginSubModules.add(loginMapper.loginSubMenuPojos$sitePojos(loginSubMenuPojos,sitePojos));
	     		          }
					  }
				    /* creating module submodule site array  */
					 loginModules.add(loginMapper.loginMenuPojos$loginSubMenuPojos(loginMenuPojos,loginSubModules));
				   }
				}	
				}else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("***** Information is not found for employeeRoleMenuGroupingPojos for given input EmployeeDepartmentMappingId *****");
					throw new InformationNotFoundException(errorMsgs);
				}
			    /* creating Department array  */
			    departments.add(loginMapper.loginMenuPojos$departmentPojo(loginModules,departmentPojos,employeeRolePojos,employeeDepartmentMappingPojo));    
			}
		  return departments;
	}
   private Map<Long,List<Long>> menuAndSubMenuMapping(@NonNull List<LoginModuleInfo> loginModuleInfos){
		LOGGER.info("**** The control is inside the menuAndSubMenuMapping in LoginServiceImpl *****");
		Set<Long> moduleIds = new HashSet<Long>();
		Map<Long,List<Long>> moduleMap = new HashMap<Long,List<Long>>();
		for(LoginModuleInfo info : loginModuleInfos)
			moduleIds.add(info.getModuleId());
		for(Long Id : moduleIds) {
			List<Long> subModulesIds = new ArrayList<Long>();
			for(LoginModuleInfo info : loginModuleInfos) {
				if(info.getModuleId().equals(Id)) {
					subModulesIds.add(info.getSubModuleId());
				}
			}
			moduleMap.put(Id, subModulesIds);
		}
		return moduleMap;
	}
  private  DepartmentRoleInfo getDepartmentEmployeeRole(LoginInfo loginInfo,EmployeeDepartmentMappingPojo employeeDepartmentMappingPojo) {
	  LOGGER.info("**** The control is inside the getDepartmentRoleMappingPojo in LoginServiceImpl *****");
	    DepartmentRoleInfo departmentRoleInfo = new DepartmentRoleInfo();
	    List<DepartmentPojo> departmentPojos = null;
		List<EmployeeRolePojo> employeeRolePojos = null;
		loginInfo.setDepartmentRoleMappingId(Util.isNotEmptyObject(employeeDepartmentMappingPojo)?Util.isNotEmptyObject(employeeDepartmentMappingPojo.getDepartmentRoleMappingId())?employeeDepartmentMappingPojo.getDepartmentRoleMappingId():0l:0l);
		/* getting departmentRoleMapping Details  */
		List<DepartmentRoleMappingPojo> departmentRoleMappingPojos = loginDaoImpl.getDepartmentRoleMappingDetails(loginInfo, Status.ACTIVE);
		LOGGER.debug("*** The departmentRoleMappingPojos obj is ***"+departmentRoleMappingPojos);
		/* Getting Employee Details */
		EmployeeTicketRequestInfo empTicketReqInfo = new EmployeeTicketRequestInfo();
		empTicketReqInfo.setEmployeeId(loginInfo.getEmployeeId());
		List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(empTicketReqInfo, Status.ACTIVE);
		if(Util.isNotEmptyObject(departmentRoleMappingPojos)) {
	    for(DepartmentRoleMappingPojo departmentRoleMappingPojo : departmentRoleMappingPojos){ 
	    	loginInfo.setDepartmentId(Util.isNotEmptyObject(departmentRoleMappingPojo)?departmentRoleMappingPojo.getDepartmentId():0l);
	    	loginInfo.setRoleId(Util.isNotEmptyObject(departmentRoleMappingPojo)?departmentRoleMappingPojo.getRoleId():0l);
	    	EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
	    	employeeTicketRequestInfo.setDepartmentId(Util.isNotEmptyObject(departmentRoleMappingPojo)?departmentRoleMappingPojo.getDepartmentId():0l);
	    	/* getting Department Details  */
	    	departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
	    	LOGGER.debug("*** The departmentPojos obj is ***"+departmentPojos);
	    	/* getting Employee Roles Details  */
	    	 employeeRolePojos = loginDaoImpl.getEmployeeRoleDetails(loginInfo, Status.ACTIVE);
	    	 if(Util.isNotEmptyObject(employeeDetailsPojos) && Util.isNotEmptyObject(employeeDetailsPojos.get(0)) 
	    		&& Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeDesignation()) && Util.isNotEmptyObject(employeeRolePojos) 
	    		&& Util.isNotEmptyObject(employeeRolePojos.get(0))) {
	    		 for(EmployeeRolePojo employeeRolePojo : employeeRolePojos) {
	    			 employeeRolePojo.setName(employeeDetailsPojos.get(0).getEmployeeDesignation()); 
	    		 }
	    	 }
	    	LOGGER.debug("*** The employeeRolePojos obj is ***"+employeeRolePojos);
	      }
		}
		departmentRoleInfo.setDepartmentPojos(departmentPojos);
		departmentRoleInfo.setEmployeeRolePojos(employeeRolePojos);
	    return departmentRoleInfo;
  }
  @Override
  public Result departmentSpecificModulesSubmodules(@NonNull LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException, InformationNotFoundException {
	 LOGGER.info("**** The control is inside the departmentSpecificModulesSubmodules in LoginServiceImpl *****");
	 LoginResponse response = new LoginResponse();
	 LoginMapper loginMapper = new LoginMapper();
	 List<Department> departments = new ArrayList<Department>();
	 
	 EmployeeDepartmentMappingPojo employeeDepartmentMappingPojo = new EmployeeDepartmentMappingPojo();
	 employeeDepartmentMappingPojo.setDepartmentRoleMappingId(Util.isNotEmptyObject(loginInfo.getDepartmentRoleMappingId())?loginInfo.getDepartmentRoleMappingId():0l); 
	 employeeDepartmentMappingPojo.setEmployeeDepartmentMappingId(Util.isNotEmptyObject(loginInfo.getDepartmentRoleMappingId())?loginInfo.getDepartmentRoleMappingId():0l); 
	 
	  departments.addAll(getDepartmentSpecificMenuAndSubMenu(loginInfo,employeeDepartmentMappingPojo));
		   
	 /* creating Employee login Response  */
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(loginInfo.getEmployeeId())?loginInfo.getEmployeeId():0l);
		List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,Status.ACTIVE);
		response = loginMapper.departments$employeeLogIns(departments,employeePojos);
		LOGGER.debug("*** The Login Response object is ***"+response);
		response.setResponseCode(HttpStatus.success.getResponceCode());
		response.setDescription(HttpStatus.success.getDescription());
		return response;
    } 

	@Override
	public Result changePassword(LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException, AuthenticationException, IOException, OtpException {
		LOGGER.info("**** The control is inside the changePassword method in LoginServiceImpl *****"); 
		Result result = new Result();
		/* checking old password */
		Boolean isOldPasswordCorrect = false;
		EmployeeLogInPojo employeeLogInPojo = new LoginMapper().loginInfoToemployeeLogInPojo(loginInfo);
		if(loginInfo.getRequestUrl().equalsIgnoreCase("changePassword")) {
			isOldPasswordCorrect = loginDaoImpl.checkOldPassword(employeeLogInPojo);
		}
		/* updating new password */
		if(isOldPasswordCorrect || (loginInfo.getRequestUrl().equalsIgnoreCase("forgotPassword"))) {
			employeeLogInPojo.setPassword(loginInfo.getNewPassword());
			Long updatedCount = loginDaoImpl.changePassword(employeeLogInPojo);
			/* sending success mail to customer */
			if(Util.isNotEmptyObject(updatedCount) && updatedCount>0) {
				EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
				employeeTicketRequestInfo.setEmployeeId(loginInfo.getEmployeeId());
				List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
				if(Util.isNotEmptyObject(employeePojos) && Util.isNotEmptyObject(employeePojos.get(0))){
					/* sending change password success mail */
					if(Util.isNotEmptyObject(employeePojos.get(0).getEmail())) {
						mailServiceImpl.sendEmployeeMailForChangePassword(employeePojos);
					}
					/* sending change password success mail */
					if(Util.isNotEmptyObject(employeePojos.get(0).getMobileNumber())) {
						/* preparing Message for Employee */
						String employeeName = Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"Employee";
						String message = "Dear "+employeeName +", your account Password has been changed successfully.";
						//mobileOTPSMS.sendChangePasswordMessage(new ArrayList<String>(Arrays.asList(employeePojos.get(0).getMobileNumber())), employeePojos.get(0), message);
					}
				}
				result.setDescription(HttpStatus.success.getDescription());
				result.setResponseCode(HttpStatus.success.getResponceCode());
			}else {
				result.setDescription(HttpStatus.failure.getDescription());
				result.setResponseCode(HttpStatus.failure.getResponceCode());
			}
		}else {
			result.setDescription(HttpStatus.INCORRECT_PASSWORD.getDescription());
			result.setResponseCode(HttpStatus.INCORRECT_PASSWORD.getResponceCode());
		}
		return result;
	}
	
	@Override
	public List<Employee> getEmployeeByMobileNo(LoginInfo loginInfo) throws IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The control is inside the getEmployeeByMobileNo method in LoginServiceImpl *****");
		List<EmployeePojo> employeePojoList = loginDaoImpl.getEmployeeByMobileNo(loginInfo);
		return new LoginMapper().employeePojoListToEmployeeList(employeePojoList);
	}
	
	@Override
	public void sendOtpToEmployee(Employee employee, Login result) throws IOException, OtpException {
		LOGGER.info("**** The control is inside the sendOtpToEmployee method in LoginServiceImpl *****");
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setEmployeeName(Util.isNotEmptyObject(employee.getEmployeeName())?employee.getEmployeeName():"Employee");
		loginInfo.setUsername(Util.isNotEmptyObject(employee.getUsername())?employee.getUsername():"N/A");
		if(Util.isNotEmptyObject(result.getRequestUrl()) && result.getRequestUrl().equalsIgnoreCase("resendOtp")) {
			loginInfo.setOtp(result.getOtp());
			loginInfo.setRequestUrl(result.getRequestUrl());
		}
		Integer otp = 0;//mobileOTPSMS.sendOTP(new ArrayList<String>(Arrays.asList(employee.getMobileNumber())), loginInfo);
		/* setting Employee details in response */
		result.setOtp(otp);
		result.setEmployeeName(Util.isNotEmptyObject(employee.getEmployeeName())?employee.getEmployeeName():"Employee");
		result.setUsername(Util.isNotEmptyObject(employee.getUsername())?employee.getUsername():"N/A");
		result.setEmail(Util.isNotEmptyObject(employee.getEmail())?employee.getEmail():"N/A");
		result.setEmployeeId(Util.isNotEmptyObject(employee.getEmployeeId())?employee.getEmployeeId():0l);
		result.setMobileNo(Util.isNotEmptyObject(employee.getMobileNumber())?employee.getMobileNumber():"");
	}  
   
}
