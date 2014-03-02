package net.engio.blox.reader.events;

import net.engio.blox.data.CsvEntry;

/**
 * An instance of {@link CsvEvent} is created by the {@link net.engio.blox.reader.BloxReader} for each line of a given source that represents relevant data. The event is passed to the corresponding handler
 * registered in the block metamodel.
 * 
 * 
 * The source line and parsed data are accessible and modifiable through the event. Modifying the data in the event will affect all succeeding handlers (as they all see the same event). Thus, it is
 * possible to define atomic data modifications in form of handlers and combine different handlers to achieve more complex tasks. For example: One handler can remove the first column, the second
 * replaces some placeholders, the third stores the data in the database.
 * 
 * 
 * @author Benjamin Diedrichsen
 * 
 */

public class CsvEvent {

	private Type type;

	private String source;

	private CsvEntry line;

	public long getLineNumber() {
		return line.getLineIdx();
	}

	public CsvEvent(Type type, String source, CsvEntry line) {
		super();
		this.type = type;
		this.source = source;
		this.line = line;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Get the source line of this event
	 * 
	 * @return A string representation of the source line in the csv
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get the parsed data of the source line
	 * 
	 * @return An array containing the parsed values of the source line
	 */
	public String[] getData() {
		return line.getData();
	}

	public boolean hasData() {
		return getData() != null;
	}
	
	public String getString(String columnName){
		return line.getString(columnName);
	}
	
	public CsvEntry getEntry(){
		return line;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public enum Type {
		BLOCK_START, // when a new block starts
		HEADER_NEWLINE, // when a new header line is found
		BODY_COLUMN_HEADERS, // when the body column headers are parsed
		BODY_DATA_NEWLINE, // when a new data entry of the body is found
		BLOCK_END, // when the block ends
		COMMENT,
		BLANK_LINE
	}

}
