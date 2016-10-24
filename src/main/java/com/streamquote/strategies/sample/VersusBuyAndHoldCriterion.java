package com.streamquote.strategies.sample;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Versus "buy and hold" criterion.
 * <p>
 * Compares the value of a provided {@link AnalysisCriterion criterion} with the
 * value of a {@link BuyAndHoldCriterion "buy and hold" criterion}.
 */
public class VersusBuyAndHoldCriterion extends AbstractAnalysisCriterion {

	private AnalysisCriterion criterion;

	/**
	 * Constructor.
	 * 
	 * @param criterion
	 *            an analysis criterion to be compared
	 */
	public VersusBuyAndHoldCriterion(AnalysisCriterion criterion) {
		this.criterion = criterion;
	}

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		TradingRecord fakeRecord = new TradingRecord();
		fakeRecord.enter(series.getBegin());
		fakeRecord.exit(series.getEnd());

		return criterion.calculate(series, tradingRecord)
				/ criterion.calculate(series, fakeRecord);
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		TradingRecord fakeRecord = new TradingRecord();
		fakeRecord.enter(series.getBegin());
		fakeRecord.exit(series.getEnd());

		return criterion.calculate(series, trade)
				/ criterion.calculate(series, fakeRecord);
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 > criterionValue2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (").append(criterion).append(')');
		return sb.toString();
	}
}
