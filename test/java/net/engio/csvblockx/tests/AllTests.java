package net.engio.csvblockx.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CsvReaderTest.class,
	CsvComparatorTest.class, 
	MultiBlockReaderTest.class, 
	MultiBlockWriterTest.class,
	FormatTest.class,
    EncodingTest.class})
public class AllTests {

}
