package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;

import java.util.List;

import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MenuModuleStatusResponse extends Result implements Serializable  {
	
	private static final long serialVersionUID = -2785280731559172850L;
	private List<MenuModuleStatusInfo> MenuModuleStatusInfoList;
	

}
