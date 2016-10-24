package com.streamquote.strategies;

import com.streamquote.strategies.indicators.CachedIndicator;

/**
 * Cross indicator.
 * <p>
 * Boolean indicator which monitors two-indicators crossings.
 */
public class CrossIndicator extends CachedIndicator<Boolean> {

	/** Upper indicator */
	private final Indicator<Decimal> up;
	/** Lower indicator */
	private final Indicator<Decimal> low;

	/**
	 * Constructor.
	 * 
	 * @param up
	 *            the upper indicator
	 * @param low
	 *            the lower indicator
	 */
	public CrossIndicator(Indicator<Decimal> up, Indicator<Decimal> low) {
		// TODO: check if up series is equal to low series
		super(up);
		this.up = up;
		this.low = low;
	}

	@Override
	protected Boolean calculate(int index) {

		int i = index;
		if (i == 0 || up.getValue(i).isGreaterThanOrEqual(low.getValue(i))) {
			return false;
		}

		i--;
		if (up.getValue(i).isGreaterThan(low.getValue(i))) {
			return true;
		} else {

			while (i > 0 && up.getValue(i).isEqual(low.getValue(i))) {
				i--;
			}
			return (i != 0) && (up.getValue(i).isGreaterThan(low.getValue(i)));
		}
	}

	/**
	 * @return the initial lower indicator
	 */
	public Indicator<Decimal> getLow() {
		return low;
	}

	/**
	 * @return the initial upper indicator
	 */
	public Indicator<Decimal> getUp() {
		return up;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + low + " " + up;
	}
}
