package net.engio.blox.blockdef;

import java.util.regex.Pattern;

/**
 * 
 * A block consists of two parts: Header and Body. The header may define an arbitrary number of lines 
 * which are not parsed but passed to the corresponding handler without further processing.
 * Standard configuration is without headers, so if header information is present it must be configured explicitly.
 * 
 * A body may define data column headers, i.e. a name for each column. 
 * Standard configuration assumes that no column names are present. If the block body defines column names, then it
 * must explicitly configured.
 * 
 * @author Benjamin Diedrichsen
 * 
 */
public class CsvBlockDescriptor implements ICsvBlockDescriptor {

	private int headerLength = 0;

	private boolean columnHeadersForBlockBody = false;

	private CsvFileFormat csvFileFormat = CsvFileFormat.Default();

	private MutableVariable<IBlockLimitDetector> beginDetector = new MutableVariable<IBlockLimitDetector>(null);

	private MutableVariable<IBlockLimitDetector> endDetector = new MutableVariable<IBlockLimitDetector>(null);

	/**
	 * Default constructor initializes a block without header or column names and using default
	 * csv format definition
	 */
	public CsvBlockDescriptor() {
		super();
	}


    @Override
	public int getHeaderLength() {
		return headerLength;
	}

	@Override
	public CsvBlockDescriptor headerSize(int headerLength) {
		this.headerLength = headerLength;
		return this;
	}

	@Override
	public boolean hasColumnsNames() {
		return columnHeadersForBlockBody;
	}

	public CsvBlockDescriptor hasColumnNames(boolean value) {
		this.columnHeadersForBlockBody = value;
		return this;
	}

	@Override
	public CsvFileFormat getCsvFileFormat() {
		return csvFileFormat;
	}
	
	public BlockLimitBuilderStage1 starts(){
		return new BlockLimitBuilder(beginDetector);
	}
	
	public BlockLimitBuilderStage1 ends(){
		return new BlockLimitBuilder(endDetector);
	}

	@Override
	public boolean isBegin(String line, long lineIndex) {
		return beginDetector.getValue().matches(line, lineIndex);
	}

	@Override
	public boolean isEnd(String line, long lineIndex) {
		return endDetector.getValue().matches(line, lineIndex);
	}
	
	public void validate(){
		if(beginDetector.getValue() == null)
			throw new RuntimeException("Block begin not configured! Please specify beginning of block by line index or regular expression");
		if(endDetector.getValue() == null)
			throw new RuntimeException("Block end not configured! Please specify end of block by line index or regular expression");
		
	}

    //pattern for passing around references to variables
	private static class MutableVariable<T> {

		private T value;

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		public MutableVariable(T value) {
			super();
			this.value = value;
		}

	}

	/**
	 * This interface exposes only a subset of the methods of BlockLimitBuilder
	 * to enforce correct use of API
	 * 
	 * @author Benjamin Diedrichsen
	 *
	 */
	public interface BlockLimitBuilderStage1{
		
		public BlockLimitBuilderStage2 with();
		
		public BlockLimitBuilderStage2 after();
	}
	
	/**
	 * This interface exposes only a subset of the methods of BlockLimitBuilder
	 * to enforce correct use of API
	 * 
	 * @author Benjamin Diedrichsen
	 *
	 */
	public interface BlockLimitBuilderStage2{
		
		public CsvBlockDescriptor emptyLine();
		
		public CsvBlockDescriptor line(final int lineNumber);
		
		public CsvBlockDescriptor pattern(final String regex);
	}
	
	/**
	 * A block limit builder is an intermediate object for convenient creation of
	 * {@link IBlockLimitDetector}
	 * 
	 * @author Benjamin Diedrichsen
	 *
	 */
	public class BlockLimitBuilder implements BlockLimitBuilderStage1, BlockLimitBuilderStage2{

		// the variable this builder is bound to
		private MutableVariable<IBlockLimitDetector> detector;
		
		private int lineOffset;

		private BlockLimitBuilder(MutableVariable<IBlockLimitDetector> detector) {
			super();
			this.detector = detector;
		}
		
		public BlockLimitBuilder with(){
			lineOffset = 0;
			return this;
		}
		
		public BlockLimitBuilder after(){
			lineOffset = 1;
			return this;
		}

		/**
		 * Configures the block limit (either end or begin, depending on which method was called before)
		 * based on line index. First line has index 1
		 * 
		 * @param lineNumber The line number (1-based index)
		 * @return
		 */
		public CsvBlockDescriptor line(final int lineNumber) {
			detector.setValue(new IBlockLimitDetector() {

				// implement matches based on line index and offset
				public boolean matches(String line, long lineIndex) {
					return lineIndex == lineNumber + lineOffset;
				}
			});
			return CsvBlockDescriptor.this; // return the outer instance
		}
		
		public CsvBlockDescriptor emptyLine(){
			return pattern(CsvFileFormat.EmptyLine);
		}

		/**
		 * Configures the block limit (either end or begin, depending on which method was called before)
		 * using a regular expression that will be matched against the line content.
		 * 
		 * @param regex The regular expression for matching
		 * @return
		 */
		public CsvBlockDescriptor pattern(final String regex) {
			detector.setValue(new IBlockLimitDetector() {
				
				private Pattern pattern = Pattern.compile(regex);
				
				private long matchedLine = -1;

				// implements matches based on regular expression over line content
				// if offset is set then the matching line is the line that matches the regex
				// plus the offset
				public boolean matches(String line, long lineIndex) {
					if(lineOffset > 0 ){ // if offset is set
						if(matchedLine != -1 && lineIndex == matchedLine + lineOffset)
							return true; // we already found the regex matching line and have reached the offset
						if(pattern.matcher(line).matches()){
							matchedLine = lineIndex;
						}
						return false;
					}
					return pattern.matcher(line).matches();
				}
			});
			return CsvBlockDescriptor.this; // return the outer instance
		}
	}

	

}
