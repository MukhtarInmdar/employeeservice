package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;

import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MenuModuleStatusInfo extends Result implements Serializable {

	private static final long serialVersionUID = 1350791356732474957L;
	private Long menuModuleStatusId;
	private Long menuModuleId;
	private Long subMenuModuleId;
	private String statusName;
	private Long statusId;
}
