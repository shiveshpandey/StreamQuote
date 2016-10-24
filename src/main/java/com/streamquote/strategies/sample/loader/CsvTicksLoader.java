package com.streamquote.strategies.sample.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Tick;

/**
 * This class build a Ta4j time series from a CSV file containing ticks.
 */
public class CsvTicksLoader {

	/**
	 * @return a time series from Apple Inc. ticks.
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static StockPriceSeries loadAppleIncSeries() {

		List<Tick> ticks = new ArrayList<Tick>();
		BufferedReader bufferedReader = null;
		try {
			FileReader fileReader = new FileReader(
					"appleinc_ticks_from_20130101_usd.csv");
			bufferedReader = new BufferedReader(fileReader);
			String[] line;
			String readline;

			while ((readline = bufferedReader.readLine()) != null) {
				line = readline.split(",");
				Date date = new Date(Date.parse(line[0]));
				Decimal open = Decimal.valueOf(Double.parseDouble(line[1]));
				Decimal high = Decimal.valueOf(Double.parseDouble(line[2]));
				Decimal low = Decimal.valueOf(Double.parseDouble(line[3]));
				Decimal close = Decimal.valueOf(Double.parseDouble(line[4]));
				Decimal volume = Decimal.valueOf(Double.parseDouble(line[5]));

				ticks.add(new Tick(date, open, high, low, close, volume));
			}
		} catch (IOException ioe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE,
					"Unable to load ticks from CSV", ioe);
		} catch (NumberFormatException nfe) {
			Logger.getLogger(CsvTicksLoader.class.getName()).log(Level.SEVERE,
					"Error while parsing value", nfe);
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new StockPriceSeries("apple_ticks", ticks);
	}

	public static void main(String[] args) {
		StockPriceSeries series = CsvTicksLoader.loadAppleIncSeries();

		System.out.println("Series: " + series.getStockName() + " ("
				+ series.getSeriesPeriodDescription() + ")");
		System.out.println("Number of ticks: " + series.getTickCount());
		System.out.println("First tick: \n" + "\tVolume: "
				+ series.getTick(0).getVolume() + "\n" + "\tOpen price: "
				+ series.getTick(0).getOpenPrice() + "\n" + "\tClose price: "
				+ series.getTick(0).getClosePrice());

	}
}
