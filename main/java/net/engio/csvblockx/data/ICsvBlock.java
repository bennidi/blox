package net.engio.csvblockx.data;

import java.util.List;
import java.util.Map;

import net.engio.csvblockx.blockdef.ICsvBlockDefinition;


/**
 * Representation of a block inside a csv document. A block consists of a header and a body and provides access
 * to its underlying data as well as its metamodel (definition).
 * 
 * @author Benjamin Diedrichsen
 *
 */
public interface ICsvBlock {

	
	/**
	 * Get the metamodel that defines this block
	 * @return
	 */
	public ICsvBlockDefinition getDefinition();

	/**
	 * Get the header of this block
	 * @return
	 */
	public IHeader getHeader();

	/**
	 * Get the number of rows of this blocks data
	 * @return the number of data lines (optional column headers are not counted)
	 */
	public int getNumberOfRows();

	/**
	 * Get the blocks data as a list of {@link CsvEntry} objects
	 * @return
	 */
	public List<CsvEntry> getEntries();

	/**
	 * Get a specific {@link CsvEntry} object by index
	 * @param idx
	 * @return
	 */
	public CsvEntry getLine(int idx) throws IllegalArgumentException;

	/**
	 * Get the blocks body
	 * @return
	 */
	public IBody getBody();
	
	
	/**
	 * Check whether the header contains any data
	 * @return
	 */
	public abstract boolean hasHeader();



	public interface IHeader{
		
		public void addHeaderLine(String line);
		
		public void addHeaderLines(List<String> lines);
		
		public void addHeaderLines(String[] lines);

		public List<String> getContent();

		public boolean hasContent();
		
	}
	
	public interface IBody{
		
		public String[] getColumnNames();

		public CsvEntry getCsvLine(int lineIndex);

		public List<CsvEntry> getCsvLines();

		public boolean hasColumnHeaders();

		public String[][] getData();
		
		public IBody addItem(Map<String, String> item);
		
		public IBody addLine(CsvEntry line);
		
		public IBody setColumnNames(String[] headers);
		
		public IBody addData(String[] data);

		public IBody appendColumn(String name, String defaultValue);
	}
	
	
	

}
