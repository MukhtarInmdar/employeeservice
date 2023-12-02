/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;




/**
 * this class will help to provide excel related information
 * 
 * @author Aniket Chavan
 * @since 06.05.2019
 * @time 05:52PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExcelHelperInfo implements Serializable {
	
	private static final long serialVersionUID = 8307560825618392627L;
	
	private String excelFileName;
	private String excelFilePath;
	private String excelFileUrl;
	
	private List<String> headerNames;
	private List<List<Map<String,Object>>> listOfRowData;
	private Map<String,String> columnType;
	
}