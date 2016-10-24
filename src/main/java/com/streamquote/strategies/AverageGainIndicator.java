package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class AverageGainIndicator extends CachedIndicator<Decimal> {

	private final CumulatedGainsIndicator cumulatedGains;

	private final int timeFrame;

	public AverageGainIndicator(Indicator<Decimal> indicator, int timeFrame) {
		super(indicator);
		this.cumulatedGains = new CumulatedGainsIndicator(indicator, timeFrame);
		this.timeFrame = timeFrame;
	}

	@Override
	protected Decimal calculate(int index) {
		final int realTimeFrame = Math.min(timeFrame, index + 1);
		return cumulatedGains.getValue(index).dividedBy(
				Decimal.valueOf(realTimeFrame));
	}
}
