package net.engio.blox.histogram.aggregates;

import net.engio.blox.data.Column;
import net.engio.blox.histogram.Aggregator;

public class Max<T extends Comparable> extends Aggregator<T, T> {

	public Max(Column column) {
		super(column);
	}

	@Override
	public T apply(T accumulator, T newValue) {
		if(accumulator == null) return newValue;
		return newValue.compareTo(accumulator) < 0 ?  accumulator : newValue;
	}

}
