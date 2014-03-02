package net.engio.blox.reader.parser;


import net.engio.blox.reader.CsvReaderException;

/**
 * A csv reader is used by the {@link net.engio.blox.reader.BlockXreader} to parse strings or string based input
 * and produce a string array that contains the parsed values.
 * 
 * @author Benjamin Diedrichsen
 *
 */

public interface ICsvParser {
	
	
	/**
	 * The given line is parsed and a string array containing the parsed values is created.
	 * 
	 * @param line A string representation of the csv entry
	 * @return The parsed values
	 * @throws net.engio.blox.reader.CsvReaderException
	 */
	public String[] parse(String line)  throws CsvReaderException;

}
