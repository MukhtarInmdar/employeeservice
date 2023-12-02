package com.sumadhura.employeeservice.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;

public class Base64FileUtil {

	private static String directoryPath;
	public static String notificationDirectoryPath;
	public static String notifyImageGetPath;

	public static String notificationDirectoryFilePath;
	public static String notifyFileGetPath;
	
	public static  String messengerDirectoryPath;
	public static  String messengerFilePath;
	
	//public static String financialDirectoryFilePath;
	//public static String financialFileGetPath;
	
	private static Logger LOGGER = Logger.getLogger(Base64FileUtil.class);
	
	// instance block
	{
		directoryPath = new ResponceCodesUtil().getApplicationProperties().getProperty("TICKETING_DIRCTORY_PATH");
		notificationDirectoryPath = new ResponceCodesUtil().getApplicationProperties().getProperty("NOTIFICATION_DIRCTORY_PATH");
		notifyImageGetPath = new ResponceCodesUtil().getApplicationProperties().getProperty("NOTIFICATION_IMAGE_GET_PATH");
		notificationDirectoryFilePath= new ResponceCodesUtil().getApplicationProperties().getProperty("NOTIFICATION_DIRCTORY_FILE_PATH");
		notifyFileGetPath= new ResponceCodesUtil().getApplicationProperties().getProperty("NOTIFICATION_FILE_GET_PATH");
		
		messengerDirectoryPath = new ResponceCodesUtil().getApplicationProperties().getProperty("MEESENGER_DIRCTORY_FILE_PATH");
		messengerFilePath = new ResponceCodesUtil().getApplicationProperties().getProperty("MEESENGER_FILE_GET_PATH");
		//financialDirectoryFilePath = new ResponceCodesUtil().getApplicationProperties().getProperty("FINANCIAL_TRANSACTION_FILE_PATH");
		//financialFileGetPath =new ResponceCodesUtil().getApplicationProperties().getProperty("FINANCIAL_TRANSACTION_FILE_URL");
		
	}
	
	public  String store(List<FileInfo> fileInfoList, String path) {
		LOGGER.info("**** The control is inside the Base64FileUtil store method *****");
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String imagePath="";
		for (FileInfo fileInfo : fileInfoList) {
			File file = new File(dir, fileInfo.getName());
			if (!file.exists()) {
				try {
					imagePath=fileInfo.getName();
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				int count = 1;
				do {
					String fileName = fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf('.'));
					String extension = fileInfo.getName().substring((fileInfo.getName().lastIndexOf('.')));
					imagePath=fileName + "_" + count + extension;
					file = new File(dir, imagePath);
					//file = new File(dir, fileName + "_" + count + extension);
					count++;
				} while (file.exists());
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		/*	if (fileInfo.getName().endsWith("pdf") || fileInfo.getName().endsWith("xls") || fileInfo.getName().endsWith("xlsx")||fileInfo.getName().endsWith("png") || fileInfo.getName().endsWith("jpg")) { */
			/*	writeBase64StringToFile(file, fileInfo.getBase64().split(",")[1]); */
				//writeBase64StringToFile(file, fileInfo.getBase64());
				
				
				
				/*if(fileInfo.getBase64().split(",").length > 1) {
					writeBase64StringToFile(file, fileInfo.getBase64().split(",",2)[1]);
				}else {
				writeBase64StringToFile(file, fileInfo.getBase64());
				}*/
			
			if(fileInfo.getBase64().split(",").length > 1) {
				writeBase64StringToFile(file, fileInfo.getBase64().split(",")[1]);
			}else {
				writeBase64StringToFile(file, fileInfo.getBase64());
			}

		/*	}  */
		}
		 return imagePath;
	}

	public  String getStoredFileName(List<FileInfo> fileInfoList, String path) {
		LOGGER.info("**** The control is inside the Base64FileUtil store method *****");
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String imagePath="";
		for (FileInfo fileInfo : fileInfoList) {
			File file = new File(dir, fileInfo.getName());
			if (!file.exists()) {
				try {
					imagePath=fileInfo.getName();
					//file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				int count = 1;
				do {
					String fileName = fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf('.'));
					String extension = fileInfo.getName().substring((fileInfo.getName().lastIndexOf('.')));
					imagePath=fileName + "_" + count + extension;
					file = new File(dir, imagePath);
					//file = new File(dir, fileName + "_" + count + extension);
					count++;
				} while (file.exists());
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		/*	if (fileInfo.getName().endsWith("pdf") || fileInfo.getName().endsWith("xls") || fileInfo.getName().endsWith("xlsx")||fileInfo.getName().endsWith("png") || fileInfo.getName().endsWith("jpg")) { */
			/*	writeBase64StringToFile(file, fileInfo.getBase64().split(",")[1]); */
				//writeBase64StringToFile(file, fileInfo.getBase64());
				
				
				
				/*if(fileInfo.getBase64().split(",").length > 1) {
					writeBase64StringToFile(file, fileInfo.getBase64().split(",",2)[1]);
				}else {
				writeBase64StringToFile(file, fileInfo.getBase64());
				}*/
			
			/*if(fileInfo.getBase64().split(",").length > 1) {
				writeBase64StringToFile(file, fileInfo.getBase64().split(",")[1]);
			}else {
				writeBase64StringToFile(file, fileInfo.getBase64());
			}*/

		/*	}  */
		}
		 return imagePath;
	}

	
	
	public static List<FileInfo> load(long ticketId) {
		LOGGER.info("**** The control is inside the Base64FileUtil load method *****");

		final boolean IS_CHUNKED = true;

		List<FileInfo> fileInfos = new ArrayList<FileInfo>();

		File dir = new File(directoryPath + File.separator + ticketId);

		if (!dir.exists()) {
			LOGGER.info(
					"**** The the directory not exist that means the given ticket id does not have the files *****");
			return fileInfos;
		} else {
			String[] fileNames = dir.list();
			for (String fileName : fileNames) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(fileName);
				fileInfo.setBase64("data:image/jpeg;base64," + new String(encode(fileName, IS_CHUNKED, dir)));
				fileInfos.add(fileInfo);
			}
		}
		LOGGER.info("**** The list of FileInfo objects  for given ticket id *****" + ticketId + "****"
				+ "******size is ******" + fileInfos.size());
		return fileInfos;
	}

	/**
	 * This method converts the content of a source file into Base64 encoded data
	 * and saves that to a target file. If isChunked parameter is set to true, there
	 * is a hard wrap of the output encoded text.
	 */

	private static byte[] encode(String sourceFile, boolean isChunked, File dir) {
		byte[] base64EncodedData = null;
		try {
			base64EncodedData = Base64.encodeBase64(loadFileAsBytesArray(dir, sourceFile), isChunked);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return base64EncodedData;
	}

	public static byte[] encode(String sourceFile, boolean isChunked) {
		// logger.info("***** The source file in encode method *****"+sourceFile);
		byte[] base64EncodedData = null;
		try {
			base64EncodedData = Base64.encodeBase64(loadFileAsBytesArray(sourceFile), isChunked);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return base64EncodedData;
	}
	/**
	 * This method writes base64 content into a file.
	 * 
	 * @param targetFile
	 * @param content
	 */
	private static void writeBase64StringToFile(File targetFile, String content) {
		try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(targetFile))) {
			writer.write(Base64.decodeBase64(content.getBytes()));
			writer.flush();
			LOGGER.info("**** The file is saved successfully into the server successfully ****"+targetFile.length()+" bytes");
			if(targetFile.length()==0) {
				LOGGER.info("****Base64 Content is ****"+content);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			LOGGER.info("****Exception is occured while uploading file into Server****"+ex.getMessage()+" and "+ex.getStackTrace());
			LOGGER.info("****Base64 Content is ****"+content);
		}
	}
	/**
	 * This method loads a file from file system and returns the byte array of the
	 * content.
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private static byte[] loadFileAsBytesArray(File dir, String fileName) throws Exception {

		File file = new File(dir, fileName);
		int length = (int) file.length();
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[length];
		reader.read(bytes, 0, length);
		reader.close();
		return bytes;

	}

	private static byte[] loadFileAsBytesArray(String fileName) throws Exception {

		File file = new File(fileName);
		int length = (int) file.length();
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[length];
		reader.read(bytes, 0, length);
		reader.close();
		return bytes;

	}

	public static List<FileInfo> load(String directoryPath) {

		LOGGER.info("**** The control is inside the Base64FileUtil load method *****");

		final boolean IS_CHUNKED = true;

		List<FileInfo> fileInfos = new ArrayList<FileInfo>();

		File dir = new File(directoryPath);

		if (!dir.exists()) {
			LOGGER.info(
					"**** The the directory not exist that means the given ticket id does not have the files *****");
			return fileInfos;
		} else {
			String[] fileNames = dir.list();
			for (String fileName : fileNames) {

				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(fileName);
				fileInfo.setBase64("data:image/jpeg;base64," + new String(encode(fileName, IS_CHUNKED, dir)));
				fileInfos.add(fileInfo);
			}
			LOGGER.info("**** The list of FileInfo objects  for given ticket id ********" + "******size is ******"
					+ fileInfos.size());
			return fileInfos;
		}
	}

	public static FileInfo loadFileAsBase64(String directoryPath) throws Exception {
		LOGGER.info("**** The control is11 inside the Base64FileUtil load method *****");
		final boolean IS_CHUNKED = true;
		FileInfo fileInfo = new FileInfo();
		File file = new File(directoryPath);
		if (file.exists()) {
			LOGGER.info("Base64FileUtil.loadFileAsBase64()");
			fileInfo.setName(file.getName());
			fileInfo.setBase64("data:image/jpeg;base64," + new String(encode(directoryPath, IS_CHUNKED)));
			return fileInfo;
		} else {
			LOGGER.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			/*String[] fileNames = file.list();
			for (String fileName : fileNames) {
				fileInfo.setName(fileName);
				fileInfo.setBase64("data:image/jpeg;base64," + new String(encode(fileName, IS_CHUNKED, file)));
				return fileInfo;
			}
			LOGGER.info("Base64FileUtil.loadFileAsBase64()");
			return fileInfo;*/
		}
	}
	
	/*public static void main(String ...strings) {
		
		Base64FileUtil util = new Base64FileUtil();
		String base64 = "data:image/jpeg;base64,JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9MZW5ndGggNzc4L0ZpbHRlci9GbGF0ZURlY29kZT4+c3RyZWFtCnicjVZdc9owEHz3r7jHttO4kizbMk91Qz7ohJCA004fFawGd8Amxkkn/74SGEpkSWaAwWhv985a+Y5nD8NfD4F6Ta88gkC+wzAEhgjUwptJ7Mq79569b5kXRHI5gizfEuTXl0sMmED22/sAH7M/+2UEZ5ipiw/3L1Uj4PZl9SjqgQyloTFsyBsx0BUC8i6oN9f6pYFNtRKQ84bDQtTCGHZMxgnyo/h/BrLPcP8wydJsNLmF2cN4nE5/KdJZG64J3lRz3hRVacp2VDTP81psNsaa7hZVud+m3qIPq0e/E3rKXl1kWyfVh8D31lnpK97ajyFgEEuPs9XxVmSTgZKQ3KNISpEfYEP05XQyNsQrZXmwtrGH0u74Ky8hXfFaXuSFPUuHebWsHvkSzvmaz4vmzZwPUY2VSiPTH+lwBLPJZfYznV7Ii5sHZfPMKBExpklcjxoxX8C5LWkUE51RHBifr99yUfNHnkMmlrx84iU3q4SRppIwFiKUkJiZCVTfoYsVL5YDWKst9vl+i78+qWV/Xq1amfeHAfmhbAbSUxoyCBDx4wTkgdq1gR0WMLWocIyRj7GGU8p85sCVNiUH7cikLfE9NzJp23ClTSK3tsSd2jZcaWPk1pa4U9uGS+1APr8ubYW7tK240o6ZWztmbm0brrQjt5cKd2rbcKVN3V4q3Kltw5V24PZS4U5tG660cY+XuMdLG97ySeL2y4rv+cztiQF/PyTkUY6w1vZnon4t5gKGYjOvi/V+HGqNiuLtg9Jhj8qiKVQ/X/D6SWwMgyDE20eowxxXZbNYvsF5tWmMnZFSfQoMC0eJ8iH1iYEkm6+PkCGDWld9Qx8UxEaQIy02EZxlhUQOQgPJVlZ7G1gfRraqaIh2LUonOKtqbz5I9GllK2t3G91462bJzsX0yXaCf11Sj39BpBve41+XcIJ/XZLbv4Ceeqpa/7qEk/wLdNd7/OvE28oiiWqHnT+UVcOX1nvG+uGI3cV04lmPcwi5u88nayqkn6qeQ0IYNbcra4oD45AiOk7xDwoa8woKZW5kc3RyZWFtCmVuZG9iago1IDAgb2JqCjw8L1BhcmVudCA0IDAgUi9Db250ZW50cyAzIDAgUi9UeXBlL1BhZ2UvUmVzb3VyY2VzPDwvUHJvY1NldCBbL1BERiAvVGV4dCAvSW1hZ2VCIC9JbWFnZUMgL0ltYWdlSV0vRm9udDw8L0YxIDEgMCBSL0YyIDIgMCBSPj4+Pi9NZWRpYUJveFswIDAgNTk1IDg0Ml0+PgplbmRvYmoKMSAwIG9iago8PC9CYXNlRm9udC9IZWx2ZXRpY2EvVHlwZS9Gb250L0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZy9TdWJ0eXBlL1R5cGUxPj4KZW5kb2JqCjIgMCBvYmoKPDwvQmFzZUZvbnQvVGltZXMtQm9sZC9UeXBlL0ZvbnQvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHlwZTE+PgplbmRvYmoKNCAwIG9iago8PC9JVFhUKDUuMC42KS9UeXBlL1BhZ2VzL0NvdW50IDEvS2lkc1s1IDAgUl0+PgplbmRvYmoKNiAwIG9iago8PC9UeXBlL0NhdGFsb2cvUGFnZUxheW91dC9TaW5nbGVQYWdlL1BhZ2VNb2RlL1VzZUF0dGFjaG1lbnRzL1BhZ2VzIDQgMCBSPj4KZW5kb2JqCjcgMCBvYmoKPDwvQ3JlYXRvcihMYXJzIFZvZ2VsKS9Qcm9kdWNlcihpVGV4dCA1LjAuNiBcKGNcKSAxVDNYVCBCVkJBKS9UaXRsZShNeSBmaXJzdCBQREYpL0tleXdvcmRzKEphdmEsIFBERiwgaVRleHQpL1N1YmplY3QoVXNpbmcgaVRleHQpL01vZERhdGUoRDoyMDE5MDEzMTE1MzQwNSswNSczMCcpL0F1dGhvcihMYXJzIFZvZ2VsKS9DcmVhdGlvbkRhdGUoRDoyMDE5MDEzMTE1MzQwNSswNSczMCcpPj4KZW5kb2JqCnhyZWYKMCA4CjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMTAyNiAwMDAwMCBuIAowMDAwMDAxMTE0IDAwMDAwIG4gCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMTIwMyAwMDAwMCBuIAowMDAwMDAwODYwIDAwMDAwIG4gCjAwMDAwMDEyNjYgMDAwMDAgbiAKMDAwMDAwMTM1NyAwMDAwMCBuIAp0cmFpbGVyCjw8L1Jvb3QgNiAwIFIvSUQgWzxhZmRmMjc1NWFiMDMzNzY0ZWU5MWM4NTY2NDcyZjY5YT48OGU0MGVhMTE5YTE0MjViZjM1MDE2Zjg1ODgzMTE3ZGM+XS9JbmZvIDcgMCBSL1NpemUgOD4+CnN0YXJ0eHJlZgoxNTk0CiUlRU9GCg==";
		FileInfo fileInfo = new FileInfo();
		fileInfo.setBase64(base64);
		fileInfo.setName("venkat.pdf");
		fileInfo.setId(1);
		List<FileInfo> list = new ArrayList<FileInfo>();
		list.add(fileInfo);
		System.out.println(util.store(list,1));
		System.out.println("*******************************************");
		
	}*/
	public static String storeFileIntoDestination(File source, File destination){
		String destFileName = null;
		try {
			String destDir = destination.getParent();
			destFileName = destination.getName();
			String extension = null;
			String name = null;
			
			if(destination.exists()) {
				int count = 1;
				name = destination.getName().substring(0, destination.getName().lastIndexOf("."));
				extension = destination.getName().substring(destination.getName().lastIndexOf("."));
				do {
					destFileName=name + "_" + count + extension;
					destination = new File(destDir, destFileName);
					count++;
				} while (destination.exists());
			}
			FileInputStream inputStream = new FileInputStream(source);
			FileOutputStream outputStream = new FileOutputStream(destination);
			int i=0;
			while((i=inputStream.read())!=-1) {
				outputStream.write(i);
			}
			inputStream.close();
			outputStream.close();
			LOGGER.info("File Created Successfully!! "+destination.getPath());
		}catch(Exception ex) {
			destFileName = null;
			LOGGER.info("Error occured while copying file to destination folder!! "+destination.getPath()+" from Source folder!! "+source.getPath());
		}
		return destFileName;
	}

}























