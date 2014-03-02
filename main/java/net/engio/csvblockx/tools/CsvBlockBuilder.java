package net.engio.csvblockx.tools;

import net.engio.csvblockx.blockdef.ICsvBlockDefinition;
import net.engio.csvblockx.data.CsvBlock;
import net.engio.csvblockx.data.ICsvBlock;
import net.engio.csvblockx.reader.events.CsvEvent;
import net.engio.csvblockx.reader.events.CsvEventHandler;
import net.engio.csvblockx.reader.events.CsvEventHandlerSet;

import java.util.LinkedList;
import java.util.List;

/**
 * A handler implementation that will produce an in-memory model of the parsed csv data
 *
 * @author Benjamin Diedrichsen
 */
public class CsvBlockBuilder extends CsvEventHandlerSet {

    private ICsvBlock block;

    public CsvBlockBuilder(ICsvBlockDefinition blockDefinition) {
        super(blockDefinition);
        addEventHandler(new BlockBeginHandler());
        addEventHandler(new HeaderlineHandler());
        addEventHandler(new BodyColumnHeaderHandler());
        addEventHandler(new BodyDataEntryHandler());
    }

    public ICsvBlock getBlock() {
        return block;
    }


    public static  CsvBlockBuilder[] fromBlockDefinitions(ICsvBlockDefinition ...definitions){
        CsvBlockBuilder[] builders = new CsvBlockBuilder[definitions.length];
        for(int i = 0; i< definitions.length ; i++)
            builders[i] = new CsvBlockBuilder(definitions[i]);
        return builders;
    }

    public static List<ICsvBlock> getBlocks(CsvBlockBuilder ...builders){
        List<ICsvBlock> blocks = new LinkedList<ICsvBlock>();
        for(int i = 0; i< builders.length ; i++)
            blocks.add(builders[i].getBlock());
        return blocks;
    }

    private class BlockBeginHandler extends CsvEventHandler {

        public BlockBeginHandler() {
            super(CsvEvent.Type.BLOCK_START);
        }

        public void doHandleEvent(CsvEvent event) {
            block = new CsvBlock(CsvBlockBuilder.this.getBlockDefinition());
        }

    }

    private class HeaderlineHandler extends CsvEventHandler {

        public HeaderlineHandler() {
            super(CsvEvent.Type.HEADER_NEWLINE);
        }

        public void doHandleEvent(CsvEvent event) {
            block.getHeader()
                    .addHeaderLine(event.getSource());
        }

    }

    private class BodyColumnHeaderHandler extends CsvEventHandler {

        public BodyColumnHeaderHandler() {
            super(CsvEvent.Type.BODY_COLUMN_HEADERS);
        }

        public void doHandleEvent(CsvEvent event) {
            block.getBody()
                    .setColumnNames(event.getData());
        }

    }

    private class BodyDataEntryHandler extends CsvEventHandler {

        public BodyDataEntryHandler() {
            super(CsvEvent.Type.BODY_DATA_NEWLINE);
        }

        public void doHandleEvent(CsvEvent event) {
            block.getBody()
                    .addLine(event.getEntry());
        }

    }

}
