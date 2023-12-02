package com.sumadhura.employeeservice.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.BlockDetailsResponse;
import com.sumadhura.employeeservice.dto.FloorDetailsResponse;
import com.sumadhura.employeeservice.dto.FloorRequest;
import com.sumadhura.employeeservice.dto.FloorResponse;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dao.FloorDao;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorDetailsPojo;
import com.sumadhura.employeeservice.service.FloorService;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;
import com.sumadhura.employeeservice.service.mappers.BlockMapper;
import com.sumadhura.employeeservice.util.Util;

@Service
public class FloorServiceImpl implements FloorService {
	
	private static final Logger LOGGER = Logger.getLogger(FloorServiceImpl.class);
	
	@Autowired
	private FloorDao floorDao;
	
	@Resource(name="BlockMapper")
	private BlockMapper blockMapper;
	
	@Override
	public List<DropDownPojo> getFloorNames(DropDownRequest blockList) {
		List<DropDownPojo> floorNames = floorDao.getFloorNames(blockList);
		return floorNames;
	}

	@Override
	public List<DropDownPojo> getFloorNamesBySite(DropDownRequest siteList) {
		List<DropDownPojo> floorNamesBySite = floorDao.getFloorNamesBySite(siteList);
		return floorNamesBySite;
	}

	@Override
	public List<NotificationDetails> getFloorDetailList(Set<Long> set) {
		List<NotificationDetails> floorDetail=floorDao.getFloorDetailList(set);
		return floorDetail;
	}

	@Override
	public FloorResponse getFloors(FloorRequest floorReq) {
		LOGGER.info("*** The control is inside of the getFloors in FloorServiceImpl ***");
		FloorResponse floorResp = new FloorResponse();
		List<BlockDetailsResponse> blockDetRespList = new ArrayList<>();
		Map<Long, List<FloorDetailsPojo>> floorDetMap = new LinkedHashMap<>();
		List<FloorDetailsPojo> floorDetPojoList = floorDao.getFloorDetails(floorReq);
		/* Seperating Floor Details by Block Det Id */
		for(FloorDetailsPojo floorDetPojo : floorDetPojoList) {
			if(Util.isNotEmptyObject(floorDetPojo) && Util.isNotEmptyObject(floorDetPojo.getBlockDetId())) {
				List<FloorDetailsPojo> floorDetailsPojoList = new ArrayList<>();
				if(floorDetMap.containsKey(floorDetPojo.getBlockDetId())) {
					floorDetailsPojoList = floorDetMap.get(floorDetPojo.getBlockDetId());
				}
				floorDetailsPojoList.add(floorDetPojo);
				floorDetMap.put(floorDetPojo.getBlockDetId(), floorDetailsPojoList);
			}
		}
		
		/* Setting all floor details by block wise */
		for(Entry<Long, List<FloorDetailsPojo>> floorDetEntrySet : floorDetMap.entrySet()) {
			BlockDetailsResponse blockDetResp = new BlockDetailsResponse();
			List<FloorDetailsResponse> floorDetRespList = new ArrayList<>();
			for(FloorDetailsPojo floorDetPojo : floorDetEntrySet.getValue()) {
				FloorDetailsResponse floorDetResp = new FloorDetailsResponse();
				blockDetResp.setBlockDetId(floorDetPojo.getBlockDetId());
				blockDetResp.setBlockId(floorDetPojo.getBlockId());
				blockDetResp.setBlockName(floorDetPojo.getBlockName());
				floorDetResp.setFloorDetId(floorDetPojo.getFloorDetId());
				floorDetResp.setFloorId(floorDetPojo.getFloorId());
				floorDetResp.setFloorName(floorDetPojo.getFloorName());
				floorDetRespList.add(floorDetResp);
			}
			blockDetResp.setFloorDetRespList(floorDetRespList);
			blockDetRespList.add(blockDetResp);
		}
		floorResp.setBlockDetRes(blockDetRespList);
		return floorResp;
	}
	

}
