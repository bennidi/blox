package net.engio.blox.blockdef;



/**
 * The file format definition provides the csv parser with information about
 * the input format (value delimiter, value encapsulation, comments) and processing instructions
 * like how to handle leading/trailing whitespaces etc.
 * 
 * @author Benjamin Diedrichsen
 * 
 */
public class CsvFileFormat {
	
	public static final String EmptyLine = "\\s*";

	private char delimiter = ',';

	private char encapsulator = '"';

	private char comment = '#';

	private boolean ignoreBlankLines = true;

	private boolean trimValues = true;

	private boolean processUnicodeEscapes = false;

    public static CsvFileFormat Default(){
        return new CsvFileFormat();
    }

    public char getDelimiter() {
		return delimiter;
	}

    public CsvFileFormat Delimiter(char delimiter) {
		this.delimiter = delimiter;
		return this;
	}

    public char getEncapsulator() {
		return encapsulator;
	}

    public CsvFileFormat Encapsulator(char encapsulator) {
		this.encapsulator = encapsulator;
		return this;
	}

    public char getComment() {
		return comment;
	}

    public CsvFileFormat Comment(char comment) {
		this.comment = comment;
		return this;
	}

    public boolean isIgnoreBlankLinesEnabled() {
		return ignoreBlankLines;
	}

    public CsvFileFormat IgnoreBlankLines(boolean ignoreBlankLines) {
		this.ignoreBlankLines = ignoreBlankLines;
		return this;
	}

    public boolean isTrimValues() {
		return trimValues;
	}

    public CsvFileFormat IgnoreLeadingWhitespaces(boolean ignoreLeadingWhitespaces) {
		this.trimValues = ignoreLeadingWhitespaces;
		return this;
	}

    public boolean isProcessUnicodeEscapesEnabled() {
		return processUnicodeEscapes;
	}

    public CsvFileFormat ProcessUnicodeEscapes(boolean processUnicodeEscapes) {
		this.processUnicodeEscapes = processUnicodeEscapes;
		return this;
	}

}
