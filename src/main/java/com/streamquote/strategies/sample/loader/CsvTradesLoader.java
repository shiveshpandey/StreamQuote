package com.streamquote.strategies.sample.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Tick;

/**
 * This class build a Ta4j time series from a CSV file containing trades.
 */
public class CsvTradesLoader {

	/**
	 * @return a time series from Bitstamp (bitcoin exchange) trades
	 * @throws IOException
	 */
	public static StockPriceSeries loadBitstampSeries() {

		// Reading all lines of the CSV file
		BufferedReader bufferedReader = null;
		List<String[]> lines = null;

		try {
			FileReader fileReader = new FileReader(
					"appleinc_ticks_from_20130101_usd.csv");
			bufferedReader = new BufferedReader(fileReader);
			String readline;
			while ((readline = bufferedReader.readLine()) != null) {
				lines.add(readline.split(","));
			}
			lines.remove(0); // Removing header line
		} catch (IOException ioe) {
			Logger.getLogger(CsvTradesLoader.class.getName()).log(Level.SEVERE,
					"Unable to load trades from CSV", ioe);
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Tick> ticks = null;
		if ((lines != null) && !lines.isEmpty()) {

			// Getting the first and last trades timestamps
			Date beginTime = new Date(Long.parseLong(lines.get(0)[0]) * 1000);
			Date endTime = new Date(
					Long.parseLong(lines.get(lines.size() - 1)[0]) * 1000);
			if (beginTime.after(endTime)) {
				beginTime = endTime;
				endTime = beginTime;
			}
			// Building the empty ticks (every 300 seconds, yeah welcome in
			// Bitcoin world)
			ticks = buildEmptyTicks(beginTime, endTime, 300);
			// Filling the ticks with trades
			for (String[] tradeLine : lines) {
				Date tradeTimestamp = new Date(
						Long.parseLong(tradeLine[0]) * 1000);
				for (Tick tick : ticks) {
					if (tick.inPeriod(tradeTimestamp)) {
						double tradePrice = Double.parseDouble(tradeLine[1]);
						double tradeAmount = Double.parseDouble(tradeLine[2]);
						tick.addTrade(tradeAmount, tradePrice);
					}
				}
			}
			// Removing still empty ticks
			removeEmptyTicks(ticks);
		}

		return new StockPriceSeries("bitstamp_trades", ticks);
	}

	/**
	 * Builds a list of empty ticks.
	 * 
	 * @param beginTime
	 *            the begin time of the whole period
	 * @param endTime
	 *            the end time of the whole period
	 * @param duration
	 *            the tick duration (in seconds)
	 * @return the list of empty ticks
	 */
	private static List<Tick> buildEmptyTicks(Date beginTime, Date endTime,
			int duration) {

		List<Tick> emptyTicks = new ArrayList<Tick>();

		Date tickEndTime = beginTime;
		do {
			tickEndTime.setSeconds(tickEndTime.getSeconds() + duration);
			emptyTicks.add(new Tick(duration, tickEndTime));
		} while (tickEndTime.before(endTime));

		return emptyTicks;
	}

	/**
	 * Removes all empty (i.e. with no trade) ticks of the list.
	 * 
	 * @param ticks
	 *            a list of ticks
	 */
	private static void removeEmptyTicks(List<Tick> ticks) {
		for (int i = ticks.size() - 1; i >= 0; i--) {
			if (ticks.get(i).getTrades() == 0) {
				ticks.remove(i);
			}
		}
	}

	public static void main(String[] args) {
		StockPriceSeries series;
		series = CsvTradesLoader.loadBitstampSeries();

		System.out.println("Series: " + series.getStockName() + " ("
				+ series.getSeriesPeriodDescription() + ")");
		System.out.println("Number of ticks: " + series.getTickCount());
		System.out.println("First tick: \n" + "\tVolume: "
				+ series.getTick(0).getVolume() + "\n" + "\tNumber of trades: "
				+ series.getTick(0).getTrades() + "\n" + "\tClose price: "
				+ series.getTick(0).getClosePrice());

	}
}
