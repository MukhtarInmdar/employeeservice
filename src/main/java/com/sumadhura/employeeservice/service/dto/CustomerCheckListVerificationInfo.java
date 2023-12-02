package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerCheckListVerificationInfo implements Serializable {

	private static final long serialVersionUID = 8183652470288643146L;
	private Long checkListVerfiId;
	//private CheckListDepartmentMappingInfo checkListDepartmentMappingInfo;
	private CheckListInfo checkListInfo;
	private Long custId;
	private String deparmentName;
	private Long departmentId;
	private Long checklistDeptMapId;
	private Long flatBookId;
	private Long is_verified;
	private String checkListStatus;

}
