package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TicketReportingResponceWrapper extends Result implements Serializable {
	private static final long serialVersionUID = -1952698791726116290L;
	private List<TicketReportingResponce> ticketReportingResponces;
	private long noOfTickets;
	private long newTT;
	private long openTT;
	private long inprogressTT;
	private long closedTT;
	private long reOpenTT;
	private double avgClosingTime;
	private double avgReplyTime;
	private String avgClosingTimeHrsFormat;
	private String avgReplyTimeHrsFormat;	
}
