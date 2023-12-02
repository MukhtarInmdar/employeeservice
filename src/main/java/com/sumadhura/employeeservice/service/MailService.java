package com.sumadhura.employeeservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.dto.SiteLevelNotifyRequestDTO;
import com.sumadhura.employeeservice.dto.TicketReportingResponce;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequestInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;

import lombok.NonNull;


/**
 * MailService Interface provides Mail specific services.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */
public interface MailService {

	public void sendEmail(final Email email);
	public void sendErrorMail(final Exception ex);
	public boolean sendEmployeeTicketForwardMail(final EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendCustomerTicketUpdateMail(final EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendEmployeeTicketEscalationMail(final EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendEmployeeTicketCloseMail(final EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendCustomerTicketCloseMail(final EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendAdminTicketEscalationMail(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendCustomerTicketForwardMail(EmployeeTicketRequestInfo info);
	public boolean sendCustomerTicketEscalationMail(EmployeeTicketRequestInfo info);
	public boolean sendBookingApprovalMail(BookingFormRequestInfo bookingFormRequestInfo);
	public boolean sendEmployeeTicketReOpenMail(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public boolean sendTicketReportingMail(@NonNull final TicketReportingResponce resp);
	public void sendFinancialMailToCustomers(Email email);
	public String geVelocityTemplateContent(Map<String, Object> model);
	public void sendEmployeeMailForChangePassword(List<EmployeePojo> employeePojos);
	public boolean sendChangeTicketTypeMailToAdmin(ChangeTicketType ChangeTicketTypeRequest);
	public boolean sendChangeTicketTypeReminderMailToAdmin(EmployeeTicketRequestInfo emailreq);
	public boolean sendChangeTicketTypeTicketOwnersMail(ChangeTicketType changeTicketTypeRequest);
	public Boolean sendMailNotificationsToApprovalLevelEmployee(List<EmployeePojo> senderEmployeePojoList, List<EmployeePojo> approverEmployeePojoList, SiteLevelNotifyRequestDTO notificationRequest);
	void sendFinancialDemandNoteAndTransactionMailToEmployee(Email email);
	public boolean sendCustomerReferralStatusToEmployeeMail(ReferedCustomer referedCustomer);
	public void sendFinancialRateOfInterestMailToEmployee(Email email);
	public void sendEmployeeEmailAlert(Email email);
	public void sendNOCLetterMailToEmployee(Email email);
	void sendFinancialTransactionExcelToEmployee(Email email);

	String sendMailToBankerOnBooking(
			List<CustomerPropertyDetailsPojo> customerPojo, EmployeeDetailsPojo employeeDetailsPojo, List<EmployeeDetailsPojo> cRM_detailsMailListForErrorMail, Long primaryKey);

	JavaMailSender sendEmailFromEmployeeEmail(Long empId);
	public String sendDefaultBankerErrorMail(Email email);
	public void sendUpdateLeadDetailsToEmployee(CustomerPropertyDetailsPojo customerPropertyDetailsPojo,
			LoanRequest loanRequest, List<EmployeeDetailsPojo> crmdetails);
}
