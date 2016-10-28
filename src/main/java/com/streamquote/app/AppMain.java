package com.streamquote.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppMain {
	public static void main(String[] args) {
		String apiKey = "abcd51hdgns"; // API KEY
		String userId = "DR1234"; // USER ID
		String publicToken = "asljfldlncnl093nnnzc4"; // PUBLIC TOKEN

		DateFormat dtFmt1 = new SimpleDateFormat("yyyy-MM-dd");
		dtFmt1.setTimeZone(TimeZone.getTimeZone("IST"));

		DateFormat dtFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dtFmt2.setTimeZone(TimeZone.getTimeZone("IST"));
		Date timeRef = null;
		try {
			timeRef = dtFmt2.parse(dtFmt1.format(Calendar.getInstance(
					TimeZone.getTimeZone("IST")).getTime())
					+ " " + ZStreamingConfig.getStreamingQuoteEndTime());
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		boolean runnable = true;
		if (!TradingHolidays.isHoliday()) {

			while (runnable) {
				ZStreamingQuoteControl.getInstance().start(apiKey, userId,
						publicToken);

				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timeRef.before(new Date())) {
					runnable = false;
				}
			}
		}
		// ZStreamingQuoteControl.getInstance().stop();
	}
}
