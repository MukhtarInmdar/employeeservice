package com.sumadhura.employeeservice.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.MessengerRequest;

import lombok.NonNull;

@Component("carouselUtils")
public class CarouselUtils {

	private final static Logger LOGGER = Logger.getLogger(CarouselUtils.class);	
	private final static String uri;
	private final static String messengerUri;
	private final static String documentUri;
	
	static{
		Properties prop = new ResponceCodesUtil().getApplicationProperties();
		uri = new ResponceCodesUtil().getApplicationProperties().getProperty("CAROUSEL_LOCAL_URL");
		messengerUri = new ResponceCodesUtil().getApplicationProperties().getProperty("MEESENGER_FILE_GET_PATH");
		documentUri = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
	}
	
	/*public List<String> findUrls(String basePath) {
		LOGGER.info("***** The control is inside the findUrls in CarouselUtils ******" + basePath);
		File baseDir = new File(basePath);
		LOGGER.info(baseDir.getAbsolutePath());
		if (baseDir.exists() && baseDir.isDirectory()) {
			//urlHelper(baseDir);
			//urlMapHelper(baseDir);
		}
		LOGGER.info("***** The  imageUrlsList is ******" + urlsList);
		return urlsList;
	}*/
	public Map<String,String> findMapUrls(String basePath) {
		
		LOGGER.info("***** The control is inside the findMapUrls in CarouselUtils ******" + basePath);
		 Map<String,String> map = new HashMap<String,String>();
		File baseDir = new File(basePath);
		LOGGER.info(baseDir.getAbsolutePath());
		if (baseDir.exists() && baseDir.isDirectory()) {
			 urlMapHelper(baseDir,map);
		}else if(baseDir.exists() && baseDir.isFile()){
			LOGGER.info("**** The file Name ****"+baseDir.getName());
			String path = baseDir.getAbsolutePath();
			String filePath = uri + path.substring(path.lastIndexOf("Tickets")
							+ "Tickets".length()).replaceAll(";", "/").replace('\\', '/');
			LOGGER.info("*** The filepath *****"+filePath);
			map.put(filePath, baseDir.getName());
		}
		LOGGER.info("***** The  imageUrlsList is ******" + map);
		return map;
	}
	
	/*private void urlHelper(File baseDir) {
		LOGGER.info("***** The control is inside the urlHelper in CarouselUtils ******" + baseDir);
		if (baseDir.exists() && baseDir.isDirectory()) {
			for (String image : baseDir.list()) {
				File file = new File(baseDir + File.separator + image);
				String path = file.getAbsolutePath();
				if (file.isDirectory()) {
					urlHelper(file);
				} else {
					LOGGER.info("**** The file Name ****"+file.getName());
					String filePath = uri + path.substring(path.lastIndexOf("Tickets")
									+ "Tickets".length()).replaceAll(";", "/").replace('\\', '/');
					LOGGER.info("*** The filepath *****"+filePath);
					urlsList.add(filePath);
				}
			}
		}
	}*/
	
	private void urlMapHelper(File baseDir, Map<String,String> map) {
		LOGGER.info("***** The control is inside the urlHelper in CarouselUtils ******" + baseDir);
	   
		if (baseDir.exists() && baseDir.isDirectory()) {
			for (String image : baseDir.list()) {
				File file = new File(baseDir + File.separator + image);
				if (file.isDirectory()) {
					urlMapHelper(file,map);
				} else {
					LOGGER.info("**** The file Name ****"+file.getName());
					String path = file.getAbsolutePath();
					String filePath = uri + path.substring(path.lastIndexOf("Tickets")
									+ "Tickets".length()).replaceAll(";", "/").replace('\\', '/');
					LOGGER.info("*** The filepath *****"+filePath);
					map.put(filePath, file.getName());
				}
			}
		}
	}
	
	public static  List<FileInfo> getWelcomeLetterFileInfosWRTlocation(@NonNull String Location,String type, MessengerRequest messengerRequest){
		LOGGER.info("***** Control inside the CarouselUtils.getWelcomeLetterFileInfosWRTlocation() *****"+Location);
		List<FileInfo> fileInfos = new ArrayList<>();
		if(Util.isNotEmptyObject(type) && type.equalsIgnoreCase("localserver")) {
		File baseDir = new File(Location);
		if (baseDir.exists() && baseDir.isDirectory()) {
			for (String image : baseDir.list()) {
				    FileInfo fileInfo = new FileInfo();
				    File file = new File(baseDir + File.separator + image);
					LOGGER.info("**** The file Name ****"+file.getName());
					String path = file.getAbsolutePath();
					String filePath = documentUri + messengerRequest.getSiteIds().get(0) + "/" + messengerRequest.getFlatBookingId() + "/"+image;
					//filePath = documentUri + path.substring(path.lastIndexOf("file") + "file".length()).replaceAll(";", "/").replace('\\', '/');
					LOGGER.info("*** The filepath *****"+filePath);
					fileInfo.setName(file.getName());
					fileInfo.setUrl(filePath);
					fileInfo.setExtension(FilenameUtils.getExtension(file.getName()));
					fileInfo.setFileType("localdrivelink");
					fileInfo.setFilePath(path);
					fileInfos.add(fileInfo);
			}
		  }
		}else if(Util.isNotEmptyObject(type) && type.equalsIgnoreCase("googledrive")){
			if(Util.isNotEmptyObject(Location)) {
			   for(String loc : Location.split(",")) {
				 Location.split(",");
			     FileInfo fileInfo = new FileInfo();
			     fileInfo.setName("googledrivefile");
				 fileInfo.setUrl(loc);
				 fileInfo.setExtension("N/A");
				 fileInfo.setFilePath(loc);
				 fileInfo.setFileType("googledrivelink");
				 fileInfos.add(fileInfo);
			   }	
			}
		}
		return fileInfos;
	
	}
	public static  List<FileInfo> getFileInfosWRTlocation(@NonNull String Location,String type){
		LOGGER.info("***** The control is inside the getFileInfosWRTlocation in CarouselUtils ******" + Location);
		List<FileInfo> fileInfos = new ArrayList<>();
		if(Util.isNotEmptyObject(type) && type.equalsIgnoreCase("localserver")) {
		File baseDir = new File(Location);
		if (baseDir.exists() && baseDir.isDirectory()) {
			for (String image : baseDir.list()) {
				    FileInfo fileInfo = new FileInfo();
				    File file = new File(baseDir + File.separator + image);
					LOGGER.info("**** The file Name ****"+file.getName());
					String path = file.getAbsolutePath();
					String filePath = messengerUri + path.substring(path.lastIndexOf("file") + "file".length()).replaceAll(";", "/").replace('\\', '/');
					LOGGER.info("*** The filepath *****"+filePath);
					fileInfo.setName(file.getName());
					fileInfo.setUrl(filePath);
					fileInfo.setExtension(FilenameUtils.getExtension(file.getName()));
					fileInfo.setFileType("localdrivelink");
					fileInfo.setFilePath(path);
					fileInfos.add(fileInfo);
			}
		  }
		}else if(Util.isNotEmptyObject(type) && type.equalsIgnoreCase("googledrive")){
			if(Util.isNotEmptyObject(Location)) {
			   for(String loc : Location.split(",")) {
				 Location.split(",");
			     FileInfo fileInfo = new FileInfo();
			     fileInfo.setName("googledrivefile");
				 fileInfo.setUrl(loc);
				 fileInfo.setExtension("N/A");
				 fileInfo.setFilePath(loc);
				 fileInfo.setFileType("googledrivelink");
				 fileInfos.add(fileInfo);
			   }	
			}
		}
		return fileInfos;
	}	
}
