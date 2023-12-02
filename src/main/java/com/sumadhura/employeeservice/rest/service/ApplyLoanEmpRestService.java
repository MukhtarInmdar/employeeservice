package com.sumadhura.employeeservice.rest.service;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dto.ApplyLoanLeadDetailsPojo;
import com.sumadhura.employeeservice.service.ApplyLaonService;
import com.sumadhura.employeeservice.util.Util;
import com.sun.istack.NotNull;

/**
 * Customer applied load details service
 * 
 * @author Aniket Chavan
 * @since 20-07-2022
 * @time 5:50
 * 
 */
@Path("/loan")
public class ApplyLoanEmpRestService {

	private final Logger LOG = Logger.getLogger(ApplyLoanEmpRestService.class);

	@Autowired(required = true)
	private ApplyLaonService applyLoanService;

	/**
	 * loading all the lead details in list
	 * for banker login list will load default
	 * for internal team after search list will load
	 */
	@POST
	@Path("/getLoanAppliedLeadDetailsList.spring")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Result getLoanAppliedLeadDetails(@NotNull LoanRequest loanRequest) throws InSufficeientInputException {
		LOG.info("*** The control is inside of the getLoanAppliedLeadDetails in ApplyLoanEmpRestService *** "+ loanRequest);
		Result result = new Result();
		if (Util.isNotEmptyObject(loanRequest)) {
			List<ApplyLoanLeadDetailsPojo> object = applyLoanService.getLoanAppliedLeadDetails(loanRequest);
			result.setResponseObjList(object);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	/**
	 *Loading lead details by id 
	 */
	@POST
	@Path("/loadLoanAppliedLeadDetailsById.spring")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Result loadLoanAppliedLeadDetailsById(@NotNull LoanRequest loanRequest) throws InSufficeientInputException {
		LOG.info("*** The control is inside of the getLoanAppliedLeadDetails in ApplyLoanEmpRestService *** "+ loanRequest);
		Result result = new Result();
		if (Util.isNotEmptyObject(loanRequest)) {
			List<ApplyLoanLeadDetailsPojo> object = applyLoanService.loadLoanAppliedLeadDetailsById(loanRequest);
			result.setResponseObjList(object);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	/**
	 * update banker seen status 
	 */
	@POST
	@Path("/updateBankerLeadSeenStatus.spring")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Result updateLeadSeenStatus(@NotNull LoanRequest loanRequest) throws InSufficeientInputException {
		LOG.info("*** The control is inside of the updateLeadSeenStatus in ApplyLoanEmpRestService *** "+ loanRequest);
		Result result = null;
		if (Util.isNotEmptyObject(loanRequest) && Util.isNotEmptyObject(loanRequest.getCustomerLoanEOIDetailsId())) {
			result = applyLoanService.updateLeadSeenStatus(loanRequest);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	/**
	 * update lead details like lead staus and comments
	 */
	@POST
	@Path("/updateApplyLoanLeadDetails.spring")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Result updateApplyLoanLeadDetails(@NotNull LoanRequest loanRequest) throws InSufficeientInputException, EmployeeFinancialServiceException {
		LOG.info("*** The control is inside of the updateApplyLoanLeadDetails in ApplyLoanEmpRestService *** "+ loanRequest);
		Result result = new Result();
		if (Util.isNotEmptyObject(loanRequest) && Util.isNotEmptyObject(loanRequest.getCustomerLoanEOIDetailsId())) {
			result = applyLoanService.updateApplyLoanLeadDetails(loanRequest);
			result.setResponseObjList("Lead details updated successfully.");
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

}
