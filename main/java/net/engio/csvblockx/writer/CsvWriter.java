package net.engio.csvblockx.writer;

import net.engio.csvblockx.blockdef.CsvFormat;
import net.engio.csvblockx.blockdef.ICsvFormat;

import java.io.IOException;
import java.io.Writer;


public class CsvWriter implements ICsvWriter{
	
	private ICsvFormat format;
	
	private Writer out;
	
	public CsvWriter(ICsvFormat format){
		this.format = format;
	}
	
	
	public CsvWriter() {
		this(CsvFormat.Default());
	}

	@Override
	public void write(String[][] data) throws IOException{
		for(int i = 0; i < data.length ; i++){
			write(data[i], i + 1 < data.length); //add new line if not last entry in array
		}
	}
	
	public void newLine() throws IOException{
		out.write("\n");
	}
	
	
	public void write(String[] data, boolean appendNewline) throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			if(data[i] == null){
				out.write("");
			}
			else{
				if(needsEncapsulation(data[i]))out.write(encapsulate(data[i]));
				else out.write(data[i]);
			}
			if(i + 1 < data.length)appendSeparator();
		}
		if(appendNewline)newLine();
	}
	
	@Override
	public void write(String[] data) throws IOException{
		write(data, true);
	}
	
	private void appendSeparator() throws IOException {
		out.write(format.getDelimiter());
	}


	public String encapsulate(String value){
		return format.getEncapsulator() + value + format.getEncapsulator();
	}
	
	public boolean needsEncapsulation(String value){
		return value.contains(format.getEncapsulator() + "");
	}

	@Override
	public void write(String line) throws IOException {
		out.write(line);
		newLine();
	}

	@Override
	public ICsvWriter useDestination(Writer out) {
		this.out = out;
		return this;
	}

}
