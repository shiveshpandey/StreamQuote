package com.streamquote.strategies;

public interface Rule {

	/**
	 * @param rule
	 *            another trading rule
	 * @return a rule which is the AND combination of this rule with the
	 *         provided one
	 */
	Rule and(Rule rule);

	/**
	 * @param rule
	 *            another trading rule
	 * @return a rule which is the OR combination of this rule with the provided
	 *         one
	 */
	Rule or(Rule rule);

	/**
	 * @param rule
	 *            another trading rule
	 * @return a rule which is the XOR combination of this rule with the
	 *         provided one
	 */
	Rule xor(Rule rule);

	/**
	 * @return a rule which is the logical negation of this rule
	 */
	Rule negation();

	/**
	 * @param index
	 *            the tick index
	 * @return true if this rule is satisfied for the provided index, false
	 *         otherwise
	 */
	boolean isSatisfied(int index);

	/**
	 * @param index
	 *            the tick index
	 * @param tradingRecord
	 *            the potentially needed trading history
	 * @return true if this rule is satisfied for the provided index, false
	 *         otherwise
	 */
	boolean isSatisfied(int index, TradingRecord tradingRecord);
}
