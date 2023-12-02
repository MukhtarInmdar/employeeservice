package com.sumadhura.employeeservice.enums;

public enum KYCDocuments {

	
//	ApplCheque(1L,"Application money Cheque"),
//	DemdDraft(2L,"Demand Draft"),
//	payOrder(3L,"pay Order"),
//	PanNumbera(4L,"Pan Number and Copy of pancard"),
//	Undertaking(5L,"Undertaking"),
//	PrfofResdRatcard(6L,"Proof of Residence Rationcard"),
//	ElectricBill(7L,"Electric Bill"),
//	PhonBill(8L,"Phone Bill"),
//	DrivingLicence(9L,"Driving Licence"),
//	voteridentitycard(10L,"voteridentity card"),
//	AadhaarCard(11L,"Aadhaar Card"),
//	passport(12L,"passport"),
//	Applicationcheque(13L,"Application number and applicant name  behind all documents and cheque"),
//	CoprateapplicantMoaandPoa(14L,"Coprate applicant Moa and Poa"),
//	Partnershipfirm(15L,"Partnership firm :partner to sign application documents and cheque"),
//	Photograph(16L,"Photograph")
	Cheque_DD_PO(1L,"Application money Cheque / Demand Draft / Pay Order."),
	PAN_UT(2L,"PAN No. and Copy of PAN Card / Undertaking."),
	Residence_Proof(3L,"Proof of Residence (Ration Card / Electricity Bill / Phone Bill / Driving Licence / Voter Identity Card / Aadhaar Card)"),
	Passport(4L,"If the Applicant and/ or Co-Applicant is an NRI, kindly attach a true copy of the Applicant's valid Passport."),
	Unit_NameOfApplciant(5L,"Please mention applicantion number/ Unit number and name of applicant behide the cheque and all supporting documents."),
	MOA(6L,"If the Applicant is a Corporate entity then the copy of Memorandum Of Association (MOA). Board Resolution, Power of Attorney of the authorised signatory."),
	Partnership(7L,"For Partnership Firm: Partnership Deed along with authority in favour of Partner to sign application / documents/ cheque."),
	Photograph(8L,"Current Photograph of Applicant/s"),
	Aadhaar(9L,"Aadhaar Card")
	;

	private KYCDocuments(Long id, String name) {
	this.id = id;
	this.name = name;
	}
	private Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	/**
	* @return the id
	*/

	/**
	* @return the name
	*/
}
