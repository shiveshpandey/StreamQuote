package com.streamquote.strategies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.streamquote.app.ZStreamingQuoteControl;

public class StockPriceSeriesListing {
	private String stockName;
	private List<StockTicker> ticks = new ArrayList<StockTicker>();
	private Date lastTickTime;
	private final static String TRADE_BUY = "BUY";
	private final static String TRADE_SELL = "SELL";
	private final static String TRADE_HOLD = "HOLD";

	public Date getLastTickTime() {
		return lastTickTime;
	}

	public void setLastTickTime(Date lastTickTime) {
		this.lastTickTime = lastTickTime;
	}

	public void addNewTick(Date timeStamp, Decimal openPrice,
			Decimal closePrice, Decimal lastTradedPrice, Decimal volume) {
		this.lastTickTime = timeStamp;

		StockTicker st = new StockTicker();
		st.setTimeStamp(timeStamp);
		st.setOpenPrice(openPrice);
		st.setClosePrice(closePrice);
		st.setLastTradedPrice(lastTradedPrice);
		st.setVolume(volume);
		ticks.add(st);

		StrategyProcess sp = new StrategyProcess(ticks);

		st.setAwesome(sp.calculateAwesomeOscillator(ticks.size() - 1));
		st.setSma1(sp.calculateSma(ticks.size() - 1, 9));
		st.setSma2(sp.calculateSma(ticks.size() - 1, 16));
		st.setSma3(sp.calculateSma(ticks.size() - 1, 26));
		st.setEma1(sp.calculateEma(ticks.size() - 1, 9));
		st.setEma2(sp.calculateEma(ticks.size() - 1, 16));
		st.setEma3(sp.calculateEma(ticks.size() - 1, 26));
		// st.setEmaD(sp.calculateDoubleEma(ticks.size() - 1, 12));
		// st.setEmaT(sp.calculateTripleEma(ticks.size() - 1, 12));
		st.setRsi(sp.calculateRSI(ticks.size() - 1, 20));
		st.setMacd(sp.calculateMacd(ticks.size() - 1, 12, 26));

		ticks.remove(ticks.size() - 1);
		ticks.add(st);

		calculateTradingCallSignal();
	}

	private void calculateTradingCallSignal() {
		String tradeSig;
		if (ticks.size() >= 3
				&& ticks.get(ticks.size() - 1).getMacd()
						.isLessThan(ticks.get(ticks.size() - 2).getMacd())
				&& ticks.get(ticks.size() - 2).getMacd()
						.isLessThan(ticks.get(ticks.size() - 3).getMacd())) {
			tradeSig = TRADE_SELL;
		} else if (ticks.size() >= 3
				&& ticks.get(ticks.size() - 1).getMacd()
						.isGreaterThan(ticks.get(ticks.size() - 2).getMacd())
				&& ticks.get(ticks.size() - 2).getMacd()
						.isGreaterThan(ticks.get(ticks.size() - 3).getMacd())) {
			tradeSig = TRADE_BUY;
		} else if (ticks.size() >= 3
				&& ticks.get(ticks.size() - 1).getRsi()
						.isLessThan(ticks.get(ticks.size() - 2).getRsi())
				&& ticks.get(ticks.size() - 2).getMacd()
						.isLessThan(ticks.get(ticks.size() - 3).getRsi())) {
			tradeSig = TRADE_SELL;

		} else if (ticks.size() >= 3
				&& ticks.get(ticks.size() - 1).getRsi()
						.isGreaterThan(ticks.get(ticks.size() - 2).getRsi())
				&& ticks.get(ticks.size() - 2).getMacd()
						.isGreaterThan(ticks.get(ticks.size() - 3).getRsi())) {
			tradeSig = TRADE_BUY;
		} else {
			tradeSig = TRADE_HOLD;
		}
		generateTradingCallSignal(this.lastTickTime, this.stockName, tradeSig);

	}

	private void generateTradingCallSignal(Date lastTickTime, String stockName,
			String tradeBuy) {
		ZStreamingQuoteControl.getInstance().storeSignalData(lastTickTime,
				stockName, tradeBuy);
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public List<StockTicker> getTicks() {
		return ticks;
	}

	public void setTicks(List<StockTicker> ticks) {
		this.ticks = ticks;
	}

}
