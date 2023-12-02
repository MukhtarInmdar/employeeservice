package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class NOCDocumentsList {
	@Column(name = "NOC_RELEASE_ID") private Long nocReleaseId;
	@Column(name = "FLAT_BOOK_ID") private Long flatBookingId;
	@Column(name = "NOC_RELEASE_DATE") private Timestamp nocReleaseDate;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "STATUS_ID") private Long fbStatusId;
	@Column(name = "CUST_ID") private Long custId;
	@Column(name = "CUST_NAME") private String custName;
	@Column(name = "FLAT_ID") private Long flatId;
	@Column(name = "FLAT_NO") private String flatName;
	@Column(name = "FLOOR_ID") private String floorId;
	@Column(name = "FLOOR_NAME") private String floorName;
	@Column(name = "BLOCK_ID") private Long blockId;
	@Column(name = "BLOCK_NAME") private String blockName;
	@Column(name = "SITE_ID") private String siteId;
	@Column(name = "SITE_NAME") private String siteName;
	@Column(name = "NOC_DOCUMENTS_ID") private Long nocDocumetId;
	@Column(name = "DOC_LOCATION") private String docLocation;
	@Column(name = "URL_LOCATION") private String nocURLLocation;
    List<NOCDcoumetsUrls> nocDocUrls;
    private String isNocGenerated;

}
