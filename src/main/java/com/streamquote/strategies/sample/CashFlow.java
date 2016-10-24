package com.streamquote.strategies.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Indicator;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * The cash flow.
 * <p>
 * This class allows to follow the money cash flow involved by a list of trades
 * over a time series.
 */
public class CashFlow implements Indicator<Decimal> {

	/** The time series */
	private final StockPriceSeries timeSeries;

	/** The cash flow values */
	private List<Decimal> values = new ArrayList<Decimal>(
			Arrays.asList(Decimal.ONE));

	/**
	 * Constructor.
	 * 
	 * @param timeSeries
	 *            the time series
	 * @param trade
	 *            a single trade
	 */
	public CashFlow(StockPriceSeries timeSeries, Trade trade) {
		this.timeSeries = timeSeries;
		calculate(trade);
		fillToTheEnd();
	}

	/**
	 * Constructor.
	 * 
	 * @param timeSeries
	 *            the time series
	 * @param tradingRecord
	 *            the trading record
	 */
	public CashFlow(StockPriceSeries timeSeries, TradingRecord tradingRecord) {
		this.timeSeries = timeSeries;
		calculate(tradingRecord);
		fillToTheEnd();
	}

	/**
	 * @param index
	 *            the tick index
	 * @return the cash flow value at the index-th position
	 */
	@Override
	public Decimal getValue(int index) {
		return values.get(index);
	}

	@Override
	public StockPriceSeries getStockPriceSeries() {
		return timeSeries;
	}

	/**
	 * @return the size of the time series
	 */
	public int getSize() {
		return timeSeries.getTickCount();
	}

	/**
	 * Calculates the cash flow for a single trade.
	 * 
	 * @param trade
	 *            a single trade
	 */
	private void calculate(Trade trade) {
		final int entryIndex = trade.getEntry().getIndex();
		int begin = entryIndex + 1;
		if (begin > values.size()) {
			Decimal lastValue = values.get(values.size() - 1);
			values.addAll(Collections.nCopies(begin - values.size(), lastValue));
		}
		int end = trade.getExit().getIndex();
		for (int i = Math.max(begin, 1); i <= end; i++) {
			Decimal ratio;
			if (trade.getEntry().isBuy()) {
				ratio = timeSeries
						.getTick(i)
						.getClosePrice()
						.dividedBy(
								timeSeries.getTick(entryIndex).getClosePrice());
			} else {
				ratio = timeSeries.getTick(entryIndex).getClosePrice()
						.dividedBy(timeSeries.getTick(i).getClosePrice());
			}
			values.add(values.get(entryIndex).multipliedBy(ratio));
		}
	}

	/**
	 * Calculates the cash flow for a trading record.
	 * 
	 * @param tradingRecord
	 *            the trading record
	 */
	private void calculate(TradingRecord tradingRecord) {
		for (Trade trade : tradingRecord.getTrades()) {
			// For each trade...
			calculate(trade);
		}
	}

	/**
	 * Fills with last value till the end of the series.
	 */
	private void fillToTheEnd() {
		if (timeSeries.getEnd() >= values.size()) {
			Decimal lastValue = values.get(values.size() - 1);
			values.addAll(Collections.nCopies(
					timeSeries.getEnd() - values.size() + 1, lastValue));
		}
	}
}