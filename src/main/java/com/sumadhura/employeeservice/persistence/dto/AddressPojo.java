package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class AddressPojo {

	@Column(name="ADDRESS_ID")
	private Long addressId;
	@Column(name="STREET")
	private String street;
	@Column(name="LATITUDE")
	private Long latitude;
	@Column(name="LONGITUDE")
	private Long Longitude;
	@Column(name="SURVEY_NO")
	private String surveyNo;
	@Column(name="DISTRICT")
	private String district;
	@Column(name="AREA")
	private String area;
	@Column(name="PINCODE")
	private String pincode;
	@Column(name="CONTACT_NO")
	private String contactNo;
	@Column(name="EMAIL")
	private String email;
	@Column(name="WEBSITE")
	private String website;
	@Column(name="CITY_ID")
	private Long cityId;
	@Column(name="STATE_ID")
	private Long stateId;
	@Column(name="NEARBY")
	private String nearBy;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="ADDRESS1")
	private String address1;
	@Column(name="ADDRESS2")
	private String address2;
	@Column(name="ADDRESS3")
	private String address3;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CITY")
	private String city;
	@Column(name="STATE")
	private String state;
	@Column(name="COUNTRY_ID")
	private Long countryId;
	@Column(name="COUNTRY_NAME")
	private String country;
	
	

}
