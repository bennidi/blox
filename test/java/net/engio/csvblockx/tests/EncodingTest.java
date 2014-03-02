package net.engio.csvblockx.tests;

import net.engio.csvblockx.blockdef.CsvBlockDefinition;
import net.engio.csvblockx.blockdef.ICsvBlockDefinition;
import net.engio.csvblockx.reader.BlockXreader;
import net.engio.csvblockx.reader.BlockXreaderFactory;
import net.engio.csvblockx.tools.CsvBlockBuilder;
import net.engio.csvblockx.tools.CsvComparator;
import net.engio.csvblockx.tools.CsvComparator.Difference;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class EncodingTest extends AbstractCsvUnitTest {
	
	private static final ICsvBlockDefinition BasicBlockDef = new CsvBlockDefinition()
		.starts().with().line(2)
		.ends().with().emptyLine()
		.hasColumnNames(true);
	
	private static final BlockXreaderFactory Iso885915ReaderFactory = new BlockXreaderFactory("iso-8859-15");
	private static final BlockXreaderFactory Utf_8ReaderFactory = new BlockXreaderFactory("UTF-8");

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
	
	@Test
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
