package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CarParkingApprovalLevelsPojo {
	
	@Column(name="CP_APR_LEV_ID")
	private Long cpAprLevId;
	
	@Column(name="CP_SET_OF_LEV_ID")
	private Long cpSetOfLevId;
	
	@Column(name="NXT_CP_SET_OF_LEV_ID")
	private Long nxtCpSetOfLevId;
	
	@Column(name="CP_SET_OF_LVL_EMP_MAP_ID")
	private Long cpSetOfLvlEmpMapId;
	
	@Column(name="EMP_ID")
	private Long empId;
	
	@Column(name="EMP_NAME")
	private String empName;
	
	@Column(name="EMP_MAIL")
	private String empMail;
	
}
