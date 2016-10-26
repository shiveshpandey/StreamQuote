package com.streamquote.strategies;

import java.util.Date;

public class StockTicker {
	private Date timeStamp;
	private Decimal openPrice;
	private Decimal closePrice;
	private Decimal lastTradedPrice;
	private Decimal volume;
	private Decimal sma1;
	private Decimal sma2;
	private Decimal sma3;
	private Decimal ema1;
	private Decimal ema2;
	private Decimal ema3;
	private Decimal emaD;
	private Decimal emaT;
	private Decimal macd;
	private Decimal awesome;
	private Decimal rsi;

	public Decimal getSma1() {
		return sma1;
	}

	public void setSma1(Decimal sma1) {
		this.sma1 = sma1;
	}

	public Decimal getSma2() {
		return sma2;
	}

	public void setSma2(Decimal sma2) {
		this.sma2 = sma2;
	}

	public Decimal getSma3() {
		return sma3;
	}

	public void setSma3(Decimal sma3) {
		this.sma3 = sma3;
	}

	public Decimal getEma1() {
		return ema1;
	}

	public void setEma1(Decimal ema1) {
		this.ema1 = ema1;
	}

	public Decimal getEma2() {
		return ema2;
	}

	public void setEma2(Decimal ema2) {
		this.ema2 = ema2;
	}

	public Decimal getEma3() {
		return ema3;
	}

	public void setEma3(Decimal ema3) {
		this.ema3 = ema3;
	}

	public Decimal getEmaD() {
		return emaD;
	}

	public void setEmaD(Decimal emaD) {
		this.emaD = emaD;
	}

	public Decimal getEmaT() {
		return emaT;
	}

	public void setEmaT(Decimal emaT) {
		this.emaT = emaT;
	}

	public Decimal getMacd() {
		return macd;
	}

	public void setMacd(Decimal macd) {
		this.macd = macd;
	}

	public Decimal getAwesome() {
		return awesome;
	}

	public void setAwesome(Decimal awesome) {
		this.awesome = awesome;
	}

	public Decimal getRsi() {
		return rsi;
	}

	public void setRsi(Decimal rsi) {
		this.rsi = rsi;
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

	public Decimal getLastTradedPrice() {
		return lastTradedPrice;
	}

	public void setLastTradedPrice(Decimal lastTradedPrice) {
		this.lastTradedPrice = lastTradedPrice;
	}

	public Decimal getVolume() {
		return volume;
	}

	public void setVolume(Decimal volume) {
		this.volume = volume;
	}
}
