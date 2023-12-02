package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class LevelPojo {
	

	@Column (name = "LEVEL_ID")
	private Long levelId;
	
	@Column (name = "LEVEL_NAME")
	private String levelName;
	

}
