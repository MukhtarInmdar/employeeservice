package com.sumadhura.employeeservice.service;

import java.util.List;

import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.exception.DefaultBankerException;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.persistence.dto.ApplyLoanLeadDetailsPojo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;

public interface ApplyLaonService {

	void sendMailToDefaultBankarOnBooking(CustomerInfo customerInfo) throws DefaultBankerException;

	List<ApplyLoanLeadDetailsPojo> getLoanAppliedLeadDetails(LoanRequest loanRequest);

	List<ApplyLoanLeadDetailsPojo> loadLoanAppliedLeadDetailsById(LoanRequest loanRequest);

	Result updateLeadSeenStatus(LoanRequest loanRequest);

	Result updateApplyLoanLeadDetails(LoanRequest loanRequest) throws EmployeeFinancialServiceException;

}
