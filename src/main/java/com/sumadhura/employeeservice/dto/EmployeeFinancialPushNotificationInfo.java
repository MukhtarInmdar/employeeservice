package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class EmployeeFinancialPushNotificationInfo {
	private Long siteId;
	private String siteName;
	private String notificationTitle;
	private String notificationBody;
	private String notificationDescription;
	private Long bookingFormId;
	
	//for Demand note push notification
	private String demandNoteName;
	private String demandNoteUrl;
	private Timestamp demandNoteCreatedDate;
	private String typeMsg;
	private String typeOfPushNotificationMsg;
	private String flatSaleOwnerId;
	private String flatSaleOwner;
	private Financial financial;
	private Long ticketId;
	//List<Map<String,Object>> milestonePushNotificationObj ;

	//for Legal Invoice note push notification
/*	private String legalInvoiceName;
	private String legalInvoiceUrl;
	private Timestamp legalInvoiceCreatedDate;
	private String typeMsg;
	private String typeOfPushNotificationMsg;*/
	
	private EmpFinPushLegalAndModifiNotificationInfo  legalAndModifiNotificationInfo;
	
	//for Receipt Push Notification
	private String status;
	private String paymentMode;
	private String amount;
	private String noOfTerms;
	private String bankName;
	private String paySchedId;
	private String receiptNumber;
	private String referenceNumber;
	private Timestamp createdDate;
	private Timestamp clearenceDate;
	private String uploadedDocs;
	private String mileStoneName;
	private String invoiceDocument;
	private Long paymentDetailsId;
	private Timestamp transactionDate;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	
	private String chequeBounceComment;
	private Timestamp chequeBounceDate;
}
