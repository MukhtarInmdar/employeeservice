package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.service.dto.FlatInfo;

import lombok.Data;

@Data
public class FinMileStoneCLSMappingBlocksResponse {
	private Long projectMileStoneClsMappingBlocksId;
	private Long blockId;
	private String blockName;
	private Long finMilestoneClassifidesId;
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	List<FlatInfo> flatInfos;
}
