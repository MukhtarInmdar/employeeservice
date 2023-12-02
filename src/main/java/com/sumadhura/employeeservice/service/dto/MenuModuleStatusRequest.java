package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;

import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuModuleStatusRequest extends Result implements Serializable {

	private static final long serialVersionUID = -1386149086858784082L;
	private Long moduleId;
	private Long subModuleId;

}
