package com.streamquote.strategies.sample;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Trade;
import com.streamquote.strategies.TradingRecord;

/**
 * Average profit criterion.
 * <p>
 * The {@link TotalProfitCriterion total profit} over the
 * {@link NumberOfTicksCriterion number of ticks}.
 */
public class AverageProfitCriterion extends AbstractAnalysisCriterion {

	private AnalysisCriterion totalProfit = new TotalProfitCriterion();

	private AnalysisCriterion numberOfTicks = new NumberOfTicksCriterion();

	@Override
	public double calculate(StockPriceSeries series, TradingRecord tradingRecord) {
		double ticks = numberOfTicks.calculate(series, tradingRecord);
		if (ticks == 0) {
			return 1;
		}
		return Math.pow(totalProfit.calculate(series, tradingRecord),
				1d / ticks);
	}

	@Override
	public double calculate(StockPriceSeries series, Trade trade) {
		double ticks = numberOfTicks.calculate(series, trade);
		if (ticks == 0) {
			return 1;
		}
		return Math.pow(totalProfit.calculate(series, trade), 1d / ticks);
	}

	@Override
	public boolean betterThan(double criterionValue1, double criterionValue2) {
		return criterionValue1 > criterionValue2;
	}
}
