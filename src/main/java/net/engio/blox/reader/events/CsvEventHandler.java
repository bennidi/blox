package net.engio.blox.reader.events;

import net.engio.blox.data.CsvBlock;

/**
 * Event handlers are called by the {@link net.engio.blox.reader.BloxReader} to handle corresponding parser events. They can be used to implement
 * arbitrary operations on the csv data.
 * 
 * Example: The {@link CsvInMemoryBlockBuilder} implements a set of handlers for creating and populating instances of {@link CsvBlock},
 * i.e. creating an in-memory model of the csv block
 * 
 * Handlers can be combined using the wrap() method. Combined handlers are executed in "outer-first"-order.
 * 
 * @author Benjamin Diedrichsen
 * 
 */

public abstract class CsvEventHandler {
	
	private CsvEvent.Type type;

	private CsvEventHandler next;

	public CsvEventHandler(CsvEvent.Type type) {
		super();
		this.type = type;
	}

	public CsvEventHandler(CsvEventHandler nested) {
		super();
		this.next = nested;
	}

	public CsvEventHandler next(CsvEventHandler inner) {
		if (next != null) {
			next.next(inner);
		} else {
			this.next = inner;
		}
		return this;
	}

	public void handle(CsvEvent event) {
		doHandleEvent(event);
		if (next != null) {
			next.handle(event);
		}
	}
	
	public boolean isHandlerFor(CsvEvent event){
		return event.getType().equals(type);
	}
	
	public CsvEvent.Type getType(){
		return type;
	}

	/**
	 * This method is called when an event is passed to the event handler. Subclasses of {@link CsvEventHandler} need to implement this
	 * method for custom event handling logic.
	 * 
	 * @param event
	 */
	public abstract void doHandleEvent(CsvEvent event);

}
