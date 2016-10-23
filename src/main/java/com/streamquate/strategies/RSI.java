package com.streamquate.strategies;

import java.util.ArrayList;

public class RSI {

	public double getRSI(ArrayList<Spot> prices, int timeframe) {
		ExponentialMovingAverage avU = new ExponentialMovingAverage(timeframe);
		ExponentialMovingAverage avD = new ExponentialMovingAverage(timeframe);
		double oldPrice = prices.get(0).getSell().getPrice();
		prices.remove(0);

		for (Spot p : prices) {
			double price = p.getSell().getPrice();
			double diff = price - oldPrice;
			if (diff > 0)
				avU.update(diff);
			else
				avD.update(Math.abs(diff));
			oldPrice = price;
		}
		double uAv = avU.getAverage();
		double dAv = avD.getAverage();
		if (uAv > 0 && dAv > 0)
			return (100 - (100 / (1 + (avU.getAverage() / avD.getAverage()))));
		else
			// needs further investigation
			return 50;
	}

}