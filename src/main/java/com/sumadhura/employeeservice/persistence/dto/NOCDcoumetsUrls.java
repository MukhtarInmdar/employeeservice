package com.sumadhura.employeeservice.persistence.dto;



import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class NOCDcoumetsUrls {
	
	@Column(name = "NOC_RELEASE_ID") private Long nocReleaseId;
	@Column(name = "NOC_DOCUMENTS_ID") private Long nocDocumetId;
	@Column(name = "DOC_LOCATION") private String docLocation;
	@Column(name = "URL_LOCATION") private String nocURLLocation;
	@Column(name = "DOC_NAME") private String docName;

}
