package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class StandardDeviationIndicator extends CachedIndicator<Decimal> {

	private VarianceIndicator variance;

	/**
	 * Constructor.
	 * 
	 * @param indicator
	 *            the indicator
	 * @param timeFrame
	 *            the time frame
	 */
	public StandardDeviationIndicator(Indicator<Decimal> indicator,
			int timeFrame) {
		super(indicator);
		variance = new VarianceIndicator(indicator, timeFrame);
	}

	@Override
	protected Decimal calculate(int index) {
		return variance.getValue(index).sqrt();
	}
}
