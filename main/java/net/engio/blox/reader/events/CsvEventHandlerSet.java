package net.engio.blox.reader.events;

import net.engio.blox.blockdef.CsvBlockDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class CsvEventHandlerSet {
	
	
	private Map<CsvEvent.Type, CsvEventHandler> handlers = new HashMap<CsvEvent.Type, CsvEventHandler>();

    private CsvBlockDescriptor blockDefinition;

    public CsvEventHandlerSet(CsvBlockDescriptor blockDefinition) {
        this.blockDefinition = blockDefinition;
    }

    public CsvBlockDescriptor getBlockDefinition() {
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
