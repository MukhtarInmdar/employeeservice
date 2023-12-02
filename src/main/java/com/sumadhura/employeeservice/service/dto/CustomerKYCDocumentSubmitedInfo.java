package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author Srivenu
 * DATE 30-MAY-2019
 * TIME 11:11 AM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
public class CustomerKYCDocumentSubmitedInfo {
	@Column (name = "DOCUMENT_ID")
	private Long  documentId;
	@Column (name = "DOCUMENT_NAME")
	private String  docName;
	private Long flatBookId;
	private Long custBookInfoId;
	private Long submittedDocId;
	private Long empId;
	private Long statusId;
	//private boolean status;
	private String status;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
}
