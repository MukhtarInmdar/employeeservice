/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sumadhura.employeeservice.enums.AmenitiesInfraMaster;
import com.sumadhura.employeeservice.enums.AmenitiesInfraSiteWise;
import com.sumadhura.employeeservice.enums.Bhk;
import com.sumadhura.employeeservice.enums.Site;

/**
 * XlSheetReaderUtil class is used to read xl file.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
public class XlSheetReaderUtil {

	private final static Logger logger = Logger.getLogger(ResponceCodesUtil.class);

	private static Properties sqlQueries;

	/* static block to load properties file. */
	{
		final ResponceCodesUtil util = new ResponceCodesUtil();
		sqlQueries = util.getSqlQueries();
	}

	public boolean generateAmenitiesInfraMaster(XSSFSheet sheet) throws Exception {
		logger.info("**** The control is inside the generateAmenitiesInfraMaster *****");
		String result = null;
		int count = 14;
		Map<String,List<String>> map = new HashMap<>();
		/* writing qeries into the file  */
		return writeIntoFile(map);
	}
	
	
	public boolean generateAmenitiesInfraSiteWise(XSSFSheet sheet) throws Exception {
		logger.info("**** The control is inside the generateAmenitiesInfraSiteWise *****");
		String result = null;
		int count = 14;
		Long siteId = 0l;
		String siteName = "";
		Map<String,List<String>> map = new HashMap<>();
		
		/* writing qeries into the file  */
		return writeIntoFile(map);
	}
	
	public boolean generateAmenitiesInfraFlatWise(XSSFSheet sheet) throws IOException {
		logger.info("**** The control is inside the generateAmenitiesInfraFlatWise *****");
		String result = null;
		Long siteId = 0l;
		@SuppressWarnings("unused")
		String siteName = "";
		Map<String,List<String>> map = new HashMap<>();
		List<String> amenitiesInfraFlatWiseQueries = new ArrayList<String>();
		//StringBuilder amenitiesInfraFlatWiseQuery = new StringBuilder(sqlQueries.getProperty("AMINITITES_INFRA_FLATWISE_QUERY", "N/A"));
		
	
		/* writing qeries into the file  */
		return writeIntoFile(map);
	}
	public boolean generateFlatDetailsQueries(XSSFSheet sheet) throws IOException {
		logger.info("**** The control is inside the generateFlatDetailsQueries *****");
		Map<String,List<String>> map = new HashMap<>();
	
		
	
		
		/* writing qeries into the file  */
		return writeIntoFile(map);
	}
	
	public boolean generateCustBookinfoAndContactInfoQueries(XSSFSheet sheet) throws IOException {
		logger.info("**** The control is inside the generateCustBookinfoAndContactInfoQueries *****");
		
		 return true;
	}
	
	
	
	@SuppressWarnings("resource")
	public  XSSFSheet loadXlSheet(String location, int sheetNo) throws IOException {
		logger.info("**** The control is inside the loadXlSheet *****");
		XSSFSheet sheet = null;
		
			
		return sheet;
	}
	
	@SuppressWarnings({ "unused", "resource" })
	private boolean writeIntoFile(Map<String,List<String>> querieMap) throws IOException {
		logger.info("*** The control is inside the writeIntoFile ****");

		return true;
	}
	
	
	public static boolean isRowEmpty(Row row) {
	    
	    return true;
	}
	
	
	private static String printCellValue(Cell cell) {
		logger.info("*** The control is inside the printCellValue ****");

		String result = null;

		

		
		return result;
	}
	
	@SuppressWarnings("unused")
	private StringBuilder appendComma(StringBuilder query) {
		logger.info("**** The control is inside the appendComma *****");
		query.append(",");
		return query;
	}
	
	public  String readFileAsString(String fileName)throws Exception { 
		logger.info("**** The control is inside the readFileAsString ****");
	    String data = null; 
	    data = new String(Files.readAllBytes(Paths.get(fileName))); 
	    return data; 
    } 
	
	
	public void updateAmenitiesInfraMasterEnum(String enumId,String enumName) throws Exception {
		logger.info("**** The control is inside the updateAmenitiesInfraMasterEnum ****");
		StringBuilder prepare = new StringBuilder();
		List<String> enumData = getAmenitiesInfraMasterEnumString(sqlQueries.getProperty("AMINITITES_INFRA_MASTER_ENUM", "N/A"));
		String str1 = enumData.get(0);
		String str2 = enumData.get(1);
		
		try(PrintWriter writer = new PrintWriter(sqlQueries.getProperty("AMINITITES_INFRA_MASTER_ENUM", "N/A"))){
		writer.print(str1);
		prepare.append(removeSpaceAndCapitalize(enumName));
		prepare.append("(");
		prepare.append(enumId+"l");
		prepare.append(",");
		prepare.append("\""+enumName.toUpperCase()+"\"");
		prepare.append(")");
		prepare.append(";");
		
		writer.println(prepare);
		writer.println("//end");
		writer.print(str2);
		
		writer.flush();
		}
	}
	
	
	private void updateAmenitiesInfraSiteWiseEnum(Long id,Long amenitiesInfraId,Long siteId,String siteName,String amenitieName) throws Exception{
	logger.info("**** The control is inside the updateAmenitiesInfraMasterEnum ****");
	StringBuilder prepare = new StringBuilder();
	List<String> enumData = getAmenitiesInfraMasterEnumString(sqlQueries.getProperty("AMINITITES_INFRA_SITEWISE_ENUM", "N/A"));
	String str1 = enumData.get(0);
	String str2 = enumData.get(1);
	try(PrintWriter writer = new PrintWriter(sqlQueries.getProperty("AMINITITES_INFRA_SITEWISE_ENUM", "N/A"))){
		writer.print(str1);
		prepare.append(removeSpaceAndCapitalize(siteName+" "+amenitieName));
		prepare.append("(");
		prepare.append(id+"l");
		prepare.append(",");
		prepare.append(amenitiesInfraId+"l");
		prepare.append(",");
		prepare.append(siteId+"l");
		prepare.append(")");
		prepare.append(";");
		writer.println(prepare);
		writer.println("//end");
		writer.print(str2);
		writer.flush();
		}
	}
	
	private String removeSpaceAndCapitalize(String str) {
		logger.info("**** The control is inside the removeSpaceAndCapitalize ****");
		 /* regex to match any number of spaces */
		
		  return str;
	}
	
	public List<String> getAmenitiesInfraMasterEnumString(String fileName) throws Exception{
		logger.info("**** The control is inside the getAmenitiesInfraMasterEnumString ****");
		List<String> list = new ArrayList<String>();
		String data = "";
		 String[] array = data.split("end", 2);
	
		 return list;
	}
	
	private String replaceLastCharecters(String data) {
		logger.info("**** The control is inside the updateAmenitiesInfraMasterEnum ****");
		StringBuilder sb = new StringBuilder(data);
		 
	    return sb.toString();
	}
	
	public static int getcolumnIndex(XSSFSheet sheet,String columnName){
		
		Map<String, Integer> map = new HashMap<String,Integer>(); //Create map
		Row row = sheet.getRow(0); //Get first row
		
		return map.get(columnName.trim());
	}
	
	public static void main(String ... args) throws Exception {
		XlSheetReaderUtil util = new XlSheetReaderUtil();
		
		 //System.out.println(util.generateFlatDetailsQueries(util.loadXlSheet("acropolis_flat_master_data.xlsx",0)));
		
		
		// System.out.println(util.generateAmenitiesInfraMaster(util.loadXlSheet("acropolis_flat_master_data.xlsx",0)));
		
		
		//System.out.println(util.generateAmenitiesInfraFlatWise(util.loadXlSheet("acropolis_flat_master_data.xlsx",0)));
		
		 
	}
	
	
}
