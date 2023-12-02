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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.dto.SearchReferrerCustomer;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dto.AddressMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraCostPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraSiteWisePojo;
import com.sumadhura.employeeservice.persistence.dto.AminititesInfraDetails;
import com.sumadhura.employeeservice.persistence.dto.AminititesInfraFlatWisePojo;
import com.sumadhura.employeeservice.persistence.dto.BlockPojo;
import com.sumadhura.employeeservice.persistence.dto.BookingStatusChangedPojo;
import com.sumadhura.employeeservice.persistence.dto.ChannelPartnerMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListLegalOfficerPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListRegistrationPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListSalesHeadPojo;
import com.sumadhura.employeeservice.persistence.dto.ChecklistCrmPojo;
import com.sumadhura.employeeservice.persistence.dto.ChecklistDepartmentMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.CoAppBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantCheckListVerificationPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.Co_ApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.ContactInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustChecklistVerificationPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerApplicationPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerData;
import com.sumadhura.employeeservice.persistence.dto.CustomerDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerKycSubmittedDocPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerOtherDetailspojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountSummaryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemeTaxMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingSchemeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDtlsAmenitsFlatMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorPojo;
import com.sumadhura.employeeservice.persistence.dto.MenuLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDocumentsList;
import com.sumadhura.employeeservice.persistence.dto.NOCReleasePojo;
import com.sumadhura.employeeservice.persistence.dto.PoaHolderPojo;
import com.sumadhura.employeeservice.persistence.dto.ProfessionalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferenceMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesCustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesFriendPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.persistence.dto.TdsAuthorizationMasterPojo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.CoApplicentBookingInfo;
import com.sumadhura.employeeservice.service.dto.CoApplicentDetailsInfo;
import com.sumadhura.employeeservice.service.dto.Co_ApplicantInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerKYCDocumentSubmitedInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.CustomerSchemeInfo;
import com.sumadhura.employeeservice.service.dto.DynamicKeyValueInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.mappers.BookingFormMapper;
import com.sumadhura.employeeservice.service.mappers.ExtractDataFromResultSet;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * BookingFormServiceDaoImpl class provides Booking Form Service specific services.
 * 
 * @author Srivenu
 * @since 29.05.2019
 * @time 10:50PM
 */
@Repository("BookingFormServiceDaoImpl")
public class BookingFormServiceDaoImpl implements BookingFormServiceDao{
	
	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;
	private Logger logger = Logger.getLogger(BookingFormServiceDaoImpl.class);
	
	@Resource
	@Qualifier("ResultSetMapper")
	private ResultSetMapper<?> resultSetMapper;

	@Override
	public List<FlatPojo> getFlat(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getFlat in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_DETAILS)
				.append(" WHERE F.FLAT_ID = :FLAT_ID ")
				.append(" AND F.STATUS_ID = :STATUS_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_ID", bookingFormRequest.getFlatId(),Types.BIGINT);
        namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_FLAT_DETAILS IS *****"+query);
		
        List<List<FlatPojo>> FlatPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FlatPojo>>() {
					public List<FlatPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatPojo> resultSetMapper = new ResultSetMapper<FlatPojo>();
						List<FlatPojo> FlatPojoLIST = null;
						try {
							FlatPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The FlatPojoLIST objects  is *****" + FlatPojoLIST);		
						return FlatPojoLIST;
					}
				});
		
        logger.info("*** The FlatPojoLists is *****"+FlatPojoLists);
		if(FlatPojoLists.isEmpty()) {
			FlatPojoLists.add(new ArrayList<FlatPojo>());
		}
		logger.info("**** The FlatPojoLists is ****"+FlatPojoLists.get(0));
		return FlatPojoLists.get(0);
	}
	
	@Override
	public List<FlatCostPojo> getFlatCost(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getFlatCost in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_COST)
				.append(" WHERE FLAT_ID = :FLAT_ID ")
				//.append(" AND STATUS_ID NOT IN  (:STATUS_ID) ")
				.append(" AND STATUS_ID IN  (:STATUS_ID) ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_ID", bookingFormRequest.getFlatId(),Types.BIGINT);
        //namedParameters.addValue("STATUS_ID", Arrays.asList(Status.REJECTED.getStatus(),Status.INACTIVE.getStatus()), Types.BIGINT);
        namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.BIGINT);
        //changed query taking only active and pending flat cost details
        logger.info("**** THE QRY_TO_GET_FLAT_COST IS *****"+query);
		
        List<List<FlatCostPojo>> FlatCostPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FlatCostPojo>>() {
					public List<FlatCostPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatCostPojo> resultSetMapper = new ResultSetMapper<FlatCostPojo>();
						List<FlatCostPojo> FlatCostPojoLIST = null;
						try {
							FlatCostPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatCostPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The FlatCostPojoLIST objects  is *****" + FlatCostPojoLIST);		
						return FlatCostPojoLIST;
					}
				});
		
        //logger.info("*** The FlatCostPojoLists is *****"+FlatCostPojoLists);
		if(FlatCostPojoLists.isEmpty()) {
			FlatCostPojoLists.add(new ArrayList<FlatCostPojo>());
		}
		logger.info("**** The FlatCostPojoLists is ****"+FlatCostPojoLists.get(0));
		return FlatCostPojoLists.get(0);
	}
	
	@Override
	public List<FlatDetailsPojo> getFlatDetails(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getFlatDetails in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_DET)
	             .append(" WHERE FLAT_ID = :FLAT_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_ID", bookingFormRequest.getFlatId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_FLAT_DET IS *****"+query);
		
        List<List<FlatDetailsPojo>> FlatDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FlatDetailsPojo>>() {
					public List<FlatDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						//logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatDetailsPojo> resultSetMapper = new ResultSetMapper<FlatDetailsPojo>();
						List<FlatDetailsPojo> FlatDetailsPojoLIST = null;
						try {
							FlatDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The FlatDetailsPojoLIST objects  is *****" + FlatDetailsPojoLIST);		
						return FlatDetailsPojoLIST;
					}
				});
		
        //logger.info("*** The FlatDetailsPojoLists is *****"+FlatDetailsPojoLists);
		if(FlatDetailsPojoLists.isEmpty()) {
			FlatDetailsPojoLists.add(new ArrayList<FlatDetailsPojo>());
		}
		logger.info("**** The FlatDetailsPojoLists is ****"+FlatDetailsPojoLists.get(0));
		return FlatDetailsPojoLists.get(0);
	}
	
	
	@Override
	public List<FloorPojo> getFloor(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getFloor in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLOOR)
	             .append(" WHERE FLOOR_ID = :FLOOR_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLOOR_ID", bookingFormRequest.getFloorId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_FLOOR IS *****"+query);
		
        List<List<FloorPojo>> FloorPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FloorPojo>>() {
					public List<FloorPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FloorPojo> resultSetMapper = new ResultSetMapper<FloorPojo>();
						List<FloorPojo> FloorPojoLIST = null;
						try {
							FloorPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FloorPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The FloorPojoLIST objects  is *****" + FloorPojoLIST);		
						return FloorPojoLIST;
					}
				});
		
        //logger.info("*** The FloorPojoLists is *****"+FloorPojoLists);
		if(FloorPojoLists.isEmpty()) {
			FloorPojoLists.add(new ArrayList<FloorPojo>());
		}
		logger.info("**** The FloorPojoLists is ****"+FloorPojoLists.get(0));
		return FloorPojoLists.get(0);
	}
	
	@Override
	public List<BlockPojo> getBlock(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getBlock in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_BLOCK)
	             .append(" WHERE BLOCK_ID = :BLOCK_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("BLOCK_ID", bookingFormRequest.getBlockId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_BLOCK IS *****"+query);
		
        List<List<BlockPojo>> BlockPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<BlockPojo>>() {
					public List<BlockPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<BlockPojo> resultSetMapper = new ResultSetMapper<BlockPojo>();
						List<BlockPojo> BlockPojoLIST = null;
						try {
							BlockPojoLIST = resultSetMapper.mapRersultSetToObject(rs, BlockPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The BlockPojoLIST objects  is *****" + BlockPojoLIST);		
						return BlockPojoLIST;
					}
				});
		
        //logger.info("*** The BlockPojoLists is *****"+BlockPojoLists);
		if(BlockPojoLists.isEmpty()) {
			BlockPojoLists.add(new ArrayList<BlockPojo>());
		}
		logger.info("**** The BlockPojoLists is ****"+BlockPojoLists.get(0));
		return BlockPojoLists.get(0);
	}
	@Override
	public List<SitePojo> getSite(BookingFormRequest bookingFormRequest) {
		//logger.info("***** The control is inside the getSite in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_SITE)
	             .append(" WHERE SITE_ID = :SITE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(),Types.BIGINT);
        
        logger.info("***** The control is inside the getSite in BookingFormServiceDaoImpl *****"+query);
		
        List<List<SitePojo>> SitePojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<SitePojo>>() {
					public List<SitePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						//logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<SitePojo> resultSetMapper = new ResultSetMapper<SitePojo>();
						List<SitePojo> SitePojoLIST = null;
						try {
							SitePojoLIST = resultSetMapper.mapRersultSetToObject(rs, SitePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						//logger.info("***** The SitePojoLIST objects  is *****" + SitePojoLIST);		
						return SitePojoLIST;
					}
				});
		
        //logger.info("*** The SitePojoLists is *****"+SitePojoLists);
		if(SitePojoLists.isEmpty()) {
			SitePojoLists.add(new ArrayList<SitePojo>());
		}
		//logger.info("**** The SitePojoLists is ****"+SitePojoLists.get(0));
		return SitePojoLists.get(0);
	}
	@Override
	public List<ProfessionalDetailsPojo> getProfessionalDetails(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getSite in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_PROFESSIONAL_DET)
	             .append(" WHERE CUST_PROFFISIONAL_ID = :CUST_PROFFISIONAL_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CUST_PROFFISIONAL_ID", bookingFormRequest.getProffisionalId(), Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_PROFESSIONAL_DET IS *****"+query);
		
        List<List<ProfessionalDetailsPojo>> ProfessionalDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ProfessionalDetailsPojo>>() {
					public List<ProfessionalDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ProfessionalDetailsPojo> resultSetMapper = new ResultSetMapper<ProfessionalDetailsPojo>();
						List<ProfessionalDetailsPojo> ProfessionalDetailsPojoLIST = null;
						try {
							ProfessionalDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ProfessionalDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ProfessionalDetailsPojoLIST objects  is *****" + ProfessionalDetailsPojoLIST);		
						return ProfessionalDetailsPojoLIST;
					}
				});
		
       // logger.info("*** The ProfessionalDetailsPojoLists is *****"+ProfessionalDetailsPojoLists);
		if(ProfessionalDetailsPojoLists.isEmpty()) {
			ProfessionalDetailsPojoLists.add(new ArrayList<ProfessionalDetailsPojo>());
		}
		logger.info("**** The ProfessionalDetailsPojoLists is ****"+ProfessionalDetailsPojoLists.get(0));
		return ProfessionalDetailsPojoLists.get(0);
	}
	
	@Override
	public List<CustomerAddressPojo> getCustomerAddress(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCustomerAddress in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_ADDRESS_DETAILS)
	             .append(" WHERE CUST_ADDR.CUST_ADD_ID = :CUST_ADD_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CUST_ADD_ID", bookingFormRequest.getCustomerAddressId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_ADDRESS_DETAILS IS *****"+query);
		
        List<List<CustomerAddressPojo>> CustomerAddressPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerAddressPojo>>() {
					public List<CustomerAddressPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerAddressPojo> resultSetMapper = new ResultSetMapper<CustomerAddressPojo>();
						List<CustomerAddressPojo> CustomerAddressPojoLIST = null;
						try {
							CustomerAddressPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerAddressPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerAddressPojoLIST objects  is *****" + CustomerAddressPojoLIST);		
						return CustomerAddressPojoLIST;
					}
				});
		
        logger.info("*** The CustomerAddressPojoLists is *****"+CustomerAddressPojoLists);
		if(CustomerAddressPojoLists.isEmpty()) {
			CustomerAddressPojoLists.add(new ArrayList<CustomerAddressPojo>());
		}
		logger.info("**** The CustomerAddressPojoLists is ****"+CustomerAddressPojoLists.get(0));
		return CustomerAddressPojoLists.get(0);
	}
	
	@Override
	public List<CustomerAddressPojo> getApplicantAddress(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getApplicantAddress in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_ADDRESS_DETAILS)
	             .append(" WHERE CUST_ADD_ID = :CUST_ADD_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CUST_ADD_ID", bookingFormRequest.getApplicantAddressId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_ADDRESS_DETAILS IS *****"+query);
		
        List<List<CustomerAddressPojo>> CustomerAddressPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerAddressPojo>>() {
					public List<CustomerAddressPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerAddressPojo> resultSetMapper = new ResultSetMapper<CustomerAddressPojo>();
						List<CustomerAddressPojo> CustomerAddressPojoLIST = null;
						try {
							CustomerAddressPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerAddressPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerAddressPojoLIST objects  is *****" + CustomerAddressPojoLIST);		
						return CustomerAddressPojoLIST;
					}
				});
		
       // logger.info("*** The CustomerAddressPojoLists is *****"+CustomerAddressPojoLists);
		if(CustomerAddressPojoLists.isEmpty()) {
			CustomerAddressPojoLists.add(new ArrayList<CustomerAddressPojo>());
		}
		logger.info("**** The CustomerAddressPojoLists is ****"+CustomerAddressPojoLists.get(0));
		return CustomerAddressPojoLists.get(0);
	}
	
	
	
	@Override
	public List<CustomerOtherDetailspojo> getCustomerOtherDetails(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCustomerAddress in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_OTHER_DETAILS)
	             .append(" WHERE FLAT_BOOK_ID = :FLAT_BOOK_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        //namedParameters.addValue("ID", bookingFormRequest.getCustOtherDetailsId(),Types.BIGINT);
        namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_OTHER_DETAILS IS *****"+query);
		
        List<List<CustomerOtherDetailspojo>> CustomerOtherDetailspojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerOtherDetailspojo>>() {
					public List<CustomerOtherDetailspojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerOtherDetailspojo> resultSetMapper = new ResultSetMapper<CustomerOtherDetailspojo>();
						List<CustomerOtherDetailspojo> CustomerOtherDetailspojoLIST = null;
						try {
							CustomerOtherDetailspojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerOtherDetailspojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerOtherDetailspojoLIST objects  is *****" + CustomerOtherDetailspojoLIST);		
						return CustomerOtherDetailspojoLIST;
					}
				});
		
       // logger.info("*** The CustomerOtherDetailspojoLists is *****"+CustomerOtherDetailspojoLists);
		if(CustomerOtherDetailspojoLists.isEmpty()) {
			CustomerOtherDetailspojoLists.add(new ArrayList<CustomerOtherDetailspojo>());
		}
		logger.info("**** The CustomerOtherDetailspojoLists is ****"+CustomerOtherDetailspojoLists.get(0));
		return CustomerOtherDetailspojoLists.get(0);
	}
	
	@Override
	public List<CheckListPojo> getCheckList(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getBlock in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHECKLIST)
	             .append(" WHERE CHECKLIST_ID = :CHECKLIST_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CHECKLIST_ID", bookingFormRequest.getCheckListId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CHECKLIST IS *****"+query);
		
        List<List<CheckListPojo>> CheckListPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CheckListPojo>>() {
					public List<CheckListPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CheckListPojo> resultSetMapper = new ResultSetMapper<CheckListPojo>();
						List<CheckListPojo> CheckListPojoLIST = null;
						try {
							CheckListPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CheckListPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CheckListPojoLIST objects  is *****" + CheckListPojoLIST);		
						return CheckListPojoLIST;
					}
				});
		
        //logger.info("*** The CheckListPojoLists is *****"+CheckListPojoLists);
		if(CheckListPojoLists.isEmpty()) {
			CheckListPojoLists.add(new ArrayList<CheckListPojo>());
		}
		logger.info("**** The CheckListPojoLists is ****"+CheckListPojoLists.get(0));
		return CheckListPojoLists.get(0);
	}
	
	@Override
	public List<CheckListSalesHeadPojo> getCheckListSalesHead(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCheckListSalesHead in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHECKLIST_SALESHEAD)
	             .append(" WHERE CUSTOMER_ID = :CUSTOMER_ID ")
	             .append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CUSTOMER_ID", bookingFormRequest.getCustomerId(),Types.BIGINT);
        namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CHECKLIST_SALESHEAD IS *****"+query);
		
        List<List<CheckListSalesHeadPojo>> CheckListSalesHeadPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CheckListSalesHeadPojo>>() {
					public List<CheckListSalesHeadPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CheckListSalesHeadPojo> resultSetMapper = new ResultSetMapper<CheckListSalesHeadPojo>();
						List<CheckListSalesHeadPojo> CheckListSalesHeadPojoLIST = null;
						try {
							CheckListSalesHeadPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CheckListSalesHeadPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CheckListSalesHeadPojoLIST objects  is *****" + CheckListSalesHeadPojoLIST);		
						return CheckListSalesHeadPojoLIST;
					}
				});
		
        //logger.info("*** The CheckListSalesHeadPojoLists is *****"+CheckListSalesHeadPojoLists);
		if(CheckListSalesHeadPojoLists.isEmpty()) {
			CheckListSalesHeadPojoLists.add(new ArrayList<CheckListSalesHeadPojo>());
		}
		logger.info("**** The CheckListSalesHeadPojoLists is ****"+CheckListSalesHeadPojoLists.get(0));
		return CheckListSalesHeadPojoLists.get(0);
	}
	@Override
	public List<CheckListLegalOfficerPojo> getCheckListLegalOfficer(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCheckListLegalOfficer in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHECKLIST_LEGAL_OFFICIER)
				.append(" WHERE CUSTOMER_ID = :CUSTOMER_ID ")
	             .append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID ")
	             .toString();
       MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       namedParameters.addValue("CUSTOMER_ID", bookingFormRequest.getCustomerId(),Types.BIGINT);
       namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CHECKLIST_LEGAL_OFFICIER IS *****"+query);
		
        List<List<CheckListLegalOfficerPojo>> CheckListLegalOfficerPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CheckListLegalOfficerPojo>>() {
					public List<CheckListLegalOfficerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CheckListLegalOfficerPojo> resultSetMapper = new ResultSetMapper<CheckListLegalOfficerPojo>();
						List<CheckListLegalOfficerPojo> CheckListLegalOfficerPojoLIST = null;
						try {
							CheckListLegalOfficerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CheckListLegalOfficerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CheckListLegalOfficerPojoLIST objects  is *****" + CheckListLegalOfficerPojoLIST);		
						return CheckListLegalOfficerPojoLIST;
					}
				});
		
       // logger.info("*** The CheckListLegalOfficerPojoLists is *****"+CheckListLegalOfficerPojoLists);
		if(CheckListLegalOfficerPojoLists.isEmpty()) {
			CheckListLegalOfficerPojoLists.add(new ArrayList<CheckListLegalOfficerPojo>());
		}
		logger.info("**** The CheckListLegalOfficerPojoLists is ****"+CheckListLegalOfficerPojoLists.get(0));
		return CheckListLegalOfficerPojoLists.get(0);
	}
	@Override
	public List<CheckListRegistrationPojo> getCheckListRegistration(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCheckListRegistration in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHECKLIST_REGISTRATION)
				.append(" WHERE CUSTOMER_ID = :CUSTOMER_ID ")
	             .append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID ")
	             .toString();
      MapSqlParameterSource namedParameters = new MapSqlParameterSource();
      namedParameters.addValue("CUSTOMER_ID", bookingFormRequest.getCustomerId(),Types.BIGINT);
      namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CHECKLIST_REGISTRATION IS *****"+query);
		
        List<List<CheckListRegistrationPojo>> CheckListRegistrationPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CheckListRegistrationPojo>>() {
					public List<CheckListRegistrationPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CheckListRegistrationPojo> resultSetMapper = new ResultSetMapper<CheckListRegistrationPojo>();
						List<CheckListRegistrationPojo> CheckListRegistrationPojoLIST = null;
						try {
							CheckListRegistrationPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CheckListRegistrationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CheckListRegistrationPojoLIST objects  is *****" + CheckListRegistrationPojoLIST);		
						return CheckListRegistrationPojoLIST;
					}
				});
		
        //logger.info("*** The CheckListRegistrationPojoLists is *****"+CheckListRegistrationPojoLists);
		if(CheckListRegistrationPojoLists.isEmpty()) {
			CheckListRegistrationPojoLists.add(new ArrayList<CheckListRegistrationPojo>());
		}
		logger.info("**** The CheckListRegistrationPojoLists is ****"+CheckListRegistrationPojoLists.get(0));
		return CheckListRegistrationPojoLists.get(0);
	}
	@Override
	public List<ChecklistCrmPojo> getChecklistCrm(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCheckListCRM in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHECKLIST_CRM)
				.append(" WHERE CUSTOMER_ID = :CUSTOMER_ID ")
	             .append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID ")
	             .toString();
      MapSqlParameterSource namedParameters = new MapSqlParameterSource();
      namedParameters.addValue("CUSTOMER_ID", bookingFormRequest.getCustomerId(),Types.BIGINT);
      namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CHECKLIST_CRM IS *****"+query);
		
        List<List<ChecklistCrmPojo>> ChecklistCrmPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ChecklistCrmPojo>>() {
					public List<ChecklistCrmPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ChecklistCrmPojo> resultSetMapper = new ResultSetMapper<ChecklistCrmPojo>();
						List<ChecklistCrmPojo> ChecklistCrmPojoLIST = null;
						try {
							ChecklistCrmPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ChecklistCrmPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ChecklistCrmPojoLIST objects  is *****" + ChecklistCrmPojoLIST);		
						return ChecklistCrmPojoLIST;
					}
				});
		
        //logger.info("*** The ChecklistCrmPojoLists is *****"+ChecklistCrmPojoLists);
		if(ChecklistCrmPojoLists.isEmpty()) {
			ChecklistCrmPojoLists.add(new ArrayList<ChecklistCrmPojo>());
		}
		logger.info("**** The ChecklistCrmPojoLists is ****"+ChecklistCrmPojoLists.get(0));
		return ChecklistCrmPojoLists.get(0);
	}
	
	@Override
	public List<CustChecklistVerificationPojo> getCustChecklistVerification(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCustChecklistVerification in BookingFormServiceDaoImpl *****");
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_CUST_CHECKLIST_VERIFICATION);
		if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())?bookingFormRequest.getRequestUrl().equalsIgnoreCase("insertOrUpdateChecklistUtil"):false) {
			    query.append(" INNER JOIN CHECKLIST_DEPARTMENT_MAPPING CDM ")
			   .append(" ON CDM.CHECKLIST_DEPT_MAP_ID = CCV.CHECKLIST_DEPT_MAP_ID ")
			   .append(" WHERE  CDM.DEPT_ID = :DEPT_ID ")
			   .append(" AND CCV.CUST_ID = :CUST_ID ")
			   .append(" AND CCV.FLAT_BOOK_ID = :FLAT_BOOK_ID  ").toString();
			   namedParameters.addValue("DEPT_ID", bookingFormRequest.getDeptId(),Types.BIGINT);
			   namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(),Types.BIGINT);
		       namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
		}else {
			   query.append(" WHERE CUST_ID = :CUST_ID ")
			  .append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID ")
			  .append(" AND CHECKLIST_DEPT_MAP_ID = :CHECKLIST_DEPT_MAP_ID ")
	          .toString();
       namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(),Types.BIGINT);
       namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
       namedParameters.addValue("CHECKLIST_DEPT_MAP_ID", bookingFormRequest.getCheckListDeptMapId(),Types.BIGINT);
		}
       
       logger.info("**** THE QRY_TO_GET_CUST_CHECKLIST_VERIFICATION IS *****"+query);
		
       List<List<CustChecklistVerificationPojo>> CustChecklistVerificationPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustChecklistVerificationPojo>>() {
					public List<CustChecklistVerificationPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustChecklistVerificationPojo> resultSetMapper = new ResultSetMapper<CustChecklistVerificationPojo>();
						List<CustChecklistVerificationPojo> CustChecklistVerificationPojoLIST = null;
						try {
							CustChecklistVerificationPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustChecklistVerificationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustChecklistVerificationPojoLIST objects  is *****" + CustChecklistVerificationPojoLIST);		
						return CustChecklistVerificationPojoLIST;
					}
				});
		
       //logger.info("*** The CustChecklistVerificationPojoLists is *****"+CustChecklistVerificationPojoLists);
		if(CustChecklistVerificationPojoLists.isEmpty()) {
			CustChecklistVerificationPojoLists.add(new ArrayList<CustChecklistVerificationPojo>());
		}
		logger.info("**** The CustChecklistVerificationPojoLists is ****"+CustChecklistVerificationPojoLists.get(0));
		return CustChecklistVerificationPojoLists.get(0);
	}
	@Override
	public List<CoApplicantCheckListVerificationPojo> getApplicantChecklistVerification(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getApplicantChecklistVerification in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_APPLICANT_CK_LIST_VERIFICATION);
		if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())?bookingFormRequest.getRequestUrl().equalsIgnoreCase("insertOrUpdateCoAppChecklistUtil"):false) {
		    query.append(" INNER JOIN CHECKLIST_DEPARTMENT_MAPPING CDM ")
		   .append(" ON CDM.CHECKLIST_DEPT_MAP_ID = ACLV.CHECK_LIST_DEPT_MAPPING_ID ")
		   .append(" WHERE  CDM.DEPT_ID = :DEPT_ID ")
		   .append(" AND ACLV.APPLICANT_ID = :APPLICANT_ID ")
		   .append(" AND ACLV.FLAT_BOOK_ID = :FLAT_BOOK_ID ").toString();
		   namedParameters.addValue("DEPT_ID", bookingFormRequest.getDeptId(),Types.BIGINT);
		   namedParameters.addValue("APPLICANT_ID", bookingFormRequest.getCoApplicantId(),Types.BIGINT);
	       namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
	}else {
		  query.append(" WHERE APPLICANT_ID = :APPLICANT_ID ")
		  .append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID ")
		  .append(" AND CHECK_LIST_DEPT_MAPPING_ID = :CHECK_LIST_DEPT_MAPPING_ID ")
          .toString();
		  namedParameters.addValue("APPLICANT_ID", bookingFormRequest.getApplicantId(),Types.BIGINT);
	      namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
	      namedParameters.addValue("CHECK_LIST_DEPT_MAPPING_ID", bookingFormRequest.getCheckListDeptMapId(),Types.BIGINT);
	}
        
        logger.info("**** THE QRY_TO_GET_APPLICANT_CK_LIST_VERIFICATION IS *****"+query);
		
        List<List<CoApplicantCheckListVerificationPojo>> CoApplicantCheckListVerificationPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CoApplicantCheckListVerificationPojo>>() {
					public List<CoApplicantCheckListVerificationPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CoApplicantCheckListVerificationPojo> resultSetMapper = new ResultSetMapper<CoApplicantCheckListVerificationPojo>();
						List<CoApplicantCheckListVerificationPojo> CoApplicantCheckListVerificationPojoLIST = null;
						try {
							CoApplicantCheckListVerificationPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CoApplicantCheckListVerificationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CoApplicantCheckListVerificationPojoLIST objects  is *****" + CoApplicantCheckListVerificationPojoLIST);		
						return CoApplicantCheckListVerificationPojoLIST;
					}
				});
		
        //logger.info("*** The CoApplicantCheckListVerificationPojoLists is *****"+CoApplicantCheckListVerificationPojoLists);
		if(CoApplicantCheckListVerificationPojoLists.isEmpty()) {
			CoApplicantCheckListVerificationPojoLists.add(new ArrayList<CoApplicantCheckListVerificationPojo>());
		}
		logger.info("**** The CoApplicantCheckListVerificationPojoLists is ****"+CoApplicantCheckListVerificationPojoLists.get(0));
		return CoApplicantCheckListVerificationPojoLists.get(0);
	}
	@Override
	public List<ChecklistDepartmentMappingPojo> getCheckListDepartmentMapping(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getCheckListDepartmentMapping in BookingFormServiceDaoImpl *****"+bookingFormRequest.getCheckListId());
		logger.info("***** The control is inside the getCheckListDepartmentMapping in BookingFormServiceDaoImpl *****"+bookingFormRequest.getDeptId());
		logger.info("***** The control is inside the getCheckListDepartmentMapping in BookingFormServiceDaoImpl *****"+bookingFormRequest.getMetadataId());
		
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_CHECKLIST_DEPARTMENT_MAPPING);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
       
		if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("noCkId")) {
			querySB.append(" WHERE DEPT_ID = :DEPT_ID ");
			querySB.append(" AND CK_META_TYPE = :CK_META_TYPE ");
			querySB.append(" AND STATUS_ID = :STATUS_ID ");
			namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus() ,Types.BIGINT);
			namedParameters.addValue("DEPT_ID", bookingFormRequest.getDeptId(),Types.BIGINT);
	        namedParameters.addValue("CK_META_TYPE", bookingFormRequest.getMetadataId(),Types.BIGINT);
		} else if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("checklistDepartmentMappringId")) {
			querySB.append(" WHERE CHECKLIST_DEPT_MAP_ID = :CHECKLIST_DEPT_MAP_ID ");
//			querySB.append(" AND CK_META_TYPE = :CK_META_TYPE ");
//			querySB.append(" AND CHECKLIST_ID = :CHECKLIST_ID ");
//			querySB.append(" AND STATUS_ID = :STATUS_ID ");
//			namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus() ,Types.BIGINT);
//			namedParameters.addValue("DEPT_ID", bookingFormRequest.getDeptId(),Types.BIGINT);
//	        namedParameters.addValue("CK_META_TYPE", bookingFormRequest.getMetadataId(),Types.BIGINT);
	        namedParameters.addValue("CHECKLIST_DEPT_MAP_ID", bookingFormRequest.getCheckListDeptMapId(),Types.BIGINT);
		} else {
			querySB.append(" WHERE DEPT_ID = :DEPT_ID ");
			querySB.append(" AND CK_META_TYPE = :CK_META_TYPE ");
			querySB.append(" AND CHECKLIST_ID = :CHECKLIST_ID ");
			querySB.append(" AND STATUS_ID = :STATUS_ID ");
			namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus() ,Types.BIGINT);
			namedParameters.addValue("DEPT_ID", bookingFormRequest.getDeptId(),Types.BIGINT);
	        namedParameters.addValue("CK_META_TYPE", bookingFormRequest.getMetadataId(),Types.BIGINT);
	        namedParameters.addValue("CHECKLIST_ID", bookingFormRequest.getCheckListId(),Types.BIGINT);
		}
		String query = querySB.toString();
        logger.info("**** THE QRY_TO_GET_CHECKLIST_DEPARTMENT_MAPPING IS *****"+query);
		
        List<List<ChecklistDepartmentMappingPojo>> ChecklistDepartmentMappingPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ChecklistDepartmentMappingPojo>>() {
					public List<ChecklistDepartmentMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ChecklistDepartmentMappingPojo> resultSetMapper = new ResultSetMapper<ChecklistDepartmentMappingPojo>();
						List<ChecklistDepartmentMappingPojo> ChecklistDepartmentMappingPojoLIST = null;
						try {
							ChecklistDepartmentMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ChecklistDepartmentMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ChecklistDepartmentMappingPojoLIST objects  is *****" + ChecklistDepartmentMappingPojoLIST);		
						return ChecklistDepartmentMappingPojoLIST;
					}
				});
		
        //logger.info("*** The ChecklistDepartmentMappingPojoLists is *****"+ChecklistDepartmentMappingPojoLists);
		if(ChecklistDepartmentMappingPojoLists.isEmpty()) {
			ChecklistDepartmentMappingPojoLists.add(new ArrayList<ChecklistDepartmentMappingPojo>());
		}
		logger.info("**** The ChecklistDepartmentMappingPojoLists is ****"+ChecklistDepartmentMappingPojoLists.get(0));
		return ChecklistDepartmentMappingPojoLists.get(0);
	}
	
	
	@Override
	public Long saveCustomerDetails(@NonNull CustomerPojo customerPojo) {
		logger.info("*** The control is inside the putCustomerDetails in BookingFormServiceDaoImpl ***"+customerPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_customer,new BeanPropertySqlParameterSource(customerPojo),keyHolder,new String[] { "CUST_ID" });
		logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}	
	
	@Override
	public Long saveCustBookInfo(@NonNull CustBookInfoPojo custBookInfoPojo) {
		logger.info("*** The control is inside the saveCustBookInfo in BookingFormServiceDaoImpl ***"+custBookInfoPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CUST_BOOK_INFO,new BeanPropertySqlParameterSource(custBookInfoPojo),keyHolder,new String[] { "CUST_BOOK_INFO_ID" });
		logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
		
	@Override
	public Long saveAddressDetails(@NonNull AddressPojo addressPojo) {	
      logger.info("*** The control is inside the putAddressDetails in BookingFormServiceDaoImpl ***"+addressPojo);
      addressPojo= formAdreesObject( addressPojo);
      GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
      nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_ADDRESS,new BeanPropertySqlParameterSource(addressPojo),keyHolder,new String[] { "ADDRESS_ID" });
      logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
	  return keyHolder.getKey().longValue();
   }
	
   @Override
	public Long saveAddressmapping(@NonNull AddressMappingPojo addressMappingPojo) {
        logger.info("*** The control is inside the putAddressDetails in BookingFormServiceDaoImpl ***"+addressMappingPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_ADDRESS_MAPPING,new BeanPropertySqlParameterSource(addressMappingPojo),keyHolder,new String[] { "ADDRESS_MAPPING_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
}

	@Override
	public Long saveCustomerProfessionalDetails(@NonNull ProfessionalDetailsPojo professionalDetailsPojo) {
         logger.info("*** The control is inside the customerProfessionalDetails in BookingFormServiceDaoImpl ***"+professionalDetailsPojo);
	     GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CUSTOMER_PROFESSIONAL_DETAILS,new BeanPropertySqlParameterSource(professionalDetailsPojo),keyHolder,new String[] { "CUST_PROFFISIONAL_ID" });
	     logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		 return keyHolder.getKey().longValue();

	}
	
	@Override
	public Long saveReferencesCustomer(@NonNull ReferencesCustomerPojo referencesCustomerPojo) {
		logger.info("**** The control is inside the referencesCustomer in BookingFormServiceDaoImpl ****"+referencesCustomerPojo);
		 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_REFERENCES_CUSTOMER,new BeanPropertySqlParameterSource(referencesCustomerPojo),keyHolder,new String[] { "REFERENCES_CUSTOMER_ID" });
		 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
   	public Long saveReferencesFriend(@NonNull ReferencesFriendPojo referencesFriendPojo) {
				 logger.info("**** The control is inside the referencesFriend in BookingFormServiceDaoImpl ****"+referencesFriendPojo);
				 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
				 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_REFERENCES_FRIEND,new BeanPropertySqlParameterSource(referencesFriendPojo),keyHolder,new String[] { "REFERENCES_FRIEND_ID" });
				 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
				return keyHolder.getKey().longValue();

		}
	
	@Override
	public Long saveReferencesMapping(@NonNull ReferencesMappingPojo referencesMappingPojo) {
		logger.info("**** The control is inside the referencesMapping in BookingFormServiceDaoImpl ****"+referencesMappingPojo);
			 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_REFERENCES_MAPPING,new BeanPropertySqlParameterSource(referencesMappingPojo),keyHolder,new String[] { "REFERENCES_MAPPING_ID" });
			 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
	}
	
	@Override
	public Long saveChannelPartnerMaster(@NonNull ChannelPartnerMasterPojo channelPartnerMasterPojo) {
		     logger.info("**** The control is inside the channelPartnerMaster in BookingFormServiceDaoImpl ****"+channelPartnerMasterPojo);
			 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CHANNEL_PARTNER_MASTER,new BeanPropertySqlParameterSource(channelPartnerMasterPojo),keyHolder,new String[] { "CHANNEL_PARTNER_MASTER_ID" });
			 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
	}
	
	@Override
	public Long saveCustomerOtherDetails(@NonNull CustomerOtherDetailspojo customerOtherDetailspojo) {
		logger.info("**** The control is inside the customerOtherDetails in BookingFormServiceDaoImpl ****"+customerOtherDetailspojo);
		 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CUSTOMER_OTHER_DETAILS,new BeanPropertySqlParameterSource(customerOtherDetailspojo),keyHolder,new String[] { "ID" });
		 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();

	}	
	
	@Override
	public Long savePoaHolder(@NonNull PoaHolderPojo PoaHolderPojo) {
		 logger.info("**** The control is inside the poaHolder in BookingFormServiceDaoImpl ****"+PoaHolderPojo);
		 AddressPojo addressPojo = new AddressPojo();
		 addressPojo.setCity(PoaHolderPojo.getCity());
		 addressPojo.setCityId(PoaHolderPojo.getCityId());
		 addressPojo.setState(PoaHolderPojo.getState());
		 addressPojo.setStateId(PoaHolderPojo.getStateId());
		 addressPojo.setCountryId(PoaHolderPojo.getCountryId());
		 addressPojo.setCountry(PoaHolderPojo.getCountry());
		 addressPojo= formAdreesObject(addressPojo);
		 
		 PoaHolderPojo.setCountryId(addressPojo.getCountryId());
		 PoaHolderPojo.setStateId(addressPojo.getStateId());
		 PoaHolderPojo.setCityId(addressPojo.getCityId());
		 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_POA_HOLDER,new BeanPropertySqlParameterSource(PoaHolderPojo),keyHolder,new String[] { "POA_HOLDER_ID" });
		 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		 return keyHolder.getKey().longValue();
	}
	
	@Override
	public Long saveCustomerApplication(@NonNull CustomerApplicationPojo customerApplicationPojo) {
		 logger.info("**** The control is inside the customerApplication in BookingFormServiceDaoImpl ****"+customerApplicationPojo);
		 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		 nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CUSTOMER_APPLICATION,new BeanPropertySqlParameterSource(customerApplicationPojo),keyHolder,new String[] { "CUST_APP_ID" });
		 logger.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		 return keyHolder.getKey().longValue();
	
	}
	
	@Override
	public Long saveCustomerKycSubmitted(@NonNull CustomerKycSubmittedDocPojo customerKycSubmittedDocPojo) {
	    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+customerKycSubmittedDocPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CUSTOMER_KYC_SUBMITTED_DOC,new BeanPropertySqlParameterSource(customerKycSubmittedDocPojo),keyHolder,new String[] { "SUBMITTED_DOC_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
		
	}

	@Override
	public Long saveApplicant(@NonNull CoApplicantPojo applicantPojo) {
	    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+applicantPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_APPLICANT,new BeanPropertySqlParameterSource(applicantPojo),keyHolder,new String[] { "APPLICANT_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public Long saveCoApplicant(@NonNull Co_ApplicantPojo applicantPojo) {
	    logger.info("*** The control is inside the saveCoApplicant in BookingFormServiceDaoImpl ***"+applicantPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CO_APPLICANT,new BeanPropertySqlParameterSource(applicantPojo),keyHolder,new String[] { "CO_APPLICANT_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long saveFlatDetails(@NonNull FlatDetailsPojo flatDetailsPojo) {
	    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+flatDetailsPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_FLAT_DETAILS,new BeanPropertySqlParameterSource(flatDetailsPojo),keyHolder,new String[] { "FLAT_DET_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long saveFlatCost(@NonNull FlatCostPojo flatCostPojo) {
		    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+flatCostPojo);
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_FLAT_COST,new BeanPropertySqlParameterSource(flatCostPojo),keyHolder,new String[] { "FLAT_COST_ID" });
	         logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
	        return keyHolder.getKey().longValue();
		}

	@Override
	public Long saveAminiteInfraCost(@NonNull AminitiesInfraCostPojo aminitiesInfraCostPojo) {
		    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+aminitiesInfraCostPojo);
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_AMINITITES_INFRA_COST,new BeanPropertySqlParameterSource(aminitiesInfraCostPojo),keyHolder,new String[] { "AMINITITES_INFRA_COST_ID" });
	        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		}
	
	@Override
	public Long saveFlatBooking(@NonNull FlatBookingPojo flatBookingPojo) {
		    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+flatBookingPojo);
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_FLAT_BOOKING,new BeanPropertySqlParameterSource(flatBookingPojo),keyHolder,new String[] { "FLAT_BOOK_ID" });
	        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		}
	
	
	//----------------------------------------------------------------------------------------------------------
	
	@Override
	public int updateFlatDetails(@NonNull FlatBookingInfo flatBookingInfo,Long status,boolean flag) {
		logger.info("***** The control is inside the updateFlatDetails in BookingFormServiceDaoImpl *****");
		String query = "";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(flag) {
		 query = SqlQuery.QRY_TO_UPDATE_FLAT_DETAILS_WRT_FLAT_ID;
		 namedParameters.addValue("FLAT_ID",flatBookingInfo.getFlatId(),Types.BIGINT);
		 namedParameters.addValue("FACING",flatBookingInfo.getFacing(),Types.VARCHAR);
		}else {
		 query = SqlQuery.QRY_TO_UPDATE_FLAT_DETAILS_WRT_FLAT_BOOK_ID;
		 namedParameters.addValue("FLAT_BOOK_ID",flatBookingInfo.getFlatBookingId(),Types.BIGINT);
		}
		namedParameters.addValue("EOI_APPLICABLE",flatBookingInfo.getEoiApplicable(),Types.VARCHAR);
		namedParameters.addValue("EOI_SEQUENCE_NUMBER",flatBookingInfo.getEoiSequenceNumber(),Types.VARCHAR);
		namedParameters.addValue("STATUS_ID",status, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in updateFlatDetails table  *****"+result);
		return result;
	}
	
	@Override
	public Long saveFlatDtlsAmenitsFlatMapping(@NonNull FlatDtlsAmenitsFlatMappingPojo flatDtlsAmenitsFlatMappingPojo) {
            logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+flatDtlsAmenitsFlatMappingPojo);
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_FLAT_BOOKING,new BeanPropertySqlParameterSource(flatDtlsAmenitsFlatMappingPojo),keyHolder,new String[] { "FLAT_DTLS_AMENITS_FLAT_MAPP_ID" });
	        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		}

	@Override
	public Long saveCoApplicationCheckListVerification(@NonNull CoApplicantCheckListVerificationPojo applicantCheckListVerificationPojo) {
		logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+applicantCheckListVerificationPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_APPLICANT_CK_LIST_VERIFICATION,new BeanPropertySqlParameterSource(applicantCheckListVerificationPojo),keyHolder,new String[] { "APPLICANTCHECKLISTVERFIID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	
	}
	
	@Override
	public int updateCoApplicantChecklistVerification(@NonNull BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the updateCoApplicantChecklistVerification in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_APPLICANT_CK_LIST_VERIFICATION;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("APPLICANTCHECKLISTVERFIID", bookingFormRequest.getCheckListVerfiId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in APPLICANT_CK_VERIFICATION table  *****"+result);
		return result;
	}

	@Override
	public Long saveCheckListSalesHead(@NonNull CheckListSalesHeadPojo checkListSalesHeadPojo) {
	    logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+checkListSalesHeadPojo);
     	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CHECKLIST_SALESHEAD,new BeanPropertySqlParameterSource(checkListSalesHeadPojo),keyHolder,new String[] { "CHECKLIST_SALESHEAD_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	
	}

	@Override
	public Long saveChecklistCrm(@NonNull ChecklistCrmPojo checklistCrmPojo) {
        logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+checklistCrmPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CHECKLIST_CRM,new BeanPropertySqlParameterSource(checklistCrmPojo),keyHolder,new String[] { "CHECKLIST_CRM_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long saveCheckListLegalOfficer(@NonNull CheckListLegalOfficerPojo checkListLegalOfficerPojo) {
	   logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+checkListLegalOfficerPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CHECKLIST_LEGAL_OFFICIER,new BeanPropertySqlParameterSource(checkListLegalOfficerPojo),keyHolder,new String[] { "CHECKLIST_LEGAL_OFFICIER_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	@Override
	public int updateCheckList(String query) {
		logger.info("***** The control is inside the updateCheckListLegalOfficer in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in CHECKLIST_LEGAL_OFFICER table  *****"+result);
		return result;
	}
	
	@Override
	public Long saveCheckListRegistration(@NonNull CheckListRegistrationPojo checkListRegistrationPojo) {
		logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+checkListRegistrationPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CHECKLIST_REGISTRATION,new BeanPropertySqlParameterSource(checkListRegistrationPojo),keyHolder,new String[] { "CHECKLIST_REGISTRATION_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	
	}
	
	@Override
	public Long saveCustChecklistVerification(CustChecklistVerificationPojo custChecklistVerificationPojo) {
		logger.info("*** The control is inside the CustomerKycSubmitted in BookingFormServiceDaoImpl ***"+custChecklistVerificationPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CUST_CHECKLIST_VERIFICATION,new BeanPropertySqlParameterSource(custChecklistVerificationPojo),keyHolder,new String[] { "CUST_CHECK_VERI_ID" });
        logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public int updateCustChecklistVerification(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the updateCustChecklistVerification in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CUST_CHECKLIST_VERIFICATION;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("CUST_CHECK_VERI_ID", bookingFormRequest.getCheckListVerfiId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in CUST_CHECKLIST_VERIFICATION table  *****"+result);
		return result;
	}
	
	@Override
	public List<CustomerPojo> getCustomerDetailsByPanCardorPassport(String panCard, String passport,Status status) {
		logger.info("***** The control is inside the getCustomerDetailsByPanCard in BookingFormServiceDaoImpl *****");
		/*List<Long> listOfBookingStatus = Arrays.asList(Status.CANCEL.getStatus() ,Status.SWAP.getStatus(),
				Status.AVAILABLE.getStatus(),Status.BLOCKED.getStatus(),Status.NOT_OPEN.getStatus(),Status.PRICE_UPDATE.getStatus(),
				Status.LEGAL_CASE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus(),Status.RETAINED.getStatus()
				,Status.CANCELLED_NOT_REFUNDED.getStatus()
				);*/
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			
		if(panCard!=null && !panCard.equals("")) {
			querySB.append(" WHERE CUST.PANCARD = :PANCARD AND ");
			namedParameters.addValue("PANCARD", panCard);
		}
		else if(passport!=null && !passport.equals("")) {
			querySB.append(" WHERE CUST.PASSPORT = :PASSPORT AND ");
			namedParameters.addValue("PASSPORT", passport);
		}
		else {
			querySB.append(" WHERE ");
		}
		if(Util.isNotEmptyObject(status) && status.getStatus().equals(Status.ACTIVE.getStatus())) {
			querySB.append(" CUST.STATUS_ID = :A_STATUS_ID ");
			//querySB.append(" CUST.STATUS_ID = :P_STATUS_ID ");
			//namedParameters.addValue("A_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("A_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}else{
			querySB.append(" CUST.STATUS_ID <> :R_STATUS_ID ");
			querySB.append(" AND CUST.STATUS_ID <> :I_STATUS_ID ");
			namedParameters.addValue("R_STATUS_ID", Status.REJECTED.getStatus(), Types.BIGINT);
			namedParameters.addValue("I_STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		}
        
        String query = querySB.toString();
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_DETAILS IS *****"+query);
		
        List<List<CustomerPojo>> CustomerPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerPojo>>() {
					public List<CustomerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPojo> resultSetMapper = new ResultSetMapper<CustomerPojo>();
						List<CustomerPojo> CustomerPojoLIST = null;
						try {
							CustomerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerPojoLists objects  is *****" + CustomerPojoLIST);		
						return CustomerPojoLIST;
					}
				});
		
        //logger.info("*** The CustomerPojoLists is *****"+CustomerPojoLists);
		if(CustomerPojoLists.isEmpty()) {
			CustomerPojoLists.add(new ArrayList<CustomerPojo>());
		}
		logger.info("**** The CustomerPojoLists is ****"+CustomerPojoLists.get(0));
		return CustomerPojoLists.get(0);
	}

	@Override
	public List<EmployeePojo> getEmployeeDetails(String empName, Long empId, Department ... dept) {
		logger.info("***** The control is inside the getEmployeeDetails in BookingFormServiceDaoImpl *****");
		
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_EMP_JOIN_EMP_DET);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		List<Long> deptIds = new ArrayList<Long>();
		/* for sales force sake */
		if(Util.isNotEmptyObject(empName)) {
		  //querySB.append(" WHERE TRIM(CONCAT(CONCAT(EMP.FIRST_NAME,' '),EMP.LAST_NAME)) = :EMP_NAME ");
			querySB.append(" WHERE EMP.SALESFORCE_EMP_NAME = :EMP_NAME ");
			namedParameters.addValue("EMP_NAME", empName);
			if(Util.isNotEmptyObject(dept) && dept.length>0) {
				querySB.append(" AND ED.DEPT_ID IN (:DEPT_ID) ");
				for(Department obj : dept) {
				  deptIds.add(obj.getId());
				}
				namedParameters.addValue("DEPT_ID",deptIds, Types.INTEGER);
			}
		}
		/*  for customerapp */
		if(empId!=null){
			querySB.append(" WHERE EMP.EMP_ID = :EMP_ID ");
			namedParameters.addValue("EMP_ID", empId, Types.BIGINT);
			if(Util.isNotEmptyObject(dept) && dept.length>0) {
				querySB.append(" AND ED.DEPT_ID IN (:DEPT_ID) ");
				for(Department obj : dept) {
				  deptIds.add(obj.getId());
				}
				namedParameters.addValue("DEPT_ID", deptIds, Types.INTEGER);
			}
		}
        String query = querySB.toString();
        logger.info("**** THE QRY_TO_GET_EMP_JOIN_EMP_DET IS *****"+query);
		
        List<List<EmployeePojo>> EmployeePojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<EmployeePojo>>() {
					public List<EmployeePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeePojo> resultSetMapper = new ResultSetMapper<EmployeePojo>();
						List<EmployeePojo> EmployeePojoLIST = null;
						try {
							EmployeePojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The EmployeePojoLists objects  is *****" + EmployeePojoLIST);		
						return EmployeePojoLIST;
					}
				});
		
        //logger.info("*** The EmployeePojoLists is *****"+EmployeePojoLists);
		if(EmployeePojoLists.isEmpty()) {
			EmployeePojoLists.add(new ArrayList<EmployeePojo>());
		}
		logger.info("**** The EmployeePojoLists is ****"+EmployeePojoLists.get(0));
		return EmployeePojoLists.get(0);
	}
	
	@Override
	public List<EmployeeDetailsPojo> getEmployeeDetails(Status status) {
		logger.info("***** The control is inside the getEmployeeDetails in BookingFormServiceDaoImpl *****");
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_EMPLOYEE_JOIN_EMPLOYEE_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID",status.getStatus(), Types.INTEGER);
	
        String query = querySB.toString();
        logger.info("**** THE QRY_TO_GET_EMP_JOIN_EMP_DET IS *****"+query);
		
        List<List<EmployeeDetailsPojo>> EmployeeDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<EmployeeDetailsPojo>>() {
					public List<EmployeeDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<EmployeeDetailsPojo> resultSetMapper = new ResultSetMapper<EmployeeDetailsPojo>();
						List<EmployeeDetailsPojo> EmployeeDetailsPojoLIST = null;
						try {
							EmployeeDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, EmployeeDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The EmployeePojoLists objects  is *****" + EmployeeDetailsPojoLIST);		
						return EmployeeDetailsPojoLIST;
					}
				});
		
        //logger.info("*** The EmployeePojoLists is *****"+EmployeeDetailsPojoLists);
		if(EmployeeDetailsPojoLists.isEmpty()) {
			EmployeeDetailsPojoLists.add(new ArrayList<EmployeeDetailsPojo>());
		}
		logger.info("**** The EmployeePojoLists is ****"+EmployeeDetailsPojoLists.get(0));
		return EmployeeDetailsPojoLists.get(0);
	}
	
	@Override
	public List<TdsAuthorizationMasterPojo> getTdsAuthorizationMaster(String optionType) {
		logger.info("***** The control is inside the getTdsAuthorizationMaster in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_TDS_AUTHORIZATION_MASTER)
				.append(" WHERE OPTION_TYPE = :OPTION_TYPE ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("OPTION_TYPE", optionType);
        
        logger.info("**** THE QRY_TO_GET_TDS_AUTHORIZATION_MASTER IS *****"+query);
		
        List<List<TdsAuthorizationMasterPojo>> TdsAuthorizationMasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<TdsAuthorizationMasterPojo>>() {
					public List<TdsAuthorizationMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<TdsAuthorizationMasterPojo> resultSetMapper = new ResultSetMapper<TdsAuthorizationMasterPojo>();
						List<TdsAuthorizationMasterPojo> TdsAuthorizationMasterPojoLIST = null;
						try {
							TdsAuthorizationMasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, TdsAuthorizationMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The TdsAuthorizationMasterPojoLists objects  is *****" + TdsAuthorizationMasterPojoLIST);		
						return TdsAuthorizationMasterPojoLIST;
					}
				});
		
        logger.info("*** The TdsAuthorizationMasterPojoLists is *****"+TdsAuthorizationMasterPojoLists);
		if(TdsAuthorizationMasterPojoLists.isEmpty()) {
			TdsAuthorizationMasterPojoLists.add(new ArrayList<TdsAuthorizationMasterPojo>());
		}
		logger.info("**** The TdsAuthorizationMasterPojoLists is ****"+TdsAuthorizationMasterPojoLists.get(0));
		return TdsAuthorizationMasterPojoLists.get(0);
	}

	@Override
	public List<CustomerPojo> getCustomerDetailsByName(String custName,String projectName,String unitNo) {
		logger.info("***** The control is inside the getCustomerDetailsByName in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS_BY_NAME)
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("SALES_FORCE_SITE_NAME", Arrays.asList(projectName));
        namedParameters.addValue("FLAT_NO", Arrays.asList(unitNo));
        namedParameters.addValue("FIRST_NAME", Arrays.asList(custName));
        namedParameters.addValue("STATUS_ID", Arrays.asList(Status.PENDING.getStatus(),Status.ACTIVE.getStatus()));
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_DETAILS IS *****"+query);
		
        List<List<CustomerPojo>> CustomerPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerPojo>>() {
					public List<CustomerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPojo> resultSetMapper = new ResultSetMapper<CustomerPojo>();
						List<CustomerPojo> CustomerPojoLIST = null;
						try {
							CustomerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerPojoLists objects  is *****" + CustomerPojoLIST);		
						return CustomerPojoLIST;
					}
				});
		
        logger.info("*** The CustomerPojoLists is *****"+CustomerPojoLists);
		if(CustomerPojoLists.isEmpty()) {
			CustomerPojoLists.add(new ArrayList<CustomerPojo>());
		}
		logger.info("**** The CustomerPojoLists is ****"+CustomerPojoLists.get(0));
		return CustomerPojoLists.get(0);
	}

	@Override
    public List<Co_ApplicantPojo> getCo_ApplicantByPanCard(String panCard,String passport,Status status) {
		logger.info("***** The control is inside the getApplicantsByPanCard in BookingFormServiceDaoImpl *****");
		StringBuilder query = new StringBuilder(SqlQuery.QUERY_TO_GET_CO_APPLICANT);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		if(Util.isNotEmptyObject(panCard)){
			query.append(" WHERE  PANCARD = :PANCARD AND ");
			namedParameters.addValue("PANCARD", panCard);
		}else if(Util.isNotEmptyObject(passport)) {
			query.append(" WHERE PASSPORT = :PASSPORT AND ");
			namedParameters.addValue("PASSPORT", passport);
		}else {
			query.append(" WHERE ");
		}
		
		if(Util.isNotEmptyObject(status) && status.getStatus().equals(Status.ACTIVE.getStatus())) {
			query.append("  STATUS_ID = :A_STATUS_ID ");
			namedParameters.addValue("A_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}else {
		query.append("  STATUS_ID <> :R_STATUS_ID ");
		query.append(" AND STATUS_ID <> :I_STATUS_ID ");
	    namedParameters.addValue("R_STATUS_ID", Status.REJECTED.getStatus(), Types.BIGINT);
        namedParameters.addValue("I_STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		}
		
        logger.info("**** THE QUERY_TO_GET_CO_APPLICANT IS *****"+query);
		
        List<List<Co_ApplicantPojo>> Co_ApplicantPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<Co_ApplicantPojo>>() {
					public List<Co_ApplicantPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<Co_ApplicantPojo> resultSetMapper = new ResultSetMapper<Co_ApplicantPojo>();
						List<Co_ApplicantPojo> Co_ApplicantPojoLIST = null;
						try {
							Co_ApplicantPojoLIST = resultSetMapper.mapRersultSetToObject(rs, Co_ApplicantPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The Co_ApplicantPojoLists objects  is *****" + Co_ApplicantPojoLIST);		
						return Co_ApplicantPojoLIST;
					}
				});
		
        logger.info("*** The Co_ApplicantPojoLists is *****"+Co_ApplicantPojoLists);
		if(Co_ApplicantPojoLists.isEmpty()) {
			Co_ApplicantPojoLists.add(new ArrayList<Co_ApplicantPojo>());
		}
		logger.info("**** The Co_ApplicantPojoLists is ****"+Co_ApplicantPojoLists.get(0));
		return Co_ApplicantPojoLists.get(0);
	}

	@Override
	public List<FlatPojo> getFlatByName(String flatName) {
		logger.info("***** The control is inside the getFlatByName in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_DETAILS)
	             .append(" WHERE F.FLAT_NO = :FLAT_NO ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_NO", flatName);
        
        logger.info("**** THE QRY_TO_GET_FLAT_DETAILS IS *****"+query);
		
        List<List<FlatPojo>> FlatPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FlatPojo>>() {
					public List<FlatPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatPojo> resultSetMapper = new ResultSetMapper<FlatPojo>();
						List<FlatPojo> FlatPojoLIST = null;
						try {
							FlatPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The FlatPojoLists objects  is *****" + FlatPojoLIST);		
						return FlatPojoLIST;
					}
				});
		
        logger.info("*** The FlatPojoLists is *****"+FlatPojoLists);
		if(FlatPojoLists.isEmpty()) {
			FlatPojoLists.add(new ArrayList<FlatPojo>());
		}
		logger.info("**** The FlatPojoLists is ****"+FlatPojoLists.get(0));
		return FlatPojoLists.get(0);
	}

	@Override
	public List<BlockPojo> getBlockByName(String blockName) {
		logger.info("***** The control is inside the getBlockByName in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_BLOCK)
	             .append(" WHERE NAME = :NAME ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("NAME", blockName);
        
        logger.info("**** THE QRY_TO_GET_BLOCK IS *****"+query);
		
        List<List<BlockPojo>> BlockPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<BlockPojo>>() {
					public List<BlockPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<BlockPojo> resultSetMapper = new ResultSetMapper<BlockPojo>();
						List<BlockPojo> BlockPojoLIST = null;
						try {
							BlockPojoLIST = resultSetMapper.mapRersultSetToObject(rs, BlockPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The BlockPojoLists objects  is *****" + BlockPojoLIST);		
						return BlockPojoLIST;
					}
				});
		
        logger.info("*** The BlockPojoLists is *****"+BlockPojoLists);
		if(BlockPojoLists.isEmpty()) {
			BlockPojoLists.add(new ArrayList<BlockPojo>());
		}
		logger.info("**** The BlockPojoLists is ****"+BlockPojoLists.get(0));
		return BlockPojoLists.get(0);
	}

	@Override
	public List<SitePojo> getSiteByName(String siteName) {
		logger.info("***** The control is inside the getSiteByName in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_SITE)
	             .append(" WHERE NAME = :NAME ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("NAME", siteName);
        
        logger.info("**** THE QRY_TO_GET_SITE IS *****"+query);
		
        List<List<SitePojo>> SitePojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<SitePojo>>() {
					public List<SitePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<SitePojo> resultSetMapper = new ResultSetMapper<SitePojo>();
						List<SitePojo> SitePojoLIST = null;
						try {
							SitePojoLIST = resultSetMapper.mapRersultSetToObject(rs, SitePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The SitePojoLists objects  is *****" + SitePojoLIST);		
						return SitePojoLIST;
					}
				});
		
        logger.info("*** The SitePojoLists is *****"+SitePojoLists);
		if(SitePojoLists.isEmpty()) {
			SitePojoLists.add(new ArrayList<SitePojo>());
		}
		logger.info("**** The SitePojoLists is ****"+SitePojoLists.get(0));
		return SitePojoLists.get(0);
	}

	@Override
	public List<AminitiesInfraMasterPojo> getAminitiesInfraMaster(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getAminitiesInfraMasterByName in BookingFormServiceDaoImpl *****");
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_AMINITITES_INFRA_MASTER);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("saveBookingDetails")) {
			querySB.append(" WHERE AMINITITES_INFRA_NAME = :AMINITITES_INFRA_NAME ");
			namedParameters.addValue("AMINITITES_INFRA_NAME", bookingFormRequest.getAminititesInfraName().toUpperCase());
		}
		else {
			querySB.append(" WHERE AMINITITES_INFRA_ID = :AMINITITES_INFRA_ID ");
			namedParameters.addValue("AMINITITES_INFRA_ID", bookingFormRequest.getAminititesInfraId());
		}
		String query = querySB.toString();
		
        logger.info("**** THE QRY_TO_GET_AMINITITES_INFRA_MASTER IS *****"+query);
		
        List<List<AminitiesInfraMasterPojo>> AminitiesInfraMasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<AminitiesInfraMasterPojo>>() {
					public List<AminitiesInfraMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AminitiesInfraMasterPojo> resultSetMapper = new ResultSetMapper<AminitiesInfraMasterPojo>();
						List<AminitiesInfraMasterPojo> AminitiesInfraMasterPojoLIST = null;
						try {
							AminitiesInfraMasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, AminitiesInfraMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The AminitiesInfraMasterPojoLists objects  is *****" + AminitiesInfraMasterPojoLIST);		
						return AminitiesInfraMasterPojoLIST;
					}
				});
		
        logger.info("*** The AminitiesInfraMasterPojoLists is *****"+AminitiesInfraMasterPojoLists);
		if(AminitiesInfraMasterPojoLists.isEmpty()) {
			AminitiesInfraMasterPojoLists.add(new ArrayList<AminitiesInfraMasterPojo>());
		}
		logger.info("**** The AminitiesInfraMasterPojoLists is ****"+AminitiesInfraMasterPojoLists.get(0));
		return AminitiesInfraMasterPojoLists.get(0);
	}

	@Override
	public List<AminitiesInfraSiteWisePojo> getAminitiesInfraSiteWise(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getAminitiesInfraSiteWise in BookingFormServiceDaoImpl *****");
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_AMINITITES_INFRA_SITE_WISE);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("saveBookingDetails")) {
			querySB.append(" WHERE SITE_ID = :SITE_ID ");
			querySB.append(" AND AMINITITES_INFRA_ID = :AMINITITES_INFRA_ID ");
			namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(), Types.BIGINT);
			namedParameters.addValue("AMINITITES_INFRA_ID", bookingFormRequest.getAminititesInfraId(), Types.BIGINT);
		}
		else {
			querySB.append(" WHERE AMINITITES_INFRA_SITE_WISE_ID = :AMINITITES_INFRA_SITE_WISE_ID ");
			namedParameters.addValue("AMINITITES_INFRA_SITE_WISE_ID", bookingFormRequest.getAminititesInfraSiteWiseId(), Types.BIGINT);
		}
		String query = querySB.toString();
        
        logger.info("**** THE QRY_TO_GET_AMINITITES_INFRA_SITE_WISE IS *****"+query);
		
        List<List<AminitiesInfraSiteWisePojo>> AminitiesInfraSiteWisePojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<AminitiesInfraSiteWisePojo>>() {
					public List<AminitiesInfraSiteWisePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AminitiesInfraSiteWisePojo> resultSetMapper = new ResultSetMapper<AminitiesInfraSiteWisePojo>();
						List<AminitiesInfraSiteWisePojo> AminitiesInfraSiteWisePojoLIST = null;
						try {
							AminitiesInfraSiteWisePojoLIST = resultSetMapper.mapRersultSetToObject(rs, AminitiesInfraSiteWisePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The AminitiesInfraSiteWisePojoLists objects  is *****" + AminitiesInfraSiteWisePojoLIST);		
						return AminitiesInfraSiteWisePojoLIST;
					}
				});
		
        logger.info("*** The AminitiesInfraSiteWisePojoLists is *****"+AminitiesInfraSiteWisePojoLists);
		if(AminitiesInfraSiteWisePojoLists.isEmpty()) {
			AminitiesInfraSiteWisePojoLists.add(new ArrayList<AminitiesInfraSiteWisePojo>());
		}
		logger.info("**** The AminitiesInfraSiteWisePojoLists is ****"+AminitiesInfraSiteWisePojoLists.get(0));
		return AminitiesInfraSiteWisePojoLists.get(0);
	}

	@Override
	public List<ChannelPartnerMasterPojo> getChannelPartnerMasterByIdName(String cpId, String cpCompanyName) {
		logger.info("***** The control is inside the getChannelPartnerMaster in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHANNEL_PARTNER_MASTER)
				.append(" WHERE CP_ID = :CP_ID ")
				.append(" AND LOWER(CP_COMPANY) = :CP_COMPANY ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CP_ID", cpId, Types.VARCHAR);
        namedParameters.addValue("CP_COMPANY", cpCompanyName.toLowerCase());
        
        logger.info("**** THE QRY_TO_GET_CHANNEL_PARTNER_MASTER IS *****"+query);
		
        List<List<ChannelPartnerMasterPojo>> ChannelPartnerMasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ChannelPartnerMasterPojo>>() {
					public List<ChannelPartnerMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ChannelPartnerMasterPojo> resultSetMapper = new ResultSetMapper<ChannelPartnerMasterPojo>();
						List<ChannelPartnerMasterPojo> ChannelPartnerMasterPojoLIST = null;
						try {
							ChannelPartnerMasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ChannelPartnerMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ChannelPartnerMasterPojoLists objects  is *****" + ChannelPartnerMasterPojoLIST);		
						return ChannelPartnerMasterPojoLIST;
					}
				});
		
        logger.info("*** The ChannelPartnerMasterPojoLists is *****"+ChannelPartnerMasterPojoLists);
		if(ChannelPartnerMasterPojoLists.isEmpty()) {
			ChannelPartnerMasterPojoLists.add(new ArrayList<ChannelPartnerMasterPojo>());
		}
		logger.info("**** The ChannelPartnerMasterPojoLists is ****"+ChannelPartnerMasterPojoLists.get(0));
		return ChannelPartnerMasterPojoLists.get(0);
	}

	@Override
	public List<PoaHolderPojo> getPoaHolder(Long custOtherDetailsId, Object object, String condition) {
		//logger.info("***** The control is inside the getPoaHolder in BookingFormServiceDaoImpl *****");
		String query = null;
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	       
		if(condition==null) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_POA_HOLDER)
					 .append(" WHERE CUSTOMER_OTHER_DETAILS_ID = :CUSTOMER_OTHER_DETAILS_ID ")
		             .toString();
		} else if (condition != null) {
			if(object!=null) {
				BookingFormRequest bookingFormRequestCopy = (BookingFormRequest) object;
				query = new StringBuilder(SqlQuery.QRY_TO_GET_POA_HOLDER)
						 .append(" WHERE CUSTOMER_OTHER_DETAILS_ID = (SELECT ID FROM CUSTOMER_OTHER_DETAILS  where FLAT_BOOK_ID = :FLAT_BOOK_ID FETCH FIRST ROW ONLY) ")
			             .toString();
				namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequestCopy.getFlatBookingId(), Types.BIGINT);
			} else {
				return new ArrayList<PoaHolderPojo>();
			}
		}
		//logger.info("2.0*****");
        namedParameters.addValue("CUSTOMER_OTHER_DETAILS_ID", custOtherDetailsId, Types.BIGINT);
        
        logger.info("**** the getPoaHolder QRY_TO_GET_POA_HOLDER IS *****"+query);
        //logger.info("2.1*****");
        List<List<PoaHolderPojo>> PoaHolderPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<PoaHolderPojo>>() {
					public List<PoaHolderPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<PoaHolderPojo> resultSetMapper = new ResultSetMapper<PoaHolderPojo>();
						List<PoaHolderPojo> PoaHolderPojoLIST = null;
						try {
							//logger.info("2.2*****");
							PoaHolderPojoLIST = resultSetMapper.mapRersultSetToObject(rs, PoaHolderPojo.class);
							//logger.info("2.3*****");
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The PoaHolderPojoLists objects  is *****" + PoaHolderPojoLIST);		
						return PoaHolderPojoLIST;
					}
				});
		
        //logger.info("*** The PoaHolderPojoLists is *****"+PoaHolderPojoLists);
		if(PoaHolderPojoLists.isEmpty()) {
			PoaHolderPojoLists.add(new ArrayList<PoaHolderPojo>());
		}
		logger.info("**** The PoaHolderPojoLists is ****"+PoaHolderPojoLists);
		return PoaHolderPojoLists.get(0);
	}

	@Override
	public List<ReferencesMappingPojo> getReferencesMapping(Long custOtherDetailsId) {
		logger.info("***** The control is inside the getReferencesMapping in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_REFERENCES_MAPPING)
				 .append(" WHERE CUST_OTHER_ID = :CUST_OTHER_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CUST_OTHER_ID", custOtherDetailsId, Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_REFERENCES_MAPPING IS *****"+query);
		
        List<List<ReferencesMappingPojo>> ReferencesMappingPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ReferencesMappingPojo>>() {
					public List<ReferencesMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ReferencesMappingPojo> resultSetMapper = new ResultSetMapper<ReferencesMappingPojo>();
						List<ReferencesMappingPojo> ReferencesMappingPojoLIST = null;
						try {
							ReferencesMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ReferencesMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ReferencesMappingPojoLists objects  is *****" + ReferencesMappingPojoLIST);		
						return ReferencesMappingPojoLIST;
					}
				});
		
        logger.info("*** The ReferencesMappingPojoLists is *****"+ReferencesMappingPojoLists);
		if(ReferencesMappingPojoLists.isEmpty()) {
			ReferencesMappingPojoLists.add(new ArrayList<ReferencesMappingPojo>());
		}
		logger.info("**** The ReferencesMappingPojoLists is ****"+ReferencesMappingPojoLists.get(0));
		return ReferencesMappingPojoLists.get(0);
	}

	@Override
	public List<ReferencesCustomerPojo> getReferencesCustomer(Long typeId) {
		logger.info("***** The control is inside the getReferencesCustomer in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_REFERENCES_CUSTOMER)
		.append(" WHERE RC.CUSTOMER_ID = (SELECT CUSTOMER_ID FROM REFERENCES_CUSTOMER WHERE REFERENCES_CUSTOMER_ID = :REFERENCES_CUSTOMER_ID) ")
	    .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("REFERENCES_CUSTOMER_ID", typeId, Types.BIGINT);
        namedParameters.addValue("STATUS_ID", Arrays.asList(Status.PENDING.getStatus(),Status.ACTIVE.getStatus()), Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_REFERENCES_CUSTOMER IS *****"+query);
		
        List<List<ReferencesCustomerPojo>> ReferencesCustomerPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ReferencesCustomerPojo>>() {
					public List<ReferencesCustomerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ReferencesCustomerPojo> resultSetMapper = new ResultSetMapper<ReferencesCustomerPojo>();
						List<ReferencesCustomerPojo> ReferencesCustomerPojoLIST = null;
						try {
							ReferencesCustomerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ReferencesCustomerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ReferencesCustomerPojoLists objects  is *****" + ReferencesCustomerPojoLIST);		
						return ReferencesCustomerPojoLIST;
					}
				});
		
        logger.info("*** The ReferencesCustomerPojoLists is *****"+ReferencesCustomerPojoLists);
		if(ReferencesCustomerPojoLists.isEmpty()) {
			ReferencesCustomerPojoLists.add(new ArrayList<ReferencesCustomerPojo>());
		}
		logger.info("**** The ReferencesCustomerPojoLists is ****"+ReferencesCustomerPojoLists.get(0));
		return ReferencesCustomerPojoLists.get(0);
	}

	@Override
	public List<ReferencesFriendPojo> getReferencesFriend(Long typeId) {
		logger.info("***** The control is inside the getReferencesMapping in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_REFERENCES_FRIEND)
				 .append(" WHERE REFERENCES_FRIEND_ID = :REFERENCES_FRIEND_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("REFERENCES_FRIEND_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE REFERENCES_FRIEND_ID IS *****"+query);
		
        List<List<ReferencesFriendPojo>> ReferencesFriendPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ReferencesFriendPojo>>() {
					public List<ReferencesFriendPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ReferencesFriendPojo> resultSetMapper = new ResultSetMapper<ReferencesFriendPojo>();
						List<ReferencesFriendPojo> ReferencesFriendPojoLIST = null;
						try {
							ReferencesFriendPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ReferencesFriendPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ReferencesFriendPojoLists objects  is *****" + ReferencesFriendPojoLIST);		
						return ReferencesFriendPojoLIST;
					}
				});
		
        logger.info("*** The ReferencesFriendPojoLists is *****"+ReferencesFriendPojoLists);
		if(ReferencesFriendPojoLists.isEmpty()) {
			ReferencesFriendPojoLists.add(new ArrayList<ReferencesFriendPojo>());
		}
		logger.info("**** The ReferencesFriendPojoLists is ****"+ReferencesFriendPojoLists.get(0));
		return ReferencesFriendPojoLists.get(0);
	}

	@Override
	public List<ReferenceMasterPojo> getReferenceMaster(Long typeId) {
		logger.info("***** The control is inside the getReferenceMaster in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_REFERENCE_MASTER)
				 .append(" WHERE REFERENCE_ID = :REFERENCE_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("REFERENCE_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_REFERENCE_MASTER IS *****"+query);
		
        List<List<ReferenceMasterPojo>> ReferenceMasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ReferenceMasterPojo>>() {
					public List<ReferenceMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ReferenceMasterPojo> resultSetMapper = new ResultSetMapper<ReferenceMasterPojo>();
						List<ReferenceMasterPojo> ReferenceMasterPojoLIST = null;
						try {
							ReferenceMasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ReferenceMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ReferenceMasterPojoLists objects  is *****" + ReferenceMasterPojoLIST);		
						return ReferenceMasterPojoLIST;
					}
				});
		
        logger.info("*** The ReferenceMasterPojoLists is *****"+ReferenceMasterPojoLists);
		if(ReferenceMasterPojoLists.isEmpty()) {
			ReferenceMasterPojoLists.add(new ArrayList<ReferenceMasterPojo>());
		}
		logger.info("**** The ReferenceMasterPojoLists is ****"+ReferenceMasterPojoLists.get(0));
		return ReferenceMasterPojoLists.get(0);
	}

	@Override
	public List<ChannelPartnerMasterPojo> getChannelPartnerMaster(Long typeId) {
		logger.info("***** The control is inside the getChannelPartnerMaster in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CHANNEL_PARTNER_MASTER)
				.append(" WHERE CHANNEL_PARTNER_MASTER_ID = :CHANNEL_PARTNER_MASTER_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("CHANNEL_PARTNER_MASTER_ID", typeId, Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CHANNEL_PARTNER_MASTER IS *****"+query);
		
        List<List<ChannelPartnerMasterPojo>> ChannelPartnerMasterPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<ChannelPartnerMasterPojo>>() {
					public List<ChannelPartnerMasterPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<ChannelPartnerMasterPojo> resultSetMapper = new ResultSetMapper<ChannelPartnerMasterPojo>();
						List<ChannelPartnerMasterPojo> ChannelPartnerMasterPojoLIST = null;
						try {
							ChannelPartnerMasterPojoLIST = resultSetMapper.mapRersultSetToObject(rs, ChannelPartnerMasterPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The ChannelPartnerMasterPojoLists objects  is *****" + ChannelPartnerMasterPojoLIST);		
						return ChannelPartnerMasterPojoLIST;
					}
				});
		
        logger.info("*** The ChannelPartnerMasterPojoLists is *****"+ChannelPartnerMasterPojoLists);
		if(ChannelPartnerMasterPojoLists.isEmpty()) {
			ChannelPartnerMasterPojoLists.add(new ArrayList<ChannelPartnerMasterPojo>());
		}
		logger.info("**** The ChannelPartnerMasterPojoLists is ****"+ChannelPartnerMasterPojoLists.get(0));
		return ChannelPartnerMasterPojoLists.get(0);
	}

	@Override
	public List<CustomerApplicationPojo> getCustomerApplication(Long flatBookId) {
		logger.info("***** The control is inside the getCustomerApplication in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_APPLICATION)
				.append(" WHERE FLAT_BOOK_ID = :FLAT_BOOK_ID ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_BOOK_ID", flatBookId, Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_APPLICATION IS *****"+query);
		
        List<List<CustomerApplicationPojo>> CustomerApplicationPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerApplicationPojo>>() {
					public List<CustomerApplicationPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerApplicationPojo> resultSetMapper = new ResultSetMapper<CustomerApplicationPojo>();
						List<CustomerApplicationPojo> CustomerApplicationPojoLIST = null;
						try {
							CustomerApplicationPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerApplicationPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerApplicationPojoLists objects  is *****" + CustomerApplicationPojoLIST);		
						return CustomerApplicationPojoLIST;
					}
				});
		
        logger.info("*** The CustomerApplicationPojoLists is *****"+CustomerApplicationPojoLists);
		if(CustomerApplicationPojoLists.isEmpty()) {
			CustomerApplicationPojoLists.add(new ArrayList<CustomerApplicationPojo>());
		}
		logger.info("**** The CustomerApplicationPojoLists is ****"+CustomerApplicationPojoLists.get(0));
		return CustomerApplicationPojoLists.get(0);
	}

	@Override
	public List<CustomerKycSubmittedDocPojo> getCustomerKycSubmittedDoc(Long flatBookId, Long custBookInfoId) {
		logger.info("***** The control is inside the getCustomerKycSubmittedDoc in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_KYC_SUBMITTED_DOC)
				.append(" WHERE FLAT_BOOK_ID = :FLAT_BOOK_ID ")
				.append(" AND CUST_BOOK_INFO_ID = :CUST_BOOK_INFO_ID ")
	            .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_BOOK_ID", flatBookId, Types.BIGINT);
        namedParameters.addValue("CUST_BOOK_INFO_ID", custBookInfoId, Types.BIGINT);
        
        logger.info("**** THE QRY_TO_GET_CUSTOMER_KYC_SUBMITTED_DOC IS *****"+query);
		
        List<List<CustomerKycSubmittedDocPojo>> CustomerKycSubmittedDocPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerKycSubmittedDocPojo>>() {
					public List<CustomerKycSubmittedDocPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerKycSubmittedDocPojo> resultSetMapper = new ResultSetMapper<CustomerKycSubmittedDocPojo>();
						List<CustomerKycSubmittedDocPojo> CustomerKycSubmittedDocPojoLIST = null;
						try {
							CustomerKycSubmittedDocPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerKycSubmittedDocPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerKycSubmittedDocPojoLists objects  is *****" + CustomerKycSubmittedDocPojoLIST);		
						return CustomerKycSubmittedDocPojoLIST;
					}
				});
		
        logger.info("*** The CustomerKycSubmittedDocPojoLists is *****"+CustomerKycSubmittedDocPojoLists);
		if(CustomerKycSubmittedDocPojoLists.isEmpty()) {
			CustomerKycSubmittedDocPojoLists.add(new ArrayList<CustomerKycSubmittedDocPojo>());
		}
		logger.info("**** The CustomerKycSubmittedDocPojoLists is ****"+CustomerKycSubmittedDocPojoLists.get(0));
		return CustomerKycSubmittedDocPojoLists.get(0);
	}
	
	
	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(@NonNull BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCustomerPropertyDetails in BookingFormServiceDaoImpl *****");
		
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && !bookingFormRequest.getRequestUrl().equalsIgnoreCase("getFormsList")) {
			if(bookingFormRequest.getRequestUrl().equalsIgnoreCase("saveBookingDetails")) {
				querySB.append(" WHERE S.NAME = :SITE_NAME ");
				querySB.append(" AND F.FLAT_NO = :FLAT_NO ");
				//querySB.append(" AND FB.FLAT_BOOK_ID = :FLAT_BOOK_ID");
				namedParameters.addValue("SITE_NAME",bookingFormRequest.getSiteName());
				namedParameters.addValue("FLAT_NO",bookingFormRequest.getFlatNo(), Types.BIGINT);
				//namedParameters.addValue("FLAT_BOOK_ID",employeeTicketRequestInfo.getFlatBookingId(), Types.BIGINT);
			} else if(bookingFormRequest.getRequestUrl().equalsIgnoreCase("validateBookingForm")) {
				//querySB.append(" WHERE S.NAME = :SITE_NAME ");
				querySB.append(" WHERE S.SALES_FORCE_SITE_NAME = :SITE_NAME ");
				querySB.append(" AND F.FLAT_NO = :FLAT_NO ");
				querySB.append(" AND FL.NAME = :FLOOR_NAME ");
				querySB.append(" AND BL.NAME = :BLOCK_NAME ");
				querySB.append(" AND (FB.STATUS_ID = :P_STATUS_ID OR FB.STATUS_ID = :A_STATUS_ID) ");
				namedParameters.addValue("SITE_NAME",bookingFormRequest.getSiteName());
				namedParameters.addValue("FLAT_NO",bookingFormRequest.getFlatNo());
				namedParameters.addValue("FLOOR_NAME",bookingFormRequest.getFloorName());
				namedParameters.addValue("BLOCK_NAME",bookingFormRequest.getBlockName());
				namedParameters.addValue("P_STATUS_ID",Status.PENDING.getStatus(), Types.BIGINT);
				namedParameters.addValue("A_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
			} else {
				querySB.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID");
				namedParameters.addValue("FLAT_BOOK_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
			}
		} else {
			if(bookingFormRequest.getStatusId()!=null) {
				querySB.append(" WHERE FB.STATUS_ID = :STATUS_ID ");
				namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
				
			} else {
		 querySB.append(" AND FB.UPLOADED_EMP_LEVEL_MAP_ID IN( ")
				.append(SqlQuery.QRT_TO_GET_MENU_LEVEL_MAPPING_ID_APPROVAL)
				.append(" AND  ML.MENU_ID = :MENU_ID ) ")
				.append(" WHERE FB.STATUS_ID = :STATUS_ID_P ");
				//.append("AND FB.STATUS_ID <> :STATUS_ID_A ")
				//.append("AND FB.STATUS_ID <> :STATUS_ID_R ");
				//querySB.append(" WHERE FB.STATUS_ID <> :STATUS_ID_A ");
				//querySB.append(" AND FB.STATUS_ID <> :STATUS_ID_R ");
				//namedParameters.addValue("STATUS_ID_A", Status.ACTIVE.getStatus(), Types.BIGINT);
				//namedParameters.addValue("STATUS_ID_R", Status.REJECTED.getStatus(), Types.BIGINT);
		 		namedParameters.addValue("STATUS_ID_P", Status.PENDING.getStatus(), Types.BIGINT);
				namedParameters.addValue("EMP_ID",Util.isNotEmptyObject(bookingFormRequest.getEmpId())?bookingFormRequest.getEmpId():0l,Types.BIGINT);
				namedParameters.addValue("MENU_ID",Util.isNotEmptyObject(bookingFormRequest.getMenuId())?bookingFormRequest.getMenuId():0l,Types.BIGINT);
			}
			if(bookingFormRequest.getSiteName()!=null && !bookingFormRequest.getSiteName().equals(""))
			{
				querySB.append(" AND S.SITE_ID in "+ bookingFormRequest.getSiteName());
			}
		}
		namedParameters.addValue("AR_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		String query = querySB.toString();
		logger.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****"+query);
		 List<List<CustomerPropertyDetailsPojo>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
					new RowMapper<List<CustomerPropertyDetailsPojo>>() {
						public List<CustomerPropertyDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
							logger.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<CustomerPropertyDetailsPojo> resultSetMapper = new ResultSetMapper<CustomerPropertyDetailsPojo>();
							List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoLIST = null;
							try {
								customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPropertyDetailsPojo.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg,ex.getCause());
							}
							logger.info("***** The customerPropertyDetailsPojoLIST objects  is *****" + customerPropertyDetailsPojoLIST);		
							return customerPropertyDetailsPojoLIST;
						}
					});
		 	logger.debug("*** The customerPropertyDetailsPojoLists is *****"+customerPropertyDetailsPojoLists);
			if(customerPropertyDetailsPojoLists.isEmpty()) {
				customerPropertyDetailsPojoLists.add(new ArrayList<CustomerPropertyDetailsPojo>());
			}
			logger.debug("**** The customerPropertyDetailsPojoLists is ****"+customerPropertyDetailsPojoLists.get(0));
			return customerPropertyDetailsPojoLists.get(0);
	}

	@Override
	public List<Map<String, Object>> getCustomerPropertyDetailsToInactive(BookingFormRequest bookingFormRequest) {
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		querySB.append(" WHERE BD.SITE_ID123 = :SITE_ID ");
		//querySB.append(" and trunc(fb.booking_date)  <= to_date('20-10-2021','dd-MM-yyyy')");
		if (Util.isNotEmptyObject(bookingFormRequest.getFlatNo())) {
			querySB.append(" AND F.FLAT_NO = :FLAT_NO ");
			namedParameters.addValue("FLAT_NO",bookingFormRequest.getFlatNo());
		}
		if(true) {
			querySB.append(" AND F.FLAT_NO IN (:FLAT_NOs) ");
			//namedParameters.addValue("FLAT_NOs",Arrays.asList("A3706AA","B3606","B1808","B2008","B3203AA","B4108"));
			namedParameters.addValue("FLAT_NOs",Arrays.asList("A1907","A2409","A2807","A3401","A3606","A3706","A4101","B1609","B1808","B2008","B2106","B3104","B3606","B4108"));
		}
		if (Util.isNotEmptyObject(bookingFormRequest.getFloorName())) {
			querySB.append(" AND FL.NAME = :FLOOR_NAME ");
			namedParameters.addValue("FLOOR_NAME",bookingFormRequest.getFloorName());
		}
		
		if (Util.isNotEmptyObject(bookingFormRequest.getBlockName())) {
			querySB.append(" AND BL.NAME = :BLOCK_NAME ");
			namedParameters.addValue("BLOCK_NAME",bookingFormRequest.getBlockName());
		}
		
		querySB.append(" AND FB.STATUS_ID IN (:STATUS_ID) ");
		querySB.append(" ORDER BY FB.FLAT_BOOK_ID");
		namedParameters.addValue("SITE_ID",bookingFormRequest.getSiteId());
		namedParameters.addValue("AR_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		//,Status.PENDING.getStatus(),Status.ACTIVE.getStatus(),
		namedParameters.addValue("STATUS_ID",Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
		List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(querySB.toString(), namedParameters);
		return list;
	}
	
	@Override
	public List<FlatBookingPojo> getFlatbookingDetails(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getFlatbookingDetails in BookingFOrmDaoImpl *****");
		
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_BOOKING_DETAILS);
		
		if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("noCustId")) {
			querySB.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID");
		}
		else if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("validateBookingForm")) {
			querySB.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID");
		} else if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("getCoAppFlatDetails")) {
			querySB.append(" WHERE FB.CUST_ID = :CUST_ID ");//ACP to get Co- APP Flat Details
		}
		else {
			querySB.append(" WHERE FB.CUST_ID = :CUST_ID AND FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
		}
		
		if(bookingFormRequest.getStatusId()!=null) {
			if (bookingFormRequest.getRequestUrl()==null || !bookingFormRequest.getRequestUrl().equalsIgnoreCase("getCustomerAndCo_AppDetails")) {
				querySB.append(" AND FB.STATUS_ID = :STATUS_ID");
			}
		}
		String query = querySB.toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CUST_ID",bookingFormRequest.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		
		List<List<FlatBookingPojo>> flatBookingPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<FlatBookingPojo>>() {
					public List<FlatBookingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatBookingPojo> resultSetMapper = new ResultSetMapper<FlatBookingPojo>();
						List<FlatBookingPojo> flatBookingPojoLIST = null;
						try {
							flatBookingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatBookingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The flatBookingPojoLIST objects  is *****" + flatBookingPojoLIST);		
						return flatBookingPojoLIST;
					}
				});
		
		//logger.info("*** The flatBookingPojoLists is *****"+flatBookingPojoLists);
		if(flatBookingPojoLists.isEmpty()) {
			flatBookingPojoLists.add(new ArrayList<FlatBookingPojo>());
		}
		logger.info("**** The flatBookingPojoLists is ****"+flatBookingPojoLists.get(0));
		return flatBookingPojoLists.get(0);	
	}

	@Override
	public Long getNumberOfCarParkingAllotedNumber(BookingFormRequest bookingFormRequest) {
		String query = SqlQuery.QRY_TO_GET_CAR_PARKING_ALLOTED_NUMBER;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOK_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOKING_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("CARPARKING_ALLOTMENT_STATUS_ID", Status.ALLOTTED.getStatus(), Types.BIGINT);
		long number = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		return number;
	}
	
	@Override
	public List<Map<String, Object>> getAggrementTypesList(BookingFormRequest bookingFormRequest) {
		String query = SqlQuery.QRY_TO_GET_AGREEMENT_DRAFT_TYPES;
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		logger.info("**** THE QRY_TO_GET_AGREEMENT_DRAFT_TYPES IS *****"+query);
		List<Map<String, Object>> listOfAgreementTypes = nmdPJdbcTemplate.queryForList(query, namedParameters);
		return listOfAgreementTypes;
	}
	
	@Override
	public int updateWelcomeMailSendDetails(BookingFormRequest bookingFormRequest) {
		String query = SqlQuery.QRY_TO_UPDATED_FLAT_BOOKING_WELCOME_MAIL_GENERATED_LOG;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("IS_WELCOME_MAIL_SEND", Status.YES.getStatus(), Types.BIGINT);
		logger.info("***** Control inside the BookingFormServiceDaoImpl.updateWelcomeMailSendDetails() query ***** "+query);
		int result =nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public List<DynamicKeyValueInfo> getTheTermsAndConditions(BookingFormRequest bookingFormRequestCopy, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		String query = SqlQuery.QRY_TO_GET_TERMS_AND_CONDITIONS;
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", bookingFormRequestCopy.getSiteId(), Types.BIGINT);
		namedParameters.addValue("FLATS_SALE_OWNERS_ID", customerPropertyDetailsInfo.getFlatSaleOwnerId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		logger.info("**** THE QRY_TO_GET_TERMS_AND_CONDITIONS IS *****"+query);
		
		List<List<DynamicKeyValueInfo>> termsAndConditionsPojoList = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<DynamicKeyValueInfo>>() {
					public List<DynamicKeyValueInfo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						//logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<DynamicKeyValueInfo> resultSetMapper = new ResultSetMapper<DynamicKeyValueInfo>();
						List<DynamicKeyValueInfo> AddressPojoLIST = null;
						try {
							AddressPojoLIST = resultSetMapper.mapRersultSetToObject(rs, DynamicKeyValueInfo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}		
						return AddressPojoLIST;
					}
				});
		
		if(termsAndConditionsPojoList.isEmpty()) {
			termsAndConditionsPojoList.add(new ArrayList<DynamicKeyValueInfo>());
		}
		return termsAndConditionsPojoList.get(0);	
	}
	
	@Override
	public List<SiteOtherChargesDetailsPojo> getTheSiteOtherChargesDetails(BookingFormRequest bookingFormRequestCopy) {
		logger.info("BookingFormServiceDaoImpl.getTheSiteOtherChargesDetails()");
		String query = SqlQuery.QRY_TO_GET_SITE_RELATED_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", bookingFormRequestCopy.getSiteId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		logger.info("**** BookingFormServiceDaoImpl.getTheSiteOtherChargesDetails() *****"+query+" "+namedParameters);
		
		List<List<SiteOtherChargesDetailsPojo>> siteOtherChargesDetailsList = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<SiteOtherChargesDetailsPojo>>() {
					public List<SiteOtherChargesDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<SiteOtherChargesDetailsPojo> resultSetMapper = new ResultSetMapper<SiteOtherChargesDetailsPojo>();
						List<SiteOtherChargesDetailsPojo> AddressPojoLIST = null;
						try {
							AddressPojoLIST = resultSetMapper.mapRersultSetToObject(rs, SiteOtherChargesDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}		
						return AddressPojoLIST;
					}
				});
		
		if(siteOtherChargesDetailsList.isEmpty()) {
			siteOtherChargesDetailsList.add(new ArrayList<SiteOtherChargesDetailsPojo>());
		}
		return siteOtherChargesDetailsList.get(0);	
	}
	
	@Override
	public List<AddressMappingPojo> getAddressMapping(BookingFormRequest bookingFormRequest) {
		//logger.info("***** The control is inside the getCustomerAddressMapping in BookingFOrmDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_ADDRESS_MAPPING)
               	.append(" WHERE TYPE = :TYPE AND TYPE_ID = :TYPE_ID ")
                .toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPE", bookingFormRequest.getMetadataId(), Types.BIGINT);
		if(bookingFormRequest.getMetadataId()==MetadataId.CUSTOMER.getId()||bookingFormRequest.getMetadataId().equals(MetadataId.CUSTOMER.getId()))
		{
			namedParameters.addValue("TYPE_ID", bookingFormRequest.getCustBookInfoId(), Types.BIGINT);
		}
		if(bookingFormRequest.getMetadataId()==MetadataId.APPLICANT1.getId() ||
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT2.getId()  || 
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT3.getId()  || 
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT4.getId()  || 
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT5.getId()  || 
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT6.getId()  || 
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT7.getId()  ||  
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT8.getId()  ||  
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT9.getId()  ||
		  bookingFormRequest.getMetadataId()==MetadataId.APPLICANT10.getId() ||
		
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT1.getId()) || 
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT2.getId()) || 
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT3.getId()) || 
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT4.getId()) ||
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT5.getId()) ||
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT6.getId()) ||
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT7.getId()) ||
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT8.getId()) ||
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT9.getId()) ||
		  bookingFormRequest.getMetadataId().equals(MetadataId.APPLICANT10.getId()))
			
		{
			namedParameters.addValue("TYPE_ID", bookingFormRequest.getCoAppBookInfoId(), Types.BIGINT);
		}
		logger.info("**** getAddressMapping in BookingFOrmDaoImpl *****"+query);
		
		List<List<AddressMappingPojo>> AddressMappingPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<AddressMappingPojo>>() {
					public List<AddressMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AddressMappingPojo> resultSetMapper = new ResultSetMapper<AddressMappingPojo>();
						List<AddressMappingPojo> AddressMappingPojoLIST = null;
						try {
							AddressMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, AddressMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						//logger.info("***** The AddressMappingPojoLIST objects  is *****" + AddressMappingPojoLIST);		
						return AddressMappingPojoLIST;
					}
				});
		
		//logger.info("*** The AddressMappingPojoLists is *****"+AddressMappingPojoLists);
		if(AddressMappingPojoLists.isEmpty()) {
			AddressMappingPojoLists.add(new ArrayList<AddressMappingPojo>());
		}
		//logger.info("**** The AddressMappingPojoLists is ****"+AddressMappingPojoLists.get(0));
		return AddressMappingPojoLists.get(0);	
	}

	@Override
	public List<AddressPojo> getAddress(Long addressId) {
		logger.info("***** The control is inside the getAddress in BookingFOrmDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_ADDRESS)
               	.append(" WHERE ADDRESS_ID = :ADDRESS_ID ")
                .toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ADDRESS_ID", addressId, Types.BIGINT);
		
		logger.info("**** THE QRY_TO_GET_ADDRESS IS *****"+query);
		
		List<List<AddressPojo>> AddressPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<AddressPojo>>() {
					public List<AddressPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AddressPojo> resultSetMapper = new ResultSetMapper<AddressPojo>();
						List<AddressPojo> AddressPojoLIST = null;
						try {
							AddressPojoLIST = resultSetMapper.mapRersultSetToObject(rs, AddressPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The AddressPojoLIST objects  is *****" + AddressPojoLIST);		
						return AddressPojoLIST;
					}
				});
		
		//logger.info("*** The AddressPojoLists is *****"+AddressPojoLists);
		if(AddressPojoLists.isEmpty()) {
			AddressPojoLists.add(new ArrayList<AddressPojo>());
		}
		logger.info("**** The AddressPojoLists is ****"+AddressPojoLists.get(0));
		return AddressPojoLists.get(0);	
	}

	@Override
	public List<AminitiesInfraCostPojo> getAminitiesInfraCost(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getAddress in BookingFOrmDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_AMINITITES_INFRA_COST)
               	.append(" WHERE FLAT_COST_ID = :FLAT_COST_ID ")
              	.append(" AND STATUS_ID=:STATUS_ID ")
                .toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_COST_ID", bookingFormRequest.getFlatCostId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.INTEGER);
		logger.info("**** THE QRY_TO_GET_AMINITITES_INFRA_COST IS *****"+query);
		
		List<List<AminitiesInfraCostPojo>> AminitiesInfraCostPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<AminitiesInfraCostPojo>>() {
					public List<AminitiesInfraCostPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AminitiesInfraCostPojo> resultSetMapper = new ResultSetMapper<AminitiesInfraCostPojo>();
						List<AminitiesInfraCostPojo> AminitiesInfraCostPojoLIST = null;
						try {
							AminitiesInfraCostPojoLIST = resultSetMapper.mapRersultSetToObject(rs, AminitiesInfraCostPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The AminitiesInfraCostPojoLIST objects  is *****" + AminitiesInfraCostPojoLIST);		
						return AminitiesInfraCostPojoLIST;
					}
				});
		logger.info("*** The AminitiesInfraCostPojoLists is *****"+AminitiesInfraCostPojoLists);
		if(AminitiesInfraCostPojoLists.isEmpty()) {
			AminitiesInfraCostPojoLists.add(new ArrayList<AminitiesInfraCostPojo>());
		}
		logger.info("**** The AminitiesInfraCostPojoLists is ****"+AminitiesInfraCostPojoLists.get(0));
		return AminitiesInfraCostPojoLists.get(0);	
	}

	@Override
	public List<CustomerPojo> getCustomer(BookingFormRequest bookingFormRequest) {
		//logger.info("***** The control is inside the getCustomer in BookingFOrmDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS)
               	.append(" WHERE CUST.CUST_ID = :CUST_ID ")
                .toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(), Types.BIGINT);
		logger.info("***** Control inside the BookingFormServiceDaoImpl.getCustomer() QRY_TO_GET_CUSTOMER_DETAILS *****"+query);
		
		List<List<CustomerPojo>> CustomerPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<CustomerPojo>>() {
					public List<CustomerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPojo> resultSetMapper = new ResultSetMapper<CustomerPojo>();
						List<CustomerPojo> CustomerPojoLIST = null;
						try {
							CustomerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerPojoLIST objects  is *****" + CustomerPojoLIST);		
						return CustomerPojoLIST;
					}
				});
		
		//logger.info("*** The CustomerPojoLists is *****"+CustomerPojoLists);
		if(CustomerPojoLists.isEmpty()) {
			CustomerPojoLists.add(new ArrayList<CustomerPojo>());
		}
		logger.info("**** The CustomerPojoLists is ****"+CustomerPojoLists.get(0));
		return CustomerPojoLists.get(0);
	}

	@Override
	public List<CustomerPojo> getCustomerCoApplicantFlatDetails(Co_ApplicantPojo co_ApplicantPojo) {
		logger.info("***** Control inside the BookingFormServiceDaoImpl.getCustomerCoApplicantFlatDetails() *****");
		//ACP
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS)
               	.append(" WHERE CUST.PANCARD = :PANCARD ")
                .toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PANCARD",co_ApplicantPojo.getPancard(), Types.VARCHAR);
		
		logger.info("**** THE QRY_TO_GET_CUSTOMER_DETAILS IS *****"+query);
		
		List<List<CustomerPojo>> CustomerPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<CustomerPojo>>() {
					public List<CustomerPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPojo> resultSetMapper = new ResultSetMapper<CustomerPojo>();
						List<CustomerPojo> CustomerPojoLIST = null;
						try {
							CustomerPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerPojoLIST objects  is *****" + CustomerPojoLIST);		
						return CustomerPojoLIST;
					}
				});
		
		//logger.info("*** The CustomerPojoLists is *****"+CustomerPojoLists);
		if(CustomerPojoLists.isEmpty()) {
			CustomerPojoLists.add(new ArrayList<CustomerPojo>());
		}
		logger.info("**** The CustomerPojoLists is ****"+CustomerPojoLists.get(0));
		return CustomerPojoLists.get(0);
	}
	
	@Override
	public List<CustBookInfoPojo> getCustBookInfo(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCustBookInfo in BookingFOrmDaoImpl *****");
		StringBuilder query = new StringBuilder(SqlQuery.QUERY_TO_GET_CUST_BOOK_INFO);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && bookingFormRequest.getRequestUrl().equalsIgnoreCase("saveBookingDetails")) {
			//query.append(" WHERE SALES_TEAM_LEAD_ID = :SALES_TEAM_LEAD_ID AND STATUS_ID = :STATUS_ID ").toString();
			query.append(" WHERE SALES_TEAM_LEAD_ID = :SALES_TEAM_LEAD_ID ").toString();
			namedParameters.addValue("SALES_TEAM_LEAD_ID", bookingFormRequest.getSalesTeamLeadId(), Types.BIGINT);
			//namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}else {
			query.append(" WHERE CUST_ID = :CUST_ID ")
               	.append(" AND FLAT_BOOK_ID = :FLAT_BOOK_ID")
               	.toString();
		namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		}
		logger.info("**** THE QUERY_TO_GET_CUST_BOOK_INFO IS *****"+query);
		List<List<CustBookInfoPojo>> CustBookInfoPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustBookInfoPojo>>() {
					public List<CustBookInfoPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustBookInfoPojo> resultSetMapper = new ResultSetMapper<CustBookInfoPojo>();
						List<CustBookInfoPojo> CustBookInfoPojoLIST = null;
						try {
							CustBookInfoPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustBookInfoPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustBookInfoPojoLIST objects  is *****" + CustBookInfoPojoLIST);		
						return CustBookInfoPojoLIST;
					}
				});
		
		//logger.info("*** The CustBookInfoPojoLists is *****"+CustBookInfoPojoLists);
		if(CustBookInfoPojoLists.isEmpty()) {
			CustBookInfoPojoLists.add(new ArrayList<CustBookInfoPojo>());
		}
		logger.info("**** The CustBookInfoPojoLists is ****"+CustBookInfoPojoLists.get(0));
		return CustBookInfoPojoLists.get(0);
	}

	@Override
	public List<Co_ApplicantPojo> getCo_Applicant(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCo_Applicant in BookingFOrmDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QUERY_TO_GET_CO_APPLICANT)
				.append(" WHERE CO_APPLICANT_ID = :CO_APPLICANT_ID ")
               	.toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CO_APPLICANT_ID", bookingFormRequest.getApplicantId(), Types.BIGINT);
		
		logger.info("**** THE QUERY_TO_GET_CO_APPLICANT IS *****"+query);
		
		List<List<Co_ApplicantPojo>> Co_ApplicantPojoLists = nmdPJdbcTemplate.query(query,namedParameters,
				new RowMapper<List<Co_ApplicantPojo>>() {
					public List<Co_ApplicantPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<Co_ApplicantPojo> resultSetMapper = new ResultSetMapper<Co_ApplicantPojo>();
						List<Co_ApplicantPojo> Co_ApplicantPojoLIST = null;
						try {
							Co_ApplicantPojoLIST = resultSetMapper.mapRersultSetToObject(rs, Co_ApplicantPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The Co_ApplicantPojoLIST objects  is *****" + Co_ApplicantPojoLIST);		
						return Co_ApplicantPojoLIST;
					}
				});
		
		//logger.info("*** The Co_ApplicantPojoLists is *****"+Co_ApplicantPojoLists);
		if(Co_ApplicantPojoLists.isEmpty()) {
			Co_ApplicantPojoLists.add(new ArrayList<Co_ApplicantPojo>());
		}
		logger.info("**** The Co_ApplicantPojoLists is ****"+Co_ApplicantPojoLists.get(0));
		return Co_ApplicantPojoLists.get(0);
	}

	@Override
	public List<CoAppBookInfoPojo> getCoAppBookInfo(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getCoAppBookInfo in BookingFOrmDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QUERY_TO_GET_CO_APP_BOOK_INFO);
				
				if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && bookingFormRequest.getRequestUrl().equalsIgnoreCase("checkcoaplicants")) {
					query.append(" WHERE CO_APPLICANT_ID = :CO_APPLICANT_ID ").toString();
					namedParameters.addValue("CO_APPLICANT_ID", bookingFormRequest.getCoApplicantId(), Types.BIGINT);
		         }else if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && bookingFormRequest.getRequestUrl().equalsIgnoreCase("updatecoaplicants")) {
		        	 query.append(" WHERE CUST_BOOK_INFO_ID = :CUST_BOOK_INFO_ID ")
		        	 .append(" AND CO_APPLICANT_ID = :CO_APPLICANT_ID ")
		        	 .toString();
		        	 namedParameters.addValue("CUST_BOOK_INFO_ID", bookingFormRequest.getCustBookInfoId(), Types.BIGINT);
		        	 namedParameters.addValue("CO_APPLICANT_ID", bookingFormRequest.getCoApplicantId(), Types.BIGINT);
		         }
		       else if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && bookingFormRequest.getRequestUrl().equalsIgnoreCase("isAlreadyPresent")){
		        	 query.append(" WHERE CUST_BOOK_INFO_ID = :CUST_BOOK_INFO_ID ")
		        	 .append(" AND TYPE = :TYPE  ")
		        	 //.append(" AND STATUS_ID IN (:STATUS_ID) ")
		        	 .toString();
		        	 namedParameters.addValue("CUST_BOOK_INFO_ID", bookingFormRequest.getCustBookInfoId(), Types.BIGINT);
		        	 namedParameters.addValue("TYPE", bookingFormRequest.getType(), Types.BIGINT);
		        	 //namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.status,Status.PENDING.status), Types.BIGINT);
		         }
				else {
					query.append(" WHERE CUST_BOOK_INFO_ID = :CUST_BOOK_INFO_ID ")
					.append(" ORDER BY TYPE ")
					.toString();
					namedParameters.addValue("CUST_BOOK_INFO_ID", bookingFormRequest.getCustBookInfoId(), Types.BIGINT);
				}
		
		logger.info("**** THE QUERY_TO_GET_CO_APP_BOOK_INFO IS *****"+query);
		
		List<List<CoAppBookInfoPojo>> CoAppBookInfoPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CoAppBookInfoPojo>>() {
					public List<CoAppBookInfoPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CoAppBookInfoPojo> resultSetMapper = new ResultSetMapper<CoAppBookInfoPojo>();
						List<CoAppBookInfoPojo> CoAppBookInfoPojoLIST = null;
						try {
							CoAppBookInfoPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CoAppBookInfoPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CoAppBookInfoPojoLIST objects  is *****" + CoAppBookInfoPojoLIST);		
						return CoAppBookInfoPojoLIST;
					}
				});
		
		//logger.info("*** The CoAppBookInfoPojoLists is *****"+CoAppBookInfoPojoLists);
		if(CoAppBookInfoPojoLists.isEmpty()) {
			CoAppBookInfoPojoLists.add(new ArrayList<CoAppBookInfoPojo>());
		}
		logger.info("**** The CoAppBookInfoPojoLists is ****"+CoAppBookInfoPojoLists.get(0));
		return CoAppBookInfoPojoLists.get(0);
	}

	@Override
	public List<AminititesInfraFlatWisePojo> getAminitiesInfraFlatWise(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getAminitiesInfraFlatWise in BookingFormServiceDaoImpl *****");
		StringBuilder querySB = new StringBuilder(SqlQuery.QRY_TO_GET_AMINITITES_INFRA_FLAT_WISE);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("saveBookingDetails")) {
			querySB.append(" WHERE FLAT_ID = :FLAT_ID ");
			querySB.append(" AND AMINITITES_INFRA_SITE_WISE_ID = :AMINITITES_INFRA_SITE_WISE_ID ");
			namedParameters.addValue("FLAT_ID", bookingFormRequest.getFlatId(), Types.BIGINT);
			namedParameters.addValue("AMINITITES_INFRA_SITE_WISE_ID", bookingFormRequest.getAminititesInfraSiteWiseId(), Types.BIGINT);
		}
		else if(bookingFormRequest.getRequestUrl()!=null && bookingFormRequest.getRequestUrl().equalsIgnoreCase("getBookingDetails")) {
			querySB.append(" WHERE FLAT_ID = :FLAT_ID ");
			namedParameters.addValue("FLAT_ID", bookingFormRequest.getFlatId(), Types.BIGINT);
		}
		else {
			querySB.append(" WHERE AMINITITES_INFRA_FLAT_WISE_ID = :AMINITITES_INFRA_FLAT_WISE_ID ");
			namedParameters.addValue("AMINITITES_INFRA_FLAT_WISE_ID", bookingFormRequest.getAminititesInfraFlatWiseId(), Types.BIGINT);
		}
		String query = querySB.toString();
        
        logger.info("**** THE QRY_TO_GET_AMINITITES_INFRA_FLAT_WISE IS *****"+query);
		
        List<List<AminititesInfraFlatWisePojo>> AminititesInfraFlatWisePojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<AminititesInfraFlatWisePojo>>() {
					public List<AminititesInfraFlatWisePojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<AminititesInfraFlatWisePojo> resultSetMapper = new ResultSetMapper<AminititesInfraFlatWisePojo>();
						List<AminititesInfraFlatWisePojo> AminititesInfraFlatWisePojoLIST = null;
						try {
							AminititesInfraFlatWisePojoLIST = resultSetMapper.mapRersultSetToObject(rs, AminititesInfraFlatWisePojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The AminititesInfraFlatWisePojoLists objects  is *****" + AminititesInfraFlatWisePojoLIST);		
						return AminititesInfraFlatWisePojoLIST;
					}
				});
		
        //logger.info("*** The AminititesInfraFlatWisePojoLists is *****"+AminititesInfraFlatWisePojoLists);
		if(AminititesInfraFlatWisePojoLists.isEmpty()) {
			AminititesInfraFlatWisePojoLists.add(new ArrayList<AminititesInfraFlatWisePojo>());
		}
		logger.info("**** The AminititesInfraFlatWisePojoLists is ****"+AminititesInfraFlatWisePojoLists.get(0));
		return AminititesInfraFlatWisePojoLists.get(0);
	}

	@Override
	public Long saveCoAppBookInfo(CoAppBookInfoPojo custBookInfoPojo) {
		logger.info("*** The control is inside the saveCoAppBookInfo in BookingFormServiceDaoImpl ***"+custBookInfoPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CO_APP_BOOK_INFO,new BeanPropertySqlParameterSource(custBookInfoPojo),keyHolder,new String[] { "CO_APP_BOOK_INFO_ID" });
		logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public int saveBookingChangedStatus(BookingStatusChangedPojo pojo) {
		logger.info("***** Control inside the BookingFormServiceDaoImpl.saveBookingChangedStatus() *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_BOOKING_STATUS_CHANGED_DTLS,new BeanPropertySqlParameterSource(pojo),keyHolder,new String[] { "BOOKING_CHANGED_DTLS_ID" });
		pojo.setBookingChangedDtlsId(keyHolder.getKey().longValue());
		logger.info("*** The primarykey generated for this record is ****" + pojo.getBookingChangedDtlsId());
		return pojo.getBookingChangedDtlsId().intValue();
	}
	
	@Override
	public int updateCustomer(BookingFormRequest bookingFormRequest,List<Long> statusIds) {
		logger.info("***** The control is inside the updateCustomer in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CUSTOMER;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
	//	namedParameters.addValue("PAST_STATUS_ID", Status.PENDING.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAST_STATUS_ID",statusIds, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in customer table  *****"+result);
		return result;
	}
	
	@Override
	public int updateCustBookInfo(BookingFormRequest bookingFormRequest,Long status) {
		logger.info("***** The control is inside the updateCustBookInfo in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CUST_BOOK_INFO;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
	//	namedParameters.addValue("PAST_STATUS_ID", Status.PENDING.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAST_STATUS_ID", status, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		logger.info("**** The number of rows updated in CustBookInfo table  *****"+result);
		return result;
	}

	@Override
	public int updateCoAppBookInfo(BookingFormRequest bookingFormRequest,Long status) {
		logger.info("***** The control is inside the updateCoAppBookInfo in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CO_APP_BOOK_INFO;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("CUST_BOOK_INFO_ID", bookingFormRequest.getCustBookInfoId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
		//namedParameters.addValue("PAST_STATUS_ID", Status.PENDING.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAST_STATUS_ID",status, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in CoAppBookInfo table  *****"+result);
		return result;
	}

	@Override
	public int updateFlatCost(BookingFormRequest bookingFormRequest,Long status) {
		logger.info("***** The control is inside the updateFlatCost in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_FLAT_COST;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
	//	namedParameters.addValue("PAST_STATUS_ID", Status.PENDING.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAST_STATUS_ID",status, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in FlatBooking table  *****"+result);
		return result;
	}

	@Override
	public int updateFlatBooking(BookingFormRequest bookingFormRequest,Long status,boolean flag) {
		logger.info("***** The control is inside the updateFlatBooking in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(flag) {
			String query = SqlQuery.QRY_TO_UPDATE_FLAT_BOOKING_WTO_CANCEL;
			namedParameters.addValue("CANCELED_EMP_ID", bookingFormRequest.getEmpId(), Types.BIGINT);
			namedParameters.addValue("CANCELED_DATE", bookingFormRequest.getBookingformCanceledDate(), Types.TIMESTAMP);
			namedParameters.addValue("COMMENTS", bookingFormRequest.getComments(), Types.VARCHAR);
			namedParameters.addValue("FLAT_BOOK_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
			int result = nmdPJdbcTemplate.update(query,namedParameters);
			logger.info("**** The number of rows updated in FlatBooking table  *****"+result);
			return result;
		}else {
		String query = SqlQuery.QRY_TO_UPDATE_FLAT_BOOKING;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("CUST_ID", bookingFormRequest.getCustomerId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		//namedParameters.addValue("PAST_STATUS_ID", Status.PENDING.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAST_STATUS_ID",status, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in FlatBooking table  *****"+result);
		return result;
		}
	}
	
	@Override
	public int updateCoApplicant(BookingFormRequest bookingFormRequest,Long status) {
		logger.info("***** The control is inside the updateCustomer in BookingFormServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_CO_APPLICANT;
		namedParameters.addValue("STATUS_ID", bookingFormRequest.getStatusId(), Types.BIGINT);
		namedParameters.addValue("CO_APPLICANT_ID", bookingFormRequest.getApplicantId(), Types.BIGINT);
		namedParameters.addValue("UPDATED_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
	//	namedParameters.addValue("PAST_STATUS_ID", Status.PENDING.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAST_STATUS_ID", status, Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		logger.info("**** The number of rows updated in customer table  *****"+result);
		return result;
	}
	@Override
	public Long saveContactInfo(ContactInfoPojo contactInfoPojo) {
		logger.info("*** The control is inside the saveContactInfo in BookingFormServiceDaoImpl ***"+contactInfoPojo);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_CONTACT_INFORMATION,new BeanPropertySqlParameterSource(contactInfoPojo),keyHolder,new String[] { "CONT_INFO_ID" });
		logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	@Override
	public List<CustomerPropertyDetailsPojo> getFlatFloorBlockSiteByNames(BookingFormRequest bookingFormRequest) {
		//logger.info("***** The control is inside the getFlatByName in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_FLOOR_BLOCK_SITE_DETAILS)
				.append(" WHERE S.SALES_FORCE_SITE_NAME = :SITE_NAME ")
				.append(" AND B.NAME = :BLOCK_NAME ")
				.append(" AND FL.NAME = :FLOOR_NAME ")
				.append(" AND F.FLAT_NO = :FLAT_NO")
	            .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("SITE_NAME", bookingFormRequest.getSiteName());
        namedParameters.addValue("BLOCK_NAME", bookingFormRequest.getBlockName());
        namedParameters.addValue("FLOOR_NAME", bookingFormRequest.getFloorName());
        namedParameters.addValue("FLAT_NO", bookingFormRequest.getFlatNo());
        logger.info("***** Control inside the BookingFormServiceDaoImpl.getFlatFloorBlockSiteByNames() ***** "+query+" \n"+namedParameters.getValues());
        //logger.info("**** THE QRY_TO_GET_FLAT_FLOOR_BLOCK_SITE_DETAILS IS *****"+query);
		
        List<List<CustomerPropertyDetailsPojo>> CustomerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<CustomerPropertyDetailsPojo>>() {
					public List<CustomerPropertyDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<CustomerPropertyDetailsPojo> resultSetMapper = new ResultSetMapper<CustomerPropertyDetailsPojo>();
						List<CustomerPropertyDetailsPojo> CustomerPropertyDetailsPojoLIST = null;
						try {
							CustomerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPropertyDetailsPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The CustomerPropertyDetailsPojoLists objects  is *****" + CustomerPropertyDetailsPojoLIST);		
						return CustomerPropertyDetailsPojoLIST;
					}
				});
		
        //logger.info("*** The CustomerPropertyDetailsPojoLists is *****"+CustomerPropertyDetailsPojoLists);
		if(CustomerPropertyDetailsPojoLists.isEmpty()) {
			CustomerPropertyDetailsPojoLists.add(new ArrayList<CustomerPropertyDetailsPojo>());
		}
		logger.info("**** The CustomerPropertyDetailsPojoLists is ****"+CustomerPropertyDetailsPojoLists.get(0));
		return CustomerPropertyDetailsPojoLists.get(0);
	}
	
	@Override
	public Integer ValidateCityState(BookingFormRequest bookingFormRequest) {
		logger.info("**** The control is inside the ValidateCityState in BookingFOrmDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CITY_ID", bookingFormRequest.getCityId());
		namedParameters.addValue("STATE_ID", bookingFormRequest.getStateId());
		Integer count = nmdPJdbcTemplate.queryForObject(SqlQuery.QRY_TO_TEST_CITY_STATE, namedParameters, Integer.class);
		logger.info("***** The noumber of rows effected for the query is *********" + count);
		return count;
	}
	
	@Override
	public List<MenuLevelMappingPojo> getMenuLevelMappingId(CustomerBookingFormInfo customerBookingFormInfo) {
		logger.info("***** The control is inside the getMenuLevelMappingId in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRT_TO_GET_MENU_LEVEL_MAPPING_ID) .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("EMP_ID",customerBookingFormInfo.getEmployeeId());
        namedParameters.addValue("MENU_ID",customerBookingFormInfo.getMenuId());
        logger.info("**** THE QRT_TO_GET_MENU_LEVEL_MAPPING_ID IS *****"+query);
        List<List<MenuLevelMappingPojo>> menuLevelMappingPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<MenuLevelMappingPojo>>() {
					public List<MenuLevelMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<MenuLevelMappingPojo> resultSetMapper = new ResultSetMapper<MenuLevelMappingPojo>();
						List<MenuLevelMappingPojo> menuLevelMappingPojoLIST = null;
						try {
							menuLevelMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs, MenuLevelMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						logger.info("***** The menuLevelMappingPojoLIST objects  is *****" + menuLevelMappingPojoLIST);		
						return menuLevelMappingPojoLIST;
					}
				});
       // logger.info("*** The menuLevelMappingPojoLists is *****"+menuLevelMappingPojoLists);
		if(menuLevelMappingPojoLists.isEmpty()) {
			menuLevelMappingPojoLists.add(new ArrayList<MenuLevelMappingPojo>());
		}
		logger.info("**** The menuLevelMappingPojoLists is ****"+menuLevelMappingPojoLists.get(0));
		return menuLevelMappingPojoLists.get(0);
	}
	@Override
	public List<CustomerPropertyDetailsPojo> getBookingFlatData(CustomerPropertyDetailsPojo flatData) {
		String query = new StringBuilder(SqlQuery.QRT_TO_GET_BOOKING_FLAT_DATA) .toString();
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	     namedParameters.addValue("FLAT_ID",flatData.getFlatId());
	        
	    List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = nmdPJdbcTemplate.query(query.toString(),namedParameters,
			 new RowMapper<CustomerPropertyDetailsPojo>() {
					public CustomerPropertyDetailsPojo mapRow(ResultSet rs, int arg1) throws SQLException {
						CustomerPropertyDetailsPojo customerPropertyDetailsPojo = new CustomerPropertyDetailsPojo();
						customerPropertyDetailsPojo.setCustBookingId(rs.getLong("CUST_BOOK_INFO_ID"));
						customerPropertyDetailsPojo.setFlatBookingId(rs.getLong("FLAT_BOOK_ID"));
						customerPropertyDetailsPojo.setCustomerId(rs.getLong("CUST_ID"));
						customerPropertyDetailsPojo.setProId(rs.getLong("CUST_PROFFISIONAL_ID"));
						customerPropertyDetailsPojo.setFlatId(rs.getLong("FLAT_ID"));
						customerPropertyDetailsPojo.setBookingStatus(rs.getLong("BOOKING_STATUS"));
						customerPropertyDetailsPojo.setBookingDate(rs.getTimestamp("BOOKING_DATE"));
						return customerPropertyDetailsPojo;
						}
					});
	   
		if(customerPropertyDetailsPojos.isEmpty()) {
			customerPropertyDetailsPojos.addAll(new ArrayList<CustomerPropertyDetailsPojo>());
			//logger.debug("*** The getBookingFlatData is *****"+customerPropertyDetailsPojos);
		}
		logger.info("*** The getBookingFlatData is *****"+customerPropertyDetailsPojos);
		return customerPropertyDetailsPojos;
	        
	}

   @Override
	public Long deleteCoApplicantBookInfoData(Long coAppBookInfoId) {
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_CO_APP_BOOKINFO_DATA) .toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("CO_APP_BOOK_INFO_ID",coAppBookInfoId);
	    return (long) nmdPJdbcTemplate.update(query, namedParameters);
	}

	@Override
	public Long deleteAddressMappingOfApplicantOrCoApplicant(Long custBookInfoId, Long type) {
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_ADDRESS_MAPPING) .toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("CUST_BOOK_INFO_ID",custBookInfoId);  
	    namedParameters.addValue("TYPE", type);
	    return (long) nmdPJdbcTemplate.update(query, namedParameters);
	}
	
	@Override
	public Long deleteAddressOfApplicantOrCoApplicant(Long addressId) {
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_ADDRESS) .toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("ADDRESS_ID",addressId);
		return  (long) nmdPJdbcTemplate.update(query, namedParameters);
	}
	
	@Override
	public Long deleteCustomerPOAHolderDetails(CustomerOtherDetailspojo customerOtherDetailspojo) {
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_POAHOLDER_DETAILS) .toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("CUSTOMER_OTHER_DETAILS_ID",customerOtherDetailspojo.getId());
	    return (long) nmdPJdbcTemplate.update(query, namedParameters);
	}
	
	@Override
	public Long deleteCustomerOtherDetails(Long flatBookId) {
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_CUSTOMER_OTHER_DETAILS) .toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("FLAT_BOOK_ID",flatBookId);
	    return (long) nmdPJdbcTemplate.update(query, namedParameters);
	}

	@Override
	public void deleteKYCDetails(Long custBookInfoId) {
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_KYC_DETAILS) .toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("CUST_BOOK_INFO_ID",custBookInfoId);
	    nmdPJdbcTemplate.update(query, namedParameters);
		
	}

	@Override
	public Long updateCustomerInfo(@NonNull CustomerPojo customerPojo) {
		logger.info("*** The control is inside the putCustomerDetails in BookingFormServiceDaoImpl ***"+customerPojo);
		String query = SqlQuery.QRY_TO_UPDATE_CUSTOMER_BASIC_INFO;
		customerPojo.setUpdatedDate(new Timestamp(new Date().getTime()));
		int result = nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(customerPojo));
		return (long) result;
	}
	
	@Override
	public Long updateFlatBookingInfo(@NonNull FlatBookingPojo flatBookingPojo) {
		logger.info("*** The control is inside the putCustomerDetails in BookingFormServiceDaoImpl ***"+flatBookingPojo);
		String query = SqlQuery.QRY_TO_UPDATE_FLAT_BOOKING_INFO;
		int result = nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(flatBookingPojo));
		return (long) result;
	}
	
	@Override
	public Long updateFlatBookingMileStoneDetails(FlatBookingPojo flatBookingPojo) {
		String query = SqlQuery.QRY_TO_UPDATE_FLAT_BOOKING_MS_INFO;
		int result = nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(flatBookingPojo));
		return (long) result;
	}
	
	@Override
	public Long updateCustomerProfessionalDetails(@NonNull ProfessionalDetailsPojo proRequest) {
       logger.info("*** The control is inside the customerProfessionalDetails in BookingFormServiceDaoImpl ***"+proRequest);
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_CUSTOMER_PROFESSIONAL_DETAILS,new BeanPropertySqlParameterSource(proRequest));
		return proRequest.getCustProffisionalId();

	}
	@Override
	public Long updateAddressDetails(@NonNull AddressPojo addressPojo) {	
		logger.info("*** The control is inside the updateAddressDetails in BookingFormServiceDaoImpl ***"+addressPojo);
		Long rowUpdate = (long) nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_CO_APPLICANT_ADDRESS, new BeanPropertySqlParameterSource(addressPojo));
		return rowUpdate;
  }

	@Override
	public Long deleteContactInfo(ContactInfoPojo contactInfoPojo) {
		logger.info("*** The control is inside the deleteContactInfo in BookingFormServiceDaoImpl ***"+contactInfoPojo);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_DELETE_CONTACT_INFORMATION;
		
		namedParameters.addValue("FLATBOOKING_ID", contactInfoPojo.getFlatBookId(), Types.BIGINT);	
		Long result = (long) nmdPJdbcTemplate.update(query,namedParameters);
		return result;	
	}

	@Override
	public List<CoAppBookInfoPojo> getSameCoApplicantforMulCustomerCount(CoApplicentDetailsInfo coApplInfo, Long custBookInfoId) {
		logger.info("*** The control is inside the getSameCoApplicantforMulCustomerCount in BookingFormServiceDaoImpl ***"+coApplInfo);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_COUNT_CO_APPLICANT_FOR_MUL_CUSTOMERS;
		
		namedParameters.addValue("PANCARD", coApplInfo.getCo_ApplicantInfo().getPancard(), Types.VARCHAR);	
		namedParameters.addValue("PASSPORT", coApplInfo.getCo_ApplicantInfo().getPassport(), Types.VARCHAR);	
		//namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.BIGINT);
		/*if(custBookInfoId!=null) {
			query +=" AND CUST_BOOK_INFO_ID=:CUST_BOOK_INFO_ID";
			namedParameters.addValue("CUST_BOOK_INFO_ID", custBookInfoId, Types.BIGINT);	
		} */
		List<CoAppBookInfoPojo> coAppBookInfoPojoList=nmdPJdbcTemplate.query(query,namedParameters, 
				new RowMapper<CoAppBookInfoPojo>() {
					@Override
					public CoAppBookInfoPojo mapRow(ResultSet rs,int rowNum) throws SQLException {
						CoAppBookInfoPojo coAppBookInfoPojo = new CoAppBookInfoPojo();
						coAppBookInfoPojo.setCoAppBookInfoId(rs.getLong("CO_APP_BOOK_INFO_ID"));
						coAppBookInfoPojo.setCoApplicantId(rs.getLong("CO_APPLICANT_ID"));
						coAppBookInfoPojo.setCustBookInfoId(rs.getLong("CUST_BOOK_INFO_ID"));
						return coAppBookInfoPojo;
					} });
		if(Util.isEmptyObject(coAppBookInfoPojoList)) {
			logger.info("*** The control is inside the updateCoApplicantInfo in BookingFormServiceDaoImpl ***"+coAppBookInfoPojoList);
			coAppBookInfoPojoList.addAll(new ArrayList<CoAppBookInfoPojo>());
		}
		return coAppBookInfoPojoList;
	}

	@Override
	public Long updateCoApplicantInfo(Co_ApplicantInfo coApplInfo) {
		logger.info("*** The control is inside the updateCoApplicantInfo in BookingFormServiceDaoImpl ***"+coApplInfo);
		String query = SqlQuery.QRY_TO_UPDATE_CO_APPLICANT_INFO; 	
		Co_ApplicantPojo co_ApplicantPojo = new Co_ApplicantPojo();
		BeanUtils.copyProperties(coApplInfo, co_ApplicantPojo);
		Long result = (long) nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(co_ApplicantPojo));
		return result;
	}

	@Override
	public Long updateCoApplicantBookInfo(CoApplicentBookingInfo coApplicentBookingInfo, Long coAppBookInfoId, CoApplicentDetailsInfo coApplicentDetailsInfo) {
		logger.info("*** The control is inside the updateCoApplicantBookInfo in BookingFormServiceDaoImpl ***"+coApplicentBookingInfo);
		String query = SqlQuery.QRY_TO_UPDATE_CO_APPLICANT_BOOK_INFO;
		CoAppBookInfoPojo coAppBookInfoPojo = new CoAppBookInfoPojo();
		BeanUtils.copyProperties(coApplicentBookingInfo, coAppBookInfoPojo);
		coAppBookInfoPojo.setCoAppBookInfoId(coAppBookInfoId);
		//coAppBookInfoPojo.setType(type);
		coAppBookInfoPojo.setType(BookingFormMapper.getMetadataId(Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos())?Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos().get(0).getAddressMappingType())?Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos().get(0).getAddressMappingType().getMetaType())?coApplicentDetailsInfo.getAddressInfos().get(0).getAddressMappingType().getMetaType():"N/A":"N/A":"N/A"));
		Long result = (long) nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(coAppBookInfoPojo));
		return result;
	}

	@Override
	public Long deleteCoApplicantProffesionalDetails(Long coAppProfId) {
		logger.info("*** The control is inside the deleteCoApplicantProffesionalDetails in BookingFormServiceDaoImpl coAppProfId***"+coAppProfId);
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_CO_APP_PROFFESIONAL_DATA).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("CUST_PROFFISIONAL_ID",coAppProfId);
	    return (long) nmdPJdbcTemplate.update(query, namedParameters);
	}

	@Override
	public Long delteCoApplicantInfoDetails(Long coAppId) {
		logger.info("*** The control is inside the delteCoApplicantInfoDetails in BookingFormServiceDaoImpl coAppId***"+coAppId);
		String query = new StringBuilder(SqlQuery.QRT_TO_DELETE_CO_APP_INFO_DATA).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("CO_APPLICANT_ID",coAppId);
	    return (long) nmdPJdbcTemplate.update(query, namedParameters);
	}

	@Override
	public Long updateCustomerBookInfo(CustBookInfoPojo custBookInfoPojo) {
		logger.info("*** The control is inside the putCustomerDetails in BookingFormServiceDaoImpl  custBookInfoPojo ***"+custBookInfoPojo);
		String query = SqlQuery.QRY_TO_UPDATE_CUSTOMER_BOOKING_INFO;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(custBookInfoPojo));
		return (long) result;
	}

	@Override
	public int updateFinancialBookingFormAccountSummaryCost(FinBookingFormAccountSummaryPojo accountSummaryPojo) {
		logger.info("***** Control inside the BookingFormServiceDaoImpl.updateFinancialBookingFormAccountSummaryCost() *****");
		String query = SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_SUMMARY_PAYABLE_AMOUNT;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(accountSummaryPojo));
		return result;
	}
	
	@Override
	public int insertFinancialBookingFormAccountSummaryCost(FinBookingFormAccountSummaryPojo accountSummaryPojo) {
		logger.info("***** Control inside the BookingFormServiceDaoImpl.insertFinancialBookingFormAccountSummaryCost() *****");
		String query = SqlQuery.QRY_TO_INSERT_BOOKING_FORM_ACCOUNT_SUMMARY;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(accountSummaryPojo));
		return result;
	}
	
	@Override
	public int inActivePastCustomerSchemeDetails(FlatBookingSchemeMappingPojo inActivePastCustomerSchemepojo) {
		String query = SqlQuery.QRY_TO_UPDATE_FLAT_BOK_SCHM_MAPPING;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(inActivePastCustomerSchemepojo));
		return result;
	}
	
	@Override
	public List<FinSchemeTaxMappingPojo> getCustomerSchemeDetailsBySchemeName(CustomerSchemeInfo customerSchemeInfo,FlatBookingInfo flatBookingInfo) {
		logger.info("***** Control inside the BookingFormServiceDaoImpl.getCustomerSchemeDetailsBySchemeName() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String condition = customerSchemeInfo.getCondition() ==null?"":customerSchemeInfo.getCondition();
		String query = "";
		if(condition.equals("getPastSchemeDetails")) {
			query = SqlQuery.QRY_TO_GET_FLAT_BOK_SCHM_MAPPING_DETAILS1;
			namedParameters.addValue("BOOKING_FORM_ID", flatBookingInfo.getFlatBookingId(),Types.BIGINT);
		} else {
			query = SqlQuery.QRY_TO_GET_FLAT_BOK_SCHM_MAPPING_DETAILS_BY_SCHEME_NAME;
		}
		
		namedParameters.addValue("SCHEME_NAME", customerSchemeInfo.getSchemeName(),Types.VARCHAR);
		namedParameters.addValue("SITE_ID", customerSchemeInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
        
		List<List<FinSchemeTaxMappingPojo>> schemeMappingPojo = nmdPJdbcTemplate.query(query.toString(),
				namedParameters, new RowMapper<List<FinSchemeTaxMappingPojo>>() {
					public List<FinSchemeTaxMappingPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						final ResultSetMapper<FinSchemeTaxMappingPojo> resultSetMapper = new ResultSetMapper<FinSchemeTaxMappingPojo>();
						List<FinSchemeTaxMappingPojo> menuLevelMappingPojoLIST = null;
						try {
							menuLevelMappingPojoLIST = resultSetMapper.mapRersultSetToObject(rs,FinSchemeTaxMappingPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg, ex.getCause());
						}
						return menuLevelMappingPojoLIST;
					}
				});

		if (schemeMappingPojo.isEmpty()) {
			schemeMappingPojo.add(new ArrayList<FinSchemeTaxMappingPojo>());
		}
		return schemeMappingPojo.get(0);
	}

	@Override
	public Long updateCustomerOtherDetails(CustomerOtherDetailspojo customerOtherDetailspojo) {
		logger.info("*** The control is inside the putCustomerDetails in BookingFormServiceDaoImpl  custBookInfoPojo ***"+customerOtherDetailspojo);
		String query = SqlQuery.QRY_TO_UPDATE_CUSTOMER_OTHER_DETAILS_INFO;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(customerOtherDetailspojo));
		return (long) result;
	}

	@Override
	public Long updatePoaHolder(PoaHolderPojo PoaHolderPojo) {
		logger.info("*** The control is inside the putCustomerDetails in BookingFormServiceDaoImpl  custBookInfoPojo ***"+PoaHolderPojo);
		String query = SqlQuery.QRY_TO_UPDATE_CUSTOMER_POA_HOLDER_INFO;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(PoaHolderPojo));
		return (long) result;
	}
	
	@Override
	public Long updateReferencesMapping(@NonNull ReferencesMappingPojo referencesMappingPojo) {
		logger.info("**** The control is inside the referencesMapping in BookingFormServiceDaoImpl ****"+referencesMappingPojo);
		String query = SqlQuery.QRY_TO_UPDATE_REFERENCES_MAPPING;
		int result = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(referencesMappingPojo));
		return (long) result;
	}

	@Override
	public List<FlatBookingPojo> getOldFlatBookingDetails(FlatBookingInfo flatBookingInfo,ServiceRequestEnum requestEnum) {
		logger.info("**** The control is inside the getOldFlatBookingDetails in BookingFormServiceDaoImpl ****"+flatBookingInfo);
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_OLD_FLAT_BOOKING_DETAILS)
								 .append(" WHERE 1=1 ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		/* Getting results based on old salesforce booking id */
		if(ServiceRequestEnum.SALESFORCE_NEW_BOOKING.equals(requestEnum)) {
			sqlQuery.append(" AND FB.SALESFORCE_BOOKING_ID=:SALESFORCE_BOOKING_ID AND FB.STATUS_ID NOT IN (:STATUS_IDS)  ");
			sqlQuery.append(" ORDER BY FB.FLAT_BOOK_ID DESC ");//if SALESFORCE_BOOKING_ID is duplicate for booking records then take max flat book id 
			namedParameters.addValue("SALESFORCE_BOOKING_ID", flatBookingInfo.getOldBookingName());
			namedParameters.addValue("STATUS_IDS", Arrays.asList(Status.ACTIVE.status));
		} else if(ServiceRequestEnum.SALESFORCE_UPDATE_BOOKING.equals(requestEnum)) {
			sqlQuery.append(" AND FB.SALESFORCE_BOOKING_ID=:SALESFORCE_BOOKING_ID AND FB.FLAT_ID=:FLAT_ID");
			sqlQuery.append(" ORDER BY FB.FLAT_BOOK_ID DESC ");//if SALESFORCE_BOOKING_ID is duplicate for booking records then take max flat book id
			namedParameters.addValue("SALESFORCE_BOOKING_ID", flatBookingInfo.getBookingId());
			namedParameters.addValue("FLAT_ID", flatBookingInfo.getFlatId());
		}
		return resultSetMapper.getResult(FlatBookingPojo.class, sqlQuery.toString(), namedParameters);
	}

	@Override
	public void updateNewFlatBookingIdInTicketDetails(FlatBookingPojo oldFlatBookingPojo,BookingFormRequest newBookingrequest,MetadataId metadataId) {
		logger.info("**** The control is inside the updateNewFlatBookingIdInTicketDetails in BookingFormServiceDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder();
		if(MetadataId.FLAT_BOOKING.equals(metadataId)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_NEW_FLAT_BOOKING_ID_IN_TICKET);
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
			namedParameters.addValue("NEW_FLAT_BOOK_ID", newBookingrequest.getFlatBookingId());
			namedParameters.addValue("OLD_FLAT_BOOK_ID", oldFlatBookingPojo.getFlatBookingId());
		}else if(MetadataId.CUSTOMER.equals(metadataId)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_NEW_CUSTOMER_ID_IN_TICKET);
			namedParameters.addValue("NEW_CUSTOMER_ID", newBookingrequest.getCustomerId());
			namedParameters.addValue("FLAT_BOOK_ID", newBookingrequest.getFlatBookingId());
			namedParameters.addValue("STATUS_UPDATE_TYPE", metadataId.getId());
		}
		int updatedRows = nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
		logger.info("**** The control is inside the updateNewFlatBookingIdInTicketDetails "+updatedRows+" No. of rows updated in Ticket Table ****");
	}

	@Override
	public void updateNewCustomerIdInTicketConversation(BookingFormRequest newBookingrequest, Status status) {
		logger.info("**** The control is inside the updateNewCustomerIdInTicketConversation in BookingFormServiceDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder();
		if(Status.RAISED.equals(status)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_FROM_ID_IN_TICKET_CONVERSATION);	
		}else if(Status.RECEIVED.equals(status)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_TO_ID_IN_TICKET_CONVERSATION);
		}
		namedParameters.addValue("NEW_CUSTOMER_ID", newBookingrequest.getCustomerId());
		namedParameters.addValue("TYPE", MetadataId.CUSTOMER.getId());
		namedParameters.addValue("FLAT_BOOK_ID", newBookingrequest.getFlatBookingId());	
		int updatedRows = nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
		logger.info("**** The control is inside the updateNewCustomerIdInTicketConversation "+updatedRows+" No. of rows updated in Ticket Conversation Table ****");
	}

	@Override
	public void updateAppRegistrationStatus(BookingFormRequest newBookingrequest) {
		logger.info("**** The control is inside the updateAppRegistrationStatus in BookingFormServiceDaoImpl ****");
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_APP_REGISTRATION_STATUS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		namedParameters.addValue("CUST_ID", newBookingrequest.getCustomerId());
		int updatedRows = nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
		logger.info("**** The control is inside the updateAppRegistrationStatus "+updatedRows+" No. of rows updated in App Registration Table ****");
	}

	@Override
	public void updateNewFlatBookingIdInMessengerDetails(FlatBookingPojo oldFlatBookingPojo,BookingFormRequest newBookingrequest,Status status) {
		logger.info("**** The control is inside the updateNewFlatBookingIdInMessengerDetails in BookingFormServiceDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder();
		if(Status.CREATED.equals(status)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_NEW_FLAT_BOOKING_ID_IN_MESSENGER);
		}else if(Status.RAISED.equals(status)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_NEW_FLAT_BOOKING_ID_IN_MESSENGER_CONVER);
		}else if(Status.RECEIVED.equals(status)) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_UPDATE_NEW_FLAT_BOOKING_ID_IN_MESSENGER_CONV_VIEW_STATUS);
		}
		namedParameters.addValue("NEW_FLAT_BOOK_ID", newBookingrequest.getFlatBookingId());
		namedParameters.addValue("OLD_FLAT_BOOK_ID", oldFlatBookingPojo.getFlatBookingId());
		namedParameters.addValue("TYPE", MetadataId.FLAT_BOOKING.getId());
		int updatedRows = nmdPJdbcTemplate.update(sqlQuery.toString(), namedParameters);
		logger.info("**** The control is inside the updateNewFlatBookingIdInMessengerDetails "+updatedRows+" No. of rows updated in Messenger Tables ****");
	}

	/**
	 * @param query Pass SQL query
	 * @param namedParameters pass namedParameters object
	 * @param clazz pass your class to set the data
	 * @return list of collection object contains u requested class object
	 */
	private <T> List<T> getData(String query, MapSqlParameterSource namedParameters, Class<T> clazz) {
		logger.info(namedParameters.getValues());
		List<List<T>> list = nmdPJdbcTemplate.query(query.toString(), namedParameters,new ExtractDataFromResultSet<T>(clazz));
		if (list.isEmpty()) {
			list.add(new ArrayList<T>());
		}
		logger.info(" getData namedParameters Size of list "+list.get(0).size());
		return list.get(0);
	}

	@Override
	public List<NOCDetailsPojo> getNOCCheckListDetails(BookingFormRequest bookingFormRequest) {
		System.out.println("BookingFormServiceDaoImpl.getNOCCheckListDetails()");
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_NOC_VALIDATION_DETAILS);
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<NOCDetailsPojo> getNOCCheckList =  getData(sqlQuery.toString(), namedParameters, NOCDetailsPojo.class);
		return getNOCCheckList;
	}
	
	@Override
	public List<Map<String, Object>> loadExtraCarParkingDetails(BookingFormRequest bookingFormRequest) {
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_EXTRA_CAR_PARKING_DETAILS);
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		
		return nmdPJdbcTemplate.queryForList(sqlQuery.toString(), namedParameters);
	}
	
	public Long updateNocLetter(FileInfo info,CustomerPropertyDetailsInfo customerPropertyDetailsInfo,BookingFormRequest bookingFormRequestCopy) {
		logger.info("*** The control is inside the updateNocLetter in BookingFormServiceDaoImpl  FileInfo ***"+info);
		String query = SqlQueryTwo.QRY_TO_UPDATE_NOC_RELEASE;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("FLAT_BOOKING_ID",customerPropertyDetailsInfo.getFlatBookingId());
	    namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
	    namedParameters.addValue("CREATED_BY",bookingFormRequestCopy.getEmpId());
//	    namedParameters.addValue("DOC_LOCATION",info.getFilePath());
//	    namedParameters.addValue("URL_LOCATION",info.getUrl());
	    namedParameters.addValue("DOC_LOCATION",null);
	    namedParameters.addValue("URL_LOCATION",null);
	   // return (long) nmdPJdbcTemplate.update(query, namedParameters);
	    nmdPJdbcTemplate.update(query, namedParameters,keyHolder,new String[] { "NOC_RELEASE_ID" });
	    logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
	    return keyHolder.getKey().longValue();
		
	}

	@Override
	public List<NOCReleasePojo>  getNOCReleaseDetails(BookingFormRequest bookingFormRequest) {
		logger.info("*** The control is inside the getNOCShowStatus in BookingFormServiceDaoImpl   ***");
		String query = SqlQueryTwo.QRY_TO_GET_SHOW_NOC_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("FLAT_BOOKING_ID",bookingFormRequest.getFlatBookingId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		List<NOCReleasePojo> NocReleaseBena =  getData(query, namedParameters, NOCReleasePojo.class);
		logger.info(" getNOCShowStatus result "+NocReleaseBena);
		return NocReleaseBena;
	}

	@Override
	public Integer updateCustomerApplicationDetails(CustomerApplicationPojo custAppPojo) {
		logger.info("*** The control is inside the updateCustomerApplicationDetails in BookingFormServiceDaoImpl ***");
		String sqlQuery = SqlQueryTwo.QRY_TO_UPDATE_CUSTOMER_APPLICATION;
		return nmdPJdbcTemplate.update(sqlQuery, new BeanPropertySqlParameterSource(custAppPojo));
	}
	
	@Override
	public Long updateNocDocuments(FileInfo info,CustomerPropertyDetailsInfo customerPropertyDetailsInfo,BookingFormRequest bookingFormRequestCopy) {
		logger.info("*** The control is inside the updateNocLetter in BookingFormServiceDaoImpl  FileInfo ***"+info);
		String query = SqlQueryTwo.QRY_TO_UPDATE_NOC_DOCUMENTS;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("TYPE_ID",customerPropertyDetailsInfo.getNocReleaseId());
	    namedParameters.addValue("TYPE",MetadataId.NOC.getId());
	    namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
	    namedParameters.addValue("CREATED_BY",bookingFormRequestCopy.getEmpId());
	    namedParameters.addValue("DOC_LOCATION",info.getFilePath());
	    namedParameters.addValue("URL_LOCATION",info.getUrl());
	   // return (long) nmdPJdbcTemplate.update(query, namedParameters);
	    nmdPJdbcTemplate.update(query, namedParameters,keyHolder,new String[] { "NOC_DOCUMENTS_ID" });
	    logger.info("*** The primarykey generated for this record is ****" + keyHolder.getKey().longValue());
	    return keyHolder.getKey().longValue();
		
	}
	
	@Override
	public List<NOCReleasePojo>  getNOCDocumentsDetails(BookingFormRequest bookingFormRequest) {
		logger.info("*** The control is inside the getNOCShowStatus in BookingFormServiceDaoImpl   ***");
		String query = SqlQueryTwo.QRY_TO_GET_SHOW_NOC_DOC_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("FLAT_BOOKING_ID",bookingFormRequest.getFlatBookingId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		List<NOCReleasePojo> NocReleaseBena =  getData(query, namedParameters, NOCReleasePojo.class);
		logger.info(" getNOCShowStatus result "+NocReleaseBena);
		return NocReleaseBena;
	}

	@Override
	public boolean isAlreadyMailSentToLoanBanker(BookingFormRequest bookingFormRequest) {

		return false;
	}
	
	@Override
	public Integer isMailSentToBankerOnBooking(BookingFormRequest bookingFormRequest) {
		logger.info("**** The control is inside the isMailSentToBankerOnBooking in BookingFOrmDaoImpl ****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOKING_ID",bookingFormRequest.getFlatBookingId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		Integer count = nmdPJdbcTemplate.queryForObject(SqlQueryTwo.QRY_TO_GET_MAIL_SENT_TO_BANKER_OR_NOT, namedParameters, Integer.class);
		logger.info("***** The noumber of rows effected for the query is *********" + count);
		return count;
	}
	
	@Override
	public List<NOCDocumentsList>  getNOCDocumentsList(BookingFormRequest bookingFormRequest) {
		logger.info("*** The control is inside the getNOCDocumentsList in BookingFormServiceDaoImpl   ***");
		List<NOCDocumentsList> finalNocDocumentsList =new ArrayList<NOCDocumentsList>();
		StringBuilder query = new StringBuilder(SqlQueryTwo.QRY_TO_GET_NOC_DETAILS_LIST);
		StringBuilder queryBuilder = new StringBuilder(SqlQueryTwo.QRY_TO_GET_NOC_PENDING_DETAILS_LIST);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.status);
		if (Util.isNotEmptyObject(bookingFormRequest.getFlatBookingId())) {
			query.append(" AND FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
		    namedParameters.addValue("FLAT_BOOK_ID",bookingFormRequest.getFlatBookingId());
		}
		if (Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
			query.append(" AND S.SITE_ID = :SITE_ID ");
		    namedParameters.addValue("SITE_ID",bookingFormRequest.getSiteId());
		}
		if (Util.isNotEmptyObject(bookingFormRequest.getFlatId())) {
			query.append(" AND F.FLAT_ID = :FLAT_ID ");
		    namedParameters.addValue("FLAT_ID",bookingFormRequest.getFlatId());
		}
		StringBuilder query2=new StringBuilder(query);
		StringBuilder query3=new StringBuilder(queryBuilder);
		if (Util.isEmptyObject(bookingFormRequest.getShowStatusKey())
				|| "NOCGeneratedFlats".equalsIgnoreCase(bookingFormRequest.getShowStatusKey()) || "All".equalsIgnoreCase(bookingFormRequest.getShowStatusKey())) {
			query2.append(" AND NR.STATUS_ID=:STATUS_ID ");
			query2.append("  ORDER BY S.SITE_ID   ");
			List<NOCDocumentsList> NocReleaseBena =  getData(query2.toString(), namedParameters, NOCDocumentsList.class);
			finalNocDocumentsList.addAll(NocReleaseBena);
		} if (Util.isEmptyObject(bookingFormRequest.getShowStatusKey())
				|| "NOCPendingFlats".equalsIgnoreCase(bookingFormRequest.getShowStatusKey()) || "All".equalsIgnoreCase(bookingFormRequest.getShowStatusKey())) {
			query3.append(" AND FB.FLAT_BOOK_ID NOT IN (SELECT DISTINCT FLAT_BOOKING_ID FROM NOC_RELEASE WHERE STATUS_ID=:STATUS_ID) ");
			query3.append("  ORDER BY S.SITE_ID   ");
			List<NOCDocumentsList> NocReleaseBena =  getData(query3.toString(), namedParameters, NOCDocumentsList.class);
			finalNocDocumentsList.addAll(NocReleaseBena);
		}
		//query.append("  ORDER BY S.SITE_ID   ");
		//List<NOCDocumentsList> NocReleaseBena =  getData(query.toString(), namedParameters, NOCDocumentsList.class);
		logger.info(" getNOCDocumentsList result "+finalNocDocumentsList);
		return finalNocDocumentsList;
	}
	
	
	
	@Override
	public List<CustomerData> getCustomerData(CustomerInfo customerInfo, Status status) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder querySB = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_DATA)
				//.append(" WHERE F.FLAT_ID = :FLAT_ID ")
				.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
				if(status!=null)
				{
					querySB.append(" AND  FB.STATUS_ID = :STATUS_ID");
					namedParameters.addValue("STATUS_ID",status.getStatus(),Types.BIGINT);
				}
	            
		namedParameters.addValue("FLAT_BOOK_ID",customerInfo.getFlatBookingId(), Types.BIGINT);
		//logger.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****"+query+"\nParams :"+namedParameters.getValues());
		 List<List<CustomerData>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(querySB.toString(),namedParameters,
					new RowMapper<List<CustomerData>>() {
						public List<CustomerData> mapRow(ResultSet rs, int arg1) throws SQLException {
							logger.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<CustomerData> resultSetMapper = new ResultSetMapper<CustomerData>();
							List<CustomerData> customerPropertyDetailsPojoLIST = null;
							try {
								customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerData.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg,ex.getCause());
							}
							//logger.info("***** The customerPropertyDetailsPojoLIST objects  is *****" + customerPropertyDetailsPojoLIST);		
							return customerPropertyDetailsPojoLIST;
						}
					});
		
		if(customerPropertyDetailsPojoLists.isEmpty()) {
			customerPropertyDetailsPojoLists.add(new ArrayList<CustomerData>());
		}
		return customerPropertyDetailsPojoLists.get(0);
	}

	
	
	@Override
	public List<FlatCostPojo> getFlatCostByFlatBooking(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		logger.info("***** The control is inside the getFlatCost in BookingFormServiceDaoImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_COST)
				.append(" WHERE FLAT_BOOK_ID = :FLAT_BOOK_ID ")
				//.append(" AND STATUS_ID NOT IN  (:STATUS_ID) ")
				//.append(" AND STATUS_ID IN  (:STATUS_ID) ")
	             .toString();
		
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        //namedParameters.addValue("STATUS_ID", Arrays.asList(Status.REJECTED.getStatus(),Status.INACTIVE.getStatus()), Types.BIGINT);
     //   namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.BIGINT);
        //changed query taking only active and pending flat cost details
        logger.info("**** THE QRY_TO_GET_FLAT_COST IS *****"+query);
		
        List<List<FlatCostPojo>> FlatCostPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FlatCostPojo>>() {
					public List<FlatCostPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						//logger.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatCostPojo> resultSetMapper = new ResultSetMapper<FlatCostPojo>();
						List<FlatCostPojo> FlatCostPojoLIST = null;
						try {
							FlatCostPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatCostPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						//logger.info("***** The FlatCostPojoLIST objects  is *****" + FlatCostPojoLIST);		
						return FlatCostPojoLIST;
					}
				});
		
        //logger.info("*** The FlatCostPojoLists is *****"+FlatCostPojoLists);
		if(FlatCostPojoLists.isEmpty()) {
			FlatCostPojoLists.add(new ArrayList<FlatCostPojo>());
		}
		logger.info("**** The FlatCostPojoLists is ****"+FlatCostPojoLists.get(0));
		return FlatCostPojoLists.get(0);
	}

	@Override
	public List<EmployeeDetailsMailPojo> getEmployeeDetails(CustomerDetailsPojo customerDetailsPojo) {
		logger.info("***** The control is inside the getEmployeeDetailsForMail in BookingFormServiceDaoImpl ******");
		List<EmployeeDetailsMailPojo> list =null;
		//query = new StringBuffer(FinancialQuerys.QRY_TO_GET_EMP_EMAILS_TO_FOR_RATE_OF_INTEREST);
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_EMPLOYEE_DETAILS);
		List<Long> deptId=new ArrayList<>();
		deptId.add(Department.CRM.getId());
	//	deptId.add(Department.CRM_TECH.getId());
		List<Long> roleIds=new ArrayList<>();
		roleIds.add(Roles.SR_CRM_EXECUTIVE.getId());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", customerDetailsPojo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("ROLE_ID", roleIds, Types.BIGINT);
		namedParameters.addValue("DEPT_IDS", deptId, Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		list=resultSetMapper.getResult(EmployeeDetailsMailPojo.class, sqlQuery.toString(), namedParameters);
		logger.info("***** List<EmployeeDetailsMailPojo> is *****" + list);
	    return list;
	}
	
	@Override
	public List<CustomerKYCDocumentSubmitedInfo> getKycDocumentsList(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getEmployeeDetailsForMail in BookingFormServiceDaoImpl ******");
		List<CustomerKYCDocumentSubmitedInfo> list =null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CUSTOMER_KYC_DETAILS);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		list=resultSetMapper.getResult(CustomerKYCDocumentSubmitedInfo.class, sqlQuery.toString(), namedParameters);
		logger.info("***** getKycDocumentsList is *****" + list);
	    return list;
	}
	
	@Override
	public List<AminititesInfraDetails> getAminities(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getEmployeeDetailsForMail in BookingFormServiceDaoImpl ******");
		List<AminititesInfraDetails> list =null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_AMINITIES_DETAILS);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		namedParameters.addValue("FLAT_ID",bookingFormRequest.getFlatId(), Types.BIGINT);
		list=resultSetMapper.getResult(AminititesInfraDetails.class, sqlQuery.toString(), namedParameters);
		logger.info("***** getAminities is *****" + list);
	    return list;
	}
	
	@Override
	public List<CustomerData> getflatDetailsByflat(BookingFormRequest bookingFormRequest) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder querySB = new StringBuilder(SqlQueryTwo.QRY_TO_GET_FLAT_DETAILS)
				.append(" WHERE F.FLAT_ID = :FLAT_ID AND F.STATUS_ID=:STATUS_ID AND BL.STATUS_ID=:STATUS_ID AND FD.STATUS_ID=:STATUS_ID  ");
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		namedParameters.addValue("FLAT_ID",bookingFormRequest.getFlatId(), Types.BIGINT);
	           
		//logger.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****"+query+"\nParams :"+namedParameters.getValues());
		 List<List<CustomerData>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(querySB.toString(),namedParameters,
					new RowMapper<List<CustomerData>>() {
						public List<CustomerData> mapRow(ResultSet rs, int arg1) throws SQLException {
							logger.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<CustomerData> resultSetMapper = new ResultSetMapper<CustomerData>();
							List<CustomerData> customerPropertyDetailsPojoLIST = null;
							try {
								customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerData.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg,ex.getCause());
							}
							//logger.info("***** The customerPropertyDetailsPojoLIST objects  is *****" + customerPropertyDetailsPojoLIST);		
							return customerPropertyDetailsPojoLIST;
						}
					});
		
		if(customerPropertyDetailsPojoLists.isEmpty()) {
			customerPropertyDetailsPojoLists.add(new ArrayList<CustomerData>());
		}
		return customerPropertyDetailsPojoLists.get(0);
	}
	
	
	@Override
	public List<CustomerData> getNonBookedDetails(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getFlatDetailsByCustomerName method in BookingFormServiceDaoImpl *******");
		List<ReferedCustomer> referredCusts = new ArrayList<ReferedCustomer>();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder sqlQuery = null;
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		if (Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())& "nonBookedFlats".equalsIgnoreCase(bookingFormRequest.getRequestUrl())) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_NON_BOOKED_FLATS);
			if (Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
				sqlQuery.append("  AND ST.SITE_ID IN (:SITE_IDS) ");
				namedParameters.addValue("SITE_IDS", bookingFormRequest.getSiteId(), Types.INTEGER);
			}
			if(Util.isNotEmptyObject(bookingFormRequest.getBlockDetId())) {
				sqlQuery.append(" AND  BLCKDT.BLOCK_DET_ID IN (:BLOCK_DET_ID) ");
				namedParameters.addValue("BLOCK_DET_ID",bookingFormRequest.getBlockDetId(), Types.INTEGER);
			}
			if(Util.isNotEmptyObject(bookingFormRequest.getFloorDetId())) {
				sqlQuery.append(" AND FLRD.FLOOR_DET_ID IN (:FLOOR_DET_ID) ");
				namedParameters.addValue("FLOOR_DET_ID",bookingFormRequest.getFloorDetId(), Types.INTEGER);
			}
			sqlQuery.append(" ORDER BY FT.FLAT_NO ");
		}
		if (Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())& "nonBookedBlocks".equalsIgnoreCase(bookingFormRequest.getRequestUrl())) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_NON_BOOKED_BLOCKS);
			if (Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
				sqlQuery.append("  AND ST.SITE_ID IN (:SITE_IDS)  ORDER BY B.NAME ");
				namedParameters.addValue("SITE_IDS", bookingFormRequest.getSiteId(), Types.INTEGER);
			}
		}
		if (Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())& "nonBookedFloor".equalsIgnoreCase(bookingFormRequest.getRequestUrl())) {
			sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_NON_BOOKED_FLOORS);
			if (Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
				sqlQuery.append("  AND ST.SITE_ID IN (:SITE_IDS) ");
				namedParameters.addValue("SITE_IDS", bookingFormRequest.getSiteId(), Types.INTEGER);
			}
			if (Util.isNotEmptyObject(bookingFormRequest.getBlockDetId())) {
				sqlQuery.append(" AND  BLCKDT.BLOCK_DET_ID IN (:BLOCK_DET_ID) ");
				namedParameters.addValue("BLOCK_DET_ID", bookingFormRequest.getBlockDetId(), Types.INTEGER);
			}
			sqlQuery.append("  ORDER BY FLRD.FLOOR_DET_ID ");
		}
		
		 List<List<CustomerData>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(sqlQuery.toString(),namedParameters,
					new RowMapper<List<CustomerData>>() {
						public List<CustomerData> mapRow(ResultSet rs, int arg1) throws SQLException {
							logger.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<CustomerData> resultSetMapper = new ResultSetMapper<CustomerData>();
							List<CustomerData> customerPropertyDetailsPojoLIST = null;
							try {
								customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerData.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								logger.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg,ex.getCause());
							}
							//logger.info("***** The customerPropertyDetailsPojoLIST objects  is *****" + customerPropertyDetailsPojoLIST);		
							return customerPropertyDetailsPojoLIST;
						}
					});
		
		if(customerPropertyDetailsPojoLists.isEmpty()) {
			customerPropertyDetailsPojoLists.add(new ArrayList<CustomerData>());
		}
		return customerPropertyDetailsPojoLists.get(0);
	}
		
	@Override
	public List<CustomerAddressPojo> getcityList(CustomerInfo customerData) {
		logger.info("***** The control is inside the getcityList in BookingFormServiceDaoImpl ******");
		List<CustomerAddressPojo> list = null;

		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CITY_LIST);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		if (Util.isNotEmptyObject(customerData.getStateId())) {
			sqlQuery.append("WHERE C.STATE_ID= :STATE_ID");
			namedParameters.addValue("STATE_ID", customerData.getStateId());
		}

		list = resultSetMapper.getResult(CustomerAddressPojo.class, sqlQuery.toString(), namedParameters);
		logger.info("***** List<CustomerAddressPojo> is *****" + list);
		return list;
	}
	
	@Override
	public List<CustomerAddressPojo> getCountryList() {
		logger.info("***** The control is inside the getCountryList in BookingFormServiceDaoImpl ******");
		List<CustomerAddressPojo> list = null;
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_COUNTRY_LIST);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		sqlQuery.append("WHERE STATUS_ID= :STATUS_ID");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		list = resultSetMapper.getResult(CustomerAddressPojo.class, sqlQuery.toString(), namedParameters);
		logger.info("***** List<CustomerAddressPojo> is *****" + list);
		return list;
	}
	@Override
	public List<FinSchemePojo> getFinSchemes(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getEmployeeDetailsForMail in BookingFormServiceDaoImpl ******");
		List<FinSchemePojo> list =null;
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_SCHEMES);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    sqlQuery.append("WHERE F.STATUS_ID= :STATUS_ID AND FSTM.STATUS_ID= :STATUS_ID ");
	    if (Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
			sqlQuery.append(" AND FSTM.SITE_ID=:SITE_ID ");
			namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(), Types.INTEGER);
		}
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		list=resultSetMapper.getResult(FinSchemePojo.class, sqlQuery.toString(), namedParameters);
		logger.info("***** List<EmployeeDetailsMailPojo> is *****" + list);
	    return list;
	}
	
	@Override
	public  List<FlatBookingPojo> getSalesForcesBookingIds(BookingFormRequest bookingFormRequest) {
		logger.info("***** The control is inside the getSalesForcesBookingIds in BookingFormServiceDaoImpl ******");
		List<FlatBookingPojo> list =null;
		StringBuilder sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_SALSEFORCEBOOKINGS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    sqlQuery.append("WHERE FLAT.STATUS_ID= :STATUS_ID  ");
	    if (Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
			sqlQuery.append(" AND SITE.SITE_ID=:SITE_ID ");
			namedParameters.addValue("SITE_ID", bookingFormRequest.getSiteId(), Types.INTEGER);
		}
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus());
		list=resultSetMapper.getResult(FlatBookingPojo.class, sqlQuery.toString(), namedParameters);
		logger.info("***** List<EmployeeDetailsMailPojo> is *****" + list);
	    return list;
	}
	
	public AddressPojo  formAdreesObject(AddressPojo addressPojo)
	{
		Long cityId= null ;
		Long stateId   = null ;
		Long countryId = null ;
		try {
			if (Util.isEmptyObject(addressPojo.getCountryId())) {
			   countryId = getcountryIdByName(addressPojo.getCountry());
			}else
			{
				countryId=addressPojo.getCountryId();
			}
//			if (Util.isEmptyObject(addressPojo.getCityId())) {
//				cityId = getcityIdByName(addressPojo.getCity());
//			}else
//			{
//				cityId=addressPojo.getCityId();
//			}
			if (Util.isEmptyObject(addressPojo.getStateId())) {
			    stateId = getstateIdByName(addressPojo.getState());
			}else
			{
				stateId=addressPojo.getStateId();
			}
			addressPojo.setCountryId(countryId);
			//addressPojo.setCityId(cityId);
			addressPojo.setStateId(stateId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addressPojo;
		
	}
	@Override
	public  Long getcountryIdByName(String name) {
		logger.info("***** The control is inside the getcountryIdByName in BookingFormServiceDaoImpl ******");
		StringBuilder sqlQuery =null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
	    if(Util.isNotEmptyObject(name)) {
	    	sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_COUNTRY_ID_BY_COUNTRY_NAME);
	    sqlQuery.append(" WHERE C.STATUS_ID= :STATUS_ID AND lOWER(COUNTRY_NAME) LIKE '%"+ name.toLowerCase()+"%' ");
	    }
	    long number=0;
		try {
			number = nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
		} catch (Exception e) {
			number=0;
			e.printStackTrace();
		}
	    return number;
	}
	@Override
	public  Long getcityIdByName(String name) {
		logger.info("***** The control is inside the getcityIdByName in BookingFormServiceDaoImpl ******");
		StringBuilder sqlQuery = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    if(Util.isNotEmptyObject(name)) {
	    	sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_CITY_ID_BY_CITY_NAME);
	    sqlQuery.append(" WHERE  lOWER(CITY_NAME) LIKE '%"+ name.toLowerCase()+"%' ");
	    }
	    long number=0;
		try {
			number = nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
		} catch (Exception e) {
			number=0;
			e.printStackTrace();
		}
	    return number;
	}
	@Override
	public  Long getstateIdByName(String name) {
		logger.info("***** The control is inside the getstateIdByName in BookingFormServiceDaoImpl ******");
		StringBuilder sqlQuery = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    if(Util.isNotEmptyObject(name)) {
	    	sqlQuery = new StringBuilder(SqlQueryTwo.QRY_TO_GET_STATE_ID_BY_STATE_NAME);
	    sqlQuery.append(" WHERE  lOWER(STATE_NAME) LIKE '%"+ name.toLowerCase()+"%' ");
	    }
	    long number=0;
		try {
			number = nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters, Long.class);
		} catch (Exception e) {
			number=0;
			e.printStackTrace();
		}
	    return number;
	}
	
	
}


