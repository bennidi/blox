package net.engio.csvblockx.histogram.aggregates;

import net.engio.csvblockx.data.Column;
import net.engio.csvblockx.histogram.Aggregator;

public class Count extends Aggregator<Integer, Object> {

	public Count() {
		super(Column.Any);
	}

	public Count(Column column) {
		super(column);
	}

	@Override
	public Integer apply(Integer accumulator, Object newValue) {
		if (accumulator == null)
			return 1;
		return newValue != null ? accumulator + 1 : accumulator;
	}

}
