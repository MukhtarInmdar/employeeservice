/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author VENKAT
 * DATE 11-MAR-2019
 * TIME 11.11 AM
 */



//check List master table


@Data
public class CheckListInfo implements Serializable {


   /**
	 * 
	 */
	private static final long serialVersionUID = -1903801904239366519L;
private Long  checkListId;
   private String  checkListName;
   private String  checkListDiscription;
public Long getCheckListId() {
	return checkListId;
}
public void setCheckListId(Long checkListId) {
	this.checkListId = checkListId;
}
public String getCheckListName() {
	return checkListName;
}
public void setCheckListName(String checkListName) {
	this.checkListName = checkListName;
}
public String getCheckListDiscription() {
	return checkListDiscription;
}
public void setCheckListDiscription(String checkListDiscription) {
	this.checkListDiscription = checkListDiscription;
}
 
	
	
}
