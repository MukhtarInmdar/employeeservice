/**
 * 
 */
package com.sumadhura.employeeservice.dto;


import java.sql.Blob;
import java.sql.Timestamp;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Site  class provides site specific fields.
 * 
 * @author Venkat_Koniki
 * @since 26.06.2019
 * @time 12:36PM
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
@Setter
@ToString
public class Site {

	private Long siteId;
	private String name;
	private Long statusId;
	private Timestamp createdDate;
	private String imageLocation;
	private Timestamp modifiedDate;
	private String referMessage;
	private String landmarkImage;
	private String projectArea;
	private String noOfUnits;
	private String rera;
	private Blob description;
	private String overviewImage;
	private String masterPlanImage;
	private String refererDescription;
}
