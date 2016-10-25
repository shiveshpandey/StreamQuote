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
					"/home/shiva/git/ta4j/ta4j-examples/src/main/resources/appleinc_ticks_from_20130101_usd.csv");
			bufferedReader = new BufferedReader(fileReader);
			String[] line;
			String readline;
			int i = 0;

			while ((readline = bufferedReader.readLine()) != null) {
				line = readline.split(",");
				if (i == 0) {
					i += 1;
					continue;
				}
				String[] temp = line[0].split("-");
				Date date = new Date();
				date.setYear(Integer.parseInt(temp[0]));
				date.setMonth(Integer.parseInt(temp[1]) + 1);
				date.setDate(Integer.parseInt(temp[2]));
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

		System.out.println("Series: " + series.getName() + " ("
				+ series.getSeriesPeriodDescription() + ")");
		System.out.println("Number of ticks: " + series.getTickCount());
		System.out.println("First tick: \n" + "\tVolume: "
				+ series.getTick(0).getVolume() + "\n" + "\tOpen price: "
				+ series.getTick(0).getOpenPrice() + "\n" + "\tClose price: "
				+ series.getTick(0).getClosePrice());

	}
}
