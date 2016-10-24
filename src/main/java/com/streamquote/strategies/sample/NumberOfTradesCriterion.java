package com.streamquote.strategies.sample;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Number of trades criterion.
 */
public class NumberOfTradesCriterion extends AbstractAnalysisCriterion {

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		return tradingRecord.getTradeCount();
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		return 1d;
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 < criterionValue2;
	}
}
