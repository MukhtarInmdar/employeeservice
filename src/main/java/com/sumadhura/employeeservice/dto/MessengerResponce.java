package com.sumadhura.employeeservice.dto;

import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.MessengerDetailsPojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessengerResponce extends Result{
	private static final long serialVersionUID = 6422530136257475029L;
	private List<MessengerDetailsPojo> messengerDetailsPojos; 
	private List<EmployeeDetailsPojo> employeeDetailsPojos;
	private List<MessengerDetailsPojo> departmentwisemessengerDetailsPojos;
	private boolean isEditable;
	private Long unviewedChatCount;
	private List<Long> messengerIds;
	private Long messengerId;
}

