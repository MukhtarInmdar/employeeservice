/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import lombok.NonNull;

/**
 * TimeUtil class provides Time utility specific functionality.
 * 
 * @author Venkat_Koniki
 * @since 23.04.2019
 * @time 06:10PM
 */
public class TimeUtil {

	private final static Logger LOGGER = Logger.getLogger(TimeUtil.class);
	
	public static int differenceBetweenDays(Timestamp escalatedDate,Timestamp extendedDate) {
		//LOGGER.info("*** The control is inside the differenceBetweenDays the TimeUtil ****");
		int daysBetween = 0;
		try {
			//Timestamp stamp = new Timestamp(System.currentTimeMillis());
			Date currentDate = new Date(escalatedDate.getTime());
			Date targetDate = new Date(extendedDate.getTime());
			long difference = targetDate.getTime() - currentDate.getTime();
			/* If Extended escalation for 12-12-2019 11:59:59 so we need to add one day extra */
			//daysBetween = (int)(difference / (1000 * 60 * 60 * 24))+1;
			  daysBetween = (int)(difference / (1000 * 60 * 60 * 24));			
			LOGGER.info("********Number of Days between dates:" + daysBetween);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween;
	}

	public static Timestamp addOneDay(Timestamp timestamp) {
		//LOGGER.info("**** The control is inside the addOneDay in TimeUtil *****");
		return addDays(timestamp, 1);
	}
	
	public static Timestamp addDays(Timestamp timestamp,int days){
		 //LOGGER.info("**** The control is inside the addOneDay in TimeUtil *****");
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(timestamp.getTime());
		 calendar.add(Calendar.DATE, days);  // number of days to adds
		 return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static Timestamp removeDays(Timestamp timestamp, int days) {
		LOGGER.info("**** The control is inside the removeDays in TimeUtil *****");
		return addDays(timestamp, days);
	}
	
	public static Timestamp removeOneDay(Timestamp timestamp) {
		//LOGGER.info("**** The control is inside the removeDays in TimeUtil *****");
		return addDays(timestamp, -1);
	}
	
	public static Timestamp add23Hours(Timestamp timestamp){
		 LOGGER.info("**** The control is inside the add23Hours in TimeUtil *****");
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(timestamp.getTime());
		 calendar=removeHoursMinutes(calendar);
		 calendar.add(Calendar.HOUR_OF_DAY, 23);
		 calendar.add(Calendar.MINUTE, 59);
		 return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static Timestamp addHours(Timestamp timestamp,int hours){
		 LOGGER.info("**** The control is inside the addHours in TimeUtil *****");
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(timestamp.getTime());
		 calendar.add(Calendar.HOUR_OF_DAY, hours);
		 return new Timestamp(calendar.getTimeInMillis());
	}
	public static Calendar removeHoursMinutes(Calendar calendar) {
		 LOGGER.info("**** The control is inside the removeHoursMinutes in TimeUtil *****");
		 calendar.set(Calendar.HOUR_OF_DAY, 0);
		 calendar.set(Calendar.MINUTE, 0);
		 calendar.set(Calendar.SECOND, 0);
		 calendar.set(Calendar.MILLISECOND, 0);
		 return calendar;
	}
	
	
	public static Timestamp endOfTheDayTimestamp(Timestamp timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp timestamp2 = new Timestamp(calendar.getTime().getTime());
		LOGGER.info(" ***** TimeUtil.removeTimePartFromTimeStamp() ***** I/P " + timestamp + " O/P " + timestamp2);
		return timestamp2;
	}
	
	public static Timestamp startingOfTheDayTimestamp(Timestamp timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp timestamp2 = new Timestamp(calendar.getTime().getTime());
		LOGGER.info(" ***** TimeUtil.startingOfTheDayTimestamp() ***** I/P " + timestamp + " O/P " + timestamp2);
		return timestamp2;
	}
	
	public static Timestamp removeTimePartFromTimeStamp(Timestamp timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Timestamp timestamp2 = new Timestamp(calendar.getTime().getTime());
		LOGGER.info(" ***** TimeUtil.removeTimePartFromTimeStamp() ***** I/P " + timestamp + " O/P " + timestamp2);
		return timestamp2;
	}
	
	public static Timestamp addCurrentTimeToTimeStamp(Timestamp timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND,  Calendar.getInstance().get(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, Calendar.MILLISECOND);

		Timestamp timestamp2 = new Timestamp(calendar.getTime().getTime());
		LOGGER.info(" ***** TimeUtil.removeTimePartFromTimeStamp() ***** I/P " + timestamp + " O/P " + timestamp2);
		return timestamp2;
	}

	public static Timestamp removeTimePartFromTimeStamp1(Timestamp timestamp) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String startDate = dateFormat.format(timestamp);
		Timestamp timestamp2 = new Timestamp(dateFormat.parse(startDate).getTime());
		//LOGGER.info(" ***** TimeUtil.removeTimePartFromTimeStamp1() ***** I/P " + timestamp + " O/P " + timestamp2);
		return timestamp2;
	}
	
	public static int getDay(Date date) {
		LOGGER.info("**** The control is inside the getDay in TimeUtil *****");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date getLastDateOfMonth(Date date) {
		LOGGER.info("**** The control is inside the getLastDateOfMonth in TimeUtil *****");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	public static Date getFirstDateOfMonth(Date date){
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	    return cal.getTime();
	}
	
	public static Date getPreviousMonth(Date date) {
		LOGGER.info("**** The control is inside the getPreviousMonth in TimeUtil *****");
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.MONTH, -1);
	    return cal.getTime();
	}

      	public static Date getFirstDateOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
		return cal.getTime();
	}
	
	public static String timestampToDD_MM_YYYY(Timestamp timestamp) {
		LOGGER.info("**** The control is inside the timestampToDD_MM_YYYY in TimeUtil *****");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		LOGGER.info("My date formatted is: " + sdf.format(timestamp));	
		return sdf.format(timestamp);	
	}
	
	public static String getTimeInDD_MM_YYYY(Date date) {
		LOGGER.info("**** The control is inside the timestampToDD_MM_YYYY in TimeUtil *****");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		LOGGER.info("My date formatted is: " + sdf.format(date));	
		return sdf.format(date);
	}
	
	public static String getTimeInDD_MM_YYYY_SlashFormat(Date date) {
		LOGGER.info("**** The control is inside the getTimeInDD_MM_YYYY_HyphoneFormat in TimeUtil *****");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		LOGGER.info("My date formatted is: " + sdf.format(date));	
		return sdf.format(date);
	}
	
	public static Date genericDate(Date date,int days) {
		LOGGER.info("**** The control is inside the genericDate in TimeUtil *****");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//cal.add(Calendar.DATE, 1);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	public static Date startDateOfWeek() {
		LOGGER.info("**** The control is inside the startDateOfWeek in TimeUtil *****");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.add(Calendar.DATE, -7); 
		return c.getTime();
	}
	
	public static Date endDateOfWeek() {
		LOGGER.info("**** The control is inside the endDateOfWeek in TimeUtil *****");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return c.getTime();
	}
	
	public static int compareTheDate(Timestamp startDate, Timestamp endDate) {
		//LOGGER.info("***** Control inside the TimeUtil.compareTheDate() *****");
		int result = 0;
		if (endDate.after(startDate)) {
			//System.out.println("Date is Greater " + startDate + " " + endDate);
			return 1;
		} else if (endDate.before(startDate)) {
			//System.out.println("Date is lesser " + startDate + " " + endDate);
			return -1;
		} else if (endDate.equals(startDate)) {
			//System.out.println("Date is same " + startDate + " " + endDate);
			return 0;
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	public static double compareTwoTimeStamps(@NonNull Timestamp createdDate,@NonNull Timestamp resolvedDate){
	  double milliseconds1 = createdDate.getTime();
	  double milliseconds2 = resolvedDate.getTime();
	  double diff = milliseconds2 - milliseconds1;
	  double diffSeconds = diff / 1000;
	  double diffMinutes = diff / (60 * 1000);
	  double diffHours = diff / (60 * 60 * 1000);
	  double diffDays = diff / (24 * 60 * 60 * 1000);
	  return diffHours;
	}
	
	public static final double roundTime(double time) {
		LOGGER.info("***** Control inside the roundTime in TimeUtil *****");
		DecimalFormat df = new DecimalFormat("#.##");
		if(Double.isNaN(time)) {
			time = 0.0;   
		}
		try {
		time = Double.valueOf(df.format(time));
		}catch(Exception ex) {
			LOGGER.error("*** The NumberFormatException ***"+ex);
		}
		LOGGER.debug("*** final Time ***"+time);
		return time;
	}
	
	public static final Timestamp past30Day(Timestamp timestamp) {
		LOGGER.info("***** The Control inside the past30Day in TimeUtil *****"+timestamp);
		Calendar cal = new GregorianCalendar();
		cal.setTime(timestamp);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		timestamp = new Timestamp(cal.getTimeInMillis());
		LOGGER.info("**** The past 30 days time ****"+timestamp);
		return timestamp;
	}
	
	public static boolean isEqulas(Timestamp soure, Timestamp target) {
		LOGGER.info("***** Control inside the isEqulas in TimeUtil *****");
		boolean result = removeTimePartFromTimeStamp(soure).equals(removeTimePartFromTimeStamp(target));
		return result;
	}
	
	public static long differenceBetweenTwoTimestampsInHours(@NonNull Timestamp startdate,@NonNull Timestamp endDate) {
		LOGGER.info("***** Control inside the differenceBetweenTwoTimestampsInHours in TimeUtil *****");
		return TimeUnit.MILLISECONDS.toHours(startdate.getTime() - endDate.getTime());
	}
	
	public static Long getDifferenceBetweenTwoTimestampsInMilliSeconds(@NonNull Timestamp startdate,@NonNull Timestamp endDate) {
		LOGGER.info("***** Control inside the getDifferenceBetweenTwoTimestampsInMilliSeconds in TimeUtil *****");
		if(Util.isNotEmptyObject(startdate) && Util.isNotEmptyObject(endDate)) {
			return (startdate.getTime() - endDate.getTime());			
		}
		return 0l;
	}
	
	public static String getDifferenceBetweenTwoTimestampsInTime(@NonNull Timestamp startdate,@NonNull Timestamp endDate) {
		LOGGER.info("***** Control inside the getDifferenceBetweenTwoTimestampsInTime in TimeUtil *****");
		if(Util.isNotEmptyObject(startdate) && Util.isNotEmptyObject(endDate)) {
			Long timeInMillis = startdate.getTime() - endDate.getTime();
			return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMillis),
				(TimeUnit.MILLISECONDS.toMinutes(timeInMillis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis))),
				(TimeUnit.MILLISECONDS.toSeconds(timeInMillis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis))));
		}
		return "00:00:00";
	}
	
	public static String getTimeFormatWithMilliSeconds(@NonNull Long timeInMillis) {
		LOGGER.info("***** Control inside the getTimeFormatWithMilliSeconds in TimeUtil *****");
		return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMillis),
				(TimeUnit.MILLISECONDS.toMinutes(timeInMillis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis))),
				(TimeUnit.MILLISECONDS.toSeconds(timeInMillis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis))));
	}
	
	public static Double getDaysFromMilliseconds(Double timeInMilliSeconds) {
		LOGGER.info("***** Control inside the getDaysFromMilliseconds in TimeUtil *****");
		return timeInMilliSeconds/(1000*60*60*24);
	}
	
	public static void main(String ... args) {
		Timestamp timestamp2 = java.sql.Timestamp.valueOf("2020-09-24 10:00:00.0");
		Timestamp timestamp1 = java.sql.Timestamp.valueOf("2020-09-25 00:00:00.0");
		System.out.println(differenceBetweenTwoTimestampsInHours(timestamp1,timestamp2));
		
		Timestamp timestamp = new Timestamp(new Date().getTime());
		System.out.println(timestamp.getTime());
		
	}

	public static Timestamp joinDateAndTimeStamp(Date date, Timestamp time) {
		LOGGER.info("*** Control is inside of the joinDateAndTimeStamp in TimeUtil ***");
		Calendar calendarForDate = Calendar.getInstance();
		calendarForDate.setTime(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time.getTime());
		calendar.set(calendarForDate.get(Calendar.YEAR), calendarForDate.get(Calendar.MONTH), calendarForDate.get(Calendar.DAY_OF_MONTH));
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static String getTimeInSpecificFormat(Timestamp time, String format) {
		LOGGER.info("*** Control is inside of the joinDateAndTimeStamp in TimeUtil ***");
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(time.getTime()));
	}

	public static Boolean isDuplicateSlotsExist(Timestamp startTimeOne,Timestamp endTimeOne,Timestamp startTime,Timestamp endTime) {
		LOGGER.info("*** Control is inside of the isDuplicateSlotsExist in TimeUtil ***");
		if(((startTimeOne.after(startTime) || startTimeOne.equals(startTime)) && (startTimeOne.before(endTime) || startTimeOne.equals(endTime)))
			|| ((endTimeOne.after(startTime) || endTimeOne.equals(startTime)) && (endTimeOne.before(endTime) || endTimeOne.equals(endTime)))
			|| ((startTime.after(startTimeOne) || startTime.equals(startTimeOne)) && (startTime.before(endTimeOne) || startTime.equals(endTimeOne)))
			|| ((endTime.after(startTimeOne) || endTime.equals(startTimeOne)) && (endTime.before(endTimeOne) || endTime.equals(endTimeOne)))){
			return true;	
		}
		return false;
	}
	
	public static int getDaysFromTwoDates(Timestamp escalatedDate,Timestamp extendedDate) {
		//LOGGER.info("*** The control is inside the getDaysFromTwoDates the TimeUtil ****");
		int daysBetween = 0;
		try {
			//Timestamp stamp = new Timestamp(System.currentTimeMillis());
			Date currentDate = new Date(escalatedDate.getTime());
			Date targetDate = new Date(extendedDate.getTime());
			long difference = targetDate.getTime() - currentDate.getTime();
			/* If Extended escalation for 12-12-2019 11:59:59 so we need to add one day extra */
			//daysBetween = (int)(difference / (1000 * 60 * 60 * 24))+1;
			  daysBetween = (int)(difference / (1000 * 60 * 60 * 24));			
			LOGGER.info("********Number of Days between dates:" + daysBetween);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween;
	}
	

	public static int getDaysFromTwoDatesForPaymentDue(Timestamp escalatedDate,Timestamp extendedDate) {
		//LOGGER.info("*** The control is inside the getDaysFromTwoDates the TimeUtil ****");
		int daysBetween = 0;
		try {
			//Timestamp stamp = new Timestamp(System.currentTimeMillis());
			Date currentDate = new Date(escalatedDate.getTime());
			Date targetDate = new Date(extendedDate.getTime());
			long difference = targetDate.getTime() - currentDate.getTime();
			/* If Extended escalation for 12-12-2019 11:59:59 so we need to add one day extra */
			daysBetween = (int)(difference / (1000 * 60 * 60 * 24))+1;
			//  daysBetween = (int)(difference / (1000 * 60 * 60 * 24));			
			LOGGER.info("********Number of Days between dates:" + daysBetween);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween;
	}	
	
}
