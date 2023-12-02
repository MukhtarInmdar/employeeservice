/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ProjectUpdate class provides  ProjectUpdates specific Properties.
 * 
 * @author Venkat_Koniki
 * @since 17.10.2019
 * @time 05:35PM
 */

@Getter
@Setter
@ToString
public class ProjectUpdate extends Result {
	private static final long serialVersionUID = -7917921385555990835L;
	private Long siteId;

}
