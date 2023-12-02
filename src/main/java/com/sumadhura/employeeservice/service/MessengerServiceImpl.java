/**
 * 
 */
package com.sumadhura.employeeservice.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumadhura.employeeservice.dto.ExcelObject;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.dto.MessengerResponce;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.persistence.dao.MessengerServiceDao;

import com.sumadhura.employeeservice.persistence.dto.DevicePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.InboxSiteDetails;
import com.sumadhura.employeeservice.persistence.dto.MessengerDetailsPojo;
import com.sumadhura.employeeservice.util.Base64FileUtil;
import com.sumadhura.employeeservice.util.CarouselUtils;
import com.sumadhura.employeeservice.util.IOSPushNotificationUtil;
import com.sumadhura.employeeservice.util.PushNotificationUtil;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * ChatBotServiceImpl provides ChatBot specific services.
 * 
 * @author Venkat_Koniki
 * @since 25.08.2020
 * @time 10:40AM
 */

@Service("MessengerServiceImpl")
public class MessengerServiceImpl implements MessengerService{

	@Resource
	@Qualifier("MessengerServiceDaoImpl") 
	private  MessengerServiceDao messengerServiceDaoImpl;
	
	
	
	
	private static final Logger LOGGER = Logger.getLogger(MessengerServiceImpl.class);
	
	@Autowired
	private ResponceCodesUtil responceCodesUtil;

	@Override
	public MessengerResponce getMessagesList(final MessengerRequest request) {
		LOGGER.info("******* The control inside of the getMessagesList  in  MessengerServiceImpl ******");
		MessengerResponce responce = new MessengerResponce();
		/* if request is coming customer side */
		if(Util.isNotEmptyObject(request.getType()) && request.getType().equalsIgnoreCase("customer")){
			final List<MessengerDetailsPojo> messengerListpojos = new ArrayList<>();
			final List<MessengerDetailsPojo> messengerConversationViewStatusCountDetailspojos = new ArrayList<>();
			final List<MessengerDetailsPojo> lastMessengerConversationDetailsPojos = new ArrayList<>();
			final ConcurrentHashMap<Long,TreeSet<MessengerDetailsPojo>> lastMessengerConversationDetailsMap = new ConcurrentHashMap<>();
			final ConcurrentHashMap<Long,Long> map = new ConcurrentHashMap<>(); 
			final CyclicBarrier barrier = new CyclicBarrier(4);
			final Map<String,List<MessengerDetailsPojo>> departmentwisemessengerDetailsPojosMap = new LinkedHashMap<>();
			List<MessengerDetailsPojo> departmentwisemessengerDetailsPojos = new ArrayList<>();
			/* This thread is responsible to read the data from Messenger */
			Thread messengerListThread = new Thread() {
				public void run() {
					try {
						messengerListpojos.addAll(messengerServiceDaoImpl.getMessagesList(request));
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						LOGGER.error("*** The Barrier is Broken ****"+e);
					}
				}
			};
			messengerListThread.start();
			/* This thread is responsible to read the MessengerConversationViewStatusCountDetails data from MessengerConversationViewStatusCountDetails */
			Thread messengerConversationViewStatusCountDetailsThread = new Thread() {
				public void run() {
					try {
						messengerConversationViewStatusCountDetailspojos.addAll(messengerServiceDaoImpl.getMessengerConversationViewStatusCountDetails(request));
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						LOGGER.error("*** The Barrier is Broken ****"+e);
					}
				}
			};
			messengerConversationViewStatusCountDetailsThread.start();
			/* This thread is responsible to read the LastMessengerConversationDetailsThread data from MessengerConversationViewStatusCountDetails */
			Thread lastMessengerConversationDetailsThread = new Thread() {
				public void run() {
					try {
						lastMessengerConversationDetailsPojos.addAll(messengerServiceDaoImpl.getLastMessengerConversationDetails(request));
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						LOGGER.error("*** The Barrier is Broken ****"+e);
					}
				}
			};
			lastMessengerConversationDetailsThread.start();
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				LOGGER.error("*** The Barrier is Broken ****"+e);
			}
			/* messenger unviewd Messeges count  */
			for(MessengerDetailsPojo pojo : messengerConversationViewStatusCountDetailspojos) {
			   if(Util.isNotEmptyObject(pojo.getMessengerId()) && Util.isNotEmptyObject(pojo.getViewCount())){
				   map.put(pojo.getMessengerId(),pojo.getViewCount());
			   }
			}
			/* To get Last conversation messege of messenger  */
		   for(MessengerDetailsPojo pojo : lastMessengerConversationDetailsPojos) {
			   if(Util.isNotEmptyObject(pojo.getMessengerId()) && Util.isNotEmptyObject(pojo.getMessengerConversationId()) && Util.isNotEmptyObject(pojo.getMessege()) && Util.isNotEmptyObject(pojo.getConversationCreatedDate())) {
				   if(lastMessengerConversationDetailsMap.containsKey(pojo.getMessengerId())){
					   lastMessengerConversationDetailsMap.get(pojo.getMessengerId()).add(pojo);
				   }else {
					   TreeSet<MessengerDetailsPojo> set = new TreeSet<>();
					   set.add(pojo);
					   lastMessengerConversationDetailsMap.put(pojo.getMessengerId(),set);
				   }
			   }
		   }
		   
		   for(MessengerDetailsPojo pojo : messengerListpojos){
			   /* setting LastConversation Messege */
			   pojo.setEmployeeName("CRM-REPRESENTATIVE");
			   pojo.setMessege(lastMessengerConversationDetailsMap.get(pojo.getMessengerId()).last().getMessege());
			   pojo.setLastChattedDate(lastMessengerConversationDetailsMap.get(pojo.getMessengerId()).last().getConversationCreatedDate());
			   pojo.setViewCount(Util.isNotEmptyObject(map.get(pojo.getMessengerId()))?map.get(pojo.getMessengerId()):0l);
			   pojo.setChatMsgWithoutTags(lastMessengerConversationDetailsMap.get(pojo.getMessengerId()).last().getChatMsgWithoutTags());
			   
			   if(departmentwisemessengerDetailsPojosMap.containsKey(pojo.getDepartmentName())){
				   departmentwisemessengerDetailsPojosMap.get(pojo.getDepartmentName()).add(pojo);
			   }else {
				   List<MessengerDetailsPojo> list = new ArrayList<>();
				   list.add(pojo);
				   departmentwisemessengerDetailsPojosMap.put(pojo.getDepartmentName(),list);
			   }
		   }
		   /* un capability of iterating map */
		  // responce.setDepartmentwisemessengerDetailsPojos(departmentwisemessengerDetailsPojosMap);
		  for( String key : departmentwisemessengerDetailsPojosMap.keySet()) {
			  MessengerDetailsPojo departmentwisemessengerDetailsPojo = new MessengerDetailsPojo();
			  List<MessengerDetailsPojo> pojos  = departmentwisemessengerDetailsPojosMap.get(key);
			  departmentwisemessengerDetailsPojo.setDepartmentName(key);
			  departmentwisemessengerDetailsPojo.setMessengerDetailsPojos(pojos);
			  //departmentwisemessengerDetailsPojo.setEmployeeName(Util.isNotEmptyObject(pojos)?Util.isNotEmptyObject(pojos.get(0))?pojos.get(0).getEmployeeName():"N/A":"N/A");
			  departmentwisemessengerDetailsPojos.add(departmentwisemessengerDetailsPojo);
		  }
		  responce.setDepartmentwisemessengerDetailsPojos(departmentwisemessengerDetailsPojos);
		}else {
		List<MessengerDetailsPojo> pojos = messengerServiceDaoImpl.getMessagesList(request);
		responce.setMessengerDetailsPojos(pojos);
		}
		return responce;
	}
	
	@Override
	public Result getUnviewChatCount(@NonNull MessengerRequest request) {
		LOGGER.info("******* The control inside of the getUnviewChatCount  in  MessengerServiceImpl ******");
		List<Long> messengerIds = new ArrayList<>();
		MessengerResponce responce = new MessengerResponce();
		List<MessengerDetailsPojo> messengerConversationViewStatusCountDetailspojos = messengerServiceDaoImpl.getMessengerConversationViewStatusCountDetails(request);
		for(MessengerDetailsPojo pojo : messengerConversationViewStatusCountDetailspojos){
			messengerIds.add(pojo.getMessengerId());
		}
		responce.setMessengerIds(messengerIds);
		responce.setUnviewedChatCount(Integer.valueOf(messengerConversationViewStatusCountDetailspojos.size()).longValue());
		return responce;
	}
	
	@Override
	public Result startNewChat(MessengerRequest request) {
		LOGGER.info("******* The control inside of the startNewChat  in  MessengerServiceImpl ******");
		MessengerResponce responce = new MessengerResponce();
		List<MessengerDetailsPojo> pojos = messengerServiceDaoImpl.startNewChat(request);
		responce.setMessengerDetailsPojos(pojos);
		return responce;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result chatSubmit(final MessengerRequest request) {
		LOGGER.info("******* The control inside of the chatSubmit  in  MessengerServiceImpl ******");
		MessengerResponce result = new MessengerResponce();
		int[] personsInvolvedrecords = null;
		/* setting fileInfo */
		final List<FileInfo> files = request.getFiles();
		
		/* insert into messenger table */
		if(Util.isEmptyObject(request.getFlag()) || Util.isEmptyObject(request.getMessengerId())){
		Long messengerId = messengerServiceDaoImpl.saveMessenger(request);
		request.setMessengerId(Util.isNotEmptyObject(messengerId) ? messengerId : 0l);
		}
		
		final Long messengerConversationId = messengerServiceDaoImpl.saveMessengerConversation(request);
		request.setMessengerConversationId(messengerConversationId);

		/* creating reciepts Map */
		ConcurrentHashMap<String, Map<Long, Long>> reciepientMap = new ConcurrentHashMap<>();
		Map<Long, Long> viewStatusMap = null;

		/* setting flatbooking Id */
		viewStatusMap = new ConcurrentHashMap<>();
		viewStatusMap.put(request.getSendType(), MetadataId.NO.getId());
		reciepientMap.put(request.getSendTo()+"-"+request.getSendType(), viewStatusMap);

		/* sender employee id */
		viewStatusMap = new ConcurrentHashMap<>();
		viewStatusMap.put(request.getCreatedByType(), MetadataId.YES.getId());
		reciepientMap.put(request.getCreatedById()+"-"+request.getCreatedByType(), viewStatusMap);

		/* sending CC Employee Messeges */
		if(request.getCreatedByType().equals(MetadataId.EMPLOYEE.getId())) {
		/* If we didnt remove sender is CC employee view status becomes no*/	
		Set<Long> set = new HashSet<Long>(request.getEmployeeIds());
		set.remove(request.getCreatedById());
		request.setEmployeeIds(new ArrayList<Long>(set));
		}
		
		for(Long empId : request.getEmployeeIds()) {
			viewStatusMap = new ConcurrentHashMap<>();
			viewStatusMap.put(MetadataId.EMPLOYEE.getId(), MetadataId.NO.getId());
			reciepientMap.put(empId+"-"+MetadataId.EMPLOYEE.getId(), viewStatusMap);
		 }
		
		/* setting reciepts Map */
		request.setReciepientMap(reciepientMap);
		
		if(Util.isEmptyObject(request.getFlag()) || Util.isEmptyObject(request.getMessengerId())){
			  personsInvolvedrecords = messengerServiceDaoImpl.saveMessengerPersonsInvolved(request);
		}
		/* Add employeeSiteId and departmentId in Gateway Controller  */
		List<MessengerDetailsPojo> pojos = messengerServiceDaoImpl.getMessengerLvlMasterDeatils(request);
		/* taking all level Employees From the table */
		for (MessengerDetailsPojo pojo : pojos) {
			viewStatusMap = new ConcurrentHashMap<>();
			viewStatusMap.put(MetadataId.EMPLOYEE.getId(), MetadataId.NO.getId());
			reciepientMap.putIfAbsent(pojo.getEmployeeId()+"-"+MetadataId.EMPLOYEE.getId(), viewStatusMap);
		}
		
		int[] viewStatusRecords = messengerServiceDaoImpl.saveMessengerConversationViewStatus(request);
		if(Util.isNotEmptyObject(files) || Util.isNotEmptyObject(request.getGoogleDriveLinks())){
		Thread thread = new Thread() {
			@Override
			public void run() {
				String location = null;
				String type = "";
				if(Util.isNotEmptyObject(files)) {
				  location = storeMessengerFileOrImage(files,messengerConversationId);
				  type = "localserver";
				}else if(Util.isNotEmptyObject(request.getGoogleDriveLinks())){
				  location = StringUtil.join( request.getGoogleDriveLinks(), ",");
				  type = "googledrive";				
				}
				Long pk = messengerServiceDaoImpl.saveDocumentLocation(messengerConversationId,location,type);
				LOGGER.info("*** The noumber of records inserted into the DocumentLocation Table ****"+pk);
				
				/*
				if(Util.isEmptyObject(request.getFlag()) || Util.isEmptyObject(request.getMessengerId())){
				  messengerServiceDaoImpl.updateMessengerLastChattedDate(request);
				}
				/* if the request is coming from customer no need to send push notification. 
				if(!request.getCreatedByType().equals(MetadataId.FLAT_BOOKING.getId())){
				   sendMessengerPushNotification(request);
				} */
			}
		};
		thread.setName("ImageOrDocument Thread");
		thread.start();
		}
		
		Thread threadObj = new Thread() {
			@Override
			public void run() {
				if (Util.isNotEmptyObject(request.getFlag()) || Util.isNotEmptyObject(request.getMessengerId())) {
					messengerServiceDaoImpl.updateMessengerLastChattedDate(request);
				}
				/* if the request is coming from customer no need to send push notification. */
				if (!request.getCreatedByType().equals(MetadataId.FLAT_BOOKING.getId())) {
					sendMessengerPushNotification(request);
				}
			}
		};
		threadObj.setName("pushnotification&Lastmodified Thread");
		threadObj.start();
		LOGGER.info("*** The noumber of records inserted into the MessengerViewStatus Table ****"+Arrays.toString(viewStatusRecords));
		LOGGER.info("*** The noumber of records inserted into the MessengerPersonsInvolved Table ****"+Arrays.toString(personsInvolvedrecords));
		result.setMessengerId(request.getMessengerId());
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@Override
	public Result getChatDetails(@NonNull final MessengerRequest request) {
		LOGGER.info("******* The control inside of the getChatDetails  in  MessengerServiceImpl ******");
		ConcurrentHashMap<Long,Long> customerViewStatusDetailsMap = new ConcurrentHashMap<>();
		MessengerResponce result = new MessengerResponce();
		Set<Long> conversationIds = new HashSet<>();
		Set<Long> empIds = new HashSet<>();
		List<MessengerDetailsPojo> chatDetailsPojos = messengerServiceDaoImpl.getChatDetails(request);
		/* persons Involved */
		List<EmployeeDetailsPojo> employeeDetailsPojos = messengerServiceDaoImpl.getMessengerPersonsInvolvedeDetails(request);
		/* setting empIds */
		for(EmployeeDetailsPojo pojo : employeeDetailsPojos) {
			empIds.add(pojo.getEmployeeId());
		}
		/* adding File Urls */
		for(MessengerDetailsPojo pojo : chatDetailsPojos) {
			String location = Util.isNotEmptyObject(pojo.getLocation())?pojo.getLocation():"N/A";
			String type = Util.isNotEmptyObject(pojo.getType())?pojo.getType():"N/A";
			pojo.setFileList(CarouselUtils.getFileInfosWRTlocation(location,type));
			/* owner messeges */
			if(Util.isNotEmptyObject(pojo.getCreatedByType()) && Util.isNotEmptyObject(pojo.getCreatedById()) /* &&  Util.isNotEmptyObject(pojo.getChatCreatedByType()) &&  Util.isNotEmptyObject(pojo.getChatCreatedBy()) */) {
				if(pojo.getCreatedByType().equals(MetadataId.EMPLOYEE.getId())){
					 if(empIds.contains(pojo.getCreatedById())){
					    pojo.setChatType(MetadataId.CC.getName());
					 }else {
					    pojo.setChatType(MetadataId.MESSEGE_OWNER.getName());
					 }
					 /* Setting chatCreatedByType Employee conversation ids */
					 if(request.getRecipientType().equals(MetadataId.EMPLOYEE.getId())) {
						 conversationIds.add(pojo.getMessengerConversationId());
					 }
				}else if(pojo.getCreatedByType().equals(MetadataId.FLAT_BOOKING.getId())){
					pojo.setChatType(MetadataId.CUSTOMER.getName());
				}
			}
		}
		
		if (request.getRecipientType().equals(MetadataId.EMPLOYEE.getId())) {
			/* setting messege ConversationIds */
			request.setConversationIds(conversationIds);
			List<MessengerDetailsPojo> customerViewStatusDetails = messengerServiceDaoImpl
					.getMessengerConversationViewStatusWrtCustomer(request);
			
			for(MessengerDetailsPojo pojo : customerViewStatusDetails) {
				if(Util.isNotEmptyObject(pojo.getMessengerConversationId()) && Util.isNotEmptyObject(pojo.getViewStatus()))
				customerViewStatusDetailsMap.putIfAbsent(pojo.getMessengerConversationId(),pojo.getViewStatus());
			}
		}
		
		/* setting customerview Status and unread status */
		for(MessengerDetailsPojo pojo : chatDetailsPojos) {
			pojo.setCustomerViewStatus(customerViewStatusDetailsMap.get(pojo.getMessengerConversationId()));
		}
		
		/* checking editable access if he is present in cc or chat Owner then only give edit Access */
		empIds.add(Util.isNotEmptyObject(chatDetailsPojos)?Util.isNotEmptyObject(chatDetailsPojos.get(0))?Util.isNotEmptyObject(chatDetailsPojos.get(0).getChatCreatedBy())?chatDetailsPojos.get(0).getChatCreatedBy():0l:0l:0l);
		if(Util.isNotEmptyObject(request.getRecipientType()) && (request.getRecipientType().equals(MetadataId.EMPLOYEE.getId()) || request.getRecipientType().equals(MetadataId.CUSTOMER.getId()))) {
		     if(empIds.contains(request.getRecipientId())){
		    	 result.setEditable(Boolean.TRUE);  
		     }else {
		    	 result.setEditable(Boolean.FALSE);
		     }
		}
		/* conversation view status updating */
		messengerServiceDaoImpl.updateMessengerViewStatusDetails(request);
		result.setMessengerDetailsPojos(chatDetailsPojos);
		result.setEmployeeDetailsPojos(employeeDetailsPojos);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		/* conversation status updating thread 
		Thread thread = new Thread() {
			public void run() {
				int updatedRecords = messengerServiceDaoImpl.updateMessengerViewStatusDetails(request);
				LOGGER.info("******* The no of records updated in MessengerViewStatusDetals  in  MessengerServiceImpl ******"+updatedRecords);
			}
		};
		thread.start(); */
		return result;
	}
	
	private boolean sendMessengerPushNotification(MessengerRequest request) {
		LOGGER.info("******* The control inside of the chatSubmit  in  MessengerServiceImpl ******");
		ReferedCustomer referedCustomer = new ReferedCustomer();
		Map<String, String> devicePojsMap = new ConcurrentHashMap<>();
		referedCustomer.setFlatBookingId(request.getFlatBookingId());
		List<DevicePojo> devicePojos =null;// referredCustomerDaoImpl.getDeviceDetails(referedCustomer);
		/* inorder to eliminate duplicate Device Tokens we are using HashMap */
		for(DevicePojo devicePojo : devicePojos) {
			if(Util.isNotEmptyObject(devicePojo)  && Util.isNotEmptyObject(devicePojo.getDevicetoken()) 
					&& Util.isNotEmptyObject(devicePojo.getOstype())) {
				devicePojsMap.put(devicePojo.getDevicetoken(), devicePojo.getOstype());
			}
		}
		/* sending push notification to Andriod devices */
		for (String deviceToken : devicePojsMap.keySet()) {
			if (devicePojsMap.get(deviceToken).equalsIgnoreCase("IOS")) {
				IOSPushNotificationUtil iOSPushNotificationUtil = new IOSPushNotificationUtil();
				JSONObject payload = iOSPushNotificationUtil.dataObject("MESSENGER SUB: "+request.getSubject().toUpperCase(), null,
						request.getMessage(), "Messenger", null, null, request.getMessengerId());
				try {
					iOSPushNotificationUtil.sendIosPushNotification(Arrays.asList(deviceToken), payload.toString(),
							false);
				} catch (Exception ex) {
					LOGGER.error("*** The Exception information is ***" + ex);
				}
			}else if (devicePojsMap.get(deviceToken).equalsIgnoreCase("Android")){
				/* sending push notifications to the andriod devices */
				PushNotificationUtil notifyUtilObj = new PushNotificationUtil();
				try {
					 notifyUtilObj.pushFCMNotification(deviceToken,
							 "MESSENGER SUB: "+request.getSubject().toUpperCase(), null,
							 request.getMessage(), "Messenger",null, null,
							 request.getMessengerId());
				} catch (Exception ex) {
					LOGGER.error("*** The Exception information is ***" + ex);
				}
			}
		}
	   return true;
	}
	
	 @Override
	   public MessengerRequest dateWiseSearchFunctionalityUtil(MessengerRequest request) {
		   LOGGER.info("*** The control is inside the dateWiseSearchFunctionalityUtil in MessengerServiceImpl ***");
		   /*  date filter   */
				if(Util.isNotEmptyObject(request.getStartDate()) && Util.isEmptyObject(request.getEndDate())){
					  if(Util.isNotEmptyObject(request.getStartDate())) {
					      request.setStartDate(TimeUtil.removeTimePartFromTimeStamp(request.getStartDate()));
				      }if(Util.isEmptyObject(request.getEndDate())) {
					    // request.setEndDate(TimeUtil.endOfTheDayTimestamp(request.getStartDate()));
				    	  request.setEndDate(TimeUtil.endOfTheDayTimestamp(new Timestamp(System.currentTimeMillis())));
			          }
				 }
				 else if(Util.isEmptyObject(request.getStartDate()) && Util.isNotEmptyObject(request.getEndDate())){
					  if(Util.isEmptyObject(request.getStartDate())) {
					      GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
					      request.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
				      }if(Util.isNotEmptyObject(request.getEndDate())) {
					     request.setEndDate(TimeUtil.endOfTheDayTimestamp(request.getEndDate()));
			          }
				 }
				else if(Util.isEmptyObject(request.getStartDate()) && Util.isEmptyObject(request.getEndDate())){ 
					 if(Util.isEmptyObject(request.getStartDate())) {
						 GregorianCalendar mortgage = new GregorianCalendar(1971, Calendar.JANUARY, 1);
					      request.setStartDate(new Timestamp(mortgage.getTimeInMillis()));
				     }
					 if(Util.isEmptyObject(request.getEndDate())) {
						 request.setEndDate(TimeUtil.endOfTheDayTimestamp(new Timestamp(System.currentTimeMillis())));
					 }
				 }
				else if(Util.isNotEmptyObject(request.getStartDate()) && Util.isNotEmptyObject(request.getEndDate())){
					 if(Util.isNotEmptyObject(request.getStartDate())) {
					      request.setStartDate(TimeUtil.removeTimePartFromTimeStamp(request.getStartDate()));
				     }if(Util.isNotEmptyObject(request.getEndDate())) {
					     request.setEndDate(TimeUtil.endOfTheDayTimestamp(request.getEndDate()));
			         } 
				 }
		   return request; 
	   }
	 
	private String storeMessengerFileOrImage(List<FileInfo> messengerFilesList, Long messengerConversationId) {
		for (FileInfo fileInfo : messengerFilesList) {
			String name = fileInfo.getName();
			fileInfo.setExtension(name.substring(name.lastIndexOf(".") + 1));
		}
		Base64FileUtil base64Image = new Base64FileUtil();
		String imageLoc = Base64FileUtil.messengerDirectoryPath+messengerConversationId;
		base64Image.store(messengerFilesList, imageLoc);
		return imageLoc;
	}

	@Override
	public Result getEmployeeDropDown(MessengerRequest request) {
		 LOGGER.info("*** The control is inside the getEmployeeDropDown in MessengerServiceImpl ***");
		 MessengerResponce result = new MessengerResponce();
		 List<EmployeeDetailsPojo> employeeDetailsPojos = new ArrayList<>();
		 List<EmployeeDetailsPojo>  pojos = messengerServiceDaoImpl.getEmployeeDropDown(request);
		 for(EmployeeDetailsPojo pojo : pojos) {
			 if(Util.isNotEmptyObject(pojo.getEmployeeName()) && !pojo.getEmployeeName().contains("TESTING")){
				 employeeDetailsPojos.add(pojo);
			 }
		 }
		result.setEmployeeDetailsPojos(employeeDetailsPojos);
		return result;
	}
	
	@Override
	public Integer updateMessengerViewStatusAsUnread(MessengerRequest request) {
		LOGGER.info("*** The control is inside the updateMessengerViewStatusAsUnread in MessengerServiceImpl ***");
		return messengerServiceDaoImpl.updateMessengerViewStatusAsUnread(request);
	}

	@Override
	public MessengerRequest createRequestObject(ExcelObject excelObject, MessengerRequest request) {
		LOGGER.info("*** The control is inside the createRequestObject in MessengerServiceImpl ***");
		String staticContent = "";
		try {
			request.setFlatNo(excelObject.getFiled_A());
			// request.setSiteIds(Arrays.asList(Long.parseLong(excelObject.getFiled_K())));
			/* gettig Flat booking Id with Flat No and Site Id. */
			Map<String, Object> map = messengerServiceDaoImpl.getFlatBookId(request);
			if (Util.isNotEmptyObject(map) && map.get("APP_STATUS").toString().equals("Registered")) {
				if (request.getRequestType().equalsIgnoreCase("RegistrationProcess")) {
					request.setFlatBookingId(Long.parseLong(map.get("FLAT_BOOK_ID").toString()));
					request.setSendTo(Long.parseLong(map.get("FLAT_BOOK_ID").toString()));
					/* subject */
					//request.setSubject(map.get("NAME").toString() + "-Registration Details");
					request.setSubject("Registration Details");
					/* send type -flat */
					request.setSendType(MetadataId.FLAT_BOOKING.getId());
					/* getting static content description from properties files */
					staticContent = responceCodesUtil.getMessangerProperties("REGISTRATION_DETAILS_DESCRIPTION");
				}
				// if(request.getRequestType().equalsIgnoreCase("requestMessags")){} // create
				// request Object here for future enhancement
				/* common code for future enhancement */
				/* calling method for replacing with dynamic values */
				String replacedContent = replaceContent(excelObject, staticContent);
				/* calling static method for removing html tags */
				String contentWithoutTags = Util.replaceHtmlTags(replacedContent);
				/* saving values in request Object */
				request.setMessage(replacedContent);
				request.setChatMsgWithoutTags(contentWithoutTags);
				request.setGoogleDriveLinks(Arrays.asList(excelObject.getFiled_J()));
				request.setFlatStatus("notempty");
			} else {
				request.setFlatStatus("empty");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
	}

	/* replacing static content with dynamic values coming from Excel Object */
	public String replaceContent(ExcelObject excelObject, String staticContent) {
		String replecedCotent = staticContent;	
		replecedCotent = replecedCotent.replace("Filed_A", Util.isNotEmptyObject(excelObject.getFiled_A())?excelObject.getFiled_A():"-");
		replecedCotent = replecedCotent.replace("Filed_B", Util.isNotEmptyObject(excelObject.getFiled_B())?excelObject.getFiled_B():"-");
		replecedCotent = replecedCotent.replace("Filed_C",Util.isNotEmptyObject( excelObject.getFiled_C())?excelObject.getFiled_C():"-");
		replecedCotent = replecedCotent.replace("Filed_D", Util.isNotEmptyObject(excelObject.getFiled_D())?excelObject.getFiled_D():"-");
		replecedCotent = replecedCotent.replace("Filed_E", Util.isNotEmptyObject(excelObject.getFiled_E())?excelObject.getFiled_E():"-");
		replecedCotent = replecedCotent.replace("Filed_F", Util.isNotEmptyObject(excelObject.getFiled_F())?excelObject.getFiled_F():"-");
		replecedCotent = replecedCotent.replace("Filed_G", Util.isNotEmptyObject(excelObject.getFiled_G())?excelObject.getFiled_G():"-");
		replecedCotent = replecedCotent.replace("Filed_H",Util.isNotEmptyObject( excelObject.getFiled_H())?excelObject.getFiled_H():"-");
		replecedCotent = replecedCotent.replace("Filed_I", Util.isNotEmptyObject(excelObject.getFiled_I())?excelObject.getFiled_I():"-");
		replecedCotent = replecedCotent.replace("Filed_J",Util.isNotEmptyObject( excelObject.getFiled_J())?excelObject.getFiled_J():"-");
		replecedCotent = replecedCotent.replace("Filed_K",Util.isNotEmptyObject( excelObject.getFiled_K())?excelObject.getFiled_K():"-");
		replecedCotent = replecedCotent.replace("Filed_L", Util.isNotEmptyObject(excelObject.getFiled_L())?excelObject.getFiled_L():"-");
		replecedCotent = replecedCotent.replace("Filed_M",Util.isNotEmptyObject( excelObject.getFiled_M())?excelObject.getFiled_M():"-");
		replecedCotent = replecedCotent.replace("Filed_N", Util.isNotEmptyObject(excelObject.getFiled_N())?excelObject.getFiled_N():"-");
		replecedCotent = replecedCotent.replace("Filed_O",Util.isNotEmptyObject( excelObject.getFiled_O())?excelObject.getFiled_O():"-");
		replecedCotent = replecedCotent.replace("Filed_P", Util.isNotEmptyObject(excelObject.getFiled_P())?excelObject.getFiled_P():"-");
		replecedCotent = replecedCotent.replace("Filed_Q", Util.isNotEmptyObject(excelObject.getFiled_Q())?excelObject.getFiled_Q():"-");
		replecedCotent = replecedCotent.replace("Filed_R",Util.isNotEmptyObject( excelObject.getFiled_R())?excelObject.getFiled_R():"-");
		replecedCotent = replecedCotent.replace("Filed_S",Util.isNotEmptyObject( excelObject.getFiled_S())?excelObject.getFiled_S():"-");
		replecedCotent = replecedCotent.replace("Filed_T", Util.isNotEmptyObject(excelObject.getFiled_T())?excelObject.getFiled_T():"-");
		replecedCotent = replecedCotent.replace("Filed_U", Util.isNotEmptyObject(excelObject.getFiled_U())?excelObject.getFiled_U():"-");
		replecedCotent = replecedCotent.replace("Filed_V", Util.isNotEmptyObject(excelObject.getFiled_V())?excelObject.getFiled_V():"-");
		replecedCotent = replecedCotent.replace("Filed_W",Util.isNotEmptyObject( excelObject.getFiled_W())?excelObject.getFiled_W():"-");
		return replecedCotent;
	}
	
	@Override
	public List<InboxSiteDetails> getSiteList(MessengerRequest request) {
		LOGGER.info("*** The control is inside the getSiteList in MessengerServiceImpl ***");
		List<InboxSiteDetails>  siteData=messengerServiceDaoImpl.getSiteDetailList(request);
		return siteData;
	}

}
