package net.engio.blox.histogram;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.reader.events.CsvEvent;
import net.engio.blox.reader.events.CsvEventHandler;
import net.engio.blox.reader.events.CsvEventHandlerSet;

public class Histogrammer extends CsvEventHandlerSet {
	
	private Histogram histogram;

	public Histogrammer(Histogram histogram, CsvBlockDescriptor blockDefinition) {
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
