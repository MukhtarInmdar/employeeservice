package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FlatBookingInfo {
	private FlatInfo flatInfo;
	private FloorInfo floorInfo;
	private BlockInfo blockInfo;
	private SiteInfo siteInfo;
	//private String SBUA;
	private Double carpetArea;
	//private String isEOAApplicable;
	//private String EOASeqNumber;
	private String facing;
	private Double sbua;
	private Double uds;
	private String eoiApplicable;
	private String eoiSequenceNumber;
	//private FlatCost FlatCostDTO;
	private FlatCostInfo flatCost ;
	private List<AminitiesInfraCostInfo> AminitiesInfraCostInfo ;
	private Long CustomerId;
	private Long flatBookingId;
    private Long flatId;
	private Timestamp registrationDate;
	private Long paymentId;
	private Long statusId;
	private Timestamp createdDate;
	private String bookingRecieptFront;
	private String bookingRecieptBack;
	private Timestamp bookingDate;
	private String flatNo;
	private String custName;
	private Double pendingAmount;
	private String pendingAmountDetails;
	// ACP
	private String applicationNumber;
	private Timestamp agreementDate;
	private Long milestoneDueDays;
	private String saleDeedNo;
	private Timestamp saleDeedDate;
	private String saleDeedValue;
	private String registrationStatus;
	private String saleDeedCDno;
	private String balconyArea;
	private String bhk;
	private Double additionalTerusArea;
	private Double proportionateCommonArea;
	private String numberOfBeds;

	private String northSideName;
	private String southSideName;
	private String eastSideName;
	private String westSideName;
	private String carParkingAllotmentNo;
	private String floorPlanLocation;
	private String carParkingSpaces;

	/* Salesforce new parameters */
	@JsonProperty("OldBookingName")
	private String oldBookingName;
	@JsonProperty("NewBookingReason")
    private String newBookingReason;
	@JsonProperty("BookingId")
    private String bookingId;
	private Timestamp handingOverDate;

	private String propertySlNo;
	@JsonProperty("salesforceTransactionId")
	private String salesforceTransactionId;
	private String customerLoanBank;
	private String rrNo;
	@Column(name="SALESFORCE_OLD_BOOKING_ID")
	private String salesforceOldBookingId;
	@Column(name="SALESFORCE_BOOKING_ID")
	private String salesforceBookingId;

}
