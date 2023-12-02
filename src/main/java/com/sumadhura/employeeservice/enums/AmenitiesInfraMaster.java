/**
 * 
 */
package com.sumadhura.employeeservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Venkat
 * @Date 10-03-2020
 */

@AllArgsConstructor
public enum AmenitiesInfraMaster {

	CLUB_HOUSE(1l,"CLUB HOUSE"),
	CAR_PARKING(2l,"CAR PARKING"),
	FLOOR_RISE(3l,"FLOOR RISE"),
	INFRA_CHARGES_ELECTRICITY_STP_WTP_PIPED_GAS_DG(4l,"INFRA CHARGES-ELECTRICITY/STP/WTP/PIPED GAS/DG"),
	PLC(1l,"PLC"),
	ELECTRICITY_STP_RO(5l,"ELECTRICITY/STP/RO"),
	INFRA_CHARGES(6l,"INFRA CHARGES"),
	PLC_CHARGES(7l,"PLC CHARGES"),
    CAR_PARKING_WATER_POWER_DGPIPED_GAS_CLUB_HOUSE(9l,"CAR PARKING, WATER, POWER, DG/PIPED GAS, CLUB HOUSE"),
    PLC__EAST_FACING(10l,"PLC - EAST FACING"),
    GRAND_TOTAL(11l,"GRAND TOTAL");
   //end


	@Getter
	private Long id;
	@Getter
	private String name;
	
	
}
