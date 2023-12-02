/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.InboxSiteDetails;
import com.sumadhura.employeeservice.persistence.dto.MessengerDetailsPojo;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Page;
import com.sumadhura.employeeservice.util.PaginationHelper;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;


/**
 * MessengerServiceDaoImpl provides MessengerServiceDao specific services.
 * 
 * @author Venkat_Koniki
 * @since 25.08.2020
 * @time 10:40AM
 */

@Repository("MessengerServiceDaoImpl")
public class MessengerServiceDaoImpl implements MessengerServiceDao {
	
	@Resource
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Resource
	@Qualifier("ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;
	
	private static final Logger LOGGER = Logger.getLogger(MessengerServiceDaoImpl.class);
	
	
	@Override
	public List<MessengerDetailsPojo> getMessagesList(MessengerRequest request){
		LOGGER.info("*** The control inside of the getMessagesList in MessengerServiceDaoImpl ***");
		StringBuilder sqlQuery=new StringBuilder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(Util.isNotEmptyObject(request.getRequestUrl()) && request.getRequestUrl().equalsIgnoreCase("getMessagesList")){
			/* If the request is coming from Customer Single Page Data */
			if(ServiceRequestEnum.CUSTOMER_SINGLE_PAGE.equals(request.getRequestEnum())) {
				/* Getting data based on FlatBook Id */
				sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_MESSAGES_WITHOUT_BOOKING_STATUS).append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_CONVERS_VIEW_STATUS).append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_DATES);
				namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.INTEGER);
				namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()), Types.INTEGER);
				namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()), Types.INTEGER);
				
				/* Getting data based on Flat Id */
				if(Util.isNotEmptyObject(request.getFlatIds())) {
				namedParameters.addValue("FLAT_IDS",request.getFlatIds(), Types.INTEGER);
				sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_FLATIDS);
				}
				
			}else {
				sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_MESSAGES).append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_CONVERS_VIEW_STATUS).append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_DATES);
				namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus(),Status.CANCEL.getStatus()), Types.INTEGER);
				namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()), Types.INTEGER);
				namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()), Types.INTEGER);
				/* searching based on the siteId */
				if(Util.isNotEmptyObject(request.getSiteIds())) {
					namedParameters.addValue("SITE_IDS",request.getSiteIds(), Types.INTEGER);
					sqlQuery.append(SqlQueryTwo.QRY_TO_GET_MESSAGES_WRT_SITEIDS);
				}
				/* searching based on the bloockId */
				if(Util.isNotEmptyObject(request.getBlockIds())) {
					namedParameters.addValue("BLOCK_IDS",request.getBlockIds(), Types.INTEGER);
					sqlQuery.append(SqlQueryTwo.QRY_TO_GET_MESSAGES_WRT_BLOCKSIDS);
				}
				/* searching based on the floorId */
				if(Util.isNotEmptyObject(request.getFloorIds())) {
					namedParameters.addValue("FLOOR_IDS",request.getFloorIds(), Types.INTEGER);
					sqlQuery.append(SqlQueryTwo.QRY_TO_GET_MESSAGES_WRT_FLOOR_IDS);
				}
				/* searching based on the flatId */
				if(Util.isNotEmptyObject(request.getFlatIds())) {
				namedParameters.addValue("FLAT_IDS",request.getFlatIds(), Types.INTEGER);
				sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_FLATIDS);
				}
				/* searching based on the subject */
				if(Util.isNotEmptyObject(request.getSubject())) {
					namedParameters.addValue("SUBJECT","%"+request.getSubject().toLowerCase()+"%", Types.VARCHAR);
					sqlQuery.append(SqlQueryTwo.QRY_TO_GET_MESSAGES_WRT_SUBJECT);
				}
				/* searching based on the customer */
				if(Util.isNotEmptyObject(request.getFlatBookingId())) {
					namedParameters.addValue("FLAT_BOOK_ID",request.getFlatBookingId(), Types.INTEGER);
					sqlQuery.append(SqlQueryTwo.QRY_TO_GET_MESSAGES_WRT_CUSTOMER);
				}
				
				/* searching based on the employeeIds */
				if(Util.isNotEmptyObject(request.getEmployeeIds()) && !request.getEmployeeIds().isEmpty()) {
					sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_EMPLOYEE_IDS);
					namedParameters.addValue("CREATED_BY_ID",request.getEmployeeIds(), Types.INTEGER);
					namedParameters.addValue("CREATED_BY_TYPE",Arrays.asList(Department.EMPLOYEE.getId()), Types.INTEGER);
				}
			}
			
			/*unread and  Last Activity done chats come first  */
			sqlQuery.append(" ORDER BY VIEW_DETAILS, MSG.LAST_CHATTED_DATE DESC ");
			namedParameters.addValue("STARTDATE",request.getStartDate(),Types.TIMESTAMP);
			namedParameters.addValue("ENDDATE",request.getEndDate(),Types.TIMESTAMP);
		}else if(Util.isNotEmptyObject(request.getRequestUrl()) && request.getRequestUrl().equalsIgnoreCase("getUnviewChatCount")) {
			sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_MESSAGES);
			if(request.getMessengerIds().size()<1000) {
				sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_IDS);
				namedParameters.addValue("MESSENGER_IDS",request.getMessengerIds(),Types.INTEGER);
			}else {
				/* In Query doesn't support 1000 elements inplace of that we can use triplets */
				sqlQuery.append(" AND (MSG.MESSENGER_ID,0) IN(");
				for(int i=0; i<request.getMessengerIds().size(); i++) {
					sqlQuery.append(" ("+request.getMessengerIds().get(i)+",0)");
					if(i==(request.getMessengerIds().size()-1)) {
						sqlQuery.append(")");
					}else {
						sqlQuery.append(",");
					}
				}
			}
			sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_ORDER_BY_LAST_ACTIVITY);
			namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.INTEGER);
		}
		return  resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	
	@Override
	public Page<MessengerDetailsPojo> getMessagesList(MessengerRequest request,int pageNo,int pageSize){
		LOGGER.info("*** The control inside of the getMessagesList in MessengerServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_MESSAGES);
		StringBuilder countQuery = new StringBuilder(SqlQuery.QRY_TO_GET_MESSAGES_COUNT);
		StringBuilder sqlQuery=new StringBuilder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		PaginationHelper<MessengerDetailsPojo> paginationHelper = new PaginationHelper<MessengerDetailsPojo>();
		
		if(Util.isNotEmptyObject(request.getRequestUrl()) && request.getRequestUrl().equalsIgnoreCase("getMessagesList")){
			sqlQuery = new StringBuilder().append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_CONVERS_VIEW_STATUS).append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_DATES);
			namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.INTEGER);
			namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()), Types.INTEGER);
			namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()), Types.INTEGER);
			
			if(Util.isNotEmptyObject(request.getFlatIds())) {
			namedParameters.addValue("FLAT_IDS",request.getFlatIds(), Types.INTEGER);
			sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_FLATIDS);
			}
			
			/* searching based on the employeeIds */
			if(Util.isNotEmptyObject(request.getEmployeeIds()) && !request.getEmployeeIds().isEmpty()) {
				sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_EMPLOYEE_IDS);
				namedParameters.addValue("CREATED_BY_ID",request.getEmployeeIds(), Types.INTEGER);
				namedParameters.addValue("CREATED_BY_TYPE",Arrays.asList(Department.EMPLOYEE.getId()), Types.INTEGER);
			}
			
			/* Last Activity done chats come first  */
			sqlQuery.append(SqlQuery.QRY_TO_GET_MESSAGES_ORDER_BY_LAST_ACTIVITY);
			namedParameters.addValue("STARTDATE",request.getStartDate(),Types.TIMESTAMP);
			namedParameters.addValue("ENDDATE",request.getEndDate(),Types.TIMESTAMP);
		}else if(Util.isNotEmptyObject(request.getRequestUrl()) && request.getRequestUrl().equalsIgnoreCase("getUnviewChatCount")) {
			sqlQuery = new StringBuilder().append(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_IDS)
			.append(SqlQuery.QRY_TO_GET_MESSAGES_ORDER_BY_LAST_ACTIVITY);
			namedParameters.addValue("MESSENGER_IDS",request.getMessengerIds(),Types.INTEGER);
			namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.INTEGER);
		}
		
		return paginationHelper.fetchPage(nmdPJdbcTemplate,countQuery.append(sqlQuery).toString(),query.append(sqlQuery).toString(),namedParameters,pageNo,pageSize,
	                new ParameterizedRowMapper<MessengerDetailsPojo>() {
	                    public MessengerDetailsPojo mapRow(ResultSet rs, int arg1) throws SQLException {
	                    	LOGGER.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<MessengerDetailsPojo> resultSetMapper = new ResultSetMapper<MessengerDetailsPojo>();
							List<MessengerDetailsPojo> messengerDetailsPojoLIST = null;
							try {
								messengerDetailsPojoLIST = resultSetMapper.mapRersultSetToPojo(rs, MessengerDetailsPojo.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								LOGGER.error(
										"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg, ex.getCause());
							}
							LOGGER.info("***** The messengerDetailsPojoLIST objects  is *****" + messengerDetailsPojoLIST);
							return messengerDetailsPojoLIST.get(0);
	                    }
	                }
	        );
	}

	@Override
	public List<MessengerDetailsPojo> startNewChat(MessengerRequest request) {
		LOGGER.info("*** The control inside of the startNewChat in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_START_NEW_CHAT_DETAILS);
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.INTEGER);
		namedParameters.addValue("FLAT_IDS",request.getFlatIds(), Types.INTEGER);
		return resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public List<MessengerDetailsPojo> getMessengerLvlMasterDeatils(MessengerRequest request) {
		LOGGER.info("*** The control inside of the getMessengerLvlMasterDeatils in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_MEESENGER_LEVEL_MASTER)
				              .append(SqlQuery.QRY_TO_GET_MEESENGER_LEVEL_MASTER_WRT_SITE_DEPT_ID);
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		namedParameters.addValue("SITE_ID",request.getSiteIds(), Types.INTEGER);
		namedParameters.addValue("DEPT_ID",request.getDeptIds(), Types.INTEGER);
		return resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public List<MessengerDetailsPojo> getLastMessengerConversationDetails(MessengerRequest request) {
		LOGGER.info("*** The control inside of the getLastMessengerConversationDetails in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_LAST_MESSENGER_CONVERSATION_WRO_MESSENGER_ID);
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()), Types.INTEGER);
		namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()), Types.INTEGER);
		return resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public List<MessengerDetailsPojo> getMessengerConversationViewStatusCountDetails(MessengerRequest request) {
		LOGGER.info("*** The control inside of the getMessengerConversationViewStatusCountDetails in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = null;
		if(MetadataId.EMPLOYEE.getId().equals(request.getRecipientType())) {
			sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_CONVERS_VIEW_STATUS_COUNT_EMP);
		}else {
			sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_MESSAGES_WRT_MESSENGER_CONVERS_VIEW_STATUS_COUNT);
		}
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()), Types.INTEGER);
		namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()), Types.INTEGER);
		namedParameters.addValue("VIEW_STATUS",Arrays.asList(MetadataId.NO.getId()), Types.INTEGER);
		return resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public Long saveMessenger(MessengerRequest request) {
		LOGGER.info("*** The control inside of the saveMessenger in MessengerServiceDaoImpl ***");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MESSAGE_ID", "MSG_"+Util.generateRandomNumber(), Types.VARCHAR);
		namedParameters.addValue("CREATED_BY_ID",request.getCreatedById(),Types.INTEGER);
		namedParameters.addValue("CREATED_BY_TYPE",request.getCreatedByType(), Types.INTEGER);
		namedParameters.addValue("STATUS",Status.ACTIVE.getStatus(),Types.INTEGER);
		namedParameters.addValue("SEND_TO",request.getSendTo(),Types.INTEGER);
		namedParameters.addValue("SEND_TYPE",request.getSendType(),Types.INTEGER);
		namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("LAST_CHATTED_DATE",new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("SUBJECT",request.getSubject(), Types.CLOB);
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_GET_INSERT_MESSENGER, namedParameters,keyHolder,new String[] { "MESSENGER_ID" });
		Long pk = keyHolder.getKey().longValue();
		LOGGER.info("**** The Record Noumber stored into the Messenger table is *****"+pk);
		return pk;
	}
	
	@Override
	public Long saveMessengerConversation(MessengerRequest request) {
		LOGGER.info("*** The control inside of the saveMessengerConversation in MessengerServiceDaoImpl ***");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MESSENGER_ID",request.getMessengerId(), Types.INTEGER);
		namedParameters.addValue("CREATED_BY_ID", request.getCreatedById(),Types.INTEGER);
		namedParameters.addValue("CREATED_BY_TYPE",request.getCreatedByType(), Types.INTEGER);
		namedParameters.addValue("CHAT_MSG",request.getMessage(), Types.CLOB);
		namedParameters.addValue("STATUS",Status.ACTIVE.getStatus(),Types.INTEGER);
		namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("MODIFIED_DATE", null, Types.TIMESTAMP);
		namedParameters.addValue("CHAT_MSG_WITHOUT_TAGS", request.getChatMsgWithoutTags(), Types.CLOB);
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_GET_INSERT_MESSENGER_CONVERSATION, namedParameters,keyHolder,new String[] { "MESSENGER_CONVERSATION_ID" });
		Long pk = keyHolder.getKey().longValue();
		LOGGER.info("**** The Record Noumber stored into the MessengerConversation table is *****"+pk);
		return pk;
	}
	
	
	@Override
	public int[] saveMessengerPersonsInvolved(MessengerRequest request) {
		LOGGER.info("*** The control inside of the saveMessengerPersonsInvolved in MessengerServiceDaoImpl ***");
		int[] pk = nmdPJdbcTemplate.batchUpdate(SqlQuery.QRY_TO_GET_INSERT_MESSENGER_PERSONS_INVOLVED, getMessengerPersonsInvolvedSqlParameterObjectList(request));
		LOGGER.info("**** The Record Noumber stored into the MessengerPersonsInvolved table is *****"+pk);
		return pk;
	}
	
	private SqlParameterSource[] getMessengerPersonsInvolvedSqlParameterObjectList(MessengerRequest request){
		SqlParameterSource[] oo=new SqlParameterSource[new HashSet<Long>(request.getEmployeeIds()).size()];
		int i=0;
		for (Long empId : (new HashSet<Long>(request.getEmployeeIds()))) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("MESSENGER_ID",request.getMessengerId(), Types.INTEGER);
			namedParameters.addValue("TYPE",MetadataId.EMPLOYEE.getId(), Types.INTEGER);
			namedParameters.addValue("TYPE_ID",empId, Types.INTEGER);
			namedParameters.addValue("RECIPENT",MetadataId.CC.getId(),Types.INTEGER);
			namedParameters.addValue("STATUS",Status.ACTIVE.getStatus(),Types.INTEGER);
			namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
			namedParameters.addValue("MODIFIED_DATE", null, Types.TIMESTAMP);
			oo[i]=namedParameters;
			i++;
		}
		return oo;
	}
	
	@Override
	public int[] saveMessengerConversationViewStatus(MessengerRequest request) {
		LOGGER.info("*** The control inside of the saveMessengerConversationViewStatus in MessengerServiceDaoImpl ***");
		int[] pk = nmdPJdbcTemplate.batchUpdate(SqlQuery.QRY_TO_GET_INSERT_MESSENGER_CONVERS_VIEW_STATUS, getMessengerConversationViewStatusSqlParameterObjectList(request));
		LOGGER.info("**** The Record Noumber stored into the saveMessengerConversationViewStatus table is *****"+pk);
		return pk;
	}
	
	private SqlParameterSource[] getMessengerConversationViewStatusSqlParameterObjectList(MessengerRequest request){
		SqlParameterSource[] oo=new SqlParameterSource[request.getReciepientMap().size()];
		int i=0;
		for (String key : request.getReciepientMap().keySet()) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("MESSENGER_CONVERSATION_ID",request.getMessengerConversationId(), Types.INTEGER);
			namedParameters.addValue("RECIPIENT_TYPE",Util.get(request.getReciepientMap().get(key).entrySet(),"key"), Types.INTEGER);
			/* do modification here  */
			namedParameters.addValue("RECIPIENT_ID",Integer.valueOf(key.split("-")[0]), Types.INTEGER);
			namedParameters.addValue("VIEW_STATUS",Util.get(request.getReciepientMap().get(key).entrySet(),"value"),Types.INTEGER);
			namedParameters.addValue("STATUS",Status.ACTIVE.getStatus(),Types.INTEGER);
			namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
			namedParameters.addValue("MODIFIED_DATE", null, Types.TIMESTAMP);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	
	@Override
	public Long saveDocumentLocation(Long messegeConversationId,String location,String type) {
		LOGGER.info("*** The control inside of the saveMessengerConversation in MessengerServiceDaoImpl ***");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MESSENGER_CONVERSATION_ID",messegeConversationId, Types.INTEGER);
		namedParameters.addValue("LOCATION", location,Types.VARCHAR);
		namedParameters.addValue("STATUS",Status.ACTIVE.getStatus(),Types.INTEGER);
		namedParameters.addValue("TYPE", type,Types.VARCHAR);
		namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("MODIFIED_DATE", null, Types.TIMESTAMP);
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_GET_INSERT_DOCUMENT_LOCATION, namedParameters,keyHolder,new String[] { "DOCUMENT_LOC_ID" });
		Long pk = keyHolder.getKey().longValue();
		LOGGER.info("**** The Record Noumber stored into the saveDocumentLocation table is *****"+pk);
		return pk;
	}
	
	@Override
	public int updateMessengerLastChattedDate(MessengerRequest request) {
		LOGGER.info("*** The control is inside of the updateMessengerLastChattedDate in MessengerServiceDaoImpl ***");
		String sqlQuery = SqlQuery.QRY_TO_UPDATE_MESSENGER_CONVERS_VIEW_STATUS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("LAST_CHATTED_DATE", new Timestamp(System.currentTimeMillis()),Types.TIMESTAMP);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(System.currentTimeMillis()),Types.TIMESTAMP);
		namedParameters.addValue("MESSENGER_ID",request.getMessengerId(),Types.BIGINT);
		namedParameters.addValue("STATUS", Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
		int rowsUpdated = nmdPJdbcTemplate.update(sqlQuery, namedParameters);
		LOGGER.info("***"+rowsUpdated +" Records are updated in Messenger successfully.***");
		return rowsUpdated;
	}
	
	@Override
	public List<MessengerDetailsPojo> getChatDetails(@NonNull MessengerRequest request){
		LOGGER.info("*** The control inside of the getChatDetails in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_MESSENGER_CHAT_DETAILS);
		namedParameters.addValue("CREATED_BY_TYPE",MetadataId.EMPLOYEE.getId(), Types.INTEGER);
		namedParameters.addValue("STATUS",Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()), Types.INTEGER);
		namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()), Types.INTEGER);
		namedParameters.addValue("MESSENGER_ID",request.getMessengerId(),Types.INTEGER);
		return resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public List<EmployeeDetailsPojo> getMessengerPersonsInvolvedeDetails(@NonNull MessengerRequest request){
		LOGGER.info("*** The control inside of the getMessengerPersonsInvolvedeDetails in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_MESSENGER_PERSONS_INVOLVED);
		namedParameters.addValue("TYPE",Arrays.asList(MetadataId.EMPLOYEE.getId()), Types.INTEGER);
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		namedParameters.addValue("RECIPENT",Arrays.asList(MetadataId.CC.getId()),Types.INTEGER);
		namedParameters.addValue("MESSENGER_ID",Arrays.asList(request.getMessengerId()),Types.INTEGER);
		return resultSetMapper.getResult(EmployeeDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	
	@Override
	public List<MessengerDetailsPojo> getMessengerConversationViewStatusWrtCustomer(@NonNull MessengerRequest request){
		LOGGER.info("*** The control inside of the getMessengerConversationViewStatusWrtCustomer in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_MESSENGER_CONVERS_VIEW_STATUS_WRT_CUST);
		namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(MetadataId.FLAT_BOOKING.getId()), Types.INTEGER);
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		namedParameters.addValue("MESSENGER_CONVERSATION_ID",request.getConversationIds(),Types.INTEGER);
		return resultSetMapper.getResult(MessengerDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	
	@Override
	public int updateMessengerViewStatusDetails(@NonNull MessengerRequest request) {
		LOGGER.info("*** The control is inside of the updateMessengerViewStatusDetails in MessengerServiceDaoImpl ***");
		String sqlQuery = SqlQuery.QRY_TO_UPDATE_MESSENGER_CONVERSATION_VIEW_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(System.currentTimeMillis()),Types.TIMESTAMP);
		namedParameters.addValue("VIEW_STATUS",MetadataId.YES.getId(),Types.BIGINT);
		namedParameters.addValue("MESSENGER_ID",request.getMessengerId(),Types.BIGINT);
		namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()),Types.BIGINT);
		namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()),Types.BIGINT);
		namedParameters.addValue("STATUS", Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
		int rowsUpdated = nmdPJdbcTemplate.update(sqlQuery, namedParameters);
		LOGGER.info("***"+rowsUpdated +" Records are updated in MessengerConversationViewStatus successfully.***");
		return rowsUpdated;
	}
	
	@Override
	public List<EmployeeDetailsPojo> getEmployeeDropDown(MessengerRequest reques){
		LOGGER.info("*** The control inside of the getEmployeeDropDown in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery=new StringBuilder(SqlQuery.QRY_TO_GET_EMP_DROPDOWN_);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.INTEGER);
		namedParameters.addValue("MODULE_ID",Integer.valueOf("18"), Types.INTEGER);
	//	namedParameters.addValue("SUBMODULE_ID",Integer.valueOf("51"), Types.INTEGER);
		namedParameters.addValue("SUBMODULE_ID",Integer.valueOf("46"), Types.INTEGER);
		return resultSetMapper.getResult(EmployeeDetailsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public Integer updateMessengerViewStatusAsUnread(@NonNull MessengerRequest request) {
		LOGGER.info("*** The control is inside of the updateMessengerViewStatusAsUnread in MessengerServiceDaoImpl ***");
		String sqlQuery = SqlQuery.QRY_TO_UPDATE_MESSENGER_CONVERSATION_VIEW_AS_UNREAD;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(System.currentTimeMillis()),Types.TIMESTAMP);
		namedParameters.addValue("VIEW_STATUS",MetadataId.NO.getId(),Types.BIGINT);
		namedParameters.addValue("MESSENGER_CONVERSATION_ID",request.getMessengerConversationId(),Types.BIGINT);
		namedParameters.addValue("RECIPIENT_TYPE",Arrays.asList(request.getRecipientType()),Types.BIGINT);
		namedParameters.addValue("RECIPIENT_ID",Arrays.asList(request.getRecipientId()),Types.BIGINT);
		namedParameters.addValue("STATUS", Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
		Integer rowsUpdated = nmdPJdbcTemplate.update(sqlQuery, namedParameters);
		LOGGER.info("***"+rowsUpdated +" Records are updated in MessengerConversationViewStatus as Unread successfully.***");
		return rowsUpdated;
	}
	@Override
    public  Map<String, Object> getFlatBookId(@NonNull MessengerRequest request){
       LOGGER.info("*** The control is inside of the getFlatBookId in MessengerServiceDaoImpl ***");
       String sqlQuery = SqlQueryTwo.QRY_TO_GET_BOOKING_ID;
       Map<String, Object> map =new HashMap<String, Object>();
 	   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
 	   namedParameters.addValue("FLAT_NO", request.getFlatNo(), Types.VARCHAR);
 	   namedParameters.addValue("SITE_ID", request.getSiteIds(), Types.BIGINT);
 	   namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
 	 //  Map<String, Object> map=  nmdPJdbcTemplate.queryForMap(sqlQuery,namedParameters);
 	   List<MessengerDetailsPojo> list=  resultSetMapper.getResult(MessengerDetailsPojo.class,sqlQuery,namedParameters);
 	   for(MessengerDetailsPojo pojo:list)
 	   {
			map.put("FLAT_BOOK_ID", pojo.getFlatBookingId());
			map.put("NAME", pojo.getSiteName());
			map.put("APP_STATUS", pojo.getAppStatus());
 	   }
	   return map;
    }
	  /*getting  both CC and Access site data  */
	@Override
	public List<InboxSiteDetails> getSiteDetailList(MessengerRequest messengerRequest) {
	    LOGGER.info("*** The control is inside of the getSiteDetailList in MessengerServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		List<InboxSiteDetails> list=null;
		String sqlQuery = SqlQueryTwo.QRY_TO_GET_SITES;
		namedParameters.addValue("SITE_ID",Util.isNotEmptyObject(messengerRequest.getSiteIds())?new ArrayList<>(messengerRequest.getSiteIds()):Arrays.asList(0l));
		namedParameters.addValue("RECIPENT",MetadataId.CC.getId());
		namedParameters.addValue("TYPE_ID",messengerRequest.getEmployeeId());
		list=  resultSetMapper.getResult(InboxSiteDetails.class,sqlQuery,namedParameters);
		namedParameters=null;
		namedParameters=null;
		return list;
	}
}
