/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.inject.Named;

import org.apache.log4j.Logger;

/**
 * FileUtil class provides support for file related issues.
 * 
 * @author Venkat_Koniki
 * @since 03.07.2020
 * @time 11:52AM
 */

@Named
public class FileUtil {
	
	private static final Logger LOGGER = Logger.getLogger(FileUtil.class);

	private static final int CONNECTION_TIMEOUT = 1000000;
	private static final int READ_TIMEOUT = 1000000;

	/* Loading secret File */
	public static File loadFile(String fileName) {
		LOGGER.info("*** The control is inside the loadSecretFile ****");
		return new File(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile());
	}

	public static void downloadFile() {
		LOGGER.info("*** The control is inside the downloadFile ****");
		try {
			URL url = new URL(" https://drive.google.com/uc?id=1J-PcYvo-RWekvxb-VEpgs1BnRZ_WNbQ5&export=download");
			System.out.println(FilenameUtils.getName(url.getPath()));
			System.out.println(FilenameUtils.getExtension(url.getPath()));
			File f = new File("C:\\Users\\admin\\Desktop", "venkat.jpeg");
			@SuppressWarnings({ "unused", "resource" })
			FileOutputStream fos = new FileOutputStream(f);
			FileUtils.copyURLToFile(url, f, CONNECTION_TIMEOUT, READ_TIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String copyFileFromUrl(String fileUrl,String destination,String text) throws IOException {
		Path path = null;
		URL url = null;
		try {
			url = new URL(fileUrl);
			path = Files.createTempFile(null, FilenameUtils.getName(url.getPath()));
			@SuppressWarnings({ "unused", "resource" })
			FileOutputStream fos = new FileOutputStream(path.toFile());
			FileUtils.copyURLToFile(url, path.toFile(), CONNECTION_TIMEOUT, READ_TIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedImage image = ImageIO.read(path.toFile());
		Font font = new Font("Calibri", Font.BOLD, 45);
		AttributedString attributedText = new AttributedString(text);
		attributedText.addAttribute(TextAttribute.FONT, font);
		attributedText.addAttribute(TextAttribute.FOREGROUND, Color.MAGENTA);

		Graphics g = image.getGraphics();
		FontMetrics metrics = g.getFontMetrics(font);
		int positionX = (image.getWidth() - metrics.stringWidth(text)) / 2;
		int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		g.drawString(attributedText.getIterator(), positionX, positionY);
		try {
			File dir = new File(destination);
			if(!dir.exists()){
				dir.mkdirs();
            } 
			File file = new File(destination,FilenameUtils.removeExtension(FilenameUtils.getName(url.getPath()))+GetCurrentTimeStamp().replace(":","_").replace(".","_")+"."+FilenameUtils.getExtension(url.getPath()));
			if (!file.exists()) {
                file.createNewFile();
            } 
			ImageIO.write(image, FilenameUtils.getExtension(url.getPath()),file);
			String messengerUri = new ResponceCodesUtil().getApplicationProperties().getProperty("HOLIDAY_SESSIONAL_CELEBRATION_FILE_GET_PATH");
			String absolutePath = file.getAbsolutePath();
			String filePath = messengerUri + absolutePath.substring(absolutePath.lastIndexOf("file") + "file".length()).replaceAll(";", "/").replace('\\', '/');
			LOGGER.info("*** The filepath *****"+filePath);
			return filePath;
		} catch (IOException e) {
			LOGGER.error(e.toString());
		}
		LOGGER.info("**** Image written completed ****");
		return "N/A";
	}
	
	 public static String GetCurrentTimeStamp() {
	        SimpleDateFormat sdfDate = new SimpleDateFormat(
	                "yyyy-MM-dd:HH:mm:ss");// dd/MM/yyyy
	        Date now = new Date();
	        String strDate = sdfDate.format(now);
	        return strDate;
	  }
	 
}
