package com.streamquote.strategies.sample.strategies;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Rule;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.TradingRecord;
import com.streamquote.strategies.indicators.oscillators.CCIIndicator;
import com.streamquote.strategies.rules.OverIndicatorRule;
import com.streamquote.strategies.rules.UnderIndicatorRule;
import com.streamquote.strategies.sample.TotalProfitCriterion;
import com.streamquote.strategies.sample.loader.CsvTradesLoader;

/**
 * CCI Correction Strategy
 * <p>
 * 
 * @see http
 *      ://stockcharts.com/school/doku.php?id=chart_school:trading_strategies
 *      :cci_correction
 */
public class CCICorrectionStrategy {

	/**
	 * @param series
	 *            a time series
	 * @return a CCI correction strategy
	 */
	public static Strategy buildStrategy(StockPriceSeries series) {
		if (series == null) {
			throw new IllegalArgumentException("Series cannot be null");
		}

		CCIIndicator longCci = new CCIIndicator(series, 200);
		CCIIndicator shortCci = new CCIIndicator(series, 5);
		Decimal plus100 = Decimal.HUNDRED;
		Decimal minus100 = Decimal.valueOf(-100);

		Rule entryRule = new OverIndicatorRule(longCci, plus100) // Bull trend
				.and(new UnderIndicatorRule(shortCci, minus100)); // Signal

		Rule exitRule = new UnderIndicatorRule(longCci, minus100) // Bear trend
				.and(new OverIndicatorRule(shortCci, plus100)); // Signal

		Strategy strategy = new Strategy(entryRule, exitRule);
		strategy.setUnstablePeriod(5);
		return strategy;
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
