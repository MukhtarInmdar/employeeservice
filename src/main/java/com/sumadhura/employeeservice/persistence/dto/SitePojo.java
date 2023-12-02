/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * LoginDao Interface provides Employee login specific functionalities.
 * 
 * @author Venkat_Koniki
 * @since 20.06.2019
 * @time 05:41PM
 */

@Data
@Entity
public class SitePojo {

		@Column(name="SITE_ID")
		private Long siteId;
		@Column(name="NAME")
		private String name;
		@Column(name="CITY_ID")
		private Long cityId;
		@Column(name="STATUS_ID")
		private Long statusId;
		@Column(name="CREATED_DATE")
		private Timestamp createdDate;
		@Column(name="IMAGE_LOCATION")
		private String imageLocation;
		@Column(name="MODIFIED_DATE")
		private Timestamp modifiedDate;
		@Column(name="REFERMESSAGE")
		private String referMessage;
		@Column(name="STATE_ID")
		private Long stateId;
		@Column(name="LANDMARK_IMAGE")
		private String landmarkImage;
		@Column(name="PROJECT_AREA")
		private String projectArea;
		@Column(name="NO_OF_UNITS")
		private String noOfUnits;
		@Column(name="RERA")
		private String rera;
		/*@Column(name="DESCRIPTION")
		private Blob description;*/
		@Column(name="OVERVIEW_IMAGES")
		private String overviewImage;
		@Column(name="MASTERPLAN_IMAGE")
		private String masterPlanImage;
		@Column(name="REFERER_DESCRIPTION")
		private String refererDescription;
		@Column(name="SHORT_FORM")
		private String siteShortName;
		@Column(name="SALES_FORCE_SITE_NAME")
		private String salesForceSiteName;
		@Column(name="OFFICE_DTLS_ID")
		private String officDtlsId;
	
}
