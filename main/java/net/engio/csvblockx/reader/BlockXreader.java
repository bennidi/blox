package net.engio.csvblockx.reader;

import net.engio.csvblockx.blockdef.ICsvFormat;
import net.engio.csvblockx.data.CsvEntry;
import net.engio.csvblockx.reader.events.CsvEvent;
import net.engio.csvblockx.reader.events.CsvEventHandlerSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This reader can be used to parse a given csv input according to some definitions that describe the structure of the
 * csv. The csv may contain various blocks of different data where each block may have its own data structure (e.g.
 * different number of columns, different column headers, different number of data entries).
 * <p/>
 * The structure of the csv is described using a set of {@link net.engio.csvblockx.blockdef.ICsvBlockDefinition}. Each instance of
 * {@link net.engio.csvblockx.blockdef.ICsvBlockDefinition} describes the structure of one single block in the csv.
 * <p/>
 * The reader works event based. It parses the csv line by line, raising a corresponding event for each relevant line.
 * The types of events that are created by the parser are defined in the enumeration {@link net.engio.csvblockx.reader.events.CsvEvent.Type} of class
 * {@link net.engio.csvblockx.reader.events.CsvEvent}. The events are passed to event handlers that can be attached to the block definitions. The event
 * handlers then take care of whatever task needs to be done (e.g. storing to database, logging into file, construction
 * of in-memory model).
 * <p/>
 * For the parser to work it is NOT NECESSARY to pass a complete metamodel of the csv. The parser will simply ignore the
 * blocks of the csv which have no metamodel description, e.g. no matching {@link net.engio.csvblockx.blockdef.ICsvBlockDefinition}. Thus, it is
 * possible to process only one block out of a multi block csv without caring about the structure of any of the
 * irrelevant blocks.
 * <p/>
 * The actual parsing of a single line is done by a {@link ICsvReader} which is created using a factory
 * {@link ICsvReaderFactory}. The factory can by passed as a constructor argument. This allows to use different csv
 * parser implementations. The reader ships with a default csv parser implementation {@link net.engio.csvblockx.reader.CsvReader}
 * <p/>
 *
 * @author Benjamin Diedrichsen
 */

public class BlockXreader {

    private ICsvReaderFactory readerFactory;

    private CsvEventHandlerSet[] handlers = null;

    private String encoding;

    public BlockXreader(ICsvReaderFactory factory, CsvEventHandlerSet[] handlers, String encoding) {
        super();
        readerFactory = factory;
        this.handlers = handlers;
        this.encoding = encoding;
    }

    /**
     * The main method of the reader. It takes an input stream which which is read line by line. The events produced by
     * each line read will be passed to the event handlers defined by the metamodel that was passed to the reader at
     * instantiation time.
     * <p/>
     * When the call to this method returns, all of the input stream has been processed.
     *
     * @param stream
     * @throws Exception
     */
    public void read(InputStream stream) throws Exception {
        if (handlers.length == 0)
            throw new Exception("No meta model information present. Please add at least one csv block definition");
        BufferedReader streamReader = wrapStream(stream);
        String currentLine = null; // the current line
        CsvEventHandlerSet handlersForCurrentBlock = null, handlersForNextBlock = null;
        ICsvFormat csvFormat = null;
        long lineNumber = 1; // line index in csv is 1-based
        long blockStartIndex = 0; // the line number where the current block started
        ICsvReader csvReader = null; // the reader for the current block
        int blockCount = 0; // the number of blocks that have been processed (including current)
        String columnNames[] = null; // the column names of the current block (null if unnamed columns are used)
        while ((currentLine = streamReader.readLine()) != null) {
            if (handlersForCurrentBlock == null) {
                if (blockCount >= handlers.length) {
                    break; // no meta model left
                    // -> any following content can not be processed
                } else {
                    handlersForNextBlock = getHandlers(currentLine, lineNumber); // either the matching block or null
                    if (handlersForNextBlock != null) { // new Block begins
                        handlersForCurrentBlock = handlersForNextBlock; // mark as current
                        blockStartIndex = lineNumber; // and remember the starting line number
                        csvReader = readerFactory.create(handlersForCurrentBlock.getBlockDefinition());
                        callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.BLOCK_START, currentLine, null)); // publish
                        // event
                        blockCount++; // remember the number of processed blocks
                        csvFormat = handlersForCurrentBlock.getBlockDefinition().getCsvFormat();
                    }
                }
            }
            if (handlersForCurrentBlock != null) { // a block meta model is being processed
                if (handlersForCurrentBlock.getBlockDefinition().isEnd(currentLine, lineNumber)) { // line is en of block
                    callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.BLOCK_END, currentLine, null));
                    handlersForCurrentBlock = null;
                    lineNumber++;
                    continue;
                }
                String trimmed = currentLine.trim();
                if (isLineComment(csvFormat, trimmed)) { // process line comment
                    callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.COMMENT, currentLine, null));
                }
                if (isBlankLine(trimmed)) { // blank line
                    if (!csvFormat.isIgnoreBlankLinesEnabled()) { // might be handled
                        callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.BLANK_LINE, currentLine, null));

                    }// or ignored
                    lineNumber++;
                    continue;
                }

                if (lineNumber - blockStartIndex < handlersForCurrentBlock.getBlockDefinition().getHeaderLength()) { // line belongs to header
                    callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.HEADER_NEWLINE, currentLine, null));
                } else { // line belongs to block body
                    if (handlersForCurrentBlock.getBlockDefinition().hasColumnsNames()
                            && lineNumber - blockStartIndex == handlersForCurrentBlock.getBlockDefinition().getHeaderLength()) { // line contains column
                        // names
                        columnNames = csvReader.parse(currentLine);
                        callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.BODY_COLUMN_HEADERS, currentLine,
                                new CsvEntry(columnNames, columnNames, lineNumber)));

                    } else { // line is simple data line
                        callEventHandler(handlersForCurrentBlock, new CsvEvent(CsvEvent.Type.BODY_DATA_NEWLINE, currentLine, new CsvEntry(
                                columnNames, csvReader.parse(currentLine), lineNumber)));
                    }

                }

            }
            lineNumber++;
        }
    }

    private BufferedReader wrapStream(InputStream stream) throws IOException {
        if (encoding.contains("UTF"))
            return new BufferedReader(new InputStreamReader(new UnicodeInputStream(stream, null), encoding));
        else return new BufferedReader(new InputStreamReader(stream, encoding));
    }

    private boolean isLineComment(ICsvFormat format, String trimmed) {
        return trimmed.startsWith(format.getComment() + "");
    }

    private boolean isBlankLine(String trimmed) {
        return trimmed.isEmpty() || trimmed.startsWith("\n") || trimmed.startsWith("\r");
    }

    private void callEventHandler(CsvEventHandlerSet model, CsvEvent event) {
        if (model.definesHandlersForEvent(event.getType())) {
            model.getHandlerForEvent(event.getType())
                    .handle(event);
        }
    }

    private CsvEventHandlerSet getHandlers(String line, long lineIndex) {
        for (CsvEventHandlerSet handler : handlers) {
            if (handler.getBlockDefinition().isBegin(line, lineIndex)) {
                return handler;
            }
        }
        return null; // the line is not the beginning of a new block
    }





}
