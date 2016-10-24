package com.streamquote.strategies.sample;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Buy and hold criterion.
 * <p>
 * 
 * @see <a
 *      href="http://en.wikipedia.org/wiki/Buy_and_hold">http://en.wikipedia.org/wiki/Buy_and_hold</a>
 */
public class BuyAndHoldCriterion extends AbstractAnalysisCriterion {

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		return series.getTick(series.getEnd()).getClosePrice()
				.dividedBy(series.getTick(series.getBegin()).getClosePrice())
				.toDouble();
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		int entryIndex = trade.getEntry().getIndex();
		int exitIndex = trade.getExit().getIndex();

		if (trade.getEntry().isBuy()) {
			return series.getTick(exitIndex).getClosePrice()
					.dividedBy(series.getTick(entryIndex).getClosePrice())
					.toDouble();
		} else {
			return series.getTick(entryIndex).getClosePrice()
					.dividedBy(series.getTick(exitIndex).getClosePrice())
					.toDouble();
		}
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 > criterionValue2;
	}
}
