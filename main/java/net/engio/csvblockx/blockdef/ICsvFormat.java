package net.engio.csvblockx.blockdef;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 10/28/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICsvFormat {

    char getDelimiter();

    ICsvFormat Delimiter(char delimiter);

    char getEncapsulator();

    ICsvFormat Encapsulator(char encapsulator);

    char getComment();

    ICsvFormat Comment(char comment);

    boolean isIgnoreBlankLinesEnabled();

    ICsvFormat IgnoreBlankLines(boolean ignoreBlankLines);

    boolean isTrimValues();

    ICsvFormat IgnoreLeadingWhitespaces(boolean ignoreLeadingWhitespaces);

    boolean isProcessUnicodeEscapesEnabled();

    ICsvFormat ProcessUnicodeEscapes(boolean processUnicodeEscapes);
}
