package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * 
 * @author A@IKET CH@V@N
 * @description this class is for employee financial service holding all the DB
 *              records annotated with Entity
 * @since 13-01-2020
 * @time 10:30 AM
 */
@Entity
@Data
public class FinancialMileStoneClassifideMappingBlocksPojo {
	
	@Column(name = "PRO_MIL_CLS_MAPPING_BLOCKS_ID")
	private Long projectMileStoneClsMappingBlocksId;
	@Column(name = "BLOCK_ID")
	private Long blockId;
	@Column(name = "BLOCK_NAME")
	private String blockName;
	@Column(name = "FIN_MILESTONE_CLASSIFIDES_ID")
	private Long finMilestoneClassifidesId;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
}
