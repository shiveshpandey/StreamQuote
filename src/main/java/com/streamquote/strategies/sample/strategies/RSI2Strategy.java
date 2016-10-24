package com.streamquote.strategies.sample.strategies;

import com.streamquote.strategies.ClosePriceIndicator;
import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Rule;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.TradingRecord;
import com.streamquote.strategies.indicators.trackers.RSIIndicator;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;
import com.streamquote.strategies.rules.CrossedDownIndicatorRule;
import com.streamquote.strategies.rules.CrossedUpIndicatorRule;
import com.streamquote.strategies.rules.OverIndicatorRule;
import com.streamquote.strategies.rules.UnderIndicatorRule;
import com.streamquote.strategies.sample.TotalProfitCriterion;
import com.streamquote.strategies.sample.loader.CsvTradesLoader;

/**
 * 2-Period RSI Strategy
 * <p>
 * 
 * @see http
 *      ://stockcharts.com/school/doku.php?id=chart_school:trading_strategies
 *      :rsi2
 */
public class RSI2Strategy {

	/**
	 * @param series
	 *            a time series
	 * @return a 2-period RSI strategy
	 */
	public static Strategy buildStrategy(StockPriceSeries series) {
		if (series == null) {
			throw new IllegalArgumentException("Series cannot be null");
		}

		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
		SMAIndicator longSma = new SMAIndicator(closePrice, 200);

		// We use a 2-period RSI indicator to identify buying
		// or selling opportunities within the bigger trend.
		RSIIndicator rsi = new RSIIndicator(closePrice, 2);

		// Entry rule
		// The long-term trend is up when a security is above its 200-period
		// SMA.
		Rule entryRule = new OverIndicatorRule(shortSma, longSma) // Trend
				.and(new CrossedDownIndicatorRule(rsi, Decimal.valueOf(5))) // Signal
																			// 1
				.and(new OverIndicatorRule(shortSma, closePrice)); // Signal 2

		// Exit rule
		// The long-term trend is down when a security is below its 200-period
		// SMA.
		Rule exitRule = new UnderIndicatorRule(shortSma, longSma) // Trend
				.and(new CrossedUpIndicatorRule(rsi, Decimal.valueOf(95))) // Signal
																			// 1
				.and(new UnderIndicatorRule(shortSma, closePrice)); // Signal 2

		// TODO: Finalize the strategy

		return new Strategy(entryRule, exitRule);
	}

	public static void main(String[] args) {

		// Getting the time series
		StockPriceSeries series = CsvTradesLoader.loadBitstampSeries();

		// Building the trading strategy
		Strategy strategy = buildStrategy(series);

		// Running the strategy
		TradingRecord tradingRecord = series.run(strategy);
		System.out.println("Number of trades for the strategy: "
				+ tradingRecord.getTradeCount());

		// Analysis
		System.out.println("Total profit for the strategy: "
				+ new TotalProfitCriterion().calculate(series, tradingRecord));
	}

}
