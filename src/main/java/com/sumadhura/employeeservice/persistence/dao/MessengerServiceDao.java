/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;
import java.util.Map;


import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.InboxSiteDetails;
import com.sumadhura.employeeservice.persistence.dto.MessengerDetailsPojo;
import com.sumadhura.employeeservice.util.Page;

/**
 * ChatBotServiceDao Interface provides ChatBotServiceDao specific services.
 * 
 * @author Venkat_Koniki
 * @since 25.08.2020
 * @time 10:40AM
 */
public interface MessengerServiceDao {

	public abstract List<MessengerDetailsPojo> getMessagesList(MessengerRequest request);
	public abstract List<MessengerDetailsPojo> startNewChat(MessengerRequest request);
	public abstract Long saveMessenger(MessengerRequest request);
	public abstract Long saveMessengerConversation(MessengerRequest request);
	public abstract int[] saveMessengerPersonsInvolved(MessengerRequest request);
	public abstract int[] saveMessengerConversationViewStatus(MessengerRequest request);
	public abstract int updateMessengerLastChattedDate(MessengerRequest request);
	public abstract List<MessengerDetailsPojo> getChatDetails(MessengerRequest request);
	public abstract int updateMessengerViewStatusDetails(MessengerRequest request);
	public abstract List<EmployeeDetailsPojo> getMessengerPersonsInvolvedeDetails(MessengerRequest request);
	public abstract List<MessengerDetailsPojo> getMessengerLvlMasterDeatils(MessengerRequest request);
	public abstract List<MessengerDetailsPojo> getMessengerConversationViewStatusCountDetails(MessengerRequest request);
	public abstract List<MessengerDetailsPojo> getLastMessengerConversationDetails(MessengerRequest request);
	public abstract List<MessengerDetailsPojo> getMessengerConversationViewStatusWrtCustomer(MessengerRequest request);
	public abstract Long saveDocumentLocation(Long messegeConversationId, String location, String type);
	public abstract Page<MessengerDetailsPojo> getMessagesList(MessengerRequest request,int pageNo,int pageSize);
    public abstract List<EmployeeDetailsPojo> getEmployeeDropDown(MessengerRequest request);
	public Integer updateMessengerViewStatusAsUnread(MessengerRequest request);
	public  Map<String, Object> getFlatBookId(MessengerRequest request);
	public List<InboxSiteDetails> getSiteDetailList(MessengerRequest request);
    }
