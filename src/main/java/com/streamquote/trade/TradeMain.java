package com.streamquote.trade;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.streamquote.app.TradingHolidays;
import com.streamquote.app.ZStreamingConfig;

public class TradeMain {
	public static void main(String[] args) {
		String apiKey = "abcd51hdgns"; // API KEY
		String userId = "DR1234"; // USER ID
		String publicToken = "asljfldlncnl093nnnzc4"; // PUBLIC TOKEN

		DateFormat dtFmt1 = new SimpleDateFormat("yyyy-MM-dd");
		dtFmt1.setTimeZone(TimeZone.getTimeZone("IST"));

		DateFormat dtFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dtFmt2.setTimeZone(TimeZone.getTimeZone("IST"));

		DateFormat dtFmt3 = new SimpleDateFormat("HH:mm:ss");
		dtFmt3.setTimeZone(TimeZone.getTimeZone("IST"));
		Date timeRef = null;
		Date timeTracker = new Date();

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
				List<Object> tradableSignals = checkDBifSignalGenerated(dtFmt3
						.format(timeTracker));
				timeTracker = new Date();
				placeTradeOrder(tradableSignals);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timeRef.before(new Date())) {
					runnable = false;
				}
			}
		}
	}

	private static void placeTradeOrder(List<Object> tradableSignals) {
		// TODO Auto-generated method stub

	}

	private static List<Object> checkDBifSignalGenerated(String format) {
		return null;
		// TODO Auto-generated method stub
	}
}
