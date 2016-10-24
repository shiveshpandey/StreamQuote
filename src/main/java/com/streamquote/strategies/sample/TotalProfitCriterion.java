package com.streamquote.strategies.sample;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Total profit criterion.
 * <p>
 * The total profit of the provided {@link Trade trade(s)} over the provided
 * {@link StockPriceSeries series}.
 */
public class TotalProfitCriterion extends AbstractAnalysisCriterion {

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		double value = 1d;
		for (Trade trade : tradingRecord.getTrades()) {
			value *= calculateProfit(series, trade);
		}
		return value;
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		return calculateProfit(series, trade);
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 > criterionValue2;
	}

	/**
	 * Calculates the profit of a trade (Buy and sell).
	 * 
	 * @param series
	 *            a time series
	 * @param trade
	 *            a trade
	 * @return the profit of the trade
	 */
	private double calculateProfit(StockPriceSeries series, Trade trade) {
		Decimal profit = Decimal.ONE;
		if (trade.isClosed()) {
			Decimal exitClosePrice = series.getTick(trade.getExit().getIndex())
					.getClosePrice();
			Decimal entryClosePrice = series.getTick(
					trade.getEntry().getIndex()).getClosePrice();

			if (trade.getEntry().isBuy()) {
				profit = exitClosePrice.dividedBy(entryClosePrice);
			} else {
				profit = entryClosePrice.dividedBy(exitClosePrice);
			}
		}
		return profit.toDouble();
	}
}
