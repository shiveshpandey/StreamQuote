package com.streamquote.strategies;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
import com.streamquote.dao.IStreamingQuoteStorage;
import com.streamquote.dao.StreamingQuoteStorageFactory;
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
		DateFormat dtFmt3 = new SimpleDateFormat("HH:mm:ss");
		dtFmt3.setTimeZone(TimeZone.getTimeZone("IST"));
		Date timeRef = null;

		DateFormat quoteTableDtFmt = new SimpleDateFormat("ddMMyyyy");
		quoteTableDtFmt.setTimeZone(TimeZone.getTimeZone("IST"));
		String date = quoteTableDtFmt.format(Calendar.getInstance(
				TimeZone.getTimeZone("IST")).getTime());

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
		IStreamingQuoteStorage streamingQuoteStorage = StreamingQuoteStorageFactory
				.getStreamingQuoteStorage();
		streamingQuoteStorage.initializeJDBCConn();
		streamingQuoteStorage.setQuoteTableName(date);
		int i = 0;
		while (runnable) {

			for (int j = 0; j < instrumentNameList.size(); j++) {

				if (isFirstRun) {
					StockPriceSeriesListing stock = new StockPriceSeriesListing();
					stock.setStockName(instrumentNameList.get(j));
					stockList.add(stock);
				}
				OHLCquote getQuote = streamingQuoteStorage
						.getOHLCDataByTimeRange(instrumentNameList.get(j),
								dtFmt3.format(startTime),
								dtFmt3.format(endTime));

				StockPriceSeriesListing stock = getStockByName(instrumentNameList
						.get(j));
				if (null != getQuote)
					stock.addNewTick(new Date(), Decimal.valueOf(getQuote
							.getOpenPrice().doubleValue()), Decimal
							.valueOf(getQuote.getClosePrice().doubleValue()),
							Decimal.valueOf(getQuote.getHighPrice()
									.doubleValue()), Decimal.valueOf(getQuote
									.getVol()), streamingQuoteStorage);
				stockList.add(stock);
				writeToFile(stock);
			}
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startTime.setSeconds(endTime.getSeconds() + 1);
			endTime.setSeconds(endTime.getSeconds() + 20);

			if (timeRef.before(new Date())) {
				runnable = false;
			}
			System.out.println(i++);
		}
	}

	private static void writeToFile(StockPriceSeriesListing stock) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("/home/shiva/abc.txt", true), "utf-8"));
			for (StockTicker tick : stock.getTicks()) {
				writer.write(tick.getClosePrice() + "," + tick.getAwesome()
						+ "," + tick.getEma1() + "," + tick.getEma2() + ","
						+ tick.getEma3() + "," + tick.getEmaD() + ","
						+ tick.getEmaT() + "," + tick.getLastTradedPrice()
						+ "," + tick.getMacd() + "," + tick.getOpenPrice()
						+ "," + tick.getRsi() + "," + tick.getSma1() + ","
						+ tick.getSma2() + "," + tick.getSma3() + ","
						+ tick.getTimeStamp() + "," + tick.getVolume() + "\n\n");
			}

		} catch (IOException ex) {
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
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
		return new StockPriceSeriesListing();
	}
}
