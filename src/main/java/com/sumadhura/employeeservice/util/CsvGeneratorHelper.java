/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibm.icu.text.NumberFormat;
import com.itextpdf.text.DocumentException;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.EmployeeFinancialResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinancialProjectMileStoneResponse;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyStatisticsPojo;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;


/**
 * PdfHelper class provides Support for to generate Pdf.
 * 
 * @author Aniket Chavan
 * @since 01-03-2020
 * @time 07:30PM
 */

@Component("CsvHelper")
public class CsvGeneratorHelper {
	
	private final static Logger log = Logger.getLogger(CsvGeneratorHelper.class);
	
	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
/*	@Autowired(required = true)
	private Velocity velo; */
	
	@Autowired
	private ResponceCodesUtil responceCodesUtil;

	public FileInfo CSVWorkerHelper(Email email, String requestFrom, EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
		FileInfo fileInfo = new FileInfo();
		String demandNotePdfFilePath = ""; 
		String demandNotePdfFileUrl = "";
		boolean isGenerateDemandNote = requestFrom.equalsIgnoreCase("GenerateDemandNote");
		DemandNoteGeneratorInfo demandNoteGeneratorInfo = email.getDemandNoteGeneratorInfoList().get(0);
		long portNumber = employeeFinancialServiceInfo.getPortNumber();

		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
	    }else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
	    }
		
		StringBuilder filePath = new StringBuilder(demandNotePdfFilePath)
				.append("\\").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"\\"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"\\"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		StringBuilder fileLocation = new StringBuilder(demandNotePdfFileUrl)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		
		String fileName = getFileName(filePath.toString(), demandNoteGeneratorInfo.getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("\\").append(fileName);
		
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		 
		email.setTemplateName("/demandnotes/financialCustomerDetailsCSV.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        model.put("DELIMETER", ",");
   
        final String data = mailServiceImpl.geVelocityTemplateContent(model);
        org.jsoup.nodes.Document htmlData = Jsoup.parse(data);
        
        //InputStream iop = new FileInputStream(new File(pdfFileName.toString()));
        //FileInputStream fis = new FileInputStream(pdfFileName.toString());
        HSSFWorkbook wb = new HSSFWorkbook();
        //XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(pdfFileName.toString()));
        // create excel sheet for page 1
        HSSFSheet sheet = wb.createSheet("Financial Details");

        //Set Header Font
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setFontName("Calibri");
        
      //Set Header Style
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        
        int rowCount = 0;
        Row header;
        
        /* Display list of headers for

        tag here i tried to fetch data with class = tableData in table tag
        you can fetch using id or other attribute
        rowCount variable to create row for excel sheet
        */
        HSSFCellStyle style = wb.createCellStyle();
		//style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		
		Cell cell;
		for (Element table : htmlData.select("table[class=tableData]")) {
			rowCount++;
			// loop through all tr of table
			for (Element row : table.select("tr")) {
				// create row for each tag
				header = sheet.createRow(rowCount);
				// loop through all tag of tag
				Elements ths = row.select("th");
				int count = 0;
				for (Element element : ths) {
					// set header style
					cell = header.createCell(count);
					cell.setCellValue(element.text());
					cell.setCellStyle(headerStyle);
					count++;
				}
				// now loop through all td tag
				Elements tds = row.select("td:not([rowspan])");
				count = 0;
				for (Element element : tds) {
					// create cell for each tag
					String text = element.text();
					Double converted = 0d;
					cell = header.createCell(count);
					cell.setCellValue(text);
					if(text.contains(",") && text.contains(".")) {
						converted = NumberFormat.getNumberInstance(java.util.Locale.US).parse(text).doubleValue();
						//cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(converted);
						cell.setCellStyle(style);						
					} else if( text.contains(".")  && text.split("\\.")[1].length()==2 ) {
						try {
							converted = NumberFormat.getNumberInstance(java.util.Locale.US).parse(text).doubleValue();
							//cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell.setCellValue(converted);
							cell.setCellStyle(style);
						}catch(Exception ex) {}
					}
					count++;
				}
				rowCount++;
				// set auto size column for excel sheet
				sheet = wb.getSheetAt(0);
				for (int j = 0; j < row.select("th").size(); j++) {
					sheet.autoSizeColumn(j);
				}
			}
	}       

		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		FileOutputStream outputStream = new FileOutputStream(new File(pdfFileName.toString()));
		outputStream.write(outArray);
		outputStream.flush();
		outputStream.close();
		wb.close();

		return fileInfo;
	}

	public FileInfo InterestBreakUpWorkerHelper(Email email, String requestFrom, EmployeeFinancialServiceInfo employeeFinancialServiceInfo, List<EmployeeFinancialResponse>
		employeeFinancialResponseList) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
		FileInfo fileInfo = new FileInfo();
		String demandNotePdfFilePath = ""; 
		String demandNotePdfFileUrl = "";
		boolean isGenerateDemandNote = requestFrom.equalsIgnoreCase("GenerateDemandNote");
		DemandNoteGeneratorInfo demandNoteGeneratorInfo = email.getDemandNoteGeneratorInfoList().get(0);
		long portNumber = employeeFinancialServiceInfo.getPortNumber();

		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
	    } else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
	 		demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
	    }
		
		StringBuilder filePath = new StringBuilder(demandNotePdfFilePath)
				.append("\\").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"\\"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"\\"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		StringBuilder fileLocation = new StringBuilder(demandNotePdfFileUrl)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		
		String fileName = getFileName(filePath.toString(), demandNoteGeneratorInfo.getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("\\").append(fileName);
		
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		 
		email.setTemplateName("/demandnotes/financialCustomerDetailsCSV.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        model.put("DELIMETER", ",");
        System.out.println(pdfFileName+"\n");
        //final String data = mailServiceImpl.geVelocityTemplateContent(model);
        org.jsoup.nodes.Document htmlData = Jsoup.parse("");
        
        //InputStream iop = new FileInputStream(new File(pdfFileName.toString()));
        //FileInputStream fis = new FileInputStream(pdfFileName.toString());
        HSSFWorkbook wb = new HSSFWorkbook();
        //XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(pdfFileName.toString()));
        // create excel sheet for page 1
        HSSFSheet sheet = wb.createSheet("Interest data");

        //Set Header Font
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setFontName("Calibri");
        
      //Set Header Style
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        
        HSSFCellStyle style = wb.createCellStyle();
		//style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		style.setFont(headerFont);
        
        int rowCount = 0;
        Row header;
        
        /* Display list of headers for

        tag here i tried to fetch data with class = tableData in table tag
        you can fetch using id or other attribute
        rowCount variable to create row for excel sheet
        */
        /*HSSFCellStyle style = wb.createCellStyle();
		//style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));*/
		
		Cell cell;
		List<String> headingName = getHeaderNames();
		header = sheet.createRow(rowCount);
		// loop through all tag of tag
		int count = 0;
		for (String element : headingName) {
			// set header style
			cell = header.createCell(count);
			cell.setCellValue(element);
			cell.setCellStyle(headerStyle);
			count++;
		}
		rowCount++;
		for (EmployeeFinancialResponse resp : employeeFinancialResponseList) {
			if (Util.isNotEmptyObject(resp.getFinancialProjectMileStoneResponse()) && Util.isNotEmptyObject(resp.getFinancialProjectMileStoneResponse().get(0).getPenaltyStatisticsPojos())) {
				for (FinancialProjectMileStoneResponse mileStoneResponse : resp.getFinancialProjectMileStoneResponse()) {
					rowCount++;
					double lastPaidAmount = 0d;
					long iterationCompleted = 0l;

					if(mileStoneResponse.getPenaltyStatisticsPojos()!=null) {
							
					for (FinPenaltyStatisticsPojo penaltyStatisticsPojo : mileStoneResponse.getPenaltyStatisticsPojos()) {
						int cellIndex = 0;
						if(iterationCompleted==0) {
							if(penaltyStatisticsPojo.getInterestCalculationOnAmount() !=null
									&& mileStoneResponse.getMileStoneBasicAmount()!=null 
									&& penaltyStatisticsPojo.getInterestCalculationOnAmount().intValue() != mileStoneResponse.getMileStoneBasicAmount().intValue() ) {
								lastPaidAmount =  mileStoneResponse.getMileStoneBasicAmount();
							}else {
								lastPaidAmount = penaltyStatisticsPojo.getInterestCalculationOnAmount();
							}
						}
						header = sheet.createRow(rowCount);
						cell = header.createCell(cellIndex++);
						cell.setCellValue(mileStoneResponse.getMileStoneNo());
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(mileStoneResponse.getMilestoneName());
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(TimeUtil.timestampToDD_MM_YYYY(mileStoneResponse.getDemandNoteDate()));
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(TimeUtil.timestampToDD_MM_YYYY(mileStoneResponse.getMileStoneDueDate()));
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(mileStoneResponse.getMileStoneBasicAmount());
						cell.setCellStyle(style);
						
						double currentPaid = penaltyStatisticsPojo.getInterestCalculationOnAmount(); 
						double currPayAmt = lastPaidAmount- currentPaid;
						cell = header.createCell(cellIndex++);
						//cell.setCellValue(penaltyStatisticsPojo.getMileStonePaidBasicAmount()==null?0d:penaltyStatisticsPojo.getMileStonePaidBasicAmount());
						if(lastPaidAmount==currentPaid) {
							
						}
						cell.setCellValue(Math.abs(currPayAmt));
						cell.setCellStyle(style);
						
						cell = header.createCell(cellIndex++);
						if(penaltyStatisticsPojo.getMileStonePaidDate()!=null) {
							cell.setCellValue(TimeUtil.timestampToDD_MM_YYYY(penaltyStatisticsPojo.getMileStonePaidDate()));
						}else {
							cell.setCellValue("");
						}
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(mileStoneResponse.getMileStoneBasicAmount()-penaltyStatisticsPojo.getInterestCalculationOnAmount());
						cell.setCellStyle(style);
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(penaltyStatisticsPojo.getInterestCalculationOnAmount());
						cell.setCellStyle(style);
						
						lastPaidAmount = penaltyStatisticsPojo.getInterestCalculationOnAmount();
						
						cell = header.createCell(cellIndex++);
						if(penaltyStatisticsPojo.getStartDate()!=null) {
							cell.setCellValue(TimeUtil.timestampToDD_MM_YYYY(penaltyStatisticsPojo.getStartDate()));
						}else {
							cell.setCellValue("");
						}	
						
						cell = header.createCell(cellIndex++);
						if(penaltyStatisticsPojo.getStartDate()!=null) {
							cell.setCellValue(TimeUtil.timestampToDD_MM_YYYY(penaltyStatisticsPojo.getEndDate()));
						}else {
							cell.setCellValue("");
						}
						cell = header.createCell(cellIndex++);
						cell.setCellValue( penaltyStatisticsPojo.getDaysDifference()==null?0l:penaltyStatisticsPojo.getDaysDifference());
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue( penaltyStatisticsPojo.getPenaltyAmount()==null?0d: penaltyStatisticsPojo.getPenaltyAmount());
						cell.setCellStyle(style);
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue( penaltyStatisticsPojo.getPenaltyTaxAmount()==null?0d: penaltyStatisticsPojo.getPenaltyTaxAmount());
						cell.setCellStyle(style);
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue( penaltyStatisticsPojo.getPenaltyAmount()+penaltyStatisticsPojo.getPenaltyTaxAmount());
						cell.setCellStyle(style);
						
						cell = header.createCell(cellIndex++);
						cell.setCellValue(penaltyStatisticsPojo.getInterestPercentage()==null?0d:penaltyStatisticsPojo.getInterestPercentage());
						
						
						count++;
						rowCount++;
						iterationCompleted++;
					}

				}
				}
			}
		}
		//putDataIntoCellData(employeeFinancialResponseList);
		
		for (Element table : htmlData.select("table[class=tableData]")) {
			rowCount++;
			// loop through all tr of table
			for (Element row : table.select("tr")) {
				// create row for each tag
				header = sheet.createRow(rowCount);
				// loop through all tag of tag
				Elements ths = row.select("th");
				count = 0;
				for (Element element : ths) {
					// set header style
					cell = header.createCell(count);
					cell.setCellValue(element.text());
					cell.setCellStyle(headerStyle);
					count++;
				}
				// now loop through all td tag
				Elements tds = row.select("td:not([rowspan])");
				count = 0;
				for (Element element : tds) {
					// create cell for each tag
					String text = element.text();
					Double converted = 0d;
					cell = header.createCell(count);
					cell.setCellValue(text);
					if(text.contains(",") && text.contains(".")) {
						converted = NumberFormat.getNumberInstance(java.util.Locale.US).parse(text).doubleValue();
						//cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(converted);
						cell.setCellStyle(style);						
					}
					count++;
				}
				rowCount++;
				// set auto size column for excel sheet
/*				sheet = wb.getSheetAt(0);
				for (int j = 0; j < row.select("th").size(); j++) {
					sheet.autoSizeColumn(j);
				}*/
			}
	}       

		sheet = wb.getSheetAt(0);
		for (int j = 0; j < headingName.size()+1; j++) {
			sheet.autoSizeColumn(j);
		}
		
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		FileOutputStream outputStream = new FileOutputStream(new File(pdfFileName.toString()));
		outputStream.write(outArray);
		outputStream.flush();
		outputStream.close();
		wb.close();

		return fileInfo;
	}

	/*private void putDataIntoCellData(List<EmployeeFinancialResponse> employeeFinancialResponseList) {
		List<String> cellData = new ArrayList<>();
		for (EmployeeFinancialResponse resp : employeeFinancialResponseList) {
			if (Util.isNotEmptyObject(resp.getFinancialProjectMileStoneResponse()) && Util.isNotEmptyObject(resp.getFinancialProjectMileStoneResponse().get(0).getPenaltyStatisticsPojos())
					&& Util.isNotEmptyObject(resp.getFlatsResponse())) {
				for (FinancialProjectMileStoneResponse mileStoneResponse : resp.getFinancialProjectMileStoneResponse()) { 
					
				}
			}
		}
	}*/

	private List<String> getHeaderNames() {
		
		List<String> heading = new ArrayList<>();
		heading.add("MILE_STONE_NO");	
		heading.add("MILESTONE_NAME");	
		heading.add("MS_DMD_NOTE_DATE");	
		heading.add("DUE_DATE");	
		heading.add("BASIC_AMOUNT");
		heading.add("PAID_AMOUNT");
		heading.add("PAID_DATE");
		heading.add("PAID_BASIC_AMOUNT");	
		heading.add("INTEREST_CAL_ON_AMOUNT");	
		heading.add("START_DATE");
		heading.add("END_DATE");	
		heading.add("DAYS_DIFF");	
		heading.add("INTEREST_AMOUNT");	
		heading.add("GST_AMOUNT_ON_INTEREST_AMOUNT");	
		heading.add("TOTAL_AMOUNT");	
		heading.add("PERCENTAGE");
		
		return heading;
	}

	public FileInfo ClosingBalanceReportWorkerHelper(Email email, String requestFrom, EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
		FileInfo fileInfo = new FileInfo();
		String demandNotePdfFilePath = ""; 
		String demandNotePdfFileUrl = "";
		//boolean isGenerateDemandNote = requestFrom.equalsIgnoreCase("GenerateDemandNote");
		long portNumber = employeeFinancialServiceInfo.getPortNumber();

		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_CLOSING_BALANCE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_CLOSING_BALANCE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_CLOSING_BALANCE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_CLOSING_BALANCE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_CLOSING_BALANCE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_CLOSING_BALANCE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_CLOSING_BALANCE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_CLOSING_BALANCE_PDF_URL");
	    } else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_CLOSING_BALANCE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_CLOSING_BALANCE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_CLOSING_BALANCE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_CLOSING_BALANCE_PDF_URL");
	    }
		
		StringBuilder filePath = new StringBuilder(demandNotePdfFilePath)
				.append("\\").append(employeeFinancialServiceInfo.getSiteId());
		
		StringBuilder fileLocation = new StringBuilder(demandNotePdfFileUrl)
				.append("/").append(employeeFinancialServiceInfo.getSiteId());		

		File f = new File(filePath.toString());
		if(f.exists()) {//delete file before creating the current file
			boolean delete = f.delete();
			System.out.println(delete);
			deleteFolder(f);
		}
		
		String fileName = getFileName(filePath.toString(), email.getName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("\\").append(fileName);
			
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		 
		email.setTemplateName("/demandnotes/ClosingBalance.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        model.put("DELIMETER", ",");
   
        final String data = mailServiceImpl.geVelocityTemplateContent(model);
        System.out.println("***** Control inside the CsvGeneratorHelper.ClosingBalanceReportWorkerHelper() *****\n"+data);
        org.jsoup.nodes.Document htmlData = Jsoup.parse(data);
        
        //InputStream iop = new FileInputStream(new File(pdfFileName.toString()));
        //FileInputStream fis = new FileInputStream(pdfFileName.toString());
        HSSFWorkbook wb = new HSSFWorkbook();
        //XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(pdfFileName.toString()));
        // create excel sheet for page 1
        HSSFSheet sheet = wb.createSheet("Closing Balance Report");

        //Set Header Font
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setFontName("Calibri");
        
        HSSFFont cellDataFont = wb.createFont();
        cellDataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellDataFont.setFontHeightInPoints((short) 12);
        cellDataFont.setFontName("Calibri");
        
        //Set Header Style
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setAlignment(CellStyle.ALIGN_LEFT);
        headerStyle.setFont(headerFont);
        //headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        //headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setFont(cellDataFont);        
        
        int rowCount = 0;
        Row header;
        int columnsInSheet = 15;
        /* Display list of headers for

        tag here i tried to fetch data with class = tableData in table tag
        you can fetch using id or other attribute
        rowCount variable to create row for excel sheet
        */
        HSSFCellStyle style = wb.createCellStyle();
		//style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		style.setFont(cellDataFont);
		
		Cell cell;
		for (Element table : htmlData.select("table[class=tableData]")) {
			//rowCount++;
			// loop through all tr of table
			for (Element row : table.select("tr")) {
				// create row for each tag
				header = sheet.createRow(rowCount);
				// loop through all tag of tag
				Elements ths = row.select("th");
				int count = 0;
				for (Element element : ths) {
					// set header style
					cell = header.createCell(count);
					cell.setCellValue(element.text());
					cell.setCellStyle(headerStyle);
					count++;
				}
				// now loop through all td tag
				Elements tds = row.select("td:not([rowspan])");
				count = 0;
				for (Element element : tds) {
					// create cell for each tag
					String text = element.text();
					Double converted = 0d;
					cell = header.createCell(count);
					cell.setCellValue(text);
					cell.setCellStyle(cellStyle);
					if(text.contains(",") && text.contains(".") && text.split("\\.")[1].length()==2 ) {
						/*if(text.contains("51,74,376.48")) {
							log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
						}*/
						converted = NumberFormat.getNumberInstance(java.util.Locale.US).parse(text).doubleValue();
						//cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(converted);
						cell.setCellStyle(style);						
					} else if( text.contains(".")  && text.split("\\.")[1].length()==2 ) {
						try {
							converted = NumberFormat.getNumberInstance(java.util.Locale.US).parse(text).doubleValue();
							//cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell.setCellValue(converted);
							cell.setCellStyle(style);
						}catch(Exception ex) {}
					}
					count++;
				}
				rowCount++;
				//columnsInSheet = row.select("th").size();
				// set auto size column for excel sheet
				/*sheet = wb.getSheetAt(0);
				for (int j = 0; j < row.select("th").size(); j++) {
					sheet.autoSizeColumn(j);
				}*/
			}
	}       
		
		sheet = wb.getSheetAt(0);
		for (int j = 0; j < columnsInSheet; j++) {
			sheet.autoSizeColumn(j);
		}
		
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		FileOutputStream outputStream = new FileOutputStream(new File(pdfFileName.toString()));
		outputStream.write(outArray);
		outputStream.flush();
		outputStream.close();
		wb.close();

		return fileInfo;
	}

	static void deleteFolder(File file) {
		if(file.listFiles()!=null)
		for (File subFile : file.listFiles()) {
			if (subFile.isDirectory()) {
				deleteFolder(subFile);
			} else {
				subFile.delete();
			}
		}
		file.delete();
	}
	
	public String getFileName(String path, String currentFileName) {
		/* Creating Folders */
		File filePath = new File(path);
		if(!filePath.exists()) {
			filePath.mkdirs();
			
		}
		/* Creating File Name */		
		File file = new File(path, currentFileName);
		if (!file.exists()) {
			return currentFileName;
		} else {
			int count = 1;
			String newFileName;
			do {
				String fileName = currentFileName.substring(0, currentFileName.lastIndexOf('.'));
				String extension = currentFileName.substring(currentFileName.lastIndexOf('.'));
				newFileName=fileName + "_" + count + extension;
				file = new File(path, newFileName);
				count++;
			} while (file.exists());
			return newFileName;
		}
		
	}

	   public static void main(String[] args) throws Exception
	    {
	        String excelfileName1="D:\\Logs\\files\\Sumadhura (7).xls";
	        String csvFileName1="D:\\Logs\\files\\csv-file1.csv";
	        String excelfileName2="D:\\Logs\\files\\excel-file2.xls";
	        String csvFileName2="D:\\Logs\\files\\Sumadhura.csv";
	        excelToCSV(excelfileName1,csvFileName1);
	        
	        csvToEXCEL(csvFileName2,excelfileName2);
	        
	        System.out.println("CsvGeneratorHelper.main()");
	    }
	   
	    private static void checkValidFile(String fileName){
	        boolean valid=true;
	        try{
	            File f = new File(fileName);
	            if ( !f.exists() || f.isDirectory() ){
	                valid=false;
	            }
	        }catch(Exception e){
	            valid=false;
	        }
	        if ( !valid){
	            System.out.println("File doesn't exist: " + fileName);
	            System.exit(0);
	        }
	    }
	    
	    /**
	     * Get the date or number value from a cell
	     * @param myCell
	     * @return
	     * @throws Exception
	     */
	    private static String getNumericValue(HSSFCell myCell) throws Exception {
	        String cellData="";
	         if ( HSSFDateUtil.isCellDateFormatted(myCell) ){
	               cellData += new SimpleDateFormat(OUTPUT_DATE_FORMAT).format(myCell.getDateCellValue()) +CVS_SEPERATOR_CHAR;
	           }else{
	               cellData += new BigDecimal(myCell.getNumericCellValue()).toString()+CVS_SEPERATOR_CHAR ;
	           }
	        return cellData;
	    }
	    
	    /**
	     * Get the formula value from a cell
	     * @param myCell
	     * @return
	     * @throws Exception
	     */
	    private static String getFormulaValue(HSSFCell myCell) throws Exception{
	        String cellData="";
	         if ( myCell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_STRING  || myCell.getCellType () ==HSSFCell.CELL_TYPE_BOOLEAN) {
	             cellData +=  myCell.getRichStringCellValue ()+CVS_SEPERATOR_CHAR;
	         }else  if ( myCell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_NUMERIC ) {
	             cellData += getNumericValue(myCell)+CVS_SEPERATOR_CHAR;
	         }
	         return cellData;
	    }
	    
	    /**
	     * Get cell value based on the excel column data type
	     * @param myCell
	     * @return
	     */
	    private static String getCellData( HSSFCell myCell) throws Exception{
	        String cellData="";
	         if ( myCell== null){
	             cellData += CVS_SEPERATOR_CHAR;;
	         }else{
	             switch(myCell.getCellType() ){
	                 case  HSSFCell.CELL_TYPE_STRING  :
	                 case  HSSFCell.CELL_TYPE_BOOLEAN  :
	                          cellData +=  myCell.getRichStringCellValue ()+CVS_SEPERATOR_CHAR;
	                          break;
	                 case HSSFCell.CELL_TYPE_NUMERIC :
	                         cellData += getNumericValue(myCell);
	                         break;
	                 case  HSSFCell.CELL_TYPE_FORMULA :
	                         cellData +=  getFormulaValue(myCell);
	             default:
	                 cellData += CVS_SEPERATOR_CHAR;;
	             }
	         }
	         return cellData;
	    }
	    
	    /**
	     * Write the string into a text file
	     * @param csvFileName
	     * @param csvData
	     * @throws Exception
	     */
	    private static void writeCSV(String csvFileName,String csvData) throws Exception{
	        FileOutputStream writer = new FileOutputStream(csvFileName);
	        writer.write(csvData.getBytes());
	        writer.close();
	    }
	    
	    /**
	     * Convert the Excel file data into CSV file
	     * @param excelFileName
	     * @param csvFileName
	     * @throws Exception
	     */
	    public static void excelToCSV(String excelFileName,String csvFileName) throws Exception{
	            checkValidFile(csvFileName);
	            @SuppressWarnings("resource")
				HSSFWorkbook myWorkBook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFileName)));
	            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
	            @SuppressWarnings("rawtypes")
				Iterator  rowIter =  mySheet.rowIterator();
	            String csvData="";
	            while (rowIter.hasNext()) {
	                    HSSFRow myRow = (HSSFRow) rowIter.next();
	                    for ( int i=0;i<myRow.getLastCellNum();i++){
	                          csvData += getCellData(myRow.getCell(i));
	                    }
	                    csvData+=NEW_LINE_CHARACTER;
	            }
	            writeCSV(csvFileName, csvData);
	    }
	    
	    
	    /***
	     * Date format used to convert excel cell date value
	     */
	    private static final String OUTPUT_DATE_FORMAT= "yyyy-MM-dd";
	    /**
	     * Comma separated characters
	     */
	    private static final String CVS_SEPERATOR_CHAR=",";
	    /**
	     * New line character for CSV file
	     */
	    private static final String NEW_LINE_CHARACTER="\r\n";
	   
	    /**
	     * Convert CSV file to Excel file
	     * @param csvFileName
	     * @param excelFileName
	     * @throws Exception
	     */
	    @SuppressWarnings("resource")
		public static void csvToEXCEL(String csvFileName,String excelFileName) throws Exception{
	        checkValidFile(csvFileName);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName)));
	        HSSFWorkbook myWorkBook = new HSSFWorkbook();
	        FileOutputStream writer = new FileOutputStream(new File(excelFileName) );
	        HSSFSheet mySheet = myWorkBook.createSheet();
	        String line= "";
	        int rowNo=0;
	        while ( (line=reader.readLine()) != null ){
	            String[] columns = line.split(CVS_SEPERATOR_CHAR);
	             HSSFRow myRow =mySheet.createRow(rowNo);
	            for (int i=0;i<columns.length;i++){
	                HSSFCell myCell = myRow.createCell(i);
	                myCell.setCellValue(columns[i]);
	            }
	             rowNo++;
	        }
	        myWorkBook.write(writer);
	        writer.close();
	    }
	
}
