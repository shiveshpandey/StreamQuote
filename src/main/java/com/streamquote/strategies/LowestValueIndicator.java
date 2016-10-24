package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class LowestValueIndicator extends CachedIndicator<Decimal> {

	private final Indicator<Decimal> indicator;

	private final int timeFrame;

	public LowestValueIndicator(Indicator<Decimal> indicator, int timeFrame) {
		super(indicator);
		this.indicator = indicator;
		this.timeFrame = timeFrame;
	}

	@Override
	protected Decimal calculate(int index) {
		int start = Math.max(0, index - timeFrame + 1);
		Decimal lowest = indicator.getValue(start);
		for (int i = start + 1; i <= index; i++) {
			if (lowest.isGreaterThan(indicator.getValue(i))) {
				lowest = indicator.getValue(i);
			}
		}
		return lowest;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " timeFrame: " + timeFrame;
	}
}
