package com.sumadhura.employeeservice.serviceImpl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.BlockDetailsResponse;
import com.sumadhura.employeeservice.dto.BlockRequest;
import com.sumadhura.employeeservice.dto.BlockResponse;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dao.BlockDao;
import com.sumadhura.employeeservice.persistence.dto.BlockDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.BlockService;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;
import com.sumadhura.employeeservice.service.mappers.BlockMapper;

@Service
public class BlockServiceImpl implements BlockService {
	
	private static final Logger LOGGER = Logger.getLogger(BlockServiceImpl.class);

	@Autowired(required = true)
	private BlockDao blockDao;
	
	@Resource(name="BlockMapper")
	private BlockMapper blockMapper;

	@Override
	public List<DropDownPojo> getBlockNames(DropDownRequest siteList) {
		LOGGER.debug("Inside BlockServiceImpl");
		List<DropDownPojo> employeeLogInPojoLISTS= blockDao.getBlockNames(siteList);
		return employeeLogInPojoLISTS;
	}

	@Override
	public List<NotificationDetails> getBlockDetailList(Set<Long> set) {
		List<NotificationDetails> blockDetails=blockDao.getBlockDetailList(set);
		return blockDetails;
	}

	@Override
	public BlockResponse getBlocks(BlockRequest blockReq) {
		LOGGER.info("*** The control is inside of the getBlocks in BlockServiceImpl ***");
		BlockResponse blockResponse = new BlockResponse();
		List<BlockDetailsPojo> blockDetPojoList = blockDao.getBlockDetails(blockReq);
		List<BlockDetailsResponse> blockDetRespList = blockMapper.blockDetPojoListToBlockDetRespList(blockDetPojoList);
		blockResponse.setBlockDetRespList(blockDetRespList);
		return blockResponse;
	}

}
