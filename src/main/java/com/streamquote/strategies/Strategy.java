package com.streamquote.strategies;

public class Strategy {

	/** The entry rule */
	private Rule entryRule;

	/** The exit rule */
	private Rule exitRule;

	/**
	 * The unstable period (number of ticks).<br>
	 * During the unstable period of the strategy any order placement will be
	 * cancelled.<br>
	 * I.e. no entry/exit signal will be fired before index == unstablePeriod.
	 */
	private int unstablePeriod;

	/**
	 * Constructor.
	 * 
	 * @param entryRule
	 *            the entry rule
	 * @param exitRule
	 *            the exit rule
	 */
	public Strategy(Rule entryRule, Rule exitRule) {
		if (entryRule == null || exitRule == null) {
			throw new IllegalArgumentException("Rules cannot be null");
		}
		this.entryRule = entryRule;
		this.exitRule = exitRule;
	}

	/**
	 * @param index
	 *            a tick index
	 * @return true if this strategy is unstable at the provided index, false
	 *         otherwise (stable)
	 */
	public boolean isUnstableAt(int index) {
		return index < unstablePeriod;
	}

	/**
	 * @param unstablePeriod
	 *            number of ticks that will be strip off for this strategy
	 */
	public void setUnstablePeriod(int unstablePeriod) {
		this.unstablePeriod = unstablePeriod;
	}

	/**
	 * @param index
	 *            the tick index
	 * @param tradingRecord
	 *            the potentially needed trading history
	 * @return true to recommend an order, false otherwise (no recommendation)
	 */
	public boolean shouldOperate(int index, TradingRecord tradingRecord) {
		Trade trade = tradingRecord.getCurrentTrade();
		if (trade.isNew()) {
			return shouldEnter(index, tradingRecord);
		} else if (trade.isOpened()) {
			return shouldExit(index, tradingRecord);
		}
		return false;
	}

	/**
	 * @param index
	 *            the tick index
	 * @return true to recommend to enter, false otherwise
	 */
	public boolean shouldEnter(int index) {
		return shouldEnter(index, null);
	}

	/**
	 * @param index
	 *            the tick index
	 * @param tradingRecord
	 *            the potentially needed trading history
	 * @return true to recommend to enter, false otherwise
	 */
	public boolean shouldEnter(int index, TradingRecord tradingRecord) {
		if (isUnstableAt(index)) {
			return false;
		}
		final boolean enter = entryRule.isSatisfied(index, tradingRecord);
		return enter;
	}

	/**
	 * @param index
	 *            the tick index
	 * @return true to recommend to exit, false otherwise
	 */
	public boolean shouldExit(int index) {
		return shouldExit(index, null);
	}

	/**
	 * @param index
	 *            the tick index
	 * @param tradingRecord
	 *            the potentially needed trading history
	 * @return true to recommend to exit, false otherwise
	 */
	public boolean shouldExit(int index, TradingRecord tradingRecord) {
		if (isUnstableAt(index)) {
			return false;
		}
		final boolean exit = exitRule.isSatisfied(index, tradingRecord);
		return exit;
	}

}
