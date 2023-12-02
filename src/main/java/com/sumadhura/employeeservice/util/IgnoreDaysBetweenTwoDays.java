package com.sumadhura.employeeservice.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.service.dto.FinPenalityInfo;

//import io.restassured.RestAssured;
 
public class IgnoreDaysBetweenTwoDays {
	
	//private final static Logger log = Logger.getLogger(IgnoreDaysBetweenTwoDays.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	static Timestamp startDateOF2018 = null;
	static Timestamp endDateOF2018 = null;
	
	static Timestamp startDateOF2020 = null;
	static Timestamp endDateOF2020 = null;
	static {
		try {
			/*1st Feb 2018 - 30th may 2018
			23rd Mar 2020- 30thJune 2020*/
			//live dates to be escaped from calculations
			/*	startDateOF2018 = dateFormat.parse("01-02-2018");
				endDateOF2018 = dateFormat.parse("30-05-2018");
			
				startDateOF2020 = dateFormat.parse("23-03-2020");
				endDateOF2020 = dateFormat.parse("30-06-2020");
			*/
			
			startDateOF2018 = new Timestamp (dateFormat.parse("15-01-2021").getTime());
			endDateOF2018 =  new Timestamp (dateFormat.parse("24-01-2021").getTime());
			
			startDateOF2020 = new Timestamp (dateFormat.parse("18-02-2021").getTime());
			endDateOF2020 = new Timestamp (dateFormat.parse("25-02-2021").getTime());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws EmployeeFinancialServiceException, Exception {
		startDateOF2018 = new Timestamp (dateFormat.parse("15-01-2021").getTime());
		endDateOF2018 =  new Timestamp (dateFormat.parse("25-01-2021").getTime());
		List<FinPenalityInfo> penaltyInfo = getList();
		
		for (int index = 0; index < penaltyInfo.size(); index++) {
			FinPenalityInfo finPenalityInfo =penaltyInfo.get(index);
 
			//System.out.println("before START DATE "+finPenalityInfo.getStartDate()+" END DATE "+finPenalityInfo.getEndDate());
			//System.out.println(TimeUtil.differenceBetweenDays(TimeUtil.removeTimePartFromTimeStamp1(finPenalityInfo.getStartDate()), TimeUtil.removeTimePartFromTimeStamp1(startDateOF2018)));
			//Timestamp startDate = finPenalityInfo.getStartDate();
			Timestamp endDate = finPenalityInfo.getEndDate();
			if(finPenalityInfo.getEndDate().after(startDateOF2018) && finPenalityInfo.getStartDate().before(endDateOF2018)) {
				//finPenalityInfo.setStartDate(TimeUtil.removeOneDay(startDateOF2018));
				finPenalityInfo.setEndDate(TimeUtil.removeOneDay(startDateOF2018));
				FinPenalityInfo finPenalityInfoNew = new FinPenalityInfo();
				BeanUtils.copyProperties(finPenalityInfo, finPenalityInfoNew);
				finPenalityInfoNew.setStartDate(TimeUtil.addOneDay(endDateOF2018));
				finPenalityInfoNew.setEndDate(endDate);
				penaltyInfo.add(index+1, finPenalityInfoNew);
			}/* else if (finPenalityInfo.getEndDate().before(endDateOF2018)) {
				finPenalityInfo.setEndDate(TimeUtil.addOneDay(endDateOF2018));
			}*/
			
			
			System.out.println("after START DATE "+finPenalityInfo.getStartDate()+" END DATE "+finPenalityInfo.getEndDate());
			
		}//for loop
		
		
		
		/*String num = String.format("%04d",100006);
		System.out.println(num);
		
		System.out.println("INV"+num);*/
	}
	
	private static List<FinPenalityInfo> getList() throws ParseException, Exception {
		List<FinPenalityInfo> penaltyInfo = new ArrayList<>();
		FinPenalityInfo finPenalityInfo=new FinPenalityInfo();
		finPenalityInfo.setStatusId(Status.ACTIVE.getStatus());
		finPenalityInfo.setType(MetadataId.FIN_PENALTY.getId());
		finPenalityInfo.setStartDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("10-12-2020").getTime())));
		finPenalityInfo.setEndDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("09-01-2021").getTime())));
		
		FinPenalityInfo finPenalityInfo1=new FinPenalityInfo();
		finPenalityInfo1.setStatusId(Status.ACTIVE.getStatus());
		finPenalityInfo1.setType(MetadataId.FIN_PENALTY.getId());
		finPenalityInfo1.setStartDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("10-01-2021").getTime())));
		finPenalityInfo1.setEndDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("09-02-2021").getTime())));
		
		FinPenalityInfo finPenalityInfo11=new FinPenalityInfo();
		finPenalityInfo11.setStatusId(Status.ACTIVE.getStatus());
		finPenalityInfo11.setType(MetadataId.FIN_PENALTY.getId());
		finPenalityInfo11.setStartDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("10-02-2021").getTime())));
		finPenalityInfo11.setEndDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("09-03-2021").getTime())));
		penaltyInfo.add(finPenalityInfo);
		penaltyInfo.add(finPenalityInfo1);
		penaltyInfo.add(finPenalityInfo11);
		
		return penaltyInfo;
	}

	//@Test
	public void testAssertArrayEquals() {
		/*byte[] expected = "trial1".getBytes();
		byte[] actual = "trial".getBytes();
		//assertArrayEquals("failure - byte arrays not same", expected, actual);
*/	}
	
	//@BeforeClass
    public static void init() {
       /* RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8082;*/
    }
	
    //@Test
    public void testUserFetchesSuccess() {
 /*       post("/employeeservice/financial/getMileStoneSetsDtls.spring")
        .then()
        .body("responseCode", equalTo(200));
*/    }

	
}
