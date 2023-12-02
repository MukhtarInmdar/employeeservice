package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.ToString;




@Entity
@ToString
@Data
public class FlatCostPojo {

	@Column(name="FLAT_COST_ID")
	private Long flatCostId;
	@Column(name="FLAT_ID")
	private Long flatId;
	@Column(name="BASIC_FLAT_COST")
	private Double basicFlatCost;
	@Column(name="AMENITIES_FLAT_COST")
	private Double amenitiesFlatCost;
	@Column(name="MODIFICATION_COST")
	private Double modificationCost;
	@Column(name="FLAT_COST")
	private Double totalCost;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_BY")
	private Long createdBy;
	@Column(name="UPDATED_BY")
	private Long updatedBy;
	@Column(name="PER_SQFT_COST")
	private Double perSqftCost;

	@Column(name = "TOTAL_GST_AMOUNT")
	private Double gstCost;
	@Column(name = "TOTAL_COST_EXCL_GST")
	private Double totalCostExcludeGst;
	@Column(name = "TAXES_PER_SFT")
	private Double taxesPerSft;
	@Column(name = "SOLD_BASE_PRICE")
	private Double soldBasePrice;
	@Column(name = "ACTUAL_PRICE_PER_SFT")
	private Double actualPricePerSft;
	@Column(name = "OVERALL_PRICE_PER_SFT")
	private Double overallPricePerSft;
	@Column(name = "UNIT_GROUP")
	private String unitGroup;
	@Column(name = "QUOTED_BASE_PRICE")
	private Double quotedBasePrice;

	/* Malladi Changes */
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookingId;
}
