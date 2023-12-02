/**
 * 
 */
package com.sumadhura.employeeservice.service.mappers;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;

/**
 * @author VENKAT
 * DATE 07-FEB-2019
 * TIME 05.30 PM
 */

/*  This class is responsible PropertyDetailsMapper */
public class PropertyDetailsMapper {
	
	private final static Logger logger = Logger.getLogger(PropertyDetailsMapper.class);

	public CustomerInfo customerToCustomerInfo(Customer customer) {
		logger.info("**** The control is inside the customerToCustomerInfo in PropertyDetailsMapper *****");
		CustomerInfo customerInfo = new CustomerInfo();
		BeanUtils.copyProperties(customer, customerInfo);
		return customerInfo;
		
	}


}
