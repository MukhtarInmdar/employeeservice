package com.sumadhura.employeeservice.persistence.daoImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.enums.NotificationsSqlQuery;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dao.SiteDao;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Util;

/**
 * 
 * @author rayudu
 *
 */
@Repository
public class SiteDaoImpl implements SiteDao {
	private static final Logger LOGGER = Logger.getLogger(SiteDaoImpl.class);


	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;

	/**
	 * This method is used to get Completed SiteList
	 * @return It return site List or EmptyList
	 */
	@Override
	public List<DropDownPojo> getSiteList() {
		List<DropDownPojo> siteList=Collections.emptyList();
		LOGGER.debug("**** The control is inside the getSiteList in SiteServiceImpl *****");
		String query = new StringBuilder(NotificationsSqlQuery.GET_ACTIVE_SITE_WITH_APP_REG).toString();
		//.append(NotificationsSqlQuery.WHERE)
		//.append(NotificationsSqlQuery.SITE_STATUS_COND).toString();
		LOGGER.debug("**** THE QRY_TO_GET_SITE_DETAILS IS *****"+query);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.COMPLETED.status, Types.INTEGER);

		siteList = nmdPJdbcTemplate.query(query,new RowMapper<DropDownPojo>() {
			public DropDownPojo mapRow(ResultSet rs, int arg1) throws SQLException {
				DropDownPojo sitePojoObj = new DropDownPojo();
				sitePojoObj.setId(rs.getLong(1));
				sitePojoObj.setName(rs.getString(2));	
				return sitePojoObj;
			}
		});
		int size = siteList.size();
		LOGGER.debug("*** The siteList is *****"+size);
		return siteList;
	}

	@Override
	public List<NotificationDetails> getSiteDetailList(Set<Long> set) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",Util.isNotEmptyObject(set)?new ArrayList<>(set):Arrays.asList(0l));
		//namedParameters.addValue("SITE_ID",new ArrayList<>(set));

		String query = new StringBuilder(NotificationsSqlQuery.GET_SITE_DETAILS).toString();
		query+=" WHERE SITE.SITE_ID IN(:SITE_ID) ORDER BY SITE.NAME";
		
		List<NotificationDetails> siteList = nmdPJdbcTemplate.query(query,namedParameters,new RowMapper<NotificationDetails>() {
			public NotificationDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				NotificationDetails sitePojoObj = new NotificationDetails();
				sitePojoObj.setSiteId(rs.getLong(1));
				sitePojoObj.setSiteName(rs.getString(2));
				sitePojoObj.setSalesForceSiteName(rs.getString(3));
				return sitePojoObj;
			}
		});
		
		return siteList;
	}

	@Override
	public List<DropDownPojo> getAllSitesList() {
		LOGGER.info("**** The control is inside the getAllSitesList in SiteServiceImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_GET_ALL_SITE_DETAILS;
		query +="WHERE S.STATUS_ID=:STATUS_ID ORDER BY S.NAME";
		namedParameters.addValue("STATUS_ID", Status.COMPLETED.getStatus());
		LOGGER.info("**** SqlQuery  QRY_TO_GET_ALL_SITE_DETAILS*****"+query);
		List<List<DropDownPojo>> sitesLists = nmdPJdbcTemplate.query(query, namedParameters, 
				new RowMapper<List<DropDownPojo>>() {

					@Override
					public List<DropDownPojo> mapRow(ResultSet rs, int rowNum) throws SQLException {
						LOGGER.debug("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<DropDownPojo> resultSetMapper = new ResultSetMapper<DropDownPojo>();
						List<DropDownPojo>sitesList = null;
						try {
							 sitesList = resultSetMapper.mapRersultSetToObject(rs, DropDownPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							LOGGER.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, e.getCause());
						}
						return sitesList;
					}
			
		});
		LOGGER.info("*** The sitesLists is *****" + sitesLists);
		if (sitesLists.isEmpty()) {
			sitesLists.add(new ArrayList<DropDownPojo>());
		}
		LOGGER.info("**** The sitesList is ****" + sitesLists.get(0));
		return sitesLists.get(0);
	}
	
	@Override
	public List<NotificationDetails> getStateWiseSiteDetailList(Set<Long> set) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue("STATE_ID",Util.isNotEmptyObject(set)?new ArrayList<>(set):Arrays.asList(0l));
		

		String query = new StringBuilder(NotificationsSqlQuery.GET_SITE_DETAILS).toString();
		query+=" WHERE SITE.STATUS_ID=2";
			if(Util.isNotEmptyObject(set)) {
				namedParameters.addValue("STATE_ID",new ArrayList<>(set));
		query+=" AND SITE.STATE_ID IN(:STATE_ID) ORDER BY SITE.NAME";
			}
		List<NotificationDetails> siteList = nmdPJdbcTemplate.query(query,namedParameters,new RowMapper<NotificationDetails>() {
			public NotificationDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				NotificationDetails sitePojoObj = new NotificationDetails();
				sitePojoObj.setSiteId(rs.getLong(1));
				sitePojoObj.setSiteName(rs.getString(2));
				return sitePojoObj;
			}
		});
		
		return siteList;
	}

}