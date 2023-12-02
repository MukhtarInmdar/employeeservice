package com.sumadhura.employeeservice.service.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.AppointmentRequest;
import com.sumadhura.employeeservice.dto.ApptmtBookingsDetailResponse;
import com.sumadhura.employeeservice.dto.ApptmtSlotTimesDetailResponse;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.ApptmtBookingsDetailPojo;
import com.sumadhura.employeeservice.persistence.dto.ApptmtSlotDatesPojo;
import com.sumadhura.employeeservice.persistence.dto.ApptmtSlotEmpProjectWisePojo;
import com.sumadhura.employeeservice.persistence.dto.ApptmtSlotTimesDetailPojo;
import com.sumadhura.employeeservice.persistence.dto.ApptmtSlotTimesPojo;
import com.sumadhura.employeeservice.service.dto.AppointmentInfo;

/**
 * AppointmentBookingMapper is responsible for providing customer raised Appointment Booking related bean mappings
 * @author Malladi Venkatesh
 * @since 2021-04-04
 * @time 16:35
 *
 */

@Component("AppointmentBookingMapper")
public class AppointmentBookingMapper {

	private final  Logger LOGGER = Logger.getLogger(AppointmentBookingMapper.class);

	public AppointmentInfo appointmentRequestToAppointmentInfo(AppointmentRequest appointmentRequest) {
		LOGGER.info("*** The control is inside of the appointmentRequestToAppointmentInfo in AppointmentBookingMapper ***");
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		BeanUtils.copyProperties(appointmentRequest, appointmentInfo);
		return appointmentInfo;
	}

	public List<ApptmtBookingsDetailResponse> apptmtBookingsDetailPojoListToApptmtBookingsDetailResponseList(List<ApptmtBookingsDetailPojo> apptmtBookingsDetailPojoList) {
		LOGGER.info("*** The control is inside of the apptmtBookingsDetailPojoListToApptmtBookingsDetailResponseList in AppointmentBookingMapper ***");
		List<ApptmtBookingsDetailResponse> apptmtBookingsDetailResponseList = new ArrayList<>();
		for(ApptmtBookingsDetailPojo apptmtBookingsDetailPojo : apptmtBookingsDetailPojoList) {
			ApptmtBookingsDetailResponse apptmtBookingsDetailResponse = new ApptmtBookingsDetailResponse();
			BeanUtils.copyProperties(apptmtBookingsDetailPojo, apptmtBookingsDetailResponse);
			apptmtBookingsDetailResponseList.add(apptmtBookingsDetailResponse);
		}
		return apptmtBookingsDetailResponseList;
	}

	public ApptmtSlotDatesPojo appointmentInfoToApptmtSlotDatesPojo(AppointmentInfo appointmentInfo, MetadataId metadataId) {
		LOGGER.info("*** The control is inside of the appointmentInfoToApptmtSlotDatesPojo in AppointmentBookingMapper ***");
		ApptmtSlotDatesPojo apptmtSlotDatesPojo = new ApptmtSlotDatesPojo();
		apptmtSlotDatesPojo.setApptmtDate(appointmentInfo.getApptmtDate());
		apptmtSlotDatesPojo.setType(metadataId.getId());
		
		/* For Employee */
		if(MetadataId.EMPLOYEE.getId().equals(metadataId.getId())) {
			apptmtSlotDatesPojo.setTypeId(appointmentInfo.getEmployeeId());
		}
		apptmtSlotDatesPojo.setCreatedBy(appointmentInfo.getEmployeeId());
		apptmtSlotDatesPojo.setStatusId(Status.ACTIVE.status);
		return apptmtSlotDatesPojo;
	}

	public ApptmtSlotTimesPojo appointmentInfoToApptmtSlotTimesPojo(AppointmentInfo appointmentInfo) {
		LOGGER.info("*** The control is inside of the appointmentInfoToApptmtSlotTimesPojo in AppointmentBookingMapper ***");
		ApptmtSlotTimesPojo apptmtSlotTimesPojo = new ApptmtSlotTimesPojo();
		apptmtSlotTimesPojo.setApptmtSlotDatesId(appointmentInfo.getApptmtSlotDatesId());;
		apptmtSlotTimesPojo.setStartTime(appointmentInfo.getStartDate());
		apptmtSlotTimesPojo.setEndTime(appointmentInfo.getEndDate());
		apptmtSlotTimesPojo.setCreatedBy(appointmentInfo.getEmployeeId());
		apptmtSlotTimesPojo.setStatusId(Status.ACTIVE.status);
		apptmtSlotTimesPojo.setSlotStatusId(Status.ACTIVE.status);
		return apptmtSlotTimesPojo;
	}

	public ApptmtSlotEmpProjectWisePojo appointmentInfoToApptmtSlotEmpProjectWisePojo(AppointmentInfo appointmentInfo) {
		LOGGER.info("*** The control is inside of the appointmentInfoToApptmtSlotEmpProjectWisePojo in AppointmentBookingMapper ***");
		ApptmtSlotEmpProjectWisePojo apptmtSlotEmpProjectWisePojo = new ApptmtSlotEmpProjectWisePojo();
		apptmtSlotEmpProjectWisePojo.setApptmtSlotTimesId(appointmentInfo.getApptmtSlotTimesId());
		apptmtSlotEmpProjectWisePojo.setSiteId(appointmentInfo.getSiteId());
		apptmtSlotEmpProjectWisePojo.setCreatedBy(appointmentInfo.getEmployeeId());
		apptmtSlotEmpProjectWisePojo.setStatusId(Status.ACTIVE.status);
		apptmtSlotEmpProjectWisePojo.setType(appointmentInfo.getType());
		apptmtSlotEmpProjectWisePojo.setTypeIds(appointmentInfo.getTypeIds());
		return apptmtSlotEmpProjectWisePojo;
	}

	public List<ApptmtSlotTimesDetailResponse> apptmtSlotTimesDetailPojoListToResponse(List<ApptmtSlotTimesDetailPojo> apptmtSlotTimesDetailPojoList) {
		LOGGER.info("*** The control is inside of the apptmtSlotTimesDetailPojoListToResponse in AppointmentBookingMapper ***");
		List<ApptmtSlotTimesDetailResponse> apptmtSlotTimesDetailResponseList = new ArrayList<>();
		for(ApptmtSlotTimesDetailPojo apptmtSlotTimesDetailPojo : apptmtSlotTimesDetailPojoList) {
			ApptmtSlotTimesDetailResponse apptmtSlotTimesDetailResponse = new ApptmtSlotTimesDetailResponse();
			BeanUtils.copyProperties(apptmtSlotTimesDetailPojo, apptmtSlotTimesDetailResponse);
			apptmtSlotTimesDetailResponseList.add(apptmtSlotTimesDetailResponse);
		}
		return apptmtSlotTimesDetailResponseList;
	}
}
