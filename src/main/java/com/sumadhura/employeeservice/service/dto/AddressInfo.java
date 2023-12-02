/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
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
public class AddressInfo implements Serializable {

	private static final long serialVersionUID = 1015951847997681728L;
	private Long addressId;
	private Long custAddressId;
	private String Hno;
	private String floorNo;
	private String tower;
	private String street;
	private String area;
	private String landmark;
	private String pincode;
	private Long cityId;
	private String city;
	private String cityIcon;
	private Long stateId;
	private String state;
	private String country;
	private Double longitude;
	private Double latitude;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private String addressType;
	private String address1;
	private String address2;
	private String address3;
	private Long statusId;
	private String Foraddress; // applicant or co applicant
	private String surveyNo;
	private String district;
	private String contactNo;
	private String email;
	private String website;
	private String nearBy;
	private AddressMappingInfo addressMappingType;
	private String actionUrl;
	private Long countryId;
	
}