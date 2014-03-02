package net.engio.csvblockx.reader;

import net.engio.csvblockx.reader.events.CsvEventHandlerSet;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 10/28/12
* Time: 4:48 PM
* To change this template use File | Settings | File Templates.
*/
public class BlockXreaderFactory {

    private String charSet;

    private ICsvReaderFactory readerFactory;

    public BlockXreaderFactory(String charSet, ICsvReaderFactory readerFactory) {
        super();
        this.charSet = charSet;
        this.readerFactory = readerFactory;
    }

    public BlockXreaderFactory(String charSet) {
        this(charSet, ICsvReaderFactory.Default);
    }


    public static BlockXreaderFactory forUTF8(){
        return new BlockXreaderFactory("UTF-8");
    }

    public BlockXreader createReaderFor(final CsvEventHandlerSet... handlers) {
        return new BlockXreader(readerFactory, handlers, charSet);
    }


}
