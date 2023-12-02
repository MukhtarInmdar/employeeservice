package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class MPRViewReportPojo {

		@Column (name = "MPR_ID")
		private Long mprId;
		
		@Column(name = "SITE_NAME")
		private String siteName;
		
		@Column (name = "MPR_NAME")
		private String mprName;
		
		@Column (name = "ALERT_NO_OF_FLATS_TO_BE_SENT")
		private Long numOfFlatsToBeSent;
		
		@Column (name = "RECIEVED_COUNT")
		private Long recievedCount;
		
		@Column (name = "VIEW_COUNT")
		private Long viewCount;
		
		@Column (name = "UPLOADED_DATE")
		private Timestamp uploadedDate;
		
		@Column (name = "TOTAL_FLAT_TO_BE_SENT")
		private Long totalFlatCount;
		
		private List<MPRViewDetailsListPojo>  MPRViewDetails;

}
