package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.util.Util;

import lombok.Data;
import lombok.ToString;

/**
 * TicketResponse bean class provides Employee Ticket Response specific properties.
 * 
 * @author Venkat_Koniki
 * @since 29.04.2019
 * @time 05:53PM
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
@ToString
public class TicketResponse implements Serializable {

	private static final long serialVersionUID = 7207319652077854443L;
	private Long ticketId;
	private String title;
	private String description;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private Long statusId;
	private String status;
	private Long ticketTypeDetailsId;
	private Long departmentId;
	private Long assignmentTo;
	private Long assignedBy;
	private Timestamp assignedDate;
	private Long flatBookingId;
	private Timestamp estimatedResolvedDate;
	private Long estimatedResolvedDateStatus;
	private Timestamp extendedEstimatedResolvedDate;
	private Timestamp resolvedDate;
	private Long statusUpdateBy;
	private Long statusUpdateType;
	private String documentLocation;
	private Long ticketTypeId;
	private Long ticketStatusId;
	private Long departmentTicketStatusId;
	private List<TicketEscalationResponse> ticketEscalationResponses;
	private CustomerPropertyDetails customerPropertyDetails;
	private Department department;
	private TicketType ticketType;
	private List<TicketComment> ticketComments;
	private List<String> viewTicketsPdfs;
	private List<FileInfo> fileInfos;
	private String assignedEmployee;
	private Long ticketEscalationId;
	private String pendingDepartmentName;
	private Boolean isTicketOwner;
    //private int rating;
	 private String rating;
	private String feedbackDesc;	
	private String ticketOwnerName;
    private Long ticketTypeChangeRequest;
    private String ticketClosedBy;
    private Long complaintStatus;
    private Long complaintCreatedBy;
    private Timestamp complaintCreatedDate;
    private String complaintCreatedByEmpName;
    private Boolean isTicketReopenEnable;
    private Long pendingEmpId;
	private Long pendingDeptId;
	private Timestamp reopenenDate;
	public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TicketResponse) {
          final TicketResponse other = (TicketResponse) o;
          final Object this$ticketId = this.ticketId;
          final Object other$ticketId = other.ticketId;
          return Util.isNotEmptyObject(this$ticketId)&&Util.isNotEmptyObject(other$ticketId)?this$ticketId.equals(other$ticketId)?true:false:false;
        }else {
           return false;
        }
    }
	public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object ticketId = this.ticketId;
        result = result * PRIME + (ticketId == null ? 43 : ticketId.hashCode());
        return result;
    }
	
}
