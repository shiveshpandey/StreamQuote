package com.streamquote.strategies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.streamquote.strategies.Order.OrderType;

public class StockPriceSeries {

	private String stockName;
	private List<Tick> ticks;
	private int beginIndex = -1;
	private int endIndex = -1;
	private int maximumTickCount = Integer.MAX_VALUE;
	private int removedTicksCount = 0;

	public StockPriceSeries(String stockName, List<Tick> ticks) {
		this(stockName, ticks, 0, ticks.size() - 1);

	}

	public StockPriceSeries(List<Tick> ticks) {
		this("unnamed", ticks);
	}

	public StockPriceSeries(String name) {
		this.stockName = name;
		this.ticks = new ArrayList<Tick>();
	}

	public StockPriceSeries() {
		this("unamed");
	}

	private StockPriceSeries(String name, List<Tick> ticks, int beginIndex,
			int endIndex) {
		if (endIndex < beginIndex - 1) {
			throw new IllegalArgumentException("end cannot be < than begin - 1");
		}
		this.stockName = name;
		this.ticks = ticks;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	public String getStockName() {
		return stockName;
	}

	public List<Tick> getTicks() {
		return ticks;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public void setTicks(List<Tick> ticks) {
		this.ticks = ticks;
	}

	public void addTick(Tick tick) {
		if (tick == null) {
			throw new IllegalArgumentException("Cannot add null tick");
		}
		final int lastTickIndex = ticks.size() - 1;
		if (!ticks.isEmpty()) {
			Date seriesEndTime = ticks.get(lastTickIndex).getTimeStamp();
			if (!tick.getTimeStamp().after(seriesEndTime)) {
				throw new IllegalArgumentException(
						"Cannot add a tick with end time <= to series end time");
			}
		}
		ticks.add(tick);
		if (beginIndex == -1) {
			beginIndex = 0;
		}
		endIndex++;
		removeExceedingTicks();
	}

	public Tick getTick(int i) {
		int innerIndex = i - removedTicksCount;
		if (innerIndex < 0) {
			if (i < 0) {
				throw new IndexOutOfBoundsException("IndexOutOfBoundsException");
			}
			if (ticks.isEmpty()) {
				throw new IndexOutOfBoundsException("IndexOutOfBoundsException");
			}
			innerIndex = 0;
		} else if (innerIndex >= ticks.size()) {
			throw new IndexOutOfBoundsException("IndexOutOfBoundsException");
		}
		return ticks.get(innerIndex);
	}

	public Tick getFirstTick() {
		return getTick(beginIndex);
	}

	/**
	 * @return the last tick of the series
	 */
	public Tick getLastTick() {
		return getTick(endIndex);
	}

	/**
	 * @return the number of ticks in the series
	 */
	public int getTickCount() {
		if (endIndex < 0) {
			return 0;
		}
		final int startIndex = Math.max(removedTicksCount, beginIndex);
		return endIndex - startIndex + 1;
	}

	/**
	 * @return the begin index of the series
	 */
	public int getBegin() {
		return beginIndex;
	}

	/**
	 * @return the end index of the series
	 */
	public int getEnd() {
		return endIndex;
	}

	public void setMaximumTickCount(int maximumTickCount) {

		if (maximumTickCount <= 0) {
			throw new IllegalArgumentException(
					"Maximum tick count must be strictly positive");
		}
		this.maximumTickCount = maximumTickCount;
		removeExceedingTicks();
	}

	/**
	 * @return the maximum number of ticks
	 */
	public int getMaximumTickCount() {
		return maximumTickCount;
	}

	public int getRemovedTicksCount() {
		return removedTicksCount;
	}

	private void removeExceedingTicks() {
		int tickCount = ticks.size();
		if (tickCount > maximumTickCount) {
			// Removing old ticks
			int nbTicksToRemove = tickCount - maximumTickCount;
			for (int i = 0; i < nbTicksToRemove; i++) {
				ticks.remove(0);
			}
			removedTicksCount += nbTicksToRemove;
		}
	}

	/**
	 * Runs the strategy over the series.
	 * <p>
	 * Opens the trades with {@link OrderType.BUY} orders.
	 * 
	 * @param strategy
	 *            the trading strategy
	 * @return the trading record coming from the run
	 */
	public TradingRecord run(Strategy strategy) {
		return run(strategy, OrderType.BUY);
	}

	/**
	 * Runs the strategy over the series.
	 * <p>
	 * Opens the trades with {@link OrderType.BUY} orders.
	 * 
	 * @param strategy
	 *            the trading strategy
	 * @param orderType
	 *            the {@link OrderType} used to open the trades
	 * @return the trading record coming from the run
	 */
	public TradingRecord run(Strategy strategy, OrderType orderType) {
		return run(strategy, orderType, Decimal.NaN);
	}

	/**
	 * Runs the strategy over the series.
	 * <p>
	 * 
	 * @param strategy
	 *            the trading strategy
	 * @param orderType
	 *            the {@link OrderType} used to open the trades
	 * @param amount
	 *            the amount used to open/close the trades
	 * @return the trading record coming from the run
	 */
	public TradingRecord run(Strategy strategy, OrderType orderType,
			Decimal amount) {

		TradingRecord tradingRecord = new TradingRecord(orderType);
		for (int i = beginIndex; i <= endIndex; i++) {
			// For each tick in the sub-series...
			if (strategy.shouldOperate(i, tradingRecord)) {
				tradingRecord.operate(i, ticks.get(i).getClosePrice(), amount);
			}
		}

		if (!tradingRecord.isClosed()) {
			// If the last trade is still opened, we search out of the end
			// index.
			// May works if the current series is a sub-series (but not the last
			// sub-series).
			for (int i = endIndex + 1; i < ticks.size(); i++) {
				// For each tick out of sub-series bound...
				// --> Trying to close the last trade
				if (strategy.shouldOperate(i, tradingRecord)) {
					tradingRecord.operate(i, ticks.get(i).getClosePrice(),
							amount);
					break;
				}
			}
		}
		return tradingRecord;
	}

	public String getSeriesPeriodDescription() {
		StringBuilder sb = new StringBuilder();
		if (!ticks.isEmpty()) {
			Tick firstTick = getFirstTick();
			Tick lastTick = getLastTick();
			sb.append(firstTick.getTimeStamp().toString()).append(" - ")
					.append(lastTick.getTimeStamp().toString());
		}
		return sb.toString();
	}

}
