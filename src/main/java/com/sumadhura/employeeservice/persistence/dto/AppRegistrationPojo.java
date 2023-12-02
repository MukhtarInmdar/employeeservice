/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * AppRegistrationPojo class provides AppRegistration Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 21.05.2019
 * @time 10:07PM
 */

@Entity
@Data
public class AppRegistrationPojo {

	@Column(name="APP_REG_ID")
	private Long appRegId;
	@Column(name="EMP_ID")
	private Long empId;
	@Column(name="CUST_ID")
	private Long customerId;
	@Column(name="DEVICE_ID")
	private Long deviceId;
	@Column(name="USER_NAME")
	private String username;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="LAST_LOGIN_TIME")
	private Timestamp lastLoginTime;
	@Column(name="REGISTER_DATE")
	private Timestamp registerDate;
	@Column(name="STATUS_ID")
	private Long statusId;

}







