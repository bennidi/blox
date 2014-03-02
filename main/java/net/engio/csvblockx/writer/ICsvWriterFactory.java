package net.engio.csvblockx.writer;

import net.engio.csvblockx.blockdef.ICsvBlockDefinition;


public interface ICsvWriterFactory {
	
	public ICsvWriter createWriter(ICsvBlockDefinition configuration);

}
