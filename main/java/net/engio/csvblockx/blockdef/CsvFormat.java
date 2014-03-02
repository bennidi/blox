package net.engio.csvblockx.blockdef;



/**
 * The format definitions provides all available parameters for handling of different csv formats.
 * It defines the characters used for value separation, encapsulation and so on.
 * 
 * @author Benjamin Diedrichsen
 * 
 */
public class CsvFormat implements ICsvFormat {
	
	public static final String EmptyLine = "\\s*";

	private char delimiter = ',';

	private char encapsulator = '"';

	private char comment = '#';

	private boolean ignoreBlankLines = true;

	private boolean trimValues = true;

	private boolean processUnicodeEscapes = false;

    public static ICsvFormat Default(){
        return new CsvFormat();
    }

    @Override
    public char getDelimiter() {
		return delimiter;
	}

	@Override
    public ICsvFormat Delimiter(char delimiter) {
		this.delimiter = delimiter;
		return this;
	}

    @Override
    public char getEncapsulator() {
		return encapsulator;
	}

	@Override
    public ICsvFormat Encapsulator(char encapsulator) {
		this.encapsulator = encapsulator;
		return this;
	}

	@Override
    public char getComment() {
		return comment;
	}

	@Override
    public ICsvFormat Comment(char comment) {
		this.comment = comment;
		return this;
	}

    @Override
    public boolean isIgnoreBlankLinesEnabled() {
		return ignoreBlankLines;
	}

	@Override
    public ICsvFormat IgnoreBlankLines(boolean ignoreBlankLines) {
		this.ignoreBlankLines = ignoreBlankLines;
		return this;
	}

    @Override
    public boolean isTrimValues() {
		return trimValues;
	}

	@Override
    public ICsvFormat IgnoreLeadingWhitespaces(boolean ignoreLeadingWhitespaces) {
		this.trimValues = ignoreLeadingWhitespaces;
		return this;
	}

    @Override
    public boolean isProcessUnicodeEscapesEnabled() {
		return processUnicodeEscapes;
	}

	@Override
    public ICsvFormat ProcessUnicodeEscapes(boolean processUnicodeEscapes) {
		this.processUnicodeEscapes = processUnicodeEscapes;
		return this;
	}

}
