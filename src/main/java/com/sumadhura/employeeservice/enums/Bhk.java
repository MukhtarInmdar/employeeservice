/**
 * 
 */
package com.sumadhura.employeeservice.enums;

/**
 * @author Venkat
 * @Date 10-03-2020
 */
public enum Bhk {
	
	BHK_0(0L, "0 BHK","0"),
	BHK_1(1L, "1 BHK","1"),
	BHK_2(2l, "2 BHK","2"),
	BHK_2_5(3l,"2.5 BHK","2.5"),
	BHK_3(4l,"3 BHK","3"),
	BHK_4(5l,"4 BHK","4"),
	BHK_3_5(6l,"3.5 BHK","3.5"),
	BHK_5(7l,"5 BHK","5")
	;

	private Bhk(Long id, String name,String alias) {
		this.id = id;
		this.name = name;
		this.alias = alias; 
	}

	private Long id;
	private String name;
	private String alias;
	
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}