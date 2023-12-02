/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 05:52PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class POADetailsInfo {
	
	private Long PoaHolderId;
	private String nameOfPOA;
	private String telOfPOA;
	private String mobileNumOfPOA;
	private String emailOfPOA;
	private String addressOfPOA;
	private String pincodeOfPOA;
	private Long stateId;
	private Long cityId;
	private String stateOfPOA;
	private String cityOfPOA;
	private String telePhone;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	
	@Column(name="COUNTRY_ID")
	private Long countryId;
	@Column(name="COUNTRY")
	private String country;

}
