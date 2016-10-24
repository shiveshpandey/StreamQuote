package com.streamquote.strategies.sample;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Reward risk ratio criterion.
 * <p>
 * (i.e. the {@link TotalProfitCriterion total profit} over the
 * {@link MaximumDrawdownCriterion maximum drawdown}.
 */
public class RewardRiskRatioCriterion extends AbstractAnalysisCriterion {

	private AnalysisCriterion totalProfit = new TotalProfitCriterion();

	private AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		return totalProfit.calculate(series, tradingRecord)
				/ maxDrawdown.calculate(series, tradingRecord);
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 > criterionValue2;
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		return totalProfit.calculate(series, trade)
				/ maxDrawdown.calculate(series, trade);
	}
}
