/**
 * 
 */
package com.sumadhura.employeeservice.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
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
import com.sumadhura.employeeservice.service.CustomerLeadServiceImpl;
import com.sumadhura.employeeservice.service.dto.CustomerLeadFormRequest;

/**
 * CustomerLeadRestService class provides CustomerLeadFormRequest specific Service .
 * services.
 * 
 * @author Inamdar
 * @since 20.01.2023
 * @time 12:30PM
 */

@Path("/customerLead")
public class CustomerLeadRestService {

	
	@Autowired(required = true)
	@Qualifier("CustomerLeadServiceImpl")
	private CustomerLeadServiceImpl customerLeadServiceImpl;
	private Logger logger = Logger.getLogger(CustomerLeadRestService.class);
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadMISCount.spring")
	public Result getCustomerlLeadMIS(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerlLeadMIS in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerLeadCountPojo> list = customerLeadServiceImpl.getCustomerLeadMISCount(customerLeadFormRequest);
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead View details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadMIS.spring")
	public Result getCustomerLeadGenerated(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerLeadGenerated in CustomerLeadRestService ********");
		Result result = new Result();
		String leadStatus = customerLeadFormRequest.getLeadStatus();
		String leadSubStatusId = "";
		if(leadStatus.equals("Lead Generated")) {
			leadSubStatusId = "0,1,2";
		}
		else if(leadStatus.equals("Site Visits Converted"))
		{
			leadSubStatusId = "3,4,5";
		}
		else if(leadStatus.equals("Booking Converted"))
		{
			leadSubStatusId = "6,7,8";
		}
		
		List<CustomerLeadPojo> list = customerLeadServiceImpl.getCustomerLeadMIS(customerLeadFormRequest,  leadSubStatusId);
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead View details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadSiteVisitList.spring")
	public Result getCustomerLeadSiteVisitList(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerLeadNewSiteVisit in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerLeadSiteVisitPojo> list = customerLeadServiceImpl.getCustomerLeadSiteVisitList(customerLeadFormRequest);
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead View details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadSave.spring")
	public Result saveCustomerlLead(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException, DataIntegrityViolationException , Exception {
		logger.info("******* The control inside of the saveCustomerlLead in CustomerLeadRestService ********");
		Result result = new Result();
		List<String> errorMsgs = null;
		try {
			int  cnt = customerLeadServiceImpl.saveCustomerLead(customerLeadFormRequest);
			if (cnt > 0) {

				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription("Customer lead save successfully");	
			} else {
				errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"Error while getting Customer Lead/Customer Lead Save details - The Invalid Input is given for requested service.");
				throw new InSufficeientInputException(errorMsgs);
			}
		}
		catch (DataIntegrityViolationException e) {
			errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead Save details - User already exist with Mobile Number or Email Id");
			throw new InSufficeientInputException(errorMsgs);
        }
		catch (Exception e) {
			errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead Save details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
        }
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadSiteVisitSave.spring")
	public Result saveCustomerlLeadSiteVisit(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException, DataIntegrityViolationException , Exception {
		logger.info("******* The control inside of the saveCustomerlLeadSiteVisit in CustomerLeadRestService ********");
		Result result = new Result();
		List<String> errorMsgs = null;
		try {
			int  cnt = customerLeadServiceImpl.saveCustomerLead(customerLeadFormRequest);
			if (cnt > 0) {

				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription("Customer lead site visit save successfully");	
			} else {
				errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"Error while getting Customer Lead/Customer Lead Save details - The Invalid Input is given for requested service.");
				throw new InSufficeientInputException(errorMsgs);
			}
		}
		catch (DataIntegrityViolationException e) {
			errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead Save details - User already exist with Mobile Number or Email Id");
			throw new InSufficeientInputException(errorMsgs);
        }
		catch (Exception e) {
			errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead Save details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
        }
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadDelete.spring")
	public Result deleteCustomerLead(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerSourceMaster in CustomerLeadRestService ********");
		Result result = new Result();
		int  cnt = customerLeadServiceImpl.deleteCustomerLead(customerLeadFormRequest);
		if (cnt > 0) {
			
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription("Customer lead deleted successfully");	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead Save details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadView.spring")
	public Result getCustomerlLeadView(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the saveCustomerlLead in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerLeadPojo> list = customerLeadServiceImpl.getCustomerLead(customerLeadFormRequest);
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Customer Lead View details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerSource.spring")
	public Result getCustomerSourceMaster()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getSourceMasterDetails in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerSourceMasterPojo> list = customerLeadServiceImpl.getCustomerSourceMaster();
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Project Prefered Location details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerProjectPreferedLocation.spring")
	public Result getCustomerProjectPreferedLocation()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerProjectPreferedLocation in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerProjectPreferedLocationMasterPojo> list = customerLeadServiceImpl.getCustomerProjectPreferedLocationMaster();
		
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Project Prefered Location details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerTimeFrameToPurchase.spring")
	public Result getCustomerTimeFrameToPurchase()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerTimeFrameToPurchase in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerTimeFrameToPurchaseMasterPojo> list = customerLeadServiceImpl.getCustomerTimeFrameTOPurchaseMaster();
		
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Time Frame To Purchase details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerHousingRequirement.spring")
	public Result getCustomerHousingRequirement()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerHousingRequirement in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerHousingRequirementMasterPojo> list = customerLeadServiceImpl.getCustomerHousingRequirementMaster();
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Housing Requirement details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadSubStatus.spring")
	public Result getCustomerLeadSubStatus()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerLeadSubStatus in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerLeadSubStatusMasterPojo> list = customerLeadServiceImpl.getCustomerLeadSubStatusMaster();
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Lead Sub Status details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerMarketingType.spring")
	public Result getCustomerMarketingType()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerMarketingType in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerMarketingTypeMasterPojo> list = customerLeadServiceImpl.getCustomerMarketingTypeMaster();
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Marketting Type details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerInactiveRemarks.spring")
	public Result getCustomerInactiveRemarks()
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerInactiveRemarks in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerInactiveRemarksPojo> list = customerLeadServiceImpl.getCustomerInactiveRemarksMaster();
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Marketting Type details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customerLeadComments.spring")
	public Result getCustomerLeadComments(CustomerLeadFormRequest customerLeadFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getCustomerLeadComments in CustomerLeadRestService ********");
		Result result = new Result();
		List<CustomerLeadCommentsPojo> list = customerLeadServiceImpl.getCustomerLeadComments(customerLeadFormRequest);
		if (list != null && list.size() > 0) {
			
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());	
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer Lead/Marketting Type details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		return result;
	}
	
	
	
}