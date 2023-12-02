package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class OfficeDtlsPojo {
	@Column(name ="ID")
	private Long id;
	
	@Column(name ="NAME")
	private String name;
	
	@Column(name ="PLOT_NO")
	private String plotNo;
	
	@Column(name ="FLOOR_NO")
	private String floorNo;
	
	@Column(name ="TOWER")
	private String tower;
	
	@Column(name ="NEAR")
	private String near;
	
	@Column(name ="CITY")
	private String city;
	
	@Column(name ="PINCODE")
	private String pincode;
	
	@Column(name ="CONTACT_NO")
	private String contactNo;
	
	@Column(name ="EMAIL")
	private String email;
	
	@Column(name ="WEBSITE")
	private String website;
	
	@Column(name ="STREET")
	private String street;
	
	@Column(name ="GSTIN")
	private String gstin;
	
	@Column(name ="PAN")
	private String pan;
	
	@Column(name ="CIN")
	private String cin;
	
	@Column(name ="TELEPHONE_NO")
	private String telephoneNo;
	
	@Column(name ="CRM_MAIL")
	private String crmMail;
	
	@Column(name ="BILLING_ADDRESS")
	private String billingAddress;
	
	@Column(name ="LLPIN")
	private String llpin;
	
	
}
