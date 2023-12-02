package com.sumadhura.employeeservice.service.helpers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.util.PdfHelper;

@Component("applyLoanHelper")
public class ApplyLoanHelper {

	private static final Logger log = Logger.getLogger(ApplyLoanHelper.class);

	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
	@Autowired(required = true)
	@Qualifier("PdfHelper")
	private PdfHelper pdfHelper;

	
}
