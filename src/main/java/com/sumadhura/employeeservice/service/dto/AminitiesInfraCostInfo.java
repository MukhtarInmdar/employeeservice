/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author Srivenu DATE 15-June-2019 TIME 05.30 PM
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AminitiesInfraCostInfo {
	private Long aminititesInfraCostId;
	private Long aminititesInfraFlatWiseId;
	private Long aminititesInfraId;
	private String aminititesInfraName;
	private Long flatCostId;
	private Double perSqftCost;
	private Double aminititesInfraCost;
	private Double totalCost;
	private Double gstAmount;
	private Timestamp creationDate;
	private Timestamp modifyDate;
	private Long statusId;
	private Long createdBy;
	private Long modifyBy;

}
