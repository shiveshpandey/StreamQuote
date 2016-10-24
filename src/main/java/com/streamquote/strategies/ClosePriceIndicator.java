package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class ClosePriceIndicator extends CachedIndicator<Decimal> {

	private StockPriceSeries series;

	public ClosePriceIndicator(StockPriceSeries series) {
		super(series);
		this.series = series;
	}

	@Override
	protected Decimal calculate(int index) {
		return series.getTick(index).getClosePrice();
	}
}
