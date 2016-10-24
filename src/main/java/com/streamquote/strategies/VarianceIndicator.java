package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;

public class VarianceIndicator extends CachedIndicator<Decimal> {

	private Indicator<Decimal> indicator;

	private int timeFrame;

	private SMAIndicator sma;

	public VarianceIndicator(Indicator<Decimal> indicator, int timeFrame) {
		super(indicator);
		this.indicator = indicator;
		this.timeFrame = timeFrame;
		sma = new SMAIndicator(indicator, timeFrame);
	}

	@Override
	protected Decimal calculate(int index) {
		final int startIndex = Math.max(0, index - timeFrame + 1);
		final int numberOfObservations = index - startIndex + 1;
		Decimal variance = Decimal.ZERO;
		Decimal average = sma.getValue(index);
		for (int i = startIndex; i <= index; i++) {
			Decimal pow = indicator.getValue(i).minus(average).pow(2);
			variance = variance.plus(pow);
		}
		variance = variance.dividedBy(Decimal.valueOf(numberOfObservations));
		return variance;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " timeFrame: " + timeFrame;
	}
}
