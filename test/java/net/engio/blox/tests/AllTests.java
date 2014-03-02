package net.engio.blox.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CsvParserTest.class,
	CsvComparatorTest.class, 
	MultiBlockReaderTest.class, 
	MultiBlockWriterTest.class,
	FormatTest.class,
    EncodingTest.class})
public class AllTests {

}
