/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.streamquote.strategies.sample.analysis;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;
import com.streamquote.strategies.TradingRecord;
import com.streamquote.strategies.sample.AverageProfitCriterion;
import com.streamquote.strategies.sample.AverageProfitableTradesCriterion;
import com.streamquote.strategies.sample.BuyAndHoldCriterion;
import com.streamquote.strategies.sample.LinearTransactionCostCriterion;
import com.streamquote.strategies.sample.MaximumDrawdownCriterion;
import com.streamquote.strategies.sample.NumberOfTicksCriterion;
import com.streamquote.strategies.sample.NumberOfTradesCriterion;
import com.streamquote.strategies.sample.RewardRiskRatioCriterion;
import com.streamquote.strategies.sample.TotalProfitCriterion;
import com.streamquote.strategies.sample.VersusBuyAndHoldCriterion;
import com.streamquote.strategies.sample.loader.CsvTradesLoader;
import com.streamquote.strategies.sample.strategies.MovingMomentumStrategy;

/**
 * This class diplays analysis criterion values after running a trading strategy
 * over a time series.
 */
public class StrategyAnalysis {

	public static void main(String[] args) {

		// Getting the time series
		StockPriceSeries series = CsvTradesLoader.loadBitstampSeries();
		// Building the trading strategy
		Strategy strategy = MovingMomentumStrategy.buildStrategy(series);
		// Running the strategy
		TradingRecord tradingRecord = series.run(strategy);

		/**
		 * Analysis criteria
		 */

		// Total profit
		TotalProfitCriterion totalProfit = new TotalProfitCriterion();
		System.out.println("Total profit: "
				+ totalProfit.calculate(series, tradingRecord));
		// Number of ticks
		System.out
				.println("Number of ticks: "
						+ new NumberOfTicksCriterion().calculate(series,
								tradingRecord));
		// Average profit (per tick)
		System.out
				.println("Average profit (per tick): "
						+ new AverageProfitCriterion().calculate(series,
								tradingRecord));
		// Number of trades
		System.out.println("Number of trades: "
				+ new NumberOfTradesCriterion()
						.calculate(series, tradingRecord));
		// Profitable trades ratio
		System.out.println("Profitable trades ratio: "
				+ new AverageProfitableTradesCriterion().calculate(series,
						tradingRecord));
		// Maximum drawdown
		System.out.println("Maximum drawdown: "
				+ new MaximumDrawdownCriterion().calculate(series,
						tradingRecord));
		// Reward-risk ratio
		System.out.println("Reward-risk ratio: "
				+ new RewardRiskRatioCriterion().calculate(series,
						tradingRecord));
		// Total transaction cost
		System.out.println("Total transaction cost (from $1000): "
				+ new LinearTransactionCostCriterion(1000, 0.005).calculate(
						series, tradingRecord));
		// Buy-and-hold
		System.out.println("Buy-and-hold: "
				+ new BuyAndHoldCriterion().calculate(series, tradingRecord));
		// Total profit vs buy-and-hold
		System.out
				.println("Custom strategy profit vs buy-and-hold strategy profit: "
						+ new VersusBuyAndHoldCriterion(totalProfit).calculate(
								series, tradingRecord));
	}
}
