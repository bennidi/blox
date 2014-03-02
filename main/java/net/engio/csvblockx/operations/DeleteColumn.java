package net.engio.csvblockx.operations;

import net.engio.csvblockx.reader.events.CsvEvent;
import net.engio.csvblockx.reader.events.CsvEvent.Type;
import net.engio.csvblockx.reader.events.CsvEventHandler;

public class DeleteColumn extends CsvEventHandler {

	public DeleteColumn(Type type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	private String name;

	private int index;

	@Override
	public void doHandleEvent(CsvEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	/*

	public DeleteColumn(String name) {
		super();
		this.name = name;
	}

	public DeleteColumn(int index) {
		super();
		this.index = index;
	}

	@Override
	public void doHandleEvent(CsvEvent event) {

		event.getEntry()
			.removeColumn(name);
	}

	public static DeleteColumn withName(String name) {
		return new DeleteColumn(name);
	}
	
	public static DeleteColumn withIndex(String name) {
		return new DeleteColumn(name);
	}*/

}
