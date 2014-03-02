package net.engio.csvblockx.tests;

import net.engio.csvblockx.blockdef.CsvBlockDefinition;
import net.engio.csvblockx.blockdef.CsvFormat;
import net.engio.csvblockx.blockdef.ICsvFormat;
import net.engio.csvblockx.blockdef.ICsvBlockDefinition;

import org.junit.Test;

public class FormatTest extends AbstractCsvUnitTest {
	
	private static final ICsvBlockDefinition BasicBlockDef = new CsvBlockDefinition()
		.starts().with().pattern("Testblock.*")
		.ends().with().emptyLine()
		.headerSize(1)
		.hasColumnNames(true);
	
	private static final ICsvFormat Format1 = CsvFormat.Default().Delimiter(';');

    @Test
    public void test(){
        // ToDo: implement
    }
}
