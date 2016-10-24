package com.streamquote.strategies.indicators.oscillators;

import com.streamquote.strategies.ClosePriceIndicator;
import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.HighestValueIndicator;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.LowestValueIndicator;
import com.streamquote.strategies.MaxPriceIndicator;
import com.streamquote.strategies.MinPriceIndicator;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.indicators.CachedIndicator;

/**
 * Stochastic oscillator K.
 * <p>
 * Receives timeSeries and timeFrame and calculates the
 * StochasticOscillatorKIndicator over ClosePriceIndicator, or receives an
 * indicator, MaxPriceIndicator and MinPriceIndicator and returns
 * StochasticOsiclatorK over this indicator.
 * 
 */
public class StochasticOscillatorKIndicator extends CachedIndicator<Decimal> {
	private final Indicator<Decimal> indicator;

	private final int timeFrame;

	private MaxPriceIndicator maxPriceIndicator;

	private MinPriceIndicator minPriceIndicator;

	public StochasticOscillatorKIndicator(StockPriceSeries timeSeries,
			int timeFrame) {
		this(new ClosePriceIndicator(timeSeries), timeFrame,
				new MaxPriceIndicator(timeSeries), new MinPriceIndicator(
						timeSeries));
	}

	public StochasticOscillatorKIndicator(Indicator<Decimal> indicator,
			int timeFrame, MaxPriceIndicator maxPriceIndicator,
			MinPriceIndicator minPriceIndicator) {
		super(indicator);
		this.indicator = indicator;
		this.timeFrame = timeFrame;
		this.maxPriceIndicator = maxPriceIndicator;
		this.minPriceIndicator = minPriceIndicator;
	}

	@Override
	protected Decimal calculate(int index) {
		HighestValueIndicator highestHigh = new HighestValueIndicator(
				maxPriceIndicator, timeFrame);
		LowestValueIndicator lowestMin = new LowestValueIndicator(
				minPriceIndicator, timeFrame);

		Decimal highestHighPrice = highestHigh.getValue(index);
		Decimal lowestLowPrice = lowestMin.getValue(index);

		return indicator.getValue(index).minus(lowestLowPrice)
				.dividedBy(highestHighPrice.minus(lowestLowPrice))
				.multipliedBy(Decimal.HUNDRED);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " timeFrame: " + timeFrame;
	}
}
