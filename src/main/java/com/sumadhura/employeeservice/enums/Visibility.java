/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/*
* Visibility enum provides different types visible status codes.
* 
* @author Venkat_Koniki
* @since 16.05.2019
* @time 04:22PM
*/
public enum Visibility {

	PUBLIC(1L,"PUBLIC"),PRIVATE(2L,"PRIVATE"),ALL(3L,"ALL");

	public Long status;
	public String description;

	Visibility(Long status, String description) {
		this.status = status;
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public Long getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Long status) {
		this.status = status;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
