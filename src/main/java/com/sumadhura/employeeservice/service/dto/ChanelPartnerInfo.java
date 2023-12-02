package com.sumadhura.employeeservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Srivenu
 * @since 30.05.2019
 * @time 11:53AM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChanelPartnerInfo {
	
	private Long channelPartnerId;
	private String channelPartnerName;
	private String channelPartnerCompanyName;
	private String channelPartnerCPID;
	private String channelPartnerRERANO;
	
}
