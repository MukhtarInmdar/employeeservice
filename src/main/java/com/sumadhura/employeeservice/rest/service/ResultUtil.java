package com.sumadhura.employeeservice.rest.service;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;

public class ResultUtil {

	
	public Result prepareResultObj(Object obj){
		Result result=new Result();
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(obj);
		return result;
		
	}
}
