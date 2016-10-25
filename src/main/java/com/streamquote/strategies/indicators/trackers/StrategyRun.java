package com.streamquote.strategies.indicators.trackers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.streamquote.strategies.Decimal;

public class StrategyRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<StockPriceSeriesListing> stockList = new ArrayList<StockPriceSeriesListing>();
		for (int i = 0; i < 360; i++) {
			System.out.println(i);

			StockPriceSeriesListing stock = new StockPriceSeriesListing();
			stock.setStockName("stock:" + 1);
			for (int j = 0; j < 300; j++) {
				stock.addNewTick(new Date(), Decimal.valueOf(100 + j),
						Decimal.valueOf(100 + 2 * j), Decimal.valueOf(100 + j),
						Decimal.valueOf(100));
			}
			stockList.add(stock);
		}
		System.out.println(stockList.size());
	}

}
