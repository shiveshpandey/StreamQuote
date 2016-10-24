package com.streamquote.strategies;

import java.util.Date;

public class Tick {

	private Date timeStamp;
	private Decimal openPrice = null;
	private Decimal minPrice = null;
	private Decimal maxPrice = null;
	private Decimal closePrice = null;
	private Decimal volume = null;
	private Decimal amount = Decimal.ZERO;
	private int trades = 0;
	private Date beginTime;
	private Date endTime;

	public Tick(Date date, Decimal open, Decimal high, Decimal low,
			Decimal close, Decimal volume) {
		this.timeStamp = date;
		this.openPrice = open;
		this.maxPrice = high;
		this.minPrice = low;
		this.closePrice = close;
		this.volume = volume;
	}

	public Tick(int duration, Date tickEndTime) {
		this.endTime = tickEndTime;
		tickEndTime.setSeconds(tickEndTime.getSeconds() - duration);
		this.beginTime = tickEndTime;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Decimal getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Decimal openPrice) {
		this.openPrice = openPrice;
	}

	public Decimal getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Decimal closePrice) {
		this.closePrice = closePrice;
	}

	public Decimal getVolume() {
		return volume;
	}

	public void setVolume(Decimal volume) {
		this.volume = volume;
	}

	public Decimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Decimal minPrice) {
		this.minPrice = minPrice;
	}

	public Decimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Decimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	/**
	 * Adds a trade at the end of tick period.
	 * 
	 * @param tradeAmount
	 *            the tradable amount
	 * @param tradePrice
	 *            the price
	 */
	public void addTrade(double tradeAmount, double tradePrice) {
		addTrade(Decimal.valueOf(tradeAmount), Decimal.valueOf(tradePrice));
	}

	/**
	 * Adds a trade at the end of tick period.
	 * 
	 * @param tradeAmount
	 *            the tradable amount
	 * @param tradePrice
	 *            the price
	 */
	public void addTrade(String tradeAmount, String tradePrice) {
		addTrade(Decimal.valueOf(tradeAmount), Decimal.valueOf(tradePrice));
	}

	/**
	 * Adds a trade at the end of tick period.
	 * 
	 * @param tradeAmount
	 *            the tradable amount
	 * @param tradePrice
	 *            the price
	 */
	public void addTrade(Decimal tradeAmount, Decimal tradePrice) {
		if (openPrice == null) {
			openPrice = tradePrice;
		}
		closePrice = tradePrice;

		if (maxPrice == null) {
			maxPrice = tradePrice;
		} else {
			maxPrice = maxPrice.isLessThan(tradePrice) ? tradePrice : maxPrice;
		}
		if (minPrice == null) {
			minPrice = tradePrice;
		} else {
			minPrice = minPrice.isGreaterThan(tradePrice) ? tradePrice
					: minPrice;
		}
		amount = amount.plus(tradeAmount);
		volume = volume.plus(tradeAmount.multipliedBy(tradePrice));
		trades++;
	}

	public int getTrades() {
		return trades;
	}

	public boolean inPeriod(Date timestamp) {
		return timestamp != null && !timestamp.before(beginTime)
				&& timestamp.before(endTime);
	}
}
