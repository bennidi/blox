package net.engio.csvblockx.blockdef;

/**
 * The metamodel of a csv block. It defines the blocks dimensions (i.e. start,
 * end and header length) and additional properties (e.g.whether the block data
 * columns are named).
 *
 * A block definition also provides a {@link ICsvFormat} that describes the format of the csv data.
 * 
 * 
 * 
 * @author Benjamin Diedrichsen
 *
 */

public interface ICsvBlockDefinition{

	/**
	 * The number of (non-data)lines that constitute the header of the block
	 * 
	 * @return
	 */
	public int getHeaderLength();

	/**
	 * Set the number of (non-data)lines that constitute the header of the block
	 * 
	 * @param headerLength
	 * @return the metamodel instance
	 */
	public ICsvBlockDefinition headerSize(int headerLength);

	/**
	 * Does the block data section define column headers? Column headers can
	 * subsequently be used to access values by column name rather than by
	 * index.
	 * 
	 * @return
	 */
	public boolean hasColumnsNames();

	/**
	 * To correctly parse the csv data of a block some description of its format
	 * is needed. Each block may come with its own csv data format and thus
	 * needs a specific {@link CsvFormat}.
	 * 
	 * @return
	 */
	public ICsvFormat getCsvFormat();



	public boolean isBegin(String line, long lineIndex);

	public boolean isEnd(String line, long lineIndex);


	/**
	 * Used to detect start or end of a block
	 * 
	 * @author Benjamin Diedrichsen
	 * 
	 */
	public static interface IBlockLimitDetector {

		public boolean matches(String line, long lineIndex);
	}

}
