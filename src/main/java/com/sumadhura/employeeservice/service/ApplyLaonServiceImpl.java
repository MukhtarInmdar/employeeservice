package com.sumadhura.employeeservice.service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.DefaultBankerException;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.persistence.dao.ApplyLoanDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.ApplayLoanDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.ApplyLoanLeadDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.BankerList;
import com.sumadhura.employeeservice.persistence.dto.CustomerDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.helpers.ApplyLoanHelper;
import com.sumadhura.employeeservice.util.CurrencyUtil;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.Util;

@Service
public class ApplyLaonServiceImpl implements ApplyLaonService {
	
	@Autowired(required = true)
	private ApplyLoanDao applyLoanDao;
	
	private final  Logger LOGGER = Logger.getLogger(ApplyLaonServiceImpl.class);
	
	@Autowired(required = true)
	private MailService mailServiceImpl;
	
	@Autowired
	@Qualifier("EmployeeFinancialServiceDao")
	private EmployeeFinancialServiceDao employeeFinancialServiceDao;

	@Autowired
	@Qualifier("applyLoanHelper")
	private ApplyLoanHelper applyLoanHelper;
	
	@Autowired(required = true)
	private ResponceCodesUtil responceCodesUtil;
	
	@Autowired
	private CurrencyUtil currencyUtil;
	
	private static final RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static final int roundingModeSize = 2;

	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void sendMailToDefaultBankarOnBooking(final CustomerInfo customerInfo) throws DefaultBankerException {
		LOGGER.info("*** The control is inside of the sendMailToDefaultBankarOnBooking in ApplyLaonServiceImpl ***");
		String crmMails="";
		String mailSentTo="";
		Properties prop = responceCodesUtil.getApplicationProperties();
		final String SEND_MAIL_DEFAULT_BANKER = prop.getProperty("SEND_MAIL_DEFAULT_BANKER");//taking due days from prop file
		List<CustomerPropertyDetailsPojo> customerPojo=null;
		List<BankerList> bakenrList = applyLoanDao.getBankEamilOnBooking(customerInfo);
		if (Util.isEmptyObject(bakenrList)) {//ACP added, if banker list not found throw exception
			//throw new DefaultBankerException("Default Banker list not found, Please add default banker list...");
			return;
		}
		BankerList bakenr = bakenrList.get(0);
		final List<CustomerPropertyDetailsPojo> customerPojoMail = new ArrayList<CustomerPropertyDetailsPojo>();
		customerInfo.setFlatBookingId(customerInfo.getFlatBookingId());
		customerPojo = applyLoanDao.getCustomerPropertyDetails(customerInfo, null);

		CustomerDetailsPojo customerDetailsPojo = new CustomerDetailsPojo();
		customerDetailsPojo.setSiteId(customerInfo.getSiteId());
		customerDetailsPojo.setBloockId(customerInfo.getBlockId());

		Site site = new Site();
		site.setSiteId(customerInfo.getSiteId());
		site.setName(customerInfo.getSiteName());
		List<Long> listOfDepartments = new ArrayList<>();
		listOfDepartments.add(Department.CRM.getId());
		
		List<Long> listOfRolles = new ArrayList<>();
		listOfRolles.add(Roles.SR_CRM_EXECUTIVE.getId());

		Map<String,Object> map = new HashMap<>();
		map.put("listOfDepartments", listOfDepartments);
		map.put("listOfRolles", listOfRolles);
		if(Util.isEmptyObject(customerPojo)) {
			return ;
		}
		
		if(com.sumadhura.employeeservice.enums.Site.EDENGARDEN.getId().equals(customerPojo.get(0).getSiteId())
				&& !"1".equals(customerPojo.get(0).getFlatSaleOwnerId())) {
			//FLATS_SALE_OWNERS in this table can check this
			//for other builder not sending email to banker
			return ;
		}
		//loading CRM details, for sending mail from CRM to banker
		final List<EmployeeDetailsPojo> crmDetailsList = employeeFinancialServiceDao.getEmployeeDetailsUsingDeptAndRollAndBlockId(customerPojo.get(0),map);
		if (Util.isEmptyObject(crmDetailsList)) {
			throw new DefaultBankerException("CRM details not found, Could not send new lead details to banker");
		}
		String internalEmpMail = prop.getProperty("DEFAULT_BANKER_ERROR_INTERNAL_EMP_EMAIL");//taking due days from prop file
		List<String> testingFlat = Arrays.asList("EG-TEST","AR-TEST","SU-TEST","NA-TEST","HO-TEST","GBB-TEST");
		
		List<EmployeeDetailsPojo> crmdetails = applyLoanDao.getEmployeeDetailsForMail(customerDetailsPojo);
		
		for(EmployeeDetailsPojo crmPojo :crmdetails) {
			if(testingFlat.contains(customerPojo.get(0).getFlatNo())) {
				crmPojo.setEmail("aniketchavan75077@gmail.com");	
			}

			if (Util.isEmptyObject(crmMails)) {
				if (Util.isNotEmptyObject(crmPojo.getEmail())) {
					crmMails = crmPojo.getEmail().trim(); 
				}
			} else {
				if (Util.isNotEmptyObject(crmPojo.getEmail())) {
					crmMails = crmMails + "," + crmPojo.getEmail().trim();
				}
			}
		}
		final List<EmployeeDetailsPojo> CRM_detailsMailListForErrorMail = new ArrayList<EmployeeDetailsPojo>(crmdetails);

		//defaultBankList =applyLoanDao.getbankName(loanRequest);
		for (CustomerPropertyDetailsPojo bean : customerPojo) {
			CustomerPropertyDetailsPojo pojo = new CustomerPropertyDetailsPojo();
			BeanUtils.copyProperties(bean, pojo);
			//adding banker mail to send email
			if (Util.isNotEmptyObject(bakenr.getEmail())) {//only default banker email here, no employee email
				pojo.setEmail(bakenr.getEmail());//Banker Email
				if(testingFlat.contains(bean.getFlatNo())) {
					pojo.setEmail(internalEmpMail);//Banker Email
					bakenr.setEmail(internalEmpMail);
				}
			}
			// pojo.setInterestedBankName(defaultBankList.getBankerName());
			pojo.setInterestedBankName(bakenr.getBankerName());
			pojo.setBankContactPersonName(bakenr.getContactPerson());
			//crmMails = "";
			pojo.setCrmMails(crmMails);//only internal team CRM emails
			if(testingFlat.contains(bean.getFlatNo())) {
				pojo.setCrmMails(internalEmpMail);//Banker Email	
			}
			customerPojoMail.add(pojo);
		}
		
		String toMail = Util.isNotEmptyObject(bakenr.getEmail()) ? bakenr.getEmail() : "";
		mailSentTo = crmMails + "," + toMail;//to whom mail sent
		//mailSentTo = "aniketchavan75077@gmail.com";
		customerInfo.setMailSentTo(mailSentTo);
		customerInfo.setBankerListId(bakenr.getBankerListId());
		//final long primaryKey = applyLoanDao.saveApplyLoanDetailsOnBooking(customerInfo);
		
		if(Util.isNotEmptyObject(SEND_MAIL_DEFAULT_BANKER) && "true".equalsIgnoreCase(SEND_MAIL_DEFAULT_BANKER) ) {
			// sending mail to banker
			 new Thread(new Runnable() {
					@Override
					public void run() {
						//sending email from crm employee id, for default banker, can check the below method
						String resp = mailServiceImpl.sendMailToBankerOnBooking(customerPojoMail,crmDetailsList.get(0),CRM_detailsMailListForErrorMail,null);
						if("success".equals(resp)) {//if success then only save
							applyLoanDao.saveApplyLoanDetailsOnBooking(customerInfo);
						}
					}
				 }).start();
		}
	}

	@Override
	public List<ApplyLoanLeadDetailsPojo> getLoanAppliedLeadDetails(LoanRequest loanRequest) {

		List<ApplyLoanLeadDetailsPojo> appliedLeadDetails = applyLoanDao.getLoanAppliedLeadDetails(loanRequest);
		for (ApplyLoanLeadDetailsPojo applyLoanLeadDetailsPojo : appliedLeadDetails) {
			if(Util.isNotEmptyObject(applyLoanLeadDetailsPojo.getFlatCost())) {
				applyLoanLeadDetailsPojo.setStrFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(applyLoanLeadDetailsPojo.getFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
			}
		}
		return appliedLeadDetails;
	}
	
	/**
	 * Loading lead details by id
	 */
	@Override
	public List<ApplyLoanLeadDetailsPojo> loadLoanAppliedLeadDetailsById(LoanRequest loanRequest) {
		
		List<ApplyLoanLeadDetailsPojo> appliedLeadDetails = applyLoanDao.getLoanAppliedLeadDetails(loanRequest);
		if(Util.isNotEmptyObject(appliedLeadDetails)) {
			List<ApplayLoanDocumentsPojo> loanDocuments = applyLoanDao.loadLeadAttachedDocuments(appliedLeadDetails.get(0));
			appliedLeadDetails.get(0).setLoanDocuments(loanDocuments);
			
			for (ApplyLoanLeadDetailsPojo applyLoanLeadDetailsPojo : appliedLeadDetails) {
				if(Util.isNotEmptyObject(applyLoanLeadDetailsPojo.getFlatCost())) {
					applyLoanLeadDetailsPojo.setStrFlatCost(currencyUtil.convertUstoInFormat(BigDecimal.valueOf(applyLoanLeadDetailsPojo.getFlatCost()).setScale(roundingModeSize, roundingMode).toString()));
				}
			}
		}
		
		return appliedLeadDetails;
	}
	
	/**
	 * Updating lead status if banker open details page, so the making status of lead READ
	 */
	@Override
	public Result updateLeadSeenStatus(LoanRequest loanRequest) {
		Result result = new Result();
		
		if(Util.isNotEmptyObject(loanRequest.getDepartmentId()) && Util.isNotEmptyObject(loanRequest.getRoleId())) {
			if(Department.BANKER.getId().equals(loanRequest.getDepartmentId()) && Roles.BANKER.getId().equals(loanRequest.getRoleId())) {
				if(Util.isNotEmptyObject(loanRequest.getBankerLeadViewStatusId()) && Status.UNREAD.getStatus().equals(loanRequest.getBankerLeadViewStatusId())) {
					loanRequest.setBankerLeadViewStatusId(Status.READ.getStatus());
					applyLoanDao.updateLeadSeenStatus(loanRequest);
				}
			}
		}		
		
		return result;
	}

	/**
	 * updating lead status details and comments 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result updateApplyLoanLeadDetails(LoanRequest loanRequest) throws EmployeeFinancialServiceException {
		Result result = new Result();
		CustomerInfo customerInfo = new CustomerInfo(); 
		String comment = loanRequest.getBankerComment();
		if(Util.isNotEmptyObject(loanRequest.getPreviousBankerComments())) {
			loanRequest.setBankerComment(loanRequest.getPreviousBankerComments().trim()+"\n-"+loanRequest.getBankerComment());
		}
		
		if(Util.isNotEmptyObject(loanRequest.getLeadStatus())) {
			try {
				loanRequest.setLeadStatusId(Status.valueOf(loanRequest.getLeadStatus().replaceAll(" ", "_").toUpperCase()).getStatus());
			} catch(Exception ex) {
				LOGGER.info("Exception raised in updateApplyLoanLeadDetails "+ex);
			}
		}
		
		customerInfo.setFlatBookingId(loanRequest.getBookingFormId());
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = applyLoanDao.getCustomerPropertyDetails(customerInfo, null);
		if(Util.isEmptyObject(customerPropertyDetailsPojos)) {
			throw new EmployeeFinancialServiceException("Customer details not found, Could not update the details.");
		}

		CustomerDetailsPojo customerDetailsPojo = new CustomerDetailsPojo();
		customerDetailsPojo.setSiteId(customerPropertyDetailsPojos.get(0).getSiteId());
		customerDetailsPojo.setBloockId(customerPropertyDetailsPojos.get(0).getBlockId());

		List<EmployeeDetailsPojo> crmdetails = applyLoanDao.getEmployeeDetailsForMail(customerDetailsPojo);
		//updating lead details
		applyLoanDao.updateApplyLoanLeadDetails(loanRequest);
		//for mail required banker entered comment
		loanRequest.setBankerComment(comment);
		//sending email to crm and crm head, if banker updated the status
		mailServiceImpl.sendUpdateLeadDetailsToEmployee(customerPropertyDetailsPojos.get(0), loanRequest, crmdetails);
		return result;
	}
	
}
