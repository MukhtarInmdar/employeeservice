package com.sumadhura.employeeservice.persistence.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;

import com.sumadhura.employeeservice.persistence.dto.ApplayLoanDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.ApplyLoanLeadDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.BankerList;
import com.sumadhura.employeeservice.persistence.dto.CustomerDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Util;

@Repository
public class ApplayLoanDaoImpl implements ApplyLoanDao {

	private final Logger LOGGER = Logger.getLogger(BookingFormServiceDaoImpl.class);

	@Autowired(required = true)
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;

	@Resource(name = "ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;

	@Override
	public List<BankerList> getBankEamilOnBooking(CustomerInfo customerInfo) {
		LOGGER.info("*** The control is inside of the getBankEamil in ApplayLoanDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_BANK_EMAIL_ON_BOOKING);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status, Types.BIGINT);
		namedParameters.addValue("PROJECT_ID", customerInfo.getSiteId(), Types.BIGINT);
		List<BankerList> list = resultSetMapper.getResult(BankerList.class, sqlQuery.toString(), namedParameters);
		LOGGER.info("**** getBankEamil() OF List<BankerList>  data is*****"+list);
		return list;
	}
	
	@Override
	public List<EmployeeDetailsPojo> getEmployeeDetailsForMail(CustomerDetailsPojo customerDetailsPojo) {
		LOGGER.info("***** The control is inside the getEmployeeDetailsForMail in RegistrationDaoImpl ******");
		List<EmployeeDetailsPojo> list = null;
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_EMPLOYEE_DETAILS_FOR_MAIL);
		List<Long> deptId = new ArrayList<>();
		deptId.add(Department.CRM.getId());
		//	deptId.add(Department.CRM_TECH.getId());
		List<Long> roleIds = new ArrayList<>();
		roleIds.add(Roles.SR_CRM_EXECUTIVE.getId());
		roleIds.add(Roles.CRM_SALES_HEAD.getId());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", customerDetailsPojo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("ROLE_ID", roleIds, Types.BIGINT);
		namedParameters.addValue("DEPT_IDS", deptId, Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		list = resultSetMapper.getResult(EmployeeDetailsPojo.class, sqlQuery.toString(), namedParameters);
		LOGGER.info("***** List<EmployeeDetailsMailPojo> is *****" + list);
		return list;
	}
	
	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(CustomerInfo customerInfo, Status status) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder querySB = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS_FOR_APPLY_LOAN)
				//.append(" WHERE F.FLAT_ID = :FLAT_ID ")
				.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
				if(status!=null)
				{
					querySB.append(" AND  FB.STATUS_ID = :STATUS_ID");
					namedParameters.addValue("STATUS_ID",status.getStatus(),Types.BIGINT);
				}
	            
		namedParameters.addValue("FLAT_BOOK_ID",customerInfo.getFlatBookingId(), Types.BIGINT);
		//logger.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****"+query+"\nParams :"+namedParameters.getValues());
		 List<List<CustomerPropertyDetailsPojo>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(querySB.toString(),namedParameters,
					new RowMapper<List<CustomerPropertyDetailsPojo>>() {
						public List<CustomerPropertyDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
							LOGGER.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<CustomerPropertyDetailsPojo> resultSetMapper = new ResultSetMapper<CustomerPropertyDetailsPojo>();
							List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoLIST = null;
							try {
								customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPropertyDetailsPojo.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								LOGGER.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg,ex.getCause());
							}
							//logger.info("***** The customerPropertyDetailsPojoLIST objects  is *****" + customerPropertyDetailsPojoLIST);		
							return customerPropertyDetailsPojoLIST;
						}
					});
		
		if(customerPropertyDetailsPojoLists.isEmpty()) {
			customerPropertyDetailsPojoLists.add(new ArrayList<CustomerPropertyDetailsPojo>());
		}
		return customerPropertyDetailsPojoLists.get(0);
	}
	
	@Override
	public Long saveApplyLoanDetailsOnBooking(CustomerInfo customerInfo) {
		LOGGER.info("******* The control inside of the saveApplyLoanDetailsOnBooking  in  ApplayLoanDaoImpl ********");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status, Types.BIGINT);
		namedParameters.addValue("FLAT_BOOKING_ID",customerInfo.getFlatBookingId(), Types.INTEGER);
		namedParameters.addValue("MAIL_SENT_TO",customerInfo.getMailSentTo(), Types.VARCHAR);
		namedParameters.addValue("BANKERS_PROJECTWISWISE_DEFAULT_ID",customerInfo.getBankerListId(), Types.INTEGER);
		nmdPJdbcTemplate.update(SqlQueryTwo.QRY_TO_SAVE_DEFAULT_MAIL_TO_BANKER, namedParameters,keyHolder,new String[] { "DEFAULT_MAIL_TO_BANKER_ID" });
		Long pk = keyHolder.getKey().longValue();
		LOGGER.info("**** The Record Noumber stored into the DEFAULT_MAIL_TO_BANKER table is *****"+pk);
		return pk;
	}
	
	@Override
	public List<ApplyLoanLeadDetailsPojo> getLoanAppliedLeadDetails(LoanRequest loanRequest) {
		LOGGER.info("**** Control inside the ApplayLoanDaoImpl.getLoanAppliedLeadDetails() **** "+loanRequest.getDepartmentId()+" "+loanRequest.getRoleId());
		StringBuffer sqlQuery = new StringBuffer(SqlQueryTwo.QRY_TO_LOAN_APPLIED_LEADD_ETAILS)
				.append(" WHERE  ")
				.append(" FB.STATUS_ID = :STATUS_ID AND LCLED.STATUS_ID = :STATUS_ID ");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
	
		if(Util.isNotEmptyObject(loanRequest.getSiteIds())) {
			sqlQuery.append(" AND S.SITE_ID IN (:SITE_ID) ");
			namedParameters.addValue("SITE_ID",loanRequest.getSiteIds(), Types.BIGINT);
		} else if(Util.isNotEmptyObject(loanRequest.getSiteId())) {
			sqlQuery.append(" AND S.SITE_ID = :SITE_ID ");
			namedParameters.addValue("SITE_ID",loanRequest.getSiteId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(loanRequest.getCustId())) {
			sqlQuery.append(" AND FB.CUST_ID = :CUST_ID ");
			namedParameters.addValue("CUST_ID",loanRequest.getCustId(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(loanRequest.getBookingFormId())) {
			sqlQuery.append(" AND FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
			namedParameters.addValue("FLAT_BOOK_ID",loanRequest.getBookingFormId(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(loanRequest.getFlatId())) {
			sqlQuery.append(" AND FB.FLAT_ID = :FLAT_ID ");
			namedParameters.addValue("FLAT_ID",loanRequest.getFlatId(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(loanRequest.getCustId())) {
			sqlQuery.append(" AND FB.CUST_ID = :CUST_ID ");
			namedParameters.addValue("CUST_ID",loanRequest.getCustId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(loanRequest.getBankerListId())) {
			sqlQuery.append(" AND LBL.BANKERS_LIST_ID = :getBankerListId ");
			namedParameters.addValue("getBankerListId",loanRequest.getBankerListId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(loanRequest.getCustomerLoanEOIDetailsId())) {
			sqlQuery.append(" AND LCLED.CUSTOMER_LOAN_EOI_DETAILS_ID = :CUSTOMER_LOAN_EOI_DETAILS_ID ");
			namedParameters.addValue("CUSTOMER_LOAN_EOI_DETAILS_ID",loanRequest.getCustomerLoanEOIDetailsId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(loanRequest.getDepartmentId()) && Util.isNotEmptyObject(loanRequest.getRoleId())
				&& "UnreadBankerList".equals(loanRequest.getRequestUrl())
				) {
			if(Department.BANKER.getId().equals(loanRequest.getDepartmentId()) && Roles.BANKER.getId().equals(loanRequest.getRoleId())) {
				sqlQuery.append(" AND LCLED.BANKER_LEAD_VIEW_STATUS_ID = :BANKER_LEAD_VIEW_STATUS_ID");
				namedParameters.addValue("BANKER_LEAD_VIEW_STATUS_ID", Status.UNREAD.getStatus(), Types.BIGINT);
			}
		}
		
		if(Util.isNotEmptyObject(loanRequest.getLeadFromDate())&&Util.isNotEmptyObject(loanRequest.getLeadToDate())) {
			//Note : if FTC.CLEARANCE_DATE is not empty means it is cheque transaction, or online transaction
			sqlQuery.append(" AND TRUNC(LCLED.CREATTION_DATE) BETWEEN TRUNC(:getLeadFromDate) AND TRUNC(:getLeanToDate)");
		} else if (Util.isNotEmptyObject(loanRequest.getLeadFromDate())) {
			sqlQuery.append(" AND TRUNC(LCLED.CREATTION_DATE)  >= TRUNC(:getLeadFromDate) ");
		} else if (Util.isNotEmptyObject(loanRequest.getLeadToDate())) {
			sqlQuery.append(" AND TRUNC(LCLED.CREATTION_DATE)  <= TRUNC(:getLeanToDate)");
		}
		
		namedParameters.addValue("getLeadFromDate", loanRequest.getLeadFromDate(),Types.DATE);
		namedParameters.addValue("getLeanToDate", loanRequest.getLeadToDate(),Types.DATE);
		
		sqlQuery.append(" ORDER BY BD.BLOCK_ID,FL.FLOOR_ID,FB.FLAT_ID ");
		LOGGER.info("namedParameters"+namedParameters.getValues());
		List<ApplyLoanLeadDetailsPojo> list = resultSetMapper.getResult(ApplyLoanLeadDetailsPojo.class, sqlQuery.toString(), namedParameters);
		return list;
	}
	
	@Override
	public List<ApplyLoanLeadDetailsPojo> loadLoanAppliedLeadDetails(LoanRequest loanRequest) {
			
		return null;
	}
	
	@Override
	public List<ApplayLoanDocumentsPojo> loadLeadAttachedDocuments(ApplyLoanLeadDetailsPojo applyLoanLeadDetailsPojo) {
		String sqlQuery = (SqlQueryTwo.QRY_TO_GET_DISPLY_DOCUMENTS);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("CUSTOMER_LOAN_EOI_DETAILS_ID",applyLoanLeadDetailsPojo.getCustomerLoanEOIDetailsId(), Types.BIGINT);

		List<ApplayLoanDocumentsPojo> list = resultSetMapper.getResult(ApplayLoanDocumentsPojo.class, sqlQuery.toString(), namedParameters);
		return list;
	}
	
	@Override
	public int updateLeadSeenStatus(LoanRequest loanRequest) {
		String sqlQuery = SqlQueryTwo.QRY_TO_UPDATE_BANKER_SEEN_LEAD_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BANKER_LEAD_VIEW_STATUS_ID", loanRequest.getBankerLeadViewStatusId(), Types.BIGINT);
		//namedParameters.addValue("LEAD_STATUS_ID", loanRequest.getLeadStatusId(), Types.BIGINT);
		namedParameters.addValue("CUSTOMER_LOAN_EOI_DETAILS_ID", loanRequest.getCustomerLoanEOIDetailsId(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(sqlQuery, namedParameters);
		return result;
	}
	
	@Override
	public int updateApplyLoanLeadDetails(LoanRequest loanRequest) {
		String sqlQuery = SqlQueryTwo.QRY_TO_UPDATE_LEAD_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue("BANKER_LEAD_VIEW_STATUS_ID", Status.READ.getStatus(), Types.BIGINT);
		namedParameters.addValue("LEAD_STATUS_ID", loanRequest.getLeadStatusId(), Types.BIGINT);
		namedParameters.addValue("BANKER_COMMENTS", loanRequest.getBankerComment(), Types.VARCHAR);
		namedParameters.addValue("CUSTOMER_LOAN_EOI_DETAILS_ID", loanRequest.getCustomerLoanEOIDetailsId(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(sqlQuery, namedParameters);
		return result;
	}
	
}
