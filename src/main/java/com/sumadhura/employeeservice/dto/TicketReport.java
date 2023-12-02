package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketReport {
	private Long ticketId;
	private List<Long> ticketIds;
	private Long siteId;
	private List<Long> siteIds;
	private List<Long> empIds;
	private Timestamp startDate;
	private Timestamp endDate;
	private Long statusId;
	private List<Long> statusIds;
	private String requestUrl;
	private List<Long> ticketTypeIds;
	private List<String> ticketStatusIds;
	private boolean flag;
	private List<Long> ticketTypeDetailsId;
	private List<Long> levelIds;
	private Long departmentId;
	private Long roleId;
	private List<Long> departmentIds;
	private List<Long> ratings;
}
