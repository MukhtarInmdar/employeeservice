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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
//import org.springframework.data.domain.Page;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Module;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.Visibility;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dto.AppRegistrationPojo;
import com.sumadhura.employeeservice.persistence.dto.ChangeTicketTypePojo;
import com.sumadhura.employeeservice.persistence.dto.ChatInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLeaveDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLevelDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.LevelPojo;
import com.sumadhura.employeeservice.persistence.dto.MailConfigurationDtlsDTO;
import com.sumadhura.employeeservice.persistence.dto.StatusPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketConversationDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscLevelMapPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscaLevelEmpMap;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationExtenstionApprovalLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationExtenstionApprovalLevelPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelEmployeeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketExtendedEscalationApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketForwardMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPendingDeptDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoRequestPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypePojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypesPojo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.MailConfigurationDtlsInfo;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Page;
import com.sumadhura.employeeservice.util.PaginationHelper;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * EmployeeTicketDaoImpl class provides Employee Ticketing specific services.
 *
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
@Repository("EmployeeTicketDaoImpl")
public class EmployeeTicketDaoImpl implements EmployeeTicketDao {

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	@Resource(name="ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;
	
	private static final Logger LOGGER = Logger.getLogger(EmployeeTicketDaoImpl.class);

	@Override
	public List<EmployeeDetailsPojo> getEmployeeDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("*** The control is inside the getEmployeeDetails in getEmployeeDetails **** ");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_DETAILS).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getCustomerTicketDetails")) {
			query = new StringBuilder(query).append(" WHERE ED.EMP_DETAILS_ID = :EMP_DETAILS_ID ")
					.append(" AND ED.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("EMP_DETAILS_ID", employeeTicketRequestInfo.getEmployeeDetailsId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getEmployeeDetails")) {
			query = new StringBuilder(query).append(
					" WHERE ED.EMP_NAME LIKE '%" + (Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeName())
							? employeeTicketRequestInfo.getEmployeeName()
							: "") + "%' ")
					.append(" AND ED.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("isEmployeeAvailable")) {
			query = new StringBuilder(query).append(" WHERE ED.DEPT_ID = :DEPT_ID ")
					.append(" AND ED.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("DEPT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketOwner")) {
			   query = new StringBuilder(query).append(" INNER JOIN TICKET_TYPE_DETAILS TTD ON ED.EMP_DETAILS_ID = TTD.EMPLOYEE_DETAILS_ID WHERE TTD.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ")
					.toString();
			   namedParameters.addValue("TICKET_TYPE_DETAILS_ID", employeeTicketRequestInfo.getTicketTypeDetailsId(), Types.BIGINT); 
		}
		else {
			query = new StringBuilder(query).append(" WHERE ED.EMPLOYEE_ID = :EMPLOYEE_ID ")
					.append(" AND ED.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("EMPLOYEE_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_DETAILS IS *****" + query);
		List<List<EmployeeDetailsPojo>> employeeDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<EmployeeDetailsPojo>>() {
					public List<EmployeeDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.debug("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeDetailsPojo> resultSetMapper = new ResultSetMapper<EmployeeDetailsPojo>();
						List<EmployeeDetailsPojo> employeeDetailsPojoLIST = null;
						try {
							employeeDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									EmployeeDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The employeeDetailsPojoLists objects  is *****" + employeeDetailsPojoLIST);
						return employeeDetailsPojoLIST;
					}
				});
		LOGGER.info("*** The employeeDetailsPojoLists is *****" + employeeDetailsPojoLists);
		if (employeeDetailsPojoLists.isEmpty()) {
			employeeDetailsPojoLists.add(new ArrayList<EmployeeDetailsPojo>());
		}
		LOGGER.info("**** The employeeDetailsPojoLists is ****" + employeeDetailsPojoLists.get(0));
		return employeeDetailsPojoLists.get(0);
	}

	@Override
	public List<TicketTypeDetailsPojo> getTicketTypeDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the getTicketTypeDetails in EmployeeTicketDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS);
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getCustomerTicketDetails")) {
			    query.append(" WHERE TD.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ")
				//.append(" AND TD.STATUS = :STATUS ")
			     .toString();
			namedParameters.addValue("TICKET_TYPE_DETAILS_ID", employeeTicketRequestInfo.getTicketTypeDetailsId(),Types.BIGINT);
			//namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTicket")) {
			query.append(" WHERE TD.EMPLOYEE_DETAILS_ID = :EMPLOYEE_DETAILS_ID ")
			    // .append(" AND TD.STATUS = :STATUS ")
				 .toString();
			namedParameters.addValue("EMPLOYEE_DETAILS_ID", employeeTicketRequestInfo.getEmployeeDetailsId(),Types.BIGINT);
			//namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		} else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketOwner")){
			query.append(" WHERE TD.EMPLOYEE_DETAILS_ID = :EMPLOYEE_DETAILS_ID ")
			.append(" AND TD.DEPARTMENT_ID =:DEPARTMENT_ID  AND ")
			.append(" TD.TICKET_TYPE_ID =(SELECT TICKET_TYPE_ID FROM TICKET WHERE TICKET_ID=:TICKET_ID) AND ")
			.append(" TD.SITE_ID =( SELECT BD.SITE_ID FROM TICKET T JOIN FLAT_BOOKING FB ON(FB.FLAT_BOOK_ID=T.BOOKING_ID) ")
			.append(" JOIN FLAT F ON(F.FLAT_ID=FB.FLAT_ID) JOIN FLOOR_DETAILS FD ON(FD.FLOOR_DET_ID=F.FLOOR_DET_ID)")
			.append(" JOIN BLOCK_DETAILS BD ON(BD.BLOCK_DET_ID=FD.BLOCK_DET_ID) WHERE T.TICKET_ID=:TICKET_ID AND ROWNUM=1)")
			.toString();
			namedParameters.addValue("EMPLOYEE_DETAILS_ID", employeeTicketRequestInfo.getEmpDetailsId(),Types.BIGINT);
			namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
			namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketOwnerDropDown")){
			query.append(" INNER JOIN TICKET T ")
			     .append(" ON TD.TICKET_TYPE_DETAILS_ID = T.TICKET_TYPE_DETAILS_ID ")
			     .append(" WHERE T.TICKET_ID IN (:TICKET_IDS) ")
			.toString();
			namedParameters.addValue("TICKET_IDS",employeeTicketRequestInfo.getTicketIds(), Types.BIGINT);
		}
		else {
			query.append(" INNER JOIN TICKET T ON TD.TICKET_TYPE_DETAILS_ID = T.TICKET_TYPE_DETAILS_ID ")
			.append(" WHERE TD.EMPLOYEE_DETAILS_ID = :EMPLOYEE_DETAILS_ID ")
					.append(" AND TD.DEPARTMENT_ID = :DEPARTMENT_ID ")
					//.append(" AND TD.SITE_ID = :SITE_ID ")
					.append(" AND TD.SITE_ID IN (:SITE_ID) ")
					//.append(" AND TD.STATUS = :STATUS ")
					.toString();
			namedParameters.addValue("EMPLOYEE_DETAILS_ID", employeeTicketRequestInfo.getEmployeeDetailsId(),
					Types.BIGINT);
			namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
			//namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.BIGINT);
			namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
			//namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_TICKET_TYPE_DETAILS IS *****" + query);

		List<List<TicketTypeDetailsPojo>> TicketTypeDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<TicketTypeDetailsPojo>>() {
					public List<TicketTypeDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketTypeDetailsPojo> resultSetMapper = new ResultSetMapper<TicketTypeDetailsPojo>();
						List<TicketTypeDetailsPojo> TicketTypeDetailsPojoLIST = null;
						try {
							TicketTypeDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketTypeDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info(
								"***** The TicketTypeDetailsPojoLIST objects  is *****" + TicketTypeDetailsPojoLIST);
						return TicketTypeDetailsPojoLIST;
					}
				});

		LOGGER.info("*** The TicketTypeDetailsPojoLists is *****" + TicketTypeDetailsPojoLists);
		if (TicketTypeDetailsPojoLists.isEmpty()) {
			TicketTypeDetailsPojoLists.add(new ArrayList<TicketTypeDetailsPojo>());
		}
		LOGGER.info("**** The TicketTypeDetailsPojoLists is ****" + TicketTypeDetailsPojoLists.get(0));
		return TicketTypeDetailsPojoLists.get(0);
	}

	@Override
	public List<TicketEscalationPojo> getTicketEscalationDtls(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the getTicketEscalationDtls in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCALATION).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getSystemEscalatedTicketDetails.spring")) {
			query = new StringBuilder(query)
					.append(" INNER JOIN TICKET T ")
					.append(" ON T.TICKET_ID = TE.TICKET_ID AND T.STATUS = 6 AND T.TICKET_STATUS <> '11' ")
					.append(" WHERE TE.ESCALATION_TO = :ESCALATION_TO ")
					.append(" AND TE.STATUS = :STATUS ")
					.toString();
			namedParameters.addValue("ESCALATION_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		} else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTicketEscaltionDtls.spring")) {
			query = new StringBuilder(query)
					.append(" INNER JOIN TICKET T ")
					.append(" ON T.TICKET_ID = TE.TICKET_ID AND T.STATUS = 6 AND T.TICKET_STATUS <> '11' ")
					.append(" WHERE TE.TICKET_ESCALATION_ID = :TICKET_ESCALATION_ID AND TE.STATUS = 6 ")
					.toString();
			/* Status check no need for ticket-Escaltion 
			 .append(" AND TE.STATUS = :STATUS ").toString(); 
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
			 */
		namedParameters.addValue("TICKET_ESCALATION_ID", employeeTicketRequestInfo.getTicketEscalationId(), Types.BIGINT);
		
		/* checking weather the ticket is alredy escalated or not. */
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) &&  employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("checkingTicketEscalationLevel")){
			query = new StringBuilder(query)
					.append(" WHERE TE.TICKET_ID = :TICKET_ID ORDER BY  TE.TICKET_ESCALATION_ID ")
					.toString();
			namedParameters.addValue("TICKET_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l, Types.BIGINT);
		
		/*  getting TicketEscalation tickets for internal level shifting */
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) &&  employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("internalTicketEscalation")) {
			query = SqlQuery.QRY_TO_GET_TICKET_ESC_LVL_MAP_FOR_ESCALATION;
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}
		else {			
			query = new StringBuilder(query)
					.append(" INNER JOIN TICKET T ")
					.append(" ON T.TICKET_ID = TE.TICKET_ID AND T.STATUS = 6 AND T.TICKET_STATUS <> '11' ")
					.append(" WHERE TE.TICKET_ID = :TICKET_ID ")
					.append(" AND TE.STATUS = :STATUS ").toString();
			namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_TICKET_ESCALATION IS *****" + query);

		List<List<TicketEscalationPojo>> TicketEscalationPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<TicketEscalationPojo>>() {
					public List<TicketEscalationPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketEscalationPojo> resultSetMapper = new ResultSetMapper<TicketEscalationPojo>();
						List<TicketEscalationPojo> TicketEscalationPojoLIST = null;
						try {
							TicketEscalationPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketEscalationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The TicketEscalationPojoLIST objects  is *****" + TicketEscalationPojoLIST);
						return TicketEscalationPojoLIST;
					}
				});
		LOGGER.debug("*** The TicketEscalationPojoLists is *****" + TicketEscalationPojoLists);
		if (TicketEscalationPojoLists.isEmpty()) {
			TicketEscalationPojoLists.add(new ArrayList<TicketEscalationPojo>());
		}
		LOGGER.debug("**** The TicketEscalationPojoLists is ****" + TicketEscalationPojoLists.get(0));
		return TicketEscalationPojoLists.get(0);
	}

	@Override
	public List<TicketCommentsPojo> getTicketComments(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getTicketComments in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_COMMENTS)
				.append(" WHERE TC.TICKET_ID = :TICKET_ID ")
				.append(" AND TC.STATUS_ID = :STATUS_ID ORDER BY TICKET_COMMENTS_ID ").toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKET_COMMENTS IS *****" + query);
		List<List<TicketCommentsPojo>> ticketCommentsPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<TicketCommentsPojo>>() {
					public List<TicketCommentsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketCommentsPojo> resultSetMapper = new ResultSetMapper<TicketCommentsPojo>();
						List<TicketCommentsPojo> ticketCommentsPojoLIST = null;
						try {
							ticketCommentsPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketCommentsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketCommentsPojoLIST objects  is *****" + ticketCommentsPojoLIST);
						return ticketCommentsPojoLIST;
					}
				});
		LOGGER.debug("*** The TicketCommentsPojoLists is *****" + ticketCommentsPojoLists);
		if (ticketCommentsPojoLists.isEmpty()) {
			ticketCommentsPojoLists.add(new ArrayList<TicketCommentsPojo>());
		}
		LOGGER.debug("**** The TicketCommentsPojoLists is ****" + ticketCommentsPojoLists.get(0));
		return ticketCommentsPojoLists.get(0);
	}

	@Override
	public List<TicketEscalationExtenstionApprovalLevelPojo> getTicketEscalationExtenstionApprovalLevalDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info(
				"***** The control is inside the getTicketEscalationExtenstionApprovalLevalDetails in EmployeeTicketDaoImpl *****");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_LEVAL);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getExtendEsacalationTimeDetails")) {
			query.append(SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_LEVAL_WTO_APPROVER_ID);
			namedParameters.addValue("APPROVER_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		} else {

			if (TimeUtil.differenceBetweenDays(employeeTicketRequestInfo.getEscalationTime(),employeeTicketRequestInfo.getExtendedEscalationTime()) > Integer
					.valueOf(2)) {
				query.append(
						SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_LEVAL_WTO_TICKET_TYPE_ID_TYPE_NO_OF_DAYS_GREATER_THAN_2);
			} else {
				query.append(
						SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_LEVAL_WTO_TICKET_TYPE_ID_TYPE_NO_OF_DAYS_LESS_THAN_2);
			}
			namedParameters.addValue("TICKET_TYPE_ID", employeeTicketRequestInfo.getTicketTypeId(), Types.BIGINT);
			namedParameters.addValue("TYPE", employeeTicketRequestInfo.getTypeOf(), Types.BIGINT);
			namedParameters.addValue("NO_OF_DAYS", Long.valueOf(2l), Types.BIGINT);
			namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_LEVAL IS *****" + query);
		List<List<TicketEscalationExtenstionApprovalLevelPojo>> ticketEscalationExtenstionApprovalLevelPojoLists = nmdPJdbcTemplate
				.query(query.toString(), namedParameters,
						new RowMapper<List<TicketEscalationExtenstionApprovalLevelPojo>>() {
							public List<TicketEscalationExtenstionApprovalLevelPojo> mapRow(ResultSet rs, int arg1)
									throws SQLException {
								LOGGER.info("***** The ResultSet object is ****" + rs);
								final ResultSetMapper<TicketEscalationExtenstionApprovalLevelPojo> resultSetMapper = new ResultSetMapper<TicketEscalationExtenstionApprovalLevelPojo>();
								List<TicketEscalationExtenstionApprovalLevelPojo> ticketEscalationExtenstionApprovalLevelPojoLIST = null;
								try {
									ticketEscalationExtenstionApprovalLevelPojoLIST = resultSetMapper
											.mapRersultSetToObject(rs,
													TicketEscalationExtenstionApprovalLevelPojo.class);
								} catch (InstantiationException | IllegalAccessException
										| InvocationTargetException ex) {
									LOGGER.error(
											"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
									String msg = "The Exception is raised while mapping resultset object to Pojo";
									throw new ResultSetMappingException(msg, ex.getCause());
								}
								LOGGER.info(
										"***** The ticketEscalationExtenstionApprovalLevelPojoLIST objects  is *****"
												+ ticketEscalationExtenstionApprovalLevelPojoLIST);
								return ticketEscalationExtenstionApprovalLevelPojoLIST;
							}
						});
		LOGGER.debug("*** The ticketEscalationExtenstionApprovalLevelPojoLists is *****"
				+ ticketEscalationExtenstionApprovalLevelPojoLists);
		if (ticketEscalationExtenstionApprovalLevelPojoLists.isEmpty()) {
			ticketEscalationExtenstionApprovalLevelPojoLists
					.add(new ArrayList<TicketEscalationExtenstionApprovalLevelPojo>());
		}
		LOGGER.debug("**** The ticketEscalationExtenstionApprovalLevelPojoLists is ****"
				+ ticketEscalationExtenstionApprovalLevelPojoLists.get(0));
		return ticketEscalationExtenstionApprovalLevelPojoLists.get(0);
	}

	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the getCustomerPropertyDetails in EmployeeTicketDaoImpl *****");

		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && !employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getFormsList")) {
			if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("saveBookingDetails")) {
				//querySB.append(" WHERE S.NAME = :SITE_NAME ");
				querySB.append(" WHERE S.SALES_FORCE_SITE_NAME = :SITE_NAME ");
				querySB.append(" AND F.FLAT_NO = :FLAT_NO ");
				querySB.append(" AND FB.STATUS_ID IN (:STATUS_ID) ");
				;
				// querySB.append(" AND FB.FLAT_BOOK_ID = :FLAT_BOOK_ID");
				namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.BIGINT);
				namedParameters.addValue("SITE_NAME", employeeTicketRequestInfo.getSiteName());
				namedParameters.addValue("FLAT_NO", employeeTicketRequestInfo.getFlatNo(), Types.VARCHAR);
				// namedParameters.addValue("FLAT_BOOK_ID",employeeTicketRequestInfo.getFlatBookingId(),
				// Types.BIGINT);
			} else {
				querySB.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID");
				namedParameters.addValue("FLAT_BOOK_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
			}
		} else {
			if (employeeTicketRequestInfo.getStatusId() != null) {
				querySB.append(" WHERE FB.STATUS_ID = :STATUS_ID ");
				namedParameters.addValue("STATUS_ID", employeeTicketRequestInfo.getStatusId(), Types.BIGINT);
			} else {
				querySB.append(" WHERE FB.STATUS_ID <> :STATUS_ID_A ");
				querySB.append(" AND FB.STATUS_ID <> :STATUS_ID_R ");
				namedParameters.addValue("STATUS_ID_A", Status.ACTIVE.getStatus(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID_R", Status.REJECTED.getStatus(), Types.BIGINT);
			}

			if (employeeTicketRequestInfo.getSiteName() != null
					&& !employeeTicketRequestInfo.getSiteName().equals("")) {
				querySB.append(" AND S.SITE_ID in " + employeeTicketRequestInfo.getSiteName());
			}
		}
		namedParameters.addValue("AR_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		String query = querySB.toString();

		LOGGER.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****" + query);
		List<List<CustomerPropertyDetailsPojo>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(query,
				namedParameters, new RowMapper<List<CustomerPropertyDetailsPojo>>() {
					public List<CustomerPropertyDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPropertyDetailsPojo> resultSetMapper = new ResultSetMapper<CustomerPropertyDetailsPojo>();
						List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoLIST = null;
						try {
							customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									CustomerPropertyDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The customerPropertyDetailsPojoLIST objects  is *****"
								+ customerPropertyDetailsPojoLIST);
						return customerPropertyDetailsPojoLIST;
					}
				});
		LOGGER.debug("*** The customerPropertyDetailsPojoLists is *****" + customerPropertyDetailsPojoLists);
		if (customerPropertyDetailsPojoLists.isEmpty()) {
			customerPropertyDetailsPojoLists.add(new ArrayList<CustomerPropertyDetailsPojo>());
		}
		LOGGER.debug("**** The customerPropertyDetailsPojoLists is ****" + customerPropertyDetailsPojoLists.get(0));
		return customerPropertyDetailsPojoLists.get(0);
	}

	@Override
	public List<DepartmentPojo> getDepartmentDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getDepartmentDetails in EmployeeTicketDaoImpl *****");
		String query = SqlQuery.QRY_TO_GET_DEPARTMENT_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (employeeTicketRequestInfo.getTypeOf() != null
				&& employeeTicketRequestInfo.getTypeOf().equals(Status.ALL.getStatus()) ? true : false) {
			query = new StringBuilder(query).append(" WHERE DEPT.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		} else {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_DEPARTMENT_DETAILS).append(" WHERE DEPT.DEPT_ID = :DEPT_ID ")
					.append(" AND DEPT.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("DEPT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_DEPARTMENT_DETAILS IS *****" + query);
		List<List<DepartmentPojo>> DepartmentPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<DepartmentPojo>>() {
					public List<DepartmentPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<DepartmentPojo> resultSetMapper = new ResultSetMapper<DepartmentPojo>();
						List<DepartmentPojo> DepartmentPojoLIST = null;
						try {
							DepartmentPojoLIST = resultSetMapper.mapRersultSetToObject(rs, DepartmentPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The DepartmentPojoLists objects  is *****" + DepartmentPojoLIST);
						return DepartmentPojoLIST;
					}
				});
		LOGGER.info("*** The DepartmentPojoLists is *****" + DepartmentPojoLists);
		if (DepartmentPojoLists.isEmpty()) {
			DepartmentPojoLists.add(new ArrayList<DepartmentPojo>());
		}
		LOGGER.info("**** The DepartmentPojoLists is ****" + DepartmentPojoLists.get(0));
		return DepartmentPojoLists.get(0);
	}

	@Override
	public List<TicketTypePojo> getTicketTypeInfo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getTicketTypeInfo in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKETTYPE_DETAILS)
				      .append(" WHERE  TT.TICKET_TYPE_ID IN (:TICKET_TYPE_ID) ")
	                  .append(" ORDER BY TMT.ID,TT.TICKET_TYPE_ID ")
				      .toString();
		LOGGER.info("**** THE QRY_TO_GET_TICKETTYPE_DETAILS IS *****" + query);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_TYPE_ID", Arrays.asList(employeeTicketRequestInfo.getTicketTypeId()), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKETTYPE_DETAILS IS *****" + query);
		List<List<TicketTypePojo>> ticketTypePojoLISTLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<TicketTypePojo>>() {
					public List<TicketTypePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketTypePojo> resultSetMapper = new ResultSetMapper<TicketTypePojo>();
						List<TicketTypePojo> ticketTypePojoLIST = null;
						try {
							ticketTypePojoLIST = resultSetMapper.mapRersultSetToObject(rs, TicketTypePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketTypePojoLIST objects  is *****" + ticketTypePojoLIST);
						return ticketTypePojoLIST;
					}
				});
		LOGGER.info("*** The ticketTypePojoLISTLists is *****" + ticketTypePojoLISTLists);
		if (ticketTypePojoLISTLists.isEmpty()) {
			ticketTypePojoLISTLists.add(new ArrayList<TicketTypePojo>());
		}
		LOGGER.info("**** The ticketTypePojoLISTLists is ****" + ticketTypePojoLISTLists.get(0));
		return ticketTypePojoLISTLists.get(0);
	}

	@Override
	public List<StatusPojo> getTicketStatusDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the getTicketStatusDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_STATUS_DETAILS)
				       .append(" WHERE ST.STATUS_ID = :STATUS_ID ")
				       .toString();
		LOGGER.info("**** THE QRY_TO_GET_STATUS_DETAILS IS *****" + query);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", employeeTicketRequestInfo.getStatusId(), Types.INTEGER);
		List<List<StatusPojo>> statusPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<StatusPojo>>() {
					public List<StatusPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<StatusPojo> resultSetMapper = new ResultSetMapper<StatusPojo>();
						List<StatusPojo> statusPojoLIST = null;
						try {
							statusPojoLIST = resultSetMapper.mapRersultSetToObject(rs, StatusPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The statusPojoLIST objects  is *****" + statusPojoLIST);
						return statusPojoLIST;
					}
				});

		LOGGER.info("*** The statusPojoLIST is *****" + statusPojoLists);
		if (statusPojoLists.isEmpty()) {
			statusPojoLists.add(new ArrayList<StatusPojo>());
		}
		LOGGER.info("**** The ServiceRequestPojoLIST is ****" + statusPojoLists.get(0));
		return statusPojoLists.get(0);
	}

	@Override
	public List<ChatInfoPojo> getTicketDtls(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the getTicketDtls in EmployeeTicketDaoImpl *****");
		String query = SqlQuery.QRY_TO_GET_INVIDUAL_TICKET_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SER_REQ_ID", employeeTicketRequestInfo.getTicketId(), Types.INTEGER);

		List<List<ChatInfoPojo>> chatInfoPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<ChatInfoPojo>>() {
					public List<ChatInfoPojo> mapRow(ResultSet rs, int arg1) throws SQLException {

						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ChatInfoPojo> resultSetMapper = new ResultSetMapper<ChatInfoPojo>();
						List<ChatInfoPojo> chatInfoPojoLIST = null;
						try {
							chatInfoPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ChatInfoPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ServiceRequestPojoLIST objects  is *****" + chatInfoPojoLIST);
						return chatInfoPojoLIST;
					}
				});

		LOGGER.info("*** The ServiceRequestPojoLIST is *****" + chatInfoPojoLists);
		if (chatInfoPojoLists.isEmpty()) {
			chatInfoPojoLists.add(new ArrayList<ChatInfoPojo>());
		}
		LOGGER.info("**** The ServiceRequestPojoLIST is ****" + chatInfoPojoLists.get(0));
		return chatInfoPojoLists.get(0);
	}

	@Override
	public List<CustomerPojo> getCustomerDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getCustomerDetails in EmployeeTicketDaoImpl *****");

		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS)
				.append(" WHERE CUST.CUST_ID = :CUST_ID AND CUST.STATUS_ID = :STATUS_ID ").toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);
		List<List<CustomerPojo>> customerPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<CustomerPojo>>() {
					public List<CustomerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {

						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPojo> resultSetMapper = new ResultSetMapper<CustomerPojo>();
						List<CustomerPojo> customerPojoLIST = null;
						try {
							customerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The customerPojoList objects  is *****" + customerPojoLIST);
						return customerPojoLIST;
					}
				});

		LOGGER.info("*** The customerPojoLists is *****" + customerPojoLists);
		if (customerPojoLists.isEmpty()) {
			customerPojoLists.add(new ArrayList<CustomerPojo>());
		}
		LOGGER.info("**** The customerPojoLists is ****" + customerPojoLists.get(0));
		return customerPojoLists.get(0);
	}

	@Override
	public List<CustBookInfoPojo> getCustBookInfo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getCustBookInfo in CustomerProfileDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QUERY_TO_GET_CUST_BOOK_INFO).append(" WHERE CUST_ID = :CUST_ID ")
				.append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID").append(" AND STATUS_ID = :STATUS_ID ").toString();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);

		LOGGER.info("**** THE QUERY_TO_GET_CUST_BOOK_INFO IS *****" + query);

		List<List<CustBookInfoPojo>> CustBookInfoPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<CustBookInfoPojo>>() {
					public List<CustBookInfoPojo> mapRow(ResultSet rs, int arg1) throws SQLException {

						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustBookInfoPojo> resultSetMapper = new ResultSetMapper<CustBookInfoPojo>();
						List<CustBookInfoPojo> CustBookInfoPojoLIST = null;
						try {
							CustBookInfoPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustBookInfoPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The CustBookInfoPojoLIST objects  is *****" + CustBookInfoPojoLIST);
						return CustBookInfoPojoLIST;
					}
				});

		LOGGER.info("*** The CustBookInfoPojoLists is *****" + CustBookInfoPojoLists);
		if (CustBookInfoPojoLists.isEmpty()) {
			CustBookInfoPojoLists.add(new ArrayList<CustBookInfoPojo>());
		}
		LOGGER.info("**** The CustBookInfoPojoLists is ****" + CustBookInfoPojoLists.get(0));
		return CustBookInfoPojoLists.get(0);
	}

	@Override
	public List<CoApplicantPojo> getCoApplicantDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getCoApplicantDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_COAPPLICANT_DETAILS)
				.append(" WHERE AP.FLAT_BOOK_ID =:FLAT_BOOK_ID AND AP.STATUS_ID = :STATUS_ID ").toString();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOK_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);

		List<List<CoApplicantPojo>> coApplicantPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<CoApplicantPojo>>() {
					public List<CoApplicantPojo> mapRow(ResultSet rs, int arg1) throws SQLException {

						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CoApplicantPojo> resultSetMapper = new ResultSetMapper<CoApplicantPojo>();
						List<CoApplicantPojo> coApplicantPojoLIST = null;
						try {
							coApplicantPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CoApplicantPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The coApplicantPojoLIST objects  is *****" + coApplicantPojoLIST);
						return coApplicantPojoLIST;
					}
				});

		LOGGER.info("*** The coApplicantPojoLists is *****" + coApplicantPojoLists);
		if (coApplicantPojoLists == null || coApplicantPojoLists.isEmpty()) {
			coApplicantPojoLists.add(new ArrayList<CoApplicantPojo>());
		}
		LOGGER.info("**** The coApplicantPojoLists is ****" + coApplicantPojoLists.get(0));
		return coApplicantPojoLists.get(0);
	}

	@Override
	public List<FlatBookingPojo> getFlatbookingDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getFlatbookingDetails in EmployeeTicketDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_BOOKING_DETAILS);

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && (employeeTicketRequestInfo
				.getRequestUrl().equalsIgnoreCase("closeTicket.spring")
				|| employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring"))) {
			query.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID AND FB.STATUS_ID = :STATUS_ID ");
			namedParameters.addValue("FLAT_BOOK_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);
		}

		else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && (employeeTicketRequestInfo
				.getRequestUrl().equalsIgnoreCase("actionBookingDetails"))){
			query.append(" WHERE   FLAT.FLAT_ID = :FLAT_ID AND FB.STATUS_ID = :STATUS_ID ");
			namedParameters.addValue("FLAT_ID", employeeTicketRequestInfo.getFlatId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);
		}

		else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && (employeeTicketRequestInfo
				.getRequestUrl().equalsIgnoreCase("insertOrUpdateCheckListDetails"))){
			query.append(" WHERE   FLAT.FLAT_ID = :FLAT_ID AND FB.STATUS_ID in (:STATUS_ID) ");
			namedParameters.addValue("FLAT_ID", employeeTicketRequestInfo.getFlatId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", Arrays.asList(Status.PENDING.getStatus(),Status.ACTIVE.getStatus()), Types.INTEGER);
		}

		else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && (employeeTicketRequestInfo
				.getRequestUrl().equalsIgnoreCase("customerAssociatedFlats"))){
			query.append(" WHERE FB.CUST_ID= :CUST_ID ");
			namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
		}else {
			query.append(" WHERE FB.CUST_ID = :CUST_ID AND FB.FLAT_ID = :FLAT_ID AND FB.STATUS_ID = :STATUS_ID ");
			namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
			namedParameters.addValue("FLAT_ID", employeeTicketRequestInfo.getFlatId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);
		}

		List<List<FlatBookingPojo>> flatBookingPojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new RowMapper<List<FlatBookingPojo>>() {
					public List<FlatBookingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {

						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatBookingPojo> resultSetMapper = new ResultSetMapper<FlatBookingPojo>();
						List<FlatBookingPojo> flatBookingPojoLIST = null;
						try {
							flatBookingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatBookingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The flatBookingPojoLIST objects  is *****" + flatBookingPojoLIST);
						return flatBookingPojoLIST;
					}
				});

		LOGGER.info("*** The flatBookingPojoLists is *****" + flatBookingPojoLists);
		if (flatBookingPojoLists.isEmpty()) {
			flatBookingPojoLists.add(new ArrayList<FlatBookingPojo>());
		}
		LOGGER.info("**** The flatBookingPojoLists is ****" + flatBookingPojoLists.get(0));
		return flatBookingPojoLists.get(0);
	}

	@Override
	public List<CustomerAddressPojo> getCustomerAddressDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {

		LOGGER.info("***** The control is inside the getFlatbookingDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_ADDRESS_DETAILS)
				.append(" WHERE CUST_ADDR.CUST_ADD_ID = :CUST_ADD_ID AND CUST_ADDR.STATUS_ID = :STATUS_ID ").toString();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CUST_ADD_ID", employeeTicketRequestInfo.getCustomerAddressId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);

		List<List<CustomerAddressPojo>> customerAddressPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<CustomerAddressPojo>>() {
					public List<CustomerAddressPojo> mapRow(ResultSet rs, int arg1) throws SQLException {

						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerAddressPojo> resultSetMapper = new ResultSetMapper<CustomerAddressPojo>();
						List<CustomerAddressPojo> customerAddressPojoLIST = null;
						try {
							customerAddressPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									CustomerAddressPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The customerAddressPojoLIST objects  is *****" + customerAddressPojoLIST);
						return customerAddressPojoLIST;
					}
				});

		LOGGER.info("*** The customerAddressPojoLists is *****" + customerAddressPojoLists);
		if (customerAddressPojoLists.isEmpty()) {
			customerAddressPojoLists.add(new ArrayList<CustomerAddressPojo>());
		}
		LOGGER.info("**** The customerAddressPojoLists is ****" + customerAddressPojoLists.get(0));
		return customerAddressPojoLists.get(0);
	}

	@Override
	public Long insertTicketConversation(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the updateTicketConversation in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
		namedParameters.addValue("FROM_ID", employeeTicketRequestInfo.getFromId(), Types.BIGINT);
		namedParameters.addValue("FROM_TYPE", employeeTicketRequestInfo.getFromType(), Types.BIGINT);
		namedParameters.addValue("FROM_DEPT_ID", employeeTicketRequestInfo.getFromDeptId(), Types.BIGINT);
		namedParameters.addValue("TO_ID",
				employeeTicketRequestInfo.getToId() != null ? employeeTicketRequestInfo.getToId() : 0l, Types.BIGINT);
		namedParameters.addValue("TO_TYPE",
				Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()) ? employeeTicketRequestInfo.getToType()
						: 0l,
				Types.BIGINT);
		namedParameters.addValue("TO_DEPT_ID",
				employeeTicketRequestInfo.getToDeptId() != null ? employeeTicketRequestInfo.getToDeptId() : 0l,
				Types.BIGINT);
		namedParameters.addValue("MESSAGE", employeeTicketRequestInfo.getMessage(), Types.VARCHAR);
		namedParameters.addValue("COMMENTS_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("VISIBLE_TYPE",
				employeeTicketRequestInfo.getVisibleType() != null ? employeeTicketRequestInfo.getVisibleType()
						: Status.PUBLIC,
				Types.VARCHAR);
		namedParameters.addValue("DOCUMENT_LOCATION", "NA", Types.VARCHAR);
		namedParameters.addValue("TICKET_CONVERSATION_DOC_ID",
				employeeTicketRequestInfo.getTicketConversationDocumentId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_CONVERSATION, namedParameters, keyHolder,
				new String[] { "TICKET_COMMENTS_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long insertTicketConversation(@NonNull TicketCommentsPojo ticketCommentsPojo) {
		LOGGER.info("***** The control is inside the updateTicketConversation in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_CONVERSATION_POJO,
				new BeanPropertySqlParameterSource(ticketCommentsPojo), keyHolder,
				new String[] { "TICKET_COMMENTS_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long insertTicketConversationDocuments(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the updateTicketConversationDocuments in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("DOCUMENTS_LOCATION",
				employeeTicketRequestInfo.getDocumentsLocation() != null
						? employeeTicketRequestInfo.getDocumentsLocation()
						: "NA",
				Types.VARCHAR);
		namedParameters.addValue("VISIBLE_TYPE",
				employeeTicketRequestInfo.getVisibleType() != null ? employeeTicketRequestInfo.getVisibleType()
						: Visibility.PRIVATE.getDescription(),
				Types.VARCHAR);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		namedParameters.addValue("TICKET_CONVERSATION_DOC_ID",
				employeeTicketRequestInfo.getTicketConversationDocumentId(), Types.BIGINT);
		namedParameters.addValue("EXTERNAL_DRIVE_FILE_LOCATION", employeeTicketRequestInfo.getExternalDriveFileLocation());

		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_CONVERSATION_DOCUMENTS, namedParameters, keyHolder,
				new String[] { "TICKET_CONVERSATION_DOC_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public List<TicketConversationDocumentsPojo> getTicketConversationDocumentsDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {

		LOGGER.info(
				"***** The control is inside the getTicketConversationDocumentsDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_CONVERSATION_DOCUMENT_DETAILS)
				.append(" WHERE TCD.TICKET_CONVERSATION_DOC_ID = :TICKET_CONVERSATION_DOC_ID ")
				.append(" AND TCD.STATUS_ID = :STATUS_ID ").toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_CONVERSATION_DOC_ID",
				employeeTicketRequestInfo.getTicketConversationDocumentId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKET_CONVERSATION_DOCUMENT_DETAILS IS *****" + query);

		List<List<TicketConversationDocumentsPojo>> ticketConversationDocumentsPojoLists = nmdPJdbcTemplate
				.query(query.toString(), namedParameters, new RowMapper<List<TicketConversationDocumentsPojo>>() {
					public List<TicketConversationDocumentsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketConversationDocumentsPojo> resultSetMapper = new ResultSetMapper<TicketConversationDocumentsPojo>();
						List<TicketConversationDocumentsPojo> ticketConversationDocumentsPojoLIST = null;
						try {
							ticketConversationDocumentsPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketConversationDocumentsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketConversationDocumentsPojoLIST objects  is *****"
								+ ticketConversationDocumentsPojoLIST);
						return ticketConversationDocumentsPojoLIST;
					}
				});
		LOGGER.info("*** The ticketConversationDocumentsPojoLists is *****" + ticketConversationDocumentsPojoLists);
		if (ticketConversationDocumentsPojoLists.isEmpty()) {
			ticketConversationDocumentsPojoLists.add(new ArrayList<TicketConversationDocumentsPojo>());
		}
		LOGGER.info(
				"**** The ticketConversationDocumentsPojoLists is ****" + ticketConversationDocumentsPojoLists.get(0));
		return ticketConversationDocumentsPojoLists.get(0);

	}

	@Override
	public List<TicketForwardMenuPojo> getTicketForwardMenuDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info(
				"***** The control is inside the getTicketConversationDocumentsDetails in EmployeeTicketDaoImpl *****");

		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_FORWARD_MENU_DETAILS)
				.append(" WHERE TFM.DEPARTMENT_ID = :DEPARTMENT_ID AND TFM.STATUS_ID = :STATUS_ID ").toString();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getFromDeptId(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKET_FORWARD_MENU_DETAILS IS *****" + query);

		List<List<TicketForwardMenuPojo>> ticketForwardMenuPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<TicketForwardMenuPojo>>() {
					public List<TicketForwardMenuPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketForwardMenuPojo> resultSetMapper = new ResultSetMapper<TicketForwardMenuPojo>();
						List<TicketForwardMenuPojo> ticketForwardMenuPojoLIST = null;
						try {
							ticketForwardMenuPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketForwardMenuPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info(
								"***** The ticketForwardMenuPojoLIST objects  is *****" + ticketForwardMenuPojoLIST);
						return ticketForwardMenuPojoLIST;
					}
				});
		LOGGER.info("*** The ticketForwardMenuPojoLists is *****" + ticketForwardMenuPojoLists);
		if (ticketForwardMenuPojoLists.isEmpty()) {
			ticketForwardMenuPojoLists.add(new ArrayList<TicketForwardMenuPojo>());
		}
		LOGGER.info("**** The ticketForwardMenuPojoLists is ****" + ticketForwardMenuPojoLists.get(0));
		return ticketForwardMenuPojoLists.get(0);
	}

	@Override
	public Integer updateTicketDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the updateTicketDetails in EmployeeTicketDaoImpl *****"
				+ employeeTicketRequestInfo.getTicketId());

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_TICKET_DETAILS;

		if (employeeTicketRequestInfo.getTypeOf().equals(Department.EMPLOYEE.getId())) {
			namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getToId(), Types.BIGINT);
			namedParameters.addValue("DEPARTMENT_ID", 0, Types.BIGINT);
		}

		if (employeeTicketRequestInfo.getTypeOf().equals(Department.DEPARTMENT.getId())) {
			namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getToDeptId(), Types.BIGINT);
			namedParameters.addValue("ASSIGNMENT_TO", 0, Types.BIGINT);
		}

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getType())
				&& employeeTicketRequestInfo.getType().equalsIgnoreCase(Status.REPLIED.getDescription())) {
			namedParameters.addValue("INVIDUAL_TICKET_STATUS", Status.REPLIED.getStatus(), Types.BIGINT);
		/* if ticket is coming back from pm dont update invidual status.  */	
		} else if((Util.isEmptyObject(employeeTicketRequestInfo.getType()) || (Util.isNotEmptyObject(employeeTicketRequestInfo.getType()) && !employeeTicketRequestInfo.getType().equalsIgnoreCase(Department.PM.getName())))){
			namedParameters.addValue("INVIDUAL_TICKET_STATUS", Status.NEW.getStatus(), Types.BIGINT);
		}else {
			namedParameters.addValue("INVIDUAL_TICKET_STATUS", Status.NEW.getStatus(), Types.BIGINT);
		}

		namedParameters.addValue("TICKET_STATUS", Status.INPROGRESS.getStatus(), Types.BIGINT);
		namedParameters.addValue("ASSIGNED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("ASSIGNED_BY", employeeTicketRequestInfo.getFromId(), Types.BIGINT);
		namedParameters.addValue("STATUS_UPDATED_BY", employeeTicketRequestInfo.getFromId(), Types.BIGINT);
		namedParameters.addValue("STATUS_UPDATE_TYPE", employeeTicketRequestInfo.getFromType(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);

		int result = nmdPJdbcTemplate.update(query, namedParameters);
		LOGGER.info("**** The noumber of Tickets updated  *****" + result);
		return result;
	}

	@Override
	public Long insertTicketSeekInfoDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the insertTicketSeekInfoDetails in EmployeeTicketDaoImpl *****"
				+ employeeTicketRequestInfo.getTicketId());

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		namedParameters.addValue("FROM_DEPT_ID", employeeTicketRequestInfo.getFromDeptId(), Types.BIGINT);
		namedParameters.addValue("FROM_ID", employeeTicketRequestInfo.getFromId(), Types.BIGINT);
		namedParameters.addValue("FROM_TYPE", employeeTicketRequestInfo.getFromType(), Types.BIGINT);
		namedParameters.addValue("TO_ID",
				employeeTicketRequestInfo.getToId() != null ? employeeTicketRequestInfo.getToId() : 0l, Types.BIGINT);
		namedParameters.addValue("TO_TYPE", employeeTicketRequestInfo.getToType(), Types.BIGINT);
		namedParameters.addValue("MESSAGE", employeeTicketRequestInfo.getMessage(), Types.VARCHAR);
		namedParameters.addValue("TO_DEPT_ID",
				employeeTicketRequestInfo.getToDeptId() != null ? employeeTicketRequestInfo.getToDeptId() : 0l,
				Types.BIGINT);
		namedParameters.addValue("VISIBLE_TYPE", employeeTicketRequestInfo.getVisibleType(), Types.VARCHAR);
		namedParameters.addValue("DOCUMENT_LOCATION", "NA", Types.VARCHAR);
		namedParameters.addValue("CHAT_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		namedParameters.addValue("TICKET_SEEKINFO_REQUEST_ID", employeeTicketRequestInfo.getTicketSeekInforequestId());
		namedParameters.addValue("TICKET_CONVERSATION_DOC_ID",
				employeeTicketRequestInfo.getTicketConversationDocumentId(), Types.BIGINT);
		namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);

		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_SEEK_INFO_DETAILS, namedParameters, keyHolder,
				new String[] { "TICKET_SEEK_INFO_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public List<TicketSeekInfoPojo> getTicketSeekInfoDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the getTicketSeekInfoDetails in EmployeeTicketDaoImpl *****"
				+ employeeTicketRequestInfo.getTicketId());
		String query = SqlQuery.QRY_TO_SELECT_TICKET_SEEK_INFO_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (status.getStatus().equals(Status.ALL.getStatus())) {
			if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("viewRequestInfo.spring")) {
				query = new StringBuilder(query)
						//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
						.append(
						" WHERE (TICKET_SEEK_INFO.FROM_ID = :FROM_ID AND TICKET_SEEK_INFO.FROM_DEPT_ID = :FROM_DEPT_ID AND TICKET_SEEK_INFO.FROM_TYPE = :FROM_TYPE)")
						.append("  OR ")
						.append(" (TICKET_SEEK_INFO.TO_DEPT_ID = :FROM_DEPT_ID) AND	TICKET_SEEK_INFO.STATUS_ID = :STATUS_ID ")
						.append("  ORDER BY TICKET_SEEK_INFO_ID ASC ").toString();
//				namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				namedParameters.addValue("FROM_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())? employeeTicketRequestInfo.getFromId(): 0l,Types.BIGINT);
				namedParameters.addValue("FROM_TYPE",Util.isNotEmptyObject(employeeTicketRequestInfo.getFromType())? employeeTicketRequestInfo.getFromType(): 0l,Types.BIGINT);
				namedParameters.addValue("FROM_DEPT_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())? employeeTicketRequestInfo.getFromDeptId(): 0l,Types.BIGINT);
			} else {
				query = new StringBuilder(query)
						.append(" WHERE TICKET_SEEK_INFO.STATUS_ID = :STATUS_ID ORDER BY TICKET_SEEK_INFO_ID ASC  ")
						.toString();
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			}
		} else {
			query = new StringBuilder(query).append(
					" WHERE TICKET_SEEK_INFO.TICKET_ID = :TICKET_ID AND TICKET_SEEK_INFO.STATUS_ID = :STATUS_ID  ORDER BY TICKET_SEEK_INFO_ID ASC  ")
					.toString();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		}
		LOGGER.debug("**** THE QRY_TO_SELECT_TICKET_SEEK_INFO_DETAILS IS *****" + query);

		List<List<TicketSeekInfoPojo>> ticketSeekInfoPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<TicketSeekInfoPojo>>() {
					public List<TicketSeekInfoPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketSeekInfoPojo> resultSetMapper = new ResultSetMapper<TicketSeekInfoPojo>();
						List<TicketSeekInfoPojo> ticketSeekInfoPojoLIST = null;
						try {
							ticketSeekInfoPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketSeekInfoPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketSeekInfoPojoLIST objects  is *****" + ticketSeekInfoPojoLIST);
						return ticketSeekInfoPojoLIST;
					}
				});
		LOGGER.info("*** The ticketSeekInfoPojoLists is *****" + ticketSeekInfoPojoLists);
		if (ticketSeekInfoPojoLists.isEmpty()) {
			ticketSeekInfoPojoLists.add(new ArrayList<TicketSeekInfoPojo>());
		}
		LOGGER.info("**** The ticketSeekInfoPojoLists is ****" + ticketSeekInfoPojoLists.get(0));
		return ticketSeekInfoPojoLists.get(0);
	}

	@Override
	public Long insertTicketSeekInfoRequest(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the insertTicketSeekInfoRequest in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("TICKET_ID",
				employeeTicketRequestInfo.getTicketId() != null ? employeeTicketRequestInfo.getTicketId() : 0l,
				Types.BIGINT);
		namedParameters.addValue("FROM_ID",
				employeeTicketRequestInfo.getFromId() != null ? employeeTicketRequestInfo.getFromId() : 0l,
				Types.BIGINT);
		namedParameters.addValue("FROM_TYPE",
				employeeTicketRequestInfo.getFromType() != null ? employeeTicketRequestInfo.getFromType() : 0l,
				Types.BIGINT);
		namedParameters.addValue("FROM_DEPT_ID",
				employeeTicketRequestInfo.getFromDeptId() != null ? employeeTicketRequestInfo.getFromDeptId() : 0l,
				Types.BIGINT);
		namedParameters.addValue("TO_DEPT_ID",
				employeeTicketRequestInfo.getToDeptId() != null ? employeeTicketRequestInfo.getToDeptId() : 0l,
				Types.BIGINT);
		namedParameters.addValue("TO_ID",
				employeeTicketRequestInfo.getToId() != null ? employeeTicketRequestInfo.getToId() : 0l, Types.BIGINT);
		namedParameters.addValue("TO_TYPE",
				employeeTicketRequestInfo.getToType() != null ? employeeTicketRequestInfo.getToType() : 0l,
				Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		namedParameters.addValue("CREATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);

		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_SEEKINFO_REQUEST, namedParameters, keyHolder,
				new String[] { "TICKET_SEEKINFO_REQUEST_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public List<TicketSeekInfoRequestPojo> getTicketSeekInfoRequestDetails(
			EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {

		LOGGER.info("***** The control is inside the getTicketSeekInfoRequestDetails in EmployeeTicketDaoImpl *****");
		String query = SqlQuery.QRY_TO_GET_TICKET_SEEKINFO_REQUEST_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (status.getStatus().equals(Status.ALL.getStatus())) {
			query = new StringBuilder(query).append(" WHERE TSR.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		} else {
			query = new StringBuilder(query)
					.append(" WHERE TSR.TICKET_SEEKINFO_REQUEST_ID = :TICKET_SEEKINFO_REQUEST_ID ")
					.append(" AND TSR.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("TICKET_SEEKINFO_REQUEST_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_TICKET_SEEKINFO_REQUEST_DETAILS IS *****" + query);
		List<List<TicketSeekInfoRequestPojo>> ticketSeekInfoRequestPojoLists = nmdPJdbcTemplate.query(query,
				namedParameters, new RowMapper<List<TicketSeekInfoRequestPojo>>() {
					public List<TicketSeekInfoRequestPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketSeekInfoRequestPojo> resultSetMapper = new ResultSetMapper<TicketSeekInfoRequestPojo>();
						List<TicketSeekInfoRequestPojo> ticketSeekInfoRequestPojoLIST = null;
						try {
							ticketSeekInfoRequestPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketSeekInfoRequestPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketSeekInfoRequestPojoLIST objects  is *****"
								+ ticketSeekInfoRequestPojoLIST);
						return ticketSeekInfoRequestPojoLIST;
					}
				});
		LOGGER.info("*** The ticketSeekInfoRequestPojoLists is *****" + ticketSeekInfoRequestPojoLists);
		if (ticketSeekInfoRequestPojoLists.isEmpty()) {
			ticketSeekInfoRequestPojoLists.add(new ArrayList<TicketSeekInfoRequestPojo>());
		}
		LOGGER.info("**** The ticketSeekInfoRequestPojoLists is ****" + ticketSeekInfoRequestPojoLists.get(0));
		return ticketSeekInfoRequestPojoLists.get(0);
	}

	@Override
	public Long insertTicketStatistics(@NonNull TicketStatisticsPojo ticketStatisticsPojo) {
		LOGGER.info("***** The control is inside the insertTicketStatistics in EmployeeTicketDaoImpl *****"
				+ ticketStatisticsPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		LOGGER.info("***** The control is inside the insertTicketStatistics in EmployeeTicketDaoImpl *****");
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_STATISTICS_DETAILS,
				new BeanPropertySqlParameterSource(ticketStatisticsPojo), keyHolder,
				new String[] { "TICKET_STATISTICS_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long updateTicketStatus(@NonNull TicketPojo ticketPojo) {
		LOGGER.info("***** The control is inside the updateTicketStatus in EmployeeTicketDaoImpl *****" + ticketPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_STATUS,
				new BeanPropertySqlParameterSource(ticketPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}
	
	@Override
	public Long updateTicketCloseStatus(@NonNull TicketPojo ticketPojo) {
		LOGGER.info("***** The control is inside the updateTicketStatus in EmployeeTicketDaoImpl *****" + ticketPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_CLOSE_STATUS,
				new BeanPropertySqlParameterSource(ticketPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}
	
	@Override
	public Long updateTicketReopenStatus(@NonNull TicketPojo ticketPojo) {
		LOGGER.info("***** The control is inside the updateTicketStatus in EmployeeTicketDaoImpl *****" + ticketPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_REOPEN_STATUS,
				new BeanPropertySqlParameterSource(ticketPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}

	@Override
	public Long makeAsPublic(@NonNull TicketConversationDocumentsPojo ticketConversationDocumentsPojo) {
		LOGGER.info("***** The control is inside the makeAsPublic in EmployeeTicketDaoImpl *****"
				+ ticketConversationDocumentsPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_CONVERSATION_DOCUMENTS_STATUS,
				new BeanPropertySqlParameterSource(ticketConversationDocumentsPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}

	@Override
	public List<AppRegistrationPojo> getAppregistrationDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getAppregistrationDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_APP_REGISTRATION)
				.append(" WHERE APR.CUST_ID = :CUST_ID AND  APR.STATUS_ID = :STATUS_ID ").toString();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_APP_REGISTRATION IS *****" + query);

		List<List<AppRegistrationPojo>> appRegistrationPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<AppRegistrationPojo>>() {
					public List<AppRegistrationPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AppRegistrationPojo> resultSetMapper = new ResultSetMapper<AppRegistrationPojo>();
						List<AppRegistrationPojo> appRegistrationPojoLIST = null;
						try {
							appRegistrationPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									AppRegistrationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The appRegistrationPojoLIST objects  is *****" + appRegistrationPojoLIST);
						return appRegistrationPojoLIST;
					}
				});
		LOGGER.debug("*** The appRegistrationPojoLists is *****" + appRegistrationPojoLists);
		if (appRegistrationPojoLists.isEmpty()) {
			appRegistrationPojoLists.add(new ArrayList<AppRegistrationPojo>());
		}
		LOGGER.debug("**** The appRegistrationPojoLists is ****" + appRegistrationPojoLists.get(0));
		return appRegistrationPojoLists.get(0);
	}

	@Override
	public String getTicketConversationDocumentsDetailsSeqId(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(
				"***** The control is inside the getTicketConversationDocumentsDetailsSeqId in EmployeeTicketDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String id = nmdPJdbcTemplate.queryForObject(SqlQuery.TICKET_CONVRSATION_DOCUMENTS_SEQ_ID, namedParameters,
				String.class);
		LOGGER.debug("****** The  sequence Id generated is **********" + id);
		return id.toString();
	}

	@Override
	public Long insertExtendEsacalationTime(
			@NonNull TicketExtendedEscalationApprovalPojo ticketExtendedEscalationApprovalPojo) {
		LOGGER.info("***** The Control is inside the extendEscalationTime in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_TICKET_ESCA_EXT_APPROVAL,
				new BeanPropertySqlParameterSource(ticketExtendedEscalationApprovalPojo), keyHolder,
				new String[] { "ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public List<TicketExtendedEscalationApprovalPojo> getExtendEsacalationTimeDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The Control is inside the getExtendEsacalationTimeDetails in EmployeeTicketDaoImpl *****");

		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (employeeTicketRequestInfo.getRequestUrl() != null
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("approval")) {
			query = new StringBuilder(query).append(" WHERE TEEA.ID = :ID AND TEEA.STATUS = :STATUS ")
					// .append(" WHERE TEEA.ID = 16 AND TEEA.STATUS = 6 ")
					.toString();
			LOGGER.debug("****************" + employeeTicketRequestInfo.getTicketExtendedEscalationApprovalId());
			namedParameters.addValue("ID", employeeTicketRequestInfo.getTicketExtendedEscalationApprovalId(),
					Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
			LOGGER.info("**** THE QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL IS *****" + query);
		
		/* If request is coming from  getExtendedEscalationTimeApprovalLevel for pending status */
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getExtendedEscalationTimeApprovalLevel")) {
			if(status.getStatus().equals(Status.NOTAPPROVED.getStatus())){
			query = new StringBuilder(query).append(SqlQuery.QRY_TO_CHECK_TICKET_ESCA_EXT_APPROVAL_STATUS)
					.toString();
			namedParameters.addValue("TICKET_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l,Types.BIGINT);
			namedParameters.addValue("APPROVED_STATUS",Arrays.asList(status.getStatus()),Types.BIGINT);
			LOGGER.info("**** THE QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL IS *****" + query);
			/* If request is coming from  getExtendedEscalationTimeApprovalLevel for Approved status */
			}else {
				query = SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_JOIN_TICKET_ESCA_EXT_APR_LVL_MAP
						.toString();	
				namedParameters.addValue("TICKET_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l,Types.BIGINT);
				namedParameters.addValue("APPROVED_STATUS",Arrays.asList(status.getStatus()),Types.BIGINT);
				LOGGER.info("**** THE QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL IS *****" + query);
			}
		}
		else {
			//query = new StringBuilder(query).append(" INNER JOIN TICKET T ON T.TICKET_ID = TEEA.TICKET_ID AND (T.TICKET_STATUS <> '11' AND T.STATUS <> '7' AND (T.ESTIMATED_RESOLVED_DATE_STATUS IS  NULL OR T.ESTIMATED_RESOLVED_DATE_STATUS <> '17')) WHERE TEEA.STATUS = :STATUS AND TEEA.REQUESTED_TO = :REQUESTED_TO ").toString();
			query =  SqlQuery.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_BY_EMPLOYEE_ID
					.toString();
			namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("APPROVED_STATUS", status.getStatus(), Types.BIGINT);
			namedParameters.addValue("EMPLOYEE_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeId())?employeeTicketRequestInfo.getEmployeeId():0l, Types.BIGINT);
			
			LOGGER.info("**** THE QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_BY_EMPLOYEE_ID IS *****" + query);
		}
		List<List<TicketExtendedEscalationApprovalPojo>> ticketExtendedEscalationApprovalPojoLists = nmdPJdbcTemplate
				.query(query, namedParameters, new RowMapper<List<TicketExtendedEscalationApprovalPojo>>() {
					public List<TicketExtendedEscalationApprovalPojo> mapRow(ResultSet rs, int arg1)
							throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketExtendedEscalationApprovalPojo> resultSetMapper = new ResultSetMapper<TicketExtendedEscalationApprovalPojo>();
						List<TicketExtendedEscalationApprovalPojo> ticketExtendedEscalationApprovalPojoLIST = null;
						try {
							ticketExtendedEscalationApprovalPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									TicketExtendedEscalationApprovalPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketExtendedEscalationApprovalPojoLIST objects  is *****"
								+ ticketExtendedEscalationApprovalPojoLIST);
						return ticketExtendedEscalationApprovalPojoLIST;
					}
				});
		LOGGER.debug("*** The ticketExtendedEscalationApprovalPojoLists is *****"
				+ ticketExtendedEscalationApprovalPojoLists);
		if (ticketExtendedEscalationApprovalPojoLists.isEmpty()) {
			ticketExtendedEscalationApprovalPojoLists.add(new ArrayList<TicketExtendedEscalationApprovalPojo>());
		}
		LOGGER.debug("**** The ticketExtendedEscalationApprovalPojoLists is ****"
				+ ticketExtendedEscalationApprovalPojoLists.get(0));
		return ticketExtendedEscalationApprovalPojoLists.get(0);
	}

	@Override
	public Long updateTicketEscalationExtenstionApprovalStatus(
			@NonNull TicketExtendedEscalationApprovalPojo ticketExtendedEscalationApprovalPojo) {
		LOGGER.info(
				"***** The control is inside the updateTicketEscalationExtenstionApprovalStatus in EmployeeTicketDaoImpl *****"
						+ ticketExtendedEscalationApprovalPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_ESCA_EXT_APPROVAL,
				new BeanPropertySqlParameterSource(ticketExtendedEscalationApprovalPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}
	
	@Override
	public Long updateTicketEscalationExtenstionApprovalStatus(@NonNull Long ticketId ) {
		LOGGER.info("***** The control is inside the updateTicketEscalationExtenstionApprovalStatus in EmployeeTicketDaoImpl *****"+ticketId);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_ID", ticketId, Types.BIGINT);
		namedParameters.addValue("P_STATUS", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("A_STATUS", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_ESCA_EXT_APPROVAL_STATUS,namedParameters);
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();	
	}
	
	@Override
	public Long updateTicketStatusInactive(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo){
		LOGGER.info("******* The control inside of the updateTicketStatusInactive  in  EmployeeTicketDaoImpl ********");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("IN_STATUS", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())?employeeTicketRequestInfo.getFlatBookingId():0l,Types.BIGINT);
		namedParameters.addValue("STATUS", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_STATUS_INACTIVE,namedParameters);
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();	
	}

	@Override
	public Long updateTicketEstimatedResolvedDate(@NonNull TicketPojo ticketPojo) {
		LOGGER.info("***** The control is inside the updateTicketEstimatedResolvedDate in EmployeeTicketDaoImpl *****"+ ticketPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_ESTIMATED_RESOLVED_DATE,
				new BeanPropertySqlParameterSource(ticketPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}

	@Override
	public Long updateTicketEstimatedResolvedDateStatus(TicketPojo ticketPojo) {
		LOGGER.info(
				"***** The control is inside the updateTicketEstimatedResolvedDateStatus in EmployeeTicketDaoImpl *****"
						+ ticketPojo);
		Integer result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TICKET_ESTIMATED_RESOLVED_DATE_STATUS,
				new BeanPropertySqlParameterSource(ticketPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}

	@Override
	public Long insertTicketEscalationDetails(@NonNull TicketEscalationPojo ticketEscalationPojo) {
		LOGGER.info("***** The control is inside the insertTicketEscalationDetails in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSER_TICKET_ESCALATION,
				new BeanPropertySqlParameterSource(ticketEscalationPojo), keyHolder,
				new String[] { "TICKET_ESCALATION_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long insertEmployeeLeaveDetails(@NonNull EmployeeLeaveDetailsPojo employeeLeaveDetailsPojo) {
		LOGGER.info("***** The control is inside the insertEmployeeLeaveDetails in EmployeeTicketDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_EMPLOYEE_LEAVE_DETAILS,
				new BeanPropertySqlParameterSource(employeeLeaveDetailsPojo), keyHolder,
				new String[] { "EMPLOYEE_LEAVE_DETAILS_ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public List<EmployeeLeaveDetailsPojo> getEmployeeLeaveDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status) {
		LOGGER.info("***** The control is inside the getAppregistrationDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_LEAVE_DETAILS).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (employeeTicketRequestInfo.getRequestUrl() != null
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("isEmployeeAvailable")) {
			query = new StringBuilder(query)
					// .append(" WHERE (SYSTIMESTAMP >=(START_DATE) AND SYSTIMESTAMP<=(END_DATE))
					// AND ELD.STATUS = :STATUS AND ELD.EMPLOYEE_ID = :EMPLOYEE_ID ")
					.append(" WHERE trunc(sysdate) >=  trunc(START_DATE) AND trunc(sysdate) <= trunc(END_DATE) AND ELD.STATUS = :STATUS AND ELD.EMPLOYEE_ID = :EMPLOYEE_ID ")
					.toString();
			namedParameters.addValue("EMPLOYEE_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		} else {
			query = new StringBuilder(query).append(" WHERE ELD.EMPLOYEE_ID = :EMPLOYEE_ID AND  ELD.STATUS = :STATUS ")
					.toString();
			namedParameters.addValue("EMPLOYEE_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_LEAVE_DETAILS IS *****" + query);

		List<List<EmployeeLeaveDetailsPojo>> employeeLeaveDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<EmployeeLeaveDetailsPojo>>() {
					public List<EmployeeLeaveDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeLeaveDetailsPojo> resultSetMapper = new ResultSetMapper<EmployeeLeaveDetailsPojo>();
						List<EmployeeLeaveDetailsPojo> employeeLeaveDetailsPojoLIST = null;
						try {
							employeeLeaveDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs,
									EmployeeLeaveDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The employeeLeaveDetailsPojoLIST objects  is *****"
								+ employeeLeaveDetailsPojoLIST);
						return employeeLeaveDetailsPojoLIST;
					}
				});
		LOGGER.debug("*** The employeeLeaveDetailsPojoLists is *****" + employeeLeaveDetailsPojoLists);
		if (employeeLeaveDetailsPojoLists.isEmpty()) {
			employeeLeaveDetailsPojoLists.add(new ArrayList<EmployeeLeaveDetailsPojo>());
		}
		LOGGER.debug("**** The employeeLeaveDetailsPojoLists is ****" + employeeLeaveDetailsPojoLists.get(0));
		return employeeLeaveDetailsPojoLists.get(0);
	}

	@Override
	public List<EmployeePojo> getEmployee(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("***** The control is inside the getAppregistrationDetails in EmployeeTicketDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (employeeTicketRequestInfo.getRequestUrl() != null && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getEmployeeDetailsJava")) {
			query = new StringBuilder(query).append(" WHERE EMP.STATUS_ID = :STATUS_ID  AND ")
					.append(" CONCAT(CONCAT(EMP .FIRST_NAME,' '),EMP.LAST_NAME) LIKE '%"
							+ (Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeName())
									? employeeTicketRequestInfo.getEmployeeName()
									: "")
							+ "%' ")
					.toString();
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}else if(employeeTicketRequestInfo.getRequestUrl() != null && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getEmployeeDetails")){
			query = new StringBuilder(query).append(" WHERE EMP.STATUS_ID = :STATUS_ID ").toString();
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) &&  employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getEscaltedEmployee")) {
			query = SqlQuery.QRY_TO_GET_ESCALATED_LEVEL_EMPLOYEE_DETAILS_BY_TICKET_ID;
			namedParameters.addValue("TICKET_ID", Arrays.asList(employeeTicketRequestInfo.getTicketId()), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}else if("getCustomerTicketDetails".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())||"forwardTicketDetails.spring".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
			query = SqlQuery.QRY_TO_GET_EMPLOYEE_NAME_WITH_DEPARTMENT_NAME;
			namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus());
		}else if("getTicketOwnerName".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
			query = SqlQuery.QRY_TO_GET_TICKET_OWNER_NAME_WITH_DEPARTMENT_NAME;
			namedParameters.addValue("TICKET_TYPE_DETAILS_ID", employeeTicketRequestInfo.getTicketTypeDetailsId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus());
		}else {
			query = new StringBuilder(query).append(" WHERE EMP.EMP_ID = :EMP_ID AND  EMP.STATUS_ID = :STATUS_ID ")
					.toString();
			namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}
		LOGGER.info("**** THE QRY_TO_GET_EMPLOYEE_DETAILS IS *****" + query);
		List<List<EmployeePojo>> employeePojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new RowMapper<List<EmployeePojo>>() {
					public List<EmployeePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeePojo> resultSetMapper = new ResultSetMapper<EmployeePojo>();
						List<EmployeePojo> employeePojoLIST = null;
						try {
							employeePojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The employeePojoLIST objects  is *****" + employeePojoLIST);
						return employeePojoLIST;
					}
				});
		LOGGER.debug("*** The employeePojoLists is *****" + employeePojoLists);
		if (employeePojoLists.isEmpty()) {
			employeePojoLists.add(new ArrayList<EmployeePojo>());
		}
		LOGGER.debug("**** The employeePojoLists is ****" + employeePojoLists.get(0));
		return employeePojoLists.get(0);
	}
	
	@Override
	public List<TicketEscalationExtenstionApprovalLevelMappingPojo> getTicketEscaltionExtentionAprovalLevelDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,@NonNull Status status) {
	LOGGER.info("***** The control is inside the getTicketEscaltionExtentionAprovalLevelDetails in EmployeeTicketDaoImpl *****");
	String query = null;
	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	 if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("insertExtendEsacalationTime")) {
		  query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_EXT_APR_LVL_BY_ID).toString();	
		  namedParameters.addValue("ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationExtenstionApprovalLevelMappingId())?employeeTicketRequestInfo.getTicketEscalationExtenstionApprovalLevelMappingId():0l, Types.BIGINT);
		  namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		  LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_EXT_APR_LVL_BY_ID IS *****" + query);
	 }
	 else {
	  query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_EXT_APR_LVL).toString();	
	  namedParameters.addValue("TICKET_ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l, Types.BIGINT);
	  namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
	  LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_EXT_APR_LVL IS *****" + query);
	 } 
	  List<List<TicketEscalationExtenstionApprovalLevelMappingPojo>> TicketEscalationExtenstionApprovalLevelMappingPojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new RowMapper<List<TicketEscalationExtenstionApprovalLevelMappingPojo>>() {
					public List<TicketEscalationExtenstionApprovalLevelMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketEscalationExtenstionApprovalLevelMappingPojo> resultSetMapper = new ResultSetMapper<TicketEscalationExtenstionApprovalLevelMappingPojo>();
						List<TicketEscalationExtenstionApprovalLevelMappingPojo> TicketEscalationExtenstionApprovalLevelMappingPojoLIST = null;
						try {
							TicketEscalationExtenstionApprovalLevelMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, TicketEscalationExtenstionApprovalLevelMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The TicketEscalationExtenstionApprovalLevelMappingPojoLIST objects  is *****" + TicketEscalationExtenstionApprovalLevelMappingPojoLIST);
						return TicketEscalationExtenstionApprovalLevelMappingPojoLIST;
					}
				});
	  LOGGER.debug("*** The TicketEscalationExtenstionApprovalLevelMappingPojoLIST is *****" + TicketEscalationExtenstionApprovalLevelMappingPojoLists);
	  
	  if (TicketEscalationExtenstionApprovalLevelMappingPojoLists.isEmpty()) {
		  TicketEscalationExtenstionApprovalLevelMappingPojoLists.add(new ArrayList<TicketEscalationExtenstionApprovalLevelMappingPojo>());
		}
		LOGGER.debug("**** The employeePojoLists is ****" + TicketEscalationExtenstionApprovalLevelMappingPojoLists.get(0));
		return TicketEscalationExtenstionApprovalLevelMappingPojoLists.get(0);
	}
	
	
	@Override
	public List<TicketEscalationLevelMappingPojo> getTicketEscaltionAprovalLevelDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,@NonNull Status status) {
	LOGGER.info("***** The control is inside the getTicketEscaltionAprovalLevelDetails in EmployeeTicketDaoImpl *****");
	MapSqlParameterSource namedParameters = new MapSqlParameterSource(); 
	 String query = null;
	if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getEscalationLevelById")) {
		query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_APR_LVL_BY_ID).toString();
		namedParameters.addValue("ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationLevelMappingId())?employeeTicketRequestInfo.getTicketEscalationLevelMappingId():0l, Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_APR_LVL_BY_ID IS *****" + query);
	}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getEscalationLevelByTicketEscalationId")) {
		query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_APR_LVL_BY_TICKET_ESCALATION_ID).toString();
		namedParameters.addValue("TICKET_ESCALATION_ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationId())?employeeTicketRequestInfo.getTicketEscalationId():0l, Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_APR_LVL_BY_TICKET_ESCALATION_ID IS *****" + query);
	}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("forwardTicketDetails.spring")) {
		query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_LVL_EXTEND_TIME).toString();
		namedParameters.addValue("TICKET_ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l, Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_LVL_EXTEND_TIME IS *****" + query);	
	}
	else {
	  query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_APR_LVL).toString();	
	  namedParameters.addValue("TICKET_ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l, Types.BIGINT);
	  namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
	  LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_APR_LVL IS *****" + query);
	}
	  List<List<TicketEscalationLevelMappingPojo>> TicketEscalationLevelMappingPojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new RowMapper<List<TicketEscalationLevelMappingPojo>>() {
					public List<TicketEscalationLevelMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketEscalationLevelMappingPojo> resultSetMapper = new ResultSetMapper<TicketEscalationLevelMappingPojo>();
						List<TicketEscalationLevelMappingPojo> TicketEscalationLevelMappingPojoLIST = null;
						try {
							TicketEscalationLevelMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, TicketEscalationLevelMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The TicketEscalationLevelMappingPojoLists objects  is *****" + TicketEscalationLevelMappingPojoLIST);
						return TicketEscalationLevelMappingPojoLIST;
					}
				});
	  LOGGER.debug("*** The TicketEscalationLevelMappingPojoLists is *****" + TicketEscalationLevelMappingPojoLists);
	  if (TicketEscalationLevelMappingPojoLists.isEmpty()) {
		  TicketEscalationLevelMappingPojoLists.add(new ArrayList<TicketEscalationLevelMappingPojo>());
		}
		LOGGER.debug("**** The employeePojoLists is ****" + TicketEscalationLevelMappingPojoLists.get(0));
		return TicketEscalationLevelMappingPojoLists.get(0);
	}
	

	@Override
	public Long updateTicketEscalationStatus(TicketEscalationPojo TicketEscalationPojo) {
		LOGGER.info("***** The control is inside the updateTicketEscalationStatus in EmployeeTicketDaoImpl *****");
		String query = null;
		if(Util.isNotEmptyObject(TicketEscalationPojo.getTicketEscalationId())) {
			query = SqlQuery.QRY_TO_UPDATE_TICKET_ESCALATION_STATUS;
		}else {
			query =SqlQuery.QRY_TO_UPDATE_TICKET_ESCALATION_STATUS_WTO_TICKET_ID;
		}
		Integer result = nmdPJdbcTemplate.update(query,
				new BeanPropertySqlParameterSource(TicketEscalationPojo));
		LOGGER.debug("***** The number of effected rows for this query is ******" + result);
		return result.longValue();
	}
	/*
	 * This method is useful for ticket pagination purpouse.
	 */
	@Override
	@Deprecated
	public Page<TicketPojo> getTickets(final int pageNo, final int pageSize) throws SQLException {
        PaginationHelper<TicketPojo> paginationHelper = new PaginationHelper<TicketPojo>();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        return paginationHelper.fetchPage(nmdPJdbcTemplate,SqlQuery.QRY_TO_GET_TICKET_COUNT,SqlQuery.QRY_TO_GET_TICKET_DETAILS,namedParameters,pageNo,pageSize,
                new ParameterizedRowMapper<TicketPojo>() {
                    public TicketPojo mapRow(ResultSet rs, int arg1) throws SQLException {
                    	LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketPojo> resultSetMapper = new ResultSetMapper<TicketPojo>();
						List<TicketPojo> ticketPojoLIST = null;
						try {
							ticketPojoLIST = resultSetMapper.mapRersultSetToPojo(rs, TicketPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketPojoLIST objects  is *****" + ticketPojoLIST);
						return ticketPojoLIST.get(0);
                    }
                }
        );
    }
	@Override
	public Page<TicketEscalationPojo> getTicketEscalationDtls(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status, int pageNo, int pageSize) {
		LOGGER.info("***** The control is inside the getTicketEscalationDtls in EmployeeTicketDaoImpl *****");
		PaginationHelper<TicketEscalationPojo> paginationHelper = new PaginationHelper<TicketEscalationPojo>();
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCALATION);
		String query = new String();
		// String query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCALATION).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getSystemEscalatedTicketDetails.spring")) {
			/*
			query = new StringBuilder(query)
					.append(" INNER JOIN TICKET T ")
					.append(" ON T.TICKET_ID = TE.TICKET_ID AND T.STATUS = 6 AND T.TICKET_STATUS <> '11' ")
					.append(" WHERE TE.ESCALATION_TO = :ESCALATION_TO ")
					.append(" AND TE.STATUS = :STATUS ").toString(); 
			*/
			query = SqlQuery.QRY_TO_GET_TICKET_ESC_BY_EMPLOYEE_ID;
			namedParameters.addValue("EMPLOYEE_ID", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}
		else {
			query = new StringBuilder(sqlQuery)
					.append(" INNER JOIN TICKET T ")
					.append(" ON T.TICKET_ID = TE.TICKET_ID AND T.STATUS = 6 AND T.TICKET_STATUS <> '11' ")
					.append(" WHERE TE.TICKET_ID = :TICKET_ID ")
					.append(" AND TE.STATUS = :STATUS ")
					.toString();
			namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}
		return paginationHelper.fetchPage(nmdPJdbcTemplate, new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCALATION_COUNT).toString(),
				query.toString(), namedParameters, pageNo, pageSize,
				new ParameterizedRowMapper<TicketEscalationPojo>() {
					public TicketEscalationPojo mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketEscalationPojo> resultSetMapper = new ResultSetMapper<TicketEscalationPojo>();
						List<TicketEscalationPojo> TicketEscalationPojoLIST = null;
						try {
							TicketEscalationPojoLIST = resultSetMapper.mapRersultSetToPojo(rs,
									TicketEscalationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The TicketEscalationPojoLIST objects  is *****" + TicketEscalationPojoLIST);
						return TicketEscalationPojoLIST.get(0);
					}
				});
	    }

    @Override
	public List<TicketPojo> getTicketList(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("**** The control is inside the getTicketList in EmployeeTicketDaoImpl ****");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTicket")) {
			LOGGER.info("**** The control is inside the getTicket in EmployeeTicketDaoImpl ****");
			if (employeeTicketRequestInfo.getFlatBookingId() != null) {
				//query.append(" WHERE T.BOOKING_ID = :BOOKING_ID AND  T.STATUS = :STATUS  ORDER BY TICKET_ID ");
				query.append(" WHERE T.BOOKING_ID = :BOOKING_ID ORDER BY TICKET_ID ");
				namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
				namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
			} else {
				//query.append(" WHERE T.TICKET_ID = :TICKET_ID AND  T.STATUS = :STATUS ");
				query.append(" WHERE T.TICKET_ID = :TICKET_ID ");
				namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
				namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
			}
		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemEscalate")) {
			query.append(
					" WHERE  T.STATUS = :STATUS  AND T.TICKET_STATUS <> '11' AND T.ESTIMATED_RESOLVED_DATE <= systimestamp  AND (T.ESTIMATED_RESOLVED_DATE_STATUS IS  NULL OR T.ESTIMATED_RESOLVED_DATE_STATUS <> '17') ");
			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		}		
		else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketType")){
			  query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_CHANGE_TICKETTYPE_REQUEST)
			  .append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
			  .append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
			  .append(" WHERE T.STATUS = :STATUS AND T.TICKET_STATUS <> '11' AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) AND CTTR.STATUS IN (:TICKET_TYPE_CHANGE_REQUEST) ")
			  .append(" AND ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) ");
  			  namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
			  namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
			  namedParameters.addValue("TICKET_TYPE_CHANGE_REQUEST", Arrays.asList(Status.RAISED.getStatus()), Types.BIGINT);
			  namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("ticketingReport")){
			 query
			 .append(" INNER JOIN TICKET_TYPE_DETAILS TTD ")
             .append(" ON T.TICKET_TYPE_DETAILS_ID = TTD.TICKET_TYPE_DETAILS_ID ")
			 .append(" WHERE T.STATUS IN (:STATUS) ")
			 .append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ")       
			 .append(" AND T.TICKET_STATUS NOT IN (:TICKET_STATUS)  ")
			 .append(" AND TTD.DEPARTMENT_ID IN (:DEPARTMENT_IDS) ")
			 .toString();
			 namedParameters.addValue("STATUS", Arrays.asList(status.getStatus()), Types.BIGINT);
			 namedParameters.addValue("TICKET_STATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
			 namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
			 namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
			 if(Department.CRM_SALES.getId().equals(employeeTicketRequestInfo.getDepartmentId()) || Department.TECH_CRM.getId().equals(employeeTicketRequestInfo.getDepartmentId())) {
			 	namedParameters.addValue("DEPARTMENT_IDS", Arrays.asList(employeeTicketRequestInfo.getDepartmentId()));
			 }else {   
			 	namedParameters.addValue("DEPARTMENT_IDS", Arrays.asList(Department.CRM_SALES.getId(),Department.TECH_CRM.getId()));
			 }
		}
		else {
			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())) {
				 /* if they are in crm sales head or central crm  write condition based on department rolemapping id*/
				if((Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getRoleId())) && ((employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_SALES_HEAD.getId())) || (employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_CENTRAL_SALES_HEAD.getId()))  ||  (employeeTicketRequestInfo.getDepartmentId().equals(Department.TECH_CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.TECH_CRM_HEAD.getId())) || (employeeTicketRequestInfo.getDepartmentId().equals(Department.TECH_CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.CENTRAL_TECH_CRM_HEAD.getId())))) {
					if(employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")) {
						query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
						.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
						.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT);
						/* if type is all send all tickets otherwise send not closed tickets. */
		  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("specific"):false) {
		  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
		  				}
		  				query.append(" ORDER BY T.CREATED_DATE ");
		  				
						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
						namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
						namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
					}else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets")) {
						//query.append(" WHERE T.STATUS = :STATUS and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID) or (T.ASSIGNMENT_TO = :ASSIGNMENT_TO and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ))");
						query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
						.append(" WHERE T.STATUS = :STATUS and T.TICKET_STATUS <> '11'and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is not null  and T.ASSIGNMENT_TO = :ASSIGNMENT_TO )or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) ");
						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
						namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
						namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
						namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
						namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
					}
					
				/*  crm sales executive and tech crm  executive */	
			    }else {
		         if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets")) {
					//query.append(" WHERE T.STATUS = :STATUS and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID) or (T.ASSIGNMENT_TO = :ASSIGNMENT_TO and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ))");
		        	query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
		        	.append(" WHERE T.STATUS = :STATUS and T.TICKET_STATUS <> '11'and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is not null  and T.ASSIGNMENT_TO = :ASSIGNMENT_TO )or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) ");
		        	namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		        	namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
				} else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")) {
					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
					query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
					//.append(" AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) ")
					.append(" AND T.STATUS = :STATUS ");
					//namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
					//namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
				 }
			  }  
			} else {
				if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MySpecificTickets")) {
					query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE).append(
							" WHERE T.ASSIGNMENT_TO = :ASSIGNMENT_TO AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS  ");
					namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
				/* mis and sales and accounts   */
				} else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets")) {
					query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE).append(
							" WHERE T.DEPARTMENT_ID = :DEPARTMENT_ID AND (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS ");
					namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),
							Types.BIGINT);
					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
				}else if(employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")) {
					/* admin  and management */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && (employeeTicketRequestInfo.getDepartmentId().equals(Department.ADMIN.getId()) || employeeTicketRequestInfo.getDepartmentId().equals(Department.MANAGEMENT.getId()))) {
						  query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
						 .append(" WHERE  T.STATUS = :STATUS ")
						 .append(" ORDER BY T.CREATED_DATE ");
						namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					/*  may be change for crm sales head but currentely wont come. */
					}else {
					/*query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE). append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS).*/
					query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
					.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS).
					//append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS ");
					append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS ");
					namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
					
					/* if type is all send all tickets otherwise send not closed tickets. */
	  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("specific"):false) {
	  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
	  				}
	  				    query.append(" ORDER BY T.CREATED_DATE ");
				   }
				}
			}
		}
		LOGGER.info("**** THE QRY_TO_GET_TICKET_DETAILS IS *****" + query);
		List<List<TicketPojo>> TicketDTOLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new RowMapper<List<TicketPojo>>() {
					public List<TicketPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketPojo> resultSetMapper = new ResultSetMapper<TicketPojo>();
						List<TicketPojo> TicketPojoLIST = null;
						try {
							TicketPojoLIST = resultSetMapper.mapRersultSetToObject(rs, TicketPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The TicketPojoLIST objects  is *****" + TicketPojoLIST);
						return TicketPojoLIST;
					}
				});
		LOGGER.debug("*** The TicketPojoLIST is *****" + TicketDTOLists);
		if (TicketDTOLists.isEmpty()) {
			TicketDTOLists.add(new ArrayList<TicketPojo>());
		}
		LOGGER.debug("**** The TicketPojoLIST is ****" + TicketDTOLists.get(0));
		return TicketDTOLists.get(0);
	}
    @Override
  	public Page<TicketPojo> getTicketList(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status,int pageNo,int pageSize) {
		LOGGER.info("**** The control is inside the getTicketList in EmployeeTicketDaoImpl ****");
  		PaginationHelper<TicketPojo> paginationHelper = new PaginationHelper<TicketPojo>();
  		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_ALL_TICKET_DETAILS);
  		StringBuilder query = new StringBuilder();
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

  		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTicket")) {
  			LOGGER.info("**** The control is inside the getTicket in EmployeeTicketDaoImpl ****");
  			if (employeeTicketRequestInfo.getFlatBookingId() != null) {
  				//query.append(" WHERE T.BOOKING_ID = :BOOKING_ID  ORDER BY TICKET_ID ");
  				query.append(" WHERE T.BOOKING_ID = :BOOKING_ID AND  T.STATUS = :STATUS  ORDER BY T.TICKET_ID ");
  				namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
  				namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  			} else {
  				query.append(" WHERE T.TICKET_ID = :TICKET_ID AND  T.STATUS = :STATUS ");
  				namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
  				namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  			}
  		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemEscalate")) {
  			query.append(" WHERE  T.STATUS = :STATUS  AND T.TICKET_STATUS <> '11'  AND T.ESTIMATED_RESOLVED_DATE <= systimestamp  AND T.ESTIMATED_RESOLVED_DATE_STATUS IS  NULL OR T.ESTIMATED_RESOLVED_DATE_STATUS <> '17' ");
  			namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketType")){
  			  query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_CHANGE_TICKETTYPE_REQUEST)
  			  //.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  			  .append(" WHERE T.STATUS = :STATUS AND T.TICKET_STATUS <> '11' AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) AND CTTR.STATUS IN (:TICKET_TYPE_CHANGE_REQUEST) ")
  			  .append(" AND S.SITE_ID IN (:SITE_ID) ")
  			  .append(" AND ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) ");
  			  namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  			  namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  			  namedParameters.addValue("TICKET_TYPE_CHANGE_REQUEST", Arrays.asList(Status.RAISED.getStatus()), Types.BIGINT);
  			  namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("ticketingReport")){
			 query
			 .append(" WHERE T.STATUS IN (:STATUS) ")
			 .append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ")       
			 .append(" AND T.TICKET_STATUS NOT IN (:TICKET_STATUS)  ")
			 .toString();
			 namedParameters.addValue("STATUS", Arrays.asList(status.getStatus()), Types.BIGINT);
			 namedParameters.addValue("TICKET_STATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
			 namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
			 namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		}else {
  			/*  If ticketTypeDetailsId is given it is (SALES CRM) or (TECH CRM)  */
  			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())) {
  				 /* if they are in crm sales head or central crm  write condition based on department rolemapping id */
  				if((Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getRoleId())) && ((employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_SALES_HEAD.getId())) || (employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_CENTRAL_SALES_HEAD.getId()))  ||  (employeeTicketRequestInfo.getDepartmentId().equals(Department.TECH_CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.TECH_CRM_HEAD.getId())) || (employeeTicketRequestInfo.getDepartmentId().equals(Department.TECH_CRM.getId()) && employeeTicketRequestInfo.getRoleId().equals(Roles.CENTRAL_TECH_CRM_HEAD.getId())))) {
					if(employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")) {
						if(!("closedTickets").equalsIgnoreCase(employeeTicketRequestInfo.getType()) && (Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())
							 || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId()))) {
		 					 
							 /* if statdate is given end date is not given(Then give particular date tickets) */
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
									  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
								  }
								  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
								  }
							 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
							  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
								  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
			 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
										employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
								  }
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
								  }
							  }
							  /* if startDate and endDate both not given */
							  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
		 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
									employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
							    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
							    }
							  }else {
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
									  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
									  }
									  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
										  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
									  }
							  }
							   
							   
							   if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
								   // query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
								    //query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
									//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
									query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
									     .append(" AND S.SITE_ID IN (:SITE_ID) ")
								         .append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE  ");
									
									/* Getting Tickets based on Ticket Id */
									if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
										query.append(" AND T.TICKET_ID=:TICKET_ID ");
										namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
									}
									
									/* based on FlatBooking Id i.e customer name search */
				  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
				  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
				  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
				  					}
								    
								    /* if type is all send all tickets otherwise send not closed tickets. */
					  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("specific"):false) {
					  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
					  				}else {
		 			  					/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
		 			  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
					  				}
					  				    query.append(" ORDER BY T.CREATED_DATE ");
								    
									namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
				  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		 							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
		 							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		  					  }else {
		 							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
		 								
		 								if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
		 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
		 								}
		 								
		 								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
		 								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
										//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
										query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
										.append(" AND S.SITE_ID IN (:SITE_ID) ")
		 								.append(" AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ");
		 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
		 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
		 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		 	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		  				            	namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
				  					/* Getting ticket List based on Escalation Status Id */    
		 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ESCALATED.getStatus())){
		 								query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
										.append(" AND S.SITE_ID IN (:SITE_ID) ")
		 								.append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND T.ESTIMATED_RESOLVED_DATE_STATUS IN (:TICKETSTATUS) ")
		 								.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
		 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
		 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
		 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT); 
				  					/* Getting ticket List based on Closed Status Id */    
		 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.CLOSED.getStatus())){
		 								query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
										.append(" AND S.SITE_ID IN (:SITE_ID) ")
		 								.append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (:TICKETSTATUS)) ");
		 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
		 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
		 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
				  					/* Getting ticket List based on Active Status Id */    
		 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ACTIVE.getStatus())){
		 								query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
										.append(" AND S.SITE_ID IN (:SITE_ID) ")
		 								.append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ");
		 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
		 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
		 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		 							}else {
		 								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
		 								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
										//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
										query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
										.append(" AND S.SITE_ID IN (:SITE_ID) ")
		 								.append(" AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ");
		 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
		 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
		 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
		  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		  				           }
		 							
		 							/* Getting Tickets based on Ticket Id */
									if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
										query.append(" AND T.TICKET_ID=:TICKET_ID ");
										namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
									}
		 							
		 							/* based on FlatBooking Id i.e customer name search */
				  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
				  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
				  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
				  					}
		 							
		 							/* if type is all send all tickets otherwise send not closed tickets. */
		 			  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("specific"):false) {
		 			  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
		 			  				}else {
		 			  					/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
		 			  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
		 			  				}
		 			  				    query.append(" ORDER BY T.CREATED_DATE ");
		  				       }
		  			       }
		  				else{
		  					/* if type is all send all tickets otherwise send not closed tickets. */
			  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("closedTickets"):false) {
			  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE_WITHOUT_SITE_ID)
			  					//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
								query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT);
			  					/* to view closed tickets service for CRM, TECH CRM Heads and Central Heads*/
			  					query.append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
			  					/* for multiple search options to get closed tickets */
			  					/* based on site ids */
			  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
			  						query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
			  						namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
			  					}else {
			  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
			  					}
			  					/* based on FlatBooking Id i.e customer name search */
			  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
			  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
			  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
			  					}
			  					/* based on Ticket Id */
			  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
			  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
			  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
			  					}
			  					/* based on From Date */
			  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
			  						query.append(" AND T.RESOLVED_DATE>=TRUNC(:FROM_DATE) ");
			  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
			  					}
			  					/* based on To Date */
			  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
			  						query.append(" AND TRUNC(T.RESOLVED_DATE)<=:TO_DATE ");
			  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
			  					}
			  				}else {
			  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
			  					//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
								query.append(SqlQuery.QRY_TO_GET_TICKET_TYPE_DETAILS_WTO_DEPARTMENT)
								.append(" AND S.SITE_ID IN (:SITE_ID) ")
			  					/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
			  					.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
			  				}
			  				query.append(" ORDER BY T.MODIFIED_DATE DESC ");
		  					
		  					namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		  		          }	
					}else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets")) {
	  					/* Adding filters to get My Tickets */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
	  						
	  						 /* if statdate is given end date is not given(Then give particular date tickets) */
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
									  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
								  }
								  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
								  }
							 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
							  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
								  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
			 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
										employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
								  }
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
								  }
							  }
							  /* if startDate and endDate both not given */
							  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
		 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
									employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
							    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
							    }
							  }else {
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
									  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
									  }
									  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
										  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
									  }
							   }
	  						
	  						if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
	  							//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
	  							query.append(" WHERE T.STATUS = :STATUS and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 )  and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE ")
	  							.append(" AND S.SITE_ID IN (:SITE_ID) ")
	  							.append(" ORDER BY T.CREATED_DATE ");
	  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
	  							namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
	  	  	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
	  	  	  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  						}else {
	  							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
	  								if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
		 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
		 							 }
	  								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
	  								query.append(" WHERE T.STATUS = :STATUS and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ")
	  								.append(" AND S.SITE_ID IN (:SITE_ID) ")
	  								.append(" ORDER BY T.CREATED_DATE ");
	  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
	  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
	  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
	  	  							namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
	  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
	  	  	  	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
	  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
	  	  	  	  			        namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  							}else {
	  								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
	  								query.append(" WHERE T.STATUS = :STATUS and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO and ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ")
	  								.append(" AND S.SITE_ID IN (:SITE_ID) ")
	  								.append(" ORDER BY T.CREATED_DATE ");
	  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
	  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
	  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
	  	  							namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
	  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
	  	  	  	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
	  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
	  	  	  	  			        namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  							}
	  						}
	  					}else {
	  					//query.append(" WHERE T.STATUS = :STATUS and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID) or (T.ASSIGNMENT_TO = :ASSIGNMENT_TO and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ))");
	  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
	  					query.append(" WHERE T.STATUS = :STATUS and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID) or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) ")
	  					.append(" AND S.SITE_ID IN (:SITE_ID) ")
	  					.append(" ORDER BY T.CREATED_DATE ");
	  					namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
	  					namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  					}
	  				}
				}
  			/* crm sales or tech crm executie employees */	
			 else {
  				if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets")) {
  					/* Adding filters to get My Tickets */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
  						
  						/* if statdate is given end date is not given(Then give particular date tickets) */
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
								  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
							  }
							  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
							  }
						 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
						  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
							  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
		 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
									employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
							  }
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
							  }
						  }
						  /* if startDate and endDate both not given */
						  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
						    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
	 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
								employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
						    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
						    }
						  }else {
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
								  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
								  }
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
							   }
						  }
  						
  						if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
  							//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  							query.append(" WHERE T.STATUS = :STATUS and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 )  and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE ")
  							.append(" AND S.SITE_ID IN (:SITE_ID) ")
  							.append(" ORDER BY T.CREATED_DATE ");
  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  							namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  						}else {
  							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
  								
  								if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
	 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
	 							}
  								
  								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  								query.append(" WHERE T.STATUS = :STATUS  and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ")
  								.append(" AND S.SITE_ID IN (:SITE_ID) ")
  								.append(" ORDER BY T.CREATED_DATE ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  	  			        namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  							}else {
  								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  								query.append(" WHERE T.STATUS = :STATUS  and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO and ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ")
  								.append(" AND S.SITE_ID IN (:SITE_ID) ")
  								.append(" ORDER BY T.CREATED_DATE ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  	  			        namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  							}
  						}
  					}else {
  					//query.append(" WHERE T.STATUS = :STATUS and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID) or (T.ASSIGNMENT_TO = :ASSIGNMENT_TO and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID = :TICKET_TYPE_DETAILS_ID ))");
  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  					//query.append(" WHERE T.STATUS = :STATUS and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) and ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17')) and ((T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID) or (T.ASSIGNMENT_TO is  not null and T.ASSIGNMENT_TO = :ASSIGNMENT_TO ) or ((T.ASSIGNMENT_TO is null and  T.DEPARTMENT_ID is null )  and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID)) or ((T.ASSIGNMENT_TO = 0 and  T.DEPARTMENT_ID = 0 ) and  T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID))) ")
  					query.append(" WHERE T.STATUS = :STATUS AND (T.TICKET_STATUS <> 11 OR T.INVIDUAL_TICKET_STATUS <> 11) AND (T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) OR T.DEPARTMENT_ID = :DEPARTMENT_ID OR T.ASSIGNMENT_TO = :ASSIGNMENT_TO) ");
  					//.append(" AND S.SITE_ID IN (:SITE_ID) ")
  					/* based on site ids */
	  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
	  					query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
	  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
  					}else {
  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					}
	  				/* based on FlatBooking Id i.e customer name search */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
  					}
  					/* based on Ticket Id */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
  					}
  					/* based on From Date */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
  						query.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
  					}
  					/* based on To Date */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
  						query.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
  					}
  					/* based on Pending Emp or Dept Id */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getPendingEmpOrDeptType())) {
  						/* Pending at Ticket Owner */
  						if(MetadataId.EMPLOYEE.getId().equals(employeeTicketRequestInfo.getPendingEmpOrDeptType())) {
  							query.append(" AND (E.EMP_ID=:PENDING_EMP_ID OR T.ASSIGNMENT_TO = :PENDING_EMP_ID) AND (T.ESTIMATED_RESOLVED_DATE_STATUS IS NULL OR T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN(17)) AND (T.DEPARTMENT_ID IS NULL OR T.DEPARTMENT_ID=0) ");
  							namedParameters.addValue("PENDING_EMP_ID", employeeTicketRequestInfo.getPendingEmpOrDeptId());
  						/* Pending at Department */
  						}else if(MetadataId.DEPARTMENT.getId().equals(employeeTicketRequestInfo.getPendingEmpOrDeptType())) {
  							query.append(" AND T.DEPARTMENT_ID = :PENDING_DEPT_ID AND (T.ESTIMATED_RESOLVED_DATE_STATUS IS NULL OR T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN(17)) ");
  							namedParameters.addValue("PENDING_DEPT_ID", employeeTicketRequestInfo.getPendingEmpOrDeptId());
  						/* Pending at Escalation Level Emp */
  						}else if(MetadataId.ESCALATION_LEVEL.getId().equals(employeeTicketRequestInfo.getPendingEmpOrDeptType())) {
  							query.append(" AND TESLEM.EMPLOYEE_ID=:PENDING_EMP_ID ");
  							namedParameters.addValue("PENDING_EMP_ID", employeeTicketRequestInfo.getPendingEmpOrDeptId());
  						}
  					}
  					query.append(" ORDER BY T.CREATED_DATE DESC");
  					namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  					//namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					}
  				} else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")) {
  					/* Adding filters to get All Tickets */
  					if(!("closedTickets").equalsIgnoreCase(employeeTicketRequestInfo.getType()) && (Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())
							 || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId()))) {
  						
  						 /* if statdate is given end date is not given(Then give particular date tickets) */
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
								  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
							  }
							  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
							  }
						 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
						  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
							  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
		 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
									employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
							  }
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
							  }
						  }
						  /* if startDate and endDate both not given */
						  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
						    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
	 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
								employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
						    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
						    }
						  }else {
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
							  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
							  }
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
							  }
						  }
  						
  						if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
  							 query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ");
  							//query.append(" WHERE (T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) or  (T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)) ")
  							//.append(" AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ORDER BY T.CREATED_DATE ");
  							
  							/* Getting Tickets based on Ticket Id */
							if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
								query.append(" AND T.TICKET_ID=:TICKET_ID ");
								namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
							}
  							
  							/* based on FlatBooking Id i.e customer name search */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  					}
							
  							/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  		  					query.append(" AND (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  							     .append(" AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ORDER BY T.CREATED_DATE ");
  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					        namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  					        //namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
  					        namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  						}else {
  							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
  								if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
	 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
	 							}
  								 query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  								//query.append(" WHERE (T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) or  (T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)) ")
  								//.append(" AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ORDER BY T.CREATED_DATE ");
  								/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  	  		  					.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  								.append(" AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  	  			       // namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
  	  	  	  				/* Getting ticket List based on Escalation Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ESCALATED.getStatus())){
 								query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  	  		  					.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  								.append(" AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND T.ESTIMATED_RESOLVED_DATE_STATUS IN (:TICKETSTATUS) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  	  			    /* Getting ticket List based on Closed Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.CLOSED.getStatus())){
 								query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  								.append(" AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (:TICKETSTATUS)) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  	  			    /* Getting ticket List based on Active Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ACTIVE.getStatus())){
 								query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  								.append(" AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 							}else{
  								query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  								//query.append(" WHERE (T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) or (T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)) ")
  								//.append(" AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND T.TICKET_STATUS IN (:TICKETSTATUS) ORDER BY T.CREATED_DATE ");
  								/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  	  		  					.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  								.append(" AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  	  	  					namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  	  	  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  	  	  			        // namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
  				            }
  							
  							/* Getting Tickets based on Ticket Id */
							if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
								query.append(" AND T.TICKET_ID=:TICKET_ID ");
								namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
							}
  							
  							/* based on FlatBooking Id i.e customer name search */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  					}
		  					
							query.append(" ORDER BY T.CREATED_DATE ");
  						   }
  		         } else {
  		        	 if(Util.isNotEmptyObject(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())) && ("closedTickets").equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
  		        		//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE_WITHOUT_SITE_ID)
  		        		query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  		        		.append(" AND T.STATUS = :STATUS ").append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
  		        		/* for multiple search options to get closed tickets */
	  					/* based on site ids */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
	  						query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
	  						namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
	  					}
	  					/* based on FlatBooking Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
	  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
	  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
	  					}
	  					/* based on Ticket Id */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
	  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
	  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
	  					}
	  					/* based on From Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
	  						query.append(" AND T.RESOLVED_DATE>=TRUNC(:FROM_DATE) ");
	  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
	  					}
	  					/* based on To Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
	  						query.append(" AND TRUNC(T.RESOLVED_DATE)<=:TO_DATE ");
	  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
	  					}
  		        	 }else {
  		        		query.append(" WHERE T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) ")
  	  		        	//query.append(" WHERE (T.TICKET_TYPE_DETAILS_ID IN (:TICKET_TYPE_DETAILS_ID) or (T.DEPARTMENT_ID is not null and T.DEPARTMENT_ID = :DEPARTMENT_ID)) ")
  						//.append(" AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS ORDER BY T.CREATED_DATE ");
  	  					.append(" AND T.STATUS = :STATUS ")
  		        		/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  	  					.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");					 
  		        	 }
  		        	 	query.append(" ORDER BY T.MODIFIED_DATE DESC ");
  		        	 	namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  		        	 	namedParameters.addValue("TICKET_TYPE_DETAILS_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsIds())? employeeTicketRequestInfo.getTicketTypeDetailsIds(): 0l,Types.BIGINT);
  	  					//namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
  					}
  				 }
			  }	
  		    }
  			/* If ticketTypeDetailsId is not given it either (CRM SALES HEAD) (TECH CRM HEAD) or (MIS) or (ADMIN)  */
  			else {
  				if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MySpecificTickets")) {
  				/* Adding filters to get Tickets */
  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
  					
  					 /* if statdate is given end date is not given(Then give particular date tickets) */
					  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
							  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
						  }
						  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
						  }
					 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
					  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
						  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
	 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
								employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
						  }
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
						  }
					  }
					  /* if startDate and endDate both not given */
					  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
					    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
							employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
					    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
					    }
					  }else {
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
						  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
						  }
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
						  }
					  }
  					
						if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
							//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
							query.append(" WHERE T.ASSIGNMENT_TO = :ASSIGNMENT_TO AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ")
							.append(" AND S.SITE_ID IN (:SITE_ID) ");
  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					        namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					        namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  					        namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  						}else {
  							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
  								if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
	 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
	 							 }
  								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  								query.append(" WHERE T.ASSIGNMENT_TO = :ASSIGNMENT_TO AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ")
  								.append(" AND S.SITE_ID IN (:SITE_ID) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  						    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  		  					    namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  	  							namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  							}else {
  								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  								query.append(" WHERE T.ASSIGNMENT_TO = :ASSIGNMENT_TO AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ")
  								.append(" AND S.SITE_ID IN (:SITE_ID) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  						    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					    namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
		  						namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  							}
  						}
  				 }
  				else {
  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  					query.append(" WHERE T.ASSIGNMENT_TO = :ASSIGNMENT_TO AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS  ")
  					.append(" AND S.SITE_ID IN (:SITE_ID) ");
  					namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId(), Types.BIGINT);
  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  				    }
  			/* (MIS) or (SALES) or (PRESALES) Tickets */
  				} else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets")) {
  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
  					
  					 /* if statdate is given end date is not given(Then give particular date tickets) */
					  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
							  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
						  }
						  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
						  }
					 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
					  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
						  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
	 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
								employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
						  }
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
						  }
					  }
					  /* if startDate and endDate both not given */
					  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
					    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
							employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
					    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
					    }
					  }else {
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
						  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
						  }
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
						  }
					  }
  					
						if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
							//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
							query.append(" WHERE T.DEPARTMENT_ID = :DEPARTMENT_ID AND (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) AND  ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ")
							.append(" AND S.SITE_ID IN (:SITE_ID) ")
							.append(" ORDER BY T.ASSIGNED_DATE ");
  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					        namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  		  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  					        namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  						}else {
  							//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  							query.append(" WHERE T.DEPARTMENT_ID = :DEPARTMENT_ID AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.INVIDUAL_TICKET_STATUS IN (:TICKETSTATUS)) ")
  							.append(" AND S.SITE_ID IN (:SITE_ID) ")
  							.append(" ORDER BY T.ASSIGNED_DATE ");
  							namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  						namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  	  		  				namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  	  		  				namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  						}
  				}else {
  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  					query.append(" WHERE T.DEPARTMENT_ID = :DEPARTMENT_ID AND (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) AND ((T.ESTIMATED_RESOLVED_DATE_STATUS is null ) or T.ESTIMATED_RESOLVED_DATE_STATUS NOT IN('17'))  AND T.STATUS = :STATUS ");
  					//.append(" AND S.SITE_ID IN (:SITE_ID) ")
  					/* based on site ids */
	  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
	  					query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
	  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
  					}else {
  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					}
	  				/* based on FlatBooking Id i.e customer name search */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
  					}
  					/* based on Ticket Id */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
  					}
  					/* based on From Date */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
  						query.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
  					}
  					/* based on To Date */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
  						query.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
  					}
  					query.append(" ORDER BY T.CREATED_DATE DESC ");
  					//namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  				}
  				}else if(employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")) {
  					/* (ADMIN) or (PROJECT HEAD) */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && (employeeTicketRequestInfo.getDepartmentId().equals(Department.ADMIN.getId())  ||  employeeTicketRequestInfo.getDepartmentId().equals(Department.MANAGEMENT.getId()))) {
  					   if(!("closedTickets").equalsIgnoreCase(employeeTicketRequestInfo.getType()) && (Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())
  							 || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId()))) {
  						
  						 /* if statdate is given end date is not given(Then give particular date tickets) */
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
								  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
							  }
							  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
							  }
						 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
						  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
							  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
		 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
									employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
							  }
							  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
							  }
						  }
						  /* if startDate and endDate both not given */
						  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
						    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
	 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
								employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
						    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
								employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
						    }
						  }else {
							      if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
								    employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
								  }
								  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
									  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
								  }
							  }
  						
 					   if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
 						    //query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
 						    //.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ORDER BY T.CREATED_DATE ");
 						   query.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ");
 						   
 						   /* Getting Tickets based on Ticket Id */
 						   	if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
								query.append(" AND T.TICKET_ID=:TICKET_ID ");
								namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
							}
 						   
 						    /* based on FlatBooking Id i.e customer name search */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  					}
 						   	
 						   /* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
 			  				query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
 			  					 .append(" AND S.SITE_ID IN (:SITE_ID) ")
 			  					 .append(" ORDER BY T.CREATED_DATE ");
 						    namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  							namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  					   }else {
  							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
  								 if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
	 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
	 							 }
  								 //query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  								//.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ORDER BY T.CREATED_DATE ");
  								query.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ")
  								 /* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  	 			  				.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  	 			  			    .append(" AND S.SITE_ID IN (:SITE_ID) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  	  		  				/* Getting ticket List based on Escalation Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ESCALATED.getStatus())){
 								query.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND T.ESTIMATED_RESOLVED_DATE_STATUS IN (:TICKETSTATUS) ")
 								 /* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
 	 			  				.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
 	 			  			    .append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
 	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
 	  		  				/* Getting ticket List based on Closed Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.CLOSED.getStatus())){
 								query.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (:TICKETSTATUS)) ")
 	 			  			    .append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
 	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
 	  		  				/* Getting ticket List based on Active Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ACTIVE.getStatus())){
 								query.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ")
 	 			  			    .append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
 	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
 							}else {
  								 //query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  								//.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ORDER BY T.CREATED_DATE ");
  								query.append(" WHERE  T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ")
  								 /* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  	 			  				.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  	 			  			    .append(" AND S.SITE_ID IN (:SITE_ID) ");
  								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
  								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
  	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  							}
  							
  							/* Getting Tickets based on Ticket Id */
							if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
								query.append(" AND T.TICKET_ID=:TICKET_ID ");
								namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
							}
  							
  							/* based on FlatBooking Id i.e customer name search */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  					}
		  					
							query.append(" ORDER BY T.CREATED_DATE ");
  						}
  					}
  					else {
  						if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("closedTickets"):false) {
  							/* to view closed tickets service for Admin and Management Deprtment Employees */
  							//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE_WITHOUT_SITE_ID)
  							query.append(" WHERE  T.STATUS = :STATUS ")
  		  					.append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
  		  					/* for multiple search options to get closed tickets */
		  					/* based on site ids */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
		  						query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
		  						namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
		  					}else {
		  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
		  					}
		  					/* based on FlatBooking Id i.e customer name search */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  					}
		  					/* based on Ticket Id */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
		  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
		  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		  					}
		  					/* based on From Date */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
		  						query.append(" AND T.RESOLVED_DATE>=TRUNC(:FROM_DATE) ");
		  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
		  					}
		  					/* based on To Date */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
		  						query.append(" AND TRUNC(T.RESOLVED_DATE)<=:TO_DATE ");
		  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
		  					}
  		  				}else {
  		  					/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  		  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  		  					query.append(" WHERE  T.STATUS = :STATUS ")
  			  				.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  			  			    .append(" AND S.SITE_ID IN (:SITE_ID) ");
  		  				}
  						query.append(" ORDER BY T.MODIFIED_DATE DESC ");
  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  						namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  					}
  				}
  				/* (CRM SALES HEAD) or (TECH CRM HEAD)  */
  				else {
  				   if(!("closedTickets").equalsIgnoreCase(employeeTicketRequestInfo.getType()) && (Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())
							 || Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) || Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId()))) {
  					 /* if statdate is given end date is not given(Then give particular date tickets) */
					  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())){
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())){
							  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
						  }
						  if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getStartDate()));
						  }
					 /* if statdate is not given end date is given(Then give tickets from stat date to to date tickets) */	  
					  }else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())){
						  if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
	 						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
								employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
						  }
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
						  }
					  }
					  /* if startDate and endDate both not given */
					  else if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate()) && Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
					    if(Util.isEmptyObject(employeeTicketRequestInfo.getStartDate())){
						    GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
							employeeTicketRequestInfo.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
					    }if(Util.isEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							employeeTicketRequestInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
					    }
					  }else {
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getStartDate())) { 
						  employeeTicketRequestInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp(employeeTicketRequestInfo.getStartDate()));
						  }
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getEndDate())) {
							  employeeTicketRequestInfo.setEndDate(TimeUtil.endOfTheDayTimestamp(employeeTicketRequestInfo.getEndDate()));
						  }
					  }
					  
					   if(Util.isEmptyObject(employeeTicketRequestInfo.getTicketStatusIds())) {
						   // query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
						    //.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
						    query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ");
						   
						    /* Getting Tickets based on Ticket Id */
							if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
								query.append(" AND T.TICKET_ID=:TICKET_ID ");
								namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
							}
						    
						    /* based on FlatBooking Id i.e customer name search */
		  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  					}
						   
						    /* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
		  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
		  					.append(" AND S.SITE_ID IN (:SITE_ID) ");
						    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 							namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					}else {
 							if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.REPLIED.getStatus())){
 								 if(employeeTicketRequestInfo.getTicketStatusIds().size()>1) {
	 								  employeeTicketRequestInfo.getTicketStatusIds().remove(Status.REPLIED.getStatus());
	 							 }
 								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
 								//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
 								query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  AND T.CREATED_DATE BETWEEN :STARTDATE  AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (25)) ")
 								.append(" AND S.SITE_ID IN (:SITE_ID) ");
 								/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
 			  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
 	  		  					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  				            	namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		  					/* Getting ticket List based on Escalation Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ESCALATED.getStatus())){
 								query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND T.ESTIMATED_RESOLVED_DATE_STATUS IN (:TICKETSTATUS) ");
 								/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
 			  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
 			  					.append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		  					/* Getting ticket List based on Closed Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.CLOSED.getStatus())){
 								query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) OR T.INVIDUAL_TICKET_STATUS IN (:TICKETSTATUS)) ")
 			  					.append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
		  					/* Getting ticket List based on Active Status Id */    
 							}else if(employeeTicketRequestInfo.getTicketStatusIds().contains(Status.ACTIVE.getStatus())){
 								query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE ")
 			  					.append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
 							}else {
 								//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
 								//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
 								query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  AND T.CREATED_DATE BETWEEN :STARTDATE AND :ENDDATE AND (T.TICKET_STATUS IN (:TICKETSTATUS) AND (T.INVIDUAL_TICKET_STATUS IS  NULL OR T.INVIDUAL_TICKET_STATUS NOT IN (25))) ");
 								/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
 			  					query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
 			  					.append(" AND S.SITE_ID IN (:SITE_ID) ");
 								namedParameters.addValue("TICKETSTATUS", employeeTicketRequestInfo.getTicketStatusIds(), Types.BIGINT);
 								namedParameters.addValue("STARTDATE", employeeTicketRequestInfo.getStartDate(), Types.TIMESTAMP);
 	  							namedParameters.addValue("ENDDATE", employeeTicketRequestInfo.getEndDate(), Types.TIMESTAMP);
  					            namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
 	  		  				    namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		  					    namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
  				           }
 							
 						   /* Getting Tickets based on Ticket Id */
						   if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
							   query.append(" AND T.TICKET_ID=:TICKET_ID ");
							   namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
						   }
 						   
 						   /* based on FlatBooking Id i.e customer name search */
		  				   if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
		  					  query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
		  					  namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		  				   }
  				       }
  			       }
  				else {
  					/* if type is all send all tickets otherwise send not closed tickets. */
  	  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType())?employeeTicketRequestInfo.getType().equalsIgnoreCase("closedTickets"):false) {
  	  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE_WITHOUT_SITE_ID)
  	  					//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
  	  					query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  ")
  	  					/* to view closed tickets service for all other Employees based on Department Who are not Ticket Owners*/
  	  					.append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
  	  					/* for multiple search options to get closed tickets */
  	  					/* based on site ids */
  	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
  	  						query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
  	  						namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
	  					}else {
	  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
	  					}
  	  				    /* based on FlatBooking Id i.e customer name search */
  	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
  	  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
  	  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
  	  					}
	  					/* based on Ticket Id */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
	  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
	  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
	  					}
	  					/* based on From Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
	  						query.append(" AND T.RESOLVED_DATE>=TRUNC(:FROM_DATE) ");
	  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
	  					}
	  					/* based on To Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
	  						query.append(" AND TRUNC(T.RESOLVED_DATE)<=:TO_DATE ");
	  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
	  					}
  	  				}else {
  	  					//query.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
  	  					//.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_TICKET_TYPE_DETAILS)
  	  					query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  ")
  	  					/* in View All Tickets we have to show All Tickets (Except Closed) add on in Live */
  	  					.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ")
  	  				    .append(" AND S.SITE_ID IN (:SITE_ID) ");
  	  				}
  	  				namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
  	  				namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
					namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
  		          }
  					query.append(" ORDER BY T.CREATED_DATE ");
  				  }
  			   }
  			}
  		}
  		LOGGER.info("**** THE QRY_TO_GET_TICKET_DETAILS IS *****" + sqlQuery.append(query));
  		LOGGER.info("**** THE QRY_TO_GET_TICKET_COUNT_DETAILS IS *****"+ new StringBuilder(SqlQuery.QRY_TO_GET_ALL_TICKET_DETAILS_COUNT).append(query));
  		return paginationHelper.fetchPage(nmdPJdbcTemplate,new StringBuilder(SqlQuery.QRY_TO_GET_ALL_TICKET_DETAILS_COUNT).append(query).toString(), sqlQuery.toString(),
  				namedParameters, pageNo, pageSize, new ParameterizedRowMapper<TicketPojo>() {
  					public TicketPojo mapRow(ResultSet rs, int arg1) throws SQLException {
  						LOGGER.info("***** The ResultSet object is ****" + rs);
  						final ResultSetMapper<TicketPojo> resultSetMapper = new ResultSetMapper<TicketPojo>();
  						List<TicketPojo> ticketPojoLIST = null;
  						try {
  							ticketPojoLIST = resultSetMapper.mapRersultSetToPojo(rs, TicketPojo.class);
  						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
  							LOGGER.error(
  									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
  							String msg = "The Exception is raised while mapping resultset object to Pojo";
  							throw new ResultSetMappingException(msg, ex.getCause());
  						}
  						LOGGER.info("***** The ticketPojoLIST objects  is *****" + ticketPojoLIST);
  						return ticketPojoLIST.get(0);
  					}
  				});
  	}
   @Override
	public Page<TicketSeekInfoPojo> getTicketSeekInfoDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status,int pageNo,int pageSize) {
		LOGGER.info("***** The control is inside the getTicketSeekInfoDetails in EmployeeTicketDaoImpl *****"+ employeeTicketRequestInfo.getTicketId());
		PaginationHelper<TicketSeekInfoPojo> paginationHelper = new PaginationHelper<TicketSeekInfoPojo>();
		String sqlQuery = SqlQuery.QRY_TO_SELECT_TICKET_SEEK_INFO_DETAILS;
		String query = new String();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (status.getStatus().equals(Status.ALL.getStatus())) {
			if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("viewRequestInfo.spring")) {
				sqlQuery = SqlQuery.QRY_TO_SELECT_DISTINCT_TICKET_SEEK_INFO_DETAILS;
				/* site access ivvali */
				query = new StringBuilder(query)
						.append(SqlQuery.QRY_TO_GET_TICKET_DETAILS_WTO_SITE)
						.append(
						" WHERE TICKET_SEEK_INFO.STATUS_ID = :STATUS_ID AND ((TICKET_SEEK_INFO.FROM_ID = :FROM_ID AND TICKET_SEEK_INFO.FROM_DEPT_ID = :FROM_DEPT_ID AND TICKET_SEEK_INFO.FROM_TYPE = :FROM_TYPE)")
						.append("  OR ")
						.append(" (TICKET_SEEK_INFO.TO_DEPT_ID = :FROM_DEPT_ID)) ")
						.append(" ORDER BY TICKET_SEEK_INFO.TICKET_ID ASC ")
						.toString();
				namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				namedParameters.addValue("FROM_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())? employeeTicketRequestInfo.getFromId(): 0l,Types.BIGINT);
				namedParameters.addValue("FROM_TYPE",Util.isNotEmptyObject(employeeTicketRequestInfo.getFromType())? employeeTicketRequestInfo.getFromType(): 0l,Types.BIGINT);
				namedParameters.addValue("FROM_DEPT_ID",Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())? employeeTicketRequestInfo.getFromDeptId(): 0l,Types.BIGINT);
			} else {
				query = new StringBuilder(sqlQuery)
						.append(" WHERE TICKET_SEEK_INFO.STATUS_ID = :STATUS_ID  ORDER BY TICKET_SEEK_INFO_ID ASC  ")
						.toString();
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			}
		} else {
			query = new StringBuilder(sqlQuery).append(
					" WHERE TICKET_SEEK_INFO.TICKET_ID = :TICKET_ID AND TICKET_SEEK_INFO.STATUS_ID = :STATUS_ID  ORDER BY TICKET_SEEK_INFO_ID ASC  ")
					.toString();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		}
		LOGGER.debug("**** THE QRY_TO_SELECT_TICKET_SEEK_INFO_DETAILS IS *****" + new StringBuilder(sqlQuery).append(query));
		return paginationHelper.fetchPage(nmdPJdbcTemplate,
				new StringBuilder(SqlQuery.QRY_TO_SELECT_TICKET_SEEK_INFO_COUNT).append(query).toString(),new StringBuilder(sqlQuery).append(query).toString(),
				namedParameters, pageNo, pageSize, new ParameterizedRowMapper<TicketSeekInfoPojo>() {
			public TicketSeekInfoPojo mapRow(ResultSet rs, int arg1) throws SQLException {
				LOGGER.info("***** The ResultSet object is ****" + rs);
				final ResultSetMapper<TicketSeekInfoPojo> resultSetMapper = new ResultSetMapper<TicketSeekInfoPojo>();
				List<TicketSeekInfoPojo> ticketSeekInfoPojoLIST = null;
				try {
					ticketSeekInfoPojoLIST = resultSetMapper.mapRersultSetToPojo(rs,
							TicketSeekInfoPojo.class);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
					LOGGER.error(
							"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
					String msg = "The Exception is raised while mapping resultset object to Pojo";
					throw new ResultSetMappingException(msg, ex.getCause());
				}
				LOGGER.info("***** The ticketSeekInfoPojoLIST objects  is *****" + ticketSeekInfoPojoLIST);
				return ticketSeekInfoPojoLIST.get(0);
			   }
			});
	}
   @Override
   public List<EmployeePojo> getTicketOwnerEmployee(@NonNull TicketPojo ticketPojo, Status status) {

	   LOGGER.info("***** The control is inside the getTicketOwnerEmployee in EmployeeTicketDaoImpl *****"+ ticketPojo.getTicketId());
	   String sqlQuery = SqlQuery.QRY_TO_GET_TICKET_OWNER_EMPLOYEE_DETAILS;
	   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	   namedParameters.addValue("TICKET_ID", ticketPojo.getTicketId(), Types.BIGINT);
	   namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
	   LOGGER.info("**** THE QRY_TO_GET_TICKET_OWNER IS *****" + sqlQuery);

		List<List<EmployeePojo>> ticketOwnerPojoLists = nmdPJdbcTemplate.query(sqlQuery,
				namedParameters, new RowMapper<List<EmployeePojo>>() {
					public List<EmployeePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeePojo> resultSetMapper = new ResultSetMapper<EmployeePojo>();
						List<EmployeePojo> ticketOwnerPojoList = null;
						try {
							ticketOwnerPojoList = resultSetMapper.mapRersultSetToObject(rs,
									EmployeePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketOwnerPojoList objects  is *****" + ticketOwnerPojoList);
						return ticketOwnerPojoList;
					}
				});
		LOGGER.debug("*** The ticketOwnerPojoLists is *****" + ticketOwnerPojoLists);
		if (ticketOwnerPojoLists.isEmpty()) {
			ticketOwnerPojoLists.add(new ArrayList<EmployeePojo>());
		}
		LOGGER.debug("**** The ticketOwnerPojoLists is ****" + ticketOwnerPojoLists.get(0));
		return ticketOwnerPojoLists.get(0);

   }

	@Override
	public List<EmployeeDetailsPojo> changeTicketOwnerDropDown(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {

		   LOGGER.info("***** The control is inside the changeTicketOwnerDropDown in EmployeeTicketDaoImpl *****"+ employeeTicketRequestInfo.getSiteIds());
		   String sqlQuery = SqlQuery.QRY_TO_GET_CHANGE_TICKET_OWNER_DROPDOWN;
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		   namedParameters.addValue("TICKET_IDS", employeeTicketRequestInfo.getTicketIds(), Types.BIGINT);
		   namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		   LOGGER.info("**** THE QRY_TO_GET_CHANGE_TICKET_OWNER_DROP_DOWN IS *****" + sqlQuery);

			List<List<EmployeeDetailsPojo>> changeTicketOwnerPojoLists = nmdPJdbcTemplate.query(sqlQuery,
					namedParameters, new RowMapper<List<EmployeeDetailsPojo>>() {
						public List<EmployeeDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
							LOGGER.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<EmployeeDetailsPojo> resultSetMapper = new ResultSetMapper<EmployeeDetailsPojo>();
							List<EmployeeDetailsPojo> changeTicketOwnerPojoList = null;
							try {
								changeTicketOwnerPojoList = resultSetMapper.mapRersultSetToObject(rs,
										EmployeeDetailsPojo.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								LOGGER.error(
										"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg, ex.getCause());
							}
							LOGGER.info("***** The ticketOwnerPojoList objects  is *****" + changeTicketOwnerPojoList);
							return changeTicketOwnerPojoList;
						}
					});
			LOGGER.debug("*** The changeTicketOwnerPojoLists is *****" + changeTicketOwnerPojoLists);
			if (changeTicketOwnerPojoLists.isEmpty()) {
				changeTicketOwnerPojoLists.add(new ArrayList<EmployeeDetailsPojo>());
			}
			LOGGER.debug("**** The ticketOwnerPojoLists is ****" + changeTicketOwnerPojoLists.get(0));
			return changeTicketOwnerPojoLists.get(0);
	   }
	@Override
	public Integer changeTicketOwner(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status, Boolean isAssignmentToEqual) {

		   LOGGER.info("***** The control is inside the changeTicketOwner in EmployeeTicketDaoImpl *****"+ employeeTicketRequestInfo.getSiteIds());
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		   
		   StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_CHANGE_TICKET_OWNER);
		   if(isAssignmentToEqual) {
			  sqlQuery.append(" ,T.ASSIGNMENT_TO = :ASSIGNMENT_TO ");
			  namedParameters.addValue("ASSIGNMENT_TO", employeeTicketRequestInfo.getEmployeeId());
		   }
		   sqlQuery.append("WHERE T.TICKET_ID=:TICKET_ID AND T.STATUS=:STATUS");
		   
		   /* this condition for checking customer site_id and also for TicketTypeDetailsId and DeptId and EmdDetId
		   namedParameters.addValue("FLAT_BOOK_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
		   namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
		   namedParameters.addValue("EMPLOYEE_DETAILS_ID", employeeTicketRequestInfo.getEmpDetailsId(), Types.BIGINT);*/
		   namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
		   namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		   namedParameters.addValue("TICKET_TYPE_DETAILS_ID", employeeTicketRequestInfo.getTicketTypeDetailsId(), Types.BIGINT);

		   LOGGER.info("**** THE QRY_TO_CHANGE_TICKET_OWNER_ IS *****" + sqlQuery);

		   Integer i= nmdPJdbcTemplate.update(sqlQuery.toString(),namedParameters);
		   LOGGER.debug("**** The TicketOwner is updated ****" + i);
		   return i;
	   }
	
	
	@Override
	public MailConfigurationDtlsDTO getEmailDtls(MailConfigurationDtlsInfo email) {
		
		LOGGER.info("******* The control inside of the getEmailDtls method in  SiteServiceDaoImpl *******");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MODULE", email.getModule(), Types.VARCHAR);
		namedParameters.addValue("SITE_ID", email.getSiteId(), Types.INTEGER);
		
		List<MailConfigurationDtlsDTO> dtoList = nmdPJdbcTemplate.query(SqlQuery.QRY_TO_GET_MAIL_DTLS,namedParameters,new RowMapper<MailConfigurationDtlsDTO>() {
			public MailConfigurationDtlsDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				LOGGER.info("***** The ResultSet object is ****" + rs);

				MailConfigurationDtlsDTO dto = new MailConfigurationDtlsDTO();
				do {
					dto.setId(rs.getLong("ID"));
					dto.setModule(rs.getString("MODULE"));
					dto.setUserName(rs.getString("USERNAME"));
					dto.setPassword(rs.getString("PASSWORD"));
					dto.setSiteId(rs.getLong("SITE_ID"));
					dto.setKey(rs.getString("KEY"));
					dto.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
				} while (rs.next());
				LOGGER.info("***** The Office object is *****" + dto);
				return dto;
			}
		});
		if (dtoList.isEmpty()) {
			dtoList.add(new MailConfigurationDtlsDTO());
		}
		return dtoList.get(0);
	}
	
	@Override
	public MailConfigurationDtlsDTO getEmployeeEmailDtls(MailConfigurationDtlsInfo email) {
		LOGGER.info("control inside the EmployeeTicketDaoImpl.getEmployeeEmailDtls()");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("EMP_ID", email.getEmpId(), Types.VARCHAR);
		//namedParameters.addValue("SITE_ID", email.getSiteId(), Types.INTEGER);
		
		List<MailConfigurationDtlsDTO> dtoList = nmdPJdbcTemplate.query(SqlQueryTwo.QRY_TO_EMP_EMAIL_PASS,namedParameters,new RowMapper<MailConfigurationDtlsDTO>() {
			public MailConfigurationDtlsDTO mapRow(ResultSet rs, int arg1) throws SQLException {

				MailConfigurationDtlsDTO dto = new MailConfigurationDtlsDTO();
				do {
					dto.setId(rs.getLong("EMP_ID"));
					//dto.setModule(rs.getString("MODULE"));
					dto.setUserName(rs.getString("EMAIL"));
					dto.setPassword(rs.getString("EMP_ENCRYPTED_PASS"));
					//dto.setSiteId(rs.getLong("SITE_ID"));
					dto.setKey(rs.getString("KEY"));
					//dto.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
					
				} while (rs.next());
				LOGGER.info("***** The Office object is *****" + dto);
				return dto;
			}
		});
		if (dtoList.isEmpty()) {
			dtoList.add(new MailConfigurationDtlsDTO());
		}
		return dtoList.get(0);
	}
	
	@Override
	public List<TicketEscalationLevelEmployeeMappingPojo> getTicketscalationEmployeeLevelMappingDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {

		   LOGGER.info("***** The control is inside the getTicketscalationEmployeeLevelMappingDetails  in EmployeeTicketDaoImpl *****");
		   String sqlQuery = SqlQuery.QRY_TO_GET_TICKET_ESC_LVL_EMP_MAP;
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		   namedParameters.addValue("TICKET_ESC_LVL_MAP_ID", Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationLevelMappingId())?employeeTicketRequestInfo.getTicketEscalationLevelMappingId():0l, Types.BIGINT);
		   namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		   
		   LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_LVL_EMP_MAP IS *****" + sqlQuery);

			List<List<TicketEscalationLevelEmployeeMappingPojo>> TicketEscalationLevelEmployeeMappingPojoLists = nmdPJdbcTemplate.query(sqlQuery,
					namedParameters, new RowMapper<List<TicketEscalationLevelEmployeeMappingPojo>>() {
						public List<TicketEscalationLevelEmployeeMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
							LOGGER.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<TicketEscalationLevelEmployeeMappingPojo> resultSetMapper = new ResultSetMapper<TicketEscalationLevelEmployeeMappingPojo>();
							List<TicketEscalationLevelEmployeeMappingPojo> TicketEscalationLevelEmployeeMappingPojoList = null;
							try {
								TicketEscalationLevelEmployeeMappingPojoList = resultSetMapper.mapRersultSetToObject(rs,
										TicketEscalationLevelEmployeeMappingPojo.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								LOGGER.error(
										"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg, ex.getCause());
							}
							LOGGER.info("***** The ticketOwnerPojoList objects  is *****" + TicketEscalationLevelEmployeeMappingPojoList);
							return TicketEscalationLevelEmployeeMappingPojoList;
						}
					});
			LOGGER.debug("*** The TicketEscalationLevelEmployeeMappingPojoLists is *****" + TicketEscalationLevelEmployeeMappingPojoLists);
			if (TicketEscalationLevelEmployeeMappingPojoLists.isEmpty()) {
				TicketEscalationLevelEmployeeMappingPojoLists.add(new ArrayList<TicketEscalationLevelEmployeeMappingPojo>());
			}
			LOGGER.debug("**** The TicketEscalationLevelEmployeeMappingPojoLists is ****" + TicketEscalationLevelEmployeeMappingPojoLists.get(0));
			return TicketEscalationLevelEmployeeMappingPojoLists.get(0);
	   }
		
	@Override
	public List<TicketDetailsPojo> getChangeTicketTypeDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status) {
		LOGGER.info("**** The control is inside the getChangeTicketTypeMailDetails in EmployeeTicketDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "";
	    /*  to get escalation level employee details based on ticketId  */	
		if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())?employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getChangeTicketTypeMailDetails"):false) {
		 query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESC_APR_LVL_EMP_DET)
				      .toString();
		 namedParameters.addValue("ROLE_ID",employeeTicketRequestInfo.getRoleId());
		 namedParameters.addValue("DEPT_ID",employeeTicketRequestInfo.getDepartmentId());
		LOGGER.info("**** THE QRY_TO_GET_TICKET_ESC_APR_LVL_EMP_DET IS *****" + query);
		/* to get ticketdetails based on ticket id */
		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())?employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getChangeTicketTypeDetails"):false) {
		 query = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_DET_BY_TICKET_ID)
				      .toString();
		LOGGER.info("**** THE QRY_TO_GET_TICKET_DET_BY_TICKET_ID IS *****" + query);
		}
		namedParameters.addValue("TICKET_ID",employeeTicketRequestInfo.getTicketId(), Types.INTEGER);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.INTEGER);
		
		List<List<TicketDetailsPojo>> ticketDetailsPojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<TicketDetailsPojo>>() {
					public List<TicketDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TicketDetailsPojo> resultSetMapper = new ResultSetMapper<TicketDetailsPojo>();
						List<TicketDetailsPojo> ticketDetailsPojoLIST = null;
						try {
							ticketDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, TicketDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The ticketDetailsPojoLIST objects  is *****" + ticketDetailsPojoLIST);
						return ticketDetailsPojoLIST;
					}
				});
		LOGGER.info("*** The ticketDetailsPojoLists is *****" + ticketDetailsPojoLists);
		if (ticketDetailsPojoLists.isEmpty()) {
			ticketDetailsPojoLists.add(new ArrayList<TicketDetailsPojo>());
		}
		LOGGER.info("**** The ticketReportingPojoLISTS is ****" + ticketDetailsPojoLists.get(0));
		return ticketDetailsPojoLists.get(0);
	}
	
	@Override
	public Long insertChangeTicketTypeRequest(@NonNull ChangeTicketType changeTicketType) {
		LOGGER.info("***** The control is inside the insertChangeTicketTypeRequest in EmployeeTicketDaoImpl *****"
				+ changeTicketType.getTicketId());
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_GET_INSERT_CHANGE_TICKET_TYPE_REQUEST,
				new BeanPropertySqlParameterSource(changeTicketType), keyHolder,
				new String[] { "ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public Long updateChangeTicketTypeRequest(@NonNull ChangeTicketType changeTicketType) {
		LOGGER.info("***** The control is inside the insertChangeTicketTypeRequest in EmployeeTicketDaoImpl *****"
				+ changeTicketType.getTicketId());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CHANGE_TICKET_TYPE_REQUEST;
		namedParameters.addValue("ticketId",changeTicketType.getTicketId());
		namedParameters.addValue("categoryToBeChanged", changeTicketType.getCategoryToBeChanged());
		namedParameters.addValue("messageBody", changeTicketType.getMessageBody());
		namedParameters.addValue("employeeId", changeTicketType.getEmployeeId());
		namedParameters.addValue("modifiedDate", changeTicketType.getModifiedDate());
		namedParameters.addValue("ticketTypeId", changeTicketType.getTicketTypeId());
		namedParameters.addValue("changedTicketTypeId", changeTicketType.getChangedTicketTypeId());
		namedParameters.addValue("noOfTimesRequested", changeTicketType.getNoOfTimesRequested());
		namedParameters.addValue("statusId", changeTicketType.getStatusId());
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		LOGGER.info("**** The noumber of Tickets updated  *****" + result);
		return ((Integer)result).longValue();
	}
	@Override
	public Long updateTicketTypeRequestStatus(@NonNull ChangeTicketType changeTicketType,Status status) {
		LOGGER.info("***** The control is inside the updateTicketTypeRequestStatus in EmployeeTicketDaoImpl *****"
				+ changeTicketType);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_TICKET_TYPE_CHANGE_REQUEST_STATUS;
		namedParameters.addValue("TICKET_TYPE_CHANGE_REQUEST",changeTicketType.getChangeTicketTypeStatus());
		namedParameters.addValue("TICKET_ID", changeTicketType.getTicketId(), Types.BIGINT);
		namedParameters.addValue("STATUS", status.getStatus(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		LOGGER.info("**** The noumber of Tickets updated  *****" + result);
		return ((Integer)result).longValue();
	}
	
	@Override
	public Long insertChangeTicketTypeRequestStatistics(@NonNull ChangeTicketType changeTicketType) {
		LOGGER.info("***** The control is inside the insertChangeTicketTypeRequestStatistics in EmployeeTicketDaoImpl *****"
				+ changeTicketType);
		List<ChangeTicketTypePojo> pojos = getChangeTicketTypeRequestDetails(changeTicketType);
		for(ChangeTicketTypePojo pojo : pojos) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		LOGGER.info("***** The control is inside the insertChangeTicketTypeRequestStatistics in EmployeeTicketDaoImpl *****");
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_INTO_CHANGE_TICKET_TYPE_REQUEST_STATISTICS_,
				new BeanPropertySqlParameterSource(pojo), keyHolder,
				new String[] { "ID" });
		LOGGER.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
		}
		return 0l;
	}
	public <T> List<T> getResult(@NonNull final Class<T> clazz,@NonNull final String query,@NonNull final MapSqlParameterSource namedParameters) {
		LOGGER.info("**** The control is inside the getResult in EmployeeTicketDaoImpl ****"+query);
		List<List<T>> pojoLists = nmdPJdbcTemplate.query(query, namedParameters,
				new RowMapper<List<T>>() {
					public List<T> mapRow(ResultSet rs, int arg1) throws SQLException {
						LOGGER.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<T> resultSetMapper = new ResultSetMapper<T>();
						List<T> pojoLIST = null;
						try {
							pojoLIST = resultSetMapper.mapRersultSetToObject(rs, clazz);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							LOGGER.error(
									"**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						LOGGER.info("***** The pojoLIST objects  is *****" + pojoLIST);
						return pojoLIST;
					}
				});
		LOGGER.info("*** The pojoLists is *****" + pojoLists);
		if (pojoLists.isEmpty()) {
			pojoLists.add(new ArrayList<T>());
		}
		LOGGER.info("**** The pojoLists is ****" + pojoLists.get(0));
		return pojoLists.get(0);
	}

	@Override
	public List<ChangeTicketTypePojo> getChangeTicketTypeRequestDetails(@NonNull ChangeTicketType changeTicketTypeRequest) {
		LOGGER.info("**** The control is inside the getChangeTicketTypeRequestDetails in EmployeeTicketDaoImpl ****");
		 String query = new StringBuilder(SqlQuery.QRY_TO_SELECT_CHANGE_TICKET_TYPE_REQUEST)
			      .toString();
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 namedParameters.addValue("TICKET_ID",changeTicketTypeRequest.getTicketId(), Types.INTEGER);
		 namedParameters.addValue("STATUS", Arrays.asList(Status.RAISED.getStatus(),Status.APPROVED.getStatus(),Status.REJECTED.getStatus()), Types.INTEGER);
		 return getResult(ChangeTicketTypePojo.class,query,namedParameters);
	}
	
	@Override
	public List<TicketDetailsPojo> getChangeTicketTypeDetails(@NonNull ChangeTicketType changeTicketTypeRequest) {
		LOGGER.info("**** The control is inside the getChangeTicketTypeRequestDetails in EmployeeTicketDaoImpl ****");
		 String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHANGE_TICKET_TYPE_DETAILS)
			      .toString();
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 namedParameters.addValue("TICKET_ID",changeTicketTypeRequest.getTicketId(), Types.INTEGER);
		 namedParameters.addValue("TICKET_TYPE_ID",changeTicketTypeRequest.getChangedTicketTypeId(), Types.INTEGER);
		 namedParameters.addValue("STATUS", Arrays.asList(Status.ACTIVE.getStatus()), Types.INTEGER);
		 return getResult(TicketDetailsPojo.class,query,namedParameters);
	}
	@Override
	public Long updateChangeTicketTypeRequestStatus(@NonNull ChangeTicketType changeTicketType,Status status) {
		LOGGER.info("***** The control is inside the updateChangeTicketTypeRequestStatus in EmployeeTicketDaoImpl *****"
				+ changeTicketType);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CHANGE_TICKET_TYPE_REQUEST_ACTION;
		namedParameters.addValue("changedTicketTypeId",changeTicketType.getChangedTicketTypeId());
		namedParameters.addValue("ticketId", Arrays.asList(changeTicketType.getTicketId()));
		namedParameters.addValue("statusId", status.getStatus());
		namedParameters.addValue("employeeId", changeTicketType.getEmployeeId());
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		LOGGER.info("**** The noumber of records updated  *****" + result);
		return ((Integer)result).longValue();
	}
	
	
	@Override
	public Long updateChangeTicketType(@NonNull ChangeTicketType changeTicketType,Status status) {
		LOGGER.info("***** The control is inside the updateChangeTicketType in EmployeeTicketDaoImpl *****"
				+ changeTicketType);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CHANGE_TICKET_TYPE_TICKET;
		namedParameters.addValue("TITLE",changeTicketType.getCategoryToBeChanged());
		namedParameters.addValue("TICKET_TYPE_ID",changeTicketType.getChangedTicketTypeId());
		namedParameters.addValue("STATUS",Status.ACTIVE.getStatus());
		namedParameters.addValue("TICKET_TYPE_CHANGE_REQUEST", status.getStatus());
		namedParameters.addValue("TICKET_ID", Arrays.asList(changeTicketType.getTicketId()));
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		LOGGER.info("**** The noumber of records updated  *****" + result);
		return ((Integer)result).longValue();
	}

	@Override
	public List<TicketReportingPojo> getCustomerTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the getCustomerTicketList in EmployeeTicketDaoImpl *****");
		String sqlQuery = SqlQuery.QRY_TO_GET_CUSTOMER_TICKET_LIST;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		namedParameters.addValue("FLAT_BOOKING_ID",employeeTicketRequestInfo.getFlatBookingId());
		return resultSetMapper.getResult(TicketReportingPojo.class, sqlQuery, namedParameters);
	}

	@Override
	public List<DepartmentPojo> getDepartmentEmployeeDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the getDepartmentEmployeeDetails in EmployeeTicketDaoImpl *****");
		String sqlQuery = SqlQuery.QRY_TO_GET_DEPT_EMP_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		namedParameters.addValue("FLAT_BOOK_ID", employeeTicketRequestInfo.getFlatBookingId());
		namedParameters.addValue("MODULE_ID", Module.TICKETING.getId());
		namedParameters.addValue("DEPT_ID", employeeTicketRequestInfo.getDepartmentId());
		return resultSetMapper.getResult(DepartmentPojo.class, sqlQuery, namedParameters);
	}
	@Override
	public List<TicketEscalationPojo> getEscalationpojo(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_TICKET_ESCALATION_ID);
		sqlQuery.append(" WHERE TICKET_ID=:TICKET_ID");
		sqlQuery.append(" GROUP BY TICKET_ESCALATION_ID,ASSIGNED_DATE");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId());	
		return resultSetMapper.getResult(TicketEscalationPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public Integer updateTicketsESCDATE(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the updateTicketsestimated date in EmployeeTicketDaoImpl *****");
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_UPDATE_TICKETE_DATE);
		sqlQuery.append("WHERE TICKET_ID=:TICKET_ID");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ESTDATE", employeeTicketRequestInfo.getEscalationTime());
		namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId());
		return nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
	}
	
	@Override
	public Integer updateTicketsESCComplaint(TicketEscalationPojo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the updateTicketsESCALATION ASSIGNED DAT in EmployeeTicketDaoImpl *****");
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_UPDATE_TICKETESCALATION_DATE);
		sqlQuery.append("WHERE TICKET_ID=:TICKET_ID");
		sqlQuery.append(" AND TICKET_ESCALATION_ID=:TICKET_ESCALATION_ID");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("ESTDATE", employeeTicketRequestInfo.getEscalationDate());
			namedParameters.addValue("TICKET_ESCALATION_ID", employeeTicketRequestInfo.getTicketEscalationId());
			namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId());	
		return nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
	}
	
	
	@Override
	public Integer updateTicketsComplaint(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the updateTicketsComplaint in EmployeeTicketDaoImpl *****");
		String sqlQuery="";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
			 sqlQuery = SqlQuery.QRY_TO_UPDATE_TICKET_COMPLAINT_STATUS;
		
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
			namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId());
			namedParameters.addValue("TICKET_IDS", employeeTicketRequestInfo.getTicketIds());
	   if("Complaint".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
				
			namedParameters.addValue("COMPLAINT_STATUS", MetadataId.YES.getId());
		}else if("UnComplaint".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
			
			namedParameters.addValue("COMPLAINT_STATUS", MetadataId.NO.getId());
		}
		
		return nmdPJdbcTemplate.update(sqlQuery, namedParameters);
	}

	@Override
	public Page<TicketPojo> getTotalTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo, int pageNo, int pageSize) {
		LOGGER.info("***** The control is inside the getTotalTicketList in EmployeeTicketDaoImpl *****");
		PaginationHelper<TicketPojo> paginationHelper = new PaginationHelper<TicketPojo>();
  		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_ALL_TICKET_DETAILS);
  		StringBuilder query = new StringBuilder();
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		/* Getting Ticket List based on Condition in Request Url */
  		if(Util.isEmptyObject(employeeTicketRequestInfo.getRequestUrl())) {
  
  		/* Getting Ticket List based on Department Id and Role Id */
  		}else {
  			if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && (employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId()) 
  				|| employeeTicketRequestInfo.getDepartmentId().equals(Department.TECH_CRM.getId()))) {
	  			/* Crm Sales Head or Tech Crm Head */
	  			if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRoleId()) && (employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_SALES_HEAD.getId()) 
	  				|| employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_CENTRAL_SALES_HEAD.getId()) || 
	  				employeeTicketRequestInfo.getRoleId().equals(Roles.TECH_CRM_HEAD.getId()) || employeeTicketRequestInfo.getRoleId().equals(Roles.CENTRAL_TECH_CRM_HEAD.getId()))) {
	  				if("TicketComplaints".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
	  					/* To get Department Specific Ticket Complaint List */
	  					query.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  ")
		  				/* To get Ticket Complaint List */
	  					.append(" AND T.COMPLAINT_STATUS = 85 ");
		  				/* for multiple search options To get Ticket Complaint List */
		  				/* based on site ids */
		  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
		  					query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
		  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
	  					}else {
	  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
	  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  					}
		  				/* based on FlatBooking Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
	  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
	  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
	  					}
	  					/* based on Ticket Id */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
	  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
	  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
	  					}
	  					/* based on From Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
	  						query.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
	  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
	  					}
	  					/* based on To Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
	  						query.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
	  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
	  					}
	  					/* based on Ticket Status i.e Open or Closed Tickets 
	  					if(Status.CLOSED.getStatus().equals(employeeTicketRequestInfo.getTicketStatusId())) {
	  						query.append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
	  					}else if(Status.OPEN.getStatus().equals(employeeTicketRequestInfo.getTicketStatusId())) {
	  						query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
	  					} */
	  					/* based on Customer Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())) {
	  						query.append(" AND CUST.CUST_ID=:CUST_ID ");
	  						namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
	  					}
	  					query.append(" ORDER BY T.CREATED_DATE DESC ");
						namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId(),Types.BIGINT);
						namedParameters.addValue("STATUS", Status.ACTIVE.getStatus(), Types.BIGINT);
	  				}
	  			/* For CRM Executives */	
	  			}else {
	  				if("TicketComplaints".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
	  					/* To get Department Specific Ticket Complaint List */
	  					query.append(" WHERE E.EMP_ID=:EMP_ID AND T.STATUS = :STATUS  ")
		  				/* To get Ticket Complaint List */
	  					.append(" AND T.COMPLAINT_STATUS = 85 ");
		  				/* for multiple search options To get Ticket Complaint List */
		  				/* based on site ids */
		  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
		  					query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
		  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
	  					}else {
	  						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
	  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  					}
		  				/* based on FlatBooking Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
	  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
	  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
	  					}
	  					/* based on Ticket Id */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
	  						query.append(" AND T.TICKET_ID=:TICKET_ID ");
	  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
	  					}
	  					/* based on From Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
	  						query.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
	  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
	  					}
	  					/* based on To Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
	  						query.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
	  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
	  					}
	  					/* based on Ticket Status i.e Open or Closed Tickets 
	  					if(Status.CLOSED.getStatus().equals(employeeTicketRequestInfo.getTicketStatusId())) {
	  						query.append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
	  					}else if(Status.OPEN.getStatus().equals(employeeTicketRequestInfo.getTicketStatusId())) {
	  						query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
	  					} */
	  					/* based on Customer Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())) {
	  						query.append(" AND CUST.CUST_ID=:CUST_ID ");
	  						namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
	  					}
	  					query.append(" ORDER BY T.CREATED_DATE DESC ");
						namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId(),Types.BIGINT);
						namedParameters.addValue("STATUS", Status.ACTIVE.getStatus(), Types.BIGINT);
	  				}
	  			}
	  		/* Admin or Management */	
	  		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && (employeeTicketRequestInfo.getDepartmentId().equals(Department.ADMIN.getId()) 
	  			|| employeeTicketRequestInfo.getDepartmentId().equals(Department.MANAGEMENT.getId()))) {
	  			if("TicketComplaints".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
	  				/* To get All Ticket Complaint List */
					query.append(" WHERE T.STATUS = :STATUS ")
	  				/* To get Ticket Complaint List */
	  				.append(" AND T.COMPLAINT_STATUS = 85 ");
	  				/* for multiple search options To get Ticket Complaint List */
	  				/* based on site ids */
	  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
	  					query.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
	  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
					}else {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					}
	  				/* based on FlatBooking Id i.e customer name search */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
  						query.append(" AND T.BOOKING_ID=:BOOKING_ID ");
  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
  					}
					/* based on Ticket Id */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
						query.append(" AND T.TICKET_ID=:TICKET_ID ");
						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
					}
					/* based on From Date */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
						query.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
					}
					/* based on To Date */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
						query.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
					}
					/* based on Ticket Status i.e Open or Closed Tickets 
					if(Status.CLOSED.getStatus().equals(employeeTicketRequestInfo.getTicketStatusId())) {
						query.append(" and (T.TICKET_STATUS = 11 or T.INVIDUAL_TICKET_STATUS = 11 ) ");
					}else if(Status.OPEN.getStatus().equals(employeeTicketRequestInfo.getTicketStatusId())) {
						query.append(" and (T.TICKET_STATUS <> 11 or T.INVIDUAL_TICKET_STATUS <> 11 ) ");
					} */
					/* based on Customer Id i.e customer name search */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())) {
  						query.append(" AND CUST.CUST_ID=:CUST_ID ");
  						namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
  					}
					query.append(" ORDER BY T.CREATED_DATE DESC ");
					namedParameters.addValue("STATUS", Status.ACTIVE.getStatus(), Types.BIGINT);	
				}
	  		}
  		}
  		
  		sqlQuery.append(query);
  		LOGGER.info("**** THE QRY_TO_GET_TICKET_DETAILS IS *****" + sqlQuery);
  		LOGGER.info("**** THE QRY_TO_GET_TICKET_COUNT_DETAILS IS *****"+ new StringBuilder(SqlQuery.QRY_TO_GET_ALL_TICKET_DETAILS_COUNT).append(query));
  		return paginationHelper.fetchPage(nmdPJdbcTemplate,new StringBuilder(SqlQuery.QRY_TO_GET_ALL_TICKET_DETAILS_COUNT).append(query).toString(), sqlQuery.toString(),
  			namedParameters, pageNo, pageSize, new ParameterizedRowMapper<TicketPojo>() {
	  			public TicketPojo mapRow(ResultSet rs, int arg1) throws SQLException {
		  			LOGGER.info("***** The ResultSet object is ****" + rs);
		  			final ResultSetMapper<TicketPojo> resultSetMapper = new ResultSetMapper<TicketPojo>();
		  			List<TicketPojo> ticketPojoLIST = null;
		  			try {
		  				ticketPojoLIST = resultSetMapper.mapRersultSetToPojo(rs, TicketPojo.class);
		  			} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
		  				LOGGER.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
		  				String msg = "The Exception is raised while mapping resultset object to Pojo";
		  				throw new ResultSetMappingException(msg, ex.getCause());
		  			}
		  			LOGGER.info("***** The ticketPojoLIST objects  is *****" + ticketPojoLIST);
		  			return ticketPojoLIST.get(0);
	  			} 
  			});
	}

	@Override
	@SuppressWarnings("deprecation")
	public Boolean isTicketOwner(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the isTicketOwner in EmployeeTicketDaoImpl *****");
		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_CHECK_TICKET_OWNER);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		if("getTicket".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
  			sqlQuery.append(" AND TTD.TICKET_TYPE_DETAILS_ID=:TICKET_TYPE_DETAILS_ID ");
  			namedParameters.addValue("TICKET_TYPE_DETAILS_ID", employeeTicketRequestInfo.getTicketTypeDetailsId());
  		}
  		namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId());
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		Long rowsFetched = nmdPJdbcTemplate.queryForLong(sqlQuery.toString(), namedParameters);
  		if(Util.isNotEmptyObject(rowsFetched)) {
  			return Boolean.TRUE;
  		}
  		return Boolean.FALSE;
	}
	
	@Override
	public Long updateTicketOwnerForNewFlat(TicketPojo ticketPojo) {
		LOGGER.info("**** The control is inside the updateNewFlatBookingIdInMessengerDetails in BookingFormServiceDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_TICKET_OWNER);
		namedParameters.addValue("TICKET_TYPE_ID",ticketPojo.getTicketTypeId());
		namedParameters.addValue("STATUS",Status.ACTIVE.getStatus());
		namedParameters.addValue("TICKET_ID", Arrays.asList(ticketPojo.getTicketId()));
		int updatedRows = nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
		LOGGER.info("**** The control is inside the updateTicketOwnerForNewFlat "+updatedRows+" No. of rows updated in Ticket Table ****");
		return Long.valueOf(updatedRows);
	}


	@Override
	public List<TicketPojo> getClosedTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the getTotalTicketList in EmployeeTicketDaoImpl *****");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_ALL_CLOSED_TICKETS);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		/* Getting Ticket List based on Condition in Request Url */
  		if(Util.isEmptyObject(employeeTicketRequestInfo.getRequestEnum())) {
  			// To do Implement 
  		/* Getting Ticket List based on Department Id and Role Id */
  		}else {
  			if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && (employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId()) 
  				|| employeeTicketRequestInfo.getDepartmentId().equals(Department.TECH_CRM.getId()))) {
	  			/* Crm Sales Head or Tech Crm Head */
	  			if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRoleId()) && (employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_SALES_HEAD.getId()) 
	  				|| employeeTicketRequestInfo.getRoleId().equals(Roles.CRM_CENTRAL_SALES_HEAD.getId()) || 
	  				employeeTicketRequestInfo.getRoleId().equals(Roles.TECH_CRM_HEAD.getId()) || employeeTicketRequestInfo.getRoleId().equals(Roles.CENTRAL_TECH_CRM_HEAD.getId()))) {
	  				if(ServiceRequestEnum.CLOSED_TICKETS.equals(employeeTicketRequestInfo.getRequestEnum())) {
	  					/* To get Department Specific Ticket Complaint List */
	  					sqlQuery.append(" WHERE ((TYD.DEPARTMENT_ID = :DEPARTMENT_ID) OR (T.DEPARTMENT_ID = :DEPARTMENT_ID)) AND T.STATUS = :STATUS  ")
	  					/* To get Closed Ticket List */
	  					.append(" and (T.TICKET_STATUS = :CLOSED_STATUS or T.INVIDUAL_TICKET_STATUS = :CLOSED_STATUS) ");
		  				/* for multiple search options To get Closed Ticket List */
		  				/* based on site ids */
		  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
		  					sqlQuery.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
		  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
	  					}else {
	  						sqlQuery.append(" AND S.SITE_ID IN (:SITE_ID) ");
	  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  					}
		  				/* based on FlatBooking Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
	  						sqlQuery.append(" AND T.BOOKING_ID=:BOOKING_ID ");
	  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
	  					}
	  					/* based on Ticket Id */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
	  						sqlQuery.append(" AND T.TICKET_ID=:TICKET_ID ");
	  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
	  					}
	  					/* based on From Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
	  						sqlQuery.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
	  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
	  					}
	  					/* based on To Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
	  						sqlQuery.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
	  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
	  					}
	  					/* based on Customer Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())) {
	  						sqlQuery.append(" AND CUST.CUST_ID=:CUST_ID ");
	  						namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
	  					}
	  					sqlQuery.append(" ORDER BY T.CREATED_DATE DESC ");
						namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId());
						namedParameters.addValue("STATUS", Status.ACTIVE.getStatus());
						namedParameters.addValue("CLOSED_STATUS", Status.CLOSED.getStatus());
	  				}
	  			/* For CRM Executives */	
	  			}else {
	  				if(ServiceRequestEnum.CLOSED_TICKETS.equals(employeeTicketRequestInfo.getRequestEnum())) {
	  					/* To get Department Specific Ticket Complaint List */
	  					sqlQuery.append(" WHERE E.EMP_ID=:EMP_ID AND T.STATUS = :STATUS  ")
		  				/* To get Closed Ticket List */
	  					.append(" and (T.TICKET_STATUS = :CLOSED_STATUS or T.INVIDUAL_TICKET_STATUS = :CLOSED_STATUS) ");
		  				/* for multiple search options To get Closed Ticket List */
		  				/* based on site ids */
		  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
		  					sqlQuery.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
		  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
	  					}else {
	  						sqlQuery.append(" AND S.SITE_ID IN (:SITE_ID) ");
	  						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
	  					}
		  				/* based on FlatBooking Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
	  						sqlQuery.append(" AND T.BOOKING_ID=:BOOKING_ID ");
	  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
	  					}
	  					/* based on Ticket Id */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
	  						sqlQuery.append(" AND T.TICKET_ID=:TICKET_ID ");
	  						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
	  					}
	  					/* based on From Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
	  						sqlQuery.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
	  						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
	  					}
	  					/* based on To Date */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
	  						sqlQuery.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
	  						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
	  					}
	  					/* based on Customer Id i.e customer name search */
	  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())) {
	  						sqlQuery.append(" AND CUST.CUST_ID=:CUST_ID ");
	  						namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
	  					}
	  					sqlQuery.append(" ORDER BY T.CREATED_DATE DESC ");
						namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId());
						namedParameters.addValue("STATUS", Status.ACTIVE.getStatus());
						namedParameters.addValue("CLOSED_STATUS", Status.CLOSED.getStatus());
	  				}
	  			}
	  		/* Admin or Management */	
	  		}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && (employeeTicketRequestInfo.getDepartmentId().equals(Department.ADMIN.getId()) 
	  			|| employeeTicketRequestInfo.getDepartmentId().equals(Department.MANAGEMENT.getId()))) {
	  			if(ServiceRequestEnum.CLOSED_TICKETS.equals(employeeTicketRequestInfo.getRequestEnum())) {
	  				/* To get All Ticket Complaint List */
	  				sqlQuery.append(" WHERE T.STATUS = :STATUS ")
	  				/* To get Closed Ticket List */
	  				.append(" and (T.TICKET_STATUS = :CLOSED_STATUS or T.INVIDUAL_TICKET_STATUS = :CLOSED_STATUS) ");
	  				/* for multiple search options To get Closed Ticket List */
	  				/* based on site ids */
	  				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketSiteIds())) {
	  					sqlQuery.append(" AND S.SITE_ID IN (:TICKET_SITE_IDS)");
	  					namedParameters.addValue("TICKET_SITE_IDS", employeeTicketRequestInfo.getTicketSiteIds(), Types.BIGINT);
					}else {
						sqlQuery.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
					}
	  				/* based on FlatBooking Id i.e customer name search */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFlatBookingId())) {
  						sqlQuery.append(" AND T.BOOKING_ID=:BOOKING_ID ");
  						namedParameters.addValue("BOOKING_ID", employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
  					}
					/* based on Ticket Id */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
						sqlQuery.append(" AND T.TICKET_ID=:TICKET_ID ");
						namedParameters.addValue("TICKET_ID", employeeTicketRequestInfo.getTicketId(), Types.BIGINT);
					}
					/* based on From Date */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDate())) {
						sqlQuery.append(" AND T.CREATED_DATE>=TRUNC(:FROM_DATE) ");
						namedParameters.addValue("FROM_DATE", employeeTicketRequestInfo.getFromDate(), Types.TIMESTAMP);
					}
					/* based on To Date */
					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getToDate())) {
						sqlQuery.append(" AND TRUNC(T.CREATED_DATE)<=:TO_DATE ");
						namedParameters.addValue("TO_DATE", employeeTicketRequestInfo.getToDate(), Types.TIMESTAMP);
					}
					/* based on Customer Id i.e customer name search */
  					if(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())) {
  						sqlQuery.append(" AND CUST.CUST_ID=:CUST_ID ");
  						namedParameters.addValue("CUST_ID", employeeTicketRequestInfo.getCustomerId(), Types.BIGINT);
  					}
  					sqlQuery.append(" ORDER BY T.CREATED_DATE DESC ");
  					namedParameters.addValue("STATUS", Status.ACTIVE.getStatus());
					namedParameters.addValue("CLOSED_STATUS", Status.CLOSED.getStatus());	
				}
	  		}
  		}
  		return resultSetMapper.getResult(TicketPojo.class, sqlQuery.toString(), namedParameters);
	}

	@Override
	public List<TicketPendingDeptDtlsPojo> getTicketPendingDeptDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getTicketPendingDeptDtls in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_TICKET_PENDING_DEPT_DTLS);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("STATUS", Status.ACTIVE.getStatus());
  		namedParameters.addValue("DEPARTMENT_ID", employeeTicketRequestInfo.getDepartmentId());
  		namedParameters.addValue("SITE_IDS", employeeTicketRequestInfo.getSiteIds());
  		namedParameters.addValue("EMP_ID", employeeTicketRequestInfo.getEmployeeId());
  		return resultSetMapper.getResult(TicketPendingDeptDtlsPojo.class, sqlQuery.toString(), namedParameters);
	}
	
	
	@Override
	public List<EmployeeDetailsMailPojo> getCrmEmployees(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the getCrmEmployees in EmployeeTicketDaoImpl ******");
		List<EmployeeDetailsMailPojo> list =null;
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CRM_EMPLOYEES);
		List<Long> deptId=new ArrayList<>();
		List<Long> roleIds=new ArrayList<>();
		if ("crmtech".equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
			deptId.add(Department.CRM_TECH.getId());
			roleIds.add(Roles.TECH_CRM.getId());
		} else if ("crmfinance".equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
			deptId.add(Department.CRM.getId());
			roleIds.add(Roles.SR_CRM_EXECUTIVE.getId());
		} else {
			deptId.add(Department.CRM_TECH.getId());
			deptId.add(Department.CRM.getId());
			roleIds.add(Roles.TECH_CRM.getId());
			roleIds.add(Roles.SR_CRM_EXECUTIVE.getId());
		}
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getSiteIds())) {
			sqlQuery.append(" AND EDSM.SITE_ID IN (:SITE_IDS) ");
			namedParameters.addValue("SITE_IDS", employeeTicketRequestInfo.getSiteIds(), Types.BIGINT);
		}
		namedParameters.addValue("ROLE_ID", roleIds, Types.BIGINT);
		namedParameters.addValue("DEPT_IDS", deptId, Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		list=resultSetMapper.getResult(EmployeeDetailsMailPojo.class, sqlQuery.toString(), namedParameters);
		LOGGER.info("***** List<EmployeeDetailsMailPojo> is *****" + list);
	    return list;
		
	}
	
	
	@Override
	public List<Long> getTicketTypes(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getTicketPendingDeptDtls in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_TICKET_TYPES);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		if ("crmtech".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_TECH_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		if ("crmfinance".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_FINANCE_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		List<Long> list =nmdPJdbcTemplate.queryForList(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> getTicketTypes is *****" + list);
		return list;
	}
	
	
	@Override
	public Long saveTicketTypeDetails(List<TicketTypesPojo> ticketTypesPojoList) {
		LOGGER.info("******* The control inside of the saveTicketTypeDetails  in  EmployeeTicketDaoImpl ********");
		int[] count=nmdPJdbcTemplate.batchUpdate(SqlQueryTwo.SAVE_TICKET_TYPE_DETAILS.toString(),getSqlParameterObjectList(ticketTypesPojoList));
		LOGGER.info("***** saveTicketTypeDetails count is *****" + count);
		return 0l;
	}

	private SqlParameterSource[] getSqlParameterObjectList(List<TicketTypesPojo> ticketTypesPojoList){
		SqlParameterSource[] oo=new SqlParameterSource[ticketTypesPojoList.size()];
		int i=0;
		for (TicketTypesPojo mapSqlParameterSource : ticketTypesPojoList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS", Status.ACTIVE.status, Types.BIGINT);
			namedParameters.addValue("TICKET_TYPE_ID",mapSqlParameterSource.getTicketTypeId(), Types.INTEGER);
			namedParameters.addValue("DEPARTMENT_ID",mapSqlParameterSource.getDeptId(), Types.INTEGER);
			namedParameters.addValue("EMPLOYEE_DETAILS_ID",mapSqlParameterSource.getEmployeeDetailsId(), Types.INTEGER);
			namedParameters.addValue("BLOCK_ID",mapSqlParameterSource.getBlockId(), Types.INTEGER);
			namedParameters.addValue("SITE_ID",mapSqlParameterSource.getSiteId(), Types.INTEGER);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	
	
	@Override
	public Long isEmployeeConfigured(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the isEmployeeConfigured in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_IS_EMPLOYEE_CONFIGURED);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.BIGINT);
  		namedParameters.addValue("BLOCK_ID", employeeTicketRequestInfo.getBlockId(), Types.BIGINT);
  		namedParameters.addValue("DEPT_ID", employeeTicketRequestInfo.getDepartmentId(), Types.BIGINT);
  		Long count=nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** count isEmployeeConfigured is *****" + count);
		return count;
	}
	
	
	
	@Override
	public List<LevelPojo> getEscalationLevel(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getEscalationLevel in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_LEVELS);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		List<LevelPojo> list=resultSetMapper.getResult(LevelPojo.class, sqlQuery.toString(), namedParameters);
  		LOGGER.info("***** getEscalationLevel is *****" + list);
		return list;
	}
	
	@Override
	public List<EmployeeLevelDetailsPojo> getEscalationLevelEmployees(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getEscalationLevelEmployees in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_LEVEL_EMPLOYESS);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		List<Long> deptId=new ArrayList<>();
		List<Long> roleIds=new ArrayList<>();
		roleIds.add(Roles.TECH_CRM.getId());
		roleIds.add(Roles.SR_CRM_EXECUTIVE.getId());
  		if ("crmtech".equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
			deptId.add(Department.CRM_TECH.getId());
			deptId.add(Department.MANAGEMENT.getId());
			deptId.add(Department.ADMIN.getId());
		} else if ("crmfinance".equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
			deptId.add(Department.CRM.getId());
			deptId.add(Department.MANAGEMENT.getId());
			deptId.add(Department.ADMIN.getId());
		} else {
			deptId.add(Department.CRM_TECH.getId());
			deptId.add(Department.CRM.getId());
			deptId.add(Department.MANAGEMENT.getId());
			deptId.add(Department.ADMIN.getId());
		}
  		namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.BIGINT);
  		namedParameters.addValue("ROLE_IDS", roleIds, Types.BIGINT);
  		namedParameters.addValue("DEPT_IDS", deptId, Types.BIGINT);
  		List<EmployeeLevelDetailsPojo> list=resultSetMapper.getResult(EmployeeLevelDetailsPojo.class, sqlQuery.toString(), namedParameters);
  		LOGGER.info("***** getEscalationLevel is *****" + list);
		return list;
	}
	
	@Override
	public List<Long> getAllTicketTypes(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType) {
		LOGGER.info("*** The control is inside the getTicketPendingDeptDtls in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_ALL_TICKET_TYPES);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		if ("CRM Technical".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_TECH_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		if ("CRM Financial".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_FINANCE_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		List<Long> list =nmdPJdbcTemplate.queryForList(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> getAllTicketTypes is *****" + list);
		return list;
	}
	
	@Override
	public Long saveTicketEscaExtApprovalLevel(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType) {
		LOGGER.info("******* The control inside of the saveTicketEscaExtApprovalLevel  in  EmployeeTicketDaoImpl ********");
		int[] count=nmdPJdbcTemplate.batchUpdate(SqlQueryTwo.SAVE_TICKET_ESCA_EXT_APPROVAL_LEVAL.toString(),getSqlParameterObjectList(employeeTicketRequestInfo, deptType));
		LOGGER.info("***** saveTicketEscaExtApprovalLevel count is *****" + count);
		return 0l;
	}

	private SqlParameterSource[] getSqlParameterObjectList(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType){
		List<Long> ticketTypes= getAllTicketTypes(employeeTicketRequestInfo, deptType);
		SqlParameterSource[] oo=new SqlParameterSource[ticketTypes.size()];
		int i=0;
		for (Long mapSqlParameterSource : ticketTypes) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS", Status.ACTIVE.status, Types.BIGINT);
			namedParameters.addValue("TYPE", 27L, Types.BIGINT);
			namedParameters.addValue("SITE_ID",employeeTicketRequestInfo.getSiteId(), Types.INTEGER);
			namedParameters.addValue("TICKET_TYPE_ID",mapSqlParameterSource, Types.INTEGER);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	
	@Override
	public List<Long> getTicketEscExtApprovalLevelIds(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType) {
		LOGGER.info("*** The control is inside the getTicketEscExtApprovalLevelIds in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_TICKET_ESCA_EXT_APPROVAL_LEVAL);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
  		if ("CRM Technical".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_TECH_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		if ("CRM Financial".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_FINANCE_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.INTEGER);
  		List<Long> list =nmdPJdbcTemplate.queryForList(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> getTicketEscExtApprovalLevelIds is *****" + list);
		return list;
	}
	
	
	@Override
	public Long getMaxTicketEscalevelMapId(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getMaxTicketEscalevelMapId in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_TICKET_ESC_LEVEL_EMP_MAP);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		Long val =nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> getMaxTicketEscalevelMapId is *****" + val);
		return val;
	}
	
	@Override
	public Long saveTicketEscaLevelMap( List<TicketEscLevelMapPojo> mapPojoList) {
		LOGGER.info("******* The control inside of the saveTicketEscaLevelMap  in  EmployeeTicketDaoImpl ********");
		int[] count=nmdPJdbcTemplate.batchUpdate(SqlQueryTwo.SAVE_TICKET_ESCA_LVL_MAP.toString(),getSqlParameterObject(mapPojoList));
		LOGGER.info("***** saveTicketEscaLevelMap count is *****" + count);
		return 0l;
	}

	private SqlParameterSource[] getSqlParameterObject( List<TicketEscLevelMapPojo> mapPojoList){
		SqlParameterSource[] oo=new SqlParameterSource[mapPojoList.size()];
		int i=0;
		for (TicketEscLevelMapPojo mapSqlParameterSource : mapPojoList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.status, Types.BIGINT);
			namedParameters.addValue("TICKET_ESCA_LVL_MAP_ID", mapSqlParameterSource.getTicketEscLevelMapId(), Types.BIGINT);
			namedParameters.addValue("LEVEL_ID", mapSqlParameterSource.getLevelId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESCA_NXT_LVL_MAP_ID", mapSqlParameterSource.getTicketEscaNextLevelMapId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESC_APR_LVL_ID", mapSqlParameterSource.getTicketEscAppLevelId(), Types.BIGINT);
			namedParameters.addValue("TICKET_HOLD_TIME", mapSqlParameterSource.getTicketHoldTime(), Types.BIGINT);
			namedParameters.addValue("TICKET_EXTENDED_TIME", mapSqlParameterSource.getTicketExtendedTime(), Types.BIGINT);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	
	

	@Override
	public Long saveTicketEscaLevelEmpMap( List<TicketEscaLevelEmpMap> mapEmpPojoList) {
		LOGGER.info("******* The control inside of the saveTicketEscaLevelEmpMap  in  EmployeeTicketDaoImpl ********");
		int[] count=nmdPJdbcTemplate.batchUpdate(SqlQueryTwo.SAVE_TICKET_ESC_LEVEL_EMP_MAP.toString(),getSqlParameterObjectPojos(mapEmpPojoList));
		LOGGER.info("***** saveTicketEscaLevelEmpMap count is *****" + count);
		return 0l;
	}

	private SqlParameterSource[] getSqlParameterObjectPojos( List<TicketEscaLevelEmpMap> mapEmpPojoList){
		SqlParameterSource[] oo=new SqlParameterSource[mapEmpPojoList.size()];
		int i=0;
		for (TicketEscaLevelEmpMap mapSqlParameterSource : mapEmpPojoList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.status, Types.BIGINT);
			namedParameters.addValue("EMPLOYEE_ID", mapSqlParameterSource.getEmpId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESC_LEVEL_EMP_MAP_ID", mapSqlParameterSource.getTicketEscLevelEmpMapId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESC_LVL_MAP_ID", mapSqlParameterSource.getTicektEscLevelMapId(), Types.BIGINT);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	
	
	@Override
	public Long isEscaltionLevelCreated(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType) {
		LOGGER.info("*** The control is inside the isEscaltionLevelCreated in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_IS_ESCALTION_CREATED);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		if ("CRM Technical".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_TECH_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		if ("CRM Financial".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_FINANCE_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.INTEGER);
  		Long val =nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> isEscaltionLevelCreated is *****" + val);
		return val;
	}
	
	
	@Override
	public Long isTicketEscaExtApprovalLevelCreated(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType) {
		LOGGER.info("*** The control is inside the isTicketEscaExtApprovalLevelCreated in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.GET_TICKET_ESCA_EXT_APPROVAL_LEVAL_COUNT);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.INTEGER);
  		if ("CRM Technical".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_TECH_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		if ("CRM Financial".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_FINANCE_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		Long val =nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> isEscaltionLevelCreated is *****" + val);
		return val;
	}
	
	
	@Override
	public Long getMaxTicketEscaExtensionlevelMapId(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getMaxTicketEscaExtensionlevelMapId in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_TICKET_ESCA_EXT_APR_LVL_MAP);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		Long val =nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> getMaxTicketEscaExtensionlevelMapId is *****" + val);
		return val;
	}
	

	@Override
	public Long saveTicketEscaExtLevelMap( List<TicketEscLevelMapPojo> mapPojoList) {
		LOGGER.info("******* The control inside of the saveTicketEscaExtLevelMap  in  EmployeeTicketDaoImpl ********");
		int[] count=nmdPJdbcTemplate.batchUpdate(SqlQueryTwo.SAVE_TICKET_ESCA_EXT_APR_LVL_MAP.toString(),getSqlParameterObjects(mapPojoList));
		LOGGER.info("***** saveTicketEscaExtLevelMap count is *****" + count);
		return 0l;
	}

	private SqlParameterSource[] getSqlParameterObjects( List<TicketEscLevelMapPojo> mapPojoList){
		SqlParameterSource[] oo=new SqlParameterSource[mapPojoList.size()];
		int i=0;
		for (TicketEscLevelMapPojo mapSqlParameterSource : mapPojoList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.status, Types.BIGINT);
			namedParameters.addValue("TICKET_ESCA_EXT_LVL_MAP_ID", mapSqlParameterSource.getTicketEscExtLevelMapId(), Types.BIGINT);
			namedParameters.addValue("LEVEL_ID", mapSqlParameterSource.getLevelId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESC_EXT_REQ_NXT_LVL_ID", mapSqlParameterSource.getTicketEscaExcNextLevelMapId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESC_EXT_APR_LVL_ID", mapSqlParameterSource.getTicketEscExcAppLevelId(), Types.BIGINT);
			namedParameters.addValue("NO_OF_DAYS", mapSqlParameterSource.getNoOfDays(), Types.BIGINT);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	

	@Override
	public Long saveTicketEscaExtLevelEmpMap( List<TicketEscaLevelEmpMap> mapEmpPojoList) {
		LOGGER.info("******* The control inside of the saveTicketEscaLevelEmpMap  in  EmployeeTicketDaoImpl ********");
		int[] count=nmdPJdbcTemplate.batchUpdate(SqlQueryTwo.SAVE_TICKET_ESC_EXT_APR_LVL_EMP_MAP.toString(),getSqlParameteForExtLevelEmpMap(mapEmpPojoList));
		LOGGER.info("***** saveTicketEscaLevelEmpMap count is *****" + count);
		return 0l;
	}

	private SqlParameterSource[] getSqlParameteForExtLevelEmpMap( List<TicketEscaLevelEmpMap> mapEmpPojoList){
		SqlParameterSource[] oo=new SqlParameterSource[mapEmpPojoList.size()];
		int i=0;
		for (TicketEscaLevelEmpMap mapSqlParameterSource : mapEmpPojoList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.status, Types.BIGINT);
			namedParameters.addValue("EMPLOYEE_ID", mapSqlParameterSource.getEmpId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESCA_EXT_APR_LVL_MAP_ID", mapSqlParameterSource.getTicketEscLevelEmpMapId(), Types.BIGINT);
			namedParameters.addValue("TICKET_ESC_LEVEL_EMP_MAP_ID", mapSqlParameterSource.getTicektEscLevelMapId(), Types.BIGINT);
			oo[i]=namedParameters;
			i++;
		}
		return oo;	
	}
	
	@Override
	public Long isEscaltionLevelCreatedForExt(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType) {
		LOGGER.info("*** The control is inside the isEscaltionLevelCreated in EmployeeTicketDaoImpl ***");
  		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_IS_ESCALTION_CREATED_FOR_EXT);
  		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  		if ("CRM Technical".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_TECH_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		if ("CRM Financial".equalsIgnoreCase(deptType)) {
			namedParameters.addValue("TICKET_DEPT_TYPE", MetadataId.CRM_FINANCE_TICKET_TYPE.getId(), Types.BIGINT);
		}
  		namedParameters.addValue("SITE_ID", employeeTicketRequestInfo.getSiteId(), Types.INTEGER);
  		Long val =nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
  		LOGGER.info("***** List<Long> isEscaltionLevelCreated is *****" + val);
		return val;
	}
	
	
	
}
