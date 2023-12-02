package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class AminitiesInfraSiteWisePojo {

	@Column(name="AMINITITES_INFRA_SITE_WISE_ID")
	private Long aminititesInfraSiteWiseId;
	@Column(name="AMINITITES_INFRA_ID")
	private Long aminititesInfraId;
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
	@Column(name="SITE_ID")
	private Long siteId;
	@Column(name="STATUS")
	private Long status;
	@Column(name="MODIFY_DATE")
	private Timestamp modifyDate;
	
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getAminititesInfraSiteWiseId() {
		return aminititesInfraSiteWiseId;
	}
	public void setAminititesInfraSiteWiseId(Long aminititesInfraSiteWiseId) {
		this.aminititesInfraSiteWiseId = aminititesInfraSiteWiseId;
	}
	public Long getAminititesInfraId() {
		return aminititesInfraId;
	}
	public void setAminititesInfraId(Long aminititesInfraId) {
		this.aminititesInfraId = aminititesInfraId;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}

	
	
}
