package com.sumadhura.employeeservice.service.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.BlockDetailsResponse;
import com.sumadhura.employeeservice.dto.FloorDetailsResponse;
import com.sumadhura.employeeservice.persistence.dto.BlockDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorDetailsPojo;

/**
 * BlockMapper is responsible for providing project,block,floor,flat related bean mappings
 * @author Malladi Venkatesh
 * @since 2021-09-07
 * @time 12:19
 *
 */

@Component("BlockMapper")
public class BlockMapper {
	
	private  static final Logger LOGGER = Logger.getLogger(BlockMapper.class);
	
	public List<BlockDetailsResponse> blockDetPojoListToBlockDetRespList(List<BlockDetailsPojo> blockDetPojoList) {
		LOGGER.info("*** The control is inside of the blockDetPojoListToBlockDetRespList in BlockMapper ***");
		List<BlockDetailsResponse> blockDetRespList = new ArrayList<>();
		for(BlockDetailsPojo blockDetPojo : blockDetPojoList) {
			BlockDetailsResponse blockDetResp = new BlockDetailsResponse();
			BeanUtils.copyProperties(blockDetPojo, blockDetResp);
			blockDetRespList.add(blockDetResp);
		}
		return blockDetRespList;
	}

	public FloorDetailsResponse floorDetPojoToFloorDetResp(FloorDetailsPojo floorDetPojo) {
		LOGGER.info("*** The control is inside of the floorDetPojoToFloorDetResp in BlockMapper ***");
		FloorDetailsResponse floorDetResp = new FloorDetailsResponse();
		BeanUtils.copyProperties(floorDetPojo, floorDetResp);
		return floorDetResp;
	}
}
