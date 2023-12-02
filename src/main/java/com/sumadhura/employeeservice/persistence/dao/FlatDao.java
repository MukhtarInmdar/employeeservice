package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.FlatRequest;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

public interface FlatDao {
	
	public List<DropDownPojo> getFlatsNames(DropDownRequest dropDownRequest);
	public List<DropDownPojo> getFlatsNamesBySite(DropDownRequest dropDownRequest);
	public List<DropDownPojo> getFlatsNamesByBlock(DropDownRequest dropDownRequest);
	public List<NotificationDetails> getFlatDetailList(Set<Long> set);
	public List<NotificationDetails> getFlatDetailListByDetId(Set<Long> set);
	public List<DropDownPojo> getFlatsNamesBySbuaSeries(DropDownRequest dropDownRequest);
	public List<CustomerPropertyDetailsPojo> getCustomerDetailsByFlatId(Customer customer);
	public List<FlatPojo> getFlats(FlatRequest flatRequest);
	public List<FlatBookingPojo> getBookingFlats(FlatRequest flatRequest);
	
}
