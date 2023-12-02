package com.sumadhura.employeeservice.service.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class DynamicKeyValueInfo {

	@Column(name = "KEY") private String key;
	@Column(name = "VALUE") private Object value;

	public DynamicKeyValueInfo() {
		
	}

	public DynamicKeyValueInfo(String labelName, Object value) {
		super();
		this.key = labelName;
		this.value = value;
	}

}
