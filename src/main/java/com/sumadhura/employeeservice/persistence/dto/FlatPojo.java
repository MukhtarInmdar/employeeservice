package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FlatPojo {

	@Column(name="FLAT_ID")
	private Long flatId;
	
	@Column(name="FLOOR_DET_ID")
	private Long floorDetId;
	
	@Column(name="FLAT_NO")
	private String flatNo;
	
	@Column(name="IMAGE_LOCATION")
	private String imageLocation;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="EXPECTED_HANDOVER_DATE")
	private Timestamp expectedHandoverDate;
	
	@Column(name="FLOOR_ID")
	private Long floorId;
	
	@Column(name="NAME")
	private String floorName;
	
	@Column(name="BLOCK_NAME")
	private String blockName;
	
	@Column(name="BLOCK_ID")
	private Long blockId;
	
	@Column(name="BLOCK_DET_ID")
	private Long blockDetId;
	
}
