package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinProjDemNoteStatisticsPojo {
	
	@Column(name = "FIN_PROJ_DEM_DRAF_STAT_ID") private Long finProjectDemandDrafStatId;
	@Column(name = "FIN_PRO_DEM_NOTE_ID") private Long finProjectDemandNoteId;
	//@Column(name = "PROJECT_MILESTONE_ID") private Long projectMilestoneId;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	
}
