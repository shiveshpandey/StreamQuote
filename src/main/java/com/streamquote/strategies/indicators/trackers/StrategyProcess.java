package com.streamquote.strategies.indicators.trackers;

import java.util.ArrayList;
import java.util.List;

import com.streamquote.strategies.Decimal;

public class StrategyProcess {
	private final List<StockTicker> tickerList;

	public StrategyProcess(List<StockTicker> tickerList) {
		this.tickerList = tickerList;
	}

	public Decimal calculateSma(int index, int timeFrame) {
		Decimal sum = Decimal.ZERO;

		for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
			sum = sum.plus(tickerList.get(i).getClosePrice());
		}
		final int realTimeFrame = Math.min(timeFrame, index + 1);
		return sum.dividedBy(Decimal.valueOf(realTimeFrame));
	}

	private Decimal calculateSmaOfEma(List<Decimal> ema, int index,
			int timeFrame) {
		Decimal sum = Decimal.ZERO;

		for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
			sum = sum.plus(ema.get(i));
		}
		final int realTimeFrame = Math.min(timeFrame, index + 1);
		return sum.dividedBy(Decimal.valueOf(realTimeFrame));
	}

	public Decimal calculateEma(int index, int timeFrame) {
		if (index + 1 < timeFrame) {
			return calculateSma(index, timeFrame);
		}
		if (index == 0) {
			return tickerList.get(0).getClosePrice();
		}
		Decimal emaPrev = calculateEma(index - 1, timeFrame);
		Decimal multiplier = Decimal.TWO.dividedBy(Decimal
				.valueOf(timeFrame + 1));

		return tickerList.get(index).getClosePrice().minus(emaPrev)
				.multipliedBy(multiplier).plus(emaPrev);
	}

	private Decimal calculateEmaOfEma(List<Decimal> ema, int index,
			int timeFrame) {
		if (index + 1 < timeFrame) {
			return calculateSmaOfEma(ema, index, timeFrame);
		}
		if (index == 0) {
			return ema.get(0);
		}
		Decimal emaPrev = calculateEmaOfEma(ema, index - 1, timeFrame);
		Decimal multiplier = Decimal.TWO.dividedBy(Decimal
				.valueOf(timeFrame + 1));

		return ema.get(index).minus(emaPrev).multipliedBy(multiplier)
				.plus(emaPrev);
	}

	public Decimal calculateDoubleEma(int index, int timeFrame) {
		List<Decimal> ema = new ArrayList<Decimal>();

		for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
			ema.add(calculateEma(i, timeFrame));
		}
		return calculateEma(index, timeFrame).multipliedBy(Decimal.TWO).minus(
				calculateEmaOfEma(ema, index, timeFrame));
		// need to check for nullpointer & for looping above
	}

	public Decimal calculateTripleEma(int index, int timeFrame) {
		List<Decimal> ema = new ArrayList<Decimal>();
		List<Decimal> emaEma = new ArrayList<Decimal>();

		for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
			ema.add(calculateEma(i, timeFrame));
		}
		for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
			emaEma.add(calculateEmaOfEma(ema, i, timeFrame));
		}
		return Decimal.THREE.multipliedBy(ema.get(index)
				.minus(emaEma.get(index))
				.plus(calculateEmaOfEma(emaEma, index, timeFrame)));
		// need to check for nullpointer & for looping above

	}

	public Decimal calculateMacd(int index, int shortTimeFrame,
			int longTimeFrame) {
		if (shortTimeFrame > longTimeFrame) {
			throw new IllegalArgumentException(
					"Long term period count must be greater than short term period count");
		}
		return calculateEma(index, shortTimeFrame).minus(
				calculateEma(index, longTimeFrame));
	}

	public Decimal calculateAwesomeOscillator(int index) {
		return calculateSma(index, 5).minus(calculateSma(index, 34));
	}

	public Decimal calculateRSI(int index, int timeFrame) {
		Decimal sumOfLosses = Decimal.ZERO;
		for (int i = Math.max(1, index - timeFrame + 1); i <= index; i++) {
			if (tickerList.get(i).getClosePrice()
					.isLessThan(tickerList.get(i - 1).getClosePrice())) {
				sumOfLosses = sumOfLosses.plus(tickerList.get(i - 1)
						.getClosePrice()
						.minus(tickerList.get(i).getClosePrice()));
			}
		}
		final int realTimeFrame = Math.min(timeFrame, index + 1);
		Decimal lossRsi = sumOfLosses.dividedBy(Decimal.valueOf(realTimeFrame));

		Decimal sumOfGains = Decimal.ZERO;
		for (int i = Math.max(1, index - timeFrame + 1); i <= index; i++) {
			if (tickerList.get(i).getClosePrice()
					.isGreaterThan(tickerList.get(i - 1).getClosePrice())) {
				sumOfGains = sumOfGains.plus(tickerList.get(i).getClosePrice()
						.minus(tickerList.get(i - 1).getClosePrice()));
			}
		}
		Decimal gainRsi = sumOfGains.dividedBy(Decimal.valueOf(realTimeFrame));

		if (index == 0) {
			return Decimal.ZERO;
		}
		if (lossRsi.isZero()) {
			return Decimal.HUNDRED;
		}
		Decimal relativeStrength = gainRsi.dividedBy(lossRsi);

		Decimal ratio = Decimal.HUNDRED.dividedBy(Decimal.ONE
				.plus(relativeStrength));
		return Decimal.HUNDRED.minus(ratio);
	}
}
