/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Srivenu
 * DATE 16-June-2019
 * TIME 05.30 PM
 */

@Entity
@Table(name="AMINITITES_INFRA_COST")
public class AminitiesInfraCostPojo {

	@Column(name="AMINITITES_INFRA_COST_ID")
	private Long aminititesInfraCostId;
	@Column(name="AMINITITES_INFRA_FLAT_WISE_ID")
	private Long aminititesInfraFlatWiseId;
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
	@Column(name="FLAT_COST_ID")
	private Long flatCostId;
	@Column(name="PER_SQFT_COST")
	private Double perSqftCost;
	@Column(name="AMINITITES_INFRA_COST")
	private Double aminititesInfraCost;
	@Column(name="TOTAL_COST")
	private Double totalCost;
	@Column(name="CREATED_BY")
	private Long createdBy;
	@Column(name="MODIFY_DATE")
	private Timestamp modifyDate;
	@Column(name="MODIFY_BY")
	private Long modifyBy;
	@Column(name="STATUS_ID")
	private Long statusId;
	
	public Long getAminititesInfraCostId() {
		return aminititesInfraCostId;
	}
	public void setAminititesInfraCostId(Long aminititesInfraCostId) {
		this.aminititesInfraCostId = aminititesInfraCostId;
	}
	public Long getAminititesInfraFlatWiseId() {
		return aminititesInfraFlatWiseId;
	}
	public void setAminititesInfraFlatWiseId(Long aminititesInfraFlatWiseId) {
		this.aminititesInfraFlatWiseId = aminititesInfraFlatWiseId;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public Long getFlatCostId() {
		return flatCostId;
	}
	public void setFlatCostId(Long flatCostId) {
		this.flatCostId = flatCostId;
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
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(Long modifyBy) {
		this.modifyBy = modifyBy;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	
}

