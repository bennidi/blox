package net.engio.blox.tests;

import net.engio.blox.blockdef.CsvFileFormat;
import net.engio.blox.reader.parser.DefaultCsvParser;
import org.junit.Test;


public class CsvParserTest extends AbstractCsvUnitTest{
	
	@Test
	public void testNonEncapsulated(){
		DefaultCsvParser parser = new DefaultCsvParser();
		
		String result[] = parser.parse("ich,bin,ein,berliner");
		assertTrue(result.length == 4);
		assertEquals("ich", result[0]);
		assertEquals("bin", result[1]);
		assertEquals("ein", result[2]);
		assertEquals("berliner", result[3]);
	}
	
	@Test
	public void testEncapsulated(){
		DefaultCsvParser parser = new DefaultCsvParser();
		
		String result[] = parser.parse("ich,bin,\"ein,berliner\",jawoll,");
		assertTrue(result.length == 5);
		assertEquals("ich", result[0]);
		assertEquals("bin", result[1]);
		assertEquals("ein,berliner", result[2]);
		assertEquals("jawoll", result[3]);
		assertEquals(null, result[4]);
	}
	
	@Test
	public void testEmptyValues(){
		DefaultCsvParser parser = new DefaultCsvParser();
		
		String result[] = parser.parse(",ich,bin,\"\",,berliner,,,");
		assertTrue(result.length == 9);
		assertEquals(null, result[0]);
		assertEquals("ich", result[1]);
		assertEquals("bin", result[2]);
		assertEquals("", result[3]);
		assertEquals(null, result[4]);
		assertEquals("berliner", result[5]);
		assertEquals(null, result[6]);
		assertEquals(null, result[7]);
		assertEquals(null, result[8]);
	}
	
	@Test
	public void testComment(){
		DefaultCsvParser parser = new DefaultCsvParser();
		
		String result[] = parser.parse("ich,\\bin,ein#berliner");
		assertTrue(result.length == 3);
		assertEquals("ich", result[0]);
		assertEquals("\\bin", result[1]);
		assertEquals("ein", result[2]);
		
		result = parser.parse("ich,bin,ein,berliner#sdsdsaasd");
		assertTrue(result.length == 4);
		assertEquals("berliner", result[3]);
		
		result = parser.parse("#ich,bin,'ein,berliner#sdsdsaasd'");
		assertTrue(result.length == 0);
		
		
		result = parser.parse("ich,bin,ein,\"berliner#sdsdsaasd\"");
		assertTrue(result.length == 4);
		assertEquals("berliner", result[3]);
	}
	
	@Test
	public void testUnicodeEscape(){
		DefaultCsvParser parser = new DefaultCsvParser(CsvFileFormat.Default().ProcessUnicodeEscapes(true));
		
		String result[] = parser.parse("I,\\u0041m,Here");
		assertTrue(result.length == 3);
		assertEquals("I", result[0]);
		assertEquals("Am", result[1]);
		assertEquals("Here", result[2]);
	}

}
