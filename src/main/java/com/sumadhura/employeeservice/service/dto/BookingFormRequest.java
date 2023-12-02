package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * BookingFormRequest bean class provides BookingFormRequest specific properties.
 * 
 * @author Srivenu
 * @since 21.05.2019
 * @time 01:10PM
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
@Getter
@Setter
public class BookingFormRequest extends Result implements Serializable {

	private static final long serialVersionUID = 2389958727172702205L;
	private Long portNumber;
	private Long customerId;
	private Long flatBookingId;
	private Long applicantId;
	private Long flatId;
	private Long flatCostId;
	private Long floorId;
	private String floorName;
	private Long floorDetId;
	private Long blockId;
	private String blockName;
	private Long blockDetId;
	private Long siteId;
	private Long deptId;
	private Long customerAddressId;
	private Long customerProffisionalId;
	private Long proffisionalId;
	private Long custOtherDetailsId;
	private Long applicantAddressId;
	private Long applicantProffisionalId;
	private Long checkListRegistrationId;
	private Long checkListLegalOfficierId;
	private Long checkListCrmId;
	private Long checkListSalesHeadId;
	private Long checkListDeptMapId;
	private Long checkListId;
	private Long checkListVerfiId;
	private Long applicantCheckListVerfiId;
	private Long statusId;
	private Long actualStatusId;
	private String requestUrl;
	private Long metadataId;
	private Long custBookInfoId;
	private Long coAppBookInfoId;
	private Long aminititesInfraSiteWiseId;
	private Long aminititesInfraFlatWiseId;
	private Long aminititesInfraId;
	private String aminititesInfraName;
	private Long aminititesInfraCostId;
	private String siteName;
	private String flatNo;
	private String actionStr;
	//private Long actionStatusId;
	private Long empId;
	private Long menuId;
	private Long empDeptId;
	private List<Long> empSiteId;
	private List<CustomerBookingFormInfo> customerBookingFormsInfos;
	private CustomerBookingFormInfo customerBookingFormsInfo;
	private Long cityId;
	private Long stateId;
	private Long salesTeamLeadId;
	private Timestamp bookingformCanceledDate;
	private String comments;
	private String employeeName;
	private Long coApplicantId;
	private List<BookingFormApproveRequest> bookingFormApproveRequest;
	private String merchantId;
	private Long type;
	private String customerName;
	private String pancard;
	private String email;
	private String mobileNo;
	private String remarks;
	@JsonProperty("BookingId")
    private String bookingId;
	//ACP added, insert old booking trn to new booking
	private String generateDemandNoteForBooking;
	private BookingFormRequest oldBookingRequest;
	private BookingFormRequest newBookingRequest;
	private String showStatusKey;
	private String customerLoanBank;
}
