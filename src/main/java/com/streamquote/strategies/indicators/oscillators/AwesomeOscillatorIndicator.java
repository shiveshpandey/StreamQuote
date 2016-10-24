package com.streamquote.strategies.indicators.oscillators;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.indicators.CachedIndicator;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;

/**
 * Awesome oscillator. (AO)
 * <p>
 * 
 * @see http://www.forexgurus.co.uk/indicators/awesome-oscillator
 */
public class AwesomeOscillatorIndicator extends CachedIndicator<Decimal> {

	private SMAIndicator sma5;

	private SMAIndicator sma34;

	public AwesomeOscillatorIndicator(Indicator<Decimal> indicator,
			int timeFrameSma1, int timeFrameSma2) {
		super(indicator);
		this.sma5 = new SMAIndicator(indicator, timeFrameSma1);
		this.sma34 = new SMAIndicator(indicator, timeFrameSma2);
	}

	public AwesomeOscillatorIndicator(Indicator<Decimal> indicator) {
		this(indicator, 5, 34);
	}

	@Override
	protected Decimal calculate(int index) {
		return sma5.getValue(index).minus(sma34.getValue(index));
	}
}
