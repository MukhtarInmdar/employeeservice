/**
 * 
 */
package com.sumadhura.employeeservice.service;




import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.exception.DefaultBankerException;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
import com.sumadhura.employeeservice.exception.SQLInsertionException;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerData;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDocumentsList;
import com.sumadhura.employeeservice.persistence.dto.NOCReleasePojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormSavedStatus;
import com.sumadhura.employeeservice.service.dto.CoApplicantCheckListVerificationInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerCheckListVerificationInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerKYCDocumentSubmitedInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;

/**
 * BookingFormService Interface provides BookingForm specific services.
 * 
 * @author venu
 * @since 26.04.2019
 * @time 05:30PM
 */
public interface BookingFormService {

	public BookingFormSavedStatus saveBookingFormDetails(CustomerBookingFormInfo customerBookingFormInfo) throws InvalidStatusException, InSufficeientInputException, IllegalAccessException;
	public List<CustomerCheckListVerificationInfo> getCustomerCheckListVerifications(BookingFormRequest bookingFormRequest, Department department);
	public List<CoApplicantCheckListVerificationInfo> getCoAppCheckListVerifications(BookingFormRequest bookingFormRequest, Department department, String pancard);
	public List<FlatBookingInfo> getFlatBookingInfo(BookingFormRequest bookingFormRequest);
	public List<String> putActionBookingForm(BookingFormRequest bookingFormRequest) throws  SQLInsertionException;
	public Result getBookingForm(BookingFormRequest bookingFormRequest)throws InSufficeientInputException;
	public List<String> validateBookingForm(CustomerBookingFormInfo customerBookingForm) throws IllegalAccessException;
	public void cancelBookingFormHelper(BookingFormRequest bookingFormRequest) throws InSufficeientInputException;
	public BookingFormSavedStatus insertOrUpdateCheckListDetails(BookingFormRequest bookingFormRequest) throws InSufficeientInputException;
	
	public CustomerBookingFormInfo addOneDay(CustomerBookingFormInfo customerBookingForm);
	public void updateApplicantOrCoApplicantData(BookingFormRequest request)
			throws InSufficeientInputException, IllegalAccessException, SQLInsertionException, InvalidStatusException, Exception;
	public List<FlatBookingInfo> getRegistrationDetails(BookingFormRequest bookingFormRequest) throws Exception;
	public CustomerBookingFormInfo getFlatCustomerAndCoAppBookingDetails(BookingFormRequest bookingFormRequest) throws Exception;
	public List<FlatBookingInfo> loadUnitDetails(BookingFormRequest bookingFormRequest) throws Exception;
	public int saveBookingChangedStatus(BookingFormRequest bookingFormRequest);
	public  Map<String, Object> generateWelcomeLetter(BookingFormRequest bookingFormRequest) throws Exception;
	public Map<String, Object> getAgreementTypesList(BookingFormRequest bookingFormRequest) throws Exception ;
	public Map<String, Object> getWelcomeDocumentLetters(BookingFormRequest bookingFormRequest);
	public Result getCustomerFlatDocuments(MessengerRequest messengerRequest) throws Exception;
	public Map<String, Object> inactiveBookings(BookingFormRequest bookingFormRequest) throws Exception;
	public Map<String, Object> testingUrl(BookingFormRequest bookingFormRequest) throws Exception;
    public Map<String, Object> generateNOCLetter(BookingFormRequest bookingFormRequest, List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList) throws Exception;
    public	List<NOCReleasePojo> getNOCReleaseDetails(BookingFormRequest bookingFormRequest) throws EmployeeFinancialServiceException;
	
	List<NOCDocumentsList> getNOCDocumentsList(BookingFormRequest bookingFormRequest);
	List<CustomerData> getCustomerData(CustomerInfo customerInfo);
	List<FlatBookingInfo> getFlatBookingInfoList(BookingFormRequest bookingFormRequest);
	public List<FlatBookingInfo> loadUnitDetailsList(BookingFormRequest bookingFormRequest);
	public Map<String, Double> getTotalMaintainanceCostToPay(Timestamp startDate, Timestamp endDateForMaintencenDate,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<FlatDetailsPojo> flatDetailsPojos,
			FinPenaltyTaxPojo finTaxPojo, SiteOtherChargesDetailsPojo siteOtherChargesDetailsPojo) throws Exception;
	void sendMailToCustomerForBankLoan(CustomerPropertyDetailsInfo customerPropertyDetailsInfo,
			CustomerInfo customerInfo) throws InSufficeientInputException;
	void sendMailToBankerForLoan(CustomerInfo customerInfo, CustomerPropertyDetailsPojo customerPropertyDetailsPojo) throws DefaultBankerException;
	Map<String, Object> generateLoanNOCLetter(BookingFormRequest bookingFormRequest) throws Exception;
	List<CustomerKYCDocumentSubmitedInfo> getKycDocumentsList(BookingFormRequest bookingFormRequest);
	Map<String,Object> getFlatDetails(BookingFormRequest bookingFormRequest);
	List<CustomerData> getNonBookedDetails(BookingFormRequest bookingFormRequest);
	
	public List<CustomerAddressPojo> getCityList(CustomerInfo customerInfo);
	public List<CustomerAddressPojo> getCountryList();
	List<FinSchemePojo> getFinSchemes(BookingFormRequest bookingFormRequest);
	public List<FlatBookingPojo> getSalesForcesBookingIds(BookingFormRequest bookingFormRequest);

}
