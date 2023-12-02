package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@ToString
@Getter
@Setter
@Entity
public class SiteLevelNotifyRequestDTO extends NotificationRequest {

	private static final long serialVersionUID = -6899127449843450218L;

	private List<Long> siteIds;
	
	private List<Long> blockIds;
	
	private List<Long> floorIds;
	
	private List<Long> flatIds;
	
	private List<Long> sbuaList;
	
	private List<String> facingList;
	
	private List<String> bhkTypeList;
	
	private List<String> flatSeriesList;
	
	private Set<Long> bookingDateIds;
	
	@Column (name = "TYPE_ID")
	private Long typeId;
	
	@Column (name = "TYPE_DATE")
	private Timestamp typeDate;
	
	private List<Long> selectedFlatIds;
	
	@SuppressWarnings("rawtypes")
	private Map flatMap;
	
	private String requestAction;
	
	private String flatStatus;
	
	private String requestPurpose;
	
	@Column (name = "NOTIF_CREATED_BY")
    private  String notiCreatedBy;
	
	private Map<String,Object> data;
}
