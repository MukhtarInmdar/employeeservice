package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class StatePojo {
	
	@Column(name="STATE_ID")
	private Long stateId;
	@Column(name="STATE_NAME")
	private String stateName;

}
