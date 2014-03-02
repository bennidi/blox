package net.engio.blox.writer;

import net.engio.blox.blockdef.ICsvBlockDescriptor;


public interface ICsvWriterFactory {
	
	public ICsvWriter createWriter(ICsvBlockDescriptor configuration);

}
