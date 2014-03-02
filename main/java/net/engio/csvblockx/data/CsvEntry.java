package net.engio.csvblockx.data;

import java.util.LinkedHashMap;
import java.util.Map;


public class CsvEntry{
		
	private long lineNumber = -1;
	
	private Map<String, String> data = new LinkedHashMap<String, String>();

	public CsvEntry(String[] headers, String[] data, long lineNumber) {
		this();
		this.lineNumber = lineNumber;
		if(headers == null){
			for(int i = 0; i < data.length ; i++){
				addValue(data[i]);
			}
		}
		if(headers != null && headers.length != data.length)
			throw new IllegalArgumentException("Error in line (" + lineNumber + "):" +
                    "Header and data array not symmetric.\nHeader:" + printArray(headers) + "\nData:" + printArray(data));
		for(int i = 0; i < headers.length ; i++){
			this.data.put(headers[i], data[i]);
		}
	}

    private String printArray(String[] array){
        StringBuilder content = new StringBuilder("[" + array.length + "][");
        for(String s : array){
            content.append(s);
            content.append(",");
        }

        content.setCharAt(content.length() -1, ']');
        return content.toString();
    }
	
	public CsvEntry(Map<String, String> namedData){
		super();
		this.data = namedData;
	}
	
	public CsvEntry(String[] data) {
		this();
		for(int i = 0; i < data.length ; i++){
			addValue(data[i]);
		}
	}
	
	public CsvEntry() {
		super();
	}
	
	public long getLineIdx() {
		return lineNumber;
	}
	
	public CsvEntry setLineIdx(long value){
		this.lineNumber = value;
		return this;
	}
	
	
	public CsvEntry replaceValue(String columnName, String newValue){
		data.put(columnName,  newValue);
		return this;
	}
	
	public CsvEntry addValue(String columnName, String value){
		data.put(columnName, value);
		return this;
	}
	
	public CsvEntry addValue(String value){
		data.put("column " + data.size(), value);
		return this;
	}
	
	public String[] getColumnNames(){
		return data.keySet().toArray(new String[]{});
	}
	
	public String getString(String columnName){
		return data.get(columnName);
	}

	public int getNumberOfColumns(){
		return data.keySet().size();
	}
	
	
	public String[] getData(){
		return data.values().toArray(new String[]{});
	}
	
	public CsvEntry removeColumn(String name){
		data.remove(name);
		return this;
	}

}
