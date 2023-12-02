package com.sumadhura.employeeservice.service;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.BlockRequest;
import com.sumadhura.employeeservice.dto.BlockResponse;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;




public interface BlockService {
	
	public List<DropDownPojo> getBlockNames(DropDownRequest siteList);

	public List<NotificationDetails> getBlockDetailList(Set<Long> set);

	public BlockResponse getBlocks(BlockRequest blockReq);

}
