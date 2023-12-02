/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * CustomerPropertyDetailsPojo class provides CUSTOMER Property specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 12:18AM
 */

@Entity
@Setter
@Getter
public class NOCDetailsPojo {
	@Column(name = "NOC_CHECLIST_ID") private Long nocCheckListId;
	@Column(name = "CHARGE_NAME") private String chargesName;
	
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "IS_VALIDATION_REQUIRED") private Long isValidationRequired;
	@Column(name = "SITE_ID") private Long siteId;
	
	@Column(name ="MATA_DATA_TYPE_ID") private Long mataDataTypeId;
	@Column(name ="AMOUNT")	private Double amount;
	@Column(name ="AMT_FOR_YEARS")	private Long amtForYears;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	
	
	
	
	/*@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;*/
}
