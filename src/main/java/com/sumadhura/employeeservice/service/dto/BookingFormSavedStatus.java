package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
@Setter
@ToString
public class BookingFormSavedStatus extends Result implements Serializable {

	private static final long serialVersionUID = -1893303725701810373L;
	private Long leadId;
	private String custName;
	private String status;
	private List<String> error;
	
}
