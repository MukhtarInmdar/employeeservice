package com.sumadhura.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BlockRequest extends Result{

	private static final long serialVersionUID = 5175397356357588318L;
	
	private Long siteId;

}
