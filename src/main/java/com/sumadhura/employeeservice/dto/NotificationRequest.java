/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NotificationRequest class provides Employee Notification specific properties.
 * 
 * @author Venkat_Koniki
 * @since 03.05.2019
 * @time 05:02PM
 */
@Entity
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown=true)
public class NotificationRequest extends Result implements Serializable{

	private static final long serialVersionUID = 1301346993582872423L;
	private Long id;
	private String message;
	private String description;
	private Long typeOf;
	private String imgLoc;
	private Long employeeId;
	private Long genericId;
	private String osType;
	private String isView;
	
	private List<String> types;
	private List<Long> typeIds;
	private List<String> osTypes;
	private List<Long> stateIds;

	private List<FileInfo> fileInfos;
	
	private String action;
	
	private Date createdDate;
	private Long stateId;
	private Long sendType;
	private Object objList; //I am using this object for Statename DropDown
	
	private List<FileInfo> linkFiles;
	private String linkFileLoc;
	private String notificationText;
	private int pageNo;
	private int pageSize;
	private List<Long> empSiteList;
	private Timestamp startDate;
	private Timestamp endDate;
	private String comments;
	private Long nxtNotSetOfLevId;
	private String notificationType;
	private Timestamp dateOfBirth;
	private Timestamp dateOfAnniversary;
	private String requestUrl;
	private List<Long> eventTypes;
	private Long eventId;
	private String customerName;
	private Long flatId;	
	private Set<Long> floorSet; 
	private String type;
	private List<Long> siteIds;
	private List<Long> blockIds;
	private Long flatCount;
	private Long CompanyOrProjectType;
	private String sendTypeName;
	private Object siteobjList;
	private Date sentDate;
	private String strcreatedDate;
	private String strsentDate;
}
