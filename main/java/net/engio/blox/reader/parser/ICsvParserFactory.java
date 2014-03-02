package net.engio.blox.reader.parser;

import net.engio.blox.blockdef.CsvBlockDescriptor;

/**
 * A factory for instances of {@link ICsvParser}. This factory is used by the {@link net.engio.blox.reader.BloxReader} to create the
 * reader instances for reading a specific csv block
 * 
 * @author Benjamin Diedrichsen
 *
 */

public interface ICsvParserFactory {

	
	/**
	 * Create an instance of {@link ICsvParser} based on the configuration provided by the metamodel
	 * 
	 * @param metamodel
	 * @return
	 */
	public ICsvParser create(CsvBlockDescriptor metamodel);


    // this factory uses the built-in reader
    public static ICsvParserFactory Default = new ICsvParserFactory() {
        @Override
        public ICsvParser create(CsvBlockDescriptor metamodel) {
            return new DefaultCsvParser(metamodel.getCsvFileFormat());
        }
    };

}
