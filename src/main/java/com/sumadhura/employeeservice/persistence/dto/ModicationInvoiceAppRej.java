package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class ModicationInvoiceAppRej {
 
	@Column(name = "MODI_INVOICE_APPR_REJ_ID") private Long modificationInvoiceAppRejectId;
	@Column(name = "FIN_BOK_FRM_MODI_COST_ID") private Long finBookingFormModiCostId;
	@Column(name = "ACTION_TYPE") private Long actionType;
	@Column(name = "EMP_ID") private Long empId;
	@Column(name = "COMMENTS") private String comments;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	
	@Column(name = "EMP_NAME") private String empName;
	@Column(name = "EMP_EMAIL") private String empEmail;
	@Column(name = "LEVEL_NAME") private String levelName;
}
