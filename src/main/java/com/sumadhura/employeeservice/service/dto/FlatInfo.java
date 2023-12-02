/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author VENKAT
 * DATE 04-MAR-2019
 * TIME 05.06 AM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FlatInfo {

	private Long flatId;
	private Long floorDetId;
	private String flatNo;
	private String imageLocation;
	private Long status_Id;
	private Date expectedHandOverDate;
	
}
