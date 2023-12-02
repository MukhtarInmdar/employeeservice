/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


/**
 * ResponceCodesUtil class provides support for loading properties file.
 * 
 * @author Venkat_Koniki
 * @since 17.04.2019
 * @time 03:18PM
 */

@Component
public class ResponceCodesUtil {
	
	private Properties responceCodes = null;
	private final static Logger logger = Logger.getLogger(ResponceCodesUtil.class);

	/* read data from properties file. */
	public Properties getresponceCodes() {
		logger.info("***** The control is inside the getresponceCodes method in RegistrationServiceImpl ******");
		Properties responceCodes = new Properties();
		try {
			responceCodes.load(getClass().getClassLoader().getResourceAsStream("responcecodes.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responceCodes;
	}
	
	/* read data from properties file. */
	public Properties readPropertiesFromFile(String fileName) {
		logger.info("***** The control is inside the readPropertiesFromFile method in RegistrationServiceImpl ******");
		Properties fileProperties = new Properties();
		try {
			fileProperties.load(getClass().getClassLoader().getResourceAsStream(fileName+".properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileProperties;
	}
	
	public Properties getApplicationProperties() {
		logger.info("***** The control is inside the getApplicationProperties method in RegistrationServiceImpl ******");
		Properties applicationProperties = new Properties();
		try {
			applicationProperties.load(getClass().getClassLoader().getResourceAsStream("applicationname.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return applicationProperties;
	}
	
	/* Reading properties from Apache Tomcat server Config folder */
	public Properties getProperties() {
		logger.info("***** The control is inside the getresponceCodes method in RegistrationServiceImpl ******");
		if (Util.isNotEmptyObject(responceCodes)) {
			try {
				responceCodes = new Properties();
				responceCodes.load(new FileInputStream(new File(new File(System.getProperty("catalina.base"), "conf"), "applicationconfig.properties")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responceCodes;
	}
	
	/* Reading properties from sql queries  */
	public  Properties getSqlQueries() {
		logger.info("***** The control is inside the getSqlQueries method in ResponceCodesUtil ******");
		Properties sqlQueries = new Properties();
		try {
			sqlQueries.load(getClass().getClassLoader().getResourceAsStream("queries.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sqlQueries;
	}
	
	/* Reading applicationname properties */
	public String getApplicationNamePropeties(String key) throws FileNotFoundException, IOException {
		logger.info("***** The control is inside the getApplicationNamePropeties method in ResponceCodesUtil ******");
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("applicationname.properties"));
		return properties.getProperty(key);
	}
	
	public String getMessangerProperties(String key) throws FileNotFoundException, IOException {
		logger.info("***** The control is inside the getMessangerProperties method in RegistrationServiceImpl ******");
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("MessangerStaticContent.properties"));
		return properties.getProperty(key);
	}

}
