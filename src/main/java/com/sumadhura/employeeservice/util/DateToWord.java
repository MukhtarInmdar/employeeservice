package com.sumadhura.employeeservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Madhu Tokala
 * @company Veerankis.
 */

public class DateToWord {
	private final static List<String> dayInWords;
	static {
		dayInWords = new ArrayList<>();
		dayInWords.add("Zero");
		dayInWords.add("First");
		dayInWords.add("Second");
		dayInWords.add("Third");
		dayInWords.add("Fourth");
		dayInWords.add("Fifth");
		dayInWords.add("Sixth");
		dayInWords.add("Seventh");
		dayInWords.add("Eighth");
		dayInWords.add("Ninth");
		dayInWords.add("Tenth");

		dayInWords.add("Eleventh");
		dayInWords.add("Twelfth");
		dayInWords.add("Thirteenth");
		dayInWords.add("Fourteenth");
		dayInWords.add("Fifteenth");
		dayInWords.add("Sixteenth");
		dayInWords.add("Seventeenth");
		dayInWords.add("Eighteenth");
		dayInWords.add("Nineteenth");
		dayInWords.add("Twentieth");

		dayInWords.add("Twenty-First");
		dayInWords.add("Twenty-Second");
		dayInWords.add("Twenty-Third");
		dayInWords.add("Twenty-Fourth");
		dayInWords.add("Twenty-Fifth");
		dayInWords.add("Twenty-Sixth");
		dayInWords.add("Twenty-Seventh");
		dayInWords.add("Twenty-Eighth");
		dayInWords.add("Twenty-Ninth");
		dayInWords.add("Thirtieth");
		
		dayInWords.add("Thirty-First");

	}

	
	public static String getDayInWordsWithIndex(int day) {
		return dayInWords.get(day);
	}

	private static String input;
	private static String[] units = { "", " First", " Second", " Third", " Fourth", " Fifth", " Sixth", " Seventh",
			" Eighth", " Ninth" };
	private static String[] teen = { " Tenth", " Eleventh", " Twelfth", " Thirteenth", " Fourteenth", " Fifteenth",
			" Sixteenth", " Seventeenth", " Eighteenth", " Nineteenth" };
	private static String[] tens = { " Twentieth", " Thirtieth", " Fortieth", " Fiftieth", " Sixtyth", " Seventyth",
			" Eightyth", " Ninetyth" };
	private static String[] maxs = { "", "", " Hundred", " Thousand", " Lakh", " Crore" };

	public String convertNumberToWords(long n) {
		if ((String.valueOf(n)).length() > 9) {
			long number = n;
			final String[] specialNames = { "", " Thousand", " Million", " Billion", " Trillion", " Quadrillion",
					" Quintillion" };

			final String[] tensNames = { "", " Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy",
					" Eighty", " Ninety" };

			final String[] numNames = { "", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight",
					" Nine", " Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen",
					" Seventeen", " Eighteen", " Nineteen" };
			if (number == 0) {
				return "zero";
			}

			String prefix = "";

			if (number < 0) {
				number = -number;
				prefix = "negative";
			}

			String current = "";
			int place = 0;

			do {
				int n1 = (int) (number % 1000);
				if (n1 != 0) {
					String s = convertLessThanOneThousand(n1, numNames, tensNames);
					current = s + specialNames[place] + current;
				}
				place++;
				number /= 1000;
			} while (number > 0);

			return (prefix + current).trim();
		} else {
			input = numToString(n);
			String converted = "";
			int pos = 1;
			boolean hun = false;
			while (input.length() > 0) {
				if (pos == 1) // TENS AND UNIT POSITION
				{
					if (input.length() >= 2) // TWO DIGIT NUMBERS
					{
						String temp = input.substring(input.length() - 2, input.length());
						input = input.substring(0, input.length() - 2);
						converted += digits(temp);
					} else if (input.length() == 1) // 1 DIGIT NUMBER
					{
						converted += digits(input);
						input = "";
					}
					pos++;
				} else if (pos == 2) // HUNDRED POSITION
				{
					String temp = input.substring(input.length() - 1, input.length());
					input = input.substring(0, input.length() - 1);
					if (converted.length() > 0 && digits(temp) != "") {
						converted = (digits(temp) + maxs[pos] + " and") + converted;
						hun = true;
					} else {
						if (digits(temp) == "")
							;
						else
							converted = (digits(temp) + maxs[pos]) + converted;
						hun = true;
					}
					pos++;
				} else if (pos > 2) // REMAINING NUMBERS PAIRED BY TWO
				{
					if (input.length() >= 2) // EXTRACT 2 DIGITS
					{
						String temp = input.substring(input.length() - 2, input.length());
						input = input.substring(0, input.length() - 2);
						if (!hun && converted.length() > 0)
							converted = digits(temp) + maxs[pos] + " and" + converted;
						else {
							if (digits(temp) == "")
								;
							else
								converted = digits(temp) + maxs[pos] + converted;
						}
					} else if (input.length() == 1) // EXTRACT 1 DIGIT
					{
						if (!hun && converted.length() > 0)
							converted = digits(input) + maxs[pos] + " and" + converted;
						else {
							if (digits(input) == "")
								;
							else
								converted = digits(input) + maxs[pos] + converted;
							input = "";
						}
					}
					pos++;
				}
			}
			return converted;
		}
	}

	public String convertLessThanOneThousand(int number, String[] numName, String[] tenNames) {
		String current;

		if (number % 100 < 20) {
			current = numName[number % 100];
			number /= 100;
		} else {
			current = numName[number % 10];
			number /= 10;

			current = tenNames[number % 10] + current;
			number /= 10;
		}
		if (number == 0)
			return current;
		return numName[number] + " Hundred" + current;
	}

	public String convertNumberToWordsIssuetoOtheSite(String n) {
		String converted = "";
		double d = Double.valueOf(n);
		d = Math.ceil(d);
		int i = (int) d;
		converted = new DateToWord().convertNumberToWords(i);
		/*
		 * try{ input = n;
		 * 
		 * int pos = 1; boolean hun = false; while (input.length() > 0) { if (pos == 1)
		 * // TENS AND UNIT POSITION { if (input.length() >= 2) // TWO DIGIT NUMBERS {
		 * String temp = input.substring(input.length() - 2, input.length()); input =
		 * input.substring(0, input.length() - 2); converted += digits(temp); } else if
		 * (input.length() == 1) // 1 DIGIT NUMBER { converted += digits(input); input =
		 * ""; } pos++; } else if (pos == 2) // HUNDRED POSITION { String temp =
		 * input.substring(input.length() - 1, input.length()); input =
		 * input.substring(0, input.length() - 1); if (converted.length() > 0 &&
		 * digits(temp) != "") { converted = (digits(temp) + maxs[pos] + " and") +
		 * converted; hun = true; } else { if (digits(temp) == "") ; else converted =
		 * (digits(temp) + maxs[pos]) + converted; hun = true; } pos++; } else if (pos >
		 * 2) // REMAINING NUMBERS PAIRED BY TWO { if (input.length() >= 2) // EXTRACT 2
		 * DIGITS { String temp = input.substring(input.length() - 2, input.length());
		 * input = input.substring(0, input.length() - 2); if (!hun &&
		 * converted.length() > 0) converted = digits(temp) + maxs[pos] + " and" +
		 * converted; else { if (digits(temp) == "") ; else converted = digits(temp) +
		 * maxs[pos] + converted; } } else if (input.length() == 1) // EXTRACT 1 DIGIT {
		 * if (!hun && converted.length() > 0) converted = digits(input) + maxs[pos] +
		 * " and" + converted; else { if (digits(input) == "") ; else converted =
		 * digits(input) + maxs[pos] + converted; input = ""; } } pos++; } }
		 * }catch(Exception e){ e.printStackTrace(); }
		 */
		return converted;
	}

	private String digits(String temp) // TO RETURN SELECTED NUMBERS IN WORDS
	{
		String converted = "";
		for (int i = temp.length() - 1; i >= 0; i--) {
			int ch = temp.charAt(i) - 48;
			if (i == 0 && ch > 1 && temp.length() > 1)
				converted = tens[ch - 2] + converted; // IF TENS DIGIT STARTS
			// WITH 2 OR MORE IT
			// FALLS UNDER TENS
			else if (i == 0 && ch == 1 && temp.length() == 2) // IF TENS DIGIT
			// STARTS WITH 1
			// IT FALLS
			// UNDER TEENS
			{
				int sum = 0;
				for (int j = 0; j < 2; j++)
					sum = (sum * 10) + (temp.charAt(j) - 48);
				return teen[sum - 10];
			} else {
				if (ch > 0)
					converted = units[ch] + converted;
			} // IF SINGLE DIGIT PROVIDED
		}
		return converted;
	}

	private String numToString(long x) // CONVERT THE NUMBER TO STRING
	{
		String num = "";
		while (x != 0) {
			num = ((char) ((x % 10) + 48)) + num;
			x /= 10;
		}
		return num;
	}

	@SuppressWarnings("unused")
	private String getTheAmountInWords(Double amountToConvertInWords, int roundingModeSize, RoundingMode roundingMode) {
		String amount = BigDecimal.valueOf(amountToConvertInWords).setScale(roundingModeSize, roundingMode).toString();
		// System.out.println(amount+"\t first " +amountToConvertInWords);
		String[] splitedAmount = amount.split("\\.");
		StringBuffer amountInWords = new StringBuffer(convertNumberToWords(
				new BigDecimal(splitedAmount[0]).setScale(roundingModeSize, roundingMode).longValue()));
		StringBuffer amountInWordsPaisa = new StringBuffer("");
		if (splitedAmount.length > 1 && Double.valueOf(splitedAmount[1]) != 0) {
			amountInWordsPaisa = new StringBuffer(" Rupees And ").append(convertNumberToWords(
					new BigDecimal(splitedAmount[1]).setScale(roundingModeSize, roundingMode).longValue())
					+ " Paisa Only.");
		} else {
			amountInWords.append(" Rupees Only.");
		}
		return amountInWords.append(amountInWordsPaisa).toString();
	}
	
	public static String convertTimestampToString(Timestamp timestamp ) {
	//	LOGGER.info("**** The control is inside the convertTimestampToString in DateTimeUtil *****");
		 DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	     Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(timestamp.getTime());
		 String date = formatter.format(calendar.getTime()); 
	//	LOGGER.info("**** The date String is ****"+date);	
	    return date;
	}

}
