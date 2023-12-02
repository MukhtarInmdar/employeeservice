package com.sumadhura.employeeservice.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
public class SiteLevelNotifyResponseDTO extends SiteLevelNotifyRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6899127449843450218L;

	private Set<String> siteNames=new HashSet<>();
	
	private Set<String> blockNames=new HashSet<>();
	
	private Set<String> floorNames=new HashSet<>();
	
	private Set<String> flatNamess=new HashSet<>();
	
	
	
	
}
