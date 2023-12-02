package com.sumadhura.employeeservice.persistence.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.NotificationMenuDetailsDTO;
import com.sumadhura.employeeservice.enums.NotificationsSqlQuery;
import com.sumadhura.employeeservice.persistence.dao.MenuDao;

@Repository
public class MenuDaoImpl implements MenuDao {

	

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Override
	public List<NotificationMenuDetailsDTO> getMenuLevelMappingDetailsByMenuName(String menuName,Long empId) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("EMP_ID",empId);
		namedParameters.addValue("NOTIFICATION",menuName);
				
		String getNotificationLevelMenuDetailsByMenuNameByEmpId = NotificationsSqlQuery.GET_NOTIFICATION_LEVEL_MENU_DETAILS_BY_MENU_NAME_BY_EMP_ID;
		getNotificationLevelMenuDetailsByMenuNameByEmpId+=" WHERE EMPLM.EMP_ID=:EMP_ID AND MENU.NAME=:NOTIFICATION  ORDER BY MENULM.LEVEL_ID";

		List<NotificationMenuDetailsDTO> menuDetailsList = nmdPJdbcTemplate.query(getNotificationLevelMenuDetailsByMenuNameByEmpId,namedParameters,new RowMapper<NotificationMenuDetailsDTO>() {
			public NotificationMenuDetailsDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				NotificationMenuDetailsDTO sitePojoObj = new NotificationMenuDetailsDTO();
				sitePojoObj.setLevelId(rs.getLong(1));
				long long1 = rs.getLong(2);
				sitePojoObj.setApproveTo(long1==0?null:long1);
				long value = rs.getLong(3);
				sitePojoObj.setMenuLevelMapId(value==0?null:value);
			
				return sitePojoObj;
			}
		});
		
		return menuDetailsList;
	}

}
