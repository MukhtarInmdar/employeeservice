/**
 * 
 */
package com.sumadhura.employeeservice.service.mappers;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.ProjectUpdate;
import com.sumadhura.employeeservice.service.dto.ProjectUpdateInfo;

import lombok.NonNull;

/**
 * PropjectUpdatesMapper class provides  ProjectUpdates mapping Service.
 * 
 * @author Venkat_Koniki
 * @since 17.10.2019
 * @time 05:35PM
 */

@Component("PropjectUpdatesMapper")
public class PropjectUpdatesMapper {
	
	private final  Logger LOGGER = Logger.getLogger(PropjectUpdatesMapper.class);
	
	public ProjectUpdateInfo ProjectUpdateToProjectUpdateInfo(@NonNull ProjectUpdate projectUpdate) {
		LOGGER.info("*** The control is inside the ProjectUpdateToProjectUpdateInfo in PropjectUpdatesMapper ***");
		ProjectUpdateInfo  projectUpdateInfo = new ProjectUpdateInfo();
		BeanUtils.copyProperties(projectUpdate, projectUpdateInfo);
		return projectUpdateInfo;
	}

}
