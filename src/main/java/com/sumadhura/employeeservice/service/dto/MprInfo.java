package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.dto.FileInfo;

import lombok.Data;

@Data
public class MprInfo {
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
	private Timestamp createddate;
	private String uuid;
}
