package net.engio.csvblockx.reader.events;

import net.engio.csvblockx.blockdef.ICsvBlockDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class CsvEventHandlerSet {
	
	
	private Map<CsvEvent.Type, CsvEventHandler> handlers = new HashMap<CsvEvent.Type, CsvEventHandler>();

    private ICsvBlockDefinition blockDefinition;

    public CsvEventHandlerSet(ICsvBlockDefinition blockDefinition) {
        this.blockDefinition = blockDefinition;
    }

    public ICsvBlockDefinition getBlockDefinition() {
        return blockDefinition;
    }

    private CsvEventHandlerSet addEventHandler(CsvEvent.Type type, CsvEventHandler handler) {
		CsvEventHandler existingHandler = this.handlers.get(type);
		if (existingHandler == null) {
			existingHandler = handler;
		} else {
			existingHandler.next(handler);
		}
		handlers.put(type, existingHandler);
		return this;
	}
	
	public CsvEventHandlerSet addEventHandler(CsvEventHandler handler) {
		return addEventHandler(handler.getType(), handler);
	}


	public CsvEventHandler getHandlerForEvent(CsvEvent.Type type) {
		return handlers.get(type);
	}


	public boolean definesHandlersForEvent(CsvEvent.Type type) {
		return handlers.containsKey(type);
	}
	
	public Set<Entry<CsvEvent.Type, CsvEventHandler>> getAllHandlers(){
		return handlers.entrySet();
	}

}
