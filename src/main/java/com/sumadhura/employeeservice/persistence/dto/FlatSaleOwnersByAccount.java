package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;

import lombok.Data;

@Data
@Entity
public class FlatSaleOwnersByAccount {
	

	@Column(name = "FLATS_SALE_OWNERS_ID") 
	private Long flatSaleOwnerIdBasedOnAccountId;
	@Column(name = "FLAT_SALE_OWNER") 
	private String flatSaleOwnerNameBasedOnAccountId;
	
	@Column(name = "FIN_SITE_PROJ_ACC_MAP_ID") 
	private Long finSiteProjAccMapId;
//	@Column(name = "FLATS_SALE_OWNERS_ID") 
//	private Long finFlatSaleOwnerId;

}
