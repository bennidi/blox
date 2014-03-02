package net.engio.csvblockx.histogram.aggregates;

import net.engio.csvblockx.data.Column;
import net.engio.csvblockx.histogram.Aggregator;

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
