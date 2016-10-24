package com.streamquote.strategies;

import java.util.Date;
import java.util.List;

public class StockPriceSeries {

	private String stockName;
	private List<Tick> ticks;
	private int beginIndex = -1;
	private int endIndex = -1;
	private int maximumTickCount = Integer.MAX_VALUE;
	private int removedTicksCount = 0;

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

}
