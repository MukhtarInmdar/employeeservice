/**
 * 
 */
package com.sumadhura.employeeservice.service;

import java.util.List;

import com.sumadhura.employeeservice.dto.ExcelObject;
import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.dto.MessengerResponce;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.persistence.dto.InboxSiteDetails;

/**
 * ChatBotService Interface provides ChatBot specific services.
 * 
 * @author Venkat_Koniki
 * @since 25.08.2020
 * @time 10:40AM
 */
public interface MessengerService {
	public abstract MessengerResponce getMessagesList(MessengerRequest request);
	public abstract MessengerRequest dateWiseSearchFunctionalityUtil(MessengerRequest request);
	public abstract Result startNewChat(MessengerRequest request);
	public abstract Result chatSubmit(MessengerRequest request);
	public abstract Result getChatDetails(MessengerRequest request);
	public abstract Result getUnviewChatCount(MessengerRequest request);
	public abstract Result getEmployeeDropDown(MessengerRequest request);
	public Integer updateMessengerViewStatusAsUnread(MessengerRequest request);
	public MessengerRequest createRequestObject(ExcelObject excelObject,MessengerRequest request);
	public List<InboxSiteDetails> getSiteList(MessengerRequest request);
	
}
