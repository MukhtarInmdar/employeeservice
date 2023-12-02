package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FloorRequest extends Result {

	private static final long serialVersionUID = 6306218221361932820L;
	
	private Long siteId;
	private List<Long> blockDetIds;
	
}
