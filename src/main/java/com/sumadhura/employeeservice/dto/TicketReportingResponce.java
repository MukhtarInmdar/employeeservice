package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TicketReportingResponce implements Serializable{
	private static final long serialVersionUID = -6512351638539266931L;
	private List<TicketReportingPojo> ticketReportingPojos;
	private List<TicketReportingPojo> ticketEmployeeReport;
	private List<TicketReportingPojo> ticketEscalationLevelEmployeeDetails;
	private List<TicketReportingPojo> ticketEscalationEmployeeMails;
	private List<TicketReportingPojo> projectwiseTicketCount;
	private List<TicketReportingPojo> ticktTypewiseTicketCount;
	private long maxArraySize;
	private String siteName;
	private Long siteId;
	private long noOfTickets;
	private long newTT;
	private long openTT;
	private long inprogressTT;
	private long closedTT;
	private long reOpenTT;
	private long totalEscalatedTickets;
}
