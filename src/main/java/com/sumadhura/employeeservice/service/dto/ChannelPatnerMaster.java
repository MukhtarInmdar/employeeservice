package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ChannelPatnerMaster {
	
	private Long chanelPartnerMasterID;
	private Long cpId;
	private String cpCompany;
	private String  cpName;
	private String  cpReraNo;
	private Long  statusID;
	private Timestamp  createdDate;
	private Timestamp modifiedDate;
	private Long channelPartnerMasterId;

	
	
	
}
