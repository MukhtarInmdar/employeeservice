package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class AminitiesInfraMasterPojo {

	@Column(name="AMINITITES_INFRA_ID")
	private Long aminititesInfraId;
	@Column(name="AMINITITES_INFRA_NAME")
	private String aminititesInfraName;
	@Column(name="DESCRIPTION")
	private String description;
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
	@Column(name="STATUS")
	private Long status;
	
	public Long getAminititesInfraId() {
		return aminititesInfraId;
	}
	public void setAminititesInfraId(Long aminititesInfraId) {
		this.aminititesInfraId = aminititesInfraId;
	}
	public String getAminititesInfraName() {
		return aminititesInfraName;
	}
	public void setAminititesInfraName(String aminititesInfraName) {
		this.aminititesInfraName = aminititesInfraName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	
	
	
}
