package com.sumadhura.employeeservice.dto;

import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsResponse;

import lombok.Data;

@Data
public class FinBookingFormLegalCostPdfResponse {
	private FinBookingFormAccountsResponse finBookingFormAccountsResponse;
	private FinBookingFormLegalCostResponse finBookingFormLegalCostResponse;
}
