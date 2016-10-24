package com.streamquote.strategies.indicators.trackers.bollinger;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.indicators.CachedIndicator;

/**
 * Buy - Occurs when the price line cross from down to up de Bollinger Band Low.
 * Sell - Occurs when the price line cross from up to down de Bollinger Band
 * High.
 * 
 */
public class BollingerBandsMiddleIndicator extends CachedIndicator<Decimal> {

	private final Indicator<Decimal> indicator;

	public BollingerBandsMiddleIndicator(Indicator<Decimal> indicator) {
		super(indicator);
		this.indicator = indicator;
	}

	@Override
	protected Decimal calculate(int index) {
		return indicator.getValue(index);
	}

	public Indicator<Decimal> getIndicator() {
		return indicator;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " deviation: " + indicator;
	}
}
