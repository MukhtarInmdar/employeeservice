package com.sumadhura.employeeservice.schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCReleasePojo;
import com.sumadhura.employeeservice.service.BookingFormService;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.Getter;
import lombok.Setter;

@EnableAsync
@Setter
@Getter
@Component("nocScheduler")
public class NOCScheduler {
	//no need to mention this class name in application-context.xml
	private final Logger log = Logger.getLogger(this.getClass());
	
	//NOTE use live port number for schedular
	
	@Autowired
	private ResponceCodesUtil responceCodesUtil;
	
	@Autowired
	@Qualifier("EmployeeFinancialServiceDao")
	private EmployeeFinancialServiceDao employeeFinancialServiceDao;
	
	@Autowired(required = true)
	@Qualifier("BookingFormServiceImpl")
	private BookingFormService bookingFormServiceImpl;

	//@Async
	//@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	@Scheduled(cron = "${cron.expressionForNoc}")
	public void scheduleFixedRateTaskAsync() throws Exception {
		System.out.println("NOCScheduler.scheduleFixedRateTaskAsync()");
		String nocGenerateProjectSchedular = responceCodesUtil.getApplicationProperties().getProperty("NOC_GENERATE_PROJECT_SCHEDULAR");
		List<String> siteIds = Arrays.asList(nocGenerateProjectSchedular.split(","));
		BookingFormRequest bookingFormRequest = null;
		//CustomerPropertyDetailsInfo customerPropertyDetailsInfo = null;
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = null;
		for (String siteId : siteIds) {
			//customerPropertyDetailsInfo = null;
			employeeFinancialServiceInfo = null;
			bookingFormRequest = null;
			employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
			//employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(1296l,361l));
			employeeFinancialServiceInfo.setSiteId(Long.valueOf(siteId));
			employeeFinancialServiceInfo.setSiteIds(Arrays.asList(Long.valueOf(siteId)));
			bookingFormRequest = new BookingFormRequest();
			
			/* getting Customer Details */
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList = null;
			try {
				 customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
			} catch (Exception e) {
				e.printStackTrace(); 
				customerPropertyDetailsPojoList = new ArrayList<>();
			}
			
			for (CustomerPropertyDetailsPojo customerPropertyDetailsPojo : customerPropertyDetailsPojoList) {
				
				if(Util.isEmptyObject(customerPropertyDetailsPojo.getRegistrationDate())) {
					continue;
				}
				try {
					BeanUtils.copyProperties(customerPropertyDetailsPojo,bookingFormRequest);
					bookingFormRequest.setRequestUrl("generateBookingNOCLetter");
					bookingFormRequest.setActionStr("generateNOCLetter");
					//***use Live port for live code ***
					bookingFormRequest.setPortNumber(Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName()));//make sure port number is correct
					bookingFormRequest.setEmpId(22l);
					bookingFormRequest.setEmployeeName("Accounts");
					List<NOCReleasePojo> list = bookingFormServiceImpl.getNOCReleaseDetails(bookingFormRequest);
					if(Util.isNotEmptyObject(list) && list.get(0).getNocShowStatus().equals("showNocButton")) {
						
						Map<String, Object> nocMap = bookingFormServiceImpl.generateNOCLetter(bookingFormRequest,Arrays.asList(customerPropertyDetailsPojo));
						
						if ("success".equals(nocMap.get("successMasg"))) {
							log.info("Success NOCScheduler.scheduleFixedRateTaskAsync() "+customerPropertyDetailsPojo.getFlatBookingId());
						} else {
							log.info("Failed NOCScheduler.scheduleFixedRateTaskAsync() "+customerPropertyDetailsPojo.getFlatBookingId());
						}
					}
				} catch (EmployeeFinancialServiceException ex) {
					ex.printStackTrace();
					log.info(ex.getMessages());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}//customerPropertyDetailsPojoList
			log.info("NOCScheduler.scheduleFixedRateTaskAsync() executed "+siteId);
		}//siteIds
	}
}
