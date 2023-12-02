package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReferedCustomerWrapper {
	private List<ReferedCustomer> referedCustomer ;
	private Long pagecount;
}
