package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlatRequest extends Result {

	private static final long serialVersionUID = -163308350029834484L;
	
	private Long siteId;
	private List<Long> blockDetIds;
	private List<Long> floorDetIds;
	private Timestamp fromDate;
	private Timestamp toDate;
	
}
