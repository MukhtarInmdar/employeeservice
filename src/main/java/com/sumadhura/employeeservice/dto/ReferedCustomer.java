package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReferedCustomer extends Result {

	private static final long serialVersionUID = 5972439905574607360L;

	private String referrerName;
	
	private String refrenceId;
	
	private String customerName;
	
	private Long custId;
	
	private String customerFlatNo;
	
	private String customerImg;
	
	private String referrerImg;
	
	private String refrenceSite;
	
	private String referrerEmailId;

	private String mobileNo;

	private String empComments;
	
	private String status;
	
	private String cityName;
	
	private String stateName;
	
	private Long pincode;
	
	private Long siteId;
	
	private String panNumber;
	
	private String interestFlat;
	
	private String comments;
	
	private Long StatusId;
	
	private Long pagecount;
	
	private String siteNameCustomerFlatNo;
	
	private Long stateId;
	
	private Long flatBookingId;
	
	private String customerSiteName;
	
	private Long id;
	
	private List<String> flatPreferences;

	
	private String customerSiteShortName;
	
	private String referralStatusName;
	
	private Long referralStatusValue;
	
	private List<String> interestedSiteNames;
	
	private String notificationTitle;
	private String notificationBody;
	private String notificationDescription;
	
	private List<Long> siteIds;
	
	private String requestUrl;
	
	private Long flatId;
	private String employeeName;
	private Long empId;
	private String employeeEmailId;
	private String SiteShortName;
	private Timestamp referredDate;
	private String salesforceSiteName;
	private String salesforceBookingId;
    private Long floorDetId;
    private String floorName;
    private Long blockDetId;
    private String blockName;
    private String siteName;

	
}
