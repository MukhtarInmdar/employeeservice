package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class MenuModuleStatusPojo  {
	
	@Column(name="MENU_MODULE_SUBMENU_MODULE_STATUS_ID")
	private Long menuModuleStatusId;
	@Column(name="EMP_MENU_MODULE_ID")
	private Long menuModuleId;
	@Column(name="EMP_SUBMENU_MODULE_ID")
	private Long subMenuModuleId;
	@Column(name="STATUS_NAME")
	private String statusName;
	@Column(name="STATUS_ID")
	private Long statusId;

}
