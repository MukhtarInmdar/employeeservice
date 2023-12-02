/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

/**
 * @author VENKAT
 * DATE 07-FEB-2019
 * TIME 05.30 PM
 */

public class FlatCost {

		private Long flatCostId;
		private Long flatId;
		private Double unitNumber;
		private Double sqftCost;
		private Double subaSqft;
		private Double carpetAreaSqft;
		private Double perSqftCost;
		private Double plc;
		private Double floorRise;
		private Double basicFlatCost;
		private Double amenitiesCost;
		private Double gstCost;
		private Double gstPercentage;
		private Double totalCost;
		private Double extraChanges;
		private Double fourWheelerParking;
		private Double twoWheelerParking;
        private Double clubHouse;
        private Double infra;
        private Double modificationCost;
      //  private Double flatCost;
        private Float flatCost;
		private Timestamp createdDate;
		private Timestamp updatedDate;
		private Long statusId;
		private Integer createdBy;
		private Integer updatedBy;
		
		
		/**
		 * @return the statusId
		 */
		public Long getStatusId() {
			return statusId;
		}
		/**
		 * @param statusId the statusId to set
		 */
		public void setStatusId(Long statusId) {
			this.statusId = statusId;
		}
		/**
		 * @return the flatCostId
		 */
		public Long getFlatCostId() {
			return flatCostId;
		}
		/**
		 * @param flatCostId the flatCostId to set
		 */
		public void setFlatCostId(Long flatCostId) {
			this.flatCostId = flatCostId;
		}
		/**
		 * @return the flatId
		 */
		public Long getFlatId() {
			return flatId;
		}
		/**
		 * @param flatId the flatId to set
		 */
		public void setFlatId(Long flatId) {
			this.flatId = flatId;
		}
		/**
		 * @return the unitNumber
		 */
		public Double getUnitNumber() {
			return unitNumber;
		}
		/**
		 * @param unitNumber the unitNumber to set
		 */
		public void setUnitNumber(Double unitNumber) {
			this.unitNumber = unitNumber;
		}
		/**
		 * @return the sqftCost
		 */
		public Double getSqftCost() {
			return sqftCost;
		}
		/**
		 * @param sqftCost the sqftCost to set
		 */
		public void setSqftCost(Double sqftCost) {
			this.sqftCost = sqftCost;
		}
		/**
		 * @return the subaSqft
		 */
		public Double getSubaSqft() {
			return subaSqft;
		}
		/**
		 * @param subaSqft the subaSqft to set
		 */
		public void setSubaSqft(Double subaSqft) {
			this.subaSqft = subaSqft;
		}
		/**
		 * @return the carpetAreaSqft
		 */
		public Double getCarpetAreaSqft() {
			return carpetAreaSqft;
		}
		/**
		 * @param carpetAreaSqft the carpetAreaSqft to set
		 */
		public void setCarpetAreaSqft(Double carpetAreaSqft) {
			this.carpetAreaSqft = carpetAreaSqft;
		}
		/**
		 * @return the perSqftCost
		 */
		public Double getPerSqftCost() {
			return perSqftCost;
		}
		/**
		 * @param perSqftCost the perSqftCost to set
		 */
		public void setPerSqftCost(Double perSqftCost) {
			this.perSqftCost = perSqftCost;
		}
		/**
		 * @return the plc
		 */
		public Double getPlc() {
			return plc;
		}
		/**
		 * @param plc the plc to set
		 */
		public void setPlc(Double plc) {
			this.plc = plc;
		}
		/**
		 * @return the floorRise
		 */
		public Double getFloorRise() {
			return floorRise;
		}
		/**
		 * @param floorRise the floorRise to set
		 */
		public void setFloorRise(Double floorRise) {
			this.floorRise = floorRise;
		}
		/**
		 * @return the basicFlatCost
		 */
		public Double getBasicFlatCost() {
			return basicFlatCost;
		}
		/**
		 * @param basicFlatCost the basicFlatCost to set
		 */
		public void setBasicFlatCost(Double basicFlatCost) {
			this.basicFlatCost = basicFlatCost;
		}
		/**
		 * @return the amenitiesCost
		 */
		public Double getAmenitiesCost() {
			return amenitiesCost;
		}
		/**
		 * @param amenitiesCost the amenitiesCost to set
		 */
		public void setAmenitiesCost(Double amenitiesCost) {
			this.amenitiesCost = amenitiesCost;
		}
		/**
		 * @return the gstCost
		 */
		public Double getGstCost() {
			return gstCost;
		}
		/**
		 * @param gstCost the gstCost to set
		 */
		public void setGstCost(Double gstCost) {
			this.gstCost = gstCost;
		}
		/**
		 * @return the gstPercentage
		 */
		public Double getGstPercentage() {
			return gstPercentage;
		}
		/**
		 * @param gstPercentage the gstPercentage to set
		 */
		public void setGstPercentage(Double gstPercentage) {
			this.gstPercentage = gstPercentage;
		}
		/**
		 * @return the totalCost
		 */
		public Double getTotalCost() {
			return totalCost;
		}
		/**
		 * @param totalCost the totalCost to set
		 */
		public void setTotalCost(Double totalCost) {
			this.totalCost = totalCost;
		}
		/**
		 * @return the createdDate
		 */
		public Timestamp getCreatedDate() {
			return createdDate;
		}
		/**
		 * @param createdDate the createdDate to set
		 */
		public void setCreatedDate(Timestamp createdDate) {
			this.createdDate = createdDate;
		}
		/**
		 * @return the updatedDate
		 */
		public Timestamp getUpdatedDate() {
			return updatedDate;
		}
		/**
		 * @param updatedDate the updatedDate to set
		 */
		public void setUpdatedDate(Timestamp updatedDate) {
			this.updatedDate = updatedDate;
		}
		
		/**
		 * @return the createdBy
		 */
		public Integer getCreatedBy() {
			return createdBy;
		}
		/**
		 * @param createdBy the createdBy to set
		 */
		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}
		/**
		 * @return the updatedBy
		 */
		public Integer getUpdatedBy() {
			return updatedBy;
		}
		/**
		 * @param updatedBy the updatedBy to set
		 */
		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}
		/**
		 * @return the extraChanges
		 */
		public Double getExtraChanges() {
			return extraChanges;
		}
		/**
		 * @param extraChanges the extraChanges to set
		 */
		public void setExtraChanges(Double extraChanges) {
			this.extraChanges = extraChanges;
		}
		/**
		 * @return the fourWheelerParking
		 */
		public Double getFourWheelerParking() {
			return fourWheelerParking;
		}
		/**
		 * @param fourWheelerParking the fourWheelerParking to set
		 */
		public void setFourWheelerParking(Double fourWheelerParking) {
			this.fourWheelerParking = fourWheelerParking;
		}
		/**
		 * @return the twoWheelerParking
		 */
		public Double getTwoWheelerParking() {
			return twoWheelerParking;
		}
		/**
		 * @param twoWheelerParking the twoWheelerParking to set
		 */
		public void setTwoWheelerParking(Double twoWheelerParking) {
			this.twoWheelerParking = twoWheelerParking;
		}
		/**
		 * @return the clubHouse
		 */
		public Double getClubHouse() {
			return clubHouse;
		}
		/**
		 * @param clubHouse the clubHouse to set
		 */
		public void setClubHouse(Double clubHouse) {
			this.clubHouse = clubHouse;
		}
		/**
		 * @return the infra
		 */
		public Double getInfra() {
			return infra;
		}
		/**
		 * @param infra the infra to set
		 */
		public void setInfra(Double infra) {
			this.infra = infra;
		}
		
		/**
		 * @return the modificationCost
		 */
		public Double getModificationCost() {
			return modificationCost;
		}
		/**
		 * @param modificationCost the modificationCost to set
		 */
		public void setModificationCost(Double modificationCost) {
			this.modificationCost = modificationCost;
		}
		/**
		 * @return the flatCost
		 */
		public float getFlatCost() {
			return flatCost;
		}
		/**
		 * @param flatCost the flatCost to set
		 */
		public void setFlatCost(float flatCost) {
			this.flatCost = flatCost;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "FlatCostDTO [flatCostId=" + flatCostId + ", flatId=" + flatId + ", unitNumber=" + unitNumber
					+ ", sqftCost=" + sqftCost + ", subaSqft=" + subaSqft + ", carpetAreaSqft=" + carpetAreaSqft
					+ ", perSqftCost=" + perSqftCost + ", plc=" + plc + ", floorRise=" + floorRise + ", basicFlatCost="
					+ basicFlatCost + ", amenitiesCost=" + amenitiesCost + ", gstCost=" + gstCost + ", gstPercentage="
					+ gstPercentage + ", totalCost=" + totalCost + ", extraChanges=" + extraChanges
					+ ", fourWheelerParking=" + fourWheelerParking + ", twoWheelerParking=" + twoWheelerParking
					+ ", clubHouse=" + clubHouse + ", infra=" + infra + ", modificationCost=" + modificationCost
					+ ", flatCost=" + flatCost + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate
					+ ", statusId=" + statusId + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
		
		
	}

