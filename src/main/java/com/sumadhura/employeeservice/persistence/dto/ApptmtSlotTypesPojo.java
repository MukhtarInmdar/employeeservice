package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApptmtSlotTypesPojo {
	
	@Column(name="APPTMT_SLOT_TIMES_ID")
	private Long apptmtSlotTimesId;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="TYPE_ID")
	private Long typeId;
	
	@Column(name="TYPE_NAME")
	private String typeName;

}
