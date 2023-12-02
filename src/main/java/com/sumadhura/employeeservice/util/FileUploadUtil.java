/**
 * 
 */
package com.sumadhura.employeeservice.util;

/**
 * FileUploadUtil class provides FileUploading specific functionality.
 * 
 * @author Venkat_Koniki
 * @since 04.14.2019
 * @time 06:50PM
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
 
import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
 
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;


public class FileUploadUtil {
	
	  public static final String UPLOAD_FILE_SERVER = "D:\\Demo\\upload\\";
	  private static Logger LOGGER = Logger.getLogger(FileUploadUtil.class);

	public Response uploadFile(List<Attachment> attachments, HttpServletRequest request) {
    
		LOGGER.info("**** The control is insdie the uploadFile in FileUploadUtil *****");
		InputStream inputStream = null;
		for (Attachment attachment : attachments) {
			DataHandler dataHandler = attachment.getDataHandler();
			try {
				// get filename to be uploaded
				MultivaluedMap<String, String> multivaluedMap = attachment.getHeaders();
				String fileName = getFileName(multivaluedMap);

				// write & upload file to server
				 inputStream = dataHandler.getInputStream();
				writeToFileServer(inputStream, fileName);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Response.ok("upload success").build();
	}

	/**
	 * @param inputStream
	 * @param fileName
	 */
	private void writeToFileServer(InputStream inputStream, String fileName) {

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(UPLOAD_FILE_SERVER + fileName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param multivaluedMap
	 * @return
	 */
	private String getFileName(MultivaluedMap<String, String> multivaluedMap) {

		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "unknownFile";
	}

}
