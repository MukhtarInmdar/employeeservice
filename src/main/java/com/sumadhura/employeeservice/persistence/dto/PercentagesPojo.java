package com.sumadhura.employeeservice.persistence.dto;


import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class PercentagesPojo {
	
	@Column(name ="PERCENTAGE_ID")
	private Long percentagesId;
	@Column(name ="PERCENTAGE")
	private Double percentage;
}
