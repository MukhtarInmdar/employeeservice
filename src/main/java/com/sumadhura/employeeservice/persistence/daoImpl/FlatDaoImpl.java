package com.sumadhura.employeeservice.persistence.daoImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.FlatRequest;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.enums.NotificationsSqlQuery;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dao.FlatDao;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Util;

/**
 * 
 * @author rayudu
 *
 */
@Repository
public class FlatDaoImpl implements FlatDao {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FlatDaoImpl.class);

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Resource(name="ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;

	/**
	 * This method Gives Flat Details Dropdown
	 * @param floorDetIds It takes floorDetIds as Input
	 * @return It returns Flat List or Empty List
	 */
	@Override
	public List<DropDownPojo> getFlatsNames(DropDownRequest dropDownRequest) {
		StringBuilder query = new StringBuilder();
		final String url =dropDownRequest.getRequestUrl()==null?"":dropDownRequest.getRequestUrl();
		if(Util.isNotEmptyObject(dropDownRequest.getRequestUrl()) && "All".equalsIgnoreCase(dropDownRequest.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITHOUT_APP_REG);
		}else if("ViewAllData".equalsIgnoreCase(dropDownRequest.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_FLAT_WITHOUT_APP_REG);
		}else {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITH_APP_REG);
		}
		
		//.append(NotificationsSqlQuery.FLOR_STATUS_COND_CHECK)
		query.append(NotificationsSqlQuery.WHERE).append("FLATD.FLOOR_DET_ID IN(:FLOOR_DET_ID) ORDER BY NAME ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.ACTIVE.getStatus(), Types.INTEGER);
		namedParameters.addValue("FLOOR_DET_ID",Util.isNotEmptyObject(dropDownRequest.getIds())?dropDownRequest.getIds():Arrays.asList(0l));

		List<DropDownPojo> employeeLogInPojoLISTS= nmdPJdbcTemplate.query(query.toString(), namedParameters, new RowMapper<DropDownPojo>(){  
			@Override  
			public DropDownPojo mapRow(ResultSet rs, int rownumber) throws SQLException {  
				DropDownPojo flatDropDownObj=new DropDownPojo();  
				flatDropDownObj.setId(rs.getLong(1));  
				flatDropDownObj.setName(rs.getString(2));
				flatDropDownObj.setDetId(rs.getLong(3));
				flatDropDownObj.setMappingId(rs.getLong(4));

				if("ViewAllData".equalsIgnoreCase(url)) {
					flatDropDownObj.setFlatBookingId(rs.getLong(5));
					flatDropDownObj.setCustomerId(rs.getLong(6));
					flatDropDownObj.setStatusId(rs.getLong(7));
					flatDropDownObj.setStatus(rs.getString(8));
					flatDropDownObj.setSalesforceBookingId(rs.getString(9));
					flatDropDownObj.setSalesforceSiteName(rs.getString(10));
				}
				return flatDropDownObj;  
			}  
		});  
		return employeeLogInPojoLISTS;
	}

	/**
	 * This method Gives Flat Details Dropdown
	 * @param siteIds It takes siteIds as Input
	 * @return It returns Flat List or Empty List
	 */
	@Override
	public List<DropDownPojo> getFlatsNamesBySite(DropDownRequest dropDownRequest) {
		StringBuilder query = new StringBuilder();
		final String url =dropDownRequest.getRequestUrl()==null?"":dropDownRequest.getRequestUrl();
		if(Util.isNotEmptyObject(dropDownRequest.getRequestUrl()) && "All".equalsIgnoreCase(dropDownRequest.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITHOUT_APP_REG);
		}else if("ViewAllData".equalsIgnoreCase(dropDownRequest.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_FLAT_WITHOUT_APP_REG);
		}else {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITH_APP_REG);
		}
		
		
		query.append(NotificationsSqlQuery.WHERE).append(NotificationsSqlQuery.SITE_ID_IN_COND).append(" ORDER BY NAME ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.ACTIVE.getStatus(), Types.INTEGER);

		namedParameters.addValue(NotificationsSqlQuery.SITE_ID,Util.isNotEmptyObject(dropDownRequest.getIds())?dropDownRequest.getIds():Arrays.asList(0l));

		List<DropDownPojo> employeeLogInPojoLISTS= nmdPJdbcTemplate.query(query.toString(), namedParameters, new RowMapper<DropDownPojo>(){  
			@Override  
			public DropDownPojo mapRow(ResultSet rs, int rownumber) throws SQLException {  
				DropDownPojo flatDropDownObj=new DropDownPojo();  
				flatDropDownObj.setId(rs.getLong(1));  
				flatDropDownObj.setName(rs.getString(2));
				flatDropDownObj.setDetId(rs.getLong(3));
				flatDropDownObj.setMappingId(rs.getLong(4));
				if("ViewAllData".equalsIgnoreCase(url)) {
					flatDropDownObj.setFlatBookingId(rs.getLong(5));
					flatDropDownObj.setCustomerId(rs.getLong(6));
					flatDropDownObj.setStatusId(rs.getLong(7));
					flatDropDownObj.setStatus(rs.getString(8));
					flatDropDownObj.setSalesforceBookingId(rs.getString(9));
					flatDropDownObj.setSalesforceSiteName(rs.getString(10));
				}
				return flatDropDownObj;  
			}  
		});  
		return employeeLogInPojoLISTS;
	}

	/**
	 * This method Gives Flat Details Dropdown
	 * @param siteIds It takes blockDetIds as Input
	 * @return It returns Flat List or Empty List
	 */
	@Override
	public List<DropDownPojo> getFlatsNamesByBlock(DropDownRequest dropDownRequest) {
		StringBuilder query = new StringBuilder();
		final String url =dropDownRequest.getRequestUrl()==null?"":dropDownRequest.getRequestUrl();
		if(Util.isNotEmptyObject(dropDownRequest.getRequestUrl()) && "All".equalsIgnoreCase(dropDownRequest.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITHOUT_APP_REG);
		}else if("ViewAllData".equalsIgnoreCase(dropDownRequest.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_FLAT_WITHOUT_APP_REG);
		}else {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITH_APP_REG);
		}
		//.append(NotificationsSqlQuery.FLOR_STATUS_COND_CHECK)
		query.append(NotificationsSqlQuery.WHERE).append("BLOCKD.BLOCK_DET_ID IN(:BLOCK_DET_ID)").append(" ORDER BY NAME ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.ACTIVE.getStatus(), Types.INTEGER);
		namedParameters.addValue("BLOCK_DET_ID",Util.isNotEmptyObject(dropDownRequest.getIds())?dropDownRequest.getIds():Arrays.asList(0l));

		List<DropDownPojo> employeeLogInPojoLISTS= nmdPJdbcTemplate.query(query.toString(), namedParameters, new RowMapper<DropDownPojo>(){  
			@Override  
			public DropDownPojo mapRow(ResultSet rs, int rownumber) throws SQLException {  
				DropDownPojo flatDropDownObj=new DropDownPojo();  
				flatDropDownObj.setId(rs.getLong(1));  
				flatDropDownObj.setName(rs.getString(2));
				flatDropDownObj.setDetId(rs.getLong(3));
				flatDropDownObj.setMappingId(rs.getLong(4));

				if("ViewAllData".equalsIgnoreCase(url)) {
					flatDropDownObj.setFlatBookingId(rs.getLong(5));
					flatDropDownObj.setCustomerId(rs.getLong(6));
					flatDropDownObj.setStatusId(rs.getLong(7));
					flatDropDownObj.setStatus(rs.getString(8));
					flatDropDownObj.setSalesforceBookingId(rs.getString(9));
					flatDropDownObj.setSalesforceSiteName(rs.getString(10));
				}
				return flatDropDownObj;  
			}  
		});  
		return employeeLogInPojoLISTS;

	}

	@Override
	public List<NotificationDetails> getFlatDetailList(Set<Long> set) {
		
		if(set==null || set.isEmpty()){
			return Collections.emptyList();
		}
		
		System.out.println("size"+set.size());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",new ArrayList<>(set));

		String query = new StringBuilder(NotificationsSqlQuery.GET_FLAT_DETAILS).toString();
		query+=" WHERE FLATD.FLAT_ID IN(:SITE_ID) ";
		
		List<NotificationDetails> siteList = nmdPJdbcTemplate.query(query,namedParameters,new RowMapper<NotificationDetails>() {
			public NotificationDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				NotificationDetails sitePojoObj = new NotificationDetails();
				sitePojoObj.setSiteId(rs.getLong(1));
				sitePojoObj.setSiteName(rs.getString(2));
				
				sitePojoObj.setBlockId(rs.getLong(3));
				sitePojoObj.setBlockName(rs.getString(4));
				sitePojoObj.setBlockDetId(rs.getLong(5));
				
				sitePojoObj.setFloorId(rs.getLong(6));
				sitePojoObj.setFloorName(rs.getString(7));
				sitePojoObj.setFloorDetId(rs.getLong(8));
				
				sitePojoObj.setFlatId(rs.getLong(10));
				sitePojoObj.setFlatName(rs.getString(9));
				
				return sitePojoObj;
			}
		});
		
		return siteList;
	}

	@Override
	public List<NotificationDetails> getFlatDetailListByDetId(Set<Long> set) {
		if(set==null || set.isEmpty()){
			return Collections.emptyList();
		}
		
		System.out.println("size"+set.size());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",new ArrayList<>(set));

		String query = new StringBuilder(NotificationsSqlQuery.GET_FLAT_DETAILS).toString();
		query+=" WHERE FLATD.FLAT_DET_ID IN(:SITE_ID) ";
		
		List<NotificationDetails> siteList = nmdPJdbcTemplate.query(query,namedParameters,new RowMapper<NotificationDetails>() {
			public NotificationDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				NotificationDetails sitePojoObj = new NotificationDetails();
				sitePojoObj.setSiteId(rs.getLong(1));
				sitePojoObj.setSiteName(rs.getString(2));
				
				sitePojoObj.setBlockId(rs.getLong(3));
				sitePojoObj.setBlockName(rs.getString(4));
				sitePojoObj.setBlockDetId(rs.getLong(5));
				
				sitePojoObj.setFloorId(rs.getLong(6));
				sitePojoObj.setFloorName(rs.getString(7));
				sitePojoObj.setFloorDetId(rs.getLong(8));
				
				sitePojoObj.setFlatId(rs.getLong(10));
				sitePojoObj.setFlatName(rs.getString(9));
				sitePojoObj.setFlatDetId(rs.getLong(11));
				
				return sitePojoObj;
			}
		});
		
		return siteList;

	}

	@Override
	public List<DropDownPojo> getFlatsNamesBySbuaSeries(DropDownRequest dropDownRequest) {
		LOGGER.info("**** The control is inside the getFlatsNamesBySbuaSeries in FlatDaoImpl ****");
		StringBuilder query = new StringBuilder(NotificationsSqlQuery.GET_ACTIVE_FLAT_ID_WITH_APP_REG).append(" WHERE 1=1 ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(Util.isNotEmptyObject(dropDownRequest.getFlatSeriesList())) {
			query.append(" AND SUBSTR(FLAT.FLAT_NO, -2) IN (:FLAT_SERIES) ");
			namedParameters.addValue("FLAT_SERIES", dropDownRequest.getFlatSeriesList());
		}
		if(Util.isNotEmptyObject(dropDownRequest.getSbuaList())) {
			query.append(" AND FLATD.SBUA IN (:SBUAS) ");
			namedParameters.addValue("SBUAS", dropDownRequest.getSbuaList());
		}
		if(Util.isNotEmptyObject(dropDownRequest.getFacingList())) {
			query.append(" AND FLATD.FACING IN (:FACINGS) ");
			namedParameters.addValue("FACINGS", dropDownRequest.getFacingList());
		}
		if(Util.isNotEmptyObject(dropDownRequest.getBhkTypeList())) {
			query.append(" AND FLATD.BHK IN (:BHK_TYPES) ");
			namedParameters.addValue("BHK_TYPES", dropDownRequest.getBhkTypeList());
		}
		if(Util.isNotEmptyObject(dropDownRequest.getSiteIds())){
			query.append(" AND SITE.SITE_ID IN(:SITE_IDS) ");
			namedParameters.addValue("SITE_IDS", dropDownRequest.getSiteIds());
		}	
		if(Util.isNotEmptyObject(dropDownRequest.getBlockDetIds())) {
			query.append(" AND BLOCKD.BLOCK_DET_ID IN(:BLOCK_DET_IDS) ");
			namedParameters.addValue("BLOCK_DET_IDS", dropDownRequest.getBlockDetIds());
		}	
		if(Util.isNotEmptyObject(dropDownRequest.getFloorDetIds())) {
			query.append(" AND FLOORD.FLOOR_DET_ID IN(:FLOOR_DET_IDS) ");
			namedParameters.addValue("FLOOR_DET_IDS", dropDownRequest.getFloorDetIds());
		}
		query.append(" ORDER BY NAME ");
		LOGGER.debug("*** Query to get Active_Flats_With_App_Reg ***"+query.toString());
		List<List<DropDownPojo>> dropDownPojosLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new RowMapper<List<DropDownPojo>>() {

					@Override
					public List<DropDownPojo> mapRow(ResultSet rs, int rowNum) throws SQLException {
						ResultSetMapper<DropDownPojo> resultSetMapper = new ResultSetMapper<DropDownPojo>();
						try {
							return resultSetMapper.mapRersultSetToObject(rs, DropDownPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
  							String msg = "The Exception is raised while mapping resultset object to Pojo";
  							throw new ResultSetMappingException(msg, ex.getCause());
						}
					}	
		});
		LOGGER.debug("**** dropDownPojoList is ****"+dropDownPojosLists);
		if(Util.isEmptyObject(dropDownPojosLists)) {
			dropDownPojosLists.add(new ArrayList<DropDownPojo>());
		}
		return dropDownPojosLists.get(0);
	}

	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerDetailsByFlatId(Customer customer) {
		LOGGER.info("**** The control is inside the getCustomerDetailsByFlatId in FlatDaoImpl ****");
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS_BY_FLAT_ID);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_ID", customer.getFlatId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		LOGGER.info("*** Query to get QRY_TO_GET_CUSTOMER_DETAILS_BY_FLAT_ID ***"+sqlQuery);
		return resultSetMapper.getResult(CustomerPropertyDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}

	@Override
	public List<FlatPojo> getFlats(FlatRequest flatRequest) {
		LOGGER.info("**** The control is inside of the getFlats in FlatDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_ALL_FLAT_DETAILS);
		/* Getting Details by Site Id */
		if(Util.isNotEmptyObject(flatRequest.getSiteId())) {
			sqlQuery.append(" AND ST.SITE_ID=:SITE_ID ");
			namedParameters.addValue("SITE_ID", flatRequest.getSiteId());
		}
		/* Getting Details by Block Det Id */
		if(Util.isNotEmptyObject(flatRequest.getBlockDetIds())) {
			sqlQuery.append(" AND BLD.BLOCK_DET_ID IN (:BLOCK_DET_IDS) ");
			namedParameters.addValue("BLOCK_DET_IDS", flatRequest.getBlockDetIds());
		}
		/* Getting Details by Floor Det Id */
		if(Util.isNotEmptyObject(flatRequest.getFloorDetIds())) {
			sqlQuery.append(" AND FLD.FLOOR_DET_ID IN (:FLOOR_DET_IDS) ");
			namedParameters.addValue("FLOOR_DET_IDS", flatRequest.getFloorDetIds());
		}
		sqlQuery.append(" ORDER BY BLOCK_NAME,FLOOR_ID,NAME,FLAT_NO ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		return resultSetMapper.getResult(FlatPojo.class, sqlQuery.toString(), namedParameters);
	}

	@Override
	public List<FlatBookingPojo> getBookingFlats(FlatRequest flatRequest) {
		LOGGER.info("**** The control is inside of the getBookingFlats in FlatDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_ALL_BOOKING_FLATS);
		
		/* Getting Details by Booking From Date */
		if(Util.isNotEmptyObject(flatRequest.getFromDate())) {
			sqlQuery.append(" AND FB.BOOKING_DATE>=TRUNC(:FROM_DATE) ");
			namedParameters.addValue("FROM_DATE", flatRequest.getFromDate());
		}
		
		/* Getting Details by Booking To Date */
		if(Util.isNotEmptyObject(flatRequest.getToDate())) {
			sqlQuery.append(" AND TRUNC(FB.BOOKING_DATE)<=:TO_DATE ");
			namedParameters.addValue("TO_DATE", flatRequest.getToDate());
		}
		sqlQuery.append(" ORDER BY FT.FLAT_NO ");
		namedParameters.addValue("SITE_ID", flatRequest.getSiteId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		return resultSetMapper.getResult(FlatBookingPojo.class, sqlQuery.toString(), namedParameters);
	}

}
