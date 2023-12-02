/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.AppRegistrationPojo;
import com.sumadhura.employeeservice.persistence.dto.ChangeTicketTypePojo;
import com.sumadhura.employeeservice.persistence.dto.ChatInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLeaveDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLevelDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.LevelPojo;
import com.sumadhura.employeeservice.persistence.dto.MailConfigurationDtlsDTO;
import com.sumadhura.employeeservice.persistence.dto.StatusPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketConversationDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscLevelMapPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscaLevelEmpMap;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationExtenstionApprovalLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationExtenstionApprovalLevelPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelEmployeeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketExtendedEscalationApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketForwardMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPendingDeptDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoRequestPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypePojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypesPojo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.MailConfigurationDtlsInfo;
import com.sumadhura.employeeservice.util.Page;

/**
 * EmployeeTicketDao Interface provides Employee Ticketing specific services.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
public interface EmployeeTicketDao {

	public List<ChatInfoPojo> getTicketDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public Page<TicketPojo> getTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status,int pageNo,int pageSize);
	public List<TicketPojo> getTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
	public List<TicketEscalationPojo> getTicketEscalationDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
	public List<TicketTypeDetailsPojo> getTicketTypeDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
	public List<EmployeeDetailsPojo> getEmployeeDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
	public List<TicketCommentsPojo> getTicketComments(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<DepartmentPojo>  getDepartmentDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<TicketTypePojo> getTicketTypeInfo(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<StatusPojo> getTicketStatusDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo);
    public List<CustomerPojo> getCustomerDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<FlatBookingPojo> getFlatbookingDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<CoApplicantPojo> getCoApplicantDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<CustomerAddressPojo> getCustomerAddressDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public Long insertTicketConversation(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public Long insertTicketConversationDocuments(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<TicketConversationDocumentsPojo> getTicketConversationDocumentsDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<TicketForwardMenuPojo> getTicketForwardMenuDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public Integer updateTicketDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public Long insertTicketSeekInfoDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<TicketSeekInfoPojo> getTicketSeekInfoDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public Long insertTicketSeekInfoRequest(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public List<TicketSeekInfoRequestPojo> getTicketSeekInfoRequestDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public Long updateTicketStatus(TicketPojo ticketPojo);
	public Long makeAsPublic( TicketConversationDocumentsPojo ticketConversationDocumentsPojo);
	public List<AppRegistrationPojo> getAppregistrationDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
    public String getTicketConversationDocumentsDetailsSeqId(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public Long insertTicketConversation(TicketCommentsPojo ticketCommentsPojo);
	public Long insertExtendEsacalationTime(TicketExtendedEscalationApprovalPojo ticketExtendedEscalationApprovalPojo);
	public List<TicketExtendedEscalationApprovalPojo> getExtendEsacalationTimeDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
	public Long updateTicketEscalationExtenstionApprovalStatus(TicketExtendedEscalationApprovalPojo ticketExtendedEscalationApprovalPojo);
	public Long updateTicketEstimatedResolvedDate(TicketPojo ticketPojo);
	public Long updateTicketEstimatedResolvedDateStatus(TicketPojo ticketPojo);
	public Long insertTicketEscalationDetails(TicketEscalationPojo ticketEscalationPojo);
	public Long insertEmployeeLeaveDetails(EmployeeLeaveDetailsPojo employeeLeaveDetailsPojo);
    public List<EmployeeLeaveDetailsPojo> getEmployeeLeaveDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status);
	public List<EmployeePojo> getEmployee(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public Long insertTicketStatistics(TicketStatisticsPojo ticketStatisticsPojo);
	public Long updateTicketEscalationStatus(TicketEscalationPojo TicketEscalationPojo);
	public List<CustBookInfoPojo> getCustBookInfo(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public List<TicketEscalationExtenstionApprovalLevelPojo> getTicketEscalationExtenstionApprovalLevalDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public Page<TicketPojo> getTickets(int pageNo, int pageSize) throws SQLException;
	public Page<TicketEscalationPojo> getTicketEscalationDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo,Status status, int pageNo, int pageSize);
	public Page<TicketSeekInfoPojo> getTicketSeekInfoDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status, int pageNo, int pageSize);
	public List<EmployeePojo> getTicketOwnerEmployee(TicketPojo ticketPojo, Status status);
	public List<EmployeeDetailsPojo> changeTicketOwnerDropDown(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public Integer changeTicketOwner(EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status, Boolean isAssignmentToEqual);
	public Long updateTicketEscalationExtenstionApprovalStatus(Long ticketId);
	public Long updateTicketReopenStatus(TicketPojo ticketPojo);
	public MailConfigurationDtlsDTO getEmailDtls(MailConfigurationDtlsInfo email);
	public Long updateTicketStatusInactive(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public List<TicketEscalationExtenstionApprovalLevelMappingPojo> getTicketEscaltionExtentionAprovalLevelDetails(
			EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public List<TicketEscalationLevelMappingPojo> getTicketEscaltionAprovalLevelDetails(
			EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public List<TicketEscalationLevelEmployeeMappingPojo> getTicketscalationEmployeeLevelMappingDetails(
			EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status);
	public Long updateTicketCloseStatus(TicketPojo ticketPojo);
	public List<TicketDetailsPojo> getChangeTicketTypeDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo,
			Status status);
	public Long insertChangeTicketTypeRequest(ChangeTicketType changeTicketType);
	public Long updateTicketTypeRequestStatus(ChangeTicketType changeTicketType, Status status);
	public Long insertChangeTicketTypeRequestStatistics(ChangeTicketType changeTicketType);
	public List<ChangeTicketTypePojo> getChangeTicketTypeRequestDetails(ChangeTicketType changeTicketTypeRequest);
	public Long updateChangeTicketTypeRequest(ChangeTicketType changeTicketType);
	public Long updateChangeTicketTypeRequestStatus(ChangeTicketType changeTicketType, Status status);
	public Long updateChangeTicketType(ChangeTicketType changeTicketType, Status status);
	public List<TicketDetailsPojo> getChangeTicketTypeDetails(ChangeTicketType changeTicketTypeRequest);
	public List<TicketReportingPojo> getCustomerTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public List<DepartmentPojo> getDepartmentEmployeeDetails(EmployeeTicketRequestInfo info);
	public List<TicketEscalationPojo> getEscalationpojo(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public Integer updateTicketsComplaint(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public Page<TicketPojo> getTotalTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo, int pageNo, int pageSize);
	public Boolean isTicketOwner(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public Long updateTicketOwnerForNewFlat(TicketPojo ticketPojo);
	public List<TicketPojo> getClosedTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	public List<TicketPendingDeptDtlsPojo> getTicketPendingDeptDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	List<EmployeeDetailsMailPojo> getCrmEmployees(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	List<Long> getTicketTypes(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	Long saveTicketTypeDetails(List<TicketTypesPojo> ticketTypesPojoList);
	Long isEmployeeConfigured(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	List<LevelPojo> getEscalationLevel(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	List<EmployeeLevelDetailsPojo> getEscalationLevelEmployees(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	List<Long> getAllTicketTypes(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType);
	Long saveTicketEscaExtApprovalLevel(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType);
	List<Long> getTicketEscExtApprovalLevelIds(EmployeeTicketRequestInfo employeeTicketRequestInfo,String deptType);
	Long getMaxTicketEscalevelMapId(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	Long saveTicketEscaLevelMap(List<TicketEscLevelMapPojo> mapPojoList);
	Long saveTicketEscaLevelEmpMap(List<TicketEscaLevelEmpMap> mapPojoList);
	Long isEscaltionLevelCreated(EmployeeTicketRequestInfo employeeTicketRequestInfo, String deptType);
	Long isTicketEscaExtApprovalLevelCreated(EmployeeTicketRequestInfo employeeTicketRequestInfo, String deptType);
	Long getMaxTicketEscaExtensionlevelMapId(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	Long saveTicketEscaExtLevelMap(List<TicketEscLevelMapPojo> mapPojoList);
	Long saveTicketEscaExtLevelEmpMap(List<TicketEscaLevelEmpMap> mapEmpPojoList);
	Long isEscaltionLevelCreatedForExt(EmployeeTicketRequestInfo employeeTicketRequestInfo, String deptType);
	public MailConfigurationDtlsDTO getEmployeeEmailDtls(MailConfigurationDtlsInfo email);
	public Integer updateTicketsESCComplaint(TicketEscalationPojo employeeTicketRequestInfo);
	public Integer updateTicketsESCDATE(EmployeeTicketRequestInfo employeeTicketRequestInfo);
	
}
