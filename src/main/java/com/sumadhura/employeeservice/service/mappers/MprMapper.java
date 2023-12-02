package com.sumadhura.employeeservice.service.mappers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.MprDocumentsResponse;
import com.sumadhura.employeeservice.dto.MprRequest;
import com.sumadhura.employeeservice.dto.MprResponse;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.MprDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.MprPojo;
import com.sumadhura.employeeservice.service.dto.MprInfo;
import com.sumadhura.employeeservice.util.Util;

/**
 * @author Malladi Venkatesh
 * @since 2020-11-18
 * @time 13:23
 */
@Component(value="MprMapper")
public class MprMapper {
	private static final Logger LOGGER = Logger.getLogger(MprMapper.class);
	
	public MprInfo getMprInfoFromMprRequest(MprRequest mprRequest) {
		LOGGER.info("**** The control is inside the getMprInfoFromMprRequest in MprMapper*****");
		MprInfo mprInfo = new MprInfo();
		BeanUtils.copyProperties(mprRequest, mprInfo);
		return mprInfo;
	}

	public MprPojo getMprPojoFromMPrInfo(MprInfo mprInfo) {
		LOGGER.info("**** The control is inside the getMprPojoFromMPrInfo in MprMapper*****");
		MprPojo mprPojo = new MprPojo();
		BeanUtils.copyProperties(mprInfo, mprPojo);
		mprPojo.setType(MetadataId.SITE.getId());
		mprPojo.setTypeId(mprInfo.getSiteId());
		mprPojo.setCreatedBy(mprInfo.getEmployeeId());
		mprPojo.setStatusId(Status.ACTIVE.getStatus());
		return mprPojo;
	}

	public MprDocumentsPojo getExternalDriveLocationDetails(MprInfo mprInfo) {
		LOGGER.info("**** The control is inside the getExternalDriveLocationDetails in MprMapper*****");
		MprDocumentsPojo mprDocumentsPojo = new MprDocumentsPojo();
		mprDocumentsPojo.setMprId(mprInfo.getMprId());
		mprDocumentsPojo.setLocationType("ExternalServer");
		mprDocumentsPojo.setDocumentLocation(mprInfo.getExternalDriveLocation());
		mprDocumentsPojo.setCreatedBy(mprInfo.getEmployeeId());
		mprDocumentsPojo.setStatusId(Status.ACTIVE.getStatus());
		return mprDocumentsPojo;
	}

	public List<MprDocumentsResponse> getMprDocumentsResponseListFromMprDocumentsPojoList(List<MprDocumentsPojo> mprDocumentsPojoList) {
		LOGGER.info("**** The control is inside the getMprDocumentsResponseListFromMprDocumentsPojoList in MprMapper*****");
		List<MprDocumentsResponse> mprDocumentsResponseList = new ArrayList<>();
		for(MprDocumentsPojo mprDocumentsPojo : mprDocumentsPojoList) {
			MprDocumentsResponse mprDocumentsResponse = new MprDocumentsResponse();
			BeanUtils.copyProperties(mprDocumentsPojo, mprDocumentsResponse);
			mprDocumentsResponseList.add(mprDocumentsResponse);
		}
		return mprDocumentsResponseList;
	}

	public MprResponse getMprResponseFromMprPojo(MprPojo mprPojo, List<MprDocumentsResponse> mprDocumentsResponseList) {
		LOGGER.info("**** The control is inside the getMprResponseFromMprPojo in MprMapper*****");
		MprResponse mprResponse = new MprResponse();
		BeanUtils.copyProperties(mprPojo, mprResponse);
		if(Util.isNotEmptyObject(mprDocumentsResponseList) && Util.isNotEmptyObject(mprDocumentsResponseList.get(0))) {
			mprResponse.setMprDocumentsResponseList(mprDocumentsResponseList);			
		}
		return mprResponse;
	}

	public List<MprDocumentsResponse> getMprDocumentsResponseListFromMprPojoList(List<MprPojo> mprAllDetailsPojoList) {
		LOGGER.info("**** The control is inside the getMprDocumentsResponseListFromMprPojoList in MprMapper*****");
		List<MprDocumentsResponse> mprDocumentsResponseList = new ArrayList<>();
		for(MprPojo mprPojo : mprAllDetailsPojoList) {
			MprDocumentsResponse mprDocumentsResponse = new MprDocumentsResponse();
			BeanUtils.copyProperties(mprPojo, mprDocumentsResponse);
			mprDocumentsResponseList.add(mprDocumentsResponse);
		}
		return mprDocumentsResponseList;
	}

	public Set<MprResponse> getMprResponseListFromMprAllDetailsPojoList(List<MprPojo> mprAllDetailsPojoList,LinkedHashMap<Long, List<MprDocumentsResponse>> mprDocumentsResponseMap) {
		LOGGER.info("**** The control is inside the getMprResponseListFromMprAllDetailsPojoList in MprMapper*****");
		Set<MprResponse> mprResponseSet = new LinkedHashSet<>();
		for(MprPojo mprPojo : mprAllDetailsPojoList) {
			if(Util.isNotEmptyObject(mprPojo) && Util.isNotEmptyObject(mprPojo.getMprId())) {
				MprResponse mprResponse = new MprResponse();
				BeanUtils.copyProperties(mprPojo, mprResponse);
				/*setting mpr viewed or not*/
				mprResponse.setIsViewed(mprPojo.getIsViewed()==null?"NotViewed":"Viewed");
				/* setting MPR Documents Response List */
				if(mprDocumentsResponseMap.containsKey(mprPojo.getMprId())) {
					mprResponse.setMprDocumentsResponseList(mprDocumentsResponseMap.get(mprPojo.getMprId()));
				}
				mprResponseSet.add(mprResponse);
			}
		}
		return mprResponseSet;
	}

}
