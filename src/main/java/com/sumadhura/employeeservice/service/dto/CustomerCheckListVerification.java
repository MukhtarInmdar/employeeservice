/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Data;

/**
 * @author VENKAT
 * DATE 11-MAR-2019
 * TIME 11.11 AM
 */



//check List master table


@Data
public class CustomerCheckListVerification {

   private Long  checkListVerfiId;  
   private Long custCheckVeriId;
   private CheckListDepartmentMappingInfo  checkListDepartmentMappingInfo;
   private CheckListInfo  checkListInfo;
   private Long  custId;
   private Long  flatBookId;
   private Long statusId;
   private Timestamp createdDate;
   private Timestamp modifiedDate;
   
   
   public Long getCheckListVerfiId() {
	return checkListVerfiId;
}
public void setCheckListVerfiId(Long checkListVerfiId) {
	this.checkListVerfiId = checkListVerfiId;
}
public CheckListDepartmentMappingInfo getCheckListDepartmentMappingInfo() {
	return checkListDepartmentMappingInfo;
}
public void setCheckListDepartmentMappingInfo(CheckListDepartmentMappingInfo checkListDepartmentMappingInfo) {
	this.checkListDepartmentMappingInfo = checkListDepartmentMappingInfo;
}
public CheckListInfo getCheckListInfo() {
	return checkListInfo;
}
public void setCheckListInfo(CheckListInfo checkListInfo) {
	this.checkListInfo = checkListInfo;
}
public Long getCustId() {
	return custId;
}
public void setCustId(Long custId) {
	this.custId = custId;
}
public Long getFlatBookId() {
	return flatBookId;
}
public void setFlatBookId(Long flatBookId) {
	this.flatBookId = flatBookId;
}
	
}
