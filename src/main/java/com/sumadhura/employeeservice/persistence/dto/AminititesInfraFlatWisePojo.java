package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AminititesInfraFlatWisePojo {

	@Column(name="AMINITITES_INFRA_FLAT_WISE_ID")
	private Long aminititesInfraFlatWiseId;
	@Column(name="AMINITITES_INFRA_SITE_WISE_ID")
	private Long aminititesInfraSiteWiseId;
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
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
	
	public Long getAminititesInfraFlatWiseId() {
		return aminititesInfraFlatWiseId;
	}
	public void setAminititesInfraFlatWiseId(Long aminititesInfraFlatWiseId) {
		this.aminititesInfraFlatWiseId = aminititesInfraFlatWiseId;
	}
	public Long getAminititesInfraSiteWiseId() {
		return aminititesInfraSiteWiseId;
	}
	public void setAminititesInfraSiteWiseId(Long aminititesInfraSiteWiseId) {
		this.aminititesInfraSiteWiseId = aminititesInfraSiteWiseId;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public Long getFlatId() {
		return flatId;
	}
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	public Double getPerSqftCost() {
		return perSqftCost;
	}
	public void setPerSqftCost(Double perSqftCost) {
		this.perSqftCost = perSqftCost;
	}
	public Double getAminititesInfraCost() {
		return aminititesInfraCost;
	}
	public void setAminititesInfraCost(Double aminititesInfraCost) {
		this.aminititesInfraCost = aminititesInfraCost;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	
}
