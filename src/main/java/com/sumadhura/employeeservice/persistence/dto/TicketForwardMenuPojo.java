/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketForwardMenuPojo class provides  CRMEmployee TicketForwardMenu specific properties.
 * 
 * @author Venkat_Koniki
 * @since 10.05.2019
 * @time 12:19AM
 */
@Entity
@Data
public class TicketForwardMenuPojo {

	@Column(name="MENU_ID")
	private Long menuId;
	@Column(name="TYPE_OF")
	private Long typeOf;
	@Column(name="GENERIC_ID")
	private Long genericId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;
	
	
}
