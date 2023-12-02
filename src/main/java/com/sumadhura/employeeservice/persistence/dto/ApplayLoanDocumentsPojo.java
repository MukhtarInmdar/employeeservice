package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ApplayLoanDocumentsPojo {

	@Column(name = "DOCUMENT_ID")	private Long doucumentId;
	@Column(name = "DOC_LOCATION")	private String docLocation;
	@Column(name = "URL_LOCATION")	private String urlLocation;
	@Column(name = "DOC_TYPE")	private String docType;
	@Column(name = "CUSTOMER_LOAN_EOI_DETAILS_ID")	private Long customerLoanEOIDetailsId;

}
