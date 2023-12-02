package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CoApplicantCheckListVerificationInfo {

	private Long  checkListVerfiId;  
	private CheckListInfo  checkListInfo=new CheckListInfo();
	private Long  coApplicantId;
	private Long  checkListDeptMappingId;
	private String CoapplicentPanCard;
	private String CoapplicentPassport;
	private Long custId;
	private Long is_verified;
	private Long flatBookId;
	private String checkListStatus;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private Long departmentId;
	private Long checklistDeptMapId;

}
