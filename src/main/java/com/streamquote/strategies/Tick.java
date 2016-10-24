package com.streamquote.strategies;

import java.util.Date;

public class Tick {

	private Date timeStamp;
	private Decimal openPrice = null;
	private Decimal minPrice = null;
	private Decimal maxPrice = null;
	private Decimal closePrice = null;
	private Decimal volume = null;

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

}
