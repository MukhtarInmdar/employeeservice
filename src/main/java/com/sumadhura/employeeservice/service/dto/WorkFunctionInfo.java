/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;


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
public class WorkFunctionInfo {
	
	private Long workFunctionId;
	private String workFunctionName;
	private String ifOtherworkFunctionName;
	
}
