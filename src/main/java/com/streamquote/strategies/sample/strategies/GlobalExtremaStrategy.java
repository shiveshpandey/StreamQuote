package com.streamquote.strategies.sample.strategies;

import com.streamquote.strategies.ClosePriceIndicator;
import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.HighestValueIndicator;
import com.streamquote.strategies.LowestValueIndicator;
import com.streamquote.strategies.MaxPriceIndicator;
import com.streamquote.strategies.MinPriceIndicator;
import com.streamquote.strategies.Rule;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.TradingRecord;
import com.streamquote.strategies.indicators.MultiplierIndicator;
import com.streamquote.strategies.rules.OverIndicatorRule;
import com.streamquote.strategies.rules.UnderIndicatorRule;
import com.streamquote.strategies.sample.TotalProfitCriterion;
import com.streamquote.strategies.sample.loader.CsvTradesLoader;

/**
 * Strategies which compares current price to global extrema over a week.
 */
public class GlobalExtremaStrategy {

	// We assume that there were at least one trade every 5 minutes during the
	// whole week
	private static final int NB_TICKS_PER_WEEK = 12 * 24 * 7;

	/**
	 * @param series
	 *            a time series
	 * @return a global extrema strategy
	 */
	public static Strategy buildStrategy(StockPriceSeries series) {
		if (series == null) {
			throw new IllegalArgumentException("Series cannot be null");
		}

		ClosePriceIndicator closePrices = new ClosePriceIndicator(series);

		// Getting the max price over the past week
		MaxPriceIndicator maxPrices = new MaxPriceIndicator(series);
		HighestValueIndicator weekMaxPrice = new HighestValueIndicator(
				maxPrices, NB_TICKS_PER_WEEK);
		// Getting the min price over the past week
		MinPriceIndicator minPrices = new MinPriceIndicator(series);
		LowestValueIndicator weekMinPrice = new LowestValueIndicator(minPrices,
				NB_TICKS_PER_WEEK);

		// Going long if the close price goes below the min price
		MultiplierIndicator downWeek = new MultiplierIndicator(weekMinPrice,
				Decimal.valueOf("1.004"));
		Rule buyingRule = new UnderIndicatorRule(closePrices, downWeek);

		// Going short if the close price goes above the max price
		MultiplierIndicator upWeek = new MultiplierIndicator(weekMaxPrice,
				Decimal.valueOf("0.996"));
		Rule sellingRule = new OverIndicatorRule(closePrices, upWeek);

		return new Strategy(buyingRule, sellingRule);
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
