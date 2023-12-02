package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class FinClosingBalanceReportPojo {
	@Column(name = "SITE_NAME") private String siteName; 
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "BLOCK_ID") private Long blockId;
	@Column(name = "BLOCK_NAME") private String blockName;
	
	@Column(name = "CUST_ID") private Long customerId;
	@Column(name = "CUST_NAME") private String customerName;
	@Column(name = "FLAT_BOOK_ID") 	private Long flatBookingId;
	@Column(name = "FLAT_ID") private Long flatId;
	@Column(name = "FLAT_NO") private String flatNo;
	@Column(name = "BOOKING_DATE") 	private Timestamp bookingDate;
	@Column(name = "AGREEMENT_DATE") private Timestamp agreementDate;
	@Column(name = "PANCARD") private String pancard;
	@Column(name = "SBUA") private String sbua;
	
	@Column(name ="BASIC_FLAT_COST") private Double basicFlatCost;
	@Column(name ="AMENITIES_FLAT_COST") private Double amenitiesFlatCost;
	@Column(name ="TOTAL_COST") private Double totalAgreementCost;
	@Column(name ="TOTAL_AMOUNT_PAID") private Double totalAmountPaid;
	@Column(name ="EXCESS_AMOUNT") private Double excessAmount;
	@Column(name ="TOTAL_PENDING_AMOUNT_AS_PER_WORK_COMPLETION") private Double totalPendingAmountAsPerWorkCompletion;
	
	/* Malladi Changes */
	@Column(name="SALE_DEED_DATE")
	private Timestamp saleDeedDate;
	
	@Column(name="HANDING_OVER_DATE")
	private Timestamp handingOverDate;
	
	@Column(name="REG_DATE")
	private Timestamp registrationDate;

}
