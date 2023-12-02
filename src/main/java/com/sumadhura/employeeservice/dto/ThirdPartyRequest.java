package com.sumadhura.employeeservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class ThirdPartyRequest extends Result implements Serializable {
	private static final long serialVersionUID = 2908943929161565147L;
	private String empId;
	private String employeeName;
	private Long siteId;
	private String siteName;
	private String actionUrl;
	private String requestUrl;
	private String condition;
	private Long thirdPartyportNumber;
	private Long portNumber;
	private String portalName;
}
