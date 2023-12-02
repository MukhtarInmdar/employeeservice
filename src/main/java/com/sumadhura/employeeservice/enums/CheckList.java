package com.sumadhura.employeeservice.enums;

public enum CheckList {

	/*
	BookingDate(1L,"Booking Date"),
	AmenitiesFacing(2L,"Amenities Facing"),
	ProjectName(3L,"Project Name"),
	TotalCost(4L,"Total Cost"),
	ContactDetails(5L,"Contact Details"),
	FlatNumber(6L,"Flat Number"),
	AreaOfFlat(7L,"Area of Flat"),
	DOBApplicant(8L,"DOB Applicant"),
	BaseRate(9L,"Base Rate"),
	AnniversaryDate(10L,"Anniversary Date"),
	FloorRise(11L,"Floor Rise"),
	Pancard(14L,"Pancard"),
	DOBCoApplicant(15L,"DOB Co-Applicant"),
	BookingForm(16L,"Booking Form"),
	ApplicantName(17L,"Applicant Name"),
	FlatNo(18L,"Flat No"),
	Age(19L,"Age"),
	Block_Wing(20L,"Block/Wing"),
	Relation(21L,"Relation"),
	SBUA(22L,"SBUA"),
	Address(23L,"Address"),
	CarpetArea_UDS(24L,"Carpet Area, UDS"),
	AgreementCost(25L,"Agreement Cost"),
	CostBreakUp(26L,"Cost Break Up"),
	TripartiteAgreement(27L,"Tripartite Agreement"),
	OwnFunds(28L,"Own Funds"),
	FlatAdvance_RECO(29L,"Flat Advance(RECO)"),
	ExtraworksReceiptsare(31L,"Extra works Receipts are"),
	TDSChallanPayments(32L,"TDS Challan Payments"),
	FloorNo(33L,"Floor No"),
	Applicant_CoApplicantsNames(35L,"Applicant & Co applicants names"),
	Project_FlatNumber(34L,"Project/ Flat Number"),
	Floor_SBUA_UDS(36L,"Floor, SBUA, UDS"),
	VERIFIED_ALL(38L,"VERIFIED ALL THE ABOVE PARAMETERS WITH SALE DEED."), */
	
	/*--------generic checklistinfo---------*/
	CheckList_Sales_Head(39L, "Checklist Sales Head"),
	CheckList_Crm(40L, "Checklist Crm"),
	CheckList_Legal(41L, "Checklist Legal"),
	CheckList_Registration(42L, "Checklist Registration"),
	/*--------------updated checklists------------*/
	/* SalesHead checkList */
	Bkg_dt_Project_Flat_no_Area_verification(43L,"Bkg dt,Project,Flat no,Area verification"),
	Sqft_Rates_Amenities_Cost_verification(44L,"Sqft Rates,Amenities & Cost verification"),
	All_applicants_KYC_Details_verification(45L,"All applicants KYC,Details verification"),
	/* CrmSales checkList */
	//Bkg_dt_Project_Flat_no_Area_Verification(46L,"Bkg dt,Project,Flat no,Area Verification"),
	//All_applicants_KYC_Details_Verification(47L,"All applicants KYC,Details verification"),
	Total_Cost_Verification(48L,"Total Cost Verification"),
	/* Legal_Officer checklist */
	Booking_form(49L,"Booking form"),
	Applicant1_Age_Address_Name_Relation(50L,"Applicant1 - Age,Address,Name,Relation"),
	Applicant2_Age_Address_Name_Relation(51L,"Applicant2 - Age,Address,Name,Relation"),
	Project_Flat_No_Block_Wing_Floor_No(52L,"Project, Flat No, Block/Wing, Floor No"),
	SBUA_Carpet_Area_UDS(53L,"SBUA, Carpet Area, UDS"),
	Agreement_cost_cost_break_up(54L,"Agreement cost, cost break up"),
	Tripartite_Agreement(55L,"Tripartite Agreement"),
	Own_Funds(56L,"Own Funds"),
	/* Registration checklist */
	Project_Flat_Number_Verified(57L,"Project/Flat Number - Verified"),
	Flat_Advance_RECO(58L,"Flat Advance(RECO)"),
	Applicant_CoApplicants_Names(59L,"Applicant CoApplicants Name-Verification"),
	Extraworks_Receiptsare(60L,"Extra works Receipts are -Verified"),
	Floor_SBUA__UDS(61L,"Floor,SBUA,UDS-Verification"),
	TDSChallan_Payments(62L,"TDS Challan Payments -Verification"),
	Agreement_Cost_verification(63L,"Agreement Cost -Verification"),
	;

	private CheckList(Long id, String name) {
	this.id = id;
	this.name = name;
	}
	private Long id;
	private String name;
	/**
	* @return the id
	*/

	/**
	* @return the name
	*/
	
	public String getName() {
		return name;
	}


	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
