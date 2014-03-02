package net.engio.blox.tests;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.data.CsvBlock;
import net.engio.blox.reader.BloxReader;
import net.engio.blox.tools.CsvBlockBuilder;
import org.junit.Test;

public class MultiBlockReaderTest extends AbstractCsvUnitTest {

    private BloxReader.Factory Utf8Reader = BloxReader.Factory.forUTF8();

    @Test
    public void testStaticSingleBlockReading() {
        try {
            CsvBlockBuilder blockBuilder = new CsvBlockBuilder(new CsvBlockDescriptor()
                    .starts().after().line(18)
                    .ends().after().line(30)
                    .hasColumnNames(true));
            BloxReader isoReader = Utf8Reader.createReaderFor(blockBuilder);
            isoReader.read(getTestResource(Testfiles.Common.PaymentMultiblock));

            // now the in-memory model is initialized
            CsvBlock block = blockBuilder.getBlock();

            // 12 lines minus one line of column headers
            assertTrue("Data length was  " + block.getBody()
                    .getData().length, block.getBody()
                    .getData().length == 11);

            assertTrue(block.getBody()
                    .hasColumnHeaders());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDynamicSingleBlockReading() {
        try {
            CsvBlockBuilder blockBuilder = new CsvBlockBuilder(new CsvBlockDescriptor()
                    .starts().with().pattern("Daten.*")
                    .ends().with().emptyLine()
                    .hasColumnNames(true)
                    .headerSize(1));
            BloxReader isoReader = Utf8Reader.createReaderFor(blockBuilder);
            isoReader.read(getTestResource(Testfiles.Common.PaymentMultiblock));

            // the model is now initialized
            CsvBlock block = blockBuilder.getBlock();

            assertTrue(block.getNumberOfRows() == 57);

            // block has headers
            assertTrue(block.getBody()
                    .hasColumnHeaders());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMultipleBlockReading() {
        try {

            CsvBlockBuilder block1 = new CsvBlockBuilder(new CsvBlockDescriptor()
                    .starts().after().pattern("Parameter.*")
                    .ends().with().emptyLine()
                    .hasColumnNames(true));
            CsvBlockBuilder block2 = new CsvBlockBuilder(new CsvBlockDescriptor()
                    .starts().after().pattern("Daten.*")
                    .ends().with().emptyLine()
                    .hasColumnNames(true));
            BloxReader isoReader = Utf8Reader.createReaderFor(block1, block2);
            isoReader.read(getTestResource(Testfiles.Common.PaymentMultiblock));

            assertTrue(!block1.getBlock().getEntries().isEmpty());
            assertTrue(!block2.getBlock().getEntries().isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
