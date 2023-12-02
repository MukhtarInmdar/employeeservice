/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 * CustomerProfile class provides customer address specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 07:39PM
 */
public class CustomerProfileResponse extends Result implements Serializable{

	private static final long serialVersionUID = 5614031549156611871L;
	
	private Customer customer;
	private List<CoApplicant>  coApplicants;
	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	/**
	 * @return the coApplicants
	 */
	public List<CoApplicant> getCoApplicants() {
		return coApplicants;
	}
	/**
	 * @param coApplicants the coApplicants to set
	 */
	public void setCoApplicants(List<CoApplicant> coApplicants) {
		this.coApplicants = coApplicants;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerProfile [customer=" + customer + ", coApplicants=" + coApplicants + ", responseCode="
				+ responseCode + ", description=" + description + ", errors=" + errors + ", sessionKey=" + sessionKey
				+ "]";
	}
	

}
