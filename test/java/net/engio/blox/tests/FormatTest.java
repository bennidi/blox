package net.engio.blox.tests;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.blockdef.CsvFileFormat;

import org.junit.Test;

public class FormatTest extends AbstractCsvUnitTest {
	
	private static final CsvBlockDescriptor BasicBlockDef = new CsvBlockDescriptor()
		.starts().with().pattern("Testblock.*")
		.ends().with().emptyLine()
		.headerSize(1)
		.hasColumnNames(true);
	
	private static final CsvFileFormat Format1 = CsvFileFormat.Default().Delimiter(';');

    @Test
    public void test(){
        // ToDo: implement
    }
}
