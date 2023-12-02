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
public class PoaHolderPojo {

	@Column(name="POA_HOLDER_ID")
	private Long PoaHolderId;
	@Column(name="POA_HOLDER_NAME")
	private String nameOfPOA;
	@Column(name="MOBILENO")
	private String mobileNumOfPOA;
	@Column(name="TELEPHONE")
	private String telOfPOA;
	@Column(name="ADDRESS1")
	private String addressOfPOA;
	@Column(name="ADDRESS2")
	private String address2;
	@Column(name="ADDRESS3")
	private String address3;
	
	@Column(name="PINCODE")
	private String pincodeOfPOA;
	@Column(name="STATE_ID")
	private Long   stateId;
	@Column(name="CITY_ID")
	private Long cityId;
	@Column(name="CITY")
	private String city;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="EMAIL")
	private String emailOfPOA;
	@Column(name="CUSTOMER_OTHER_DETAILS_ID")
	private Long custOtherDetailsId;
	
	@Column(name="STATE")
	private String   state;
	@Column(name="COUNTRY_ID")
	private Long countryId;
	@Column(name="COUNTRY")
	private String country;
	
}
