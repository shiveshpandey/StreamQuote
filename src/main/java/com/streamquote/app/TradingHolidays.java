package com.streamquote.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TradingHolidays {

	/**
	 * method to check if today is trading holiday or not
	 * 
	 * @return boolean
	 */
	public static boolean isHoliday() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		TimeZone timeZone = TimeZone.getTimeZone("IST");
		dateFormat.setTimeZone(timeZone);
		String todayString = dateFormat.format(Calendar.getInstance(timeZone)
				.getTime());
		System.out.println("TradingHolidays.isHoliday(): Trading Date: ["
				+ todayString + "]");

		String tradingHolidaysArr[] = ZStreamingConfig.getTradingHolidays();
		try {
			Date today = dateFormat.parse(todayString);
			for (String tradingDay : tradingHolidaysArr) {
				Date refDay = dateFormat.parse(tradingDay);
				if (today.compareTo(refDay) == 0) {
					System.out
							.println("TradingHolidays.isHoliday(): ITS A TRADING HOLIDAY !!!");
					return true;
				}
			}
		} catch (ParseException e) {
			System.out
					.println("TradingHolidays.isHoliday(): ERROR: ParseException on parsing date !!!");
			e.printStackTrace();
			return true;
		}
		return false;
	}
}
