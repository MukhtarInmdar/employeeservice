package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class ReferencesCustomerPojo {

	@Column(name="REFERENCES_CUSTOMER_ID")
	private String referencesCustomerId;
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	@Column(name="ID")
	private Long id;
	@Column(name="CUST_NAME")
	private String customerName;
	@Column(name="SITE_NAME")
	private String projectName;
	@Column(name="FLAT_NO")
	private String unitNo;
	
}
