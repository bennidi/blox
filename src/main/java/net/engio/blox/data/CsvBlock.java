package net.engio.blox.data;

import net.engio.blox.blockdef.ICsvBlockDescriptor;

import java.util.*;

/**
 * Representation of a block inside a csv document. A block consists of a header and a body and provides access
 * to its underlying data as well as its metamodel (definition).
 *
 * @author Benjamin Diedrichsen
 *
 */
public class CsvBlock{
	
	protected Body body;

	protected ICsvBlockDescriptor descriptor;

	protected Header header;
	
	public CsvBlock(ICsvBlockDescriptor metamodel) {
		super();
		this.body = new Body();
		this.header = new Header();
		this.descriptor = metamodel;
	}

    /**
     * Get the metamodel that defines this block
     * @return
     */
	public ICsvBlockDescriptor getDefinition() {
		return descriptor;
	}

    /**
     * Get the header of this block
     * @return
     */
	public Header getHeader() {
		return header;
	}

    /**
     * Check whether the block has any header data
     * @return
     */
	public boolean hasHeader() {
		return getHeader().hasContent();
	}

    /**
     * Get the blocks body
     * @return
     */
	public Body getBody() {
		return body;
	}

    /**
     * Get the number of rows of this blocks data
     * @return the number of data lines (optional column headers are not counted)
     */
	public int getNumberOfRows() {
		return getBody().getData().length;
	}

    /**
     * Get the blocks data as a list of {@link CsvEntry} objects
     * @return
     */
	public List<CsvEntry> getEntries() {
		return body.getCsvLines();
	}


    /**
     * Get a specific {@link CsvEntry} object by index
     * @param idx
     * @return
     */
	public CsvEntry getLine(int idx) {
		return body.getCsvLine(idx);
	}

	public CsvBlock addItem(Map<String, String> item) {
		getBody().addItem(item);
		return this;
	}

	public CsvBlock addLine(CsvEntry line) {
		getBody().addLine(line);
		return this;
	}
	
	public static enum Integrity {
		STRICT, TOLERANT
	}
	
	public class Header {

		private List<String> content = new LinkedList<String>();

		public void addHeaderLine(String line) {
			content.add(line);
		}
		
		public void addHeaderLines(List<String> lines) {
			for(String line : lines){
				addHeaderLine(line);
			}
		}
		
		public void addHeaderLines(String[] lines) {
			for(String line : lines){
				addHeaderLine(line);
			}
		}

		public List<String> getContent() {
			return content;
		}

		public boolean hasContent() {
			return content.size() > 0;
		}

	}

	public class Body {

		private String[] columnNames;

		private HashMap<Long, CsvEntry> lineCache = new HashMap<Long, CsvEntry>(
				200);

		private List<String[]> data;

		private Integrity integrityMode = Integrity.TOLERANT;

		public Body() {
			super();
			this.data = new ArrayList<String[]>(200);
		}

		public Body(String[][] data, boolean includesColumnNames) {
			super();
			this.data = transformData(data, includesColumnNames);
			// this.includesHeaders = includesHeaders;
			columnNames = includesColumnNames ? data[0] : new String[] {};
		}

		/*
		 * private void extractHeaders(String[][] data){ headers = new
		 * ArrayList<String>(data[0].length); for(int i = 0; i < data[0].length;
		 * i++){ headers.add(data[0][i]); } }
		 */

		private List<String[]> transformData(String[][] data,
				boolean includesHeaders) {
			List<String[]> result = new ArrayList<String[]>(data.length);
			int fromIndex = includesHeaders ? 1 : 0;
			for (int i = fromIndex; i < data.length; i++) {
				result.add(data[i]);
			}
			return result;
		}

		public String[] getColumnNames() {
			return columnNames != null && columnNames.length > 0 ? columnNames
					: getCsvLine(0).getColumnNames();
		}

		public Body setColumnNames(String[] names) {
			if (columnNames != null && columnNames.length > 0)
				throw new IllegalArgumentException(
						"The column names have been set already. Resetting column names is not allowed");
			this.columnNames = names;
			return this;
		}

		public CsvEntry getCsvLine(int lineIndex) {
			if (lineIndex >= data.size() || lineIndex < 0)
				throw new IllegalArgumentException(
						"The given index was out of range. There is no csv line with index "
								+ lineIndex + "Size of existing dataset was "
								+ data.size());
			return makeLine(lineIndex);
		}

		private CsvEntry makeLine(long lineIndex) {
			CsvEntry line = lineCache.get(lineIndex);
			if (line == null) { // TOD: should use a more lists to support
								// indexes greater than int range
				line = new CsvEntry(columnNames, data.get((int) lineIndex),
						lineIndex);
				lineCache.put(lineIndex, line);
			}
			return line;
		}

		public Body addData(String[] data) {
			// TODO: add column headers
			return addLine(new CsvEntry(data));
		}

		public Body addItem(Map<String, String> item) {
			return addLine(new CsvEntry(item));
		}

		public Body addLine(CsvEntry line) {
			// extract the header information from the line
			// and use them for the block if the block does not have any
			// if (!hasColumnHeaders() && line.getColumnNames()
			// .length > 0) {
			// columnNames = line.getColumnNames();
			// }

			if (!lineIsBlockCompliant(line)) {
				throw new IllegalArgumentException(
						"The line passed to this block does not conform the blocks configuration. Check dimension and headers.");
			}
			line.setLineIdx(data.size() - 1);
			if (lineCache.size() > 0)
				lineCache.put(line.getLineIdx(), line);
			data.add(line.getData());
			return this;
		}

		public boolean lineIsBlockCompliant(CsvEntry line) {
			if (integrityMode.equals(Integrity.TOLERANT))
				return true;
			else {
				// check column headers
				// check dimensions
				// TODO implement
				return true;
			}
		}

		public List<CsvEntry> getCsvLines() {
			List<CsvEntry> lines = new ArrayList<CsvEntry>();
			for (int i = 0; i < data.size(); i++) {
				lines.add(makeLine(i));
			}
			return lines;
		}

		public boolean hasColumnHeaders() {
			return columnNames != null && columnNames.length > 0;
		}

		public String[][] getData() {
			// the cache must be the source since modifications to the CsvLine
			// objects
			// are not mirrored to the underlying data array
			List<String[]> data = new ArrayList<String[]>(this.data.size());
			for (CsvEntry line : getCsvLines()) {
				data.add(line.getData());
			}
			this.data = data;
			return data.toArray(new String[][] {});
		}

		public Body appendColumn(String name, String defaultValue) {
			for (CsvEntry line : getCsvLines()) {
				line.addValue(name, defaultValue);
			}
			appendToColumnHeaders(name);
			return this;
		}

		private void appendToColumnHeaders(String name) {
			String[] newHeaders = new String[columnNames.length + 1];
			for (int i = 0; i < columnNames.length; i++) {
				newHeaders[i] = columnNames[i];
			}
			newHeaders[newHeaders.length - 1] = name;
			columnNames = newHeaders;
		}

	}
}
