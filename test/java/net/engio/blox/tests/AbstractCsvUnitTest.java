package net.engio.blox.tests;

import junit.framework.Assert;
import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.reader.BlockXreader;
import net.engio.blox.reader.parser.ICsvParserFactory;
import net.engio.blox.tools.CsvBlockBuilder;
import net.engio.blox.tools.CsvComparator;
import net.engio.blox.tools.CsvComparator.Difference;
import net.engio.blox.writer.MultiBlockWriter;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public abstract class AbstractCsvUnitTest {
	
	

	public AbstractCsvUnitTest() {
		super();
	}
	
	public void fail(String message) {
		org.junit.Assert.fail(message);
	}

	public void fail() {
		org.junit.Assert.fail();
	}

	public void assertTrue(Boolean condition) {
		org.junit.Assert.assertTrue(condition);
	}

	public void assertTrue(String message, Boolean condition) {
		org.junit.Assert.assertTrue(message, condition);
	}

	public void assertFalse(Boolean condition) {
		org.junit.Assert.assertFalse(condition);
	}

	public void assertNull(Object object) {
		org.junit.Assert.assertNull(object);
	}

	public void assertNotNull(Object object) {
		Assert.assertNotNull(object);
	}

	public void assertFalse(String message, Boolean condition) {
		org.junit.Assert.assertFalse(message, condition);
	}

	public void assertEquals(Object expected, Object actual) {
		org.junit.Assert.assertEquals(expected, actual);
	}
	
	protected String asOutputFile(String inputFile){
		return inputFile + ".out";
	}

    protected InputStream getTestResource(String resourceUrl){
        return getClass().getClassLoader().getResourceAsStream(resourceUrl);
    }

    protected String getAbsolutePathToResource(String resourceUrl){
        return getClass().getClassLoader().getResource(resourceUrl).getFile();
    }

	protected void doRoundTripAndCompare(String srcFile, String encoding, CsvBlockDescriptor...definitions) {
		try {
			// create input stream and run the reader
			InputStream input = getTestResource(srcFile);
            CsvBlockBuilder[] blockBuilders = CsvBlockBuilder.fromBlockDefinitions(definitions);
            BlockXreader reader = new BlockXreader(ICsvParserFactory.Default, blockBuilders, encoding);
            reader.read(input);

            String outputFileLocation = getAbsolutePathToResource(srcFile) + ".out";
            Writer output = new FileWriter(outputFileLocation);
            MultiBlockWriter blockWriter = MultiBlockWriter.createForDestination(output);
			blockWriter.writeBlocks(CsvBlockBuilder.getBlocks(blockBuilders));
            blockWriter.close();
			
			// now run the comparator on input and output file
			// any structural difference will be detected
			CsvComparator comparator = new CsvComparator()
				.setBlockOrderImportant(true)  
				.setLineOrderImportant(true);
	
			// get the differences (there should be none)
			List<Difference> differences =
				comparator.compare(getTestResource(srcFile), getTestResource(srcFile + ".out"), definitions);
	
			// print any difference for debugging purpose
			for (Difference diff : differences)
				System.out.println(diff);
	
			// if all goes well, no differences should occur
			assertTrue(differences.size() == 0);
	
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	
	}

}
