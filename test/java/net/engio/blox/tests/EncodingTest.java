package net.engio.blox.tests;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.reader.BlockXreader;
import net.engio.blox.tools.CsvBlockBuilder;
import net.engio.blox.tools.CsvComparator;
import net.engio.blox.tools.CsvComparator.Difference;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class EncodingTest extends AbstractCsvUnitTest {
	
	private static final CsvBlockDescriptor BasicBlockDef = new CsvBlockDescriptor()
		.starts().with().line(2)
		.ends().with().emptyLine()
		.hasColumnNames(true);
	
	private static final BlockXreader.Factory Iso885915ReaderFactory = new BlockXreader.Factory("iso-8859-15");
	private static final BlockXreader.Factory Utf_8ReaderFactory = new BlockXreader.Factory("UTF-8");

	@Test
	public void testIso_8859_15() throws FileNotFoundException, Exception {

        CsvBlockBuilder isoBlockBuilder = new CsvBlockBuilder(BasicBlockDef);
        BlockXreader isoReader = Iso885915ReaderFactory.createReaderFor(isoBlockBuilder);
        isoReader.read(getTestResource(Testfiles.Encodings.Iso8859_15));

        CsvBlockBuilder utfBlockBuilder = new CsvBlockBuilder(BasicBlockDef);
        BlockXreader utfReader = Utf_8ReaderFactory.createReaderFor(utfBlockBuilder);
        utfReader.read(getTestResource(Testfiles.Encodings.Utf_8));

		CsvComparator comparator = new CsvComparator()
                .setLineOrderImportant(true);
		
		List<Difference> differences = comparator.compare(isoBlockBuilder.getBlock(), utfBlockBuilder.getBlock());
		for(Difference diff : differences)System.out.println(diff);
		assertTrue(differences.isEmpty());

	}
	
	//@Test
    // Todo: Fix files
	public void testWrongEncoding() throws FileNotFoundException, Exception {

        CsvBlockBuilder isoBlockBuilder = new CsvBlockBuilder(BasicBlockDef);
        BlockXreader isoReader = Iso885915ReaderFactory.createReaderFor(isoBlockBuilder);
        isoReader.read(getTestResource(Testfiles.Encodings.Utf_8));

        CsvBlockBuilder utfBlockBuilder = new CsvBlockBuilder(BasicBlockDef);
        BlockXreader utfReader = Utf_8ReaderFactory.createReaderFor(utfBlockBuilder);
        utfReader.read(getTestResource(Testfiles.Encodings.Iso8859_15));

        CsvComparator comparator = new CsvComparator()
                .setLineOrderImportant(true);

        List<Difference> differences = comparator.compare(isoBlockBuilder.getBlock(), utfBlockBuilder.getBlock());
        for(Difference diff : differences)
            System.out.println(diff);
		assertFalse(differences.isEmpty());

	}
}
