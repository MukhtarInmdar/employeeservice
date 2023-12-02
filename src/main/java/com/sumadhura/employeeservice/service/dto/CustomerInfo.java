package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerInfo implements Serializable {
	
	private static final long serialVersionUID = 9069554047222591979L;
	private long id;
	private long custNo;
	private String addaharNo;
	/*private String firstname;
	private String lastname ;*/
	private String name ;
	private String address  ; 
	private int phone ; 
	private Long mobile    ; 
	private String email ;
	private Long customerId;
	private String namePrefix;
	private String firstName;
	private String lastName;
	private String gender;
	private Long age;
	private String aadharNumber;
	private Long stateId;
	private String phoneNo;
	private Integer pincode  ; 
	private Integer alternatePhoneNo;
	private String telephone;
	private String pancard;
	private String passport;
	private String voterId;
	private Integer addressId;
	/*private String relationname;*/
	private String maritalStatus;
	private String documentsUpload;
	private Integer customerProfileId;
	private String deviceId;
	private Long siteId;
	private String siteName;
	private Long flatId;
	private Long cityId;
	private String relationName;
	private Timestamp dob;
	private String profilePic;
	private String nationality;
	private String adharNumber;
	private String relationNamePrefix;
	private String relationWith;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Long flatBookingId;
	private Long blockId;
	private String mailSentTo;
    private Long bankerListId;
	
}
	
	
	




