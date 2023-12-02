package com.sumadhura.employeeservice.service.mappers;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.sumadhura.employeeservice.exception.ResultSetMappingException;

import lombok.NonNull;
/**
 * @author @NIKET CH@V@N
 * @since 21-01-2020
 * @time 7:30 PM
 * @description common class to get List of requested object, pass Type of class as generic 
 * @param <class>
 */
//@Component
public class ExtractDataFromResultSet<T> implements RowMapper<List<T>>{

	private final Logger log = Logger.getLogger(ExtractDataFromResultSet.class);

 	private Class<T> clazz;

	public ExtractDataFromResultSet(Class<T> clazz) {
		this.clazz = clazz;
	}

	public List<T> mapRow(ResultSet rs, int rowNum) throws SQLException {
		//log.info("***** The ResultSet object is ****" + rs);
		final ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
		List<T> financialPropertyDetailsPojoList = null;
		try {
			financialPropertyDetailsPojoList = resultSetMapper.mapRersultSetToObject(rs, clazz);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
			log.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
			String msg = "The Exception is raised while mapping resultset object to Pojo";
			throw new ResultSetMappingException(msg,ex.getCause());
		}
		//log.info("***** The financialPropertyDetailsPojoList objects  is *****" +financialPropertyDetailsPojoList.size());		
		return financialPropertyDetailsPojoList;
	}

	
	@SuppressWarnings("hiding")
	public <T> List<T> getResult(@NonNull final Class<T> clazz,final String query,MapSqlParameterSource namedParameters, NamedParameterJdbcTemplate nmdPJdbcTemplate) {
		log.info("*** The control is inside the getResult in EmployeeTicketDaoImpl ***");
		List<List<T>> pojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<T>>() {
					public List<T> mapRow(ResultSet rs, int arg1) throws SQLException {
						log.info("**** The ResultSet object is ***" + rs);
						final ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
						List<T> pojoLIST = null;
						try {
							pojoLIST = resultSetMapper.mapRersultSetToObject(rs, clazz);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							log.error(
									"*** The Exception is raised while Mapping Resultset object to the PersistenDTO object ***");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						log.info("**** The pojoLIST objects  is ****" + pojoLIST);
						return pojoLIST;
					}
				});
		log.info("** The pojoLists is ****" + pojoLists);
		if (pojoLists.isEmpty()) {
			pojoLists.add(new ArrayList<T>());
		}
		log.info("*** The pojoLists is ***" + pojoLists.get(0));
		return pojoLists.get(0);
	}
	
	public  List<T> returnConstructedListObject(List<List<T>> listObject){
		if (listObject.isEmpty()) {
			listObject.add(new ArrayList<T>());
		}
		return listObject.get(0);	
	}
}
