/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * CustomerPropertyDetailsPojo class provides CUSTOMER Property specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 12:18AM
 */

@Entity
@Data
public class CustomerPropertyDetailsPojo {

	@Column(name = "CUST_ID")
	private Long customerId;

	@Column(name = "CUST_NAME")
	private String customerName;

	@Column(name = "CUST_EMAIL")
	private String customerEmail;
	
	@Column(name = "CONTACT_NO")
	private String contactNumber;
	
	@Column(name = "CUST_PROFILE_PIC")
	private String customerProfilePic;

	@Column(name = "SALES_TEAM_LEAD_ID")
	private Long salesTeamLeadId;
	
	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookingId;

	@Column(name = "FLAT_ID")
	private Long flatId;

	@Column(name = "FLAT_NO")
	private String flatNo;

	@Column(name = "FLOOR_ID")
	private Long flooId;

	@Column(name = "FLOOR_NAME")
	private String floorName;

	@Column(name = "BLOCK_ID")
	private Long blockId;

	@Column(name = "BLOCK_NAME")
	private String blockName;

	@Column(name = "SITE_ID")
	private Long siteId;

	@Column(name = "SITE_NAME")
	private String siteName;
	
	@Column(name = "STATE_ID")
	private Long stateId;

	@Column(name = "CUST_BOOK_INFO_ID")
	private Long custBookingId;
	
	@Column(name = "CUST_PROFFISIONAL_ID")
	private Long proId;
	
	@Column(name = "SITE_SHORT_FORM")
	private String siteShortName;
	
	@Column(name = "PANCARD")
	private String pancard;
	
	@Column(name = "ALTERNATIVE_CONTACT_NO")
	private String alternatePhoneNo;
	
	@Column(name = "BOOKING_DATE")
	private Timestamp bookingDate;
	
	@Column(name = "BOOKING_STATUS")
	private Long bookingStatus;
	
	@Column(name ="MILESTONE_DAYS")
	private Long milestoneDueDays;
	
	@Column(name ="TOTAL_COST_WITH_GST")
	private Double totalAgreementCost;
	
	@Column(name ="FLAT_COST")
	private Double flatCost;
	
	@Column(name ="SBUA")
	private Long sbua;
	
	@Column(name = "AGREEMENT_DATE")
	private Timestamp agreementDate;
	
	@Column(name ="BHK")
	private String bhk;
	
	@Column(name="APP_STATUS")
	private String appStatus;
	
	@Column(name="LAST_LOGIN_TIME")
	private Timestamp lastLoginTime;
	
	@Column(name="FLAT_SALE_OWNER")
	private String flatSaleOwner;
	
	@Column(name="FLATS_SALE_OWNERS_ID")
	private String flatSaleOwnerId;
	
	/* Malladi Changes */
	@Column(name="SALE_DEED_DATE")
	private Timestamp saleDeedDate;
	
	@Column(name="HANDING_OVER_DATE")
	private Timestamp handingOverDate;
	
	@Column(name="REG_DATE")
	private Timestamp registrationDate;
	
	@Column(name ="BASIC_FLAT_COST") 
	private Double basicFlatCost;
	
	@Column(name ="AMENITIES_FLAT_COST") 
	private Double amenitiesFlatCost;
	
	@Column(name ="TOTAL_FLAT_COST_EXCL_GST")
	private Double totalFlatCostExclGst;
	
	@Column(name ="EMAIL")
	private String email;
	
	private List<String> filePaths;
	
	@Column(name ="TOTAL_COST")
	private String totalCost;
	
	@Column(name ="INTERESTED_BANK_NAME")
	private String interestedBankName;

	@Column(name ="CONTACT_PERSON")
	private String bankContactPersonName;
	
	@Column(name ="CRM_MAILS")
	private String crmMails;
	
}
