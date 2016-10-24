package com.streamquote.strategies.sample;

import com.streamquote.strategies.ClosePriceIndicator;
import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.Rule;
import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.TradingRecord;
import com.streamquote.strategies.indicators.trackers.SMAIndicator;
import com.streamquote.strategies.rules.CrossedDownIndicatorRule;
import com.streamquote.strategies.rules.CrossedUpIndicatorRule;
import com.streamquote.strategies.rules.StopGainRule;
import com.streamquote.strategies.rules.StopLossRule;
import com.streamquote.strategies.sample.loader.CsvTradesLoader;

/**
 * Quickstart for ta4j.
 * <p>
 * Global example.
 */
public class Quickstart {

	public static void main(String[] args) {

		// Getting a time series (from any provider: CSV, web service, etc.)
		StockPriceSeries series = CsvTradesLoader.loadBitstampSeries();

		// Getting the close price of the ticks
		Decimal firstClosePrice = series.getTick(0).getClosePrice();
		System.out.println("First close price: " + firstClosePrice.toDouble());
		// Or within an indicator:
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		// Here is the same close price:
		System.out.println(firstClosePrice.isEqual(closePrice.getValue(0))); // equal
																				// to
																				// firstClosePrice

		// Getting the simple moving average (SMA) of the close price over the
		// last 5 ticks
		SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
		// Here is the 5-ticks-SMA value at the 42nd index
		System.out.println("5-ticks-SMA value at the 42nd index: "
				+ shortSma.getValue(42).toDouble());

		// Getting a longer SMA (e.g. over the 30 last ticks)
		SMAIndicator longSma = new SMAIndicator(closePrice, 30);

		// Ok, now let's building our trading rules!

		// Buying rules
		// We want to buy:
		// - if the 5-ticks SMA crosses over 30-ticks SMA
		// - or if the price goes below a defined price (e.g $800.00)
		Rule buyingRule = new CrossedUpIndicatorRule(shortSma, longSma)
				.or(new CrossedDownIndicatorRule(closePrice, Decimal
						.valueOf("800")));

		// Selling rules
		// We want to sell:
		// - if the 5-ticks SMA crosses under 30-ticks SMA
		// - or if if the price looses more than 3%
		// - or if the price earns more than 2%
		Rule sellingRule = new CrossedDownIndicatorRule(shortSma, longSma).or(
				new StopLossRule(closePrice, Decimal.valueOf("3"))).or(
				new StopGainRule(closePrice, Decimal.valueOf("2")));

		// Running our juicy trading strategy...
		TradingRecord tradingRecord = series.run(new Strategy(buyingRule,
				sellingRule));
		System.out.println("Number of trades for our strategy: "
				+ tradingRecord.getTradeCount());

		// Analysis

		// Getting the cash flow of the resulting trades
		@SuppressWarnings("unused")
		CashFlow cashFlow = new CashFlow(series, tradingRecord);

		// Getting the profitable trades ratio
		AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
		System.out.println("Profitable trades ratio: "
				+ profitTradesRatio.calculate(series, tradingRecord));
		// Getting the reward-risk ratio
		AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();
		System.out.println("Reward-risk ratio: "
				+ rewardRiskRatio.calculate(series, tradingRecord));

		// Total profit of our strategy
		// vs total profit of a buy-and-hold strategy
		AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(
				new TotalProfitCriterion());
		System.out.println("Our profit vs buy-and-hold profit: "
				+ vsBuyAndHold.calculate(series, tradingRecord));

		// Your turn!
	}
}
