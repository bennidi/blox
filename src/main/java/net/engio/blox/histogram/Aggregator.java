package net.engio.blox.histogram;

import net.engio.blox.data.Column;


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
