package com.sumadhura.employeeservice.persistence.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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

import com.sumadhura.employeeservice.dto.BlockRequest;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.enums.NotificationsSqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dao.BlockDao;
import com.sumadhura.employeeservice.persistence.dto.BlockDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.serviceImpl.BlockServiceImpl;
import com.sumadhura.employeeservice.util.Util;
/**
 * 
 * @author rayudu
 *
 */
@Repository
public class BlockDaoImpl implements BlockDao {
	
	private static final Logger LOGGER = Logger.getLogger(BlockServiceImpl.class);

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Resource(name="ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;

	/**
	 * @param siteList It takes siteIds as a Input
	 * @return It returns Blocks List or Empty List
	 */
	@Override
	public List<DropDownPojo> getBlockNames(DropDownRequest siteList) {
		StringBuilder query = new StringBuilder();
		if(Util.isNotEmptyObject(siteList.getRequestUrl()) && "All".equalsIgnoreCase(siteList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ACTIVE_BLOCK_WITHOUT_APP_REG);
		}else if(Util.isNotEmptyObject(siteList.getRequestUrl()) && "AllData".equalsIgnoreCase(siteList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_BLOCK_S);
		}
		else if(Util.isNotEmptyObject(siteList.getRequestUrl()) && "ViewAllData".equalsIgnoreCase(siteList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_BLOCK_WITHOUT_APP_REG);
		}else {
			query.append(NotificationsSqlQuery.GET_ACTIVE_BLOCK_WITH_APP_REG);
		}
		//.append(NotificationsSqlQuery.STATUS_COND_CHECK)
		//.append(NotificationsSqlQuery.AND )
		query.append(NotificationsSqlQuery.WHERE ).append(" BLOCKD.SITE_ID IN(:SITE_ID) ORDER BY NAME ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.ACTIVE.getStatus(), Types.INTEGER);
		namedParameters.addValue(NotificationsSqlQuery.SITE_ID,Util.isNotEmptyObject(siteList.getIds())?siteList.getIds():Arrays.asList(0l));

		List<DropDownPojo> blocksList= nmdPJdbcTemplate.query(query.toString(), namedParameters, new RowMapper<DropDownPojo>(){  
			@Override  
			public DropDownPojo mapRow(ResultSet rs, int rownumber) throws SQLException {  
				DropDownPojo dropdownObj=new DropDownPojo();  
				dropdownObj.setId(rs.getLong(1));
				dropdownObj.setName(rs.getString(2));
				dropdownObj.setDetId(rs.getLong(3));
				dropdownObj.setMappingId(rs.getLong(4));
				return dropdownObj;  
			}  
		});  
		return blocksList;
	}

	@Override
	public List<NotificationDetails> getBlockDetailList(Set<Long> set) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",set);

		String query = new StringBuilder(NotificationsSqlQuery.GET_BLOCK_DETAILS).toString();
		query+=" WHERE BLOCKD.BLOCK_DET_ID IN(:SITE_ID) ";
		
		List<NotificationDetails> siteList = nmdPJdbcTemplate.query(query,namedParameters,new RowMapper<NotificationDetails>() {
			public NotificationDetails mapRow(ResultSet rs, int arg1) throws SQLException {
				NotificationDetails sitePojoObj = new NotificationDetails();
				sitePojoObj.setSiteId(rs.getLong(1));
				sitePojoObj.setSiteName(rs.getString(2));
				
				sitePojoObj.setBlockId(rs.getLong(3));
				sitePojoObj.setBlockName(rs.getString(4));
				sitePojoObj.setBlockDetId(rs.getLong(5));
				
				return sitePojoObj;
			}
		});
		
		return siteList;
	}

	@Override
	public List<BlockDetailsPojo> getBlockDetails(BlockRequest blockReq) {
		LOGGER.info("*** The control is inside of the getBlockDetail in BlockServiceImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String sqlQuery = SqlQueryTwo.QRY_TO_GET_ALL_BLOCK_DETAILS;
		namedParameters.addValue("SITE_ID", blockReq.getSiteId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		return resultSetMapper.getResult(BlockDetailsPojo.class, sqlQuery, namedParameters);
	}
}
