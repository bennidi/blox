package net.engio.blox.histogram;

import net.engio.blox.data.Column;


public interface IAggregator<T,V> {
	
	public T apply(T accumulator, V newValue);

	public abstract Column getColumn();

}
