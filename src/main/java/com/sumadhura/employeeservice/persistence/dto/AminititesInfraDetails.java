package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class AminititesInfraDetails {

	@Column(name="AMINITITES_INFRA_FLAT_WISE_ID")
	private Long aminititesInfraFlatWiseId;
	@Column(name="AMINITITES_INFRA_SITE_WISE_ID")
	private Long aminititesInfraSiteWiseId;
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
	@Column(name="SITE_ID")
	private Long siteId;
	@Column(name="FLAT_ID")
	private Long flatId;
	@Column(name="PER_SQFT_COST")
	private Double perSqftCost;
	@Column(name="AMINITITES_INFRA_COST")
	private Double aminititesInfraCost;
	@Column(name="TOTAL_COST")
	private Double totalCost;
	@Column(name="MODIFY_DATE")
	private Timestamp modifyDate;
	@Column(name="STATUS")
	private Long status;
	@Column(name="AMINITITES_INFRA_ID")
	private Long aminititesInfraId;
	@Column(name="AMINITITES_INFRA_NAME")
	private String aminititesInfraName;
	
	
	
}
