package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MprRequest extends Result implements Serializable{

	private static final long serialVersionUID = -4148157379920844493L;
	private Long mprId;
	private String mprName;
	private String remarks;
	private Long siteId;
	private Long employeeId;
	private String fileLocationType;
	private List<FileInfo> fileInfos;
	private String externalDriveLocation;
	private List<Long> siteIds;
	private int pageNo;
	private int pageSize;
	private Long customerId;
	private Long flatBookingId;
	private List<Long> mprIds;
	private String requestUrl;
	private Long appRegId;
	private String devicetoken;
	private String uuid;
}
