package com.streamquote.strategies.sample.strategies;

import com.streamquote.strategies.ClosePriceIndicator;
import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Rule;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.TradingRecord;
import com.streamquote.strategies.indicators.oscillators.StochasticOscillatorKIndicator;
import com.streamquote.strategies.indicators.trackers.EMAIndicator;
import com.streamquote.strategies.indicators.trackers.MACDIndicator;
import com.streamquote.strategies.rules.CrossedDownIndicatorRule;
import com.streamquote.strategies.rules.CrossedUpIndicatorRule;
import com.streamquote.strategies.rules.OverIndicatorRule;
import com.streamquote.strategies.rules.UnderIndicatorRule;
import com.streamquote.strategies.sample.TotalProfitCriterion;
import com.streamquote.strategies.sample.loader.CsvTradesLoader;

/**
 * Moving momentum strategy.
 * <p>
 * 
 * @see http://stockcharts.com/help/doku.php?id=chart_school:trading_strategies:
 *      moving_momentum
 */
public class MovingMomentumStrategy {

	/**
	 * @param series
	 *            a time series
	 * @return a moving momentum strategy
	 */
	public static Strategy buildStrategy(StockPriceSeries series) {
		if (series == null) {
			throw new IllegalArgumentException("Series cannot be null");
		}

		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

		// The bias is bullish when the shorter-moving average moves above the
		// longer moving average.
		// The bias is bearish when the shorter-moving average moves below the
		// longer moving average.
		EMAIndicator shortEma = new EMAIndicator(closePrice, 9);
		EMAIndicator longEma = new EMAIndicator(closePrice, 26);

		StochasticOscillatorKIndicator stochasticOscillK = new StochasticOscillatorKIndicator(
				series, 14);

		MACDIndicator macd = new MACDIndicator(closePrice, 9, 26);
		EMAIndicator emaMacd = new EMAIndicator(macd, 18);

		// Entry rule
		Rule entryRule = new OverIndicatorRule(shortEma, longEma) // Trend
				.and(new CrossedDownIndicatorRule(stochasticOscillK, Decimal
						.valueOf(20))) // Signal 1
				.and(new OverIndicatorRule(macd, emaMacd)); // Signal 2

		// Exit rule
		Rule exitRule = new UnderIndicatorRule(shortEma, longEma) // Trend
				.and(new CrossedUpIndicatorRule(stochasticOscillK, Decimal
						.valueOf(80))) // Signal 1
				.and(new UnderIndicatorRule(macd, emaMacd)); // Signal 2

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
