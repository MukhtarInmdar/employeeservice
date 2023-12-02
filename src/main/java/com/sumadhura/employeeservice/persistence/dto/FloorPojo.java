package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class FloorPojo {
	@Column(name="FLOOR_ID")
	private Long floorId;
	@Column(name="NAME")
	private String floorName;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	
	public Long getFloorId() {
		return floorId;
	}
	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	
		
	
	
	
}
