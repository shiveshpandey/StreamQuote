package com.streamquote.strategies.sample;

import java.util.List;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * An analysis criterion.
 * <p>
 * Can be used to:
 * <ul>
 * <li>Analyze the performance of a {@link Strategy strategy}
 * <li>Compare several {@link Strategy strategies} together
 * </ul>
 */
public interface AnalysisCriterion {

	/**
	 * @param series
	 *            a time series
	 * @param trade
	 *            a trade
	 * @return the criterion value for the trade
	 */
	double calculate(StockPriceSeries series, Trade trade);

	/**
	 * @param series
	 *            a time series
	 * @param tradingRecord
	 *            a trading record
	 * @return the criterion value for the trades
	 */
	double calculate(StockPriceSeries series, TradingRecord tradingRecord);

	/**
	 * @param series
	 *            the time series
	 * @param strategies
	 *            a list of strategies
	 * @return the best strategy (among the provided ones) according to the
	 *         criterion
	 */
	Strategy chooseBest(StockPriceSeries series, List<Strategy> strategies);

	/**
	 * @param criterionValue1
	 *            the first value
	 * @param criterionValue2
	 *            the second value
	 * @return true if the first value is better than (according to the
	 *         criterion) the second one, false otherwise
	 */
	boolean betterThan(double criterionValue1, double criterionValue2);
}