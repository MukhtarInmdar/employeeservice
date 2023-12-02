package com.sumadhura.employeeservice.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.service.dto.ExcelHelperInfo;

@Component("excelHelper")
public class ExcelHelper {

	private final static Logger log = Logger.getLogger(ExcelHelper.class);

	/*@Autowired
	private ResponceCodesUtil responceCodesUtil;*/

	public FileInfo ExcelWorkerHelper(ExcelHelperInfo excelHelperInfo) throws Exception {
		log.info("Control inside the ExcelHelper.ExcelWorkerHelper() ***** ");
		// Creating Workbook instances
		HSSFWorkbook wb = new HSSFWorkbook();

		// An output stream accepts output bytes and sends them to sink
		OutputStream fileOut = new FileOutputStream(excelHelperInfo.getExcelFilePath());

		// Now creating Sheets using sheet object
		HSSFSheet transaction = wb.createSheet("Transactions");
		//HSSFSheet sheet2 = wb.createSheet("Milestones");

		List<String> columnNames = excelHelperInfo.getHeaderNames();

		List<List<Map<String, Object>>> listOfData = excelHelperInfo.getListOfRowData();

		Map<String, String> columnTypeList = excelHelperInfo.getColumnType();

		// Set Header Font
		HSSFFont headerFont = wb.createFont();
		// headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setFontName("Calibri");

		// Set Header Style
		CellStyle headerStyle = wb.createCellStyle();
		// headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);
		// headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		// headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

		HSSFCellStyle style = wb.createCellStyle();
		// style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		style.setFont(headerFont);
		
		HSSFCellStyle numberFormat = wb.createCellStyle();
		numberFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		//numberFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		numberFormat.setFont(headerFont);

		
		CellStyle dateFormat = wb.createCellStyle();
		//dateFormat.setDataFormat(createHelper.createDataFormat().getFormat(“m/d/yy h:mm”));
		dateFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat("dd-MM-yyyy"));
		dateFormat.setFont(headerFont);
		
		
		HSSFCellStyle my_style_0 = wb.createCellStyle();
        //HSSFCellStyle my_style_1 = wb.createCellStyle();
        /* Define date formats with the style */                        
        my_style_0.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        //my_style_1.setDataFormat(HSSFDataFormat.getBuiltinFormat("d-mm-yy"));
		
        my_style_0.setFont(headerFont);
        //my_style_1.setFont(headerFont);
		int rowCount = 0;
		Row header;
		Cell cell;

		header = transaction.createRow(rowCount);
		// loop through all tag of tag
		int count = 0;
		for (String element : columnNames) {
			// set header style
			cell = header.createCell(count);
			cell.setCellValue(element);
			cell.setCellStyle(headerStyle);
			Object columnType = columnTypeList.get(columnNames.get(count));
			if(element!=null && columnType!=null && columnType.toString().equalsIgnoreCase("Date")) {
				cell.setCellStyle(my_style_0);	
			} else if(element!=null && columnType!=null && columnType.toString().equalsIgnoreCase("Number")) {
				cell.setCellStyle(numberFormat);
			}
			count++;
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		for (List<Map<String, Object>> data : listOfData) {
			rowCount++;
			int cellIndex = 0;
			header = transaction.createRow(rowCount);
			for (Entry<String, Object> cell2 : data.get(0).entrySet()) {
				System.out.println(cell2);
				Object value = data.get(0).get(columnNames.get(cellIndex));
				Object columnType = columnTypeList.get(columnNames.get(cellIndex));
				cell = header.createCell(cellIndex);
				cell.setCellStyle(headerStyle);
				
				if(value!=null) {
					cell.setCellValue(value.toString());
				} else {
					cell.setCellValue("");
				}
				if(value!=null && columnType!=null && columnType.toString().equalsIgnoreCase("Date")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dateformat.parse(value.toString()));
					cell.setCellStyle(my_style_0);
					cell.setCellValue(calendar);
				} else if(Util.isEmptyObject(value) && columnType!=null && columnType.toString().equalsIgnoreCase("Date")) { 
					cell.setCellStyle(my_style_0);
				}else if(value!=null && columnType!=null && columnType.toString().equalsIgnoreCase("Number")) {
					cell.setCellStyle(numberFormat);
				}
				cellIndex++;
			}
		}

		// set auto size column for excel sheet
		transaction = wb.getSheetAt(0);
		for (int j = 0; j < columnNames.size(); j++) {
			transaction.autoSizeColumn(j);
		}

		// Display message on console for successful
		// execution of program
		System.out.println("Sheets Has been Created successfully");

		// Closing the output stream
		wb.write(fileOut);
		fileOut.close();
		wb.close();

		return null;
	}


}
