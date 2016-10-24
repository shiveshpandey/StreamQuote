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
public class BollingerBandsLowerIndicator extends CachedIndicator<Decimal> {

	private final Indicator<Decimal> indicator;

	private final BollingerBandsMiddleIndicator bbm;

	private final Decimal k;

	public BollingerBandsLowerIndicator(BollingerBandsMiddleIndicator bbm,
			Indicator<Decimal> indicator) {
		this(bbm, indicator, Decimal.TWO);
	}

	public BollingerBandsLowerIndicator(BollingerBandsMiddleIndicator bbm,
			Indicator<Decimal> indicator, Decimal k) {
		super(indicator);
		this.bbm = bbm;
		this.indicator = indicator;
		this.k = k;
	}

	@Override
	protected Decimal calculate(int index) {
		return bbm.getValue(index).minus(
				indicator.getValue(index).multipliedBy(k));
	}

	/**
	 * @return the K multiplier
	 */
	public Decimal getK() {
		return k;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "deviation: " + indicator
				+ "series: " + bbm;
	}
}
