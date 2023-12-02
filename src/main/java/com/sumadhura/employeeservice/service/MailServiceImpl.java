package com.sumadhura.employeeservice.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.LoanRequest;
import com.sumadhura.employeeservice.dto.MailConfigurationDtls;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.dto.SiteLevelNotifyRequestDTO;
import com.sumadhura.employeeservice.dto.TicketReportingResponce;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequestInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.util.CurrencyUtil;
import com.sumadhura.employeeservice.util.EmailService;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * MailServiceImpl class provides Implementation for Mail specific services.
 * 
 * @author Venkat_Koniki
 * @since 20.04.2019
 * @time 05:41PM
 */


@SuppressWarnings("deprecation")
@Service("mailService")
public class MailServiceImpl implements MailService{
	
	@Autowired
	@Qualifier("mailsenderObj")
	private JavaMailSender mailSender;
	
	@Autowired
	@Qualifier("employeemailsenderObj")
	private JavaMailSender employeeMailSender;
	
	/*  sales@sumadhuragroup.com  */
	@Autowired
	@Qualifier("customerMailsenderObj")
	private JavaMailSender customerMailSender;

	/*  events@sumadhuragroup.com  */
	@Autowired
	@Qualifier("EmployeeMailsenderObj")
	private JavaMailSender EmployeeMailSender;
	
	/*  financial@sumadhuragroup.com  */
	@Autowired
	@Qualifier("financialMailSenderObject")
	private JavaMailSender financialMailSender;
	
/*	@Autowired
	@Qualifier("financialMailSenderObject")
	private JavaMailSender financialMailSender1;
*/
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Value("${ERROR_MAIL}")
	private String ERROR_MAIL;
	
	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;
	
	@Autowired(required = true)
	private EmailService emailService;
	
	@Autowired(required = true)
	private ResponceCodesUtil responceCodesUtil;

	private final static Logger logger = Logger.getLogger(MailServiceImpl.class);
	
	@Override
	public JavaMailSender sendEmailFromEmployeeEmail(Long empId) {
		MailConfigurationDtls mailConfigurationDtls = new MailConfigurationDtls();
		mailConfigurationDtls.setEmpId(empId);
		//based on employee id , loading employee email and encrypted email password
		JavaMailSender mail = emailService.getEmployeeMailSenderObject(mailConfigurationDtls);
		return mail;
	}
	
	public static Map<Long,JavaMailSender> mailSenderList = new HashMap<>(); 
	public String sendMailFromEmployeeEmail(Email email, EmployeeDetailsPojo fromEmployeeDetailsPojo, List<EmployeeDetailsPojo> cRM_detailsMailListForErrorMail) {
		if(Util.isNotEmptyObject(email.getToMails())) {
			logger.info("**** sendMailFromEmployeeEmail getToMails "+Arrays.asList(email.getToMails()));	
		}
		if(Util.isNotEmptyObject(email.getCcs())) {
			logger.info("**** sendMailFromEmployeeEmail getCcs "+Arrays.asList(email.getCcs()));	
		}
		MimeMessagePreparator preparator = getMessagePreparator(email);
		JavaMailSender mailOfCRM = null;
		try {
			//mailOfCRM = sendEmailFromEmployeeEmail(fromEmployeeDetailsPojo.getEmployeeId());
			
			if(mailSenderList.containsKey(fromEmployeeDetailsPojo.getEmployeeId())) {
				mailOfCRM = mailSenderList.get(fromEmployeeDetailsPojo.getEmployeeId());
			} else {
				synchronized (this) {
					if(mailSenderList.containsKey(fromEmployeeDetailsPojo.getEmployeeId())) {
						mailOfCRM = mailSenderList.get(fromEmployeeDetailsPojo.getEmployeeId());
					} else {
						//sending email from crm employee id, for default banker
						mailOfCRM = sendEmailFromEmployeeEmail(fromEmployeeDetailsPojo.getEmployeeId());
						mailSenderList.put(fromEmployeeDetailsPojo.getEmployeeId(), mailOfCRM);
					}
				}
			}
			
			mailOfCRM.send(preparator);
			logger.info("**** Mail sent successfully to employee ****"+email.getToMail()+" "+email.getToMails());
			return "success";//success
		} catch (Exception ex) {// | MailException ex
			//if exception came then remove this object from Map, may be the password has been updated
			mailSenderList.remove(fromEmployeeDetailsPojo.getEmployeeId());
			Properties prop = responceCodesUtil.getApplicationProperties();
			
			String internalEmpMail = prop.getProperty("DEFAULT_BANKER_ERROR_INTERNAL_EMP_EMAIL");//taking due days from prop file
			String errorMsg = prop.getProperty("DEFAULT_BANKER_ERROR_MSG");//taking due days from prop file
			email.setSubject("Failed to send new lead details to banker");
			List<String> emailsList = new ArrayList<>();
			if(Util.isNotEmptyObject(cRM_detailsMailListForErrorMail)) {
				
				if(Util.isNotEmptyObject(cRM_detailsMailListForErrorMail)) {
					for (EmployeeDetailsPojo employeeDetailsPojo1 : cRM_detailsMailListForErrorMail) {
						if(Util.isNotEmptyObject(employeeDetailsPojo1.getEmail())) {
							if(!emailsList.contains(employeeDetailsPojo1.getEmail())) {
								emailsList.add(employeeDetailsPojo1.getEmail());
							}
						}
					}
				}
			}
			
			email.setToMails(emailsList.toArray(new String[] {}));
			
			errorMsg = errorMsg.replace("#error_Msg", "Email authentication error from CRM email, Failed to send mail to default banker,<br> Project "+email.getSiteName()+" Flat No "+email.getFlatNo());
			
			email.setEmailBodyText(errorMsg);
			
			if(Util.isNotEmptyObject(internalEmpMail)) {
				email.setCcs(internalEmpMail.split(","));//cc in internal team, for default banker error
			}

			try {
				//new preparator for error email
				//preparator = getMessagePreparator(email);
				if(Util.isNotEmptyObject(email.getToMails())) {
					sendDefaultBankerErrorMail(email);
				}
			} catch (MailException exx) {
				logger.info("exx.getMessage() - " + exx.getMessage());
			}
			logger.info("ex.getMessage() - "+ex.getMessage());
			return "Failure";
		}
	}
	
	@Override
	public void sendEmail(Email email) {
		logger.info("***** The control is inside the sendMail method in MailserviceImpl ******");	
		MimeMessagePreparator preparator = getMessagePreparator(email);
		
		try {
            mailSender.send(preparator);
            System.out.println("Message has been sent.............................");
            logger.info("**** Mail sent successfully to ****"+email.getToMail());
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
            logger.info("**** Error while sending mail to "+email.getToMail()+" ****"+ex.getMessage());
        }
	}
	
	public void sendEmployeeEmail(Email email) {
		logger.info("***** The control is inside the sendMail method in MailserviceImpl ******");
		MimeMessagePreparator preparator = getMessagePreparator(email);

		try {
			EmployeeMailSender.send(preparator);
			System.out.println("Message has been sent.............................");
			logger.info("**** Mail sent successfully to ****"+email.getToMails());
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
			logger.info("**** Error while sending mail to "+email.getToMail()+" ****"+ex.getMessage());
		}
	}

	public void sendCustomerEmail(Email email) {
		logger.info("***** The control is inside the sendMail method in MailserviceImpl ******");
		MimeMessagePreparator preparator = getMessagePreparator(email);

		try {
			customerMailSender.send(preparator);
			System.out.println("Message has been sent.............................");
			logger.info("**** Mail sent successfully to ****"+email.getToMail());
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
			logger.info("**** Error while sending mail to "+email.getToMail()+" ****"+ex.getMessage());
		}
	}
	
	private MimeMessagePreparator getMessagePreparator(final Email email){
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
            	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
 
                helper.setSubject(email.getSubject());
                
//             	if(Util.isNotEmptyObject(email.getToMail()))
//               	helper.setTo(email.getToMail());
             	
             	if (Util.isNotEmptyObject(email.getToMail())) {
					helper.setTo(email.getToMail());
				} else if (Util.isNotEmptyObject(email.getToMails())) {
					helper.setTo(email.getToMails());
				}
               
               	/*  Adding cc to mail  */
               	if(Util.isNotEmptyObject(email.getCc())) {
               		helper.setCc(email.getCc());
               	}
               /* Adding ccs to mail */	
               	if(Util.isNotEmptyObject(email.getCcs())) {
               		helper.setCc(email.getCcs());
               	}
               	Map<String, Object> model = new HashMap<String, Object>();
                model.put("email", email);
                
            	String text = geVelocityTemplateContent(model);
           
                // use the true flag to indicate you need a multipart message
            	helper.setText(text, true);

            	//Additionally, let's add a resource as an attachment as well.
            	if(email.isStatus()) {
                  /*  Additionally, let's add a resource as an attachment as well.*/
            		helper.addAttachment("dhoni_dashboard.jpg", new ClassPathResource("images/dhoni_dashboard.jpg"));
                  /*helper.addAttachment("Sumadhura_Project_Brochures.pdf", new ClassPathResource("images//Sumadhura_Project_Brochures.pdf"));
                 	helper.addAttachment("Eden-Garden-Brochure.pdf", new ClassPathResource("images//Eden-Garden-_-Brochure.pdf"));
                 	helper.addAttachment("Nandanam-Brochure.pdf", new ClassPathResource("images//Nandanam-Brochure.pdf"));*/
                }
            }
        };
        return preparator;
	}
	
	@Override
	public String geVelocityTemplateContent(Map<String, Object> model){
		StringBuffer content = new StringBuffer();
		try{
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,((Email)model.get("email")).getTemplateName(), model));
			return content.toString();
		}catch(Exception e){
			System.out.println("Exception occured while processing velocity template:"+e.getMessage());
		}
	      return "success";
	}

	public void sendErrorMail(final Exception ex) {
		logger.info("***** The control is inside the sendErrorMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		String[] errorAdmins = new String[2];
		errorAdmins[0] = "teamamaravadhis@gmail.com" ;
		errorAdmins[1] = "teamamaravadhis@gmail.com";
		for(String admin :errorAdmins ) {
			Email email = new Email();
			email.setSubject(" Production exception information");
			email.setTemplateName("/vmtemplates/velocity_ErrormailTemplate.vm");
			email.setToMail(admin);
			email.setException(ex);
			emailList.add(email);
		}
		for(Email email : emailList) {
			sendEmail(email);
		}
	}

	@Override
	public String sendDefaultBankerErrorMail(Email email) {
		logger.info("***** The control is inside the sendDefaultBankerErrorMail method in MailserviceImpl ******");
		MimeMessagePreparator preparator = getMimePreparatorDefaultBanker(email);
		try {
			 EmployeeMailSender.send(preparator);
			 logger.info("**** Mail sent successfully to employee ****"+email.getToMail()+""+email.getToMails()+" "+email.getCcs());
			 return "Success";
		} catch(MailException ex) {
			 logger.info("**** Error while sending mail to emplyee ****"+ex.getMessage());
			 return "Failure";
		}
	}
	
	private MimeMessagePreparator getMimePreparatorDefaultBanker(final Email email) {
		logger.info("**** The control is inside the getMimePreparatorDefaultBanker in MailServiceImpl ****");
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				logger.info("**** The control is inside the prepare in getMimePreparatorDefaultBanker of MailServiceImpl ****"+email.getToMail()+" "+email.getToMails());
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setSubject(email.getSubject());
				
				if (Util.isNotEmptyObject(email.getToMail())) {
					helper.setTo(email.getToMail());
				} else if (Util.isNotEmptyObject(email.getToMails())) {
					helper.setTo(email.getToMails());
				}
				
				if(Util.isNotEmptyObject(email.getCc())) {
					helper.setCc(email.getCc());
				} else if(Util.isNotEmptyObject(email.getCcs())) {
					helper.setCc(email.getCcs());
				}
				
				if(Util.isNotEmptyObject(email.getBccs())) {
					helper.setBcc(email.getBccs());
				}
				//String subject = email.getSubject();
				//email.getTitle();
				Map<String, Object> model = new HashMap<String, Object>();
                model.put("email", email);
            	//String text = geVelocityTemplateContent(model);
                String text = email.getEmailBodyText();
            	logger.info("HTML DATA\n"+text);
                // use the true flag to indicate you need a multipart message
            	helper.setText(text, true);
            	if(Util.isNotEmptyObject(email.getFilePath())) {
            		
            	}
			}
		};
	
	}

	@Override
	public boolean sendTicketReportingMail(@NonNull final TicketReportingResponce resp) {
		logger.info("***** The control is inside the sendTicketReportingMail method in MailserviceImpl ******");
		final List<Email> emailList = new ArrayList<Email>();
		for(TicketReportingPojo pojo : resp.getTicketEscalationEmployeeMails()) {
			Email email = new Email();
			List<Long> siteIds = new ArrayList<>();
			StringBuilder siteName = new StringBuilder("");
			for(TicketReportingPojo.Site site : pojo.getSiteList()) {
				siteName.append(site.getSiteName()+",");
				siteIds.add(site.getSiteId());
			}
			email.setSiteName(StringUtils.chop(siteName.toString()));
			email.setSubject(" Ticketing Status Report for -<"+StringUtils.chop(siteName.toString())+">- From <"+pojo.getStratDate()+"> To <"+pojo.getEndDate()+">!.");
			email.setTemplateName("/vmtemplates/Ticket_Reporting_template.vm");
			email.setToMail(pojo.getMail());
			email.setSiteIds(siteIds);
			email.setEmployeeId(pojo.getEmpId());
			email.setEmployeeName(pojo.getEmployeeName());
			email.setStartDate(pojo.getStratDate());
			email.setEndDate(pojo.getEndDate());
			email.setStartDateParam(pojo.getStratDate().getTime());
			email.setEndDateParam(pojo.getEndDate().getTime());
			email.setSiteIdsParam(siteIds.toString().substring(1, siteIds.toString().length()-1).replaceAll("\\s", ""));
			emailList.add(email);
		}
		/* here mail is going employee */
		Thread thread = new Thread() {
			public synchronized void run() {
				//System.out.println(emailList);
				for(Email email :emailList) {
				   // System.out.println(email);	
				    sendEmployeeEmail(email);
				// sendEmail(email);
			   }
			}		
		};
		thread.setName("Ticket Report Thread");
		thread.start();
		return true;
	}
	@Override
	public boolean sendEmployeeTicketForwardMail(@NonNull final EmployeeTicketRequestInfo info) {
		logger.info("***** The control is inside the sendEmployeeTicketForwardMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : info.getMails()) {
			Email email = new Email();
			email.setSubject(" Ticket Reassigned -<"+info.getTicketId()+"|"+info.getSiteName()+"-"+info.getFlatNo()+">-"+info.getDeptName()+">");
			email.setTicketId(info.getTicketId());
			email.setFlatNumber(info.getFlatNo());
			email.setDepartmentName(info.getDeptName());
			email.setName(info.getGeneric());
			email.setMessage(info.getMessage());
			email.setTemplateName("/vmtemplates/Email_to_CRM_Team_on_a_ticket_REASSIGNMENT.vm");
			email.setToMail(employeeMail);
			emailList.add(email);
		}
		for(Email email : emailList) {
			/*  email is sent to employee */
			sendEmployeeEmail(email);
			//sendEmail(email);
		}
		return true;
	}
	
	@Override
	public boolean sendBookingApprovalMail(@NonNull BookingFormRequestInfo info) {
		logger.info("***** The control is inside the sendBookingApprovalMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : info.getMails()) {
			Email email = new Email();
			email.setSubject(" Booking Form Approved Mail ");
			email.setCustomerName(info.getCustomerName());
			email.setFlatNumber(info.getFlatNo());
			email.setSiteName(info.getSiteName());
			email.setTemplateName("/vmtemplates/bookingform_approval.vm");
			email.setToMail(employeeMail);
			emailList.add(email);
		}for(Email email : emailList) {
			/* sending mail to customer. */
		     sendCustomerEmail(email);
			//sendEmail(email);
		}
		return true;
	}
	
	@Override
	public boolean sendCustomerTicketForwardMail(@NonNull final EmployeeTicketRequestInfo info) {
		logger.info("***** The control is inside the sendCustomerTicketForwardMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : info.getMails()) {
			Email email = new Email();
			email.setSubject(" Ticket Reassigned -<"+info.getTicketId()+"|"+info.getSiteName()+"-"+info.getFlatNo()+">-"+info.getDeptName()+">");
			email.setCustomerName(info.getCustomerName());
			email.setTicketId(info.getTicketId());
			email.setFlatNumber(info.getFlatNo());
			email.setDepartmentName(info.getDeptName());
			email.setMessage(info.getMessage());
			email.setTemplateName("/vmtemplates/Email_to_Customer on_a_ticket_REASSIGNMENT.vm");
			email.setTicketOwner(info.getTicketOwner());
			email.setToMail(employeeMail);
			emailList.add(email);
		}
		/* No need to send Mails to Customers */
		/*for(Email email : emailList) {
			/* sending mail to customer. 
			sendCustomerEmail(email);
			//sendEmail(email);
		}*/
		return true;
	}
	
	@Override
	public boolean sendEmployeeTicketEscalationMail(@NonNull final EmployeeTicketRequestInfo info) {
		logger.info("***** The control is inside the sendEmployeeTicketForwardMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : info.getMails()) {
			Email email = new Email();
			email.setSubject("Ticket Escalation -"+info.getEmployeeName()+"-<"+info.getTicketId()+"|"+info.getFlatNo()+">-<"+info.getDeptName()+">");
			email.setName(info.getEmployeeName());
			email.setTicketId(info.getTicketId());
			email.setFlatNumber(info.getFlatNo());
			email.setTicketClouserDate(info.getEndDate().toString());
			email.setTemplateName("/vmtemplates/Email_to_CRM_Head_-_Ticket_Escalation.vm");
			email.setToMail(employeeMail);
			emailList.add(email);
		}
		for(Email email : emailList) {
			/*  email is sent to employee */
			sendEmployeeEmail(email);
			//sendEmail(email);
		}
		return true;
	}
	
	@Override
	public boolean sendCustomerTicketEscalationMail(@NonNull final EmployeeTicketRequestInfo info) {
		logger.info("***** The control is inside the sendEmployeeTicketForwardMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : info.getMails()) {
			Email email = new Email();
			email.setSubject("Ticket Escalation -"+info.getEmployeeName()+"-<"+info.getTicketId()+">-<"+info.getDeptName()+">");
			email.setName(info.getEmployeeName());
			email.setCustomerName(info.getCustomerName());
			email.setTicketId(info.getTicketId());
			email.setFlatNumber(info.getFlatNo());
			email.setTicketClouserDate(info.getEndDate().toString());
			email.setTemplateName("/vmtemplates/Email_for_Ticket_Escalation_-_to_customer.vm");
			email.setToMail(employeeMail);
			emailList.add(email);
		}
		/* No need to send Mails to Customers */
		/*for(Email email : emailList) {
			/* sending mail to customer. 
			sendCustomerEmail(email);
			//sendEmail(email);
		}*/
		return true;
	}

	@Override
	public boolean sendAdminTicketEscalationMail(@NonNull final EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		logger.info("***** The control is inside the sendAdminTicketEscalationMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : employeeTicketRequestInfo.getMails()) {
			Email email = new Email();
			email.setSubject(" Escalation ticket from <"+employeeTicketRequestInfo.getSiteShortName()+"-"+employeeTicketRequestInfo.getFlatNo()+"|"+employeeTicketRequestInfo.getTicketId()+"|"+employeeTicketRequestInfo.getPendingEmployeeName() +">");
			
			/* Project Head mail  */
			email.setName(employeeTicketRequestInfo.getEmployeeName());
			email.setFlatNumber(employeeTicketRequestInfo.getFlatNo());
			email.setTicketId(employeeTicketRequestInfo.getTicketId());
			email.setPendingEmployeeAndDepartmentName(employeeTicketRequestInfo.getPendingEmployeeName());
			email.setSiteName(employeeTicketRequestInfo.getSiteName());
			email.setEscalationTime(employeeTicketRequestInfo.getEscalationTime());
			email.setTicketType(employeeTicketRequestInfo.getTicketType());
			email.setTicketSubject(employeeTicketRequestInfo.getTicketSubject());
			/* forward Ticket Details */
			email.setEmployeeId(employeeTicketRequestInfo.getEmployeeId());
			email.setDepartmentId(employeeTicketRequestInfo.getDepartmentId());
			email.setRequestUrl(employeeTicketRequestInfo.getRequestUrl());
			email.setTicketEscalationId(employeeTicketRequestInfo.getTicketEscalationId());
			email.setToId(employeeTicketRequestInfo.getToId());
			email.setToDeptId(employeeTicketRequestInfo.getToDeptId());
			email.setTypeOf(employeeTicketRequestInfo.getTypeOf());
			email.setMailOtpApproval(employeeTicketRequestInfo.getMailOtpApproval());
			email.setType("PM");
			email.setMerchantId(employeeTicketRequestInfo.getMerchantId());
			
			email.setTemplateName("/vmtemplates/ticketforwardactionmail.vm");
			email.setToMail(employeeMail);
			emailList.add(email);
		}
		for(Email email : emailList) {
			/*  email is sent to employee */
			sendEmployeeEmail(email);
			//sendEmail(email);
		}
		return true;
	}
	
	@Override
	public boolean sendCustomerTicketUpdateMail(@NonNull final EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		logger.info("***** The control is inside the sendCustomerTicketUpdateMail method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : employeeTicketRequestInfo.getMails()) {
			Email email = new Email();
			email.setSubject("Ticket Response -<"+employeeTicketRequestInfo.getTicketId()+" | "+employeeTicketRequestInfo.getFlatNo()+">-"+employeeTicketRequestInfo.getDeptName());
			email.setTicketId(employeeTicketRequestInfo.getTicketId());
			email.setCustomerName(employeeTicketRequestInfo.getCustomerName());
			email.setDepartmentName(employeeTicketRequestInfo.getDeptName());
			email.setFlatNumber(employeeTicketRequestInfo.getFlatNo());
			email.setMessage(employeeTicketRequestInfo.getMessage());
			email.setTemplateName("/vmtemplates/Email_for_Ticket_Response.vm");
			email.setToMail(employeeMail);
			emailList.add(email);
		}
		/* No need to send Mails to Customers */
		/*for(Email email : emailList) {
			/* sending mail to customer. 
			sendCustomerEmail(email);
			//sendEmail(email);
		}*/
		return true;
	}
	@Override
	public boolean sendEmployeeTicketCloseMail(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
      logger.info("**** The control is inside the sendEmployeeTicketCloseMail in MailServiceImpl ****");
      List<Email> emailList = new ArrayList<Email>();
		for(String employeeMail : employeeTicketRequestInfo.getMails()) {
			Email email = new Email();
			email.setSubject("Ticket Close Mail");
			email.setName(employeeTicketRequestInfo.getGeneric());
			//email.setTemplateName("/vmtemplates/velocity_ErrormailTemplate.vm");
			email.setTemplateName("/vmtemplates/velocity_CustomerEmailTemplate.vm");
			email.setToMail(employeeMail);
			//email.setException(new ArithmeticException("*** This is for mail checking purpous ****"));
			emailList.add(email);
		}
		for(Email email : emailList) {
			/*  email is sent to employee */
			sendEmployeeEmail(email);
			//sendEmail(email);
		}
		return true;
	}
	/*@Override
	public boolean sendCustomerTicketCloseMail(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		 logger.info("**** The control is inside the sendCustomerTicketCloseMail in MailServiceImpl ****");
		 List<Email> emailList = new ArrayList<Email>();
			for(String employeeMail : employeeTicketRequestInfo.getMails()) {
				Email email = new Email();
				email.setSubject("Ticket Update Mail");
				email.setName(employeeTicketRequestInfo.getGeneric());
				//email.setTemplateName("/vmtemplates/velocity_ErrormailTemplate.vm");
				email.setTemplateName("/vmtemplates/velocity_CustomerEmailTemplate.vm");
				email.setToMail(employeeMail);
				//email.setException(new ArithmeticException("*** This is for mail checking purpous ****"));
				emailList.add(email);
			}
			for(Email email : emailList) {
				sendEmail(email);
			}
			return true;
	}
	*/
	@Override
	public boolean sendCustomerTicketCloseMail(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		 logger.info("**** The control is inside the sendCustomerTicketCloseMail in MailServiceImpl ****");
		 List<Email> emailList = new ArrayList<Email>();
			for(String employeeMail : employeeTicketRequestInfo.getMails()) {
				Email email = new Email();
				Long ticketId = employeeTicketRequestInfo.getTicketId();
				String empName=employeeTicketRequestInfo.getCustomerName();
				String flatNo = employeeTicketRequestInfo.getFlatNo();
				String deptName = employeeTicketRequestInfo.getDeptName();
				String deptNam=(deptName!=null)?"-"+deptName:"";
				email.setSubject("Ticket Closing- "+ticketId+"|"+flatNo+deptNam);
				email.setName(employeeTicketRequestInfo.getGeneric());
				email.setTicketId(ticketId);
				email.setCustomerName(empName);
				email.setFlatNo(flatNo);
				email.setTemplateName("/vmtemplates/close_ticket_EmpToCustEmailTemplate.vm");

				email.setToMail(employeeMail);
				emailList.add(email);
			}
			/* No need to send Mails to Customers */
			/*for(Email email : emailList) {
				/* sending mail to customer. 
				sendCustomerEmail(email);
				//sendEmail(email);
			}*/
			return true;
	}
		
	@Override
	public boolean sendEmployeeTicketReOpenMail(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		 logger.info("**** The control is inside the sendEmployeeTicketReOpenMail in MailServiceImpl ****");
		 List<Email> emailList = new ArrayList<Email>();
			for(String employeeMail : employeeTicketRequestInfo.getMails()) {
				Email email = new Email();
				email.setSubject("Ticket ReOpen -<"+employeeTicketRequestInfo.getTicketId()+" | "+employeeTicketRequestInfo.getFlatNo()+">-"+employeeTicketRequestInfo.getDeptName());
				email.setTicketId(employeeTicketRequestInfo.getTicketId());
				email.setEmployeeName(employeeTicketRequestInfo.getTicketOwner());
				email.setCustomerName(employeeTicketRequestInfo.getCustomerName());
				email.setFlatNumber(employeeTicketRequestInfo.getFlatNo());
				email.setSiteName(employeeTicketRequestInfo.getSiteName());
				email.setTicketType(employeeTicketRequestInfo.getTicketType());
				email.setDepartmentName(employeeTicketRequestInfo.getDeptName());
				email.setTicketCreatedDate(employeeTicketRequestInfo.getTicketCreatedDate());
				email.setTicketExpectedCloserDate(employeeTicketRequestInfo.getTicketExpectedCloserDate());
				email.setTemplateName("/vmtemplates/E-Mail_to_CRM_team_on_a__ticket_Reopen.vm");
				email.setCc(employeeTicketRequestInfo.getEscalatedTicketAssignedEmployeeMail());
				email.setToMail(employeeMail);
				emailList.add(email);
			}
			for(Email email : emailList) {
				/*  email is sent to employee */
				sendEmployeeEmail(email);
				//sendEmail(email);
			}
			return true;
	}
	
	private static int countMail = 1;
	@Override
	public void sendFinancialMailToCustomers(Email email) {
		 logger.info("**** The control is inside the sendFinancialMailToCustomers in MailServiceImpl ****");
		 MimeMessagePreparator preparator = getMimePreparatorForFinancial(email);
		 try {
			if (countMail == 2 || countMail == 4) {//these if condition only for CUG, don't move this condition to live
				 countMail++;//these if condition only for CUG, don't move this condition to live
				 throw new RuntimeException("Error occured while sending mail to customer.");
			 }
			 //customerMailSender.send(preparator);
			 financialMailSender.send(preparator);
			 countMail++;
			 logger.info("**** Mail sent successfully to customer ****"+email.getToMail());
		 } catch(MailException ex) {
			 logger.info("**** Error while sending mail to customer ****"+ex.getMessage());
			 throw new RuntimeException("Error occured while sending mail to customer.");
		 }
	}
	
	@Override
	public void sendNOCLetterMailToEmployee(Email email) {
		logger.info("**** The control is inside the sendNOCLetterMailToEmployee in MailServiceImpl ****");
		 MimeMessagePreparator preparator = getMimePreparatorForFinancial(email);
		 try {
			 EmployeeMailSender.send(preparator);
			 logger.info("**** Mail sent successfully to employee ****"+email.getToMail());
		 }catch(MailException ex) {
			 logger.info("**** Error while sending mail to employee ****"+ex.getMessage());
		 }
	}
	@Override
	public void sendFinancialDemandNoteAndTransactionMailToEmployee(Email email) {
		 logger.info("**** The control is inside the sendFinancialDemandNoteAndTransactionMailToEmployee in MailServiceImpl ****");
		 MimeMessagePreparator preparator = getMimePreparatorForFinancial(email);
		 try {
			 EmployeeMailSender.send(preparator);
			 logger.info("**** Mail sent successfully to employee ****"+email.getToMail()+""+email.getToMails());
		 }catch(MailException ex) {
			 logger.info("**** Error while sending mail to emplyee ****"+ex.getMessage());
		 }
	}
	
	@Override
	public void sendFinancialRateOfInterestMailToEmployee(Email email) {
		 logger.info("**** The control is inside the sendFinancialDemandNoteAndTransactionMailToEmployee in MailServiceImpl ****");
		 MimeMessagePreparator preparator = getMimePreparatorForFinancial(email);
		 try {
			 EmployeeMailSender.send(preparator);
			 logger.info("**** Mail sent successfully to employee ****"+email.getToMail()+""+email.getToMails());
		 }catch(MailException ex) {
			 logger.info("**** Error while sending mail to emplyee ****"+ex.getMessage());
		 }
	}

	@Override
	public void sendFinancialTransactionExcelToEmployee(Email email) {
		 logger.info("**** The control is inside the sendFinancialDemandNoteAndTransactionMailToEmployee in MailServiceImpl ****");
		 if(Util.isNotEmptyObject(email.getCcs())) {
			logger.info("**** sendFinancialTransactionExcelToEmployee getCcs "+Arrays.asList(email.getCcs()));	
		 }
		 MimeMessagePreparator preparator = getMimePreparatorForFinancialTRNExcel(email);
		 try {
			 EmployeeMailSender.send(preparator);
			 logger.info("**** Mail sent successfully to employee ****"+email.getToMails()+""+email.getToMails());
		 }catch(MailException ex) {
			 logger.info("**** Error while sending mail to emplyee ****"+ex.getMessage());
		 }
	}

	@SuppressWarnings("unused")
	private MimeMessagePreparator getMimePreparatorForFinancialWithOutTemplate(final Email email) {
		logger.info("**** The control is inside the getMimePreparatorForFinancial in MailServiceImpl ****");
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				logger.info("**** The control is inside the prepare in MimeMessagePreparator of MailServiceImpl ****"+email.getToMail()+" "+email.getToMails());
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setSubject(email.getSubject());
				
				if (Util.isNotEmptyObject(email.getToMail())) {
					helper.setTo(email.getToMail());
				} else if (Util.isNotEmptyObject(email.getToMails())) {
					helper.setTo(email.getToMails());
				}
				
				if(Util.isNotEmptyObject(email.getCc())) {
					helper.setCc(email.getCc());
				} else if(Util.isNotEmptyObject(email.getCcs())) {
					helper.setCc(email.getCcs());
				}
				
				if(Util.isNotEmptyObject(email.getBccs())) {
					helper.setBcc(email.getBccs());
				}
				//email.getTitle();
				String text = email.getMessage();
				Map<String, Object> model = new HashMap<String, Object>();
                model.put("email", email);
            	//String text = geVelocityTemplateContent(model);
            	logger.info("HTML DATA\n"+text);
                // use the true flag to indicate you need a multipart message
            	helper.setText(text, true);
            	if(Util.isNotEmptyObject(email.getFilePath())) {
            		//do not uncomment this line in live
            		//make it comment in live, if required then uncomment
            		helper.addAttachment(email.getFileName(), new File(email.getFilePath()));
            	}
			}
		};
	}	

	
	private MimeMessagePreparator getMimePreparatorForFinancial(final Email email) {
		logger.info("**** The control is inside the getMimePreparatorForFinancial in MailServiceImpl ****");
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				logger.info("**** The control is inside the prepare in MimeMessagePreparator of MailServiceImpl ****"+email.getToMail()+" "+email.getToMails());
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setSubject(email.getSubject());
				
				if (Util.isNotEmptyObject(email.getToMail())) {
					helper.setTo(email.getToMail());
				} else if (Util.isNotEmptyObject(email.getToMails())) {
					helper.setTo(email.getToMails());
				}
				
				if(Util.isNotEmptyObject(email.getCc())) {
					helper.setCc(email.getCc());
				} else if(Util.isNotEmptyObject(email.getCcs())) {
					helper.setCc(email.getCcs());
				}
				
				if(Util.isNotEmptyObject(email.getBccs())) {
					helper.setBcc(email.getBccs());
				}
				String subject = email.getSubject();
				//email.getTitle();
				Map<String, Object> model = new HashMap<String, Object>();
                model.put("email", email);
            	String text = geVelocityTemplateContent(model);
            	logger.info("HTML DATA\n"+text);
                // use the true flag to indicate you need a multipart message
            	helper.setText(text, true);
            	if(Util.isNotEmptyObject(email.getFilePath())) {
            		//do not uncomment this line in live
            		//make it comment in live
            		if(subject.contains("Demand Note") || subject.contains("We Received your Payment") ||"Interest Letter".equalsIgnoreCase(email.getRequestUrl())) {
            			helper.addAttachment(email.getFileName(), new File(email.getFilePath()));	
            		}
            	}
			}
		};
	}	

	private MimeMessagePreparator getMimePreparatorForFinancialTRNExcel(final Email email) {
		logger.info("**** The control is inside the getMimePreparatorForFinancial in MailServiceImpl ****");
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				logger.info("**** The control is inside the prepare in MimeMessagePreparator of MailServiceImpl ****"+email.getToMail()+" "+email.getToMails());
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setSubject(email.getSubject());
				
				if (Util.isNotEmptyObject(email.getToMail())) {
					helper.setTo(email.getToMail());
				} else if (Util.isNotEmptyObject(email.getToMails())) {
					helper.setTo(email.getToMails());
				}
				
				if(Util.isNotEmptyObject(email.getCc())) {
					helper.setCc(email.getCc());
				} else if(Util.isNotEmptyObject(email.getCcs())) {
					helper.setCc(email.getCcs());
				}
				
				if(Util.isNotEmptyObject(email.getBccs())) {
					helper.setBcc(email.getBccs());
				}
				//String subject = email.getSubject();
				//email.getTitle();
				Map<String, Object> model = new HashMap<String, Object>();
                model.put("email", email);
            	String text = geVelocityTemplateContent(model);
            	logger.info("HTML DATA\n"+text);
                // use the true flag to indicate you need a multipart message
            	helper.setText(text, true);
            	if(Util.isNotEmptyObject(email.getFilePath())) {
            			helper.addAttachment(email.getFileName(), new File(email.getFilePath()));	
            	}
			}
		};
	}	

	
	@Async
	@Override
	public void sendEmployeeMailForChangePassword(List<EmployeePojo> employeePojos) {
		 logger.info("**** The control is inside the sendEmployeeMailForChangePassword in MailServiceImpl ****");
		for(EmployeePojo employeePojo : employeePojos) {
			 	Email email = new Email();
				email.setSubject("Password Changed Successfully.");
				email.setEmployeeName(Util.isNotEmptyObject(employeePojo.getEmployeeName())?employeePojo.getEmployeeName():"Employee");
				email.setTemplateName("/vmtemplates/Change_Password_Email_To_Employee.vm");
				email.setToMail(employeePojo.getEmail());
				sendEmployeeEmail(email);
			}
	} 
	
	@Override
	public boolean sendChangeTicketTypeMailToAdmin(@NonNull ChangeTicketType changeTicketTypeRequest) {
		 logger.info("**** The control is inside the sendChangeTicketTypeMailToAdmin in MailServiceImpl ****");
		 List<Email> emailList = new ArrayList<Email>();
		     for(String to : changeTicketTypeRequest.getTo().split(",")) {
			 /* setting dynamic properties to Vm Template */
		       Email email = new Email();
		       /* setting template name */
			   email.setTemplateName("/vmtemplates/Change_TicketType_Admin_MailTemplate.vm");
			   /*  setting to */
			   email.setToMail(to);
			   /* setting cc */
			   if(Util.isNotEmptyObject(changeTicketTypeRequest.getCc())) {
				   email.setCcs(changeTicketTypeRequest.getCc().split(",")); 
			   }
			   /* setting subject */
			   email.setSubject(changeTicketTypeRequest.getSubject());
			   /* setting employeeName */
			   email.setEmployeeName(changeTicketTypeRequest.getEmployeeName()); 
			   /* setting Ticket Message */
			   email.setMessage(changeTicketTypeRequest.getMessageBody());
			   /* setting TicketNo */
			   email.setTicketId(changeTicketTypeRequest.getTicketId());
			   /* setting TicketType */
			   email.setTicketType(changeTicketTypeRequest.getRaisedUnderCategory());
			   /* setting category to be raised  */
			   email.setCategoryToBeRaised(changeTicketTypeRequest.getCategoryToBeChanged());
			  /* setting TicketMessage */
			   email.setTicketSubject(changeTicketTypeRequest.getDescription());
			   emailList.add(email);
		     }
		     for(Email email : emailList) {
			   /*  email is sent to employee */
			   sendEmployeeEmail(email);
			} 
		 return true;
	}
	@Override
	public boolean sendChangeTicketTypeReminderMailToAdmin(@NonNull EmployeeTicketRequestInfo emailReq) {
	logger.info("**** The control is inside the sendChangeTicketTypeReminderMailToAdmin in MailServiceImpl ****");	
	 List<Email> emailList = new ArrayList<Email>();	
	 for(String to : emailReq.getMails()) {
		 /* setting dynamic properties to Vm Template */
	       Email email = new Email();
	       /* setting subject */
		   try {
			email.setSubject(responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_REMINDER_MAIL_SUBJECT")+"<"+emailReq.getTicketId()+">");
		   } catch (IOException e) {
			   logger.error(e);
		   }
	       /* setting template name */
		   email.setTemplateName("/vmtemplates/Change_TicketType_Admin_Reminder_MailTemplate.vm");
		   /*  setting to */
		   email.setToMail(to);
		   /* setting cc */
		   if(Util.isNotEmptyObject(emailReq.getCcMails())) {
			   email.setCcs(emailReq.getCcMails());
		   }
		   /* setting employeeName */
		   email.setEmployeeName(emailReq.getEmployeeName()); 
		   /* setting TicketNo */
		   email.setTicketId(emailReq.getTicketId());
		   
		   emailList.add(email);
	     }
	    for(Email email : emailList) {
		   /* email is sent to employee */
		   sendEmployeeEmail(email);
		}
      return true;
	}
	
	
	@Override
	public boolean sendChangeTicketTypeTicketOwnersMail(@NonNull ChangeTicketType changeTicketTypeRequest) {
		logger.info("**** The control is inside the sendChangeTicketTypeTicketOwnersMail in MailServiceImpl ****");
		List<Email> emailList = new ArrayList<Email>();
		for(String to : new String[] {changeTicketTypeRequest.getTo()}) {
			 /* setting dynamic properties to Vm Template */
		       Email email = new Email();
		       /* setting template name */
			   email.setTemplateName("/vmtemplates/Change_Tickettype_Ticket_Owners_MailTemplate.vm");
			   /*  setting to */
			   email.setToMail(to);
			   /* setting cc */
			  // email.setCcs(changeTicketTypeRequest.getCc().split(","));
			   /* setting subject */
			   email.setSubject(changeTicketTypeRequest.getSubject());
			   /* setting employeeName */
			   email.setEmployeeName(changeTicketTypeRequest.getEmployeeName()); 
			   /* setting Ticket Message */
			   email.setMessage(changeTicketTypeRequest.getMessageBody());
			   /* setting TicketNo */
			   email.setTicketId(changeTicketTypeRequest.getTicketId());
			   /* setting TicketType */
			   email.setTicketType(changeTicketTypeRequest.getRaisedUnderCategory());
			   /* setting category to be raised  */
			   email.setCategoryToBeRaised(changeTicketTypeRequest.getCategoryToBeChanged());
			  /* setting TicketMessage */
			   email.setTicketSubject(changeTicketTypeRequest.getDescription());
			   emailList.add(email);
		     }
		     for(Email email : emailList) {
			   /*  email is sent to employee */
			   sendEmployeeEmail(email);
			} 
		 return true;
	}
	
	@Override
	public Boolean sendMailNotificationsToApprovalLevelEmployee(List<EmployeePojo> senderEmployeePojoList, List<EmployeePojo> approverEmployeePojoList, SiteLevelNotifyRequestDTO notificationRequest) {
		logger.info("**** The control is inside the sendMailNotificationsToApprovalLevelEmployee in MailServiceImpl ****");
		for(EmployeePojo employeePojo : approverEmployeePojoList) {
			if(Util.isNotEmptyObject(employeePojo) && Util.isNotEmptyObject(notificationRequest)) {
				Email email = new Email();
				String senderEmpName = "";
				email.setToMail(Util.isNotEmptyObject(employeePojo.getEmail())?employeePojo.getEmail():"");
				email.setSubject("Regarding Request of approval for Notification.");
				email.setTemplateName("/vmtemplates/Notification_Approval_Mail_Template.vm");
				/* Approver Employee Name */
				email.setEmployeeName(Util.isNotEmptyObject(employeePojo.getFirstName())?employeePojo.getFirstName():"");
				email.setId(Util.isNotEmptyObject(notificationRequest.getId())?notificationRequest.getId():0l);
				email.setTitle(Util.isNotEmptyObject(notificationRequest.getMessage())?notificationRequest.getMessage():"");
				/* sender Employee Name */
				if(Util.isNotEmptyObject(senderEmployeePojoList) && Util.isNotEmptyObject(senderEmployeePojoList.get(0))) {
					senderEmpName= Util.isNotEmptyObject(senderEmployeePojoList.get(0).getFirstName())?senderEmployeePojoList.get(0).getFirstName():"";
				}
				email.setName(senderEmpName);
				/* sending email to employee */
				sendEmployeeEmail(email);
			}
		}
		return true;
	}

	@Override
	public boolean sendCustomerReferralStatusToEmployeeMail(ReferedCustomer referedCustomer) {
		 logger.info("**** The control is inside the sendCustomerReferralStatusToEmployeeMail in MailServiceImpl ****");
		 @SuppressWarnings("unused")
		List<Email> emailList = new ArrayList<Email>();
				Email email = new Email();
				String siteShortName=(Util.isNotEmptyObject(referedCustomer.getSiteShortName())?referedCustomer.getSiteShortName():"N/A");
				email.setSubject("Referral Lead from "+siteShortName+"-"+referedCustomer.getCustomerFlatNo()+" Status");
				email.setReferrerName(referedCustomer.getReferrerName());
				email.setReferralStatusName(referedCustomer.getReferralStatusName());
				email.setRefrenceId(referedCustomer.getRefrenceId());
				email.setCustomerName(referedCustomer.getCustomerName());
				email.setFlatNo(referedCustomer.getCustomerFlatNo());
				email.setTemplateName("/vmtemplates/ReferrarStatuToEmpTemplate.vm");
				email.setRefferalPhoneNo(Util.isNotEmptyObject(referedCustomer.getMobileNo())?referedCustomer.getMobileNo():"N/A");
				email.setEmployeeName(referedCustomer.getEmployeeName());
				email.setSiteShortNameWithFlatNo(siteShortName+"-"+referedCustomer.getCustomerFlatNo());
				email.setToMail(referedCustomer.getEmployeeEmailId());       
				sendEmployeeEmail(email);
			    return true;
	}

	@Override
	public void sendEmployeeEmailAlert(Email email) {
		logger.info("**** The control is inside the sendEmployeeEmailAlert in MailServiceImpl ****");
		sendEmployeeEmail(email);
	}
	
	@Override
	public String sendMailToBankerOnBooking(List<CustomerPropertyDetailsPojo> customerPojo, EmployeeDetailsPojo employeeDetailsPojo
			, List<EmployeeDetailsPojo> cRM_detailsMailListForErrorMail, Long primaryKey) {
		logger.info("***** The control is inside the sendMailToBanker  method in MailserviceImpl ******");
		List<Email> emailList = new ArrayList<Email>();
		String subject = "";
		String flat = "";
		String siteName = "";
		String bookingDate = "";
		for (CustomerPropertyDetailsPojo pojo : customerPojo) {
			Email email = new Email();
			flat = Util.isNotEmptyObject(pojo.getFlatNo()) ? pojo.getFlatNo() : "N/A";
			siteName = Util.isNotEmptyObject(pojo.getSiteName()) ? pojo.getSiteName() : "N/A";
			// subject="Flat \""+flat+"\" of \""+siteName+" by sumadhura\" is sold out..!";
			subject = "New Lead - " + siteName;
			email.setSubject(subject);
			email.setTemplateName("/vmtemplates/HomeLoanMailToBankerOnBooking.vm");
			email.setInterestedBankName(Util.isNotEmptyObject(pojo.getInterestedBankName())?pojo.getInterestedBankName():"N/A");
			email.setBankContactPersonName(Util.isNotEmptyObject(pojo.getBankContactPersonName())?pojo.getBankContactPersonName():"N/A");
			email.setCustomerName(Util.isNotEmptyObject(pojo.getCustomerName())?pojo.getCustomerName():"N/A");
			email.setFlatNo(flat);
			email.setFilePaths(pojo.getFilePaths());
			if (Util.isNotEmptyObject(pojo.getEmail())) {//to mail is banker email
				email.setToMails(pojo.getEmail().split(","));
			}
		//	email.setToMail("venkateshwar444@gmail.com");
			email.setSiteName(pojo.getSiteName()+" by sumadhura");
			if (Util.isNotEmptyObject(pojo.getCrmMails())) {//cc emails is internal employee emails
				email.setCcs(pojo.getCrmMails().split(","));
			}
			email.setSiteName(pojo.getSiteName());
			email.setContactNumber(Util.isNotEmptyObject(pojo.getContactNumber())?pojo.getContactNumber():"N/A");
			email.setCustomerEmail(Util.isNotEmptyObject(pojo.getCustomerEmail())?pojo.getCustomerEmail():"N/A");
			if (Util.isNotEmptyObject(pojo.getBookingDate())) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				bookingDate = formatter.format(pojo.getBookingDate());
			}
			email.setBookingDate(Util.isNotEmptyObject(bookingDate)?bookingDate:"N/A");
			//email.setTotalAgreementCost(pojo.getTotalAgreementCost());
			email.setTotalCost(Util.isNotEmptyObject(pojo.getTotalCost())?pojo.getTotalCost():"N/A");
			emailList.add(email);
		}
		
		logger.info("***** The control is inside the sendMailToBanker  method in MailserviceImpl ******"+emailList);
		for (Email email : emailList) {
			//sendEmployeeEmail(email);
			return sendMailFromEmployeeEmail(email,employeeDetailsPojo,cRM_detailsMailListForErrorMail);
		}
		return "";
	}

	@Autowired
	private CurrencyUtil currencyUtil;
	
	private static final RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static final int roundingModeSize = 2;
	
	@Override
	public void sendUpdateLeadDetailsToEmployee(CustomerPropertyDetailsPojo pojo, LoanRequest loanRequest, List<EmployeeDetailsPojo> crmDetails) {
		Email email = new Email();
		List<Map<String,String>> dataForTemplate = new ArrayList<>();
		Map<String,String> data = new HashMap<>();
		data.put("leadStatus", loanRequest.getLeadStatus());
		data.put("bankerComment", loanRequest.getBankerComment());
		data.put("customerLoanEOIDetailsId", loanRequest.getCustomerLoanEOIDetailsId().toString());
		data.put("siteName", pojo.getSiteName());
		data.put("flatNo", pojo.getFlatNo());
		data.put("custName",Util.isNotEmptyObject(pojo.getCustomerName())?pojo.getCustomerName():"-");
		data.put("flatCost", Util.isNotEmptyObject(pojo.getTotalCost())?currencyUtil.convertUstoInFormat(BigDecimal.valueOf(Double.valueOf(pojo.getTotalCost())).setScale(roundingModeSize, roundingMode).toString()):"N/A");
		dataForTemplate.add(data);
		email.setSubject("Re : Sumadhura Customer "+pojo.getFlatNo()+" EOI for loan- Status update");
		email.setDataForTemplate(dataForTemplate);
		email.setTemplateName("/vmtemplates/ApplyLoanInfoMail.vm");
		email.setInterestedBankName(Util.isNotEmptyObject(pojo.getInterestedBankName()) ? pojo.getInterestedBankName() : "N/A");

		email.setCustomerName(Util.isNotEmptyObject(pojo.getCustomerName()) ? pojo.getCustomerName() : "N/A");
		email.setFlatNo(pojo.getFlatNo());
		
		if (Util.isNotEmptyObject(crmDetails)) {
			List<String> emailList = getTheEmails(crmDetails);
			if (Util.isNotEmptyObject(emailList)) {
				email.setToMails(emailList.toArray(new String[] {}));
				sendEmployeeEmail(email);
			}
		}
	}

	private List<String> getTheEmails(List<EmployeeDetailsPojo> crmdetails) {
		List<String> emailList = new ArrayList<>();

		for (EmployeeDetailsPojo map : crmdetails) {
			String email = map.getEmail();
			if (email.length() == 0 || emailList.contains(email)) {
				continue;
			}
			emailList.add(email);
		}

		return emailList;
	}
}