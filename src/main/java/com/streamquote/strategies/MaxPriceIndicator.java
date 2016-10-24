package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class MaxPriceIndicator extends CachedIndicator<Decimal> {

	private StockPriceSeries series;

	public MaxPriceIndicator(StockPriceSeries series) {
		super(series);
		this.series = series;
	}

	@Override
	protected Decimal calculate(int index) {
		return series.getTick(index).getMaxPrice();
	}
}
