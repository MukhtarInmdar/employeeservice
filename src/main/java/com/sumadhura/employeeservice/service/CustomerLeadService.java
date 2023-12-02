/**
 * 
 */
package com.sumadhura.employeeservice.service;




import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.sumadhura.employeeservice.exception.InSufficeientInputException;
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
 * CustomerLeadService Interface provides CustomerLeadFormRequest specific services.
 * 
 * @author Inamdar
 * @since 20.01.2023
 * @time 05:30PM
 */
public interface CustomerLeadService {

	public List<CustomerLeadCountPojo> getCustomerLeadMISCount(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException ;
	public List<CustomerLeadPojo> getCustomerLeadMIS(CustomerLeadFormRequest customerLeadFormRequest, String leadSubStatusId) throws InSufficeientInputException ;
	public List<CustomerLeadSiteVisitPojo> getCustomerLeadSiteVisitList(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException ;
	public List<CustomerSourceMasterPojo> getCustomerSourceMaster()throws InSufficeientInputException;
	public List<CustomerInactiveRemarksPojo> getCustomerInactiveRemarksMaster() throws InSufficeientInputException;
	public List<CustomerLeadCommentsPojo> getCustomerLeadComments(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException;
	public List<CustomerProjectPreferedLocationMasterPojo> getCustomerProjectPreferedLocationMaster()throws InSufficeientInputException;
	public List<CustomerTimeFrameToPurchaseMasterPojo> getCustomerTimeFrameTOPurchaseMaster()throws InSufficeientInputException;
	public List<CustomerHousingRequirementMasterPojo> getCustomerHousingRequirementMaster()throws InSufficeientInputException;
	public List<CustomerLeadSubStatusMasterPojo> getCustomerLeadSubStatusMaster()throws InSufficeientInputException;
	public List<CustomerMarketingTypeMasterPojo> getCustomerMarketingTypeMaster()throws InSufficeientInputException;
	public List<CustomerLeadPojo> getCustomerLead(CustomerLeadFormRequest customerLeadFormRequest)throws InSufficeientInputException;
	public int saveCustomerLead(CustomerLeadFormRequest customerLeadFormRequest)throws InSufficeientInputException, DataIntegrityViolationException, Exception;
	public int deleteCustomerLead(CustomerLeadFormRequest customerLeadFormRequest)throws InSufficeientInputException;
	public int saveCustomerLeadSiteVisit(CustomerLeadFormRequest customerLeadFormRequest) throws InSufficeientInputException, DataIntegrityViolationException ,Exception; 
	
}
