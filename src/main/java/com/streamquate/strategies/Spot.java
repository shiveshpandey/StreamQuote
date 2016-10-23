package com.streamquate.strategies;

import java.io.Serializable;

public class Spot implements Serializable {
	private Good buy;
	private Good sell;

	public Spot(Good buy, Good sell) {
		this.buy = buy;
		this.sell = sell;
	}

	public Good getBuy() {
		return buy;
	}

	public Good getSell() {
		return sell;
	}

}