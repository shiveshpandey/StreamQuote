package com.streamquate.strategies;

public class Listing {
	// MACD indicator with the Relative Vigor Index or with the Awesome
	// Oscillator
	// support and resistance >> R2 = P + (H - L) = P + (R1 - S1)
	// R1 = (P x 2) - L
	// P = (H + L + C) / 3
	// S1 = (P x 2) - H
	// S2 = P - (H - L) = P - (R1 - S1)
	// neural network
	// pattern recognition
	// 5, 10, 15, 30 min. strategy based on MACD indicator and SMA
	// EMA
	// stochastic and MACD
	// RSI
	// Bollinger Bands and RSI
	// Simple Moving Averages, Exponential Moving Averages, Double Exponential
	// Moving Averages & Triple Exponential Moving Averages
	// Swing Trading
	// 15 Minute Time Frame based Swing AFL made with mix-up of MACD,
	// Stochastic, EMAs & RSIs

	/*
	 * Trading Strategy
	 * 
	 * Quite Simple and straightforward. Rules in the next section needs to be
	 * adhered to increase the success rates dramatically. Any stock creates a
	 * range in the first 30 minutes of trading in a day. This is calling
	 * Opening Range. The highs and lows of this timeframe is taken as support
	 * and resistance.
	 * 
	 * 1. Buy when the stock moves above the Opening Range high. 2. Sell when
	 * the stock moves below the Opening Range low.
	 * 
	 * PLEASE NOTE THAT THE ABOVE SYSTEM IS GENERIC, THE RULES BELOW WILL MAKE
	 * IT A SPECIFIC SYSTEM. IF YOU ARE FOLLOWING THIS SYSTEM, PLEASE FOLLOW ALL
	 * THE RULES FOR BUY / SELL STRICTLY.
	 * 
	 * General Rules – Applicable for both Buy and Sell:
	 * 
	 * Opening range is defined by the high and low made in the first 30
	 * minutes.
	 * 
	 * 5 min chart with 5 EMA and 20 EMA used for making trading decisions.
	 * 
	 * Entry should be made only on close of the 5 min candle outside the
	 * opening range.
	 * 
	 * 20 EMA is one of the key technical indicators used in this system for
	 * trend trading. Stop loss is always kept at 20 EMA for riding the profits.
	 * 
	 * Volume confirmation – Breakout candle should show increase in volume.
	 * 
	 * Optional confirmation- One of the two indicators - MACD or Stochastics
	 * should be favorable for the trade. (We have four indicators in Simplified
	 * Technical Analysis - Moving Averages, RSI, MACD, Stochastics. The idea
	 * here is at least two indicators should confirm the trade.).
	 * 
	 * This is purely optional condition to enter trade.
	 * 
	 * Respect support and resistance levels. Do not buy just below a resistance
	 * or sell just above a support. Always trade with 2 lots and book 50% as
	 * soon as you see few points profit. Second lot will be used for taking
	 * advantage of days trend.
	 * 
	 * Rules for Buy
	 * 
	 * Stock should be trading above the 20 EMA line before the breakout. Buy
	 * when the 5 minutes candle closes above the opening range. 5 EMA line
	 * should be above the opening range at the time of breakout.
	 * 
	 * Where to keep Stoploss
	 * 
	 * Initial Stoploss – Low of the Opening Range. Trailing Stoploss - As the
	 * stock moves in your direction and you are in profits, book 50% , trail
	 * the stoploss at 20 EMA. A close of 5 min candle below 20 EMA confirms
	 * exit.
	 * 
	 * When to book full profits
	 * 
	 * When the 5 min candle closes below the 20 EMA in the case of longs.
	 * 
	 * Rules for Sell
	 * 
	 * Stock should be trading below the 20 EMA line before the breakdown. Sell
	 * when the 5 minute candle closes below the opening range. 5 EMA line
	 * should be below the opening range at the time of breakout
	 * 
	 * Where to keep Stoploss Initial Stoploss – High of the Opening Range.
	 * Trailing Stoploss - As the stock moves in your direction and you are in
	 * profits, book 50% , trail the stoploss at 20 EMA. A close of 5 min candle
	 * above 20 EMA confirms exit.
	 * 
	 * When to book full profits
	 * 
	 * When the 5 min candle closes above the 20 EMA. High Probability Trade
	 * Setups
	 * 
	 * Below additional conditions will give high probability of success:
	 * 
	 * The Opening Range breakout is above previous day’s high for buy. The
	 * Opening Range breakout is below previous day’s low for sell. Trade is in
	 * the direction of higher time frame charts (15 min /30 min). Overall
	 * Market is moving in the direction of the trade. Opening range breakout
	 * happens after brief period of consolidation.
	 * 
	 * Important Additional Points
	 * 
	 * If the opening range is too wide, better do not trade ORB, since the SLs
	 * will be very far in our system. You can use other trading systems in such
	 * a case. Avoid Opening Range Breakout trades in case of a heavy news flow
	 * day. ( Like Inflation, Manufacturing, Policy decisions etc.). Use other
	 * trading systems once the market settles down after the news.
	 */
}
