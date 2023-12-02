package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FinancialFlatCostDetailsInfo {
	private Long siteId;
	private String siteName;
	private String blockName;
	private String flatNo;
	private Long flatId;
	private Long flatBookingId;
	private Long bookingFormId;
	private List<Long> flatIds;
	private List<Long> blockIds;
	private Double basicFlatCost;
	private Double amenitiesCost;
	private Double flatCostWithBasicAndAmenities;
	private Double gstAmount;
	private Double flatCostWithGST;
	private Double percentageValue;
	private String actionOfRecord;
	private Long createdBy;
	private Long modifiedBy;
	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
}
