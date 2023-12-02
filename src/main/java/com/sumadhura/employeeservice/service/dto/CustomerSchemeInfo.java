package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSchemeInfo {
	private Long siteId;
	private Long statusId;
	private String schemeName;
	private Double percentageValue;
	private String condition;
	private List<CustomerSchemeInfo> CustomerSchemeInfos;
}
