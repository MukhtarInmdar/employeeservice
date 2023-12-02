package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormDemandNoteDocPojo {
	@Column(name = "FIN_BOK_FRM_DMD_NTE_DOC_ID") private Long finBookingFormDemandNoteDocId;
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "FIN_BOK_FOM_DMD_NOTE_ID") private Long finBookingFormDemandNoteId;
	@Column(name = "DOC_NAME") private String documentName;
	@Column(name = "DOC_PATH") private String filePath;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
}
