package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinMilDemNoteMappingPojo {
	@Column(name = "FIN_MIL_DEM_DRF_MAP_ID")private Long finMileStoneDemandDrfMapId;
	@Column(name = "PROJECT_MILESTONE_ID")private Long projectMilestoneId;
	@Column(name = "FIN_PRO_DEM_NOTE_ID")private Long finProjectDemandNoteId;
	
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
}
