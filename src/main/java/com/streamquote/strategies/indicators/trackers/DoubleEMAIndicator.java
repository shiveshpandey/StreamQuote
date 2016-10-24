package com.streamquote.strategies.indicators.trackers;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.indicators.CachedIndicator;

/**
 * Double exponential moving average indicator.
 * <p>
 * 
 * @see https://en.wikipedia.org/wiki/Double_exponential_moving_average
 */
public class DoubleEMAIndicator extends CachedIndicator<Decimal> {

	private final int timeFrame;

	private final EMAIndicator ema;

	public DoubleEMAIndicator(Indicator<Decimal> indicator, int timeFrame) {
		super(indicator);
		this.timeFrame = timeFrame;
		this.ema = new EMAIndicator(indicator, timeFrame);
	}

	@Override
	protected Decimal calculate(int index) {
		EMAIndicator emaEma = new EMAIndicator(ema, timeFrame);
		return ema.getValue(index).multipliedBy(Decimal.TWO)
				.minus(emaEma.getValue(index));
	}
}