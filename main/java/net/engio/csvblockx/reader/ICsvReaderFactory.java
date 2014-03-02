package net.engio.csvblockx.reader;

import net.engio.csvblockx.blockdef.ICsvBlockDefinition;

/**
 * A factory for instances of {@link ICsvReader}. This factory is used by the {@link BlockXreader} to create the
 * reader instances for reading a specific csv block
 * 
 * @author Benjamin Diedrichsen
 *
 */

public interface ICsvReaderFactory {

	
	/**
	 * Create an instance of {@link ICsvReader} based on the configuration provided by the metamodel
	 * 
	 * @param metamodel
	 * @return
	 */
	public ICsvReader create(ICsvBlockDefinition metamodel);


    // this factory uses the built-in reader
    public static ICsvReaderFactory Default = new ICsvReaderFactory() {
        @Override
        public ICsvReader create(ICsvBlockDefinition metamodel) {
            return new CsvReader(metamodel.getCsvFormat());
        }
    };

}
