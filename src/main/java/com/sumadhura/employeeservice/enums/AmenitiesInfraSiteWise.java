package com.sumadhura.employeeservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AmenitiesInfraSiteWise {


    NAN_CLUB_HOUSE(1l,1l,1l),                                   
    NAN_CAR_PARKING(2l,2l,2l),	                                  
    NAN_FLOOR_RISE(3l,3l,1l),	                                  
    NAN_INFRA_CHARGES_ELECTRICITY_STP_WTP_PIPED_GAS_DG(4l,4l,1l),
    NAN_PLC(5l,5l,1l),                          	              
    EG_CLUB_HOUSE(6l,1l,1l),	                                  
    EG_CAR_PARKING(7l,2l,1l),	                                  
    EG_FLOOR_RISE(8l,3l,1l),	                                  
    EG_ELECTRICITY_STP_RO(9l,6l,1l),	                          
    HOR_CLUB_HOUSE(10l,1l,1l),	                                  
    HOR_CAR_PARKING(11l,2l,1l),	                                  
    HOR_FLOOR_RISE(12l,3l,1l),	                                  
    HOR_PLC_CHARGES(13l,8l,1l),                      	          
    HOR_INFRA_CHARGES(14l,7l,1l),
    ACR_CAR_PARKING_WATER_POWER_DGPIPED_GAS_CLUB_HOUSE(15l,9l,102l),
    ACR_FLOOR_RISE(16l,3l,102l),
    ACR_PLC__EAST_FACING(17l,10l,102l),
    ACR_GRAND_TOTAL(18l,11l,102l);
//end






	@Getter
	private Long id;
	@Getter
	private Long amenitiesInfraId;
	@Getter
	private Long siteId;

}
