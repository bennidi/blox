package net.engio.csvblockx.histogram;

import net.engio.csvblockx.data.Column;


public abstract class Aggregator<T, V> implements IAggregator<T, V> {
	
	private Column column;

	public Aggregator(Column column) {
		super();
		this.column = column;
	}
	
	@Override
	public Column getColumn(){
		return column;
	}
	
}
