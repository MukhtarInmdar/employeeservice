package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;


@Entity
@Data
public class FloorDetailsPojo {

	@Column(name="FLOOR_DET_ID")
	private Long floorDetId;
	
	@Column(name="BLOCK_DET_ID")
	private Long blockDetId;
	
	@Column(name="FLOOR_ID")
	private Long floorId;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="IMAGE_LOCATION")
	private String imageLocation;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="NAME")
	private String floorName;
	
	@Column(name="BLOCK_NAME")
	private String blockName;
	
	@Column(name="BLOCK_ID")
	private Long blockId;
	
}
