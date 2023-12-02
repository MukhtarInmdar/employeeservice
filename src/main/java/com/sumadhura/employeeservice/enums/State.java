package com.sumadhura.employeeservice.enums;

public enum State {

	Telangana(1L,"Telangana"),
	Karnataka(2L,"Karnataka"),
	NewDelhI(3L,"New Delhi"),
	Bihar(4L,"Bihar"),
	Punjab(5L,"Punjab"),
	Andaman_and_Nicobar_Islands(6L,"Andaman and Nicobar Islands (UT)"),
	Andhra_Pradesh (7L,"Andhra Pradesh"),
	Arunachal_Pradesh(8L,"Arunachal Pradesh"),
	Assam(9L,"Assam"),
	Chandigarh(11L,"Chandigarh (UT)"), 
	Chhattisgarh(12L,"Chhattisgarh"),
	Dadra_and_Nagar_Haveli(13L,"Dadra and Nagar Haveli (UT)"),
	Daman_and_Diu(14L,"Daman and Diu (UT)"),
	Delhi(15L,"Delhi"),
	Goa(16L,"Goa"),
	Gujarat(17L,"Gujarat"),
	Haryana(18L,"Haryana"),
	Himachal_Pradesh(19L,"Himachal Pradesh"),
	Jammu_and_Kashmir (20L,"Jammu and Kashmir"),
	Nagaland(20L,"Nagaland"),
	Jharkhand(21L,"Jharkhand"),
	Kerala(22L,"Kerala"),
	Lakshadweep(23L,"Lakshadweep (UT)"),
	Madhya_Pradesh(24L,"Madhya Pradesh"),
	Maharashtra(25L,"Maharashtra"),
	Manipur(26L,"Manipur"),
	Meghalaya(27L,"Meghalaya"),
	Mizoram(28L,"Mizoram"),
	Odisha(31L,"Odisha"),
	Puducherry(32L,"Puducherry (UT)"), 
	Rajasthan(33L,"Rajasthan"),
	Sikkim(34L,"Sikkim"),
	Tamil_Nadu(35L,"Tamil Nadu"),
	Tripura(36L,"Tripura"),
	Uttar_Pradesh (37L,"Uttar Pradesh"),
	Uttarakhand(38L,"Uttarakhand"),
	West_Bengal(39L,"West Bengal") 
	;

	private State(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	private Long id;
	private String name;


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
