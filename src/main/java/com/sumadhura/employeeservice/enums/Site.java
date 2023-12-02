/**
 * 
 */
package com.sumadhura.employeeservice.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* 
* Department enum provides different department codes.
* 
* @author Venkat_Koniki
* @since 03.01.2020
* @time 07:50PM
*/

@AllArgsConstructor
public enum Site {

	ACROPOLIS(102l,"Sumadhura Acropolis","Acropolis","ACR"),
	EDENGARDEN(107l,"Sumadhura Eden Garden","Eden Garden","EG"),
	NANDANAM(111l,"Sumadhura Nandanam","Nandanam","NAN"),
	HORIZON(114l,"Sumadhura Horizon","Sumadhura Horizon","HOR"),
	SUSHANTHAM(124l, "Sumadhura Sushantham","Sumadhura Sushantham","SST"),
	ASPIRE_AURUM(131l, "Aspire Aurum","Aspire Aurum","ASPA"),
	THE_OLYMPUS(134l, "The Olympus","The Olympus", "OLY"),
	SUSHANTHAM_PHASE_2(130l, "Sumadhura Sushantham Phase 2","Sumadhura Sushantham Phase 2","SST2"),
	FOLIUM(126l,"Folium by Sumadhura Phase 1","Folium by Sumadhura Phase 1","FOL"),
	ASPIRE_AMBER(139l,"Aspire Amber","Aspire Amber","AA"),
	GARDENS_BY_THE_BROOK(133l,"Sumadhura's Gardens by the Brook","Sumadhura's Gardens by the Brook","GBB"),
	;
	
	@Getter
	private Long id;
	@Getter
	private String name;
	
	@Getter
	private String salesForceName;
	
	@Getter
	private String shortName;
	
	private static Map<Long, String> siteIdNameMap = new LinkedHashMap<>();
	
	static {
		for(Site site : Site.values()) {
			siteIdNameMap.put(site.id, site.name);
		}
	}
	
	public static String getSiteNameById(Long siteId) {
		return siteIdNameMap.get(siteId);
	}
	
}

