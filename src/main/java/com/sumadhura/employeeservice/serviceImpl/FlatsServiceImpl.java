package com.sumadhura.employeeservice.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.BlockDetailsResponse;
import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.FlatDetailsResponse;
import com.sumadhura.employeeservice.dto.FlatRequest;
import com.sumadhura.employeeservice.dto.FlatResponse;
import com.sumadhura.employeeservice.dto.FloorDetailsResponse;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dao.FlatDao;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.service.FlatService;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;
import com.sumadhura.employeeservice.util.Util;

@Service
public class FlatsServiceImpl implements FlatService {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FlatsServiceImpl.class);

	@Autowired(required = true)
	private FlatDao flatDao;

	@Override
	public List<DropDownPojo> getFlatsNames(DropDownRequest dropDownRequest) {
		List<DropDownPojo> flatNamesList= flatDao.getFlatsNames(dropDownRequest);  
		return flatNamesList;
	}

	@Override
	public List<DropDownPojo> getFlatsNamesBySite(DropDownRequest dropDownRequest) {
		List<DropDownPojo> flatNamesList= flatDao.getFlatsNamesBySite(dropDownRequest);  
		return flatNamesList;
	}

	@Override
	public List<DropDownPojo> getFlatsNamesByBlock(DropDownRequest dropDownRequest) {
		List<DropDownPojo> flatNamesList= flatDao.getFlatsNamesByBlock(dropDownRequest);  
		return flatNamesList;

	}

	@Override
	public List<NotificationDetails> getFlatDetailList(Set<Long> set) {
		List<NotificationDetails> flatData=flatDao.getFlatDetailList(set);
		return flatData;
	}

	@Override
	public List<NotificationDetails> getFlatDetailListByDetId(Set<Long> set) {
		List<NotificationDetails> flatData=flatDao.getFlatDetailListByDetId(set);
		return flatData;
	}

	@Override
	public List<DropDownPojo> getFlatsNamesBySbuaSeries(DropDownRequest dropDownRequest) {
		LOGGER.info("**** The control is inside the getFlatsNamesBySbuaSeries in FlatsServiceImpl"+dropDownRequest);
		return flatDao.getFlatsNamesBySbuaSeries(dropDownRequest);  
	}

	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerDetailsByFlatId(Customer customer) {
		LOGGER.info("**** The control is inside the getCustomerDetailsByFlatId in FlatsServiceImpl"+customer);
		return flatDao.getCustomerDetailsByFlatId(customer);
	}

	@Override
	public FlatResponse getFlats(FlatRequest flatRequest) {
		LOGGER.info("**** The control is inside the getFlats in FlatsServiceImpl"+flatRequest);
		FlatResponse flatResp = new FlatResponse();
		List<FlatPojo> flatPojoList = flatDao.getFlats(flatRequest);
		Map<Long, Map<Long, List<FlatPojo>>> blockMap = new LinkedHashMap<>();
		
		/* Seperating flat details block and floor wise */
		for(FlatPojo flatPojo : flatPojoList) {
			if(Util.isNotEmptyObject(flatPojo) && Util.isNotEmptyObject(flatPojo.getFloorDetId()) 
				&& Util.isNotEmptyObject(flatPojo.getBlockDetId())) {
				Map<Long, List<FlatPojo>> floorMap = new LinkedHashMap<>();
				List<FlatPojo> flatPojos = new ArrayList<>();
				if(blockMap.containsKey(flatPojo.getBlockDetId())) {
					floorMap = blockMap.get(flatPojo.getBlockDetId());
					if(floorMap.containsKey(flatPojo.getFloorDetId())) {
						flatPojos = floorMap.get(flatPojo.getFloorDetId());
					}
				}
				flatPojos.add(flatPojo);
				floorMap.put(flatPojo.getFloorDetId(), flatPojos);
				blockMap.put(flatPojo.getBlockDetId(), floorMap);
			}
		}
		
		/* Setting all flat details by block wise and floor wise */
		List<BlockDetailsResponse> blockDetRespList = new ArrayList<>();
		for(Entry<Long, Map<Long, List<FlatPojo>>> blockMapEntrySet : blockMap.entrySet()) {
			List<FloorDetailsResponse> floorDetRespList = new ArrayList<>();
			BlockDetailsResponse blockDetResp = new BlockDetailsResponse();
			for(Entry<Long, List<FlatPojo>> floorMapEntrySet : blockMapEntrySet.getValue().entrySet()) {
				List<FlatDetailsResponse> flatDetRespList = new ArrayList<>();
				FloorDetailsResponse floorDetResp = new FloorDetailsResponse();
				for(FlatPojo flatPojo : floorMapEntrySet.getValue()) {
					FlatDetailsResponse flatDetResp = new FlatDetailsResponse();
					blockDetResp.setBlockDetId(flatPojo.getBlockDetId());
					blockDetResp.setBlockId(flatPojo.getBlockId());
					blockDetResp.setBlockName(flatPojo.getBlockName());
					floorDetResp.setFloorDetId(flatPojo.getFloorDetId());
					floorDetResp.setFloorId(flatPojo.getFloorId());
					floorDetResp.setFloorName(flatPojo.getFloorName());
					flatDetResp.setFlatId(flatPojo.getFlatId());
					flatDetResp.setFlatNo(flatPojo.getFlatNo());
					flatDetRespList.add(flatDetResp);
				}
				floorDetResp.setFlatDetRespList(flatDetRespList);
				floorDetRespList.add(floorDetResp);
			}
			blockDetResp.setFloorDetRespList(floorDetRespList);
			blockDetRespList.add(blockDetResp);
		}
		flatResp.setBlockDetRespList(blockDetRespList);
		return flatResp;
	}

	@Override
	public FlatResponse getBookingFlats(FlatRequest flatRequest) {
		LOGGER.info("**** The control is inside the getBookingFlats in FlatsServiceImpl"+flatRequest);
		FlatResponse flatResp = new FlatResponse();
		List<FlatBookingPojo> flatBookingPojoList = flatDao.getBookingFlats(flatRequest);
		flatResp.setFlatBookingPojoList(flatBookingPojoList);
		return flatResp;
	}

}
