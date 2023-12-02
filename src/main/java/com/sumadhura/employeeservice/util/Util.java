package com.sumadhura.employeeservice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.util.StringUtils;

/**
 * IsEmptyUtil class provides utility specific functionality.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
public class Util {
	
	private final static Logger LOGGER = Logger.getLogger(Util.class);
	
	public static boolean isEmptyObject(Object object) {
		
		//LOGGER.info("**** The control is inside the isEmptyObject in IsEmptyUtil*****");

		if (object == null) {
			return true;
		} else if (object != null && object instanceof String
				&& StringUtils.isEmpty(object)) {
			return true;
		} else if (object != null && object instanceof Long
				&& ((Long) object) == 0) {
			return true;
		} else if (object != null && object instanceof Integer
				&& ((Integer) object) == 0) {
			return true;
		} else if (object != null && object instanceof Byte
				&& ((Byte) object) == 0) {
			return true;
		} else if (object != null && object instanceof Collection<?>
				&& ((Collection<?>) object).isEmpty()) {
			return true;
		}else if (object != null && object instanceof Map<?,?>
		&& ((Map<?,?>) object).isEmpty()) {
			 return true;
        }else if(org.springframework.util.ObjectUtils.isArray(object) && Arrays.asList(object).isEmpty()) {
        	 return true;
        }
		else {
			return false;
		}
	}

	public static boolean isNotEmptyObject(Object object) {
		return !(isEmptyObject(object));
	}
	
	public static boolean checkNull(Object obj,List<String> fieldNames) throws IllegalAccessException {
		LOGGER.info("**** The control is inside the checkNull in Util*****");
	    for (Field f :obj.getClass().getDeclaredFields()) {
	    	f.setAccessible(true);
	        if ((fieldNames.contains(f.getName())) && (f.get(obj) == null)) {
	        	 return true;
	        }
	    } 
	    return false;           
	}
	
	public static boolean checkNotNull(Object obj, List<String> fieldNames) throws IllegalAccessException {
		LOGGER.info("**** The control is inside the checkNotNull in Util*****");
		return !checkNull(obj, fieldNames);
	}

	public static int generateOTP() {
		int randomNumber = 100000 + new Random().nextInt(80000);
		return randomNumber;
	}
	
	public static String getDate(String pattern,Date date) {
		 LOGGER.info("**** The control is inside the getDate in Util*****");
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		 String result = simpleDateFormat.format(new Date());
		 LOGGER.info("**** The output result is *****"+result);
		 return result;
	}
	
	public static String html2text(String html) {
		 LOGGER.info("**** The control is inside the html2text in Util*****");
	    return Jsoup.parse(html).text();
	}
	
	public static String htmlTotext(String html) throws IOException {
		LOGGER.info("**** The control is inside the html2text in Util*****");
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new StringReader(html)); // creates a buffering character input stream

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(html2text(line)); // appends line to string buffer
			sb.append("\n"); // line feed
		}
		br.close();
		return sb.toString();
	}
	
	public static String roundOff(String value) {
		LOGGER.info("**** The control is inside the roundOff in Util*****");
		return value.replaceAll("\\.0*$", "");
		/*
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(0);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		String rounded = nf.format(value);
		return rounded;
		*/
	}
	
	public static String replaceSpecialCharacters(String value) {
		return value.replaceAll("[^a-zA-Z0-9.]", " ");
	}
	
	public static boolean containsCaseInsensitive(String s, List<String> list) {
		for (String string : list) {
			if (string.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static String generateRandomNumber() {
		LOGGER.info("**** The control is inside the generateRandomNumber in Util*****");
		return String.valueOf(UUID.randomUUID());
	}
	
	public static Long get(Set<Entry<Long,Long>> set,String flag) {
		LOGGER.info("**** The control is inside the getKey in Util*****");
		Long result = null;
		for(Entry<Long,Long> entry : set) {
			if(flag.equalsIgnoreCase("key")) {
				result = entry.getKey();
			}else if(flag.equalsIgnoreCase("value")) {
				result = entry.getValue();
			}
		}
		return result;
	}
	
	public static void main(String ... args) throws IOException {
		
		//html2text("<html><body>Hii</body></html>");
		
		System.out.println(html2text("<html><body>Hii<br/>Hello</body></html>"));
		System.out.println(htmlTotext("<html><body>Hii<br/>\nHello</body></html>"));
		System.out.println(roundOff("700000.0"));
	}
		public static String replaceHtmlTags(String message) {
		LOGGER.info("*** The control is inside the eliminateHtmlTags in Util ***");
		String descriptionWithOutTags = message.replaceAll("\\<.*?\\>", "");
		descriptionWithOutTags = descriptionWithOutTags.replaceAll("&nbsp;", " ");
		descriptionWithOutTags = descriptionWithOutTags.replaceAll("\n", " ");
		descriptionWithOutTags = descriptionWithOutTags.replaceAll("\t", " ");
		descriptionWithOutTags = descriptionWithOutTags.replaceAll("\r", " ");
		descriptionWithOutTags = descriptionWithOutTags.replaceAll("( )+", " ");
		return descriptionWithOutTags;
	}
}
