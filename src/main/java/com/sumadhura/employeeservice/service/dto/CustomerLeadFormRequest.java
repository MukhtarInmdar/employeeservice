package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CustomerLeadFormRequest bean class provides CustomerLeadFormRequest specific properties.
 * 
 * @author Inamdar
 * @since 21.01.2023
 * @time 01:10PM
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
@Getter
@Setter
public class CustomerLeadFormRequest extends Result implements Serializable {

	private static final long serialVersionUID = 2389958727102702205L;
	private Long leadId;
	private String customerName;
	private String company;
	private String designation;
	private String registeredCreated;
	private String leadCreatedToSiteScheduled;
	private String email;
	private String aleternativeEmail1;
	private String alternativeEmail2;
	private Long mobile;
	private Long additionalMobile1;
	private Long additionalMobile2;
	private Long phone;
	private Long extension;
	private Long projectId;
	private Long preferdProjectLocation;
	private Long firstSourceId;
	private Long lastSourceId;
	private Long leadOwnerId;
	private Long minBudget;
	private Long maxBudget;
	private String budgetRange;
	private String requirementType;
	private Long minFlatArea;
	private Long maxFlatArea;
	private Long timeFrameToPurchase;
	private Long housingRequirement;
	private String customerComments;
	private String customerAddressArea;
	private String customerLocality;
	private String customerAlterAddress;
	private int customerCity;
	private int customerState;
	private Long marketingId;
	private String leadCreationStatus;
	private Long leadSubStatusId;
	private Date createdDate;
	private Date modifiedDate;
	private String fromDate;
	private String toDate;
	private String leadInactiveComments;
	private Long channelPartnerNumber;
	private String channelPartnerAddress;
	private String channelPartnerName;
	private String channelPartnerLeadid;
	private String leadTaskComments;
	private Long leadSiteVisitId;
	private String leadProjectName;
	private Date leadDateVisit;
	private int leadSiteVisitStatus;
	private String leadSaleRep;
	private String leadSalesMetComment;
	private String markAsSiteVisit;
	private String markAsBooking;
	private String leadStatus;
	
}
