/**
 * 
 */
package com.sumadhura.employeeservice.service.mappers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.Assert;

import com.sumadhura.employeeservice.exception.EntityNotFoundException;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;

import lombok.NonNull;

/**
 * ResultSetMapper class provides generic mapping to ResultSet Object to PersistentDTO.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */

@Named(value = "ResultSetMapper")
public class ResultSetMapper<T> {
	
	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	private final Logger logger = Logger.getLogger(ResultSetMapper.class);
	
	@SuppressWarnings("unchecked")
	public List<T> mapRersultSetToObject(ResultSet rs, @SuppressWarnings("rawtypes") Class outputClass) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> outputList = null;
		logger.info("**** The Control is inside the mapRersultSetToObject in ResultSetMapper class *****"+rs);
		 Assert.notNull(rs, "Resultset must not be null");
			// make sure resultset is not null
			if (rs != null) {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();
					logger.info("**** The size of the resultset object is *****"+rs.getFetchSize());
					do{
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);
							// iterating over outputClass attributes to check if any attribute has 'Column' annotation with matching 'name' value
							for (Field field : fields) {
							if (field.isAnnotationPresent(Column.class)) {
									Column column = field.getAnnotation(Column.class);
									if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
										if(rsmd.getColumnType(_iterator + 1)== Types.CLOB) {
											LobHandler lobHandler = new DefaultLobHandler();
											String requestData = lobHandler.getClobAsString(rs,columnName);
											BeanUtils.setProperty(bean, field.getName(), requestData);
										}else {
										/* here we can check column datatypes  */				
										BeanUtils.setProperty(bean, field.getName(), columnValue);
										}
										break;
									}
								}
							}
						}
						if (outputList == null) {
							outputList = new ArrayList<T>();
						}
						outputList.add(bean);
					}while(rs.next());

				} else {
					// bean class not annotated with entity annotation
					String msg = "Pojo class is not annotated with @Entity Annotation.";
					throw new EntityNotFoundException(msg);
				}
			} else {
				// here result set object is null or empty.
				return null;
			}
		
		return outputList;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> mapRersultSetToPojo(ResultSet rs, @SuppressWarnings("rawtypes") Class outputClass) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> outputList = null;
		logger.info("**** The Control is inside the mapRersultSetToObject in ResultSetMapper class *****"+rs);
		 Assert.notNull(rs, "Resultset must not be null");
			// make sure resultset is not null
			if (rs != null) {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();
					logger.info("**** The size of the resultset object is *****"+rs.getFetchSize());
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd
								.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd
									.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);
							// iterating over outputClass attributes to check if any attribute has 'Column' annotation with matching 'name' value
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field
											.getAnnotation(Column.class);
									if (column.name().equalsIgnoreCase(
											columnName)
											&& columnValue != null) {
						           /* here we can check column datatypes  */				
										BeanUtils.setProperty(bean, field
												.getName(), columnValue);
										break;
									}
								}
							}
						}
						if (outputList == null) {
							outputList = new ArrayList<T>();
						}
						outputList.add(bean);

				} else {
					// bean class not annotated with entity annotation
					String msg = "Pojo class is not annotated with @Entity Annotation.";
					throw new EntityNotFoundException(msg);
				}
			} else {
				// here result set object is null or empty.
				return null;
			}
		
		return outputList;
	}
	
	@SuppressWarnings("hiding")
	public <T> List<T> getResult(@NonNull final Class<T> clazz,@NonNull final String query,@NonNull final MapSqlParameterSource namedParameters) {
		//logger.info("**** The control is inside the getResult in EmployeeTicketDaoImpl ****"+query);
		List<List<T>> pojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<T>>() {
					public List<T> mapRow(ResultSet rs, int arg1) throws SQLException {
						//logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
						List<T> pojoLIST = null;
						try {
							pojoLIST = resultSetMapper.mapRersultSetToObject(rs, clazz);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						//logger.info("***** The pojoLIST objects  is *****" + pojoLIST);
						return pojoLIST;
					}
				});
		
		if (pojoLists.isEmpty()) {
			pojoLists.add(new ArrayList<T>());
		}
		
		return pojoLists.get(0);
	}
	@SuppressWarnings("hiding")
	public <T> T getSingleValue(Class<T> requiredType,@NonNull final String query,@NonNull final MapSqlParameterSource namedParameters) {
		try {
		  return nmdPJdbcTemplate.queryForObject(query, namedParameters, requiredType);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
}