package com.streamquote.strategies.sample;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Number of ticks criterion.
 * <p>
 * Returns the number of ticks during the provided trade(s).
 */
public class NumberOfTicksCriterion extends AbstractAnalysisCriterion {

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		int nTicks = 0;
		for (Trade trade : tradingRecord.getTrades()) {
			nTicks += calculate(series, trade);
		}
		return nTicks;
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		return (1 + trade.getExit().getIndex()) - trade.getEntry().getIndex();
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 < criterionValue2;
	}
}
