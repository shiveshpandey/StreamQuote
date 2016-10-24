package com.streamquote.strategies.indicators.oscillators;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.indicators.CachedIndicator;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;

/**
 * Stochastic oscillator D.
 * <p>
 * Receive {@link StochasticOscillatorKIndicator} and returns its
 * {@link SMAIndicator SMAIndicator(3)}.
 */
public class StochasticOscillatorDIndicator extends CachedIndicator<Decimal> {

	private Indicator<Decimal> indicator;

	public StochasticOscillatorDIndicator(StochasticOscillatorKIndicator k) {
		this(new SMAIndicator(k, 3));
	}

	public StochasticOscillatorDIndicator(Indicator<Decimal> indicator) {
		super(indicator);
		this.indicator = indicator;
	}

	@Override
	protected Decimal calculate(int index) {
		return indicator.getValue(index);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + indicator;
	}
}
