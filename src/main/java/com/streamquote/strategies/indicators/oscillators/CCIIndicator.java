package com.streamquote.strategies.indicators.oscillators;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.indicators.CachedIndicator;
import com.streamquote.strategies.indicators.MeanDeviationIndicator;
import com.streamquote.strategies.indicators.TypicalPriceIndicator;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;

/**
 * Commodity Channel Index (CCI) indicator.
 * <p>
 * 
 * @see http 
 *      ://stockcharts.com/school/doku.php?id=chart_school:technical_indicators
 *      :commodity_channel_in
 */
public class CCIIndicator extends CachedIndicator<Decimal> {

	public static final Decimal FACTOR = Decimal.valueOf("0.015");

	private TypicalPriceIndicator typicalPriceInd;

	private SMAIndicator smaInd;

	private MeanDeviationIndicator meanDeviationInd;

	private int timeFrame;

	/**
	 * Constructor.
	 * 
	 * @param series
	 *            the time series
	 * @param timeFrame
	 *            the time frame
	 */
	public CCIIndicator(StockPriceSeries series, int timeFrame) {
		super(series);
		typicalPriceInd = new TypicalPriceIndicator(series);
		smaInd = new SMAIndicator(typicalPriceInd, timeFrame);
		meanDeviationInd = new MeanDeviationIndicator(typicalPriceInd,
				timeFrame);
		this.timeFrame = timeFrame;
	}

	@Override
	protected Decimal calculate(int index) {
		final Decimal typicalPrice = typicalPriceInd.getValue(index);
		final Decimal typicalPriceAvg = smaInd.getValue(index);
		final Decimal meanDeviation = meanDeviationInd.getValue(index);
		if (meanDeviation.isZero()) {
			return Decimal.ZERO;
		}
		return (typicalPrice.minus(typicalPriceAvg)).dividedBy(meanDeviation
				.multipliedBy(FACTOR));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " timeFrame: " + timeFrame;
	}
}
