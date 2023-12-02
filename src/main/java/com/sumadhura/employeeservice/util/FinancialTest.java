package com.sumadhura.employeeservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import com.sumadhura.employeeservice.dto.Employee;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;

//import io.restassured.RestAssured;

public class FinancialTest {
	
	public static void removeDuplicateElements(int[] arr){
		for (int i = 0; i < arr.length; i++) {
			for (int j = i+1; j < arr.length; j++) {
				if(arr[i]==arr[j]) {
					arr[j]=0;
				}
			}
		}
		System.out.println(Arrays.toString(arr));
		return ;
	}

	public static void countDuplicateElements(int[] arr){
		for (int i = 0; i < arr.length; i++) {
			int count = 1;
			for (int j = i+1; j < arr.length; j++) {
				if(arr[i]==arr[j]) {
					count++;
					arr[j]=0;
				}
			}
			if(arr[i]!=0) {
				System.out.println(arr[i]+" "+count);
			}
		}
		//System.out.println(Arrays.toString(arr));
		return ;
	}
	
	public static void countDuplicateElements(String[] arr){
		for (int i = 0; i < arr.length; i++) {
			int count = 1;
			for (int j = i+1; j < arr.length; j++) {
				if(arr[i].equals(arr[j])) {
					count++;
					arr[j]="-";
				}
			}
			
			if(!arr[i].equals("-")) {
				System.out.println(arr[i]+" "+count);
			}
		}
		//System.out.println(Arrays.toString(arr));
		return ;
	}


	public static void binarySearch(int arr[], int first, int last, int key) {
		int mid = (first + last) / 2;
		while (first <= last) {
			if (arr[mid] < key) {
				first = mid + 1;
			} else if (arr[mid] == key) {
				System.out.println("Element is found at index: " + mid);
				break;
			} else {
				last = mid - 1;
			}
			mid = (first + last) / 2;
		}
		if (first > last) {
			System.out.println("Element is not found!");
		}
	}
	
	public static int FirstFactorial(int num) { 
		System.out.println("Enter the number...");
		if (num != 1) {
			return num*FirstFactorial(num - 1);
		} 
		return num;		
	}  
	
	public static void main(String[] args) throws Exception {

		// keep this function call here  
		System.out.println("from date "+new Timestamp(1654041600000l));
		System.out.println("from date "+new Timestamp(1656028800000l));
	    Scanner  s = new Scanner(System.in);
	  
	    System.out.println(FirstFactorial(Integer.valueOf(s.nextLine())));
		
		
		int arr1[] = {10,20,30,40,50};
		int key = 30;
        int last=arr1.length-1;
		binarySearch(arr1,0,last,key);
		
		System.exit(0);

		int[] arrSort = { 2, 5, 1, 5, 3, 6, 5, 8, 6 };
		int temp = 0;
		for (int i = 0; i < arrSort.length; i++) {
			for (int j = i + 1; j < arrSort.length; j++) {
				if (arrSort[i] > arrSort[j]) {
					temp = arrSort[j];
					arrSort [j]  = arrSort[i];
					arrSort[i] = temp;
				}
			}
		}
		System.out.println(Arrays.toString(arrSort));
		System.exit(0);
		
		int[] arr = {1,5,2,5,3,6,5,8,6};
		//removeDuplicateElements(arr);	
		countDuplicateElements(arr);
		System.out.println("FinancialTest.main()");
		countDuplicateElements(new String[] {"1","5","2","5","3","6","5","8","6"});
		
		System.exit(0);
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		FinPenaltyTaxPojo finTaxPojo = new FinPenaltyTaxPojo();
		finTaxPojo.setPercentageValue(18d);
		
		List<FlatDetailsPojo> flatDetailsPojos = new ArrayList<>();
		FlatDetailsPojo f1 = new FlatDetailsPojo();
		f1.setSbua(1799d);
		flatDetailsPojos.add(f1);
		
		SiteOtherChargesDetailsPojo siteOtherChargesDetailsPojo = new SiteOtherChargesDetailsPojo();
		siteOtherChargesDetailsPojo.setAmtForYears(1l);
		siteOtherChargesDetailsPojo.setAmount(42d);
		
		Timestamp nan107StartDate = new Timestamp(format.parse("01-02-2022").getTime());
		//Timestamp eg107StartDate = new Timestamp(format.parse("01-03-2022").getTime());
		
		Timestamp bookingDate = new Timestamp(format.parse("15-02-2022").getTime());
		String bookingDateInString = format.format(format.parse("15-02-2022"));
		
		Timestamp startDate = nan107StartDate;
		Timestamp endDateForMaintencenDate = new Timestamp(format.parse("31-01-2023").getTime());

		//double totalMaintainanceCostToPay = 0.0d;
		
		if (startDate != null && endDateForMaintencenDate != null ) {
			if(bookingDate.after(startDate) || bookingDate.equals(startDate)) {
				RoundingMode roundingMode = RoundingMode.HALF_UP;
				int roundingModeSize = 2;
				bookingDate =  TimeUtil.addDays(bookingDate, 30);
				bookingDateInString = format.format(bookingDate);
				bookingDate = TimeUtil.removeTimePartFromTimeStamp1(bookingDate);
				Timestamp lastDayOfMonth =  TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(TimeUtil.getLastDateOfMonth(bookingDate).getTime()));
				
				int days = TimeUtil.differenceBetweenDays(bookingDate, lastDayOfMonth);
				//int days = TimeUtil.differenceBetweenDays(bookingDate, new Timestamp(TimeUtil.getLastDateOfMonth(endDateForMaintencenDate).getTime()));
				days +=1;
				System.out.println(TimeUtil.getLastDateOfMonth(bookingDate)+" "+TimeUtil.addOneDay(new Timestamp(TimeUtil.getLastDateOfMonth(bookingDate).getTime())));
				
				if(lastDayOfMonth.after(endDateForMaintencenDate)) {
					System.out.println(" max date in month is after the end date of maintenance charges");
					days = 0;
					//return 0;
				}
				
				bookingDateInString = format.format(TimeUtil.addOneDay(new Timestamp(TimeUtil.getLastDateOfMonth(bookingDate).getTime())));
				Calendar startDateForMaintenance = new GregorianCalendar(Integer.valueOf(bookingDateInString.split("-")[2]),Integer.valueOf(bookingDateInString.split("-")[1])-1,Integer.valueOf(bookingDateInString.split("-")[0]));
				//birthDay.set(bookingDate.getYear(), bookingDate.getMonth(), bookingDate.getDay());
				Calendar allDaysInMonth = Calendar.getInstance();
				allDaysInMonth.setTime(bookingDate);
				int monthMaxDays = allDaysInMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
				
				//Long amountForYear = siteOtherChargesDetailsPojo.getAmtForYears();
				Double maintenanceChargesForYear = siteOtherChargesDetailsPojo.getAmount();
				Double maintenanceChargesForMonth = BigDecimal.valueOf(maintenanceChargesForYear/12).setScale(roundingModeSize, roundingMode).doubleValue();
				Double maintenanceChargesForDay = maintenanceChargesForMonth/30;
				maintenanceChargesForDay = BigDecimal.valueOf(maintenanceChargesForDay).setScale(3, roundingMode).doubleValue();
				
				System.out.println("maintenanceAmountForYear "+maintenanceChargesForYear);
				System.out.println("maintenanceAmountForMonth "+maintenanceChargesForMonth);
				System.out.println("maintenanceAmountForDay "+maintenanceChargesForDay);
				Double maintenanceCalculatedAmount = 0.0d;
				if (days > 0) {
					System.out.println("FinancialTest.main( ) days "+days +" "+(maintenanceChargesForDay*days));
					System.out.println((maintenanceChargesForDay*days)*flatDetailsPojos.get(0).getSbua());
					//maintenanceCalculatedAmount = (maintenanceChargesForDay*days)*flatDetailsPojos.get(0).getSbua();
				}
				
				// Using Calendar - calculating number of months between two dates  
		        //Calendar birthDay = new GregorianCalendar(2022, Calendar.MAY, 01);
		        Calendar today = new GregorianCalendar();
		        
		        //today.setTime(format.parse("31-01-2023"));//End Date of maintenance
		        today.setTime(endDateForMaintencenDate);//End Date of maintenance
		        
		        int yearsInBetween = today.get(Calendar.YEAR) - startDateForMaintenance.get(Calendar.YEAR);
		        int monthsDiff = today.get(Calendar.MONTH) - startDateForMaintenance.get(Calendar.MONTH);
		        long ageInMonths = yearsInBetween*12 + monthsDiff;
		        ageInMonths +=1;
		        //long age = yearsInBetween;

		        if(ageInMonths<0) {
		        	ageInMonths = 0;//if the months are in minus make the month zero, so calculation will not happen
		        	//return 0;
		        }
		        //calculating per month maintenance charges
		        Double maintenanceAmountForMonth = maintenanceChargesForMonth*flatDetailsPojos.get(0).getSbua();
		        maintenanceAmountForMonth = maintenanceAmountForMonth+(maintenanceAmountForMonth*finTaxPojo.getPercentageValue()/100);//amount with gst
		        maintenanceAmountForMonth = BigDecimal.valueOf(maintenanceAmountForMonth).setScale(roundingModeSize, roundingMode).doubleValue();
		        
		        //calculating month wise maintenance amount
		        maintenanceCalculatedAmount +=  (maintenanceChargesForMonth*ageInMonths)*flatDetailsPojos.get(0).getSbua();
		        maintenanceCalculatedAmount = (maintenanceCalculatedAmount+(maintenanceCalculatedAmount*finTaxPojo.getPercentageValue()/100));
		        System.out.println("Maintenance Amount "+maintenanceCalculatedAmount);//amount with gst
		        
		        //calculating current month balance days maintenance amount, from bookingDate + 31
		        Double perDayAmount = BigDecimal.valueOf(maintenanceAmountForMonth/monthMaxDays).setScale(roundingModeSize, roundingMode).doubleValue();
		        Double perDayCalculatedAmount = (perDayAmount)*days;//
		        
		        maintenanceCalculatedAmount +=perDayCalculatedAmount;
		        maintenanceCalculatedAmount = BigDecimal.valueOf(maintenanceCalculatedAmount).setScale(roundingModeSize, roundingMode).doubleValue();
		        
		        System.out.println("per day wise amount in month "+perDayAmount);
		        System.out.println("balance days amount in month "+perDayCalculatedAmount);
				System.out.println("Maintenance Amount "+maintenanceCalculatedAmount);
				
			}
		}
	
	}
	public static int removeDuplicate(int[] arrNumbers, int num) {
		int[] arrNumbers1 = new int[arrNumbers.length];
		int index = 0;
		for (int i = 0; i < arrNumbers.length; i++) {
			boolean flag = true;
			for (int j = 0; j < arrNumbers.length; j++) {
				if (arrNumbers1[j] == arrNumbers[i]) {
					flag = false;
					break;
				}
			}
			if (flag == true) {
				arrNumbers1[index++] = arrNumbers[i];
			}
		}
		System.out.println(Arrays.toString(arrNumbers1));
		return 0;
	}


}
