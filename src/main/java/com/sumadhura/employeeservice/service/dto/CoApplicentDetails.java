/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 05:52PM
 */


@Data
public class CoApplicentDetails {
	
	
	private CoApplicentInfo coApplicentInfo;
	private List<AddressInfo> addressInfos;
	private ProfessionalInfo professionalInfo;
	
	public CoApplicentInfo getCoApplicentInfo() {
		return coApplicentInfo;
	}
	public void setCoApplicentInfo(CoApplicentInfo coApplicentInfo) {
		this.coApplicentInfo = coApplicentInfo;
	}
	public List<AddressInfo> getAddressInfos() {
		return addressInfos;
	}
	public void setAddressInfos(List<AddressInfo> addressInfos) {
		this.addressInfos = addressInfos;
	}
	public ProfessionalInfo getProfessionalInfo() {
		return professionalInfo;
	}
	public void setProfessionalInfo(ProfessionalInfo professionalInfo) {
		this.professionalInfo = professionalInfo;
	}

	
}
