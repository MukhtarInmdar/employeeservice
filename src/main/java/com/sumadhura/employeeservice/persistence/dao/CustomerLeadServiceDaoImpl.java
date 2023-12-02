/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dto.CustomerHousingRequirementMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerInactiveRemarksPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerLeadCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerLeadCountPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerLeadPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerLeadSiteVisitPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerLeadSubStatusMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerMarketingTypeMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerProjectPreferedLocationMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerSourceMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerTimeFrameToPurchaseMasterPojo;
import com.sumadhura.employeeservice.service.dto.CustomerLeadFormRequest;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;

/**
 * CustomerLeadServiceDaoImpl class provides CustomerLeadServiceDao Form Service specific services.
 * 
 * @author Inamdar
 * @since 17.01.2023
 * @time 10:36PM
 */
@Repository("CustomerLeadServiceDaoImpl")
public class CustomerLeadServiceDaoImpl implements CustomerLeadServiceDao{
	
	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	
	private Logger logger = Logger.getLogger(CustomerLeadServiceDaoImpl.class);
	
	@Override
	public int saveCustomerLead(CustomerLeadFormRequest customerLeadFormRequest)throws DataIntegrityViolationException , Exception
	{
		// TODO Auto-generated method stub
		logger.info("******* The control inside of the saveCustomerLead  in  CustomerLeadServiceDaoImpl ********");
			int count;
			if(customerLeadFormRequest.getLeadId() == null || customerLeadFormRequest.getLeadId() == 0) {
				System.out.println("Lead id is blannk, hence data is getting insert");
				count=nmdPJdbcTemplate.update(SqlQueryTwo.SAVE_LEAD_CREATION.toString(),getSqlParameterObjectList(customerLeadFormRequest));
			}else {
				System.out.println("Lead id is not blank, hence data is getting update");
				count=nmdPJdbcTemplate.update(SqlQueryTwo.UPDATE_LEAD_CREATION.toString(),getSqlParameterObjectList(customerLeadFormRequest));
			}
		logger.info("***** saveCustomerLead count is *****" + count);
		return count;
	}
	
	@Override
	public int deleteCustomerLead(CustomerLeadFormRequest customerLeadFormRequest) {
		// TODO Auto-generated method stub
		logger.info("******* The control inside of the deleteCustomerLead  in  CustomerLeadServiceDaoImpl ********");
		int count;
		 count=nmdPJdbcTemplate.update(SqlQueryTwo.DELETE_LEAD_CREATION	.toString(),getSqlParameterObjectList(customerLeadFormRequest));
		logger.info("***** deleteCustomerLead count is *****" + count);
		return count;
	}

	
	/*((SELECT LEAD_CREATION_SEQ.NEXTVAL FROM DUAL),:CUSTOMER_NAME,:COMPANY,:DESIGNATION,sysdate,:LEAD_CREATED_TO_SITE_SCHEDULED,
			:EMAIL,:ALTERNATE_EMAIL1,:ALTERNATE_EMAIL2,:MOBILE,:ADDITIONAL_MOBILE1,:ADDITIONAL_MOBILE2,:PHONE,:EXTENSION,:PROJECT_ID,
			:PREFERED_PROJECT_LOCATION,:FIRST_SOURCE_ID,:LAST_SOURCE_ID,:LEAD_OWNER_ID,:MIN_BUDGET,:MAX_BUDGET,:BUDGET_RANGE,
			:REQUIREMENT_TYPE,:MIN_FLAT_AREA,:MAX_FLAT_AREA,:TIME_FRAME_TO_PURCHASE,:HOUSING_REQUIREMENT,:CUSTOMER_COMMENTS,
			CUSTOMER_ADDRESS_AREA,:CUSTOMER_LOCALITY,CUSTOMER_ALTERNATE_ADDRESS,CUSTOMER_CITY,CUSTOMER_STATE,MARKETING_ID,'A',
			LEAD_SUB_STATUS_ID,sysdate,sysdate)*/

	private MapSqlParameterSource getSqlParameterObjectList(CustomerLeadFormRequest customerLeadFormRequest){
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("LEAD_ID",customerLeadFormRequest.getLeadId(), Types.BIGINT);
			namedParameters.addValue("CUSTOMER_NAME", customerLeadFormRequest.getCustomerName(), Types.VARCHAR);
			namedParameters.addValue("COMPANY",customerLeadFormRequest.getCompany(), Types.VARCHAR);
			namedParameters.addValue("DESIGNATION",customerLeadFormRequest.getDesignation(), Types.VARCHAR);
			namedParameters.addValue("LEAD_CREATED_TO_SITE_SCHEDULED",customerLeadFormRequest.getLeadCreatedToSiteScheduled(), Types.VARCHAR);
			namedParameters.addValue("EMAIL",customerLeadFormRequest.getEmail(), Types.VARCHAR);
			namedParameters.addValue("ALTERNATE_EMAIL1",customerLeadFormRequest.getAleternativeEmail1(), Types.VARCHAR);
			
			namedParameters.addValue("ALTERNATE_EMAIL2", customerLeadFormRequest.getAlternativeEmail2(), Types.VARCHAR);
			namedParameters.addValue("MOBILE",customerLeadFormRequest.getMobile(), Types.BIGINT);
			namedParameters.addValue("ADDITIONAL_MOBILE1",customerLeadFormRequest.getAdditionalMobile1(), Types.BIGINT);
			namedParameters.addValue("ADDITIONAL_MOBILE2",customerLeadFormRequest.getAdditionalMobile2(), Types.BIGINT);
			namedParameters.addValue("PHONE",customerLeadFormRequest.getPhone(), Types.BIGINT);
			namedParameters.addValue("EXTENSION",customerLeadFormRequest.getExtension(), Types.BIGINT);
			
			namedParameters.addValue("PROJECT_ID", customerLeadFormRequest.getProjectId(), Types.BIGINT);
			namedParameters.addValue("PREFERED_PROJECT_LOCATION",customerLeadFormRequest.getPreferdProjectLocation(), Types.BIGINT);
			namedParameters.addValue("FIRST_SOURCE_ID",customerLeadFormRequest.getFirstSourceId(), Types.BIGINT);
			namedParameters.addValue("LAST_SOURCE_ID",customerLeadFormRequest.getLastSourceId(), Types.BIGINT);
			namedParameters.addValue("LEAD_OWNER_ID",customerLeadFormRequest.getLeadOwnerId(), Types.BIGINT);
			namedParameters.addValue("MIN_BUDGET",customerLeadFormRequest.getMinBudget(), Types.BIGINT);
			
			namedParameters.addValue("MAX_BUDGET", customerLeadFormRequest.getMaxBudget(), Types.BIGINT);
			namedParameters.addValue("BUDGET_RANGE",customerLeadFormRequest.getBudgetRange(), Types.VARCHAR);
			namedParameters.addValue("REQUIREMENT_TYPE",customerLeadFormRequest.getRequirementType(), Types.VARCHAR);
			namedParameters.addValue("MIN_FLAT_AREA",customerLeadFormRequest.getMinFlatArea(), Types.BIGINT);
			namedParameters.addValue("MAX_FLAT_AREA",customerLeadFormRequest.getMaxFlatArea(), Types.BIGINT);
			namedParameters.addValue("TIME_FRAME_TO_PURCHASE",customerLeadFormRequest.getTimeFrameToPurchase(), Types.BIGINT);
			
			namedParameters.addValue("HOUSING_REQUIREMENT", customerLeadFormRequest.getHousingRequirement(), Types.BIGINT);
			namedParameters.addValue("CUSTOMER_COMMENTS",customerLeadFormRequest.getCustomerComments(), Types.VARCHAR);
			namedParameters.addValue("CUSTOMER_ADDRESS_AREA",customerLeadFormRequest.getCustomerAddressArea(), Types.VARCHAR);
			namedParameters.addValue("CUSTOMER_LOCALITY",customerLeadFormRequest.getCustomerLocality(), Types.VARCHAR);
			namedParameters.addValue("CUSTOMER_ALTERNATE_ADDRESS",customerLeadFormRequest.getCustomerAlterAddress(), Types.VARCHAR);
			namedParameters.addValue("CUSTOMER_CITY",customerLeadFormRequest.getCustomerCity(), Types.BIGINT);
			
			namedParameters.addValue("CUSTOMER_STATE", customerLeadFormRequest.getCustomerState(), Types.BIGINT);
			namedParameters.addValue("MARKETING_ID",customerLeadFormRequest.getMarketingId(), Types.BIGINT);
			
			if(customerLeadFormRequest.getMarkAsSiteVisit().equals("Y")) 
				customerLeadFormRequest.setLeadSubStatusId(3l);
			else if(customerLeadFormRequest.getMarkAsBooking().equals("Y")) 
				customerLeadFormRequest.setLeadSubStatusId(6l);
			
			namedParameters.addValue("LEAD_SUB_STATUS_ID",customerLeadFormRequest.getLeadSubStatusId(), Types.BIGINT);
			
			namedParameters.addValue("LEAD_TASK_COMMENTS",customerLeadFormRequest.getLeadTaskComments(), Types.VARCHAR);
			namedParameters.addValue("CHANNEL_PARTNER_LEAD_ID",customerLeadFormRequest.getChannelPartnerLeadid(), Types.VARCHAR);
			namedParameters.addValue("CHANNEL_PARTNER_NAME",customerLeadFormRequest.getChannelPartnerName(), Types.VARCHAR);
			namedParameters.addValue("CHANNEL_PARTNER_ADDRESS",customerLeadFormRequest.getChannelPartnerAddress(), Types.VARCHAR);
			namedParameters.addValue("CHANNEL_PARTNER_NUMBER",customerLeadFormRequest.getChannelPartnerNumber(), Types.BIGINT);
			namedParameters.addValue("LEAD_INACTIVE_COMMENTS",customerLeadFormRequest.getLeadInactiveComments(), Types.VARCHAR);
			namedParameters.addValue("MARK_AS_SITE_VISIT",customerLeadFormRequest.getMarkAsSiteVisit(), Types.VARCHAR);
			namedParameters.addValue("MARK_AS_BOOKING",customerLeadFormRequest.getMarkAsBooking(), Types.VARCHAR);
		
			
			namedParameters.addValue("PROJECT_NAME",customerLeadFormRequest.getLeadProjectName(), Types.VARCHAR);
			namedParameters.addValue("lead_date_visit",customerLeadFormRequest.getLeadDateVisit(), Types.DATE);
			namedParameters.addValue("lead_site_visit_status",customerLeadFormRequest.getLeadSiteVisitId(), Types.BIGINT);
			namedParameters.addValue("lead_site_visit_status_name",customerLeadFormRequest.getLeadSiteVisitStatus(), Types.VARCHAR);
			namedParameters.addValue("SALES_REPLY",customerLeadFormRequest.getLeadSaleRep(), Types.VARCHAR);
			namedParameters.addValue("MEET_COMMENTS",customerLeadFormRequest.getLeadSalesMetComment(), Types.VARCHAR);
		
		return namedParameters;	
	}
	
	@Override
	public List<CustomerLeadPojo> getCustomerLead(CustomerLeadFormRequest customerLeadFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCustomerLead in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_LEAD)
				 .append(" WHERE LC.LEAD_CREATION_STATUS = 'A' and LC.LEAD_SUB_STATUS_ID = LSS.LEAD_SUB_STATUS_ID  ")
	             .toString();
		
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (customerLeadFormRequest.getFirstSourceId() != null && customerLeadFormRequest.getFirstSourceId() != 0) {
			query +="  and FIRST_SOURCE_ID = :FIRST_SOURCE_ID ";
			namedParameters.addValue("FIRST_SOURCE_ID",customerLeadFormRequest.getFirstSourceId(), Types.BIGINT);
		}
		
		if (customerLeadFormRequest.getLastSourceId() != null && customerLeadFormRequest.getLastSourceId() != 0) {
			query +="  and LAST_SOURCE_ID = :LAST_SOURCE_ID ";
			namedParameters.addValue("LAST_SOURCE_ID",customerLeadFormRequest.getLastSourceId(), Types.BIGINT);
		}
		
		if(customerLeadFormRequest.getFromDate() !=null && customerLeadFormRequest.getToDate() != null) {
			query +=" and to_char(REGISTERED_CREATED,'DD/MM/YYYY') between :fromDate and :toDate";
			    namedParameters.addValue("fromDate",customerLeadFormRequest.getFromDate(), Types.VARCHAR);
		        namedParameters.addValue("toDate",  customerLeadFormRequest.getToDate(), Types.VARCHAR);
		}
		
		if( customerLeadFormRequest.getLeadSubStatusId() != null && customerLeadFormRequest.getLeadSubStatusId() != 0  ) {
			query +=" and LC.LEAD_SUB_STATUS_ID = :LEAD_SUB_STATUS_ID";
		        namedParameters.addValue("LEAD_SUB_STATUS_ID",  customerLeadFormRequest.getLeadSubStatusId(), Types.VARCHAR);
		}
		
		if( customerLeadFormRequest.getMobile() != null && customerLeadFormRequest.getMobile() != 0  ) {
			query +=" and MOBILE = :MOBILE";
		        namedParameters.addValue("MOBILE",  customerLeadFormRequest.getMobile(), Types.BIGINT);
		}
		
		if( customerLeadFormRequest.getLeadOwnerId() != null && customerLeadFormRequest.getLeadOwnerId() != 0  ) {
			query +=" and LEAD_OWNER_ID = :LEAD_OWNER_ID";
		        namedParameters.addValue("LEAD_OWNER_ID",  customerLeadFormRequest.getLeadOwnerId(), Types.BIGINT);
		}
		if( customerLeadFormRequest.getLeadId() != null && customerLeadFormRequest.getLeadId() != 0  ) {
			query +=" and LEAD_ID = :LEAD_ID";
		        namedParameters.addValue("LEAD_ID",  customerLeadFormRequest.getLeadId(), Types.BIGINT);
		}
		  
		  
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_LEAD IS *****"+query);
		
        List<List<CustomerLeadPojo>> PojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerLeadPojo>>() {
					public List<CustomerLeadPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerLeadPojo> resultSetMapper = new ResultSetMapper<CustomerLeadPojo>();
						List<CustomerLeadPojo> PojoLIST = null;
						try {
							PojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerLeadPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The SourceMasterPojoLists objects  is *****" + PojoLIST);		
						return PojoLIST;
					}
				});
		
        logger.info("*** The SourceMasterPojoLists is *****"+PojoLists);
		if(PojoLists.isEmpty()) {
			PojoLists.add(new ArrayList<CustomerLeadPojo>());
		}
		logger.info("**** The SourceMasterPojoLists is ****"+PojoLists.get(0));
		return PojoLists.get(0);
	}
	

	
	@Override
	public List<CustomerLeadPojo> getCustomerLeadMIS(CustomerLeadFormRequest customerLeadFormRequest, String leadSubStatusId) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCustomerLead in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_LEAD_MIS)
				 .append(" WHERE LC.LEAD_CREATION_STATUS = 'A' and LC.LEAD_SUB_STATUS_ID = LSS.LEAD_SUB_STATUS_ID  and LSS.LEAD_SUB_STATUS_ID  in ("+leadSubStatusId+") ")
	             .toString();
		
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (customerLeadFormRequest.getFirstSourceId() != null && customerLeadFormRequest.getFirstSourceId() != 0) {
			query +="  and FIRST_SOURCE_ID = :FIRST_SOURCE_ID ";
			namedParameters.addValue("FIRST_SOURCE_ID",customerLeadFormRequest.getFirstSourceId(), Types.BIGINT);
		}
		
		if (customerLeadFormRequest.getLastSourceId() != null && customerLeadFormRequest.getLastSourceId() != 0) {
			query +="  and LAST_SOURCE_ID = :LAST_SOURCE_ID ";
			namedParameters.addValue("LAST_SOURCE_ID",customerLeadFormRequest.getLastSourceId(), Types.BIGINT);
		}
		
		if(customerLeadFormRequest.getFromDate() !=null && customerLeadFormRequest.getToDate() != null) {
			query +=" and to_char(REGISTERED_CREATED,'DD/MM/YYYY') between :fromDate and :toDate";
			    namedParameters.addValue("fromDate",customerLeadFormRequest.getFromDate(), Types.VARCHAR);
		        namedParameters.addValue("toDate",  customerLeadFormRequest.getToDate(), Types.VARCHAR);
		}
		
		if( customerLeadFormRequest.getLeadSubStatusId() != null && customerLeadFormRequest.getLeadSubStatusId() != 0  ) {
			query +=" and LC.LEAD_SUB_STATUS_ID = :LEAD_SUB_STATUS_ID";
		        namedParameters.addValue("LEAD_SUB_STATUS_ID",  customerLeadFormRequest.getLeadSubStatusId(), Types.VARCHAR);
		}
		
		if( customerLeadFormRequest.getMobile() != null && customerLeadFormRequest.getMobile() != 0  ) {
			query +=" and MOBILE = :MOBILE";
		        namedParameters.addValue("MOBILE",  customerLeadFormRequest.getMobile(), Types.BIGINT);
		}
		
		if( customerLeadFormRequest.getLeadOwnerId() != null && customerLeadFormRequest.getLeadOwnerId() != 0  ) {
			query +=" and LEAD_OWNER_ID = :LEAD_OWNER_ID";
		        namedParameters.addValue("LEAD_OWNER_ID",  customerLeadFormRequest.getLeadOwnerId(), Types.BIGINT);
		}
		  
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_LEAD IS *****"+query);
		
        List<List<CustomerLeadPojo>> PojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerLeadPojo>>() {
					public List<CustomerLeadPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerLeadPojo> resultSetMapper = new ResultSetMapper<CustomerLeadPojo>();
						List<CustomerLeadPojo> PojoLIST = null;
						try {
							PojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerLeadPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The SourceMasterPojoLists objects  is *****" + PojoLIST);		
						return PojoLIST;
					}
				});
		
        logger.info("*** The SourceMasterPojoLists is *****"+PojoLists);
		if(PojoLists.isEmpty()) {
			PojoLists.add(new ArrayList<CustomerLeadPojo>());
		}
		logger.info("**** The SourceMasterPojoLists is ****"+PojoLists.get(0));
		return PojoLists.get(0);
	}


	
	@Override
	public List<CustomerLeadCountPojo> getCustomerLeadMISCount(CustomerLeadFormRequest customerLeadFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCustomerLead in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_LEAD_MIS_COUNT)
				 .append(" WHERE LC.LEAD_CREATION_STATUS = 'A'  ")
	             .toString();
		
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (customerLeadFormRequest.getFirstSourceId() != null && customerLeadFormRequest.getFirstSourceId() != 0) {
			query +="  and FIRST_SOURCE_ID = :FIRST_SOURCE_ID ";
			namedParameters.addValue("FIRST_SOURCE_ID",customerLeadFormRequest.getFirstSourceId(), Types.BIGINT);
		}
		
		if (customerLeadFormRequest.getLastSourceId() != null && customerLeadFormRequest.getLastSourceId() != 0) {
			query +="  and LAST_SOURCE_ID = :LAST_SOURCE_ID ";
			namedParameters.addValue("LAST_SOURCE_ID",customerLeadFormRequest.getLastSourceId(), Types.BIGINT);
		}
		
		if(customerLeadFormRequest.getFromDate() !=null && customerLeadFormRequest.getToDate() != null) {
			query +=" and to_char(REGISTERED_CREATED,'DD/MM/YYYY') between :fromDate and :toDate";
			    namedParameters.addValue("fromDate",customerLeadFormRequest.getFromDate(), Types.VARCHAR);
		        namedParameters.addValue("toDate",  customerLeadFormRequest.getToDate(), Types.VARCHAR);
		}
		
		if( customerLeadFormRequest.getLeadSubStatusId() != null && customerLeadFormRequest.getLeadSubStatusId() != 0  ) {
			query +=" and LC.LEAD_SUB_STATUS_ID = :LEAD_SUB_STATUS_ID";
		        namedParameters.addValue("LEAD_SUB_STATUS_ID",  customerLeadFormRequest.getLeadSubStatusId(), Types.VARCHAR);
		}
		
		if( customerLeadFormRequest.getMobile() != null && customerLeadFormRequest.getMobile() != 0  ) {
			query +=" and MOBILE = :MOBILE";
		        namedParameters.addValue("MOBILE",  customerLeadFormRequest.getMobile(), Types.BIGINT);
		}
		
		if( customerLeadFormRequest.getLeadOwnerId() != null && customerLeadFormRequest.getLeadOwnerId() != 0  ) {
			query +=" and LEAD_OWNER_ID = :LEAD_OWNER_ID";
		        namedParameters.addValue("LEAD_OWNER_ID",  customerLeadFormRequest.getLeadOwnerId(), Types.BIGINT);
		}
		  
        query += " group by LEAD_SUB_STATUS_ID) ";
        logger.info("**** THE QRY_TO_GET_CUSTOMER_LEAD IS *****"+query);
		
        List<List<CustomerLeadCountPojo>> PojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerLeadCountPojo>>() {
					public List<CustomerLeadCountPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerLeadCountPojo> resultSetMapper = new ResultSetMapper<CustomerLeadCountPojo>();
						List<CustomerLeadCountPojo> PojoLIST = null;
						try {
							PojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerLeadCountPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The SourceMasterPojoLists objects  is *****" + PojoLIST);		
						return PojoLIST;
					}
				});
		
        logger.info("*** The SourceMasterPojoLists is *****"+PojoLists);
		if(PojoLists.isEmpty()) {
			PojoLists.add(new ArrayList<CustomerLeadCountPojo>());
		}
		logger.info("**** The SourceMasterPojoLists is ****"+PojoLists.get(0));
		return PojoLists.get(0);
	}

	
	@Override
	public List<CustomerLeadSiteVisitPojo> getCustomerLeadSiteVisitList(CustomerLeadFormRequest customerLeadFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCustomerLead in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_LEAD_SITE_VISIT)
				 .append(" WHERE LC.LEAD_CREATION_STATUS = 'A'  and LC.LEAD_SUB_STATUS_ID = LSS.LEAD_SUB_STATUS_ID  ")
	             .toString();
		
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		
		/*if(customerLeadFormRequest.getFromDate() !=null && customerLeadFormRequest.getToDate() != null) {
			query +=" and to_char(REGISTERED_CREATED,'DD/MM/YYYY') between :fromDate and :toDate";
			    namedParameters.addValue("fromDate",customerLeadFormRequest.getFromDate(), Types.VARCHAR);
		        namedParameters.addValue("toDate",  customerLeadFormRequest.getToDate(), Types.VARCHAR);
		}*/
		   
		 if( customerLeadFormRequest.getLeadId() != null && customerLeadFormRequest.getLeadId() != 0  ) {
				query +=" and LC.LEAD_ID = :LEAD_ID";
			        namedParameters.addValue("LEAD_ID",  customerLeadFormRequest.getLeadId(), Types.BIGINT);
		}		  
		
		if( customerLeadFormRequest.getLeadSubStatusId() != null && customerLeadFormRequest.getLeadSubStatusId() != 0  ) {
			query +=" and LC.LEAD_SUB_STATUS_ID = :LEAD_SUB_STATUS_ID";
		        namedParameters.addValue("LEAD_SUB_STATUS_ID",  customerLeadFormRequest.getLeadSubStatusId(), Types.VARCHAR);
		}
		
		
		if( customerLeadFormRequest.getLeadOwnerId() != null && customerLeadFormRequest.getLeadOwnerId() != 0  ) {
			query +=" and LEAD_OWNER_ID = :LEAD_OWNER_ID";
		        namedParameters.addValue("LEAD_OWNER_ID",  customerLeadFormRequest.getLeadOwnerId(), Types.BIGINT);
		}
		  
        logger.info("**** THE QRY_TO_GET_CUSTOMER_LEAD IS *****"+query);
		
        List<List<CustomerLeadSiteVisitPojo>> PojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerLeadSiteVisitPojo>>() {
					public List<CustomerLeadSiteVisitPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerLeadSiteVisitPojo> resultSetMapper = new ResultSetMapper<CustomerLeadSiteVisitPojo>();
						List<CustomerLeadSiteVisitPojo> PojoLIST = null;
						try {
							PojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerLeadSiteVisitPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The SourceMasterPojoLists objects  is *****" + PojoLIST);		
						return PojoLIST;
					}
				});
		
        logger.info("*** The SourceMasterPojoLists is *****"+PojoLists);
		if(PojoLists.isEmpty()) {
			PojoLists.add(new ArrayList<CustomerLeadSiteVisitPojo>());
		}
		logger.info("**** The SourceMasterPojoLists is ****"+PojoLists.get(0));
		return PojoLists.get(0);
	}


	@Override
	public List<CustomerSourceMasterPojo> getCustomerSourceMaster() {
		logger.info("***** The control is inside the getCustomerSourceMaster in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_SOURCE_MASTER)
				// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_SOURCE_MASTER IS *****"+query);
		
        List<List<CustomerSourceMasterPojo>> SourceMasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerSourceMasterPojo>>() {
					public List<CustomerSourceMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerSourceMasterPojo> resultSetMapper = new ResultSetMapper<CustomerSourceMasterPojo>();
						List<CustomerSourceMasterPojo> SourceMasterPojoLIST = null;
						try {
							SourceMasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerSourceMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The SourceMasterPojoLists objects  is *****" + SourceMasterPojoLIST);		
						return SourceMasterPojoLIST;
					}
				});
		
        logger.info("*** The SourceMasterPojoLists is *****"+SourceMasterPojoLists);
		if(SourceMasterPojoLists.isEmpty()) {
			SourceMasterPojoLists.add(new ArrayList<CustomerSourceMasterPojo>());
		}
		logger.info("**** The SourceMasterPojoLists is ****"+SourceMasterPojoLists.get(0));
		return SourceMasterPojoLists.get(0);
	}


	@Override
	public List<CustomerProjectPreferedLocationMasterPojo> getCustomerProjectPreferedLocationMaster() {
		logger.info("***** The control is inside the getCustomerProjectPreferedLocationMaster in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_PROJECT_PREFERRED_LOCATION_MASTER)
				// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE getCustomerProjectPreferedLocationMaster IS *****"+query);
		
        List<List<CustomerProjectPreferedLocationMasterPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerProjectPreferedLocationMasterPojo>>() {
					public List<CustomerProjectPreferedLocationMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerProjectPreferedLocationMasterPojo> resultSetMapper = new ResultSetMapper<CustomerProjectPreferedLocationMasterPojo>();
						List<CustomerProjectPreferedLocationMasterPojo> MasterPojoLIST = null;
						try {
							MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerProjectPreferedLocationMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
						return MasterPojoLIST;
					}
				});
		
        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
		if(MasterPojoLists.isEmpty()) {
			MasterPojoLists.add(new ArrayList<CustomerProjectPreferedLocationMasterPojo>());
		}
		logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
		return MasterPojoLists.get(0);
	}


	@Override
	public List<CustomerTimeFrameToPurchaseMasterPojo> getCustomerTimeFrameTOPurchaseMaster() {
		logger.info("***** The control is inside the getTimeFrameTOPurchaseMaster in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_TIME_FRAME_TO_PURCHASE_MASTER)
				// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE getTimeFrameTOPurchaseMaster IS *****"+query);
		
        List<List<CustomerTimeFrameToPurchaseMasterPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerTimeFrameToPurchaseMasterPojo>>() {
					public List<CustomerTimeFrameToPurchaseMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerTimeFrameToPurchaseMasterPojo> resultSetMapper = new ResultSetMapper<CustomerTimeFrameToPurchaseMasterPojo>();
						List<CustomerTimeFrameToPurchaseMasterPojo> MasterPojoLIST = null;
						try {
							MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerTimeFrameToPurchaseMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
						return MasterPojoLIST;
					}
				});
		
        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
		if(MasterPojoLists.isEmpty()) {
			MasterPojoLists.add(new ArrayList<CustomerTimeFrameToPurchaseMasterPojo>());
		}
		logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
		return MasterPojoLists.get(0);
	}


	@Override
	public List<CustomerHousingRequirementMasterPojo> getCustomerHousingRequirementMaster() {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getTimeFrameTOPurchaseMaster in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_HOUSING_REQUIREMENT_MASTER)
				// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE getHousingRequirementMaster IS *****"+query);
		
        List<List<CustomerHousingRequirementMasterPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerHousingRequirementMasterPojo>>() {
					public List<CustomerHousingRequirementMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerHousingRequirementMasterPojo> resultSetMapper = new ResultSetMapper<CustomerHousingRequirementMasterPojo>();
						List<CustomerHousingRequirementMasterPojo> MasterPojoLIST = null;
						try {
							MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerHousingRequirementMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
						return MasterPojoLIST;
					}
				});
		
        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
		if(MasterPojoLists.isEmpty()) {
			MasterPojoLists.add(new ArrayList<CustomerHousingRequirementMasterPojo>());
		}
		logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
		return MasterPojoLists.get(0);
	}


	@Override
	public List<CustomerLeadSubStatusMasterPojo> getCustomerLeadSubStatusMaster() {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getLeadSubStatusMaster in CustomerLeadServiceDaoImpl *****");
		String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_Lead_Sub_Status_MASTER)
				// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE getLeadSubStatusMaster IS *****"+query);
		
        List<List<CustomerLeadSubStatusMasterPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerLeadSubStatusMasterPojo>>() {
					public List<CustomerLeadSubStatusMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerLeadSubStatusMasterPojo> resultSetMapper = new ResultSetMapper<CustomerLeadSubStatusMasterPojo>();
						List<CustomerLeadSubStatusMasterPojo> MasterPojoLIST = null;
						try {
							MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerLeadSubStatusMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
						return MasterPojoLIST;
					}
				});
		
        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
		if(MasterPojoLists.isEmpty()) {
			MasterPojoLists.add(new ArrayList<CustomerLeadSubStatusMasterPojo>());
		}
		logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
		return MasterPojoLists.get(0);
	}


	@Override
	public List<CustomerMarketingTypeMasterPojo> getCustomerMarketingTypeMaster() {
		// TODO Auto-generated method stub
				logger.info("***** The control is inside the getMarketingTypeMaster in CustomerLeadServiceDaoImpl *****");
				String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_MARKETING_TYPE_MASTER)
						// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
			             .toString();
		        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
		        
		        logger.info("**** THE getMarketingTypeMaster IS *****"+query);
				
		        List<List<CustomerMarketingTypeMasterPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
						new RowMapper<List<CustomerMarketingTypeMasterPojo>>() {
							public List<CustomerMarketingTypeMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
								logger.info("***** The ResultSet object is ****" + rs);
								final ResultSetMapper<CustomerMarketingTypeMasterPojo> resultSetMapper = new ResultSetMapper<CustomerMarketingTypeMasterPojo>();
								List<CustomerMarketingTypeMasterPojo> MasterPojoLIST = null;
								try {
									MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerMarketingTypeMasterPojo.class);
								} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
									logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
									String msg = "The Exception is raised while mapping resultset object to Pojo";
									throw new ResultSetMappingException(msg,ex.getCause());
								}
								logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
								return MasterPojoLIST;
							}
						});
				
		        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
				if(MasterPojoLists.isEmpty()) {
					MasterPojoLists.add(new ArrayList<CustomerMarketingTypeMasterPojo>());
				}
				logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
				return MasterPojoLists.get(0);
	}

	
	@Override
	public List<CustomerInactiveRemarksPojo> getCustomerInactiveRemarksMaster() {
		// TODO Auto-generated method stub
				logger.info("***** The control is inside the getCustomerInactiveRemarksMaster in CustomerLeadServiceDaoImpl *****");
				String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_INACTIVE_REMARKS_MASTER)
						// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
			             .toString();
		        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		       // namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
		        
		        logger.info("**** THE getCustomerInactiveRemarksMaster IS *****"+query);
				
		        List<List<CustomerInactiveRemarksPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
						new RowMapper<List<CustomerInactiveRemarksPojo>>() {
							public List<CustomerInactiveRemarksPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
								logger.info("***** The ResultSet object is ****" + rs);
								final ResultSetMapper<CustomerInactiveRemarksPojo> resultSetMapper = new ResultSetMapper<CustomerInactiveRemarksPojo>();
								List<CustomerInactiveRemarksPojo> MasterPojoLIST = null;
								try {
									MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerInactiveRemarksPojo.class);
								} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
									logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
									String msg = "The Exception is raised while mapping resultset object to Pojo";
									throw new ResultSetMappingException(msg,ex.getCause());
								}
								logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
								return MasterPojoLIST;
							}
						});
				
		        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
				if(MasterPojoLists.isEmpty()) {
					MasterPojoLists.add(new ArrayList<CustomerInactiveRemarksPojo>());
				}
				logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
				return MasterPojoLists.get(0);
	}

	@Override
	public List<CustomerLeadCommentsPojo> getCustomerLeadComments(CustomerLeadFormRequest customerLeadFormRequest) {
		// TODO Auto-generated method stub
				logger.info("***** The control is inside the getCustomerLeadComments in CustomerLeadServiceDaoImpl *****");
				String query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_LEAD_COMMENTS)
						// .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
			             .toString();
		        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		        namedParameters.addValue("LEAD_COMMENT_ID", customerLeadFormRequest.getLeadId(), Types.BIGINT);
		        
		        logger.info("**** THE getCustomerLeadComments IS *****"+query);
				
		        List<List<CustomerLeadCommentsPojo>> MasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
						new RowMapper<List<CustomerLeadCommentsPojo>>() {
							public List<CustomerLeadCommentsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
								logger.info("***** The ResultSet object is ****" + rs);
								final ResultSetMapper<CustomerLeadCommentsPojo> resultSetMapper = new ResultSetMapper<CustomerLeadCommentsPojo>();
								List<CustomerLeadCommentsPojo> MasterPojoLIST = null;
								try {
									MasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerLeadCommentsPojo.class);
								} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
									logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
									String msg = "The Exception is raised while mapping resultset object to Pojo";
									throw new ResultSetMappingException(msg,ex.getCause());
								}
								logger.info("***** The MasterPojoLists objects  is *****" + MasterPojoLIST);		
								return MasterPojoLIST;
							}
						});
				
		        logger.info("*** The MasterPojoLists is *****"+MasterPojoLists);
				if(MasterPojoLists.isEmpty()) {
					MasterPojoLists.add(new ArrayList<CustomerLeadCommentsPojo>());
				}
				logger.info("**** The MasterPojoLists is ****"+MasterPojoLists.get(0));
				return MasterPojoLists.get(0);
	}

	@Override
	public int saveCustomerLeadSiteVisit(CustomerLeadFormRequest customerLeadFormRequest)
			throws DataIntegrityViolationException, Exception {
		// TODO Auto-generated method stub
		logger.info("******* The control inside of the saveCustomerLeadSiteVisit  in  CustomerLeadServiceDaoImpl ********");
		int count;
		if(customerLeadFormRequest.getLeadId() == null || customerLeadFormRequest.getLeadId() == 0) {
			count=nmdPJdbcTemplate.update(SqlQueryTwo.SAVE_LEAD_SITE_VISIT.toString(),getSqlParameterObjectList(customerLeadFormRequest));
		}else {
			count=nmdPJdbcTemplate.update(SqlQueryTwo.UPDATE_LEAD_SITE_VISIT.toString(),getSqlParameterObjectList(customerLeadFormRequest));
		}
	logger.info("***** saveCustomerLead count is *****" + count);
	return count;
	}


	

}


