package net.engio.csvblockx.tests;

import net.engio.csvblockx.blockdef.CsvBlockDefinition;
import net.engio.csvblockx.blockdef.CsvFormat;
import net.engio.csvblockx.blockdef.ICsvBlockDefinition;

import org.junit.Test;

public class MultiBlockWriterTest extends AbstractCsvUnitTest {

	@Test
	public void testSingleBlock() {
		doRoundTripAndCompare(Testfiles.Common.PaymentSingleBlock,  "UTF-8", getSingleBlockCsvConfiguration());
	}

	@Test
	public void testWorldPay() {
		doRoundTripAndCompare(Testfiles.Common.PaymentMultiblock,   "UTF-8", getWorldpayCsvConfiguration());
	}

	private ICsvBlockDefinition[] getSingleBlockCsvConfiguration() {
		return new ICsvBlockDefinition[]{
                new CsvBlockDefinition()
                        .starts().with().pattern("Daten.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1)
        };
	}

	// create a complete description of the worldpay csv
	private ICsvBlockDefinition[] getWorldpayCsvConfiguration() {

        return new ICsvBlockDefinition[]{
                new CsvBlockDefinition()
                        .starts().with().pattern("Parameter.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1),
                new CsvBlockDefinition()
                        .starts().with().pattern("Zusammenfassung nach.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1),
                new CsvBlockDefinition()
                        .starts().with().pattern("Non Payment.*")
                        .ends().with().pattern(CsvFormat.EmptyLine)
                        .hasColumnNames(true)
                        .headerSize(1),
                new CsvBlockDefinition()
                        .starts().with().pattern("Daten.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1)

        };
	}

}
