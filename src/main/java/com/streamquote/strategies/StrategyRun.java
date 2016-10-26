package com.streamquote.strategies;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.streamquote.app.ZStreamingConfig;
import com.streamquote.app.ZStreamingQuoteControl;
import com.streamquote.model.OHLCquote;

public class StrategyRun {
	static List<StockPriceSeriesListing> stockList = new ArrayList<StockPriceSeriesListing>();

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
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

		Date startTime = new Date();
		Date endTime = new Date();

		startTime.setHours(9);
		startTime.setMinutes(15);
		startTime.setSeconds(0);

		List<String> instrumentNameList = ZStreamingQuoteControl.getInstance()
				.getInstrumentTokensList();

		boolean runnable = true;
		boolean isFirstRun = true;

		while (runnable) {

			for (int j = 0; j < instrumentNameList.size(); j++) {

				if (isFirstRun) {
					StockPriceSeriesListing stock = new StockPriceSeriesListing();
					stock.setStockName(instrumentNameList.get(j));
					stockList.add(stock);
				}
				OHLCquote getQuote = ZStreamingQuoteControl.getInstance()
						.getOHLCDataByTimeRange(instrumentNameList.get(j),
								startTime.toString(), endTime.toString());
				StockPriceSeriesListing stock = getStockByName(instrumentNameList
						.get(j));
				stock.addNewTick(
						new Date(),
						Decimal.valueOf(getQuote.getOpenPrice().doubleValue()),
						Decimal.valueOf(getQuote.getClosePrice().doubleValue()),
						Decimal.valueOf(getQuote.getHighPrice().doubleValue()),
						Decimal.valueOf(getQuote.getVol()));
				stockList.add(stock);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startTime.setSeconds(endTime.getSeconds() + 1 * 1000);
			endTime.setSeconds(endTime.getSeconds() + 60 * 1000);

			if (timeRef.before(new Date())) {
				runnable = false;
			}
		}
		System.out.println(stockList.size());
	}

	private static StockPriceSeriesListing getStockByName(String string) {
		StockPriceSeriesListing stockPrice = null;
		for (int i = 0; i < stockList.size(); i++) {
			if (string.equalsIgnoreCase(stockList.get(i).getStockName())) {
				stockPrice = stockList.get(i);
				stockList.remove(stockList.get(i));
				return stockPrice;
			}
		}
		return null;
	}
}
