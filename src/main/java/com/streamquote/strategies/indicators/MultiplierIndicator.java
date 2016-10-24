package com.streamquote.strategies.indicators;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;

/**
 * Simple multiplier indicator.
 * <p>
 */
public class MultiplierIndicator extends CachedIndicator<Decimal> {

	private Indicator<Decimal> indicator;

	private Decimal coefficient;

	public MultiplierIndicator(Indicator<Decimal> indicator, Decimal coefficient) {
		super(indicator);
		this.indicator = indicator;
		this.coefficient = coefficient;
	}

	@Override
	protected Decimal calculate(int index) {
		return indicator.getValue(index).multipliedBy(coefficient);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " Coefficient: " + coefficient;
	}
}
