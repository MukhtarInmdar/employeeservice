package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;




@Entity
public class FlatDetailsPojo {

	@Column(name="FLAT_DET_ID")
	private Long flatDetId;
	@Column(name="FLAT_ID")
	private Long flatId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="IMAGE_LOCATION")
	private String imageLocation;
	@Column(name="FLOOR_DET_ID")
	private Long floorDetId;
	@Column(name="BHK_ID")
	private Long bhkId;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="PROPERTY_UPDATES_CAROUSEL")
	private String propertyUpdatedCarousel;
	@Column(name="UPLOADED_DOCS")
	private String uploadedDos;
	@Column(name="FACING")
	private String facing;
	@Column(name="SBUA")
	private Double sbua;
	@Column(name="CARPET_AREA")
	private Double carpetArea;
	@Column(name="UDS")
	private Double uds;
	@Column(name="EOI_APPLICABLE")
	private String eoiApplicable;
	@Column(name="EOI_SEQUENCE_NUMBER")
	private String eoiSequenceNumber;
	
	@Column(name="BALCONY_AREA")
	private String balconyArea;
	
	@Column(name="BHK")
	private String bhk;
	
	@Column(name="ADDITIONAL_TERUS_AREA")
	private Long additionalTerusArea;

	@Column(name="PROPORTIONATE_COMMON_AREA")
	private Double proportionateCommonArea;
	
	@Column(name="NUMBER_OF_BEDS")
	private String numberOfBeds;
	
	@Column(name="NORTH_SIDE_NAME")
	private String northSideName;

	@Column(name="SOUTH_SIDE_NAME")
	private String southSideName;

	@Column(name="EAST_SIDE_NAME")
	private String eastSideName;

	@Column(name="WEST_SIDE_NAME")
	private String westSideName;

	@Column(name="CAR_PARKING_ALLOT_NO")
	private String carParkingAllotmentNo;

	@Column(name="FLOOR_PLAN_LOCATION")
	private String floorPlanLocation;

	/*@Column(name="CAR_PARKING_SPACES")
	private String carParkingSpaces;*/
	
	public String getFloorPlanLocation() {
		return floorPlanLocation;
	}
	public void setFloorPlanLocation(String floorPlanLocation) {
		this.floorPlanLocation = floorPlanLocation;
	}
	/*public String getCarParkingSpaces() {
		return carParkingSpaces;
	}
	public void setCarParkingSpaces(String carParkingSpaces) {
		this.carParkingSpaces = carParkingSpaces;
	}*/
	public Long getAdditionalTerusArea() {
		return additionalTerusArea;
	}
	public void setAdditionalTerusArea(Long additionalTerusArea) {
		this.additionalTerusArea = additionalTerusArea;
	}
	public Double getProportionateCommonArea() {
		return proportionateCommonArea;
	}
	public void setProportionateCommonArea(Double proportionateCommonArea) {
		this.proportionateCommonArea = proportionateCommonArea;
	}
	public String getNorthSideName() {
		return northSideName;
	}
	public void setNorthSideName(String northSideName) {
		this.northSideName = northSideName;
	}
	public String getSouthSideName() {
		return southSideName;
	}
	public void setSouthSideName(String southsideName) {
		this.southSideName = southsideName;
	}
	public String getEastSideName() {
		return eastSideName;
	}
	public void setEastSideName(String eastSideName) {
		this.eastSideName = eastSideName;
	}
	public String getWestSideName() {
		return westSideName;
	}
	public void setWestSideName(String westSideName) {
		this.westSideName = westSideName;
	}
	public String getCarParkingAllotmentNo() {
		return carParkingAllotmentNo;
	}
	public void setCarParkingAllotmentNo(String carParkingAllotmentNo) {
		this.carParkingAllotmentNo = carParkingAllotmentNo;
	}
	public String getNumberOfBeds() {
		return numberOfBeds;
	}
	public void setNumberOfBeds(String numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}
	public String getBalconyArea() {
		return balconyArea;
	}
	public void setBalconyArea(String balconyArea) {
		this.balconyArea = balconyArea;
	}
	public String getBhk() {
		return bhk;
	}
	public void setBhk(String bhk) {
		this.bhk = bhk;
	}
	public Long getFlatDetId() {
		return flatDetId;
	}
	public void setFlatDetId(Long flatDetId) {
		this.flatDetId = flatDetId;
	}
	public Long getFlatId() {
		return flatId;
	}
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	public Long getFloorDetId() {
		return floorDetId;
	}
	public void setFloorDetId(Long floorDetId) {
		this.floorDetId = floorDetId;
	}
	public Long getBhkId() {
		return bhkId;
	}
	public void setBhkId(Long bhkId) {
		this.bhkId = bhkId;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getPropertyUpdatedCarousel() {
		return propertyUpdatedCarousel;
	}
	public void setPropertyUpdatedCarousel(String propertyUpdatedCarousel) {
		this.propertyUpdatedCarousel = propertyUpdatedCarousel;
	}
	public String getUploadedDos() {
		return uploadedDos;
	}
	public void setUploadedDos(String uploadedDos) {
		this.uploadedDos = uploadedDos;
	}
	public String getFacing() {
		return facing;
	}
	public void setFacing(String facing) {
		this.facing = facing;
	}
	public Double getSbua() {
		return sbua;
	}
	public void setSbua(Double sbua) {
		this.sbua = sbua;
	}
	public Double getCarpetArea() {
		return carpetArea;
	}
	public void setCarpetArea(Double carpetArea) {
		this.carpetArea = carpetArea;
	}
	public Double getUds() {
		return uds;
	}
	public void setUds(Double uds) {
		this.uds = uds;
	}
	public String getEoiApplicable() {
		return eoiApplicable;
	}
	public void setEoiApplicable(String eoiApplicable) {
		this.eoiApplicable = eoiApplicable;
	}
	public String getEoiSequenceNumber() {
		return eoiSequenceNumber;
	}
	public void setEoiSequenceNumber(String eoiSequenceNumber) {
		this.eoiSequenceNumber = eoiSequenceNumber;
	}
	
	
}
