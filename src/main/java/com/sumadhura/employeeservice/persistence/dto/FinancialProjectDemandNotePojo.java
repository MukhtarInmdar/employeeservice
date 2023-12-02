package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinancialProjectDemandNotePojo {

	@Column(name = "FIN_PRO_DEM_NOTE_ID")
	private Long finProjectDemandNoteId;
	@Column(name = "DUE_DATE")
	private Timestamp mileStoneDueDate;
	@Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name = "CREATED_BY")
	private Long createdBy;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")
	private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
}
