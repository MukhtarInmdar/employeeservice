package com.sumadhura.employeeservice.persistence.dto;


import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class DropDownPojo {
	@Column(name="SITE_ID")
    private Long id;
	@Column(name="NAME")
    private String name;
	@Column(name="DETID")
	private Long detId;
	@Column(name="MAPID")
	private Long mappingId;
	private String osType;
	@Column(name="SBUA")
	private Long sbua;
	@Column(name="FACING")
	private String facing;
	@Column(name="BHK_TYPE")
	private String bhkType;
	@Column(name="FLAT_SERIES")
	private String flatSeries;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookingId;
	@Column(name="CUST_ID")
	private Long customerId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="STATUS")
	private String status;
	@Column(name="UUID")
	private String uuid;
	@Column(name="DEVICE_ID")
	private Long deviceId;
	private String salesforceSiteName;
	private String salesforceBookingId;
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof DropDownPojo))
			return false;
		if (obj == this)
			return true;

		DropDownPojo chldProd = (DropDownPojo) obj;
		return this.name.equals(chldProd.name);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return name.hashCode();
	}
	
}
