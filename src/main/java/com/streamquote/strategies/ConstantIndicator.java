package com.streamquote.strategies;

import com.streamquote.strategies.indicators.AbstractIndicator;

public class ConstantIndicator<T> extends AbstractIndicator<T> {

	private T value;

	public ConstantIndicator(T t) {
		super(null);
		this.value = t;
	}

	@Override
	public T getValue(int index) {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " Value: " + value;
	}
}
