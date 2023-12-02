
package com.sumadhura.employeeservice.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dao.CustomerLeadServiceDaoImpl;
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

/**
 * CustomerLeadServiceImpl class provides Implementation for
 * CustomerLeadService.
 * 
 * @author Inamdar
 * @since 23.01.2023
 * @time 11:47AM
 */
@Service("CustomerLeadServiceImpl")
public class CustomerLeadServiceImpl implements CustomerLeadService {

	@Autowired(required = true)
	@Qualifier("CustomerLeadServiceDaoImpl")
	private CustomerLeadServiceDaoImpl customerLeadServiceDaoImpl;
	
	private Logger logger = Logger.getLogger(CustomerLeadServiceImpl.class);
	
	
	@Override
	public List<CustomerLeadPojo> getCustomerLeadMIS(CustomerLeadFormRequest customerLeadFormRequest, String leadSubStatusId) throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLeadGenerated() *****");
		List<CustomerLeadPojo> list = customerLeadServiceDaoImpl.getCustomerLeadMIS(customerLeadFormRequest,  leadSubStatusId);
		
		
		return list;
	}
	
	
	@Override
	public List<CustomerLeadCountPojo> getCustomerLeadMISCount(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLeadMIS() *****");
		List<CustomerLeadCountPojo> list = customerLeadServiceDaoImpl.getCustomerLeadMISCount(customerLeadFormRequest);
		
		
		return list;
	}
	
	@Override
	public List<CustomerLeadSiteVisitPojo> getCustomerLeadSiteVisitList(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLeadNewSiteVisit() *****");
		List<CustomerLeadSiteVisitPojo> list = customerLeadServiceDaoImpl.getCustomerLeadSiteVisitList(customerLeadFormRequest);
		
		
		return list;
	}
	
	
	@Override
	public List<CustomerLeadPojo> getCustomerLead(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLead() *****");
		List<CustomerLeadPojo> list = customerLeadServiceDaoImpl.getCustomerLead(customerLeadFormRequest);
		
		
		return list;
	}
	
	@Override
	public int saveCustomerLead(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException, DataIntegrityViolationException ,Exception {
		logger.info("***** Control inside the CustomerLeadServiceImpl.saveCustomerLead() *****");
		Result result = new Result();
		int count = customerLeadServiceDaoImpl.saveCustomerLead(customerLeadFormRequest);
		
		return count;
	}
	
	@Override
	public int saveCustomerLeadSiteVisit(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException, DataIntegrityViolationException ,Exception {
		logger.info("***** Control inside the CustomerLeadServiceImpl.saveCustomerLeadSiteVisit() *****");
		Result result = new Result();
		int count = customerLeadServiceDaoImpl.saveCustomerLeadSiteVisit(customerLeadFormRequest);
		
		return count;
	}
	
	
	@Override
	public int deleteCustomerLead(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.deleteCustomerLead() *****");
		Result result = new Result();
		int count = customerLeadServiceDaoImpl.deleteCustomerLead(customerLeadFormRequest);
		
		return count;
	}
	
	@Override
	public List<CustomerSourceMasterPojo> getCustomerSourceMaster() {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getSourceMaster() *****");
		List<CustomerSourceMasterPojo> list = customerLeadServiceDaoImpl.getCustomerSourceMaster();
		
		return list;
	}

	@Override
	public List<CustomerProjectPreferedLocationMasterPojo> getCustomerProjectPreferedLocationMaster()
			throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerProjectPreferedLocationMaster() *****");
		List<CustomerProjectPreferedLocationMasterPojo> list = customerLeadServiceDaoImpl.getCustomerProjectPreferedLocationMaster();
		
		return list;
	}

	@Override
	public List<CustomerTimeFrameToPurchaseMasterPojo> getCustomerTimeFrameTOPurchaseMaster()
			throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerTimeFrameTOPurchaseMaster() *****");
		List<CustomerTimeFrameToPurchaseMasterPojo> list = customerLeadServiceDaoImpl.getCustomerTimeFrameTOPurchaseMaster();
		
		return list;
	}

	@Override
	public List<CustomerHousingRequirementMasterPojo> getCustomerHousingRequirementMaster() throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerHousingRequirementMaster() *****");
		List<CustomerHousingRequirementMasterPojo> list = customerLeadServiceDaoImpl.getCustomerHousingRequirementMaster();
		
		return list;
	}

	@Override
	public List<CustomerLeadSubStatusMasterPojo> getCustomerLeadSubStatusMaster() throws InSufficeientInputException {
		// TODO Auto-generated method stub
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLeadSubStatusMaster() *****");
		List<CustomerLeadSubStatusMasterPojo> list = customerLeadServiceDaoImpl.getCustomerLeadSubStatusMaster();
		
		return list;
	}

	@Override
	public List<CustomerMarketingTypeMasterPojo> getCustomerMarketingTypeMaster() throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLeadSubStatusMaster() *****");
		List<CustomerMarketingTypeMasterPojo> list = customerLeadServiceDaoImpl.getCustomerMarketingTypeMaster();
		
		return list;
	}

	@Override
	public List<CustomerInactiveRemarksPojo> getCustomerInactiveRemarksMaster() throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerInactiveRemarksMaster() *****");
		List<CustomerInactiveRemarksPojo> list = customerLeadServiceDaoImpl.getCustomerInactiveRemarksMaster();
		
		return list;
	}
	
	@Override
	public List<CustomerLeadCommentsPojo> getCustomerLeadComments(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException {
		logger.info("***** Control inside the CustomerLeadServiceImpl.getCustomerLeadComments() *****");
		List<CustomerLeadCommentsPojo> list = customerLeadServiceDaoImpl.getCustomerLeadComments(customerLeadFormRequest);
		
		return list;
	}


}
