package com.sumadhura.employeeservice.service;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.FlatRequest;
import com.sumadhura.employeeservice.dto.FlatResponse;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

public interface FlatService {
	
	public List<DropDownPojo> getFlatsNames(DropDownRequest dropDownRequest);
	public List<DropDownPojo> getFlatsNamesBySite(DropDownRequest dropDownRequest);
	public List<DropDownPojo> getFlatsNamesByBlock(DropDownRequest dropDownRequest);
	public List<NotificationDetails> getFlatDetailList(Set<Long> set);
	public List<NotificationDetails> getFlatDetailListByDetId(Set<Long> set);
	public List<DropDownPojo> getFlatsNamesBySbuaSeries(DropDownRequest dropDownRequest);
	public List<CustomerPropertyDetailsPojo> getCustomerDetailsByFlatId(Customer customer);
	public FlatResponse getFlats(FlatRequest flatRequest);
	public FlatResponse getBookingFlats(FlatRequest flatRequest);
	
}
