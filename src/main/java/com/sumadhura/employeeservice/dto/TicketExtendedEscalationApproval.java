package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class TicketExtendedEscalationApproval {

	private Long id;
	private Long ticketId;
	private Timestamp currentEscalationDateTimestamp;
	private Timestamp extendedEscalationDate;
	private Long requestedBy;
	private Long requestedTo;
	private Long approvedStatus;
	private String comments;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private Long status;
	private Long noOfDays;
}
