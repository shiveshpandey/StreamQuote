package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class HighestValueIndicator extends CachedIndicator<Decimal> {

	private final Indicator<Decimal> indicator;

	private final int timeFrame;

	public HighestValueIndicator(Indicator<Decimal> indicator, int timeFrame) {
		super(indicator);
		this.indicator = indicator;
		this.timeFrame = timeFrame;
	}

	@Override
	protected Decimal calculate(int index) {
		int start = Math.max(0, index - timeFrame + 1);
		Decimal highest = indicator.getValue(start);
		for (int i = start + 1; i <= index; i++) {
			if (highest.isLessThan(indicator.getValue(i))) {
				highest = indicator.getValue(i);
			}
		}
		return highest;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " timeFrame: " + timeFrame;
	}
}
