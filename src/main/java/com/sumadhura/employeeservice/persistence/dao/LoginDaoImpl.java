/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.SqlQuery;
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
import com.sumadhura.employeeservice.service.mappers.ExtractDataFromResultSet;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * LoginDao Interface provides Employee login specific functionalities.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */

@Repository("LoginDaoImpl")
public class LoginDaoImpl implements LoginDao{

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	private static final Logger LOGGER = Logger.getLogger(LoginDaoImpl.class);
	
	@Override
	public List<EmployeeLogInPojo> getEmployeeLogInDetails(@NonNull LoginInfo loginInfo,Status status){
		LOGGER.info("**** The control is inside the authenticateCredentials in LoginDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_LOGIN_DETAILS).
				append(SqlQuery.QRY_TO_GET_EMPLOYEE_LOGIN_WTO_USERNAME_PASSWORD)
			   .toString(); 
		
		LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_LOGIN_DETAILS IS *****"+query);	
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("USERNAME",loginInfo.getUsername().toLowerCase());
		namedParameters.addValue("PASSWORD",loginInfo.getPassword());
		namedParameters.addValue("STATUS",status.status, Types.INTEGER);
		
		final long startTime = new Date().getTime();
		
		List<List<EmployeeLogInPojo>> employeeLogInPojoLISTS = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<EmployeeLogInPojo>>() {
					public List<EmployeeLogInPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						long endTime = new Date().getTime();
						LOGGER.info("*** The Total connection Time is  ****" + (startTime - endTime));
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeLogInPojo> resultSetMapper = new ResultSetMapper<EmployeeLogInPojo>();
						List<EmployeeLogInPojo> employeeLogInPojoLIST = null;
						try {
							employeeLogInPojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeLogInPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The EmployeeLogInPojoLIST objects  is *****" + employeeLogInPojoLIST);
						return employeeLogInPojoLIST;
					}
				});
		LOGGER.info("*** The EmployeeLogInPojoLISTS is *****"+employeeLogInPojoLISTS);
		if(employeeLogInPojoLISTS.size()==0) {
		employeeLogInPojoLISTS.add(new ArrayList<EmployeeLogInPojo>());
		}
		LOGGER.info("**** The EmployeeLogInPojoLIST is ****"+employeeLogInPojoLISTS.get(0));
		return employeeLogInPojoLISTS.get(0);
	}
	@Override
	public List<EmployeeDepartmentMappingPojo> getEmployeeDepartmentMappingDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getEmployeeDepartmentMappingDetails in LoginDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_DEPARTMENT_MAPING_DETAILS)
				       .append(SqlQuery.QRY_TO_GET_EMPLOYEE_DEPARTMENT_MAPING_DETAILS_WTO_EMPLOYEE_ID)
				       .toString();
		LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_DEPARTMENT_MAPING_DETAILS IS *****"+query);	
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("EMPLOYEE_ID",loginInfo.getEmployeeId(),Types.BIGINT);
		namedParameters.addValue("STATUS",status.status, Types.INTEGER);
		
		List<List<EmployeeDepartmentMappingPojo>> employeeDepartmentMappingPojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<EmployeeDepartmentMappingPojo>>() {
		
					public List<EmployeeDepartmentMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeDepartmentMappingPojo> resultSetMapper = new ResultSetMapper<EmployeeDepartmentMappingPojo>();
						List<EmployeeDepartmentMappingPojo> employeeDepartmentMappingPojoLIST = null;
						try {
							employeeDepartmentMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeDepartmentMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The EmployeeDepartmentMappingPojoLIST objects  is *****" + employeeDepartmentMappingPojoLIST);						
						return employeeDepartmentMappingPojoLIST;
					}
				});
		LOGGER.info("*** The EmployeeDepartmentMappingPojoLISTS is *****"+employeeDepartmentMappingPojoLISTS);
		if(employeeDepartmentMappingPojoLISTS.size()==0) {
			employeeDepartmentMappingPojoLISTS.add(new ArrayList<EmployeeDepartmentMappingPojo>());
		}
		LOGGER.info("**** The EmployeeDepartmentMappingPojoLISTS is ****"+employeeDepartmentMappingPojoLISTS.get(0));
		return employeeDepartmentMappingPojoLISTS.get(0);
	}

	@Override
	public List<DepartmentRoleMappingPojo> getDepartmentRoleMappingDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_DEPARTMENT_ROLE_MAPING_DETAILS)
			       .append(SqlQuery.QRY_TO_GET_DEPARTMENT_ROLE_MAPING_DETAILS_WTO_DEPARTMENT_ROLE_MAPPING_ID)
			       .toString();
	    LOGGER.info("**** THE QRY_TO_GET_DEPARTMENT_ROLE_MAPING_DETAILS IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("DEPARTMENT_ROLE_MAPPING_ID",loginInfo.getDepartmentRoleMappingId(),Types.BIGINT);
		namedParameters.addValue("STATUS",status.status, Types.INTEGER);
		
		List<List<DepartmentRoleMappingPojo>> departmentRoleMappingPojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<DepartmentRoleMappingPojo>>() {
					public List<DepartmentRoleMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<DepartmentRoleMappingPojo> resultSetMapper = new ResultSetMapper<DepartmentRoleMappingPojo>();
						List<DepartmentRoleMappingPojo> departmentRoleMappingPojoLIST = null;
						try {
							departmentRoleMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, DepartmentRoleMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The departmentRoleMappingPojoLIST objects  is *****" + departmentRoleMappingPojoLIST);						
						return departmentRoleMappingPojoLIST;
					}
				});
		LOGGER.info("*** The departmentRoleMappingPojoLISTS is *****"+departmentRoleMappingPojoLISTS);
		if(departmentRoleMappingPojoLISTS.size()==0) {
			departmentRoleMappingPojoLISTS.add(new ArrayList<DepartmentRoleMappingPojo>());
		}
		LOGGER.info("**** The departmentRoleMappingPojoLISTS is ****"+departmentRoleMappingPojoLISTS.get(0));
		return departmentRoleMappingPojoLISTS.get(0);
	}
	
	@Override
	public List<EmployeeRoleMenuGroupingPojo> getEmployeeRoleMenuGroupingDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_ROLE_MENU_GROUPING)
				   .append(SqlQuery.QRY_TO_GET_EMP_ROLE_MENU_SUBMENU_MENU_MODULE_JOIN)
			       .append(SqlQuery.QRY_TO_GET_EMPLOYEE_ROLE_MENU_GROUPING_WTO_DEPARTMENT_ROLE_MAPPING_ID)
			       .toString();
	    LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_ROLE_MENU_GROUPING IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	//	namedParameters.addValue("EMPLOYEE_DEPARTMENT_MAPING_ID",loginInfo.getDepartmentRoleMappingId(),Types.BIGINT);
		namedParameters.addValue("DEPARTMENT_ROLE_MAPPING_ID",loginInfo.getDepartmentRoleMappingId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",status.status, Types.INTEGER);
		
		List<List<EmployeeRoleMenuGroupingPojo>> employeeRoleMenuGroupingPojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<EmployeeRoleMenuGroupingPojo>>() {
					public List<EmployeeRoleMenuGroupingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeRoleMenuGroupingPojo> resultSetMapper = new ResultSetMapper<EmployeeRoleMenuGroupingPojo>();
						List<EmployeeRoleMenuGroupingPojo> employeeRoleMenuGroupingPojoLIST = null;
						try {
							employeeRoleMenuGroupingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeRoleMenuGroupingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The employeeRoleMenuGroupingPojoLIST objects  is *****" + employeeRoleMenuGroupingPojoLIST);						
						return employeeRoleMenuGroupingPojoLIST;
					}
				});
		LOGGER.info("*** The employeeRoleMenuGroupingPojoLISTS is *****"+employeeRoleMenuGroupingPojoLISTS);
		if(employeeRoleMenuGroupingPojoLISTS.size()==0) {
			employeeRoleMenuGroupingPojoLISTS.add(new ArrayList<EmployeeRoleMenuGroupingPojo>());
		}
		LOGGER.info("**** The employeeRoleMenuGroupingPojoLISTS is ****"+employeeRoleMenuGroupingPojoLISTS.get(0));
		return employeeRoleMenuGroupingPojoLISTS.get(0);
	}
	
	@Override
	public List<EmployeeMenuSubMenuMappingPojo> getEmployeeMenuSubMenuMappingDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_MENU_SUBMENU_MAPPING_DETAILS).toString();
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		
		if(Util.isNotEmptyObject(loginInfo.getIdentifier())?loginInfo.getIdentifier().equalsIgnoreCase("menu specific submenu"):false) {
			query = new StringBuilder(query)
		      .append(SqlQuery.QRY_TO_GET_EMPLOYEE_MENU_SUBMENU_MAPPING_DETAILS_WTO_MODULE_ID_SUB_MODULE_ID)
		      .toString();
		    namedParameters.addValue("MENU_SUB_MENU_MAPPING_ID",loginInfo.getEmployeeMenuSubMenuMappingIds(),Types.BIGINT);
			namedParameters.addValue("MODULE_ID",loginInfo.getModuleIds(),Types.BIGINT);
			namedParameters.addValue("SUBMODULE_ID",loginInfo.getSubModuleIds(),Types.BIGINT);
					
		}else {
			query = new StringBuilder(query)
			   .append(SqlQuery.QRY_TO_GET_EMPLOYEE_MENU_SUBMENU_MAPPING_DETAILS_WTO_MENU_SUB_MENU_MAPPING_ID)
			   .toString();
			namedParameters.addValue("MENU_SUB_MENU_MAPPING_ID",loginInfo.getEmployeeMenuSubMenuMappingIds(),Types.BIGINT);
		}		       
	    LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_MENU_SUBMENU_MAPPING_DETAILS IS *****"+query);
	    
		namedParameters.addValue("STATUS_ID",status.status, Types.INTEGER);
		
		List<List<EmployeeMenuSubMenuMappingPojo>> employeeMenuSubMenuMappingLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<EmployeeMenuSubMenuMappingPojo>>() {
					public List<EmployeeMenuSubMenuMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeMenuSubMenuMappingPojo> resultSetMapper = new ResultSetMapper<EmployeeMenuSubMenuMappingPojo>();
						List<EmployeeMenuSubMenuMappingPojo> employeeMenuSubMenuMappingLIST = null;
						try {
							employeeMenuSubMenuMappingLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeMenuSubMenuMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The employeeMenuSubMenuMappingLIST objects  is *****" + employeeMenuSubMenuMappingLIST);					
						return employeeMenuSubMenuMappingLIST;
					}
				});
		LOGGER.info("*** The employeeMenuSubMenuMappingLISTS is *****"+employeeMenuSubMenuMappingLISTS);
		if(employeeMenuSubMenuMappingLISTS.size()==0) {
			employeeMenuSubMenuMappingLISTS.add(new ArrayList<EmployeeMenuSubMenuMappingPojo>());
		}
		LOGGER.info("**** The employeeMenuSubMenuMappingLISTS is ****"+employeeMenuSubMenuMappingLISTS.get(0));
		return employeeMenuSubMenuMappingLISTS.get(0);
	}
	

	@Override
	public List<LoginMenuPojo> getEmployeeMenuDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_MENU_DETAILS)
			       .append(SqlQuery.QRY_TO_GET_EMPLOYEE_MENU_DETAILS_WTO_MENU_ID)
			       .toString();
	    LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_MENU_DETAILS IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MODULE_ID",loginInfo.getModuleIds(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",status.status, Types.INTEGER);
		
		List<List<LoginMenuPojo>> LoginMenuPojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<LoginMenuPojo>>() {
					public List<LoginMenuPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<LoginMenuPojo> resultSetMapper = new ResultSetMapper<LoginMenuPojo>();
						List<LoginMenuPojo> LoginMenuPojoLIST = null;
						try {
							LoginMenuPojoLIST = resultSetMapper.mapRersultSetToObject(rs, LoginMenuPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The LoginMenuPojoLIST objects  is *****" + LoginMenuPojoLIST);					
						return LoginMenuPojoLIST;
					}
				});
		LOGGER.info("*** The LoginMenuPojoLISTS is *****"+LoginMenuPojoLISTS);
		if(LoginMenuPojoLISTS.size()==0) {
			LoginMenuPojoLISTS.add(new ArrayList<LoginMenuPojo>());
		}
		LOGGER.info("**** The LoginMenuPojoLISTS is ****"+LoginMenuPojoLISTS.get(0));
		return LoginMenuPojoLISTS.get(0);
	}
	
	@Override
	public List<LoginSubMenuPojo> getEmployeeSubMenuDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_SUBMENU_DETAILS)
			       .append(SqlQuery.QRY_TO_GET_EMPLOYEE_SUBMENU_DETAILS_WTO_SUB_MENU_ID)
			       .toString();
	    LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_SUBMENU_DETAILS IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SUB_MODULE_ID",loginInfo.getSubModuleIds(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",status.status, Types.INTEGER);
		
		List<List<LoginSubMenuPojo>> LoginSubMenuPojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<LoginSubMenuPojo>>() {
					public List<LoginSubMenuPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<LoginSubMenuPojo> resultSetMapper = new ResultSetMapper<LoginSubMenuPojo>();
						List<LoginSubMenuPojo> LoginSubMenuPojoLIST = null;
						try {
							LoginSubMenuPojoLIST = resultSetMapper.mapRersultSetToObject(rs, LoginSubMenuPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The LoginSubMenuPojoLIST objects  is *****" + LoginSubMenuPojoLIST);					
						return LoginSubMenuPojoLIST;
					}
				});
		LOGGER.info("*** The LoginSubMenuPojoLISTS is *****"+LoginSubMenuPojoLISTS);
		if(LoginSubMenuPojoLISTS.size()==0) {
			LoginSubMenuPojoLISTS.add(new ArrayList<LoginSubMenuPojo>());
		}
		LOGGER.info("**** The LoginSubMenuPojoLISTS is ****"+LoginSubMenuPojoLISTS.get(0));
		return LoginSubMenuPojoLISTS.get(0);
	}
	
	@Override
	public List<EmployeeSubMenuSiteMappingPojo> getEmployeeSubMenuSiteMappingDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_SUBMENU_SITE_MAPPING_DETAILS)
			       .append(SqlQuery.QRY_TO_GET_EMPLOYEE_SUBMENU_SITE_MAPPING_DETAILS_WTO_MENU_SUB_MENU_MAPPING_ID)
			       .toString();
	    LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_SUBMENU_SITE_MAPPING_DETAILS IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MENU_SUB_MENU_MAPPING_ID",loginInfo.getEmployeeMenuSubMenuMappingIds(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",status.status, Types.INTEGER);
		
		List<List<EmployeeSubMenuSiteMappingPojo>> LoginSubMenuSiteMappingPojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<EmployeeSubMenuSiteMappingPojo>>() {
					public List<EmployeeSubMenuSiteMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeSubMenuSiteMappingPojo> resultSetMapper = new ResultSetMapper<EmployeeSubMenuSiteMappingPojo>();
						List<EmployeeSubMenuSiteMappingPojo> LoginSubMenuSiteMappingPojoLIST = null;
						try {
							LoginSubMenuSiteMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeSubMenuSiteMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The LoginSubMenuSiteMappingPojoLIST objects  is *****" + LoginSubMenuSiteMappingPojoLIST);					
						return LoginSubMenuSiteMappingPojoLIST;
					}
				});
		LOGGER.info("*** The LoginSubMenuSiteMappingLISTS is *****"+LoginSubMenuSiteMappingPojoLISTS);
		if(LoginSubMenuSiteMappingPojoLISTS.size()==0) {
			LoginSubMenuSiteMappingPojoLISTS.add(new ArrayList<EmployeeSubMenuSiteMappingPojo>());
		}
		LOGGER.info("**** The LoginSubMenuSiteMappingLISTS is ****"+LoginSubMenuSiteMappingPojoLISTS.get(0));
		return LoginSubMenuSiteMappingPojoLISTS.get(0);
	}
	
	
	@Override
	public List<SitePojo> getSiteDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_SITE_DETAILS)
				.append(SqlQuery.QRY_TO_GET_SITE_DETAILS_WTO_SITE_ID)
			       .toString();
	    LOGGER.info("**** THE QRY_TO_GET_SITE_DETAILS IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",loginInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Arrays.asList(1L,2L), Types.INTEGER);
		
		List<List<SitePojo>> sitePojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<SitePojo>>() {
					public List<SitePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<SitePojo> resultSetMapper = new ResultSetMapper<SitePojo>();
						List<SitePojo> sitePojoLIST = null;
						try {
							sitePojoLIST = resultSetMapper.mapRersultSetToObject(rs, SitePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The sitePojoLIST objects  is *****" + sitePojoLIST);					
						return sitePojoLIST;
					}
				});
		LOGGER.info("*** The sitePojoLISTS is *****"+sitePojoLISTS);
		if(sitePojoLISTS.size()==0) {
			sitePojoLISTS.add(new ArrayList<SitePojo>());
		}
		LOGGER.info("**** The sitePojoLISTS is ****"+sitePojoLISTS.get(0));
		return sitePojoLISTS.get(0);
	}
	
	@Override
	public List<EmployeeRolePojo> getEmployeeRoleDetails(@NonNull LoginInfo loginInfo, Status status) {
		LOGGER.info("**** The control is inside the getDepartmentRoleMappingDetails in LoginDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_ROLES)
				        .append(SqlQuery.QRY_TO_GET_EMPLOYEE_ROLES_WTO_ROLE_ID)
			            .toString();
	    LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_ROLES IS *****"+query);
	    
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ROLE_ID",loginInfo.getRoleId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",status.status, Types.INTEGER);
		
		List<List<EmployeeRolePojo>> employeeRolePojoLISTS = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<EmployeeRolePojo>>() {
					public List<EmployeeRolePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeRolePojo> resultSetMapper = new ResultSetMapper<EmployeeRolePojo>();
						List<EmployeeRolePojo> employeeRolePojoLIST = null;
						try {
							employeeRolePojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeRolePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while creating the PersistenDTO object ****");
						}
						LOGGER.info("***** The EmployeeRolePojo objects  is *****" + employeeRolePojoLIST);					
						return employeeRolePojoLIST;
					}
				});
		LOGGER.info("*** The employeeRolePojoLISTS is *****"+employeeRolePojoLISTS);
		if(employeeRolePojoLISTS.size()==0) {
			employeeRolePojoLISTS.add(new ArrayList<EmployeeRolePojo>());
		}
		LOGGER.info("**** The employeeRolePojoLISTS is ****"+employeeRolePojoLISTS.get(0));
		return employeeRolePojoLISTS.get(0);
	}
	
	@Override
	public Long updateEmployeeLogin(@NonNull EmployeeLogInPojo employeeLogInPojo) {
		LOGGER.info("***** The control is inside the updateEmployeeLogin in LoginDaoImpl *****"+employeeLogInPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_EMPLOYEE_LOGIN,new BeanPropertySqlParameterSource(employeeLogInPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}
	
	@Override
	public Boolean checkOldPassword(@NonNull EmployeeLogInPojo employeeLogInPojo) {
		LOGGER.info("***** The control is inside the checkOldPassword method in LoginDaoImpl *****");
		return nmdPJdbcTemplate.queryForObject(SqlQuery.QRY_TO_CHECK_OLD_PASSWORD, new BeanPropertySqlParameterSource(employeeLogInPojo), Boolean.class);
	}
	
	@Override
	public Long changePassword(EmployeeLogInPojo employeeLogInPojo) {
		LOGGER.info("***** The control is inside the changePassword method in LoginDaoImpl *****");
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_CHANGE_PASSWORD, new BeanPropertySqlParameterSource(employeeLogInPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}
	
	@Override
	public List<EmployeePojo> getEmployeeByMobileNo(LoginInfo loginInfo) {
		LOGGER.info("***** The control is inside the getEmployeeByMobileNo method in LoginDaoImpl *****");
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_EMP_BY_MOBILE_NO);
		MapSqlParameterSource namedParameteres = new MapSqlParameterSource();
		/*if(Util.isNotEmptyObject(loginInfo.getRequestUrl()) && loginInfo.getRequestUrl().equalsIgnoreCase("sendOtp")
			&& Util.isEmptyObject(loginInfo.getAction())) { */
		if(Util.isNotEmptyObject(loginInfo.getRequestUrl()) && Util.isEmptyObject(loginInfo.getAction())) { 
			sqlQuery.append(" WHERE EMP.MOBILE_NO=:MOBILE_NO AND EMLG.STATUS=:STATUS_ID AND EMP.STATUS_ID=:STATUS_ID");
			namedParameteres.addValue("MOBILE_NO", loginInfo.getMobileNo(), Types.VARCHAR);
			namedParameteres.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}else if(Util.isNotEmptyObject(loginInfo.getAction()) && loginInfo.getAction().equalsIgnoreCase(MetadataId.MULTIPLE_ACCOUNTS.getName())) {
			sqlQuery.append(" WHERE EMP.EMP_ID=:EMP_ID AND EMLG.STATUS=:STATUS_ID AND EMP.STATUS_ID=:STATUS_ID ");
			namedParameteres.addValue("EMP_ID", loginInfo.getEmployeeId(), Types.BIGINT);
			namedParameteres.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}
		sqlQuery.append(" ORDER BY EMP.EMP_ID ");
		List<List<EmployeePojo>> employeePojoLists = nmdPJdbcTemplate.query(sqlQuery.toString(), namedParameteres, 
				new ExtractDataFromResultSet<EmployeePojo>(EmployeePojo.class));
		if(Util.isEmptyObject(employeePojoLists)) {
			employeePojoLists.add(new ArrayList<EmployeePojo>());
		}
		return employeePojoLists.get(0);
	}
}
