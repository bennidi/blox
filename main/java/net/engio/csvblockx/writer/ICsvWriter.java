package net.engio.csvblockx.writer;

import java.io.IOException;
import java.io.Writer;


public interface ICsvWriter {
	
	public void write(String[][] data) throws IOException;
	 
	public void write(String[] data) throws IOException;
	
	public void write(String line) throws IOException;
	
	public void newLine() throws IOException;

	public abstract ICsvWriter useDestination(Writer out);

}
