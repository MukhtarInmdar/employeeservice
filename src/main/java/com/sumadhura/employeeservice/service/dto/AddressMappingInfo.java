/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Srivenu Aare
 * @since 31.05.2019
 * @time 01:52PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AddressMappingInfo {
	
	private Long addressMappingTypeId;
	private Long typeId;
	private Long type;
	private String metaType;
	private String addressType;
	private Long addressId;
	private String actionUrl;

}
