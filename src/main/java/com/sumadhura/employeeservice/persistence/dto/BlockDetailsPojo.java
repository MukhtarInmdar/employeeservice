package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class BlockDetailsPojo {

	@Column(name="BLOCK_DET_ID")
	private Long blockDetId;
	
	@Column(name="IMAGE_LOCATION")
	private String imageLocation;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="BLOCK_ID")
	private Long blockId;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="NAME")
	private String blockName;
	
}
