package com.streamquote.strategies.sample;

import java.util.List;

import com.streamquote.strategies.StockPriceSeries;
import com.streamquote.strategies.Strategy;

/**
 * An abstract analysis criterion.
 */
public abstract class AbstractAnalysisCriterion implements AnalysisCriterion {

	@Override
	public Strategy chooseBest(StockPriceSeries series,
			List<Strategy> strategies) {
		Strategy bestStrategy = strategies.get(0);
		double bestCriterionValue = calculate(series, series.run(bestStrategy));

		for (int i = 1; i < strategies.size(); i++) {
			Strategy currentStrategy = strategies.get(i);
			double currentCriterionValue = calculate(series,
					series.run(currentStrategy));

			if (betterThan(currentCriterionValue, bestCriterionValue)) {
				bestStrategy = currentStrategy;
				bestCriterionValue = currentCriterionValue;
			}
		}
		return bestStrategy;
	}

	@Override
	public String toString() {
		String[] tokens = getClass().getSimpleName().split("(?=\\p{Lu})", -1);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.length - 1; i++) {
			sb.append(tokens[i]).append(' ');
		}
		return sb.toString().trim();
	}
}
