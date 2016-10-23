package com.streamquate.strategies;

import java.io.Serializable;

public class Good implements Serializable {
	private double price;
	private double amount;

	public Good(double price, double amount) {
		this.price = price;
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public double getAmount() {
		return amount;
	}

}