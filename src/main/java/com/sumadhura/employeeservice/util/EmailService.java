package com.sumadhura.employeeservice.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.web.bind.annotation.InitBinder;

import com.sumadhura.employeeservice.dto.MailConfigurationDtls;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;
import com.sumadhura.employeeservice.persistence.dto.MailConfigurationDtlsDTO;
import com.sumadhura.employeeservice.service.dto.MailConfigurationDtlsInfo;


@Configuration
public class EmailService {

	@Value("${HOST_EMAIL}")
	private String HOST_EMAIL;
	
	@Value("${HOST_EMAIL_PASSWORD}")
	private String HOST_EMAIL_PASSWORD;
	
	@Value("${HOST}")
	private String HOST;
	
	@Value("${PORT}")
	private String PORT;
	
	@Value("${HOST_EMAIL}")
	private String EMPLOYEE_HOST_EMAIL;
	
	@Value("${HOST_EMAIL_PASSWORD}")
	private String EMPLOYEE_HOST_EMAIL_PASSWORD;
	
	@Value("${NONCUST_CUST_MODEL}")
	private String NONCUST_CUST_MODEL;
	
	@Value("${NONCUST_EMPLOYEE_MODEL}")
	private String NONCUST_EMPLOYEE_MODEL;

	@Value("${NONCUST_SITEID}")
	private String NONCUST_SITEID;
	
	@Value("${FINANCIAL_EMP_MODEL}")
	private String FINANCIAL_EMP_MODEL;
	
	@Value("${FINANCIAL_EMP_MODEL_SITE_ID}")
	private String FINANCIAL_EMP_MODEL_SITE_ID;
	
	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;
	
	
	private final static Logger logger = Logger.getLogger(EmailService.class);
	
	/*@Bean(name="nmdPJdbcTemplateSumadhura")
	public NamedParameterJdbcTemplate getDT() throws SQLException {
		UCPSample sam = new UCPSample();
		return new NamedParameterJdbcTemplate(sam.getDataSource());
	}*/
	
	@Bean(name = "mailsenderObj")
	//@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)  prototype
    //@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		logger.info("****** The control is inside the getMailSender *******");
		// Using gmail.
		mailSender.setHost(HOST);
		mailSender.setPort(Integer.valueOf(PORT));
		mailSender.setUsername(HOST_EMAIL);
		mailSender.setPassword(HOST_EMAIL_PASSWORD);
		
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");
		javaMailProperties.put("mail.smtp.socketFactory.port",Integer.valueOf(PORT));
		javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
		
		mailSender.setJavaMailProperties(javaMailProperties);
		
		return mailSender;
	}

	public JavaMailSender getEmployeeMailSenderObject(MailConfigurationDtls mailConfigurationDtls) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		logger.info("EmailService.getEmployeeMailSenderObject()");
		// Using gmail.
		mailSender.setHost(HOST);
		mailSender.setPort(Integer.valueOf(PORT));
		//loading employee email and password
		mailConfigurationDtls = getEmployeeEmailDtls(mailConfigurationDtls);
		mailSender.setUsername(mailConfigurationDtls.getUserName());
		mailSender.setPassword(mailConfigurationDtls.getPassword());

		/*mailSender.setUsername("crm.aspire@realvaluehomes.in");
		mailSender.setPassword("qlyrhansesrobyqd");

		mailSender.setUsername("customerrelations@sumadhuragroup.com");
		mailSender.setPassword("wyalzhntizwvfohk");

		mailSender.setUsername("crm.hyderabad@sumadhuragroup.com");
		mailSender.setPassword("eorgnabiixwtnckl");*/

/*		mailSender.setUsername(gmailUsername);
		mailSender.setPassword(gmailPassword);
*/
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");
		javaMailProperties.put("mail.smtp.socketFactory.port",Integer.valueOf(PORT));
		javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
		
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	
	@Bean(name = "employeemailsenderObj")
	//@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    //@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JavaMailSender getEmployeeMailSenderObj() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		logger.info("****** The control is inside the getEmployeeMailSender *******");
		mailSender.setHost(HOST);
		mailSender.setPort(Integer.valueOf(PORT));
		mailSender.setUsername(EMPLOYEE_HOST_EMAIL);
		mailSender.setPassword(EMPLOYEE_HOST_EMAIL_PASSWORD);
		
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");
		javaMailProperties.put("mail.smtp.socketFactory.port",Integer.valueOf(PORT));
		javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	
	 @Bean(name = "customerMailsenderObj")
		// @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
			public JavaMailSender getCustomerMailSender() {
				JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

				System.out.println("****** The control is inside the getCustomerMailSender *******");
				// Using gmail.
				mailSender.setHost(HOST);
				mailSender.setPort(Integer.valueOf(PORT));
			//	mailSender.setUsername(HOST_EMAIL_CUSTOMER);
			//	mailSender.setPassword(HOST_EMAIL_CUSTOMER_PASSWORD);
				MailConfigurationDtls mailConfigurationDtls = getEmailDtls(new MailConfigurationDtls(NONCUST_CUST_MODEL,Long.valueOf(NONCUST_SITEID)));
				mailSender.setUsername(mailConfigurationDtls.getUserName());
				mailSender.setPassword(mailConfigurationDtls.getPassword());


				Properties javaMailProperties = new Properties();
				javaMailProperties.put("mail.smtp.starttls.enable", "true");
				javaMailProperties.put("mail.smtp.auth", "true");
				javaMailProperties.put("mail.transport.protocol", "smtp");
				javaMailProperties.put("mail.debug", "false");
				javaMailProperties.put("mail.smtp.socketFactory.port",Integer.valueOf(PORT));
				javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
				
				mailSender.setJavaMailProperties(javaMailProperties);
				return mailSender;
			}
	 
	@Bean(name = "financialMailSenderObject")
	// @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JavaMailSender getfinaniclaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		System.out.println("****** The control is inside the getfinaniclaMailSender *******");
		// Using gmail.
		mailSender.setHost(HOST);
		mailSender.setPort(Integer.valueOf(PORT));
		// mailSender.setUsername(HOST_EMAIL_CUSTOMER);
		// mailSender.setPassword(HOST_EMAIL_CUSTOMER_PASSWORD);
		MailConfigurationDtls mailConfigurationDtls = getEmailDtls(
				new MailConfigurationDtls(FINANCIAL_EMP_MODEL, Long.valueOf(FINANCIAL_EMP_MODEL_SITE_ID)));
		mailSender.setUsername(mailConfigurationDtls.getUserName());
		mailSender.setPassword(mailConfigurationDtls.getPassword());

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");
		javaMailProperties.put("mail.smtp.socketFactory.port", Integer.valueOf(PORT));
		javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
		//Session session = Session.getDefaultInstance(javaMailProperties);
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}	 

	//@Bean(name = "replyEmployeeMailsenderObj")
//	@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JavaMailSender replyEmployeeMailsenderObj() throws MessagingException {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		System.out.println("****** The control is inside the replyEmployeeMailsenderObj *******");
		mailSender.setHost(HOST);
		mailSender.setPort(Integer.valueOf(PORT));
		
		MailConfigurationDtls mailConfigurationDtls = getEmailDtls(new MailConfigurationDtls(NONCUST_EMPLOYEE_MODEL,Long.valueOf(NONCUST_SITEID)));
		mailSender.setUsername(mailConfigurationDtls.getUserName());
		mailSender.setPassword(mailConfigurationDtls.getPassword());

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");
		javaMailProperties.put("mail.smtp.socketFactory.port",Integer.valueOf(PORT));
		javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
		 
		mailSender.setJavaMailProperties(javaMailProperties);
		
		mailSender.getSession().getStore("");
		
		
		return mailSender;
	}

			
			@Bean(name = "EmployeeMailsenderObj")
		//	@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
			public JavaMailSender getEmployeeMailSender() {
				JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

				System.out.println("****** The control is inside the getMailSender *******");
				// Using gmail.
				mailSender.setHost(HOST);
				mailSender.setPort(Integer.valueOf(PORT));
		//		mailSender.setUsername(HOST_EMAIL_EMPLOYEE);
		//		mailSender.setPassword(HOST_EMAIL_EMPLOYEE_PASSWORD);
				
				MailConfigurationDtls mailConfigurationDtls = getEmailDtls(new MailConfigurationDtls(NONCUST_EMPLOYEE_MODEL,Long.valueOf(NONCUST_SITEID)));
				mailSender.setUsername(mailConfigurationDtls.getUserName());
				mailSender.setPassword(mailConfigurationDtls.getPassword());

				Properties javaMailProperties = new Properties();
				javaMailProperties.put("mail.smtp.starttls.enable", "true");
				javaMailProperties.put("mail.smtp.auth", "true");
				javaMailProperties.put("mail.transport.protocol", "smtp");
				javaMailProperties.put("mail.debug", "false");
				javaMailProperties.put("mail.smtp.socketFactory.port",Integer.valueOf(PORT));
				javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
				
				mailSender.setJavaMailProperties(javaMailProperties);
				return mailSender;
			}
			
	/*
	 * Velocity configuration.
	 */
	@Bean
	public VelocityEngine getVelocityEngine() throws VelocityException, IOException {
		VelocityEngineFactory factory = new VelocityEngineFactory();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		factory.setVelocityProperties(props);
		return factory.createVelocityEngine();
	}

	public MailConfigurationDtls getEmailDtlsHelper(MailConfigurationDtlsInfo email) {
		logger.info("******* The control inside of the getEmailDtls method  inside  SiteServiceImpl *******"+email);
		MailConfigurationDtlsInfo configurationDtlsInfo = new MailConfigurationDtlsInfo();
		MailConfigurationDtls configurationDtls = new MailConfigurationDtls();
		MailConfigurationDtlsDTO mailConfigurationDtlsDTO = employeeTicketDaoImpl.getEmailDtls(email);
		try {
			if(mailConfigurationDtlsDTO.getPassword() != null && mailConfigurationDtlsDTO.getPassword() != null)
			mailConfigurationDtlsDTO.setPassword(AESEncryptDecrypt.decrypt(mailConfigurationDtlsDTO.getPassword(), mailConfigurationDtlsDTO.getKey()));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
		BeanUtils.copyProperties(mailConfigurationDtlsDTO, configurationDtlsInfo);
		BeanUtils.copyProperties(configurationDtlsInfo, configurationDtls,new String[]{"id","module","siteId","key","createdDate"});
		return  configurationDtls;
	}
	
	public MailConfigurationDtls getEmailDtls(MailConfigurationDtls mail) {
		logger.info("******* The control inside of the getEmailDtls service in EmailService **********");
		MailConfigurationDtlsInfo configurationDtlsInfo = new MailConfigurationDtlsInfo();
		BeanUtils.copyProperties(mail, configurationDtlsInfo);
		return getEmailDtlsHelper(configurationDtlsInfo);
	}
	
	public MailConfigurationDtls getEmployeeEmailDtls(MailConfigurationDtls mail) {
		logger.info("******* The control inside of the getEmailDtls service in EmailService **********");
		MailConfigurationDtlsInfo configurationDtlsInfo = new MailConfigurationDtlsInfo();
		BeanUtils.copyProperties(mail, configurationDtlsInfo);
		return getEmployeeEmailDtlsHelper(configurationDtlsInfo);
	}
	
	public MailConfigurationDtls getEmployeeEmailDtlsHelper(MailConfigurationDtlsInfo email) {
		logger.info("******* The control inside of the getEmailDtls method  inside  SiteServiceImpl *******"+email);
		MailConfigurationDtlsInfo configurationDtlsInfo = new MailConfigurationDtlsInfo();
		MailConfigurationDtls configurationDtls = new MailConfigurationDtls();
		MailConfigurationDtlsDTO mailConfigurationDtlsDTO = employeeTicketDaoImpl.getEmployeeEmailDtls(email);
		try {
			if(mailConfigurationDtlsDTO.getPassword() != null && mailConfigurationDtlsDTO.getPassword() != null)
			mailConfigurationDtlsDTO.setPassword(AESEncryptDecrypt.decrypt(mailConfigurationDtlsDTO.getPassword(), mailConfigurationDtlsDTO.getKey()));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
		BeanUtils.copyProperties(mailConfigurationDtlsDTO, configurationDtlsInfo);
		BeanUtils.copyProperties(configurationDtlsInfo, configurationDtls,new String[]{"id","module","siteId","key","createdDate"});
		return  configurationDtls;
	}

}
