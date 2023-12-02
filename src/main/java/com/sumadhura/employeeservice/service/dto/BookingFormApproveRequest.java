package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookingFormApproveRequest extends Result implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long leadId;
	private String custName;
	private Long customerId;
	private Long flatBookingId;
	private String flatNo;
	private String floorName;
	private String blockName;
	private String siteName;
	private Timestamp bookingformCanceledDate;
	private String comments;
	private String employeeName;
}
