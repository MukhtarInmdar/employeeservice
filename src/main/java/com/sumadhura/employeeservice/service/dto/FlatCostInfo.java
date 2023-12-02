/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author VENKAT DATE 07-FEB-2019 TIME 05.30 PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FlatCostInfo {

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
	private Double amenitiesFlatCost;
	private Double gstCost;
	private Double gstPercentage;
	private Double totalCost;
	private Double extraChanges;
	private Double fourWheelerParking;
	private Double twoWheelerParking;
	private Double clubHouse;
	private Double infra;
	private Double modificationCost;
	// private Double flatCost;
	private double flatCost;//flatCost nothing but excluding GST
	
	private Date createdDate;
	private Date updatedDate;
	private Long statusId;
	private Long createdBy;
	private Long updatedBy;

	//ACP
	private Double totalCostExcludeGst;
	private Double taxesPerSft;
	private Double soldBasePrice;
	private Double actualPricePerSft;
	private Double overallPricePerSft;
	private String unitGroup;
	private Double quotedBasePrice;
	
	/* Malladi Changes */
	private Long flatBookingId;
}
