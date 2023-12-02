package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerProfessionalInformationPojo {

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "CUST_ADD_ID")
	private String nameOfOrganization;

	@Column(name = "CUST_ADD_ID")
	private String addressOfOrganization;

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getNameOfOrganization() {
		return nameOfOrganization;
	}

	public void setNameOfOrganization(String nameOfOrganization) {
		this.nameOfOrganization = nameOfOrganization;
	}

	public String getAddressOfOrganization() {
		return addressOfOrganization;
	}

	public void setAddressOfOrganization(String addressOfOrganization) {
		this.addressOfOrganization = addressOfOrganization;
	}

	
	
}
