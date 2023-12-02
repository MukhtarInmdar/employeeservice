/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import com.sumadhura.employeeservice.enums.Status;

import lombok.Data;

/**
 * @author VENKAT
 * DATE 11-MAR-2019
 * TIME 11.11 AM
 */



//check List master table


@Data
public class CheckListDepartmentMappingInfo {


   private Status  CheckListInfo;
   private Long  checkListDeptMapId;
   private Long ckMetaType;
   
public Status getCheckListInfo() {
	return CheckListInfo;
}
public void setCheckListInfo(Status checkListInfo) {
	CheckListInfo = checkListInfo;
}
public Long getCheckListDeptMapId() {
	return checkListDeptMapId;
}
public void setCheckListDeptMapId(Long checkListDeptMapId) {
	this.checkListDeptMapId = checkListDeptMapId;
}
public Long getCkMetaType() {
	return ckMetaType;
}
public void setCkMetaType(Long ckMetaType) {
	this.ckMetaType = ckMetaType;
}
 
	
   
  
  
}
