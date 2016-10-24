package com.streamquote.strategies.sample;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Maximum drawdown criterion.
 * <p>
 * 
 * @see <a
 *      href="http://en.wikipedia.org/wiki/Drawdown_%28economics%29">http://en
 *      .wikipedia.org/wiki/Drawdown_%28economics%29</a>
 */
public class MaximumDrawdownCriterion extends AbstractAnalysisCriterion {

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		CashFlow cashFlow = new CashFlow(series, tradingRecord);
		Decimal maximumDrawdown = calculateMaximumDrawdown(series, cashFlow);
		return maximumDrawdown.toDouble();
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		if (trade != null && trade.getEntry() != null
				&& trade.getExit() != null) {
			CashFlow cashFlow = new CashFlow(series, trade);
			Decimal maximumDrawdown = calculateMaximumDrawdown(series, cashFlow);
			return maximumDrawdown.toDouble();
		}
		return 0;
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 < criterionValue2;
	}

	/**
	 * Calculates the maximum drawdown from a cash flow over a series.
	 * 
	 * @param series
	 *            the time series
	 * @param cashFlow
	 *            the cash flow
	 * @return the maximum drawdown from a cash flow over a series
	 */
	private Decimal calculateMaximumDrawdown(StockPriceSeries series,
			CashFlow cashFlow) {
		Decimal maximumDrawdown = Decimal.ZERO;
		Decimal maxPeak = Decimal.ZERO;
		for (int i = series.getBegin(); i <= series.getEnd(); i++) {
			Decimal value = cashFlow.getValue(i);
			if (value.isGreaterThan(maxPeak)) {
				maxPeak = value;
			}

			Decimal drawdown = maxPeak.minus(value).dividedBy(maxPeak);
			if (drawdown.isGreaterThan(maximumDrawdown)) {
				maximumDrawdown = drawdown;
			}
		}
		return maximumDrawdown;
	}
}
