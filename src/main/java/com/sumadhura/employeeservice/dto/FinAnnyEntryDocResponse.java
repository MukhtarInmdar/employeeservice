package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FinAnnyEntryDocResponse {
	private Long finAnnyEntryDocId;
	private Long finAnmsEntryId;
	private String location;
	private String docName;
	private String filePath;
	private Long createdBy;
	private Timestamp createdDate;
}
