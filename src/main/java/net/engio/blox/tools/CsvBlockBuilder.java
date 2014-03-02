package net.engio.blox.tools;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.data.CsvBlock;
import net.engio.blox.reader.events.CsvEvent;
import net.engio.blox.reader.events.CsvEventHandler;
import net.engio.blox.reader.events.CsvEventHandlerSet;

import java.util.LinkedList;
import java.util.List;

/**
 * A handler implementation that will produce an in-memory model of the parsed csv data
 *
 * @author Benjamin Diedrichsen
 */
public class CsvBlockBuilder extends CsvEventHandlerSet {

    private CsvBlock block;

    public CsvBlockBuilder(CsvBlockDescriptor blockDefinition) {
        super(blockDefinition);
        addEventHandler(new BlockBeginHandler());
        addEventHandler(new HeaderlineHandler());
        addEventHandler(new BodyColumnHeaderHandler());
        addEventHandler(new BodyDataEntryHandler());
    }

    public CsvBlock getBlock() {
        return block;
    }


    public static  CsvBlockBuilder[] fromDescriptors(CsvBlockDescriptor... descriptors){
        CsvBlockBuilder[] builders = new CsvBlockBuilder[descriptors.length];
        for(int i = 0; i< descriptors.length ; i++)
            builders[i] = new CsvBlockBuilder(descriptors[i]);
        return builders;
    }

    public static List<CsvBlock> getBlocks(CsvBlockBuilder ...builders){
        List<CsvBlock> blocks = new LinkedList<CsvBlock>();
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
