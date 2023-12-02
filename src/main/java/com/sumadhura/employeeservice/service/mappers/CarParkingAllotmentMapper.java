package com.sumadhura.employeeservice.service.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.CarParkingAllotmentRequest;
import com.sumadhura.employeeservice.dto.CarParkingAllotmentSlotResponse;
import com.sumadhura.employeeservice.dto.CarParkingBasementSlotsResponse;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.CarParkingAllotmentPdfPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingAllotmentPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingAllotmentSlotPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingApprovalLevelsPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingBasementSlotsPojo;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentInfo;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentPdfInfo;

/**
 * CarParkingAllotmentMapper is responsible for providing customer Car Parking Allotment related bean mappings
 * @author Malladi Venkatesh
 * @since 2021-04-14
 * @time 16:35
 *
 */

@Component("CarParkingAllotmentMapper")
public class CarParkingAllotmentMapper {
	
	private final  Logger LOGGER = Logger.getLogger(CarParkingAllotmentMapper.class);

	public CarParkingAllotmentInfo carParkingAllotmentRequestToInfo(CarParkingAllotmentRequest carParkingAllotmentRequest) {
		LOGGER.info("*** The control is inside of the carParkingAllotmentRequestToInfo in CarParkingAllotmentMapper ***");
		CarParkingAllotmentInfo carParkingAllotmentInfo = new CarParkingAllotmentInfo();
		BeanUtils.copyProperties(carParkingAllotmentRequest, carParkingAllotmentInfo);
		return carParkingAllotmentInfo;
	}

	public List<CarParkingBasementSlotsResponse> carParkingBasementSlotsPojoListToResponse(List<CarParkingBasementSlotsPojo> carParkingBasementSlotsPojoList) {
		LOGGER.info("*** The control is inside of the carParkingBasementSlotsPojoListToResponse in CarParkingAllotmentMapper ***");
		List<CarParkingBasementSlotsResponse> carParkingBasementSlotsResponseList = new ArrayList<>();
		for(CarParkingBasementSlotsPojo carParkingBasementSlotsPojo : carParkingBasementSlotsPojoList) {
			CarParkingBasementSlotsResponse carParkingBasementSlotsResponse = new CarParkingBasementSlotsResponse();
			BeanUtils.copyProperties(carParkingBasementSlotsPojo, carParkingBasementSlotsResponse);
			carParkingBasementSlotsResponseList.add(carParkingBasementSlotsResponse);
		}
		return carParkingBasementSlotsResponseList;
	}

	public CarParkingAllotmentPojo carParkingAllotmentInfoToPojo(CarParkingAllotmentInfo carParkingAllotmentInfo) {
		LOGGER.info("*** The control is inside of the carParkingAllotmentInfoToPojo in CarParkingAllotmentMapper ***");
		CarParkingAllotmentPojo carParkingAllotmentPojo = new CarParkingAllotmentPojo();
		BeanUtils.copyProperties(carParkingAllotmentInfo, carParkingAllotmentPojo);
		carParkingAllotmentPojo.setCreatedBy(carParkingAllotmentInfo.getEmployeeId());
		carParkingAllotmentPojo.setStatusId(Status.PENDING_FOR_APPROVAL.status);
		return carParkingAllotmentPojo;
	}

	public CarParkingAllotmentPdfInfo carParkingAllotmentPdfPojoListToInfo(CarParkingAllotmentPdfPojo carParkingAllotmentPdfPojo) {
		LOGGER.info("*** The control is inside of the carParkingAllotmentPdfPojoListToInfo in CarParkingAllotmentMapper ***");
		CarParkingAllotmentPdfInfo carParkingAllotmentPdfInfo = new CarParkingAllotmentPdfInfo();
		BeanUtils.copyProperties(carParkingAllotmentPdfPojo,carParkingAllotmentPdfInfo);
		return carParkingAllotmentPdfInfo;
	}

	public List<CarParkingAllotmentSlotResponse> carParkingAllotmentSlotPojoListToResponseList(List<CarParkingAllotmentSlotPojo> carParkingAllotmentSlotPojoList) {
		LOGGER.info("*** The control is inside of the carParkingAllotmentSlotPojoListToResponseList in CarParkingAllotmentMapper ***");
		List<CarParkingAllotmentSlotResponse> carParkingAllotmentSlotResponseList = new ArrayList<>();
		for(CarParkingAllotmentSlotPojo carParkingAllotmentSlotPojo : carParkingAllotmentSlotPojoList) {
			CarParkingAllotmentSlotResponse carParkingAllotmentSlotResponse = new CarParkingAllotmentSlotResponse();
			BeanUtils.copyProperties(carParkingAllotmentSlotPojo, carParkingAllotmentSlotResponse);
			carParkingAllotmentSlotResponseList.add(carParkingAllotmentSlotResponse);
		}
		return carParkingAllotmentSlotResponseList;
	}

	public CarParkingApprovalPojo carParkingAllotmentInfoToApprovalPojo(CarParkingAllotmentInfo allotmentInfo, CarParkingApprovalLevelsPojo approvalLevelsPojo) {
		LOGGER.info("*** The control is inside of the carParkingAllotmentInfoToApprovalPojo in CarParkingAllotmentMapper ***");
		CarParkingApprovalPojo approvalPojo  = new CarParkingApprovalPojo();
		approvalPojo.setAllotmentId(allotmentInfo.getAllotmentId());
		approvalPojo.setCpAprLevId(approvalLevelsPojo.getCpAprLevId());
		approvalPojo.setCreatedBy(allotmentInfo.getEmployeeId());
		approvalPojo.setStatusId(Status.INCOMPLETE.status);
		return approvalPojo;
	}

	public CarParkingApprovalPojo getApprovalPojoForUpdate(CarParkingAllotmentInfo carParkingAllotmentInfo, Status status) {
		LOGGER.info("*** The control is inside of the getApprovalPojoForUpdate in CarParkingAllotmentMapper ***");
		CarParkingApprovalPojo approvalPojo  = new CarParkingApprovalPojo();
		approvalPojo.setCpAprLevId(carParkingAllotmentInfo.getCpAprLevId());
		approvalPojo.setAllotmentId(carParkingAllotmentInfo.getAllotmentId());
		approvalPojo.setComments(carParkingAllotmentInfo.getComments());
		approvalPojo.setModifiedBy(carParkingAllotmentInfo.getEmployeeId());
		approvalPojo.setStatusId(status.status);
		return approvalPojo;
	}

}
