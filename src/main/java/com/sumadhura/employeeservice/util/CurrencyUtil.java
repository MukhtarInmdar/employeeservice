package com.sumadhura.employeeservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * CurrencyUtil class provides CurrencyUtil specific services.
 * 
 * @author Venkat_Koniki
 * @since 27.03.2019
 * @time 06:26PM
 */
@Component
public class CurrencyUtil {

	private final Logger LOGGER = Logger.getLogger(CurrencyUtil.class);

	public String convertUstoInFormat(String currency) {
		LOGGER.info("***** Control inside the CurrencyUtil.convertUstoInFormat() *****");
		Format format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		return format.format(new BigDecimal(currency)).substring(1);
	}
	
	public Double convertCommaSeperatedAmountInDouble(String currency) throws Exception {
		LOGGER.info("***** Control inside the CurrencyUtil.convertCommaSeperatedAmountInDouble() *****");
		//Format format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		return com.ibm.icu.text.NumberFormat.getNumberInstance(java.util.Locale.US).parse(currency).doubleValue();
	}

	public String getTheAmountWithCommas(Double amountToConvertInWords, int roundingModeSize, RoundingMode roundingMode) {
		return convertUstoInFormat(BigDecimal.valueOf(amountToConvertInWords).setScale(roundingModeSize, roundingMode).toString());
	}

	public double roundPercentage(double percentage) {
		return Math.round(percentage * 10.0) / 10.0;
	}
	
	public static void main(String[] args) {
		/*System.out.println(new CurrencyUtil().convertUstoInFormat(BigDecimal.valueOf(112100).setScale(roundingModeSize, roundingMode).toString()));
			System.out.println(new CurrencyUtil().convertUstoInFormat("112100"));*/
	}
}
