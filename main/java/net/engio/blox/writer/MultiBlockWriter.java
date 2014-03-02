package net.engio.blox.writer;

import net.engio.blox.blockdef.ICsvBlockDescriptor;
import net.engio.blox.data.CsvBlock;

import java.io.IOException;
import java.io.Writer;
import java.util.List;


/**
 * The multi block csv writer can be used to write an in-memory model of a csv document to a given writer destination.
 * 
 * @author Benjamin Diedrichsen
 *
 */

public class MultiBlockWriter {
	
	public static MultiBlockWriter createForDestination(Writer destination){
		return new MultiBlockWriter(destination);
	}
	
	private Writer destination;
	
	private ICsvWriterFactory writerFactory;
	
	
	public MultiBlockWriter(ICsvWriterFactory factory, Writer destination) {
		super();
		this.destination = destination;
		this.writerFactory = factory;
	}
	
	public MultiBlockWriter(Writer destination) {
		this(new DefaultWriterFactory(), destination);
	}
	
	public void writeBlock(CsvBlock  block) throws IOException{
		String[][] data = block.getBody().getData();
		ICsvWriter csvWriter = writerFactory.createWriter(block.getDefinition()).useDestination(destination);
		for(String header : block.getHeader().getContent()){
			csvWriter.write(header);
		}
		if(block.getBody().hasColumnHeaders()){
			csvWriter.write(block.getBody().getColumnNames());
		}
		csvWriter.write(data);
	}
	
	public void writeBlocks(List<CsvBlock> blocks) throws IOException{
		for(int i = 0 ; i < blocks.size(); i++){
			writeBlock(blocks.get(i));
			if(i + 1 < blocks.size())appendBlockSeparator();
		}
	}
	
	private void appendBlockSeparator(){
		try {
			destination.append("\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			destination.flush();
			destination.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class DefaultWriterFactory implements ICsvWriterFactory{

		@Override
		public ICsvWriter createWriter(ICsvBlockDescriptor configuration) {
			return new CsvWriter(configuration.getCsvFileFormat());
		}

	}

}
