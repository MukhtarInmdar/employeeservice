package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumadhura.employeeservice.dto.MprViewDetails;

import lombok.Data;

@Entity
@Data
public class MprPojo {
	@Column(name="MPR_ID")
	private Long mprId;
	
	@Column(name="MPR_NAME")
	private String mprName;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="TYPE_ID")
	private Long typeId;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="TYPE_NAME")
	private String typeName;
	
	/* MPR Document Related Fields */
	@Column(name="MPR_DOC_ID")
	private Long mprDocId;
	
	@Column(name="LOCATION_TYPE")
	private String locationType;
	
	@Column(name="DOCUMENT_LOCATION")
	private String documentLocation;
	
	@Column(name="ALERT_NO_OF_FLATS_TO_BE_SENT")
	private Long alertNoOfflatsToBeSent;
	

	@Column(name="ISVIEWED")
	private String isViewed;
	
	@Column(name="APP_REG_ID")
	private Long appRegId;
	
	@Column(name="DEVICE_VIEW_STATUS")
	private Long deviceViewStatus;
	
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="DEVICE_ID")
	private Long deviceid;
	
	private List<MprViewDetails> devices = new ArrayList<MprViewDetails>();
	
}
