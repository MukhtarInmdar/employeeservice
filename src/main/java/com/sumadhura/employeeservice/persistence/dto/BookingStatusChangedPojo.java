package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class BookingStatusChangedPojo {
	@Column(name = "BOOKING_CHANGED_DTLS_ID") private Long bookingChangedDtlsId ;
	@Column(name = "FLAT_BOOK_ID") private Long flatBookingId;
	@Column(name = "ACTUAL_STATUS") private Long actualStatusId ;
	@Column(name = "CHANGED_STATUS") private Long changedStatusId ;
	@Column(name = "EMP_ID") private Long empId ;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "REMARKS") private String remarks;
}
