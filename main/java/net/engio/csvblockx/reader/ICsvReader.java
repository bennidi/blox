package net.engio.csvblockx.reader;



/**
 * A csv reader is used by the {@link BlockXreader} to parse strings or string based input
 * and produce a string array that contains the parsed values.
 * 
 * @author Benjamin Diedrichsen
 *
 */

public interface ICsvReader {
	
	
	/**
	 * The given line is parsed and a string array containing the parsed values is created.
	 * 
	 * @param line A string representation of the csv entry
	 * @return The parsed values
	 * @throws CsvReaderException
	 */
	public String[] parse(String line)  throws CsvReaderException;

}
