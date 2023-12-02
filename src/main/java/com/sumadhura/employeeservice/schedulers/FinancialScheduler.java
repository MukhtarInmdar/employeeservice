package com.sumadhura.employeeservice.schedulers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.FinInterestRatesPojo;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.FinancialProjectMileStoneInfo;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialHelper;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.Getter;
import lombok.Setter;

//@EnableAsync
@Setter
@Getter
//@Component("financialScheduler")
public class FinancialScheduler {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;

	@Autowired
	private ResponceCodesUtil responceCodesUtil;
	
	@Autowired
	@Qualifier("EmployeeFinancialServiceDao")
	private EmployeeFinancialServiceDao employeeFinancialServiceDao;

	// @Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris")
	// we're scheduling a task to be executed at 10:15 AM on the 15th day of every
	// month.
	// @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}") //in properties
	// file

	// @Scheduled(cron = "0 50 18 28 * ?")
	// 18 means 6 PM 50 means 6:50 and 28 means the 28th date of the month

	// @Scheduled(cron = "0-5 50 18 28 * ?")
	// 0-5 means starting of the 6:50 to 5 seconds,18 means 6 PM, 50 means 6:50 and
	// 28 means the 28th date of the month

	// @Scheduled(cron = "0-5 50 18 * * ?")
	// 0-5 means starting of the 6:50 to 5 seconds,18 means 6 PM, 50 means 6:50 and
	// 4th * means every day of month, 5th star means every month

	// 0 0 19 * * ?
	// every date on 7pm only 1 time
	private List<Map<String, Object>> listOfSiteIds; 
	{
		 //listOfSiteIds = employeeFinancialServiceDao.getSiteIdsOfRateOfInterest();
	}
	
	//@Async
	//@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	@Scheduled(cron = "${cron.expression}")
	public void scheduleFixedRateTaskAsync() throws Exception {
		Timestamp rateOfInterestEndDate = null;
		Timestamp currentDate = new Timestamp(new Date().getTime());
		System.out.println("Fixed rate task async - "+currentDate);
		
		if(listOfSiteIds==null || listOfSiteIds.isEmpty()) {
			listOfSiteIds = employeeFinancialServiceDao.getSiteIdsOfRateOfInterest();	
		}
		
		for (Map<String, Object> map : listOfSiteIds) {
			int daysDiff = 0;
			
			FinancialProjectMileStoneInfo mileStoneInfo = new FinancialProjectMileStoneInfo();
			long siteId = map.get("SITE_ID") == null ? 0l : Long.valueOf(map.get("SITE_ID").toString());
			String siteName = map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString();
			if (siteId == 0) {
				continue;
			}
			/*if (siteId != 107l) {
				continue;
			}*/
			mileStoneInfo.setSiteId(siteId);
			
			List<FinInterestRatesPojo> finTaxDetailsListPojo = employeeFinancialServiceDao.getAvailbleMonthInterestDetailsOnDue(mileStoneInfo);
			rateOfInterestEndDate = finTaxDetailsListPojo.get(0).getEndDate();
			mileStoneInfo.setInterestCalculationUptoDate(rateOfInterestEndDate);
			daysDiff = TimeUtil.differenceBetweenDays(TimeUtil.removeTimePartFromTimeStamp1(rateOfInterestEndDate),TimeUtil.removeTimePartFromTimeStamp1(currentDate));
			log.info("FinancialScheduler.scheduleFixedRateTaskAsync() "+siteId+" "+rateOfInterestEndDate + " " + currentDate+ " " + daysDiff);
			//if (daysDiff < 2 || daysDiff > 0) {
			if (daysDiff >= 0) {
				//employeeFinancialServiceDao.getEmployeeEmailsForUpdateRateOfInteres(mileStoneInfo);
				Site si = new Site();
				si.setSiteId(siteId);
				si.setName(siteName);
				sendUpdateRateOfInterestEmail(daysDiff,mileStoneInfo,si);
			}
		}
	}

	private void sendUpdateRateOfInterestEmail(int daysDiff, FinancialProjectMileStoneInfo mileStoneInfo, Site site) {
		log.info("FinancialScheduler.sendUpdateRateOfInterestEmail() Date has not updated " + daysDiff);
		String logoForPdf = "";
		String thanksAndRegardsFrom="";
		String greetingsFrom = "";
		DemandNoteGeneratorInfo info = new DemandNoteGeneratorInfo();
		Email email = new Email();
		EmployeeFinancialServiceInfo serviceInfo=new EmployeeFinancialServiceInfo();
		serviceInfo.setSiteId(site.getSiteId());
		//String defaultEmpNamesInEmail = responceCodesUtil.getApplicationProperties().getProperty("RATE_OF_INTEREST_DEFAULT_EMP_NAMES");
		//StringBuffer emailMsg = new StringBuffer(responceCodesUtil.getApplicationProperties().getProperty("RATE_OF_INTEREST_MSG").replace("${siteName}", site.getName()));
		//defaultEmpNamesInEmail = defaultEmpNamesInEmail==null?"":defaultEmpNamesInEmail;
		Properties prop= responceCodesUtil.getApplicationProperties();
		if(site.getSiteId()!=null && site.getSiteId().equals(131l)) {
			logoForPdf = prop.getProperty("ASPIRE_LOGO1");
			thanksAndRegardsFrom = prop.getProperty("ASPIRE_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("ASPIRE_GREETING_MSG_FROM");
		} else {
			logoForPdf = prop.getProperty("SUMADHURA_LOGO1");
			thanksAndRegardsFrom = prop.getProperty("SUMADHURA_THANKS_AND_REGARDS_MSG_FROM");
			greetingsFrom = prop.getProperty("SUMADHURA_GREETING_MSG_FROM");
		}
		
		List<OfficeDtlsPojo> officeDetailsPojoList = employeeFinancialServiceDao.getOfficeDetailsBySite(serviceInfo);
		List<Long> listOfDepartments = new ArrayList<>();
		listOfDepartments.add(Department.CRM.getId());//995
		listOfDepartments.add(Department.CRM_MIS.getId());//993
		
		List<Long> listOfRolles = new ArrayList<>();
		listOfRolles.add(Roles.CRM_SALES_HEAD.getId());//12
		listOfRolles.add(Roles.TECH_CRM_HEAD.getId());//15

		Map<String,Object> map = new HashMap<>();
		map.put("listOfDepartments", listOfDepartments);
		map.put("listOfRolles", listOfRolles);
		List<Map<String, Object>>  emails = employeeFinancialServiceDao.getEmployeeDetailsUsingDeptAndRoll(site,map);
		if(Util.isEmptyObject(emails)) {
			return;
		}
		List<String> toMails = getTheEmails(emails);
		//List<String> toMails =new ArrayList<>();
		log.info("FinancialScheduler.sendUpdateRateOfInterestEmail() emails " + toMails+" \t "+site.getSiteId());
		if(toMails.isEmpty()) {
			return;
		}
		//toMails.add("aniketchavan75077@gmail.com");
		new EmployeeFinancialHelper().setOfficeDetailsPojo(officeDetailsPojoList,null,info);
		info.setSiteName(site.getName());
		//setting rate of interest last date//used existing field
		info.setTransactionDate(TimeUtil.timestampToDD_MM_YYYY(mileStoneInfo.getInterestCalculationUptoDate()));
		info.setCondition("RateOfInterestAlert");
		info.setRightSidelogoForPdf(logoForPdf);
		info.setThanksAndRegardsFrom(thanksAndRegardsFrom);
		info.setGreetingsFrom(greetingsFrom);
		
		//email.setPortNumber(transactionServiceInfo.getPortNumber());
		email.setToMails(toMails.toArray(new String[] {}));
		//email.setCc(cc);
		/*String[] arr= {"aniketchavan75077@gmail.com"};
		email.setBccs(arr);*/
		email.setSubject("Rate of interest not up to date.");
		//email.setMessage(emailMsg.toString());
		email.setTemplateName("/demandnotes/CommonMailTemplate.vm");
		email.setDemandNoteGeneratorInfo(info);
		mailServiceImpl.sendFinancialRateOfInterestMailToEmployee(email);
	}

	private List<String> getTheEmails(List<Map<String, Object>> emails) {
		List<String> emailList = new ArrayList<>();
		
		for (Map<String, Object> map : emails) {
			String email = map.get("EMAIL") == null ? "" : map.get("EMAIL").toString();
			if (email.length() == 0 || emailList.contains(email)) {
				continue;
			}
			emailList.add(email);
		}

		return emailList;
	}

	@SuppressWarnings("unused")
	private void setOfficeDetails(List<OfficeDtlsPojo> officeDetailsPojoList, DemandNoteGeneratorInfo info) {
		if(Util.isNotEmptyObject(officeDetailsPojoList) && Util.isNotEmptyObject(officeDetailsPojoList.get(0))) {
			OfficeDtlsPojo officeDetailsResponse = officeDetailsPojoList.get(0);
			if(Util.isNotEmptyObject(officeDetailsResponse.getName())) {
				info.setCompanyName(officeDetailsResponse.getName());
			}else {
				info.setCompanyName("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getBillingAddress())){
				info.setCompanyBillingAddress(officeDetailsResponse.getBillingAddress());
			}else {
				info.setCompanyBillingAddress("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getTelephoneNo())) {
				info.setCompanyTelephoneNo(officeDetailsResponse.getTelephoneNo());
			}else {
				info.setCompanyTelephoneNo("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getEmail())) {
				info.setCompanyEmail(officeDetailsResponse.getEmail());
			}else {
				info.setCompanyEmail("N/A");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getCin())) {
				info.setCompanyCin(officeDetailsResponse.getCin());
			}else {
				info.setCompanyCin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getGstin())) {
				info.setCompanyGstin(officeDetailsResponse.getGstin());
			}else {
				info.setCompanyGstin("-");
			}
			if(Util.isNotEmptyObject(officeDetailsResponse.getWebsite())) {
				info.setCompanyWebsite(officeDetailsResponse.getWebsite());
			}else {
				info.setCompanyWebsite("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getLlpin())) {
				info.setCompanyLlpin(officeDetailsResponse.getLlpin());
			}else {
				info.setCompanyLlpin("-");
			}
			
			if(Util.isNotEmptyObject(officeDetailsResponse.getPan())) {
				info.setCompanyPanNumber(officeDetailsResponse.getPan());
			}else {
				info.setCompanyPanNumber("N/A");
			}
			
		}else {
			info.setCompanyName("N/A");
			info.setCompanyBillingAddress("N/A");
			info.setCompanyTelephoneNo("N/A");
			info.setCompanyEmail("N/A");
			info.setCompanyCin("N/A");
			info.setCompanyGstin("N/A");
			info.setCompanyWebsite("N/A");
			info.setCompanyPanNumber("N/A");
		}
		
	}
	
}
