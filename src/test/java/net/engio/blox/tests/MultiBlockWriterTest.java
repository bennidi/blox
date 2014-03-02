package net.engio.blox.tests;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.blockdef.CsvFileFormat;

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

	private CsvBlockDescriptor[] getSingleBlockCsvConfiguration() {
		return new CsvBlockDescriptor[]{
                new CsvBlockDescriptor()
                        .starts().with().pattern("Daten.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1)
        };
	}

	// create a complete description of the worldpay csv
	private CsvBlockDescriptor[] getWorldpayCsvConfiguration() {

        return new CsvBlockDescriptor[]{
                new CsvBlockDescriptor()
                        .starts().with().pattern("Parameter.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1),
                new CsvBlockDescriptor()
                        .starts().with().pattern("Zusammenfassung nach.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1),
                new CsvBlockDescriptor()
                        .starts().with().pattern("Non Payment.*")
                        .ends().with().pattern(CsvFileFormat.EmptyLine)
                        .hasColumnNames(true)
                        .headerSize(1),
                new CsvBlockDescriptor()
                        .starts().with().pattern("Daten.*")
                        .ends().with().emptyLine()
                        .hasColumnNames(true)
                        .headerSize(1)

        };
	}

}
