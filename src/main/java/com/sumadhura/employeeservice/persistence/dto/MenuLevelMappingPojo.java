/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;


import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * MenuLevelMappingPojo class provides MENU_LEVEL_MAPPING Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 16.09.2019
 * @time 07:04PM
 */

@Data
@Entity
public class MenuLevelMappingPojo {

	@Column(name = "MENU_LEVEL_MAP_ID")
	private Long menuLevelMappingId;
	@Column(name = "APPROVE_TO")
	private Long approveTo;
	@Column(name = "LEVEL_ID")
	private Long levelId;
	@Column(name = "MENU_ID")
	private Long menuId;
	
}
