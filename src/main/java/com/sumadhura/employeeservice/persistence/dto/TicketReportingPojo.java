package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class TicketReportingPojo {
	@Column(name = "TICKET_NO")
	private Long ticketId;
	@Column(name = "TICKET_TYPE")
	private String ticketType;
	private String hrsPending;
	@Column(name = "TICKET_OWNER")
	private String ticketOwner;
	@Column(name = "EMP_ID")
	private Long empId;
	@Column(name = "SITE_ID")
	private Long siteId;
	@Column(name = "SITE_NAME")
	private String siteName;
	@Column(name = "EMAIL")
	private String mail;
	@Column(name = "TICKET_STATUS")
	private String ticketStatus;
	@Column(name = "HIGHLEVEL_STATUS")
	private Long originalTicketStatus;
	@Column(name = "PENDING_EMPLOYEE")
	private String pendingEmployee;
	@Column(name = "EMP_NAME")
	private String employeeName;
	@Column(name = "ESCALATED_COUNT")
	private Long escaltionCount;
	@Column(name = "REOPEN_COUNT")
	private Long reopenCount;
	@Column(name = "LEVEL_ID")
	private Long levelId;
	@Column(name = "DEPT_NAME")
	private Long deptName;
	@Column(name = "TICKET_COUNT")
	private Long ticketCount;
	@Column(name = "CUST_NAME")
	private String customerName;
	@Column(name = "PENDING_DEPT_EMP")
	private String pendingDeptOrEmpName;
	@Column(name = "TICKET_HIGHLEVEL_STATUS")
	private String ticketHighLevelStatus;
	@Column(name = "RECIVED_DATE")
	private Timestamp ticketRecivedDate;
	@Column(name = "ESCALATION_DATE")
	private Timestamp ticketEscalationDate;
	@Column(name = "TICKET_TYPE_ID")
	private Long ticketTypeId;
	@Column(name = "TICKET_STATUS_ID")
	private Long ticketStatusId;
	@Column(name = "TICKET_TYPE_DETAILS_ID")
	private Long ticketTypeDetailsId;
	@Column(name = "ESCALATION_LEVEL")
	private Long escalationLevel;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate; 
	@Column(name = "RESOLVED_DATE")
	private Timestamp resolvedDate; 
	@Column(name = "MODIFIED_DATE")
	private Timestamp lastActivityDate;
	@Column(name="TITLE")
	private String title;
	@Column(name="RATING")
	private Long rating;
	@Column(name="FEEDBACK_DESC")
	private String feedbackDesc;
	@Column(name="ESCALATION_LEVEL_EMP")
	private String escalationLevelEmpName;
	@Column(name="FLAT_NO")
	private String flatNo;
	@Column(name="DESCRIPTION")
	private String description;
	@Column(name="COMPLAINT_OR_QUERY")
	private String complaintOrQuery;
	@Column(name="ESCALATION_TICKET_COUNT")
	private Long escalationTicketCount;
	
	@Column(name="TICKET_IDS")
	private Long ticket_ids;
	
	
	private String closedBy;
	private Timestamp repliedDate;
	private String creation_reply;
	private String reply_close;
	private String lifeTime;
	private long totalTickets;
	private long sitewisetotalTickets;
	private long newT;
	private long openT;
	private long inprogressT;
	private long repliedT;
	private long closedT;
	private long reopenT;
	private long escalatedT;
	private long totalEscalatedTickets;
	private long onedayClosedTickets;
	private long twodaysClosedTickets;
	private long threedaysClosedTickets;
	List<Long> Escalatedtickets;
	private Timestamp stratDate;
	private Timestamp endDate;
	private List<Site> siteList;
	private List<TicketReportingPojo> ticketReportingPojoList;
	@Setter
	@Getter
	@ToString
	public class Site {
	    Long siteId;
	    String siteName;
	    long openT;
	    long closedT;
	    long totalTickets;
	    long sitewisetotalTickets;
	    List<TicketType> ticketTypeList;
	    String employeeName;
	    double avgClosingTime;
	    double avgReplyTime;
	    double avgClosingTimeInMilliSeconds;
	    double avgReplyTimeInMilliSeconds;
	    String avgClosingTimeHrsFormat;
	    String avgReplyTimeHrsFormat;
	    List<TicketReportingPojo> ticketIds;
	    @Setter
		@Getter
		@ToString
	    public class TicketType{
	    	Long ticketTypeDetailsId;
	    	Long ticketTypeId;
	    	String ticketType;
	    	long totalTickets;
	    	long openT;
	    	long closedT;
	    }
	}    
}
