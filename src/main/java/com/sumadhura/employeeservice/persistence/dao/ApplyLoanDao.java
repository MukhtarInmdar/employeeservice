package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;

import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.ApplayLoanDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.ApplyLoanLeadDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.BankerList;
import com.sumadhura.employeeservice.persistence.dto.CustomerDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;

public interface ApplyLoanDao {

	List<BankerList> getBankEamilOnBooking(CustomerInfo s);

	List<EmployeeDetailsPojo> getEmployeeDetailsForMail(CustomerDetailsPojo customerDetailsPojo);

	List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(CustomerInfo customerInfo, Status status);

	Long saveApplyLoanDetailsOnBooking(CustomerInfo customerInfo);

	List<ApplyLoanLeadDetailsPojo> getLoanAppliedLeadDetails(LoanRequest loanRequest);

	List<ApplyLoanLeadDetailsPojo> loadLoanAppliedLeadDetails(LoanRequest loanRequest);

	List<ApplayLoanDocumentsPojo> loadLeadAttachedDocuments(ApplyLoanLeadDetailsPojo applyLoanLeadDetailsPojo);

	int updateLeadSeenStatus(LoanRequest loanRequest);

	int updateApplyLoanLeadDetails(LoanRequest loanRequest);

}
