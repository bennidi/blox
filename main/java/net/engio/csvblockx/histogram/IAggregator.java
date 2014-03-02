package net.engio.csvblockx.histogram;

import net.engio.csvblockx.data.Column;


public interface IAggregator<T,V> {
	
	public T apply(T accumulator, V newValue);

	public abstract Column getColumn();

}
