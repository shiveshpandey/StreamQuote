package com.streamquote.strategies.indicators.trackers;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.indicators.CachedIndicator;

/**
 * Triple exponential moving average indicator.
 * <p>
 * a.k.a TRIX
 */
public class TripleEMAIndicator extends CachedIndicator<Decimal> {

	private final int timeFrame;

	private final EMAIndicator ema;

	public TripleEMAIndicator(Indicator<Decimal> indicator, int timeFrame) {
		super(indicator);
		this.timeFrame = timeFrame;
		this.ema = new EMAIndicator(indicator, timeFrame);
	}

	@Override
	protected Decimal calculate(int index) {
		EMAIndicator emaEma = new EMAIndicator(ema, timeFrame);
		EMAIndicator emaEmaEma = new EMAIndicator(emaEma, timeFrame);
		return Decimal.THREE.multipliedBy(
				ema.getValue(index).minus(emaEma.getValue(index))).plus(
				emaEmaEma.getValue(index));
	}
}
