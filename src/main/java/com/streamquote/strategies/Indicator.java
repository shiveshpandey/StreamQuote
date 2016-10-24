package com.streamquote.strategies;

public interface Indicator<T> {

	/**
	 * @param index
	 *            the tick index
	 * @return the value of the indicator
	 */
	T getValue(int index);

	/**
	 * @return the related time series
	 */
	StockPriceSeries getStockPriceSeries();
}
