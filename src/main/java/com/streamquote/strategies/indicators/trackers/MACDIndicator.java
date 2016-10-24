package com.streamquote.strategies.indicators.trackers;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.indicators.CachedIndicator;

public class MACDIndicator extends CachedIndicator<Decimal> {

	private final EMAIndicator shortTermEma;

	private final EMAIndicator longTermEma;

	public MACDIndicator(Indicator<Decimal> indicator, int shortTimeFrame,
			int longTimeFrame) {
		super(indicator);
		if (shortTimeFrame > longTimeFrame) {
			throw new IllegalArgumentException(
					"Long term period count must be greater than short term period count");
		}
		shortTermEma = new EMAIndicator(indicator, shortTimeFrame);
		longTermEma = new EMAIndicator(indicator, longTimeFrame);
	}

	@Override
	protected Decimal calculate(int index) {
		return shortTermEma.getValue(index).minus(longTermEma.getValue(index));
	}
}