/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * FlatBookingPojo class provides FlatBooking Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 04:35PM
 */

@Entity
@Data
public class FlatBookingPojo {

	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookingId;
	@Column(name = "CUST_ID")
	private Long customerId;
	@Column(name = "FLAT_ID")
	private Long flatId;
	@Column(name = "REG_DATE")
	private Timestamp registrationDate;
	@Column(name = "PAYMENT_ID")
	private Long paymentId;
	@Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "BOOKING_RECIEPT_FRONT")
	private String bookingRecieptFront;
	@Column(name = "BOOKING_RECIEPT_BACK")
	private String bookingRecieptBack;
	@Column(name = "BOOKING_DATE")
	private Timestamp bookingDate;
	@Column(name = "UPLOADED_EMP_LEVEL_MAP_ID")
	private Long uploadedEmpLevelMapId;
	@Column(name = "UPLOADED_EMP_ID")
	private Long uploadedEmpId;
	@Column(name = "FLAT_NO")
	private String flatNo;
	@Column(name = "CUST_NAME")
	private String custName;
	@Column(name ="PENDING_AMOUNT")
	private Double pendingAmount;
	
	//ACP
	@Column(name ="APPLICATION_NUMBER") private String applicationNumber;
	@Column(name ="IS_WELCOME_MAIL_SEND") private Long isWelcomeMailSend;
	
	@Column(name ="AGREEMENT_DATE") private Timestamp agreementDate;
	@Column(name ="MILESTONE_DAYS") private Long milestoneDueDays;
	@Column(name ="SALE_DEED_NO") private String saleDeedNo;
	@Column(name ="SALE_DEED_DATE") private Timestamp saleDeedDate;
	@Column(name ="SALE_DEED_VALUE") private String saleDeedValue;
	@Column(name ="REGISTRATION_STATUS") private String registrationStatus;
	@Column(name ="SALE_DEED_CD_NO") private String saleDeedCDno;
	
	@Column(name="SALESFORCE_BOOKING_ID")
	private String salesforceBookingId;
	
	@Column(name="SALESFORCE_OLD_BOOKING_ID")
	private String salesforceOldBookingId;
	
	@Column(name="NEW_BOOKING_REASON")
	private String newBookingReason;
	
	@Column(name="SALESFORCE_TRANSACTION_ID")
	private String salesforceTransactionId;
	
	@Column(name="CUST_BOOK_INFO_ID")
	private Long custBookInfoId;
	
	@Column(name="BLOCK_ID")
	private Long blockId;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
/*	@Column(name="SITE_NAME")
	private Long siteName;*/
	
	@Column(name="CUST_APP_ID")
	private Long custAppId;
	
	@Column(name="CUST_PROFFISIONAL_ID")
	private Long custProId;
	
	@Column(name="HANDING_OVER_DATE")
	private Timestamp handingOverDate;

	@Column(name="PROPERTY_SL_NO")
	private String propertySlNo;
	
	@Column(name="CAR_PARKING_SPACES")
	private String carParkingSpaces;
	
	@Column(name="RR_NO")
	private String rrNo;
	
	@Column(name="CUST_LOAN_BANK")
	private String customerLoanBank;
	
}
