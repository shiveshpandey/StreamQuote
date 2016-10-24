package com.streamquote.strategies.indicators.trackers.bollinger;

import com.streamquote.strategies.Decimal;
import com.streamquote.strategies.indicators.CachedIndicator;

public class BollingerBandWidthIndicator extends CachedIndicator<Decimal> {

	private final BollingerBandsUpperIndicator bbu;

	private final BollingerBandsMiddleIndicator bbm;

	private final BollingerBandsLowerIndicator bbl;

	public BollingerBandWidthIndicator(BollingerBandsUpperIndicator bbu,
			BollingerBandsMiddleIndicator bbm, BollingerBandsLowerIndicator bbl) {
		super(bbm.getStockPriceSeries());
		this.bbu = bbu;
		this.bbm = bbm;
		this.bbl = bbl;
	}

	@Override
	protected Decimal calculate(int index) {
		return bbu.getValue(index).minus(bbl.getValue(index))
				.dividedBy(bbm.getValue(index)).multipliedBy(Decimal.HUNDRED);
	}
}
