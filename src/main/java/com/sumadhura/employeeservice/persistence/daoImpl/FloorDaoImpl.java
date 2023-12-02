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

import com.sumadhura.employeeservice.dto.FloorRequest;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.enums.NotificationsSqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dao.FloorDao;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorDetailsPojo;
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
public class FloorDaoImpl implements FloorDao {
	
	private static final Logger LOGGER = Logger.getLogger(BlockServiceImpl.class);

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Resource(name="ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;

	/**
	 * @param blockDetIds It takes Input as blockDetIds
	 * @return It returns FloorNames List or Empty List
	 */
	@Override
	public List<DropDownPojo> getFloorNames(DropDownRequest blockList) {

		//String query = new StringBuilder(NotificationsSqlQuery.GET_ACTIVE_FLOOR_WITH_APP_REG)
		StringBuilder query = new StringBuilder();
		if(Util.isNotEmptyObject(blockList.getRequestUrl()) && "All".equalsIgnoreCase(blockList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLOOR_WITHOUT_APP_REG);
		}else if(Util.isNotEmptyObject(blockList.getRequestUrl()) && "ViewAllData".equalsIgnoreCase(blockList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_FLOOR_WITHOUT_APP_REG);
		}else {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLOOR_WITH_APP_REG);
		}
		//.append(NotificationsSqlQuery.FLOR_STATUS_COND_CHECK)
		query.append(NotificationsSqlQuery.WHERE).append("FLOORD.BLOCK_DET_ID IN(:BLOCK_DET_ID) ORDER BY DETID ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.ACTIVE.getStatus(), Types.INTEGER);
		namedParameters.addValue("BLOCK_DET_ID",Util.isNotEmptyObject(blockList.getIds())?blockList.getIds():Arrays.asList(0l));

		List<DropDownPojo> floorNamesList= nmdPJdbcTemplate.query(query.toString(), namedParameters, new RowMapper<DropDownPojo>(){  
			@Override  
			public DropDownPojo mapRow(ResultSet rs, int rownumber) throws SQLException {  
				DropDownPojo floorList=new DropDownPojo();  
				floorList.setId(rs.getLong(1));  
				floorList.setName(rs.getString(2));
				floorList.setDetId(rs.getLong(3));
				floorList.setMappingId(rs.getLong(4));
				return floorList;  
			}  
		});  

		return floorNamesList;
	}


	/**
	 * @param blockDetIds It takes Input as siteIds
	 * @return It returns FloorNames List or Empty List
	 */
	@Override
	public List<DropDownPojo> getFloorNamesBySite(DropDownRequest siteList) {
		//String query = new StringBuilder(NotificationsSqlQuery.GET_ACTIVE_FLOOR_WITH_APP_REG)
		StringBuilder query = new StringBuilder();
		if(Util.isNotEmptyObject(siteList.getRequestUrl()) && "All".equalsIgnoreCase(siteList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLOOR_WITHOUT_APP_REG);
		}else if(Util.isNotEmptyObject(siteList.getRequestUrl()) && "ViewAllData".equalsIgnoreCase(siteList.getRequestUrl())) {
			query.append(NotificationsSqlQuery.GET_ALL_FLOOR_WITHOUT_APP_REG);
		}else {
			query.append(NotificationsSqlQuery.GET_ACTIVE_FLOOR_WITH_APP_REG);
		}
		query.append(NotificationsSqlQuery.WHERE).append(NotificationsSqlQuery.SITE_ID_IN_COND).append(" ORDER BY DETID ");
		//.append(NotificationsSqlQuery.FLOOR_DET_STATUS_ID_COND)
		//.append(NotificationsSqlQuery.AND)

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue(NotificationsSqlQuery.STATUS_ID,Status.ACTIVE.getStatus(), Types.INTEGER);		
		namedParameters.addValue(NotificationsSqlQuery.SITE_ID,Util.isNotEmptyObject(siteList.getIds())?siteList.getIds():Arrays.asList(0l));

		List<DropDownPojo> floorNamesList= nmdPJdbcTemplate.query(query.toString(), namedParameters, new RowMapper<DropDownPojo>(){  
			@Override  
			public DropDownPojo mapRow(ResultSet rs, int rownumber) throws SQLException {  
				DropDownPojo floorDropDownObj=new DropDownPojo();  
				floorDropDownObj.setId(rs.getLong(1));  
				floorDropDownObj.setName(rs.getString(2));
				floorDropDownObj.setDetId(rs.getLong(3));
				return floorDropDownObj;  
			}  
		});  

		return floorNamesList;
	}


	@Override
	public List<NotificationDetails> getFloorDetailList(Set<Long> set) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",set);

		String query = new StringBuilder(NotificationsSqlQuery.GET_FLOOR_DETAILS).toString();
		query+=" WHERE FLOORD.FLOOR_DET_ID IN(:SITE_ID) ";
		
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
				return sitePojoObj;
			}
		});
		
		return siteList;
	}

	@Override
	public List<FloorDetailsPojo> getFloorDetails(FloorRequest floorReq) {
		LOGGER.info("*** The control is inside of the getFloorDetails in FloorDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_ALL_FLOOR_DETAILS);
		/* Getting Details by Site Id */
		if(Util.isNotEmptyObject(floorReq.getSiteId())) {
			sqlQuery.append(" AND ST.SITE_ID=:SITE_ID ");
			namedParameters.addValue("SITE_ID", floorReq.getSiteId());
		}
		/* Getting Details by Block Det Id */
		if(Util.isNotEmptyObject(floorReq.getBlockDetIds())) {
			sqlQuery.append(" AND BLD.BLOCK_DET_ID IN (:BLOCK_DET_IDS) ");
			namedParameters.addValue("BLOCK_DET_IDS", floorReq.getBlockDetIds());
		}
		sqlQuery.append(" ORDER BY BLOCK_NAME,FLOOR_ID,NAME ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		return resultSetMapper.getResult(FloorDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}

}
