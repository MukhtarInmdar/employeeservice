package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApptmtSlotEmpProjectWisePojo {
	
	@Column(name="APPTMT_SLOT_EMP_PROJECT_WISE_ID")
	private Long apptmtSlotEmpProjectWiseId;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="BLOCK_ID")
	private Long blockId;
	
	@Column(name="APPTMT_SLOT_TIMES_ID")
	private Long apptmtSlotTimesId;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="TYPE_ID")
	private Long typeId;
	
	private List<Long> typeIds;
}
