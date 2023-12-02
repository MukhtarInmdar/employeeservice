/**
 * 
 */
package com.sumadhura.employeeservice.aspect;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.service.MailService;


/**
 * This Annotation is responsible to Print Error Mail.
 * 
 * @author Venkat_Koniki
 * @since 08.04.2019
 * @time 07:06PM
 */

@Aspect
@Component
public class ErrorMailAspect {

	@Autowired(required=true)
	private MailService mailServiceImpl;

	private final static Logger logger = Logger.getLogger(ErrorMailAspect.class);
	
	 @After("execution(* com.sumadhura.employeeservice.exception.provider.*.*(..))")
	// @After("@annotation(com.sumadhura.employeeservice.annotations.ErrorMail)")
	public void errorMail(JoinPoint joinPoint) throws IllegalAccessException, InvocationTargetException {
		logger.info("*** The Control is inside the errorMail in ErrorMailAspect ****");
		logger.info("**** The  afterCreateSession method is executed for the method *****"+ joinPoint.getSignature().getName());
		Object[] arguments = joinPoint.getArgs();
		for (Object obj : arguments) {
			logger.info("**** The arguments passes for this method is " + joinPoint.getSignature().getName()+ " ********" + obj);
		    if(obj instanceof Throwable) {
		    	errorMail((Throwable)obj);
		    }
		}
	}
	  
	private void errorMail(Throwable exception) throws IllegalAccessException, InvocationTargetException {
		logger.info("*** The Control is inside the errorMail in CustomGlobalExceptionHandler ****");
		final Exception exceptionObj = new Exception();
		BeanUtils.copyProperties(exceptionObj,(Exception) exception);
		Thread thread = new Thread() {
	        public synchronized void run() {
	          logger.debug("**** The control is inside upload ticket documents into the server *****");
	          mailServiceImpl.sendErrorMail(exceptionObj);
	        }
	    };
	    thread.start();
	}
}














