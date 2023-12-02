package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * CustomerPropertyDetailsPojo class provides CUSTOMER Property specific fields.
 * 
 * @author Srivenu
 * @since 18.06.2019
 * @time 1:50PM
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerPropertyDetailsInfo implements Cloneable {
	
	private Long customerId;
	private String customerName;
	private Long flatBookingId;
	private Long salesTeamLeadId;
	private Long flatId;
	private String flatNo;
	private Long flooId;
	private String floorName;
	private Long blockId;
	private String blockName;
	private Long siteId;
	private String siteName;
	private Double totalAgreementCost;
	
	//properties for financial module 
	private Long finProjectDemandNoteId;
	private Long transactionSetOffEntryId;
	private Long statusId;
	private List<Long> statusIds;
	private Timestamp startDate;
	private Timestamp endDate;
	private String demandNoteSelectionType;
	private String condition;
	private Long type;
	private Long typeId;
	private String custFullName;
	private String coAppFullName;
	private String pancard;
	private String customerEmail;
	private String contactNumber;
	private String alternatePhoneNo;
	private String finBokAccInvoiceNo;
	private Timestamp bookingDate;
	private Long milestoneDueDays;
	private String actionUrl;
	
	private Long finSchemeId;
	private String finSchemeName;
	private String finSchemeType;
	
	private Timestamp agreementDate;
	private Long sbua;
	private String bhk;
	private String appStatus;
	private Timestamp lastLoginTime;
	private String flatSaleOwnerId;
	private String flatSaleOwner;
	
	/* Malladi Changes */
	private Timestamp saleDeedDate;
	private Timestamp handingOverDate;
	private Timestamp registrationDate;
	private Double basicFlatCost;
	private Double amenitiesFlatCost;
	private Double totalFlatCostExclGst;
	
	private Long nocReleaseId;
	

	/*bvr*/
	private Long flatSaleOwnerIdBasedOnAccountId;
	private String flatSaleOwnerNameBasedOnAccountId;
	private String requestFrom;
	
	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
}
