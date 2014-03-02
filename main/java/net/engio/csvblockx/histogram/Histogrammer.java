package net.engio.csvblockx.histogram;

import net.engio.csvblockx.blockdef.ICsvBlockDefinition;
import net.engio.csvblockx.reader.events.CsvEvent;
import net.engio.csvblockx.reader.events.CsvEventHandler;
import net.engio.csvblockx.reader.events.CsvEventHandlerSet;

public class Histogrammer extends CsvEventHandlerSet {
	
	private Histogram histogram;

	public Histogrammer(Histogram histogram, ICsvBlockDefinition blockDefinition) {
		super(blockDefinition);
        this.histogram = histogram;
        addEventHandler(new HistogrammerHandler());
	}


    private class HistogrammerHandler extends CsvEventHandler{

        public HistogrammerHandler() {
            super(CsvEvent.Type.BODY_DATA_NEWLINE);
        }

        @Override
        public void doHandleEvent(CsvEvent event) {
            histogram.store(event.getEntry());
        }

    }

}
