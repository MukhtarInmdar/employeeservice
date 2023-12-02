/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author VENKAT
 * DATE 11-MAR-2019
 * TIME 11.11 AM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BlockInfo {

	private Long blockId;
	private String name;
	private Date createdDate;
}
