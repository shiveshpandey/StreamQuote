package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

public class MinPriceIndicator extends CachedIndicator<Decimal> {

	private StockPriceSeries series;

	public MinPriceIndicator(StockPriceSeries series) {
		super(series);
		this.series = series;
	}

	@Override
	protected Decimal calculate(int index) {
		return series.getTick(index).getMinPrice();
	}
}
