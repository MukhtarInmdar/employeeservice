/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;




/**
 * CustomerBookingFormInfo class provides CustomerBookingForm specific fields.
 * 
 * @author Srivenu
 * @since 30.05.2019
 * @time 11:55AM
 */


@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerBookingFormInfo extends Result implements Serializable {
	
	private static final long serialVersionUID = 3025681126550993608L;
	private CustomerInfo customerInfo;
	//private CustomerSchemeInfo customerSchemeInfo;
	private List<CustomerSchemeInfo> customerSchemeInfos;
	private CustomerBookingInfo customerBookingInfo;
	private List<AddressInfo> addressInfos ;
	private ProfessionalInfo professionalInfo ;
	private CustomerOtherDetailsInfo customerOtherDetailsInfo;
	private List<CoApplicentDetailsInfo> coApplicentDetails;
	private FlatBookingInfo  flatBookingInfo ;
	private CheckListSalesHeadInfo  checkListSalesHead ;
	private CheckListCRMInfo  checkListCRM ;
	private CheckListLegalOfficerInfo  checkListLegalOfficer ;
	private CheckListRegistrationInfo  checkListRegistration ;
	private CustomerApplicationInfo customerApplicationInfo;
	private List<CustomerKYCDocumentSubmitedInfo> customerKYCSubmitedInfo;
	private Long userId;
	private Long employeeId;
	private Long menuId;
	private String employeeName;
	private Boolean customerAppBookingApproval;
	private List<Map<String, Object>> customerFinancialDetails;
	private Long portNumber;
	
}
