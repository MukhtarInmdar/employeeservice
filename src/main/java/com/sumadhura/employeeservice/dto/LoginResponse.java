package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class LoginResponse extends Result implements Serializable{

	private static final long serialVersionUID = 7303599456802940779L;
	private List<Department> departments;
	private String empName;
	private long empId;
	private Employee employee;
	
}
