package com.streamquote.strategies.indicators.trackers.bollinger;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.StandardDeviationIndicator;
import com.streamquote.strategies.indicators.CachedIndicator;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;

public class PercentBIndicator extends CachedIndicator<Decimal> {

	private final Indicator<Decimal> indicator;

	private final BollingerBandsUpperIndicator bbu;

	private final BollingerBandsMiddleIndicator bbm;

	private final BollingerBandsLowerIndicator bbl;

	/**
	 * Constructor.
	 * 
	 * @param indicator
	 *            an indicator (usually close price)
	 * @param timeFrame
	 *            the time frame
	 * @param k
	 *            the K multiplier (usually 2.0)
	 */
	public PercentBIndicator(Indicator<Decimal> indicator, int timeFrame,
			Decimal k) {
		super(indicator);
		this.indicator = indicator;
		this.bbm = new BollingerBandsMiddleIndicator(new SMAIndicator(
				indicator, timeFrame));
		StandardDeviationIndicator sd = new StandardDeviationIndicator(
				indicator, timeFrame);
		this.bbu = new BollingerBandsUpperIndicator(bbm, sd, k);
		this.bbl = new BollingerBandsLowerIndicator(bbm, sd, k);
		;
	}

	@Override
	protected Decimal calculate(int index) {
		Decimal value = indicator.getValue(index);
		Decimal upValue = bbu.getValue(index);
		Decimal lowValue = bbl.getValue(index);
		return value.minus(lowValue).dividedBy(upValue.minus(lowValue));
	}
}
