package net.engio.csvblockx.tests;

import net.engio.csvblockx.blockdef.CsvBlockDefinition;
import net.engio.csvblockx.data.ICsvBlock;
import net.engio.csvblockx.reader.BlockXreader;
import net.engio.csvblockx.reader.BlockXreaderFactory;
import net.engio.csvblockx.tools.CsvBlockBuilder;
import org.junit.Test;

public class MultiBlockReaderTest extends AbstractCsvUnitTest {

    private BlockXreaderFactory Utf8Reader = BlockXreaderFactory.forUTF8();

    @Test
    public void testStaticSingleBlockReading() {
        try {
            CsvBlockBuilder blockBuilder = new CsvBlockBuilder(new CsvBlockDefinition()
                    .starts().after().line(18)
                    .ends().after().line(30)
                    .hasColumnNames(true));
            BlockXreader isoReader = Utf8Reader.createReaderFor(blockBuilder);
            isoReader.read(getTestResource(Testfiles.Common.PaymentMultiblock));

            // now the in-memory model is initialized
            ICsvBlock block = blockBuilder.getBlock();

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
            CsvBlockBuilder blockBuilder = new CsvBlockBuilder(new CsvBlockDefinition()
                    .starts().with().pattern("Daten.*")
                    .ends().with().emptyLine()
                    .hasColumnNames(true)
                    .headerSize(1));
            BlockXreader isoReader = Utf8Reader.createReaderFor(blockBuilder);
            isoReader.read(getTestResource(Testfiles.Common.PaymentMultiblock));

            // the model is now initialized
            ICsvBlock block = blockBuilder.getBlock();

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

            CsvBlockBuilder block1 = new CsvBlockBuilder(new CsvBlockDefinition()
                    .starts().after().pattern("Parameter.*")
                    .ends().with().emptyLine()
                    .hasColumnNames(true));
            CsvBlockBuilder block2 = new CsvBlockBuilder(new CsvBlockDefinition()
                    .starts().after().pattern("Daten.*")
                    .ends().with().emptyLine()
                    .hasColumnNames(true));
            BlockXreader isoReader = Utf8Reader.createReaderFor(block1, block2);
            isoReader.read(getTestResource(Testfiles.Common.PaymentMultiblock));

            assertTrue(!block1.getBlock().getEntries().isEmpty());
            assertTrue(!block2.getBlock().getEntries().isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
