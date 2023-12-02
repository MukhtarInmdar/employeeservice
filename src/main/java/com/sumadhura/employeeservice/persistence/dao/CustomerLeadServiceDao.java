/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

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
 * CustomerLeadServiceDao Interface provides CustomerLeadFormRequest Ticketing specific services.
 * 
 * @author Inamdar
 * @since 20.01.2023
 * @time 05:50PM
 */
public interface CustomerLeadServiceDao {
	
	public List<CustomerLeadCountPojo> getCustomerLeadMISCount(CustomerLeadFormRequest customerLeadFormRequest);
	public List<CustomerLeadPojo> getCustomerLeadMIS(CustomerLeadFormRequest customerLeadFormRequest, String leadSubStatusId);
	public List<CustomerLeadSiteVisitPojo> getCustomerLeadSiteVisitList(CustomerLeadFormRequest customerLeadFormRequest);
	public List<CustomerSourceMasterPojo> getCustomerSourceMaster();
	public List<CustomerInactiveRemarksPojo> getCustomerInactiveRemarksMaster();
	public List<CustomerLeadCommentsPojo> getCustomerLeadComments(CustomerLeadFormRequest customerLeadFormRequest);
	
	public List<CustomerProjectPreferedLocationMasterPojo> getCustomerProjectPreferedLocationMaster();
	public List<CustomerTimeFrameToPurchaseMasterPojo> getCustomerTimeFrameTOPurchaseMaster();
	public List<CustomerHousingRequirementMasterPojo> getCustomerHousingRequirementMaster();
	public List<CustomerLeadSubStatusMasterPojo> getCustomerLeadSubStatusMaster();
	public List<CustomerMarketingTypeMasterPojo> getCustomerMarketingTypeMaster();
	public List<CustomerLeadPojo> getCustomerLead(CustomerLeadFormRequest customerLeadFormRequest);
	public int saveCustomerLead(CustomerLeadFormRequest customerLeadFormRequest) throws DataIntegrityViolationException , Exception;
	public int deleteCustomerLead(CustomerLeadFormRequest customerLeadFormRequest);
	public int saveCustomerLeadSiteVisit(CustomerLeadFormRequest customerLeadFormRequest) throws DataIntegrityViolationException , Exception;	
	
}
